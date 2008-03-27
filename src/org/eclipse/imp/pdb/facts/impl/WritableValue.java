package org.eclipse.imp.pdb.facts.impl;

import org.eclipse.imp.pdb.facts.type.Type;

public abstract class WritableValue<WriterT> extends Value {
	/*package*/ MutabilityState fState = MutabilityState.Mutable;

	private WriterT fWriter;

	public WritableValue(Type type) {
		super(type);
	}

	protected abstract WriterT createWriter();
	
	/*package*/ void setImmutable() {
		fState = MutabilityState.Immutable;
		fWriter = null;
	}

	public final WriterT getWriter() {
		if (fState == MutabilityState.Mutable) {
			if (fWriter == null) {
				fWriter = createWriter();
			}
			return fWriter;
		} else {
			throw new IllegalStateException(
					"Can only obtain writer on a mutable value.");
		}
	}
}
