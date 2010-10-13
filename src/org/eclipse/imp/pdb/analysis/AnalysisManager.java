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

package org.eclipse.imp.pdb.analysis;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExtensionPoint;
import org.eclipse.core.runtime.Platform;
import org.eclipse.imp.core.ErrorHandler;
import org.eclipse.imp.pdb.PDBPlugin;
import org.eclipse.imp.pdb.facts.IValue;
import org.eclipse.imp.pdb.facts.db.FactBase;
import org.eclipse.imp.pdb.facts.db.IFactKey;
import org.eclipse.imp.pdb.facts.type.Type;
import org.eclipse.imp.preferences.PreferenceCache;

public class AnalysisManager {
    public static final String ANALYSIS_FACTORY_EXTENSION= "analyzerFactory";

    final static AnalysisManager sInstance= new AnalysisManager();

    private static final boolean DEBUG_DISCOVERY= true;
    
    public static AnalysisManager getInstance() {
        return sInstance;
    }

    private final Map<IAnalysisDescriptor, IFactGeneratorFactory> fAnalysisFactoryMap= new HashMap<IAnalysisDescriptor, IFactGeneratorFactory>();

    private final Map<Type, IFactGeneratorFactory> fFactTypeMap= new HashMap<Type, IFactGeneratorFactory>();

    private final FactBase fFactBase= FactBase.getInstance();

    /* package */private AnalysisManager() {
        discoverAnalyzers();
    }

    public IFactGeneratorFactory findGeneratorFactory(IFactKey factKey) {
        Type factType= factKey.getType();
        IFactGeneratorFactory factory= fFactTypeMap.get(factType);

        return factory;
    }

    public void registerAnalysisFactory(IAnalysisDescriptor desc, IFactGeneratorFactory factory) {
        fAnalysisFactoryMap.put(desc, factory);
        for(Type factType : desc.getOutputDescriptors()) {
            fFactTypeMap.put(factType, factory);
        }
    }

    private void discoverAnalyzers() {
       fAnalysisFactoryMap.clear();
       fFactTypeMap.clear();
       
        // consult the extension point to find all analyzers registered via
        // plugin meta-data.
        
        try {
            IExtensionPoint extensionPoint= Platform.getExtensionRegistry().getExtensionPoint(PDBPlugin.kPluginID, ANALYSIS_FACTORY_EXTENSION);
            IConfigurationElement[] elements= extensionPoint.getConfigurationElements();

            if (elements != null) {
                AnalysisFactoryElement[] factories = new AnalysisFactoryElement[elements.length];
                
                // There are two phases because analysis factories may declare types that are used
                // by other analysis factories. In the first phase we collect all the factories and
                // ask them to define their types. In the second phase we collect all signatures 
                // of the analyzers (generators) which use the type names declared in Phase 1.
                for(int n= 0; n < elements.length; n++) {
                    IConfigurationElement element= elements[n];
                    factories[n] = new AnalysisFactoryElement(element);
                }
                
                for(int n= 0; n < factories.length; n++) {
                    AnalysisFactoryElement factoryElement= factories[n];
                    
					for(IAnalysisDescriptor desc: factoryElement.getDescriptors()) {
                        final IFactGeneratorFactory factory = factoryElement.getFactory();
                        fAnalysisFactoryMap.put(desc, factory);
                        for(Type factType : desc.getOutputDescriptors()) {
                            fFactTypeMap.put(factType, factory);
                        }
                    }
                    if (PreferenceCache.emitMessages) {
                        PDBPlugin.getInstance().writeInfoMsg("Found analyzer extension " + factoryElement.getName());
                    }
                }
            } else {
                PDBPlugin.getInstance().logException("No analyzers defined", null);
            }
        } catch (Throwable e) {
            ErrorHandler.reportError("IMP Analysis Registry error", e);
        }
    }

    public IValue produceFact(IFactKey factKey) throws AnalysisException {
        if (DEBUG_DISCOVERY) {
            discoverAnalyzers();
        }
        Type factType= factKey.getType();
        IFactGeneratorFactory factory= fFactTypeMap.get(factType);

        if (factory == null) {
            throw new AnalysisException("Unable to find generator factory for fact of type " + factType);
        }

        IFactGenerator analyzer= factory.create(factType);

        try {
            analyzer.generate(fFactBase, factType, factKey.getContext());
        } catch (AnalysisException e) {
            throw e;
        } catch (Exception e) {
            throw new AnalysisException("Exception encountered while producing " + factKey + ": " + e.getMessage(), e);
        }

        IValue result= fFactBase.queryFact(factKey); // Don't call getFact() - that will bring us back here :-(

        return result;
    }

    public void produceFactAsynchronously(IFactKey factKey) throws AnalysisException {
        Type factType= factKey.getType();
        IFactGeneratorFactory factory= fFactTypeMap.get(factType);

        if (factory == null) {
            throw new AnalysisException("Unable to find generator factory for fact of type " + factType);
        }

//      IFactGenerator analyzer= factory.create(factType);
        // TODO submit job to analyzer scheduler to produce this fact
        throw new UnsupportedOperationException("produceFactAsynchronously()");
    }
    
    public Set<IAnalysisDescriptor> getAnalysisDescriptorSet() {
        return fAnalysisFactoryMap.keySet();
    }
}
