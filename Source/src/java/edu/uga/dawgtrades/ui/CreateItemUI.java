package edu.uga.dawgtrades.ui;

import edu.uga.dawgtrades.DTException;
import edu.uga.dawgtrades.control.LoginControl;
import edu.uga.dawgtrades.model.RegisteredUser;
import edu.uga.dawgtrades.model.Category;
import edu.uga.dawgtrades.model.AttributeType;
import edu.uga.dawgtrades.control.CreateItemCtrl;
import edu.uga.dawgtrades.control.CategoryControl;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class CreateItemUI extends HttpServlet{
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)throws ServletException, IOException{
		HttpSession session = request.getSession(true);
		request.setAttribute("baseContext", session.getServletContext().getContextPath());
		String error = "Error unknown";
		LoginControl ctrl = new LoginControl();
		CategoryControl catCtrl = new CategoryControl();
		if(!ctrl.checkIsLoggedIn(session)){
			response.sendRedirect("login");
			return;
		}else{
			RegisteredUser currentUser = (RegisteredUser)session.getAttribute("currentSessionUser");
			request.setAttribute("loggedInUser",currentUser);
		}
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
		if(itemName != null){
			if(itemName.isEmpty()) {
				request.setAttribute("error","Name is required.");
			}else if(desc == null || desc.isEmpty()) {
				request.setAttribute("error","Description is required.");
			}else if(catID == null || Long.parseLong(catID) <= 0) {
				request.setAttribute("error","Category is invalid.");
			}else{
				//send the itemName, Item Desc, attributes, and category to the control
				RegisteredUser currentUser = (RegisteredUser)session.getAttribute("currentSessionUser");
				long itemId = itemCtrl.attemptItemCreate(request.getParameterMap(),currentUser.getId());	
				if(itemId<0){
					if(itemCtrl.hasError()){
						error = itemCtrl.getError();
					}
					request.setAttribute("error","error: "+error);
					request.getRequestDispatcher("/createItem.ftl").forward(request,response);
					//response.sendRedirect("/createAuction?id="+error);
					return;	
				}
				
				response.sendRedirect("createAuction?id="+itemId);
				return;
			}
		}
		
		request.getRequestDispatcher("/createItem.ftl").forward(request,response);
		
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)throws ServletException, IOException{
				
		doGet(request,response);	
	}
}
