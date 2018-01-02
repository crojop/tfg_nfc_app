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
package org.ounl.lifelonglearninghub.learntracker.gis.ou;

import java.text.SimpleDateFormat;

/**
 * @author Bernardo Tabuenca
 *
 */
public class Constants {
	
	
	public static long SEM = 604800000;
	public static long HORA = 3600000;
	public static long CUARTOHORA = 900000;
	public static long MEDIAHORA = 1800000;
	public static long TRESCUARTOSHORA = 2700000;
	public static long DIA = 86400000;
	
	// Text sizes for charts
	public static final int TEXT_SIZE_XXXXHDPI = 52;
	public static final int TEXT_SIZE_XXXHDPI = 48;
	public static final int TEXT_SIZE_XXHDPI = 36;
	public static final int TEXT_SIZE_XHDPI = 24;
	public static final int TEXT_SIZE_HDPI = 20;
	public static final int TEXT_SIZE_MDPI = 18;
	public static final int TEXT_SIZE_LDPI = 13;
	
	public static final int DENSITY_XXHIGH = 480;
	
	public static SimpleDateFormat TIME_FORMAT = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss");	

	// User types and treatemnts
	public final static int USER_TYPE_SOCIAL = 1;
	public final static int USER_TYPE_YARDST = 2;
	
	// Results from operations
	public final static int OPERATION_SUCCESS = 0;
	public final static int OPERATION_FAILED = 1;
	

	// Constants in config.properties. Read only file. Asset file
	public final static String CONFIG_PROPERTIES_FILE = "config.properties";
	public final static String CP_VERSION = "version";
	public final static String CP_COURSE_ID = "course_id";
	public final static String CP_WS_PATH = "ws_path";
	
	// Constants in lllhub.properties. Read and write file. Sandbox file
	public final static String USER_PROPERTIES_FILE = "lllhub.properties";
	public final static String UP_USER_ID = "user_id";
	public final static String UP_USER_TYPE = "user_type";
	//public final static String UP_IMEI = "imei";
	


}
