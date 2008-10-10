package org.eclipse.imp.pdb.facts;

import java.io.IOException;
import java.io.Reader;

import org.eclipse.imp.pdb.facts.type.FactTypeError;
import org.eclipse.imp.pdb.facts.type.Type;

/**
 * An instance of IValueReader can parse a serialized representation
 * of IValues. There should be a corresponding IValueWriter to serialize
 * them again. Note that IValueReaders should also <emph>validate</emph> the 
 * serialized input against a @ref Type.
 * @author jurgenv
 *
 */

public interface IValueReader {  
  /**
	 * Parse an IValue, validate it and build it if it can be validated.
	 * 
	 * @param factory used when building the value
	 * @param type    used to validate the value
	 * @param reader  source of bytes to parse
	 * @return an IValue that represents the string input
	 */
  IValue read(IValueFactory factory, Type type, Reader reader) throws FactTypeError, IOException;
}