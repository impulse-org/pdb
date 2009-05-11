package org.eclipse.imp.pdb.analysis;

import org.eclipse.core.resources.IResource;
import org.eclipse.imp.pdb.facts.db.FactBase;
import org.eclipse.imp.pdb.facts.db.IFactContext;
import org.eclipse.imp.pdb.facts.type.Type;

/**
 * This interface should be implemented by clients that wish to keep facts
 * in the PDB up to date as workspace resources change.
 * @author rfuhrer@watson.ibm.com
 */
public interface IFactUpdater {
    /**
     * Update the given FactBase with a fact of the given type and fact context,
     * given a change to the given resource.
     * @throws AnalysisException
     */
    void update(FactBase factBase, Type type, IFactContext context, IResource res) throws AnalysisException;
}
