package org.eclipse.imp.pdb.facts;

import org.eclipse.imp.pdb.facts.type.FactTypeError;


public interface ISetWriter {
    ISet getSet();
    void insert(IValue v) throws FactTypeError ;
    void insertAll(ISet other)  throws FactTypeError;
    void insertAll(IRelation relation)  throws FactTypeError;
    void insertAll(IList list) throws FactTypeError;
    void insertAll(Iterable<? extends IValue> collection) throws FactTypeError;
    void done();
   
}
