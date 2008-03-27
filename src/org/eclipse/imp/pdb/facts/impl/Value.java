package org.eclipse.imp.pdb.facts.impl;

import org.eclipse.imp.pdb.facts.IValue;
import org.eclipse.imp.pdb.facts.type.Type;

public abstract class Value implements IValue {
    /**
     * The type of this value
     */
    protected final Type fType;

    protected Value(Type type) {
    	fType= type;
    }

    /**
     * @return the type of this value
     */
    public Type getType() {
    	return fType;
    }
    
    /**
	 * @return the smallest super type of this type that is not a NamedType.
	 */
    public Type getBaseType() {
    	return fType.getBaseType();
    }
}
