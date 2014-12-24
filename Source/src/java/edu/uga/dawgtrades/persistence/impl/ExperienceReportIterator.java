package edu.uga.dawgtrades.persistence.impl;

import java.sql.ResultSet;
import java.util.Date;
import java.util.Iterator;
import java.util.NoSuchElementException;

import edu.uga.dawgtrades.DTException;
import edu.uga.dawgtrades.model.ExperienceReport;
import edu.uga.dawgtrades.model.ObjectModel;
import edu.uga.dawgtrades.model.RegisteredUser;
import edu.uga.dawgtrades.model.impl.RegisteredUserImpl;
import edu.uga.dawgtrades.model.impl.ExperienceReportImpl;

public class ExperienceReportIterator implements Iterator<ExperienceReport> {

    private ResultSet rs = null;
    private ObjectModel objectModel = null;
    private boolean more = false;

    public ExperienceReportIterator(ResultSet rs, ObjectModel objectModel)
            throws DTException {

        this.rs = rs;
        this.objectModel = objectModel;

        try {
            more = rs.next();
        } catch (Exception e) {
            throw new DTException("ExperienceReportIterator: Cannot create bid iterator; root cause: " + e);
        }

    }

    public boolean hasNext() {
        return more;
    }

    public ExperienceReport next() {
        //result: (long) id, (long) reviewer_id, (long)reviewed_id,(int)rating,(string)report,(date)date
        ExperienceReport er = new ExperienceReportImpl();

        if (more) {
            try {
                RegisteredUser userReviewer = objectModel.createRegisteredUser();
                RegisteredUser userReviewed = new RegisteredUserImpl();
                RegisteredUser reviewerUser = null;
                Iterator<RegisteredUser> userIter = null;
                er.setId(rs.getLong(1));
                userReviewer.setId(rs.getLong(2));
                userIter = objectModel.findRegisteredUser(userReviewer);
                if (userIter.hasNext()) {
                    reviewerUser = userIter.next();
                }
                er.setReviewer(reviewerUser);
                userReviewed.setId(rs.getLong(3));
                er.setReviewed(userReviewed);
                er.setRating(rs.getInt(4));
                er.setReport(rs.getString(5));
                er.setDate(rs.getDate(6));
                more = rs.next();
                return er;

            } catch (Exception e) {
                throw new NoSuchElementException("ExperienceReportIterator: No next experience report; Root Cause: " + e);
            }
        }
        //temp return statement
        return null;
    }

    public void remove() {
        throw new UnsupportedOperationException();
    }

};
