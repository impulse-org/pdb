/*******************************************************************************
* Copyright (c) 2008 CWI.
* All rights reserved. This program and the accompanying materials
* are made available under the terms of the Eclipse Public License v1.0
* which accompanies this distribution, and is available at
* http://www.eclipse.org/legal/epl-v10.html
*
* Contributors:
*    Robert Fuhrer (rfuhrer@watson.ibm.com) - initial API and implementation
*    Stan Sutton (suttons@us.ibm.com) - added copyright notice

*******************************************************************************/

package org.eclipse.imp.pdb.facts;

public interface IValueVisitor {
   public IString visitString(IString o);
   public IDouble visitDouble(IDouble o);
   public IList visitList(IList o);
   public <T> IObject<T> visitObject(IObject<T> o);
   public IRelation visitRelation(IRelation o);
   public ISet visitSet(ISet o);
   public ISourceLocation visitSourceLocation(ISourceLocation o);
   public ISourceRange visitSourceRange(ISourceRange o);
   public ITuple visitTuple(ITuple o);
   public ITree visitTree(ITree o);
   public IInteger visitInteger(IInteger o);
   public IMap visitMap(IMap o);
}
