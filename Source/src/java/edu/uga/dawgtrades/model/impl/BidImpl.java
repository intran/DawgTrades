package edu.uga.dawgtrades.model.impl;

import edu.uga.dawgtrades.model.*;
import java.util.Date;

public class BidImpl extends Persistent implements Bid {

    private Auction auction;
    private RegisteredUser registereduser;
    private double amount;
    private Date date;
    private boolean isWinning;

    public BidImpl() {
    }

    public BidImpl(Auction auction, RegisteredUser registereduser, double amount) {
        this.auction = auction;
        this.registereduser = registereduser;
        this.amount = amount;
    }

    
    public Auction getAuction() {
        return this.auction;
    }

    public void setAuction(Auction auction) {
        this.auction = auction;
    }

    public double getAmount() {
        return this.amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public boolean isWinning() {
        return isWinning;
    }

    public RegisteredUser getRegisteredUser() {
        return this.registereduser;
    }

    public void setRegisteredUser(RegisteredUser registereduser) {
        this.registereduser = registereduser;
    }

    public String toString() {
        return "Bid[" + getId() + "] " + getRegisteredUser().toString() + " " + getAuction().toString() + " " + getAmount() + " " + getDate() + " " + isWinning();
    }

    public boolean equals(Object otherBid) {
        if (otherBid == null) {
            return false;
        }
        if (otherBid instanceof Bid) // name is a unique attribute
        {
            return (getRegisteredUser().equals(((Bid) otherBid).getRegisteredUser())
                    && getAuction().equals(((Bid) otherBid).getAuction())
                    && getAmount() == ((Bid) otherBid).getAmount()
                    && getDate() == ((Bid) otherBid).getDate()
                    && isWinning() == ((Bid) otherBid).isWinning());
        }
        return false;
    }
}
