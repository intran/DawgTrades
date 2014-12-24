package edu.uga.dawgtrades.persistence.impl;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import edu.uga.dawgtrades.DTException;
import edu.uga.dawgtrades.model.Membership;
import edu.uga.dawgtrades.model.ObjectModel;
import edu.uga.dawgtrades.model.RegisteredUser;
import java.sql.PreparedStatement;
import java.util.Iterator;


class MembershipManager {

    private ObjectModel objectModel = null;
    private Connection conn = null;

    public MembershipManager(Connection conn, ObjectModel objectModel) {
        this.conn = conn;
        this.objectModel = objectModel;
    }

    public void save(Membership membership)
            throws DTException {
        String insertMembershipSql = "insert into Membership (price, date ) values ( ?, ? )";
        String updateMembershipSql = "update Membership set price = ?, date = ? where id = ?";
        PreparedStatement stmt = null;
        int inscnt;
        long membershipId;
        java.util.Date mDate = null;

        try {

            if (!membership.isPersistent()) {
                stmt = (PreparedStatement) conn.prepareStatement(insertMembershipSql);
            } else {
                stmt = (PreparedStatement) conn.prepareStatement(updateMembershipSql);
            }

            if (membership.getPrice() != 0.0) // price is unique and non null
            {
                stmt.setDouble(1, membership.getPrice());
            } else {
                throw new DTException("MembershipManager.save: can't save a Membership: price undefined");
            }

            if (membership.getDate() != null) {
                mDate = membership.getDate();
                java.sql.Timestamp ts = new java.sql.Timestamp(mDate.getTime());
                stmt.setTimestamp(2, ts);
            } else {
                stmt.setNull(2, java.sql.Types.TIMESTAMP);
            }

            inscnt = stmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
            throw new DTException("MembershipManager.save: failed to save a Membership: " + e);
        }
    }

    public Membership restore()
            throws DTException {
        String selectMembershipSql = "SELECT id, price, date FROM Membership WHERE date = (SELECT Max(date) FROM Membership)";
        Statement stmt = null;
        Membership membership = null;

        try {

            stmt = conn.createStatement();

            if (stmt.execute(selectMembershipSql)) {
                ResultSet r = stmt.getResultSet();
                r.next();
                membership = objectModel.createMembership(r.getDouble(2), r.getDate(3));
                membership.setId(r.getLong(1));
                return membership;
            }
        } catch (Exception e) {      // just in case...
            e.printStackTrace();
            throw new DTException("MembershipManager.restore: Could not restore persistent Membership object; Root cause: " + e);
        }

        throw new DTException("MembershipManager.restore: Could not restore persistent Membership object");
    }

    public Iterator<Membership> restore(Membership membership) throws DTException {
        String selectMembershipSql = "select id, price, date from Membership order by id DESC";
        Statement stmt = null;
        StringBuffer query = new StringBuffer(100);
        StringBuffer condition = new StringBuffer(100);

        condition.setLength(0);

        // form the query based on the given Person object instance
        query.append(selectMembershipSql);

        if (membership != null) {
            if (membership.getId() >= 0) // id is unique, so it is sufficient to get a person
            {
                query.append(" where id = " + membership.getId());
            } else {
                if (membership.getDate() != null) // userName is unique, so it is sufficient to get a person
                {
                    condition.append(" date = " + membership.getDate());
                }
                if (membership.getDate() != null) // userName is unique, so it is sufficient to get a person
                {
                    if (condition.length() > 0) {
                            condition.append(" and");
                        }
                    condition.append(" date = " + membership.getDate());
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
                return new MembershipIterator(r, objectModel);
            }
        } catch (Exception e) {      // just in case...
            e.printStackTrace();
            throw new DTException("MembershipManager.restore: Could not restore persistent Membership object; Root cause: " + e);
        }

        throw new DTException("MembershipManager.restore: Could not restore persistent Membership object");
    }
    
    public void delete(Membership membership)
            throws DTException {
        String deleteMembershipSql = "delete from Membership where id = ?";
        PreparedStatement stmt = null;
        int inscnt;

        if (!membership.isPersistent()) // is the Club object persistent?  If not, nothing to actually delete
        {
            return;
        }

        try {
            stmt = (PreparedStatement) conn.prepareStatement(deleteMembershipSql);
            stmt.setLong(1, membership.getId());
            inscnt = stmt.executeUpdate();
            if (inscnt == 1) {
                return;
            } else {
                throw new DTException("MembershipManager.delete: failed to delete a Club");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DTException("MembershipManager.delete: failed to delete a Club: " + e);
        }
    }

}
