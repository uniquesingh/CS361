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

		List<String> errors = new ArrayList<String>();

		if (username.isEmpty()) {
			errors.add("Username is required.");
		}

		if (password.isEmpty()) {
			errors.add("Password is required.");
		} 

		//
		// TODO - add error if username is taken
		//
		
		DatastoreService dsNew =  ds.getDatastore();
		Entity e = new Entity("user");
		e.setProperty("username", username);
		e.setProperty("password", password);
		
		Query q = new Query("user").setFilter(
				new Query.FilterPredicate("username", Query.FilterOperator.EQUAL, username));
		
		
		List<Entity> users = dsNew.prepare(q).asList(FetchOptions.Builder.withDefaults());
		if(!users.isEmpty())
			errors.add("No such username");
		
		//String passCheck = users.get(0).getProperty("password").toString();
		//if(!password.equals(passCheck))
			//errors.add("Incorrect PW");

		if (errors.size() > 0) {
			page.banner(req,resp);
			page.layout(displayForm(req,resp,errors),req,resp);
			page.menu(req,resp);
		} else {
			//
			// TODO - create user
			//
			dsNew.put(e);
			resp.getWriter().println("Youve Got Mail " + e.getProperty("username"));
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
		+							"Username *: <input type=\"text\" id='username' name='username' value='" + username + "'/><br>"
		+							"First Name *: <input type=\"text\" name=\"firstname\"/><br>"
		+							"Date of Birth *: <input type=\"text\" name=\"dob\"/><br>"
		+							"Address *: <input type=\"text\" name=\"address\"<br>"
		+						"</td>"
		+						"<td class=\"form\">"
		+							"Password *: <input type=\"password\" id='password' name='password' value='" + password + "'/><br>"
		+							"Last Name *: <input type=\"text\" name=\"lastname\"/><br>"
		+							"Email *: <input type=\"email\" name=\"email\" /><br>"
		+							"Telephone: <input type=\"text\" name=\"telephone\"/><br>"
		+						"</td>"
		+					"</tr>"
		+				"</table>"
		+				"<input class=\"submit\" type=\"submit\" value=\"Submit\" />"
		+			"</div>"
		+		"</form>";
		
		return http;
	}
}