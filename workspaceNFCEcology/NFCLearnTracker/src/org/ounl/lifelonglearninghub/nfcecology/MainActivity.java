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
package org.ounl.lifelonglearninghub.nfcecology;

import java.nio.charset.Charset;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import org.ounl.lifelonglearninghub.nfcecology.db.CreateGoalActivity;
import org.ounl.lifelonglearninghub.nfcecology.db.DatabaseHandler;
import org.ounl.lifelonglearninghub.nfcecology.db.tables.Goal;
import org.ounl.lifelonglearninghub.nfcecology.db.tables.Tagctivity;
import org.ounl.lifelonglearninghub.nfcecology.fcube.MainCubeActivity;
import org.ounl.lifelonglearninghub.nfcecology.fcube.commands.FCColor;
import org.ounl.lifelonglearninghub.nfcecology.fcube.commands.FCOff;
import org.ounl.lifelonglearninghub.nfcecology.fcube.commands.FCOn;
import org.ounl.lifelonglearninghub.nfcecology.fcube.config.FeedbackCubeConfig;
import org.ounl.lifelonglearninghub.nfcecology.fcube.config.FeedbackCubeManager;
import org.ounl.lifelonglearninghub.nfcecology.nfcrecord.IParsedNdefRecord;
import org.ounl.lifelonglearninghub.nfcecology.nfcrecord.NdefMessageParser;
import org.ounl.lifelonglearninghub.nfcecology.scheduler.SampleAlarmReceiver;
import org.ounl.lifelonglearninghub.nfcecology.swipe.SwipeActivity;
import org.ounl.lifelonglearninghub.nfcecology.R;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Intent;
import android.graphics.Color;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.nfc.tech.MifareClassic;
import android.nfc.tech.MifareUltralight;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;


public class MainActivity extends Activity {

	private String CLASSNAME = this.getClass().getSimpleName();
	
	private LinearLayout mTagContent;
	private static SimpleDateFormat TIME_FORMAT = new SimpleDateFormat(
			"dd/MM/yyyy hh:mm:ss");
	private static SimpleDateFormat DATE_FORMAT = new SimpleDateFormat(
			"yyyy-MM-dd");

	private NfcAdapter mAdapter;
	private PendingIntent mPendingIntent;
	private NdefMessage mNdefPushMessage;

	private AlertDialog mDialog;

	private DatabaseHandler db;
	
	SampleAlarmReceiver alarm = new SampleAlarmReceiver();
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		db = new DatabaseHandler(getApplicationContext());

		mTagContent = (LinearLayout) findViewById(R.id.list);
		resolveIntent(getIntent());
		mDialog = new AlertDialog.Builder(this).setNeutralButton("Ok", null)
				.create();

		// NFC preparation
		mAdapter = NfcAdapter.getDefaultAdapter(this);
		if (mAdapter == null) {
			showMessage(R.string.error, R.string.no_nfc);
		}
		mPendingIntent = PendingIntent.getActivity(this, 0, new Intent(this,
				getClass()).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP), 0);
		mNdefPushMessage = new NdefMessage(new NdefRecord[] { newTextRecord(
				"Message from NFC Reader :-)", Locale.ENGLISH, true) });
	}

	private void showMessage(int title, int message) {
		Log.d(CLASSNAME, "showMessage Title:" + title + " Mesagge:"
				+ message);
		mDialog.setTitle(title);
		mDialog.setMessage(getText(message));
		mDialog.show();
	}
	
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }	
    
    // Menu options to set and cancel the alarm.
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // When the user clicks START ALARM, set the alarm.
            case R.id.start_action:
            	
    			Intent intent = new Intent(this, MainCubeActivity.class);
//    			intent.putExtra(
//    					org.ounl.lifelonglearninghub.nfcecology.db.tables.Tag.KEY_ID_TAG,
//    					record.getId());
//    			intent.putExtra(TagColours.TAG_COLOR, record.getColor());
    			startActivity(intent);

            	
        		//PASAR PARAMETROS AQUI O QUITA LOS BOTONES
