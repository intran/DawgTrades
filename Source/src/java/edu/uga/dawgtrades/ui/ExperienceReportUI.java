package edu.uga.dawgtrades.ui;

import edu.uga.dawgtrades.DTException;
import edu.uga.dawgtrades.control.ExperienceReportControl;
import edu.uga.dawgtrades.control.LoginControl;
import edu.uga.dawgtrades.model.ExperienceReport;
import edu.uga.dawgtrades.model.RegisteredUser;
import java.io.IOException;
import java.util.ArrayList;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class ExperienceReportUI extends HttpServlet {

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String userId = "";
        RegisteredUser currentUser = null;
        HttpSession session = request.getSession(true);
        request.setAttribute("baseContext", session.getServletContext().getContextPath());
        LoginControl ctrl = new LoginControl();
        if (!ctrl.checkIsLoggedIn(session)) {
            response.sendRedirect("/login");
            request.setAttribute("loggedInUser", "");
            request.removeAttribute("loggedInUser");
            return;
        } else {
            currentUser = (RegisteredUser) session.getAttribute("currentSessionUser");
            request.setAttribute("loggedInUser", currentUser);            
            
        }
        fillUsersList(request, response);
        if (request.getParameter("show") != null) {
            userId = request.getParameter("listId");
            request.setAttribute("userselct", userId);
            fillreportList(request, response, Long.parseLong(userId));
        }

        // Insert
        if (request.getParameter("add") != null) {
            userId = request.getParameter("listId");
            request.setAttribute("userselct", userId);
            String reportvalue = request.getParameter("report");
            String rate = request.getParameter("rate");
            ExperienceReportControl userCtrl = new ExperienceReportControl();

            if (reportvalue != null && rate != null) {
                try {
                    if (!userCtrl.attemptToWriteExperienceReport(currentUser, Long.parseLong(userId), Integer.parseInt(rate), reportvalue)) {
                        request.setAttribute("error", "Error: " + userCtrl.getError());
                        request.getRequestDispatcher("/experience.ftl").forward(request, response);
                    }
                } catch (DTException ex) {
                    request.setAttribute("error", "Error: " + userCtrl.getError() + "  " + ex.toString());
                }
            }
            fillreportList(request, response, Long.parseLong(userId));
        }

        //Update
        if (request.getParameter("update") != null && !request.getParameter("id").isEmpty()) {
            userId = request.getParameter("listId");
            request.setAttribute("userselct", userId);
            String reportvalue = request.getParameter("report");
            String rate = request.getParameter("rate");
            String id = request.getParameter("id");
            ExperienceReportControl userCtrl = new ExperienceReportControl();
            if (reportvalue != null && rate != null) {
                try {
                    if (!userCtrl.attemptToUpdateExperienceReport(Integer.parseInt(rate), reportvalue, Long.parseLong(id))) {
                        request.setAttribute("error", "Error: " + userCtrl.getError());
                        request.getRequestDispatcher("/experience.ftl").forward(request, response);
                    }
                } catch (DTException ex) {
                    request.setAttribute("error", "Error: " + userCtrl.getError() + "  " + ex.toString());
                }
            }
            fillreportList(request, response, Long.parseLong(userId));
        }

        //Delete
        if (request.getParameter("delete") != null && !request.getParameter("id").isEmpty()) {
            String id = request.getParameter("id");
            userId = request.getParameter("listId");
            request.setAttribute("userselct", userId);
            ExperienceReportControl userCtrl = new ExperienceReportControl();
            try {
                if (!userCtrl.attemptToDeleteExperienceReport(Integer.parseInt(id))) {
                    request.setAttribute("error", "Error: " + userCtrl.getError());
                    request.getRequestDispatcher("/experience.ftl").forward(request, response);
                }
            } catch (DTException ex) {
                request.setAttribute("error", "Error: " + userCtrl.getError() + "  " + ex.toString());
            }
            fillreportList(request, response, Long.parseLong(userId));
        }
        request.getRequestDispatcher("/experience.ftl").forward(request, response);
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

    public void fillUsersList(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        ExperienceReportControl userCtrl = new ExperienceReportControl();
        ArrayList<RegisteredUser> userList;
        try {
            userList = userCtrl.getAllUsers();
            if (userList != null) {
                request.setAttribute("users", userList);
            } else if (userCtrl.hasError()) {
                request.setAttribute("error", "Error: " + userCtrl.getError());
            }
        } catch (DTException ex) {
            request.setAttribute("error", "Error: " + userCtrl.getError());
            request.getRequestDispatcher("/experience.ftl").forward(request, response);
        }
    }

    public void fillreportList(HttpServletRequest request, HttpServletResponse response, long user_id)
            throws ServletException, IOException {
        ExperienceReportControl reportCtrl = new ExperienceReportControl();
        ArrayList<ExperienceReport> reportList;
        try {
            reportList = reportCtrl.getAllRepotsOfUser(user_id);
            if (reportList != null) {
                request.setAttribute("reports", reportList);

            } else if (reportCtrl.hasError()) {
                request.setAttribute("error", "Error: " + reportCtrl.getError());
                request.getRequestDispatcher("/experience.ftl").forward(request, response);
            }
        } catch (DTException ex) {
            request.setAttribute("error", "Error: " + reportCtrl.getError());
            request.getRequestDispatcher("/experience.ftl").forward(request, response);
        }
    }
}
