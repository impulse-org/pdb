package org.eclipse.imp.pdb.facts.impl;

import org.eclipse.imp.pdb.facts.IObject;
import org.eclipse.imp.pdb.facts.type.Type;

public class ObjectValue<T> extends Value implements IObject<T> {
    T fValue;
    
    /*package*/ ObjectValue(Type type, T o) {
    	super(type);
    	fValue = o;
	}
    
	public T getValue() {
		return fValue;
	}
}
