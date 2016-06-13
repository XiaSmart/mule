/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.runtime.module.json.transformers;

import static org.mule.runtime.api.metadata.DataTypeFactory.BYTE_ARRAY_DATA_TYPE;
import static org.mule.runtime.api.metadata.DataTypeFactory.INPUT_STREAM_DATA_TYPE;
import static org.mule.runtime.api.metadata.DataTypeFactory.STRING_DATA_TYPE;
import static org.mule.runtime.api.metadata.DataTypeFactory.XML_STRING;
import static org.mule.runtime.api.metadata.DataTypeFactory.dataTypeBuilder;

import org.mule.runtime.core.api.transformer.TransformerException;
import org.mule.runtime.core.util.IOUtils;

import java.io.File;
import java.io.Reader;
import java.net.URL;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLOutputFactory;
import javax.xml.transform.Source;
import javax.xml.transform.stax.StAXSource;

import de.odysseus.staxon.json.JsonXMLInputFactory;

/**
 * Convert JSON to an XML document string
 */
public class JsonToXml extends AbstractToFromXmlTransformer
{
    public JsonToXml()
    {
        this.registerSourceType(STRING_DATA_TYPE);
        this.registerSourceType(INPUT_STREAM_DATA_TYPE);
        this.registerSourceType(BYTE_ARRAY_DATA_TYPE);
        this.registerSourceType(dataTypeBuilder(Reader.class).build());
        this.registerSourceType(dataTypeBuilder(URL.class).build());
        this.registerSourceType(dataTypeBuilder(File.class).build());
        setReturnDataType(XML_STRING);
    }


    /**
     * Use Staxon to convert JSON to an XML string
     */
    @Override
    protected Object doTransform(Object src, String enc) throws TransformerException
    {
        XMLInputFactory inputFactory = new JsonXMLInputFactory();
        inputFactory.setProperty(JsonXMLInputFactory.PROP_MULTIPLE_PI, false);
        TransformerInputs inputs = new TransformerInputs(this,src);
        Source source;
        try
        {
            if (inputs.getInputStream() != null)
            {
                source = new StAXSource(inputFactory.createXMLStreamReader(inputs.getInputStream(), enc == null ? "UTF-8" : enc));
            }
            else
            {
                source = new StAXSource(inputFactory.createXMLStreamReader(inputs.getReader()));
            }

            XMLOutputFactory outputFactory = XMLOutputFactory.newInstance();
            return convert(source, outputFactory);
        }
        catch (Exception ex)
        {
            throw new TransformerException(this, ex);
        }
        finally
        {
            IOUtils.closeQuietly(inputs.getInputStream());
            IOUtils.closeQuietly(inputs.getReader());
        }
    }
}
