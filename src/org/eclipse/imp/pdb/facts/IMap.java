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

import org.eclipse.imp.pdb.facts.type.FactTypeError;

public interface IMap extends Iterable<IValue>, IValue {
    public boolean isEmpty();

    public int size();

    public IMapWriter getWriter();
    
    public int arity();

    public IMap put(IValue key, IValue value) throws FactTypeError ;
    
    public IValue get(IValue key);

    public boolean containsKey(IValue key) throws FactTypeError ;
    
    public boolean containsValue(IValue value) throws FactTypeError ;
}
