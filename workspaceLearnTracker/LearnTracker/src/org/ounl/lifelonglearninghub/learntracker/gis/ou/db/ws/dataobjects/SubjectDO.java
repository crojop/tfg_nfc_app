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
package org.ounl.lifelonglearninghub.learntracker.gis.ou.db.ws.dataobjects;

import org.ounl.lifelonglearninghub.learntracker.gis.ou.db.tables.SubjectDb;

import android.util.Log;

import com.google.gson.annotations.SerializedName;

public class SubjectDO {
	
	private String CLASSNAME = this.getClass().getName();

	
	@SerializedName("id")
	private String id;
	// tolong

	@SerializedName("subject_desc")
	private String subject_desc;

	@SerializedName("subject_task_desc")
	private String subject_task_desc;

	@SerializedName("subject_task_alternative_desc")
	private String subject_task_alternative_desc;	

	@SerializedName("subject_task_date_start")
	private String subject_task_date_start;
	// tolong

	@SerializedName("subject_task_time_duration")
	private String subject_task_time_duration;
	// tolong

	@SerializedName("subject_task_level")
	private String subject_task_level;
	// toint

	@SerializedName("subject_task_order")
	private String subject_task_order;

	// toint

	public String getId() {
		return id;
	}

	public String getSubject_desc() {
		return subject_desc;
	}

	public String getSubject_task_desc() {
		return subject_task_desc;
	}

	public String getSubject_task_date_start() {
		return subject_task_date_start;
	}

	public String getSubject_task_time_duration() {
		return subject_task_time_duration;
	}

	public String getSubject_task_level() {
		return subject_task_level;
	}

	public String getSubject_task_order() {
		return subject_task_order;
	}

	public String toString() {

		String sAux = "";

		sAux += "| id: " + id;
		sAux += "| subject_desc: " + subject_desc;
		sAux += "| subject_task_desc: " + subject_task_desc;
		sAux += "| subject_task_alternative_desc: " + subject_task_alternative_desc;
		sAux += "| subject_task_date_start: " + subject_task_date_start;		
		sAux += "| subject_task_time_duration: " + subject_task_time_duration;
		sAux += "| subject_task_level: " + subject_task_level;
		sAux += "| subject_task_order: " + subject_task_order;

		return sAux;
	}
	
	public SubjectDb toSqliteObject(){
				
		String sSubjectId, sSubjectDesc, sSubjectTaskDesc, sSubjectTaskAlternDesc;
		long lSubjectTaskDateStart, lSubjectTaskTimeDuration;
		int iSubjectTaskLevel, iSubjectTaskOrder;
				
		
		try{
			if (id == null) id = "-";
			sSubjectId = id;
		}catch(Exception e){
			Log.e(CLASSNAME, "Error parsing backend to SQLite object. sSubjectId. "+e.getMessage());
			sSubjectId = "0";
		}
		try{
			if (subject_desc == null) subject_desc = "-";
			sSubjectDesc = subject_desc;
		}catch(Exception e){
			Log.e(CLASSNAME, "Error parsing backend to SQLite object. sSubjectDesc. "+e.getMessage());
			sSubjectDesc = "0";
		}
		try{
			if (subject_task_desc == null) subject_task_desc = "-";
			sSubjectTaskDesc = subject_task_desc;
		}catch(Exception e){
			Log.e(CLASSNAME, "Error parsing backend to SQLite object. sSubjectTaskDesc. "+e.getMessage());
			sSubjectTaskDesc = "-";
		}
		
		try{
			if (subject_task_alternative_desc == null) subject_task_alternative_desc = "-";
			sSubjectTaskAlternDesc = subject_task_alternative_desc;
		}catch(Exception e){
			Log.e(CLASSNAME, "Error parsing backend to SQLite object. sSubjectTaskAlternDesc. "+e.getMessage());
			sSubjectTaskAlternDesc = "-";
		}		
		
		try{
			Long oL = new Long(subject_task_date_start);
			lSubjectTaskDateStart = oL.longValue();
		}catch(Exception e){
			Log.e(CLASSNAME, "Error parsing backend to SQLite object. lSubjectTaskDateStart. "+e.getMessage());
			lSubjectTaskDateStart = 0;
		}
		try{
			Long oL = new Long(subject_task_time_duration);
			lSubjectTaskTimeDuration = oL.longValue();
		}catch(Exception e){
			Log.e(CLASSNAME, "Error parsing backend to SQLite object. lSubjectTaskTimeDuration. "+e.getMessage());
			lSubjectTaskTimeDuration = 0;
		}		
		try{
			Integer oI = new Integer(subject_task_level);
			iSubjectTaskLevel= oI.intValue();
		}catch(Exception e){
			Log.e(CLASSNAME, "Error parsing backend to SQLite object. iSubjectTaskLevel. "+e.getMessage());
			iSubjectTaskLevel= 0;
		}		
		try{
			Integer oI = new Integer(subject_task_order);
			iSubjectTaskOrder= oI.intValue();
		}catch(Exception e){
			Log.e(CLASSNAME, "Error parsing backend to SQLite object. iSubjectTaskOrder. "+e.getMessage());
			iSubjectTaskOrder= 0;
		}		
		
		
		SubjectDb s =  new SubjectDb(sSubjectId,sSubjectDesc,sSubjectTaskDesc,sSubjectTaskAlternDesc,lSubjectTaskDateStart,lSubjectTaskTimeDuration,iSubjectTaskLevel,iSubjectTaskOrder);
		
		
		
		return s;
	}

}