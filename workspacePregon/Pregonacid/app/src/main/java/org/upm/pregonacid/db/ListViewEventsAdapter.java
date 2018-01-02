package org.upm.pregonacid.db;

import android.app.Activity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import org.upm.pregonacid.R;
import org.upm.pregonacid.db.tables.EventDb;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

public class ListViewEventsAdapter extends BaseAdapter {
	
	private String CLASSNAME = this.getClass().getName();
	
	private ArrayList<HashMap> list;

	Activity activity;

	public ListViewEventsAdapter(Activity activity, ArrayList<HashMap> list) {
		super();
		this.activity = activity;
		this.list = list;
		

		Log.d(CLASSNAME, "Creating list adapter with " + getCount() + " items  ");
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return list.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return list.get(position);
	}

	@Override
	public long getItemId(int position) {

		HashMap map = list.get(position);
		
		// Assign itemId as subject id
		Long oLong = (Long)map.get(EventDb.KEY_ID);
		
		Log.e(CLASSNAME, "Item id: "+oLong.longValue());

		return oLong.longValue();
		
	}

	@Override
	public View getView(int position, View v, ViewGroup parent) {

		EventRow sr;

		LayoutInflater inflater = activity.getLayoutInflater();
		if (v == null) {
			v = inflater.inflate(R.layout.event_list_row, null);
			sr = new EventRow();
			sr.iv = (ImageView) v.findViewById(R.id.ivSubjectIcon);
			sr.tvField1 = (TextView) v.findViewById(R.id.tvSubjectLevel1);
			sr.tvField2 = (TextView) v.findViewById(R.id.tvSubjectLevel2);
			sr.tvField3 = (TextView) v.findViewById(R.id.tvSubjectLevel3);
			v.setTag(sr);


		} else {
			sr = (EventRow) v.getTag();
		}

		HashMap map = list.get(position);
		
		sr.sIdEvent = (Long)map.get(EventDb.KEY_ID);
		
		// Level 1
		sr.tvField1.setText((String)map.get(EventDb.KEY_SUBTITLE));
		
		// Level 2
		sr.tvField2.setText((String)map.get(EventDb.KEY_SUBSUBTITLE));
		
		// Level 3		
		long lDateStart = (Long)map.get(EventDb.KEY_TIMESTAMP);
		Date dDateStart = new Date();
		dDateStart.setTime(lDateStart);
		// DateFormat df = SimpleDateFormat.getDateTimeInstance();
		DateFormat df = new SimpleDateFormat("E, dd/MM/yyyy HH:mm:ss");  
		String reportDate = df.format(dDateStart);
		sr.tvField3.setText("  "+reportDate);
		
		return v;
	}
}
