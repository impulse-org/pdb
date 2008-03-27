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

import org.eclipse.imp.pdb.facts.IValue;

public interface IFactBaseListener {
    enum Reason {
	FACT_DEFINED , FACT_REMOVED , FACT_UPDATED ;
    }
    public void factChanged(IFactKey key, IValue newValue, Reason reason);
    public void factBaseCleared();
}
