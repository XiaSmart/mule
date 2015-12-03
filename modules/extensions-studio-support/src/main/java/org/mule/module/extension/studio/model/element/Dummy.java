/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.module.extension.studio.model.element;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

import org.mule.module.extension.studio.model.IEditorElementVisitor;

@XmlRootElement
public class Dummy extends BaseFieldEditorElement {

    private Boolean hiddenForUser;
    private Boolean loadLater;

    @XmlAttribute
    public Boolean getHiddenForUser() {
        return hiddenForUser;
    }

    public void setHiddenForUser(Boolean hiddenForUser) {
        this.hiddenForUser = hiddenForUser;
    }

    @XmlAttribute
    public Boolean getLoadLater() {
        return loadLater;
    }

    public void setLoadLater(Boolean loadLater) {
        this.loadLater = loadLater;
    }

    @Override
    public void accept(IEditorElementVisitor visitor) {
        visitor.visit(this);
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = super.hashCode();
        result = prime * result + ((hiddenForUser == null) ? 0 : hiddenForUser.hashCode());
        result = prime * result + ((loadLater == null) ? 0 : loadLater.hashCode());
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
        Dummy other = (Dummy) obj;
        if (hiddenForUser == null) {
            if (other.hiddenForUser != null)
                return false;
        } else if (!hiddenForUser.equals(other.hiddenForUser))
            return false;
        if (loadLater == null) {
            if (other.loadLater != null)
                return false;
        } else if (!loadLater.equals(other.loadLater))
            return false;
        return true;
    }
}
