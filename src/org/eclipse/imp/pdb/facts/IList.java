package org.eclipse.imp.pdb.facts;

import org.eclipse.imp.pdb.facts.type.FactTypeError;
import org.eclipse.imp.pdb.facts.type.Type;

public interface IList extends Iterable<IValue>, IValue {
    public Type getElementType();
    
    public int length();
    public IList reverse();
    public IList append(IValue e) throws FactTypeError;
    public IList insert(IValue e) throws FactTypeError;
    public IValue get(int i);
   
    public IListWriter getWriter();
}
