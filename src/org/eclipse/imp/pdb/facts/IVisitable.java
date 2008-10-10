/*******************************************************************************
* Copyright (c) 2007 IBM Corporation.
* All rights reserved. This program and the accompanying materials
* are made available under the terms of the Eclipse Public License v1.0
* which accompanies this distribution, and is available at
* http://www.eclipse.org/legal/epl-v10.html
*
* Contributors:
*    jurgen@vinju.org

*******************************************************************************/
package org.eclipse.imp.pdb.facts;

public interface IVisitable {

	/**
     * Execute the @see IValueVisitor on the current node
     * 
     * @param
     */
    IValue accept(IValueVisitor v);
    
    /**
     * Provide a @see Iterable for the children of this node
     */
    Iterable<IValue> getChildren();
}
