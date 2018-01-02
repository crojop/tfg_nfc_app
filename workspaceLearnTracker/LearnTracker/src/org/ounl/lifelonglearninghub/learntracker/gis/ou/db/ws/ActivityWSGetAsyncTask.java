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
import java.util.Arrays;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.ounl.lifelonglearninghub.learntracker.gis.ou.db.ws.dataobjects.ActivityDO;
import org.ounl.lifelonglearninghub.learntracker.gis.ou.db.ws.dataobjects.ActivityDOList;
import org.ounl.lifelonglearninghub.learntracker.gis.ou.session.Session;

import android.os.AsyncTask;
import android.util.Log;

import com.google.gson.Gson;

public class ActivityWSGetAsyncTask extends
		AsyncTask<String, Integer, List<ActivityDO>> {
	
	private String CLASSNAME = this.getClass().getName();

	public List<ActivityDO> listActivities(String sCourseId, String sUserId) {

		// TODO THIS IS NOW RETURNING ALL THE ACTIVITIES FOR A USER
		// Fix this issue https://code.google.com/p/lifelong-learning-hub/issues/detail?id=11
		
		// e.g. query https://lifelong-learning-hub.appspot.com/_ah/api/subjectendpoint/v1/subject/course/S23222
		// e.g. query https://lifelong-learning-hub.appspot.com/_ah/api/activityendpoint/v1/activity/
		
		// You want to do it like this ...
		// e.g. query https://lifelong-learning-hub.appspot.com/_ah/api/activityendpoint/v1/activity/course/S23222
		// e.g. query https://lifelong-learning-hub.appspot.com/_ah/api/activityendpoint/v1/activity/user/Bernd
		// e.g. query https://lifelong-learning-hub.appspot.com/_ah/api/activityendpoint/v1/activity/course/S23222/user/Bernd	
		// listActivityCourseUser
		
		InputStream is = null;
		String url = Session.getSingleInstance().getWSPath()+"/_ah/api/activityendpoint/v1/activity/course/"+sCourseId+"/user/"+sUserId;

		try {
			Log.d(CLASSNAME, " Querying to backend listActivities course:" + sCourseId + " user:"+sUserId);
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
		ActivityDOList r = gson.fromJson(reader, ActivityDOList.class);
		List<ActivityDO> activities = r.activities;


		return activities;
	}

	@Override
	protected void onPreExecute() {
		super.onPreExecute();

	}

	@Override
	protected List<ActivityDO> doInBackground(String... sParam) {

		Log.d(CLASSNAME, "Starting "+CLASSNAME+" task in background...");
		List<ActivityDO> sOutList = new ArrayList<ActivityDO>();
		String sUser = "";
		String sCourse = "";

		try {

			sCourse = sParam[0].toString();
			sUser = sParam[1].toString();			
			
			Log.d(CLASSNAME, "Course ID [" + sParam[0].toString() + "]  User ID [" + sParam[1].toString() + "]");
			sOutList = listActivities(sCourse, sUser);
			
			// Log.d(CLASSNAME, toString(sOutList));


		} catch (Exception e) {
			e.printStackTrace();
		}

		return sOutList;
	}

	@Override
	protected void onPostExecute(List<ActivityDO> result) {
		super.onPostExecute(result);

	}


    
    /**
     * Print items in debug mode
     * @param list
     */
    public String toString(List<ActivityDO> list){
    	
    	String sResult = "";
    	for (ActivityDO ActivityDO : list) {
    		sResult+=ActivityDO.toString();
		}
    	
    	return sResult;
    	
    }		
	

}
