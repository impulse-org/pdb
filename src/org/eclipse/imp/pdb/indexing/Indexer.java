/*******************************************************************************
 * Copyright (c) 2011 IBM Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Robert Fuhrer (rfuhrer@watson.ibm.com) - initial API and implementation
 *******************************************************************************/

package org.eclipse.imp.pdb.indexing;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Stack;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IResourceChangeEvent;
import org.eclipse.core.resources.IResourceChangeListener;
import org.eclipse.core.resources.IResourceDelta;
import org.eclipse.core.resources.IResourceDeltaVisitor;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.MultiStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.IJobManager;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.imp.editor.UniversalEditor;
import org.eclipse.imp.parser.IModelListener;
import org.eclipse.imp.parser.IParseController;
import org.eclipse.imp.pdb.PDBPlugin;
import org.eclipse.imp.pdb.analysis.AnalysisException;
import org.eclipse.imp.pdb.analysis.AnalysisManager;
import org.eclipse.imp.pdb.analysis.IFactGenerator;
import org.eclipse.imp.pdb.analysis.IFactGeneratorFactory;
import org.eclipse.imp.pdb.analysis.IFactUpdater;
import org.eclipse.imp.pdb.facts.IValue;
import org.eclipse.imp.pdb.facts.db.FactBase;
import org.eclipse.imp.pdb.facts.db.IFactContext;
import org.eclipse.imp.pdb.facts.db.IFactKey;
import org.eclipse.imp.pdb.facts.db.context.ISourceEntityContext;
import org.eclipse.imp.pdb.facts.type.Type;
import org.eclipse.jface.text.DocumentEvent;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.IDocumentListener;
import org.eclipse.ui.IEditorPart;

/**
 * Monitors resource and document changes and triggers fact updates for the
 * relevant facts in the {@link FactBase}. Each Indexer instance manages a distinct
 * {@link FactBase} instance.
 * @author rfuhrer
 */
public class Indexer extends Job {
    /**
     * This class is a simple "struct" used to record the {@link IResourcePredicate} and
     * {@link IResourceKeyFactory} associated with a given request to manage key creation
     * for new projects.
     */
    private static class KeyManager {
        public final IResourcePredicate projectFilter;
        public final IResourceKeyFactory keyFactory;

        public KeyManager(IResourcePredicate projectFilter, IResourceKeyFactory keyFactory) {
            this.projectFilter = projectFilter;
            this.keyFactory = keyFactory;
        }
    }

    /**
     * Tracks document changes and creates appropriate work items for the work
     * queue to update any facts relevant to the given document.
     */
    private final class DocumentChangeHandler implements IDocumentListener {
        public void documentAboutToBeChanged(DocumentEvent event) { }

        public void documentChanged(DocumentEvent event) {
            final IDocument doc= event.fDocument;
            final IResource res= fDocumentToResourceMap.get(doc);

            if (res != null) {
                final IProject project= res.getProject();
                final Set<IndexerDescriptor> indexers= fScannerMap.get(project.getFullPath());

                if (indexers != null) {
                    long curTime= System.currentTimeMillis();

                    fDocumentChangeTime.put(doc, curTime);
                }
            }            
        }
    }

    /**
     * The amount of time between scans of fDocumentChangeTime.
     * @see DocumentChangeProcessor
     */
    public static final int DOC_RESCAN_DELAY_MSEC_DEFAULT= 100;

    /**
     * The amount of time that a document change has to exist before attempting
     * to update any facts that depend on it
     */
    public static final int DOC_CHANGE_DELAY_DEFAULT= 1000;

    /**
     * The amount of time between schedulings of the Job that examines the work
     * queue for work to do
     */
    public static final int RESCHEDULE_DELAY_MSEC = 100;

    /**
     * Periodically scans fDocumentChangeTime to see whether any changed documents
     * mentioned there have quiesced long enough to put on the work queue as a WorkItem.
     */
    private class DocumentChangeProcessor extends Job {
        public DocumentChangeProcessor() {
            super("IMP Program Database Document Change Processor");
        }

        @Override
        protected IStatus run(IProgressMonitor monitor) {
            long curTime= System.currentTimeMillis();

            for (IDocument doc: fDocumentChangeTime.keySet()) {
                if (curTime - fDocumentChangeTime.get(doc).longValue() >= fDocChangeDelay) {
                    final IResource res= fDocumentToResourceMap.get(doc);
                    final IProject project= res.getProject();
                    final Set<IndexerDescriptor> indexers= fScannerMap.get(project.getFullPath());

                    for(IndexerDescriptor indexer: indexers) {
                        fWorkQueue.push(new WorkItem(indexer, res, doc));
                    }
                    fDocumentMap.put(res, new IndexedDocumentDescriptor(doc, res, null));
                    fDocumentChangeTime.remove(doc);
                }
            }
            schedule(fDocRescanDelay);
            return Status.OK_STATUS;
        }
        
    }

