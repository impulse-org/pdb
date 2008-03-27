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
import org.eclipse.imp.model.ISourceProject;

public final class ProjectContext implements ISourceEntityContext {
    private ISourceProject fProject;

    public ProjectContext(ISourceProject proj) {
        fProject= proj;
    }

    public ISourceProject getProject() {
        return fProject;
    }

    public ISourceEntity getEntity() {
        return fProject;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof ProjectContext)) {
            return false;
        }
        ProjectContext other= (ProjectContext) obj;

        return fProject.equals(other.fProject);
    }

    @Override
    public int hashCode() {
        return 14741 + 929 * fProject.hashCode();
    }

    @Override
    public String toString() {
        return "<context: " + fProject.getName() + ">";
    }
}
