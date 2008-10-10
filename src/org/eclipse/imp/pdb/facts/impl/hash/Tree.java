/*******************************************************************************
* Copyright (c) 2007 IBM Corporation.
* All rights reserved. This program and the accompanying materials
* are made available under the terms of the Eclipse Public License v1.0
* which accompanies this distribution, and is available at
* http://www.eclipse.org/legal/epl-v10.html
*
* Contributors:
*   jurgen@vinju.org
*******************************************************************************/

package org.eclipse.imp.pdb.facts.impl.hash;

import java.util.ArrayList;
import java.util.Iterator;

import org.eclipse.imp.pdb.facts.ITree;
import org.eclipse.imp.pdb.facts.IValue;
import org.eclipse.imp.pdb.facts.IValueFactory;
import org.eclipse.imp.pdb.facts.IValueVisitor;
import org.eclipse.imp.pdb.facts.impl.Value;
import org.eclipse.imp.pdb.facts.type.FactTypeError;
import org.eclipse.imp.pdb.facts.type.NamedType;
import org.eclipse.imp.pdb.facts.type.TreeNodeType;
import org.eclipse.imp.pdb.facts.type.TreeSortType;
import org.eclipse.imp.pdb.facts.type.TupleType;

/**
 * Naive implementation of a typed tree node, using array of children.
 * 
 *
 */
public class Tree extends Value implements ITree {
    protected ArrayList<IValue> fChildren;
    protected IValueFactory fFactory;
    protected TreeNodeType fType;
    
	/*package*/ Tree(IValueFactory factory, NamedType type, IValue[] children) {
		this(factory, (TreeNodeType) type.getBaseType(), children);
		
	}
	
	/*package*/ Tree(IValueFactory factory, TreeNodeType type, IValue[] children) {
		super(type);
		fType = type;
		fFactory = factory;
		fChildren = new ArrayList<IValue>(children.length);
		for (IValue child : children) {
			fChildren.add(child);
		}
	}

	public IValue accept(IValueVisitor v) {
		return v.visitTree(this);
	}

	public int arity() {
		return ((TreeNodeType) fType).getArity();
	}

	public IValue get(int i) {
		try {
		 return fChildren.get(i);
		}
		catch (ArrayIndexOutOfBoundsException e) {
			throw new FactTypeError("Tree node does not have child at pos " + i, e);
		}
	}

	public IValue get(String label) {
		return get(((TreeNodeType) fType).getChildIndex(label));
	}

	public Iterable<IValue> getChildren() {
		return fChildren;
	}

	public TupleType getChildrenTypes() {
		return ((TreeNodeType) fType).getChildrenTypes();
	}

	public String getName() {
		return ((TreeNodeType) fType).getName();
	}

	public TreeSortType getTreeSortType() {
		return ((TreeNodeType) fType).getTreeSortType();
	}

	@SuppressWarnings("unchecked")
	public  ITree set(int i, IValue newChild) {
		Tree tmp = new Tree(fFactory, fType, null);
	    tmp.fChildren = (ArrayList<IValue>) fChildren.clone();
		fChildren.set(i, newChild);
		return tmp;
	}
	
	public ITree set(String label, IValue newChild) {
		return set(((TreeNodeType) fType).getChildIndex(label), newChild);
	}

	public Iterator<IValue> iterator() {
		return fChildren.iterator();
	}
}