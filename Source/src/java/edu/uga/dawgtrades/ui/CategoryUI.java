package edu.uga.dawgtrades.ui;
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
import edu.uga.dawgtrades.DTException;
import edu.uga.dawgtrades.control.*;
import edu.uga.dawgtrades.model.*;
import java.util.*;
import java.io.IOException;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import edu.uga.dawgtrades.model.RegisteredUser;


/**
 *
 * @author sahar
 */
public class CategoryUI extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        // Get current session
        HttpSession session = request.getSession(true);
        request.setAttribute("baseContext", session.getServletContext().getContextPath());
        LoginControl loginCtrl = new LoginControl();
        if(loginCtrl.checkIsLoggedIn(session))
        {
            RegisteredUser currentUser = (RegisteredUser) session.getAttribute("currentSessionUser");
            request.setAttribute("loggedInUser", currentUser);
        }else{
            request.setAttribute("loggedInUser", "");;
            request.removeAttribute("loggedInUser");
        }
        CategoryControl catCtrl = new CategoryControl();

        // Check if an ID is given
        String categoryID = request.getParameter("id");
        if(categoryID != null) {
            try {
                long id = Long.parseLong(categoryID, 10);

                // If it's valid...
                if(id > 0) {
                    // Get category
                    Category toBrowse = catCtrl.getCategoryWithID(id);
                    if(toBrowse != null) {
                        request.setAttribute("specificCategory", toBrowse);

                        // Get direct subcategories.
                        ArrayList<Category> subCats = catCtrl.getCategoriesWithParentID(id);
                        if(subCats != null && !subCats.isEmpty()) {
                            // Grab a count of each subcategory's auctions
                            ArrayList<Long> counts = new ArrayList<Long>();
                            for(Category cat : subCats) {
                                counts.add(new Long(catCtrl.getCategoryItemCount(cat.getId())));
                            }
                            request.setAttribute("subCategories", subCats);
                            request.setAttribute("subCategoryCounts", counts);
                        } else if(subCats == null) {
                            if(catCtrl.hasError()) {
                                request.setAttribute("error", "Error while getting subcategories: " + catCtrl.getError());
                            }else{
                                request.setAttribute("error", "Unknown error while getting subcategories");
                            }
                        }

                        // Get the auctions under this category.
                        ArrayList<Auction> auctions = catCtrl.getCategoryAuctions(id);
                        if(auctions != null) {
                            // Get auction metadata
                            HashMap<String, Bid> bids = catCtrl.getBidsForAuctions(auctions);
                            HashMap<String, Item> items = catCtrl.getItemsForAuctions(auctions);
                            if(bids != null && items != null) {
                                if(!auctions.isEmpty()) {
                                    request.setAttribute("categoryAuctions", auctions);
                                    request.setAttribute("categoryItems", items);
                                    request.setAttribute("auctionBids", bids);
                                }
                            }else {
                                if(catCtrl.hasError()) {
                                    request.setAttribute("error", "Error while getting auction data: " + catCtrl.getError());
                                }else{
                                    request.setAttribute("error", "Unknown error while getting auction data");
                                }
                            }
                        }else{
                            if(catCtrl.hasError()) {
                                request.setAttribute("error", "Error while getting auctions: " + catCtrl.getError());   
                            }else{
                                request.setAttribute("error", "Unknown error while getting auctions");
                            }   
                        }

                        // Forward request.
                        request.getRequestDispatcher("/category.ftl").forward(request, response);
                        return;
                    }else{
                        if(catCtrl.hasError()) {
                            request.setAttribute("error", catCtrl.getError());
                        }else{
                            request.setAttribute("error", "Category does not exist.");
                        }
                    }
                }
            }
            catch(NumberFormatException e) {
                request.setAttribute("error", "Invalid category ID. Please try again.");
            }
        }
        // Fall through to this if error'd.
        ArrayList<Category> subCats = catCtrl.getCategoriesWithParentID(0);
        if(subCats != null && !subCats.isEmpty()) {
            ArrayList<Long> counts = new ArrayList<Long>();
            for(Category cat : subCats) {
                counts.add(new Long(catCtrl.getCategoryItemCount(cat.getId())));
            }
            request.setAttribute("subCategories", subCats);
            request.setAttribute("subCategoryCounts", counts);
            ArrayList<Auction> auctions = catCtrl.getCategoryAuctions(0);
            if(auctions != null) {
                HashMap<String, Bid> bids = catCtrl.getBidsForAuctions(auctions);
                HashMap<String, Item> items = catCtrl.getItemsForAuctions(auctions);
                if(bids != null && items != null) {
                    if(!auctions.isEmpty()) {
                        request.setAttribute("categoryAuctions", auctions);
                        request.setAttribute("categoryItems", items);
                        request.setAttribute("auctionBids", bids);
                    }

                }else {
                    if(catCtrl.hasError()) {
                        request.setAttribute("error", "Error while getting auction data: " + catCtrl.getError());
                    }else{
                        request.setAttribute("error", "Unknown error while getting auction data");
                    }
                }
            }else{
                if(catCtrl.hasError()) {
                    request.setAttribute("error", "Error while getting auctions: " + catCtrl.getError());  
                }else{
                    request.setAttribute("error", "Unknown error while getting auctions");
                }
            }
        } else if(subCats == null) {
            if(catCtrl.hasError()) {
                request.setAttribute("error", "Error while getting categories: " + catCtrl.getError());
            }else{
                request.setAttribute("error", "Unknown error while categories");
            }
        }
        
        request.getRequestDispatcher("/category.ftl").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doGet(request, response);
    }

    @Override
    public String getServletInfo() {
        return "Category UI";
    }// </editor-fold>

}
