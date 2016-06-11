/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.runtime.core.transformer.types;

import org.mule.runtime.api.metadata.DataTypeBuilder;
import org.mule.runtime.api.metadata.DataTypeFactory;

public class DataTypeFactoryImpl extends DataTypeFactory
{

    @Override
    protected <T> DataTypeBuilder<T> createDataTypeBuilder()
    {
        return new DataTypeBuilderImpl<>();
    }
}
