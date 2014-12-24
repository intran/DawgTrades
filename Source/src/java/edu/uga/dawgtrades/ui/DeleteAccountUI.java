package edu.uga.dawgtrades.ui;

import edu.uga.dawgtrades.model.*;
import java.io.*;
import java.util.*;
import javax.servlet.http.*;
import javax.servlet.*;
import edu.uga.dawgtrades.DTException;
import edu.uga.dawgtrades.control.*;

public class DeleteAccountUI extends HttpServlet{

	 @Override
        protected void doGet(HttpServletRequest request, HttpServletResponse response)throws ServletException, IOException{
		HttpSession session = request.getSession(true);
        request.setAttribute("baseContext", session.getServletContext().getContextPath());
                LoginControl ctrl = new LoginControl();
		DeleteAccountControl deleteControl = new DeleteAccountControl();
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
		String confirm = request.getParameter("confirm");
		if(confirm != null){
			if(confirm.equals("yes")){
				RegisteredUser currentUser = (RegisteredUser)session.getAttribute("currentSessionUser");

				if(!deleteControl.attemptDelete(currentUser.getId())){
					request.setAttribute("error",deleteControl.getError());
				}else{
					response.sendRedirect("/");
					return;
				}
			}else{
				response.sendRedirect("/settings");
				return;
			}
		}
		request.getRequestDispatcher("/deleteAccount.ftl").forward(request,response);
	}
	
	 @Override
        protected void doPost(HttpServletRequest request, HttpServletResponse response)throws ServletException, IOException{
		doGet(request,response);
	}		
	
}
