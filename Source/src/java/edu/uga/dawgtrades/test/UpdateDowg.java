/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.uga.dawgtrades.test;

import edu.uga.dawgtrades.DTException;
import edu.uga.dawgtrades.model.Item;
import edu.uga.dawgtrades.model.ObjectModel;
import edu.uga.dawgtrades.model.RegisteredUser;
import edu.uga.dawgtrades.model.impl.ObjectModelImpl;
import edu.uga.dawgtrades.persistence.Persistence;
import edu.uga.dawgtrades.persistence.impl.DbUtils;
import edu.uga.dawgtrades.persistence.impl.PersistenceImpl;
import java.sql.Connection;
import java.util.Iterator;

public class UpdateDowg {

    public UpdateDowg() {
        Connection conn = null;
        ObjectModel objectModel = null;
        Persistence persistence = null;

        // get a database connection
        try {
            conn = DbUtils.connect();
        } catch (Exception seq) {
            System.err.println("ObjectModelDelete: Unable to obtain a database connection");
        }

        // obtain a reference to the ObjectModel module      
        objectModel = new ObjectModelImpl();

        // obtain a reference to Persistence module and connect it to the ObjectModel        
        persistence = new PersistenceImpl(conn, objectModel);

        // connect the ObjectModel module to the Persistence module
        objectModel.setPersistence(persistence);

        try {

            // modify the name of the "Sahar" club to "Arash"
            // First: locate the Sahar club
            RegisteredUser saharUser = null;
            Iterator<RegisteredUser> userIter = null;
            RegisteredUser modelUser = objectModel.createRegisteredUser();
            modelUser.setName("sahr");
            userIter = objectModel.findRegisteredUser(modelUser);
            while (userIter.hasNext()) {
                saharUser = userIter.next();
                System.out.println("sahar User is updated.  " + saharUser);
            }

            // modify the name of the "Sahar" club to "Arash"
            // First: locate the Sahar club
            Item firtItem = null;
            Iterator<Item> itemIter = null;
            Item modelItem = objectModel.createItem();
            modelItem.setName("saharItem");
            itemIter = objectModel.findItem(modelItem);
            while (itemIter.hasNext()) {
                firtItem = itemIter.next();
                System.out.println("sahar'item Item is updated.  " + firtItem);
            }

        } catch (DTException ce) {
            System.err.println("Exception: " + ce);
        } catch (Exception e) {
            System.err.println("Exception: " + e);
        } finally {
            // close the connection
            try {
                conn.close();
            } catch (Exception e) {
                System.err.println("Exception: " + e);
            }
        }
    }

}
