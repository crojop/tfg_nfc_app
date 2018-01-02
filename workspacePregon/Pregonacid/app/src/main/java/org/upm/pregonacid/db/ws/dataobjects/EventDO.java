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
package org.upm.pregonacid.db.ws.dataobjects;

import android.util.Log;

import com.google.gson.annotations.SerializedName;

import org.upm.pregonacid.db.tables.EventDb;

public class EventDO {
	
	private String CLASSNAME = this.getClass().getName();

	@SerializedName("id")
	private Long id;

	@SerializedName("timestamp")
	private Long timestamp;

	@SerializedName("title")
	private String title;

	@SerializedName("subtitle")
	private String subtitle;

	@SerializedName("subsubtitle")
	private String subsubtitle;

	@SerializedName("author")
	private String author;

	@SerializedName("state")
	private int state;

	public String toString() {

		String sAux = "";
		sAux += "| id: " + id;
		sAux += "| timestamp: " + timestamp;
		sAux += "| title: " + title;
		sAux += "| subtitle: " + subtitle;
		sAux += "| subsubtitle: " + subsubtitle;
		sAux += "| author: " + author;
		sAux += "| state: " + state;

		return sAux;
	}
	
	public EventDb toSqliteObject(){

		long lId;
		String sTitle, sSubTitle, sSubSubTitle, sAuthor;
		long lTimeStamp;
		int iState;

		try{
			Long oL = new Long(id);
			lId = oL.longValue();
		}catch(Exception e){
			Log.e(CLASSNAME, "Error parsing backend to SQLite object. Id. "+e.getMessage());
			lId = 0l;
		}

		try{
			Long oL = new Long(timestamp);
			lTimeStamp = oL.longValue();
		}catch(Exception e){
			Log.e(CLASSNAME, "Error parsing backend to SQLite object. Timestamp. "+e.getMessage());
			lTimeStamp = 0l;
		}


		try{
			if (title == null) title = "-";
			sTitle = title;
		}catch(Exception e){
			Log.e(CLASSNAME, "Error parsing backend to SQLite object. sTitle. "+e.getMessage());
			sTitle = "-";
		}


		try{
			if (subtitle == null) subtitle = "-";
			sSubTitle = subtitle;
		}catch(Exception e){
			Log.e(CLASSNAME, "Error parsing backend to SQLite object. sSubTitle. "+e.getMessage());
			sSubTitle = "-";
		}

		try{
			if (subsubtitle == null) subsubtitle = "-";
			sSubSubTitle = subsubtitle;
		}catch(Exception e){
			Log.e(CLASSNAME, "Error parsing backend to SQLite object. sSubSubTitle. "+e.getMessage());
			sSubSubTitle = "-";
		}

		try{
			if (author == null) author = "-";
			sAuthor = author;
		}catch(Exception e){
			Log.e(CLASSNAME, "Error parsing backend to SQLite object. sAuthor. "+e.getMessage());
			sAuthor = "-";
		}

		try{
			Integer oI = new Integer(state);
			iState= oI.intValue();
		}catch(Exception e){
			Log.e(CLASSNAME, "Error parsing backend to SQLite object. iState. "+e.getMessage());
			iState= 0;
		}



		EventDb s =  new EventDb(lId, lTimeStamp, sTitle, sSubTitle, sSubSubTitle, sAuthor, iState);
		
		
		
		return s;
	}


	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(Long timestamp) {
		this.timestamp = timestamp;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getSubtitle() {
		return subtitle;
	}

	public void setSubtitle(String subtitle) {
		this.subtitle = subtitle;
	}

	public String getSubsubtitle() {
		return subsubtitle;
	}

	public void setSubsubtitle(String subsubtitle) {
		this.subsubtitle = subsubtitle;
	}

	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	public int getState() {
		return state;
	}

	public void setState(int state) {
		this.state = state;
	}


}