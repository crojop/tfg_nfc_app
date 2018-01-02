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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.ounl.lifelonglearninghub.nfclearntracker.db.DatabaseHandler;
import org.ounl.lifelonglearninghub.nfclearntracker.db.ListViewTagsAdapter;
import org.ounl.lifelonglearninghub.nfclearntracker.db.tables.Tag;
import org.ounl.lifelonglearninghub.nfclearntracker.R;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;


/**
 * A fragment that launches other parts of the demo application.
 */
public class TagsSectionFragment  extends Fragment {
	
	private ArrayList<HashMap> list;
	private ListView lview;
	

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.activity_display_tags,
				container, false);

		
		
		lview = (ListView) rootView.findViewById(R.id.listviewTags);
		populateList();
		ListViewTagsAdapter adapter = new ListViewTagsAdapter(getActivity(), list);
		lview.setAdapter(adapter);


		return rootView;
	}
	
	private void populateList() {
		list = new ArrayList<HashMap>();
//		HashMap<String, String> temp1 = new HashMap();
//		// Header of table
//		temp1.put(Constants.FIRST, Tag.KEY_ID_GOAL);
//		temp1.put(Constants.SECOND, Tag.KEY_ID_TAG);
//		temp1.put(Constants.THIRD, Tag.KEY_VALIDITY_BEGIN);
//		temp1.put(Constants.FOURTH, Tag.KEY_VALIDITY_END);
//
//		list.add(temp1);
		DatabaseHandler dbresult = new DatabaseHandler(getActivity().getApplicationContext());
		List<Tag> recdata = dbresult.getTags();
		for (Tag t : recdata) {
			String sIdGoal = t.getsIdGoal();
			String sIdTag = t.getsIdTag();
			long validityBegin = t.getdDateValidityBegin();
			long validityEnd = t.getdDateValidityEnd();
			
			String sColor = t.getColor();
			

			// Records for data
			HashMap temp = new HashMap<String, String>();
			temp.put(Tag.KEY_ID_GOAL, sIdGoal);
			temp.put(Tag.KEY_ID_TAG, sIdTag);
			temp.put(Tag.KEY_VALIDITY_BEGIN, t.getdDateValidityBegin());
			temp.put(Tag.KEY_VALIDITY_END, t.getFormattedValidityEnd());
			temp.put(Tag.KEY_COLOR, sColor);

			list.add(temp);
		}
	}
}