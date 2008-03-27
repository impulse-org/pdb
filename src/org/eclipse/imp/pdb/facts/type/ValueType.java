package org.eclipse.imp.pdb.facts.type;

public class ValueType extends Type {
    private static final ValueType sInstance= new ValueType();

    public static ValueType getInstance() {
        return sInstance;
    }

    private ValueType() { }

    @Override
    public boolean isValueType() {
    	return true;
    }
    
    @Override
    public String getTypeDescriptor() {
        return toString();
    }

    @Override
    public boolean isSubtypeOf(Type other) {
        return other == this;
    }

    @Override
    public Type lub(Type other) {
        return this;
    }

    @Override
    public String toString() {
        return "value";
    }
    
    /**
     * Should never be called, ValueType is a singleton 
     */
    @Override
    public boolean equals(Object o) {
        return (o instanceof ValueType);
    }
    
    @Override
    public int hashCode() {
    	return 2141;
    }
}
