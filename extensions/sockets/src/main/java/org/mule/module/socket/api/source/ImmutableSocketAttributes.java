/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.module.socket.api.source;

import static org.apache.commons.lang.StringUtils.EMPTY;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.Socket;

/**
 * Immutable implementation of {@link SocketAttributes}.
 *
 * @since 4.0
 */
public class ImmutableSocketAttributes implements SocketAttributes
{

    private int port;
    private String hostAddress;
    private String hostName;

    /**
     * Creates a new instance
     *
     * @param socket TCP {@link Socket} connection with the remote host
     */
    public ImmutableSocketAttributes(Socket socket)
    {
        fromInetAddress(socket.getPort(), socket.getInetAddress());
    }

    /**
     * Creates a new instance
     *
     * @param socket UDP {@link DatagramSocket} connection with the remote host
     */
    public ImmutableSocketAttributes(DatagramSocket socket)
    {
        fromInetAddress(socket.getPort(), socket.getInetAddress());
    }

    /**
     * Creates a new instance
     *
     * @param packet UDP {@link DatagramPacket} received from remote host
     */
    public ImmutableSocketAttributes(DatagramPacket packet)
    {
        this(packet.getPort(), packet.getAddress().getHostAddress(), packet.getAddress().getHostName());
    }

    private void fromInetAddress(int port, InetAddress address)
    {
        this.port = port;

        if (address == null)
        {
            this.hostName = EMPTY;
            this.hostAddress = EMPTY;
        }
        else
        {
            this.hostName = address.getHostName();
            this.hostAddress = address.getHostAddress();
        }
    }


    public ImmutableSocketAttributes(int remotePort, String remoteHostAddress, String remoteHostName)
    {
        this.port = remotePort;
        this.hostAddress = remoteHostAddress;
        this.hostName = remoteHostName;
    }

    /**
     * {@inheritDoc}
     */
    public int getPort()
    {
        return port;
    }

    /**
     * {@inheritDoc}
     */
    public String getHostAddress()
    {
        return hostAddress;
    }

    /**
     * {@inheritDoc}
     */
    public String getHostName()
    {
        return hostName;
    }
}
