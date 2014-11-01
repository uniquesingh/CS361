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
public class EditStaffServlet extends HttpServlet{
	ProjectServlet page = new ProjectServlet();
	DemeritDatastoreService data = new DemeritDatastoreService();
	
	
	@Override
	public void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws IOException {
		DatastoreService dsNew =  data.getDatastore();
		
		Query q = new Query(data.STAFF);
		List<Entity> users = dsNew.prepare(q).asList(FetchOptions.Builder.withDefaults());
		String http = "";

		String staff = " ";
		staff = req.getParameter("staff");
		if(staff==null){
			page.banner(req,resp);
			http += "<form id=\"ccf\" method=\"GET\" action=\"/editStaff\">"
			+			"<div id=\"title-create-staff\">"
			+				"Edit Staff"
			+			"</div>"
			+			"<div id=\"sub\">"
			+				"<table>"
			+					"<tr>"
			+						"<td class='form'>"
			+							"Staff:"
			+							"<select id='staff' name='staff' class='staff-select'>";
											http += "<option disabled>Instructor's</option>";		
											for(Entity user:users){
												if(!user.getProperty(data.TYPE).equals("TA"))
														http += "<option>" + user.getProperty(data.NAME) + "</option>";
											}
											http += "<option disabled>TA's</option>";
											for(Entity user:users){
												if(user.getProperty(data.TYPE).equals("TA"))
													http += "<option>" + user.getProperty(data.NAME) + "</option>";
											}
			http +=						"</select><br><br>"
			+						"</td>"
			+					"</tr>";
			http+=				"</table>"
			+				"<input class=\"submit\" type=\"submit\" value=\"Submit\" />"
			+			"</div>"
			+		"</form>";
			page.layout(http,req,resp);
			page.menu(req,resp);
		}
		else{
			page.banner(req,resp);
			page.layout(displayForm(req,resp,new ArrayList<String>(),staff), req, resp);
			page.menu(req, resp);
		}
	}
	
	@Override
	public void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws IOException {
		
		String username = req.getParameter("username");
		String password = req.getParameter("password");
		String firstname = req.getParameter("firstname");
		String lastname = req.getParameter("lastname");
		String telephone = req.getParameter("telephone");
		String stafftype = req.getParameter("stafftype");
		String staff = req.getParameter("staff");

		List<String> errors = new ArrayList<String>();

		//
		// TODO - add error if username is taken
		//
		
		Entity e = new Entity("user");
		e.setProperty("username", username);
		e.setProperty("password", password);
		e.setProperty("firstname", firstname);
		e.setProperty("lastname", lastname);
		e.setProperty("telephone", telephone);
		e.setProperty("stafftype", stafftype);
		
		
		if (staff.isEmpty()) {
			errors.add("Username is required.");
		}
		
		if (errors.size() > 0) {
			page.banner(req,resp);
			page.layout(displayForm(req,resp,errors,staff),req,resp);
			page.menu(req,resp);
		} else {
			//
			// TODO - create user
			//		
			try {
				String[] myS = {""};
				data.updateStaff(username, firstname + " " +lastname, password, telephone, myS, myS, stafftype);
			} catch (EntityNotFoundException ex) {
				// TODO Auto-generated catch block
				ex.printStackTrace();
			}
			//dsNew.put(e);
			String http = "";
			
			http += "<form id=\"ccf\" method=\"get\" action=\"/editStaff\">"
			+			"<div id=\"title-create-staff\">"
			+				"Staff Update Conformation"
			+			"</div>"
			+ 			"<div id=\"sub\">"
			+				"UserName: " + e.getProperty("username") + "<br>" 
			+				"First Name: " + e.getProperty("firstname") + "<br>" 
			+				"Last Name: " + e.getProperty("lastname") + "<br><br>" 
			+				"Staff Type: " + e.getProperty("stafftype") + "<br>" 
			+				"The User has been Created.<br><br><br><br><br><br>"
			+				"<input class=\"submit\" type=\"submit\" value=\"Back\" />"
			+			"</div>"
			+		"</form>";
			page.banner(req,resp);
			page.layout(http,req,resp);
			page.menu(req,resp);
		}
	}
	
	private String displayForm(HttpServletRequest req, HttpServletResponse resp, List<String> errors,String staff) throws IOException
	{
		resp.setContentType("text/html");
		String http = "";
		DatastoreService dsNew =  data.getDatastore();
		
		Query q = new Query(data.STAFF);
		List<Entity> users = dsNew.prepare(q).asList(FetchOptions.Builder.withDefaults());
		
		http += "<form id=\"ccf\" method=\"POST\" action=\"/editStaff\">"
		+			"<div id=\"title-create-staff\">"
		+				"Edit Staff"
		+			"</div>"
		+			"<div id=\"sub\">"
		+				"<table>"
		+					"<tr>"
		+						"<td class='form'>"
		+							"Staff:"
		+							"<select id='staff' name='staff' class='staff-select'>";
										http += "<option disabled>Instructor's</option>";		
										for(Entity user:users){
											if(!user.getProperty(data.TYPE).equals("TA"))
													http += "<option>" + user.getProperty(data.NAME) + "</option>";
										}
										http += "<option disabled>TA's</option>";
										for(Entity user:users){
											if(user.getProperty(data.TYPE).equals("TA"))
												http += "<option>" + user.getProperty(data.NAME) + "</option>";
										}
		http +=						"</select><br><br>"
		+						"</td>"
		+					"</tr>";
		
		for(Entity user:users){
			if(user.getProperty(data.NAME).equals(staff)){
				if (errors.size() > 0) {
					http += "<tr><td><ul class='errors'>";

					for (String error : errors) {
						http +="  <li>" + error + "</li>";
					}

					http += "</ul></td></tr>";
				}
				String[] na = user.getProperty(data.NAME).toString().split(" ");

				http+=				"<tr>"
				+						"<td class=\"form\">"
				+							"Username *: <input class='createStaffInput' type=\"text\" id='username' name='username' value='" + user.getProperty(data.EMAIL) + "'/><br>"
				+							"Password *: <input class='createStaffInput' type=\"password\" id='password' name='password' value='" + user.getProperty(data.PASSWORD) + "'/><br>"
				+							"First Name *: <input class='createStaffInput' type=\"text\" id='firstname' name='firstname' value='" + na[0].toString() + "'/><br>"
				+							"Last Name *: <input class='createStaffInput' type=\"text\" id='lastname' name='lastname' value='" + na[1].toString() + "'/><br>"
				+							"Telephone: <input class='createStaffInput' type=\"text\" id='telephone' name='telephone' value='" + user.getProperty(data.TELEPHONE) + "'/><br>"
				+							"Staff Type: <select class='staff-select createStaffInput' id='stafftype' name='stafftype' value='" + user.getProperty(data.TYPE) + "'>"
				+											"<option> Instructor </option>"
				+											"<option> TA </option>"
				+										"</select><br>"
				+						"</td>"
				+					"</tr>";

			}
		}
		http+=				"</table>"
		+				"<input class=\"submit\" type=\"submit\" value=\"Submit\" />"
		+			"</div>"
		+		"</form>";
		return http;
	}

}