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
public class ApproveUserAdminUI extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        // Get current session
        HttpSession session = request.getSession(true);
        request.setAttribute("baseContext", session.getServletContext().getContextPath());
        LoginControl loginCtrl = new LoginControl();
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

        // If user ID is set, then we approve a user
        ApproveUserAdminControl usrCtrl = new ApproveUserAdminControl();
        String userID = request.getParameter("id");
        if(userID != null) {
            try {
                // Get ID from string
                long id = Long.parseLong(userID, 10);

                // Do approval
                if(usrCtrl.approve(id)) {
                    request.setAttribute("message", "User approved!");
                }else{
                    if(usrCtrl.hasError()) {
                        request.setAttribute("error", "Error approving user: " + usrCtrl.getError());
                    }else{
                        request.setAttribute("error", "Internal error approving user.");
                    }
                }
            }
            catch(NumberFormatException e) {
                request.setAttribute("error", "Invalid user ID. Please try again.");
            }
        }

        // Now, grab list of unapproved users to show
        ArrayList<RegisteredUser> unapprovedUsers = usrCtrl.getUnapprovedUsers();
        if(unapprovedUsers == null) {
                if(usrCtrl.hasError()) {
                    request.setAttribute("error", "Error getting users: " + usrCtrl.getError());
                    request.setAttribute("returnTo", "/admin");
                    request.getRequestDispatcher("/genericError.ftl").forward(request, response);
                    return;
                }else{
                    request.setAttribute("error", "Internal error getting users.");
                    request.setAttribute("returnTo", "/admin");
                    request.getRequestDispatcher("/genericError.ftl").forward(request, response);
                    return;
                }
        }
        request.setAttribute("unapprovedUsers", unapprovedUsers);

        // Display
        request.getRequestDispatcher("/approveUsersAdmin.ftl").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doGet(request, response);
    }

    @Override
    public String getServletInfo() {
        return "Admin User Approval UI";
    }// </editor-fold>

}
