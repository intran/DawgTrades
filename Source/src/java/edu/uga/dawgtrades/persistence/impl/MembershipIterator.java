package edu.uga.dawgtrades.persistence.impl;

import java.sql.ResultSet;
import java.util.Date;
import java.util.Iterator;
import java.util.NoSuchElementException;

import edu.uga.dawgtrades.DTException;
import edu.uga.dawgtrades.model.Membership;
import edu.uga.dawgtrades.model.ObjectModel;

public class MembershipIterator implements Iterator<Membership> {

    private ResultSet rs = null;
    private ObjectModel objectModel = null;
    private boolean more = false;

    public MembershipIterator(ResultSet rs, ObjectModel objectModel)
            throws DTException {
        this.rs = rs;
        this.objectModel = objectModel;
        try {
            more = rs.next();
        } catch (Exception e) {  // just in case...
            throw new DTException("MembershipIterator: Cannot create Membership iterator; root cause: " + e);
        }
    }

    public boolean hasNext() {
        return more;
    }

    public Membership next() {
        long id;
        Date date;
        double price;
        Membership membership = null;

        if (more) {

            try {
                id = rs.getLong(1);
                price = rs.getDouble(2);
                java.sql.Timestamp timestamp = rs.getTimestamp(3);
                if(timestamp == null) {
                    throw new DTException("AuctionIterator: Failed to retrieve expiration from DB.");
                }
                date = new java.util.Date(timestamp.getTime());

                more = rs.next();

            } catch (Exception e) {      // just in case...
                throw new NoSuchElementException("MembershipIterator: No next Membership object; root cause: " + e);
            }

            try {
                membership = objectModel.createMembership(price, date);
                membership.setId(id);

            } catch (Exception ce) {
                // safe to ignore: we explicitly set the persistent id of the founder Person object above!
            }

            return membership;
        } else {
            throw new NoSuchElementException("PersonIterator: No next Membership object");
        }
    }

    public void remove() {
        throw new UnsupportedOperationException();
    }

};
