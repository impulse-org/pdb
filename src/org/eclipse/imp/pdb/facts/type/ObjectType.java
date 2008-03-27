package org.eclipse.imp.pdb.facts.type;

public class ObjectType<T> extends Type {
    /*package*/ Class fClass;
    
    /*package*/ ObjectType(Class<T> clazz) {
    	fClass = clazz;
	}
    
	@Override
	public String getTypeDescriptor() {
		return toString();
	}

	@Override
	public boolean isSubtypeOf(Type other) {
		if (other == TypeFactory.getInstance().valueType()) {
			return true;
		}
		else {
			return other == this;
		}
	}

	@Override
	public Type lub(Type other) {
		if (other == this) {
			return this;
		}
		else if (other.isNamedType()) {
    		return lub(((NamedType) other).getSuperType());
    	}
		
		return TypeFactory.getInstance().valueType();
	}

	@Override
	public boolean equals(Object o) {
		if (!(o instanceof ObjectType)) {
			return false;
		}
		
		ObjectType other = (ObjectType) o;
		
		return other.fClass.equals(fClass);
	}
	
	@Override
	public int hashCode() {
		return 722222227 + 323232323 * fClass.hashCode();
	}
	
	@Override
	public String toString() {
		return "<class: " + fClass.getCanonicalName() + ">";
	}
	
	public boolean isObjectType() {
		return true;
	}

	public boolean checkClass(Class<T> clazz) {
		return fClass.equals(clazz);
	}
}
