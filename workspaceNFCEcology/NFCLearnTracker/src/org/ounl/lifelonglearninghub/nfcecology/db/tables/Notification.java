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
package org.ounl.lifelonglearninghub.nfcecology.db.tables;

import android.content.ContentValues;
import android.database.Cursor;

public class Notification implements ITables {

	public static final String TABLE_NAME = "notification";
	public static final String KEY_ID_NOTIF = "id_notification";
	public static final String KEY_ID_GOAL = "id_goal";
	public static final String KEY_NOTIF_TEXT = "notif_text";
	public static final String KEY_NOTIF_DELAY_SECS = "notif_delay_secs";
	public static final String KEY_NOTIF_TYPE = "notif_type";

	public static final int NOTIFICATION_TYPE_ONCHECKIN = 1;
	public static final int NOTIFICATION_TYPE_ONCHECKOUT = 2;
	public static final int NOTIFICATION_TYPE_SCHEDULED = 3;
	public static final int NOTIFICATION_TYPE_RANDOMIZED = 4;

	public static final String ALL_GOALS_NOTIFICATION = "ALL";

	private long lNotificationId;
	private String sNotificationGoalId;
	private String sNotificationText;
	private long lNotificationDelaySecs;
	private int iNotificationType;

	public Notification(Cursor c) {

		if (c.getString(0) != null) {
			this.lNotificationId = Long.parseLong(c.getString(0));
		}
		this.sNotificationGoalId = c.getString(1);
		this.sNotificationText = c.getString(2);
		if (c.getString(3) != null) {
			this.lNotificationDelaySecs = Long.parseLong(c.getString(3));
		}
		if (c.getString(4) != null) {
			this.iNotificationType = Integer.parseInt(c.getString(4));
		}

	}
	
	
	public static String getCreateTable(){
		
        String sSQL = "";

        sSQL= "CREATE TABLE "+TABLE_NAME+" ("+KEY_ID_NOTIF+" INTEGER, "+KEY_ID_GOAL+" TEXT, "+KEY_NOTIF_TEXT+" TEXT, "+KEY_NOTIF_DELAY_SECS+" INTEGER, "+KEY_NOTIF_TYPE+" INTEGER, PRIMARY KEY ("+KEY_ID_NOTIF+"));";
		
		return sSQL;
		
	}	
	
	public void loadContentValues(ContentValues cv) {
		
		 
		 cv.put(KEY_ID_NOTIF, lNotificationId);
		 cv.put(KEY_ID_GOAL, sNotificationGoalId);
		 cv.put(KEY_NOTIF_TEXT, sNotificationText);
		 cv.put(KEY_NOTIF_DELAY_SECS, lNotificationDelaySecs);
		 cv.put(KEY_NOTIF_TYPE, iNotificationType);

		 			
	}

	public String toString() {

		return "[" + lNotificationId + "][" + sNotificationGoalId + "]["
				+ sNotificationText + "]";
	}

	/**
	 * @return the lNotificationId
	 */
	public long getlNotificationId() {
		return lNotificationId;
	}

	/**
	 * @param lNotificationId
	 *            the lNotificationId to set
	 */
	public void setlNotificationId(long lNotificationId) {
		this.lNotificationId = lNotificationId;
	}

	/**
	 * @return the sNotificationGoalId
	 */
	public String getsNotificationGoalId() {
		return sNotificationGoalId;
	}

	/**
	 * @param sNotificationGoalId
	 *            the sNotificationGoalId to set
	 */
	public void setsNotificationGoalId(String sNotificationGoalId) {
		this.sNotificationGoalId = sNotificationGoalId;
	}

	/**
	 * @return the sNotificationText
	 */
	public String getsNotificationText() {
		return sNotificationText;
	}

	/**
	 * @param sNotificationText
	 *            the sNotificationText to set
	 */
	public void setsNotificationText(String sNotificationText) {
		this.sNotificationText = sNotificationText;
	}

	/**
	 * @return the lNotificationDelaySecs
	 */
	public long getlNotificationDelaySecs() {
		return lNotificationDelaySecs;
	}

	/**
	 * @param lNotificationDelaySecs
	 *            the lNotificationDelaySecs to set
	 */
	public void setlNotificationDelaySecs(long lNotificationDelaySecs) {
		this.lNotificationDelaySecs = lNotificationDelaySecs;
	}

	/**
	 * @return the iNotificationType
	 */
	public int getiNotificationType() {
		return iNotificationType;
	}

	/**
	 * @param iNotificationType
	 *            the iNotificationType to set
	 */
	public void setiNotificationType(int iNotificationType) {
		this.iNotificationType = iNotificationType;
	}

}
