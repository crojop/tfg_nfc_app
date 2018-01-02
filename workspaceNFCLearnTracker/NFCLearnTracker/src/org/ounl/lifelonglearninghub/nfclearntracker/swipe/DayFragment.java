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
package org.ounl.lifelonglearninghub.nfclearntracker.swipe;

import org.ounl.lifelonglearninghub.nfclearntracker.db.charts.StackedBarChart;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.ounl.lifelonglearninghub.nfclearntracker.R;

/**
 * A dummy fragment representing a section of the app, but that simply displays
 * dummy text.
 */
public class DayFragment extends Fragment {

	public static final String ARG_OBJECT = "object";
	public static final String ARG_DURATION = "duration";

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		
		View rootView = inflater.inflate(R.layout.fragment_collection_object, container, false);
		Bundle args = getArguments();
		int iDuration = args.getInt(ARG_DURATION);
		
		System.out.println("Received in dayfragment ["+iDuration+"]");
		
		
		
		TextView t = (TextView) rootView.findViewById(R.id.tvDuration);
		t.setText(iDuration+" mins");

		

		
		return rootView;
	}
	

	
}