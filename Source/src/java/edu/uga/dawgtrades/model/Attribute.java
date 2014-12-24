package edu.uga.dawgtrades.model;

public interface Attribute
        extends Persistable {

    String getValue();

    void setValue(String value);

    long getItemId();

    void setItemId(long itemId);

    long getAttributeTypeId();

    void setAttributeTypeId(long attributeId);
}
