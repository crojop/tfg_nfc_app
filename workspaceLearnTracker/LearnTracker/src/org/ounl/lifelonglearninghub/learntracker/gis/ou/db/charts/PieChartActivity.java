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
package org.ounl.lifelonglearninghub.learntracker.gis.ou.db.charts;

import java.util.HashMap;
import java.util.List;

import org.achartengine.ChartFactory;
import org.achartengine.GraphicalView;
import org.achartengine.model.CategorySeries;
import org.achartengine.model.SeriesSelection;
import org.achartengine.renderer.DefaultRenderer;
import org.achartengine.renderer.SimpleSeriesRenderer;
import org.ounl.lifelonglearninghub.learntracker.gis.ou.Constants;
import org.ounl.lifelonglearninghub.learntracker.gis.ou.session.ActivitySession;
import org.ounl.lifelonglearninghub.learntracker.gis.ou.session.Session;
import org.ounl.lifelonglearninghub.learntracker.gis.ou.swipe.DateUtils;
import org.ounl.lifelonglearninghub.learntracker.gis.ou.swipe.SubjectsActivity;
import org.ounl.lifelonglearninghub.learntracker.gis.ou.R;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v4.app.TaskStackBuilder;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.Toast;

public class PieChartActivity extends Activity {
	
	
	private String CLASSNAME = this.getClass().getName();

		private static int[] COLORS = new int[] { 
        	Color.parseColor(ChartUtils.COLOUR_A),
        	Color.parseColor(ChartUtils.COLOUR_B),
        	Color.parseColor(ChartUtils.COLOUR_C),
        	Color.parseColor(ChartUtils.COLOUR_D),
        	Color.parseColor(ChartUtils.COLOUR_E),
        	Color.parseColor(ChartUtils.COLOUR_F),
        	Color.parseColor(ChartUtils.COLOUR_G),
        	Color.parseColor(ChartUtils.COLOUR_H),
        	Color.parseColor(ChartUtils.COLOUR_I),
        	Color.parseColor(ChartUtils.COLOUR_K),
        	Color.parseColor(ChartUtils.COLOUR_L),        	
        	Color.parseColor(ChartUtils.COLOUR_M),
        	Color.parseColor(ChartUtils.COLOUR_N),
        	Color.parseColor(ChartUtils.COLOUR_O),
        	Color.parseColor(ChartUtils.COLOUR_P),
        	Color.parseColor(ChartUtils.COLOUR_Q),
        	Color.parseColor(ChartUtils.COLOUR_R),
        	Color.parseColor(ChartUtils.COLOUR_S),
        	Color.parseColor(ChartUtils.COLOUR_T),
        	Color.parseColor(ChartUtils.COLOUR_U),
        	Color.parseColor(ChartUtils.COLOUR_V),
        	Color.parseColor(ChartUtils.COLOUR_W),
        	Color.parseColor(ChartUtils.COLOUR_X),
        	Color.parseColor(ChartUtils.COLOUR_Y),
        	Color.parseColor(ChartUtils.COLOUR_Z)
        	
//        	Color.parseColor("#D97E45"),
//        	Color.parseColor("#DBA481"), 
//        	Color.parseColor("#CCC8C6") 
        	};
		
	

        //private static double[] VALUES = new double[] { 10, 11, 12, 13 };
        private static double[] VALUES ;

        private static String[] NAME_LIST;
        //private static String[] NAME_LIST = new String[] { "A", "B", "C", "D" };

        private CategorySeries mSeries = new CategorySeries("");

        private DefaultRenderer renderer = new DefaultRenderer();

        private GraphicalView mChartView;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
                super.onCreate(savedInstanceState);
                setContentView(R.layout.piechart);
                
                final ActionBar actionBar = getActionBar();
                actionBar.setDisplayHomeAsUpEnabled(true); 
                
                populateChart();
                applySize();

