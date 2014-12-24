package edu.uga.dawgtrades.test;

import edu.uga.dawgtrades.DTException;
import edu.uga.dawgtrades.model.Auction;
import edu.uga.dawgtrades.model.Bid;
import edu.uga.dawgtrades.model.Category;
import edu.uga.dawgtrades.model.Item;
import edu.uga.dawgtrades.model.ObjectModel;
import edu.uga.dawgtrades.model.RegisteredUser;
import edu.uga.dawgtrades.model.impl.ObjectModelImpl;
import edu.uga.dawgtrades.persistence.Persistence;
import edu.uga.dawgtrades.persistence.impl.DbUtils;
import edu.uga.dawgtrades.persistence.impl.PersistenceImpl;
import java.sql.Connection;
import java.util.Iterator;

public class DeleteDowg {

    public DeleteDowg() {
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

            //Delete the Running User object
            // First: find the Running User
            Iterator<RegisteredUser> userIter = null;
            RegisteredUser runningUser = null;
            RegisteredUser modelUser = objectModel.createRegisteredUser();
            modelUser.setFirstName("Sahar");
            userIter = objectModel.findRegisteredUser(modelUser);
            while (userIter.hasNext()) {
                runningUser = userIter.next();
                System.out.println(runningUser);
            }

            //Delete the Running Bid object
            // First: find the Running Bid            
            Bid runningBid = null;
            Iterator<Bid> bidIter = null;
            Bid modelBid = objectModel.createBid();
            modelBid.setRegisteredUser(runningUser);
            bidIter = objectModel.findBid(modelBid);
            while (bidIter.hasNext()) {
                runningBid = bidIter.next();
                System.out.println(runningBid);
            }

            //Delete the Running Item object
            // First: find the Running Item
            Iterator<Item> itemIter = null;
            Item runningItem = null;
            Item modelItem = objectModel.createItem();
            modelItem.setName("saharItem");
            itemIter = objectModel.findItem(modelItem);
            while (itemIter.hasNext()) {
                runningItem = itemIter.next();
                System.out.println(runningItem);
            }

            //Delete the Running Action object
            // First: find the Running Auction
            Iterator<Auction> auctionIter = null;
            Auction runningAuction = null;
            Auction modelAuction = objectModel.createAuction();
            modelAuction.setItemId(runningItem.getId());

            auctionIter = objectModel.findAuction(modelAuction);
            while (auctionIter.hasNext()) {
                runningAuction = auctionIter.next();
                System.out.println(runningAuction);
            }

            // Delete the Running Category object
            // First: find the Running Category
            Category runningCat = null;
            Iterator<Category> catIter = null;
            Category modelCategory = objectModel.createCategory();
            modelCategory.setName("Applet");
            catIter = objectModel.findCategory(modelCategory);
            while (catIter.hasNext()) {
                runningCat = catIter.next();
                System.out.println(runningCat);
            }

            // Second: delete the Running Bid
            if (runningBid != null) {
                objectModel.deleteBid(runningBid);
                System.out.println("Deleted the Running Bid");
            } else {
                System.out.println("Failed to retrieve the Running Bid object");
            }

            // Second: delete the Running Auction
            if (runningAuction != null) {
                objectModel.deleteAuction(runningAuction);
                System.out.println("Deleted the Running Auction");
            } else {
                System.out.println("Failed to retrieve the Running Auction object");
            }

            //Second: delete the Running Item
            if (runningItem != null) {
                objectModel.deleteItem(runningItem);
                System.out.println("Deleted the Running Item");
            } else {
                System.out.println("Failed to retrieve the Running Item object");
            }

            // Second: delete the Running Category
            if (runningCat != null) {
                objectModel.deleteCategory(runningCat);
                System.out.println("Deleted the Running Category");
            } else {
                System.out.println("Failed to retrieve the Running Category object");
            }

            //Second: delete the Running User
            if (runningUser != null) {
                objectModel.deleteRegisteredUser(runningUser);
                System.out.println("Deleted the Running User");
            } else {
                System.out.println("Failed to retrieve the Running User object");
            }

        } catch (DTException ce) {
            System.err.println("ClubsException: " + ce);
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
