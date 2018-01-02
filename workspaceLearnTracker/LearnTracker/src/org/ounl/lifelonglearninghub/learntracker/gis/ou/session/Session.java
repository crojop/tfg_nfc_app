/*******************************************************************************
 * Copyright (C) 2014 Open University of The Netherlands
 * Author: Bernardo Tabuenca Archilla
 * LearnTracker project 
 * 
 * This library is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public License
 * along with this library.  If not, see <http://www.gnu.org/licenses/>.
 ******************************************************************************/
package org.ounl.lifelonglearninghub.learntracker.gis.ou.session;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;
import java.util.Map.Entry;

import org.ounl.lifelonglearninghub.learntracker.gis.ou.Constants;
import org.ounl.lifelonglearninghub.learntracker.gis.ou.db.DatabaseHandler;
import org.ounl.lifelonglearninghub.learntracker.gis.ou.db.tables.SubjectDb;
import org.ounl.lifelonglearninghub.learntracker.gis.ou.db.ws.dataobjects.SubjectDO;
import org.ounl.lifelonglearninghub.learntracker.gis.ou.swipe.DateUtils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.AvoidXfermode;
import android.location.LocationManager;
import android.util.Log;

@SuppressLint("UseValueOf") public class Session {

	private String CLASSNAME = this.getClass().getName();

	// Database
	private DatabaseHandler db;	
	
	// User identification
	private String user_name = "";
	private int user_type;
	private String course_id = "";
	private String version = "";
	private String ws_path = "";

	
	// Track list of check-ins and check-outs for each of the subject
	// where:
	// 		Integer indicates the order in which the items are presented
	//			in the swype list fragmetns.
	//		ActivitySession is the status
	private HashMap<Integer, ActivitySession> hmActvitities;
	
	
	// List of Subjects containing list of users that performed activity on this subject
	//
	// Key String is the subject id
	// Value HashMap<String,Long> is the list of users that have recorded time on this subject
	//
	private HashMap<String,HashMap<String,Long>> hmSubjects;
	
	
	
	private LocationManager locationManager;
	private double longitude;
	private double latitude;
	
	private static Session singleInstance;

	private Session() {
		Log.d(CLASSNAME, "Session initialized.");
	}

	public static Session getSingleInstance() {
		if (singleInstance == null) {
			synchronized (Session.class) {
				if (singleInstance == null) {
					singleInstance = new Session();
				}
			}
		}
		return singleInstance;
	}

	/**
	 * Indicates whether the app is correctly configured with the necessary
	 * params to work properly
	 * 
	 * @return
	 */
	public boolean isConfigured() {

		boolean bResult = true;
	
		if(isbDBInitilized()){
			if(!isValid(user_name)){
				bResult = false;
				Log.e(CLASSNAME, "User id has not been initialized.");					
			}else if (!isValid(course_id)){
				bResult = false;
				Log.e(CLASSNAME, "Course id has not been initialized.");
//			}else if (!isValid(imei)){
//				bResult = false;
//				Log.i(CLASSNAME, "IMEI has not been initialized.");
			}else if (!isValid(version)){
				bResult = false;
				Log.e(CLASSNAME, "Version has not been initialized.");				
			}
		}else{
			bResult = false;
			Log.i(CLASSNAME, "Database returned empty list of subjects");
		}
		

		return bResult;
	}

	/**
	 * Indicates whether a param is valid, that is, not null and not empty
	 * 
	 * @param sValue
	 * @return
	 */
	private boolean isValid(String sValue) {
		if (sValue != null) {
			if (sValue.trim().length() > 0) {
				return true;
			}
		}
		return false;
	}

	
	/**
	 * Inits database handler
	 * 
	 * @param ctx
	 */
	public void initDatabaseHandler(Context ctx){		
		db = new DatabaseHandler(ctx);	
		hmActvitities = new HashMap<Integer, ActivitySession>();
		
	}
	
	
	/**
	 * Gets database handler
	 * 
	 * @return
	 */
	public DatabaseHandler getDatabaseHandler(){
		return db;
	}
	
	
	
