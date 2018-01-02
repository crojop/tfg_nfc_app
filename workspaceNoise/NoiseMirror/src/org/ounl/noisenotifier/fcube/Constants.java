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
package org.ounl.noisenotifier.fcube;

/**
 * @author Bernardo Tabuenca
 * 
 */
public class Constants {

	// Results from operations
	public final static int OPERATION_SUCCESS = 0;
	public final static int OPERATION_FAILED = 1;

	// Constants in config.properties. Read / write file
	//public final static String CONFIG_PROPERTIES_FILE = "config.properties";
	public final static String CP_VERSION = "version";
	public final static String CP_TITLE = "title";
	public final static String CP_COMMAND = "command";
	public final static String CP_PARAMS = "params";
	public final static String CP_METHOD = "method";	
	public final static String CP_IP_ADDRESS = "ipaddress";
	
	// Sandbox file
	public final static String JUKEBOX_PROPERTIES_FILE = "jukebox.properties";
	public final static String JB_A = "A";
	public final static String JB_B = "B";
	public final static String JB_C = "C";
	public final static String JB_D = "D";
	public final static String JB_E = "E";
	public final static String JB_F = "F";
	public final static String JB_G = "G";
	public final static String JB_H = "H";
	public final static String JB_I = "I";
	public final static String JB_J = "J";
	
	
	// Levels of noise
	public final static int NOISE_LEVEL_INIT = -1;
	public final static int NOISE_LEVEL_0 = 0;
	public final static int NOISE_LEVEL_1 = 1;
	public final static int NOISE_LEVEL_2 = 2;
	public final static int NOISE_LEVEL_3 = 3;
	
	

}
