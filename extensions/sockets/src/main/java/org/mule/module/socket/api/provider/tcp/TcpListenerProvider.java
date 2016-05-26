/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.module.socket.api.provider.tcp;

import static org.mule.runtime.extension.api.introspection.parameter.ExpressionSupport.NOT_SUPPORTED;
import org.mule.module.socket.api.config.ListenerConfig;
import org.mule.module.socket.api.connection.ConnectionSettings;
import org.mule.module.socket.api.connection.tcp.TcpListenerConnection;
import org.mule.module.socket.api.protocol.SafeProtocol;
import org.mule.module.socket.api.protocol.TcpProtocol;
import org.mule.module.socket.api.socket.tcp.DefaultTcpServerSocketProperties;
import org.mule.module.socket.internal.SocketUtils;
import org.mule.module.socket.internal.socket.factory.SimpleServerSocketFactory;
import org.mule.module.socket.internal.socket.factory.SslServerSocketFactory;
import org.mule.module.socket.internal.socket.factory.TcpServerSocketFactory;
import org.mule.runtime.api.connection.ConnectionException;
import org.mule.runtime.api.connection.ConnectionHandlingStrategy;
import org.mule.runtime.api.connection.ConnectionHandlingStrategyFactory;
import org.mule.runtime.api.connection.ConnectionProvider;
import org.mule.runtime.api.connection.ConnectionValidationResult;
import org.mule.runtime.api.tls.TlsContextFactory;
import org.mule.runtime.core.api.MuleRuntimeException;
import org.mule.runtime.extension.api.annotation.Alias;
import org.mule.runtime.extension.api.annotation.Expression;
import org.mule.runtime.extension.api.annotation.Parameter;
import org.mule.runtime.extension.api.annotation.ParameterGroup;
import org.mule.runtime.extension.api.annotation.param.Optional;

import java.net.ServerSocket;


/**
 * A {@link ConnectionProvider} which provides instances of
 * {@link TcpListenerConnection}
 *
 * @since 4.0
 */
@Alias("tcp-listener")
public final class TcpListenerProvider implements ConnectionProvider<ListenerConfig, TcpListenerConnection>
{

    @Parameter
    @Optional
    @Expression(NOT_SUPPORTED)
    private TlsContextFactory tlsContext;


    /**
     * This configuration parameter refers to the address where the TCP socket should listen for incoming connections.
     */
    @ParameterGroup
    private ConnectionSettings connectionSettings;

    /**
     * {@link ServerSocket} configuration properties
     */
    @ParameterGroup
    private DefaultTcpServerSocketProperties tcpServerSocketProperties;

    /**
     * {@link TcpProtocol} that knows how the data is going to be read and written.
     * If not specified, the {@link SafeProtocol} will be used.
     */
    @Parameter
    @Optional
    private TcpProtocol protocol = new SafeProtocol();

    @Override
    public TcpListenerConnection connect(ListenerConfig listenerConfig) throws ConnectionException
    {
        SimpleServerSocketFactory serverSocketFactory = null;

        try
        {
            serverSocketFactory = tlsContext != null && tlsContext.isTrustStoreConfigured()
                                  ? new SslServerSocketFactory(tlsContext)
                                  : new TcpServerSocketFactory();
        }
        catch (Exception e)
        {
            throw new MuleRuntimeException(e);
        }

        TcpListenerConnection connection = new TcpListenerConnection(connectionSettings, protocol, tcpServerSocketProperties, serverSocketFactory);
        connection.connect();
        return connection;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void disconnect(TcpListenerConnection connection)
    {
        connection.disconnect();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ConnectionValidationResult validate(TcpListenerConnection connection)
    {
        return SocketUtils.validate(connection);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ConnectionHandlingStrategy<TcpListenerConnection> getHandlingStrategy(ConnectionHandlingStrategyFactory<ListenerConfig, TcpListenerConnection> handlingStrategyFactory)
    {
        return handlingStrategyFactory.none();
    }
}
