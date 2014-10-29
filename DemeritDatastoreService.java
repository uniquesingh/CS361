package edu.uwm.cs361;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.EntityNotFoundException;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.api.datastore.Transaction;

public class DemeritDatastoreService {
	
	public final String STAFF = "staff";
	private final String EMAIL = "email";
	private final String OFFICE = "officeHours";
	
	
	private final String COURSE_AND_SECTION = "courses";
	private final String NAME = "name";
	private final String TYPE = "type";
	
	public final String COURSE = "course";
	private final String SECTION_LIST = "sections";
	private final String CREDITS = "credits";
	
	public final String SECTION = "section";
	private final String DAYS = "days";
	private final String ROOM = "room";
	private final String TIME = "time";
	
	private final String DELIMITER = "~";
	
	private DatastoreService ds = null;
	
	/**
	 * Constructor for DemeritDatastoreService
	 * Use an instance of this for creates, updates, retrievals.
	 * Use the getDatastore() method to get a DS reference for your queries.
	 */
	public DemeritDatastoreService()
	{
		ds = DatastoreServiceFactory.getDatastoreService();
	}
	
	/**
	 * getDatastore
	 * 
	 * Gets a reference to the internal datastore object- used to run your own queries
	 * @return reference to the internal datastore.
	 */
	public DatastoreService getDatastore(){
		return ds;
	}
	
	/**
	 * CreateStaff
	 * 
	 * Creates a new staff within the datastore.
	 * <br><br>Precondition: Method does not check for duplicates, must call hasNoDuplicate(String entityType, String myKey) first.
	 * 
	 * @param email String representing staff's email address
	 * @param name String representing staff's name: First Last
	 * @param courseNumSectionNum: String array, each index is a string representing a class taught, MUST be in "COURSENUM SECTIONNUM" format.
	 * @param officeHours String array, each index is a string representing a set of office hours. MUST be in "DAY TIMEFROM-TIMETO" format.
	 * @param type String representing type of staff: TA, Instructor, Admin.
	 * @throws EntityNotFoundException Throws exception if any index of courseNumSectionNum is not already created.
	 * 
	 * @postcondition Staff will be an entity in datastore.
	 */
	public void createStaff(String email, String name, String[] courseNumSectionNum, String[] officeHours, String type) throws EntityNotFoundException
	{
		
		String cNSN = makeDelString(courseNumSectionNum);
		String oH = makeDelString(officeHours);
		
		Entity newStaff = new Entity(STAFF,email);
		newStaff.setProperty(TYPE, type);
		newStaff.setProperty(NAME,name);
		newStaff.setProperty(COURSE_AND_SECTION,cNSN);
		newStaff.setProperty(OFFICE,oH);
		
		ds.put(newStaff);
		
		if(!cNSN.equals(""))
			addStaffToSection(courseNumSectionNum, email);
		
	}
	
	/*
	 * Helper method
	 */
	private void addStaffToSection(String[] courseNumSectionNum, String email) throws EntityNotFoundException {
		Entity mySection = null;
		
		for(int i = 0; i < courseNumSectionNum.length; ++i)
		{
			if(courseNumSectionNum[i].equals("") || courseNumSectionNum[i] == null)
				return;
			
			Transaction txn = ds.beginTransaction();
			try {
				mySection = getSection(courseNumSectionNum[i]);
				
				Entity myRealSection = ds.get(mySection.getKey());
				myRealSection.setProperty(STAFF, email);
				
				ds.put(myRealSection);
				txn.commit();
			} finally {
				if (txn.isActive()) {
					txn.rollback();
				}
			}
			
		}
		
	}

	/*
	 * Helper method
	 */
	private void addSectionToStaff(String courseNumSectionNum, String staffEmail) throws EntityNotFoundException {
		Transaction txn = ds.beginTransaction();
		try {
			Entity myTeacher = getStaff(staffEmail);
			myTeacher = ds.get(myTeacher.getKey());
			
			String mySections = (String) myTeacher.getProperty(COURSE_AND_SECTION);
			mySections = delimitedStringAppend(mySections,courseNumSectionNum);
			
			myTeacher.setProperty(COURSE_AND_SECTION, mySections);
			
			ds.put(myTeacher);
			
			txn.commit();
		} finally {
			if (txn.isActive()) {
				txn.rollback();
			}
		}
	}
	
