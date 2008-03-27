package org.eclipse.imp.pdb.facts;

public interface ISourceLocation extends IValue {
    String getPath();
    ISourceRange getRange();
}
