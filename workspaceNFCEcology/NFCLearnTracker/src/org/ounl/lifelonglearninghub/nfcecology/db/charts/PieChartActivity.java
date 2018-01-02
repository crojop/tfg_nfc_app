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
package org.ounl.lifelonglearninghub.nfcecology.db.charts;

import java.util.Map;
import java.util.TreeMap;

import org.achartengine.ChartFactory;
import org.achartengine.GraphicalView;
import org.achartengine.model.CategorySeries;
import org.achartengine.model.SeriesSelection;
import org.achartengine.renderer.DefaultRenderer;
import org.achartengine.renderer.SimpleSeriesRenderer;
import org.ounl.lifelonglearninghub.nfcecology.DateUtils;
import org.ounl.lifelonglearninghub.nfcecology.db.DatabaseHandler;
import org.ounl.lifelonglearninghub.nfcecology.R;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.Toast;

public class PieChartActivity extends Activity {
        
	private DatabaseHandler db = null;
	
        private String CLASSNAME = this.getClass().getName();

        private static double[] VALUES ;

        private static String[] NAME_LIST;

        private CategorySeries mSeries = new CategorySeries("");

        private DefaultRenderer mRenderer = new DefaultRenderer();

        private GraphicalView mChartView;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
                super.onCreate(savedInstanceState);
                setContentView(R.layout.piechart);

                mRenderer.setApplyBackgroundColor(true);
                //mRenderer.setBackgroundColor(Color.argb(100, 50, 50, 50));
                mRenderer.setBackgroundColor(Color.BLACK);
                mRenderer.setChartTitleTextSize(50);
                mRenderer.setChartTitle("Your overall time by goal");
                mRenderer.setLabelsTextSize(60);
                mRenderer.setLabelsColor(Color.WHITE);
                mRenderer.setLegendTextSize(60);
                mRenderer.setMargins(new int[] { 20, 30, 15, 0 });
                mRenderer.setZoomButtonsVisible(true);
                mRenderer.setStartAngle(90);
                
                // Retrieve values from database
                db = new DatabaseHandler(this);
                TreeMap<String, Long> hmIL = db.getGoalsAccumulated();
                
                NAME_LIST  = new String[hmIL.size()];
                VALUES  = new double[hmIL.size()];
                
                
                int i = 0;
                for(Map.Entry<String,Long> entry : hmIL.entrySet()) {
                	  String key = entry.getKey();
                	  Long value = entry.getValue();
                	  
                      NAME_LIST[i] = key;
                      VALUES[i]= value;
                       
                      mSeries.add(NAME_LIST[i], VALUES[i]);
                      String sColor = db.getTagColor(key);
                      SimpleSeriesRenderer renderer = new SimpleSeriesRenderer();
                      renderer.setColor(Color.parseColor("#"+sColor));
                      mRenderer.addSeriesRenderer(renderer);
                      
                      Log.d(CLASSNAME,  i+ " estimated "+NAME_LIST[i]+" / accomplished "+VALUES[i]);
                      

                	  i++;
                	}
                

                if (mChartView != null) {
                        mChartView.repaint();
                }

        }

        @Override
        protected void onResume() {
                super.onResume();
                if (mChartView == null) {
                        LinearLayout layout = (LinearLayout) findViewById(R.id.chart);
                        mChartView = ChartFactory.getPieChartView(this, mSeries, mRenderer);
                        mRenderer.setClickEnabled(true);
                        mRenderer.setSelectableBuffer(10);

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
                                                            " " + du.duration(lo) + " ",
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
        
     
        
        
}
