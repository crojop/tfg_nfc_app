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

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.ounl.lifelonglearninghub.nfcecology.db.DatabaseHandler;
import org.ounl.lifelonglearninghub.nfcecology.db.ListViewNotificationsAdapter;
import org.ounl.lifelonglearninghub.nfcecology.db.tables.Notification;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;

import org.ounl.lifelonglearninghub.nfcecology.R;

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
		View rootView = inflater.inflate(R.layout.activity_display_notifications,
				container, false);

		Bundle args = getArguments();
		// ((TextView)
		// rootView.findViewById(android.R.id.text1)).setText(getString(R.string.dummy_section_text,
		// args.getInt(ARG_SECTION_NUMBER)));

		lview = (ListView) rootView.findViewById(R.id.listviewNotifications);
		populateList();
		ListViewNotificationsAdapter adapter = new ListViewNotificationsAdapter(getActivity(), list);
		lview.setAdapter(adapter);

		return rootView;
	}

	private void populateList() {
		list = new ArrayList<HashMap>();
//		HashMap<String, String> temp1 = new HashMap();
//		
//		
//		// Header of table
//		temp1.put(Constants.FIRST, Tagctivity.KEY_ID_TAG);
//		temp1.put(Constants.SECOND, Tagctivity.KEY_DATE_CHECKIN);
//		temp1.put(Constants.THIRD, Tagctivity.KEY_DATE_CHECKOUT);

//		list.add(temp1);
		DatabaseHandler dbresult = new DatabaseHandler(getActivity()
				.getApplicationContext());
		List<Notification> recdata = dbresult.getNotifications();
		
		for (Notification t : recdata) {
			
			long lIdNotification = t.getlNotificationId();
			String sIdGoal = t.getsNotificationGoalId();
			String sNotifText = t.getsNotificationText();
			long lDelaySecs = t.getlNotificationDelaySecs();
			int iNotificationType = t.getiNotificationType();

			// Records for data
			HashMap<String, String> temp = new HashMap<String, String>();
			temp.put(Notification.KEY_ID_NOTIF, lIdNotification+"");
			temp.put(Notification.KEY_ID_GOAL, sIdGoal);
			temp.put(Notification.KEY_NOTIF_TEXT, sNotifText);
			temp.put(Notification.KEY_NOTIF_DELAY_SECS, lDelaySecs+"");
			temp.put(Notification.KEY_NOTIF_TYPE, iNotificationType+"");			

			list.add(temp);
		}
	}

}