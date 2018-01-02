 /*******************************************************************************
 * Copyright (C) 2015 Open University of The Netherlands
 * Author: Bernardo Tabuenca Archilla
 * Lifelong Learning Hub project 
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
package org.ounl.lifelonglearninghub.nfcecology.db.tables;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.content.ContentValues;
import android.database.Cursor;

public class Tagctivity implements ITables{
	

	public static final String TABLE_NAME = "activities";
	public static final String KEY_ID_TAG = "id_tag";
	public static final String KEY_DATE_CHECKIN = "date_check_in";
	public static final String KEY_DATE_CHECKOUT = "date_check_out";
    
	public static final long PENDING_TO_CHECK_OUT = -1; 
	
	private String sIdTag;
	private long lDateCheckIn;
	private long lDateCheckOut;	
	
	

	public Tagctivity(String sIdTagactivity) {
		this.sIdTag = sIdTagactivity;
	}
	
	public Tagctivity (Cursor c){
        

		this.sIdTag = c.getString(0);
		if (c.getString(1)!=null){
			this.lDateCheckIn = Long.parseLong(c.getString(1));	
		}
		if (c.getString(2)!=null){
			this.lDateCheckOut = Long.parseLong(c.getString(2));
		}
		
	}	



	public Tagctivity() {

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

	public String getsIdTagctivity() {
		return sIdTag;
	}

	public void setsIdTagctivity(String sIdTagctivity) {
		this.sIdTag = sIdTagctivity;
	}

	
	public static String getCreateTable(){
		
        String sSQL = "";

        sSQL= "CREATE TABLE "+TABLE_NAME+" ("+KEY_ID_TAG+" TEXT, "+KEY_DATE_CHECKIN+" INTEGER, "+KEY_DATE_CHECKOUT+" INTEGER, PRIMARY KEY ("+KEY_ID_TAG+", "+KEY_DATE_CHECKIN+"));";
		
		return sSQL;
		
	}
	
	
	public void loadCheckValues(boolean isCheckIn, ContentValues cv, long date) {
			
		 cv.put(KEY_ID_TAG, getsIdTagctivity());
		 if (isCheckIn){
			 cv.put(KEY_DATE_CHECKIN, date);
			 cv.put(KEY_DATE_CHECKOUT, PENDING_TO_CHECK_OUT);
		 }else{
			 cv.put(KEY_DATE_CHECKOUT, date);	 
		 }
		 
		 
		 			
		 
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
		
		
		long ldiff = lDateCheckOut - lDateCheckIn;
		int minutes = (int) ((ldiff / (1000*60)) % 60);
		
		
		return minutes;
	}
	
	public String toString(){
				
		return "["+sIdTag+"]["+getFormatedCheckIn()+"]["+getFormatedCheckOut()+"]";
	}
	

}
