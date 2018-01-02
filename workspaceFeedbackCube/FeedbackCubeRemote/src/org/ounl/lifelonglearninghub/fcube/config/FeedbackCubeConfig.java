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
package org.ounl.lifelonglearninghub.fcube.config;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Properties;
import java.util.Map.Entry;

import org.ounl.lifelonglearninghub.fcube.Constants;
import org.ounl.lifelonglearninghub.fcube.R;
import org.ounl.lifelonglearninghub.fcube.commands.FCColor;
import org.ounl.lifelonglearninghub.fcube.commands.FCGeneric;
import org.ounl.lifelonglearninghub.fcube.commands.IFeedbackCubeCommnads;
import org.ounl.lifelonglearninghub.fcube.jukebox.Sampler;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;


public class FeedbackCubeConfig {

	
	/**
	 * > GET / HTTP/1.1                      : Responds w/ "Hello from Arduino Server"
	 * > GET /ring/status/ HTTP/1.1          : Responds w/ "ON"/"OFF" for the power state of the strip
	 * > GET /ring/fade/ HTTP/1.1            : Responds w/ a JSON representation of 
	 *                                         the fading parameters -> {"n":x,"d":x}
	 * > GET /ring/color/ HTTP/1.1           : Responds w/ a JSON representation of 
	 *                                         the strip color -> {"r":x,"g":x,"b":x}
 	 * 
 	 *
	 * > PUT /ring/on/ HTTP/1.1              : Turns the LED strip on
	 * > PUT /ring/off/ HTTP/1.1             : Turns the LED strip off
	 * > PUT /ring/fade/ HTTP/1.1            : Color starts fading
	 *                                         The fading parameters (number, delay) are provided as a JSON object: {"n": x,"d": x}
	 * > PUT /ring/rainbow/ HTTP/1.1         : Starts a color rainbow
	 * > PUT /ring/rainbow/circle/ HTTP/1.1  : Starts a color rainbow cycle
	 * > PUT /ring/color/ HTTP/1.1           : Changes the color of the LED strip
	 *                                         The color values (red, green, blue) are provided as a JSON object: {"r": x,"g": x,"b": x}
	 * > PUT /speaker/beep/ HTTP/1.1         : Plays a beep
	 */
	
	private String CLASSNAME = this.getClass().getName();
	
	private String ip_address = "192.168.0.199";
	public static final String URL_PREFIX = "http://";
		
//	public static final String ACTION_ON = "ACTION_ON";
//	public static final String ACTION_OFF = "ACTION_OFF";
//	public static final String ACTION_FADE = "ACTION_FADE";
//	
//	public static final String ACTION_RAINBOW = "ACTION_RAINBOW";
//	public static final String ACTION_RAINBOW_CIRCLE = "ACTION_FADE_CIRCLE";
//	
//	public static final String ACTION_COLOR = "ACTION_COLOR";	
//	public static final String ACTION_BEEP = "ACTION_BEEP";
	

	private static FeedbackCubeConfig singleInstance;
	
	// Jukebox commands where String is the Tag from the button
	// and IFeedbackCubeCommnads is the command assigned to it
	private HashMap<String, Sampler> hmJukeBox = new HashMap<String, Sampler>();

	public static FeedbackCubeConfig getSingleInstance() {
		if (singleInstance == null) {
			synchronized (FeedbackCubeConfig.class) {
				if (singleInstance == null) {
					singleInstance = new FeedbackCubeConfig();
				}
			}
		}
		return singleInstance;
	}	
	

	public String getIp(){
		return ip_address;
	}
	
	public void setIp(String sIp) {
		ip_address = sIp;
	}
	
