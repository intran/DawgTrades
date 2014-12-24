
package edu.uga.dawgtrades.ui;

import edu.uga.dawgtrades.DTException;
import edu.uga.dawgtrades.control.LoginControl;
import edu.uga.dawgtrades.control.MembershipControl;
import edu.uga.dawgtrades.model.Membership;
import edu.uga.dawgtrades.model.RegisteredUser;
import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;


public class MembershipUI extends HttpServlet {

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
       HttpSession session = request.getSession(true);
        request.setAttribute("baseContext", session.getServletContext().getContextPath());
        LoginControl ctrl = new LoginControl();
        if (!ctrl.checkIsLoggedIn(session)) {
            response.sendRedirect("/login");
            return;
        } else {
            RegisteredUser currentUser = (RegisteredUser)session.getAttribute("currentSessionUser");
            if(!currentUser.getIsAdmin()) {
                response.sendRedirect("/");
                return;
            }
            request.setAttribute("loggedInUser",currentUser);
        }

        MembershipControl membershipCtrl = new MembershipControl();
        ArrayList<Membership> membership;

        String price = request.getParameter("price");
        try {

            if (price != null && !price.isEmpty()) {
                if (!membershipCtrl.attemptToCreateMembership(Double.valueOf(price))) {
                    request.setAttribute("error", "Error: " + membershipCtrl.getError());
                }
            }
            membership = membershipCtrl.getAllMembershipPrices();
            if (membership != null) {
                request.setAttribute("membershipList", membership);
            } else if (membershipCtrl.hasError()) {
                request.setAttribute("error", "Error: " + membershipCtrl.getError());
            }else{
                request.setAttribute("error", "Unknown error.");

            }

        } catch (DTException ex) {
            Logger.getLogger(MembershipUI.class.getName()).log(Level.SEVERE, null, ex);
            request.setAttribute("error", "Error: " + ex.getMessage());
        }
        request.getRequestDispatcher("/membershipAdmin.ftl").forward(request, response);

    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    @Override
    public String getServletInfo() {
        return "Short description";
    }

}