    /**
     * Tracks AST changes in a given open editor, and passes along the modified
     * AST as an indexer WorkItem. Unlike {@link DocumentChangeProcessor}, this
     * has no need to delay scheduling the {@link WorkItem}, since the editor has
     * already waited for the document to quiesce before issuing the model change
     * event.
     */
    private class IndexModelListener implements IModelListener {
        public AnalysisRequired getAnalysisRequired() {
            return IModelListener.AnalysisRequired.NAME_ANALYSIS;
        }

        public void update(IParseController parseController, IProgressMonitor monitor) {
            final IDocument doc= parseController.getDocument();
            final Object astRoot= parseController.getCurrentAst();
            final IResource res= fDocumentToResourceMap.get(doc);
            final IProject project= res.getProject();
            final Set<IndexerDescriptor> indexers= fScannerMap.get(project.getFullPath());

            fDocumentMap.put(res, new IndexedDocumentDescriptor(doc, res, astRoot));

            for(IndexerDescriptor indexer: indexers) {
                fWorkQueue.push(new WorkItem(indexer, res, doc));
            }
        }
    }

    /**
     * Tracks resource changes and creates appropriate work items for the work
     * queue to update any facts related to the any changed resource.
     */
    private final class ChangedResourceHandler implements IResourceChangeListener {
        public void resourceChanged(IResourceChangeEvent event) {
            final IResourceDelta rootResourceDelta = event.getDelta();

            for (final IResourceDelta resourceDelta : rootResourceDelta.getAffectedChildren()) {
                if (resourceDelta.getResource().getType() == IResource.PROJECT) {
                    final IProject project = resourceDelta.getResource().getProject();

                    switch (resourceDelta.getKind()) {
                        case IResourceDelta.ADDED: {
                            // See whether the project is already ready to be indexed; if not, we'll
                            // track changes and if it becomes ready later on, we'll start indexing
                            // it then.
                            for(KeyManager mgr: fKeyManagers) {
                                checkAddTrackedProject(project, mgr);
                            }
                            break;
                        }
                        case IResourceDelta.CHANGED: {
                            if (fTrackedProjects.containsKey(project)) {
                                // TODO Should we check whether the project is no longer index-worthy?
                                try {
                                    resourceDelta.accept(new IResourceDeltaVisitor() {
                                        public boolean visit(final IResourceDelta delta) throws CoreException {
                                            if (delta.getResource().getType() == IResource.FILE) {
                                                final Set<IndexerDescriptor> indexers= fScannerMap.get(project.getFullPath());
    
                                                if (indexers != null) {
                                                    int changeKind = delta.getKind();

                                                    for(IndexerDescriptor indexer: indexers) {
                                                        fWorkQueue.push(new WorkItem(indexer, delta.getResource(), changeKind));
                                                    }
                                                }
                                            }
                                            return true;
                                        }
                                    });
                                } catch (CoreException except) {
                                    PDBPlugin.getInstance().getLog().log(new MultiStatus(PDBPlugin.kPluginID, IStatus.ERROR,
                                                                                         new IStatus[] { except.getStatus() }, 
                                                                                         "Indexing error while visiting resource delta", 
                                                                                         null /* exception */));
                                }
                            } else {
                                // This project hasn't been accepted by any KeyManagers yet, but perhaps
                                // this change will make it suitable (e.g., a project nature just got
                                // configured on the project). If so, start indexing it.
                                for(KeyManager mgr: fKeyManagers) {
                                    checkAddTrackedProject(project, mgr);
                                }
                            }
                            break;
                        }
                    }
                }
            }
        }
    }

    private class IndexerDescriptor {
        private IFactKey fKey;
        private IFactGenerator fGenerator;

        public IndexerDescriptor(IFactKey key, IFactGenerator gen) {
            fKey= key;
            fGenerator= gen;
        }
        public String toString() {
            return "Indexer for key " + fKey + " via generator " + fGenerator;
        }
    }

    private class WorkItem {
        private final IndexerDescriptor fIndexer;
        private final IResource fResource;
        private final IDocument fDocument;
        private final int fChangeKind;

