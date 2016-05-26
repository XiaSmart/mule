/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.module.socket.api.socket.tcp;

import java.net.ServerSocket;

/**
 * Interface for objects that provide TCP configuration for server sockets.
 * {@code null} values can be returned by any of the methods, meaning that there is no value defined for the property.
 *
 * @since 4.0
 */
public interface TcpServerSocketProperties extends TcpSocketProperties
{

    /**
     * The maximum queue length for incoming connections.
     */
    int getReceiveBacklog();

    /**
     * Sets the SO_TIMEOUT value when the socket is used as a server.
     * Reading from the socket will block for up to this long (in milliseconds) before the read fails.
     * A value of 0 (the {@link ServerSocket} default) causes the read to wait indefinitely (if no data arrives).
     */
    Integer getServerTimeout();

}
