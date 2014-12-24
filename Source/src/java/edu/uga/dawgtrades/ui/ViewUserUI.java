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
public class ViewUserUI extends HttpServlet {

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

        // this all basically fetches user info + auction info for user... see others for an idea of how.
        ViewUserControl vuserCtrl = new ViewUserControl();
        CategoryControl catCtrl = new CategoryControl();
        String userID = request.getParameter("id");
        if(userID != null) {
            try {
                long id = Long.parseLong(userID, 10);
                RegisteredUser toView = vuserCtrl.getUserWithID(id);
                if(toView != null) {
                    request.setAttribute("viewUser", toView);

                    ArrayList<Auction> userAuctions = vuserCtrl.getAuctionsForUser(toView);
                    if(userAuctions != null) {
                        HashMap<String, Bid> bids = catCtrl.getBidsForAuctions(userAuctions);
                        HashMap<String, Item> items = catCtrl.getItemsForAuctions(userAuctions);
                        if(bids != null && items != null) {
                            request.setAttribute("userAuctions", userAuctions);
                            request.setAttribute("items", items);
                            request.setAttribute("auctionBids", bids);
                            request.getRequestDispatcher("/viewUser.ftl").forward(request, response);
                        }else {
                            if(catCtrl.hasError()) {
                                request.setAttribute("error", "Error while getting auction data: " + catCtrl.getError());
                                request.setAttribute("returnTo", "/");
                                request.getRequestDispatcher("/genericError.ftl").forward(request, response);
                                return;
                            }else{
                                request.setAttribute("error", "Unknown error while getting auction data");
                                request.setAttribute("returnTo", "/");
                                request.getRequestDispatcher("/genericError.ftl").forward(request, response);
                                return;
                            }
                        }
                    }else{
                        if(catCtrl.hasError()) {
                            request.setAttribute("error", "Error while getting auctions: " + catCtrl.getError());
                            request.setAttribute("returnTo", "/");
                            request.getRequestDispatcher("/genericError.ftl").forward(request, response);
                            return;
                        }else{
                            request.setAttribute("error", "Unknown error while getting auctions");
                            request.setAttribute("returnTo", "/");
                            request.getRequestDispatcher("/genericError.ftl").forward(request, response);
                            return;
                        }

                    }
                }else{
                    if(vuserCtrl.hasError()) {
                        request.setAttribute("error", vuserCtrl.getError());
                        request.setAttribute("returnTo", "/");
                        request.getRequestDispatcher("/genericError.ftl").forward(request, response);
                        return;
                    }else{
                        request.setAttribute("error", "User does not exist.");
                        request.setAttribute("returnTo", "/");
                        request.getRequestDispatcher("/genericError.ftl").forward(request, response);
                        return;
                    }
                }
            }
            catch(NumberFormatException e) {
                request.setAttribute("error", "Invalid user ID. Please try again.");
                request.setAttribute("returnTo", "/");
                request.getRequestDispatcher("/genericError.ftl").forward(request, response);
                return;
            }
        }else{
            request.setAttribute("error", "User ID required. Please try again.");
            request.setAttribute("returnTo", "/");
            request.getRequestDispatcher("/genericError.ftl").forward(request, response);
        }
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
