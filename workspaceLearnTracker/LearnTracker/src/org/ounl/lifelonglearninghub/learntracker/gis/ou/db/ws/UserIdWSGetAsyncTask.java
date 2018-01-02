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
package org.ounl.lifelonglearninghub.learntracker.gis.ou.db.ws;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.ounl.lifelonglearninghub.learntracker.gis.ou.db.ws.dataobjects.UserDO;
import org.ounl.lifelonglearninghub.learntracker.gis.ou.db.ws.dataobjects.UserDOList;
import org.ounl.lifelonglearninghub.learntracker.gis.ou.session.Session;

import android.os.AsyncTask;
import android.util.Log;

import com.google.gson.Gson;

public class UserIdWSGetAsyncTask extends
		AsyncTask<String, Integer, List<String>> {
	
	private String CLASSNAME = this.getClass().getName();

	public List<String> listUserIds(String sCourseId) {


		InputStream is = null;
		String url = Session.getSingleInstance().getWSPath()+"/_ah/api/userendpoint/v1/user/course/";
		url+=sCourseId;

		try {
			Log.d(CLASSNAME, " Querying to backend " + url);
			HttpClient httpclient = new DefaultHttpClient();
			HttpGet httpget = new HttpGet(url);
			HttpResponse response = httpclient.execute(httpget);

			final int statusCode = response.getStatusLine().getStatusCode();
			if (statusCode != HttpStatus.SC_OK) {
				Log.w(getClass().getSimpleName(), "Error " + statusCode + " for URL " + url);
				return null;
			}

			HttpEntity entity = response.getEntity();
			is = entity.getContent();

		} catch (Exception e) {
			Log.e(CLASSNAME, "Error in http connection " + e.toString());
		}



		Reader reader = new InputStreamReader(is);
		Gson gson = new Gson();
		UserDOList r = gson.fromJson(reader, UserDOList.class);
		
		
		List<UserDO> usersDO = r.users;
		List<String> listUserIds = new ArrayList<String>();
		
//		Log.d(CLASSNAME, " Number of users retrieved from backend: " + users.size());
//		List<UserDO> lOrderedUsers = orderedUsers(users);
//		Log.d(CLASSNAME, " Number of users retrieved from ordered list: " + lOrderedUsers.size());
//		Log.d(CLASSNAME, toString(lOrderedUsers));
		
		for (UserDO s : usersDO) {
			Log.d(CLASSNAME, s.toString());
			listUserIds.add(s.getUserName());
		}


		return listUserIds;
	}

	@Override
	protected void onPreExecute() {
		super.onPreExecute();

	}

	@Override
	protected List<String> doInBackground(String... sParam) {

		Log.d(CLASSNAME, "Starting task in background...");
		List<String> sOutList = new ArrayList<String>();

		try {

			Log.d(CLASSNAME, "Course ID [" + sParam[0].toString() + "]");

			// sOutList = listUsers(sParam[0].toString());
			sOutList = listUserIds(sParam[0].toString());


			// Log.d(CLASSNAME, "Requesting content "+sParam[0].toString());
			// String sOutTextTv2 = nfcRequestContentSoap(sParam[0].toString());
			// Log.d(CLASSNAME, "Content launched: "+sOutTextTv2);

		} catch (Exception e) {
			e.printStackTrace();
		}
				
		return sOutList;
	}

	@Override
	protected void onPostExecute(List<String> result) {
		super.onPostExecute(result);

	}

//	private void print(List<UserDO> theList) {
//
//		Log.d(CLASSNAME, "About to print the list with " + theList
//				+ " items.");
//		for (int i = 0; i < theList.size(); i++) {
//			Log.d(CLASSNAME, "[" + i + "]------" + theList.get(i).toString());
//
//		}
//	}

	

}
