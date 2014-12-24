package edu.uga.dawgtrades.persistence.impl;

import java.sql.Connection;
import java.util.Iterator;

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
import edu.uga.dawgtrades.persistence.Persistence;

public class PersistenceImpl
        implements Persistence {

    private MembershipManager membershipManager = null;
    private AttributeTypeManager attributeTypeManager = null;
    private AttributeManager attributeManager = null;
    private ItemManager itemManager = null;
    private RegisteredUserManager registeredUserManager = null;
    private CategoryManager categoryManager= null;
    private AuctionManager auctionManager = null;
    private BidManager bidManager = null;
    private ExperienceReportManager experienceReportManager=null;


    public PersistenceImpl(Connection conn, ObjectModel objectModel) {
        membershipManager = new MembershipManager(conn, objectModel);
        registeredUserManager = new RegisteredUserManager(conn, objectModel);
        attributeTypeManager= new AttributeTypeManager (conn, objectModel);
        attributeManager = new AttributeManager(conn, objectModel);
        itemManager = new ItemManager(conn, objectModel);
        categoryManager = new CategoryManager(conn, objectModel);
        bidManager = new BidManager(conn, objectModel);
        experienceReportManager = new ExperienceReportManager(conn, objectModel);
        auctionManager = new AuctionManager(conn, objectModel);
    }

    
    public void saveMembership(Membership membership) throws DTException {
        membershipManager.save(membership);
    }

    
    public Membership restoreMembership()
            throws DTException {
        return membershipManager.restore();
    }
    
    public Iterator<Membership> restoreMembership(Membership membership) throws DTException {
        return membershipManager.restore(membership);
    }

 
    public void deleteMembership(Membership membership) throws DTException {
        membershipManager.delete(membership);
    }

   
    public void saveAttributeType(AttributeType attributeType) throws DTException {
        attributeTypeManager.save(attributeType);
    }
    
    public Iterator<AttributeType> restoreAttributeType(AttributeType attributeType) throws DTException {
        return attributeTypeManager.restore(attributeType);
    }

    public AttributeType restoreAttributeTypebyAttribute(Attribute attribute) throws DTException {
        return attributeTypeManager.restore(attribute);
    }

    public void deleteAttributeType(AttributeType attributeType) throws DTException {
        attributeTypeManager.delete(attributeType);
    }

    public void saveAttribute(Attribute attribute) throws DTException{
        attributeManager.save(attribute);
    }
    
    public Iterator<Attribute> restoreAttribute(Attribute attribute) throws DTException{
        return attributeManager.restore(attribute);
    }

    public void deleteAttribute(Attribute attribute) throws DTException{
        attributeManager.delete(attribute);
    }
    
    public void saveItem(Item item) throws DTException {
        itemManager.save(item);
    }

    public Iterator<Item> restoreItem(Item item) throws DTException {
        return itemManager.restore(item);
    }

    public void deleteItem(Item membership) throws DTException {
        itemManager.delete(membership);
    }

    public void saveCategory(Category category) throws DTException {
        categoryManager.save(category);
    }

    public Iterator<Category> restoreCategory(Category category) throws DTException {
        return categoryManager.restore(category);
    }

    public void deleteCategory(Category category) throws DTException {
        categoryManager.delete(category);
    }

    public void saveRegisteredUser(RegisteredUser registeredUser) throws DTException {
        registeredUserManager.save(registeredUser);
    }

    public Iterator<RegisteredUser> restoreRegisteredUser(RegisteredUser registeredUser) throws DTException {
        return registeredUserManager.restore(registeredUser);
    }

    public void deleteRegisteredUser(RegisteredUser registeredUser) throws DTException {
        registeredUserManager.delete(registeredUser);
    }

    
    
    public void saveBid(Bid bid) throws DTException {
        bidManager.save(bid);
    }

    
    public Iterator<Bid> restoreBid(Bid bid) throws DTException {
        return bidManager.restore(bid);
    }

    
    public void deleteBid(Bid bid) throws DTException {
        bidManager.delete(bid);
    }

    
    public void saveAuction(Auction auction) throws DTException {
        auctionManager.save(auction);
    }

    public Iterator<Auction> restoreAuction(Auction auction) throws DTException {
        return auctionManager.restore(auction);
    }

    
    public void deleteAuction(Auction auction) throws DTException {
        auctionManager.delete(auction);
    }

    
    public void saveExperienceReport(ExperienceReport er) throws DTException {
        experienceReportManager.save(er);
    }

  
    public Iterator<ExperienceReport> restoreExperienceReport(ExperienceReport er) throws DTException {
        return experienceReportManager.restore(er);
    }

    
    public void deleteExperienceReport(ExperienceReport er) throws DTException {
        experienceReportManager.delete(er);
    }

}
