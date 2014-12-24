package edu.uga.dawgtrades.persistence.impl;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Iterator;

import edu.uga.dawgtrades.DTException;
import edu.uga.dawgtrades.model.Attribute;
import edu.uga.dawgtrades.model.AttributeType;
import edu.uga.dawgtrades.model.Category;
import edu.uga.dawgtrades.model.ObjectModel;
import java.sql.PreparedStatement;

class AttributeTypeManager  {

    private ObjectModel objectModel = null;
    private Connection conn = null;

    public AttributeTypeManager(Connection conn, ObjectModel objectModel) {
        this.conn = conn;
        this.objectModel = objectModel;
    }

    public void save(AttributeType attributeType)
            throws DTException {

        String insertAttributeTypeSql = "insert into AttributeType (category_id, name, isString ) values ( ?, ?, ? )";
        String updateAttributeTypeSql = "update AttributeType set category_id = ?, name = ?, isString = ? where id = ?";
        PreparedStatement stmt = null;
        int inscnt;
        long attributeTypeId;

        if (attributeType.getCategoryId() == 0) {
            throw new DTException("AttributeTypeManager.save: Attempting to save a AttributeType with no Category defined");
        }

        try {
            if (!attributeType.isPersistent()) {
                stmt = (PreparedStatement) conn.prepareStatement(insertAttributeTypeSql);
            } else {
                stmt = (PreparedStatement) conn.prepareStatement(updateAttributeTypeSql);
            }

            stmt.setLong(1, attributeType.getCategoryId());

            if (attributeType.getName() != null) {
                stmt.setString(2, attributeType.getName());
            } else {
                throw new DTException("AttributeTypeManager.save: can't save a AttributeType: Name undefined");
            }
            stmt.setBoolean(3, attributeType.getIsString());

            if( attributeType.isPersistent() )
                stmt.setLong( 4, attributeType.getId() );
            
            inscnt = stmt.executeUpdate();

            if (!attributeType.isPersistent()) {
                if (inscnt >= 1) {
                    String sql = "select last_insert_id()";
                    if (stmt.execute(sql)) { // statement returned a result

                        // retrieve the result
                        ResultSet r = stmt.getResultSet();
                        // we will use only the first row!
                        //
                        while (r.next()) {
                            // retrieve the last insert auto_increment value
                            attributeTypeId = r.getLong(1);
                            if (attributeTypeId > 0) {
                                attributeType.setId(attributeTypeId); // set this membership's db id (proxy object)
                            }
                        }
                    }
                } else {
                    throw new DTException("AttributeTypeManager.save: failed to save a AttributeType");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DTException("AttributeTypeManager.save: failed to save a AttributeType: " + e);
        }
    }

    public Iterator<AttributeType> restore(AttributeType attrType)
            throws DTException {

        String selectAttributeTypeSql = "select att.id, att.category_id, att.name, att.isString FROM AttributeType att ";

        Statement stmt = null;
        StringBuffer query = new StringBuffer(100);

        // form the query based on the given Club object instance
        query.append(selectAttributeTypeSql);

        if (attrType != null) {
            if(attrType.getId() >= 0) {
                query.append(" where att.id = " + attrType.getId());
            } else if (attrType.getCategoryId() > 0) // id is unique, so it is sufficient to get a membership
            {
                query.append(" where att.category_id = " + attrType.getCategoryId());
            }
        }

        try {
            stmt = conn.createStatement();

            // retrieve the persistent Person object
            //
            if (stmt.execute(query.toString())) { // statement returned a result
                ResultSet r = stmt.getResultSet();
                return new AttributeTypeIterator(r, objectModel);
            }
        } catch (Exception e) {      // just in case...
            throw new DTException("AttributeTypeManager.restore: Could not restore persistent AttributeType object; Root cause: " + e);
        }

        // if we reach this point, it's an error
        throw new DTException("AttributeTypeManager.restore: Could not restore persistent AttributeType object");
    }

    public AttributeType restore(Attribute attribute)
            throws DTException {

        if (attribute != null) {
            if (attribute.isPersistent()) {
                String selectAttributeTypeSql = "select att.id, att.category_id, att.name, att.isString "
                        + "FROM AttributeType att  , Attribute at "
                        + "where at.attributeType_id = att.id and at.id = " + attribute.getId();

                Statement stmt = null;
                AttributeType attributeType = null;

                try {

                    stmt = conn.createStatement();

                    if (stmt.execute(selectAttributeTypeSql)) {
                        ResultSet r = stmt.getResultSet();
                        r.next();
                        Category category = objectModel.createCategory(null, null);
                        category.setId(r.getLong(2));

                        attributeType = objectModel.createAttributeType(category, r.getString(3), r.getBoolean(4));
                        attributeType.setId(r.getLong(1));
                        return attributeType;
                    }
                } catch (Exception e) {      // just in case...
                    e.printStackTrace();
                    throw new DTException("MembershipManager.restore: Could not restore persistent Membership object; Root cause: " + e);
                }
            }
        }
        throw new DTException("MembershipManager.restore: Could not restore persistent Membership object");

    }

    public void delete(AttributeType attributeType)
            throws DTException {

        String deleteAttributeTypeSql = "delete from AttributeType where id = ?";
        PreparedStatement stmt = null;
        int inscnt;

        if (!attributeType.isPersistent()) // is the AttributeType object persistent?  If not, nothing to actually delete
        {
            return;
        }

        try {
            stmt = (PreparedStatement) conn.prepareStatement(deleteAttributeTypeSql);
            stmt.setLong(1, attributeType.getId());
            inscnt = stmt.executeUpdate();
            if (inscnt == 0) {
                throw new DTException("AttributeTypeManager.delete: failed to delete this AttributeType");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DTException("AttributeTypeManager.delete: failed to delete a AttributeType: " + e);
        }
    }

}
