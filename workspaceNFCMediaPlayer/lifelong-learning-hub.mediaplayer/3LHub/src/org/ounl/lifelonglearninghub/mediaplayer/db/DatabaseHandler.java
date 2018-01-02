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
package org.ounl.lifelonglearninghub.mediaplayer.db;

import java.util.ArrayList;
import java.util.List;

import org.ounl.lifelonglearninghub.mediaplayer.db.tables.Video;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DatabaseHandler extends SQLiteOpenHelper {

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

		Log.d("DatabaseHandler", "Creating table Videos...");
		db.execSQL(Video.getCreateTable());
		Log.d("DatabaseHandler", "Tables created!!");

	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

		db.execSQL("DROP TABLE IF EXISTS " + Video.TABLE_NAME);

		onCreate(db);
	}

	/**
	 * Insert video into db
	 * 
	 * @param o
	 */
	public void addVideo(Video o) {

		sqliteDB = this.getWritableDatabase();
		ContentValues values = new ContentValues();
		o.loadContentValues(values);
		try {
			sqliteDB.insert(Video.TABLE_NAME, null, values);
		} catch (Exception e) {
			Log.e("DatabaseHandler", "Error inserting goal " + e.toString()
					+ "");
			e.printStackTrace();
		}
		sqliteDB.close();
	}

	/**
	 * Get videos from database
	 * 
	 * @return
	 */
	public List<Video> getVideos() {
		List<Video> list = new ArrayList<Video>();
		String selectQuery = "SELECT  * FROM " + Video.TABLE_NAME;
		sqliteDB = this.getWritableDatabase();
		Cursor cursor = sqliteDB.rawQuery(selectQuery, null);
		if (cursor.moveToFirst()) {
			do {
				Video o = new Video(cursor);
				list.add(o);
			} while (cursor.moveToNext());
		}
		cursor.close();
		sqliteDB.close();
		return list;
	}

}