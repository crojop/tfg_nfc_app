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
import org.achartengine.chart.PointStyle;
import org.achartengine.model.XYMultipleSeriesDataset;
import org.achartengine.renderer.XYMultipleSeriesRenderer;
import org.achartengine.renderer.XYSeriesRenderer;
import org.ounl.lifelonglearninghub.learntracker.gis.ou.Constants;
import org.ounl.lifelonglearninghub.learntracker.gis.ou.session.ActivitySession;
import org.ounl.lifelonglearninghub.learntracker.gis.ou.session.Session;
import org.ounl.lifelonglearninghub.learntracker.gis.ou.swipe.DateUtils;
import org.ounl.lifelonglearninghub.learntracker.gis.ou.R;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint.Align;
import android.util.DisplayMetrics;
import android.util.Log;

/**
 * Average temperature demo chart.
 */
public class YardLineChart extends AbstractChart {

	private String CLASSNAME = this.getClass().getName();

	private XYMultipleSeriesRenderer renderer = new XYMultipleSeriesRenderer();

	/**
	 * Returns the chart name.
	 * 
	 * @return the chart name
	 */
	public String getName() {
		return "Average temperature";
	}

	/**
	 * Returns the chart description.
	 * 
	 * @return the chart description
	 */
	public String getDesc() {
		return "The average temperature in 4 Greek islands (line chart)";
	}

	/**
	 * Executes the chart demo.
	 * 
	 * @param context
	 *            the context
	 * @return the built intent
	 */
	public Intent execute(Context context) {

		String[] titles = new String[] { "Estimated time", "YOUR time" };

		List<double[]> values = new ArrayList<double[]>();

		int[] colors = new int[] { Color.parseColor("#333333"),
				Color.parseColor("#E05904") };

		PointStyle[] styles = new PointStyle[] { PointStyle.CIRCLE,
				PointStyle.TRIANGLE };

		renderer = buildRenderer(colors, styles);
		int length = renderer.getSeriesRendererCount();
		for (int i = 0; i < length; i++) {
			((XYSeriesRenderer) renderer.getSeriesRendererAt(i))
					.setFillPoints(true);
		}

		List<ActivitySession> las = Session.getSingleInstance().getActivities();

		// This might be quite useless
		double[] dX = new double[las.size()];
		for (int i = 0; i < las.size(); i++) {
			dX[i] = i + 1;
		}

		List<double[]> x = new ArrayList<double[]>();
		for (int i = 0; i < titles.length; i++) {
			x.add(dX);
		}

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
			adEstimated[i] = DateUtils.round(
					d.toHours(las.get(i).getForeseenDuration()), 2);

			if (adEstimated[i] > dMaxHeight) {
				dMaxHeight = adEstimated[i];
			}

			String sIdSubject = Session.getSingleInstance().getActivity(i)
					.getId_subject();
			adAccomplished[i] = DateUtils.round(
					d.toHours(Session.getSingleInstance().getDatabaseHandler()
							.getAccumulatedTime(sIdSubject)), 2);

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
			renderer.addXTextLabel(i + 1, "    " + sXLabel);

			Log.d(CLASSNAME, i + " / " + sXLabel + " / estimated "
					+ adEstimated[i] + " / accomplished " + adAccomplished[i]);

		}

		values.add(adEstimated);
		values.add(adAccomplished);


		setChartSettings(renderer,
				"Time (in hours) devoted to study GIS", "",
				"Number of hours", 0, adEstimated.length / 3, 0, 10,
				Color.BLACK, Color.BLACK);

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
		// renderer.setYLabels(iMaxHeight);
		renderer.setYLabels(8);
		renderer.setYLabelsAlign(Align.LEFT);
		renderer.setYLabelsColor(0, Color.parseColor("#E05904"));

