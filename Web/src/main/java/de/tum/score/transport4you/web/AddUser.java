package de.tum.score.transport4you.web;


import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;

import java.io.IOException;
import java.util.Date;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.googlecode.objectify.ObjectifyService;




public class AddUser extends HttpServlet {

	@Override
	public void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
			
		User user;

		String group = "default";
		String name = req.getParameter("user_name");
		String email = req.getParameter("user_email");
		String password = User.hash(req.getParameter("user_password"));	

		user = new User(group,name,email,password);
		
		System.err.println("AddUser.java Name:"+name+" In the object: '"+user.name+"'");
		
		ObjectifyService.ofy().save().entity(user).now();

		resp.sendRedirect("/score.jsp");
	}
}
