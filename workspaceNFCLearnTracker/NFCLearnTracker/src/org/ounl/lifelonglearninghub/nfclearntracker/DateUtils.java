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
package org.ounl.lifelonglearninghub.nfclearntracker;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;


public class DateUtils {
	
	
	/**
	 * Difference in minutes between check-in and check-out
	 * 
	 * @return
	 */
	public String duration(long in, long out) {

		long ldiff = Long.valueOf(out).longValue() - Long.valueOf(in).longValue();
		return duration(ldiff);
	}
	
	/**
	 * Returns string hh:mm:ss for the mills given as param
	 * 
	 * @param mills
	 * @return
	 */
	public String duration(long mills) {
		
		int seconds = (int) (mills / 1000) % 60 ;
		int minutes = (int) ((mills / (1000*60)) % 60);
		int hours   = (int) ((mills / (1000*60*60)) % 24);
		
		String sSecs = ""+seconds;
		if (seconds < 10) {
			sSecs = "0"+seconds;
		}

		String sMins = ""+minutes;
		if (minutes < 10) {
			sMins = "0"+minutes;
		}
		
		String sHours = ""+hours;
		if (hours < 10) {
			sHours = "0"+hours;
		}		

		
		return sHours+":"+sMins+":"+sSecs;
		
	}	
	
	/**
	 * Returns minutes for given milliseconds overflowing positively
	 * 
	 * @param lMills
	 * @return
	 */
	public long toMins(long lMills){
		
		long minutes = TimeUnit.MILLISECONDS.toMinutes(lMills);
		
		return minutes ;
	}
	
	/**
	 * Returns hours for given milliseconds in double format
	 * 
	 * @param lMills
	 * @return
	 */
	public double toHours(long lMills){
		
		double hours = 0;
		double mins = 0;
		double seconds = 0;
		
		seconds = lMills / 1000;
		mins = seconds / 60;
		hours = mins / 60;
		
		return hours;
	}	
	
	
	/**
	 * Returns milliseconds for giving hours and mins
	 * 
	 * @param iHours
	 * @param iMins
	 * @return
	 */
	public long toMills (int iHours, int iMins){
		
		long lMillsMins = iMins * 60 * 1000;
		long lMillsHours = iHours * 60 * 60 * 1000;				
		
		return (lMillsMins + lMillsHours);		
	}
	
	/**
	 * Returns formated date for given milliseconds 
	 * 
	 * @param lDate
	 * @return
	 */
	public String getFormatedCheckIn(long lDate ){

		Date dateIn = new Date(lDate);
		DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");

		return formatter.format(dateIn);
	}	
	

}
