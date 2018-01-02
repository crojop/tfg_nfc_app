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
package org.ounl.noisenotifier.fcube.config;

import org.ounl.noisenotifier.NoiseUtils;
import org.ounl.noisenotifier.fcube.Constants;
import org.ounl.noisenotifier.feeback.FeedbackColor;
import org.ounl.noisenotifier.feeback.FeedbackColorFactory;


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
	
	private String ip_address = "";
	

	// Number of polls to be made to calculate the averag
	public static final int NUM_POLLS = 5;
	
	//private static final int POLL_INTERVAL = 300;
	public static final int POLL_INTERVAL = 1000; // 1 sec
	//public static final int POLL_INTERVAL = 2000; // 2 sec
	//public static final int POLL_INTERVAL = 3000; // 3 sec
	//private static final int POLL_INTERVAL = 5000; // 5 sec
	
	// Limits for alerting in decibelius
//	public static final int THRESHOLD_0 = 0;
//	public static final int THRESHOLD_1 = 10;
//	public static final int THRESHOLD_2 = 20;
	
	
//	public static final int mThresholdMax = +25;
	public static final int INIT_THRESHOLD = -25;
	public Double mThresholdMax;
	public Double mThresholdMin;
	
	// Current status of the cube
	private int mNoiseLevel = Constants.NOISE_LEVEL_INIT;

	
	// Current poll iteration
	private int mPollIndex = 0;
	
	// Buffer
	private double[] mNoiseArray  = {INIT_THRESHOLD,INIT_THRESHOLD,INIT_THRESHOLD,INIT_THRESHOLD,INIT_THRESHOLD};
	
	// Color gradient
	private FeedbackColorFactory mfccFactory = new FeedbackColorFactory();
	
	
	
	
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
	//private HashMap<String, Sampler> hmJukeBox = new HashMap<String, Sampler>();

	

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
	



	/**
	 * @return the mNoiseLevel
	 */
	public int getmNoiseLevel() {
		return mNoiseLevel;
	}


	/**
	 * @param mNoiseLevel the mNoiseLevel to set
	 */
	public void setmNoiseLevel(int mNoiseLevel) {
		this.mNoiseLevel = mNoiseLevel;
	}


	/**
	 * Returns current value of the index
	 *  
	 * @return the mPollIndex
	 */
	public int getPollIndex() {

		return mPollIndex;
	}


	/**
	 * @param mPollIndex sets 0
	 */
	public void resetPollIndex() {
		this.mPollIndex = 0;
	}


	/**
	 * @return the mNoiseArray
	 */
	public double getAverageNoise() {
		
		double dSum = 0;
		
		for (int i = 0; i < mNoiseArray.length; i++) {
			dSum = mNoiseArray[i] + dSum;
			
		}
		
		return (dSum / NUM_POLLS);
	}

	
	/**
	 * Returns color for the current average values from the buffer
	 * 
	 * @return the mNoiseArray
	 */
	public FeedbackColor getBufferColor() {
		
		double dSum = 0;
		double dAVGBufferNoise = 0;
		
		for (int i = 0; i < mNoiseArray.length; i++) {
			dSum = mNoiseArray[i] + dSum;			
		}
			
		// Calculate average from the buffer
		dAVGBufferNoise =  (dSum / NUM_POLLS);

		//
		// Look for mapping of the noise within the color gradient
		//		
		
		NoiseUtils nu = new NoiseUtils();
		return nu.getFeedbackColor(dAVGBufferNoise, mThresholdMax, mThresholdMin);
		
		

	}	

	/**
	 * @param adds noise item to buffer increasing counter
	 * @return the increase mPollIndex
	 * 
	 */
	public int addNoiseItem(double dNoiseItem) {
		this.mNoiseArray[mPollIndex] = dNoiseItem;
		mPollIndex++;
		return mPollIndex;
	}


	/**
	 * @return the mThresholdMax
	 */
	public Double getmThresholdMax() {
		return mThresholdMax;
	}


	/**
	 * @param mThresholdMax the mThresholdMax to set
	 */
	public void setmThresholdMax(Double mThresholdMax) {
		this.mThresholdMax = mThresholdMax;
	}


	/**
	 * @return the mThresholdMin
	 */
	public Double getmThresholdMin() {
		return mThresholdMin;
	}


	/**
	 * @param mThresholdMin the mThresholdMin to set
	 */
	public void setmThresholdMin(Double mThresholdMin) {
		this.mThresholdMin = mThresholdMin;
	}
	
}
