package edu.uga.dawgtrades.ui;


import edu.uga.dawgtrades.DTException;
import edu.uga.dawgtrades.control.SettingsCtrl;
import edu.uga.dawgtrades.control.LoginControl;
import edu.uga.dawgtrades.model.RegisteredUser;
import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class AdminPanelUI extends HttpServlet{

	 @Override
        protected void doGet(HttpServletRequest request, HttpServletResponse response)throws ServletException, IOException{
        // Ensure logged in
		HttpSession session = request.getSession(true);
		request.setAttribute("baseContext", session.getServletContext().getContextPath());
		LoginControl ctrl = new LoginControl();
		if(!ctrl.checkIsLoggedIn(session)){
			response.sendRedirect("/login");
			return;
		}else{
			RegisteredUser currentUser = (RegisteredUser)session.getAttribute("currentSessionUser");

			// Check authorization
			if(!currentUser.getIsAdmin()) {
				response.sendRedirect("/");
				return;
			}
			request.setAttribute("loggedInUser",currentUser);
		}
		
		// Show page
		request.getRequestDispatcher("/adminsettings.ftl").forward(request,response);

	}

	 @Override
        protected void doPost(HttpServletRequest request, HttpServletResponse response)throws ServletException, IOException{
	
	
	}

}