	public void initSamplers(){
		
		addSampler(
				Constants.JB_A,
				new Sampler(
						new FCGeneric("/ring/color/","{\"r\": 255,\"g\": 0,\"b\": 0}", "PUT"),
						"Red"
						)
				);
		
		addSampler(
				Constants.JB_B,
				new Sampler(
						new FCGeneric("/ring/color/","{\"r\": 0,\"g\": 255,\"b\": 0}", "PUT"),
						"Green"
						)
				);		

		addSampler(
				Constants.JB_C,
				new Sampler(
						new FCGeneric("/ring/color/","{\"r\": 0,\"g\": 0,\"b\": 255}", "PUT"),
						"Blue"
						)
				);		

		addSampler(
				Constants.JB_D,
				new Sampler(
						new FCGeneric("/ring/color/","{\"r\": 255,\"g\": 255,\"b\": 0}", "PUT"),
						"Yellow"
						)
				);		

		addSampler(
				Constants.JB_E,
				new Sampler(
						new FCGeneric("/ring/fade/","{\"n\": 3,\"d\": 10}", "PUT"),
						"Fade"
						)
				);		
		
		addSampler(
				Constants.JB_F,
				new Sampler(
						new FCGeneric("/ring/beep/","", "PUT"),
						"Beep"
						)
				);


		addSampler(
				Constants.JB_G,
				new Sampler(
						new FCGeneric("/ring/rainbow/","", "PUT"),
						"Rainbow"
						)
				);

		addSampler(
				Constants.JB_H,
				new Sampler(
						new FCGeneric("/ring/rainbow/circle/","", "PUT"),
						"Rbow Circ"
						)
				);

		addSampler(
				Constants.JB_I,
				new Sampler(
						new FCGeneric("/ring/on/","", "PUT"),
						"ON"
						)
				);

		addSampler(
				Constants.JB_J,
				new Sampler(
						new FCGeneric("/ring/off/","", "PUT"),
						"OFF"
						)
				);

		
		
	}
	
	/**
	 * Returns sampler from jukebox for a given tag
	 * 
	 * @param sTag
	 * @return
	 */
	public Sampler getSampler(String sTag){		
		return hmJukeBox.get(sTag);		
	}
	
	/**
	 * Returns all samplers
	 * 
	 * @return
	 */
	public HashMap<String, Sampler> getSamplers(){		
		return hmJukeBox;		
	}	
		
	/**
	 * Adds or replaces sampler for given sTag key
	 * @param sTag key
	 * @param s new sampler
	 */
	private void addSampler(String sTag, Sampler s){
		Log.d(CLASSNAME, "Adding sampler "+sTag+" to jukebox.");
		hmJukeBox.put(sTag, s);
	}
	
	
	public void addSampler(String sTag, Sampler sa, Context c){
		Log.d(CLASSNAME, "Adding sampler "+sTag+" to jukebox.");
		

		// Persist in memory
		addSampler(sTag, sa);
		
		
		// Persiste into jukebox file		
	    try {
	    	
	    	String sFile = Constants.JUKEBOX_PROPERTIES_FILE;
	    	OutputStreamWriter outputStreamWriter = new OutputStreamWriter(c.openFileOutput(sFile, Context.MODE_PRIVATE));

	    	// This line creates a file in the following path:
	    	// /data/data/org.ounl.lifelonglearninghub.learntracker/files
	    	
	    	String sIP = Constants.CP_IP_ADDRESS+ "=" + FeedbackCubeConfig.getSingleInstance().getIp() + "\n";
	        outputStreamWriter.write(sIP);

	    	for (Iterator<Entry<String, Sampler>> iterator = hmJukeBox.entrySet().iterator(); iterator.hasNext();) {
	    		Entry<String, Sampler> type = (Entry<String, Sampler>) iterator.next();
				
	    		Sampler s = (Sampler) type.getValue();
	    		IFeedbackCubeCommnads fc = (IFeedbackCubeCommnads)s.getmFCCommand();
	    		
		        String sTitle = type.getKey() + "." + Constants.CP_TITLE+ "=" + s.getmTitle() + "\n";
		        outputStreamWriter.write(sTitle);
		        
		        String sComma = type.getKey() + "." + Constants.CP_COMMAND+ "=" + fc.getWSPath() + "\n";
		        outputStreamWriter.write(sComma);
		        
		        String sParams = type.getKey() + "." + Constants.CP_PARAMS+ "=" + fc.getParams() + "\n";
		        outputStreamWriter.write(sParams);
		        
		        String sMethod = type.getKey() + "." + Constants.CP_METHOD+ "=" + fc.getHttpMethod() + "\n";
		        outputStreamWriter.write(sMethod);

		        		
		        
			}
	    	
	        outputStreamWriter.close();

	    }
	    catch (IOException e) {
	        Log.e("Exception", "File write failed: " + e.toString());
	    } 

	}
	
}
