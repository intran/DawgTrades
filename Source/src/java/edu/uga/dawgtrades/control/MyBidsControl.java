package edu.uga.dawgtrades.control;
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
import edu.uga.dawgtrades.DTException;
import edu.uga.dawgtrades.control.LoginControl;
import edu.uga.dawgtrades.model.*;
import edu.uga.dawgtrades.model.impl.ObjectModelImpl;
import edu.uga.dawgtrades.persistence.Persistence;
import edu.uga.dawgtrades.persistence.impl.DbUtils;
import edu.uga.dawgtrades.persistence.impl.PersistenceImpl;
import edu.uga.dawgtrades.persistence.impl.ItemIterator;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.util.*;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpSession;

/**
 *
 * @author reanimus
 */
public class MyBidsControl {

    private Connection conn = null;
    private ObjectModel objectModel = null;
    private Persistence persistence = null;
    private boolean hasError = false;
    private String error;

    public boolean hasError() {
        return this.hasError;
    }

    public String getError() {
        String err = null;
        if (this.hasError) {
            err = this.error;
            this.error = null;
            this.hasError = false;
        }
        return err;
    }

    private void connect() throws DTException{
            this.close();
            this.conn = DbUtils.connect();
            this.objectModel = new ObjectModelImpl();
            this.persistence = new PersistenceImpl(conn,objectModel);
            this.objectModel.setPersistence(persistence);
        
    }
    private void close(){
        try{
            if(this.conn != null) {
                this.conn.close();
            }

        }catch (Exception e){
            System.err.println("Exception: "+e);
        }
    }

    public ArrayList<Bid> getBidsForID(long id) {
        ArrayList<Bid> out = new ArrayList<Bid>();
        try {
            this.connect();
            Bid model = this.objectModel.createBid();
            RegisteredUser user = this.objectModel.createRegisteredUser();
            user.setId(id);
            model.setRegisteredUser(user);
            Iterator<Bid> results = this.objectModel.findBid(model);
            HashMap<Long, Bid> bidMap = new HashMap<Long, Bid>();
            while (results.hasNext()) {
                model = results.next();
                Long auctionID = new Long(model.getAuction().getId());
                if(bidMap.get(auctionID) != null) {
                    Bid candidate = bidMap.get(auctionID);
                    if(candidate.getAmount() < model.getAmount()) {
                        bidMap.put(auctionID, model);
                    }
                }else{
                    bidMap.put(auctionID, model);
                }
            }
            for(Bid bid : bidMap.values()) {
                out.add(bid);
            }
            return out;
        }
        catch(DTException e) {
            this.hasError = true;
            this.error = e.getMessage();
            return null;
        }
        finally {
            this.close();
        }
    }

    public ArrayList<Auction> getAuctionsForBids(ArrayList<Bid> bids) {
        ArrayList<Auction> out = new ArrayList<Auction>();
        try {
            this.connect();
            for(Bid bid : bids) {
                Auction model = bid.getAuction();
                Iterator<Auction> results = this.objectModel.findAuction(model);
                if(results.hasNext()) {
                    out.add(results.next());
                }
            }
            return out;
        }
        catch(DTException e) {
            this.hasError = true;
            this.error = e.getMessage();
            return null;
        }
        finally {
            this.close();
        }

    }

}
