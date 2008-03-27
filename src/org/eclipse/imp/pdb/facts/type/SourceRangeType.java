package org.eclipse.imp.pdb.facts.type;

public class SourceRangeType extends Type {
    private static final SourceRangeType sInstance= new SourceRangeType();

    public static SourceRangeType getInstance() {
        return sInstance;
    }

    private SourceRangeType() { }

    @Override
    public boolean isSourceRangeType() {
    	return true;
    }
    
    @Override
    public String getTypeDescriptor() {
        return toString();
    }

    @Override
    public boolean isSubtypeOf(Type other) {
        return other == this || other.isValueType();
    }

    @Override
    public Type lub(Type other) {
        if (other.isSubtypeOf(this)) {
            return this;
        }
        return TypeFactory.getInstance().valueType();
    }

    @Override
    public String toString() {
        return "sourceRange";
    }
}
