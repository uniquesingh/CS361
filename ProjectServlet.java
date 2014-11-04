package edu.uwm.cs361;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.*;

import edu.uwm.cs361.DemeritDatastoreService;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.FetchOptions;
import com.google.appengine.api.datastore.Query;

@SuppressWarnings("serial")
public class ProjectServlet extends HttpServlet {
	
	public ProjectServlet(){};

	DemeritDatastoreService data = new DemeritDatastoreService();
	
	@Override
	public void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws IOException {
		String http = "";
		http += "<form id=\"ccf\">"
		+			"<div id=\"title-create-staff\">"
		+				"Staff List"
		+			"</div>"
		+ 			"<div id=\"sub\">";
		
		Query q = new Query(data.STAFF);

		DatastoreService ds = data.getDatastore();
		
		List<Entity> users = ds.prepare(q).asList(FetchOptions.Builder.withDefaults());
		http += "Ther are " + users.size() + " users.<br><br>";
		for(Entity user:users){
			http += "Name: " + user.getProperty(data.NAME) + "<br>";
			//ds.delete(user.getKey());
		}
		http += "</div>"
		+		"</form>";
		banner(req,resp);
		layout(http,req,resp);
		menu(req,resp);
	}
	
	@Override
	public void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws IOException {
	}
	
	public void banner(HttpServletRequest req, HttpServletResponse resp)throws IOException{
		resp.setContentType("text/html");
		
		resp.getWriter().println("<head>"
		+							"<meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\">"
		+							"<title>UW Milwaukee</title>"
		+							"<link href=\"main.css\" rel=\"stylesheet\" type=\"text/css\"/>"
		+							"<style type=\"text/css\"></style>"
		+						"</head>");
		
		resp.getWriter().println("<div class=\"banner\">"
		+							"<a class=\"plbrand mainlogo-link\" href=\"Admin_Home_Page.html\" title=\"UW-Milwaukee D2L\">"
		+								"<img class=\"mainlogo-img\" src=\"Images/UWM_D2L_banner_960w1.png\" alt=\"UW-Milwaukee D2L\">"
		+							"</a>"
		+						"</div>");
	}
	
	public void layout(String http, HttpServletRequest req, HttpServletResponse resp)throws IOException{
		resp.setContentType("text/html");
		
		resp.getWriter().println("<div class=\"layout background-style\">"
		+							"	<div class=\"page-after-banner\">"
		+									http
		+							"	</div>"
		+							"</div>");
	}
	
