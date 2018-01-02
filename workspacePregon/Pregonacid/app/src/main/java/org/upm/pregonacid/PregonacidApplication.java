package org.upm.pregonacid;

import android.app.Application;

import org.upm.pregonacid.db.DatabaseHandler;
import org.upm.pregonacid.db.tables.EventDb;
import org.upm.pregonacid.db.ws.dataobjects.EventDO;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;


public class PregonacidApplication extends Application {
	
	
	List<EventDb> events = new ArrayList<EventDb>();
	DatabaseHandler db;
	Properties config = new Properties();
	
	
	@Override
	public void onCreate() {
		super.onCreate();
	}
/*
	@Override
	protected void attachBaseContext(Context base) {
		super.attachBaseContext(base);
		MultiDex.install(this);
	}
*/
	public void setEventsDO(List<EventDO> eDO) {
		
		List<EventDb> eventsDb = new ArrayList<EventDb>();		
		for (EventDO eventDO : eDO) {
			eventsDb.add(new EventDb(eventDO));
		}
		
		events = eventsDb;
	}

	public DatabaseHandler getDb() {
		return db;
	}
	public void setDb(DatabaseHandler db) {
		this.db = db;
	}

	public Properties getConfig() {
		return config;
	}
	public void setConfig(Properties config) {
		this.config = config;
	}

	public List<EventDb> getEvents() {
		return events;
	}
	public void setEvents(List<EventDb> p) {
		events = p;
	}

}
