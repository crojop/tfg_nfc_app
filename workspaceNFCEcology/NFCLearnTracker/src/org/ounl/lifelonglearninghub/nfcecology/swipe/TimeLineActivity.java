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
package org.ounl.lifelonglearninghub.nfcecology.swipe;

import java.util.ArrayList;
import java.util.List;

import org.ounl.lifelonglearninghub.nfcecology.db.DatabaseHandler;
import org.ounl.lifelonglearninghub.nfcecology.db.charts.LineChart;
import org.ounl.lifelonglearninghub.nfcecology.db.charts.StackedBarChart;
import org.ounl.lifelonglearninghub.nfcecology.db.tables.Goal;
import org.ounl.lifelonglearninghub.nfcecology.db.tables.Tagctivity;
import org.ounl.lifelonglearninghub.nfcecology.R;

import android.app.ActionBar;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.NavUtils;
import android.support.v4.app.TaskStackBuilder;
import android.support.v4.view.ViewPager;
import android.view.MenuItem;
import android.view.View;


public class TimeLineActivity extends FragmentActivity {

    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide fragments representing
     * each object in a collection. We use a {@link android.support.v4.app.FragmentStatePagerAdapter}
     * derivative, which will destroy and re-create fragments as needed, saving and restoring their
     * state in the process. This is important to conserve memory and is a best practice when
     * allowing navigation between objects in a potentially large collection.
     */
	DayFragmentStatePagerAdapter mDemoCollectionPagerAdapter;

    /**
     * The {@link android.support.v4.view.ViewPager} that will display the object collection.
     */
    ViewPager mViewPager;
    
    
    private List<Tagctivity> list;
    private String sGoalId = "";

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_collection_demo);
        
        Bundle extras = getIntent().getExtras();

        if (extras != null) {
            sGoalId = extras.getString(Goal.KEY_NAME);
        }        
        
        
        System.out.println(" RECIBIDO GOAL ID "+sGoalId);
        		
        // BTB HACER CONSULTA DEL TAG_ID PARA ESE GOAL_ID
        // BTB HACER CONSULTA DE ACTIVITIES PARA ESE TAG ID
        // BTB AGRUPAR DATOS POR DIA EN SUMA DE MINUTOS EN UN ARRAY
        // Ahora le estas pasando los tagactivity a cholon
        // EL ARRAY SE LO PASAS EN DayFragmentStatePagerAdapter(getSupportFragmentManager());
        
        list = new ArrayList<Tagctivity>();
        populateList();
        
        
        		

        // Create an adapter that when requested, will return a fragment representing an object in
        // the collection.
        // 
        // ViewPager and its adapters use support library fragments, so we must use
        // getSupportFragmentManager.
        mDemoCollectionPagerAdapter = new DayFragmentStatePagerAdapter(getSupportFragmentManager(), list);

        // Set up action bar.
        final ActionBar actionBar = getActionBar();

        // Specify that the Home button should show an "Up" caret, indicating that touching the
        // button will take the user one step up in the application's hierarchy.
        actionBar.setDisplayHomeAsUpEnabled(true);

        // Set up the ViewPager, attaching the adapter.
        mViewPager = (ViewPager) findViewById(R.id.pager);
        mViewPager.setAdapter(mDemoCollectionPagerAdapter);
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
    
    private void populateList(){
    	    	
		DatabaseHandler dbresult = new DatabaseHandler(getApplicationContext());		
		String sTagId = dbresult.getTagId(sGoalId);
		list = dbresult.getActivities(sTagId);
    	
    }
        
	public void onClickBarChart(View v){
//		StackedBarChart barChart = new StackedBarChart();
//		Intent intent = null;
//		intent = barChart.execute(this);
//		startActivity(intent);		
		
	}    

	public void onClickLineChart(View v){
//		LineChart lineChart = new LineChart();
//		Intent intent = null;
//		intent = lineChart.execute(this);
//		startActivity(intent);		
		
	}	

}