        public WorkItem(IndexerDescriptor indexer, IResource r, int changeKind) {
            fIndexer= indexer;
            fResource= r;
            fDocument= null;
            fChangeKind= changeKind;
        }

        public WorkItem(IndexerDescriptor indexer, IResource r, IDocument doc) {
            fIndexer= indexer;
            fResource= r;
            fChangeKind= IResourceDelta.CHANGED;
            fDocument = doc;
        }

        @Override
        public String toString() {
            return "Indexer work item for <" + fIndexer + "> for " + (fDocument != null ? "document " : "") +
                   "change to resource " + fResource.getFullPath().toPortableString();
        }
    }

    /**
     * The unique FactBase managed by this indexer instance
     */
    private final FactBase fFactBase = new FactBase();

    private final List<KeyManager> fKeyManagers = new LinkedList<KeyManager>();

    private final Map<IProject,Set<KeyManager>> fTrackedProjects = new HashMap<IProject,Set<KeyManager>>();

    private final Map<IResource, IDocument> fResourceToDocumentMap = new HashMap<IResource, IDocument>();

    private final Map<IDocument, IResource> fDocumentToResourceMap = new HashMap<IDocument, IResource>();

    private final Map<IResource, IndexedDocumentDescriptor> fDocumentMap = new HashMap<IResource, IndexedDocumentDescriptor>();

    private final DocumentChangeHandler fDocChangeHandler = new DocumentChangeHandler();

    private final Map<IPath,Set<IndexerDescriptor>> fScannerMap= new HashMap<IPath, Set<IndexerDescriptor>>();

    private final Map<IDocument, Long> fDocumentChangeTime = new HashMap<IDocument, Long>();

    private final Stack<WorkItem> fWorkQueue= new Stack<WorkItem>();

    private final ChangedResourceHandler fListener= new ChangedResourceHandler();

    private final IModelListener fModelListener = new IndexModelListener();

    private final Object fJobFamily;

    private long fDocRescanDelay = DOC_RESCAN_DELAY_MSEC_DEFAULT;

    private long fDocChangeDelay = DOC_CHANGE_DELAY_DEFAULT;

    private long fQueueScanScheduleDelay = RESCHEDULE_DELAY_MSEC;

    private boolean fInitialized= false;

    private boolean fIsWorking = false;

    /**
     * Creates a new Indexer instance with its own FactBase. Clients will typically
     * subsequently make calls to {@link Indexer#keepFactUpdated(IFactKey)} or
     * {@link Indexer#manageKeysForProjects(IResourcePredicate, IResourceKeyFactory)}
     * to arrange for facts to be created/updated.
     * @param indexerName an arbitrary name used to identify the job instance, e.g., in
     * the Progress view
     */
    public Indexer(String indexerName) {
        this(indexerName, null);
    }

    /**
     * Creates a new Indexer instance with its own FactBase. Clients will typically
     * subsequently make calls to {@link Indexer#keepFactUpdated(IFactKey)} or
     * {@link Indexer#manageKeysForProjects(IResourcePredicate, IResourceKeyFactory)}
     * to arrange for facts to be created/updated.
     * @param indexerName an arbitrary name used to identify the job instance, e.g., in
     * the Progress view
     * @param familyID a client-supplied ID that can be used with {@link IJobManager#join(Object, IProgressMonitor)}
     * to determine when a given Indexer instance is idle
     */
    public Indexer(String indexerName, Object familyID) {
        super(indexerName);
        fJobFamily= familyID;
        setSystem(true);
    }

    /**
     * @return the unique {@link FactBase} associated with this Indexer instance
     */
    public FactBase getFactBase() {
        return fFactBase;
    }

    @Override
    public boolean belongsTo(Object family) {
        return fJobFamily == family;
    }

    /**
     * Initializes this Indexer instance, and will schedule the initial work-item
     * scan for initialDelayMSecs milliseconds from now. Equivalent to calling
     * initialize(initialDelayMSecs, {@link Indexer#DOC_RESCAN_DELAY_MSEC_DEFAULT},
     * {@link Indexer#DOC_CHANGE_DELAY_DEFAULT}, {@link Indexer#RESCHEDULE_DELAY_MSEC})
     * @param initialDelayMSecs delay before initial scheduling of indexer in milliseconds
     */
    public void initialize(long initialDelayMSecs) {
        initialize(initialDelayMSecs, DOC_RESCAN_DELAY_MSEC_DEFAULT, DOC_CHANGE_DELAY_DEFAULT, RESCHEDULE_DELAY_MSEC);
    }

