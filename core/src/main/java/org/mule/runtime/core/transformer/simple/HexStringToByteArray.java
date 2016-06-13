/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.runtime.core.transformer.simple;

import static org.mule.runtime.api.metadata.DataTypeFactory.BYTE_ARRAY_DATA_TYPE;
import static org.mule.runtime.api.metadata.DataTypeFactory.STRING_DATA_TYPE;

import org.mule.runtime.core.api.transformer.TransformerException;
import org.mule.runtime.core.transformer.AbstractTransformer;
import org.mule.runtime.core.util.ArrayUtils;
import org.mule.runtime.core.util.StringUtils;

/**
 * Converts a Hex String to a Byte array
 */
public class HexStringToByteArray extends AbstractTransformer
{
    public HexStringToByteArray()
    {
        registerSourceType(STRING_DATA_TYPE);
        setReturnDataType(BYTE_ARRAY_DATA_TYPE);
    }

    @Override
    protected Object doTransform(Object src, String outputEncoding) throws TransformerException
    {
        if (src == null)
        {
            return ArrayUtils.EMPTY_BYTE_ARRAY;
        }

        try
        {
            return StringUtils.hexStringToByteArray((String) src);
        }
        catch (Exception ex)
        {
            throw new TransformerException(this, ex);
        }
    }

}
