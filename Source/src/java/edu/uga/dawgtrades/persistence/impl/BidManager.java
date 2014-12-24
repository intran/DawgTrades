package edu.uga.dawgtrades.persistence.impl;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Iterator;
import edu.uga.dawgtrades.DTException;
import edu.uga.dawgtrades.model.Bid;
import edu.uga.dawgtrades.model.ObjectModel;
import java.sql.PreparedStatement;
import java.sql.Timestamp;

public class BidManager {

    private ObjectModel objectModel = null;
    private Connection conn = null;

    public BidManager(Connection conn, ObjectModel objectmodel) {
        this.objectModel = objectModel;
        this.conn = conn;
    }

    public void save(Bid bid) throws DTException {
        String insertBidSQL = "INSERT INTO Bid(user_id,auction_id,amount,date)VALUES(?,?,?,?)";
        String updateBidSQL = "UPDATE Bid SET user_id=?,auction_id=?,amount=?,date=? where id = ?";
        PreparedStatement stmt = null;
        ResultSet rs = null;
        long bidID;
        int inscnt;
        try {
            if (!bid.isPersistent()) {
                stmt = (PreparedStatement) conn.prepareStatement(insertBidSQL);
            } else {
                stmt = (PreparedStatement) conn.prepareStatement(updateBidSQL);
            }
            stmt.setLong(1, bid.getRegisteredUser().getId());
            stmt.setLong(2, bid.getAuction().getId());
            stmt.setDouble(3, bid.getAmount());

            stmt.setTimestamp(4, new Timestamp(System.currentTimeMillis()));

            if (bid.isPersistent()) {
                stmt.setLong(4, bid.getId());
            }

            inscnt = stmt.executeUpdate();

            if (!bid.isPersistent()) {
                if (inscnt >= 1) {
                    String sql = "select last_insert_id()";
                    if (stmt.execute(sql)) { // statement returned a result

                        // retrieve the result
                        ResultSet r = stmt.getResultSet();
                        // we will use only the first row!
                        //
                        while (r.next()) {
                            // retrieve the last insert auto_increment value
                            bidID = r.getLong(1);
                            if (bidID > 0) {
                                bid.setId(bidID); // set this auction's db id (proxy object)
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

    public Iterator<Bid> restore(Bid bid) throws DTException {
        String selectBidSql = "SELECT id,user_id,auction_id,amount,date FROM Bid";
        Statement stmt = null;
        StringBuffer query = new StringBuffer(100);
        StringBuffer condition = new StringBuffer(100);
        condition.setLength(0);
        query.append(selectBidSql);
        if (bid != null) {
            if (bid.getId() >= 0) {
                query.append(" WHERE id=" + bid.getId());
            } else {
                if (bid.getRegisteredUser()!=null){
                    if ((bid.getRegisteredUser()).getId() > 0) {
                    condition.append(" user_id = " + (bid.getRegisteredUser()).getId());
                    }
                }
                if (bid.getAuction()!=null){
                    if ((bid.getAuction()).getId() > 0) {
                    if (condition.length() > 0) {
                        condition.append(" and");
                    }
                    condition.append(" auction_id = " + (bid.getAuction()).getId());
                }
                }
                if (bid.getAmount() > 0) {
                    if (condition.length() > 0) {
                        condition.append(" and");
                    }
                    condition.append(" amount = " + bid.getAmount());
                }
                if (bid.getDate() != null) {
                    if (condition.length() > 0) {
                        condition.append(" and");
                    }
                    condition.append(" date = " + bid.getDate());
                }
                if (condition.length() > 0) {
                    query.append(" where ");
                    query.append(condition);
                }
            }
            // Convenience: make things go in descending order by amount.
            query.append(" ORDER BY amount DESC");
        }
        try {
            stmt = conn.createStatement();
            if (stmt.execute(query.toString())) {
                ResultSet r = stmt.getResultSet();
                return new BidIterator(r, objectModel);
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new DTException("BidManager.restore: Could not restore persistent Bid object; Root cause: " + e);
        }
        throw new DTException("BidManager.restore: Could not restore persistent Bid object");
    }

    public void delete(Bid bid)
            throws DTException {
        String deleteBidSql = "DELETE FROM Bid WHERE id = ?";
        PreparedStatement stmt = null;
        int inscnt;
        if (!bid.isPersistent()) // is the Club object persistent? If not, nothing to actually delete
        {
            return;
        }
        try {
            stmt = (PreparedStatement) conn.prepareStatement(deleteBidSql);
            stmt.setLong(1, bid.getId());
            inscnt = stmt.executeUpdate();
            if (inscnt == 1) {
                return;
            } else {
                throw new DTException("BidManager.delete: failed to delete a bid");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DTException("BidManager.delete: failed to delete a bid: " + e);
        }
    }
}
