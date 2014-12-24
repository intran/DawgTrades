package edu.uga.dawgtrades;

import javax.servlet.*;
import java.util.*;
import edu.uga.dawgtrades.model.*;
import edu.uga.dawgtrades.model.impl.*;
import edu.uga.dawgtrades.persistence.impl.*;
import edu.uga.dawgtrades.persistence.*;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;


public class DawgTradesContextListener implements ServletContextListener {
    private class WorkerThreadClass implements Runnable {
        public boolean running = true;
        public void run() {
            while(running) {
                try{
                    Connection conn = null;
                    Statement stmt = null;
                    ResultSet auctionSet = null;
                    Statement bidstmt = null;
                    Statement updateStmt = null;
                    ResultSet bidSet = null;
                    try {
                        conn = DbUtils.connect();
                        // Close expired auctions
                        String selectAuctionSql = "select auct.id,auct.expiration,auct.isClosed,auct.item_id, it.id, it.user_id, user.id, user.email, user.firstName " +
                            "FROM Auction auct, RegisteredUser user, Item it WHERE auct.isClosed != 1 AND auct.item_id = it.id AND it.user_id = user.id";
                        stmt = conn.createStatement();
                        stmt = conn.createStatement();
                        auctionSet = stmt.executeQuery(selectAuctionSql);

                        while(auctionSet.next()) {
                            java.sql.Timestamp timestamp = auctionSet.getTimestamp(2);
                            if(timestamp == null) {
                                System.out.println("[LISTENER] [WARN] Encountered null timestamp.");
                                continue;
                            }
                            Date expiration = new java.util.Date(timestamp.getTime());
                            Date now = new Date();
                            if(now.getTime() >= expiration.getTime()) {
                                System.out.println("[LISTENER] Auction expired. ID = " + auctionSet.getLong(1));
                                String selectBid = "SELECT b.id,b.user_id,b.auction_id,user.id,user.email,user.firstName FROM Bid b, RegisteredUser user WHERE b.auction_id = " + auctionSet.getLong(1) + " AND b.user_id = user.id ORDER BY amount DESC LIMIT 1";
                                bidstmt = conn.createStatement();
                                bidSet = bidstmt.executeQuery(selectBid);
                                if(bidSet.next()) {
                                    System.out.println("[LISTENER] Auction has winning bid.");

                                    updateStmt = conn.createStatement();
                                    updateStmt.executeUpdate("UPDATE Auction SET isClosed = 1 WHERE id = " + auctionSet.getLong(1));

                                    // Email owner + winner
                                    String winnerEmail = bidSet.getString(5);
                                    String winnerName = bidSet.getString(6);
                                    String ownerEmail = auctionSet.getString(8);
                                    String ownerName = auctionSet.getString(9);
                                    System.out.println("[LISTENER] Sending email to " + winnerName + " (" + winnerEmail + ") (Winner)");
                                    if(DawgTradesMailer.sendMail(winnerEmail, "Auction Won", "Congratulations, " + winnerName + ", you won an auction. View the auction at http://dawgtrades.devisedby.us/auction?id=" + auctionSet.getLong(1) + "." )) {
                                        System.out.println("[LISTENER] Winner mail sent.");
                                    }else{
                                        System.out.println("[LISTENER] Failed to send winner mail.");
                                    }
                                    System.out.println("[LISTENER] Sending email to " + ownerName + " (" + ownerEmail + ") (Owner)");
                                    if(DawgTradesMailer.sendMail(ownerEmail, "Auction Won", "Congratulations, " + ownerName + ", there was a winning bid on one of your auctions. View the auction at http://dawgtrades.devisedby.us/auction?id=" + auctionSet.getLong(1) + "." )) {
                                        System.out.println("[LISTENER] Winner mail sent.");
                                    }else{
                                        System.out.println("[LISTENER] Failed to send winner mail.");
                                    }
                                }else{
                                    // Expired without winner
                                    long itemID = auctionSet.getLong(4);
                                    System.out.println("[LISTENER] Auction has NO winning bid. Deleting item with ID = " + itemID);
                                    updateStmt = conn.createStatement();
                                    updateStmt.executeUpdate("DELETE FROM Item WHERE id = " + itemID);
                                }
                            }
                        }
                    }
                    catch (SQLException e ) {
                        System.out.println("[LISTENER] Exception in Listener during Auction Closing: " + e.getMessage());
                    }
                    finally{
                        if(conn != null) {
                            conn.close();
                        }
                        if(stmt != null) {
                            stmt.close();
                        }
                        if(bidstmt != null) {
                            bidstmt.close();
                        }
                        if(updateStmt != null) { 
                            updateStmt.close();
                        }
                    }
                    Thread.sleep(1000);
                }
                catch(InterruptedException e) {return;}
                catch(Exception e) {
                    System.out.println("[LISTENER] Exception in Listener: " + e.getMessage());
                }
            }
        }
    }
    private Thread myThread = null;
    private WorkerThreadClass worker = null;

    public void contextInitialized(ServletContextEvent sce) {
        if ((myThread == null) || (!myThread.isAlive())) {
            worker = new WorkerThreadClass();
            myThread = new Thread(worker);
            myThread.start();
        }
    }

    public void contextDestroyed(ServletContextEvent sce){
        try {
            worker.running = false;
            myThread.interrupt();
        } catch (Exception ex) {
        }
    }
}