package edu.uga.dawgtrades.control;

import edu.uga.dawgtrades.DTException;
import edu.uga.dawgtrades.control.LoginControl;
import edu.uga.dawgtrades.model.Item;
import edu.uga.dawgtrades.model.Auction;
import edu.uga.dawgtrades.model.ObjectModel;
import edu.uga.dawgtrades.model.impl.ObjectModelImpl;
import edu.uga.dawgtrades.persistence.Persistence;
import edu.uga.dawgtrades.persistence.impl.DbUtils;
import edu.uga.dawgtrades.persistence.impl.PersistenceImpl;

import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.util.*;

public class MyAuctionsControl{


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

	public Map<String,Auction> getUserAuctions(long userId){
		try{
			this.connect();
			ArrayList<Item> items = this.getUserItems(userId);
			ArrayList<Auction> auctions = new ArrayList<Auction>();
			Auction auction = this.objectModel.createAuction();
			Map<String,Auction> map = new HashMap<String,Auction>();
			for(Item item : items){
				auction.setItemId(item.getId());
				Iterator<Auction> results = this.objectModel.findAuction(auction);
				if(results.hasNext()){
					map.put(Long.toString(item.getId()),results.next());
				}
			}
			return map;
			
		}catch(DTException e){
			this.error = e.getMessage();
			this.hasError = true;
			return null;
		}finally{
			this.close();
		}
	}
	private ArrayList<Item> getUserItems(long userId){
                try{
                      //  this.connect();
			Item modelItem = this.objectModel.createItem();
			modelItem.setOwnerId(userId);
			Iterator<Item> results = this.objectModel.findItem(modelItem);
			ArrayList<Item> items = new ArrayList<Item>();
						
			while(results.hasNext()){
				items.add(results.next());
			}
			return items;

                }catch(DTException e){
                        this.error = e.getMessage();
                        this.hasError = true;
                        return null;
                }finally{
                 //       this.close();
                }

	}
	public ArrayList<Item> getUserItemsPub(long userId){
                try{
                        this.connect();
                        Item modelItem = this.objectModel.createItem();
                        modelItem.setOwnerId(userId);
                        Iterator<Item> results = this.objectModel.findItem(modelItem);
                        ArrayList<Item> items = new ArrayList<Item>();

                        while(results.hasNext()){
                                items.add(results.next());
                        }
                /*        long[] itemIDs = new long[items.size()];
                //      Item[] itemsArray = items.toArray();
                        for(int i =0;i<itemIDs.length; i++){
                                itemIDs[i] = items.get(i).getId();
                        }*/
                        return items;

                }catch(DTException e){
                        this.error = e.getMessage();
                        this.hasError = true;
                        return null;
                }finally{
                      this.close();
                }

	}
	public boolean hasError(){
		return this.hasError;
	}
	public String getError(){
		return this.error;
	}
	
}
