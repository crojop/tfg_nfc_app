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

import android.content.ContentValues;
import android.database.Cursor;

public class Tag implements ITables{
	
	public static final String TABLE_NAME = "tags";
	public static final String KEY_ID_TAG = "id_tag";
	public static final String KEY_ID_GOAL = "id_goal";
	public static final String KEY_VALIDITY_BEGIN = "date_validity_begin";
	public static final String KEY_VALIDITY_END = "date_validity_end";
	public static final String KEY_COLOR = "color";
    	
	
	
	private String sIdTag;
	private String sIdGoal;
	private long dDateValidityBegin;
	private long dDateValidityEnd;
	private String sColor;
	
	
	
	public Tag(String sIdGoal, String sIdTag, long dDateValidityBegin,
			long dDateValidityEnd, String sColor) {
		this.sIdGoal = sIdGoal;		
		this.sIdTag = sIdTag;
		this.dDateValidityBegin = dDateValidityBegin;
		this.dDateValidityEnd = dDateValidityEnd;
		this.sColor = sColor;
	}

	
	public Tag (Cursor c){
        
        this.sIdGoal = c.getString(0);
        this.sIdTag = c.getString(1);
        this.dDateValidityBegin = Long.parseLong(c.getString(2));
        this.dDateValidityEnd = Long.parseLong(c.getString(3));
        this.sColor = c.getString(4);
		
	}

	
	/**
	 * @return the sIdTag
	 */
	public String getsIdTag() {
		return sIdTag;
	}
	/**
	 * @param sIdTag the sIdTag to set
	 */
	public void setsIdTag(String sIdTag) {
		this.sIdTag = sIdTag;
	}
	/**
	 * @return the sIdGoal
	 */
	public String getsIdGoal() {
		return sIdGoal;
	}
	/**
	 * @param sIdGoal the sIdGoal to set
	 */
	public void setsIdGoal(String sIdGoal) {
		this.sIdGoal = sIdGoal;
	}
	/**
	 * @return the dDateValidityBegin
	 */
	public long getdDateValidityBegin() {
		return dDateValidityBegin;
	}
	/**
	 * @param dDateValidityBegin the dDateValidityBegin to set
	 */
	public void setdDateValidityBegin(long dDateValidityBegin) {
		this.dDateValidityBegin = dDateValidityBegin;
	}
	/**
	 * @return the dDateValidityEnd
	 */
	public long getdDateValidityEnd() {
		return dDateValidityEnd;
	}
	/**
	 * @param dDateValidityEnd the dDateValidityEnd to set
	 */
	public void setdDateValidityEnd(long dDateValidityEnd) {
		this.dDateValidityEnd = dDateValidityEnd;
	}
	/**
	 * @return the sColor
	 */
	public String getColor() {
		return sColor;
	}
	/**
	 * @param sColor the sColor to set
	 */
	public void setColor(String sColor) {
		this.sColor = sColor;
	}
	
	
	
	/**
	 * Returns formated date validity begin 
	 * 
	 * @return
	 */
	public String getFormattedValidityBegin( ){

		DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
		return formatter.format(dDateValidityBegin);
	}
	
	
	/**
	 * Returns formated date validity end 
	 * 
	 * @return
	 */
	public String getFormattedValidityEnd( ){

		DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
		return formatter.format(dDateValidityEnd);
	}
		
	
	
	
	public static String getCreateTable(){
		
        String sSQL = "";
        
        sSQL= "CREATE TABLE "+TABLE_NAME+" ("+KEY_ID_GOAL+" TEXT, "+KEY_ID_TAG+" TEXT, "+KEY_VALIDITY_BEGIN+" INTEGER, "+KEY_VALIDITY_END+" INTEGER, "+KEY_COLOR+" TEXT, PRIMARY KEY ("+KEY_ID_TAG+", "+KEY_ID_GOAL+"));";

		return sSQL;
		
	}


	public void loadContentValues(ContentValues cv) {
			
		 cv.put(KEY_ID_GOAL, getsIdGoal());
		 cv.put(KEY_ID_TAG, getsIdTag());
		 cv.put(KEY_VALIDITY_BEGIN, getdDateValidityBegin());
		 cv.put(KEY_VALIDITY_END, getdDateValidityEnd());
		 cv.put(KEY_COLOR, getColor());
		 			
	}
	
	public String toString() {

		String sOut = "Tag : ";
		sOut += KEY_ID_GOAL + " " + getsIdGoal() + "|";
		sOut += KEY_ID_TAG + " " + getsIdTag() + "|";
		sOut += KEY_VALIDITY_BEGIN + " " + getdDateValidityBegin() + "|";
		sOut += KEY_VALIDITY_END + " " + getdDateValidityEnd() + "|";
		sOut += KEY_COLOR + " " + getColor() + "|";

		return sOut;
	}

}
