package org.eclipse.imp.pdb.facts.type;

public class SourceLocationType extends LocationType {
    private final static SourceLocationType sInstance= new SourceLocationType();

    /*package*/ static SourceLocationType getInstance() {
        return sInstance;
    }

    private SourceLocationType() { }

    @Override
    public boolean isSourceLocationType() {
    	return true;
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
    public String getTypeDescriptor() {
        return toString();
    }

    /**
     * Should never need to be called; there should be only one instance of IntegerType
     */
    @Override
    public boolean equals(Object obj) {
        return (obj instanceof SourceLocationType);
    }

    @Override
    public int hashCode() {
        return 61547;
    }

    @Override
    public String toString() {
        return "sourceLocation";
    }
}
