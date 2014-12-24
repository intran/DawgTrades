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
public class BidControl {

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

    public boolean placeBid(long auctionID, double amount, long userID) {
        AuctionControl auctionCtrl = new AuctionControl();
        Auction auction = auctionCtrl.getAuctionWithID(auctionID);
        if(auction != null) {
            try {
                this.connect();
                RegisteredUser user = this.objectModel.createRegisteredUser();
                user.setId(userID);
                Iterator<RegisteredUser> results = this.objectModel.findRegisteredUser(user);
                if (results.hasNext()) {
                    user = results.next();
                    Bid bid = this.objectModel.createBid(auction, user, amount);
                    this.objectModel.storeBid(bid);
                    return true;
                }else{
                    this.hasError = true;
                    this.error = "User placing bid is invalid.";
                    return false;
                }
            }
            catch(DTException e) {
                this.hasError = true;
                this.error = e.getMessage();
                return false;
            }
            finally {
                this.close();
            }
        }else{
            if(auctionCtrl.hasError()) {
                this.hasError = true;
                this.error = auctionCtrl.getError();
            }
        }
        return false;
    }

}
