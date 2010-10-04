package org.eclipse.imp.pdb.indexing.internal;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
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
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.imp.pdb.PDBPlugin;
import org.eclipse.imp.pdb.analysis.AnalysisException;
import org.eclipse.imp.pdb.analysis.AnalysisManager;
import org.eclipse.imp.pdb.analysis.IFactGenerator;
import org.eclipse.imp.pdb.analysis.IFactGeneratorFactory;
import org.eclipse.imp.pdb.analysis.IFactUpdater;
import org.eclipse.imp.pdb.facts.db.FactBase;
import org.eclipse.imp.pdb.facts.db.IFactContext;
import org.eclipse.imp.pdb.facts.db.IFactKey;
import org.eclipse.imp.pdb.facts.db.context.ISourceEntityContext;
import org.eclipse.imp.pdb.facts.type.Type;

public class Indexer extends Job {
  
    private static final int RESCHEDULE_DELAY_MSEC = 10000;
    
    private boolean fIsWorking = false;

    private static final Indexer sInstance = new Indexer();

    public static Indexer getInstance() {
        return sInstance;
    }

    public static void initialize(long initialDelay) {
        getInstance().initializeAndSchedule(initialDelay);
    }

    private final class ChangedResourceHandler implements IResourceChangeListener {
        public void resourceChanged(IResourceChangeEvent event) {
            final IResourceDelta rootResourceDelta = event.getDelta();
            for (final IResourceDelta resourceDelta : rootResourceDelta.getAffectedChildren()) {
                if ((resourceDelta.getResource().getType() == IResource.PROJECT) &&
                    (resourceDelta.getKind() == IResourceDelta.CHANGED)) {
                     try {
                         resourceDelta.accept(new IResourceDeltaVisitor() {
                      
                             public boolean visit(final IResourceDelta delta) throws CoreException {
                                 if (delta.getResource().getType() == IResource.FILE) {
                                     final IProject project = delta.getResource().getProject();
                                     final Set<IndexerDescriptor> indexers= fScannerMap.get(project.getFullPath());

                                     if (indexers != null) {
                                         for(IndexerDescriptor indexer: indexers) {
                                             fWorkQueue.push(new WorkItem(indexer, delta.getResource()));
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
        private IndexerDescriptor fIndexer;
        private IResource fResource;

        public WorkItem(IndexerDescriptor indexer, IResource r) {
            fIndexer= indexer;
            fResource= r;
        }
        @Override
        public String toString() {
            return "Indexer work item for <" + fIndexer + "> on resource " + fResource.getFullPath().toPortableString();
        }
    }

    private final Map<IPath,Set<IndexerDescriptor>> fScannerMap= new HashMap<IPath, Set<IndexerDescriptor>>();

    private final Stack<WorkItem> fWorkQueue= new Stack<WorkItem>();

    private boolean fInitialized= false;

    private Indexer() {
        super("IMP PDB Indexer");
    }

    private void initializeAndSchedule(long initialDelay) {
        if (!fInitialized) {
            ResourcesPlugin.getWorkspace().addResourceChangeListener(new ChangedResourceHandler(),
                                                                     IResourceChangeEvent.POST_CHANGE);
            this.schedule(initialDelay);
            fInitialized= true;
        }
    }

    public void keepFactUpdated(IFactKey key) {
        Type resultType= key.getType();
        IFactContext context= key.getContext();

        if (!(context instanceof ISourceEntityContext)) {
            throw new IllegalArgumentException("Fact key has non-resource context");
        }

        ISourceEntityContext srcContext= (ISourceEntityContext) context;
        IResource r= srcContext.getEntity().getResource();
        IFactGeneratorFactory genFactory= AnalysisManager.getInstance().findGeneratorFactory(key);
        IndexerDescriptor indexerDesc= new IndexerDescriptor(key, genFactory.create(resultType));

        if (genFactory == null) {
            throw new IllegalArgumentException("No factory registered for fact type: " + resultType);
        }
        if (!FactBase.getInstance().getAllKeys().contains(key)) {
            // Produce the initial fact value if it doesn't yet exist
            fWorkQueue.push(new WorkItem(indexerDesc, r));
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

    public void cancelFactUpdating(IFactKey key) {
        Type resultType= key.getType();
        IFactContext context= key.getContext();

        if (!(context instanceof ISourceEntityContext)) {
            throw new IllegalArgumentException("Fact key has non-resource context");
        }

        ISourceEntityContext srcContext= (ISourceEntityContext) context;
        IResource r= srcContext.getEntity().getResource();

        removeIndexer(r.getFullPath(), key);
    }
    
    public boolean hasWork() {
      return ! this.fWorkQueue.isEmpty();
    }
    
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
        final FactBase factBase= FactBase.getInstance();

        this.fIsWorking = true;
        while (!fWorkQueue.isEmpty()) {
            WorkItem item= fWorkQueue.pop();
            try {
                IndexerDescriptor indexer= item.fIndexer;
                IResource res= item.fResource;
                IFactKey key= indexer.fKey;
                IFactGenerator generator= indexer.fGenerator;

                // BUG If a generator is registered to produce two fact types, it will
                // get invoked once for each type, even if it computes both in one pass.
                if (generator instanceof IFactUpdater) {
                    IFactUpdater updater= (IFactUpdater) generator;
                    
                    updater.update(factBase, key.getType(), key.getContext(), res);
                } else {
                    generator.generate(factBase, key.getType(), key.getContext());
                }
            } catch (AnalysisException e) {
                e.printStackTrace();
            }
        }
        this.fIsWorking = false;
        // Every work item we had is done; wait a while before rescheduling...
        this.schedule(RESCHEDULE_DELAY_MSEC);
        return new Status(IStatus.OK, PDBPlugin.kPluginID, "");
    }
    
}
