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
import java.util.HashMap;
import java.util.List;

import org.ounl.lifelonglearninghub.nfcecology.db.DatabaseHandler;
import org.ounl.lifelonglearninghub.nfcecology.db.ListViewGoalsAdapter;
import org.ounl.lifelonglearninghub.nfcecology.db.charts.DurationChart;
import org.ounl.lifelonglearninghub.nfcecology.db.charts.PieChartActivity;
import org.ounl.lifelonglearninghub.nfcecology.db.charts.ScatterChart;
import org.ounl.lifelonglearninghub.nfcecology.db.tables.Goal;
import org.ounl.lifelonglearninghub.nfcecology.R;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;


/**
 * A fragment that launches other parts of the demo application.
 */
public class GoalsSectionFragment extends Fragment {
	
	private ArrayList<HashMap> list;
	private ListView lview;
	

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_section_goals,
				container, false);

		
		
		lview = (ListView) rootView.findViewById(R.id.listviewGoals);
		populateList();
		ListViewGoalsAdapter adapter = new ListViewGoalsAdapter(getActivity(), list);
		lview.setAdapter(adapter);

		
		// Plot chart
		rootView.findViewById(R.id.ivScatterChart).setOnClickListener(
				new View.OnClickListener() {
					@Override
					public void onClick(View view) {
						ScatterChart chart = new ScatterChart();
						Intent intent = null;
						intent = chart.execute(getActivity().getApplicationContext());
						//intent.putExtra(Goal.KEY_NAME, sGoalId);
						startActivity(intent);						
						
						
					}
				});		
		
		// Pie chart
		rootView.findViewById(R.id.ivPieChart).setOnClickListener(
				new View.OnClickListener() {
					@Override
					public void onClick(View view) {
						
						Intent intent = new Intent(getActivity(),PieChartActivity.class);
						//intent.putExtra(Goal.KEY_ID, oI.intValue());
						startActivity(intent);
					}
				});
		
		


		return rootView;
	}
	
	
	private void populateList() {
		list = new ArrayList<HashMap>();
		
		DatabaseHandler dbresult = new DatabaseHandler(getActivity().getApplicationContext());
		List<Goal> recdata = dbresult.getGoals();
		for (Goal t : recdata) {
		
			String sName = t.getsName();
			String sDesc = t.getsDesc();
			String sDailyTimeMins = t.getDailyTimeMins()+"";
			

			// Records for data
			HashMap temp = new HashMap<String, String>();
			temp.put(Goal.KEY_NAME, sName);
			temp.put(Goal.KEY_DESC, sDesc);
			temp.put(Goal.KEY_DAILY_TIME, sDailyTimeMins);

			list.add(temp);
		}
	}
}