package edu.uga.dawgtrades.ui;
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
import edu.uga.dawgtrades.DTException;
import edu.uga.dawgtrades.model.*;
import edu.uga.dawgtrades.*;
import edu.uga.dawgtrades.model.impl.*;
import edu.uga.dawgtrades.control.*;
import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 *
 * @author sahar
 */
public class DoPasswordResetUI extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        // Get current session
        HttpSession session = request.getSession(true);
        request.setAttribute("baseContext", session.getServletContext().getContextPath());
        LoginControl ctrl = new LoginControl();
        if(ctrl.checkIsLoggedIn(session))
        {
            // Already logged in, redirect.
            response.sendRedirect("/");
            return;
        }
        String id = request.getParameter("id");
        String key = request.getParameter("key");

        // If not enough params, redirect.
        if(id == null || key == null) {
            response.sendRedirect("/");
            return;
        }
        ViewUserControl vCtrl = new ViewUserControl();
        try {
            long idNumber = Long.parseLong(id, 10);
            RegisteredUser user = vCtrl.getUserWithID(idNumber);
            if(user == null || !user.getPassword().equals(key)) {
                response.sendRedirect("/");
                return;
            }
            request.setAttribute("id", id);
            request.setAttribute("key", key);
            request.setAttribute("user", user);
            request.getRequestDispatcher("/passResetReal.ftl").forward(request, response);
        }
        catch(Exception e) {
            response.sendRedirect("/");
            return;
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        // Get current session
        HttpSession session = request.getSession(true);
        request.setAttribute("baseContext", session.getServletContext().getContextPath());
        LoginControl ctrl = new LoginControl();
        ResetPasswordControl pwCtrl = new ResetPasswordControl();
        if(ctrl.checkIsLoggedIn(session))
        {
            // Already logged in, redirect.
            response.sendRedirect("/");
            return;
        }

        String id = request.getParameter("id");
        String key = request.getParameter("key");
        String pass = request.getParameter("password");
        String rePass = request.getParameter("passwordRepeat");
        request.setAttribute("id", id);
        request.setAttribute("key", key);

        // If not enough params, redirect.
        if(id == null || key == null) {
            response.sendRedirect("/");
            return;
        }
        ViewUserControl vCtrl = new ViewUserControl();
        try {
            long idNumber = Long.parseLong(id, 10);
            RegisteredUser user = vCtrl.getUserWithID(idNumber);
            if(user == null || !user.getPassword().equals(key)) {
                response.sendRedirect("/");
                return;
            }
            request.setAttribute("user", user);
            if(pass == null || rePass == null || pass.isEmpty() || rePass.isEmpty()) {
                request.setAttribute("error", "Password and repeat password are both required.");
                request.getRequestDispatcher("/passResetReal.ftl").forward(request, response);
                return;
            }
            if(!pass.equals(rePass)) {
                request.setAttribute("error", "The passwords do not match..");
                request.getRequestDispatcher("/passResetReal.ftl").forward(request, response);
                return;
            }    

            if(pwCtrl.resetPassword(user, pass)) {
                response.sendRedirect("/");
            }else{
                if(pwCtrl.hasError()) {
                    request.setAttribute("error", pwCtrl.getError());
                    request.getRequestDispatcher("/passResetReal.ftl").forward(request, response);
                } else {
                    request.setAttribute("error", "Password change failed.");
                    request.getRequestDispatcher("/passResetReal.ftl").forward(request, response);
                }
            }
        }
        catch(Exception e) {
            response.sendRedirect("/");
            return;
        }
    }

    @Override
    public String getServletInfo() {
        return "Password Reset UI";
    }// </editor-fold>

}