//        		Intent intent = new Intent();
//        		intent.putExtra("MINS", 2);
//        		this.setIntent(intent);
//            	alarm.setDelayMins(2);
//                alarm.setAlarm(this);
//                return true;
//            // When the user clicks CANCEL ALARM, cancel the alarm. 
//            case R.id.cancel_action:
//                alarm.cancelAlarm(this);
                return true;
        }
        return false;
    }    

	private NdefRecord newTextRecord(String text, Locale locale,
			boolean encodeInUtf8) {
		Log.d(CLASSNAME, "nextTextRecord Text:" + text + " Locale:"
				+ locale.toString() + " encodeinUtf8:" + encodeInUtf8);
		byte[] langBytes = locale.getLanguage().getBytes(
				Charset.forName("US-ASCII"));

		Charset utfEncoding = encodeInUtf8 ? Charset.forName("UTF-8") : Charset
				.forName("UTF-16");
		byte[] textBytes = text.getBytes(utfEncoding);

		int utfBit = encodeInUtf8 ? 0 : (1 << 7);
		char status = (char) (utfBit + langBytes.length);

		byte[] data = new byte[1 + langBytes.length + textBytes.length];
		data[0] = (byte) status;
		System.arraycopy(langBytes, 0, data, 1, langBytes.length);
		System.arraycopy(textBytes, 0, data, 1 + langBytes.length,
				textBytes.length);

		return new NdefRecord(NdefRecord.TNF_WELL_KNOWN, NdefRecord.RTD_TEXT,
				new byte[0], data);
	}

	@SuppressWarnings("deprecation")
	@Override
	protected void onResume() {
		super.onResume();
		Log.d(CLASSNAME, "onResume");
		if (mAdapter != null) {
			if (!mAdapter.isEnabled()) {
				showMessage(R.string.error, R.string.nfc_disabled);
			}
			mAdapter.enableForegroundDispatch(this, mPendingIntent, null, null);
			mAdapter.enableForegroundNdefPush(this, mNdefPushMessage);
		}
	}

	@SuppressWarnings("deprecation")
	@Override
	protected void onPause() {
		super.onPause();
		Log.d(CLASSNAME, "onPause");
		if (mAdapter != null) {
			mAdapter.disableForegroundDispatch(this);
			mAdapter.disableForegroundNdefPush(this);
		}
	}

	private void resolveIntent(Intent intent) {
		Log.d(CLASSNAME, "resolveIntent intent:" + intent.toString());
		String action = intent.getAction();
		if (NfcAdapter.ACTION_TAG_DISCOVERED.equals(action)
				|| NfcAdapter.ACTION_TECH_DISCOVERED.equals(action)
				|| NfcAdapter.ACTION_NDEF_DISCOVERED.equals(action)) {
			Parcelable[] rawMsgs = intent
					.getParcelableArrayExtra(NfcAdapter.EXTRA_NDEF_MESSAGES);
			NdefMessage[] msgs;
			if (rawMsgs != null) {
				msgs = new NdefMessage[rawMsgs.length];
				for (int i = 0; i < rawMsgs.length; i++) {
					msgs[i] = (NdefMessage) rawMsgs[i];
				}
			} else {
				// Unknown tag type
				byte[] empty = new byte[0];
				byte[] id = intent.getByteArrayExtra(NfcAdapter.EXTRA_ID);
				Parcelable tag = intent
						.getParcelableExtra(NfcAdapter.EXTRA_TAG);
				byte[] payload = dumpTagData(tag).getBytes();
				NdefRecord record = new NdefRecord(NdefRecord.TNF_UNKNOWN,
						empty, id, payload);
				NdefMessage msg = new NdefMessage(new NdefRecord[] { record });
				msgs = new NdefMessage[] { msg };
			}

			// Setup the views
			// buildTagViews(msgs);

			// Insert record into DB
			processNdefMessageArray(msgs);

		}
	}

	private String dumpTagData(Parcelable p) {
		Log.d(CLASSNAME, "dumpTagData Parcelable" + p);
		StringBuilder sb = new StringBuilder();
		Tag tag = (Tag) p;
		byte[] id = tag.getId();
		sb.append("Tag ID (hex): ").append(getHex(id)).append("\n");
		sb.append("Tag ID (dec): ").append(getDec(id)).append("\n");

		String prefix = "android.nfc.tech.";
		sb.append("Technologies: ");
		for (String tech : tag.getTechList()) {
			sb.append(tech.substring(prefix.length()));
			sb.append(", ");
		}
		sb.delete(sb.length() - 2, sb.length());
		for (String tech : tag.getTechList()) {
			if (tech.equals(MifareClassic.class.getName())) {
				sb.append('\n');
				MifareClassic mifareTag = MifareClassic.get(tag);
				String type = "Unknown";
				switch (mifareTag.getType()) {
				case MifareClassic.TYPE_CLASSIC:
					type = "Classic";
					break;
				case MifareClassic.TYPE_PLUS:
					type = "Plus";
					break;
				case MifareClassic.TYPE_PRO:
					type = "Pro";
					break;
				}
				sb.append("Mifare Classic type: ");
				sb.append(type);
				sb.append('\n');

				sb.append("Mifare size: ");
				sb.append(mifareTag.getSize() + " bytes");
				sb.append('\n');

				sb.append("Mifare sectors: ");
				sb.append(mifareTag.getSectorCount());
				sb.append('\n');

				sb.append("Mifare blocks: ");
				sb.append(mifareTag.getBlockCount());
			}

			if (tech.equals(MifareUltralight.class.getName())) {
				sb.append('\n');
				MifareUltralight mifareUlTag = MifareUltralight.get(tag);
				String type = "Unknown";
				switch (mifareUlTag.getType()) {
				case MifareUltralight.TYPE_ULTRALIGHT:
					type = "Ultralight";
					break;
				case MifareUltralight.TYPE_ULTRALIGHT_C:
					type = "Ultralight C";
					break;
				}
				sb.append("Mifare Ultralight type: ");
				sb.append(type);
			}
		}

		return sb.toString();
	}

	private String getHex(byte[] bytes) {
		Log.d(CLASSNAME, "getHex:" + bytes);

		StringBuilder sb = new StringBuilder();
		for (int i = bytes.length - 1; i >= 0; --i) {
			int b = bytes[i] & 0xff;
			if (b < 0x10)
				sb.append('0');
			sb.append(Integer.toHexString(b));
			if (i > 0) {
				sb.append(" ");
			}
		}
		return sb.toString();
	}

	private long getDec(byte[] bytes) {
		long result = 0;
		long factor = 1;
		for (int i = 0; i < bytes.length; ++i) {
			long value = bytes[i] & 0xffl;
			result += value * factor;
			factor *= 256l;
		}
		return result;
	}

	void processNdefMessageArray(NdefMessage[] msgs) {
		
		Log.d(CLASSNAME, "Number of items in NdefMessage[] " + msgs.length + ".");
		if (msgs == null || msgs.length == 0) {
			return;
		}

		// Only the identifier of the first ndef message is considered.
		// The rest of the messages in the record are discarded
		//
		NdefMessageParser.printNdefMessages(msgs);
		IParsedNdefRecord record = NdefMessageParser.parseFirstNdefMessage(msgs[0]);
						
		// Currrent timestamp for checkin / checkout
		Date d = new Date();
		String sGoalId = db.getGoalId(record.getId(), d.getTime());

		
		// Check whether this tag is already mapped to a goal
		if (sGoalId.length() < 1) {

			// There is no goal mapped to this tag, then it is a new tag
			Intent intent = new Intent(this, CreateGoalActivity.class);
			intent.putExtra(
					org.ounl.lifelonglearninghub.nfcecology.db.tables.Tag.KEY_ID_TAG,
					record.getId());
			intent.putExtra(TagColours.TAG_COLOR, record.getColor());
			startActivity(intent);

		} else {

			// Tag already exists in database
			Tagctivity t = new Tagctivity(record.getId());
			
			// Update database
			boolean bIsCheckIn = db.addTagctivity(t, d.getTime());
			Goal g = db.getGoal(record.getId(), d.getTime());

			if (bIsCheckIn) {
				// Check in
				startTracker(g, t, d, record.getColor());

			} else {
				// Check out
				long lDuration = db.getDurationLastCheckOut(record.getId());
				stopTracker(g, d, lDuration);
				

			}


		}

	}

	@Override
	public void onNewIntent(Intent intent) {
		setIntent(intent);
		resolveIntent(intent);
	}

	public void onClickSwipeButton(View v) {
		Intent intent = new Intent(this, SwipeActivity.class);
		startActivity(intent);

	}
	
	
	/**
	 * Manage stop counting 
	 * 
	 * @param sGoalName
	 * @param tmstmp
	 * @param lDuration
	 */
	public void stopTracker(Goal goal, Date tmstmp, long lDuration) {

		LayoutInflater inflater = LayoutInflater.from(this);
		LinearLayout llNewRecord = (LinearLayout) inflater.inflate(R.layout.check_item, null);
		
		// Child 0 (Bullet + Text)
		LinearLayout llNewRecordContent = (LinearLayout) llNewRecord.getChildAt(0);
		ImageView ivParent = (ImageView) llNewRecordContent.getChildAt(0);
		TextView tvParent = (TextView) llNewRecordContent.getChildAt(1);
		tvParent.setText(goal.getsName());
		tvParent.setTextColor(Color.parseColor("#E05904"));
		ivParent.setImageResource(R.drawable.stop_50x);
		Toast.makeText(getApplicationContext(), "Activity successfully recorded!", Toast.LENGTH_SHORT).show();
		
		
		// Child 1 (Timestamp)
		TextView tvChild = (TextView) llNewRecord.getChildAt(1);
		DateUtils du = new DateUtils();
		tvChild.setText("["+du.duration(lDuration)+"] "+TIME_FORMAT.format(tmstmp));
		tvChild.setTextColor(Color.GRAY);
		
		
		// Modify previous one
		try{
			LinearLayout llPrevRecord = (LinearLayout)mTagContent.getChildAt(0);
			LinearLayout llPrevRecordContent = (LinearLayout) llPrevRecord.getChildAt(0);
			ImageView ivPrevParent = (ImageView) llPrevRecordContent.getChildAt(0);
			ivPrevParent.setImageResource(R.drawable.start_50x);
	
			Animation anim = new AlphaAnimation(0.0f, 1.0f);
			anim.setRepeatCount(0);
			ivPrevParent.setAnimation(anim);
		}catch(ClassCastException cce){
			Log.d(CLASSNAME, "First check in.");
		}

		

		mTagContent.addView(llNewRecord, 0);
		mTagContent.addView(inflater.inflate(R.layout.tag_divider, mTagContent, false), 1);
		
		
		//
		// Feedback from cube
		//
		//
		
		// Switch off alarm
		new FeedbackCubeManager().execute(new FCOff(FeedbackCubeConfig.getSingleInstance().getIp()));
		

// COMMENTADAS PARA DEMO.DESCOMENTA LAS SIGUIENTES LINEAS QUE STAN DE PUTA MADRE		
		// Set alarm
//		alarm.cancelAlarm(this);		
		
	}
	
	
	/**
	 * Manage start counting 
	 * 
	 * @param sGoalName
	 * @param tmstmp
	 */
	public void startTracker(Goal goal, Tagctivity tag, Date tmstmp, String color) {

		LayoutInflater inflater = LayoutInflater.from(this);
		
		LinearLayout llParent = (LinearLayout) inflater.inflate(R.layout.check_item, null);
		LinearLayout liContent = (LinearLayout) llParent.getChildAt(0);
		TextView tvChild = (TextView) llParent.getChildAt(1);
		tvChild.setText(TIME_FORMAT.format(tmstmp));
		tvChild.setTextColor(Color.GRAY);

		ImageView ivParent = (ImageView) liContent.getChildAt(0);
		TextView tvParent = (TextView) liContent.getChildAt(1);
		
		tvParent.setText(goal.getsName());

		// Check in
		Animation anim = new AlphaAnimation(0.0f, 1.0f);
		anim.setDuration(500);
		anim.setStartOffset(20);
		anim.setRepeatMode(Animation.REVERSE);
		anim.setRepeatCount(Animation.INFINITE);

		ivParent.setImageResource(R.drawable.rec_50x);
		ivParent.setAnimation(anim);

		tvParent.setTextColor(Color.parseColor("#237ADE"));
		// ivParent.setImageResource(R.drawable.start_50x);
		Toast.makeText(getApplicationContext(), "Starting activity ..."+goal.getsDesc() + " / "+color,
				Toast.LENGTH_SHORT).show();

		mTagContent.addView(llParent, 0);
		mTagContent.addView(
				inflater.inflate(R.layout.tag_divider, mTagContent, false), 1);		
		
		//
		// Feedback from cube
		//
		//
		
		// Switch on 
		new FeedbackCubeManager().execute(new FCOn(FeedbackCubeConfig.getSingleInstance().getIp()));
		
		new FeedbackCubeManager().execute(new FCColor(FeedbackCubeConfig.getSingleInstance().getIp(), color));
		

// COMMENTADAS PARA DEMO.DESCOMENTA LAS SIGUIENTES LINEAS QUE STAN DE PUTA MADRE
//		//
//		// Schedule fade
//		//
//		// Get accumulated activity for given goal in milliseconds
//		long lDurat = db.getDurationActivityGivenDay(tag.getsIdTagctivity(), DATE_FORMAT.format(tmstmp));
//		
//		// Calculate pending time
//		long lPending = goal.getDailyTimeMills() - lDurat;	
//		if (lPending < 0 ){
//			lPending = 0;
//		}
//		DateUtils du = new DateUtils();
//		
//		Log.d(CLASSNAME, "Alarm to be triggered in " + du.toMins(lPending) + " minutes");
//		
//		// Set alarm with delay
//		alarm.setDelayMins((int)du.toMins(lPending));
//		alarm.setAlarm(this);
		
	}

	
	

}
