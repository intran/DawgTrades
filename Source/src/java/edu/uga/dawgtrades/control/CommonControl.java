package edu.uga.dawgtrades.control;

import edu.uga.dawgtrades.DTException;
import edu.uga.dawgtrades.model.ObjectModel;
import edu.uga.dawgtrades.model.RegisteredUser;
import edu.uga.dawgtrades.model.impl.ObjectModelImpl;
import edu.uga.dawgtrades.persistence.Persistence;
import edu.uga.dawgtrades.persistence.impl.DbUtils;
import edu.uga.dawgtrades.persistence.impl.PersistenceImpl;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.util.Iterator;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpSession;
import java.util.*;

public class CommonControl{
	public static final Map<String, String> infoMessages;
    static
    {
        infoMessages = new HashMap<String, String>();
        infoMessages.put(
        	"c3daec042ebd773d1d2e505235f1449c4ff1625f",
        	"Registration successful! Login with your username and password below."
    	);
        infoMessages.put(
        	"556961b3f4bbd252ff169bbf5502611444faa0de",
        	"Bid placed!"
    	);
        infoMessages.put(
        	"4b1b068d61f5815e55092a8af8fac6ed71936b42",
        	"Reset email sent! Check your mail."
    	);
    }
	public boolean isAdmin(RegisteredUser user)throws ServletException, IOException,DTException{
		Connection conn = null;
		ObjectModel objectModel = null;
		Persistence persistence = null;
		try{
			conn = DbUtils.connect();
			objectModel = new ObjectModelImpl();
			persistence = new PersistenceImpl(conn,objectModel);
			objectModel.setPersistence(persistence);

			Iterator<RegisteredUser> userIter = null;
			RegisteredUser runningUser = null;
			userIter = objectModel.findRegisteredUser(user);
			if(userIter.hasNext()){
				runningUser = userIter.next();
				return runningUser.getIsAdmin();
			}
		}
		finally{
			try{
				conn.close();
			}catch(Exception e){
				System.err.println("Exception: "+e);
			}
		}	
		
		return false;
				
	}
	
}
