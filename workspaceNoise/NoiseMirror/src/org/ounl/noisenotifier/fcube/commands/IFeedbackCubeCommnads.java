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
package org.ounl.noisenotifier.fcube.commands;

import java.net.URL;

public interface IFeedbackCubeCommnads {
	
	public static final String HTTP_METHOD_PUT = "PUT";
	public static final String HTTP_METHOD_GET = "GET";
	
	public static final String ACTION_MELODY1 = "ACTION_MELODY1";
	public static final String ACTION_BEEP = "ACTION_BEEP";
	public static final String ACTION_COLOR = "ACTION_COLOR";
	public static final String ACTION_FADE = "ACTION_FADE";
	public static final String ACTION_PIECHART = "ACTION_PIECHART";
	public static final String ACTION_GENERIC = "ACTION_GENERIC";
	public static final String ACTION_OFF = "ACTION_OFF";
	public static final String ACTION_ON = "ACTION_ON";
	public static final String ACTION_RAINBOW = "ACTION_RAINBOW";
	public static final String ACTION_RAINBOW_CIRCLE = "ACTION_RAINBOW_CIRCLE";

	public URL getUrlCommand();

	public String toString();
	
	public boolean hasParams();
	
	public String getParams();
	
	public String getHttpMethod();
	
	public String getWSPath();
	
	public String getAction();
	

}
