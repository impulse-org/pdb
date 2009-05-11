package org.eclipse.imp.pdb.indexing;

import org.eclipse.imp.model.ICompilationUnit;
import org.eclipse.imp.pdb.facts.db.FactBase;
import org.eclipse.imp.pdb.facts.db.FactKey;

public interface IIndexContributor {
    public void updateFactFor(ICompilationUnit icu, FactBase factBase, FactKey key);
}
