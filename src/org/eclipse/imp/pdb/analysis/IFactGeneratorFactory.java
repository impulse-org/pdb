/**
 * 
 */
package org.eclipse.imp.pdb.analysis;

import org.eclipse.imp.pdb.facts.type.Type;
import org.eclipse.imp.pdb.facts.type.TypeFactory;


public interface IFactGeneratorFactory {
    String getName();
    IFactGenerator create(Type type);
    void declareTypes(TypeFactory factory);
}
