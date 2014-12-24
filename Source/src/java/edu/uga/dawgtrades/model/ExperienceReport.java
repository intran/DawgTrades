package edu.uga.dawgtrades.model;

import edu.uga.dawgtrades.DTException;
import java.util.Date;

public interface ExperienceReport
        extends Persistable {

    int getRating();

    void setRating(int rating) throws DTException;

    String getReport();

    void setReport(String report);

    Date getDate();

    void setDate(Date date);

    RegisteredUser getReviewer();

    void setReviewer(RegisteredUser reviewer);

    RegisteredUser getReviewed();

    void setReviewed(RegisteredUser reviewed);
}
