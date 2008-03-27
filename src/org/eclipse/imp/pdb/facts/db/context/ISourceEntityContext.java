/**
 * 
 */
package org.eclipse.imp.pdb.facts.db.context;

import org.eclipse.imp.model.ISourceEntity;
import org.eclipse.imp.pdb.facts.db.IFactContext;

public interface ISourceEntityContext extends IFactContext {
    ISourceEntity getEntity();
}
