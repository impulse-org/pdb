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

/**
 * 
 */
package org.eclipse.imp.pdb.facts.db.context;

import org.eclipse.imp.model.ISourceEntity;
import org.eclipse.imp.model.IWorkspaceModel;
import org.eclipse.imp.model.ModelFactory;

public final class WorkspaceContext implements ISourceEntityContext {
    private static final WorkspaceContext sInstance= new WorkspaceContext();

    public static WorkspaceContext getInstance() {
        return sInstance;
    }

    private IWorkspaceModel  fWorkspace;

    private WorkspaceContext() {
        fWorkspace= ModelFactory.getModelRoot();
    }

    public IWorkspaceModel getWorkspace() {
        return fWorkspace;
    }

    public ISourceEntity getEntity() {
        return fWorkspace;
    }

    @Override
    public boolean equals(Object obj) {
        return (obj instanceof WorkspaceContext);
    }

    @Override
    public int hashCode() {
        return 10301;
    }

    @Override
    public String toString() {
        return "<workspace context>";
    }
}
