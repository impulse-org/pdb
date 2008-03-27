package org.eclipse.imp.pdb.facts.db;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.eclipse.imp.pdb.analysis.AnalysisException;
import org.eclipse.imp.pdb.analysis.AnalysisManager;
import org.eclipse.imp.pdb.facts.IRelation;
import org.eclipse.imp.pdb.facts.ISet;
import org.eclipse.imp.pdb.facts.IValue;
import org.eclipse.imp.pdb.facts.db.IFactBaseListener.Reason;
import org.eclipse.imp.pdb.facts.type.Type;

public class FactBase {
    private static final FactBase sInstance = new FactBase();

	public static FactBase getInstance() {
		return sInstance;
	}
	
	private Map<IFactKey, IValue> fFactDatabase= new HashMap<IFactKey, IValue>();

        private IFactKey masterKey = new IFactKey() {
		public IFactContext getContext() {
			throw new UnsupportedOperationException("masterKey");
		}

		public Type getType() {
			throw new UnsupportedOperationException("masterKey");
		}
	};
	
	private FactBase() {
	}
	
	private HashMap<IFactKey, Set<IFactBaseListener>> fListenerMap = new HashMap<IFactKey, Set<IFactBaseListener>>();

    public Collection<IFactKey> getAllKeys() {
        return fFactDatabase.keySet();
    }

    public Collection<IValue> getAllFacts() {
        return fFactDatabase.values();
    }

    public ISet getSet(IFactKey key) throws AnalysisException {
        IValue factValue= fFactDatabase.get(key);

        // TODO Catch FactTypeError and wrap it in an AnalysisException
        if (factValue == null) {
        	factValue = AnalysisManager.getInstance().produceFact(key);
        }
        
        return (ISet) factValue;
    }
    
    public void getSetAsync(IFactKey key, IFactBaseListener l) {
    	throw new UnsupportedOperationException("NQS");
    }

    public IRelation getRelation(IFactKey key) throws AnalysisException {
        IValue factValue= fFactDatabase.get(key);

        // TODO Catch FactTypeError and wrap it in an AnalysisException
        if (factValue == null) {
        	factValue = AnalysisManager.getInstance().produceFact(key);
        }
        
        return (IRelation) factValue;
    }

    public void getRelationAsync(IFactKey key, IFactBaseListener l) {
    	throw new UnsupportedOperationException("NQS");
    }

    public IValue getFact(IFactKey key) throws AnalysisException {
        IValue factValue= fFactDatabase.get(key);

        // TODO Catch FactTypeError and wrap it in an AnalysisException
        if (factValue == null) {
        	factValue = AnalysisManager.getInstance().produceFact(key);
        }
        
        return factValue;
    }
    
    public void getFactAsync(IFactKey key, IFactBaseListener l) {
    	throw new UnsupportedOperationException("NQS");
    }

    public void removeFact(IFactKey key) {
        fFactDatabase.remove(key);
        notifyListener(key, Reason.FACT_REMOVED);
    }

    public void defineFact(IFactKey key, IValue value) {
    	checkNull(value);
    	fFactDatabase.put(key, value);
    	notifyListener(key, Reason.FACT_DEFINED);
    }

	
    public void updateFact(IFactKey key, IValue value) {
    	if (fFactDatabase.get(key) == null) {
    		throw new IllegalArgumentException("Can not update an undefined fact: " + key);
    	}
    	defineFact(key, value);
    	notifyListener(key, Reason.FACT_UPDATED);
    }

    public void addListener(IFactBaseListener listener) {
    	addListener(masterKey, listener);
    }
    
    public void addListener(IFactKey key, IFactBaseListener listener) {
    	Set<IFactBaseListener> set = fListenerMap.get(key);
    	
    	if (set == null) {
    		set = new HashSet<IFactBaseListener>();
    		fListenerMap.put(key, set);
    	}
    	
    	set.add(listener);
    }

    private void notifyListener(IFactKey key, Reason reason) {
    	final Set<IFactBaseListener> listenerSet= fListenerMap.get(key);
        if (listenerSet != null) {
            for(IFactBaseListener l: listenerSet) {
                l.factChanged(key, fFactDatabase.get(key), reason);
            }
        }
    	final Set<IFactBaseListener> universalListeners= fListenerMap.get(masterKey);
        if (universalListeners != null) {
            for(IFactBaseListener l: universalListeners) {
                l.factChanged(key, fFactDatabase.get(key), reason);
            }
        }
    }
    
    private void checkNull(IValue value) {
		if (value == null) {
    		throw new IllegalArgumentException("value should not be null");
    	}
	}

    public void clear() {
        fFactDatabase.clear();
        
        for (Set<IFactBaseListener> listenerSet : fListenerMap.values()) {
            for(IFactBaseListener l: listenerSet) {
                l.factBaseCleared();
            }
        }
    }
}