	//course num and section num cannot change
	/**
	 * updateSection
	 * 
	 * Use this to change any fields of the section, 
	 * <br>EXCEPT the course number or section number ~these cannot change.
	 * 
	 * @param courseNumber String representing the course number
	 * @param days Days of the week section meets. Use single letter representation to match standard. ex) M W R F
	 * @param lecLabDis String indicating if section is LEC, LAB, or DIS
	 * @param sectionNumber String representing the section number
	 * @param room String representing the section location. ex) EMS W120
	 * @param staffEmail String representing the staff's email address, use "" if none
	 * @param time String representing meeting times.
	 * @throws EntityNotFoundException Throws exception if trying to update course which doesn't exist
	 */
	public void updateSection(String courseNumber, String days, String lecLabDis,
			String sectionNumber, String room, String staffEmail,
			String time) throws EntityNotFoundException
	{
		Transaction txn = ds.beginTransaction();
		try {
			String scKey = courseNumber+" "+sectionNumber;
		    Key sectionKey = KeyFactory.createKey(SECTION, scKey);
		    Entity updatedSection = ds.get(sectionKey);
			
		    updatedSection.setProperty(DAYS, days);
		    updatedSection.setProperty(TYPE, lecLabDis);
		    updatedSection.setProperty(ROOM, room);
		    updatedSection.setProperty(STAFF, staffEmail);
		    updatedSection.setProperty(TIME, time);
			    
			ds.put(updatedSection);
			
			if(!staffEmail.equals(""))
					addSectionToStaff(scKey, staffEmail);
		
			txn.commit();
		} 
			
		finally {
			if (txn.isActive()) {
				txn.rollback();
			}
		}
		
	}

	/**
	 * updateStaff
	 * 
	 * Use this to update any of the passed staff's fields. 
	 * <br>Email CANNOT change and any sections MUST already exist **Will e updating to allow changing email**
	 * 
	 * @param email String representing staff's current email CANNOT change
	 * @param name  String representing staffs new / unchanged name
	 * @param courseNumSectionNum String array, each index is a string representing a class taught, MUST be in "COURSENUM SECTIONNUM" format.
	 * @param officeHours String array, each index is a string representing a set of office hours. MUST be in "DAY TIMEFROM-TIMETO" format.
	 * @param type String indicating Instructor or TA
	 * @throws EntityNotFoundException Throws exception if staff is not found. i.e. email not existing staff
	 */
	public void updateStaff(String email, String name, String[] courseNumSectionNum, String[] officeHours, String type) throws EntityNotFoundException 
	{

			Transaction txn = ds.beginTransaction();
			try {
			    Key employeeKey = KeyFactory.createKey(STAFF, email);
			    Entity employee = ds.get(employeeKey);
  
			    employee.setProperty(NAME, name);
			    employee.setProperty(TYPE, type);
			     
			    String newCourseSection = makeDelString(courseNumSectionNum);
			    String newOfficeHours = makeDelString(officeHours);
				    
				employee.setProperty(COURSE_AND_SECTION, newCourseSection);
				employee.setProperty(OFFICE, newOfficeHours);
				    
				
				addStaffToSection(courseNumSectionNum, email);
				
				ds.put(employee);
				txn.commit();
			} 
				
			finally {
				if (txn.isActive()) {
					txn.rollback();
				}
			}
		

	}
	
