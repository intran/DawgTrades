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
import java.text.*;


/**
 *
 * @author reanimus
 */
public class BidUI extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // This isn't meant to be called via GET.
        response.sendRedirect("/");
        return; 
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        // Get current session
        HttpSession session = request.getSession(true);
        request.setAttribute("baseContext", session.getServletContext().getContextPath());
        LoginControl loginCtrl = new LoginControl();
        AuctionControl auctionCtrl = new AuctionControl();
        if(!loginCtrl.checkIsLoggedIn(session)){
            response.sendRedirect("/login");
            return;
        }

        RegisteredUser currentUser = (RegisteredUser)session.getAttribute("currentSessionUser");
        request.setAttribute("loggedInUser",currentUser);

        // Grab POST data
        String auctionID = request.getParameter("auctionID");
        String amountString = request.getParameter("amount");

        // Check that both were provided
        if(auctionID != null && amountString != null) {
            // ensure an amount was entered
            if(amountString.isEmpty()) {
                request.setAttribute("error", "An amount is required.");
                request.setAttribute("returnTo", "auction?id=" + auctionID);
                request.getRequestDispatcher("/genericError.ftl").forward(request, response);
                return;
            }
            try {
                // Parse it
                long id = Long.parseLong(auctionID, 10);
                double amount = Double.parseDouble(amountString);

                // Rounded to 00
                DecimalFormat df = new DecimalFormat("0.00");
                String format = df.format(amount);
                amount = df.parse(format).doubleValue();


                // Get the auction
                Auction auction = auctionCtrl.getAuctionWithID(id);
                if(auction != null) {
                    // Check that it's still open.
                    if(auction.getIsClosed() ) {
                        request.setAttribute("error", "You can't bid on a closed auction.");
                        request.setAttribute("returnTo", "auction?id=" + auctionID);
                        request.getRequestDispatcher("/genericError.ftl").forward(request, response);
                        return;
                    }

                    // Grab owner
                    RegisteredUser owner = auctionCtrl.getOwnerForAuctionID(id);
                    if(owner == null) {
                        if(auctionCtrl.hasError()) {
                            request.setAttribute("error", "Error getting owner: " + auctionCtrl.getError());
                            request.setAttribute("returnTo", "auction?id=" + auctionID);
                            request.getRequestDispatcher("/genericError.ftl").forward(request, response);
                            return;
                        }else{
                            request.setAttribute("error", "Internal error. Auction is invalid: No owner.");
                            request.setAttribute("returnTo", "auction?id=" + auctionID);
                            request.getRequestDispatcher("/genericError.ftl").forward(request, response);
                            return;
                        }   
                    }

                    // Ensure WE'RE not the owner
                    if(owner.getId() == currentUser.getId()) {
                        request.setAttribute("error", "You can't bid on your own auction.");
                        request.setAttribute("returnTo", "auction?id=" + auctionID);
                        request.getRequestDispatcher("/genericError.ftl").forward(request, response);
                        return;
                    }

                    // Ensure it's above minimum
                    if(amount < auction.getMinPrice()) {
                        request.setAttribute("error", "You must bid at least $" + auction.getMinPrice() + ".");
                        request.setAttribute("returnTo", "auction?id=" + auctionID);
                        request.getRequestDispatcher("/genericError.ftl").forward(request, response);
                        return;
                    }

                    // Grab current bids
                    ArrayList<Bid> currentBids = auctionCtrl.getBidsForAuctionID(id);
                    if(currentBids == null) {
                        if(auctionCtrl.hasError()) {
                            request.setAttribute("error", "Error getting bids: " + auctionCtrl.getError());
                            request.setAttribute("returnTo", "/auction?id=" + auctionID);
                            request.getRequestDispatcher("/genericError.ftl").forward(request, response);
                            return;
                        }else{
                            request.setAttribute("error", "Internal error. Unable to get bids.");
                            request.setAttribute("returnTo", "auction?id=" + auctionID);
                            request.getRequestDispatcher("/genericError.ftl").forward(request, response);
                            return;
                        }   
                    }

                    // Check to ensure we're bidding more than current winning bid
                    if(!currentBids.isEmpty()) {
                        Bid currentMax = currentBids.get(0);
                        if(currentMax.getAmount() >= amount) {
                            request.setAttribute("error", "You must bid higher than the current maximum (" + currentMax.getAmount() + ")");
                            request.setAttribute("returnTo", "auction?id=" + auctionID);
                            request.getRequestDispatcher("/genericError.ftl").forward(request, response);
                            return;
                        }
                    }
                    // Ready to insert.
                    BidControl bidCtrl = new BidControl();
                    if(bidCtrl.placeBid(id, amount, currentUser.getId())) {
                        response.sendRedirect("/auction?id=" + auctionID + "&message=556961b3f4bbd252ff169bbf5502611444faa0de");
                    }else{
                        if(bidCtrl.hasError()) {
                            request.setAttribute("error", "Error placing bid: " + bidCtrl.getError());
                            request.setAttribute("returnTo", "auction?id=" + auctionID);
                            request.getRequestDispatcher("/genericError.ftl").forward(request, response);
                            return;
                        }else{
                            request.setAttribute("error", "Internal error. Unable to place bid.");
                            request.setAttribute("returnTo", "auction?id=" + auctionID);
                            request.getRequestDispatcher("/genericError.ftl").forward(request, response);
                            return;
                        }   

                    }
                }else{
                    if(auctionCtrl.hasError()) {
                        request.setAttribute("error", "Error getting auction: " + auctionCtrl.getError());
                        request.setAttribute("returnTo", "category");
                        request.getRequestDispatcher("/genericError.ftl").forward(request, response);
                        return;
                    }else{
                        request.setAttribute("error", "Auction doesn't exist.");
                        request.setAttribute("returnTo", "category");
                        request.getRequestDispatcher("/genericError.ftl").forward(request, response);
                        return;
                    }
                }
            }
            catch(NumberFormatException e) {
                request.setAttribute("error", "Invalid input given: Not a number.");
                request.setAttribute("returnTo", "category");
                request.getRequestDispatcher("/genericError.ftl").forward(request, response);
                return;
            }
            catch(ParseException e) {
                // This should never happen given the way we've set things up
                request.setAttribute("error", "Internal error.");
                request.setAttribute("returnTo", "./");
                request.getRequestDispatcher("/genericError.ftl").forward(request, response);
                return;

            }
        }else{
            request.setAttribute("error", "Insufficient parameters given.");
            request.setAttribute("returnTo", "category");
            request.getRequestDispatcher("/genericError.ftl").forward(request, response);
            return;
        }
    }

    @Override
    public String getServletInfo() {
        return "Bid UI";
    }// </editor-fold>

}
