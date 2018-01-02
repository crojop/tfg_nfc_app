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
package org.ounl.lifelonglearninghub.learntracker.gis.ou.db.tables;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.content.ContentValues;
import android.database.Cursor;
import android.util.Log;

public class ActividadDb implements ITables{
	

	public static final String TABLE_NAME = "activities";
	public static final String KEY_ID_SUBJECT = "id_subject";
	public static final String KEY_DATE_CHECKIN = "date_check_in";
	public static final String KEY_DATE_CHECKOUT = "date_check_out";
	public static final String KEY_LATITUDE = "latitude";
	public static final String KEY_LONGITUDE = "longitude";	
    
	public static final long PENDING_TO_CHECK_OUT = -1; 
	
	private String sIdSubject;
	private long lDateCheckIn;
	private long lDateCheckOut;	
	private double dLatitude;
	private double dLongitude;
	
	

	public ActividadDb(String sIdTagactivity) {
		this.sIdSubject = sIdTagactivity;
	}
	
	public ActividadDb (Cursor c){
        

		this.sIdSubject = c.getString(0);
		if (c.getString(1)!=null){
			this.lDateCheckIn = Long.parseLong(c.getString(1));	
		}
		if (c.getString(2)!=null){
			this.lDateCheckOut = Long.parseLong(c.getString(2));
		}
		if (c.getString(3)!=null){
			this.dLatitude = Double.parseDouble(c.getString(3));	
		}
		if (c.getString(4)!=null){
			this.dLongitude = Double.parseDouble(c.getString(4));
		}
		
		
	}	



	public ActividadDb(String idSubject, long checkIn, long checkOut, double longi, double lati) {
		this.sIdSubject = idSubject;
		this.lDateCheckIn = checkIn;	
		this.lDateCheckOut = checkOut;
		this.dLongitude = longi;
		this.dLatitude = lati;
	}


	/**
	 * @return the lDateCheckIn
	 */
	public long getlDateCheckIn() {
		return lDateCheckIn;
	}

	/**
	 * @param lDateCheckIn
	 *            the lDateCheckIn to set
	 */
	public void setlDateCheckIn(long lDateCheckIn) {
		this.lDateCheckIn = lDateCheckIn;
	}

	/**
	 * @return the lDateCheckOut
	 */
	public long getlDateCheckOut() {
		return lDateCheckOut;
	}

	/**
	 * @param lDateCheckOut
	 *            the lDateCheckOut to set
	 */
	public void setlDateCheckOut(long lDateCheckOut) {
		this.lDateCheckOut = lDateCheckOut;
	}

	public String getsIdSubject() {
		return sIdSubject;
	}

	public void setsIdSubject(String sIdSbuject) {
		this.sIdSubject = sIdSbuject;
	}

	
	public double getdLatitude() {
		return dLatitude;
	}

	public void setdLatitude(double dLatitude) {
		this.dLatitude = dLatitude;
	}

	public double getdLongitude() {
		return dLongitude;
	}

	public void setdLongitude(double dLongitude) {
		this.dLongitude = dLongitude;
	}
	

	
	
	public static String getCreateTable(){
		
        String sSQL = "";

        sSQL= "CREATE TABLE "+TABLE_NAME+" ("+KEY_ID_SUBJECT+" TEXT, "+KEY_DATE_CHECKIN+" INTEGER, "+KEY_DATE_CHECKOUT+" INTEGER, "+KEY_LATITUDE+" REAL, "+KEY_LONGITUDE+" REAL, PRIMARY KEY ("+KEY_ID_SUBJECT+", "+KEY_DATE_CHECKIN+"));";
		
		return sSQL;
		
	}
	
	
	public void loadCheckValues(boolean isCheckIn, ContentValues cv, long date) {
			
		 cv.put(KEY_ID_SUBJECT, getsIdSubject());
		 if (isCheckIn){
			 cv.put(KEY_DATE_CHECKIN, date);
			 cv.put(KEY_DATE_CHECKOUT, PENDING_TO_CHECK_OUT);
		 }else{
			 cv.put(KEY_DATE_CHECKOUT, date);	 
		 }
		 
	}
	
	
	public void loadContentValues(ContentValues cv) {

		cv.put(KEY_ID_SUBJECT, sIdSubject);
		cv.put(KEY_DATE_CHECKIN, lDateCheckIn);
		cv.put(KEY_DATE_CHECKOUT, lDateCheckOut);
		cv.put(KEY_LATITUDE, dLatitude);
		cv.put(KEY_LONGITUDE, dLongitude);		

	}	
	
	public String getFormatedCheckIn(){

		Date dateIn = new Date(lDateCheckIn);
		DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");

		return formatter.format(dateIn);
	}
	
	public String getFormatedLongCheckIn(){

		Date dateIn = new Date(lDateCheckIn);
		DateFormat formatter = new SimpleDateFormat("EEEE, dd/MM/yyyy HH:mm:ss");

		return formatter.format(dateIn);
	}	
	
	public String getFormatedCheckOut(){

		Date dateOut = new Date(lDateCheckOut);
		DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");

		return formatter.format(dateOut);
	}	
	
	
	/**
	 * Difference in minutes between check-in and check-out
	 * @return
	 */
	public int durationMins(){
		
		
		long diff = lDateCheckOut - lDateCheckIn;
        long diffSeconds = diff / 1000 % 60;
        long diffMinutes = diff / (60 * 1000) % 60;
        long diffHours = diff / (60 * 60 * 1000);
        int diffInDays = (int) ((lDateCheckOut - lDateCheckIn) / (1000 * 60 * 60 * 24));


		return (int)(diffInDays * 1440) + (int)(diffHours * 60) + (int)diffMinutes;
	}
	
	public String toString(){
				
		return "["+sIdSubject+"]["+getFormatedCheckIn()+"]["+getFormatedCheckOut()+"]["+durationMins()+"]";
	}


}
