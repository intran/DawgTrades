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
public class CategoryControl {

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

    public HashMap<String, ArrayList> populateHashmapWithCategories(long id) {
        // Gets a hashmap, fills it with id(string) -> subcategory pairs.
        // Useful to get all subcategories in an easily-traversed object.
        ArrayList<Category> subCats = this.getCategoriesWithParentID(id);
        HashMap<String, ArrayList> children = new HashMap<String, ArrayList>();
        // Assuming it's got subcategories.
        if(subCats != null && !subCats.isEmpty()) {
            children.put(Long.valueOf(id).toString(), subCats);
            for(Category cat : subCats) {
                children.putAll(this.populateHashmapWithCategories(cat.getId()));
            }
        }
        return children;
    }

    public Category getCategoryWithID(long id) {
        try {
            this.connect();
            Category toFind = this.objectModel.createCategory();
            toFind.setId(id);
            Iterator<Category> results = this.objectModel.findCategory(toFind);
            if(results.hasNext()) {
                return results.next();
            }else{
                return null;
            }
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

    public long getCategoryItemCount(long id) {
        // Essentially, iterate through all items w/ open auctions, and sum them up for category + descendants
        long count = 0;
        try {
            this.connect();
            // Grab category
            Category toFind = this.objectModel.createCategory();
            toFind.setId(id);
            Iterator<Category> results = this.objectModel.findCategory(toFind);
            if(results.hasNext()) {
                // Got it.
                toFind = results.next();

                // Grab items
                Item itemSearch = this.objectModel.createItem();
                itemSearch.setCategoryId(toFind.getId());
                ItemIterator items = (ItemIterator) this.objectModel.findItem(itemSearch);
                while(items.hasNext()) {
                    // Got em
                    Item item = items.next();

                    // Get auction for item.
                    Auction auction = this.objectModel.createAuction();
                    auction.setItemId(item.getId());
                    Iterator<Auction> auctionResult = this.objectModel.findAuction(auction);

                    // Check if closed
                    if(auctionResult.hasNext()) {
                        if(!auctionResult.next().getIsClosed()) {
                            count++;
                        }
                    }
                }

                // Recurse into subcategories.
                ArrayList<Category> subCats = this.getCategoriesWithParentID(toFind.getId());
                for(Category cat : subCats) {
                    long catCount = this.getCategoryItemCount(cat.getId());
                    if(catCount > 0) {
                        count += catCount;
                    }
                }
                return count;
            }else{
                return -1;
            }
        }
        catch(DTException e) {
            this.hasError = true;
            this.error = e.getMessage();
            return -1;
        }
        finally {
            this.close();
        }
    }

    public HashMap<String, Bid> getBidsForAuctions(ArrayList<Auction> auctions) {

        // Get bids where auction id is auction->id
        HashMap<String, Bid> bids = new HashMap<String, Bid>();
        try {
            this.connect();
            for(Auction auction : auctions) {
                Bid bidFind = this.objectModel.createBid();
                bidFind.setAuction(auction);
                Iterator<Bid> results = this.objectModel.findBid(bidFind);
                Bid candidate = null;
                while(results.hasNext()) {
                    Bid current = results.next();
                    if(candidate == null) {
                        candidate = current;
                    } else {
                        if(current.getAmount() > candidate.getAmount()) {
                            candidate = current;
                        }
                    }
                }
                if(candidate != null) {
                    bids.put(Long.valueOf(auction.getId()).toString(), candidate);
                }
            }
            return bids;
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

    public HashMap<String, Item> getItemsForAuctions(ArrayList<Auction> auctions) {

        // This has an item iterator basically... put into a hashmap for freemarker
        HashMap<String, Item> items = new HashMap<String, Item>();
        try {
            this.connect();
            for(Auction auction : auctions) {
                Item toFind = this.objectModel.createItem();
                toFind.setId(auction.getItemId());
                Iterator<Item> results = this.objectModel.findItem(toFind);
                if (results.hasNext()) {
                    items.put(Long.valueOf(auction.getId()).toString(), results.next());
                } else {
                    throw new DTException("An auction with an invalid item was found.");
                }
            }
            return items;
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

    public ArrayList<Auction> getCategoryAuctions(long id) {
        // again, long-winded mess to get auctions for cat
        ArrayList<Auction> auctions = new ArrayList<Auction>();
        try {
            if(id != 0) {
                this.connect();
                // Get category
                Category toFind = this.objectModel.createCategory();
                toFind.setId(id);
                Iterator<Category> results = this.objectModel.findCategory(toFind);
                if(results.hasNext()) {
                    toFind = results.next();
                    // get item
                    Item itemSearch = this.objectModel.createItem();
                    itemSearch.setCategoryId(toFind.getId());
                    ItemIterator items = (ItemIterator) this.objectModel.findItem(itemSearch);
                    while(items.hasNext()) {
                        Item item = items.next();
                        // get auction
                        Auction auction = this.objectModel.createAuction();
                        auction.setItemId(item.getId());
                        Iterator<Auction> auctionResult = this.objectModel.findAuction(auction);
                        while(auctionResult.hasNext()) {
                            auction = auctionResult.next();
                            if(!auction.getIsClosed()) {
                                auctions.add(auction);
                            }
                        }
                    }
                    // Recurse into subdirectories
                    ArrayList<Category> subCats = this.getCategoriesWithParentID(toFind.getId());
                    for(Category cat : subCats) {
                        ArrayList<Auction> subAuctions = this.getCategoryAuctions(cat.getId());
                        if(subAuctions != null) {
                            auctions.addAll(subAuctions);
                        }
                    }
                    return auctions;
                }else{
                    return null;
                }
            }else{
                // Do for all categories
                ArrayList<Category> subCats = this.getCategoriesWithParentID(0);
                for(Category cat : subCats) {
                    ArrayList<Auction> subAuctions = this.getCategoryAuctions(cat.getId());
                    if(subAuctions != null) {
                        auctions.addAll(subAuctions);
                    }
                }
                return auctions;
            }
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

    public ArrayList<Category> getCategoriesWithParentID(long id) {
        // Does what it says
        try {
            this.connect();
            Category toFind = this.objectModel.createCategory();
            toFind.setParentId(id);
            Iterator<Category> results = this.objectModel.findCategory(toFind);
            ArrayList<Category> out = new ArrayList<Category>();
            while(results.hasNext()) {
                Category cat = results.next();
                if(cat.getId() != 0)
                    out.add(cat);
            }
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

    public boolean categoryExists(long id) {
        // Basic
        if(id < 1)
            return false;
        // Simple existance check
        long count = 0;
        try {
            this.connect();
            Category toFind = this.objectModel.createCategory();
            toFind.setId(id);
            Iterator<Category> results = this.objectModel.findCategory(toFind);
            if(results.hasNext()) {
                return true;
            }else{
                return false;
            }
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

    public boolean createCategoryWithParent(String name, long id) {
        // Spawn new cateogry with a parent
        long count = 0;
        try {
            this.connect();
            Category toMake = this.objectModel.createCategory();
            toMake.setName(name);
            toMake.setParentId(id);
            this.objectModel.storeCategory(toMake);
            return true;
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

    public boolean updateCategory(String name, long id) {
        long count = 0;
        try {
            this.connect();
            Category toEdit = this.objectModel.createCategory();
            toEdit.setId(id);
            Iterator<Category> results = this.objectModel.findCategory(toEdit);
            if(results.hasNext()) {
                toEdit = results.next();
                toEdit.setName(name);
                this.objectModel.storeCategory(toEdit);
                return true;
            }else{
                return false;
            }
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

    public boolean deleteCategory(long id) {
        try {
            this.connect();
            Category toDelete = this.objectModel.createCategory();
            toDelete.setId(id);
            this.objectModel.deleteCategory(toDelete);
            return true;
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

    public long getParentCategoryIDForID(long id) {
        // Get parent category ID for a category ID.
        try {
            this.connect();
            Category toFind = this.objectModel.createCategory();
            toFind.setId(id);
            Iterator<Category> results = this.objectModel.findCategory(toFind);
            if(results.hasNext()) {
                toFind = results.next();
                return toFind.getParentId();
            }
            return -1;
        }
        catch(DTException e) {
            this.hasError = true;
            this.error = e.getMessage();
            return -1;
        }
        finally {
            this.close();
        }

    }

    public ArrayList<AttributeType> getAttributesForCategory(long id) {
        Category toGet = this.getCategoryWithID(id);
        ArrayList<AttributeType> out = new ArrayList<AttributeType>();
        if(toGet != null) {
            try {
                this.connect();
                Iterator<AttributeType> results = this.objectModel.getAttributeType(toGet);
                while(results.hasNext()) {
                    out.add(results.next());
                }
                return out;
            }
            catch(DTException e) {
                this.hasError = true;
                this.error = e.getMessage();
                return null;
            }
        }
        return null;
    }

}
