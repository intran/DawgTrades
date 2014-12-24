package edu.uga.dawgtrades.model;

import edu.uga.dawgtrades.DTException;
import edu.uga.dawgtrades.persistence.Persistence;
import java.util.Iterator;

public interface ObjectModel {

    public void setPersistence(Persistence persistence);

    // Category
    public Category createCategory(Category parent, String name) throws DTException;   

    public Category createCategory();

    public java.util.Iterator<Category> findCategory(Category modelCategory) throws DTException;

    public void storeCategory(Category category) throws DTException;

    public void deleteCategory(Category category) throws DTException;

    public Category getParent(Category category) throws DTException;

    public java.util.Iterator<Category> getChild(Category category) throws DTException;

    public Category getCategory(AttributeType attributeType) throws DTException;

    public Category getCategory(Item item) throws DTException;

    //Attribute Type
    public AttributeType createAttributeType(Category category, String name, boolean isString) throws DTException;

    public AttributeType createAttributeType();

    public void storeAttributeType(AttributeType attributeType) throws DTException;

    public void deleteAttributeType(AttributeType attributeType) throws DTException;

    public java.util.Iterator<AttributeType> findAttributeType(AttributeType modelAttributeType) throws DTException;

    public java.util.Iterator<AttributeType> getAttributeType(Category category) throws DTException;

    public AttributeType getAttributeType(Attribute attribute) throws DTException;

    //Item
    public Item createItem(Category category, RegisteredUser user, String name, String description) throws DTException;

    public Item createItem();

    public java.util.Iterator<Item> findItem(Item modelItem) throws DTException;

    public void storeItem(Item item) throws DTException;

    public void deleteItem(Item item) throws DTException;

    public java.util.Iterator<Item> getItem(Category category) throws DTException;

    public Item getItem(Attribute attribute) throws DTException;

    public java.util.Iterator<Item> getItem(RegisteredUser registeredUser) throws DTException;

    public Item getItem(Auction auction) throws DTException;

    //Attribute
    public Attribute createAttribute(AttributeType attributeType, Item item, String value) throws DTException;

    public Attribute createAttribute();

    public void storeAttribute(Attribute attribute) throws DTException;

    public void deleteAttribute(Attribute attribute) throws DTException;

    public java.util.Iterator<Attribute> getAttribute(Item item) throws DTException;

    //Auction
    public Auction createAuction(Item item, double minPrice, java.util.Date expiration) throws DTException;

    public Auction createAuction();

    public java.util.Iterator<Auction> findAuction(Auction modelAuction) throws DTException;

    public void storeAuction(Auction auction) throws DTException;

    public void deleteAuction(Auction auction) throws DTException;

    public Auction getAuction(Item item) throws DTException;

    //Registered User
    public RegisteredUser createRegisteredUser(String firstName, String lastName, String name, String password,
            String email, String phone, boolean canText, boolean isAdmin, boolean isApproved) throws DTException;

    public RegisteredUser createRegisteredUser();

    public Iterator<RegisteredUser> findRegisteredUser(RegisteredUser registeredUser)throws DTException;
    
    public void storeRegisteredUser(RegisteredUser registeredUser) throws DTException;
    
    public void deleteRegisteredUser(RegisteredUser registeredUser) throws DTException;
     
    public RegisteredUser getRegisteredUser(Item item) throws DTException;
    
    

    //Bid
    public Bid createBid(Auction auction, RegisteredUser user, double price) throws DTException;

    public Bid createBid();

    public java.util.Iterator<Bid> findBid(Bid modelBid) throws DTException;

    public void storeBid(Bid bid) throws DTException;

    public void deleteBid(Bid bid) throws DTException;

    //Experience Report
    public ExperienceReport createExperienceReport();
    
    public ExperienceReport createExperienceReport(RegisteredUser reviewer, RegisteredUser reviewed,
            int rating, String report, java.util.Date date) throws DTException;
    
    public Iterator<ExperienceReport> findExperienceReport(ExperienceReport modelER) throws DTException;

    public void storeExperienceReport(ExperienceReport experienceReport) throws DTException;

    public void deleteExperienceReport(ExperienceReport experienceReport) throws DTException;

    //Membership
    public Membership createMembership(double price, java.util.Date date) throws DTException;

    public Membership createMembership();

    public Membership findMembership() throws DTException;
    
    public Iterator<Membership> findMembership(Membership membership) throws DTException;

    public void storeMembership(Membership membership) throws DTException;
    
    public void deleteMembership(Membership membership) throws DTException;

}
