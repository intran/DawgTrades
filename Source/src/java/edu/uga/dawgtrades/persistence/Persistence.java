package edu.uga.dawgtrades.persistence;

import java.util.Iterator;

import edu.uga.dawgtrades.DTException;
import edu.uga.dawgtrades.model.Attribute;
import edu.uga.dawgtrades.model.AttributeType;
import edu.uga.dawgtrades.model.Category;
import edu.uga.dawgtrades.model.Item;
import edu.uga.dawgtrades.model.Membership;
import edu.uga.dawgtrades.model.RegisteredUser;
import edu.uga.dawgtrades.model.Auction;
import edu.uga.dawgtrades.model.Bid;
import edu.uga.dawgtrades.model.ExperienceReport;

public interface Persistence {

    //Membership
    public void saveMembership(Membership membership) throws DTException;

    public Membership restoreMembership() throws DTException;
    
    public Iterator<Membership> restoreMembership(Membership membership) throws DTException;

    public void deleteMembership(Membership membership) throws DTException;

    // AttributeType
    public void saveAttributeType(AttributeType attributeType) throws DTException;

    public Iterator<AttributeType> restoreAttributeType(AttributeType attributeType) throws DTException;

    public AttributeType restoreAttributeTypebyAttribute(Attribute attribute) throws DTException;

    public void deleteAttributeType(AttributeType attributeType) throws DTException;

    //Attribute
    public void saveAttribute(Attribute attribute) throws DTException;
    
    public Iterator<Attribute> restoreAttribute(Attribute attribute) throws DTException;

    public void deleteAttribute(Attribute attribute) throws DTException;

    //Item
    public void saveItem(Item item) throws DTException;

    public Iterator<Item> restoreItem(Item item) throws DTException;

    public void deleteItem(Item item) throws DTException;

    // Category
    public void saveCategory(Category category) throws DTException;

    public Iterator<Category> restoreCategory(Category category) throws DTException;

    public void deleteCategory(Category category) throws DTException;

    // RegisteredUser
    public void saveRegisteredUser(RegisteredUser registeredUser) throws DTException;

    public Iterator<RegisteredUser> restoreRegisteredUser(RegisteredUser registeredUser) throws DTException;

    public void deleteRegisteredUser(RegisteredUser registeredUser) throws DTException;

    //Bid
    public void saveBid(Bid bid) throws DTException;

    public Iterator<Bid> restoreBid(Bid bid) throws DTException;

    public void deleteBid(Bid bid) throws DTException;

    //Auction
    public void saveAuction(Auction auction) throws DTException;

    public Iterator<Auction> restoreAuction(Auction auction) throws DTException;

    public void deleteAuction(Auction auction) throws DTException;

    //ExperienceReport
    public void saveExperienceReport(ExperienceReport er) throws DTException;

    public Iterator<ExperienceReport> restoreExperienceReport(ExperienceReport er) throws DTException;

    public void deleteExperienceReport(ExperienceReport er) throws DTException;
};
