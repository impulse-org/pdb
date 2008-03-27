package org.eclipse.imp.pdb.facts;

public interface ISourceRange extends IValue {
    int getStartOffset();
    int getLength();

    int getStartLine();
    int getEndLine();

    int getStartColumn();
    int getEndColumn();
}
