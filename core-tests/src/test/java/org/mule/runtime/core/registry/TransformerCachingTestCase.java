/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.runtime.core.registry;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mule.runtime.api.metadata.DataTypeFactory.BYTE_ARRAY_DATA_TYPE;
import static org.mule.runtime.api.metadata.DataTypeFactory.INPUT_STREAM_DATA_TYPE;
import static org.mule.runtime.api.metadata.DataTypeFactory.dataTypeBuilder;

import org.mule.runtime.api.metadata.DataType;
import org.mule.runtime.core.api.transformer.DiscoverableTransformer;
import org.mule.runtime.core.api.transformer.Transformer;
import org.mule.runtime.core.api.transformer.TransformerException;
import org.mule.runtime.core.transformer.AbstractTransformer;
import org.mule.runtime.core.transformer.simple.ObjectToByteArray;
import org.mule.tck.junit4.AbstractMuleContextTestCase;

import java.io.FilterInputStream;

import org.junit.Test;

public class TransformerCachingTestCase extends AbstractMuleContextTestCase
{
    @Test
    public void testCacheUpdate() throws Exception
    {
        DataType<FilterInputStream> sourceType = dataTypeBuilder(FilterInputStream.class).build();
        Transformer trans = muleContext.getRegistry().lookupTransformer(sourceType, BYTE_ARRAY_DATA_TYPE);
        assertNotNull(trans);
        assertTrue(trans instanceof ObjectToByteArray);

        Transformer trans2 = new FilterInputStreamToByteArray();
        muleContext.getRegistry().registerTransformer(trans2);

        trans = muleContext.getRegistry().lookupTransformer(sourceType, BYTE_ARRAY_DATA_TYPE);
        assertNotNull(trans);
        assertTrue(trans instanceof FilterInputStreamToByteArray);

        trans = muleContext.getRegistry().lookupTransformer(INPUT_STREAM_DATA_TYPE, BYTE_ARRAY_DATA_TYPE);
        assertNotNull(trans);
        assertTrue(trans instanceof ObjectToByteArray);

        muleContext.getRegistry().unregisterTransformer(trans2.getName());

        trans = muleContext.getRegistry().lookupTransformer(sourceType, BYTE_ARRAY_DATA_TYPE);
        assertNotNull(trans);
        assertTrue(trans instanceof ObjectToByteArray);
    }

    public static class FilterInputStreamToByteArray extends AbstractTransformer implements DiscoverableTransformer
    {
        public FilterInputStreamToByteArray()
        {
            registerSourceType(dataTypeBuilder(FilterInputStream.class).build());
            setReturnDataType(BYTE_ARRAY_DATA_TYPE);
        }

        @Override
        protected Object doTransform(Object src, String outputEncoding) throws TransformerException
        {
            throw new UnsupportedOperationException("This is a transformer only to be used for testing");
        }

        @Override
        public int getPriorityWeighting()
        {
            return 0;
        }

        @Override
        public void setPriorityWeighting(int weighting)
        {
            //no-op
        }
    }
}