    /**
     * Initializes this Indexer instance, and will schedule the initial work-item
     * scan for initialDelayMSecs milliseconds from now.
     * @param initialDelayMSecs delay before initial scheduling of indexer in milliseconds
     * @param docChangeDelayMSecs delay after a document change before an indexing request is submitted
     * @param docRescanDelayMSecs delay between scans of the "document change" history
     * @param queueScanDelayMSecs delay between scans of the indexer's work queue
     */
    public void initialize(long initialDelayMSecs, long docChangeDelayMSecs, long docRescanDelayMSecs, long queueScanDelayMSecs) {
        start(initialDelayMSecs);
        fDocChangeDelay= docChangeDelayMSecs;
        fDocRescanDelay= docRescanDelayMSecs;
        fQueueScanScheduleDelay= queueScanDelayMSecs;
    }

    /**
     * Stop all indexing activity for this Indexer instance.
     */
    public void shutdown() {
        stop();
    }

    private void start(long initialDelayMSecs) {
        if (!fInitialized) {
            ResourcesPlugin.getWorkspace().addResourceChangeListener(fListener, IResourceChangeEvent.POST_CHANGE);
            schedule(initialDelayMSecs);
            new DocumentChangeProcessor().schedule();
            fInitialized= true;
        }
    }

    private void stop() {
        cancel();
        ResourcesPlugin.getWorkspace().removeResourceChangeListener(fListener);
    }

    /**
     * Arranges for facts to be automatically created for new projects that satisfy
     * the given {@link IResourcePredicate}. The fact to be created for such projects is
     * identified by the key produced by the given {@link IResourceKeyFactory}.
     * @param projectFilter
     * @param keyFactory
     */
    public void manageKeysForProjects(IResourcePredicate projectFilter, IResourceKeyFactory keyFactory) {
        KeyManager mgr = new KeyManager(projectFilter, keyFactory);

        fKeyManagers.add(mgr);
        initialScan(mgr);
    }

    private void initialScan(KeyManager mgr) {
        IProject[] allProjects = ResourcesPlugin.getWorkspace().getRoot().getProjects();

        for(int i= 0; i < allProjects.length; i++) {
            IProject project= allProjects[i];

            if (project.isAccessible()) {
                checkAddTrackedProject(project, mgr);
            }
        }
    }

    private void checkAddTrackedProject(final IProject project, KeyManager mgr) {
        if (mgr.projectFilter.satisfies(project)) {
            IFactKey key= mgr.keyFactory.createKeyForResource(project);

            keepFactUpdated(key);
            addTrackedProject(project, mgr);
        }
    }

    private void addTrackedProject(final IProject project, KeyManager mgr) {
        Set<KeyManager> interestedMgrs= fTrackedProjects.get(project);

        if (interestedMgrs == null) {
            interestedMgrs= new HashSet<KeyManager>();
            fTrackedProjects.put(project, interestedMgrs);
        }
        interestedMgrs.add(mgr);
    }

    /**
     * Registers the given document as corresponding to the given resource. As a result,
     * the indexing framework will listen for document changes and keep any related facts
     * up to date with those changes. Requires that an appropriate IFactUpdater is available
     * for any related facts. This method is typically called when an editor opens a resource
     * for editing.
     */
    public void registerDocument(IDocument doc, IResource res, IEditorPart editor) {
        fResourceToDocumentMap.put(res, doc);
        fDocumentToResourceMap.put(doc, res);
        if (editor instanceof UniversalEditor) {
            // Good, a UniversalEditor, so we can register for AST model changes
            UniversalEditor univEditor= (UniversalEditor) editor;
            univEditor.addModelListener(fModelListener);
        } else {
            // An ordinary editor, so just register for plain document changes
            doc.addDocumentListener(fDocChangeHandler);
        }
    }

    /**
     * Unregisters the given document, so that the indexing framework ceases listening for
     * changes. Typically called when an editor is closed.
     */
    public void unregisterDocument(IDocument doc) {
        IResource res = fDocumentToResourceMap.get(doc);
        if (res != null) {
            fDocumentToResourceMap.remove(doc);
            fResourceToDocumentMap.remove(res);
            fDocumentMap.remove(res);
        }
        doc.removeDocumentListener(fDocChangeHandler);
    }

