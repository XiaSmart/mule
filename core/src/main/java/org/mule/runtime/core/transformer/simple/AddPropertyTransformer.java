/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.runtime.core.transformer.simple;


import org.mule.runtime.core.api.MuleEvent;
import org.mule.runtime.api.metadata.DataType;

import java.io.Serializable;

public class AddPropertyTransformer extends AbstractAddVariablePropertyTransformer<Serializable>
{

    @Override
    protected void addProperty(MuleEvent event, String propertyName, Serializable value, DataType dataType)
    {
        event.getMessage().setOutboundProperty(propertyName, value, dataType);
    }

    @Override
    protected void removeProperty(MuleEvent event, String propertyName)
    {
        event.getMessage().removeOutboundProperty(propertyName);
    }

}
