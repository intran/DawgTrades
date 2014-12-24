package edu.uga.dawgtrades.persistence.impl;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Iterator;
import edu.uga.dawgtrades.DTException;
import edu.uga.dawgtrades.model.ExperienceReport;
import edu.uga.dawgtrades.model.ObjectModel;
import java.sql.PreparedStatement;

public class ExperienceReportManager {

    private ObjectModel objectModel = null;
    private Connection conn = null;

    public ExperienceReportManager(Connection conn, ObjectModel objectmodel) {
        this.objectModel = objectmodel;
        this.conn = conn;
    }

    public void save(ExperienceReport er) throws DTException {
        String insertERSQL = "INSERT INTO ExperienceReport(reviewer_id,reviewed_id,rating,report,date)VALUES(?,?,?,?,?)";
        String updateERSQL = "UPDATE ExperienceReport SET reviewer_id=?,reviewed_id=?,rating=?,report=?,date=? where id = ?";
        PreparedStatement stmt = null;
        ResultSet rs = null;
        int inscnt;
        long erID;
        
        try {
            if (!er.isPersistent()) {
                stmt = (PreparedStatement) conn.prepareStatement(insertERSQL);
            } else {
                stmt = (PreparedStatement) conn.prepareStatement(updateERSQL);
            }
            stmt.setLong(1, er.getReviewer().getId());
            stmt.setLong(2, er.getReviewed().getId());
            stmt.setInt(3, er.getRating());
            stmt.setString(4, er.getReport());
            stmt.setDate(5, new java.sql.Date(er.getDate().getTime()));

            if( er.isPersistent() )
                stmt.setLong( 6, er.getId() );
            
            inscnt = stmt.executeUpdate();
            
            if (!er.isPersistent()) {
                if (inscnt >= 1) {
                    String sql = "select last_insert_id()";
                    if (stmt.execute(sql)) { // statement returned a result

                        // retrieve the result
                        ResultSet r = stmt.getResultSet();
                        // we will use only the first row!
                        //
                        while (r.next()) {
                            // retrieve the last insert auto_increment value
                            erID = r.getLong(1);
                            if (erID > 0) {
                                er.setId(erID); // set this auction's db id (proxy object)
                            }
                        }
                    }
                } else {
                    throw new DTException("ExperienceManager.save: failed to save a Experience");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DTException("ExperienceManager.save: failed to save a Experience: " + e);
        }

    }

    public Iterator<ExperienceReport> restore(ExperienceReport er) throws DTException {
        String selectErSql = "select id,reviewer_id,reviewed_id,rating,report,date from ExperienceReport";
        Statement stmt = null;
        StringBuffer query = new StringBuffer(100);
        StringBuffer condition = new StringBuffer(100);
        condition.setLength(0);
        query.append(selectErSql);
        if (er != null) {
            if (er.getId() >= 0) {
                query.append(" where id=" + er.getId());
            } else {
                if (er.getReviewer() != null) {
                    condition.append(" reviewer_id = " + er.getReviewer().getId());
                }
                if (er.getReviewed() != null) {
                    if (condition.length() > 0) {
                        condition.append(" and");
                    }
                    condition.append(" reviewed_id = " + er.getReviewed().getId());
                }
                if (er.getRating() > 0) {
                    if (condition.length() > 0) {
                        condition.append(" and");
                    }
                    condition.append(" rating = " + er.getRating());
                }
                if (er.getDate() != null) {
                    if (condition.length() > 0) {
                        condition.append(" and");
                    }
                    condition.append(" date = " + er.getDate());
                }
                if (condition.length() > 0) {
                    query.append(" where ");
                    query.append(condition);
                }
            }
        }
        try {
            stmt = conn.createStatement();
            if (stmt.execute(query.toString())) {
                ResultSet r = stmt.getResultSet();
                return new ExperienceReportIterator(r, objectModel);
            }
        } catch (Exception e) { // just in case...
            throw new DTException("ExperienceReportManager.restore: Could not restore persistent ExperienceReport object; Root cause: " + e);
        }
        throw new DTException("ExperienceReportManager.restore: Could not restore persistent ExperienceReport object");
    }

    public void delete(ExperienceReport er) throws DTException {
        String deleteErSql = "DELETE FROM ExperienceReport WHERE id = ?";
        PreparedStatement stmt = null;
        int inscnt;

        if (!er.isPersistent()) {
            return;
        }
        try {
            stmt = (PreparedStatement) conn.prepareStatement(deleteErSql);
            stmt.setLong(1, er.getId());
            inscnt = stmt.executeUpdate();
            if (inscnt == 1) {
                return;
            } else {
                throw new DTException("ExperienceReportManager.delete: failed to delete an experience report");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DTException("ExperienceReportManager.delete: failed to delete an experience report " + e);
        }
    }

}
