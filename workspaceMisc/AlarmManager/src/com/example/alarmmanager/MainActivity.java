package com.example.alarmmanager;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import java.util.Calendar;

public class MainActivity extends Activity {
	
	private String CLASSNAME = this.getClass().getName();

    private PendingIntent pendingIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /* Retrieve a PendingIntent that will perform a broadcast */
        Intent alarmIntent = new Intent(MainActivity.this, AlarmReceiver.class);
        pendingIntent = PendingIntent.getBroadcast(MainActivity.this, 0, alarmIntent, 0);

        findViewById(R.id.startAlarm).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                start();
            }
        });

        findViewById(R.id.stopAlarm).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cancel();
            }
        });

        findViewById(R.id.startAlarmAt10).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startAt10();
            }
        });
    }

    public void start() {
        AlarmManager manager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        int interval = 8000;

        manager.setInexactRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), interval, pendingIntent);
        
        Toast.makeText(this, "Alarm Set", Toast.LENGTH_SHORT).show();
        Log.d(CLASSNAME, "Alarm set");
    }

    public void cancel() {
        AlarmManager manager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        manager.cancel(pendingIntent);
        
        Toast.makeText(this, "Alarm Canceled", Toast.LENGTH_SHORT).show();
        Log.d(CLASSNAME, "Alarm canceled");
    }

    public void startAt10() {
        AlarmManager manager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        
        /* Repeating on every iMinsInterval minutes interval */
        int iMinsInterval = 1;
        int iHH = 15;
        int iMM = 00;
        
        
        int interval = 1000 * 60 * iMinsInterval;

        /* Set the alarm to start at HH:MM  */
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.set(Calendar.HOUR_OF_DAY, iHH);
        calendar.set(Calendar.MINUTE, iMM);

        
        manager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),
                interval, pendingIntent);
        
        Toast.makeText(this, "Alarm set at "+iHH+":"+iMM, Toast.LENGTH_SHORT).show();
        Log.d(CLASSNAME, "Alarm set at "+iHH+":"+iMM+" every "+iMinsInterval+" minutes");        
    }

}