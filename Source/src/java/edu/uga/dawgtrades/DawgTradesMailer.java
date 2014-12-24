package edu.uga.dawgtrades;

import javax.servlet.*;
import java.util.*;
import edu.uga.dawgtrades.model.*;
import edu.uga.dawgtrades.model.impl.*;
import edu.uga.dawgtrades.persistence.impl.*;
import edu.uga.dawgtrades.persistence.*;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import com.sun.jersey.api.client.filter.*;
import com.sun.jersey.api.client.*;
import com.sun.jersey.api.*;
import com.sun.jersey.core.util.*;
import javax.ws.rs.core.MediaType;


public class DawgTradesMailer  {

    public static boolean sendMail(String email, String subject, String message) {
        try {
            Client client = Client.create();
            client.addFilter(new HTTPBasicAuthFilter("api", "key-9ce8872b34a5ace134075b0383356ede"));
            WebResource webResource =client.resource("https://api.mailgun.net/v2/dawgtrades.devisedby.us/messages");
            MultivaluedMapImpl formData = new MultivaluedMapImpl();
            formData.add("from", "DawgTrades <noreply@dawgtrades.devisedby.us>");
            formData.add("to", email);
            formData.add("subject", subject);
            formData.add("text", message);
            webResource.type(MediaType.APPLICATION_FORM_URLENCODED).post(ClientResponse.class, formData);
            return true;
        }
        catch(Exception e) {
            return false;
        }

    }
}