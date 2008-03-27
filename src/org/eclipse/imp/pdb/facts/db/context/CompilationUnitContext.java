/**
 * 
 */
package org.eclipse.imp.pdb.facts.db.context;

import org.eclipse.imp.model.ICompilationUnit;
import org.eclipse.imp.model.ISourceEntity;

public final class CompilationUnitContext implements ISourceEntityContext {
    private ICompilationUnit fUnit;

    public CompilationUnitContext(ICompilationUnit unit) {
        fUnit= unit;
    }

    public ICompilationUnit getCompilationUnit() {
        return fUnit;
    }

    public ISourceEntity getEntity() {
        return fUnit;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof CompilationUnitContext)) {
            return false;
        }
        CompilationUnitContext other= (CompilationUnitContext) obj;

        return fUnit.equals(other.fUnit);
    }

    @Override
    public int hashCode() {
        return 16361 + 353 * fUnit.hashCode();
    }

    @Override
    public String toString() {
        return "<context: " + fUnit.getName() + ">";
    }
}
