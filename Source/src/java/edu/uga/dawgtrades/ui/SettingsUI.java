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

public class SettingsUI extends HttpServlet{

	 @Override
        protected void doGet(HttpServletRequest request, HttpServletResponse response)throws ServletException, IOException{
		HttpSession session = request.getSession(true);
        request.setAttribute("baseContext", session.getServletContext().getContextPath());
		LoginControl ctrl = new LoginControl();
		if(!ctrl.checkIsLoggedIn(session)){
			response.sendRedirect("/login");
			request.setAttribute("loggedInUser","");
			request.removeAttribute("loggedInUser");
			return;
		}else{
			RegisteredUser currentUser = (RegisteredUser)session.getAttribute("currentSessionUser");
			request.setAttribute("loggedInUser",currentUser);
		}
		String page = request.getParameter("id");
		if(page == null){
			request.setAttribute("profile"," ");
		}else if(page.equals("password")){
			request.setAttribute("password"," ");
		} else if(page.equals( "other")){
			request.setAttribute("other"," ");
		}
		request.getRequestDispatcher("/settings.ftl").forward(request,response);

	}

	 @Override
        protected void doPost(HttpServletRequest request, HttpServletResponse response)throws ServletException, IOException{
	
	
	}

}


