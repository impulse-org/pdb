package org.eclipse.imp.pdb.facts.impl;

import org.eclipse.imp.pdb.facts.IDouble;
import org.eclipse.imp.pdb.facts.type.NamedType;
import org.eclipse.imp.pdb.facts.type.TypeFactory;

/*package*/ class DoubleValue extends Value implements IDouble {
    private final double fValue;

    /*package*/ DoubleValue(double value) {
        super(TypeFactory.getInstance().doubleType());
        fValue= value;
    }

    /*package*/ DoubleValue(NamedType type, double d) {
		super(type);
		fValue = d;
	}

	public double getValue() {
        return fValue;
    }

    @Override
    public String toString() {
        return Double.toString(fValue);
    }
    
    @Override
    public boolean equals(Object o) {
    	if (o instanceof DoubleValue) {
    		return ((DoubleValue) o).fValue == fValue;
    	}
    	return false;
    }
    
    @Override
    public int hashCode() {
    	long bits = Double.doubleToLongBits(fValue);
    	return (int)(bits ^ (bits >>> 32));
    }
}
