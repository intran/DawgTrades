package edu.uga.dawgtrades.model.impl;

import edu.uga.dawgtrades.model.*;

import java.util.Date;

public class ExperienceReportImpl extends Persistent implements ExperienceReport {

    private RegisteredUser reviewer;
    private RegisteredUser reviewed;
    private int rating;
    private String report;
    private Date date;

    public ExperienceReportImpl() {
    }

    public ExperienceReportImpl(RegisteredUser reviewer, RegisteredUser reviewed, int rating, Date date) {
        this.reviewer = reviewer;
        this.reviewed = reviewed;
        this.rating = rating;
        this.date = date;
    }

    public ExperienceReportImpl(RegisteredUser reviewer, RegisteredUser reviewed, int rating, String report, Date date) {
        this.reviewer = reviewer;
        this.reviewed = reviewed;
        this.rating = rating;
        this.report = report;
        this.date = date;
    }

    
    public RegisteredUser getReviewer() {
        return this.reviewer;
    }

    public void setReviewer(RegisteredUser reviewer) {
        this.reviewer = reviewer;
    }

    public RegisteredUser getReviewed() {
        return this.reviewed;
    }

    public void setReviewed(RegisteredUser reviewed) {
        this.reviewed = reviewed;
    }

    public int getRating() {
        return this.rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public String getReport() {
        return this.report;
    }

    public void setReport(String report) {
        this.report = report;
    }

    public Date getDate() {
        return this.date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String toString() {
        return "ExperienceReport[" + getId() + "] " + getReviewer() + " " + getReviewed() + " " + getRating() + " " + getDate() + " " + getReport();
    }

    public boolean equals(Object otherExperienceReport) {
        if (otherExperienceReport == null) {
            return false;
        }
        if (otherExperienceReport instanceof ExperienceReport) // name is a unique attribute
        {
            return (getReviewer() == ((ExperienceReport) otherExperienceReport).getReviewer()
                    && getReviewed() == ((ExperienceReport) otherExperienceReport).getReviewed()
                    && getRating() == ((ExperienceReport) otherExperienceReport).getRating()
                    && getDate() == ((ExperienceReport) otherExperienceReport).getDate()
                    && getReport() == ((ExperienceReport) otherExperienceReport).getReport());
        }
        return false;
    }

}
