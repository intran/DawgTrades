package edu.uga.dawgtrades.model;

public interface Category
        extends Persistable {

    String getName();

    void setName(String name);

    long getParentId();

    void setParentId(long parentId);
}
