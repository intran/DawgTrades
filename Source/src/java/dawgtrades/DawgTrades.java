package dawgtrades;

import edu.uga.dawgtrades.persistence.impl.DbUtils;
import edu.uga.dawgtrades.test.InsertDowg;
import edu.uga.dawgtrades.test.DeleteDowg;
import edu.uga.dawgtrades.test.UpdateDowg;
import java.sql.Connection;
import java.sql.Statement;

public class DawgTrades {

    public static void main(String[] args) {
        Connection conn = null;
        float result = 0;

        // get a database connection
        try {
            conn = DbUtils.connect();

            Statement stmt = null;

            stmt = conn.createStatement();

            String query = "delete from RegisteredUser";
            stmt.executeUpdate(query);

            query = "delete from Category";
            stmt.executeUpdate(query);

        } catch (Exception e) {      // just in case...
            System.err.println("CurrencyManager: Unable to obtain a database connection");
        
        } finally {
            // close the connection
            try {
                conn.close();
            } catch (Exception e) {
                System.err.println("Exception: " + e);
            }
        }

        InsertDowg insert = new InsertDowg();

        UpdateDowg update = new UpdateDowg();

        DeleteDowg delete = new DeleteDowg();

    }

}
