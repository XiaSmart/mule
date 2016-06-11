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
import static org.mule.runtime.api.metadata.DataTypeFactory.dataTypeBuilder;

import org.mule.runtime.api.metadata.DataType;
import org.mule.runtime.api.metadata.MimeTypes;
import org.mule.runtime.core.api.MuleEvent;
import org.mule.runtime.core.api.lifecycle.InitialisationException;
import org.mule.runtime.core.api.transformer.TransformerException;
import org.mule.runtime.core.config.i18n.CoreMessages;
import org.mule.runtime.core.util.IOUtils;
import org.mule.runtime.module.json.JsonData;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

/**
 * A transformer that will convert a JSON encoded object graph to a java object. The
 * object type is determined by the 'returnType' attribute. Note that this
 * transformers supports Arrays and Lists. For example, to convert a JSON string to
 * an array of org.foo.Person, set the the returnClass=[Lorg.foo.Person;.
 */
public class JsonToObject extends AbstractJsonTransformer
{
    private static final DataType<JsonData> JSON_TYPE = dataTypeBuilder(JsonData.class).forMimeType(MimeTypes.APPLICATION_JSON).build();

    private Map<Class<?>, Class<?>> deserializationMixins = new HashMap<Class<?>, Class<?>>();

    public JsonToObject()
    {
        this.registerSourceType(dataTypeBuilder(Reader.class).build());
        this.registerSourceType(dataTypeBuilder(URL.class).build());
        this.registerSourceType(dataTypeBuilder(File.class).build());
        this.registerSourceType(STRING_DATA_TYPE);
        this.registerSourceType(INPUT_STREAM_DATA_TYPE);
        this.registerSourceType(BYTE_ARRAY_DATA_TYPE);
        setReturnDataType(JSON_TYPE);
    }

    @Override
    public void initialise() throws InitialisationException
    {
        super.initialise();
        //Add shared mixins first
        for (Map.Entry<Class<?>, Class<?>> entry : getMixins().entrySet())
        {
            getMapper().getDeserializationConfig().addMixInAnnotations(entry.getKey(), entry.getValue());
        }

        for (Map.Entry<Class<?>, Class<?>> entry : deserializationMixins.entrySet())
        {
            getMapper().getDeserializationConfig().addMixInAnnotations(entry.getKey(), entry.getValue());
        }
    }

    @Override
    public Object transformMessage(MuleEvent event, String outputEncoding) throws TransformerException
    {
        Object src = event.getMessage().getPayload();
        Object returnValue;
        InputStream is = null;
        Reader reader = null;

        try
        {
            if (src instanceof InputStream)
            {
                is = (InputStream) src;
            }
            else if (src instanceof File)
            {
                is = new FileInputStream((File) src);
            }
            else if (src instanceof URL)
            {
                is = ((URL) src).openStream();
            }
            else if (src instanceof byte[])
            {
                is = new ByteArrayInputStream((byte[]) src);
            }

            if (src instanceof Reader)
            {
                if (getReturnDataType().equals(JSON_TYPE))
                {
                    returnValue = new JsonData((Reader) src);
                }
                else
                {
                    returnValue = getMapper().readValue((Reader) src, getReturnDataType().getType());
                }
            }
            else if (src instanceof String)
            {
                if (getReturnDataType().equals(JSON_TYPE))
                {
                    returnValue = new JsonData((String) src);
                }
                else
                {
                    returnValue = getMapper().readValue((String) src, getReturnDataType().getType());
                }
            }
            else
            {
                reader = new InputStreamReader(is, outputEncoding);
                if (getReturnDataType().equals(JSON_TYPE))
                {
                    returnValue = new JsonData(reader);
                }
                else
                {
                    returnValue = getMapper().readValue(reader, getReturnDataType().getType());
                }
            }
            return returnValue;
        }
        catch (Exception e)
        {
            throw new TransformerException(CoreMessages.transformFailed("json",
                getReturnDataType().getType().getName()), this, e);
        }
        finally
        {
            IOUtils.closeQuietly(reader);
            IOUtils.closeQuietly(is);
        }
    }

    public Map<Class<?>, Class<?>> getDeserializationMixins()
    {
        return deserializationMixins;
    }

    public void setDeserializationMixins(Map<Class<?>, Class<?>> deserializationMixins)
    {
        this.deserializationMixins = deserializationMixins;
    }
}
