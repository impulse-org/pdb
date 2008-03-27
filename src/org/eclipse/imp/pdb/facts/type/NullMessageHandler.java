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
