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
 * aString with this library.  If not, see <http://www.gnu.org/licenses/>.
 ******************************************************************************/
package org.ounl.lifelonglearninghub.mediaplayer.db.tables;

import android.content.ContentValues;
import android.database.Cursor;

public class Video implements ITables{
	
	public static final String TABLE_NAME = "videos";
	
	public static final String KEY_NAME = "name";
	public static final String KEY_DESC = "description";
	public static final String KEY_URL= "url";

    
	
	private String sName;
	private String sDesc;	
	private String sURL;
	
	
	public Video(String sName, String sDesc, String sUrl) {
		
		this.sName = sName;
		this.sDesc = sDesc;
		
		this.sURL = sUrl;
	}
	
	public Video (Cursor c){
        
        
        this.sName = c.getString(0);
        this.sDesc = c.getString(1);        
        this.sURL = c.getString(2);

		
	}	
	
	


	/**
	 * @return the sName
	 */
	public String getName() {
		return sName;
	}
	/**
	 * @param sName the sName to set
	 */
	public void setName(String sName) {
		this.sName = sName;
	}
	/**
	 * @return the sDesc
	 */
	public String getDesc() {
		return sDesc;
	}
	/**
	 * @param sDesc the sDesc to set
	 */
	public void setDesc(String sDesc) {
		this.sDesc = sDesc;
	}

	public String getURL() {
		return sURL;
	}

	public void setURL(String sURL) {
		this.sURL = sURL;
	}	
	
	
	public static String getCreateTable(){
		
        String sSQL = "CREATE TABLE " + TABLE_NAME + "("
                + KEY_NAME + " TEXT PRIMARY KEY," 
        		+ KEY_DESC + " TEXT, " 
                + KEY_URL + " TEXT" + ")";

		
		return sSQL;
		
	}
	

	public void loadContentValues(ContentValues cv) {
			
		 
		 cv.put(KEY_NAME, getName());
		 cv.put(KEY_DESC, getDesc());
		 cv.put(KEY_URL, getURL());

		 			
	}




}
