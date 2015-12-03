/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.module.extension.studio.model.element;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;

@XmlEnum
public enum Category {
    @XmlEnumValue("org.mule.tooling.category.scopes")
    SCOPES,
    @XmlEnumValue("org.mule.tooling.category.cloudconnectors")
    CLOUD_CONNECTORS,
    @XmlEnumValue("org.mule.tooling.category.flowControl")
    FLOW_CONTROL,
    @XmlEnumValue("org.mule.tooling.category.endpoints")
    ENDPOINTS,
    @XmlEnumValue("org.mule.tooling.category.core")
    CORE,
    @XmlEnumValue("category:org.mule.tooling.ui.modules.core.exceptions")
    EXCEPTIONS
}
