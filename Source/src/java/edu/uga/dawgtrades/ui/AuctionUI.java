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
public class AuctionUI extends HttpServlet {

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

        // Check for a message to display
        String messageID = request.getParameter("message");
        if(messageID != null) {
            String message = CommonControl.infoMessages.get(messageID);
            if(message != null) {
                request.setAttribute("message", message);
            }
        }


        // Get the auction we're viewing.
        AuctionControl auctionCtrl = new AuctionControl();
        String auctionID = request.getParameter("id");
        if(auctionID != null) {
            try {
                // Get + check if the ID works.
                long id = Long.parseLong(auctionID, 10);
                Auction toView = auctionCtrl.getAuctionWithID(id);
                if(toView != null) {

                    // Got an auction, moving on to get its metadata.
                    request.setAttribute("auction", toView);

                    // Get its category.
                    CategoryControl catCtrl = new CategoryControl();
                    Category auctionCategory = catCtrl.getCategoryWithID(auctionCtrl.getCategoryIDForAuctionID(id));
                    if(auctionCategory == null) {
                        if(catCtrl.hasError()) {
                           request.setAttribute("error", catCtrl.getError());
                        }else{
                           request.setAttribute("error", "Unkown error occurred while getting auction category.");
                        }
                        request.setAttribute("returnTo", "category");
                        request.getRequestDispatcher("/genericError.ftl").forward(request, response);
                        return;

                    }
                    
                    // Get the attributes for it.
                    ArrayList<Attribute> attributes = auctionCtrl.getAttributesForAuctionID(id);
                    HashMap<String, Attribute> attributeForType = new HashMap<String, Attribute>();
                    
                    if(attributes == null) {
                        if(auctionCtrl.hasError()) {
                           request.setAttribute("error", auctionCtrl.getError());
                        }else{
                           request.setAttribute("error", "Unkown error occurred while getting auction attributes.");
                        }
                        request.setAttribute("returnTo", "category");
                        request.getRequestDispatcher("/genericError.ftl").forward(request, response);
                        return;

                    }

                    for(Attribute attr : attributes) {
                        attributeForType.put(new Long(attr.getAttributeTypeId()).toString(), attr);
                    }

                    // Get the attribute type metadata
                    ArrayList<AttributeType> attributeTypes = auctionCtrl.getAttributeTypesForCategory(auctionCategory.getId());
                    
                    if(attributeTypes == null) {
                        if(auctionCtrl.hasError()) {
                           request.setAttribute("error", auctionCtrl.getError());
                        }else{
                           request.setAttribute("error", "Unkown error occurred while getting category attributes.");
                        }
                        request.setAttribute("returnTo", "category");
                        request.getRequestDispatcher("/genericError.ftl").forward(request, response);
                        return;

                    }
                    
                    // Grab the list of bids
                    ArrayList<Bid> bids = auctionCtrl.getBidsForAuctionID(id);
                    
                    if(bids == null) {
                        if(auctionCtrl.hasError()) {
                           request.setAttribute("error", auctionCtrl.getError());
                        }else{
                           request.setAttribute("error", "Unkown error occurred while getting bids.");
                        }
                        request.setAttribute("returnTo", "category");
                        request.getRequestDispatcher("/genericError.ftl").forward(request, response);
                        return;

                    }
                    

                    // Grab the owner.
                    RegisteredUser owner = auctionCtrl.getOwnerForAuctionID(id);
                    if(owner == null) {
                        if(auctionCtrl.hasError()) {
                           request.setAttribute("error", auctionCtrl.getError());
                        }else{
                           request.setAttribute("error", "Unkown error occurred while getting the auction owner.");
                        }
                        request.setAttribute("returnTo", "category");
                        request.getRequestDispatcher("/genericError.ftl").forward(request, response);
                        return;

                    }
                    
                    // Grab the item in the auction.
                    Item item = auctionCtrl.getItemForAuctionID(id);
                    if(item == null) {
                        if(auctionCtrl.hasError()) {
                           request.setAttribute("error", auctionCtrl.getError());
                        }else{
                           request.setAttribute("error", "Unknown error occurred while getting the auction item.");
                        }
                        request.setAttribute("returnTo", "category");
                        request.getRequestDispatcher("/genericError.ftl").forward(request, response);
                        return;

                    }

                    // If it's closed, calculate a winner (if any)
                    if(toView.getIsClosed()) {
                        // Get winner of auction (assuming there is one...)
                        if(!bids.isEmpty()) {
                            Bid winningBid = bids.get(0);
                            RegisteredUser winner = auctionCtrl.getUser(winningBid.getRegisteredUser());
                            if(winner != null) {
                                request.setAttribute("winner", winner);
                            }else{
                                if(auctionCtrl.hasError()) {
                                   request.setAttribute("error", "Error while getting auction winner: " + auctionCtrl.getError());
                                }else{
                                   request.setAttribute("error", "Unknown error occurred while getting the auction winner.");
                                }
                            }
                        }
                    }

                    request.setAttribute("category", auctionCategory);
                    request.setAttribute("attributeForType", attributeForType);
                    request.setAttribute("attributeTypes", attributeTypes);
                    request.setAttribute("bids", bids);
                    request.setAttribute("owner", owner);
                    request.setAttribute("item", item);
                    request.setAttribute("auction", toView);
                    request.getRequestDispatcher("/viewAuction.ftl").forward(request, response);
                    return;
                }else{
                    if(auctionCtrl.hasError()) {
                        request.setAttribute("error", auctionCtrl.getError());
                        request.setAttribute("returnTo", "category");
                        request.getRequestDispatcher("/genericError.ftl").forward(request, response);
                        return;
                    }else{
                        request.setAttribute("error", "Auction does not exist.");
                        request.setAttribute("returnTo", "category");
                        request.getRequestDispatcher("/genericError.ftl").forward(request, response);
                        return;
                    }
                }
            }
            catch(NumberFormatException e) {
                request.setAttribute("error", "Invalid auction ID. Please try again.");
                request.setAttribute("returnTo", "category");
                request.getRequestDispatcher("/genericError.ftl").forward(request, response);
                return;
            }
        }else{
            // No auction means a redirect home.
            response.sendRedirect("/");
            return;
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doGet(request, response);
    }

    @Override
    public String getServletInfo() {
        return "Auction UI";
    }// </editor-fold>

}
