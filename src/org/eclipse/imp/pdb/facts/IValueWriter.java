package org.eclipse.imp.pdb.facts;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;

/**
 * An instance of IValueWriter can serialize all types of IValues.
 * There should be a corresponding IValueReader to de-serialize them
 * back to IValues.
 *  
 * @author jurgenv
 *
 */
public interface IValueWriter {
	 void write(IValue value, StringBuffer string) throws IOException;
	 void write(IValue value, File file) throws IOException;
	 void write(IValue value, OutputStream stream) throws IOException;
}
