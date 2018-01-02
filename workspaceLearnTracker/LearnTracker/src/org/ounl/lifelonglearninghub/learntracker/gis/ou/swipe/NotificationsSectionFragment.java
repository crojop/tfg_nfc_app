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
package org.ounl.lifelonglearninghub.learntracker.gis.ou.swipe;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.ounl.lifelonglearninghub.learntracker.gis.ou.R;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;

/**
 * A dummy fragment representing a section of the app, but that simply displays
 * dummy text.
 */
public class NotificationsSectionFragment extends Fragment {

	public static final String ARG_SECTION_NUMBER = "section_number";
	private ArrayList<HashMap> list;
	Button home;
	String[] problemgrid = null;
	List<String> getnumbers = null;
	ListView lview;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.activity_display_activities,
				container, false);

		return rootView;
	}

}