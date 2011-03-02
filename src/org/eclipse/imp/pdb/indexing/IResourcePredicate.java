package org.eclipse.imp.pdb.indexing;

import org.eclipse.core.resources.IResource;

/**
 * A simple class encapsulating a predicate that says whether a given resource
 * has a given property. Used by manageKeysForProjects() to identify projects
 * for which a given fact should be created. A typical implementation might
 * return true if the given project has a particular project nature.
 */
public interface IResourcePredicate {
    boolean satisfies(IResource r);
}