package org.eclipse.imp.pdb.facts;

import org.eclipse.imp.pdb.facts.type.FactTypeError;


public interface IRelationWriter {
    IRelation getRelation();
    void insert(ITuple tuple) throws FactTypeError;
    void insertAll(IRelation other) throws FactTypeError;
    void insertAll(ISet set) throws FactTypeError;
    void insertAll(IList list) throws FactTypeError;
    void insertAll(Iterable<? extends ITuple> collection) throws FactTypeError;
    void done();
}
