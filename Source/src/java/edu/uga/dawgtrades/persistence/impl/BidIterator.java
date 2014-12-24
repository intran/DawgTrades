package edu.uga.dawgtrades.persistence.impl;

import java.sql.ResultSet;
import java.util.Date;
import java.util.Iterator;
import java.util.NoSuchElementException;

import edu.uga.dawgtrades.DTException;
import edu.uga.dawgtrades.model.Bid;
import edu.uga.dawgtrades.model.Auction;
import edu.uga.dawgtrades.model.ObjectModel;
import edu.uga.dawgtrades.model.impl.BidImpl;
import edu.uga.dawgtrades.model.impl.AuctionImpl;
import edu.uga.dawgtrades.model.RegisteredUser;
import edu.uga.dawgtrades.model.impl.RegisteredUserImpl;

public class BidIterator implements Iterator<Bid> {

    private ResultSet rs = null;
    private ObjectModel objectModel = null;
    private boolean more = false;

    public BidIterator(ResultSet rs, ObjectModel objectModel)
            throws DTException {

        this.rs = rs;
        this.objectModel = objectModel;

        try {
            more = rs.first();
        } catch (Exception e) {
            throw new DTException("BidIterator: Cannot create bid iterator; root cause: " + e);
        }

    }

    public boolean hasNext() {
        return more;
    }

    public Bid next() {
        //result: (long)id, (long)user_id,(long)auction_id,(float)amount,date(date)
        Bid bid = new BidImpl();
        
        
        if (more) {
            try {

                RegisteredUser user = new RegisteredUserImpl();
                user.setId(rs.getLong(2));
                bid.setRegisteredUser(user);
                Auction auction = new AuctionImpl();
                auction.setId(rs.getLong(3));
                bid.setAuction(auction);
                bid.setAmount(rs.getDouble(4));
                java.sql.Timestamp timestamp = rs.getTimestamp(5);
                if(timestamp == null) {
                    throw new DTException("BidIterator: Failed to retrieve timestamp from DB.");
                }
                bid.setDate(new java.util.Date(timestamp.getTime()));
                bid.setId(rs.getLong(1));
                more = rs.next();
                return bid;

            } catch (Exception e) {
                throw new NoSuchElementException("BidIterator: No next Bid; Root Cause: " + e);
            }
        }
        //temp return statement
        return null;
    }

    public void remove() {
        throw new UnsupportedOperationException();
    }

};
