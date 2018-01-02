/*
 * Copyright (C) 2013 Google Inc. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.ounl.lifelonglearninghub.mediaplayer.cast.refplayer;

import java.nio.charset.Charset;
import java.util.Locale;

import org.ounl.lifelonglearninghub.mediaplayer.R;
import org.ounl.lifelonglearninghub.mediaplayer.cast.refplayer.browser.VideoBrowserListFragment;
import org.ounl.lifelonglearninghub.mediaplayer.cast.refplayer.settings.CastPreference;
import org.ounl.lifelonglearninghub.mediaplayer.nfc.nfcrecord.IParsedNdefCommand;
import org.ounl.lifelonglearninghub.mediaplayer.nfc.nfcrecord.NdefMessageParser;

import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.nfc.tech.MifareClassic;
import android.nfc.tech.MifareUltralight;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Parcelable;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.MediaRouteButton;
import android.support.v7.media.MediaRouter.RouteInfo;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.github.amlcurran.showcaseview.ShowcaseView;
import com.github.amlcurran.showcaseview.targets.ViewTarget;
import com.google.sample.castcompanionlibrary.cast.VideoCastManager;
import com.google.sample.castcompanionlibrary.cast.callbacks.IVideoCastConsumer;
import com.google.sample.castcompanionlibrary.cast.callbacks.VideoCastConsumerImpl;
import com.google.sample.castcompanionlibrary.widgets.MiniController;

public class NFCVideoBrowserActivity extends ActionBarActivity {

	private String CLASSNAME = this.getClass().getName();

	private static final String TAG = "NFCVideoBrowserActivity";
	private VideoCastManager mCastManager;
	private IVideoCastConsumer mCastConsumer;
	private MiniController mMini;
	private MenuItem mediaRouteMenuItem;
	private ListView lvVideoBrowserList;
	boolean mIsHoneyCombOrAbove = Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB;
	private Toolbar mToolbar;
	
	// NFC
	private NfcAdapter mAdapter;
	private PendingIntent mPendingIntent;
	private NdefMessage mNdefPushMessage;
	private AlertDialog mDialog;	


	/*
	 * (non-Javadoc)
	 * 
	 * @see android.support.v4.app.FragmentActivity#onCreate(android.os.Bundle)
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Log.d(TAG, "onCreate() is called");
		
		VideoCastManager.checkGooglePlayServices(this);
		setContentView(R.layout.video_browser);
		
		
		mCastManager = CastApplication.getCastManager(this);
		
		// -- Adding MiniController
		mMini = (MiniController) findViewById(R.id.miniController1);
		mCastManager.addMiniController(mMini);

		mCastConsumer = new VideoCastConsumerImpl() {

			@Override
			public void onFailed(int resourceId, int statusCode) {

			}

			@Override
			public void onConnectionSuspended(int cause) {
				Log.d(TAG, "onConnectionSuspended() was called with cause: "
						+ cause);
				org.ounl.lifelonglearninghub.mediaplayer.cast.refplayer.utils.Utils
						.showToast(NFCVideoBrowserActivity.this,
								R.string.connection_temp_lost);
			}

			@Override
			public void onConnectivityRecovered() {
				org.ounl.lifelonglearninghub.mediaplayer.cast.refplayer.utils.Utils
						.showToast(NFCVideoBrowserActivity.this,
								R.string.connection_recovered);
			}

			@Override
			public void onCastDeviceDetected(final RouteInfo info) {
				if (!CastPreference.isFtuShown(NFCVideoBrowserActivity.this)
						&& mIsHoneyCombOrAbove) {
					CastPreference.setFtuShown(NFCVideoBrowserActivity.this);

					Log.d(TAG, "Route is visible: " + info);
					new Handler().postDelayed(new Runnable() {

						@Override
						public void run() {
							if (mediaRouteMenuItem.isVisible()) {
								Log.d(TAG,
										"Cast Icon is visible: "
												+ info.getName());
								showFtu();
							}
						}
					}, 1000);
				}
			}
		};

		setupActionBar();

		mCastManager.reconnectSessionIfPossible(this, false);

		//
		// NFC
		//		
		resolveIntent(getIntent());
		mDialog = new AlertDialog.Builder(this).setNeutralButton("Ok", null)
				.create();

		mAdapter = NfcAdapter.getDefaultAdapter(this);
		if (mAdapter == null) {
			showMessage(R.string.error, R.string.no_nfc);
		}
		mPendingIntent = PendingIntent.getActivity(this, 0, new Intent(this,
				getClass()).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP), 0);
		mNdefPushMessage = new NdefMessage(new NdefRecord[] { buildTextRecord(
				"Message from NFC Reader :-)", Locale.ENGLISH, true) });
		


	}

	private void setupActionBar() {
		Log.d(TAG, "setupActionBar() is called");
		
		mToolbar = (Toolbar) findViewById(R.id.toolbar);
		mToolbar.setLogo(R.drawable.actionbar_logo_castvideos);
		mToolbar.setTitle("");
		setSupportActionBar(mToolbar);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);
		Log.d(TAG, "onCreateOptionsMenu() is called");
		
		getMenuInflater().inflate(R.menu.main, menu);

		mediaRouteMenuItem = mCastManager.addMediaRouterButton(menu,
				R.id.media_route_menu_item);

		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		Log.d(TAG, "onOptionsItemSelected() is called");
		
		switch (item.getItemId()) {
		case R.id.action_settings:
			Intent i = new Intent(NFCVideoBrowserActivity.this,
					CastPreference.class);
			startActivity(i);
			break;
		}
		return true;
	}

	/**
	 * The getActionView() method used in this method requires API 11 or above.
	 * If one needs to extend this below that version, one possible solution
	 * could be using reflection and such.
	 */
	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	private void showFtu() {
		Menu menu = mToolbar.getMenu();
		View view = menu.findItem(R.id.media_route_menu_item).getActionView();
		if (view != null && view instanceof MediaRouteButton) {
			new ShowcaseView.Builder(this).setTarget(new ViewTarget(view))
					.setContentTitle(R.string.touch_to_cast).build();
		}
	}

	@Override
	public boolean dispatchKeyEvent(KeyEvent event) {
		if (mCastManager.onDispatchVolumeKeyEvent(event,
				CastApplication.VOLUME_INCREMENT)) {
			return true;
		}
		return super.dispatchKeyEvent(event);
	}

	@Override
	protected void onResume() {
		super.onResume();
		
		Log.d(TAG, "onResume() is called");
		mCastManager = CastApplication.getCastManager(this);
		if (null != mCastManager) {
			mCastManager.addVideoCastConsumer(mCastConsumer);
			mCastManager.incrementUiCounter();
		}
		
		// NFC
		if (mAdapter != null) {
			if (!mAdapter.isEnabled()) {
				showMessage(R.string.error, R.string.nfc_disabled);
			}
			mAdapter.enableForegroundDispatch(this, mPendingIntent, null, null);
			mAdapter.enableForegroundNdefPush(this, mNdefPushMessage);
		}
				
	}

	@Override
	protected void onPause() {
		super.onPause();
		Log.d(TAG, "onPause() is called");
		
		mCastManager.decrementUiCounter();
		mCastManager.removeVideoCastConsumer(mCastConsumer);
		
		// NFC
		if (mAdapter != null) {
			mAdapter.disableForegroundDispatch(this);
			mAdapter.disableForegroundNdefPush(this);
		}		

		
	}

	@Override
	protected void onDestroy() {
		Log.d(TAG, "onDestroy is called");
		if (null != mCastManager) {
			mMini.removeOnMiniControllerChangedListener(mCastManager);
			mCastManager.removeMiniController(mMini);
			mCastManager.clearContext(this);
		}
		super.onDestroy();
	}
	
	
	@Override
	public void onNewIntent(Intent intent) {
		setIntent(intent);
		String sCommand = resolveIntent(intent);

		if(sCommand.contains(IParsedNdefCommand.COMMAND_FORWARD)){
			VideoBrowserListFragment vblf = (VideoBrowserListFragment)getSupportFragmentManager().findFragmentById(R.id.browse);
			lvVideoBrowserList = vblf.getListView();
			int iNumRelativeLayouts = lvVideoBrowserList.getChildCount();
			int iSelectedItem = iNumRelativeLayouts-1;
			for (int i = 0; i < iNumRelativeLayouts; i++) {
				RelativeLayout rl = (RelativeLayout)lvVideoBrowserList.getChildAt(i);
				int color = Color.TRANSPARENT;
	            Drawable background = rl.getBackground();
	            if (background instanceof ColorDrawable){
	                color = ((ColorDrawable) background).getColor();
	                if (Color.parseColor(CastApplication.COLOR_ORANGE) == color){
	                	iSelectedItem = i;
	                	Log.d(CLASSNAME, "Selected item ["+i+"]");    	
	                }
	            }
			}
						
			lvVideoBrowserList.getChildAt(iSelectedItem).setBackgroundColor(Color.WHITE);
			lvVideoBrowserList.getChildAt((iSelectedItem+1) % iNumRelativeLayouts ).setBackgroundColor(Color.parseColor(CastApplication.COLOR_ORANGE));
			
		}else if (sCommand.contains(IParsedNdefCommand.COMMAND_CAST)){
			VideoBrowserListFragment vblf = (VideoBrowserListFragment)getSupportFragmentManager().findFragmentById(R.id.browse);
			lvVideoBrowserList = vblf.getListView();
			int iNumRelativeLayouts = lvVideoBrowserList.getChildCount();
			int iSelectedItem = iNumRelativeLayouts-1;
			for (int i = 0; i < iNumRelativeLayouts; i++) {
				RelativeLayout rl = (RelativeLayout)lvVideoBrowserList.getChildAt(i);
				int color = Color.TRANSPARENT;
	            Drawable background = rl.getBackground();
	            if (background instanceof ColorDrawable){
	                color = ((ColorDrawable) background).getColor();
	                if (Color.parseColor(CastApplication.COLOR_ORANGE) == color){
	                	iSelectedItem = i;
	                	Log.d(CLASSNAME, "Selected item ["+i+"]"); 
	                	
	                }
	            }
			}
			vblf.onListItemClick(lvVideoBrowserList, (RelativeLayout)lvVideoBrowserList.getChildAt(iSelectedItem), iSelectedItem, iSelectedItem);
			
		}
		
		
		
	}	
	
	//
	// NFC
	//
	/**
	 * Show dialogue message
	 * 
	 * @param title
	 * @param message
	 */
	private void showMessage(int title, int message) {
		Log.d(CLASSNAME, "showMessage is called Title:" + title + " Mesagge:" + message);
		mDialog.setTitle(title);
		mDialog.setMessage(getText(message));
		mDialog.show();
	}	

	

	/**
	 * Build NDEF text record for given parameters
	 * 
	 * @param text
	 * @param locale
	 * @param encodeInUtf8
	 * @return
	 */
	private NdefRecord buildTextRecord(String text, Locale locale,
			boolean encodeInUtf8) {
		
		Log.d(CLASSNAME, "buildTextRecord is called Text:" + text + " Locale:"
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

	
	/**
	 * On create, process NDEF message with its intent action
	 *  
	 * @param intent
	 */
	private String resolveIntent(Intent intent) {
		Log.d(CLASSNAME, "resolveIntent is called intent:" + intent.toString());
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


			return processNdefMessageArray(msgs);

		}
		
		return IParsedNdefCommand.COMMAND_UNKNOWN;
	}

	/**
	 * Returns text data for given parecelable 
	 * 
	 * @param p
	 * @return
	 */
	private String dumpTagData(Parcelable p) {
		Log.d(CLASSNAME, "dumpTagData is called Parcelable" + p);
		
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
		Log.d(CLASSNAME, "getHex is called :" + bytes);

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

	private String processNdefMessageArray(NdefMessage[] msgs) {
		
		Log.d(CLASSNAME, "processNdefMessageArray is called. Number of items in NdefMessage[] " + msgs.length + ".");
		if (msgs == null || msgs.length == 0) {
			return IParsedNdefCommand.COMMAND_UNKNOWN;
		}

		// Only the identifier of the first ndef message is considered.
		// The rest of the messages in the record are discarded
		//
		NdefMessageParser.printNdefMessages(msgs);
		IParsedNdefCommand cmd = NdefMessageParser.parseFirstNdefMessage(msgs[0]);
						
		// PROCESS COMMAND HERE
//		Log.d(CLASSNAME, cmd.getCommand() + " | " + cmd.getId() + " | " + cmd.getColor());
//		
//
//		Toast.makeText(this, cmd.getCommand() + " | " + cmd.getId() + " | " + cmd.getColor(), Toast.LENGTH_LONG).show();
//		if(cmd.getCommand().contains(IParsedNdefCommand.COMMAND_FORWARD)){
//			Toast.makeText(this, "Executing ..." + cmd.getCommand() + " | " + cmd.getId() + " | " + cmd.getColor(), Toast.LENGTH_LONG).show();
//		}else{
//			Toast.makeText(this, "Cannot execute ..." + cmd.getCommand() + " | " + cmd.getId() + " | " + cmd.getColor(), Toast.LENGTH_LONG).show();
//		}
		return cmd.getCommand();


	}
	
	
}
