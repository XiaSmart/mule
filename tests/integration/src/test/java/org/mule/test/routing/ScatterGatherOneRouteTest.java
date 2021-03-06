/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.test.routing;

import org.mule.runtime.core.api.MuleContext;
import org.mule.runtime.core.api.config.ConfigurationException;
import org.mule.runtime.core.api.lifecycle.InitialisationException;
import org.mule.runtime.core.api.processor.MessageProcessor;
import org.mule.runtime.config.spring.SpringXmlConfigurationBuilder;
import org.mule.runtime.core.context.DefaultMuleContextFactory;
import org.mule.runtime.core.routing.ScatterGatherRouter;
import org.mule.tck.junit4.AbstractMuleTestCase;

import com.yourkit.util.Asserts;

import java.util.Collections;

import org.junit.Test;

public class ScatterGatherOneRouteTest extends AbstractMuleTestCase
{

    @Test(expected = ConfigurationException.class)
    public void oneRouteOnXml() throws Exception
    {
        MuleContext context = new DefaultMuleContextFactory().createMuleContext();
        SpringXmlConfigurationBuilder builder = new SpringXmlConfigurationBuilder("scatter-gather-one-route-test.xml");
        builder.configure(context);
    }

    @Test(expected = InitialisationException.class)
    public void oneRouteProgramatically() throws Exception
    {
        ScatterGatherRouter sc = new ScatterGatherRouter();
        sc.setRoutes(Collections.<MessageProcessor>emptyList());

        try
        {
            sc.initialise();
        }
        catch (InitialisationException e)
        {
            Asserts.assertTrue(e.getCause() instanceof IllegalStateException);
            throw e;
        }
    }
}
