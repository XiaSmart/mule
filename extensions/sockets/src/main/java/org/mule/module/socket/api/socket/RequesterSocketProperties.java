/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.module.socket.api.socket;

import org.mule.module.socket.api.connection.ConnectionSettings;

/**
 * Interface for common configuration required on sockets that will send requests.
 * <p>
 * {@code null} values can be returned by any of the methods, meaning that there is no value defined for the property.
 *
 * @since 4.0
 */
public interface RequesterSocketProperties
{

    /**
     * @return {@link ConnectionSettings} with the address parameters to where the requester socket should bind to.
     */
    ConnectionSettings getLocalAddress();
    ///**
    // * Specifies a certain port to be used by the socket for binding.
    // * If it was not set, the system will pick up an ephemeral port to bind the socket with.
    // */
    //Integer getLocalPort();
    //
    //
    ///**
    // * Specifies a certain host to be used by the socket for binding.
    // * If it was not set, the system will pick up a host name to bind the socket with.
    // */
    //String getBindingHost();
}
