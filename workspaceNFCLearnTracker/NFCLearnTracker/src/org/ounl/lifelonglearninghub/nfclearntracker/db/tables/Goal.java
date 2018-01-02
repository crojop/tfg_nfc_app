 /*******************************************************************************
 * Copyright (C) 2014 Open University of The Netherlands
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
package org.ounl.lifelonglearninghub.nfclearntracker.db.tables;

import android.content.ContentValues;
import android.database.Cursor;

public class Goal implements ITables{
	
	public static final String TABLE_NAME = "goals";
	
	public static final String KEY_NAME = "name";
	public static final String KEY_DESC = "description";
	public static final String KEY_DAILY_TIME= "daily_time";

    
	
	private String sName;
	private String sDesc;	
	private long lDailyTimeMills;
	
	
	public Goal(String sName, String sDesc, long lDailyTime) {
		
		this.sName = sName;
		this.sDesc = sDesc;
		
		this.lDailyTimeMills = lDailyTime;
	}
	
	public Goal (Cursor c){
        
        
        this.sName = c.getString(0);
        this.sDesc = c.getString(1);        
        this.lDailyTimeMills = Long.parseLong(c.getString(2));

		
	}	
	
	
	
	/**
	 * @return the lDailyTime
	 */
	public long getDailyTimeMills() {
		return lDailyTimeMills;
	}
	/**
	 *  
	 * @return minutes for daily time
	 */
	public int getDailyTimeMins(){
		int minutes = (int) ((lDailyTimeMills / (1000*60)) % 60);		
		return minutes;
	}
	
	
	/**
	 * @param lDailyTime the sDailyTime to set
	 */
	public void setDailyTime(long lDailyTime) {
		this.lDailyTimeMills = lDailyTime;
	}
	/**
	 * @return the sName
	 */
	public String getsName() {
		return sName;
	}
	/**
	 * @param sName the sName to set
	 */
	public void setsName(String sName) {
		this.sName = sName;
	}
	/**
	 * @return the sDesc
	 */
	public String getsDesc() {
		return sDesc;
	}
	/**
	 * @param sDesc the sDesc to set
	 */
	public void setsDesc(String sDesc) {
		this.sDesc = sDesc;
	}

	
	public static String getCreateTable(){
		
        String sSQL = "CREATE TABLE " + TABLE_NAME + "("
                + KEY_NAME + " TEXT PRIMARY KEY," + KEY_DESC + " TEXT, " + KEY_DAILY_TIME + " INTEGER" + ")";

		
		return sSQL;
		
	}
	

	public void loadContentValues(ContentValues cv) {
			
		 
		 cv.put(KEY_NAME, getsName());
		 cv.put(KEY_DESC, getsDesc());
		 cv.put(KEY_DAILY_TIME, getDailyTimeMills());

		 			
	}


}
