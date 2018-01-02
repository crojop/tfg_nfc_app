package org.upm.pregonacid.db.tables;

import android.content.ContentValues;
import android.database.Cursor;

import org.upm.pregonacid.db.ws.dataobjects.EventDO;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class EventDb implements ITables {

	public static final String TABLE_NAME = "event";

	public static final String KEY_ID = "id";
	public static final String KEY_TIMESTAMP = "timestamp";
	public static final String KEY_TITLE = "title";
	public static final String KEY_SUBTITLE = "subtitle";
	public static final String KEY_SUBSUBTITLE = "subsubtitle";
	public static final String KEY_AUTHOR = "author";
	public static final String KEY_STATE = "state";

	private long lId;
	private long lTimeStamp;
	private String sTitle;
	private String sSubTitle;
	private String sSubSubTitle;
	private String sAuthor;
	private int iState;

	public EventDb(long lId, long lTimeStamp, String sTitle, String sSubTitle, String sSubSubTitle,
				   String sAuthor,
				   int iState) {
		super();
		this.lId = lId;
		this.lTimeStamp = lTimeStamp;
		this.sTitle = sTitle;
		this.sSubTitle = sSubTitle;
		this.sSubSubTitle = sSubSubTitle;
		this.sAuthor = sAuthor;
		this.iState = iState;
	}

	public EventDb(Cursor c) {

		this.lId = Long.parseLong(c.getString(0));
		this.lTimeStamp = Long.parseLong(c.getString(1));
		this.sTitle = c.getString(2);
		this.sSubTitle = c.getString(3);
		this.sSubSubTitle = c.getString(4);
		this.sAuthor = c.getString(5);
		this.iState = Integer.parseInt(c.getString(6));
	}
	
	public EventDb(EventDO edo) {

		try{
			this.lId = Long.valueOf(edo.getId());
		}catch(Exception e){
			this.lId = 0l;
		}

		try{
			this.lTimeStamp = Long.valueOf(edo.getTimestamp());
		}catch(Exception e){
			this.lTimeStamp = 0l;
		}

		this.sTitle = edo.getTitle();
		this.sSubTitle = edo.getSubtitle();
		this.sSubSubTitle = edo.getSubsubtitle();
		this.sAuthor = edo.getAuthor();

		try{
			Integer oI = Integer.valueOf(edo.getState());
			this.iState = oI.intValue();
		}catch(Exception e){
			this.iState = 0;
		}
	}

	/**
	 * Receives milliseconds
	 * @return duration in minutes
	 */
	public int getTaskDurationInMins(long lTaskTimeDuration) {

		int minutes = (int) ((lTaskTimeDuration / 1000) / 60);
		return minutes;
	}

	public static String getCreateTable() {

		String sSQL = "CREATE TABLE " + TABLE_NAME + "(" 
				+ KEY_ID + " integer primary key autoincrement,"
				+ KEY_TIMESTAMP + " INTEGER,"
				+ KEY_TITLE + " TEXT, "
				+ KEY_SUBTITLE + " TEXT, "
				+ KEY_SUBSUBTITLE + " TEXT, "
				+ KEY_AUTHOR + " TEXT,"
				+ KEY_STATE + " INTEGER "
				+ ")";

		return sSQL;

	}

	public void loadContentValues(ContentValues cv) {

		cv.put(KEY_ID, lId);
		cv.put(KEY_TIMESTAMP, lTimeStamp);
		cv.put(KEY_TITLE, sTitle);
		cv.put(KEY_SUBTITLE, sSubTitle);
		cv.put(KEY_SUBSUBTITLE, sSubSubTitle);
		cv.put(KEY_AUTHOR, sAuthor);
		cv.put(KEY_STATE, iState);

	}

	public String getFormattedTaskDateStart() {
		DateFormat df = new SimpleDateFormat("dd/MM/yyyy HH:mm");
		Date dDateStart = new Date(lTimeStamp);

		return df.format(dDateStart);
	}

	public int getiState() {
		return iState;
	}

	public void setiState(int iState) {
		this.iState = iState;
	}

	public String getsAuthor() {
		return sAuthor;
	}

	public void setsAuthor(String sAuthor) {
		this.sAuthor = sAuthor;
	}

	public String getsSubSubTitle() {
		return sSubSubTitle;
	}

	public void setsSubSubTitle(String sSubSubTitle) {
		this.sSubSubTitle = sSubSubTitle;
	}

	public String getsSubTitle() {
		return sSubTitle;
	}

	public void setsSubTitle(String sSubTitle) {
		this.sSubTitle = sSubTitle;
	}

	public String getsTitle() {
		return sTitle;
	}

	public void setsTitle(String sTitle) {
		this.sTitle = sTitle;
	}

	public long getlTimeStamp() {
		return lTimeStamp;
	}

	public void setlTimeStamp(long lTimeStamp) {
		this.lTimeStamp = lTimeStamp;
	}

	public long getlId() {
		return lId;
	}

	public void setlId(long lId) {
		this.lId = lId;
	}

}
