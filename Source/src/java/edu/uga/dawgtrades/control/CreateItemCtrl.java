package edu.uga.dawgtrades.control;

import edu.uga.dawgtrades.DTException;
import edu.uga.dawgtrades.control.LoginControl;
import edu.uga.dawgtrades.model.ObjectModel;
import edu.uga.dawgtrades.model.RegisteredUser;
import edu.uga.dawgtrades.model.Item;
import edu.uga.dawgtrades.model.Attribute;
import edu.uga.dawgtrades.model.AttributeType;
import edu.uga.dawgtrades.model.Category;
import edu.uga.dawgtrades.model.impl.CategoryImpl;
import edu.uga.dawgtrades.model.impl.ObjectModelImpl;
import edu.uga.dawgtrades.persistence.Persistence;
import edu.uga.dawgtrades.persistence.impl.DbUtils;
import edu.uga.dawgtrades.persistence.impl.PersistenceImpl;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.*;
import java.util.*;
import javax.servlet.*;
import javax.servlet.http.HttpSession;
public class CreateItemCtrl{
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

	public long attemptItemCreate(Map<String,String[]> parameters, long userId){

		boolean created=true;
		try{
			connect();
			Item item = objectModel.createItem();
			item.setName(parameters.get("name")[0]);
			item.setDescription(parameters.get("desc")[0]);
			//RegisteredUser currentUser = ctrl.getLoggedInUser(session);
			item.setOwnerId(userId);
			long categoryId = Long.parseLong(parameters.get("catId")[0]);			
			item.setCategoryId(categoryId);
			objectModel.storeItem(item);
			long itemId = item.getId();
			Iterator<AttributeType> attributeTypes = this.getCategoryAttributes(categoryId).iterator();
			AttributeType attrType = null;
			while(attributeTypes.hasNext()){
				attrType = attributeTypes.next();
				String value = parameters.get(Long.toString(attrType.getId()))[0];
				Attribute attribute = objectModel.createAttribute();
				attribute.setAttributeTypeId(attrType.getId());
				attribute.setValue(value);
				attribute.setItemId(itemId);
				this.objectModel.storeAttribute(attribute);
			}
			
			return itemId;	
				
		}catch(DTException e){
			error = e.getMessage();
			hasError = true;
			return -1;
		}finally{
			close();
		}
	
	}
	
	private void addAttributes(Set<Attribute> attributes){	
		// add attributes for this item				
	}
	public ArrayList<Category> getCategoryList(){
		try{
			this.connect();
			Category modelCategory = this.objectModel.createCategory();
			Iterator<Category> categories = this.objectModel.findCategory(modelCategory);
			ArrayList<Category> categoriesMap = new ArrayList<Category>();
			while(categories.hasNext()){				
				categoriesMap.add(categories.next());
			}
//			error = Integer.toString(categoriesMap.size());		
			return categoriesMap;	
		}catch(DTException e){
			error = e.getMessage();
			hasError=true;
			return null;
		}finally{
			this.close();
		}
	}
	public ArrayList<AttributeType> getCategoryAttributes(long id){
		AuctionControl auctCtrl = new AuctionControl();
		ArrayList<AttributeType> out = auctCtrl.getAttributeTypesForCategory(id);
		if(auctCtrl.hasError()) {
			this.hasError = true;
			this.error = auctCtrl.getError();
		}
		return out;
	}
	public void removeItem(long itemId){
		try{
			this.connect();
			Item item = objectModel.createItem();
			item.setId(itemId);
			objectModel.deleteItem(item);
		}catch(DTException e){
                        error = e.getMessage();
                        hasError=true;
                //        return null;
                }finally{
                        this.close();
                }

	}
	public String getError(){
		return error;
	}	
	public boolean hasError(){
		return hasError;
	}
}