                renderer.setApplyBackgroundColor(true);
                //mRenderer.setBackgroundColor(Color.argb(100, 50, 50, 50));
                renderer.setBackgroundColor(Color.WHITE);
                //renderer.setChartTitleTextSize(50f);
                renderer.setChartTitle("Overall distribution of time by assignment");
                //renderer.setLabelsTextSize(30f);
                renderer.setLabelsColor(Color.BLACK);
                //renderer.setLegendTextSize(30f);
                renderer.setMargins(new int[] { 20, 30, 15, 0 });
                renderer.setZoomButtonsVisible(true);
                renderer.setStartAngle(90);
                
                // Retrieve values from database
                HashMap<Integer, Long> hmIL = Session.getSingleInstance().getDatabaseHandler().getSubjectsAccumulated();
                List<ActivitySession> las = Session.getSingleInstance().getActivities();
                
                NAME_LIST  = new String[las.size()];
                VALUES  = new double[las.size()];
                
                int iNumRecords = 0; 
                for (int i = 0; i < las.size(); i++) {
 

                	Long LAccum = hmIL.get(new Integer(i));
                	if (LAccum == null){
                		LAccum = 0L;
                	}
                	if(LAccum > 0){
                    	NAME_LIST[i] = las.get(i).getSubject_task_desc();
                    	VALUES[i]= LAccum.longValue();
                    	mSeries.add(NAME_LIST[i], VALUES[i]);
                    	SimpleSeriesRenderer ssr = new SimpleSeriesRenderer();
                        ssr.setColor(COLORS[(mSeries.getItemCount() - 1) % COLORS.length]);
                        renderer.addSeriesRenderer(ssr);
                        Log.d(CLASSNAME,  i+ " estimated "+NAME_LIST[i]+" / accomplished "+VALUES[i]);
                        iNumRecords++;
                	}
            		
            		
            	}
                
                if(iNumRecords == 0){
                	Toast.makeText( PieChartActivity.this,                                                           
                            "The pie chart is empty because you have not recorded any activity yet.",
                            Toast.LENGTH_LONG).show();                	
                }
                
