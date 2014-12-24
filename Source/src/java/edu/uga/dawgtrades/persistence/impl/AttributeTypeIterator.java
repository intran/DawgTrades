package edu.uga.dawgtrades.persistence.impl;

import java.sql.ResultSet;
import java.util.Date;
import java.util.Iterator;
import java.util.NoSuchElementException;

import edu.uga.dawgtrades.DTException;
import edu.uga.dawgtrades.model.AttributeType;
import edu.uga.dawgtrades.model.Category;
import edu.uga.dawgtrades.model.Membership;
import edu.uga.dawgtrades.model.ObjectModel;

public class AttributeTypeIterator implements Iterator<AttributeType> {

    private ResultSet rs = null;
    private ObjectModel modelFactory = null;
    private boolean more = false;

    // these two will be used to create a new object
    //
    public AttributeTypeIterator(ResultSet rs, ObjectModel modelFactory)
            throws DTException {
        this.rs = rs;
        this.modelFactory = modelFactory;
        try {
            more = rs.next();
        } catch (Exception e) {  // just in case...
            throw new DTException("AttributeTypeIterator: Cannot create an iterator; root cause: " + e);
        }
    }

    public boolean hasNext() {
        return more;
    }

    public AttributeType next() {
        long id;
        long category_id;
        String name;
        boolean isString;
        Category category = null;

        if (more) {

            try {
                id = rs.getLong(1);
                category_id = rs.getLong(2);
                name = rs.getString(3);
                isString = rs.getBoolean(4);
                more = rs.next();
            } catch (Exception e) {      // just in case...
                throw new NoSuchElementException("PersonIterator: No next Person object; root cause: " + e);
            }

            AttributeType attributeType = null;
            try {
                category = modelFactory.createCategory(null, null);
                category.setId(category_id);
                attributeType = modelFactory.createAttributeType(category, name, isString);
                attributeType.setId(id);

            } catch (DTException ce) {
                ce.printStackTrace();
                System.out.println(ce);
            }

            return attributeType;
        } else {
            throw new NoSuchElementException("PersonIterator: No next Person object");
        }
    }

    public void remove() {
        throw new UnsupportedOperationException();
    }

}
