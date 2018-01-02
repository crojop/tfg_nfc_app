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

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import org.ounl.lifelonglearninghub.learntracker.gis.ou.db.tables.SubjectDb;
import org.ounl.lifelonglearninghub.learntracker.gis.ou.session.ActivitySession;
import org.ounl.lifelonglearninghub.learntracker.gis.ou.session.Session;
import org.ounl.lifelonglearninghub.learntracker.gis.ou.R;

import android.app.Activity;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class ListViewSubjectsAdapter extends BaseAdapter {
	
	private String CLASSNAME = this.getClass().getName();
	
	private ArrayList<HashMap> list;

	Activity activity;

	public ListViewSubjectsAdapter(Activity activity, ArrayList<HashMap> list) {
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
		String sSubject = (String)map.get(SubjectDb.KEY_ID);
		Long oLong = new Long(sSubject);
		
		Log.e(CLASSNAME, "Item id: "+oLong.longValue());

		return oLong.longValue();
		
	}

	@Override
	public View getView(int position, View v, ViewGroup parent) {

		SubjectRow sr;

		LayoutInflater inflater = activity.getLayoutInflater();
		if (v == null) {
			v = inflater.inflate(R.layout.listview_row_subject, null);
			sr = new SubjectRow();
			sr.iv = (ImageView) v.findViewById(R.id.ivSubjectIcon);
			sr.tvField1 = (TextView) v.findViewById(R.id.tvSubjectLevel1);
			sr.tvField2 = (TextView) v.findViewById(R.id.tvSubjectLevel2);
			sr.tvField3 = (TextView) v.findViewById(R.id.tvSubjectLevel3);
			v.setTag(sr);


		} else {
			sr = (SubjectRow) v.getTag();
		}

		HashMap map = list.get(position);
		
		sr.sIdSubject = (String)map.get(SubjectDb.KEY_ID);
		
		// Level 1
		//sr.tvField1.setText( (position+1) + ". " + map.get(SubjectDb.KEY_TASK_ALTERNATIVE_DESC));
		//sr.tvField1.setText("["+(position+1)+"] "+(String)map.get(SubjectDb.KEY_TASK_DESC));
		sr.tvField1.setText((String)map.get(SubjectDb.KEY_TASK_DESC));
		
		// Level 2
		sr.tvField2.setText((String)map.get(SubjectDb.KEY_TASK_ALTERNATIVE_DESC));
		
		// Level 3		
		long lDateStart = (Long)map.get(SubjectDb.KEY_TASK_DATE_START);
		Date dDateStart = new Date();
		dDateStart.setTime(lDateStart);
		DateFormat df = new SimpleDateFormat("E, dd/MM/yyyy");  
		String reportDate = df.format(dDateStart);
		sr.tvField3.setText("  Begin: "+reportDate);
		

		ActivitySession as = Session.getSingleInstance().getActivity(position);		
		if (as != null) {

			if (as.getStatus() == ActivitySession.STATUS_STOPPED) {
				Animation anim = new AlphaAnimation(0.0f, 1.0f);
				anim.setRepeatCount(0);

				Drawable d = activity.getResources().getDrawable(
						R.drawable.start_50x);
				sr.iv.setImageDrawable(d);
				sr.iv.setAnimation(anim);
			} else {
				Animation anim = new AlphaAnimation(0.0f, 1.0f);
				anim.setDuration(500);
				anim.setStartOffset(20);
				anim.setRepeatMode(Animation.REVERSE);
				anim.setRepeatCount(Animation.INFINITE);

				Drawable d = activity.getResources().getDrawable(
						R.drawable.rec_50x);
				sr.iv.setImageDrawable(d);
				sr.iv.setAnimation(anim);
			}

		}
		
		return v;
	}
}
