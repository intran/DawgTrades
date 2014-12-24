package edu.uga.dawgtrades.persistence.impl;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Iterator;
import com.mysql.jdbc.PreparedStatement;
import edu.uga.dawgtrades.DTException;
import edu.uga.dawgtrades.model.Category;
import edu.uga.dawgtrades.model.ObjectModel;

public class CategoryManager {

    private ObjectModel objectModel = null;
    private Connection conn = null;

    public CategoryManager(Connection conn, ObjectModel objectModel) {
        this.conn = conn;
        this.objectModel = objectModel;
    }

    public void save(Category category)
            throws DTException {
        String insertCategorySql = "insert into Category ( Category_id, name ) values ( ?, ? )";
        String updateCategorySql = "update Category set Category_id = ?, name = ? where id = ?";
        PreparedStatement stmt = null;
        int inscnt;
        long categoryId;

        try {

            if (!category.isPersistent()) {
                stmt = (PreparedStatement) conn.prepareStatement(insertCategorySql);
            } else {
                stmt = (PreparedStatement) conn.prepareStatement(updateCategorySql);
            }

            if (category.getParentId() >= 0) {
                stmt.setLong(1, category.getParentId());
            } else {
                throw new DTException("CategoryManager.save: can't save a category: Parent undefined");
            }

            if (category.getName() != null) // name is unique unique and non null
            {
                stmt.setString(2, category.getName());
            } else {
                throw new DTException("CategoryManager.save: can't save a category: Name undefined");
            }

            if (category.isPersistent()) {
                stmt.setLong(3, category.getId());
            }

            inscnt = stmt.executeUpdate();

            if (!category.isPersistent()) {
                if (inscnt >= 1) {
                    String sql = "select last_insert_id()";
                    if (stmt.execute(sql)) { // statement returned a result

                        // retrieve the result
                        ResultSet r = stmt.getResultSet();

                        // we will use only the first row!
                        //
                        while (r.next()) {

                            // retrieve the last insert auto_increment value
                            categoryId = r.getLong(1);
                            if (categoryId > 0) {
                                category.setId(categoryId); // set this person's db id (proxy object)                                

                            }
                        }
                    }
                } else {
                    throw new DTException("CategoryManager.save: failed to save a Category");
                }
            } else {
                if (inscnt < 1) {
                    throw new DTException("CategoryManager.save: failed to save a Category");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DTException("CategoryManager.save: failed to save a Category: " + e);
        }
    }

    public Iterator<Category> restore(Category category)
            throws DTException {
        String selectCategorySql = "select id,Category_id, name from Category";
        Statement stmt = null;
        StringBuffer query = new StringBuffer(100);
        StringBuffer condition = new StringBuffer(100);

        condition.setLength(0);
        query.append(selectCategorySql);

        if (category != null) {
            if (category.getId() > 0) // id is unique, so it is sufficient to get a person
            {
                query.append(" where id = " + category.getId());
            } else if (category.getName() != null) // userName is unique, so it is sufficient to get a person
            {
                query.append(" where name = '" + category.getName() + "'");
            } else {
                if (category.getParentId() >= 0) {
                    condition.append(" Category_id = '" + category.getParentId() + "'");
                }

                if (condition.length() > 0) {
                    query.append(" where ");
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
                return new CategoryIterator(r, objectModel);
            }
        } catch (Exception e) {      // just in case...
            e.printStackTrace();
            throw new DTException("CategoryManager.restore: Could not restore persistent Category object; Root cause: " + e);
        }

        throw new DTException("CategoryManager.restore: Could not restore persistent Category object");
    }

    public void delete(Category category)
            throws DTException {
        String deleteCategorySql = "delete from Category where id = ?";
        PreparedStatement stmt = null;
        int inscnt;

        if (!category.isPersistent()) // is the Category object persistent?  If not, nothing to actually delete
        {
            return;
        }

        try {
            stmt = (PreparedStatement) conn.prepareStatement(deleteCategorySql);
            stmt.setLong(1, category.getId());
            inscnt = stmt.executeUpdate();
            if (inscnt == 1) {
                return;
            } else {
                throw new DTException("CategoryManager.delete: failed to delete a Category");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DTException("CategoryManager.delete: failed to delete a Category: " + e);
        }
    }
}
