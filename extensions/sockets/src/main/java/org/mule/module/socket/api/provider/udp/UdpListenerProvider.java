/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.module.socket.api.provider.udp;

import org.mule.module.socket.api.config.ListenerConfig;
import org.mule.module.socket.api.connection.ConnectionSettings;
import org.mule.module.socket.api.connection.udp.UdpListenerConnection;
import org.mule.module.socket.api.exceptions.UnresolvableHostException;
import org.mule.module.socket.api.socket.udp.DefaultUdpSocketProperties;
import org.mule.module.socket.internal.SocketUtils;
import org.mule.runtime.api.connection.ConnectionException;
import org.mule.runtime.api.connection.ConnectionHandlingStrategy;
import org.mule.runtime.api.connection.ConnectionHandlingStrategyFactory;
import org.mule.runtime.api.connection.ConnectionProvider;
import org.mule.runtime.api.connection.ConnectionValidationResult;
import org.mule.runtime.extension.api.annotation.Alias;
import org.mule.runtime.extension.api.annotation.ParameterGroup;

import java.net.DatagramSocket;


@Alias("udp-listener")
public class UdpListenerProvider implements ConnectionProvider<ListenerConfig, UdpListenerConnection>
{

    /**
     * This configuration parameter refers to the address where the UDP socket should listen for incoming packets.
     */
    @ParameterGroup
    ConnectionSettings connectionSettings;

    /**
     * {@link DatagramSocket} configuration properties
     */
    @ParameterGroup
    DefaultUdpSocketProperties udpSocketProperties;

    @Override
    public UdpListenerConnection connect(ListenerConfig config) throws ConnectionException, UnresolvableHostException
    {
        UdpListenerConnection connection = new UdpListenerConnection(connectionSettings, udpSocketProperties);
        connection.connect();
        return connection;
    }

    @Override
    public void disconnect(UdpListenerConnection connection)
    {
        connection.disconnect();
    }

    @Override
    public ConnectionValidationResult validate(UdpListenerConnection connection)
    {
        return SocketUtils.validate(connection);
    }

    @Override
    public ConnectionHandlingStrategy<UdpListenerConnection> getHandlingStrategy(ConnectionHandlingStrategyFactory<ListenerConfig, UdpListenerConnection> handlingStrategyFactory)
    {
        return handlingStrategyFactory.none();
    }
}
