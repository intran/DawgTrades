package edu.uga.dawgtrades.model.impl;

import edu.uga.dawgtrades.model.*;

public class AttributeImpl extends Persistent implements Attribute {

    private long attributetypeId;
    private long itemId;
    private String value;

    public AttributeImpl() {
    }

    public AttributeImpl(long attributetypeId, long itemId, String value) {
        this.attributetypeId = attributetypeId;
        this.itemId = itemId;
        this.value = value;
    }

    public long getAttributeTypeId() {
        return this.attributetypeId;
    }

    public void setAttributeTypeId(long attributetypeId) {
        this.attributetypeId = attributetypeId;
    }

    public long getItemId() {
        return this.itemId;
    }

    public void setItemId(long itemId) {
        this.itemId = itemId;
    }

    public String getValue() {
        return this.value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String toString() {
        return "Attribute[" + getId() + "] " + getAttributeTypeId() + " " + getItemId() + " " + getValue();
    }

    public boolean equals(Object otherAttribute) {
        if (otherAttribute == null) {
            return false;
        }
        if (otherAttribute instanceof AttributeType) // name is a unique attribute
        {
            return getValue().equals(((Attribute) otherAttribute).getValue());
        }
        return false;
    }

}
