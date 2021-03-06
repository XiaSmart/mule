/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.runtime.module.ws.consumer;

import org.apache.cxf.interceptor.Fault;
import org.apache.cxf.interceptor.StaxInInterceptor;
import org.apache.cxf.message.Message;
import org.apache.cxf.phase.AbstractPhaseInterceptor;
import org.apache.cxf.phase.Phase;

/**
 * CXF Interceptor that makes use of a Namespace Restorer XMLStream to allow the first
 * element of the body to carry all the namespaces from the parent nodes.
 * This interceptor works with {@link NamespaceSaverStaxInterceptor}
 */
public class NamespaceRestorerStaxInterceptor extends AbstractPhaseInterceptor<Message>
{

    public NamespaceRestorerStaxInterceptor()
    {
        super(Phase.PRE_INVOKE);
        getAfter().add(StaxInInterceptor.class.getName());
    }

    public void handleMessage(Message message) throws Fault
    {
        NamespaceRestorerXMLStreamReader reader = message.getContent(NamespaceRestorerXMLStreamReader.class);
        reader.restoreNamespaces();
    }
}


