package edu.uga.dawgtrades.model.impl;

import edu.uga.dawgtrades.model.*;

import java.util.HashSet;
import java.util.Set;

public class ItemImpl extends Persistent implements Item {

    private long categoryId;
    private long attributeId;
    private long ownerId;
    private String code;
    private String name;
    private String description;

    public ItemImpl() {
        this.categoryId = -1;
        this.attributeId = -1;
        this.ownerId = -1;
        this.code = "";
        this.name = "";
        this.description = "";
    }

    public ItemImpl(long categoryId, long ownerId, String name) {
        this.categoryId = categoryId;
        this.ownerId = ownerId;
        this.code = code;
        this.name = name;
    }

    public ItemImpl(long categoryId, long ownerId, String name, String description) {
        this.categoryId = categoryId;
        this.ownerId = ownerId;
        this.code = code;
        this.name = name;
        this.description = description;

    }
    
    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return this.description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public long getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(long categoryId) {
        this.categoryId = categoryId;
    }

    public long getAttributeId() {
        return attributeId;
    }

    public void setAttributeId(long attributeId) {
        this.attributeId = attributeId;
    }

    public long getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(long ownerId) {
        this.ownerId = ownerId;
    }

    public String toString() {
        return "Item[" + getId() + "] " + getCategoryId() + " " + getOwnerId() + " " + getAttributeId() + " " + getName() + " " + getDescription();
    }

    public boolean equals(Object otherItem) {
        if (otherItem == null) {
            return false;
        }
        if (otherItem instanceof Item) // name is a unique attribute
        {
            return getName().equals(((Item) otherItem).getName());
        }
        return false;
    }

}
