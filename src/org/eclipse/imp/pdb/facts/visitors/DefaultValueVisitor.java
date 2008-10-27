/*******************************************************************************
* Copyright (c) 2008 CWI.
* All rights reserved. This program and the accompanying materials
* are made available under the terms of the Eclipse Public License v1.0
* which accompanies this distribution, and is available at
* http://www.eclipse.org/legal/epl-v10.html
*
* Contributors:
*    jurgen@vinju.org
*******************************************************************************/
package org.eclipse.imp.pdb.facts.visitors;

import org.eclipse.imp.pdb.facts.IDouble;
import org.eclipse.imp.pdb.facts.IInteger;
import org.eclipse.imp.pdb.facts.IList;
import org.eclipse.imp.pdb.facts.IMap;
import org.eclipse.imp.pdb.facts.IObject;
import org.eclipse.imp.pdb.facts.IRelation;
import org.eclipse.imp.pdb.facts.ISet;
import org.eclipse.imp.pdb.facts.ISourceLocation;
import org.eclipse.imp.pdb.facts.ISourceRange;
import org.eclipse.imp.pdb.facts.IString;
import org.eclipse.imp.pdb.facts.ITree;
import org.eclipse.imp.pdb.facts.ITuple;

/**
 * This abstract class does nothing except implementing identity. Extend it
 * to easily implement a visitor that visits selected types of IValues.
 * 
 */
public abstract class DefaultValueVisitor implements IValueVisitor {
	public IDouble visitDouble(IDouble o) {
		return o;
	}

	public IInteger visitInteger(IInteger o) {
		return o;
	}

	public IList visitList(IList o) {
		return o;
	}

	public IMap visitMap(IMap o) {
		return o;
	}

	public <T> IObject<T> visitObject(IObject<T> o) {
		return o;
	}

	public IRelation visitRelation(IRelation o) {
		return o;
	}

	public ISet visitSet(ISet o) {
		return o;
	}

	public ISourceLocation visitSourceLocation(ISourceLocation o) {
		return o;
	}

	public ISourceRange visitSourceRange(ISourceRange o) {
		return o;
	}

	public IString visitString(IString o) {
		return o;
	}

	public ITree visitTree(ITree o) {
		return o;
	}

	public ITuple visitTuple(ITuple o) {
		return o;
	}
}
