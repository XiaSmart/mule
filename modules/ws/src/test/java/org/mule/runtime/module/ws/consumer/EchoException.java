/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.runtime.module.ws.consumer;


import javax.xml.ws.WebFault;

import org.apache.cxf.frontend.FaultInfoException;

@WebFault
public class EchoException extends FaultInfoException
{
    private EchoFault echoFault;

    public EchoException(String message)
    {
        super(message);
        this.echoFault = new EchoFault();
        this.echoFault.setText(message);
    }

    public EchoFault getFaultInfo()
    {
        return echoFault;
    }
}
