package edu.uga.dawgtrades.model.impl;


import java.util.Date;
import java.util.Iterator;

import edu.uga.dawgtrades.DTException;
import edu.uga.dawgtrades.model.Membership;
import edu.uga.dawgtrades.model.ObjectModel;
import edu.uga.dawgtrades.persistence.Persistence;

import edu.uga.dawgtrades.model.*;
import edu.uga.dawgtrades.model.impl.*;

public class ObjectModelImpl extends Persistent implements ObjectModel {

    Persistence persistence = null;

    public void setPersistence(Persistence persistence) {
        this.persistence = persistence;
    }

    public ObjectModelImpl() {
        this.persistence = null;
    }

    public ObjectModelImpl(Persistence persistence) {
        this.persistence = persistence;
    }

    //Membership  -----------------------------------------------------------------------------------------------
    public Membership createMembership() {
        Membership membership = new MembershipImpl(0, null);
        membership.setId(-1);
        return membership;
    }

    public Membership createMembership(double price, Date date) throws DTException {
        Membership membership = new MembershipImpl(price, date);
        return membership;
    }

    public Membership findMembership() throws DTException {
        return persistence.restoreMembership();
    }
    
    public Iterator<Membership> findMembership(Membership membership) throws DTException {
        return persistence.restoreMembership(membership);
    }

    public void storeMembership(Membership membership) throws DTException {
        persistence.saveMembership(membership);
    }

    public void deleteMembership(Membership membership) throws DTException {
        persistence.deleteMembership(membership);
    }

    // Attribute Type --------------------------------------------------------------------------------------------
    public AttributeType createAttributeType(Category category, String name, boolean isString) throws DTException {
        AttributeType attributeType = null;
        if (category.isPersistent()) {
            attributeType = new AttributeTypeImpl(category.getId(), name, isString);
        }
        return attributeType;
    }

    public AttributeType createAttributeType() {
        AttributeType attributeType = new AttributeTypeImpl();
        attributeType.setId(-1);
        return attributeType;
    }

    public void storeAttributeType(AttributeType attributeType) throws DTException {
        persistence.saveAttributeType(attributeType);
    }

    public void deleteAttributeType(AttributeType attributeType) throws DTException {
        persistence.deleteAttributeType(attributeType);
    }

    public java.util.Iterator<AttributeType> findAttributeType(AttributeType modelAttributeType) throws DTException {
        return persistence.restoreAttributeType(modelAttributeType);
    }


    public Iterator<AttributeType> getAttributeType(Category category) throws DTException {
        AttributeType attr = this.createAttributeType();
        attr.setCategoryId(category.getId());
        return persistence.restoreAttributeType(attr);
    }

    public AttributeType getAttributeType(Attribute attribute) throws DTException {
        return persistence.restoreAttributeTypebyAttribute(attribute);
    }

    //Attribute
    public Attribute createAttribute(AttributeType attributeType, Item item, String value) throws DTException {
        Attribute attribute = null;
        if (attributeType.isPersistent() && item.isPersistent()) {
            attribute = new AttributeImpl(attributeType.getId(), item.getId(), value);
        }
        return attribute;
    }

    public Attribute createAttribute() {
        Attribute attribute = new AttributeImpl();
        attribute.setId(-1);
        return attribute;
    }

    public void storeAttribute(Attribute attribute) throws DTException {
        persistence.saveAttribute(attribute);
    }

    public void deleteAttribute(Attribute attribute) throws DTException {
        persistence.deleteAttribute(attribute);
    }

    public Iterator<Attribute> getAttribute(Item item) throws DTException {
        Attribute attributeModel = this.createAttribute();
        attributeModel.setItemId(item.getId());
        Iterator<Attribute> results = persistence.restoreAttribute(attributeModel);
        return results;
    }

    //Item
    public Item createItem(Category category, RegisteredUser user,
            String name, String description) {
        Item newItem = new ItemImpl(category.getId(), user.getId(), name);
        newItem.setDescription(description);
        return newItem;
    }

    public Item createItem() {
        return new ItemImpl();
    }

    public Iterator<Item> findItem(Item modelItem) throws DTException {
        return persistence.restoreItem(modelItem);
    }

    public void storeItem(Item item) throws DTException {
        persistence.saveItem(item);
    }

    public void deleteItem(Item item) throws DTException {
        persistence.deleteItem(item);
    }

    
    public Iterator<Item> getItem(Category category) throws DTException {
        Item modelItem = this.createItem();
        modelItem.setCategoryId(category.getId());
        return persistence.restoreItem(modelItem);
    }

    public Item getItem(Attribute attribute) throws DTException {
        Item modelItem = this.createItem();
        modelItem.setId(attribute.getItemId());
        Iterator<Item> results = persistence.restoreItem(modelItem);
        if (results.hasNext()) {
            return results.next();
        } else {
            throw new DTException("Attribute has invalid item ID.");
        }
    }

    

    public Iterator<Item> getItem(RegisteredUser registeredUser) throws DTException {
        Item modelItem = this.createItem();
        modelItem.setOwnerId(registeredUser.getId());
        Iterator<Item> results = persistence.restoreItem(modelItem);
        return results;

    }

    public Item getItem(Auction auction) throws DTException {
        Item modelItem = this.createItem();
        modelItem.setId(auction.getItemId());
        Iterator<Item> results = persistence.restoreItem(modelItem);
        if (results.hasNext()) {
            return results.next();
        } else {
            throw new DTException("Auction has invalid item ID.");
        }
    }
    
    

    //Category -----------------------------------------------------------------------------------------------
    public Category createCategory() {
        Category category = new CategoryImpl();
        category.setId(-1);
        category.setParentId(-1);
        return category;

    }

