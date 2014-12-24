package edu.uga.dawgtrades.model;

public interface Item
        extends Persistable {

    public String getName();

    public void setName(String name);

    public String getDescription();

    public void setDescription(String description);

    public long getCategoryId();

    public void setCategoryId(long categoryId);

    public long getAttributeId();

    public void setAttributeId(long attributeId);

    public long getOwnerId();

    public void setOwnerId(long ownerId);
}
