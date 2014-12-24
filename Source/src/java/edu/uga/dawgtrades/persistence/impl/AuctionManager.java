package edu.uga.dawgtrades.persistence.impl;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Iterator;
import edu.uga.dawgtrades.DTException;
import edu.uga.dawgtrades.model.Auction;
import edu.uga.dawgtrades.model.ObjectModel;
import java.sql.*;

public class AuctionManager {

    private ObjectModel objectModel = null;
    private Connection conn = null;

    public AuctionManager(Connection conn, ObjectModel objectmodel) {
        this.objectModel = objectModel;
        this.conn = conn;
    }

    public void save(Auction auction) throws DTException {

        long auctionID;
        java.util.Date expDate = null;
        ResultSet rs = null;
        int inscnt;

        String insertAuctionSQL = "insert into Auction(item_id,minPrice,expiration)values(?,?,?)";
        String updateAuctionSQL = "update Auction set item_id=?, minPrice=?,expiration=? where id = ?";
        PreparedStatement stmt = null;
        //check that the auction has an item
        if (auction.getItemId() <= 0) {
            throw new DTException("AuctionManager.save: Attempting to save an Auction with no Item defined");
        }
		//check that the auction item is persistent

        try {
            if (!auction.isPersistent()) {
                stmt = (PreparedStatement) conn.prepareStatement(insertAuctionSQL);
            } else {
                stmt = (PreparedStatement) conn.prepareStatement(updateAuctionSQL);
            }

            stmt.setLong(1, auction.getItemId());
            if (auction.getMinPrice() != 0.0) {

                stmt.setDouble(2, auction.getMinPrice());
            } else {
                throw new DTException("AuctionManager.save: Can't save auction. Price undefined");
            }

            if (auction.getExpiration() != null) {
                expDate = auction.getExpiration();
                java.sql.Timestamp ts = new java.sql.Timestamp(expDate.getTime());
                stmt.setTimestamp(3, ts);
            } else {
                stmt.setNull(3, java.sql.Types.TIMESTAMP);
            }

            if( auction.isPersistent() )
                stmt.setLong( 4, auction.getId() );
            
            inscnt = stmt.executeUpdate();
            
            if (!auction.isPersistent()) {
                if (inscnt >= 1) {
                    String sql = "select last_insert_id()";
                    if (stmt.execute(sql)) { // statement returned a result

                        // retrieve the result
                        ResultSet r = stmt.getResultSet();
                        // we will use only the first row!
                        //
                        while (r.next()) {
                            // retrieve the last insert auto_increment value
                            auctionID = r.getLong(1);
                            if (auctionID > 0) {
                                auction.setId(auctionID); // set this auction's db id (proxy object)
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

    public Iterator<Auction> restore(Auction auction) throws DTException {
        String selectAuctionSql = "select id, item_id,minPrice,expiration from Auction";
        Statement stmt = null;
        StringBuffer query = new StringBuffer(100);
        StringBuffer condition = new StringBuffer(100);

        condition.setLength(0);
        query.append(selectAuctionSql);

        if (auction != null) {
            if (auction.getId() >= 0) {
                query.append(" where id=" + auction.getId());
            } else {
                if (auction.getMinPrice() != 0.0) {
                    condition.append(" minPrice = " + auction.getMinPrice());
                }
                if (auction.getExpiration() != null) {
                    if (condition.length() > 0) {
                        condition.append(" and");
                    }
                    condition.append(" expiration = " + auction.getExpiration());
                }
                if (auction.getItemId() != 0) {
                    if (condition.length() > 0) {
                        condition.append(" and");
                    }
                    condition.append(" item_id = " + auction.getItemId());
                }
                if (condition.length() > 0) {
                    query.append(" where ");
                    query.append(condition);
                }
            }

        }
        try {
            stmt = conn.createStatement();
            if (stmt.execute(query.toString())) {
                ResultSet r = stmt.getResultSet();
                return new AuctionIterator(r, objectModel);
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new DTException("AuctionManager.restore: Could not restore persistent Auction object; Root cause: " + e);
        }
        throw new DTException("AuctionManager.restore: Could not restore persistent Auction object");

    }

    public void delete(Auction auction) throws DTException {
        String deleteAuctionSql = "delete from Auction where id = ?";
        PreparedStatement stmt = null;
        int inscnt;

        if (!auction.isPersistent()) {
            return;
        }
        try {
            stmt = (PreparedStatement) conn.prepareStatement(deleteAuctionSql);
            stmt.setLong(1, auction.getId());
            inscnt = stmt.executeUpdate();
            if (inscnt == 1) {
                return;
            } else {
                throw new DTException("AuctionManager.delete: failed to delete an auction");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DTException("AuctionManager.delete: failed to delete an auction " + e);
        }
    }

}// AuctionManager.java
