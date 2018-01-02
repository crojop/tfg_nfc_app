/*******************************************************************************
 * Copyright (C) 2015 Open University of The Netherlands
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
package org.ounl.lifelonglearninghub.nfcecology.db;

import java.util.Date;

import org.ounl.lifelonglearninghub.nfcecology.db.charts.DurationChart;
import org.ounl.lifelonglearninghub.nfcecology.db.charts.LineChart;
import org.ounl.lifelonglearninghub.nfcecology.db.charts.StackedBarChart;
import org.ounl.lifelonglearninghub.nfcecology.db.tables.Goal;
import org.ounl.lifelonglearninghub.nfcecology.db.tables.Tag;
import org.ounl.lifelonglearninghub.nfcecology.fcube.config.FeedbackCubeConfig;
import org.ounl.lifelonglearninghub.nfcecology.swipe.SwipeActivity;
import org.ounl.lifelonglearninghub.nfcecology.MainActivity;
import org.ounl.lifelonglearninghub.nfcecology.R;

import com.estimote.examples.demos.AllDemosActivity;
import com.estimote.examples.demos.DistanceBeaconActivity;
import com.estimote.examples.demos.ListBeaconsActivity;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v4.app.TaskStackBuilder;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class ViewGoalActivity extends Activity {

	private String CLASSNAME = this.getClass().getSimpleName();
	
	TextView tvGoalName, tvGoalDesc, tvGoalMinsDailyTime, tvDateEndGoal, tvTagId;
	EditText etMinsFicha;
	LinearLayout llBanner;
	String sGoalId = "";
	String sTagId = "";
	int year, day, month;
	DatabaseHandler db;
	Goal currentGoal;
	private Intent intent;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_goal);

		Bundle extras = getIntent().getExtras();

		if (extras != null) {
			sGoalId = extras.getString(Goal.KEY_NAME);
		}

		loadGoalValues();
		

		// Enable home button
		final ActionBar actionBar = getActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true); 
	

	}


	/**
	 * Fill in goals data
	 * 
	 */
	private void loadGoalValues() {

		DatabaseHandler dbresult = new DatabaseHandler(getApplicationContext());
		currentGoal = dbresult.getGoal(sGoalId);		
		Tag t = dbresult.getTag(sGoalId);
		
		llBanner = (LinearLayout)findViewById(R.id.llBannerViewGoal);
		llBanner.setBackgroundColor(Color.parseColor("#"+t.getColor()));
		
		tvTagId = (TextView) findViewById(R.id.tvNFCTag_ficha);
		sTagId = t.getsIdTag();
		Log.e(CLASSNAME, t.toString());

		
		tvTagId.setText(sTagId);
		

		tvGoalName = (TextView) findViewById(R.id.tvGoalDescValue_ficha);
		tvGoalName.setText(sGoalId);

		tvGoalDesc = (TextView) findViewById(R.id.tvGoalDescription_ficha);
		tvGoalDesc.setText(currentGoal.getsDesc());
		
		tvDateEndGoal = (TextView) findViewById(R.id.tvChallengeEndValue_ficha);
		tvDateEndGoal.setText(t.getFormattedValidityEnd());

		tvGoalMinsDailyTime = (TextView) findViewById(R.id.tvMins_ficha);
		org.ounl.lifelonglearninghub.nfcecology.DateUtils du = new org.ounl.lifelonglearninghub.nfcecology.DateUtils();
		tvGoalMinsDailyTime.setText(du.duration(currentGoal.getDailyTimeMills()));
		
		etMinsFicha = (EditText) findViewById(R.id.etMins_ficha);
		etMinsFicha.setText(""+du.toMins(currentGoal.getDailyTimeMills()));
		

	}

	public void onClickStackedBarChart(View v) {

		StackedBarChart barChart = new StackedBarChart();
		Intent intent = null;
		intent = barChart.execute(this, sTagId);
		intent.putExtra(Goal.KEY_NAME, sGoalId);
		intent.putExtra(Tag.KEY_ID_TAG, sTagId);
		startActivity(intent);

	}

	public void onClickSpotChart(View v) {

		LineChart lineChart = new LineChart();
		Intent intent = null;
		intent = lineChart.execute(this);
		intent.putExtra(Goal.KEY_NAME, sGoalId);
		startActivity(intent);

	}

	public void onClickDurationBarChart(View v) {
		
		
		DurationChart chart = new DurationChart();
		Intent intent = null;
		intent = chart.execute(this);
		intent.putExtra(Goal.KEY_NAME, sGoalId);
		startActivity(intent);		

	}
	
	public void onClickUpdate(View v) {
		
		etMinsFicha = (EditText) findViewById(R.id.etMins_ficha);
		String sMins = etMinsFicha.getText().toString();
		db = new DatabaseHandler(getApplicationContext());
		db.updateGoal(currentGoal, sMins);
		
		this.finish();

	}	

	// Power Set IP
	public void onClickOnSetIp(View v) {
		

			LayoutInflater li = LayoutInflater.from(this);
			View promptsView = li.inflate(R.layout.prompts, null);
			AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
			alertDialogBuilder.setView(promptsView);

			TextView tvPrompt = (TextView) promptsView
					.findViewById(R.id.textViewPrompt);
			tvPrompt.setText("Ip Address: ");
			final EditText userInput = (EditText) promptsView.findViewById(R.id.editTextPrompt);
			if (FeedbackCubeConfig.getSingleInstance().getIp() != null) {
				userInput.setText(FeedbackCubeConfig.getSingleInstance().getIp());
			}

			alertDialogBuilder.setCancelable(false).setPositiveButton(
					"Save", new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int id) {
							FeedbackCubeConfig.getSingleInstance().setIp(userInput.getText().toString());
							dialog.dismiss();

						}
					});

			AlertDialog alertDialog = alertDialogBuilder.create();
			alertDialog.show();
		
		
		
	}	
	
	@Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                // This is called when the Home (Up) button is pressed in the action bar.
                // Create a simple intent that starts the hierarchical parent activity and
                // use NavUtils in the Support Package to ensure proper handling of Up.
                Intent upIntent = new Intent(this, SwipeActivity.class);
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



}