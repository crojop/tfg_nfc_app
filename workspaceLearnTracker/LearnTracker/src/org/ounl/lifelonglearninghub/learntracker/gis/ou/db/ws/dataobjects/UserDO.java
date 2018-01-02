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

import com.google.gson.annotations.SerializedName;

public class UserDO {
	
	private String CLASSNAME = this.getClass().getName();

	
	@SerializedName("id")
	private String id;

	@SerializedName("subject_desc")
	private String subject_desc;

	@SerializedName("user_name")
	private String user_name;

	@SerializedName("user_type")
	private int user_type;


	public String toString() {

		String sAux = "User ";

		sAux += "| id: " + id;
		sAux += "| subject_desc: " + subject_desc;
		sAux += "| user_name: " + user_name;
		sAux += "| user_type: " + user_type;

		return sAux;
	}
	
	
	public String getUserName(){
		return user_name;
	}
	
	public int getUserType(){
		return user_type;
	}	
	
//	public SubjectDb toSqliteObject(){
//		
//
//		
//		String sSubjectId, sSubjectDesc, sUserNAme;
//		int iUserType;
//		
//		try{
//			if (id == null) id = "-";
//			sSubjectId = id;
//		}catch(Exception e){
//			Log.e(CLASSNAME, "Error parsing backend to SQLite object. sSubjectId. "+e.getMessage());
//			sSubjectId = "0";
//		}
//		try{
//			if (subject_desc == null) subject_desc = "-";
//			sSubjectDesc = subject_desc;
//		}catch(Exception e){
//			Log.e(CLASSNAME, "Error parsing backend to SQLite object. sSubjectDesc. "+e.getMessage());
//			sSubjectDesc = "0";
//		}
//		try{
//			if (user_name == null) user_name = "-";
//			sUserNAme = user_name;
//		}catch(Exception e){
//			Log.e(CLASSNAME, "Error parsing backend to SQLite object. sSubjectTaskDesc. "+e.getMessage());
//			sUserNAme = "0";
//		}		
//		
//		
//		try{
//			Integer oI = new Integer(user_type);
//			iUserType= oI.intValue();
//		}catch(Exception e){
//			Log.e(CLASSNAME, "Error parsing backend to SQLite object. iUserType. "+e.getMessage());
//			iUserType= 0;
//		}		
//
//		
//		
//		SubjectDb s =  new SubjectDb(sSubjectId,sSubjectDesc,sUserNAme,lSubjectTaskDateStart,lSubjectTaskTimeDuration,iUserType,iSubjectTaskOrder);
//		
//		
//		
//		return s;
//	}

}