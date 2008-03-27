package org.eclipse.imp.pdb.facts;

import org.eclipse.imp.pdb.facts.type.FactTypeError;

public interface IRelation extends Iterable<ITuple>, IValue {
    public boolean isEmpty();

    public int size();

    public IRelationWriter getWriter();
    
    public int arity();

    public IRelation insert(ITuple tuple) throws FactTypeError ;

    public boolean contains(ITuple tuple) throws FactTypeError ;

    public IRelation union(IRelation rel)  throws FactTypeError;

    public IRelation intersect(IRelation rel) throws FactTypeError;

    public IRelation subtract(IRelation rel) throws FactTypeError;

    public IRelation invert(IRelation universe) throws FactTypeError;
    
    public IRelation product(IRelation rel);
    
    public IRelation product(ISet set);

    public IRelation compose(IRelation rel) throws FactTypeError;

    public IRelation closure() throws FactTypeError;

    public IRelation union(ISet set) throws FactTypeError;

    public IRelation intersect(ISet set) throws FactTypeError;

    public IRelation subtract(ISet set) throws FactTypeError;

    public IRelation invert(ISet universe) throws FactTypeError;
    
    public ISet toSet();
    
    public ISet carrier();
    
    public IList topologicalOrderedList() throws FactTypeError;
}
