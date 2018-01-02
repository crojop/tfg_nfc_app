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
package org.ounl.lifelonglearninghub.nfcecology.fcube.jukebox;

import org.ounl.lifelonglearninghub.nfcecology.fcube.commands.IFeedbackCubeCommnads;

public class Sampler {
	
	private IFeedbackCubeCommnads mFCCommand;
	private String mTitle;
	
	
	public Sampler(IFeedbackCubeCommnads mFCCommand, String mTitle) {
		this.mFCCommand = mFCCommand;
		this.mTitle = mTitle;
	}	
	
	
	public IFeedbackCubeCommnads getmFCCommand() {
		return mFCCommand;
	}
	public void setmFCCommand(IFeedbackCubeCommnads mFCCommand) {
		this.mFCCommand = mFCCommand;
	}
	public String getmTitle() {
		return mTitle;
	}
	public void setmTitle(String mTitle) {
		this.mTitle = mTitle;
	}
	

}
