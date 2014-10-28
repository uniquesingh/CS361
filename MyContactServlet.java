package edu.uwm.cs361;

import java.io.IOException;
import javax.servlet.http.*;
import edu.uwm.cs361.ProjectServlet;;

@SuppressWarnings("serial")
public class MyContactServlet extends HttpServlet{
	ProjectServlet page = new ProjectServlet();
	
	@Override
	public void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws IOException {
		page.banner(req,resp);
		page.layout("My Contacts",contact(),req,resp);
		page.menu(req,resp);
	}
	
	@Override
	public void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws IOException {
	}
	
	private String contact(){
		String http = "testing";
		return http;
	}
}
