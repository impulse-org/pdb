package org.eclipse.imp.pdb.facts.impl;

import org.eclipse.imp.pdb.facts.IInteger;
import org.eclipse.imp.pdb.facts.type.NamedType;
import org.eclipse.imp.pdb.facts.type.TypeFactory;

/*package*/ class IntegerValue extends Value implements IInteger {
    private final int fValue;

    /*package*/ IntegerValue(int i) {
        super(TypeFactory.getInstance().integerType());
        fValue= i;
    }

    /*package*/ IntegerValue(NamedType type, int i) {
		super(type);
		fValue = i;
	}

	public int getValue() {
        return fValue;
    }

    @Override
    public String toString() {
        return Integer.toString(fValue);
    }
    
    @Override
    public boolean equals(Object o) {
    	if (o instanceof IntegerValue) {
    		return ((IntegerValue) o).fValue == fValue;
    	}
    	return false;
    }
    
    @Override
    public int hashCode() {
    	return fValue;
    }
}
