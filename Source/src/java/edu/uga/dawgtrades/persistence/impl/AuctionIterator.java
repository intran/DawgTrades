package edu.uga.dawgtrades.persistence.impl;

import java.sql.ResultSet;
import java.util.*;

import edu.uga.dawgtrades.DTException;
import edu.uga.dawgtrades.model.Auction;
import edu.uga.dawgtrades.model.ObjectModel;
import edu.uga.dawgtrades.model.impl.AuctionImpl;

public class AuctionIterator implements Iterator<Auction> {

    private ResultSet rs = null;
    private ObjectModel objectModel = null;
    private boolean more = false;

    public AuctionIterator(ResultSet rs, ObjectModel objectModel)
            throws DTException {

        this.rs = rs;
        this.objectModel = objectModel;

        try {
            more = rs.first();
        } catch (Exception e) {
            throw new DTException("AuctionIterator: Cannot create Auction iterator; root cause: " + e);
        }

    }

    public boolean hasNext() {
        return more;
    }

    public Auction next() {
        //result: id(long), item_id(long),minPrice(double),expiration(date),isClosed(boolean)
        Auction auction = new AuctionImpl();

        if (more) {
            try {
                auction.setId(rs.getLong(1));
                auction.setItemId(rs.getLong(2));
                auction.setMinPrice(rs.getDouble(3));
                java.sql.Timestamp timestamp = rs.getTimestamp(4);
                if(timestamp == null) {
                    throw new DTException("AuctionIterator: Failed to retrieve expiration from DB.");
                }
                auction.setExpiration(new java.util.Date(timestamp.getTime()));
                more = rs.next();
                return auction;
            } catch (Exception e) {
                throw new NoSuchElementException("AuctionIterator: No next Auction; Root Cause: " + e);
            }
        }
        //temp return statement
        return null;
    }

    public void remove() {
        throw new UnsupportedOperationException();
    }

};
