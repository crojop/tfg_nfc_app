package org.upm.pregonacid.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import org.upm.pregonacid.db.tables.EventDb;
import org.upm.pregonacid.db.ws.dataobjects.EventDO;

import java.util.ArrayList;
import java.util.List;

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
		
		Log.d(CLASSNAME, "Creating table subjects...");
		db.execSQL(EventDb.getCreateTable());
		Log.d(CLASSNAME, "Tables created!!");
		

	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		
		db.execSQL("DROP TABLE IF EXISTS " + EventDb.TABLE_NAME);
		
		onCreate(db);
	}



	/**
	 * Insert subject into db
	 * 
	 * @param o
	 */
	public void addEvent(EventDb o) {

		sqliteDB = this.getWritableDatabase();
		ContentValues values = new ContentValues();
		o.loadContentValues(values);
		try {
			sqliteDB.insert(EventDb.TABLE_NAME, null, values);
		} catch (Exception e) {
			Log.e("DatabaseHandler", "Error inserting subject " + e.toString() + "");
			e.printStackTrace();
		}
		sqliteDB.close();
	}

	/**
	 * Insert list of subjects into db
	 * 
	 * @param eList
	 */
	public void addEvents(List<EventDb> eList) {

		for (int i = 0; i < eList.size(); i++) {
			addEvent(eList.get(i));
		}

	}

	
	/**
	 * Insert list of subject data objects into local db
	 * 
	 * @param events
	 */
	public void addListEventsDO(List<EventDO> events) {

		for (int i = 0; i < events.size(); i++) {
			addEvent(events.get(i).toSqliteObject());
		}

	}
	
	
	
	/**
	 * Get subjects from database ordered by order
	 * 
	 * @return
	 */
	public List<EventDb> getEvents() {
		List<EventDb> list = new ArrayList<EventDb>();
		String selectQuery = "SELECT  * FROM " + EventDb.TABLE_NAME + " ORDER BY "+EventDb.KEY_TIMESTAMP+ " desc";
		sqliteDB = this.getWritableDatabase();
		Cursor cursor = sqliteDB.rawQuery(selectQuery, null);
		if (cursor.moveToFirst()) {
			do {
				EventDb o = new EventDb(cursor);
				list.add(o);
			} while (cursor.moveToNext());
		}
		cursor.close();
		sqliteDB.close();
		return list;
	}


}