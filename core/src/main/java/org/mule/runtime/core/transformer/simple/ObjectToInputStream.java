/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.runtime.core.transformer.simple;

import static org.mule.runtime.api.metadata.DataTypeFactory.INPUT_STREAM_DATA_TYPE;
import static org.mule.runtime.api.metadata.DataTypeFactory.STRING_DATA_TYPE;
import static org.mule.runtime.api.metadata.DataTypeFactory.dataTypeBuilder;

import org.mule.runtime.core.RequestContext;
import org.mule.runtime.core.api.transformer.TransformerException;
import org.mule.runtime.core.message.OutputHandler;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

/**
 * <code>ObjectToInputStream</code> converts Serializable objects to an InputStream
 * but treats <code>java.lang.String</code>, <code>byte[]</code> and
 * <code>org.mule.runtime.core.message.OutputHandler</code> differently by using their
 * byte[] content rather thqn Serializing them.
 */
public class ObjectToInputStream extends SerializableToByteArray
{

    public ObjectToInputStream()
    {
        this.registerSourceType(STRING_DATA_TYPE);
        this.registerSourceType(dataTypeBuilder(OutputHandler.class).build());
        setReturnDataType(INPUT_STREAM_DATA_TYPE);
    }

    @Override
    public Object doTransform(Object src, String encoding) throws TransformerException
    {
        try
        {
            if (src instanceof String)
            {
                return new ByteArrayInputStream(((String) src).getBytes(encoding));
            }
            else if (src instanceof byte[])
            {
                return new ByteArrayInputStream((byte[]) src);
            }
            else if (src instanceof OutputHandler)
            {
                OutputHandler oh = (OutputHandler) src;

                ByteArrayOutputStream out = new ByteArrayOutputStream();
                oh.write(RequestContext.getEvent(), out);

                return new ByteArrayInputStream(out.toByteArray());
            }
            else
            {
                return new ByteArrayInputStream((byte[]) super.doTransform(src, encoding));
            }
        }
        catch (Exception e)
        {
            throw new TransformerException(this, e);
        }
    }

}
