package org.ounl.lifelonglearninghub.nfclearntracker.scheduler;

import org.ounl.lifelonglearninghub.nfclearntracker.MainActivity;
import org.ounl.lifelonglearninghub.nfclearntracker.R;
import org.ounl.lifelonglearninghub.nfclearntracker.fcube.commands.FCFade;
import org.ounl.lifelonglearninghub.nfclearntracker.fcube.config.FeedbackCubeConfig;
import org.ounl.lifelonglearninghub.nfclearntracker.fcube.config.FeedbackCubeManager;

import android.app.IntentService;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.Toast;

/**
 * This {@code IntentService} does the app's actual work.
 * {@code SampleAlarmReceiver} (a {@code WakefulBroadcastReceiver}) holds a
 * partial wake lock for this service while the service does its work. When the
 * service is finished, it calls {@code completeWakefulIntent()} to release the
 * wake lock.
 */
public class SampleSchedulingService extends IntentService {

	private String CLASSNAME = this.getClass().getName();

	// An ID used to post the notification.
	public static final int NOTIFICATION_ID = 1;

	public SampleSchedulingService() {
		super("SchedulingService");
	}

	private NotificationManager mNotificationManager;
	NotificationCompat.Builder builder;

	@Override
	protected void onHandleIntent(Intent intent) {

		
		// Fade params: (number, delay)
		new FeedbackCubeManager().execute( new FCFade(FeedbackCubeConfig.getSingleInstance().getIp(), "5", "3"));

		Toast.makeText(this, " TIME IS UP!!!!!!!!! ", Toast.LENGTH_LONG).show();
		Log.i(CLASSNAME, " TIME IS UP!!!!!!!!! ");

//		// TODO You need to give as aparam here the name of the task.
//		Time devoted to task XXX is over
		
		
		sendNotification(getString(R.string.task_over));
		Log.i(CLASSNAME, getString(R.string.task_over));

		// Release the wake lock provided by the BroadcastReceiver.
		SampleAlarmReceiver.completeWakefulIntent(intent);
		// END_INCLUDE(service_onhandle)
	}

	// Post a notification indicating whether a doodle was found.
	private void sendNotification(String msg) {
		mNotificationManager = (NotificationManager) this
				.getSystemService(Context.NOTIFICATION_SERVICE);

		PendingIntent contentIntent = PendingIntent.getActivity(this, 0,
				new Intent(this, MainActivity.class), 0);

		NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(
				this).setSmallIcon(R.drawable.ic_launcher)
				.setContentTitle(getString(R.string.learn_tracker_alert))
				.setStyle(new NotificationCompat.BigTextStyle().bigText(msg))
				.setContentText(msg);

		mBuilder.setContentIntent(contentIntent);
		mNotificationManager.notify(NOTIFICATION_ID, mBuilder.build());
	}

}
