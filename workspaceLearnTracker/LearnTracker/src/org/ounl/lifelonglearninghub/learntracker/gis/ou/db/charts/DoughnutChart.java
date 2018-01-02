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
package org.ounl.lifelonglearninghub.learntracker.gis.ou.db.charts;

import java.util.ArrayList;
import java.util.List;

import org.achartengine.ChartFactory;
import org.achartengine.renderer.DefaultRenderer;
import org.ounl.lifelonglearninghub.learntracker.gis.ou.Constants;
import org.ounl.lifelonglearninghub.learntracker.gis.ou.session.ActivitySession;
import org.ounl.lifelonglearninghub.learntracker.gis.ou.session.Session;
import org.ounl.lifelonglearninghub.learntracker.gis.ou.swipe.DateUtils;
import org.ounl.lifelonglearninghub.learntracker.gis.ou.R;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.util.DisplayMetrics;
import android.util.Log;

/**
 * Budget demo pie chart.
 */
public class DoughnutChart extends AbstractChart {

	private String CLASSNAME = this.getClass().getName();

	private DefaultRenderer renderer = new DefaultRenderer();

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

	};

	/**
	 * Returns the chart name.
	 * 
	 * @return the chart name
	 */
	public String getName() {
		return "Budget chart for several years";
	}

	/**
	 * Returns the chart description.
	 * 
	 * @return the chart description
	 */
	public String getDesc() {
		return "The budget per project for several years (doughnut chart)";
	}

	/**
	 * Executes the chart demo.
	 * 
	 * @param context
	 *            the context
	 * @return the built intent
	 */
	public Intent execute(Context context) {;

		renderer = buildCategoryRenderer(COLORS);

		List<ActivitySession> las = Session.getSingleInstance().getActivities();
		Log.d(CLASSNAME, "Number of subjects to be presented in the chart "
				+ las.size());

		double[] adEstimated = new double[las.size()];
		double[] adAccomplished = new double[las.size()];
		String[] asTitles = new String[las.size()];

		// This parameter configures maximum height for the Y axis based on the
		// maximum value
		// for rather foreseen or accomplished time
		double dMaxHeight = 0;

		for (int i = 0; i < las.size(); i++) {
			DateUtils d = new DateUtils();
			adEstimated[i] = d.toHours(las.get(i).getForeseenDuration());

			if (adEstimated[i] > dMaxHeight) {
				dMaxHeight = adEstimated[i];
			}

			String sIdSubject = Session.getSingleInstance().getActivity(i)
					.getId_subject();
			adAccomplished[i] = d.toHours(Session.getSingleInstance()
					.getDatabaseHandler().getAccumulatedTime(sIdSubject));

			if (adAccomplished[i] > dMaxHeight) {
				dMaxHeight = adAccomplished[i];
			}

			// Labels
			int iXLabelLength = 30;
			String sXLabel = las.get(i).getSubject_task_desc();
			if (sXLabel.length() > iXLabelLength + 1) {
				sXLabel = las.get(i).getSubject_task_desc()
						.substring(0, iXLabelLength);
			}
			asTitles[i] = sXLabel;

			Log.d(CLASSNAME, i + ". " + sXLabel + " / estimated "
					+ adEstimated[i] + " / accomplished " + adAccomplished[i]);

		}

		List<double[]> values = new ArrayList<double[]>();
		values.add(adEstimated);
		values.add(adAccomplished);

		List<String[]> titles = new ArrayList<String[]>();
		titles.add(asTitles);
		titles.add(asTitles);



		renderer.setApplyBackgroundColor(true);
		renderer.setBackgroundColor(Color.WHITE);
		renderer.setLabelsColor(Color.BLACK);

		applySize(context);

		return ChartFactory.getDoughnutChartIntent(context,
				buildMultipleCategoryDataset("Project budget", titles, values),
				renderer, context.getString(R.string.app_name));
	}

	private void applySize(Context context) {
		switch (context.getResources().getDisplayMetrics().densityDpi) {

		case Constants.DENSITY_XXHIGH:
			// renderer.setMargins(new int[] { 50, 130, 30, 10 });
			// renderer.setAxisTitleTextSize(Constants.TEXT_SIZE_XXHDPI);
			renderer.setChartTitleTextSize(Constants.TEXT_SIZE_XXXHDPI);
			renderer.setLabelsTextSize(Constants.TEXT_SIZE_XXHDPI);
			renderer.setLegendTextSize(Constants.TEXT_SIZE_XXHDPI);
			break;
		case DisplayMetrics.DENSITY_XHIGH:
			// renderer.setMargins(new int[] { 40, 90, 25, 10 });
			// renderer.setAxisTitleTextSize(Constants.TEXT_SIZE_XHDPI);
			renderer.setChartTitleTextSize(Constants.TEXT_SIZE_XHDPI);
			renderer.setLabelsTextSize(Constants.TEXT_SIZE_XHDPI);
			renderer.setLegendTextSize(Constants.TEXT_SIZE_XHDPI);
			break;
		case DisplayMetrics.DENSITY_HIGH:
			// renderer.setMargins(new int[] { 30, 50, 20, 10 });
			// renderer.setAxisTitleTextSize(Constants.TEXT_SIZE_HDPI);
			renderer.setChartTitleTextSize(Constants.TEXT_SIZE_HDPI);
			renderer.setLabelsTextSize(Constants.TEXT_SIZE_HDPI);
			renderer.setLegendTextSize(Constants.TEXT_SIZE_HDPI);
			break;
		default:
			// renderer.setMargins(new int[] { 30, 50, 20, 10 });
			// renderer.setAxisTitleTextSize(Constants.TEXT_SIZE_LDPI);
			renderer.setChartTitleTextSize(Constants.TEXT_SIZE_LDPI);
			renderer.setLabelsTextSize(Constants.TEXT_SIZE_LDPI);
			renderer.setLegendTextSize(Constants.TEXT_SIZE_LDPI);
			break;
		}

	}

}
