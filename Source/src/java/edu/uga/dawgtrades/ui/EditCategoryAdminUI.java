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
public class EditCategoryAdminUI extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        // Get current session
        HttpSession session = request.getSession(true);
        request.setAttribute("baseContext", session.getServletContext().getContextPath());
        LoginControl loginCtrl = new LoginControl();
        CategoryControl catCtrl = new CategoryControl();
        if(!loginCtrl.checkIsLoggedIn(session)){
            response.sendRedirect("/login");
            return;
        }else{
            RegisteredUser currentUser = (RegisteredUser)session.getAttribute("currentSessionUser");
            if(!currentUser.getIsAdmin()) {
                response.sendRedirect("/");
                return;
            }
            request.setAttribute("loggedInUser",currentUser);
        }
        String categoryID = request.getParameter("id");
        if(categoryID != null) {
            try {
                long id = Long.parseLong(categoryID, 10);
                if(catCtrl.categoryExists(id)) {
                    request.setAttribute("myID", Long.valueOf(id).toString());
                    request.setAttribute("parent", catCtrl.getCategoryWithID(catCtrl.getParentCategoryIDForID(id)).getName());
                    request.setAttribute("toEdit", catCtrl.getCategoryWithID(id));
                    ArrayList<AttributeType> attributes = catCtrl.getAttributesForCategory(id);
                    if(attributes != null && !attributes.isEmpty()) {
                        request.setAttribute("attributes", attributes);
                    }
                    request.getRequestDispatcher("/categoryEditAdmin.ftl").forward(request, response);
                }else{
                    request.setAttribute("error", "Category doesn't exist.");
                    request.setAttribute("returnTo", "../categories");
                    request.getRequestDispatcher("/genericError.ftl").forward(request, response);
                    return; 
                }
            }catch(NumberFormatException e) {
                request.setAttribute("error", "Invalid category given: Not a number.");
                request.setAttribute("returnTo", "../categories");
                request.getRequestDispatcher("/genericError.ftl").forward(request, response);
                return; 
            }
        }else{
            request.setAttribute("error", "No category given.");
            request.setAttribute("returnTo", "../categories");
            request.getRequestDispatcher("/genericError.ftl").forward(request, response);
            return; 
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        // Get current session
        HttpSession session = request.getSession(true);
        request.setAttribute("baseContext", session.getServletContext().getContextPath());
        LoginControl loginCtrl = new LoginControl();
        CategoryControl catCtrl = new CategoryControl();
        if(!loginCtrl.checkIsLoggedIn(session)){
            response.sendRedirect("/login");
            return;
        }else{
            RegisteredUser currentUser = (RegisteredUser)session.getAttribute("currentSessionUser");
            if(!currentUser.getIsAdmin()) {
                response.sendRedirect("/");
                return;
            }
            request.setAttribute("loggedInUser",currentUser);
        }
        String name = request.getParameter("name");
        String categoryID = request.getParameter("id");
        if(categoryID != null && name != null) {
            if(name.isEmpty()) {
                request.setAttribute("error", "Name is required.");
                this.doGet(request, response);
                return;
            }
            try {
                long id = Long.parseLong(categoryID, 10);
                if(catCtrl.categoryExists(id)) {
                    if(catCtrl.updateCategory(name, id)) {
                        response.sendRedirect("/admin/categories");
                    }else{
                        if(catCtrl.hasError()) {
                            request.setAttribute("error", "Error: " + catCtrl.getError());
                            this.doGet(request, response);
                            return;
                        }else{
                            request.setAttribute("error", "An unknown error occurred.");
                            this.doGet(request, response);
                            return;
                        }
                    }
                }
            }
            catch(NumberFormatException e) {
                request.setAttribute("error", "Invalid parent category given: Not a number.");
                request.setAttribute("returnTo", "../categories");
                request.getRequestDispatcher("/genericError.ftl").forward(request, response);
                return;
            }
        }else{
            request.setAttribute("error", "Insufficient parameters given.");
            request.setAttribute("returnTo", "../categories");
            request.getRequestDispatcher("/genericError.ftl").forward(request, response);
            return;
        }
    }

    @Override
    public String getServletInfo() {
        return "Edit Category UI";
    }// </editor-fold>

}
