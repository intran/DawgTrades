package edu.uga.dawgtrades.ui;

import edu.uga.dawgtrades.DTException;
import edu.uga.dawgtrades.control.*;
import edu.uga.dawgtrades.model.*;
import java.util.*;
import java.io.*;
import javax.servlet.http.*;
import javax.servlet.*;

/*
List the open auctions for items owned by the current user.
*/
public class MyBidsUI extends HttpServlet{

	@Override
        protected void doGet(HttpServletRequest request, HttpServletResponse response)throws ServletException, IOException{
		HttpSession session = request.getSession(true);
		request.setAttribute("baseContext", session.getServletContext().getContextPath());
                String error = "Error unknown";
                LoginControl ctrl = new LoginControl();
                if(!ctrl.checkIsLoggedIn(session)){
                        response.sendRedirect("/login");
                        request.setAttribute("loggedInUser","");
                        request.removeAttribute("loggedInUser");
                        return;
                }else{
                        RegisteredUser currentUser = (RegisteredUser)session.getAttribute("currentSessionUser");
                        request.setAttribute("loggedInUser",currentUser);
                }
		 RegisteredUser currentUser = (RegisteredUser)session.getAttribute("currentSessionUser");

		long userId = currentUser.getId();
		MyBidsControl myBidsCtrl = new MyBidsControl();
        CategoryControl catCtrl = new CategoryControl();
		ArrayList<Bid> myBids = myBidsCtrl.getBidsForID(userId);
		if(myBids == null) {
	        if(myBidsCtrl.hasError()) {
	            request.setAttribute("error", "Error: " + myBidsCtrl.getError());
	            request.setAttribute("returnTo", "./");
	            request.getRequestDispatcher("/genericError.ftl").forward(request, response);
	            return;
	        }else{
	            request.setAttribute("error", "An unknown error occurred.");
	            request.setAttribute("returnTo", "./");
	            request.getRequestDispatcher("/genericError.ftl").forward(request, response);
	            return;
	        }
		}
		ArrayList<Auction> auctions = myBidsCtrl.getAuctionsForBids(myBids);
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

		 request.getRequestDispatcher("/myBids.ftl").forward(request,response);

		}else{
	        if(myBidsCtrl.hasError()) {
	            request.setAttribute("error", "Error: " + myBidsCtrl.getError());
	            request.setAttribute("returnTo", "./");
	            request.getRequestDispatcher("/genericError.ftl").forward(request, response);
	            return;
	        }else{
	            request.setAttribute("error", "An unknown error occurred.");
	            request.setAttribute("returnTo", "./");
	            request.getRequestDispatcher("/genericError.ftl").forward(request, response);
	            return;
	        }
		}
	}

	
        @Override
        protected void doPost(HttpServletRequest request, HttpServletResponse response)throws ServletException, IOException{
		doGet(request,response);
	}
}

