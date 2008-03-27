package org.eclipse.imp.pdb.facts;

import org.eclipse.imp.pdb.facts.type.FactTypeError;


public interface IListWriter {
    IList getList();
    void insert(IValue value) throws FactTypeError;
    void append(IValue value) throws FactTypeError;
    void insertAll(IList other) throws FactTypeError;
    void insertAll(IRelation other) throws FactTypeError;
    void insertAll(ISet set) throws FactTypeError;
    void insertAll(Iterable<? extends IValue> collection) throws FactTypeError;
    void done();

}
