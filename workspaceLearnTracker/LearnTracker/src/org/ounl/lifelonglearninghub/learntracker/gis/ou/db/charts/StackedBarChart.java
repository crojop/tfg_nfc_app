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

import java.util.ArrayList;
import java.util.List;

import org.achartengine.ChartFactory;
import org.achartengine.chart.BarChart.Type;
import org.achartengine.renderer.XYMultipleSeriesRenderer;
import org.ounl.lifelonglearninghub.learntracker.gis.ou.Constants;
import org.ounl.lifelonglearninghub.learntracker.gis.ou.session.ActivitySession;
import org.ounl.lifelonglearninghub.learntracker.gis.ou.session.Session;
import org.ounl.lifelonglearninghub.learntracker.gis.ou.swipe.DateUtils;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint.Align;
import android.util.DisplayMetrics;
import android.util.Log;

public class StackedBarChart extends AbstractChart {

	private String CLASSNAME = this.getClass().getName();

	private XYMultipleSeriesRenderer renderer = new XYMultipleSeriesRenderer();

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
		return "This chart presents foreseen time VS effective time";
	}

	/**
	 * Executes the chart demo.
	 * 
	 * @param context
	 *            the context
	 * @return the built intent
	 */
	public Intent execute(Context context) {
		String[] titles = new String[] { "Foreseen by OUNL", "Your time" };
		// Colours for bars
		int[] colors = new int[] { Color.parseColor("#aaaaaa"),
				Color.parseColor("#E05904") };

		renderer = buildBarRenderer(colors);

		List<ActivitySession> las = Session.getSingleInstance().getActivities();

		Log.d(CLASSNAME,
				"Number of subjects to be presented in the stacked barchart "
						+ las.size());

		double[] adEstimated = new double[las.size()];
		double[] adAccomplished = new double[las.size()];

		// This parameter configures maximum height for the Y axis based on the
		// maximum value
		// for rather foreseen or accomplished time
		double dMaxHeight = 0;

		for (int i = 0; i < las.size(); i++) {
			DateUtils d = new DateUtils();
			adEstimated[i] = round(d.toHours(las.get(i).getForeseenDuration()),2);

			if (adEstimated[i] > dMaxHeight) {
				dMaxHeight = adEstimated[i];
			}

			String sIdSubject = Session.getSingleInstance().getActivity(i)
					.getId_subject();
			adAccomplished[i] = round(d.toHours(Session.getSingleInstance()
					.getDatabaseHandler().getAccumulatedTime(sIdSubject)), 2);

			if (adAccomplished[i] > dMaxHeight) {
				dMaxHeight = adAccomplished[i];
			}

			// Labels in X axis
			int iXLabelLength = 30;
			String sXLabel = las.get(i).getSubject_task_desc();
			if (sXLabel.length() > iXLabelLength + 1) {
				sXLabel = las.get(i).getSubject_task_desc()
						.substring(0, iXLabelLength);
			}
			renderer.addXTextLabel(i+1, "    " + sXLabel);

			Log.d(CLASSNAME, i + " / " + sXLabel + " / estimated "
					+ adEstimated[i] + " / accomplished " + adAccomplished[i]);

		}

		List<double[]> values = new ArrayList<double[]>();
		values.add(adEstimated);
		values.add(adAccomplished);

		int iMaxHeight = (int) Math.round(dMaxHeight);
		// First color is the color for the lines from the axis
		// Second color is the color form the legend
		// setChartSettings(renderer,
		// "Time (in hours) devoted to each assignment", "", "Number of hours",
		// 0, adEstimated.length+1, 0, iMaxHeight+1, Color.BLACK, Color.BLACK);
		setChartSettings(renderer,
				"Time (in hours) devoted to each assignment", "",
				"Number of hours", 0, adEstimated.length / 3, 0,
				iMaxHeight + 1, Color.BLACK, Color.BLACK);

		int iSizeFont = applySize(context);

		// X axis
		renderer.setXLabels(0);
		renderer.setXLabelsColor(Color.BLACK);
		renderer.setXLabelsAlign(Align.LEFT);
		renderer.setXLabelsAngle(90);
		// renderer.setXLabelsPadding(0);
		renderer.setXLabelsPadding(1);

		// Y axis
		renderer.setShowGridY(true);
		renderer.setYLabels(iMaxHeight);
		renderer.setYLabelsAlign(Align.LEFT);
		renderer.setYLabelsColor(0, Color.parseColor("#E05904"));

		// Misc
		renderer.getSeriesRendererAt(0).setDisplayChartValues(true);
		renderer.getSeriesRendererAt(0).setChartValuesTextSize(iSizeFont);
		renderer.getSeriesRendererAt(1).setDisplayChartValues(true);
		renderer.getSeriesRendererAt(1).setChartValuesTextSize(iSizeFont);

		renderer.setPanEnabled(true, true);
		renderer.setZoomRate(1.1f);
		// renderer.setBarSpacing(0.5f);
		// renderer.setBarSpacing(2);
		renderer.setMargins(new int[] { 20, 50, 0, 20 });
		renderer.setApplyBackgroundColor(true);
		renderer.setBackgroundColor(Color.WHITE);
		renderer.setMarginsColor(Color.WHITE);

		return ChartFactory.getBarChartIntent(context,
				buildBarDataset(titles, values), renderer, Type.STACKED);

	}

	private int applySize(Context context) {

		int iFontSize = 0;
		switch (context.getResources().getDisplayMetrics().densityDpi) {

		case Constants.DENSITY_XXHIGH:
			// renderer.setMargins(new int[] { 50, 130, 30, 10 });
			renderer.setBarWidth(Constants.TEXT_SIZE_XXHDPI * 2);
			renderer.setAxisTitleTextSize(Constants.TEXT_SIZE_XXHDPI);
			renderer.setChartTitleTextSize(Constants.TEXT_SIZE_XXXHDPI);
			renderer.setLabelsTextSize(Constants.TEXT_SIZE_XXHDPI);
			renderer.setLegendTextSize(Constants.TEXT_SIZE_XXHDPI);
			iFontSize = Constants.TEXT_SIZE_XXHDPI;
			break;
		case DisplayMetrics.DENSITY_XHIGH:
			// renderer.setMargins(new int[] { 40, 90, 25, 10 });
			renderer.setBarWidth(Constants.TEXT_SIZE_XHDPI * 2);
			renderer.setAxisTitleTextSize(Constants.TEXT_SIZE_XHDPI);
			renderer.setChartTitleTextSize(Constants.TEXT_SIZE_XHDPI);
			renderer.setLabelsTextSize(Constants.TEXT_SIZE_XHDPI);
			renderer.setLegendTextSize(Constants.TEXT_SIZE_XHDPI);
			iFontSize = Constants.TEXT_SIZE_XHDPI;
			break;
		case DisplayMetrics.DENSITY_HIGH:
			// renderer.setMargins(new int[] { 30, 50, 20, 10 });
			renderer.setBarWidth(Constants.TEXT_SIZE_HDPI * 2);
			renderer.setAxisTitleTextSize(Constants.TEXT_SIZE_HDPI);
			renderer.setChartTitleTextSize(Constants.TEXT_SIZE_HDPI);
			renderer.setLabelsTextSize(Constants.TEXT_SIZE_HDPI);
			renderer.setLegendTextSize(Constants.TEXT_SIZE_HDPI);
			iFontSize = Constants.TEXT_SIZE_HDPI;
			break;
		default:
			// renderer.setMargins(new int[] { 30, 50, 20, 10 });
			renderer.setBarWidth(Constants.TEXT_SIZE_LDPI * 2);
			renderer.setAxisTitleTextSize(Constants.TEXT_SIZE_LDPI);
			renderer.setChartTitleTextSize(Constants.TEXT_SIZE_LDPI);
			renderer.setLabelsTextSize(Constants.TEXT_SIZE_LDPI);
			renderer.setLegendTextSize(Constants.TEXT_SIZE_LDPI);
			iFontSize = Constants.TEXT_SIZE_LDPI;
			break;
		}
		return iFontSize;

	}

	public static double round(double value, int places) {
		if (places < 0)
			throw new IllegalArgumentException();

		long factor = (long) Math.pow(10, places);
		value = value * factor;
		long tmp = Math.round(value);
		return (double) tmp / factor;
	}

}