		// Misc
		renderer.getSeriesRendererAt(0).setDisplayChartValues(true);
		renderer.getSeriesRendererAt(0).setChartValuesTextSize(iSizeFont);
		renderer.getSeriesRendererAt(1).setDisplayChartValues(true);
		renderer.getSeriesRendererAt(1).setChartValuesTextSize(iSizeFont);

		renderer.setPanEnabled(true, true);
		renderer.setZoomRate(1.1f);
		renderer.setMargins(new int[] { 20, 8, 0, 20 });
		renderer.setApplyBackgroundColor(true);
		renderer.setBackgroundColor(Color.WHITE);
		renderer.setMarginsColor(Color.WHITE);

		int len = renderer.getSeriesRendererCount();
		for (int i = 0; i < len; i++) {
			XYSeriesRenderer seriesRenderer = (XYSeriesRenderer) renderer
					.getSeriesRendererAt(i);
			seriesRenderer.setLineWidth(10f);
		}

		XYMultipleSeriesDataset dataset = buildDataset(titles, x, values);
		Intent intent = ChartFactory.getLineChartIntent(context, dataset,
				renderer, context.getString(R.string.app_name));
		return intent;
	}

	private int applySize(Context context) {

		int iFontSize = 0;
		switch (context.getResources().getDisplayMetrics().densityDpi) {

		case Constants.DENSITY_XXHIGH:
			// renderer.setMargins(new int[] { 50, 130, 30, 10 });
			renderer.setBarWidth(Constants.TEXT_SIZE_XXHDPI * 2);
			renderer.setAxisTitleTextSize(Constants.TEXT_SIZE_XXHDPI);
			renderer.setChartTitleTextSize(Constants.TEXT_SIZE_XXXXHDPI);
			renderer.setLabelsTextSize(Constants.TEXT_SIZE_XXHDPI);
			renderer.setLegendTextSize(Constants.TEXT_SIZE_XXHDPI);
			iFontSize = Constants.TEXT_SIZE_XXHDPI;
			break;
		case DisplayMetrics.DENSITY_XHIGH:
			// renderer.setMargins(new int[] { 40, 90, 25, 10 });
			renderer.setBarWidth(Constants.TEXT_SIZE_XHDPI * 2);
			renderer.setAxisTitleTextSize(Constants.TEXT_SIZE_XHDPI);
			renderer.setChartTitleTextSize(Constants.TEXT_SIZE_XXHDPI);
			renderer.setLabelsTextSize(Constants.TEXT_SIZE_XHDPI);
			renderer.setLegendTextSize(Constants.TEXT_SIZE_XHDPI);
			iFontSize = Constants.TEXT_SIZE_XHDPI;
			break;
		case DisplayMetrics.DENSITY_HIGH:
			// renderer.setMargins(new int[] { 30, 50, 20, 10 });
			renderer.setBarWidth(Constants.TEXT_SIZE_HDPI * 2);
			renderer.setAxisTitleTextSize(Constants.TEXT_SIZE_HDPI);
			renderer.setChartTitleTextSize(Constants.TEXT_SIZE_XHDPI);
			renderer.setLabelsTextSize(Constants.TEXT_SIZE_HDPI);
			renderer.setLegendTextSize(Constants.TEXT_SIZE_HDPI);
			iFontSize = Constants.TEXT_SIZE_HDPI;
			break;
		default:
			// renderer.setMargins(new int[] { 30, 50, 20, 10 });
			renderer.setBarWidth(Constants.TEXT_SIZE_LDPI * 2);
			renderer.setAxisTitleTextSize(Constants.TEXT_SIZE_LDPI);
			renderer.setChartTitleTextSize(Constants.TEXT_SIZE_HDPI);
			renderer.setLabelsTextSize(Constants.TEXT_SIZE_LDPI);
			renderer.setLegendTextSize(Constants.TEXT_SIZE_LDPI);
			iFontSize = Constants.TEXT_SIZE_LDPI;
			break;
		}
		return iFontSize;

	}

}
