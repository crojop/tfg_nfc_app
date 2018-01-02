/**
 * Copyright (C) 2009 - 2013 SC 4ViewSoft SRL
 *  
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *  
 *      http://www.apache.org/licenses/LICENSE-2.0
 *  
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.ounl.lifelonglearninghub.nfcecology.db.charts;

import org.achartengine.ChartFactory;
import org.achartengine.chart.BarChart.Type;
import org.achartengine.model.RangeCategorySeries;
import org.achartengine.model.XYMultipleSeriesDataset;
import org.achartengine.renderer.SimpleSeriesRenderer;
import org.achartengine.renderer.XYMultipleSeriesRenderer;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint.Align;

/**
 * Duration range chart.
 */
public class DurationChart extends AbstractChart {

  /**
   * Returns the chart name.
   * 
   * @return the chart name
   */
  public String getName() {
    return "Lifelong Learning Hub";
  }

  /**
   * Returns the chart description.
   * 
   * @return the chart description
   */
  public String getDesc() {
    return "Weekly (vertical range chart)";
  }

  /**
   * Executes the chart demo.
   * 
   * @param context the context
   * @return the built intent
   */
  public Intent execute(Context context) {
    double[] minValues = new double[] { 8, 13, 20, 20, 17, 12, 15 };
    double[] maxValues = new double[] { 7, 12, 23, 21, 18, 14, 18 };

    XYMultipleSeriesDataset dataset = new XYMultipleSeriesDataset();
    RangeCategorySeries series = new RangeCategorySeries("B2 Level in Dutch");
    int length = minValues.length;
    for (int k = 0; k < length; k++) {
      series.add(minValues[k], maxValues[k]);
    }
    dataset.addSeries(series.toXYSeries());
    int[] colors = new int[] { Color.WHITE };
    XYMultipleSeriesRenderer renderer = buildBarRenderer(colors);
    setChartSettings(renderer, "Weekly duration ranges", "Day of the week", "Time of the day", 0.5, 7.5, 0, 24, Color.GRAY, Color.LTGRAY);
    renderer.setBarSpacing(0.5);
    renderer.setXLabels(0);
    renderer.setYLabels(10);
    
    renderer.addXTextLabel(1, "Mon");
    renderer.addXTextLabel(2, "Tue");
    renderer.addXTextLabel(3, "Wed");
    renderer.addXTextLabel(4, "Thu");
    renderer.addXTextLabel(5, "Fri");
    renderer.addXTextLabel(6, "Sat");
    renderer.addXTextLabel(7, "Sun");

    renderer.addYTextLabel(6, "Wake up");
    renderer.addYTextLabel(8, "Work");
    renderer.addYTextLabel(18, "Home");
    renderer.addYTextLabel(23, "Sleep");
    
    
    renderer.setMargins(new int[] {30, 70, 10, 0});
    renderer.setYLabelsAlign(Align.RIGHT);
    
    renderer.setApplyBackgroundColor(true);
    renderer.setBackgroundColor(Color.BLACK);
    renderer.setMarginsColor(Color.BLACK); 
    
    
    SimpleSeriesRenderer r = renderer.getSeriesRendererAt(0);
    r.setDisplayChartValues(true);
    r.setChartValuesTextSize(12);
    r.setChartValuesSpacing(3);
    r.setGradientEnabled(true);
    r.setGradientStart(-20, Color.BLUE);
    r.setGradientStop(20, Color.WHITE);
    return ChartFactory.getRangeBarChartIntent(context, dataset, renderer, Type.DEFAULT,
        "Lifelong Learning Hub");
  }

}
