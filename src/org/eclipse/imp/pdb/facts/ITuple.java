package org.eclipse.imp.pdb.facts;

public abstract interface ITuple extends Iterable<IValue>, IValue {
    public IValue get(int i);

    public int arity();

    public boolean equals(Object o);
}