                if (mChartView != null) {
                        mChartView.repaint();
                }

        }
        
        
	private void applySize() {
		switch (getResources().getDisplayMetrics().densityDpi) {
		case Constants.DENSITY_XXHIGH:
			renderer.setMargins(new int[] { 50, 130, 30, 10 });
			// renderer.setAxisTitleTextSize(Constants.TEXT_SIZE_XXHDPI);
			renderer.setChartTitleTextSize(Constants.TEXT_SIZE_XXXHDPI);
			renderer.setLabelsTextSize(Constants.TEXT_SIZE_XXHDPI);
			renderer.setLegendTextSize(Constants.TEXT_SIZE_XXHDPI);
			break;		
		case DisplayMetrics.DENSITY_XHIGH:
			renderer.setMargins(new int[] { 40, 90, 25, 10 });
			// renderer.setAxisTitleTextSize(Constants.TEXT_SIZE_XHDPI);
			renderer.setChartTitleTextSize(Constants.TEXT_SIZE_XHDPI);
			renderer.setLabelsTextSize(Constants.TEXT_SIZE_XHDPI);
			renderer.setLegendTextSize(Constants.TEXT_SIZE_XHDPI);
			break;
		case DisplayMetrics.DENSITY_HIGH:
			renderer.setMargins(new int[] { 30, 50, 20, 10 });
			// renderer.setAxisTitleTextSize(Constants.TEXT_SIZE_HDPI);
			renderer.setChartTitleTextSize(Constants.TEXT_SIZE_HDPI);
			renderer.setLabelsTextSize(Constants.TEXT_SIZE_HDPI);
			renderer.setLegendTextSize(Constants.TEXT_SIZE_HDPI);
			break;
		default:
			renderer.setMargins(new int[] { 30, 50, 20, 10 });
			// renderer.setAxisTitleTextSize(Constants.TEXT_SIZE_LDPI);
			renderer.setChartTitleTextSize(Constants.TEXT_SIZE_LDPI);
			renderer.setLabelsTextSize(Constants.TEXT_SIZE_LDPI);
			renderer.setLegendTextSize(Constants.TEXT_SIZE_LDPI);
			break;
		}

	}
        

        @Override
        protected void onResume() {
                super.onResume();
                if (mChartView == null) {
                        LinearLayout layout = (LinearLayout) findViewById(R.id.chart);
                        mChartView = ChartFactory.getPieChartView(this, mSeries, renderer);
                        renderer.setClickEnabled(true);
                        renderer.setSelectableBuffer(10);

                        mChartView.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                        SeriesSelection seriesSelection = mChartView
                                                        .getCurrentSeriesAndPoint();

                                        if (seriesSelection == null) {
// Temporary commented by btb                                        	
//                                                Toast.makeText(PieChartActivity.this,
//                                                                "No chart element was clicked",
//                                                                Toast.LENGTH_SHORT).show();
                                        } else {
                                        	long lo = (long) seriesSelection.getValue();
                                        	DateUtils du = new DateUtils();

                                        	
                                        	
                                            Toast.makeText( PieChartActivity.this,                                                           
                                                            " You have invested " + du.duration(lo) + " on '" + Session.getSingleInstance().getActivity(seriesSelection.getPointIndex()).getSubject_task_desc() + "'",
                                                            Toast.LENGTH_LONG).show();                                        	
// Temporary commented by btb                                        	
//                                                Toast.makeText(
//                                                                PieChartActivity.this,
//                                                                "Chart element data point index "
//                                                                                + (seriesSelection.getPointIndex() + 1)
//                                                                                + " was clicked" + " point value="
//                                                                                + du.duration(lo),
//                                                                Toast.LENGTH_LONG).show();
                                        }
                                }
                        });

                        mChartView.setOnLongClickListener(new View.OnLongClickListener() {
                                @Override
                                public boolean onLongClick(View v) {
                                        SeriesSelection seriesSelection = mChartView
                                                        .getCurrentSeriesAndPoint();
                                        if (seriesSelection == null) {
// Temporary commented by btb
//                                                Toast.makeText(PieChartActivity.this,
//                                                                "No chart element was long pressed",
//                                                                Toast.LENGTH_SHORT);
                                                return false;
                                        } else {
// Temporary commented by btb                                        	
//                                                Toast.makeText(PieChartActivity.this,
//                                                                "Chart element data point index "
//                                                                                + seriesSelection.getPointIndex()
//                                                                                + " was long pressed",
//                                                                Toast.LENGTH_SHORT);
                                                return true;
                                        }
                                }
                        });
                        layout.addView(mChartView, new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT));
                } else {
                        mChartView.repaint();
                }
        }
        
        /**
         * Gets data from database to populate the chart
         * 
         */
        private void populateChart() {
                
//                DatabaseHandler dbresult = new DatabaseHandler(this);
//                HashMap<String, Long> mp = dbresult.getGoalsTime();
//                VALUES = new double[mp.size()];
//                NAME_LIST = new String[mp.size()];
//            Iterator it = mp.entrySet().iterator();
//            int ind = 0;
//            while (it.hasNext()) {
//                Map.Entry pairs = (Map.Entry)it.next();
//                
//                System.out.println(ind+" "+pairs.getKey() + " = " + pairs.getValue());
//                NAME_LIST[ind] = (String)pairs.getKey();
//                
//                // Milliseconds to minutes
//                long milliseconds = (Long)pairs.getValue();
//                //VALUES[ind] = ((milliseconds / (1000*60)) % 60);
//                VALUES[ind] = ((milliseconds / (1000*60)));
//                ind++;
//                
//                it.remove();
//
//            }
//            
           
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