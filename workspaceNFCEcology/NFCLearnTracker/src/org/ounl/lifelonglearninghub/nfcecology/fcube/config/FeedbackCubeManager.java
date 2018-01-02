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
package org.ounl.lifelonglearninghub.nfcecology.fcube.config;

import java.io.EOFException;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;

import org.ounl.lifelonglearninghub.nfcecology.fcube.commands.FCBeep;
import org.ounl.lifelonglearninghub.nfcecology.fcube.commands.FCColor;
import org.ounl.lifelonglearninghub.nfcecology.fcube.commands.FCFade;
import org.ounl.lifelonglearninghub.nfcecology.fcube.commands.FCOff;
import org.ounl.lifelonglearninghub.nfcecology.fcube.commands.FCOn;
import org.ounl.lifelonglearninghub.nfcecology.fcube.commands.FCRainbow;
import org.ounl.lifelonglearninghub.nfcecology.fcube.commands.FCRainbowCircle;
import org.ounl.lifelonglearninghub.nfcecology.fcube.commands.IFeedbackCubeCommnads;

import android.os.AsyncTask;
import android.util.Log;

public class FeedbackCubeManager extends AsyncTask<IFeedbackCubeCommnads, Void, String> {
	
	private String CLASSNAME = this.getClass().getName(); 

	@Override
	protected String doInBackground(IFeedbackCubeCommnads... params) {



		try {
			IFeedbackCubeCommnads Ifcc = params[0];
			Log.d(CLASSNAME, "Lunch command "+Ifcc.toString());
			run(Ifcc);
			Log.d(CLASSNAME, "Command launched! "+Ifcc.toString());
			
			
			
//			
//			if(Ifcc.contains(FCOn.ACTION)){
//				connOn();				
//			}else if (Ifcc.contains(FCOff.ACTION)){
//				connOff();
//			}else if (Ifcc.contains(FCRainbow.ACTION)){
//				connRainbow();
//			}else if (Ifcc.contains(FCRainbowCircle.ACTION)){
//				connRainbowCircle();
//			}else if (Ifcc.contains(FCBeep.ACTION)){
//				connBeep();				
//			}else if (Ifcc.contains(FCColor.ACTION)){
//				connColor(params[1], params[2], params[3]);
//			}else if (Ifcc.contains(FCFade.ACTION)){
//				connFade(params[1], params[2]);
//			}else{
//				System.out.println("Command not found "+Ifcc);
//			}
//			

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return "Executed "+params[0].getAction();
	}
	
//	private void connOn() {
////		FeedbackCubeConfig.getSingleInstance();
//		FCOn fcon = new FCOn(FeedbackCubeConfig.getSingleInstance().getIp());
//		run(fcon);
//	}
//	
//	private void connOff() {
////		FeedbackCubeConfig.getSingleInstance();
//		FCOff fcoff = new FCOff(FeedbackCubeConfig.getSingleInstance().getIp());
//		run(fcoff);
//	}
//	
//	private void connColor(String sRed, String sGreen, String sBlue) {
////		FeedbackCubeConfig.getSingleInstance();
//		FCColor fccolor = new FCColor(FeedbackCubeConfig.getSingleInstance().getIp(), sRed, sGreen, sBlue);
//		run(fccolor);
//	}
//	
//	private void connFade(String sNum, String sDel) {
////		FeedbackCubeConfig.getSingleInstance();
//		FCFade fcfade = new FCFade(FeedbackCubeConfig.getSingleInstance().getIp(), sNum, sDel);
//		run(fcfade);
//	}
//	
//
//	private void connRainbow() {
////		FeedbackCubeConfig.getSingleInstance();
//		FCRainbow fcon = new FCRainbow(FeedbackCubeConfig.getSingleInstance().getIp());
//		run(fcon);
//	}
//	
//	private void connRainbowCircle() {
////		FeedbackCubeConfig.getSingleInstance();
//		FCRainbowCircle fcon = new FCRainbowCircle(FeedbackCubeConfig.getSingleInstance().getIp());
//		run(fcon);
//	}
//	
//	private void connBeep() {
////		FeedbackCubeConfig.getSingleInstance();
//		FCBeep fcon = new FCBeep(FeedbackCubeConfig.getSingleInstance().getIp());
//		run(fcon);
//	}		
//	

	
	private void run(IFeedbackCubeCommnads cmd) {
		
		HttpURLConnection connection = null;

		try {

			connection = (HttpURLConnection) cmd.getUrlCommand().openConnection();
			connection.setRequestMethod(cmd.getHttpMethod());
			connection.setDoOutput(true);
			connection.setDoInput(true);
			connection.setRequestProperty( "Accept-Encoding", "" );
			
			
	        if (cmd.hasParams()){
		        
		        connection.setRequestProperty("Content-Type", "application/json");
		        connection.setRequestProperty("Accept", "application/json");
		        OutputStreamWriter osw = new OutputStreamWriter(connection.getOutputStream());
		        osw.write(cmd.getParams());
		        osw.flush();
		        osw.close();
		        
	        }
			
//	        if (Build.VERSION.SDK != null && Build.VERSION.SDK_INT > 13) { 
//        		connection.setRequestProperty("Connection", "close"); 
//	        }
	        
			connection.connect();
			connection.getResponseCode();

		} catch (EOFException e) {
			Log.e(CLASSNAME, "There seems to be some bug reading stream from webservice");
	        if (connection != null) {
	        	connection.disconnect();
	        }
			
	    } catch (Exception e) {
	        if (connection != null) {
	        	connection.disconnect();
	        }
			e.printStackTrace();
		}finally {
	        if (connection != null) {
	        	connection.disconnect();
	        }
	    }
		

	}
	
	
	

	@Override
	protected void onPostExecute(String result) {
		System.out.println("On poste execute: " + result);
	}

	@Override
	protected void onPreExecute() {
		System.out.println("On pre execute.");
	}

	@Override
	protected void onProgressUpdate(Void... values) {
		System.out.println("On progress update.");
	}
}