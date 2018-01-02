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
package org.ounl.lifelonglearninghub.nfcecology.db;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.TreeMap;

import org.ounl.lifelonglearninghub.nfcecology.db.tables.Goal;
import org.ounl.lifelonglearninghub.nfcecology.db.tables.Notification;
import org.ounl.lifelonglearninghub.nfcecology.db.tables.Tag;
import org.ounl.lifelonglearninghub.nfcecology.db.tables.Tagctivity;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DatabaseHandler extends SQLiteOpenHelper {

	private String CLASSNAME = this.getClass().getSimpleName();
	
	private static final int DATABASE_VERSION = 1;
	private static final String DATABASE_NAME = "bernauersdatabase.db";
	private SQLiteDatabase sqliteDB = null;
	
	
	

	public DatabaseHandler(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
		sqliteDB = this.getWritableDatabase();
		sqliteDB.close();

	}

	@Override
	public void onCreate(SQLiteDatabase db) {

		sqliteDB = db;

		Log.d(CLASSNAME, "Creating table Goals...");
		db.execSQL(Goal.getCreateTable());
		Log.d(CLASSNAME, "Creating table Tags...");
		db.execSQL(Tag.getCreateTable());
		Log.d(CLASSNAME, "Creating table Activities...");
		db.execSQL(Tagctivity.getCreateTable());
		Log.d(CLASSNAME, "Creating table Notifications...");
		db.execSQL(Notification.getCreateTable());
		Log.d(CLASSNAME, "Tables created!!");

	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

		db.execSQL("DROP TABLE IF EXISTS " + Goal.TABLE_NAME);
		db.execSQL("DROP TABLE IF EXISTS " + Tag.TABLE_NAME);
		db.execSQL("DROP TABLE IF EXISTS " + Tagctivity.TABLE_NAME);
		db.execSQL("DROP TABLE IF EXISTS " + Notification.TABLE_NAME);

		onCreate(db);
	}

	/**
	 * Insert tag into db
	 * 
	 * @param o
	 */
	public void addTag(Tag o) {

		sqliteDB = this.getWritableDatabase();
		ContentValues values = new ContentValues();
		o.loadContentValues(values);
		try {
			sqliteDB.insert(Tag.TABLE_NAME, null, values);
		} catch (Exception e) {
			Log.e(CLASSNAME, "Error inserting Tag " + e.toString() + "");
			e.printStackTrace();
		}
		sqliteDB.close();

	}

	/**
	 * Insert list of tags into db
	 * 
	 * @param tags
	 */
	public void addTags(List<Tag> tags) {

		for (int i = 0; i < tags.size(); i++) {
			addTag(tags.get(i));
		}
	}

	/**
	 * Insert goal into db
	 * 
	 * @param o
	 */
	public void addGoal(Goal o) {

		sqliteDB = this.getWritableDatabase();
		ContentValues values = new ContentValues();
		o.loadContentValues(values);
		try {
			sqliteDB.insert(Goal.TABLE_NAME, null, values);
		} catch (Exception e) {
			Log.e(CLASSNAME, "Error inserting goal " + e.toString()
					+ "");
			e.printStackTrace();
		}
		sqliteDB.close();
	}

	/**
	 * Insert list of goals into db
	 * 
	 * @param goals
	 */
	public void addGoals(List<Goal> goals) {

		for (int i = 0; i < goals.size(); i++) {
			addGoal(goals.get(i));
		}

	}

	/**
	 * Insert tag into db
	 * 
	 * @param o Activity being tracked
	 * @param now is timestamp when tag is tagged
	 * 
	 * @returns true whenever it is a checkin 
	 * @returns false whenever it is a checkout
	 */
	public boolean addTagctivity(Tagctivity o, long now) {

		try {

			// Update to make checkout
			sqliteDB = this.getWritableDatabase();
			ContentValues valuesUpdate = new ContentValues();
			o.loadCheckValues(false, valuesUpdate, now);
			int iNumRows = sqliteDB.update(
					Tagctivity.TABLE_NAME,
					valuesUpdate,
					Tagctivity.KEY_DATE_CHECKOUT + "="
							+ Tagctivity.PENDING_TO_CHECK_OUT + " and "
							+ Tagctivity.KEY_ID_TAG + "='"
							+ o.getsIdTagctivity() + "'", null);
			sqliteDB.close();

			if (iNumRows < 1) {
				// Insert to make check in
				sqliteDB = this.getWritableDatabase();
				ContentValues valuesInsert = new ContentValues();
				o.loadCheckValues(true, valuesInsert, now);
				sqliteDB.insert(Tagctivity.TABLE_NAME, null, valuesInsert);
				sqliteDB.close();

				Log.d(CLASSNAME, "CHECKIN "+o.getsIdTagctivity());
				return true;
			} else {
				Log.d(CLASSNAME, "CHECKOUT "+o.getsIdTagctivity());
				return false;
			}

		} catch (Exception e) {
			Log.e(CLASSNAME,
					"Error inserting Tagctivity " + e.toString() + "");
			e.printStackTrace();
			sqliteDB.close();
			return false;
		}

	}

	/**
	 * Insert notification into db
	 * 
	 * @param o
	 *            @ returns true went right @ returns false went wrong
	 */
	public boolean addTagctivity(Notification o) {

		try {

			// Insert to make check in
			sqliteDB = this.getWritableDatabase();
			ContentValues valuesInsert = new ContentValues();
			o.loadContentValues(valuesInsert);
			sqliteDB.insert(Tagctivity.TABLE_NAME, null, valuesInsert);
			sqliteDB.close();

		} catch (Exception e) {
			Log.e(CLASSNAME,
					"Error inserting Tagctivity " + e.toString() + "");
			e.printStackTrace();
			sqliteDB.close();
			return false;

		}
		return true;

	}

	/**
	 * Get goals from database
	 * 
	 * @return
	 */
	public List<Goal> getGoals() {
		List<Goal> list = new ArrayList<Goal>();
		String selectQuery = "SELECT  * FROM " + Goal.TABLE_NAME;
		sqliteDB = this.getWritableDatabase();
		Cursor cursor = sqliteDB.rawQuery(selectQuery, null);
		if (cursor.moveToFirst()) {
			do {
				Goal o = new Goal(cursor);
				list.add(o);
			} while (cursor.moveToNext());
		}
		cursor.close();
		sqliteDB.close();
		return list;
	}

	
	/**
	 * Get goal for a given goal id
	 * @param sIdGoal
	 * @return
	 */
	public Goal getGoal(String sIdGoal) {

		String selectQuery = "SELECT g.* FROM " + Goal.TABLE_NAME
				+ " g WHERE  g."+Goal.KEY_NAME + "='" + sIdGoal + "'";
		sqliteDB = this.getWritableDatabase();
		Cursor cursor = sqliteDB.rawQuery(selectQuery, null);
		cursor.moveToFirst();
		Goal o = new Goal(cursor);
		cursor.close();
		sqliteDB.close();
		return o;
	}
	
	/**
	 * 
	 * @param o
	 * @param now
	 * @return
	 */
	public boolean updateGoal(Goal o, String smins) {

		try {

			sqliteDB = this.getWritableDatabase();
			ContentValues valuesUpdate = new ContentValues();
			
			// Updates daily time mins
			o.loadUpdatedValues(valuesUpdate, smins);		
			
			int iNumRows = sqliteDB.update(
					Goal.TABLE_NAME,
					valuesUpdate,
					Goal.KEY_NAME + "='" + o.getsName() + "'", 
					null);
			sqliteDB.close();

			if (iNumRows < 1) {
				Log.e(CLASSNAME, "Goal not found "+o.getsName());
				return false;
			}

		} catch (Exception e) {
			Log.e(CLASSNAME,
					"Error inserting Tagctivity " + e.toString() + "");
			e.printStackTrace();
			sqliteDB.close();
			return false;
		}
		return true;

	}
	
	
	
	/**
	 * Returns goal for a given tag and date of check
	 * 
	 * @param sIdTag
	 * @return
	 */
	public Goal getGoal(String sIdTag, long lDateCheck) {

		String selectQuery = "SELECT g.* FROM " + Goal.TABLE_NAME
				+ " g INNER JOIN " + Tag.TABLE_NAME + " t ON g."
				+ Goal.KEY_NAME + " = t." + Tag.KEY_ID_GOAL + " WHERE  t."
				+ Tag.KEY_ID_TAG + "='" + sIdTag + "' AND t."
				+ Tag.KEY_VALIDITY_BEGIN + " < " + lDateCheck + " AND t."
				+ Tag.KEY_VALIDITY_END + " > " + lDateCheck;
		sqliteDB = this.getWritableDatabase();
		Cursor cursor = sqliteDB.rawQuery(selectQuery, null);
		cursor.moveToFirst();
		Goal o = new Goal(cursor);
		cursor.close();
		sqliteDB.close();
		return o;
	}

	/**
	 * Get goals from database
	 * 
	 * @return
	 */
	public List<Tag> getTags() {
		List<Tag> list = new ArrayList<Tag>();
		String selectQuery = "SELECT  * FROM " + Tag.TABLE_NAME;
		sqliteDB = this.getWritableDatabase();
		Cursor cursor = sqliteDB.rawQuery(selectQuery, null);
		if (cursor.moveToFirst()) {
			do {
				Tag o = new Tag(cursor);
				list.add(o);
			} while (cursor.moveToNext());
		}
		cursor.close();
		sqliteDB.close();
		return list;
	}

	/**
	 * Search idTag into table Tags table with idGoal and validity time now
	 * 
	 * @param goalId
	 * @return
	 */
	public String getTagId(String goalId) {

		Date d = new Date();
		String sTag = getTagId(goalId, d.getTime());
		return sTag;

	}

	/**
	 * Search idTag into table Tags table with idGoal and validity
	 * 
	 * @param goalId
	 *            lDate
	 * @return tagId
	 */
	public String getTagId(String goalId, long lDate) {
		String sTagId = "";
		String selectQuery = "SELECT  " + Tag.KEY_ID_TAG + " FROM "
				+ Tag.TABLE_NAME + " WHERE " + Tag.KEY_ID_GOAL + "='" + goalId
				+ "' AND " + lDate + " < " + Tag.KEY_VALIDITY_END + " AND "
				+ lDate + " > " + Tag.KEY_VALIDITY_BEGIN + "";
		sqliteDB = this.getWritableDatabase();
		Cursor cursor = sqliteDB.rawQuery(selectQuery, null);
		if (cursor.moveToFirst()) {
			sTagId = cursor.getString(0);
		}
		cursor.close();
		sqliteDB.close();
		return sTagId;
	}
	

	
	/**
	 * Returns Tag for given goal id for the current validity time
	 * 
	 * @param goalId
	 * @return
	 */
	public Tag getTag(String goalId) {

		Tag tag = null;
		Date d = new Date();
		
		
		String selectQuery = "SELECT  " + Tag.KEY_ID_TAG + ", "+ Tag.KEY_VALIDITY_BEGIN +", "+ Tag.KEY_VALIDITY_END +", "+ Tag.KEY_COLOR +" FROM "
				+ Tag.TABLE_NAME + " WHERE " + Tag.KEY_ID_GOAL + "='" + goalId
				+ "' AND " + d.getTime() + " < " + Tag.KEY_VALIDITY_END + " AND "
				+ d.getTime() + " > " + Tag.KEY_VALIDITY_BEGIN + "";
		
		sqliteDB = this.getWritableDatabase();
		Cursor cursor = sqliteDB.rawQuery(selectQuery, null);
		if (cursor.moveToFirst()) {
			tag = new Tag(goalId, cursor.getString(0), cursor.getLong(1), cursor.getLong(2), cursor.getString(3));
		}
				
		cursor.close();
		sqliteDB.close();
		return tag;
	}	

	/**
	 * Search idGoal into table Tags table with idTag
	 * that is within the validity
	 * 
	 * @param idTag
	 * @return idGoal
	 */
	public String getGoalId(String tagId, long lDate) {
		String sGoalId = "";
		String selectQuery = "SELECT  " + Tag.KEY_ID_GOAL + " FROM "
				+ Tag.TABLE_NAME + " WHERE " + Tag.KEY_ID_TAG + "='" + tagId
				+ "' AND " + lDate + " < " + Tag.KEY_VALIDITY_END + " AND "
				+ lDate + " > " + Tag.KEY_VALIDITY_BEGIN + "";
		sqliteDB = this.getWritableDatabase();
		Cursor cursor = sqliteDB.rawQuery(selectQuery, null);
		if (cursor.moveToFirst()) {
			sGoalId = cursor.getString(0);
		}
		cursor.close();
		sqliteDB.close();
		return sGoalId;
	}
	
	/**
	 * Returns color of the tag for a given color
	 * @param sGoalId
	 * @return
	 */
	public String getTagColor(String sGoalId) {
		String sColor = "";

		String selectQuery = "SELECT "+Tag.KEY_COLOR+" FROM "+Tag.TABLE_NAME+" t, "+Goal.TABLE_NAME+" g WHERE t."+Tag.KEY_ID_GOAL+"=g."+Goal.KEY_NAME+" and "+Tag.KEY_ID_GOAL+"='"+sGoalId+"'";
		
		sqliteDB = this.getWritableDatabase();
		Cursor cursor = sqliteDB.rawQuery(selectQuery, null);
		if (cursor.moveToFirst()) {
			sColor = cursor.getString(0);
		}
		cursor.close();
		sqliteDB.close();
		return sColor;
	}	

	
	

	/**
	 * Get activities from database
	 * 
	 * @return
	 */
	public List<Tagctivity> getActivities() {
		List<Tagctivity> list = new ArrayList<Tagctivity>();
		String selectQuery = "SELECT  * FROM " + Tagctivity.TABLE_NAME;
		sqliteDB = this.getWritableDatabase();
		Cursor cursor = sqliteDB.rawQuery(selectQuery, null);
		if (cursor.moveToFirst()) {
			do {
				Tagctivity o = new Tagctivity(cursor);
				list.add(o);
			} while (cursor.moveToNext());
		}
		cursor.close();
		sqliteDB.close();
		return list;
	}

	/**
	 * Return list of activities from table Tagctivity for a given tagId
	 * 
	 * @param tagId
	 * @return
	 */
	public List<Tagctivity> getActivities(String tagId) {
		List<Tagctivity> list = new ArrayList<Tagctivity>();
		String selectQuery = "SELECT  * FROM " + Tagctivity.TABLE_NAME
				+ " WHERE " + Tagctivity.KEY_ID_TAG + "='" + tagId + "'";
		sqliteDB = this.getWritableDatabase();
		Cursor cursor = sqliteDB.rawQuery(selectQuery, null);
		if (cursor.moveToFirst()) {
			do {
				Tagctivity o = new Tagctivity(cursor);
				list.add(o);
			} while (cursor.moveToNext());
		}
		cursor.close();
		sqliteDB.close();
		return list;
	}
	
	/**
	 * Returns sum of activity group by day between the two dates and the given tag
	 * 
	 * @param tagId
	 * @param dIni
	 * @param dEnd
	 * @return
	 */
	public TreeMap<Date, Long> getAccumActivitiesBetweenDates(String tagId, Date dIni, Date dEnd) {
		
		TreeMap<Date, Long> tmResult = new TreeMap<Date, Long>();
		long lDuration = 0l;


		String sDay = "";
		
		// select sum(date_check_out-date_check_in) duration, date(date_check_in/1000, 'unixepoch', 'localtime') days from activities where date_check_in < 1411099200000 and date_check_in > 0111704000000 and id_tag = 'http://www.ttag.be/m/04F77751962280' group by days order by days asc;
		
		String selectQuery = "SELECT sum("+Tagctivity.KEY_DATE_CHECKOUT+"-"+Tagctivity.KEY_DATE_CHECKIN+") duration, date("+Tagctivity.KEY_DATE_CHECKIN+"/1000, 'unixepoch', 'localtime') days FROM "+Tagctivity.TABLE_NAME+" WHERE "+Tagctivity.KEY_DATE_CHECKIN+" < "+dEnd.getTime()+" AND "+Tagctivity.KEY_DATE_CHECKIN+" > "+dIni.getTime()+" AND "+Tagctivity.KEY_ID_TAG+" = '"+tagId+"' GROUP BY days ORDER BY days ASC";
		Log.d(CLASSNAME, "Querying "+selectQuery);

		sqliteDB = this.getWritableDatabase();
		Cursor cursor = sqliteDB.rawQuery(selectQuery, null);
		if (cursor.moveToFirst()) {
			do {
				
				lDuration = cursor.getLong(0);
				sDay = cursor.getString(1);
				
				// sDay has format 2009-03-23
				
				
				//Date date = new SimpleDateFormat("yyyy-MM-dd").parse(sDay);
				SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");							
				Date d = new Date();
				try {
					d = sf.parse(sDay);
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				tmResult.put(d, lDuration);
				

			} while (cursor.moveToNext());
		}
		cursor.close();
		sqliteDB.close();
		return tmResult;
	}	
	
	

	/**
	 * Get goal for a given goal id
	 * @param sIdGoal
	 * @return
	 */
	public long getDurationLastCheckOut(String sIdTag) {

		// SELECT max(date_check_out), date_check_out-date_check_in durationmills FROM activities where id_tag= 'http://www.ttag.be/m/04F77751962280'
		
		long lDuration = 0l;
		
		String selectQuery = "SELECT max(date_check_out), date_check_out-date_check_in durationmills FROM " + Tagctivity.TABLE_NAME
				+ " WHERE  "+Tagctivity.KEY_ID_TAG + "='" + sIdTag + "'";
		sqliteDB = this.getWritableDatabase();
		Cursor cursor = sqliteDB.rawQuery(selectQuery, null);
		cursor.moveToFirst();
		

        lDuration = cursor.getLong(1);        
        //this.lDailyTimeMills = Long.parseLong(c.getString(2));
		
		
		cursor.close();
		sqliteDB.close();
		return lDuration;
	}
	
	/**
	 * Returns sum of activity in milliseconds for a given
	 * sIdTag in a specific day with format YYYY-MM-DD
	 * 
	 * @param sIdTag
	 * @param sYYYYMMDD 
	 * 
	 * @return
	 */
	public long getDurationActivityGivenDay(String sIdTag, String sYYYYMMDD) {

		/*
		 * 
			select 
				SUM(date_check_out-date_check_in) duration, 
				date(date_check_in/1000, 'unixepoch', 'localtime') days
			from 
				activities 
			where 
				id_tag = 'http://www.ttag.be/m/04F77751962280' AND
				days = '2014-06-20'
		 */
		
		long lDuration = 0l;
				
		String selectQuery = "";
		selectQuery += "select"; 
		selectQuery += " SUM("+Tagctivity.KEY_DATE_CHECKOUT+"-"+Tagctivity.KEY_DATE_CHECKIN+") duration,"; 
		selectQuery += " date("+Tagctivity.KEY_DATE_CHECKIN+"/1000, 'unixepoch', 'localtime') days";
		selectQuery += " from " + Tagctivity.TABLE_NAME;
		selectQuery += " where ";
		selectQuery += " "+Tagctivity.KEY_DATE_CHECKOUT+" > 0 AND";		
		selectQuery += " "+Tag.KEY_ID_TAG+" = '"+sIdTag+"' AND";
		selectQuery += " days = '"+sYYYYMMDD+"'";
		
		sqliteDB = this.getWritableDatabase();
		Cursor cursor = sqliteDB.rawQuery(selectQuery, null);
		
		if (cursor.getCount() > 0) {
			cursor.moveToFirst();
	        lDuration = cursor.getLong(0);        			
		}
		
		if (lDuration < 0){
			lDuration = 0;
		}
		
		cursor.close();
		sqliteDB.close();
		return lDuration;
	}	
	
	
	
	
	/**
	 * Get notifications from database
	 * 
	 * @return
	 */
	public List<Notification> getNotifications() {
		List<Notification> list = new ArrayList<Notification>();
		String selectQuery = "SELECT  * FROM " + Notification.TABLE_NAME + " ORDER BY "+Notification.KEY_ID_GOAL;
		sqliteDB = this.getWritableDatabase();
		Cursor cursor = sqliteDB.rawQuery(selectQuery, null);
		if (cursor.moveToFirst()) {
			do {
				Notification o = new Notification(cursor);
				list.add(o);
			} while (cursor.moveToNext());
		}
		cursor.close();
		sqliteDB.close();
		return list;
	}	

	/**
	 * Providing a goalId retuns all the activities performed within that goal
	 * even when they were with different tags. Joins table Tag and Activities
	 * and checks tags validity.
	 * 
	 * @param sGoalId
	 * @return
	 */
	public List<Tagctivity> getActivities4GoalId(String sGoalId) {
		List<Tagctivity> listTags = new ArrayList<Tagctivity>();
		String selectQueryTag = "SELECT a.* FROM " + Tag.TABLE_NAME
				+ " t INNER JOIN " + Tagctivity.TABLE_NAME + " a ON t."
				+ Tag.KEY_ID_TAG + " = a." + Tagctivity.KEY_ID_TAG
				+ " WHERE t." + Tag.KEY_ID_GOAL + " = '" + sGoalId + "' AND a."
				+ Tagctivity.KEY_DATE_CHECKIN + " < t." + Tag.KEY_VALIDITY_END
				+ " AND a." + Tagctivity.KEY_DATE_CHECKOUT + " > t."
				+ Tag.KEY_VALIDITY_BEGIN + "";
		// SELECT a.* FROM tags t INNER JOIN activities a ON t.id_tag = a.id_tag
		// WHERE t.id_goal = '1' AND a.date_check_in < t.date_validity_end AND
		// a.date_check_out > t.date_validity_begin

		sqliteDB = this.getWritableDatabase();

		Cursor cursor = sqliteDB.rawQuery(selectQueryTag, null);
		if (cursor.moveToFirst()) {
			do {
				Tagctivity o = new Tagctivity(cursor);
				listTags.add(o);

			} while (cursor.moveToNext());
		}
		cursor.close();
		sqliteDB.close();
		return listTags;
	}
	
	
	

	/**
	 * Returns entries in a hashmap corresponding to the Goal name and time
	 * investment on the goal in mills
	 * 
	 * @return
	 */
	public HashMap<String, Long> getGoalsTime() {
		HashMap<String, Long> listTags = new HashMap<String, Long>();
		String selectQueryTag = "SELECT g.name, SUM(a.date_check_out-a.date_check_in) FROM tags t INNER JOIN activities a ON t.id_tag = a.id_tag INNER JOIN goals g ON t.id_goal = g."
				+ Goal.KEY_NAME
				+ " WHERE  a.date_check_in < t.date_validity_end AND a.date_check_out > t.date_validity_begin GROUP BY t.id_goal";

		sqliteDB = this.getWritableDatabase();

		Cursor cursor = sqliteDB.rawQuery(selectQueryTag, null);
		if (cursor.moveToFirst()) {
			do {

				listTags.put(cursor.getString(0),
						Long.parseLong(cursor.getString(1)));

			} while (cursor.moveToNext());
		}
		cursor.close();
		sqliteDB.close();
		return listTags;
	}
	
	
    /**
     * Returns list with time accumulated for each goal.
     * Note that this Hashmap does not return an element (with acummulation 0) for those goals that did not accumulated time.
     * 
     * where
     *      String is the goal id 
     *  	Long is the accumulated time for this task
     * @return
     */     
    public TreeMap<String, Long> getGoalsAccumulated() {
            
    		TreeMap<String, Long> hm = new TreeMap<String, Long>();

            String selectQuery = "SELECT "+Tag.KEY_ID_GOAL+", sum("+Tagctivity.KEY_DATE_CHECKOUT+"-"+Tagctivity.KEY_DATE_CHECKIN+") accum FROM "+Tagctivity.TABLE_NAME+" a, "+Tag.TABLE_NAME+" t WHERE a."+Tagctivity.KEY_ID_TAG+"=t."+Tag.KEY_ID_TAG+" GROUP BY "+Tag.KEY_ID_GOAL;
            
            sqliteDB = this.getWritableDatabase();
            
            Cursor cursor = sqliteDB.rawQuery(selectQuery, null);
            if (cursor.moveToFirst()) {
                    do {                            
                            // Update data in session
                            String sGoalId = cursor.getString(0);
                            long lDuration = Long.parseLong(cursor.getString(1));
                            
                            hm.put(sGoalId, lDuration);
                            


                    } while (cursor.moveToNext());
            }
            cursor.close();
            sqliteDB.close();
            
            
            return hm;
    }	
    
    /**
     * Adds days to d
     */
    public static void addDays(Date d, int days)
    {
        Calendar c = Calendar.getInstance();
        c.setTime(d);
        c.add(Calendar.DATE, days);
        d.setTime( c.getTime().getTime() );
    }    

}