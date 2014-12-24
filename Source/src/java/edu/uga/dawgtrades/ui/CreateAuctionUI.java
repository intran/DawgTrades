package edu.uga.dawgtrades.ui;

import edu.uga.dawgtrades.DTException;
import edu.uga.dawgtrades.control.LoginControl;
import edu.uga.dawgtrades.control.CreateAuctionCtrl;
import edu.uga.dawgtrades.control.CreateItemCtrl;
import edu.uga.dawgtrades.control.CategoryControl;
import edu.uga.dawgtrades.model.Category;
import edu.uga.dawgtrades.model.RegisteredUser;
import edu.uga.dawgtrades.model.AttributeType;
import edu.uga.dawgtrades.model.Attribute;
import edu.uga.dawgtrades.model.Item;
import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.*;

public class CreateAuctionUI extends HttpServlet{
	private static long itemId;
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)throws ServletException, IOException{
		HttpSession session = request.getSession(true);
        request.setAttribute("baseContext", session.getServletContext().getContextPath());
		LoginControl ctrl = new LoginControl();
                CategoryControl catCtrl = new CategoryControl();
		CreateAuctionCtrl auctionCtrl = new CreateAuctionCtrl();
		String error = "Error unknown";
		if(!ctrl.checkIsLoggedIn(session)){
			response.sendRedirect("/login");
			request.setAttribute("loggedInUser","");
			request.removeAttribute("loggedInUser");
			return;
		}else{
			RegisteredUser currentUser = (RegisteredUser)session.getAttribute("currentSessionUser");
			request.setAttribute("loggedInUser",currentUser);
		}
		//if the form hasn't been filled out yet. Just so it doesn't do this part for no reason
		                CreateItemCtrl itemCtrl = new CreateItemCtrl();
        HashMap<String, ArrayList> children = catCtrl.populateHashmapWithCategories(0);
                if(children != null) {
                    request.setAttribute("categoriesMap", children);
                }else{
                    if(catCtrl.hasError()) {
                        request.setAttribute("error", "Error getting categories: " + catCtrl.getError());
                        request.setAttribute("returnTo", "./");
                        request.getRequestDispatcher("/genericError.ftl").forward(request, response);
                        return;
                    }else{
                        request.setAttribute("error", "Internal error getting categories.");
                        request.setAttribute("returnTo", "./");
                        request.getRequestDispatcher("/genericError.ftl").forward(request, response);
                        return;
                    }

                }
		                String categoryId = request.getParameter("id");
                if(categoryId != null){
                        try{
                                long id = Long.parseLong(categoryId,10);
                                Category category = catCtrl.getCategoryWithID(id);
                                request.setAttribute("categoryChosen",category);
                                ArrayList<AttributeType> attributeTypes = itemCtrl.getCategoryAttributes(id);
                                if(attributeTypes != null){
                                        request.setAttribute("attributes",attributeTypes);
                                }else if(itemCtrl.hasError()){
                                        request.setAttribute("error","Error: "+itemCtrl.getError());
                                }
                        }catch(NumberFormatException e){
                                request.setAttribute("error","Invalid category ID. Please try again.");
                        }
                }
                String itemName = request.getParameter("name");
                String desc = request.getParameter("desc");
                String catID = request.getParameter("catId");
		String expiration = request.getParameter("expiration");
		String expTime = request.getParameter("expiration-time");
		String minPrice = request.getParameter("minPrice");
                if(itemName != null){
                        if(itemName.isEmpty()) {
                                request.setAttribute("error","Name is required.");
                        }else if(desc == null || desc.isEmpty()) {
                                request.setAttribute("error","Description is required.");
                        }else if(catID == null || Long.parseLong(catID) <= 0) {
                                request.setAttribute("error","Category is invalid.");
                        }else if(!auctionCtrl.dateIsValid(expiration,expTime)){
				request.setAttribute("error","Date is invalid.");			
			}else if(Double.parseDouble(minPrice) <= 0){
				request.setAttribute("error","Minimum Selling price must greater than 0");
			}else{
				Map<String,String[]> parameters = request.getParameterMap();
                                //send the itemName, Item Desc, attributes, and category to the control
                                RegisteredUser currentUser = (RegisteredUser)session.getAttribute("currentSessionUser");
                                
				
					long itemId = itemCtrl.attemptItemCreate(request.getParameterMap(),currentUser.getId());
                                	if(itemId<0){
                                        	if(itemCtrl.hasError()){
                                                error = itemCtrl.getError();
                                        	}
                                        	request.setAttribute("error","error: "+error);
                                        	request.getRequestDispatcher("/createAuction.ftl").forward(request,response);
						return;
                                	}else{
		                                if(!auctionCtrl.attemptAuctionCreate(parameters,itemId)){
							itemCtrl.removeItem(itemId);
                		                        request.setAttribute("error", "Error: "+auctionCtrl.getError());
							request.getRequestDispatcher("/createAuction.ftl").forward(request,response);
							return;
                               			 }
		                 
				               response.sendRedirect("/myAuctions");
                		                return;

					}
				
                                
                        }
			
                }

		
		request.getRequestDispatcher("/createAuction.ftl").forward(request,response);
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)throws ServletException, IOException{
		doGet(request,response);	
			
	}
}
