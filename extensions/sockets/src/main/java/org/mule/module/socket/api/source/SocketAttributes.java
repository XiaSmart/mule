/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */

package org.mule.module.socket.api.source;

import java.io.Serializable;

/**
 * Canonical representation a connection's metadata attributes.
 * <p>
 * It contains information such as a the port from which the sending endpoint
 * is writing information, its host name and address.
 *
 * @since 4.0
 */
public interface SocketAttributes extends Serializable
{

    /**
     *  @return the port number from which the sender is bounded.
     */
    int getPort();

    /**
     * @return the host address of the sender
     */
    String getHostAddress();

    /**
     * @return the host name of the sender
     */
    String getHostName();
}
