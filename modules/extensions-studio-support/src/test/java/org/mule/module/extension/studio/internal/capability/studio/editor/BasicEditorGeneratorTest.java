/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.module.extension.studio.internal.capability.studio.editor;

import org.mule.module.extension.basic.BasicExtension;

/**
 * Created by pablocabrera on 11/25/15.
 */
public class BasicEditorGeneratorTest extends AbstractEditorGeneratorTest
{

    @Override
    protected Class<?> getExtensionUnderTest()
    {
        return BasicExtension.class;
    }

    @Override
    protected String getExpectedContentFileName()
    {
        return "expected-editor.xml";
    }
}
