/**
 * 
 */
package org.eclipse.imp.pdb.analysis;

import org.eclipse.imp.pdb.facts.db.FactBase;
import org.eclipse.imp.pdb.facts.db.IFactContext;
import org.eclipse.imp.pdb.facts.type.Type;
import org.eclipse.imp.pdb.facts.type.TypeFactory;

public interface IFactGenerator {
    void generate(FactBase factBase, Type type, IFactContext context) throws AnalysisException;
}
