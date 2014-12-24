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
 * @author reanimus
 */
public class EditAttributeAdminUI extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        // Get current session
        HttpSession session = request.getSession(true);
        request.setAttribute("baseContext", session.getServletContext().getContextPath());
        LoginControl loginCtrl = new LoginControl();
        CategoryControl catCtrl = new CategoryControl();
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
        String attributeTypeID = request.getParameter("id");
        if(attributeTypeID != null) {
            try {
                long id = Long.parseLong(attributeTypeID, 10);
                if(attrTypeCtrl.attributeTypeExists(id)) {
                    request.setAttribute("myID", Long.valueOf(id).toString());
                    request.setAttribute("parent", catCtrl.getCategoryWithID(attrTypeCtrl.getCategoryIDForAttributeTypeID(id)).getName());
                    request.setAttribute("isString", attrTypeCtrl.getIsString(id) == 1);
                    request.setAttribute("name", attrTypeCtrl.getName(id));
                    request.getRequestDispatcher("/attributeEditAdmin.ftl").forward(request, response);
                }else{
                    request.setAttribute("error", "Attribute doesn't exist.");
                    request.setAttribute("returnTo", "/admin/categories");
                    request.getRequestDispatcher("/genericError.ftl").forward(request, response);
                    return; 
                }
            }catch(NumberFormatException e) {
                request.setAttribute("error", "Invalid attribute given: Not a number.");
                request.setAttribute("returnTo", "/admin/categories");
                request.getRequestDispatcher("/genericError.ftl").forward(request, response);
                return; 
            }
        }else{
            request.setAttribute("error", "No attribute given.");
            request.setAttribute("returnTo", "/admin/categories");
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
        String name = request.getParameter("name");
        String attributeTypeID = request.getParameter("id");
        if(attributeTypeID != null && name != null) {
            if(name.isEmpty()) {
                request.setAttribute("error", "Name is required.");
                this.doGet(request, response);
                return;
            }
            try {
                long id = Long.parseLong(attributeTypeID, 10);
                if(attrTypeCtrl.attributeTypeExists(id)) {
                    if(attrTypeCtrl.updateAttributeType(name, id)) {
                        response.sendRedirect("/admin/categories/edit?id=" + attrTypeCtrl.getCategoryIDForAttributeTypeID(id));
                    }else{
                        if(attrTypeCtrl.hasError()) {
                            request.setAttribute("error", "Error: " + attrTypeCtrl.getError());
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
                request.setAttribute("error", "Invalid attribute type ID given: Not a number.");
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
        return "Category UI";
    }// </editor-fold>

}
