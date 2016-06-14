/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.module.socket.internal.socket.factory;

import org.mule.runtime.api.tls.TlsContextFactory;
import org.mule.runtime.core.api.security.tls.RestrictedSSLServerSocketFactory;

import java.io.IOException;
import java.net.ServerSocket;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;

public class SslServerSocketFactory implements SimpleServerSocketFactory
{

    private final RestrictedSSLServerSocketFactory restrictedSSLServerSocketFactory;

    public SslServerSocketFactory(TlsContextFactory tlsContextFactory) throws NoSuchAlgorithmException, KeyManagementException
    {
        restrictedSSLServerSocketFactory = new RestrictedSSLServerSocketFactory(tlsContextFactory.createSslContext(),
                                                                                tlsContextFactory.getEnabledCipherSuites(),
                                                                                tlsContextFactory.getEnabledProtocols());
    }

    @Override
    public ServerSocket createServerSocket() throws IOException
    {
        return restrictedSSLServerSocketFactory.createServerSocket();
    }
}
