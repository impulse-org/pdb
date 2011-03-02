package org.eclipse.imp.pdb.indexing;

import org.eclipse.core.resources.IResource;
import org.eclipse.imp.pdb.facts.db.IFactKey;

/**
 * A factory for resource keys. Used by manageKeysForProjects() to create keys
 * for new projects when the project gets created.
 */
public interface IResourceKeyFactory {
    IFactKey createKeyForResource(IResource r);
}