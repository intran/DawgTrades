package edu.uga.dawgtrades.control;

import edu.uga.dawgtrades.DTException;
import edu.uga.dawgtrades.model.Membership;
import edu.uga.dawgtrades.model.ObjectModel;
import edu.uga.dawgtrades.model.impl.ObjectModelImpl;
import edu.uga.dawgtrades.persistence.Persistence;
import edu.uga.dawgtrades.persistence.impl.DbUtils;
import edu.uga.dawgtrades.persistence.impl.PersistenceImpl;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;

public class MembershipControl {

    private Connection conn = null;
    private ObjectModel objectModel = null;
    private Persistence persistence = null;
    private String error = "Error Unknown";
    private boolean hasError = false;

    private void connect() throws DTException {

        conn = DbUtils.connect();
        objectModel = new ObjectModelImpl();
        persistence = new PersistenceImpl(conn, objectModel);
        objectModel.setPersistence(persistence);

    }

    private void close() {
        try {
            conn.close();
        } catch (Exception e) {
            System.err.println("Exception: " + e);
        }
    }

    public ArrayList<Membership> getAllMembershipPrices() throws DTException {

        Iterator<Membership> membershipIter = null;
        ArrayList<Membership> membershipMap = new ArrayList<Membership>();
        try {
            connect();
            membershipIter = objectModel.findMembership(null);
            while (membershipIter.hasNext()) {
                membershipMap.add(membershipIter.next());
            }
            return membershipMap;
        } catch (DTException e) {
            this.hasError = true;
            this.error = e.getMessage();
            return null;
        } finally {
            close();
        }
    }

    public boolean attemptToCreateMembership(double price) throws DTException {
        Membership modelMembership = null;
        try {

            connect();

            modelMembership = objectModel.createMembership(price, new Date());

            objectModel.storeMembership(modelMembership);

            return true;
        } catch (DTException e) {
            this.hasError = true;
            this.error = e.getMessage();
            return false;
        } finally {
            close();
        }

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

    public boolean hasError() {
        return this.hasError;
    }

}
