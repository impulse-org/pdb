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
 * Extend this class to easily create a reusable generic visitor implementation.
 *
 */
public abstract class VisitorAdapter implements IValueVisitor {
	protected IValueVisitor fVisitor;

	public VisitorAdapter(IValueVisitor visitor) {
		this.fVisitor = visitor;
	}

	public IDouble visitDouble(IDouble o) {
		return fVisitor.visitDouble(o);
	}

	public IInteger visitInteger(IInteger o) {
		return fVisitor.visitInteger(o);
	}

	public IList visitList(IList o) {
		return fVisitor.visitList(o);
	}

	public IMap visitMap(IMap o) {
		return fVisitor.visitMap(o);
	}

	public <T> IObject<T> visitObject(IObject<T> o) {
		return fVisitor.visitObject(o);
	}

	public IRelation visitRelation(IRelation o) {
		return fVisitor.visitRelation(o);
	}

	public ISet visitSet(ISet o) {
		return fVisitor.visitSet(o);
	}

	public ISourceLocation visitSourceLocation(ISourceLocation o) {
		return fVisitor.visitSourceLocation(o);
	}

	public ISourceRange visitSourceRange(ISourceRange o) {
		return fVisitor.visitSourceRange(o);
	}

	public IString visitString(IString o) {
		return fVisitor.visitString(o);
	}

	public ITree visitTree(ITree o) {
		return fVisitor.visitTree(o);
	}

	public ITuple visitTuple(ITuple o) {
		return fVisitor.visitTuple(o);
	}
}
