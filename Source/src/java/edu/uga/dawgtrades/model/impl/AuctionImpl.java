package edu.uga.dawgtrades.model.impl;

import edu.uga.dawgtrades.model.*;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

public class AuctionImpl extends Persistent implements Auction {

    private long itemId;
    private long userId;
    private double minPrice;
    private Date expiration;
    private double SellingPrice;

    public AuctionImpl() {
    }

    public AuctionImpl(long itemId, double minPrice, Date expiration) {
        this.itemId = itemId;
        this.minPrice = minPrice;
        this.expiration = expiration;
    }

   
    public long getItemId() {
        return this.itemId;
    }

    public void setItemId(long itemId) {
        this.itemId = itemId;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public double getMinPrice() {
        return this.minPrice;
    }

    public void setMinPrice(double minPrice) {
        this.minPrice = minPrice;
    }

    public Date getExpiration() {
        return this.expiration;
    }

    public void setExpiration(Date expiration) {
        this.expiration = expiration;
    }

    public boolean getIsClosed() {
        Date now = new Date();
        return now.getTime() >= this.expiration.getTime();
    }

    public void setIsClosed(boolean isClosed) {
    }

    public double getSellingPrice() {
        return SellingPrice;
    }

    public void setSellingPrice(double SellingPrice) {
        this.SellingPrice = SellingPrice;
    }

    public String toString() {
        return "Auction[" + getId() + "] " + getUserId() + " " + getItemId() + " " + getMinPrice() + " " + getExpiration() + " " + getIsClosed() + " " + getSellingPrice();
    }

    public boolean equals(Object otherAuction) {
        if (otherAuction == null) {
            return false;
        }
        if (otherAuction instanceof Auction) // name is a unique attribute
        {
            return (getMinPrice() == ((Auction) otherAuction).getMinPrice()
                    && getExpiration().equals(((Auction) otherAuction).getExpiration())
                    && getItemId() == ((Auction) otherAuction).getItemId()
                    && getSellingPrice() == ((Auction) otherAuction).getSellingPrice()
                    && getIsClosed() == ((Auction) otherAuction).getIsClosed());
        }
        return false;
    }
}
