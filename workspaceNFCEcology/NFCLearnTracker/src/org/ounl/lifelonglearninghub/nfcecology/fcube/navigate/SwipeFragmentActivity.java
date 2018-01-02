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
package org.ounl.lifelonglearninghub.nfcecology.fcube.navigate;

import org.ounl.lifelonglearninghub.nfcecology.fcube.commands.FCBeep;
import org.ounl.lifelonglearninghub.nfcecology.fcube.commands.FCColor;
import org.ounl.lifelonglearninghub.nfcecology.fcube.commands.FCFade;
import org.ounl.lifelonglearninghub.nfcecology.fcube.commands.FCMelody1;
import org.ounl.lifelonglearninghub.nfcecology.fcube.commands.FCOff;
import org.ounl.lifelonglearninghub.nfcecology.fcube.commands.FCOn;
import org.ounl.lifelonglearninghub.nfcecology.fcube.commands.FCPieChart;
import org.ounl.lifelonglearninghub.nfcecology.fcube.commands.FCRainbow;
import org.ounl.lifelonglearninghub.nfcecology.fcube.commands.FCRainbowCircle;
import org.ounl.lifelonglearninghub.nfcecology.fcube.commands.IFeedbackCubeCommnads;
import org.ounl.lifelonglearninghub.nfcecology.fcube.config.FeedbackCubeConfig;
import org.ounl.lifelonglearninghub.nfcecology.fcube.config.FeedbackCubeManager;
import org.ounl.lifelonglearninghub.nfcecology.fcube.jukebox.Sampler;
import org.ounl.lifelonglearninghub.nfcecology.MainActivity;
import org.ounl.lifelonglearninghub.nfcecology.R;

import android.app.ActionBar;
import android.app.AlertDialog;
import android.app.FragmentTransaction;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.NavUtils;
import android.support.v4.app.TaskStackBuilder;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

