package wake.kevin;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.EntityNotFoundException;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.api.datastore.Transaction;

public class DemeritDatastoreService {
	
	private final String STAFF = "staff";
	private final String EMAIL = "email";
	private final String OFFICE = "officeHours";
	
	
	private final String COURSE_AND_SECTION = "courses";
	private final String NAME = "name";
	private final String TYPE = "type";
	
	private final String COURSE = "course";
	private final String SECTION_LIST = "sections";
	private final String CREDITS = "credits";
	
	private final String SECTION = "section";
	private final String DAYS = "days";
	private final String ROOM = "room";
	private final String TIME = "time";
	
	private final String DELIMITER = "~";
	
	private DatastoreService ds = null;
	
	/**
	 * Constructor for DemeritDatastoreService
	 */
	public DemeritDatastoreService()
	{
		ds = DatastoreServiceFactory.getDatastoreService();
	}
	
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


	//section must already exist
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
	
	//NOTE 10.26.14 15:12 if section key is not entered in proper format ("COURSENUM SECTIONNUM") method will fail
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
	
	
	//create course
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

	public String[] makeDelStringToArray(String mySections) {
		if(mySections.equals("")) {
			String[] myRetFixer = {""};
			return myRetFixer; 
		}
		
		String[] ret = new String[50];
		ret[0] = "";
		
		int i = mySections.indexOf(DELIMITER);
		int j = -1;
		int count = 0;
		while(i >= 0)
		{
			if(j == -1) j = 0;
			String addend = mySections.substring(j, i);
			ret[count] = addend;
			count++;
			j = i;
			i = mySections.indexOf(DELIMITER, i+1);
		}
		
		ret[count] = mySections.substring(j+1);
		
		return ret;
	}
	
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
	
	private String delimitedStringAppend(String mySections, String addend) {
		String ret = "";
		if(mySections != null) {
			ret = mySections + DELIMITER;
		}
		return ret + addend;
	}
	
	public String getOurKey(Key entry){
		String asString = entry.toString();
		int index1 = asString.indexOf("\"");
		int index2 = asString.indexOf("\"", index1 + 1);
		
		return asString.substring(index1 + 1, index2);
	}

	public Key createSectionKey(String courseNumber, String sectionNumber) {
		
		return KeyFactory.createKey(SECTION, (courseNumber + " " + sectionNumber));
	}
	
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
	
	public Entity getStaff(String myKey) throws EntityNotFoundException
	{
		return getEntity(STAFF,myKey);
	}
	public Entity getSection(String myKey) throws EntityNotFoundException
	{
		return getEntity(SECTION,myKey);
	}
	private Entity getCourse(String myKey) throws EntityNotFoundException
	{
		return getEntity(COURSE,myKey);
	}
}
