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

import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.imp.preferences.PreferencesService;
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

    public void start(BundleContext context) throws Exception {
        super.start(context);
    }

    public String getID() {
        return kPluginID;
    }

    protected static PreferencesService preferencesService= null;

    public static PreferencesService getPreferencesService() {
        if (preferencesService == null) {
            preferencesService= new PreferencesService(ResourcesPlugin.getWorkspace().getRoot().getProject());
            preferencesService.setLanguageName(kLanguageName);
            // TODO:  When some actual preferences are created, put
            // a call to the preferences initializer here
            // (The IMP New Preferences Support wizard creates such
            // an initializer.)
        }
        return preferencesService;
    }
}
