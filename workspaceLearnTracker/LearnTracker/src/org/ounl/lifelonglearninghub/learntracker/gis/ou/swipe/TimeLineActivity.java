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

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.ounl.lifelonglearninghub.learntracker.gis.ou.Constants;
import org.ounl.lifelonglearninghub.learntracker.gis.ou.db.tables.ActividadDb;
import org.ounl.lifelonglearninghub.learntracker.gis.ou.db.tables.SubjectDb;
import org.ounl.lifelonglearninghub.learntracker.gis.ou.db.ws.ActivityWSDeleteAsyncTask;
import org.ounl.lifelonglearninghub.learntracker.gis.ou.db.ws.ActivityWSPostAsyncTask;
import org.ounl.lifelonglearninghub.learntracker.gis.ou.db.ws.WSConstants;
import org.ounl.lifelonglearninghub.learntracker.gis.ou.db.ws.dataobjects.ActivityDO;
import org.ounl.lifelonglearninghub.learntracker.gis.ou.session.ActivitySession;
import org.ounl.lifelonglearninghub.learntracker.gis.ou.session.Session;
import org.ounl.lifelonglearninghub.learntracker.gis.ou.R;

import android.app.ActionBar;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.NavUtils;
import android.support.v4.app.TaskStackBuilder;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;


public class TimeLineActivity extends FragmentActivity {
	
	private String CLASSNAME = this.getClass().getName();

	private DayFragmentStatePagerAdapter mDemoCollectionPagerAdapter;
    private ViewPager mViewPager;
    private LinearLayout llListFragment;
    private LinearLayout llHistory; 
    private List<DayFragment> lDayFragments;

