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

import org.eclipse.imp.pdb.facts.IObject;
import org.eclipse.imp.pdb.facts.type.Type;

public class ObjectValue<T> extends Value implements IObject<T> {
    T fValue;
    
    /*package*/ ObjectValue(Type type, T o) {
    	super(type);
    	fValue = o;
	}
    
	public T getValue() {
		return fValue;
	}
}
