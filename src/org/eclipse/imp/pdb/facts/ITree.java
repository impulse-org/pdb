package org.eclipse.imp.pdb.facts;

import org.eclipse.imp.pdb.facts.type.TreeSortType;
import org.eclipse.imp.pdb.facts.type.TupleType;

/**
 * Typed trees. Trees are typed according to an algebraic signature (grammar).
 * @see TypeFactory and @see TreeType and @see TreeSortType
 * 
 * @author jurgenv
 *
 */
public interface ITree extends IValue, Iterable<IValue> {
	public IValue get(int i);
	public IValue get(String label);
	public ITree  set(int i, IValue newChild);
	public ITree  set(String label, IValue newChild);
	public int arity();
	public String getName();
	public TreeSortType getTreeSortType();
	public TupleType getChildrenTypes();
	public Iterable<IValue> getChildren();
}
