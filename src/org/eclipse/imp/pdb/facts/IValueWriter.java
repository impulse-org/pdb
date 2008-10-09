package org.eclipse.imp.pdb.facts;

import java.io.IOException;
import java.io.Writer;

/**
 * An instance of IValueWriter can serialize all types of IValues.
 * There should be a corresponding IValueReader to de-serialize them
 * back to IValues.
 *  
 * @author jurgenv
 *
 */
public interface IValueWriter {
	 void write(IValue value, Writer writer) throws IOException;
}
