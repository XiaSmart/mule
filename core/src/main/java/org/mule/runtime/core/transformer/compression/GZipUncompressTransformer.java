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
import static org.mule.runtime.api.metadata.DataTypeFactory.STRING_DATA_TYPE;

import org.mule.runtime.api.metadata.DataType;
import org.mule.runtime.core.api.serialization.SerializationException;
import org.mule.runtime.core.api.transformer.TransformerException;
import org.mule.runtime.core.config.i18n.MessageFactory;
import org.mule.runtime.core.util.compression.GZipCompression;

import java.io.IOException;
import java.io.InputStream;


/**
 * <code>GZipCompressTransformer</code> will uncompress a byte[] or InputStream
 */
public class GZipUncompressTransformer extends AbstractCompressionTransformer
{
    public GZipUncompressTransformer()
    {
        super();
        this.setStrategy(new GZipCompression());
        this.registerSourceType(BYTE_ARRAY_DATA_TYPE);
        this.registerSourceType(INPUT_STREAM_DATA_TYPE);
        // No type checking for the return type by default. It could either be a byte array, an input stream or an object.
        this.setReturnDataType(OBJECT_DATA_TYPE);
    }

    @Override
    public Object doTransform(Object src, String outputEncoding) throws TransformerException
    {
        try
        {
            if (src instanceof InputStream)
            {
                return getStrategy().uncompressInputStream((InputStream) src);
            }
            else
            {
                byte[] buffer = getStrategy().uncompressByteArray((byte[]) src);
                DataType<?> returnDataType = getReturnDataType();

                // If a return type has been specified, then deserialize the uncompressed byte array.
                if (STRING_DATA_TYPE.equals(returnDataType))
                {
                    return new String(buffer, outputEncoding);
                }
                else if (!OBJECT_DATA_TYPE.equals(returnDataType) && !BYTE_ARRAY_DATA_TYPE.equals(returnDataType))
                {
                    try
                    {
                        return muleContext.getObjectSerializer().deserialize(buffer);
                    }
                    catch (SerializationException e)
                    {
                        throw new TransformerException(this, e);
                    }
                }
                else
                {
                    // First try to deserialize the byte array. If it can be deserialized, then it was originally serialized.
                    try
                    {
                        return muleContext.getObjectSerializer().deserialize(buffer);
                    }
                    catch (SerializationException e)
                    {
                        // If it fails, ignore it. We assume it was not serialized in the first place and return the buffer as it is.
                        return buffer;
                    }
                }
            }
        }
        catch (IOException e)
        {
            throw new TransformerException(
                    MessageFactory.createStaticMessage("Failed to uncompress message."), this, e);
        }
    }
}
