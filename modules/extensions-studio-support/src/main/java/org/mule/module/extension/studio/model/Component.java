/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.module.extension.studio.model;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class Component extends EditorElement {

    private Boolean processesResponse;

    @Override
    public void accept(IEditorElementVisitor visitor) {
        visitor.visit(this);
    }

    @XmlAttribute
    public Boolean getProcessesResponse() {
        return processesResponse;
    }

    public void setProcessesResponse(Boolean processesResponse) {
        this.processesResponse = processesResponse;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = super.hashCode();
        result = prime * result + ((processesResponse == null) ? 0 : processesResponse.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (!super.equals(obj))
            return false;
        if (getClass() != obj.getClass())
            return false;
        Component other = (Component) obj;
        if (processesResponse == null) {
            if (other.processesResponse != null)
                return false;
        } else if (!processesResponse.equals(other.processesResponse))
            return false;
        return true;
    }
}
