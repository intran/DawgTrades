package edu.uga.dawgtrades.persistence.impl;

import java.sql.ResultSet;
import java.util.Date;
import java.util.Iterator;
import java.util.Locale;
import java.util.NoSuchElementException;

import edu.uga.dawgtrades.DTException;
import edu.uga.dawgtrades.model.Category;
import edu.uga.dawgtrades.model.ObjectModel;

public class CategoryIterator implements Iterator<Category> {

    private ResultSet rs = null;
    private ObjectModel objectModel = null;
    private boolean more = false;

    public CategoryIterator(ResultSet rs, ObjectModel objectModel)
            throws DTException {
        this.rs = rs;
        this.objectModel = objectModel;
        try {
            more = rs.next();
        } catch (Exception e) {	// just in case...
            throw new DTException("CategoryIterator: Cannot create Category iterator; root cause: " + e);
        }
    }

    public boolean hasNext() {
        return more;
    }

    public Category next() {
        String Name;
        long Category_id;
        long id;

        if (more) {
            Category category = null;
            try {
                id = rs.getLong(1);
                Name = rs.getString(3);
                Category_id = rs.getLong(2);
                more = rs.next();
                category = objectModel.createCategory(null, Name);
                category.setParentId(Category_id);
                category.setId(id);
                
            } catch (Exception e) {	// just in case...
                throw new NoSuchElementException("CategoryIterator: No next Category object; root cause: " + e);
            }

            return category;
        } else {
            throw new NoSuchElementException("CategoryIterator: No next Category object");
        }
    }

    public void remove() {
        throw new UnsupportedOperationException();
    }
}
