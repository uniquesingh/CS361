package edu.uwm.cs361;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import edu.uwm.cs361.DemeritDatastoreService;
import javax.servlet.http.*;

import com.google.appengine.api.datastore.BaseDatastoreService;
import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.EntityNotFoundException;
import com.google.appengine.api.datastore.FetchOptions;
import com.google.appengine.api.datastore.Query;

@SuppressWarnings("serial")
public class EditStaffContactServlet extends HttpServlet{
	ProjectServlet page = new ProjectServlet();
	DemeritDatastoreService data = new DemeritDatastoreService();
	DatastoreService ds =  data.getDatastore();
	
	@Override
	public void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws IOException {
		
		Query q = new Query(data.STAFF);
		List<Entity> users = ds.prepare(q).asList(FetchOptions.Builder.withDefaults());
		String http = "";

		String staff = "";
		staff = req.getParameter("staff");
		if(staff==null){
			page.banner(req,resp);
			http += "<form id=\"ccf\" method=\"GET\" action=\"/editStaffContact\">"
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
			page.layout(displayForm(req,resp, new ArrayList<String>()), req, resp);
			page.menu(req,resp);
		}
	}
	
	@Override
	public void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws IOException {
		
		String officePhone = req.getParameter("officePhone");
		String office = req.getParameter("office");
		String homeAddress = req.getParameter("homeAddress");
		String homePhone = req.getParameter("homePhone");
		
		List<String> errors = new ArrayList<String>();
		
		if (officePhone == null)
			officePhone = "";
		if (office == null || office == "")
			office = "";
		if (homeAddress == null)
			homeAddress = "";
		if (homePhone == null)
			homePhone = "";
		

		Query q = new Query(data.STAFF);
		DatastoreService dsQ =  data.getDatastore();
		List<Entity> users = dsQ.prepare(q).asList(FetchOptions.Builder.withDefaults());
		Entity toUpdate = users.get(0);
		String toUpdateEmail = data.getOurKey(toUpdate.getKey());

		
		if (errors.size() > 0) {
			page.banner(req,resp);
			page.layout(displayForm(req,resp,errors),req,resp);
			page.menu(req,resp);
		} else {
	
			Entity e = null;
			
			try {
				
				data.updateStaffContact(toUpdateEmail, office, officePhone, homeAddress, homePhone);
				e = data.getStaff(toUpdateEmail);
				
			} catch (EntityNotFoundException ex) {
				// TODO Auto-generated catch block
				ex.printStackTrace();
			}
			
			String http = "";
			
			http += "<form id=\"ccf\" method=\"GET\" action=\"/editStaffContact\">"
			+			"<div id=\"title-create-staff\">"
			+				"Edit Contact info: " + users.get(0).getProperty(data.NAME).toString()
			+			"</div>"
			+ 			"<div id=\"sub\">"
			+				"Office: " + e.getProperty(data.OFFICE_LOC) + "<br>" 
			+				"Office Phone: " + e.getProperty(data.OFFICE_PHONE) + "<br>" 
			+				"Home Address: " + e.getProperty(data.HOME_ADDRESS) + "<br><br>" 
			+				"Home Phone: " + e.getProperty(data.HOME_PHONE) + "<br>" 
			+				"The User's contact info has been updated.<br><br><br><br><br><br>"
			+				"<input class=\"submit\" type=\"submit\" value=\"Back\" />"
			+			"</div>"
			+		"</form>";
			page.banner(req,resp);
			page.layout(http,req,resp);
			page.menu(req,resp);
		}
	}
	
	private String displayForm(HttpServletRequest req, HttpServletResponse resp, List<String> errors) throws IOException
	{	
		
		Query q = new Query(data.STAFF);
		DatastoreService dsQ =  data.getDatastore();
		List<Entity> users = dsQ.prepare(q).asList(FetchOptions.Builder.withDefaults());
		Entity toUpdate = users.get(0);
		
		resp.setContentType("text/html");
		String http = "";
		
		http += "<form id=\"ccf\" method=\"POST\" action=\"/editStaffContact\">"
		+			"<div id=\"title-create-staff\">"
		+				"Edit Contact info: " + users.get(0).getProperty(data.NAME).toString()
		+			"</div>";
		
		String name = toUpdate.getProperty(data.NAME).toString();
		String office = toUpdate.getProperty(data.OFFICE_LOC).toString();
		String officePhone = toUpdate.getProperty(data.OFFICE_PHONE).toString();
		String homeAddress = toUpdate.getProperty(data.HOME_ADDRESS).toString();
		String homePhone = toUpdate.getProperty(data.HOME_PHONE).toString();

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
		+							"Office: <input class='createStaffInput' type=\"text\" id='officeLoc' name='office' value='" + office + "'/><br>"
		+							"Office Phone: <input class='createStaffInput' type=\"text\" id='officePhone' name='officePhone' value='" + officePhone + "'/><br>"
		+							"Home Address: <input class='createStaffInput' type=\"text\" id='homeAddress' name='homeAddress' value='" + homeAddress + "'/><br>"
		+							"Home Phone: <input class='createStaffInput' type=\"text\" id='homePhone' name='homePhone' value='" + homePhone + "'/><br>"
		+						"</td>"
		+					"</tr>"
		+				"</table>"
		+				"<input class=\"submit\" type=\"submit\" value=\"Submit\" />"
		+			"</div>"
		+		"</form>";
		
		return http;
	}

}