package org.eclipse.imp.pdb.facts.type;

public class TupleType extends Type {
    protected Type[] fFieldTypes;
    protected int fHashcode= -1;

    /**
     * Creates a tuple type with the given field types. Copies the array.
     */
    /*package*/ TupleType(int len, int start, Type[] fieldTypes) {
        if (fieldTypes != null && len > 0) {
            fFieldTypes= new Type[len];
            System.arraycopy(fieldTypes, start, fFieldTypes, 0, len);
        } else {
            throw new IllegalArgumentException("Null array of field types or non-positive length passed to TupleType ctor!");
        }
    }

    @Override
    public boolean isTupleType() {
    	return true;
    }
    
    public Type getFieldType(int i) {
        return fFieldTypes[i];
    }

    public int getArity() {
        return fFieldTypes.length;
    }
    
    public TupleType product(TupleType other) {
    	return TypeFactory.getInstance().tupleProduct(this, other);
    }
    
    public TupleType compose(TupleType other) {
    	return TypeFactory.getInstance().tupleCompose(this, other);
    }

    @Override
    public boolean isSubtypeOf(Type other) {
        if (other == this || other.isValueType()) {
        	return true;
        }
        else if (other.isTupleType()) {
        	TupleType o = (TupleType) other;
        	if (getArity() == o.getArity()) {
        		for (int i = 0; i < getArity(); i++) {
        			if (!getFieldType(i).isSubtypeOf(o.getFieldType(i))) {
        				return false;
        			}
        		}
        		return true;
        	}
        }
        	
        return false;
    }

    @Override
    public Type lub(Type other) {
    	if (other.isSubtypeOf(this)) {
    		return this;
    	}
    	else if (other.isTupleType()) {
    		TupleType o = (TupleType) other;
    		if (getArity() == o.getArity()) {
    	      return TypeFactory.getInstance().lubTupleTypes(this, o);
    		}
    	}
    	else if (other.isNamedType()) {
    		return lub(((NamedType) other).getSuperType());
    	}
    	
    	return TypeFactory.getInstance().valueType();
    }

    @Override
    public String getTypeDescriptor() {
        return toString();
    }

    @Override
    public int hashCode() {
        if (fHashcode == -1) {
            fHashcode= 55501;
            for(Type elemType: fFieldTypes) {
                fHashcode= fHashcode * 44927 + elemType.hashCode();
            }
        }
        return fHashcode;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof TupleType)) {
            return false;
        }
        
        TupleType other= (TupleType) obj;
        if (fFieldTypes.length != other.fFieldTypes.length) {
            return false;
        }
        
        for(int i=0; i < fFieldTypes.length; i++) {
            // N.B.: The field types must have been created and canonicalized before any
            // attempt to manipulate the outer type (i.e. TupleType), so we can use object
            // identity here for the fFieldTypes.
            if (fFieldTypes[i] != other.fFieldTypes[i]) {
                return false;
            }
        }
        return true;
    }

    @Override
    public String toString() {
        StringBuilder sb= new StringBuilder();
        sb.append("<");
        int idx= 0;
        for(Type elemType: fFieldTypes) {
            if (idx++ > 0)
                sb.append(", ");
            sb.append(elemType.toString());
        }
        sb.append(">");
        return sb.toString();
    }
}
