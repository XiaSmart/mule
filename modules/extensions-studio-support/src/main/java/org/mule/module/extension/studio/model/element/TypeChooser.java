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

@XmlRootElement(name = "type-chooser")
public class TypeChooser extends BaseFieldEditorElement {

    private String associatedConfig;
    private MetaDataKeyParamAffectsType affects;

    @XmlAttribute
    public MetaDataKeyParamAffectsType getAffects() {
        return affects;
    }

    public void setAffects(MetaDataKeyParamAffectsType affects) {
        this.affects = affects;
    }

    @Override
    public void accept(IEditorElementVisitor visitor) {
        visitor.visit(this);
    }

    @XmlAttribute
    public String getAssociatedConfig() {
        return associatedConfig;
    }

    public void setAssociatedConfig(String associatedConfig) {
        this.associatedConfig = associatedConfig;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = super.hashCode();
        result = prime * result + ((affects == null) ? 0 : affects.hashCode());
        result = prime * result + ((associatedConfig == null) ? 0 : associatedConfig.hashCode());
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
        TypeChooser other = (TypeChooser) obj;
        if (affects != other.affects)
            return false;
        if (associatedConfig == null) {
            if (other.associatedConfig != null)
                return false;
        } else if (!associatedConfig.equals(other.associatedConfig))
            return false;
        return true;
    }
}
