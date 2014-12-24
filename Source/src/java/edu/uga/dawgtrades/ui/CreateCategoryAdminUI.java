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
public class CreateCategoryAdminUI extends HttpServlet {
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
        HashMap<String, ArrayList> children = catCtrl.populateHashmapWithCategories(0);
        request.setAttribute("categoriesMap", children);
        request.getRequestDispatcher("/categoryCreateAdmin.ftl").forward(request, response);
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
        String parentID = request.getParameter("category");
        if(parentID != null && name != null) {
            if(name.isEmpty()) {
                request.setAttribute("error", "Name is required.");
                HashMap<String, ArrayList> children = catCtrl.populateHashmapWithCategories(0);
                request.setAttribute("categoriesMap", children);
                request.getRequestDispatcher("/categoryCreateAdmin.ftl").forward(request, response);
                return;
            }
            try {
                long id = Long.parseLong(parentID, 10);
                if(catCtrl.categoryExists(id) || id == 0) {
                    if(catCtrl.createCategoryWithParent(name, id)) {
                        response.sendRedirect("/admin/categories");
                    }else{
                        if(catCtrl.hasError()) {
                            request.setAttribute("error", "Error: " + catCtrl.getError());
                            HashMap<String, ArrayList> children = catCtrl.populateHashmapWithCategories(0);
                            request.setAttribute("categoriesMap", children);
                            request.getRequestDispatcher("/categoryCreateAdmin.ftl").forward(request, response);
                            return;
                        }else{
                            request.setAttribute("error", "An unknown error occurred.");
                            HashMap<String, ArrayList> children = catCtrl.populateHashmapWithCategories(0);
                            request.setAttribute("categoriesMap", children);
                            request.getRequestDispatcher("/categoryCreateAdmin.ftl").forward(request, response);
                            return;
                        }
                    }
                }
            }
            catch(NumberFormatException e) {
                request.setAttribute("error", "Invalid parent category given: Not a number.");
                HashMap<String, ArrayList> children = catCtrl.populateHashmapWithCategories(0);
                request.setAttribute("categoriesMap", children);
                request.getRequestDispatcher("/categoryCreateAdmin.ftl").forward(request, response);
                return;
            }
        }else{
            request.setAttribute("error", "Insufficient parameters given.");
            HashMap<String, ArrayList> children = catCtrl.populateHashmapWithCategories(0);
            request.setAttribute("categoriesMap", children);
            request.getRequestDispatcher("/categoryCreateAdmin.ftl").forward(request, response);
            return;
        }
    }

    @Override
    public String getServletInfo() {
        return "Category UI";
    }// </editor-fold>

}
