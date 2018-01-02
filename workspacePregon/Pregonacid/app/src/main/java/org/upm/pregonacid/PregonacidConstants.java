package org.upm.pregonacid;

import java.text.SimpleDateFormat;

public class PregonacidConstants {
	
	
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

	// Results from operations
	public final static int OPERATION_SUCCESS = 0;
	public final static int OPERATION_FAILED = 1;
	

	// PregonacidConstants in config.properties. Read only file. Asset file
	public final static String CONFIG_PROPERTIES_FILE = "config.properties";
	public final static String CP_VERSION = "version";
	public final static String CP_COURSE_ID = "course_id";
	public final static String CP_WS_PATH = "ws_path";
	public final static String CP_WS_GET_EVENTS_PATH = "ws_get_events_path";
	

	


}
