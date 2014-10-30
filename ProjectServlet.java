package edu.uwm.cs361;

import java.io.IOException;

import javax.servlet.http.*;

@SuppressWarnings("serial")
public class ProjectServlet extends HttpServlet {
	
	public ProjectServlet(){};
	
	@Override
	public void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws IOException {
		banner(req,resp);
		layout("test contents",req,resp);
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
		+								"<img class=\"mainlogo-img\" id=\"box-outline\" src=\"Images/UWM_D2L_banner_960w1.png\" alt=\"UW-Milwaukee D2L\">"
		+							"</a>"
		+						"</div>");
	}
	
	public void layout(String http, HttpServletRequest req, HttpServletResponse resp)throws IOException{
		resp.setContentType("text/html");
		
		resp.getWriter().println("<div class=\"layout background-style box-outline\">"
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
		resp.getWriter().println("			<li> <a href=\"Admin_Home_Page.html\"> Home</a></li>");
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
		resp.getWriter().println("					<li><a href=\"staffEdit.html\"> Edit Staff</a></li>");
		resp.getWriter().println("				</ul>");
		resp.getWriter().println("				<ul class=\"buttons-outline\">");
		resp.getWriter().println("					<li><a href=\"staffView.html\"> View Staff</a></li>");
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
		resp.getWriter().println("			<li> <a href=\"/myContact\"> My Contact</a></li>");
		resp.getWriter().println("		</ul>");
		resp.getWriter().println("		<ul class=\"buttons-outline\">");
		resp.getWriter().println("			<li> <a href=\"login.html\">Logout</a></li>");
		resp.getWriter().println("		</ul>");
		resp.getWriter().println("	</div>");
		resp.getWriter().println("</div>");

	}
}
