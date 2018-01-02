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
package org.ounl.lifelonglearninghub.nfclearntracker.fcube.commands;

import java.net.MalformedURLException;
import java.net.URL;

import org.ounl.lifelonglearninghub.nfclearntracker.fcube.config.FeedbackCubeConfig;


public class FCPieChart implements IFeedbackCubeCommnads{
	/**
	 * * > PUT /ring/pixel/range/{"n1": 0,"n2": 15,"r": 255,"g": 0,"b": 0} HTTP/1.1  : Changes the color of the LED strip
	 * 
	 * Sets red color (255 0 0) in range pixels from n1 to n2
	 * Take into account that 15 is the maximum limit since there is only 16 leds  
	 * 
	 */
	private static final String WS_PATH = "/ring/pixel/range/";
	private String ipAdress = "";
	private String sStart = "0";
	private String sEnd = "0";
	private String sColorRed_DECIMAL = "0";
	private String sColorGreen_DECIMAL = "0";
	private String sColorBlue_DECIMAL = "0";
	
	
	public FCPieChart(String sIp, String start, String end, String red, String green, String blue){
		ipAdress = sIp;
		sStart = start;
		sEnd = end;
		sColorRed_DECIMAL = red;
		sColorGreen_DECIMAL = green;
		sColorBlue_DECIMAL = blue;
	}
	
	private String getCommand(){		
		
		return FeedbackCubeConfig.URL_PREFIX + ipAdress + WS_PATH; 
	}
	
	
	public URL getUrlCommand(){
		try {
			return new URL(getCommand());
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	@Override
	public String toString(){
		return "COMMAND FADE: URL["+getUrlCommand().toString()+"] COMMAND["+getCommand()+"] HAS PARAMS:["+hasParams()+"] PARAMS:["+getParams()+"] METHOD:["+getHttpMethod()+"]";
	}
	
	@Override
	public boolean hasParams() {
		return true;
	}

	
	@Override
	public String getParams() {
		
		String sJson = "{\"n1\":" + sStart +
				",\"n2\":" + sEnd + 
				",\"r\":" + sColorRed_DECIMAL +
				",\"g\":" + sColorGreen_DECIMAL + 
				",\"b\":" + sColorBlue_DECIMAL +"}";
				
		return sJson;
	}
	
	@Override
	public String getHttpMethod() {		
		return IFeedbackCubeCommnads.HTTP_METHOD_PUT;
	}	
	
	@Override
	public String getAction() {
		return ACTION_PIECHART;
	}	
	
	@Override
	public String getWSPath() {
		return WS_PATH;
	}	

}
