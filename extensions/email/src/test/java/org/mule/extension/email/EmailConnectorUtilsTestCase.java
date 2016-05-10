/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.extension.email;

import static java.lang.String.format;
import static java.util.Arrays.asList;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsInstanceOf.instanceOf;
import static org.hamcrest.core.IsNull.nullValue;
import static org.junit.Assert.assertThat;
import static org.mule.extension.email.internal.util.EmailConnectorUtils.getAttachments;
import static org.mule.extension.email.internal.util.EmailConnectorUtils.getTextBody;
import static org.mule.extension.email.internal.util.EmailConnectorUtils.toAddress;
import static org.mule.extension.email.internal.util.EmailConnectorUtils.toAddressArray;
import static org.mule.extension.email.util.EmailTestUtils.ALE_EMAIL;
import static org.mule.extension.email.util.EmailTestUtils.EMAIL_CONTENT;
import static org.mule.extension.email.util.EmailTestUtils.EMAIL_JSON_ATTACHMENT_CONTENT;
import static org.mule.extension.email.util.EmailTestUtils.EMAIL_JSON_ATTACHMENT_NAME;
import static org.mule.extension.email.util.EmailTestUtils.EMAIL_TEXT_PLAIN_ATTACHMENT_CONTENT;
import static org.mule.extension.email.util.EmailTestUtils.EMAIL_TEXT_PLAIN_ATTACHMENT_NAME;
import static org.mule.extension.email.util.EmailTestUtils.ESTEBAN_EMAIL;
import static org.mule.extension.email.util.EmailTestUtils.JUANI_EMAIL;
import static org.mule.extension.email.util.EmailTestUtils.PABLON_EMAIL;
import static org.mule.extension.email.util.EmailTestUtils.assertAttachmentContent;
import static org.mule.extension.email.util.EmailTestUtils.buildMultipartMessage;
import static org.mule.extension.email.util.EmailTestUtils.buildSinglePartMessage;

import java.util.Map;

import javax.activation.DataHandler;
import javax.mail.Address;
import javax.mail.Message;
import javax.mail.internet.InternetAddress;

import org.junit.Test;

public class EmailConnectorUtilsTestCase
{

    private static final String JUANI_NAME = "Juan Desimoni";

    @Test
    public void getEmailTextBodyFromMultipart() throws Exception
    {
        Message message = buildMultipartMessage();
        assertThat(getTextBody(message), is(EMAIL_CONTENT));
    }

    @Test
    public void getEmailTextBodyFromSinglepart() throws Exception
    {
        Message message = buildSinglePartMessage();
        assertThat(getTextBody(message), is(EMAIL_CONTENT));
    }

    @Test
    public void getEmailAttachmentsFromMultipart() throws Exception
    {
        Message message = buildMultipartMessage();
        Map<String, DataHandler> attachments = getAttachments(message);

        assertThat(attachments.entrySet(), hasSize(2));
        assertAttachmentContent(attachments, EMAIL_TEXT_PLAIN_ATTACHMENT_NAME, EMAIL_TEXT_PLAIN_ATTACHMENT_CONTENT);
        assertAttachmentContent(attachments, EMAIL_JSON_ATTACHMENT_NAME, EMAIL_JSON_ATTACHMENT_CONTENT);
    }

    @Test
    public void stringAddressToInternetAddress()
    {
        Address address = toAddress(JUANI_EMAIL);
        assertAddress(address, JUANI_EMAIL, null);
    }

    @Test
    public void nameAddressToInternetAddress()
    {
        String nameAddress = getNameAddressFormatInternetAddress();  // address in the "name<address>" format.
        Address address = toAddress(nameAddress);
        assertAddress(address, JUANI_EMAIL, JUANI_NAME);
    }

    @Test
    public void listAddressesToInternetAddressArray()
    {
        Address[] addresses = toAddressArray(asList(JUANI_EMAIL, ESTEBAN_EMAIL, ALE_EMAIL, PABLON_EMAIL, getNameAddressFormatInternetAddress()));
        assertThat(addresses.length, is(5));
        for (Address address : addresses)
        {
            assertThat(address, instanceOf(InternetAddress.class));
        }
    }

    private void assertAddress(Address address, String addressValue, String personal)
    {
        assertThat(address, is(not(nullValue())));
        assertThat(address, instanceOf(InternetAddress.class));
        assertThat(address.getType(), is("rfc822"));
        assertThat(((InternetAddress) address).getAddress(), is(addressValue));
        assertThat(((InternetAddress) address).getPersonal(), is(personal));
    }

    private String getNameAddressFormatInternetAddress()
    {
        return format("%s<%s>", JUANI_NAME, JUANI_EMAIL);
    }


}
