package edu.uwm.cs361;

import java.io.IOException;

import javax.servlet.http.*;

import com.google.appengine.api.datastore.EntityNotFoundException;

import edu.uwm.cs361.ProjectServlet;
import edu.uwm.cs361.DemeritDatastoreService;;

@SuppressWarnings("serial")
public class MyContactServlet extends HttpServlet{
	ProjectServlet page = new ProjectServlet();
	DemeritDatastoreService ds = new DemeritDatastoreService();
	
	@Override
	public void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws IOException {
		page.banner(req,resp);
		page.layout("Course",contact(),req,resp);
		page.menu(req,resp);
	}
	
	@Override
	public void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws IOException {
	}
	
	private String contact(){
		String http = "";
		try {
			String[] myS = {""};
			http += ds.getStaff("sukh@gmail.com");
		} catch (EntityNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println(http);
		http = http.replaceAll("<Entity", "");
		http = http.replaceAll(">", "");
		
		if(ds.hasNoDuplicate(ds.STAFF, "sukh@gmail.com")){
			System.out.println("found no duplicatea");
		}
		else
			System.out.println("found duplicatea");
			
		return http;
	}
}
