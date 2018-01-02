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

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * This class implements the status in which an Activity could be.
 * 
 * It can be activated (started, having check_in time) or deactivated (stopped)
 * @author BTB
 *
 */
public class ActivitySession {

	// Constants
	private static final DateFormat DATE_FORMATTER = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
	private static final String INIT_DATE_STRING = "25/08/1977 16:05:00";
	private static Date INIT_DATE;
	private static final double INIT_LATITUDE = 41.35;
	private static final double INIT_LONGITUDE = -1.63;
	public static int STATUS_STARTED = 1;
	public static int STATUS_STOPPED = 0;	
	
	// Vars
	private int status = STATUS_STOPPED;
	private String id_subject;
	private String subject_task_desc;
	private Date date_checkin;
	private double location_latitude;
	private double location_longitude;
	private long subject_task_time_duration;
	

	public ActivitySession(String sSubjectId, String sSubjectDesc, long lForeseen) {
		this.id_subject = sSubjectId;
		this.subject_task_desc = sSubjectDesc;
		this.subject_task_time_duration = lForeseen;
		
		reset();
	}


	/**
	 * Inits activity with default values
	 * 
	 */
	private void reset() {
		
		try {
			INIT_DATE = DATE_FORMATTER.parse(INIT_DATE_STRING);
			this.date_checkin = INIT_DATE;
			this.location_latitude = INIT_LATITUDE;
			this.location_longitude = INIT_LONGITUDE;
			this.status = STATUS_STOPPED;
			
		} catch (ParseException e) {
			e.printStackTrace();
		}
	}	
	

	/**
	 * Sets current time as checkin
	 * Sets current status as STARTED
	 */
	public void doCheckIn(){
		
		date_checkin = new Date();
		this.location_latitude = Session.getSingleInstance().getLatitude();
		this.location_longitude = Session.getSingleInstance().getLongitude();
		this.status = STATUS_STARTED;
		
	}

	
	/**
	 * Sets current status as STOPPED
	 * @return check in time
	 */
	public long doCheckOut(){
		long lCheckIn = date_checkin.getTime();
		reset();
		return lCheckIn;
	}
	

	public int getStatus(){
		return status;
	}
	
	
	// Getters and setters
	public String getId_subject() {
		return id_subject;
	}

	public void setId_subject(String id_subject) {
		this.id_subject = id_subject;
	}

	public Date getDate_checkin() {
		return date_checkin;
	}

	public void setDate_checkin(Date date_checkin) {
		this.date_checkin = date_checkin;
	}

	public double getLocation_latitude() {
		return location_latitude;
	}

	public void setLocation_latitude(double location_latitude) {
		this.location_latitude = location_latitude;
	}

	public double getLocation_longitude() {
		return location_longitude;
	}

	public void setLocation_longitude(double location_longitude) {
		this.location_longitude = location_longitude;
	}

	public String getSubject_task_desc() {
		return subject_task_desc;
	}
	
	public long getForeseenDuration(){
		return subject_task_time_duration;
	}
	
	
	public String toString() {

		String sAux = "";

		sAux += "* id_subject: " + id_subject;
		sAux += "| subject_task_desc: " + subject_task_desc;		
		sAux += "| date_checkin: " + date_checkin;
		sAux += "| location_latitude: " + location_latitude;
		sAux += "| location_longitude: " + location_longitude;
		sAux += "| status: " + status;
		sAux += "| foreseen_time_duration: " + subject_task_time_duration;

		return sAux;
	}	

}