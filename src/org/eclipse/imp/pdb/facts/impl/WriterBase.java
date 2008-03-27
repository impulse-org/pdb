package org.eclipse.imp.pdb.facts.impl;

public abstract class WriterBase<WriterT> {
    private final WritableValue<WriterT> fValue;

    public WriterBase(WritableValue<WriterT> value) {
    	fValue= value;
    }

    public final void done() {
    	fValue.setImmutable();
    }

    public final MutabilityState getState() {
    	return fValue.fState;
    }

    public final void checkMutable() {
    	if (fValue.fState == MutabilityState.Immutable) {
    	    throw new IllegalStateException("Value is immutable");
    	}
    }    
}
