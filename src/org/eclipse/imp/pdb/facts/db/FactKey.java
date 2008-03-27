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

package org.eclipse.imp.pdb.facts.db;

import org.eclipse.imp.pdb.facts.type.Type;


public class FactKey implements IFactKey {
    private final Type fType;
    private final IFactContext fContext;

    public FactKey(Type type, IFactContext cont) {
        fType= type;
        fContext= cont;
    }

    public IFactContext getContext() {
        return fContext;
    }

    public Type getType() {
        return fType;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof FactKey)) {
            return false;
        }
        FactKey other= (FactKey) obj;

        return fType.equals(other.fType) && fContext.equals(other.fContext);
    }

    @Override
    public int hashCode() {
        return 16361 + 353 * fType.hashCode() + 18181 * fContext.hashCode();
    }

    @Override
    public String toString() {
        return fType + "@" + fContext;
    }
}