    public Category createCategory(Category parent, String name) throws DTException {
        Category category ;
        if (parent != null)
            category = new CategoryImpl(parent.getId(), name);
        else
            category = new CategoryImpl(name);
        return category;
    }   
   

    public java.util.Iterator<Category> findCategory(Category modelCategory) throws DTException {
        return persistence.restoreCategory(modelCategory);
    }

    public void storeCategory(Category category) throws DTException {
        persistence.saveCategory(category);
    }

    public void deleteCategory(Category category) throws DTException {
        persistence.deleteCategory(category);
    }

    public Category getParent(Category category) throws DTException {
        Category categoryModel = this.createCategory();
        categoryModel.setId(category.getParentId());
        Iterator<Category> results = persistence.restoreCategory(categoryModel);
        if (results.hasNext()) {
            return results.next();
        } else {
            throw new DTException("Attribute type has invalid category ID.");
        }
    }

    public java.util.Iterator<Category> getChild(Category category) throws DTException {
        Category categoryModel = this.createCategory();
        categoryModel.setParentId(category.getId());
        return persistence.restoreCategory(categoryModel);

    }

    public Category getCategory(AttributeType attributeType) throws DTException {
        Category template = this.createCategory();
        template.setId(attributeType.getCategoryId());
        Iterator<Category> results = this.findCategory(template);
        if (results.hasNext()) {
            return results.next();
        } else {
            throw new DTException("Attribute type has invalid category ID.");
        }
    }
    
    public Category getCategory(Item item) throws DTException {
        Category template = this.createCategory();
        template.setId(item.getCategoryId());
        Iterator<Category> results = this.findCategory(template);
        if (results.hasNext()) {
            return results.next();
        } else {
            throw new DTException("Item has an invalid category ID.");
        }
    }


    //RegisteredUser -----------------------------------------------------------------------------------------------
    public RegisteredUser createRegisteredUser() {
        RegisteredUser registeredUser = new RegisteredUserImpl();
        registeredUser.setId(-1);
        return registeredUser;
    }

    public RegisteredUser createRegisteredUser(String firstName, String lastName, String name, String password,  String email, String phone, boolean canText,boolean isAdmin, boolean isApproved) throws DTException {
        RegisteredUser registeredUser = new RegisteredUserImpl(firstName, lastName, name, password, email, phone,  canText, isAdmin, isApproved);
        return registeredUser;
    }

    public Iterator<RegisteredUser> findRegisteredUser(RegisteredUser registeredUser) throws DTException {
        return persistence.restoreRegisteredUser(registeredUser);
    }

    public void storeRegisteredUser(RegisteredUser registeredUser) throws DTException {
        persistence.saveRegisteredUser(registeredUser);
    }

    public void deleteRegisteredUser(RegisteredUser registeredUser) throws DTException {
        persistence.deleteRegisteredUser(registeredUser);
    }
    
    public RegisteredUser getRegisteredUser(Item item) throws DTException {
        RegisteredUser template = this.createRegisteredUser();
        template.setId(item.getOwnerId());
        Iterator<RegisteredUser> results = this.findRegisteredUser(template);
        if (results.hasNext()) {
            return results.next();
        } else {
            throw new DTException("Item has an invalid owner ID.");
        }
    }
    

    //Bid ------------------------------------
    public Bid createBid() {
        Bid bid = new BidImpl(null, null, 0);
        bid.setId(-1);
        return bid;
    }

    public Bid createBid(Auction auction, RegisteredUser registeredUser, double amount) {
        Bid bid = new BidImpl(auction, registeredUser, amount);
        return bid;
    }

    public Iterator<Bid> findBid(Bid modelBid) throws DTException {
        return persistence.restoreBid(modelBid);
    }

    public void storeBid(Bid bid) throws DTException {
        persistence.saveBid(bid);
    }

    public void deleteBid(Bid bid) throws DTException {
        persistence.deleteBid(bid);
    }

    //Auction ------------------------------------
    public Auction createAuction() {
        Auction auction = new AuctionImpl(0, (double) 0, null);
        auction.setId(-1);
        return auction;
    }

    public Auction createAuction(Item item, double minPrice, Date expiration) {
        Auction auction = new AuctionImpl(item.getId(), minPrice, expiration);
        return auction;
    }

    public Iterator<Auction> findAuction(Auction modelAuction) throws DTException {
        return persistence.restoreAuction(modelAuction);
    }

    public void storeAuction(Auction auction) throws DTException {
        persistence.saveAuction(auction);
    }

    public void deleteAuction(Auction auction) throws DTException {
        persistence.deleteAuction(auction);
    }
    
    public Auction getAuction(Item item) throws DTException {
        Auction template = this.createAuction();
        template.setItemId(item.getId());
        Iterator<Auction> results = persistence.restoreAuction(template);
        if (results.hasNext()) {
            return results.next();
        } else {
            throw new DTException("No auction found with this Item ID.");
        }
    }

    //Experience report ------------------------------------
    public ExperienceReport createExperienceReport() {
        ExperienceReport er = new ExperienceReportImpl(null, null, 0, null, null);
        er.setId(-1);
        return er;
    }

    public ExperienceReport createExperienceReport(RegisteredUser reviewer, RegisteredUser reviewed, int rating, String report, Date date) {
        ExperienceReport er = new ExperienceReportImpl(reviewer, reviewed, rating, report, date);
        return er;
    }

    public Iterator<ExperienceReport> findExperienceReport(ExperienceReport modelER) throws DTException {
        return persistence.restoreExperienceReport(modelER);
    }

    public void storeExperienceReport(ExperienceReport er) throws DTException {
        persistence.saveExperienceReport(er);
    }

    public void deleteExperienceReport(ExperienceReport er) throws DTException {
        persistence.deleteExperienceReport(er);
    }

}
