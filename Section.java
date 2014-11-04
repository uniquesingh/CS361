package edu.uwm.cs361;

public class Section {
	String units;
	String designation;
	String hours;
	String days;
	String dates;
	String instructor;
	String room;
	
	public Section(String un, String des, 
			String hr, String dy, String dts, String ins, String rm){
		units = un;
		designation = des;
		hours = hr;
		days = dy;
		dates = dts;
		instructor = ins;
		room = rm;
	}

	// CLOS does OO better
	public String getUnits() {
		return units;
	}
	public String getDesignation() {
		return designation;
	}
	public String getType(){
		return designation.substring(0, 4);
	}
	public String getNumber() {
		return designation.replaceFirst(".*?(?=\\d)", "");
	}
	public String getHours() {
		return hours;
	}
	public String getDays() {
		return days;
	}
	public String getDates() {
		return dates;
	}
	public String getInstructor() {
		return instructor;
	}
	public String getRoom() {
		return room;
	}
	@Override
	public String toString() {
		return "Section [units=" + units + ", designation=" + designation
				+ ", hours=" + hours + ", days=" + days + ", dates=" + dates
				+ ", instructor=" + instructor + ", room=" + room + "]";
	}
}
