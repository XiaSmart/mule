/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.module.extension.studio.model.element;

import org.mule.module.extension.studio.model.AbstractEditorElement;
import org.mule.module.extension.studio.model.IEditorElementVisitor;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class Option extends AbstractEditorElement {

    private String value;
    private String caption;
    private String versions;

    @XmlAttribute
    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    @XmlAttribute
    public String getCaption() {
        return caption;
    }

    public void setCaption(String caption) {
        this.caption = caption;
    }

    @XmlAttribute
    public String getVersions() {
        return versions;
    }

    public void setVersions(String versions) {
        this.versions = versions;
    }

    @Override
    public String toString() {
        return "Option [value=" + value + ", caption=" + caption + ", versions=" + versions + "]";
    }

    @Override
    public void accept(IEditorElementVisitor visitor) {
        visitor.visit(this);
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = super.hashCode();
        result = prime * result + ((caption == null) ? 0 : caption.hashCode());
        result = prime * result + ((value == null) ? 0 : value.hashCode());
        result = prime * result + ((versions == null) ? 0 : versions.hashCode());
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
        Option other = (Option) obj;
        if (caption == null) {
            if (other.caption != null)
                return false;
        } else if (!caption.equals(other.caption))
            return false;
        if (value == null) {
            if (other.value != null)
                return false;
        } else if (!value.equals(other.value))
            return false;
        if (versions == null) {
            if (other.versions != null)
                return false;
        } else if (!versions.equals(other.versions))
            return false;
        return true;
    }

}
