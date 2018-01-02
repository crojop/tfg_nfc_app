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
package org.ounl.lifelonglearninghub.nfclearntracker.db.charts;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.TimeUnit;

import org.achartengine.ChartFactory;
import org.achartengine.chart.BarChart.Type;
import org.achartengine.renderer.XYMultipleSeriesRenderer;
import org.ounl.lifelonglearninghub.nfclearntracker.DateUtils;
import org.ounl.lifelonglearninghub.nfclearntracker.db.DatabaseHandler;
import org.ounl.lifelonglearninghub.nfclearntracker.db.tables.Tagctivity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint.Align;
import android.service.textservice.SpellCheckerService.Session;
import android.util.Log;

public class StackedBarChart extends AbstractChart {

	private String CLASSNAME = this.getClass().getName();
	private DatabaseHandler db = null;
	private double[] adAccomplished = null;
	private double[] adEstimated = null;
	

	/**
	 * Returns the chart name.
	 * 
	 * @return the chart name
	 */
	public String getName() {
		return "Foreseen VS effective time chart";
	}

	/**
	 * Returns the chart description.
	 * 
	 * @return the chart description
	 */
	public String getDesc() {
		return "This chart presents foreseen time VS effective time in the last 7 days";
	}

	/**
	 * Executes the chart demo.
	 * 
	 * @param context
	 *            the context
	 * @return the built intent
	 */
	public Intent execute(Context context, String sTag) {
		String[] titles = new String[] { "Foreseen time", "Effective time" };
		
		db = new DatabaseHandler(context);
		
		Log.d(CLASSNAME, "Showing stacked bar chart for tag :"+sTag);
		
		
		
		// Colours for bars
		int[] colors = new int[] { Color.parseColor("#aaaaaa"),
				Color.parseColor("#E05904") };

		XYMultipleSeriesRenderer renderer = buildBarRenderer(colors);

		Date dNow = new Date();
		Date dPrevWeek = new Date();
		addDays(dPrevWeek, -7);
		
		TreeMap<Date, Long> tmChart = new TreeMap<Date, Long>();
		tmChart = db.getAccumActivitiesBetweenDates(sTag, dPrevWeek, dNow);
		
		adAccomplished = new double[tmChart.size()];
		adEstimated = new double[tmChart.size()];
		

		Log.d(CLASSNAME, "Number of items to be presented in the stacked barchart " + tmChart.size());
		
		// This parameter configures maximum height for the Y axis based on the
		// maximum value
		// for rather foreseen or accomplished time
		double dMaxHeight = 0;
		int i = 0;
		
		for(Map.Entry<Date,Long> entry : tmChart.entrySet()) {
			  Date dKey = entry.getKey();
			  Long lValue = entry.getValue();

			  System.out.println(dKey + " => " + lValue);


			DateUtils d = new DateUtils();
			adEstimated[i] = 0;


			adAccomplished[i] = d.toHours(lValue);
			if (adAccomplished[i] > dMaxHeight) {
				dMaxHeight = adAccomplished[i];
			}


			
			
			DateFormat df = new SimpleDateFormat("dd-MM-yyyy");
			Date today = Calendar.getInstance().getTime();        
			String reportDate = df.format(today);
			
			
			renderer.addXTextLabel(i, "    " + reportDate);

			Log.d(CLASSNAME, i + " estimated " + adEstimated[i]
					+ " / accomplished " + adAccomplished[i]);

			i++;
		}

		List<double[]> values = new ArrayList<double[]>();
		values.add(adEstimated);
		values.add(adAccomplished);

		// int[] colors = new int[] { Color.parseColor("#DBA481"),
		// Color.parseColor("#E05904") };

		// setChartSettings(renderer, "Time devoted to learning (in minutes)",
		// "Day", "Mills", 0.5, 7.5, 0, expectedTime+60, Color.GRAY,
		// Color.LTGRAY);
		int a = (int) Math.round(dMaxHeight);
		// First color is the color for the lines from the axis
		// Second color is the color form the legend
		setChartSettings(renderer,
				"Time (in hours) devoted to each assignment", "",
				"Number of hours", 0, adEstimated.length + 1, 0, a + 1,
				Color.BLACK, Color.BLACK);
		// renderer.getSeriesRendererAt(0).setDisplayChartValues(true);
		// renderer.getSeriesRendererAt(1).setDisplayChartValues(true);

		renderer.setChartTitleTextSize(50);
		renderer.setLabelsTextSize(40);
		renderer.setLegendTextSize(40);

		renderer.setShowGridY(true);

		renderer.setXLabels(0);

		renderer.setXLabelsColor(Color.BLACK);
		renderer.setXLabelsAlign(Align.LEFT);
		renderer.setXLabelsAngle(90);
		renderer.setXLabelsPadding(0);
		// renderer.setXLabels(las.size());

		renderer.getSeriesRendererAt(0).setDisplayChartValues(false);
		renderer.getSeriesRendererAt(1).setDisplayChartValues(false);

		// renderer.setXLabels(240);
		// renderer.setYLabels(10);

		renderer.setYLabels(a);

		renderer.setYLabelsAlign(Align.LEFT);
		renderer.setYLabelsColor(0, Color.parseColor("#E05904"));
		// renderer.setPanEnabled(true, false);
		renderer.setPanEnabled(true, true);
		// renderer.setZoomEnabled(false);
		renderer.setZoomRate(1.1f);
		renderer.setBarSpacing(0.5f);

		// renderer.setMargins(new int[] { 50, 50, 25, 22 });
		// top, left, bottom, right
		renderer.setMargins(new int[] { 20, 50, 0, 20 });

		renderer.setApplyBackgroundColor(true);
		renderer.setBackgroundColor(Color.WHITE);

		renderer.setMarginsColor(Color.WHITE);

		return ChartFactory.getBarChartIntent(context,
				buildBarDataset(titles, values), renderer, Type.STACKED);

	}
	
//	
//	private void loadData(String tagId, Date dIni, Date dEnd){
//		
//		
//		long diff = dEnd.getTime() - dIni.getTime();
//		int iDiffDays = (int)TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS);
//		
//	    Log.d(CLASSNAME, "Difference in days between "+ dIni +" and "+dEnd+" :"+ iDiffDays);
//	    
//		adAccomplished = new double[iDiffDays];
//
//		
//		TreeMap<Date, Long> tmChart = new TreeMap<Date, Long>();
//		tmChart = db.getAccumActivitiesBetweenDates(tagId, dIni, dEnd);
//		
//		
//		
//		
//		
//		
//		
//		DateUtils d = new DateUtils();
//		adEstimated[i] = d.toHours(las.get(i).getForeseenDuration());
//		
//		
//		
//		
//		
//		
//	}

	
    /**
     * Adds days to d
     */
    public static void addDays(Date d, int days)
    {
        Calendar c = Calendar.getInstance();
        c.setTime(d);
        c.add(Calendar.DATE, days);
        d.setTime( c.getTime().getTime() );
    }  
}