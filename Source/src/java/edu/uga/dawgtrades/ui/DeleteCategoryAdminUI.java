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
public class DeleteCategoryAdminUI extends HttpServlet {
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
                    if(catCtrl.getCategoryItemCount(id) > 0) {
                        request.setAttribute("error", "Category isn't empty.");
                        request.setAttribute("returnTo", "/admin/categories");
                        request.getRequestDispatcher("/genericError.ftl").forward(request, response);
                        return; 
                    }else if(!catCtrl.getCategoriesWithParentID(id).isEmpty()) {
                        request.setAttribute("error", "Category has subcategories.");
                        request.setAttribute("returnTo", "/admin/categories");
                        request.getRequestDispatcher("/genericError.ftl").forward(request, response);
                        return; 
                    }
                    request.setAttribute("toDelete", categoryID);
                    request.getRequestDispatcher("/categoryDeleteAdmin.ftl").forward(request, response);
                }else{
                    request.setAttribute("error", "Category doesn't exist.");
                    request.setAttribute("returnTo", "/admin/categories");
                    request.getRequestDispatcher("/genericError.ftl").forward(request, response);
                    return; 
                }
            }
            catch(NumberFormatException e) {
                request.setAttribute("error", "Bad ID passed.");
                request.setAttribute("returnTo", "/admin/categories");
                request.getRequestDispatcher("/genericError.ftl").forward(request, response);
                return;
            }
        }else{
            response.sendRedirect("/admin/categories");
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
        String categoryID = request.getParameter("id");
        if(categoryID != null) {
            try {
                long id = Long.parseLong(categoryID, 10);
                if(catCtrl.categoryExists(id)) {
                    if(catCtrl.getCategoryItemCount(id) > 0) {
                        request.setAttribute("error", "Category isn't empty.");
                        request.setAttribute("returnTo", "/admin/categories");
                        request.getRequestDispatcher("/genericError.ftl").forward(request, response);
                        return; 
                    }else if(!catCtrl.getCategoriesWithParentID(id).isEmpty()) {
                        request.setAttribute("error", "Category has subcategories.");
                        request.setAttribute("returnTo", "/admin/categories");
                        request.getRequestDispatcher("/genericError.ftl").forward(request, response);
                        return; 
                    }
                    if(catCtrl.deleteCategory(id)) {
                        response.sendRedirect("/admin/categories");
                    }else{
                        if(catCtrl.hasError()) {
                            request.setAttribute("error", "Error: " + catCtrl.getError());
                            request.setAttribute("returnTo", "/admin/categories");
                            request.getRequestDispatcher("/genericError.ftl").forward(request, response);
                            return;
                        }else{
                            request.setAttribute("error", "An unknown error occurred.");
                            request.setAttribute("returnTo", "/admin/categories");
                            request.getRequestDispatcher("/genericError.ftl").forward(request, response);
                            return;
                        }
                    }
                }
            }
            catch(NumberFormatException e) {
                request.setAttribute("error", "Bad ID passed.");
                request.setAttribute("returnTo", "/admin/categories");
                request.getRequestDispatcher("/genericError.ftl").forward(request, response);
                return;
            }
        }else{
            response.sendRedirect("/admin/categories");
            return;
        }
    }

    @Override
    public String getServletInfo() {
        return "Category UI";
    }// </editor-fold>

}
