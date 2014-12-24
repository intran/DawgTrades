package edu.uga.dawgtrades.model;

import java.util.Date;

public interface Auction
        extends Persistable {

    double getMinPrice();

    void setMinPrice(double minPrice);

    Date getExpiration();

    void setExpiration(Date expiration);

    boolean getIsClosed();

    double getSellingPrice();

    long getItemId();

    void setItemId(long itemId);
}
