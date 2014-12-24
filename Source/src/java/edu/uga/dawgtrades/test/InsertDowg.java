package edu.uga.dawgtrades.test;

import edu.uga.dawgtrades.DTException;
import edu.uga.dawgtrades.model.Attribute;
import edu.uga.dawgtrades.model.AttributeType;
import edu.uga.dawgtrades.model.Auction;
import edu.uga.dawgtrades.model.Bid;
import edu.uga.dawgtrades.model.Category;
import edu.uga.dawgtrades.model.ExperienceReport;
import edu.uga.dawgtrades.model.Item;
import edu.uga.dawgtrades.model.Membership;
import edu.uga.dawgtrades.model.ObjectModel;
import edu.uga.dawgtrades.model.RegisteredUser;
import edu.uga.dawgtrades.model.impl.ObjectModelImpl;
import edu.uga.dawgtrades.persistence.Persistence;
import edu.uga.dawgtrades.persistence.impl.DbUtils;
import edu.uga.dawgtrades.persistence.impl.PersistenceImpl;
import java.sql.Connection;
import java.util.Date;

/**
 *
 * @author sahar
 */
public class InsertDowg {

    public InsertDowg() {

        Connection conn = null;
        ObjectModel objectModel = null;
        Persistence persistence = null;

        RegisteredUser sahar;
        RegisteredUser Daniel;
        Category tabletCat;
        Category appleCat;
        AttributeType modelAttType;
        AttributeType connectivityAttType;
        Item sahatTab;
        Attribute iPadAtt;
        Attribute verivonAtt;
        Auction action;
        Bid bid;
        ExperienceReport experienceReport;
        Membership membership;

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

            //   create a few user
            sahar = objectModel.createRegisteredUser("sahar", "voghoei", "sahr", "ss", "joe@mail.com", "23567895", false, false, true);
            objectModel.storeRegisteredUser(sahar);
            System.out.println("Entity objects User Sahar created and saved.");

            Daniel = objectModel.createRegisteredUser("daniel", "last", "san", "di", "sa@mail.com", "23567895", false, false, true);
            objectModel.storeRegisteredUser(Daniel);
            System.out.println("Entity objects User Daniel created and saved.");

            //Category
            tabletCat = objectModel.createCategory(null, "Tablet");
            objectModel.storeCategory(tabletCat);
            System.out.println("Entity objects Category Tablet created and saved.");

            appleCat = objectModel.createCategory(tabletCat, "Applet");
            objectModel.storeCategory(appleCat);
            System.out.println("Entity objects Category Applet created and saved.");

            //Item
            sahatTab = objectModel.createItem(tabletCat, sahar, "saharItem", null);
            objectModel.storeItem(sahatTab);
            System.out.println("Entity objects Category sahar'Item created and saved.");

            //AttributeType
            modelAttType = objectModel.createAttributeType(appleCat, "Model", true);
            objectModel.storeAttributeType(modelAttType);
            System.out.println("Entity objects AttType Model  created and saved.");

            connectivityAttType = objectModel.createAttributeType(appleCat, "Connectivity", true);
            objectModel.storeAttributeType(connectivityAttType);
            System.out.println("Entity objects AttType Connectivity  created and saved.");

            //Attribute
            iPadAtt = objectModel.createAttribute(modelAttType, sahatTab, "ipad");
            objectModel.storeAttribute(iPadAtt);
            System.out.println("Entity objects Att ipad  created and saved.");

            verivonAtt = objectModel.createAttribute(connectivityAttType, sahatTab, "Verizon");
            objectModel.storeAttribute(verivonAtt);
            System.out.println("Entity objects Att Verivon  created and saved.");

            //Auction
            action = objectModel.createAuction(sahatTab, 20, new Date());
            objectModel.storeAuction(action);
            System.out.println("Entity objects Auction created and saved.");

            //Bid
            bid = objectModel.createBid(action, sahar, 36);
            objectModel.storeBid(bid);
            System.out.println("Entity objects Bid created and saved.");

            //ExperienceReport
            experienceReport = objectModel.createExperienceReport(sahar, Daniel, 2, "report", new Date());
            objectModel.storeExperienceReport(experienceReport);
            System.out.println("Entity objects ExperienceReport created and saved.");

            // create a membership
            membership = objectModel.createMembership(10, new Date());
            objectModel.storeMembership(membership);
            System.out.println("Entity objects membership created and saved.");

        } catch (DTException ce) {
            System.err.println("Exception: " + ce);
            ce.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            // close the connection
            try {
                conn.close();
            } catch (Exception e) {
                System.err.println("Exception: " + e);
                e.printStackTrace();
            }
        }
    }

}
