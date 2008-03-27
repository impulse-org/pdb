package org.eclipse.imp.pdb.facts.type;

public class FactTypeError extends RuntimeException {
    private static final long serialVersionUID= 2135696551442574010L;

    public FactTypeError() {
        super("Fact type error");
    }
    
    public FactTypeError(String reason) {
    	super(reason);
    }
}
