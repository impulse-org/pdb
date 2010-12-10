package org.eclipse.imp.pdb.indexing;

import org.eclipse.imp.pdb.analysis.IFactGenerator;
import org.eclipse.imp.pdb.facts.db.IFactKey;
import org.eclipse.imp.pdb.indexing.internal.Indexer;

public class IndexManager {
//  public interface IResourceKeyFactory {
//      IFactKey createKeyForResource(IResource r);
//  }
//
//  public interface IResourcePredicate {
//      boolean satisfies(IResource r);
//  }
//
//  public static void createKeyforNewProjects(IResourcePredicate projectFilter, IResourcePredicate projectReady, IResourceKeyFactory factory) {
//      Indexer.getInstance().createKeyForNewProjects(projectFilter, projectReady, factory);
//  }

    /**
     * Keep the fact with the given key up to date with any relevant resource changes.
     * This causes the corresponding {@link IFactGenerator} to be called as needed
     * to compute an updated fact value.
     * @param key 
     */
    public static void keepFactUpdated(IFactKey key) {
        Indexer.getInstance().keepFactUpdated(key);
    }    

    /**
     * Cancel the automatic updating of the fact with the given key.
     * @param key
     */
    public static void cancelFactUpdating(IFactKey key) {
        Indexer.getInstance().cancelFactUpdating(key);
    }

    public static boolean isAvailable() {
        Indexer indexer= Indexer.getInstance();

        return ! indexer.hasWork() && ! indexer.isWorking() && !indexer.isLocked();
    }

    /**
     * Lock the Indexer instance to prevent it temporarily from creating/updating any facts.
     * @deprecated This will be removed in deference to a higher-level interface for
     * scheduling the creation/update of facts that waits until the corresponding resources
     * have been fully created
     */
    public static void lock() {
        Indexer.getInstance().lock();
    }

    /**
     * @deprecated
     */
    public static void unlock() {
        Indexer.getInstance().unlock();
    }
}
