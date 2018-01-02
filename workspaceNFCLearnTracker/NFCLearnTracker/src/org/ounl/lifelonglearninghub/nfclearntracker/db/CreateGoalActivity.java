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
package org.ounl.lifelonglearninghub.nfclearntracker.db;

import java.util.Calendar;
import java.util.Date;

import org.ounl.lifelonglearninghub.nfclearntracker.MainActivity;
import org.ounl.lifelonglearninghub.nfclearntracker.R;
import org.ounl.lifelonglearninghub.nfclearntracker.TagColours;
import org.ounl.lifelonglearninghub.nfclearntracker.db.tables.Goal;
import org.ounl.lifelonglearninghub.nfclearntracker.db.tables.Tag;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;


public class CreateGoalActivity extends Activity {

	// Controls
	EditText etGoalName, etGoalDesc, etGoalDailyTime;
	TextView tvTagId;
	DatePicker dpResult;
	LinearLayout llBanner;
	
	
	// Values
	String sName, sDesc, sDailyTime;
	String sTagId = "The NFC tag could not be read";
	String sColor = "";
	int year, day, month;
	DatabaseHandler db;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_create_goal);
		
		
        Bundle extras = getIntent().getExtras();
        
        if (extras != null) {
        	sTagId = extras.getString(Tag.KEY_ID_TAG);
        	sColor = extras.getString(TagColours.TAG_COLOR);
        }       

		etGoalName = (EditText) findViewById(R.id.etGoalName);
		etGoalDesc = (EditText) findViewById(R.id.etGoalDesc);
		etGoalDailyTime = (EditText) findViewById(R.id.etGoalTimeDaily);
		tvTagId = (TextView) findViewById(R.id.tvNFCTag);
		tvTagId.setText(sTagId);
		
		llBanner = (LinearLayout) findViewById(R.id.llBannerCreateGoal);
		llBanner.setBackgroundColor(Color.parseColor("#"+sColor));		
		
		setCurrentDateOnView();

		
	}
	
	
	public void onClickSave(View v){
		sName = etGoalName.getText().toString();
		sDesc = etGoalDesc.getText().toString();
		sDailyTime = etGoalDailyTime.getText().toString();
		Date d = new Date();

		// Minutes to mills conversion
		Integer oI = Integer.valueOf(sDailyTime);
		Long oL = Long.valueOf(oI.intValue() * 60000);

		db = new DatabaseHandler(getApplicationContext());
		db.addGoal(new Goal(sName, sDesc, oL.longValue()));
		

		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.DAY_OF_MONTH, dpResult.getDayOfMonth());
		cal.set(Calendar.MONTH, dpResult.getMonth());
		cal.set(Calendar.YEAR, dpResult.getYear());
		
		Tag t = new Tag(sName, sTagId, d.getTime(),cal.getTime().getTime(),sColor);
		
		db.addTag(t);
		Toast.makeText(getApplicationContext(),
				"Goal "+sName+" successfully bound to tag "+sTagId+"!", Toast.LENGTH_LONG)
				.show();
		
    	Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
		
	}
	
	/**
	 * Display current date in date picker
	 * 
	 */
	public void setCurrentDateOnView() {
		 

		dpResult = (DatePicker) findViewById(R.id.dpChallengeEnd);
 
		final Calendar c = Calendar.getInstance();
		year = c.get(Calendar.YEAR);
		month = c.get(Calendar.MONTH) + 1;
		day = c.get(Calendar.DAY_OF_MONTH);
 

		// set current date into datepicker
		dpResult.init(year, month, day, null);
 
	}
	

}