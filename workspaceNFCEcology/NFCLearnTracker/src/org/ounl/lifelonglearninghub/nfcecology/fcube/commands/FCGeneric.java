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
package org.ounl.lifelonglearninghub.nfcecology.fcube.commands;

import java.net.MalformedURLException;
import java.net.URL;

import org.ounl.lifelonglearninghub.nfcecology.fcube.config.FeedbackCubeConfig;

import android.util.Log;

public class FCGeneric implements IFeedbackCubeCommnads{


	private String mCommand;
	private String mParams;
	private String mMethod;
	
	
	public FCGeneric(String sCommand, String sParams, String sMethod){
		
		mCommand = sCommand;
		mParams = sParams;
		mMethod = sMethod;
	}
	
	private String getCommand(){	
		return mCommand; 
	}
	
	public URL getUrlCommand(){
		String sUrl = "http://"+FeedbackCubeConfig.getSingleInstance().getIp()+getCommand();
		try {
			return new URL(sUrl);
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
		return null;
	}
	

	@Override
	public String toString(){
		return "COMMAND GENERIC: URL["+getUrlCommand().toString()+"] COMMAND["+getCommand()+"] HAS PARAMS:["+hasParams()+"] PARAMS:["+getParams()+"] METHOD:["+getHttpMethod()+"]";
	}
	
	@Override
	public boolean hasParams() {
		return mParams.length() > 0;
	}

	
	@Override
	public String getParams() {		
		return mParams;
	}

	@Override
	public String getHttpMethod() {		
		return mMethod;
	}

	@Override
	public String getAction() {
		return ACTION_GENERIC;
	}

	@Override
	public String getWSPath() {
		return mCommand;
	}
	
}
