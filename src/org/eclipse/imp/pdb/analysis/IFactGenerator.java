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

package org.eclipse.imp.pdb.analysis;

import java.util.Map;

import org.eclipse.core.resources.IResource;
import org.eclipse.imp.pdb.facts.IValue;
import org.eclipse.imp.pdb.facts.db.IFactContext;
import org.eclipse.imp.pdb.facts.type.Type;
import org.eclipse.imp.pdb.indexing.IndexedDocumentDescriptor;

public interface IFactGenerator {
    IValue generate(Type type, IFactContext context, Map<IResource, IndexedDocumentDescriptor> workingCopies) throws AnalysisException;
}
