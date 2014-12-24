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
public class DeleteAttributeAdminUI extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        // Get current session
        HttpSession session = request.getSession(true);
        request.setAttribute("baseContext", session.getServletContext().getContextPath());
        LoginControl loginCtrl = new LoginControl();
        AttributeTypeControl attrTypeCtrl = new AttributeTypeControl();
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

        // Check existence
        String attributeID = request.getParameter("id");
        if(attributeID != null) {
            try {
                long id = Long.parseLong(attributeID, 10);
                if(attrTypeCtrl.attributeTypeExists(id)) {
                    request.setAttribute("toDelete", attributeID);
                    request.getRequestDispatcher("/attributeDeleteAdmin.ftl").forward(request, response);
                }else{
                    request.setAttribute("error", "Attribute Type doesn't exist.");
                    request.setAttribute("returnTo", "../categories");
                    request.getRequestDispatcher("/genericError.ftl").forward(request, response);
                    return; 
                }
            }
            catch(NumberFormatException e) {
                request.setAttribute("error", "Bad ID passed.");
                request.setAttribute("returnTo", "../categories");
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
        AttributeTypeControl attrTypeCtrl = new AttributeTypeControl();
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

        // Get the ID
        String attributeID = request.getParameter("id");
        if(attributeID != null) {
            try {
                long id = Long.parseLong(attributeID, 10);
                if(attrTypeCtrl.attributeTypeExists(id)) {
                    // Get the ID to go back to
                    long toRedir = attrTypeCtrl.getCategoryIDForAttributeTypeID(id);

                    // Delete
                    if(attrTypeCtrl.deleteAttributeType(id)) {
                        response.sendRedirect("../categories/edit?id=" + toRedir);
                    }else{
                        if(attrTypeCtrl.hasError()) {
                            request.setAttribute("error", "Error: " + attrTypeCtrl.getError());
                            request.setAttribute("returnTo", "../categories");
                            request.getRequestDispatcher("/genericError.ftl").forward(request, response);
                            return;
                        }else{
                            request.setAttribute("error", "An unknown error occurred.");
                            request.setAttribute("returnTo", "../categories");
                            request.getRequestDispatcher("/genericError.ftl").forward(request, response);
                            return;
                        }
                    }
                }
            }
            catch(NumberFormatException e) {
                request.setAttribute("error", "Bad ID passed.");
                request.setAttribute("returnTo", "../categories");
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
        return "Delete Attribute Type UI";
    }// </editor-fold>

}
