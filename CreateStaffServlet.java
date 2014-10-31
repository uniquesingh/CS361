package edu.uwm.cs361;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.*;

import com.google.appengine.api.datastore.BaseDatastoreService;
import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.EntityNotFoundException;
import com.google.appengine.api.datastore.FetchOptions;
import com.google.appengine.api.datastore.Query;

import edu.uwm.cs361.ProjectServlet;
import edu.uwm.cs361.DemeritDatastoreService;;

@SuppressWarnings("serial")
public class CreateStaffServlet extends HttpServlet{
	ProjectServlet page = new ProjectServlet();
	DemeritDatastoreService ds = new DemeritDatastoreService();
	
	@Override
	public void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws IOException {
		page.banner(req,resp);
		page.layout(displayForm(req,resp,new ArrayList<String>()),req,resp);
		page.menu(req,resp);
	}
	
	@Override
	public void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws IOException {
		
		String username = req.getParameter("username");
		String password = req.getParameter("password");
		String firstname = req.getParameter("firstname");
		String lastname = req.getParameter("lastname");
		String telephone = req.getParameter("telephone");

		List<String> errors = new ArrayList<String>();

		//
		// TODO - add error if username is taken
		//
		
		DatastoreService dsNew =  ds.getDatastore();
		Entity e = new Entity("user");
		e.setProperty("username", username);
		e.setProperty("password", password);
		e.setProperty("firstname", firstname);
		e.setProperty("lastname", lastname);
		e.setProperty("telephone", telephone);
		
		Query q = new Query("user").setFilter(
				new Query.FilterPredicate("username", Query.FilterOperator.EQUAL, username));
		
		
		List<Entity> users = dsNew.prepare(q).asList(FetchOptions.Builder.withDefaults());
		if(!users.isEmpty())
			errors.add("User '"+ e.getProperty("username")+"' Already Exist.");
		else{
			if (username.isEmpty()) {
				errors.add("Username is required.");
			}
			if (password.isEmpty()) {
				errors.add("Password is required.");
			} 
			if (firstname.isEmpty()) {
				errors.add("First is required.");
			} 
			if (lastname.isEmpty()) {
				errors.add("Lastname is required.");
			} 
		}
		if (errors.size() > 0) {
			page.banner(req,resp);
			page.layout(displayForm(req,resp,errors),req,resp);
			page.menu(req,resp);
		} else {
			//
			// TODO - create user
			//
			dsNew.put(e);
			String http = "";
			
			http += "<form id=\"ccf\">"
			+			"<div id=\"title-create-staff\">"
			+				"Staff Created Conformation"
			+			"</div>"
			+ 			"<div id=\"sub\">"
			+				"UserName: " + e.getProperty("username") + "<br>" 
			+				"First Name: " + e.getProperty("firstname") + "<br>" 
			+				"Last Name: " + e.getProperty("lastname") + "<br><br>" 
			+				"The User has been Created."
			+			"</div>"
			+		"</form>";
			page.banner(req,resp);
			page.layout(http,req,resp);
			page.menu(req,resp);
		}
	}
	
	private String displayForm(HttpServletRequest req, HttpServletResponse resp, List<String> errors) throws IOException
	{
		resp.setContentType("text/html");
		String http = "";
		
		http += "<form id=\"ccf\" method=\"POST\" action=\"/createStaff\">"
		+			"<div id=\"title-create-staff\">"
		+				"Create Staff"
		+			"</div>";
		
		String username = req.getParameter("username") != null ? req.getParameter("username") : "";
		String password = req.getParameter("password") != null ? req.getParameter("password") : "";
		String firstname = req.getParameter("firstname") != null ? req.getParameter("firstname") : "";
		String lastname = req.getParameter("lastname") != null ? req.getParameter("lastname") : "";
		String telephone = req.getParameter("telephone") != null ? req.getParameter("telephone") : "";

		if (errors.size() > 0) {
			http += "<ul class='errors'>";

			for (String error : errors) {
				http +="  <li>" + error + "</li>";
			}

			http += "</ul>";
		}

		http += 	"<div id=\"sub\">"
		+				"<table>"
		+					"<tr>"
		+						"<td class=\"form\">"
		+							"Username *: <input class='createStaffInput' type=\"text\" id='username' name='username' value='" + username + "'/><br>"
		+							"Password *: <input class='createStaffInput' type=\"password\" id='password' name='password' value='" + password + "'/><br>"
		+							"First Name *: <input class='createStaffInput' type=\"text\" id='firstname' name='firstname' value='" + firstname + "'/><br>"
		+							"Last Name *: <input class='createStaffInput' type=\"text\" id='lastname' name='lastname' value='" + lastname + "'/><br>"
		+							"Telephone: <input class='createStaffInput' type=\"text\" id='telephone' name='telephone' value='" + telephone + "'/><br>"
		+						"</td>"
		+					"</tr>"
		+				"</table>"
		+				"<input class=\"submit\" type=\"submit\" value=\"Submit\" />"
		+			"</div>"
		+		"</form>";
		
		return http;
	}

}