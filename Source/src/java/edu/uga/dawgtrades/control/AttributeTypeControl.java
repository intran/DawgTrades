package edu.uga.dawgtrades.control;
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
import edu.uga.dawgtrades.DTException;
import edu.uga.dawgtrades.control.LoginControl;
import edu.uga.dawgtrades.model.*;
import edu.uga.dawgtrades.model.impl.ObjectModelImpl;
import edu.uga.dawgtrades.persistence.Persistence;
import edu.uga.dawgtrades.persistence.impl.DbUtils;
import edu.uga.dawgtrades.persistence.impl.PersistenceImpl;
import edu.uga.dawgtrades.persistence.impl.ItemIterator;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.util.*;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpSession;

/**
 *
 * @author reanimus
 */
public class AttributeTypeControl {

    private Connection conn = null;
    private ObjectModel objectModel = null;
    private Persistence persistence = null;
    private boolean hasError = false;
    private String error;

    public boolean hasError() {
        return this.hasError;
    }

    public String getError() {
        String err = null;
        if (this.hasError) {
            err = this.error;
            this.error = null;
            this.hasError = false;
        }
        return err;
    }

    private void connect() throws DTException{
            this.close();
            this.conn = DbUtils.connect();
            this.objectModel = new ObjectModelImpl();
            this.persistence = new PersistenceImpl(conn,objectModel);
            this.objectModel.setPersistence(persistence);
        
    }
    private void close(){
        try{
            if(this.conn != null) {
                this.conn.close();
            }

        }catch (Exception e){
            System.err.println("Exception: "+e);
        }
    }
    public boolean createAttributeType(long categoryID, String name, boolean isString) {

        // Check category exists
        CategoryControl catCtrl = new CategoryControl();
        if(catCtrl.categoryExists(categoryID)) {
            try {
                this.connect();

                // It does, create + add type
                AttributeType type = this.objectModel.createAttributeType(catCtrl.getCategoryWithID(categoryID), name, isString);
                this.objectModel.storeAttributeType(type);
                return true;
            }
            catch(DTException e) {
                this.hasError = true;
                this.error = e.getMessage();
                return false;
            }
            finally {
                this.close();
            }
        }else{
            return false;
        }
    }

    public long getCategoryIDForAttributeTypeID(long id) {
        try {
            this.connect();
            // Instantiate type
            AttributeType type = this.objectModel.createAttributeType();
            type.setId(id);
            Iterator<AttributeType> results = this.objectModel.findAttributeType(type);

            // Result?
            if(results.hasNext()) {
                // Yep, get + return.
                type = results.next();
                return type.getCategoryId();
            }else{
                // Nope.
                return -1;
            }
        }
        catch(DTException e) {
            this.hasError = true;
            this.error = e.getMessage();
            return -1;
        }
        finally {
            this.close();
        }
    }

    public int getIsString(long id) {
        try {
            this.connect();
            // Instantiate
            AttributeType type = this.objectModel.createAttributeType();
            type.setId(id);
            Iterator<AttributeType> results = this.objectModel.findAttributeType(type);

            // Result?
            if(results.hasNext()) {
                // Yes, just return.
                type = results.next();
                if(type.getIsString()) {
                    return 1;
                }else{
                    return 0;
                }
            }else{
                return -1;
            }
        }
        catch(DTException e) {
            this.hasError = true;
            this.error = e.getMessage();
            return -1;
        }
        finally {
            this.close();
        }
    }

    public String getName(long id) {
        // Instantiate, check, return.
        try {
            this.connect();
            AttributeType type = this.objectModel.createAttributeType();
            type.setId(id);
            Iterator<AttributeType> results = this.objectModel.findAttributeType(type);
            if(results.hasNext()) {
                type = results.next();
                return type.getName();
            }else{
                return null;
            }
        }
        catch(DTException e) {
            this.hasError = true;
            this.error = e.getMessage();
            return null;
        }
        finally {
            this.close();
        }
    }

    public boolean deleteAttributeType(long id) {
        // Simple instantiate + attempt to delete.
        try {
            this.connect();
            AttributeType type = this.objectModel.createAttributeType();
            type.setId(id);
            this.objectModel.deleteAttributeType(type);
            return true;
        }
        catch(DTException e) {
            this.hasError = true;
            this.error = e.getMessage();
            return false;
        }
        finally {
            this.close();
        }

    }

    public boolean updateAttributeType(String name, long id) {
        // Can only change name. to change more, delete + remake
        try {
            this.connect();
            AttributeType type = this.objectModel.createAttributeType();
            type.setId(id);
            Iterator<AttributeType> results = this.objectModel.findAttributeType(type);
            if(results.hasNext()) {
                type = results.next();
                type.setName(name);
                this.objectModel.storeAttributeType(type);
                return true;
            }else{
                return false;
            }
        }
        catch(DTException e) {
            this.hasError = true;
            this.error = e.getMessage();
            return false;
        }
        finally {
            this.close();
        }

    }
    public boolean attributeTypeExists(long id) {
        // Check ID is valid
        try {
            this.connect();
            AttributeType type = this.objectModel.createAttributeType();
            type.setId(id);
            Iterator<AttributeType> results = this.objectModel.findAttributeType(type);
            if(results.hasNext()) {
                return true;
            }else{
                return false;
            }
        }
        catch(DTException e) {
            this.hasError = true;
            this.error = e.getMessage();
            return false;
        }
        finally {
            this.close();
        }
    }

}
