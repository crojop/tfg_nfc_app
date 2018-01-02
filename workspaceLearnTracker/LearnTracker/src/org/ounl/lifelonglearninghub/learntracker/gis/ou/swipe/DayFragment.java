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
package org.ounl.lifelonglearninghub.learntracker.gis.ou.swipe;

import java.util.List;

import org.ounl.lifelonglearninghub.learntracker.gis.ou.Constants;
import org.ounl.lifelonglearninghub.learntracker.gis.ou.db.tables.ActividadDb;
import org.ounl.lifelonglearninghub.learntracker.gis.ou.session.ActivitySession;
import org.ounl.lifelonglearninghub.learntracker.gis.ou.session.Session;
import org.ounl.lifelonglearninghub.learntracker.gis.ou.R;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TimePicker;



public class DayFragment extends Fragment {
	
	private String CLASSNAME = this.getClass().getName();

	public static final String ARG_POSITION = "position";
	
	private LayoutInflater mInflater;
	private View rootView;
	private int mPosition = 0;
	private String mTitle = "";

	
	

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

	
		
		// polla AQUI SOLO SE LLAMA UNA VEZ QUE ES LA PRIMERA CUANDO SE HACE getitem en el dayfragmentstatepageradapter
		
		rootView = inflater.inflate(R.layout.fragment_collection_object, container, false);
		mInflater = inflater;
		
		Bundle args = getArguments();
		mPosition = args.getInt(ARG_POSITION);
		
		inflateLayout(mPosition);
				
		Log.d(CLASSNAME, "onCreateView DayFragment item position["+mPosition+"] title["+mTitle+"].");
				
		
		
		
		return rootView;
		
	}
	
//	NONE OF THE METHODS BELOW IS CALLED WHEN CALLED FOR THE SECOND TIME
//	LOOK THIS ADVICE
//	http://stackoverflow.com/questions/19339500/when-is-fragmentpageradapters-getitem-called
		
	
//	
//	@Override
//	public void onResume (){
//		super.onResume();
//		Log.d(CLASSNAME, "onResume DayFragment item["+mPosition+"].");
//	}
//	
//	@Override
//	public void onStart (){
//		super.onStart();
//		Log.d(CLASSNAME, "onStart DayFragment item["+mPosition+"].");
//	}
//	
//	@Override
//	public void onViewCreated (View view, Bundle savedInstanceState){
//		super.onViewCreated(view, savedInstanceState);
//		Log.d(CLASSNAME, "onViewCreated DayFragment item["+mPosition+"].");
//	}
//	
//    @Override
//    public void setMenuVisibility(final boolean visible) {
//        super.setMenuVisibility(visible);
//		Log.d(CLASSNAME, "setMenuVisibility DayFragment Visible["+visible+"] item["+mPosition+"].");
//    }
//    
//    @Override
//    public void setUserVisibleHint(boolean isVisibleToUser) {
//        super.setUserVisibleHint(isVisibleToUser);
//        Log.d(CLASSNAME, "setUserVisibleHint DayFragment Visible["+isVisibleToUser+"] item["+mPosition+"].");
//    }    
	
	
	public void inflateLayout(int iPos){
		
		String sIdSubject = Session.getSingleInstance().getDatabaseHandler().getSubjects().get(iPos).getsId();
		
        // String sIdSubject = Session.getSingleInstance().getActivity(iPos).getId_subject();        
        long lDuration = Session.getSingleInstance().getDatabaseHandler().getAccumulatedTime(sIdSubject);
        
        // List of activities to inflate history
        List<ActividadDb> lADb = Session.getSingleInstance().getDatabaseHandler().getActivities(sIdSubject);
        
		
		// Duration
		DateUtils du = new DateUtils();
		TextView t = (TextView) rootView.findViewById(R.id.tvDuration);
		t.setText(du.duration(lDuration));

		
		// Time picker
        TimePicker timePicker = (TimePicker)rootView.findViewById(R.id.tpTask);        
        timePicker.setIs24HourView(true);        
        timePicker.setCurrentHour(0);
        timePicker.setCurrentMinute(45);

        
        // Button image view	
		ImageView iv = (ImageView)rootView.findViewById(R.id.ivActionActivity);
		int currentStatus = Session.getSingleInstance().getActivity(iPos).getStatus();		
		if (currentStatus == ActivitySession.STATUS_STOPPED){
			Drawable d = getResources().getDrawable(R.drawable.play);
			iv.setImageDrawable(d);   
		}else{
			Drawable d = getResources().getDrawable(R.drawable.stop);
			iv.setImageDrawable(d);
		}	
		
		
		// History		
		if (lADb.size() != 0){
			
			LinearLayout llHistory = (LinearLayout)rootView.findViewById(R.id.llHistory);
			TextView tvHeader = (TextView) llHistory.findViewById(R.id.tvHeaderHisory);
			tvHeader.setVisibility(View.VISIBLE);
			
			
			for (int i = 0; i < lADb.size(); i++) {


				ActividadDb adb = lADb.get(i);
		        
		        
		        LinearLayout llCheckItemWrapper = (LinearLayout) mInflater.inflate(R.layout.check_item, null);
		        // Passing order as param so it can be removed
		        llCheckItemWrapper.setTag(i);

		        LinearLayout liContent = (LinearLayout) llCheckItemWrapper.getChildAt(0);
		                     
    
		        TextView tvTimeStamp = (TextView) liContent.findViewById(R.id.textViewTimeStamp);
		        tvTimeStamp.setText(Constants.TIME_FORMAT.format(adb.getlDateCheckIn()));
		        // Passing checkin time in mills as tag
		        tvTimeStamp.setTag(Long.valueOf(adb.getlDateCheckIn()));
		        
		        	        
		        TextView tvDurRecord = (TextView) liContent.findViewById(R.id.textViewDuration);
		        tvDurRecord.setText(" ["+du.duration(adb.getlDateCheckIn(), adb.getlDateCheckOut())+"]");
		        // Passing subject id as parameter	     
		        tvDurRecord.setTag(adb.getsIdSubject());
		        


		        
		        llHistory.addView(mInflater.inflate(R.layout.tag_divider, llHistory, false), 1);
		        llHistory.addView(llCheckItemWrapper, 2);
				
				
				
				
			}
			

			
		}
		
	}
	
	
	public int getPosition(){
		return mPosition;
	}
	
	

	
}