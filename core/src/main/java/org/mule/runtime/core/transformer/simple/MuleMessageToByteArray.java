/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.runtime.core.transformer.simple;

import static org.mule.runtime.api.metadata.DataTypeFactory.BYTE_ARRAY_DATA_TYPE;
import static org.mule.runtime.api.metadata.DataTypeFactory.MULE_MESSAGE_DATA_TYPE;

import org.mule.runtime.core.api.MuleEvent;
import org.mule.runtime.core.transformer.AbstractMessageTransformer;

/** TODO */
public class MuleMessageToByteArray extends AbstractMessageTransformer
{
    public MuleMessageToByteArray()
    {
        registerSourceType(MULE_MESSAGE_DATA_TYPE);
        setReturnDataType(BYTE_ARRAY_DATA_TYPE);
    }

    @Override
    public Object transformMessage(MuleEvent event, String outputEncoding)
    {
        return muleContext.getObjectSerializer().serialize(event.getMessage());
    }
}
