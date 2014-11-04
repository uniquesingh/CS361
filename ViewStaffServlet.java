package edu.uwm.cs361;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.*;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.EntityNotFoundException;
import com.google.appengine.api.datastore.FetchOptions;
import com.google.appengine.api.datastore.Query;

import edu.uwm.cs361.ProjectServlet;
import edu.uwm.cs361.DemeritDatastoreService;;

@SuppressWarnings("serial")
public class ViewStaffServlet extends HttpServlet{
	/*
	 * Create a variable to call project servlet methods for HTTP
	 * create insistence of datastore service 
	 */
	ProjectServlet page = new ProjectServlet();
	DemeritDatastoreService data = new DemeritDatastoreService();
	
	/*
	 * (non-Javadoc)
	 * @see javax.servlet.http.HttpServlet#doGet(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	@Override
	public void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws IOException {
		page.banner(req,resp);
		page.layout(displayForm(req,resp,""),req,resp);
		page.menu(req,resp);
	}
	
	/*
	 * (non-Javadoc)
	 * @see javax.servlet.http.HttpServlet#doPost(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	@Override
	public void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws IOException {
		
		//get the value from the dropdown
		String stafftype = req.getParameter("stafftype");
		
		page.banner(req,resp);
		page.layout(displayForm(req,resp,stafftype),req,resp);
		page.menu(req,resp);
		
	}
	
	/*
	 * display form will list all the information for the staff
	 */
	private String displayForm(HttpServletRequest req, HttpServletResponse resp, String staff) throws IOException
	{
		resp.setContentType("text/html");
		String http = "";

		DatastoreService dsNew =  data.getDatastore();
		
		Query q = new Query(data.STAFF);
		List<Entity> users = dsNew.prepare(q).asList(FetchOptions.Builder.withDefaults());

		String stafftype = staff;
		
		
		http += "<form id=\"ccf\" method=\"POST\" action=\"/viewStaff\">"
		+			"<div id=\"title-create-staff\">"
		+				"View Staff"
		+			"</div>";

		http += 	"<div id=\"sub\">"
		+				"<table>"
		+					"<tr>"
		+						"<td class='form'>"
		+							"Staff:"
		+							"<select id='stafftype' name='stafftype' class='staff-select'>"
		+									"<option value = '' selected> Select a Person </option>";
										http += "<option disabled>Instructor's</option>";		
										for(Entity user:users){
											if(!user.getProperty(data.TYPE).equals("TA"))
													http += "<option>" + user.getProperty(data.EMAIL) + "</option>";
										}
										http += "<option disabled>TA's</option>";
										for(Entity user:users){
											if(user.getProperty(data.TYPE).equals("TA"))
												http += "<option>" + user.getProperty(data.EMAIL) + "</option>";
										}
		http +=						"</select>"
		+						"</td>"
		+					"</tr>"
		+					"<tr>"
		+					"<td></td>"
		+					"<td>"
		+						"<input class='view-submit-staff' type='submit' value='Submit' />"
		+					"</td>"
		+				"</tr>";
		
		for(Entity user:users){
			if(user.getProperty(data.EMAIL).equals(stafftype)){					
				http+=	"<tr>"
				+			"<td class='view-staff'>"
				+				"Name:<br>"
				+				"Username:<br>"
				+				"Password:<br>"
				+				"Telephone:<br>"
				+				"Staff Type:<br>"
				+			"</td>"
				+			"<td class='view-staff-result'>"
				+				user.getProperty(data.NAME) + "<br>"
				+				user.getProperty(data.EMAIL) + "<br>"
				+				user.getProperty(data.PASSWORD) + "<br>"
				+				user.getProperty(data.TELEPHONE) + "<br>"
				+				user.getProperty(data.TYPE) + "<br>"
				+			"</td>"
				+		"</tr>";
			}
		}
		
		http+= 		"</table>"
		+		"</div>"
		+	"</form>";
		return http;
	}

}