	/**
	 * Inits location manager
	 * 
	 * @param lm
	 */
	public void initLocation(LocationManager lm){		
		locationManager = lm ; 
		updateLocation();		
	}	
	
	/**
	 * Updates location manager
	 */
	public void updateLocation(){
		 
//		Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
//		if(location!=null){
//			longitude = location.getLongitude();
//			latitude = location.getLatitude();		
//			Log.d(CLASSNAME, "Updating location. Latitude:"+latitude+" / Longitude:"+longitude);
//		}else{
//			Log.e(CLASSNAME, "GPS seems to be disabled");
//		}
		
	}

	public double getLongitude() {
		return longitude;
	}

	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}

	public double getLatitude() {
		return latitude;
	}

	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}

	public boolean isbDBInitilized() {		
		if(db == null){
			Log.e(CLASSNAME, "Databasehandler not yet initialized");
			return false;
		}else{
			return (db.getSubjects().size() > 0);	
		}
		
	}

	
	
	// User
	public String getUserName() {
		return user_name;
	}

	public void setUserName(String sName) {
		this.user_name = sName;
	}
	
	public int getUserType() {
		return user_type;
	}

	public void setUserType(int iType) {
		this.user_type = iType;
	}	
	
	public boolean hasUserName(){
		return isValid(user_name);
	}
	
	//
	// Properties file
	//
	public String getCourse_id() {
		return course_id;
	}

	public void setCourse_id(String course_id) {
		this.course_id = course_id;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}
	
	public String getWSPath(){
		return this.ws_path;
	}
	
	
	/**
	 * Inits list of activities in session with given list
	 * of data objects from backend
	 * 
	 * @param lista
	 */
	public void initActivitiesDO(List<SubjectDO> lista){		
		for (int i = 0; i < lista.size(); i++) {
			
			Long oForeseen = new Long(lista.get(i).getSubject_task_time_duration());
			
			ActivitySession element = new ActivitySession(lista.get(i).getId(),lista.get(i).getSubject_task_desc(),oForeseen.longValue());
			Integer iOrder = new Integer(lista.get(i).getSubject_task_order());			
			hmActvitities.put(iOrder, element);

			
		}		
		Log.i(CLASSNAME, "initActivitiesDO Session initialized session with "+hmActvitities.size()+" ActivitySessions");
	}
	
	/**
	 * Inits list of activities in session with given list
	 * of data objects from localdatabse
	 * 
	 * @param lista
	 */
	public void initActivitiesDb(List<SubjectDb> lista){		
		
		for (int i = 0; i < lista.size(); i++) {
			
			Long oForeseen = new Long(lista.get(i).getlTaskTimeDuration());
			
			ActivitySession element = new ActivitySession(lista.get(i).getsId(),lista.get(i).getsTaskDesc(),oForeseen.longValue());
			Integer iOrder = new Integer(lista.get(i).getiTaskOrder());			
			hmActvitities.put(iOrder, element);

			
		}
		Log.i(CLASSNAME, "initActivitiesDb Session initialized session with "+hmActvitities.size()+" ActivitySessions");		
	}	

	/**
	 * Prints current state of the activities in the session
	 */
	public void printActivities() {
	    Iterator it = hmActvitities.entrySet().iterator();
	    while (it.hasNext()) {
	    	HashMap.Entry pairs = (HashMap.Entry)it.next();
	        Log.d(CLASSNAME, "["+pairs.getKey()+"]"+pairs.getValue().toString());
	        //it.remove(); // avoids a ConcurrentModificationException
	    }
	}	
	
	/**
	 * Returns activity for the given position
	 * 
	 * @param position
	 * @return
	 */
	public ActivitySession getActivity(int position){		
		return hmActvitities.get(position);		
	}
	


	
	/**
	 * Returns list of existing activities ordered by position
	 * 
	 * @return
	 */
	public List<ActivitySession> getActivities(){
		
		List<ActivitySession> las = new ArrayList<ActivitySession>();
		for (int i = 0; i < getActivitiesCount(); i++) {
			las.add(i, hmActvitities.get(i));			
		}
		return las;		
	}
	
	
	/**
	 * Returns number of activities
	 * @return
	 */
	public int getActivitiesCount(){
		return hmActvitities.size();
	}
	
	
	/**
	 * Sets hmSubject local structure.
	 * 
	 * @param theHmSubjects
	 */
	public void setSocialActivity(HashMap<String,HashMap<String,Long>> theHmSubjects){
		hmSubjects = theHmSubjects;
	}

	/**
	 * Prints content of the activity accumulated by user and subject 
	 */
	public void printSocialActivity(){
		
		DateUtils du = new DateUtils();
		
		for (Entry<String, HashMap<String, Long>> entrySubject : hmSubjects.entrySet()) {
		    String keyHmUSers = entrySubject.getKey();
		    HashMap<String, Long> mpUsers = entrySubject.getValue();
		    Log.d(CLASSNAME, "Iterating over Task ["+keyHmUSers + "]");
		    
		    for (Entry<String, Long> entry : mpUsers.entrySet()) {
		        String key = entry.getKey();
		        Long value = entry.getValue();
		        Log.d(CLASSNAME, "  - "+key+" ["+du.duration(value) + "]");
		    }		    		    
		}		
	}
	
	/**
	 * Returns an order list of average time for each on of the subjects.
	 * The list is ordered in the same order as in the yardstick.
	 * 
	 * @return
	 */
	public double[] getSocialActivityByOrderedSubject(){

		double[] adAVGClassroom = new double[getActivitiesCount()];
		
		for (int i = 0; i < getActivitiesCount(); i++) {
			ActivitySession as = hmActvitities.get(i);
			try{
				HashMap<String,Long> users = hmSubjects.get(as.getId_subject());			
				adAVGClassroom[i] = getAVGSocialActivityBySubject(users);				
			}catch(Exception e){
				adAVGClassroom[i] = 0;
			}
						
		}
		
		return adAVGClassroom;
		
	}	
		
	
	/**
	 * Returns sum of time in the activity for the given subject map.
	 * The value returned is in decimal hours.
	 * 
	 * @param mpUsers
	 * @return sum in hours
	 */
	@SuppressWarnings("unused")
	private double getAVGSocialActivityBySubject(HashMap<String,Long> mpUsers){
		

		double doubAVG = 0;		
		int iCountUsers = 0;
		
		if(mpUsers == null){
			return doubAVG;
		}else{
			iCountUsers = mpUsers.size();
		}
		
		if(iCountUsers == 0){
			return doubAVG;
		}
			
	    for (Entry<String, Long> entry : mpUsers.entrySet()) {
	        Long value = entry.getValue();
	        doubAVG+=value;
	        //Log.d(CLASSNAME, "  - "+key+" ["+du.duration(value) + "]");
	    }	


	    DateUtils d = new DateUtils();
	    double dAvgMills = doubAVG / mpUsers.size();
	    long lAvgMills = (long)dAvgMills; 
	    double result = DateUtils.round(d.toHours(lAvgMills),2);
	    
	    return result;	
	}

	
	
	/**
	 * @param properties
	 *            the properties to set
	 */
	public void setProperties(Properties properties) {
//		this.user_id = properties.getProperty(Constants.UP_USER_ID);
//		this.imei = properties.getProperty(Constants.UP_IMEI);
		this.version = properties.getProperty(Constants.CP_VERSION);
		this.course_id = properties.getProperty(Constants.CP_COURSE_ID);
		this.ws_path = properties.getProperty(Constants.CP_WS_PATH);
		
		printProperties();
	}
	
	/**
	 * Prints values for existing properties in file lllhub.properties
	 */
	public void printProperties(){
		Log.d(CLASSNAME, "Properties | " + Constants.UP_USER_ID+" : "+user_name				
				+ " | " + Constants.CP_COURSE_ID+" : "+course_id
				+ " | " + Constants.CP_WS_PATH+" : "+ws_path
//				+ " | " + Constants.UP_IMEI+" : "+imei
				+ " | " + Constants.CP_VERSION+" : "+version
				);
	}
	


}
