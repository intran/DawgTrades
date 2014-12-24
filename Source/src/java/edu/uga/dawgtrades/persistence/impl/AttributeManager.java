package edu.uga.dawgtrades.persistence.impl;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Iterator;

import edu.uga.dawgtrades.DTException;
import edu.uga.dawgtrades.model.Attribute;
import edu.uga.dawgtrades.model.ObjectModel;
import java.sql.PreparedStatement;

class AttributeManager {

    private ObjectModel objectModel = null;
    private Connection conn = null;

    public AttributeManager(Connection conn, ObjectModel objectModel) {
        this.conn = conn;
        this.objectModel = objectModel;
    }

    public void save(Attribute attribute)
            throws DTException {

        String insertAttributeSql = "insert into Attribute (attributeType_id,item_id, value ) values ( ?, ? ,?)";
        String updateAttributeSql = "update Attribute set attributeType_id = ?,item_id= ? , value = ? where id = ?";
        PreparedStatement stmt = null;
        int inscnt;
        long attributeId;

        if (attribute.getAttributeTypeId() == 0) {
            throw new DTException("AttributeManager.save: Attempting to save a Attribute with no AttributeType defined");
        }
        if (attribute.getItemId() == 0) {
            throw new DTException("AttributeManager.save: Attempting to save a Attribute with no Item defined");
        }

        try {
            if (!attribute.isPersistent()) {
                stmt = (PreparedStatement) conn.prepareStatement(insertAttributeSql);
            } else {
                stmt = (PreparedStatement) conn.prepareStatement(updateAttributeSql);
            }

            if (attribute.getAttributeTypeId() != 0) {
                stmt.setLong(1, attribute.getAttributeTypeId());
            } else {
                throw new DTException("AttributeManager.save: can't save a Attribute: AttributeType undefined");
            }
            
            if (attribute.getItemId() != 0) {
                stmt.setLong(2, attribute.getItemId());
            } else {
                throw new DTException("AttributeManager.save: can't save a Attribute: Item undefined");
            }            

            if (attribute.getValue() != null) {
                stmt.setString(3, attribute.getValue());
            } else {
                throw new DTException("AttributeManager.save: can't save a Attribute: value undefined");
            }

            if( attribute.isPersistent() )
                stmt.setLong( 4, attribute.getId() );
            
            inscnt = stmt.executeUpdate();

            if (!attribute.isPersistent()) {
                if (inscnt >= 1) {
                    String sql = "select last_insert_id()";
                    if (stmt.execute(sql)) { // statement returned a result

                        // retrieve the result
                        ResultSet r = stmt.getResultSet();
                        // we will use only the first row!
                        //
                        while (r.next()) {
                            // retrieve the last insert auto_increment value
                            attributeId = r.getLong(1);
                            if (attributeId > 0) {
                                attribute.setId(attributeId); // set this attribute's db id (proxy object)
                            }
                        }
                    }
                } else {
                    throw new DTException("AttributeManager.save: failed to save a Attribute");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DTException("AttributeManager.save: failed to save a Attribute: " + e);
        }
    }

    public Iterator<Attribute> restore(Attribute attribute)
            throws DTException {

        String selectAttributeSql = "select att.id,att.attributeType_id, att.item_id, att.value, "
                + "attype.Category_id, attype.name, attype.isString, "
                + "it.user_id, it.name itemName,it.description "
                + "from Attribute att, AttributeType attype, Item it "
                + "where attype.id = att.attributeType_id and it.id = att.item_id ";

        Statement stmt = null;
        StringBuffer query = new StringBuffer(100);
        StringBuffer condition = new StringBuffer(100);

        // This is the only hard requirement we have
        if (attribute.getItemId() <= 0) {
            throw new DTException("AttributeManager.restore: the argument attribute includes a non-persistent Item object");
        }

        condition.setLength(0);

        // form the query based on the given Club object instance
        query.append(selectAttributeSql);

        if (attribute != null) {
            if (attribute.isPersistent()) // id is unique, so it is sufficient to get a attribute
            {
                query.append(" where id = " + attribute.getId());
            } else {

                if (attribute.getAttributeTypeId() != 0) {
                    condition.append(" and att.attributeType_id = " + attribute.getAttributeTypeId());
                }

                if (attribute.getItemId() != 0) {
                    condition.append(" and att.item_id = " + attribute.getItemId());
                }

                if (attribute.getValue() != null) {
                    // fix the date conversion
                    condition.append(" and att.value = " + attribute.getValue());
                }

                if (condition.length() > 0) {
                    query.append(condition);
                }
            }
        }

        try {
            stmt = conn.createStatement();

            // retrieve the persistent Person object
            //
            if (stmt.execute(query.toString())) { // statement returned a result
                ResultSet r = stmt.getResultSet();
                return new AttributeIterator(r, objectModel);
            }
        } catch (Exception e) {      // just in case...
            throw new DTException("AttributeManager.restore: Could not restore persistent Attribute object; Root cause: " + e);
        }

        // if we reach this point, it's an error
        throw new DTException("AttributeManager.restore: Could not restore persistent Attribute object");
    }

    public void delete(Attribute attribute)
            throws DTException {

        String deleteAttributeSql = "delete from attribute where id = ?";
        PreparedStatement stmt = null;
        int inscnt;

        if (!attribute.isPersistent()) // is the AttributeType object persistent?  If not, nothing to actually delete
        {
            return;
        }

        try {
            stmt = (PreparedStatement) conn.prepareStatement(deleteAttributeSql);
            stmt.setLong(1, attribute.getId());
            inscnt = stmt.executeUpdate();
            if (inscnt == 0) {
                throw new DTException("AttributeManager.delete: failed to delete this Attribute");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DTException("AttributeManager.delete: failed to delete a Attribute: " + e);
        }
    }

}
