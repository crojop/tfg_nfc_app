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
package org.upm.pregonacid.db.ws;

import android.os.AsyncTask;
import android.util.Log;

import com.google.gson.Gson;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.upm.pregonacid.db.ws.dataobjects.EventDO;
import org.upm.pregonacid.db.ws.dataobjects.EventDOList;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class EventWSGetAsyncTask extends AsyncTask<String, Integer, List<EventDO>> {
	
	private String CLASSNAME = this.getClass().getName();


	public List<EventDO> listEventszzz(String urlString) {

		InputStream inputStream = null;
		URL url = null;
		HttpURLConnection connection = null;


		try{
			url = new URL(urlString);

			HttpParams httpParameters = new BasicHttpParams();
			// Set the timeout in milliseconds until a connection is established.
			int timeoutConnection = 3000;
			HttpConnectionParams.setConnectionTimeout(httpParameters, timeoutConnection);



			connection = (HttpURLConnection)url.openConnection();
			//connection.setRequestProperty("User-Agent", "");
			//connection.setRequestMethod("GET");
			//connection.setDoInput(true);
			//connection.connect();
			inputStream = new BufferedInputStream(connection.getInputStream());


		}
		catch (IOException e) {
			// Writing exception to log
			e.printStackTrace();
		}finally {
			connection.disconnect();
		}

		Reader reader = new InputStreamReader(inputStream);
		Gson gson = new Gson();
		EventDOList r = gson.fromJson(reader, EventDOList.class);
		List<EventDO> events = r.events;

		return events;

	}

	public List<EventDO> listEvents(String url) {

		InputStream is = null;

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
		EventDOList r = gson.fromJson(reader, EventDOList.class);
		List<EventDO> events = r.events;


		return events;
	}

	@Override
	protected void onPreExecute() {
		super.onPreExecute();

	}

	@Override
	protected List<EventDO> doInBackground(String... sParam) {

		Log.d(CLASSNAME, "Starting "+CLASSNAME+" task in background...");		
		List<EventDO> sOutList = new ArrayList<EventDO>();

		try {

			Log.e(CLASSNAME, "WS GET EVENT PATH [" + sParam[0].toString() + "]");
			sOutList = listEvents(sParam[0].toString());
			Log.e(CLASSNAME, toString(sOutList));

		} catch (Exception e) {
			e.printStackTrace();
		}

		return sOutList;
	}

	@Override
	protected void onPostExecute(List<EventDO> result) {
		super.onPostExecute(result);

	}


	

    /**
     * Print items in debug mode
     * @param list
     */
    public String toString(List<EventDO> list){
    	
    	String sResult = "";
    	for (EventDO eventDO : list) {
    		sResult+=eventDO.toString();
		}
    	
    	return sResult;
    	
    }		

}