public class SwipeFragmentActivity extends FragmentActivity implements
		ActionBar.TabListener {
	
	private String CLASSNAME = this.getClass().getName();

	/**
	 * The {@link android.support.v4.view.PagerAdapter} that will provide
	 * fragments for each of the three primary sections of the app. We use a
	 * {@link android.support.v4.app.FragmentPagerAdapter} derivative, which
	 * will keep every loaded fragment in memory. If this becomes too memory
	 * intensive, it may be best to switch to a
	 * {@link android.support.v4.app.FragmentStatePagerAdapter}.
	 */
	AppSectionsPagerAdapter mAppSectionsPagerAdapter;

	/**
	 * The {@link ViewPager} that will display the three primary sections of the
	 * app, one at a time.
	 */
	ViewPager mViewPager;
	
	// Active command
	IFeedbackCubeCommnads ifcc;	
	

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_pager);

		// Create the adapter that will return a fragment for each of the three
		// primary sections
		// of the app.
		mAppSectionsPagerAdapter = new AppSectionsPagerAdapter(
				getSupportFragmentManager());

		// Actvate home button
		final ActionBar actionBar = getActionBar();
		actionBar.setHomeButtonEnabled(true);
		

		// Specify that we will be displaying tabs in the action bar.
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

		// Set up the ViewPager, attaching the adapter and setting up a listener
		// for when the
		// user swipes between sections.
		mViewPager = (ViewPager) findViewById(R.id.pager);
		mViewPager.setAdapter(mAppSectionsPagerAdapter);
		mViewPager
				.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
					@Override
					public void onPageSelected(int position) {
						// When swiping between different app sections, select
						// the corresponding tab.
						// We can also use ActionBar.Tab#select() to do this if
						// we have a reference to the
						// Tab.
						actionBar.setSelectedNavigationItem(position);
					}
				});

		// For each of the sections in the app, add a tab to the action bar.
		for (int i = 0; i < mAppSectionsPagerAdapter.getCount(); i++) {
			// Create a tab with text corresponding to the page title defined by
			// the adapter.
			// Also specify this Activity object, which implements the
			// TabListener interface, as the
			// listener for when this tab is selected.
			actionBar.addTab(actionBar.newTab()
					.setText(mAppSectionsPagerAdapter.getPageTitle(i))
					.setTabListener(this));
		}

		

		
	}

	@Override
	public void onTabUnselected(ActionBar.Tab tab,
			FragmentTransaction fragmentTransaction) {
	}

	@Override
	public void onTabSelected(ActionBar.Tab tab,
			FragmentTransaction fragmentTransaction) {
		// When the given tab is selected, switch to the corresponding page in
		// the ViewPager.
		mViewPager.setCurrentItem(tab.getPosition());
	}

	@Override
	public void onTabReselected(ActionBar.Tab tab,
			FragmentTransaction fragmentTransaction) {
	}

	/**
	 * Jukebox fragment Clicked on any of the buttons
	 * from the jukebox fragmen
	 * 
	 * @param v
	 */
	public void onClickButton(View v) {

		IFeedbackCubeCommnads c = FeedbackCubeConfig.getSingleInstance()
				.getSampler((String) v.getTag()).getmFCCommand();
		

		new FeedbackCubeManager().execute(c);

	}
	
	/**
	 * Reset values in session with default values
	 * @param v
	 */
	public void onClickResetGoals(View v) {
		FeedbackCubeConfig.getSingleInstance().initSamplers();
	}
	
	public void onClickON(View v) {
		FCOn c = new FCOn(FeedbackCubeConfig.getSingleInstance().getIp());
		new FeedbackCubeManager().execute(c);
	}
	
	public void onClickOFF(View v) {
		FCOff c = new FCOff(FeedbackCubeConfig.getSingleInstance().getIp());
		new FeedbackCubeManager().execute(c);
	}	
	
	/**
	 * Replaces sampler button in jukebox
	 * 
	 * @param v
	 */
	public void onClickReplaceSamplerButton(View v) {

		

		
		// Rename button
		
		LinearLayout llButtons = (LinearLayout)v.getParent();
		LinearLayout llParent  = (LinearLayout)llButtons.getParent();
		
		EditText etRen = (EditText)llParent.findViewById(R.id.editTextRename);
		String sNewName = etRen.getText().toString();
		Button but = (Button)v;
		but.setTextColor(Color.CYAN);
		but.setText(sNewName);
		
//		Fragment f = mAppSectionsPagerAdapter.getItem(iActiveFragment);
//
//		
//
//		
//		if(f.getClass().equals(VisualSectionFragment.class)){
//			
//			FCColor fcc = (FCColor)ifcc;
//			Sampler samp = new Sampler(fcc, sNewName);
//			FeedbackCubeConfig.getSingleInstance().addSampler((String)v.getTag(), samp, this);			
//			
//		}else if (f.getClass().equals(AudioSectionFragment.class)){
//					
//			FCBeep fcc = (FCBeep)ifcc;
//			Sampler samp = new Sampler(fcc, sNewName);
//			FeedbackCubeConfig.getSingleInstance().addSampler((String)v.getTag(), samp, this);			
//			
//		}else if (f.getClass().equals(EffectsSectionFragment.class)){
//			
//			
//			if(ifcc.getClass().equals(FCRainbow.class)){
//				FCRainbow fcc = (FCRainbow)ifcc;
//				Sampler samp = new Sampler(fcc, sNewName);
//				FeedbackCubeConfig.getSingleInstance().addSampler((String)v.getTag(), samp, this);
//			}else if (ifcc.getClass().equals(FCRainbowCircle.class)){
//				FCRainbowCircle fcc = (FCRainbowCircle)ifcc;
//				Sampler samp = new Sampler(fcc, sNewName);
//				FeedbackCubeConfig.getSingleInstance().addSampler((String)v.getTag(), samp, this);
//			}else if (ifcc.getClass().equals(FCFade.class){	
//
//			
//				FCFade fcc = (FCFade)ifcc;
//				Sampler samp = new Sampler(fcc, sNewName);
//				FeedbackCubeConfig.getSingleInstance().addSampler((String)v.getTag(), samp, this);
//			}
//			
//			
//		}
//		
	Sampler samp = new Sampler(ifcc, sNewName);
	FeedbackCubeConfig.getSingleInstance().addSampler((String)v.getTag(), samp, this);			



	}	

	/**
	 * Clicked on the cube button in any of the fragments v.getTag determines
	 * the tab where the action comes from
	 * 
	 * @param v
	 */
	public void onClickCube(View v) {
		
		if (getCommnand(v) != null){
			ifcc = getCommnand(v);
			new FeedbackCubeManager().execute(ifcc);
		}else{
			Toast.makeText(this, "This command cannot be launched",Toast.LENGTH_SHORT).show();			
		}

//		LinearLayout llButtons = (LinearLayout) v.getParent();
//		LinearLayout llRoot = (LinearLayout) llButtons.getParent();
//
//		String sTag = v.getTag().toString();
//		if (sTag.compareTo(getString(R.string.tab_vi)) == 0) {
//
//			TextView tvRed = (TextView) llRoot
//					.findViewById(R.id.textViewRedValue);
//			TextView tvGreen = (TextView) llRoot
//					.findViewById(R.id.textViewGreenValue);
//			TextView tvBlue = (TextView) llRoot
//					.findViewById(R.id.textViewBlueValue);
//
//			FCColor c = new FCColor(FeedbackCubeConfig.getSingleInstance()
//					.getIp(), tvRed.getText().toString(), tvGreen.getText()
//					.toString(), tvBlue.getText().toString());
//
//			Toast.makeText(this, "Launch cube " + c.toString(),
//					Toast.LENGTH_SHORT).show();
//
//			new FeedbackCubeManager().execute(c);
//
//		} else if (sTag.compareTo(getString(R.string.tab_au)) == 0) {
//
//			CheckBox cb = (CheckBox) llRoot.findViewById(R.id.cbBeep);
//			if (cb.isChecked()) {
//				FCBeep c = new FCBeep(FeedbackCubeConfig.getSingleInstance()
//						.getIp());
//				Toast.makeText(this, "Launch cube " + c.toString(),
//						Toast.LENGTH_SHORT).show();
//
//				new FeedbackCubeManager().execute(c);
//			} else {
//				Toast.makeText(this,
//						"Check off the Beep! to launch the action.",
//						Toast.LENGTH_SHORT).show();
//			}
//
//		} else if (sTag.compareTo(getString(R.string.tab_ef)) == 0) {
//
//			RadioButton rbRainbow = (RadioButton) llRoot
//					.findViewById(R.id.rbRainbow);
//			RadioButton rbRainbowC = (RadioButton) llRoot
//					.findViewById(R.id.rbRainbowCircle);
//			RadioButton rbFade = (RadioButton) llRoot.findViewById(R.id.rbFade);
//
//			if (rbRainbow.isChecked()) {
//
//				FCRainbow c = new FCRainbow(FeedbackCubeConfig
//						.getSingleInstance().getIp());
//				Toast.makeText(this, "Launch cube " + c.toString(),
//						Toast.LENGTH_SHORT).show();
//
//				new FeedbackCubeManager().execute(c);
//
//			} else if (rbRainbowC.isChecked()) {
//
//				FCRainbowCircle c = new FCRainbowCircle(FeedbackCubeConfig
//						.getSingleInstance().getIp());
//
//				Toast.makeText(this, "Launch cube " + c.toString(),
//						Toast.LENGTH_SHORT).show();
//
//				new FeedbackCubeManager().execute(c);
//
//			} else if (rbFade.isChecked()) {
//
//				
//				NumberPicker npD = (NumberPicker) llRoot.findViewById(R.id.npDelay);
//				NumberPicker npN = (NumberPicker) llRoot.findViewById(R.id.npNumber);
//				
//				
//				FCFade c = new FCFade(FeedbackCubeConfig.getSingleInstance()
//						.getIp(), ""+npN.getValue(), ""+npD.getValue());
//
//				Toast.makeText(this, "Launch cube " + c.toString(),
//						Toast.LENGTH_SHORT).show();
//
//				new FeedbackCubeManager().execute(c);
//			}
//
//		}

	}
	
	/**
	 * Returns active command for controls selected in view .
	 * Returns null for wrong command
	 * 
	 * @param v
	 * @return
	 */
	private IFeedbackCubeCommnads getCommnand(View v){
		
		LinearLayout llButtons = (LinearLayout) v.getParent();
		LinearLayout llRoot = (LinearLayout) llButtons.getParent();

		String sTag = v.getTag().toString();
		if (sTag.compareTo(getString(R.string.tab_vi)) == 0) {

			TextView tvRed = (TextView) llRoot
					.findViewById(R.id.textViewRedValue);
			TextView tvGreen = (TextView) llRoot
					.findViewById(R.id.textViewGreenValue);
			TextView tvBlue = (TextView) llRoot
					.findViewById(R.id.textViewBlueValue);

			
			
			RadioButton rbF = (RadioButton) llRoot
					.findViewById(R.id.rbFullColor);
			RadioButton rbP = (RadioButton) llRoot
					.findViewById(R.id.rbPartColor);


			if (rbP.isChecked()) {
				
				NumberPicker npStart = (NumberPicker) llRoot.findViewById(R.id.npLedStart);
				NumberPicker npStop = (NumberPicker) llRoot.findViewById(R.id.npLedStop);
				

				FCPieChart c = new FCPieChart(
						FeedbackCubeConfig.getSingleInstance().getIp(),
						npStart.getValue()+"",
						npStop.getValue()+"",
						tvRed.getText().toString(), 
						tvGreen.getText().toString(), 
						tvBlue.getText().toString()						
						);

//				Toast.makeText(this, "Launch cube " + c.toString(),
//						Toast.LENGTH_SHORT).show();

				return c;

			} else {
				FCColor c = new FCColor(
						FeedbackCubeConfig.getSingleInstance().getIp(), 
						tvRed.getText().toString(), 
						tvGreen.getText().toString(), 
						tvBlue.getText().toString());
//				Toast.makeText(this, "Launch cube " + c.toString(),
//						Toast.LENGTH_SHORT).show();

				return c;

				
			}
			
			


		} else if (sTag.compareTo(getString(R.string.tab_au)) == 0) {

//			CheckBox cb = (CheckBox) llRoot.findViewById(R.id.cbBeep);
//			if (cb.isChecked()) {
//				FCBeep c = new FCBeep(FeedbackCubeConfig.getSingleInstance()
//						.getIp());
////				Toast.makeText(this, "Launch cube " + c.toString(),
////						Toast.LENGTH_SHORT).show();
//
//				return c;
//			} else {
//				Toast.makeText(this,
//						"Check off the Beep! to launch the action.",
//						Toast.LENGTH_SHORT).show();
//			}
			
			
			RadioButton rbBeep = (RadioButton) llRoot
					.findViewById(R.id.rbBeep);
			RadioButton rbMelody = (RadioButton) llRoot
					.findViewById(R.id.rbMelody1);

			if (rbBeep.isChecked()) {

				
				FCBeep c = new FCBeep(FeedbackCubeConfig.getSingleInstance()
				.getIp());
//				Toast.makeText(this, "Launch cube " + c.toString(),
//						Toast.LENGTH_SHORT).show();

				return c;

			} else if (rbMelody.isChecked()) {



				FCMelody1 c = new FCMelody1(FeedbackCubeConfig.getSingleInstance()
				.getIp());

//				Toast.makeText(this, "Launch cube " + c.toString(),
//						Toast.LENGTH_SHORT).show();

				return c;

			}			
			
			
			

		} else if (sTag.compareTo(getString(R.string.tab_ef)) == 0) {

			RadioButton rbRainbow = (RadioButton) llRoot
					.findViewById(R.id.rbRainbow);
			RadioButton rbRainbowC = (RadioButton) llRoot
					.findViewById(R.id.rbRainbowCircle);
			RadioButton rbFade = (RadioButton) llRoot.findViewById(R.id.rbFade);

			if (rbRainbow.isChecked()) {

				FCRainbow c = new FCRainbow(FeedbackCubeConfig
						.getSingleInstance().getIp());
//				Toast.makeText(this, "Launch cube " + c.toString(),
//						Toast.LENGTH_SHORT).show();

				return c;

			} else if (rbRainbowC.isChecked()) {

				FCRainbowCircle c = new FCRainbowCircle(FeedbackCubeConfig
						.getSingleInstance().getIp());

//				Toast.makeText(this, "Launch cube " + c.toString(),
//						Toast.LENGTH_SHORT).show();

				return c;

			} else if (rbFade.isChecked()) {

				
				NumberPicker npD = (NumberPicker) llRoot.findViewById(R.id.npDelay);
				NumberPicker npN = (NumberPicker) llRoot.findViewById(R.id.npNumber);
				
				
				FCFade c = new FCFade(FeedbackCubeConfig.getSingleInstance()
						.getIp(), ""+npN.getValue(), ""+npD.getValue());

//				Toast.makeText(this, "Launch cube " + c.toString(),
//						Toast.LENGTH_SHORT).show();

				return c;
			}
			
			

		}
		return null;
		
	}
	
	
	

	/**
	 * On click one radiobutton, deslect the rest of the radiobuttons
	 * 
	 * @param v
	 */
	public void onSelectRadioButton(View v) {

		LinearLayout llRadioButton = (LinearLayout) v.getParent();
		LinearLayout llBranch = (LinearLayout) llRadioButton.getParent();
		LinearLayout llRoot = (LinearLayout) llBranch.getParent();

		RadioButton rbF = (RadioButton) llRoot.findViewById(R.id.rbFade);
		RadioButton rbRainbow = (RadioButton) llRoot
				.findViewById(R.id.rbRainbow);
		RadioButton rbRainbowC = (RadioButton) llRoot
				.findViewById(R.id.rbRainbowCircle);

		String sTag = v.getTag().toString();
		if (getString(R.string.rb_fade).compareTo(sTag) == 0) {

			rbRainbow.setChecked(false);
			rbRainbowC.setChecked(false);

		} else if (getString(R.string.rb_rainbow).compareTo(sTag) == 0) {

			rbF.setChecked(false);
			rbRainbowC.setChecked(false);

		} else if (getString(R.string.rb_rainbow_circle).compareTo(sTag) == 0) {

			rbF.setChecked(false);
			rbRainbow.setChecked(false);

		}

	}
	
	/**
	 * On click one audio radiobutton, deselect the rest of the radiobuttons
	 * 
	 * @param v
	 */
	public void onSelectAudioRadioButton(View v) {

		LinearLayout llRadioButton = (LinearLayout) v.getParent();
		LinearLayout llRoot = (LinearLayout) llRadioButton.getParent();

		RadioButton rbB = (RadioButton) llRoot.findViewById(R.id.rbBeep);
		RadioButton rbM = (RadioButton) llRoot
				.findViewById(R.id.rbMelody1);

		String sTag = v.getTag().toString();
		if (getString(R.string.rb_beep).compareTo(sTag) == 0) {

			rbM.setChecked(false);

		} else if (getString(R.string.rb_melody).compareTo(sTag) == 0) {

			rbB.setChecked(false);


		}

	}	
	
	/**
	 * On click one color radiobutton, deselect the rest of the radiobuttons
	 * 
	 * @param v
	 */
	public void onSelectColorRadioButton(View v) {

		LinearLayout llRadioButton = (LinearLayout) v.getParent();
		LinearLayout llRoot = (LinearLayout) llRadioButton.getParent();

		RadioButton rbF = (RadioButton) llRoot.findViewById(R.id.rbFullColor);
		RadioButton rbP = (RadioButton) llRoot
				.findViewById(R.id.rbPartColor);

		String sTag = v.getTag().toString();
		if (getString(R.string.rb_fully).compareTo(sTag) == 0) {

			rbP.setChecked(false);

		} else if (getString(R.string.rb_partly).compareTo(sTag) == 0) {

			rbF.setChecked(false);


		}

	}	

	
	/**
	 *  Click on buton panel jukebox
	 *  
	 * @param v
	 */
	public void onClickSave(View v) {

		// Get current command
		if (getCommnand(v) != null){
			ifcc = getCommnand(v);
		}else{
			Toast.makeText(this, "This command cannot be assinged",Toast.LENGTH_SHORT).show();			
		}
		
		// Inflate prompt jukebox
        LayoutInflater li = LayoutInflater.from(this);
        View promptsView = li.inflate(R.layout.prompt_jukebox, null);
        
        Button ba = (Button)promptsView.findViewById(R.id.bPromptA);
        ba.setText(FeedbackCubeConfig.getSingleInstance().getSampler(getString(R.string.jukebox_button_A)).getmTitle());
        
        Button bb = (Button)promptsView.findViewById(R.id.bPromptB);
        bb.setText(FeedbackCubeConfig.getSingleInstance().getSampler(getString(R.string.jukebox_button_B)).getmTitle());
        
        Button bc = (Button)promptsView.findViewById(R.id.bPromptC);
        bc.setText(FeedbackCubeConfig.getSingleInstance().getSampler(getString(R.string.jukebox_button_C)).getmTitle());
        
        Button bd = (Button)promptsView.findViewById(R.id.bPromptD);
        bd.setText(FeedbackCubeConfig.getSingleInstance().getSampler(getString(R.string.jukebox_button_D)).getmTitle());
        
        Button be = (Button)promptsView.findViewById(R.id.bPromptE);
        be.setText(FeedbackCubeConfig.getSingleInstance().getSampler(getString(R.string.jukebox_button_E)).getmTitle());
        
        Button bf = (Button)promptsView.findViewById(R.id.bPromptF);
        bf.setText(FeedbackCubeConfig.getSingleInstance().getSampler(getString(R.string.jukebox_button_F)).getmTitle());
        
        Button bg = (Button)promptsView.findViewById(R.id.bPromptG);
        bg.setText(FeedbackCubeConfig.getSingleInstance().getSampler(getString(R.string.jukebox_button_G)).getmTitle());
                
        Button bh = (Button)promptsView.findViewById(R.id.bPromptH);
        bh.setText(FeedbackCubeConfig.getSingleInstance().getSampler(getString(R.string.jukebox_button_H)).getmTitle());
        
        Button bi = (Button)promptsView.findViewById(R.id.bPromptI);
        bi.setText(FeedbackCubeConfig.getSingleInstance().getSampler(getString(R.string.jukebox_button_I)).getmTitle());
        
        Button bj = (Button)promptsView.findViewById(R.id.bPromptJ);
        bj.setText(FeedbackCubeConfig.getSingleInstance().getSampler(getString(R.string.jukebox_button_J)).getmTitle());
        
        
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setView(promptsView);

//        TextView tvPrompt = (TextView) promptsView
//                        .findViewById(R.id.textViewPrompt);
//        tvPrompt.setText("Ip Address: ");
//        final EditText userInput = (EditText) promptsView
//                        .findViewById(R.id.editTextPrompt);
//        if (FeedbackCubeConfig.getSingleInstance().getIp() != null) {
//                userInput.setText(FeedbackCubeConfig.getSingleInstance().getIp());
//        }

        alertDialogBuilder.setCancelable(true).setPositiveButton("Done!",
                        new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
//                                        FeedbackCubeConfig.getSingleInstance().setIp(
//                                                        userInput.getText().toString());
                                        dialog.dismiss();
                                }
                        });
        

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
		
		
		
	}
	
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			// This is called when the Home (Up) button is pressed in the action
			// bar.
			// Create a simple intent that starts the hierarchical parent
			// activity and
			// use NavUtils in the Support Package to ensure proper handling of
			// Up.
			Intent upIntent = new Intent(this, MainActivity.class);
			if (NavUtils.shouldUpRecreateTask(this, upIntent)) {
				// This activity is not part of the application's task, so
				// create a new task
				// with a synthesized back stack.
				TaskStackBuilder.from(this)
				// If there are ancestor activities, they should be added here.
						.addNextIntent(upIntent).startActivities();
				finish();
			} else {
				// This activity is part of the application's task, so simply
				// navigate up to the hierarchical parent activity.
				NavUtils.navigateUpTo(this, upIntent);
			}
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	

}
