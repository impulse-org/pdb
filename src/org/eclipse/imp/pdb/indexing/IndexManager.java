package org.eclipse.imp.pdb.indexing;

import org.eclipse.imp.pdb.facts.db.IFactKey;
import org.eclipse.imp.pdb.indexing.internal.Indexer;

public class IndexManager {
    
    public static void initializeAndSchedule(final long initialDelay) {
        Indexer.getInstance().initialize(initialDelay);
    }
  
    public static void keepFactUpdated(IFactKey key) {
        Indexer.getInstance().keepFactUpdated(key);
    }    

    public static void cancelFactUpdating(IFactKey key) {
        Indexer.getInstance().cancelFactUpdating(key);
    }
    
    public static boolean isAvailable() {
      return ! Indexer.getInstance().hasWork() && ! Indexer.getInstance().isWorking();
    }
    
}
