/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.extension.socket;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import org.mule.functional.functional.EventCallback;
import org.mule.functional.functional.FunctionalStreamingTestComponent;
import org.mule.runtime.core.api.MuleEventContext;
import org.mule.tck.junit4.rule.DynamicPort;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

import org.junit.Rule;
import org.junit.Test;

public class TcpStreamingTestCase extends SocketExtensionTestCase
{
    public static final String TEST_MESSAGE = "Test TCP Request";
    public static final String RESULT = "Received stream; length: 16; 'Test...uest'";


    @Override
    protected String getConfigFile()
    {
        return "streaming-protocol-config.xml";
    }

    @Test
    public void testStreaming() throws Exception
    {
        final CountDownLatch latch = new CountDownLatch(1);
        final AtomicReference<String> message = new AtomicReference<String>();
        final AtomicInteger loopCount = new AtomicInteger(0);

        EventCallback callback = new EventCallback()
        {
            @Override
            public synchronized void eventReceived(MuleEventContext context, Object component)
            {
                try
                {
                    logger.info("called " + loopCount.incrementAndGet() + " times");
                    FunctionalStreamingTestComponent ftc = (FunctionalStreamingTestComponent) component;
                    // without this we may have problems with the many repeats
                    if (1 == latch.getCount())
                    {
                        message.set(ftc.getSummary());
                        assertEquals(RESULT, message.get());
                        latch.countDown();
                    }
                }
                catch (Exception e)
                {
                    logger.error(e.getMessage(), e);
                }
            }
        };

        // this works only if singleton set in descriptor
        Object ftc = getComponent("tcp-listen");
        assertTrue("FunctionalStreamingTestComponent expected", ftc instanceof FunctionalStreamingTestComponent);
        assertNotNull(ftc);

        ((FunctionalStreamingTestComponent) ftc).setEventCallback(callback, TEST_MESSAGE.length());

        flowRunner("tcp-send").withPayload(TEST_MESSAGE).run().getMessage().getPayload();

        latch.await(10, TimeUnit.SECONDS);
        assertEquals(RESULT, message.get());
    }
}
