package edu.uga.dawgtrades.model.impl;

import edu.uga.dawgtrades.model.*;
import java.util.HashSet;
import java.util.Set;

public class AttributeTypeImpl extends Persistent implements AttributeType {

    
    private long categoryId;
    private String name;
    private boolean isString = false;

    public AttributeTypeImpl() {
    }

    public AttributeTypeImpl(long categoryId, String name, boolean isString) {
        this.categoryId = categoryId;
        this.name = name;
        this.isString = isString;
    }

    
    public long getCategoryId() {
        return this.categoryId;
    }

    public void setCategoryId(long categoryId) {
        this.categoryId = categoryId;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean getIsString() {
        return this.isString;
    }

    public void setIsString(boolean isString) {
        this.isString = isString;
    }

    public String toString() {
        return "AttributeType[" + getId() + "] " + getCategoryId() + " " + getName();
    }

    public boolean equals(Object otherAttributeType) {
        if (otherAttributeType == null) {
            return false;
        }
        if (otherAttributeType instanceof AttributeType) // name is a unique attribute
        {
            return getName().equals(((AttributeType) otherAttributeType).getName());
        }
        return false;
    }

}
