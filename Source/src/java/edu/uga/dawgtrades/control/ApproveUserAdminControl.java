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
public class ApproveUserAdminControl {

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

    public ArrayList<RegisteredUser> getUnapprovedUsers() {
        ArrayList<RegisteredUser> out = new ArrayList<RegisteredUser>();
        try {
            this.connect();
            // Just grab all users
            RegisteredUser modelUser = this.objectModel.createRegisteredUser();
            Iterator<RegisteredUser> userIter = this.objectModel.findRegisteredUser(modelUser);
            while (userIter.hasNext()) {
                RegisteredUser user = userIter.next();

                // Put in unapproved ones
                if(!user.getIsApproved()) {
                    out.add(user);
                }
            }

            // Return list
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

    public boolean approve(long id) {
        try {
            this.connect();

            // Grab user from DB
            RegisteredUser modelUser = this.objectModel.createRegisteredUser();
            modelUser.setId(id);
            Iterator<RegisteredUser> userIter = this.objectModel.findRegisteredUser(modelUser);
            if (userIter.hasNext()) {
                // Got user
                RegisteredUser user = userIter.next();

                // Set approval
                user.setIsApproved(true);

                // Save
                this.objectModel.storeRegisteredUser(user);
                return true;
            }

            // otherwise, return error
            this.hasError = true;
            this.error = "User not found.";
            return false;
        }
        catch(DTException e) {
            this.hasError = true;
            this.error = e.getMessage();
            return false;
        }
        finally {
            this.close();
        }
    }

}
