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

package org.eclipse.imp.pdb;

import org.eclipse.imp.pdb.indexing.internal.Indexer;
import org.eclipse.imp.runtime.PluginBase;
import org.osgi.framework.BundleContext;

public class PDBPlugin extends PluginBase {
    public static final String kPluginID= "org.eclipse.imp.pdb";

    // This language name is bogus; it's only used as a qualifying prefix in the preference store.
    public static final String kLanguageName= "pdb";

    /**
     * The unique instance of this plugin class
     */
    protected static PDBPlugin sPlugin;

    public static PDBPlugin getInstance() {
        if (sPlugin == null)
            new PDBPlugin();
        return sPlugin;
    }

    public PDBPlugin() {
        super();
        sPlugin= this;
    }

    public String getID() {
        return kPluginID;
    }

    @Override
    public String getLanguageID() {
        return kLanguageName;
    }

    public void start(BundleContext context) throws Exception {
        super.start(context);
        if (preferencesService == null) {
            getPreferencesService();
        }
        Indexer.initialize(5000);
    }

    @Override
    public void stop(BundleContext context) throws Exception {
        Indexer.shutdown();
        super.stop(context);
    }
}
