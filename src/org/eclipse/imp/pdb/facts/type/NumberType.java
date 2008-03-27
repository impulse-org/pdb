package org.eclipse.imp.pdb.facts.type;

public class NumberType extends Type {
	private static final NumberType sInstance = new NumberType();

	public static NumberType getInstance() {
		return sInstance;
	}

	private NumberType() {
	}

	@Override
	public boolean isNumberType() {
		return true;
	}
	
	@Override
	public String getTypeDescriptor() {
		return toString();
	}

	@Override
	public boolean isSubtypeOf(Type other) {
		return (other == this || other.isValueType());
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
		return "number";
	}
	
	@Override
	public boolean equals(Object o) {
		return o == this;
	}
	
	@Override
	public int hashCode() {
		return 929;
	}
}
