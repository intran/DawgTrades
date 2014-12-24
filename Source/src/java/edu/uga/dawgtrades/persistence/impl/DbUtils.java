package edu.uga.dawgtrades.persistence.impl;




import edu.uga.dawgtrades.DTException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;


public class DbUtils {


    public static void disableAutoCommit( Connection conn ) 
            throws  DTException 
    {
        try {
            conn.setAutoCommit(false);
        } 
        catch( SQLException ex ) {
            throw new  DTException( "DbUtils.disableAutoCommit: Transaction error. " + ex.getMessage() );
        }
    }

    public static void enableAutoCommit( Connection conn ) 
            throws  DTException 
    {
        try {
            conn.setAutoCommit(true);
        } 
        catch( SQLException ex ) {
            throw new  DTException( "DbUtils.enableAutoCommit: Transaction error. " + ex.getMessage() );
        }
    }

    public static void commit(Connection conn) 
            throws  DTException 
    {
        try {
            conn.commit();
        } catch (SQLException ex) {
            throw new  DTException( "DbUtils.commit: SQLException on commit " + ex.getMessage() );
        }
    }

    
    public static void rollback(Connection conn) 
            throws  DTException 
    {
        try {
            conn.rollback();
        } catch (SQLException ex) {
            throw new  DTException( "DbUtils.rollback: Unable to rollback transaction. " + ex.getMessage() );
        }
    }

    
    public static Connection connect() 
            throws  DTException 
    {
        try {
            Class.forName(DbAccessConfig.DB_DRIVE_NAME);
        } 
        catch (Exception ex) {
            throw new  DTException( "DbUtils.connect: Unable to find Driver" );
        }
        try {
            return DriverManager.getConnection( DbAccessConfig.DB_CONNECTION_URL,
                                                DbAccessConfig.DB_CONNECTION_USERNAME,
                                                DbAccessConfig.DB_CONNECTION_PWD );
        } 
        catch (Exception ex) {
            throw new  DTException( "DbUtils.connect: Unable to connect to database " + ex.getMessage() );
        }
    }

}
