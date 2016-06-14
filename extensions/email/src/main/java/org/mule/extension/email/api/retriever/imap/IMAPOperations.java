/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.extension.email.api.retriever.imap;

import static javax.mail.Flags.Flag.DELETED;
import static javax.mail.Flags.Flag.SEEN;
import static org.mule.extension.email.internal.util.EmailConnectorUtils.INBOX_FOLDER;
import org.mule.extension.email.api.retriever.RetrieverConnection;
import org.mule.extension.email.internal.commands.ExpungeCommand;
import org.mule.extension.email.internal.commands.SetFlagCommand;
import org.mule.runtime.api.message.MuleMessage;
import org.mule.runtime.core.api.MuleContext;
import org.mule.runtime.extension.api.annotation.param.Connection;
import org.mule.runtime.extension.api.annotation.param.Optional;

import javax.inject.Inject;

/**
 * Basic set of operations which perform on top the IMAP email protocol.
 *
 * @since 4.0
 */
public class IMAPOperations
{

    @Inject
    private MuleContext context;

    private final SetFlagCommand setFlagOperation = new SetFlagCommand();
    private final ExpungeCommand expungeOperation = new ExpungeCommand();

    /**
     * Marks an incoming email as READ
     * <p>
     * This operation can target a single email, but also if the incoming {@link MuleMessage} is carrying a list of emails
     * this operation will mark all the emails that the {@link MuleMessage} is carrying.
     *
     * @param message     the incoming {@link MuleMessage}.
     * @param connection  the corresponding {@link RetrieverConnection} instance.
     * @param folder     the folder where the emails are going to be fetched
     * @param emailNumber an optional email number to look up in the folder, if there is no email in the incoming {@link MuleMessage}.
     */
    public void markAsRead(MuleMessage message,
                           @Connection RetrieverConnection connection,
                           @Optional(defaultValue = INBOX_FOLDER) String folder,
                           @Optional Integer emailNumber)
    {
        setFlagOperation.set(message, connection, folder, emailNumber, SEEN);
    }

    /**
     * Marks an incoming email as DELETED. If {@code expunge} is true, the email is going to be isDeleted from the mailbox folder when it's closed.
     * <p>
     * This operation can target a single email, but also if the incoming {@link MuleMessage} is carrying a list of emails
     * this operation will mark all the emails that the {@link MuleMessage} is carrying.
     *
     * @param message     the incoming {@link MuleMessage}.
     * @param connection  the corresponding {@link RetrieverConnection} instance.
     * @param folder     the folder where the emails are going to be fetched
     * @param emailNumber an optional email number to look up in the folder, if there is no email in the incoming {@link MuleMessage}.
     */
    public void markAsDeleted(MuleMessage message,
                              @Connection RetrieverConnection connection,
                              @Optional(defaultValue = INBOX_FOLDER) String folder,
                              @Optional Integer emailNumber)
    {
        setFlagOperation.set(message, connection, folder, emailNumber, DELETED);
    }

    /**
     * Removes from the mailbox all deleted messages
     * if the flag is setted true.
     *
     * @param connection the associated {@link RetrieverConnection}.
     * @param folder     the folder where the emails are going to be fetched
     */
    public void expunge(@Connection RetrieverConnection connection,
                        @Optional(defaultValue = INBOX_FOLDER) String folder)
    {
        expungeOperation.expunge(connection, folder);
    }
}
