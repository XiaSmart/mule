/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.runtime.module.xml.transformer;

import static org.mule.runtime.api.metadata.DataTypeFactory.BYTE_ARRAY_DATA_TYPE;
import static org.mule.runtime.api.metadata.DataTypeFactory.INPUT_STREAM_DATA_TYPE;
import static org.mule.runtime.api.metadata.DataTypeFactory.STRING_DATA_TYPE;
import static org.mule.runtime.api.metadata.DataTypeFactory.dataTypeBuilder;

import org.mule.runtime.core.api.MuleEvent;
import org.mule.runtime.core.api.transformer.DiscoverableTransformer;
import org.mule.runtime.core.message.OutputHandler;
import org.mule.runtime.module.xml.util.XMLUtils;

import java.io.IOException;
import java.io.OutputStream;

import javax.xml.stream.XMLStreamReader;
import javax.xml.stream.XMLStreamWriter;
import javax.xml.transform.Source;
import javax.xml.transform.TransformerFactoryConfigurationError;
import javax.xml.transform.stream.StreamResult;

import org.dom4j.Document;

public class XmlToOutputHandler extends AbstractXmlTransformer implements DiscoverableTransformer
{

    private int priorityWeighting = DiscoverableTransformer.DEFAULT_PRIORITY_WEIGHTING;

    public XmlToOutputHandler()
    {
        registerSourceType(STRING_DATA_TYPE);
        registerSourceType(BYTE_ARRAY_DATA_TYPE);
        registerSourceType(dataTypeBuilder(Source.class).build());
        registerSourceType(dataTypeBuilder(Document.class).build());
        registerSourceType(dataTypeBuilder(org.w3c.dom.Document.class).build());
        registerSourceType(dataTypeBuilder(org.w3c.dom.Element.class).build());
        registerSourceType(INPUT_STREAM_DATA_TYPE);
        registerSourceType(dataTypeBuilder(OutputHandler.class).build());
        registerSourceType(dataTypeBuilder(XMLStreamReader.class).build());
        registerSourceType(dataTypeBuilder(DelayedResult.class).build());
        setReturnDataType(dataTypeBuilder(OutputHandler.class).build());
    }

    @Override
    public Object transformMessage(MuleEvent event, final String encoding)
    {
        final Object src = event.getMessage().getPayload();
        return new OutputHandler()
        {
            @Override
            public void write(MuleEvent event, OutputStream out) throws IOException
            {
                writeXml(src, encoding, out);
            }
        };
    }

    protected void writeXml(final Object src, final String encoding, OutputStream out)
        throws TransformerFactoryConfigurationError, IOException
    {
        try
        {
            if (src instanceof XMLStreamReader)
            {
                // Unfortunately, the StAX source doesn't copy/serialize correctly so
                // we have to do this little hack.
                XMLStreamReader reader = (XMLStreamReader)src;
                XMLStreamWriter writer = getXMLOutputFactory().createXMLStreamWriter(out);
                
                try {
                    writer.writeStartDocument();
                    XMLUtils.copy(reader, writer);
                    writer.writeEndDocument();
                } finally {
                    writer.close();
                    reader.close();
                }
            }
            else if (src instanceof DelayedResult)
            {
                DelayedResult result = (DelayedResult) src;
                
                StreamResult streamResult = new StreamResult(out);
                result.write(streamResult);
            }
            else
            {
                writeToStream(src, encoding, out);
            }
        }
        catch (Exception e)
        {
            IOException ioe = new IOException(e.toString());
            ioe.initCause(e);
            throw ioe;
        }
    }

    @Override
    public int getPriorityWeighting()
    {
        return priorityWeighting;
    }

    @Override
    public void setPriorityWeighting(int priorityWeighting)
    {
        this.priorityWeighting = priorityWeighting;
    }
    
}
