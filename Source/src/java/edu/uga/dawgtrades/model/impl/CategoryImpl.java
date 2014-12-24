package edu.uga.dawgtrades.model.impl;

import edu.uga.dawgtrades.model.*;

import java.util.HashSet;
import java.util.Set;

public class CategoryImpl extends Persistent implements Category {

    private long ParentId = -1;
    private String name;

    public CategoryImpl() {
    }

    public CategoryImpl(String name) {
        this.name = name;
    }

    public CategoryImpl(long ParentId, String name) {
        this.ParentId = ParentId;
        this.name = name;

    }

    public long getParentId() {
        return this.ParentId;
    }

    public void setParentId(long ParentId) {
        this.ParentId = ParentId;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String toString() {
        return "Category[" + getId() + "] " + getParentId() + " " + getName();
    }

    public boolean equals(Object otherCategory) {
        if (otherCategory == null) {
            return false;
        }
        if (otherCategory instanceof Category) // name is a unique attribute
        {
            return getName().equals(((Category) otherCategory).getName());
        }
        return false;
    }
}
