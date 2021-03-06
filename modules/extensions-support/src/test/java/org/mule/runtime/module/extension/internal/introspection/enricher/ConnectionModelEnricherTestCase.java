/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.runtime.module.extension.internal.introspection.enricher;

import static java.util.Arrays.asList;
import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;
import static org.mockito.Answers.RETURNS_DEEP_STUBS;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mule.runtime.extension.api.introspection.declaration.DescribingContext;
import org.mule.runtime.extension.api.introspection.declaration.fluent.ExtensionDeclaration;
import org.mule.runtime.extension.api.introspection.declaration.fluent.ExtensionDeclarer;
import org.mule.runtime.extension.api.introspection.declaration.fluent.OperationDeclaration;
import org.mule.runtime.extension.api.introspection.declaration.spi.ModelEnricher;
import org.mule.runtime.extension.api.runtime.InterceptorFactory;
import org.mule.runtime.module.extension.internal.model.property.ConnectionTypeModelProperty;
import org.mule.runtime.module.extension.internal.runtime.connector.ConnectionInterceptor;
import org.mule.tck.junit4.AbstractMuleTestCase;
import org.mule.tck.size.SmallTest;

import java.util.Optional;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

@SmallTest
@RunWith(MockitoJUnitRunner.class)
public class ConnectionModelEnricherTestCase extends AbstractMuleTestCase
{

    @Mock(answer = RETURNS_DEEP_STUBS)
    private DescribingContext describingContext;

    @Mock(answer = RETURNS_DEEP_STUBS)
    private ExtensionDeclarer extensionDeclarer;

    @Mock(answer = RETURNS_DEEP_STUBS)
    private ExtensionDeclaration extensionDeclaration;

    @Mock(answer = RETURNS_DEEP_STUBS)
    private OperationDeclaration connectedOperation;

    @Mock(answer = RETURNS_DEEP_STUBS)
    private OperationDeclaration notConnectedOperation;

    private ModelEnricher enricher = new ConnectionModelEnricher();

    @Before
    public void before() throws Exception
    {
        when(describingContext.getExtensionDeclarer()).thenReturn(extensionDeclarer);
        when(extensionDeclarer.getDeclaration()).thenReturn(extensionDeclaration);
        when(extensionDeclaration.getOperations()).thenReturn(asList(connectedOperation, notConnectedOperation));
        when(connectedOperation.getModelProperty(ConnectionTypeModelProperty.class)).thenReturn(Optional.of(new ConnectionTypeModelProperty(Object.class)));
        when(notConnectedOperation.getModelProperty(ConnectionTypeModelProperty.class)).thenReturn(Optional.empty());
    }

    @Test
    public void enrichConnectedOperation() throws Exception
    {
        enricher.enrich(describingContext);
        ArgumentCaptor<InterceptorFactory> captor = ArgumentCaptor.forClass(InterceptorFactory.class);
        verify(connectedOperation).addInterceptorFactory(captor.capture());

        InterceptorFactory factory = captor.getValue();
        assertThat(factory, is(notNullValue()));
        assertThat(factory.createInterceptor(), is(instanceOf(ConnectionInterceptor.class)));
    }

    @Test
    public void enrichOnlyOnceWhenFlyweight() throws Exception
    {
        when(extensionDeclaration.getOperations()).thenReturn(asList(connectedOperation, connectedOperation, notConnectedOperation));
        enricher.enrich(describingContext);
        verify(connectedOperation, times(1)).addInterceptorFactory(any());
    }

    @Test
    public void skipNotConnectedOperation() throws Exception
    {
        enricher.enrich(describingContext);
        verify(notConnectedOperation, never()).addInterceptorFactory(any(InterceptorFactory.class));
    }
}
