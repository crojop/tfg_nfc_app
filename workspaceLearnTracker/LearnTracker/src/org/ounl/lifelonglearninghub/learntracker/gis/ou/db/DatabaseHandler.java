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
package org.ounl.lifelonglearninghub.learntracker.gis.ou.db;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.ounl.lifelonglearninghub.learntracker.gis.ou.db.pojo.SubjectAcummulatedPJ;
import org.ounl.lifelonglearninghub.learntracker.gis.ou.db.tables.ActividadDb;
import org.ounl.lifelonglearninghub.learntracker.gis.ou.db.tables.SubjectDb;
import org.ounl.lifelonglearninghub.learntracker.gis.ou.db.ws.dataobjects.ActivityDO;
import org.ounl.lifelonglearninghub.learntracker.gis.ou.db.ws.dataobjects.SubjectDO;
import org.ounl.lifelonglearninghub.learntracker.gis.ou.session.Session;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DatabaseHandler extends SQLiteOpenHelper {

	private String CLASSNAME = this.getClass().getName();
	
	//private static final int DATABASE_VERSION = 1;
	private static final int DATABASE_VERSION = 2;
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

		Log.d("DatabaseHandler", "Creating table Activities...");
		db.execSQL(ActividadDb.getCreateTable());
		
		Log.d(CLASSNAME, "Creating table subjects...");
		db.execSQL(SubjectDb.getCreateTable());
		
		Log.d(CLASSNAME, "Tables created!!");
		

	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

		db.execSQL("DROP TABLE IF EXISTS " + ActividadDb.TABLE_NAME);
		db.execSQL("DROP TABLE IF EXISTS " + SubjectDb.TABLE_NAME);
		
		onCreate(db);
	}



	/**
	 * Insert subject into db
	 * 
	 * @param o
	 */
	public void addSubject(SubjectDb o) {

		sqliteDB = this.getWritableDatabase();
		ContentValues values = new ContentValues();
		o.loadContentValues(values);
		try {
			sqliteDB.insert(SubjectDb.TABLE_NAME, null, values);
		} catch (Exception e) {
			Log.e("DatabaseHandler", "Error inserting subject " + e.toString()
					+ "");
			e.printStackTrace();
		}
		sqliteDB.close();
	}

	/**
	 * Insert list of subjects into db
	 * 
	 * @param goals
	 */
	public void addSubject(List<SubjectDb> subjects) {

		for (int i = 0; i < subjects.size(); i++) {
			addSubject(subjects.get(i));
		}

	}

	
	/**
	 * Insert list of subject data objects into local db
	 * 
	 * @param goals
	 */
	public void addSubjectDO(List<SubjectDO> subjects) {

		for (int i = 0; i < subjects.size(); i++) {
			addSubject(subjects.get(i).toSqliteObject());
		}

	}
	
	
	/**
	 * Insert list of activity data objects into local db
	 * 
	 * @param goals
	 */
	public void addActivityDO(List<ActivityDO> las) {

		for (int i = 0; i < las.size(); i++) {
			addActivity(las.get(i).toSqliteObject());
		}

	}

	


	/**
	 * Insert notification into db
	 * 
	 * @param o
	 *            @ returns true went right 
	 *            @ returns false went wrong
	 */
	public boolean addActivity(ActividadDb o) {

		try {

			// Insert to make check in
			sqliteDB = this.getWritableDatabase();
			ContentValues valuesInsert = new ContentValues();
			o.loadContentValues(valuesInsert);
			sqliteDB.insert(ActividadDb.TABLE_NAME, null, valuesInsert);
			sqliteDB.close();

		} catch (Exception e) {
			Log.e("DatabaseHandler",
					"Error inserting Tagctivity " + e.toString() + "");
			e.printStackTrace();
			sqliteDB.close();
			return false;

		}
		return true;

	}
	
	/**
	 * Get subjects from database ordered by order
	 * 
	 * @return
	 */
	public List<SubjectDb> getSubjects() {
		List<SubjectDb> list = new ArrayList<SubjectDb>();
		String selectQuery = "SELECT  * FROM " + SubjectDb.TABLE_NAME + " ORDER BY "+SubjectDb.KEY_TASK_ORDER;
		sqliteDB = this.getWritableDatabase();
		Cursor cursor = sqliteDB.rawQuery(selectQuery, null);
		if (cursor.moveToFirst()) {
			do {
				SubjectDb o = new SubjectDb(cursor);
				list.add(o);
			} while (cursor.moveToNext());
		}
		cursor.close();
		sqliteDB.close();
		return list;
	}

	
	/**
	 * Returns list with time accumulated for each task.
	 * Note that this Hashmap does not return an element (with acummulation 0) for those subjects that did not accumulated time.
	 * 
	 * where
	 * 	Integer is the order 
	 *  Long is the accumulated time for this task
	 * @return
	 */	
	public HashMap<Integer, Long> getSubjectsAccumulated() {
		
		HashMap<Integer, Long> hm = new HashMap<Integer, Long>();

		String selectQuery = "SELECT "
				+SubjectDb.KEY_TASK_ORDER
				+", sum("+ActividadDb.KEY_DATE_CHECKOUT+"-"+ActividadDb.KEY_DATE_CHECKIN
				+") FROM " + ActividadDb.TABLE_NAME + " a INNER JOIN "+ SubjectDb.TABLE_NAME 
				+" s ON s."+SubjectDb.KEY_ID+"=a."+ActividadDb.KEY_ID_SUBJECT
				+" GROUP BY "+ActividadDb.KEY_ID_SUBJECT
				+" ORDER BY "+SubjectDb.KEY_TASK_ORDER;
		
		
		sqliteDB = this.getWritableDatabase();
		
		Cursor cursor = sqliteDB.rawQuery(selectQuery, null);
		if (cursor.moveToFirst()) {
			do {				
				// Update data in session
				Integer oISubjectTaskOrder = new Integer(cursor.getString(0));
				long lDuration = Long.parseLong(cursor.getString(1));
				
				SubjectAcummulatedPJ o = new SubjectAcummulatedPJ(oISubjectTaskOrder.intValue(), lDuration);
				hm.put(oISubjectTaskOrder, lDuration);
				


			} while (cursor.moveToNext());
		}
		cursor.close();
		sqliteDB.close();
		
		
		return hm;
	}
	
	
	
	
	/**
	 * Returns accumulated duration in milliseconds for given subjectID
	 * 
	 * @param sSubjectId
	 * @return
	 */
	public long getAccumulatedTime(String sSubjectId) {
		
		String selectQuery = "SELECT "
				+SubjectDb.KEY_TASK_ORDER
				+", sum("+ActividadDb.KEY_DATE_CHECKOUT+"-"+ActividadDb.KEY_DATE_CHECKIN
				+") FROM " + ActividadDb.TABLE_NAME + " a INNER JOIN "+ SubjectDb.TABLE_NAME 
				+" s ON s."+SubjectDb.KEY_ID+"=a."+ActividadDb.KEY_ID_SUBJECT
				+" WHERE " + ActividadDb.KEY_ID_SUBJECT + "='" + sSubjectId + "'";
		
		
		
		sqliteDB = this.getWritableDatabase();
		long lDuration = 0L;
		Cursor cursor = sqliteDB.rawQuery(selectQuery, null);
		if (cursor!=null){
			if (cursor.moveToFirst()) {
				try{
					lDuration = Long.parseLong(cursor.getString(1));
				}catch(Exception e){
					Log.i(CLASSNAME, "getAccumulatedTime. The following query returns 0 activities ["+selectQuery+"]");
				}
			}else{
				Log.i(CLASSNAME, "getAccumulatedTime. The following query returned empty cursor ["+selectQuery+"]");
			}
		}else{
			Log.i(CLASSNAME, "getAccumulatedTime. The following query returned null cursor ["+selectQuery+"]");
		}

		cursor.close();
		sqliteDB.close();
		
		return lDuration;
	}
	
	
	

	/**
	 * Return list of activities 
	 * 
	 * @param idSubject
	 * @return
	 */
	public List<ActividadDb> getActivities() {
		List<ActividadDb> list = new ArrayList<ActividadDb>();
		String selectQuery = "SELECT  * FROM " + ActividadDb.TABLE_NAME;		
		sqliteDB = this.getWritableDatabase();
		Cursor cursor = sqliteDB.rawQuery(selectQuery, null);
		if (cursor!=null){
			if (cursor.moveToFirst()) {
				do {
					ActividadDb o = new ActividadDb(cursor);
					list.add(o);
				} while (cursor.moveToNext());
			}
		}else{
			Log.i(CLASSNAME, "The following query returned an empty cursor ["+selectQuery+"]");
		}
		
		
		
		cursor.close();
		sqliteDB.close();
		return list;
	}
	
	
	/**
	 * Return list of activities for a given subjectId
	 * 
	 * @param idSubject
	 * @return
	 */
	public List<ActividadDb> getActivities(String sSubjectId) {
		List<ActividadDb> list = new ArrayList<ActividadDb>();
		String selectQuery = "SELECT  * FROM " + ActividadDb.TABLE_NAME+" WHERE " + ActividadDb.KEY_ID_SUBJECT + "='" + sSubjectId + "'";
		sqliteDB = this.getWritableDatabase();
		Cursor cursor = sqliteDB.rawQuery(selectQuery, null);
		if (cursor!=null){
			if (cursor.moveToFirst()) {
				do {
					ActividadDb o = new ActividadDb(cursor);
					list.add(o);
				} while (cursor.moveToNext());
			}
		}else{
			Log.i(CLASSNAME, "The following query returned an empty cursor ["+selectQuery+"]");
		}
		
		
		
		cursor.close();
		sqliteDB.close();
		return list;
	}
	
	
	/**
	 * Deletes activity(ies) for given date check in 
	 * 
	 * @param check_in
	 * @return the number of rows affected if a whereClause is passed in, 0 otherwise. 
	 * To remove all rows and get a count pass "1" as the whereClause.
	 * 
	 */
	public int deleteActivity(long check_in) 
	{
		sqliteDB = this.getWritableDatabase();
	    int iResult = sqliteDB.delete(ActividadDb.TABLE_NAME, ActividadDb.KEY_DATE_CHECKIN + "=" + check_in, null);
	    sqliteDB.close();
	    
	    return iResult;
	    
	}	
	
	
	/**
	 * Loads default course into local database whenever there is no connection to backend
	 * 
	 */
	public List<SubjectDb> addDefaultSubjects(){
		
		List<SubjectDb> lSDb = new ArrayList<SubjectDb>();
		
		
		SubjectDb sdb0 = new SubjectDb("1000", "Default Spanish Course","Gramatica", "Self study", 1479945600000L, 18000000L, 1, 0);
		SubjectDb sdb1 = new SubjectDb("1001", "Default Spanish Course","Escuchar", "Self study", 1480118400000L, 10800000L, 1, 1);
		SubjectDb sdb2 = new SubjectDb("1002", "Default Spanish Course","Hablar", "Self study", 1480291200000L, 7200000L, 1, 2);
		SubjectDb sdb3 = new SubjectDb("1003", "Default Spanish Course","Escribir", "Self study", 1480377600000L, 14400000L, 1, 3);
		SubjectDb sdb4 = new SubjectDb("1004", "Default Spanish Course","Leccion 1", "Lecture", 1480550400000L, 18000000L, 1, 4);
		SubjectDb sdb5 = new SubjectDb("1005", "Default Spanish Course","Leccion 2", "Lecture", 1480809600000L, 7200000L, 1, 5);
		SubjectDb sdb6 = new SubjectDb("1006", "Default Spanish Course","Leccion 3", "Lecture", 1481068800000L, 7200000L, 1, 6);
		SubjectDb sdb7 = new SubjectDb("1007", "Default Spanish Course","Leccion 4", "Lecture", 1481155200000L, 7200000L, 1, 7);
		SubjectDb sdb8 = new SubjectDb("1008", "Default Spanish Course","Leccion 5", "Lecture", 1481241600000L, 7200000L, 1, 8);
		SubjectDb sdb9 = new SubjectDb("1009", "Default Spanish Course","Examen final", "Evaluation", 1481328000000L, 7200000L, 1, 9);

		
//		SubjectDb sdb0 = new SubjectDb("DSC", "Default Spanish Course","Gramatica", "Self study", 1479945600000L, 18000000L, 1, 0);
//		SubjectDb sdb1 = new SubjectDb("DSC", "Default Spanish Course","Escuchar", "Self study", 1480118400000L, 10800000L, 1, 1);
//		SubjectDb sdb2 = new SubjectDb("DSC", "Default Spanish Course","Hablar", "Self study", 1480291200000L, 7200000L, 1, 2);
//		SubjectDb sdb3 = new SubjectDb("DSC", "Default Spanish Course","Escribir", "Self study", 1480377600000L, 14400000L, 1, 3);
//		SubjectDb sdb4 = new SubjectDb("DSC", "Default Spanish Course","Leccion 1", "Lecture", 1480550400000L, 18000000L, 1, 4);
//		SubjectDb sdb5 = new SubjectDb("DSC", "Default Spanish Course","Leccion 2", "Lecture", 1480809600000L, 7200000L, 1, 5);
//		SubjectDb sdb6 = new SubjectDb("DSC", "Default Spanish Course","Leccion 3", "Lecture", 1481068800000L, 7200000L, 1, 6);
//		SubjectDb sdb7 = new SubjectDb("DSC", "Default Spanish Course","Leccion 4", "Lecture", 1481155200000L, 7200000L, 1, 7);
//		SubjectDb sdb8 = new SubjectDb("DSC", "Default Spanish Course","Leccion 5", "Lecture", 1481241600000L, 7200000L, 1, 8);
//		SubjectDb sdb9 = new SubjectDb("DSC", "Default Spanish Course","Examen final", "Evaluation", 1481328000000L, 7200000L, 1, 9);		
		
		lSDb.add(sdb0);
		lSDb.add(sdb1);
		lSDb.add(sdb2);
		lSDb.add(sdb3);
		lSDb.add(sdb4);
		lSDb.add(sdb5);
		lSDb.add(sdb6);
		lSDb.add(sdb7);
		lSDb.add(sdb8);
		lSDb.add(sdb9);
		
		addSubject(lSDb);
		
		return lSDb;
		
		
		
	}


}