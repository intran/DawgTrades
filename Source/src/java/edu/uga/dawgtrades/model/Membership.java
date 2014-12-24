package edu.uga.dawgtrades.model;

import java.util.Date;

public interface Membership extends Persistable {

    double getPrice();

    void setPrice(double price);

    Date getDate();

}
