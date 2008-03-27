/**
 * 
 */
package org.eclipse.imp.pdb.facts.db.context;

import org.eclipse.imp.model.ISourceEntity;
import org.eclipse.imp.model.ISourceProject;

public final class ProjectContext implements ISourceEntityContext {
    private ISourceProject fProject;

    public ProjectContext(ISourceProject proj) {
        fProject= proj;
    }

    public ISourceProject getProject() {
        return fProject;
    }

    public ISourceEntity getEntity() {
        return fProject;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof ProjectContext)) {
            return false;
        }
        ProjectContext other= (ProjectContext) obj;

        return fProject.equals(other.fProject);
    }

    @Override
    public int hashCode() {
        return 14741 + 929 * fProject.hashCode();
    }

    @Override
    public String toString() {
        return "<context: " + fProject.getName() + ">";
    }
}
