/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.runtime.core.transformer.simple;

import static org.mule.runtime.api.metadata.DataTypeFactory.OBJECT_DATA_TYPE;
import static org.mule.runtime.api.metadata.DataTypeFactory.dataTypeBuilder;

import org.mule.runtime.core.api.transformer.TransformerException;
import org.mule.runtime.core.config.i18n.MessageFactory;
import org.mule.runtime.core.transformer.AbstractTransformer;

import java.util.Map;

/**
 * <code>MapLookup</code> looks up and returns an object from a Map based on a key.
 */

public class MapLookup extends AbstractTransformer
{

    protected volatile Object key;

    public MapLookup()
    {
        registerSourceType(dataTypeBuilder(Map.class).build());
        setReturnDataType(OBJECT_DATA_TYPE);
    }

    @Override
    public Object doTransform(Object src, String encoding) throws TransformerException
    {
        if (src instanceof Map)
        {
            if (key != null)
            {
                return ((Map) src).get(key);
            }
            else
            {
                throw new TransformerException(MessageFactory
                        .createStaticMessage("Property 'key' must be set in order to use this transformer."));
            }
        }
        else
        {
            throw new TransformerException(MessageFactory
                    .createStaticMessage("Message to transform must be of type java.util.Map"));
        }
    }

    public Object getKey()
    {
        return key;
    }

    public void setKey(Object key)
    {
        this.key = key;
    }

}
