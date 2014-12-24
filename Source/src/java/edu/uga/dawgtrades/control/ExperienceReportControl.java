package edu.uga.dawgtrades.control;

import edu.uga.dawgtrades.DTException;
import edu.uga.dawgtrades.model.ExperienceReport;
import edu.uga.dawgtrades.model.Membership;
import edu.uga.dawgtrades.model.ObjectModel;
import edu.uga.dawgtrades.model.RegisteredUser;
import edu.uga.dawgtrades.model.impl.ObjectModelImpl;
import edu.uga.dawgtrades.persistence.Persistence;
import edu.uga.dawgtrades.persistence.impl.DbUtils;
import edu.uga.dawgtrades.persistence.impl.PersistenceImpl;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import javax.servlet.http.HttpSession;

public class ExperienceReportControl {

    private Connection conn = null;
    private ObjectModel objectModel = null;
    private Persistence persistence = null;
    private String error = "Error Unknown";
    private boolean hasError = false;

    private void connect() throws DTException {

        conn = DbUtils.connect();
        objectModel = new ObjectModelImpl();
        persistence = new PersistenceImpl(conn, objectModel);
        objectModel.setPersistence(persistence);

    }

    private void close() {
        try {
            conn.close();
        } catch (Exception e) {
            System.err.println("Exception: " + e);
        }
    }

    public ArrayList<RegisteredUser> getAllUsers() throws DTException {
        Iterator<RegisteredUser> userIter = null;
        ArrayList<RegisteredUser> userMap = new ArrayList<RegisteredUser>();
        try {
            connect();
            RegisteredUser modelUser = objectModel.createRegisteredUser();
            userIter = objectModel.findRegisteredUser(modelUser);
            while (userIter.hasNext()) {
                userMap.add(userIter.next());
            }

        } catch (DTException e) {
            error = e.getMessage();

        } finally {
            close();
        }
        return userMap;
    }

    public ArrayList<ExperienceReport> getAllRepotsOfUser(long user_id) throws DTException {
        Iterator<ExperienceReport> reportIter = null;
        Iterator<RegisteredUser> userIter = null;
        RegisteredUser runningUser = null;
        ArrayList<ExperienceReport> userReportMap = new ArrayList<ExperienceReport>();
        try {
            connect();

            RegisteredUser modelUser = objectModel.createRegisteredUser();
            modelUser.setId(user_id);
            userIter = objectModel.findRegisteredUser(modelUser);
            if (userIter.hasNext()) {

                runningUser = userIter.next();
                ExperienceReport modelReport = objectModel.createExperienceReport();
                modelReport.setReviewed(runningUser);
                reportIter = objectModel.findExperienceReport(modelReport);
                while (reportIter.hasNext()) {
                    userReportMap.add(reportIter.next());
                }

            } else {
                error = "user not found";
            }

        } catch (DTException e) {
            error = e.getMessage();

        } finally {
            close();
        }
        return userReportMap;
    }

    public boolean attemptToWriteExperienceReport(RegisteredUser reviewer, long reviewed_id, int rate, String report) throws DTException {
        ExperienceReport modelExperienceReport = null;
        Iterator<RegisteredUser> userIter = null;
        RegisteredUser reviewedUser = null;
        try {

            connect();
            RegisteredUser modelUser = objectModel.createRegisteredUser();
            modelUser.setId(reviewed_id);
            userIter = objectModel.findRegisteredUser(modelUser);
            if (userIter.hasNext()) {

                reviewedUser = userIter.next();
                modelExperienceReport = objectModel.createExperienceReport(reviewer, reviewedUser, rate, report, new Date());
                objectModel.storeExperienceReport(modelExperienceReport);
                return true;
            } else {
                error = "User not Found";
                return false;
            }
        } catch (DTException e) {
            error = e.getMessage();
            return false;
        } finally {
            close();
        }

    }

    public boolean attemptToUpdateExperienceReport(int rate, String report, long id) throws DTException {
        ExperienceReport modelExperienceReport = null;

        Iterator<ExperienceReport> reportIter = null;
        ExperienceReport reportCurrent = null;
        try {

            connect();

            modelExperienceReport = objectModel.createExperienceReport();
            modelExperienceReport.setId(id);

            reportIter = objectModel.findExperienceReport(modelExperienceReport);

            while (reportIter.hasNext()) {
                reportCurrent = reportIter.next();
            }
            reportCurrent.setRating(rate);
            reportCurrent.setReport(report);
            reportCurrent.setDate(new Date());
            objectModel.storeExperienceReport(reportCurrent);
            return true;

        } catch (DTException e) {
            error = e.getMessage();
            return false;
        } finally {
            close();
        }

    }

    public boolean attemptToDeleteExperienceReport(long id) throws DTException {
        ExperienceReport modelExperienceReport = null;

        Iterator<ExperienceReport> reportIter = null;
        ExperienceReport reportCurrent = null;
        try {
            connect();
            modelExperienceReport = objectModel.createExperienceReport();
            modelExperienceReport.setId(id);
            reportIter = objectModel.findExperienceReport(modelExperienceReport);
            while (reportIter.hasNext()) {
                reportCurrent = reportIter.next();
            }
            objectModel.deleteExperienceReport(reportCurrent);
            return true;

        } catch (DTException e) {
            error = e.getMessage();
            return false;
        } finally {
            close();
        }
    }

    public String getError() {
        String err = null;
        if (this.hasError) {
            err = this.error;
            this.error = null;
            this.hasError = false;
        }
        return err;
    }

    public boolean hasError() {
        return this.hasError;
    }

}
