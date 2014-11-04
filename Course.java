package edu.uwm.cs361;

import java.util.ArrayList;

public class Course {
	String designation;
	String title;
	ArrayList<Section> sections;
	
	public Course(String des, String titl, ArrayList<Section> secs){
		designation = des;
		title = titl;
		sections = secs;
	}

	public String getDesignation() {
		return designation;
	}
	public String getNumber() {
		// gotta comply with DemeritDatastoreService.java
		return designation.replaceFirst(".*?(?=\\d)", "");
	}
	public String getTitle() {
		return title;
	}
	public ArrayList<Section> getSections() {
		return sections;
	}
	@Override
	public String toString() {
		return "Course [designation=" + designation + ", title=" + title
				+ ", sections=" + sections + "]";
	}
}
