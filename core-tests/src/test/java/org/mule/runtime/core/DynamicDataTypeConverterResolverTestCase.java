/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.runtime.core;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.mule.runtime.api.metadata.DataTypeFactory.BYTE_ARRAY_DATA_TYPE;
import static org.mule.runtime.api.metadata.DataTypeFactory.INPUT_STREAM_DATA_TYPE;
import static org.mule.runtime.api.metadata.DataTypeFactory.STRING_DATA_TYPE;

import org.mule.runtime.api.metadata.DataType;
import org.mule.runtime.core.api.MuleContext;
import org.mule.runtime.core.api.registry.MuleRegistry;
import org.mule.runtime.core.api.transformer.Transformer;
import org.mule.runtime.core.api.transformer.TransformerException;
import org.mule.runtime.core.transformer.builder.MockConverterBuilder;
import org.mule.tck.junit4.AbstractMuleTestCase;
import org.mule.tck.size.SmallTest;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.mockito.Mockito;

@SmallTest
public class DynamicDataTypeConverterResolverTestCase extends AbstractMuleTestCase
{

    private MuleContext muleContext = mock(MuleContext.class);
    private MuleRegistry muleRegistry = mock(MuleRegistry.class);

    @Test
    public void doesNotFailWhenThereIsNoDataTypeResolution() throws TransformerException
    {
        when(muleContext.getRegistry()).thenReturn(muleRegistry);
        when(muleRegistry.lookupTransformer(Mockito.any(DataType.class), Mockito.any(DataType.class))).thenReturn(null);

        DynamicDataTypeConversionResolver resolver = new DynamicDataTypeConversionResolver(muleContext);

        List<DataType<?>> targetTypes = new ArrayList<DataType<?>>();
        targetTypes.add(STRING_DATA_TYPE);
        Transformer resolvedConverter = resolver.resolve(INPUT_STREAM_DATA_TYPE, targetTypes);

        assertEquals(null, resolvedConverter);
    }

    @Test
    public void findsExpectedConverter() throws TransformerException
    {
        Transformer expectedConverter = new MockConverterBuilder().from(BYTE_ARRAY_DATA_TYPE).to(STRING_DATA_TYPE).build();

        when(muleContext.getRegistry()).thenReturn(muleRegistry);
        when(muleRegistry.lookupTransformer(BYTE_ARRAY_DATA_TYPE, STRING_DATA_TYPE)).thenReturn(expectedConverter);

        DynamicDataTypeConversionResolver resolver = new DynamicDataTypeConversionResolver(muleContext);

        List<DataType<?>> targetTypes = new ArrayList<DataType<?>>();
        targetTypes.add(INPUT_STREAM_DATA_TYPE);
        targetTypes.add(STRING_DATA_TYPE);
        Transformer resolvedConverter = resolver.resolve(BYTE_ARRAY_DATA_TYPE, targetTypes);

        assertEquals(expectedConverter, resolvedConverter);
    }
}
