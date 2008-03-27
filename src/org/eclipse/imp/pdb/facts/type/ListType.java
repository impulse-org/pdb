package org.eclipse.imp.pdb.facts.type;

public class ListType extends Type {
	/*package*/ Type fEltType;
	
	/*package*/ ListType(Type eltType) {
		fEltType = eltType;
	}

	public Type getElementType() {
		return fEltType;
	}
	
	@Override
	public boolean isListType() {
		return true;
	}
	
	@Override
	public String getTypeDescriptor() {
		return toString();
	}

	@Override
	public boolean isSubtypeOf(Type other) {
		if (this == other || other.isValueType()) {
			return true;
		}
		else if (other.isListType()) {
			ListType o = (ListType) other;
			return fEltType.isSubtypeOf(o.getElementType());
		}
		return false;
	}

	@Override
	public Type lub(Type other) {
		if (this == other) {
			return this;
		}
		else if (other.isValueType()) {
			return other;
		}
		else if (other.isListType()) {
			ListType o = (ListType) other;
			return TypeFactory.getInstance().listType(fEltType.lub(o.getElementType()));
		}
		else if (other.isNamedType()) {
			return lub(other.getBaseType());
		}
		
		return TypeFactory.getInstance().valueType();
	}
	
	@Override
	public String toString() {
		return "list[" + fEltType + "]";
	}

	@Override
	public boolean equals(Object o) {
		if (o instanceof ListType) {
			ListType other = (ListType) o;
			return fEltType == other.fEltType;
		}
		
		return false;
	}
	
	@Override
	public int hashCode() {
		return 75703 + 104543 * fEltType.hashCode();
	}
}
