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

import java.util.Date;

import org.ounl.lifelonglearninghub.nfclearntracker.R;
import org.ounl.lifelonglearninghub.nfclearntracker.db.charts.DurationChart;
import org.ounl.lifelonglearninghub.nfclearntracker.db.charts.LineChart;
import org.ounl.lifelonglearninghub.nfclearntracker.db.charts.StackedBarChart;
import org.ounl.lifelonglearninghub.nfclearntracker.db.tables.Goal;
import org.ounl.lifelonglearninghub.nfclearntracker.db.tables.Tag;
import org.ounl.lifelonglearninghub.nfclearntracker.fcube.config.FeedbackCubeConfig;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class ViewGoalActivity extends Activity {

	private String CLASSNAME = this.getClass().getSimpleName();
	
	TextView tvGoalName, tvGoalDesc, tvGoalMinsDailyTime, tvDateEndGoal;
	TextView tvTagId;
	LinearLayout llBanner;
	String sGoalId = "";
	String sTagId = "";
	int year, day, month;
	DatabaseHandler db;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_goal);

		Bundle extras = getIntent().getExtras();

		if (extras != null) {
			sGoalId = extras.getString(Goal.KEY_NAME);
		}

		loadGoalValues();

	}


	/**
	 * Fill in goals data
	 * 
	 */
	private void loadGoalValues() {

		DatabaseHandler dbresult = new DatabaseHandler(getApplicationContext());
		Goal g = dbresult.getGoal(sGoalId);		
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
		tvGoalDesc.setText(g.getsDesc());
		
		tvDateEndGoal = (TextView) findViewById(R.id.tvChallengeEndValue_ficha);
		tvDateEndGoal.setText(t.getFormattedValidityEnd());

		tvGoalMinsDailyTime = (TextView) findViewById(R.id.tvMins_ficha);
		org.ounl.lifelonglearninghub.nfclearntracker.DateUtils du = new org.ounl.lifelonglearninghub.nfclearntracker.DateUtils();
		tvGoalMinsDailyTime.setText(du.duration(g.getDailyTimeMills()));

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
	
//	// Power On
//	public void onClickOn(View v) {
//		Toast.makeText(ViewGoalActivity.this, "Starting feedback cube ...",
//				Toast.LENGTH_SHORT).show();
//		new FeedbackCubeManager().execute(FCOn.ACTION);
//	}
//
//	// Power Off
//	public void onClickOff(View v) {
//		Toast.makeText(ViewGoalActivity.this, "...closing feedback cube.",
//				Toast.LENGTH_SHORT).show();
//		new FeedbackCubeManager().execute(FCOff.ACTION);
//	}

//	// Light colours for feedback cube
//	public void onClick1zz(View v) {
//		//lightFeedbackCube("#C90808");
//		new FeedbackCubeManager().execute(FCColor.ACTION, "201", "8",  "8");
//	}
//
//	public void onClick2zz(View v) {
//		//lightFeedbackCube("#E35656");
//		new FeedbackCubeManager().execute(FCColor.ACTION, "227", "86",  "86");
//	}
//
//	public void onClick3zz(View v) {
//		//lightFeedbackCube("#E05904");
//		new FeedbackCubeManager().execute(FCColor.ACTION, "224", "89",  "4");
//	}
//
//	public void onClick4zz(View v) {
//		//lightFeedbackCube("#D97E45");
//		new FeedbackCubeManager().execute(FCColor.ACTION, "217", "126",  "69");
//	}
//
//	public void onClick5zz(View v) {
//		//lightFeedbackCube("#DBA481");
//		new FeedbackCubeManager().execute(FCColor.ACTION, "219", "164",  "129");
//	}
//
//	public void onClick6zz(View v) {
//		//lightFeedbackCube("#FAE500");
//		new FeedbackCubeManager().execute(FCColor.ACTION, "250", "229",  "0");
//	}
//
//	public void onClick7zz(View v) {
//		//lightFeedbackCube("#F7EB65");
//		new FeedbackCubeManager().execute(FCColor.ACTION, "247", "235",  "101");
//	}
//
//	public void onClick8zz(View v) {
//		//lightFeedbackCube("#FAF9B1");
//		new FeedbackCubeManager().execute(FCColor.ACTION, "250", "249",  "177");
//	}
//
//	public void onClick9zz(View v) {
//		//lightFeedbackCube("#CAEB78");
//		new FeedbackCubeManager().execute(FCColor.ACTION, "202", "235",  "120");
//	}
//
//	public void onClick10zz(View v) {
//		//lightFeedbackCube("#6B9403");
//		new FeedbackCubeManager().execute(FCColor.ACTION, "107", "148",  "3");
//	}
//	
//	public void onClickzzFade(View v) {
//		//lightFeedbackCube("#6B9403");
//		new FeedbackCubeManager().execute(FCFade.ACTION, "5", "20");
//	}	
//	
//	public void onClickzzBeep(View v) {
//		new FeedbackCubeManager().execute(FCBeep.ACTION);
//	}
//	
//	public void onClickzzRb(View v) {
//		new FeedbackCubeManager().execute(FCRainbow.ACTION);
//	}
//	
//	public void onClickzzRbCircle(View v) {
//		new FeedbackCubeManager().execute(FCRainbowCircle.ACTION);
//	}	
//	


}