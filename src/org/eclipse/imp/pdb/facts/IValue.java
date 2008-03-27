package org.eclipse.imp.pdb.facts;

import org.eclipse.imp.pdb.facts.type.Type;


public interface IValue {
	/** 
	 * @return the Type of a value
	 */
    Type getType();
    
    /**
     * @return the smallest super type of getType() that is not a named type.
     */
    Type getBaseType();
}
