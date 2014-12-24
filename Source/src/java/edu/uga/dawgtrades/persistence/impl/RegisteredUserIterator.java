package edu.uga.dawgtrades.persistence.impl;

import java.sql.ResultSet;
import java.util.Date;
import java.util.Iterator;
import java.util.NoSuchElementException;

import edu.uga.dawgtrades.DTException;
import edu.uga.dawgtrades.model.RegisteredUser;
import edu.uga.dawgtrades.model.ObjectModel;

public class RegisteredUserIterator implements Iterator<RegisteredUser> {

    private ResultSet rs = null;
    private ObjectModel objectModel = null;
    private boolean more = false;

    public RegisteredUserIterator(ResultSet rs, ObjectModel objectModel)
            throws DTException {
        this.rs = rs;
        this.objectModel = objectModel;
        try {
            more = rs.next();
        } catch (Exception e) {	// just in case...
            throw new DTException("RegisteredUserIterator: Cannot create RegisteredUser iterator; root cause: " + e);
        }
    }

    public boolean hasNext() {
        return more;
    }

    public RegisteredUser next() {
        long id;
        String Name;
        String FirstName;
        String LastName;
        String Password;
        boolean IsAdmin;
        String Email;
        String Phone;
        boolean CanText;
        boolean IsApproved;

        if (more) {

            try {
                id = rs.getLong(1);
                Name = rs.getString(4);
                FirstName = rs.getString(2);
                LastName = rs.getString(3);
                Password = rs.getString(5);
                IsAdmin = rs.getBoolean(9);
                Email = rs.getString(6);
                Phone = rs.getString(7);
                CanText = rs.getBoolean(8);
                IsApproved = rs.getBoolean(10);

                more = rs.next();
            } catch (Exception e) {	// just in case...
                throw new NoSuchElementException("RegisteredUserIterator: No next RegisteredUser object; root cause: " + e);
            }

            RegisteredUser registeredUser = null;
            try {
                registeredUser = objectModel.createRegisteredUser(FirstName, LastName, Name, Password, Email, Phone, CanText, IsAdmin, IsApproved);
                registeredUser.setPasswordDirect(Password);
                registeredUser.setId(id);

            } catch (DTException ce) {
                ce.printStackTrace();
                System.out.println(ce);
            }
            return registeredUser;
        } else {
            throw new NoSuchElementException("RegisteredUserIterator: No next RegisteredUser object");
        }
    }

    public void remove() {
        throw new UnsupportedOperationException();
    }
}
