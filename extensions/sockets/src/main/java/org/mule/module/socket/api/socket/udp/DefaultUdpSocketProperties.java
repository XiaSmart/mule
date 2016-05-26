/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.module.socket.api.socket.udp;

import org.mule.module.socket.api.socket.AbstractSocketProperties;
import org.mule.runtime.extension.api.annotation.Parameter;
import org.mule.runtime.extension.api.annotation.param.Optional;

import java.net.DatagramSocket;

/**
 * Default immutable implementation of the {@code UdpSocketProperties} interface.
 *
 * @since 4.0
 */
public class DefaultUdpSocketProperties extends AbstractSocketProperties implements UdpSocketProperties
{

    // TODO UDP needs to specify a default value for the receiving buffer size but TCP doesn't
    private static final Integer DEFAULT_UDP_RECEIVE_BUFFER_SIZE = 1024 * 16;
    /**
     * Enable/disable SO_BROADCAST into the {@link DatagramSocket}
     */
    @Parameter
    @Optional(defaultValue = "false")
    protected boolean broadcast = false;

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean getBroadcast()
    {
        return broadcast;
    }

    @Override
    public Integer getReceiveBufferSize()
    {
        return receiveBufferSize == null ? DEFAULT_UDP_RECEIVE_BUFFER_SIZE : receiveBufferSize;
    }

}
