package org.ounl.lifelonglearninghub.nfclearntracker.scheduler;

import java.util.Calendar;

import org.ounl.lifelonglearninghub.nfclearntracker.TagColours;
import org.ounl.lifelonglearninghub.nfclearntracker.db.tables.Tag;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.content.WakefulBroadcastReceiver;
import android.util.Log;


/**
 * When the alarm fires, this WakefulBroadcastReceiver receives the broadcast Intent 
 * and then starts the IntentService {@code SampleSchedulingService} to do some work.
 */
public class SampleAlarmReceiver extends WakefulBroadcastReceiver {
	
	private String CLASSNAME = this.getClass().getName();

    private AlarmManager alarmMgr;

    private PendingIntent alarmIntent;
    
    private int iMinsss = 0;
    
  
    @Override
    public void onReceive(Context context, Intent intent) {   

    	Log.d(CLASSNAME, "onReceive");
        Intent service = new Intent(context, SampleSchedulingService.class);
        
//
//        Bundle extras = intent.getExtras();
//        if (extras != null) {
//        	iMins = extras.getInt("MINS");
//        	Log.d(CLASSNAME, "Params received. iMins: "+iMins);
//        }else{
//        	Log.d(CLASSNAME, "No params received");
//        }
        
        startWakefulService(context, service);

    }

    /**
     * Sets an alarm
     * @param context
     */
    public void setAlarm(Context context) {
    	
    	Log.d(CLASSNAME, "setAlarm");
    	    	
        alarmMgr = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, SampleAlarmReceiver.class);
        alarmIntent = PendingIntent.getBroadcast(context, 0, intent, 0);

        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.add(Calendar.MINUTE, iMinsss);

        Log.d(CLASSNAME, "Setting alarm at "+calendar.getTime());

        alarmMgr.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), alarmIntent);
        
        
        ComponentName receiver = new ComponentName(context, SampleBootReceiver.class);
        PackageManager pm = context.getPackageManager();

        pm.setComponentEnabledSetting(receiver,
                PackageManager.COMPONENT_ENABLED_STATE_ENABLED,
                PackageManager.DONT_KILL_APP);   
        
    }

    /**
     * Cancels the alarm.
     * @param context
     */
    public void cancelAlarm(Context context) {
    	
    	Log.i(CLASSNAME, "Alarm removed ");

        if (alarmMgr!= null) {
            alarmMgr.cancel(alarmIntent);
        }

        ComponentName receiver = new ComponentName(context, SampleBootReceiver.class);
        PackageManager pm = context.getPackageManager();

        pm.setComponentEnabledSetting(receiver,
                PackageManager.COMPONENT_ENABLED_STATE_DISABLED,
                PackageManager.DONT_KILL_APP);
    }
    
    
    /**
     * Delay for alarm in minutes
     * 
     * @param mins
     */
    public void setDelayMins(int mins) {
    	iMinsss = mins;
    }

}
