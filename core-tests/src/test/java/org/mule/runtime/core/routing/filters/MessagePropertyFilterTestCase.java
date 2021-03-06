/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.runtime.core.routing.filters;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import org.mule.runtime.core.DefaultMuleMessage;
import org.mule.runtime.core.MessageExchangePattern;
import org.mule.runtime.core.api.MuleMessage;
import org.mule.tck.junit4.AbstractMuleContextTestCase;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

public class MessagePropertyFilterTestCase extends AbstractMuleContextTestCase
{
    @Test
    public void testMessagePropertyFilter() throws Exception
    {
        MessagePropertyFilter filter = new MessagePropertyFilter("foo=bar");
        MuleMessage message = new DefaultMuleMessage("blah", muleContext);
        assertTrue(!filter.accept(message));
        message.setOutboundProperty("foo", "bar");
        assertTrue("Filter didn't accept the message", filter.accept(message));
    }

    @Test
    public void testMessagePropertyFilterInboundScope() throws Exception
    {
        DefaultMuleMessage message = new DefaultMuleMessage("blah", muleContext);
        MessagePropertyFilter filter = new MessagePropertyFilter("inbound:foo=bar");
        assertEquals("inbound", filter.getScope());

        assertFalse(filter.accept(message));
        message.setInboundProperty("foo", "bar");
        assertTrue("Filter didn't accept the message", filter.accept(message));
    }

    @Test
    public void testMessagePropertyFilterWithURL() throws Exception
    {
        DefaultMuleMessage message = new DefaultMuleMessage("blah", muleContext);
        MessagePropertyFilter filter = new MessagePropertyFilter("inbound:foo=http://foo.com");
        assertEquals("inbound", filter.getScope());

        assertFalse(filter.accept(message));

        Map inboundProps = new HashMap();
        inboundProps.put("foo", "http://foo.com");
        message = new DefaultMuleMessage("blah", inboundProps, null, null, muleContext);
        assertTrue("Filter didn't accept the message", filter.accept(message));

        // Checking here that a ':' in the value doesn't throw things off
        filter = new MessagePropertyFilter("bar=http://bar.com");
        // default scope
        assertEquals("outbound", filter.getScope());

        assertFalse(filter.accept(message));
        message.setOutboundProperty("bar", "http://bar.com");
        assertTrue("Filter didn't accept the message", filter.accept(message));
    }

    @Test
    public void testMessagePropertyFilterWithNot() throws Exception
    {
        MessagePropertyFilter filter = new MessagePropertyFilter("foo!=bar");
        MuleMessage message = new DefaultMuleMessage("blah", muleContext);

        assertTrue("Filter didn't accept the message", filter.accept(message));
        message.setOutboundProperty("foo", "bar");
        assertFalse(filter.accept(message));
        message.setOutboundProperty("foo", "car");
        assertTrue("Filter didn't accept the message", filter.accept(message));
    }

    @Test
    public void testMessagePropertyFilterWithNotNull() throws Exception
    {
        MessagePropertyFilter filter = new MessagePropertyFilter("foo!=null");
        MuleMessage message = new DefaultMuleMessage("blah", muleContext);

        assertFalse(filter.accept(message));
        removeProperty(message, "foo");
        assertFalse(filter.accept(message));
        message.setOutboundProperty("foo", "car");
        assertTrue("Filter didn't accept the message", filter.accept(message));
    }

    @Test
    public void testMessagePropertyFilterWithCaseSensitivity() throws Exception
    {
        MessagePropertyFilter filter = new MessagePropertyFilter("foo=Bar");
        MuleMessage message = new DefaultMuleMessage("blah", muleContext);
        message.setOutboundProperty("foo", "bar");
        assertFalse(filter.accept(message));
        filter.setCaseSensitive(false);
        assertTrue("Filter didn't accept the message", filter.accept(message));
    }

    @Test
    public void testMessagePropertyFilterWithWildcard() throws Exception
    {
        MessagePropertyFilter filter = new MessagePropertyFilter("foo=B*");
        MuleMessage message = new DefaultMuleMessage("blah", muleContext);
        message.setOutboundProperty("foo", "bar");
        assertFalse(filter.accept(message));
        filter.setCaseSensitive(false);
        assertTrue("Filter didn't accept the message", filter.accept(message));
        filter.setPattern("foo=*a*");
        assertTrue("Filter didn't accept the message", filter.accept(message));
    }

    @Test
    public void testMessagePropertyFilterDodgyValues() throws Exception
    {
        MessagePropertyFilter filter = new MessagePropertyFilter();
        assertFalse(filter.accept((MuleMessage) null));

        filter = new MessagePropertyFilter("foo = bar");
        MuleMessage message = new DefaultMuleMessage("blah", muleContext);
        message.setOutboundProperty("foo", "bar");
        assertTrue("Filter didn't accept the message", filter.accept(message));
        filter.setCaseSensitive(false);

        filter = new MessagePropertyFilter("foo2 =null");
        removeProperty(message, "foo2");
        assertTrue("Filter didn't accept the message", filter.accept(message));

        filter = new MessagePropertyFilter("foo2 =");
        message.setOutboundProperty("foo2", "");
        assertTrue("Filter didn't accept the message", filter.accept(message));

        removeProperty(message, "foo2");
        assertFalse(filter.accept(message));
    }

    private void removeProperty(MuleMessage message, String property)
    {
        message.removeOutboundProperty(property);
    }

    @Test
    public void testMessagePropertyFilterPropertyExists() throws Exception
    {
        MessagePropertyFilter filter = new MessagePropertyFilter("foo!=null");
        MuleMessage message = new DefaultMuleMessage("blah", muleContext);

        assertFalse(filter.accept(message));
        message.setOutboundProperty("foo", "car");
        assertTrue("Filter didn't accept the message", filter.accept(message));
    }

    @Test
    public void testMessagePropertyWithEnum() throws Exception
    {
        MessagePropertyFilter filter = new MessagePropertyFilter("foo=ONE_WAY");
        MuleMessage message = new DefaultMuleMessage("", muleContext);
        assertFalse(filter.accept(message));
        message.setOutboundProperty("foo", MessageExchangePattern.ONE_WAY);
        assertTrue(filter.accept(message));
    }
}
