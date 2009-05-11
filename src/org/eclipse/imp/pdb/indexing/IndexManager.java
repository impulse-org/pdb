package org.eclipse.imp.pdb.indexing;

import org.eclipse.imp.pdb.facts.db.IFactKey;
import org.eclipse.imp.pdb.indexing.internal.Indexer;

public class IndexManager {
    public static void keepFactUpdated(IFactKey key) {
        Indexer.getInstance().keepFactUpdated(key);
    }    

    public static void cancelFactUpdating(IFactKey key) {
        Indexer.getInstance().cancelFactUpdating(key);
    }
}
