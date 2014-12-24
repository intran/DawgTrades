package edu.uga.dawgtrades.persistence.impl;

import java.util.ArrayList;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Iterator;

import com.mysql.jdbc.PreparedStatement;

import edu.uga.dawgtrades.DTException;
import edu.uga.dawgtrades.model.Item;
import edu.uga.dawgtrades.model.ObjectModel;

public class ItemManager {

    private ObjectModel objectModel = null;
    private Connection conn = null;

    public ItemManager(Connection conn, ObjectModel objectmodel) {
        this.objectModel = objectModel;
        this.conn = conn;
    }

    public void save(Item item) throws DTException {
        String insertItemSQL = "INSERT INTO Item (user_id, Category_id, name, description) VALUES (?, ?, ?, ?)";
        String updateItemSQL = "UPDATE Item SET user_id=?,Category_id=?,name=?,description=? WHERE id = ?";
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            // Check if we're creating or updating.
            if (!item.isPersistent()) {
                stmt = (PreparedStatement) conn.prepareStatement(insertItemSQL, Statement.RETURN_GENERATED_KEYS);
            } else {
                stmt = (PreparedStatement) conn.prepareStatement(updateItemSQL);
                stmt.setLong(5, item.getId());
            }
            // Set attributes
            stmt.setLong(1, item.getOwnerId());
            stmt.setLong(2, item.getCategoryId());
            stmt.setString(3, item.getName());
            stmt.setString(4, item.getDescription());

            // Again, check which type of updte.
            if (!item.isPersistent()) {
                // Get the number of rows affected by creation (should be 1.)
                int count = stmt.executeUpdate();
                if (count != 1) {
                    throw new SQLException("Insert failed, row count was not 1. Count: " + count);
                } else {
                    // Fetch the ID key to update the existing object with its persistence ID.
                    long idKey = -1;
                    rs = stmt.getGeneratedKeys();

                    // Double check that it has a key (it should)
                    if (rs.next()) {
                        idKey = rs.getLong(1);
                    } else {
                        throw new SQLException("Insert was successful but failed to retreive ID key.");
                    }
                    item.setId(idKey);
                }
            } else {
                // Count the number of updated rows (should be 1).
                int count = stmt.executeUpdate();
                if (count != 1) {
                    throw new SQLException("Update failed, row count was not 1. Count: " + count);
                }
            }
        } catch (SQLException e) {
            // Oops
            e.printStackTrace();
            throw new DTException("ItemManager save failed: " + e);
        } finally {
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException ex) {
                    // ignore
                }
            }

            if (stmt != null) {
                try {
                    stmt.close();
                } catch (SQLException ex) {
                    // ignore
                }
            }

        }

    }

    public Iterator<Item> restore(Item item) throws DTException {
        // Base selectItem SQL
        String selectItemSql = "SELECT id, user_id, Category_id, name, description FROM Item";
        PreparedStatement stmt = null;
        ResultSet rs = null;

        // Argument list
        ArrayList<Object> arguments = new ArrayList<Object>();
        Boolean conditions = false;
        try {
            // If item is null, means fetch ALL Items
            if (item != null) {
                // We have search criteria. If there's an ID, just use that.
                if (item.getId() >= 0) {
                    // Append ID statement, grab results, return iterator.
                    selectItemSql += " WHERE id = ?";
                    stmt = (PreparedStatement) conn.prepareStatement(selectItemSql);
                    stmt.setLong(1, item.getId());
                    rs = stmt.executeQuery();
                    return new ItemIterator(rs, objectModel);
                } else {
                    // Each of these adds a WHERE/AND as appropriate and adds another constraint
                    // to the SQL query.
                    if (item.getName() != "") {
                        conditions = true;
                        selectItemSql += " WHERE name = ?";
                        arguments.add(item.getName());
                    }
                    if (item.getDescription() != "") {
                        if (!conditions) {
                            conditions = true;
                            selectItemSql += " WHERE";
                        } else {
                            selectItemSql += " AND";
                        }
                        selectItemSql += " description = ?";
                        arguments.add(item.getDescription());
                    }
                    if (item.getOwnerId() >= 0) {
                        if (!conditions) {
                            conditions = true;
                            selectItemSql += " WHERE";
                        } else {
                            selectItemSql += " AND";
                        }
                        selectItemSql += " user_id = ?";
                        arguments.add(new Long(item.getOwnerId()));
                    }
                    if (item.getCategoryId() >= 0) {
                        if (!conditions) {
                            conditions = true;
                            selectItemSql += " WHERE";
                        } else {
                            selectItemSql += " AND";
                        }
                        selectItemSql += " Category_id = ?";
                        arguments.add(new Long(item.getCategoryId()));
                    }
                    
                }
            }

            // Construct the prepared statement.
            stmt = (PreparedStatement) conn.prepareStatement(selectItemSql);

            // Iterates through the list of arguments and sets them in the prepared statement.
            if (conditions) {
                int i = 1;
                for (Object arg : arguments) {
                    // All arguments for Item are either Long or String.
                    if (arg instanceof Long) {
                        long argument = ((Long) arg).longValue();
                        stmt.setLong(i++, argument);
                    } else {
                        String argument = (String) arg;
                        stmt.setString(i++, argument);
                    }
                }
            }
            // Execute, return iterator.
            rs = stmt.executeQuery();
            return new ItemIterator(rs, objectModel);
        } catch (Exception e) {      // just in case...
            e.printStackTrace();
            throw new DTException("ItemManager.restore: Could not restore persistent Item object; Root cause: " + e);
        }
        //throw new DTException("ItemManager.restore: Could not restore persistent Item object");
    }

    public void delete(Item item) throws DTException {
        // We only delete with IDs.
        String deleteItemSql = "DELETE FROM Item WHERE id = ?";
        PreparedStatement stmt = null;
        int inscnt;

        if (!item.isPersistent()) // is the object persistent?  If not, nothing to actually delete
        {
            return;
        }

        try {
            stmt = (PreparedStatement) conn.prepareStatement(deleteItemSql);
            stmt.setLong(1, item.getId());
            inscnt = stmt.executeUpdate();
            if (inscnt == 1) {
                return;
            } else {
                throw new DTException("ItemManager.delete: failed to delete an Item");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DTException("ItemManager.delete: failed to delete an Item: " + e);
        }
    }
}
