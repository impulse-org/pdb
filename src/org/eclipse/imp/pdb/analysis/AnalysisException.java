package org.eclipse.imp.pdb.analysis;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IStatus;

public class AnalysisException extends Exception {
    private static final long serialVersionUID= -7932650437713377880L;

    public AnalysisException(String message) {
        super(message);
    }

    public AnalysisException(String message, Exception cause) {
        super(message, cause);
    }

    @Override
    public String getMessage() {
        StringBuilder sb= new StringBuilder();
        for(Throwable e1= this; e1 != null; ) {
            sb.append((e1 == this) ? super.getMessage() : e1);
            sb.append("\n");

            if (e1 instanceof CoreException) {
                IStatus s= ((CoreException) e1).getStatus();
                Throwable cause= s.getException();
                ByteArrayOutputStream bs= new ByteArrayOutputStream();
                cause.printStackTrace(new PrintStream(bs));
                sb.append(bs.toString());
                e1= s.getException();
            } else {
                e1= e1.getCause();                                
            }
        }
        return sb.toString();
    }
}