	public void menu(HttpServletRequest req, HttpServletResponse resp)throws IOException{
		resp.setContentType("text/html");
		resp.getWriter().println("<div class=\"menu\">");					
		resp.getWriter().println("	<div class=\"buttons\">");
		resp.getWriter().println("		<ul class=\"buttons-outline\">");
		resp.getWriter().println("			<li> <a href=\"/project\"> Home</a></li>");
		resp.getWriter().println("		</ul>");
		resp.getWriter().println("		<ul class=\"buttons-outline\">");
		resp.getWriter().println("			<li id=\"b1\"> <a href=\"#\">Admin</a>");
		resp.getWriter().println("				<ul class=\"buttons-outline\">");
		resp.getWriter().println("					<li><a href=\"adminQueuryPage.html\"> Admin Query</a></li>");
		resp.getWriter().println("				</ul>");
		resp.getWriter().println("				<ul class=\"buttons-outline\">");
		resp.getWriter().println("					<li><a href=\"contactPageAdmin.html\"> Staff Contacts</a></li>");
		resp.getWriter().println("				</ul>");
		resp.getWriter().println("			</li>");
		resp.getWriter().println("		</ul>");
		resp.getWriter().println("		<ul class=\"buttons-outline\">");
		resp.getWriter().println("			<li> <a href=\"#\">Staff</a>");
		resp.getWriter().println("				<ul class=\"buttons-outline\">");
		resp.getWriter().println("					<li><a href=\"Staff_Home_Page.html\"> Staff Home </a></li>");
		resp.getWriter().println("				</ul>");
		resp.getWriter().println("				<ul class=\"buttons-outline\">");
		resp.getWriter().println("					<li><a href=\"/createStaff\"> Create Staff</a></li>");
		resp.getWriter().println("				</ul>");
		resp.getWriter().println("				<ul class=\"buttons-outline\">");
		resp.getWriter().println("					<li><a href=\"/editStaff\"> Edit Staff</a></li>");
		resp.getWriter().println("				</ul>");
		resp.getWriter().println("				<ul class=\"buttons-outline\">");
		resp.getWriter().println("					<li><a href=\"/viewStaff\"> View Staff</a></li>");
		resp.getWriter().println("				</ul>");
		resp.getWriter().println("				<ul class=\"buttons-outline\">");
		resp.getWriter().println("					<li><a href=\"staffListPage.html\">Staff List</a></li>");
		resp.getWriter().println("				</ul>");
		resp.getWriter().println("				<ul class=\"buttons-outline\">");
		resp.getWriter().println("					<li><a href=\"staffOfficePageAdmin.html\"> Staff Office</a></li>");
		resp.getWriter().println("				</ul>");
		resp.getWriter().println("			</li>");
		resp.getWriter().println("		</ul>");
		resp.getWriter().println("		<ul class=\"buttons-outline\">");
		resp.getWriter().println("			<li> <a href=\"#\">Instructor</a>");
		resp.getWriter().println("				<ul class=\"buttons-outline\">");
		resp.getWriter().println("					<li><a href=\"staffListPage.html\"> TA's List </a></li>");
		resp.getWriter().println("				</ul>");
		resp.getWriter().println("				<ul class=\"buttons-outline\">");
		resp.getWriter().println("					<li><a href=\"Edit_Course_InstructorView_Page.html\"> Assign TA's</a></li>");
		resp.getWriter().println("				</ul>");
		resp.getWriter().println("			</li>");
		resp.getWriter().println("		</ul>");
		resp.getWriter().println("		<ul class=\"buttons-outline\">");
		resp.getWriter().println("			<li> <a href=\"#\"> Course</a>");
		resp.getWriter().println("				<ul class=\"buttons-outline\">");
		resp.getWriter().println("					<li><a href=\"courseCreate.html\"> Create Course</a></li>");
		resp.getWriter().println("				</ul>");
		resp.getWriter().println("				<ul class=\"buttons-outline\">");
		resp.getWriter().println("					<li><a href=\"Edit_Course_AdminView_Page.html\"> Edit Course</a></li>");
		resp.getWriter().println("				</ul>");
		resp.getWriter().println("				<ul class=\"buttons-outline\">");
		resp.getWriter().println("					<li><a href=\"courseView.html\"> View Course</a></li>");
		resp.getWriter().println("				</ul>");
		resp.getWriter().println("			</li>");
		resp.getWriter().println("		</ul>");
		resp.getWriter().println("		<ul class=\"buttons-outline\">");
		resp.getWriter().println("			<li> <a href=\"staffOfficePage.html\"> My Office Hours</a></li>");
		resp.getWriter().println("		</ul>");
		resp.getWriter().println("		<ul class=\"buttons-outline\">");
		resp.getWriter().println("			<li> <a href=\"#\"> My Contact</a>");
		resp.getWriter().println("				<ul class=\"buttons-outline\">");
		resp.getWriter().println("					<li><a href='/myContact'> Contact</a></li>");
		resp.getWriter().println("				</ul>");
		resp.getWriter().println("				<ul class=\"buttons-outline\">");
		resp.getWriter().println("					<li><a href='/editStaffContact'> Edit Staff Contact</a></li>");
		resp.getWriter().println("				</ul>");
		resp.getWriter().println("			</li>");
		resp.getWriter().println("		</ul>");
		resp.getWriter().println("		<ul class=\"buttons-outline\">");
		resp.getWriter().println("			<li> <a href=\"login.html\">Logout</a></li>");
		resp.getWriter().println("		</ul>");
		resp.getWriter().println("	</div>");
		resp.getWriter().println("</div>");

	}
}