    private int iCurrentPos = 0;
    private long lCheckInToDelete = 0;
    private String sIdSbuject = "";
    private int iToRemove = -1;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_collection_demo);
        

        lDayFragments = new ArrayList<DayFragment>();
        
        Bundle extras = getIntent().getExtras();   
        if (extras != null) {
            String sPosition =  extras.getString("POSITION");
            Integer oiPosition = new Integer(sPosition);
            iCurrentPos = oiPosition.intValue();
        }                
        
        List<SubjectDb> lSubjectDb = Session.getSingleInstance().getDatabaseHandler().getSubjects();
        
        for (int i = 0; i < lSubjectDb.size(); i++) {
        	DayFragment dayF = new DayFragment();
        	
            Bundle args = new Bundle();
            args.putInt(DayFragment.ARG_POSITION, i);
            dayF.setArguments(args);
            
            lDayFragments.add(dayF);
            
			
		}
        
        
        // Navigation onOptionsItemSelected
        final ActionBar actionBar = getActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);        

        
    }
    
	@Override	
	protected void onResume(){
		super.onResume();
		Log.d(CLASSNAME, "onResume TimeLineActivity.");
		
		
        mDemoCollectionPagerAdapter = new DayFragmentStatePagerAdapter(getSupportFragmentManager(), lDayFragments);        
        mDemoCollectionPagerAdapter.notifyDataSetChanged();
        
        
        mViewPager = (ViewPager) findViewById(R.id.pager);
        mViewPager.setAdapter(mDemoCollectionPagerAdapter);
        mViewPager.setCurrentItem(iCurrentPos);

       		
	}
        

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                // This is called when the Home (Up) button is pressed in the action bar.
                // Create a simple intent that starts the hierarchical parent activity and
                // use NavUtils in the Support Package to ensure proper handling of Up.
                Intent upIntent = new Intent(this, SubjectsActivity.class);
                if (NavUtils.shouldUpRecreateTask(this, upIntent)) {
                    // This activity is not part of the application's task, so create a new task
                    // with a synthesized back stack.
                    TaskStackBuilder.from(this)
                            // If there are ancestor activities, they should be added here.
                            .addNextIntent(upIntent)
                            .startActivities();
                    finish();
                } else {
                    // This activity is part of the application's task, so simply
                    // navigate up to the hierarchical parent activity.
                    NavUtils.navigateUpTo(this, upIntent);
                }
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
    


	/**
	 * On click image view to start stop recording 
	 * 
	 * Records time SYNCHRONOUSLY
	 * 
	 * @param v
	 */
	public void onClickSwitchAction(View v){

		Drawable drwStart = getResources().getDrawable(R.drawable.play);
		Drawable drwStop = getResources().getDrawable(R.drawable.stop);
		
		LinearLayout lllFrag = (LinearLayout) v.getParent();			
		LinearLayout llTimePicker = (LinearLayout)lllFrag.findViewById(R.id.llTimePicker);
		LinearLayout llStatus = (LinearLayout)lllFrag.findViewById(R.id.llStatus);
		TextView tvRecording = (TextView) llStatus.findViewById(R.id.tvRecording);
		ImageView ivRecording = (ImageView) llStatus.findViewById(R.id.ivRecording);
		
		
		
		Animation anim = new AlphaAnimation(0.0f, 1.0f);
		
		Log.d(CLASSNAME, "BEFORE: "+Session.getSingleInstance().getActivity(mViewPager.getCurrentItem()).toString());
		
		ImageView iv = (ImageView)v;
		int currentStatus = Session.getSingleInstance().getActivity(mViewPager.getCurrentItem()).getStatus();
		
		if (currentStatus == ActivitySession.STATUS_STOPPED){
			Log.d(CLASSNAME, "Status is STOPPED. Shift to STARTED");
			iv.setImageDrawable(drwStop);
			llTimePicker.setVisibility(View.INVISIBLE);			

			anim.setDuration(500);
			anim.setStartOffset(20);
			anim.setRepeatMode(Animation.REVERSE);
			anim.setRepeatCount(Animation.INFINITE);
			
			Session.getSingleInstance().getActivity(mViewPager.getCurrentItem()).doCheckIn();			
			
		}else{
			Log.d(CLASSNAME, "Status is STARTED. Shift to STOPPED");
			iv.setImageDrawable(drwStart);		    
		    llTimePicker.setVisibility(View.VISIBLE);
			anim.setRepeatCount(0);
			
			ActivitySession as = Session.getSingleInstance().getActivity(mViewPager.getCurrentItem());
			
			String sSubjectId = as.getId_subject();
			double dLat = as.getLocation_latitude();
			double dLong = as.getLocation_longitude();
			long lCheckOut = new Date().getTime();
			long lCheckIn  =  Session.getSingleInstance().getActivity(mViewPager.getCurrentItem()).doCheckOut();			
			
			// Save data into both databases
			DateUtils du = new DateUtils();
			Log.i(CLASSNAME, "Recording activity into both databasees:"+as.getId_subject()+" / "+du.duration(lCheckIn, lCheckOut));
			
			// TODO make some control here to make this transactional
			// Save in backend
			recordActivityBackend(sSubjectId, lCheckIn,  lCheckOut, dLat,  dLong, ActivityDO.ACTIVITY_RECORD_MODE_SYNCHRONOUS);
			// Save in local database
			recordActivitySQLite(sSubjectId, lCheckIn, lCheckOut, dLat, dLong);
			
			TextView tvDuration = (TextView) lllFrag.findViewById(R.id.tvDuration);
			tvDuration.setText(du.duration(Session.getSingleInstance().getDatabaseHandler().getAccumulatedTime(sSubjectId)));
			
			Toast.makeText(getApplicationContext(), "Recorded " + du.duration(lCheckIn, lCheckOut), Toast.LENGTH_SHORT).show();
			


			//
			// Update history layout
			//
			
	        LayoutInflater inflater = LayoutInflater.from(this);
	        LinearLayout llParent = (LinearLayout) inflater.inflate(R.layout.check_item, null);
	        LinearLayout liContent = (LinearLayout) llParent.getChildAt(0);
	                     
//	        ImageView ivParent = (ImageView) liContent.findViewById(R.id.imageViewStart);
//	        ivParent.setImageResource(R.drawable.rec_50x);
	        

	        //TextView tvTimeStamp = (TextView) liContent.getChildAt(1);    
	        TextView tvTimeStamp = (TextView) liContent.findViewById(R.id.textViewTimeStamp);
	        tvTimeStamp.setText(Constants.TIME_FORMAT.format(lCheckIn));
	        tvTimeStamp.setTag(Long.valueOf(lCheckIn));	        
	        	        
	        TextView tvDurRecord = (TextView) liContent.findViewById(R.id.textViewDuration);
	        tvDurRecord.setText(" ["+du.duration(lCheckIn, lCheckOut)+"]");	     
	        // Passing subject id as parameter	     
	        tvDurRecord.setTag(sSubjectId);

	        LinearLayout llHistory = (LinearLayout)lllFrag.findViewById(R.id.llHistory);
	        // Set index number so that the record can be removed
	        int iTag = (llHistory.getChildCount()-1)/2;
	        llParent.setTag(iTag);
	        
	        llHistory.addView(inflater.inflate(R.layout.tag_divider, llHistory, false), 1);
	        llHistory.addView(llParent, 2);
	        

	        
			
		}
		tvRecording.startAnimation(anim);
		ivRecording.startAnimation(anim);
		
		Log.d(CLASSNAME, "AFTER: "+Session.getSingleInstance().getActivity(mViewPager.getCurrentItem()).toString());

		



        
}	
	
	/**
	 * Recording time ASYNCHRONOUSLY
	 * 
	 * @param v
	 */
	public void onClickRecord(View v){
		
		LinearLayout llTimePicker = (LinearLayout) v.getParent();
		LinearLayout lllFrag = (LinearLayout) llTimePicker.getParent();
		LinearLayout llTime = (LinearLayout)llTimePicker.findViewById(R.id.llTimePicker);
		
		
		
		
		
		TimePicker tp = (TimePicker) llTime.findViewById(R.id.tpTask);
		Integer oiHour = tp.getCurrentHour();
		Integer oiMin = tp.getCurrentMinute();
		DateUtils du = new DateUtils();
		long lmills = du.toMills(oiHour, oiMin);


		
		ActivitySession as = Session.getSingleInstance().getActivity(mViewPager.getCurrentItem());
		String sSubjectId = as.getId_subject();
		double dLat = as.getLocation_latitude();
		double dLong = as.getLocation_longitude();
		long lCheckIn = new Date().getTime();
		long lCheckOut  =  lCheckIn + lmills;			
		
		// Save data into both databases
		Log.i(CLASSNAME, "Recording activity into both databasees:"+as.getId_subject()+" / "+du.duration(lCheckIn, lCheckOut));
		
		// TODO make some control here to make this transactional
		recordActivityBackend(sSubjectId, lCheckIn,  lCheckOut, dLat,  dLong, ActivityDO.ACTIVITY_RECORD_MODE_ASYNCHRONOUS);
		recordActivitySQLite(sSubjectId, lCheckIn, lCheckOut, dLat, dLong);		
		
		TextView tvDuration = (TextView) lllFrag.findViewById(R.id.tvDuration);
		tvDuration.setText(du.duration(Session.getSingleInstance().getDatabaseHandler().getAccumulatedTime(sSubjectId)));
		
		
		
		//
		// Update history layout
		//		
        LayoutInflater inflater = LayoutInflater.from(this);
        
        LinearLayout llParent = (LinearLayout) inflater.inflate(R.layout.check_item, null);
        
        LinearLayout liContent = (LinearLayout) llParent.getChildAt(0);                     

        TextView tvTimeStamp = (TextView) liContent.findViewById(R.id.textViewTimeStamp);
        tvTimeStamp.setText(Constants.TIME_FORMAT.format(lCheckIn));
        tvTimeStamp.setTag(Long.valueOf(lCheckIn));
        
        	        
        TextView tvDurRecord = (TextView) liContent.findViewById(R.id.textViewDuration);
        tvDurRecord.setText(" ["+du.duration(lCheckIn, lCheckOut)+"]");
        // Passing subject id as parameter	     
        tvDurRecord.setTag(sSubjectId);


        LinearLayout llHistory = (LinearLayout)lllFrag.findViewById(R.id.llHistory);
        // Set index number so that the record can be removed
        int iTag = (llHistory.getChildCount()-1)/2;
        llParent.setTag(iTag);        
        llHistory.addView(inflater.inflate(R.layout.tag_divider, llHistory, false), 1);
        llHistory.addView(llParent, 2);		
		
		
		
		Toast.makeText(getApplicationContext(), "Recorded " + du.duration(lCheckIn, lCheckOut), Toast.LENGTH_SHORT).show();
				
		
	}
	
	/**
	 * Removes record both from sqlite and backend
	 * 
	 * @param v
	 */
	public void onClickDeleteActivity(View v){

		LinearLayout llCheckItemRow = (LinearLayout) v.getParent();
		LinearLayout llCheckItemWrapper = (LinearLayout) llCheckItemRow.getParent();
		int iNumItemsHistory = 0;
		int iTag = 0;
		int iClicked = 0;
		
		
		try{

			llHistory = (LinearLayout) llCheckItemWrapper.getParent();
			
			llListFragment = (LinearLayout) llHistory.getParent();
			
			iNumItemsHistory = ((llHistory.getChildCount()-1)/2)-1;
			iTag = new Integer(llCheckItemWrapper.getTag().toString());
			iClicked = (iTag - iNumItemsHistory)* (-1);
			iToRemove = (iClicked+1)*2;
			
		}catch(Exception e){
			e.printStackTrace();
		}

		
		
		TextView myText = (TextView) llCheckItemRow.findViewById(R.id.textViewTimeStamp);				
		lCheckInToDelete = (Long) myText.getTag();
		String sCheckIn = (String)myText.getText();
		TextView tvDur = (TextView) llCheckItemRow.findViewById(R.id.textViewDuration);				
		sIdSbuject = (String) tvDur.getTag();
		
		
		
		new AlertDialog.Builder(this)
        .setIcon(android.R.drawable.ic_dialog_alert)
        .setTitle("Remove activity?")
        .setMessage("Are you sure to remove activity started at "+sCheckIn+" ?")
        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
            	
        		//Toast.makeText(getApplicationContext(), "About to delete activity "+sCheckIn+" ... ", Toast.LENGTH_SHORT).show();		
        		Log.d(CLASSNAME, "About to delte activity   mills["+lCheckInToDelete+"]");
        		
        		
        		
        		// Delete this transactional
        		// Issue 16
        		// https://code.google.com/p/lifelong-learning-hub/issues/detail?id=16		
        		deleteActivityBackend(lCheckInToDelete, Session.getSingleInstance().getUserName());
        		Session.getSingleInstance().getDatabaseHandler().deleteActivity(lCheckInToDelete);
        		

        		DateUtils du = new DateUtils();

    			TextView tvDuration = (TextView) llListFragment.findViewById(R.id.tvDuration);
    			tvDuration.setText(du.duration(Session.getSingleInstance().getDatabaseHandler().getAccumulatedTime(sIdSbuject)));
    			
    			llHistory.getChildAt(iToRemove).setVisibility(View.GONE);

    			
        		
        		
        		
                //Stop the activity
                //TimeLineActivity.this.finish();    
            }

        })
        .setNegativeButton("No", null)
        .show();		
				

		
	}
	

	
	
	
	/**
	 * Method used to check-in a new activity into backend
	 * 
	 * @return
	 */
	private int recordActivityBackend(String sIdSubject, long lcheckin, long lcheckout, double latitude, double longitude, int mode) {

		int result = WSConstants.POST_RESPONSE_OK;

		ActivityWSPostAsyncTask wsat = new ActivityWSPostAsyncTask();
		ActivityDO a = new ActivityDO(Session.getSingleInstance().getUserName(), sIdSubject, lcheckin, lcheckout, longitude, latitude, mode);
		
		//  TODO cannot insert longitude "50.883333", and latitude "5.983333"
		// Da este pete com.google.appengine.repackaged.org.codehaus.jackson.map.JsonMappingException: 
		// Can not construct instance of int from String value '50.883333': not a valid Integer value at [Source: N/A; line: -1, column: -1] (through reference chain: org.ounl.lifelonglearninghub.db.Activity["activity_location_latitude"])

		try {
			wsat.execute(a);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			result = WSConstants.POST_RESPONSE_KO;
		}

		return result;
	}	
	
	
	/**
	 * Save activity into SQLite
	 * @return
	 */
	private int recordActivitySQLite(String sIdSubject, long lcheckin, long lcheckout, double latitude, double longitude){
		
		ActividadDb aDb = new ActividadDb(sIdSubject, lcheckin, lcheckout, longitude, latitude);
		Session.getSingleInstance().getDatabaseHandler().addActivity(aDb);
		
		return 0;
	}
	
	/**
	 * Delete activity in backend for a given checkin and user
	 * 
	 * @param lcheckin check in date in milliseconds
	 * @param sUser user id
	 * @return
	 */
	private int deleteActivityBackend(long lcheckin, String sUser) {

		int result = WSConstants.POST_RESPONSE_OK;

		ActivityWSDeleteAsyncTask wsat = new ActivityWSDeleteAsyncTask();
		try {
			wsat.execute(lcheckin+"", sUser);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			result = WSConstants.POST_RESPONSE_KO;
		}

		return result;
	}	


}
