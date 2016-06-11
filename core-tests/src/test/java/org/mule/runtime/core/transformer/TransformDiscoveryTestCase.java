/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.runtime.core.transformer;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;
import static org.mule.runtime.api.metadata.DataTypeFactory.STRING_DATA_TYPE;
import static org.mule.runtime.api.metadata.DataTypeFactory.dataTypeBuilder;

import org.mule.runtime.core.api.transformer.Transformer;
import org.mule.runtime.core.api.transformer.TransformerException;
import org.mule.tck.junit4.AbstractMuleContextTestCase;
import org.mule.tck.testmodels.fruit.Apple;
import org.mule.tck.testmodels.fruit.Banana;
import org.mule.tck.testmodels.fruit.Orange;
import org.mule.tck.testmodels.fruit.RedApple;

import org.junit.Test;

public class TransformDiscoveryTestCase extends AbstractMuleContextTestCase
{
    @Override
    protected void doSetUp() throws Exception
    {
        muleContext.getRegistry().registerTransformer(new StringToApple());
        muleContext.getRegistry().registerTransformer(new StringToOrange());
    }

    @Test
    public void testSimpleDiscovery() throws Exception
    {
        Transformer t = muleContext.getRegistry().lookupTransformer(STRING_DATA_TYPE, dataTypeBuilder(Apple.class).build());
        assertNotNull(t);
        assertEquals(StringToApple.class, t.getClass());

        t = muleContext.getRegistry().lookupTransformer(STRING_DATA_TYPE, dataTypeBuilder(Orange.class).build());
        assertNotNull(t);
        assertEquals(StringToOrange.class, t.getClass());


        try
        {
            muleContext.getRegistry().lookupTransformer(STRING_DATA_TYPE, dataTypeBuilder(Banana.class).build());
            fail("There is no transformer to go from String to Banana");
        }
        catch (TransformerException e)
        {
            //expected
        }


        muleContext.getRegistry().registerTransformer(new StringToRedApple());

        t = muleContext.getRegistry().lookupTransformer(STRING_DATA_TYPE, dataTypeBuilder(RedApple.class).build());
        assertNotNull(t);
        assertEquals(StringToRedApple.class, t.getClass());
    }

    protected class StringToApple extends org.mule.runtime.core.transformer.AbstractDiscoverableTransformer
    {
        public StringToApple()
        {
            setReturnDataType(dataTypeBuilder(Apple.class).build());
        }

        @Override
        protected Object doTransform(Object src, String encoding) throws TransformerException
        {
            return new Apple();
        }
    }

    protected class StringToRedApple extends org.mule.runtime.core.transformer.AbstractDiscoverableTransformer
    {
        public StringToRedApple()
        {
            setReturnDataType(dataTypeBuilder(RedApple.class).build());
            setPriorityWeighting(MAX_PRIORITY_WEIGHTING);
        }

        @Override
        protected Object doTransform(Object src, String encoding) throws TransformerException
        {
            return new RedApple();
        }
    }

    protected class StringToOrange extends org.mule.runtime.core.transformer.AbstractDiscoverableTransformer
    {
        public StringToOrange()
        {
            setReturnDataType(dataTypeBuilder(Orange.class).build());
        }

        @Override
        protected Object doTransform(Object src, String encoding) throws TransformerException
        {
            return new Orange();
        }
    }
}
