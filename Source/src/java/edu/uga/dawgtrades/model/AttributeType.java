package edu.uga.dawgtrades.model;

public interface AttributeType
        extends Persistable {

    String getName();

    void setName(String name);

    boolean getIsString();

    void setIsString(boolean isString);

    long getCategoryId();

    void setCategoryId(long categoryId);
}
