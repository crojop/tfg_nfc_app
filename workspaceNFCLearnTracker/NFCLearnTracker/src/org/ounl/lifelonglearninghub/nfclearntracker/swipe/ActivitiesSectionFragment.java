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

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.ounl.lifelonglearninghub.nfclearntracker.db.DatabaseHandler;
import org.ounl.lifelonglearninghub.nfclearntracker.db.ListViewActivitiesAdapter;
import org.ounl.lifelonglearninghub.nfclearntracker.db.tables.Tagctivity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;

import org.ounl.lifelonglearninghub.nfclearntracker.R;

/**
 * A dummy fragment representing a section of the app, but that simply displays
 * dummy text.
 */
public class ActivitiesSectionFragment extends Fragment {
	
	private ArrayList<HashMap<String, String>> list;
	Button home;
	String[] problemgrid = null;
	List<String> getnumbers = null;
	ListView lview, lviewHeader;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.activity_display_activities, container, false);

		//Bundle args = getArguments();
		// ((TextView)
		// rootView.findViewById(android.R.id.text1)).setText(getString(R.string.dummy_section_text,
		// args.getInt(ARG_SECTION_NUMBER)));
		
		
//		
				
		lview = (ListView) rootView.findViewById(R.id.listviewActivities);
		populateList();
		
		ListViewActivitiesAdapter adapter = new ListViewActivitiesAdapter(getActivity(), list);
		lview.setAdapter(adapter);

		return rootView;
	}
	


	private void populateList() {
		list = new ArrayList<HashMap<String, String>>();
		
		DatabaseHandler dbresult = new DatabaseHandler(getActivity()
				.getApplicationContext());
		List<Tagctivity> recdata = dbresult.getActivities();
		
//		// Header of table
//		HashMap<String, String> temp = new HashMap<String, String>();
//		temp.put(Tagctivity.KEY_ID_TAG, Tagctivity.KEY_ID_TAG);
//		temp.put(Tagctivity.KEY_DATE_CHECKIN, Tagctivity.KEY_DATE_CHECKIN);
//		temp.put(Tagctivity.KEY_DATE_CHECKOUT, Tagctivity.KEY_DATE_CHECKOUT);
//		list.add(temp);			
		
		for (Tagctivity t : recdata) {
			
			String sID = t.getsIdTagctivity();
			long lIn = t.getlDateCheckIn();
			Date dateIn = new Date(lIn);
			
			
			DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
			String dateInFormatted = formatter.format(dateIn);
			
			long lOut = t.getlDateCheckOut();
			Date dateOut = new Date(lOut);
			String dateOutFormatted = formatter.format(dateOut);

			// Records for data
			HashMap<String, String> temp1 = new HashMap<String, String>();
			temp1.put(Tagctivity.KEY_ID_TAG, sID);
			temp1.put(Tagctivity.KEY_DATE_CHECKIN, dateInFormatted);
			temp1.put(Tagctivity.KEY_DATE_CHECKOUT, dateOutFormatted);

			list.add(temp1);
						
		}
		System.out.println("Test size :"+ list.size());
	}

}