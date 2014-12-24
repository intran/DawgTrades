package edu.uga.dawgtrades.persistence.impl;

import edu.uga.dawgtrades.model.Attribute;
import edu.uga.dawgtrades.DTException;
import edu.uga.dawgtrades.model.AttributeType;
import edu.uga.dawgtrades.model.Category;
import edu.uga.dawgtrades.model.Item;
import edu.uga.dawgtrades.model.ObjectModel;
import edu.uga.dawgtrades.model.RegisteredUser;
import java.sql.ResultSet;
import java.util.Iterator;
import java.util.NoSuchElementException;

public class AttributeIterator
        implements Iterator<Attribute> {

    private ResultSet rs = null;
    private ObjectModel modelFactory = null;
    private boolean more = false;

    // these two will be used to create a new object
    //
    public AttributeIterator(ResultSet rs, ObjectModel modelFactory)
            throws DTException {
        this.rs = rs;
        this.modelFactory = modelFactory;
        try {
            more = rs.next();
        } catch (Exception e) {  // just in case...
            throw new DTException("AttributeIterator: Cannot create an iterator; root cause: " + e);
        }
    }

    public boolean hasNext() {
        return more;
    }

    public Attribute next() {
        long id;
        long itemId;
        long attributeTypeId;
        long catId;
        long userId;
        String itemName;
        String attTypeName;
        boolean attTypeIsString;
        String value;
        String itemCode;
        String itemDesc;
        AttributeType attributeType = null;
        Item item = null;
        Category category = null;
        RegisteredUser user = null;

        if (more) {

            try {
                id = rs.getLong(1);
                attributeTypeId = rs.getLong(2);
                itemId = rs.getLong(3);
                value = rs.getString(4);
                catId = rs.getLong(5);
                attTypeName = rs.getString(6);
                attTypeIsString = rs.getBoolean(7);
                userId = rs.getLong(8);
                itemName = rs.getString(9);
                itemDesc = rs.getString(10);

                more = rs.next();
            } catch (Exception e) {      // just in case...
                throw new NoSuchElementException("AttributeIterator: No next Person object; root cause: " + e);
            }

            Attribute attribute = null;
            try {
                category = modelFactory.createCategory(null, null);
                category.setId(catId);
                attributeType = modelFactory.createAttributeType(category, attTypeName, attTypeIsString);
                attributeType.setId(attributeTypeId);

                user = modelFactory.createRegisteredUser(null, null, null, null, null, null, false,false, false );
                user.setId(userId);

                item = modelFactory.createItem(category, user, itemName, itemDesc);
                item.setId(itemId);
                
                attribute = modelFactory.createAttribute(attributeType, item, value);
                attribute.setId(id);
                
                
            } catch (DTException ce) {
                ce.printStackTrace();
                System.out.println(ce);
            }           

            return attribute;
        } else {
            throw new NoSuchElementException("PersonIterator: No next Person object");
        }
    }

    public void remove() {
        throw new UnsupportedOperationException();
    }

}
