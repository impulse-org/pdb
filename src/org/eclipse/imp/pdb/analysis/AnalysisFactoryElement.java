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

import java.util.HashSet;
import java.util.Properties;
import java.util.Set;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.imp.pdb.PDBPlugin;
import org.eclipse.imp.pdb.facts.type.FactTypeError;
import org.eclipse.imp.pdb.facts.type.Type;
import org.eclipse.imp.pdb.facts.type.TypeFactory;

class AnalysisFactoryElement {
    private final class AnalysisDescriptor implements IAnalysisDescriptor {
        final String fFactoryName= fName;

        private final TypeFactory fTypeFactory= TypeFactory.getInstance();

        final Set<Type> fOutputs= new HashSet<Type>();

        private AnalysisDescriptor(IConfigurationElement outputElems) {
            IConfigurationElement[] outputTypes= outputElems.getChildren();
            boolean hasErrors= false;

            for(int i= 0; i < outputTypes.length; i++) {
                IConfigurationElement outputTypeElem= outputTypes[i];
                String outputTypeStr= outputTypeElem.getAttribute(ANALYZER_TYPE_ATTR);
                try {
                    Type outputType= fTypeFactory.lookup(outputTypeStr);

                    fOutputs.add(outputType);
                } catch(FactTypeError e) {
                    PDBPlugin.getInstance().logException("Invalid type: " + outputTypeStr, e);                    
                    hasErrors= true;
                }
            }
            if (hasErrors) {
                PDBPlugin.getInstance().logException("Errors in analysis descriptor: " + AnalysisFactoryElement.this, null);
            }
        }

        public String getName() {
            return fFactoryName;
        }

        public Set<Type> getOutputDescriptors() {
            return fOutputs;
        }

        public Properties getProperties() {
            throw new UnsupportedOperationException("AnalysisDescriptor.getProperties()");
        }
    }

    /**
     * Extension element attribute ID for the name associated with the analyzer
     * factory.
     */
    public static final String ANALYZER_FACTORY_NAME_ATTR= "name";

    /**
     * Extension element attribute ID for the implementation class associated
     * with the analyzer factory.
     */
    public static final String ANALYZER_FACTORY_CLASS_ATTR= "class";

    /**
     * Extension element ID for the signatures associated with the analyzer
     * factory.
     */
    public static final String ANALYZER_SIGNATURE_DESC_ELEMENT= "signatureDescriptor";

    /**
     * Extension element ID for the outputs associated with the one of the
     * analyzer factory's signatures.
     */
    public static final String ANALYZER_OUTPUTS_ELEMENT= "outputs";

    /**
     * Extension element ID for the type descriptor associated with an input
     * or output of one of the analyzer factory's signatures.
     */
    public static final String ANALYZER_TYPE_DESC_ELEMENT= "typeDescriptor";

    /**
     * Extension element attribute ID for the type descriptor associated
     * with the given analyzer.
     */
    public static final String ANALYZER_TYPE_ATTR= "typeName";

    private final String fName;

    private Set<IAnalysisDescriptor> fDescriptors;

    private final IFactGeneratorFactory fFactory;

    private IConfigurationElement fConfigurationElement;

    public AnalysisFactoryElement(final IConfigurationElement configElement) {
        fConfigurationElement = configElement;
        fName= fConfigurationElement.getAttribute(ANALYZER_FACTORY_NAME_ATTR);

        try {
            fFactory= (IFactGeneratorFactory) fConfigurationElement.createExecutableExtension(ANALYZER_FACTORY_CLASS_ATTR);
         
        } catch (CoreException e) {
            PDBPlugin.getInstance().logException("Unable to instantiate analyzer factory: " + fName, e);
            throw new IllegalArgumentException("Invalid analyzer extension class or analyzer factory creation failed", e);
        }
    }

    private void initFromConfigurationElement() {
        IConfigurationElement[] signatures= fConfigurationElement.getChildren(ANALYZER_SIGNATURE_DESC_ELEMENT);

        fDescriptors= new HashSet<IAnalysisDescriptor>();
        for(int i= 0; i < signatures.length; i++) {
            IConfigurationElement signature= signatures[i];
            IConfigurationElement outputTypes= signature.getChildren(ANALYZER_OUTPUTS_ELEMENT)[0];
            IAnalysisDescriptor analysisDesc= new AnalysisDescriptor(outputTypes);

            fDescriptors.add(analysisDesc);
        }
    }

    public String getName() {
        return fName;
    }

    public Set<IAnalysisDescriptor> getDescriptors() {
        if (fDescriptors == null) {
            initFromConfigurationElement();
        }
        return fDescriptors;
    }

    public IFactGeneratorFactory getFactory() {
        return fFactory;
    }
}