package org.ounl.lifelonglearninghub.learnbeacon;

import android.app.Application;
import android.content.Context;

/**
 * The {@link Application} for this demo application.
 */
public class CastApplication extends Application {
	private static String APPLICATION_ID;

	public static final double VOLUME_INCREMENT = 0.05;
	public static final String COLOR_ORANGE = "#FCC668";

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.app.Application#onCreate()
	 */
	@Override
	public void onCreate() {
		super.onCreate();
		APPLICATION_ID = "aaaa";

	}
	
	
    public static String getAppId(Context context) {
        if (null == APPLICATION_ID) {
            APPLICATION_ID = "fff";

        }


        return APPLICATION_ID;
    }	

}