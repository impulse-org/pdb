package org.eclipse.imp.pdb.facts.db;

import org.eclipse.imp.pdb.facts.type.Type;

/**
 * A fact key is used to look up values in the fact base
 */
public interface IFactKey {
    Type getType();
    IFactContext getContext();
}