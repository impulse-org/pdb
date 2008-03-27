/*******************************************************************************
* Copyright (c) 2007 IBM Corporation.
* All rights reserved. This program and the accompanying materials
* are made available under the terms of the Eclipse Public License v1.0
* which accompanies this distribution, and is available at
* http://www.eclipse.org/legal/epl-v10.html
*
* Contributors:
*    Robert Fuhrer (rfuhrer@watson.ibm.com) - initial API and implementation

*******************************************************************************/

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
