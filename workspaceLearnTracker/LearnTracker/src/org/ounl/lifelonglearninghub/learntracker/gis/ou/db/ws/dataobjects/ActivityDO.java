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

import org.ounl.lifelonglearninghub.learntracker.gis.ou.db.tables.ActividadDb;

import android.util.Log;

import com.google.gson.annotations.SerializedName;

public class ActivityDO {
	
	private String CLASSNAME = this.getClass().getName();

//	public static final String KEY_ID_SUBJECT = "id_subject";
//	public static final String KEY_DATE_CHECKIN = "date_check_in";
//	public static final String KEY_DATE_CHECKOUT = "date_check_out";
	
	public static final int ACTIVITY_RECORD_MODE_SYNCHRONOUS = 0;
	public static final int ACTIVITY_RECORD_MODE_ASYNCHRONOUS = 1;
	

	public ActivityDO(
			String id_user, 
			String id_subject,
			long activity_date_checkin, 
			long activity_date_checkout,
			double activity_location_latitude,
			double activity_location_longitude,
			int activity_record_mode) {
		super();

		
		this.id_user = id_user;
		this.id_subject = id_subject;
		this.activity_date_checkin = activity_date_checkin;
		this.activity_date_checkout = activity_date_checkout;
		this.activity_location_latitude = activity_location_latitude;
		this.activity_location_longitude = activity_location_longitude;
		this.activity_record_mode = activity_record_mode;
	}

	
	@SerializedName("id")
	private Long id;

	@SerializedName("id_user")
	private String id_user;

	@SerializedName("id_subject")
	private String id_subject;

	@SerializedName("activity_date_checkin")
	private long activity_date_checkin;

	@SerializedName("activity_date_checkout")
	private long activity_date_checkout;

	@SerializedName("activity_location_latitude")
	private double activity_location_latitude;

	@SerializedName("activity_location_longitude")
	private double activity_location_longitude;
	
	@SerializedName("activity_record_mode")
	private int activity_record_mode;



	public String toString() {

		String sAux = "";

		sAux += "| id_user: " + id_user;
		sAux += "| id_subject: " + id_subject;
		sAux += "| activity_date_checkin: " + activity_date_checkin;
		sAux += "| activity_date_checkout: " + activity_date_checkout;
		sAux += "| activity_location_latitude: " + activity_location_latitude;
		sAux += "| activity_location_longitude: " + activity_location_longitude;
		sAux += "| activity_record_mode: " + activity_record_mode;

		return sAux;
	}

	// Getters
	public Long getId() {
		return id;
	}

	public String getId_user() {
		return id_user;
	}

	public String getId_subject() {
		return id_subject;
	}

	public long getActivity_date_checkin() {
		return activity_date_checkin;
	}

	public long getActivity_date_checkout() {
		return activity_date_checkout;
	}

	public double getActivity_location_latitude() {
		return activity_location_latitude;
	}

	public double getActivity_location_longitude() {
		return activity_location_longitude;
	}

	public int getActivity_record_mode() {
		return activity_record_mode;
	}


	public ActividadDb toSqliteObject(){		
		
//		long lId;
//		try{
//			Long oL = new Long(id);
//			lId = oL.longValue();
//		}catch(Exception e){
//			Log.e(CLASSNAME, "Error parsing backend to SQLite object. lId. "+e.getMessage());
//			lId = 0;
//		}
		
//		String sId;
//		try{
//			if (id == null) sId = "-";
//			Long oL = new Long(id);
//			sId = oL.toString();
//		}catch(Exception e){
//			Log.e(CLASSNAME, "Error parsing backend to SQLite object. sId. "+e.getMessage());
//			sId = "0";
//		}		
		
		String sId_user;
		try{
			if (id_user == null) sId_user = "-";
			sId_user = id_user;
		}catch(Exception e){
			Log.e(CLASSNAME, "Error parsing backend to SQLite object. sId_user. "+e.getMessage());
			sId_user = "0";
		}
		
		
		String sId_subject;
		try{
			if (id_subject == null) sId_subject = "-";
			sId_subject = id_subject;
		}catch(Exception e){
			Log.e(CLASSNAME, "Error parsing backend to SQLite object. sId_subject. "+e.getMessage());
			sId_subject = "0";
		}
		
		
		long lActivity_date_checkin;
		try{
			Long oL = new Long(activity_date_checkin);
			lActivity_date_checkin = oL.longValue();
		}catch(Exception e){
			Log.e(CLASSNAME, "Error parsing backend to SQLite object. lActivity_date_checkin. "+e.getMessage());
			lActivity_date_checkin = 0;
		}
		
		
		long lActivity_date_checkout;
		try{
			Long oL = new Long(activity_date_checkout);
			lActivity_date_checkout = oL.longValue();
		}catch(Exception e){
			Log.e(CLASSNAME, "Error parsing backend to SQLite object. lActivity_date_checkout. "+e.getMessage());
			lActivity_date_checkout = 0;
		}		
		
		
		double dActivity_location_latitude;
		try{
			Double oD = new Double(activity_location_latitude);
			dActivity_location_latitude = oD.doubleValue();
		}catch(Exception e){
			Log.e(CLASSNAME, "Error parsing backend to SQLite object. dActivity_location_latitude. "+e.getMessage());
			dActivity_location_latitude = 0;
		}
		
		
		double dActivity_location_longitude;
		try{
			Double oD = new Double(activity_location_longitude);
			dActivity_location_longitude = oD.doubleValue();
		}catch(Exception e){
			Log.e(CLASSNAME, "Error parsing backend to SQLite object. dActivity_location_longitude. "+e.getMessage());
			dActivity_location_longitude = 0;
		}		
		
		
//		int iActivity_record_mode;
//		try{
//			Integer oI = new Integer(activity_record_mode);
//			iActivity_record_mode= oI.intValue();
//		}catch(Exception e){
//			Log.e(CLASSNAME, "Error parsing backend to SQLite object. iActivity_record_mode. "+e.getMessage());
//			iActivity_record_mode= 0;
//		}

		
		ActividadDb a =  new ActividadDb(sId_subject, lActivity_date_checkin, lActivity_date_checkout, dActivity_location_longitude, dActivity_location_latitude);				
		
		return a;
	}


}