    /**
     * Keep the fact with the given key up to date with any relevant resource changes.
     * This causes the corresponding {@link IFactGenerator} to be called as needed
     * to compute an updated fact value.
     * @param key 
     */
    public void keepFactUpdated(IFactKey key) {
        Type resultType= key.getType();
        IFactContext context= key.getContext();

        if (!(context instanceof ISourceEntityContext)) {
            throw new IllegalArgumentException("Fact key has non-resource context");
        }

        ISourceEntityContext srcContext= (ISourceEntityContext) context;
        IResource r= srcContext.getEntity().getResource();
        IFactGeneratorFactory genFactory= AnalysisManager.getInstance().findGeneratorFactory(key);

        if (genFactory == null) {
            throw new IllegalArgumentException("No factory registered for fact type: " + resultType);
        }

        IndexerDescriptor indexerDesc= new IndexerDescriptor(key, genFactory.create(resultType));

        if (!FactBase.getInstance().getAllKeys().contains(key)) {
            // Produce the initial fact value if it doesn't yet exist
            fWorkQueue.push(new WorkItem(indexerDesc, r, IResourceDelta.ADDED)); // Is "ADDED" really appropriate here?
        }
        addIndexer(r.getFullPath(), indexerDesc);
    }

    private void addIndexer(IPath path, IndexerDescriptor desc) {
        Set<IndexerDescriptor> indexers= fScannerMap.get(path);

        if (indexers == null) {
            indexers= new HashSet<IndexerDescriptor>();
            fScannerMap.put(path, indexers);
        }
        indexers.add(desc);
    }

    /**
     * Cancel the automatic updating of the fact with the given key.
     * @param key
     */
    public void cancelFactUpdating(IFactKey key) {
//      Type resultType= key.getType();
        IFactContext context= key.getContext();

        if (!(context instanceof ISourceEntityContext)) {
            throw new IllegalArgumentException("Fact key has non-resource context");
        }

        ISourceEntityContext srcContext= (ISourceEntityContext) context;
        IResource r= srcContext.getEntity().getResource();

        removeIndexer(r.getFullPath(), key);
    }
    
    public boolean isAvailable() {
        return ! hasWork() && ! isWorking();
    }

    /**
     * @return true if this Indexer has any resource/document changes left to process
     */
    public boolean hasWork() {
      return ! this.fWorkQueue.isEmpty();
    }

    /**
     * @return true if this Indexer is currently processing the work queue of
     * resource/document changes
     */
    public boolean isWorking() {
      return this.fIsWorking;
    }

    private void removeIndexer(IPath path, IFactKey key) {
        Set<IndexerDescriptor> indexers= fScannerMap.get(path);

        if (indexers == null) {
            return;
        }
        for(Iterator<IndexerDescriptor> iter= indexers.iterator(); iter.hasNext(); ) {
            IndexerDescriptor indexerDesc= iter.next();

            if (indexerDesc.fKey.equals(key)) {
                iter.remove();
            }
        }
    }

    @Override
    protected IStatus run(IProgressMonitor monitor) {
        this.fIsWorking = true;
        while (!fWorkQueue.isEmpty()) {
            WorkItem workItem= fWorkQueue.pop();
            try {
                IndexerDescriptor indexer= workItem.fIndexer;
                IResource res= workItem.fResource;
                IFactKey key= indexer.fKey;
                IFactGenerator generator= indexer.fGenerator;
                Map<IResource, IndexedDocumentDescriptor> workingCopySet= Collections.unmodifiableMap(fDocumentMap);

                // BUG If a generator is registered to produce two fact types, it will
                // get invoked once for each type, even if it computes both in one pass.
                if (workItem.fDocument != null) {
                    if (!(generator instanceof IFactUpdater)) {
                        PDBPlugin.getInstance().writeErrorMsg("Document update received for " + res.getName() + " but the registered fact generator is not an updater.");
                        // No point in passing this on to the generator API; the resource hasn't changed...
                        continue;
                    }
                    IFactUpdater updater= (IFactUpdater) generator;

                    updater.update(fFactBase, key.getType(), key.getContext(), res, workingCopySet);
                } else if (generator instanceof IFactUpdater) {
                    IFactUpdater updater= (IFactUpdater) generator;

                    updater.update(fFactBase, key.getType(), key.getContext(), res, workItem.fChangeKind, workingCopySet);
                } else {
                    IValue value = generator.generate(key.getType(), key.getContext(), workingCopySet);

                    fFactBase.defineFact(key, value);
                }
            } catch (AnalysisException e) {
                e.printStackTrace();
            }
        }
        this.fIsWorking = false;
        // Every work item we had is done; wait a while before rescheduling...
        this.schedule(fQueueScanScheduleDelay);
        return new Status(IStatus.OK, PDBPlugin.kPluginID, "");
    }
}
