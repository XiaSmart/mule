/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.module.socket.internal.socket.factory;

import org.mule.runtime.api.tls.TlsContextFactory;
import org.mule.runtime.core.api.security.tls.RestrictedSSLSocketFactory;

import java.io.IOException;
import java.net.Socket;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;

public class SslSocketFactory  implements SimpleSocketFactory
{
    private final RestrictedSSLSocketFactory restrictedSSLSocketFactory;


    public SslSocketFactory(TlsContextFactory tlsContextFactory) throws NoSuchAlgorithmException, KeyManagementException
    {
        restrictedSSLSocketFactory = new RestrictedSSLSocketFactory(tlsContextFactory.createSslContext(),
                                                                    tlsContextFactory.getEnabledCipherSuites(),
                                                                    tlsContextFactory.getEnabledProtocols());
    }

    @Override
    public Socket createSocket() throws IOException
    {
        return restrictedSSLSocketFactory.createSocket();
    }
}
