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

import java.io.IOException;
import java.io.UnsupportedEncodingException;

import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.ounl.lifelonglearninghub.learntracker.gis.ou.db.ws.dataobjects.ActivityDO;
import org.ounl.lifelonglearninghub.learntracker.gis.ou.session.Session;

import android.os.AsyncTask;
import android.util.Log;

import com.google.gson.GsonBuilder;

public class ActivityWSPostAsyncTask extends
		AsyncTask<ActivityDO, Integer, Integer> {

	private String CLASSNAME = this.getClass().getName();
	
	private int executeCheckIn(ActivityDO a) {

		String json = new GsonBuilder().create().toJson(a, ActivityDO.class);

		String uri = Session.getSingleInstance().getWSPath()+"/_ah/api/activityendpoint/v1/activity";

		try {
			HttpPost httpPost = new HttpPost(uri);
			httpPost.setEntity(new StringEntity(json));
			httpPost.setHeader("Accept", "application/json");
			httpPost.setHeader("Content-type", "application/json");
			new DefaultHttpClient().execute(httpPost);

		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			return WSConstants.POST_RESPONSE_KO;
		} catch (ClientProtocolException e) {
			e.printStackTrace();
			return WSConstants.POST_RESPONSE_KO;
		} catch (IOException e) {
			e.printStackTrace();
			return WSConstants.POST_RESPONSE_KO;
		}
		return WSConstants.POST_RESPONSE_OK;
	}

	@Override
	protected void onPreExecute() {
		super.onPreExecute();

	}

	@Override
	protected Integer doInBackground(ActivityDO... sParam) {

		Log.d(CLASSNAME, "Starting "+CLASSNAME+" task in background...");
		
		Integer oIOut = WSConstants.POST_RESPONSE_OK;

		try {
			executeCheckIn(sParam[0]);
			
			// Insert activity into local database
			//Session.getSingleInstance().addActivity(sParam[0]);

		} catch (Exception e) {
			e.printStackTrace();
			oIOut = WSConstants.POST_RESPONSE_KO;
		}

		return oIOut;
	}

	@Override
	protected void onPostExecute(Integer result) {
		super.onPostExecute(result);

	}

}
