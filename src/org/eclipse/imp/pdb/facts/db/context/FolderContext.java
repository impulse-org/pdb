/**
 * 
 */
package org.eclipse.imp.pdb.facts.db.context;

import org.eclipse.imp.model.ISourceEntity;
import org.eclipse.imp.model.ISourceFolder;

public final class FolderContext implements ISourceEntityContext {
    private ISourceFolder fFolder;

    public FolderContext(ISourceFolder folder) {
        fFolder= folder;
    }

    public ISourceFolder getFolder() {
        return fFolder;
    }

    public ISourceEntity getEntity() {
        return fFolder;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof FolderContext)) {
            return false;
        }
        FolderContext other= (FolderContext) obj;

        return fFolder.equals(other.fFolder);
    }

    @Override
    public int hashCode() {
        return 787 + 17971 * fFolder.hashCode();
    }

    @Override
    public String toString() {
        return "<context: " + fFolder.getName() + ">";
    }
}
