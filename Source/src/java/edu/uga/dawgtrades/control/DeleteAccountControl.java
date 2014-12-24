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

public class DeleteAccountControl{

        private Connection conn = null;
        private ObjectModel objectModel = null;
        private Persistence persistence = null;
        private String error="Error Unknown";

        private void connect() throws DTException{

                        conn = DbUtils.connect();
                        objectModel = new ObjectModelImpl();
                        persistence = new PersistenceImpl(conn,objectModel);
                        objectModel.setPersistence(persistence);

        }
        private void close(){
                try{
                        conn.close();
                }catch (Exception e){
                        System.err.println("Exception: "+e);
                }
        }
	public boolean attemptDelete(long userId){
		try{
			this.connect();
			RegisteredUser modelUser = this.objectModel.createRegisteredUser();
			modelUser.setId(userId);
			this.objectModel.deleteRegisteredUser(modelUser);
			return true;
		}catch(DTException e){
			this.error = e.getMessage();
			return false;
		}finally{
			this.close();
		}
	}
	public String getError(){
		return this.error;
	}

}
