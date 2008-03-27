/*******************************************************************************
* Copyright (c) 2007 IBM Corporation.
* All rights reserved. This program and the accompanying materials
* are made available under the terms of the Eclipse Public License v1.0
* which accompanies this distribution, and is available at
* http://www.eclipse.org/legal/epl-v10.html
*
* Contributors:
*    Robert Fuhrer (rfuhrer@watson.ibm.com) - initial API and implementation

*******************************************************************************/

/**
 * 
 */
package org.eclipse.imp.pdb.analysis;

import java.util.Properties;
import java.util.Set;

import org.eclipse.imp.pdb.facts.type.Type;

public interface IAnalysisDescriptor {
    /**
     * @return a human-readable name for this analysis engine
     */
    String getName();

    /**
     * @return a set of analysis-specific properties that describe, e.g.,
     * certain aspects of the precision of the analysis, which further
     * qualifies the nature of the results produced. This is not chiefly
     * intended to define, e.g., whether the analysis is context-sensitive,
     * since that would likely be reflected in the output types (see {@link
     * getOutputDescriptors()}), but might indicate that, say, String literals
     * will not be tracked by the analysis.
     */
    Properties getProperties();

    /**
     * @return a set of descriptors identifying the kinds of facts this analysis
     * engine produces
     */
    Set<Type> getOutputDescriptors();
}
