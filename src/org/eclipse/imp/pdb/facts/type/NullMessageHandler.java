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

package org.eclipse.imp.pdb.facts.type;

import lpg.runtime.IMessageHandler;
import lpg.runtime.ParseErrorCodes;

public class NullMessageHandler implements IMessageHandler {
    public void handleMessage(int errorCode, int[] msgLocation, int[] errorLocation, String filename, String[] errorInfo) {
        System.out.println(ParseErrorCodes.errorMsgText[errorCode]);
        for(int i= 0; i < errorInfo.length; i++) {
            System.out.println(errorInfo[i]);
        }
    }
}
