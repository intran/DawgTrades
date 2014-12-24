package edu.uga.dawgtrades.control;

import edu.uga.dawgtrades.DTException;
import edu.uga.dawgtrades.control.LoginControl;
import edu.uga.dawgtrades.model.ObjectModel;
import edu.uga.dawgtrades.model.RegisteredUser;
import edu.uga.dawgtrades.model.impl.ObjectModelImpl;
import edu.uga.dawgtrades.persistence.Persistence;
import edu.uga.dawgtrades.persistence.impl.DbUtils;
import edu.uga.dawgtrades.persistence.impl.PersistenceImpl;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.util.*;
import javax.servlet.*;
import javax.servlet.http.*;

public class SettingsCtrl{
	private Connection conn = null;
        private ObjectModel objectModel = null;
        private Persistence persistence = null;
        private String error="Error Unknown";
        private boolean hasError = false;
        private LoginControl ctrl = new LoginControl();

        private void connect() throws DTException{
                conn = DbUtils.connect();
                objectModel = new ObjectModelImpl();
                persistence = new PersistenceImpl(conn,objectModel);
                objectModel.setPersistence(persistence);
        }

        private void close(){
                try{
                        conn.close();
                }catch(Exception e){
                        System.err.println("Exception: "+e);
                }
        }
/*
	public boolean updatePassword(String password){
				
	}
	public boolean updateName(String name){
	
	}
	public boolean updatePhoneNumber(String name){
	
	}
*/
}
