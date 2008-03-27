package org.eclipse.imp.pdb.facts.db;

import org.eclipse.imp.pdb.facts.IValue;

public interface IFactBaseListener {
    enum Reason {
	FACT_DEFINED , FACT_REMOVED , FACT_UPDATED ;
    }
    public void factChanged(IFactKey key, IValue newValue, Reason reason);
    public void factBaseCleared();
}
