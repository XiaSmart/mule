/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.runtime.core.transformer.compression;

import static org.mule.runtime.api.metadata.DataTypeFactory.BYTE_ARRAY_DATA_TYPE;
import static org.mule.runtime.api.metadata.DataTypeFactory.INPUT_STREAM_DATA_TYPE;
import static org.mule.runtime.api.metadata.DataTypeFactory.OBJECT_DATA_TYPE;
import static org.mule.runtime.api.metadata.DataTypeFactory.dataTypeBuilder;

import org.mule.runtime.core.api.transformer.TransformerException;
import org.mule.runtime.core.util.compression.GZipCompression;

import java.io.InputStream;
import java.io.Serializable;

/**
 * <code>GZipCompressTransformer</code> is a transformer compressing objects into
 * byte arrays.
 */
public class GZipCompressTransformer extends AbstractCompressionTransformer
{
    public GZipCompressTransformer()
    {
        super();
        this.setStrategy(new GZipCompression());
        this.registerSourceType(dataTypeBuilder(Serializable.class).build());
        this.registerSourceType(BYTE_ARRAY_DATA_TYPE);
        this.registerSourceType(INPUT_STREAM_DATA_TYPE);
        // No type checking for the return type by default. It could either be a byte array or an input stream.
        this.setReturnDataType(OBJECT_DATA_TYPE);
    }

    @Override
    public Object doTransform(Object src, String outputEncoding) throws TransformerException
    {
        try
        {
            if (src instanceof InputStream)
            {
                return getStrategy().compressInputStream((InputStream) src);
            }
            else
            {
                byte[] data;
                if (src instanceof byte[])
                {
                    data = (byte[]) src;
                }
                else if (src instanceof String)
                {
                    data = ((String) src).getBytes(outputEncoding);
                }
                else
                {
                    data = muleContext.getObjectSerializer().serialize(src);
                }
                return getStrategy().compressByteArray(data);
            }
        }
        catch (Exception ioex)
        {
            throw new TransformerException(this, ioex);
        }
    }
}
