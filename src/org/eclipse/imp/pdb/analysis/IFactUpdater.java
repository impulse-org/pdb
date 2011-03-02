package org.eclipse.imp.pdb.analysis;

import java.util.Map;

import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IResourceDelta;
import org.eclipse.imp.pdb.facts.db.FactBase;
import org.eclipse.imp.pdb.facts.db.IFactContext;
import org.eclipse.imp.pdb.facts.type.Type;
import org.eclipse.imp.pdb.indexing.IndexedDocumentDescriptor;
import org.eclipse.jface.text.IDocument;

/**
 * This interface should be implemented by clients that wish to keep facts
 * in the PDB up to date as workspace resources or open {@link IDocument}s change.
 * @author rfuhrer@watson.ibm.com
 */
public interface IFactUpdater {
    /**
     * Update the given FactBase with a fact of the given type and fact context,
     * given a change to the given resource.
     * @throws AnalysisException
     * @param changeKind a valid value as returned by {@link IResourceDelta#getKind()}
     */
    void update(FactBase factBase, Type type, IFactContext context, IResource res, int changeKind,
                Map<IResource, IndexedDocumentDescriptor> workingCopies) throws AnalysisException;

    /**
     * Update the given FactBase with a fact of the given type and fact context,
     * given a change to the document corresponding to the given resource.
     * @throws AnalysisException
     */
    void update(FactBase factBase, Type type, IFactContext context, IResource res,
                Map<IResource, IndexedDocumentDescriptor> workingCopies) throws AnalysisException;
}
