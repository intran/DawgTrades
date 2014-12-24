package edu.uga.dawgtrades.persistence.impl;

public abstract class DbAccessConfig
{
    /** The fully qualified name of the JDBC driver.
     */
    static final String DB_DRIVE_NAME  = "com.mysql.jdbc.Driver";
    
    /** The database name
     */
    static final String DB_NAME        = "dawgtrades";
    
    /** The database server name for the connection pool
     */
    static final String DB_SERVER_NAME = "localhost";

    /** The JDBC connection string/URL.
     */
    static final String DB_CONNECTION_URL ="jdbc:mysql://localhost:3306/dawgtrades";


    /** The database user name.
     */
    static  String DB_CONNECTION_USERNAME = "root";

    /** The password for the database user.
     */
    static  String DB_CONNECTION_PWD = "root";

}

