package com.example.alarmmanager;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

/**
 * @author Neel
 *         <p/>
 *         Broadcast reciever, starts when the device gets starts.
 *         Start your repeating alarm here.
 */
public class DeviceBootReceiver extends BroadcastReceiver {
	
	private String CLASSNAME = this.getClass().getName();

    @Override
    public void onReceive(Context context, Intent intent) {
    	
    	Log.d(CLASSNAME, "DeviceBootReciver.onReceive "+intent.getAction());
    	
        if (intent.getAction().equals("android.intent.action.BOOT_COMPLETED")) {
            /* Setting the alarm here */

            Intent alarmIntent = new Intent(context, AlarmReceiver.class);
            PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, alarmIntent, 0);
            
            AlarmManager manager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
            int interval = 8000;
            manager.setInexactRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), interval, pendingIntent);
            
            Toast.makeText(context, "DeviceBootReciver.onReceive Alarm Set", Toast.LENGTH_SHORT).show();
            Log.d(CLASSNAME, "DeviceBootReciver.onReceive Alarm set");
            
        }
    }
}