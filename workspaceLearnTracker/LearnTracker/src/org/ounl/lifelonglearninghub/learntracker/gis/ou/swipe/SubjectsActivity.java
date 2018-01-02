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
import java.util.HashMap;
import java.util.List;

import org.ounl.lifelonglearninghub.learntracker.gis.ou.Constants;
import org.ounl.lifelonglearninghub.learntracker.gis.ou.db.ListViewSubjectsAdapter;
import org.ounl.lifelonglearninghub.learntracker.gis.ou.db.charts.CombiLineChart;
import org.ounl.lifelonglearninghub.learntracker.gis.ou.db.charts.DoughnutChart;
import org.ounl.lifelonglearninghub.learntracker.gis.ou.db.charts.YardLineChart;
import org.ounl.lifelonglearninghub.learntracker.gis.ou.db.charts.LineChart;
import org.ounl.lifelonglearninghub.learntracker.gis.ou.db.charts.PieChartActivity;
import org.ounl.lifelonglearninghub.learntracker.gis.ou.db.charts.ScatterChart;
import org.ounl.lifelonglearninghub.learntracker.gis.ou.db.charts.SocialLineChart;
import org.ounl.lifelonglearninghub.learntracker.gis.ou.db.charts.SocialStackedBarChart;
import org.ounl.lifelonglearninghub.learntracker.gis.ou.db.charts.StackedBarChart;
import org.ounl.lifelonglearninghub.learntracker.gis.ou.db.tables.SubjectDb;
import org.ounl.lifelonglearninghub.learntracker.gis.ou.session.Session;
import org.ounl.lifelonglearninghub.learntracker.gis.ou.R;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v4.app.TaskStackBuilder;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

/**
 * A fragment that launches other parts of the demo application.
 */
public class SubjectsActivity extends Activity {

	private ArrayList<HashMap> list;
	private ListView lview;
	private Intent intent;
	private ListViewSubjectsAdapter adapter;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.fragment_section_subjects);
		
        final ActionBar actionBar = getActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true); 
        
		intent = new Intent(this, TimeLineActivity.class);
		lview = (ListView) findViewById(R.id.listviewSubjects);

		lview.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

				intent.putExtra("POSITION", "" + position);
				startActivity(intent);


			}

		});

	}
	
	
	
	
	@Override	
	protected void onResume(){
		super.onResume();
		
		// Populate SQLlite list
		populateSubjectsFromLocal();

		adapter = new ListViewSubjectsAdapter(this,list);
		lview.setAdapter(adapter);
		
	}
	
	
	

	/**
	 * Populates list from local database
	 */
	private void populateSubjectsFromLocal() {
		list = new ArrayList<HashMap>();

		List<SubjectDb> lSubjectDb = Session.getSingleInstance().getDatabaseHandler().getSubjects();

		for (SubjectDb t : lSubjectDb) {

			// Records for data
			HashMap temp = new HashMap<String, String>();
			temp.put(SubjectDb.KEY_ID, t.getsId());
			temp.put(SubjectDb.KEY_TASK_DESC, t.getsTaskDesc());
			temp.put(SubjectDb.KEY_TASK_ALTERNATIVE_DESC, t.getsTaskAltDesc());
			temp.put(SubjectDb.KEY_DESC, t.getsDesc());
			temp.put(SubjectDb.KEY_TASK_TIME_DURATION, t.getlTaskTimeDuration());
			temp.put(SubjectDb.KEY_TASK_DATE_START, t.getlTaskDateStart());
			temp.put(SubjectDb.KEY_TASK_ORDER, t.getiTaskOrder());
		
			list.add(temp);
		}
	}
	
	

	
	public void onClickChart(View v){
		String sTag = (String)v.getTag();
		
		if(sTag.compareToIgnoreCase(getString(R.string.linechart)) == 0){
			LineChart chart = new LineChart();
			Intent intent = null;
			intent = chart.execute(this);
			startActivity(intent);
			
		}else if(sTag.compareToIgnoreCase(getString(R.string.stackedchart)) == 0){
			
			CombiLineChart barChart = new CombiLineChart();
			//SocialStackedBarChart barChart = new SocialStackedBarChart();
			Intent intent = null;
			intent = barChart.execute(this);
			startActivity(intent);
			
			
//			if(Session.getSingleInstance().getUserType() == Constants.USER_TYPE_SOCIAL){
//				// Social treatment
//				SocialLineChart barChart = new SocialLineChart();
//				//SocialStackedBarChart barChart = new SocialStackedBarChart();
//				Intent intent = null;
//				intent = barChart.execute(this);
//				startActivity(intent);				
//			}else{
//				// Yardstick treatment
//				YardLineChart barChart = new YardLineChart();
//				//StackedBarChart barChart = new StackedBarChart();
//				Intent intent = null;
//				intent = barChart.execute(this);
//				startActivity(intent);								
//			}

			
		}else if(sTag.compareToIgnoreCase(getString(R.string.piechart)) == 0){
			Intent intent = new Intent(this,PieChartActivity.class);
			startActivity(intent);
			
		}else if(sTag.compareToIgnoreCase(getString(R.string.doughnutchart)) == 0){
			DoughnutChart doughnutChart = new DoughnutChart();
			Intent intent = null;
			intent = doughnutChart.execute(this);
			startActivity(intent);			
			
		}else if(sTag.compareToIgnoreCase(getString(R.string.scatterchart)) == 0){			
			ScatterChart chart = new ScatterChart();
			Intent intent = null;
			intent = chart.execute(this);
			startActivity(intent);
			
			
		}else if(sTag.compareToIgnoreCase(getString(R.string.socialchart)) == 0){			
			SocialStackedBarChart barChart = new SocialStackedBarChart();
			Intent intent = null;
			intent = barChart.execute(this);
			startActivity(intent);					
			
		}
		
		
		
		
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
	
	

}