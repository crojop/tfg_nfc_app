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

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.os.AsyncTask;
import android.util.Log;

public class WSPostAsyncTask extends AsyncTask<String, Integer, List<String>> {


	public List<String> nfcRequest(String sIn){

		InputStream is = null;
		String result = "";

		// Http post
		try {
			HttpClient httpclient = new DefaultHttpClient();
			//HttpPost httppost = new HttpPost("http://www.1902.es/events/time.php");
			
			
			HttpPost httppost = new HttpPost("http://www.1902.es/checkme/wsactivation");
			
			ArrayList<NameValuePair> postParameters;
			
			postParameters = new ArrayList<NameValuePair>();
			postParameters.add(new BasicNameValuePair("user_id", "1902_corp"));
			postParameters.add(new BasicNameValuePair("service", "fidelizacion"));
			postParameters.add(new BasicNameValuePair("branch_id", "600001234"));
			postParameters.add(new BasicNameValuePair("terminal_id", "0104"));
			postParameters.add(new BasicNameValuePair("serial_number", "SN-1234-G-4567"));
			postParameters.add(new BasicNameValuePair("date", "20140120"));
			postParameters.add(new BasicNameValuePair("time", "223025"));
			postParameters.add(new BasicNameValuePair("transaction_id", "000002"));
			postParameters.add(new BasicNameValuePair("transaction_type", "01"));
			postParameters.add(new BasicNameValuePair("code", "1234567890"));
			postParameters.add(new BasicNameValuePair("latitude", "40.4570784"));
			postParameters.add(new BasicNameValuePair("longitude", "-3.692387300000064"));
			postParameters.add(new BasicNameValuePair("Xtra", ""));

			httppost.setEntity(new UrlEncodedFormEntity(postParameters));

			

			// HttpPost httppost = new  HttpPost("http://www.movilucion.es/basemostrar.php");

			// Commented because now we need to send params
			// httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

			HttpResponse response = httpclient.execute(httppost);
			HttpEntity entity = response.getEntity();
			is = entity.getContent();

		} catch (Exception e) {
			Log.e("log_tag", "Error in http connection " + e.toString());
		}

		// Convert response to string
		try {
			BufferedReader reader = new BufferedReader(new InputStreamReader(is, "iso-8859-1"), 8);
			StringBuilder sb = new StringBuilder();
			String line = null;
			while ((line = reader.readLine()) != null) {
				sb.append(line + "\n");
			}
			is.close();

			result = sb.toString();

			System.out.println("Response: " + result);
		} catch (Exception e) {
			Log.e("log_tag", "Error converting result " + e.toString());
		}

		// Parse json data

		List<String> response = new ArrayList<String>();

		try {
			JSONArray jArray = new JSONArray(result);
			for (int i = 0; i < jArray.length(); i++) {
				JSONObject json_data = jArray.getJSONObject(i);
				System.out.println("Result: [" + i + "]:"
						+ json_data.toString());
				response.add("Result: [" + i + "]:" + json_data.toString());

				// Log.i("log_tag","id: "+json_data.getInt("id")+
				// ", name: "+json_data.getString("name")+
				// ", sex: "+json_data.getInt("sex")+
				// ", birthyear: "+json_data.getInt("birthyear")
				// );
			}
		} catch (JSONException e) {
			Log.e("log_tag", "Error parsing data " + e.toString());
		}

		return response;
	}

	@Override
	protected void onPreExecute() {
		super.onPreExecute();

	}

	@Override
	protected List<String> doInBackground(String... sParam) {

		System.out.println("Empieza el espectaculo en el asynctask");
		List<String> sOutList = new ArrayList<String>();

		try {

			System.out.println("Initializing device " + sParam[0].toString());

			sOutList = nfcRequest(sParam[0].toString());
			print(sOutList);
			System.out.println("Initialized device ");

			// System.out.println("Requesting content "+sParam[0].toString());
			// String sOutTextTv2 = nfcRequestContentSoap(sParam[0].toString());
			// System.out.println("Content launched: "+sOutTextTv2);

		} catch (Exception e) {
			e.printStackTrace();
		}

		return sOutList;
	}

	@Override
	protected void onPostExecute(List<String> result) {
		super.onPostExecute(result);

	}

	private void print(List<String> theList) {

		System.out.println("About to print the list with " + theList
				+ " items.");
		for (int i = 0; i < theList.size(); i++) {
			System.out.println(theList.get(i));

		}
	}

}
