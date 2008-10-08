package org.eclipse.imp.pdb.facts;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

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
	 * @param factory the factory to use when building the value
	 * @param type    the type to validate the top node of the value against
	 * @param value   the string to parse and validate
	 * @return an IValue that represents the string input
	 */
  IValue read(IValueFactory factory, Type type, String value) throws FactTypeError;
  
  /**
	 * Parse an IValue, validate it and build it if it can be validated.
	 * 
	 * @param factory the factory to use when building the value
	 * @param type    the type to validate the top node of the value against
	 * @param file   the file to parse and validate
	 * @return an IValue that represents the string input
	 */
  IValue read(IValueFactory factory, Type type, File file) throws FactTypeError, IOException;
  
  /**
	 * Parse an IValue, validate it and build it if it can be validated.
	 * 
	 * @param factory the factory to use when building the value
	 * @param type    the type to validate the top node of the value against
	 * @param stream   the stream to parse and validate
	 * @return an IValue that represents the string input
	 */
  IValue read(IValueFactory factory, Type type, InputStream stream) throws FactTypeError, IOException;
}