	/**
	 * hasNoDuplicate
	 * 
	 * Returns true if there is no duplicate of the passed key within the datastore, false otherwise.
	 * 
	 * @param entityType String representing what you are looking for. Can use our datastore instance variables:
	 * <br>ds.STAFF
	 * <br>ds.SECTION
	 * <br>ds.COURSE
	 * @param myKey String of the key for the particular obejct you are checking.
	 * <br>For staff: use email
	 * <br>For course: use course number
	 * <br>For section, use COURSENUM SECTIONNUM !If section key not entered properly, will fail
	 * @return True if entity already exists, false otherwise.
	 */
	public boolean hasNoDuplicate(String entityType, String myKey){
		
		Transaction txn = ds.beginTransaction();
		try {
		    Key entityKey = KeyFactory.createKey(entityType, myKey);
		    @SuppressWarnings("unused")
			Entity lookingFor = ds.get(entityKey);
		} catch (EntityNotFoundException e) {
		    return true;
		}
		txn.commit();
		
		return false;
	}
	
	
	/**
	 * createCourse
	 * 
	 * Adds a new course to the datastore. MUST be created with an initial section. MUST check for duplicates first.
	 * <br>If no staff, pass "" into email
	 * @param name String representing the staff teaching the course
	 * @param number String containing the course number
	 * @param sectionNumber String containing the section number
	 * @param credits String containing the number of credits
	 * @param days String of days of the week section meets. Use single letter representation to match standard. ex) M W R F
	 * @param lecLabDis String indicating if section is LEC, LAB, or DIS 
	 * @param room String of the room section meets
	 * @param staffEmail String representing the email of the section's instructor ("" if no staff)
	 * @param time String of the times the section is meeting
	 * @throws EntityNotFoundException Throws exception if the passed staff does not exist.
	 */
	public void createCourse(String name, String number, String sectionNumber, String credits, 
							String days, String lecLabDis,
							String room, String staffEmail, String time) throws EntityNotFoundException
	{
		
		Entity newCourse = new Entity(COURSE,number);
		newCourse.setProperty(NAME,name);
		newCourse.setProperty(CREDITS, credits);
		
		ds.put(newCourse);

		createSection(number, days, lecLabDis,
				sectionNumber, room, staffEmail, time);
	}
	
	/**
	 * createSection
	 * 
	 * Adds a section to a given course, saves to datastore.
	 * <br>Both the staff AND the course MUST already exist
	 * 
	 * @param courseNumber String representing course number
	 * @param days String representing days the section meets
	 * @param lecLabDis String indicating section type: LEC, LAB, or DIS
	 * @param sectionNumber String representing section number
	 * @param room String indicating section's meeting room
	 * @param staffEmail String containing section's instructor's email address
	 * @param time String representing times the section meets
	 * @throws EntityNotFoundException Throws exception if either staff OR course do not exist
	 */
	public void createSection(String courseNumber, String days, String lecLabDis,
							String sectionNumber, String room, String staffEmail,
							String time) throws EntityNotFoundException
	{
		String scKey = courseNumber+" "+sectionNumber;
		Entity newSection = new Entity(SECTION, scKey);
		
		
		newSection.setProperty(DAYS,days);
		newSection.setProperty(TYPE,lecLabDis);
		newSection.setProperty(ROOM,room);
		newSection.setProperty(STAFF,staffEmail);
		newSection.setProperty(TIME,time);

		if(!staffEmail.equals(""))
			addSectionToStaff(scKey, staffEmail);
		
		Transaction txn = ds.beginTransaction();
		try {
			Entity myFakeCourse = getCourse(courseNumber);
			Entity myCourse = ds.get(myFakeCourse.getKey());
			
			String mySections = (String) myCourse.getProperty(SECTION_LIST);
			mySections = delimitedStringAppend(mySections,sectionNumber);
			
			myCourse.setProperty(SECTION_LIST,mySections);
		
			ds.put(myCourse);
			
			txn.commit();
		} finally {
			if (txn.isActive()) {
				txn.rollback();
			}
		}
	
		ds.put(newSection);
	}

