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

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.impl.client.DefaultHttpClient;
import org.ounl.lifelonglearninghub.learntracker.gis.ou.session.Session;

import android.os.AsyncTask;
import android.util.Log;

public class ActivityWSDeleteAsyncTask extends
		AsyncTask<String, Integer, Integer> {

	private String CLASSNAME = this.getClass().getName();

	public int deleteActivities(long lCheckIn, String sUserId) {

		// e.g. query
		// https://lifelong-learning-hub.appspot.com/_ah/api/activityendpoint/v1/activity/checkin/{activity_date_checkin}/user/{user_id}
		// e.g. query
		// https://lifelong-learning-hub.appspot.com/_ah/api/activityendpoint/v1/activity/checkin/867232233232/user/Bernd

		InputStream is = null;
		String url = Session.getSingleInstance().getWSPath()+"/_ah/api/activityendpoint/v1/activity/checkin/"
				+ lCheckIn + "/user/" + sUserId;

		try {
			Log.d(CLASSNAME, " Querying to backend deleteActivities check in:"
					+ lCheckIn + " user:" + sUserId);
			HttpClient httpclient = new DefaultHttpClient();

			HttpDelete httpdelete = new HttpDelete(url);
			HttpResponse response = httpclient.execute(httpdelete);

			// HttpGet httpget = new HttpGet(url);
			// HttpResponse response = httpclient.execute(httpget);

			final int statusCode = response.getStatusLine().getStatusCode();
			if (statusCode != HttpStatus.SC_OK) {
				Log.w(getClass().getSimpleName(), "Error " + statusCode
						+ " for URL " + url);
				return 0;
			}

			HttpEntity entity = response.getEntity();
			is = entity.getContent();
			Log.d(CLASSNAME, "deleteActivities returns [" + is.toString() + "]");

		} catch (Exception e) {
			Log.e(CLASSNAME, "Error in http connection " + e.toString());
		}

		// TODO this method should return the numer of items deleted in backend
		// Issue 15
		// https://code.google.com/p/lifelong-learning-hub/issues/detail?id=15

		return 1;
	}

	@Override
	protected void onPreExecute() {
		super.onPreExecute();

	}

	@Override
	protected Integer doInBackground(String... sParam) {

		Log.d(CLASSNAME, "Starting " + CLASSNAME + " task in background...");
		Integer oIDeleted = -1;
		String sUser = "";
		String sCheckIn = "";

		try {

			sCheckIn = sParam[0].toString();
			long lCheckIn = new Long(sCheckIn);
			sUser = sParam[1].toString();

			Log.d(CLASSNAME, "Check IN [" + lCheckIn + "]  User ID ["
					+ sParam[1].toString() + "]");
			oIDeleted = deleteActivities(lCheckIn, sUser);

		} catch (Exception e) {
			e.printStackTrace();
		}

		return oIDeleted;
	}

	@Override
	protected void onPostExecute(Integer result) {
		super.onPostExecute(result);

	}

}
