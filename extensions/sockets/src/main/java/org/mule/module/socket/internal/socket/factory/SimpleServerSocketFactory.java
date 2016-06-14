/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.module.socket.internal.socket.factory;

import java.io.IOException;
import java.net.ServerSocket;

public interface SimpleServerSocketFactory
{

    ServerSocket createServerSocket() throws IOException;


}