	/**
	 * makeDelStringToArray
	 * 
	 * Makes a deliminated string into an array.Likely string will be office hours or section list.
	 * <br>Use this on the entity properties from the entities you get from queries
	 * 
	 * @param stringIn Deliminated string to convert to array
	 * @return String array containing the stringIn tokens
	 */
	public String[] makeDelStringToArray(String stringIn) {
		if(stringIn.equals("")) {
			String[] myRetFixer = {""};
			return myRetFixer; 
		}
		
		String[] ret = new String[50];
		ret[0] = "";
		
		int i = stringIn.indexOf(DELIMITER);
		int j = -1;
		int count = 0;
		while(i >= 0)
		{
			if(j == -1) j = 0;
			String addend = stringIn.substring(j, i);
			ret[count] = addend;
			count++;
			j = i;
			i = stringIn.indexOf(DELIMITER, i+1);
		}
		
		ret[count] = stringIn.substring(j+1);
		
		return ret;
	}
	
	/*
	 * Helper method
	 */
	private String makeDelString(String[] rawStr){

		String ret = "";
		
		for(int i=0;i< rawStr.length;++i) {
			if(i > 0) {
				ret += DELIMITER;
			}
			
			ret += rawStr[i];
			
		}
		
		return ret;
	}
	
	/*
	 * Helper method
	 */
	private String delimitedStringAppend(String mySections, String addend) {
		String ret = "";
		if(mySections != null) {
			ret = mySections + DELIMITER;
		}
		return ret + addend;
	}
	
	/**
	 * getOurKey
	 * 
	 * Gets an actual key value from a Key value returned from an Entities getKey() method
	 * 
	 * @param entry Key value returned from an Entities getKey() method
	 * @return String containing a usable key
	 */
	public String getOurKey(Key entry){
		String asString = entry.toString();
		int index1 = asString.indexOf("\"");
		int index2 = asString.indexOf("\"", index1 + 1);
		
		return asString.substring(index1 + 1, index2);
	}

	/**
	 * createSectionKey
	 * 
	 * Creates a unique section key which can be used for queries, datastore retrieves, datastore updates, etc.
	 * 
	 * @param courseNumber String representing the course number
	 * @param sectionNumber String representing the section number
	 * @return Key unique section key
	 */
	public Key createSectionKey(String courseNumber, String sectionNumber) {
		
		return KeyFactory.createKey(SECTION, (courseNumber + " " + sectionNumber));
	}
	
	/*
	 * Helper method
	 */
	private Entity getEntity(String type, String myKey) throws EntityNotFoundException
	{

		Entity lookingFor;
		Transaction txn = ds.beginTransaction();
		try {
		    Key staffKey = KeyFactory.createKey(type, myKey);
			lookingFor = ds.get(staffKey);
			txn.commit();
		}finally {
			if (txn.isActive()) {
				txn.rollback();
			}
		}
		
		return lookingFor;
	}
	
	/**
	 * getStaff
	 * 
	 * Returns a staff entity from datastore based off the unique email identifier
	 * 
	 * @param myKey String representing the staff's email address
	 * @return Entity representing the given staff
	 * @throws EntityNotFoundException Throws exception if staff does not exist
	 */
	public Entity getStaff(String myKey) throws EntityNotFoundException
	{
		return getEntity(STAFF,myKey);
	}
	
	/**
	 * getSection
	 * 
	 * Returns a section entity from datastore based off the unique section identifier(COURSENUM SECTIONNUM)
	 * <br>Can use createSectionKey() to get key value
	 * 
	 * @param myKey String representing the the section
	 * @return Entity representing the given section
	 * @throws EntityNotFoundException Throws exception if section does not exist
	 */
	public Entity getSection(String myKey) throws EntityNotFoundException
	{
		return getEntity(SECTION,myKey);
	}
	
	/**
	 * getCourse
	 * 
	 * Returns a course entity from datastore based off the unique course identifier(COURSENUM)
	 * 
	 * @param myKey String representing the course number
	 * @return Entity representing the given course
	 * @throws EntityNotFoundException Throws exception if course does not exist
	 */
	private Entity getCourse(String myKey) throws EntityNotFoundException
	{
		return getEntity(COURSE,myKey);
	}
}
