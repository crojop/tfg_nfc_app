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
package org.ounl.lifelonglearninghub.nfcecology.fcube;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.Properties;

import org.ounl.lifelonglearninghub.nfcecology.fcube.commands.FCGeneric;
import org.ounl.lifelonglearninghub.nfcecology.fcube.config.FeedbackCubeConfig;
import org.ounl.lifelonglearninghub.nfcecology.fcube.jukebox.Sampler;
import org.ounl.lifelonglearninghub.nfcecology.fcube.navigate.SwipeFragmentActivity;
import org.ounl.lifelonglearninghub.nfcecology.MainActivity;
import org.ounl.lifelonglearninghub.nfcecology.R;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v4.app.TaskStackBuilder;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class MainCubeActivity extends Activity {
	
	private String CLASSNAME = this.getClass().getName();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_splash);
		
		
		// LOS VALORES INICIALES LOS VAS A LEER DE LA SESSION
		FeedbackCubeConfig.getSingleInstance().initSamplers();
		
		int iSize = getJukeboxFromFile().size();
		Log.i(CLASSNAME, "Number of elems in jukebox file :"+iSize);
		
		// COMPROBAR SI EXISTE FICHERO JUKEBOX
		if (iSize > 2){
			// SI YA EXISTE, MACHAR VALORES DE SESION CON LOS DEL FICHERO
			Log.i(CLASSNAME, "Jukebox file already exists. Load into session...");
			loadPropertiesToSession(getJukeboxFromFile());
			//printFile(Constants.JUKEBOX_PROPERTIES_FILE);
		}else{
			// SI NO EXISTE, CREAR FICHERO CON LOS VALORES INICIALES DE LA SESION
			Log.i(CLASSNAME, "Jukebox file does not exist yet. Load file from session...");
			writeSamplersToFile(FeedbackCubeConfig.getSingleInstance().getSamplers());
			//printFile(Constants.JUKEBOX_PROPERTIES_FILE);
		}
		
		// Actvate home button
		final ActionBar actionBar = getActionBar();
		actionBar.setHomeButtonEnabled(true);
		

	}
	@Override	
	protected void onResume(){
		super.onResume();

		initJukeboxTitles();
		
	}




	/**
	 * Clicked on cube
	 * 
	 * @param v
	 */
	public void onClickSwipeButton(View v) {
		Intent intent = new Intent(this, SwipeFragmentActivity.class);
		startActivity(intent);

	}
	
	
	private void initJukeboxTitles(){
		
		
		// Load sample titles in jukebox
		LayoutInflater li = LayoutInflater.from(this);
		View vJukeBox = li.inflate(R.layout.activity_display_jukebox, null);
		
		Button bSamplera = (Button)vJukeBox.findViewWithTag(Constants.JB_A);
		bSamplera.setText(FeedbackCubeConfig.getSingleInstance().getSampler(Constants.JB_A).getmTitle());
		
		Button bSamplerb = (Button)vJukeBox.findViewWithTag(Constants.JB_B);
		bSamplerb.setText(FeedbackCubeConfig.getSingleInstance().getSampler(Constants.JB_B).getmTitle());
		
		Button bSamplerc = (Button)vJukeBox.findViewWithTag(Constants.JB_C);
		bSamplerc.setText(FeedbackCubeConfig.getSingleInstance().getSampler(Constants.JB_C).getmTitle());
		
		Button bSamplerd = (Button)vJukeBox.findViewWithTag(Constants.JB_D);
		bSamplerd.setText(FeedbackCubeConfig.getSingleInstance().getSampler(Constants.JB_D).getmTitle());
		
		Button bSamplere = (Button)vJukeBox.findViewWithTag(Constants.JB_E);
		bSamplere.setText(FeedbackCubeConfig.getSingleInstance().getSampler(Constants.JB_E).getmTitle());
		
		Button bSamplerf = (Button)vJukeBox.findViewWithTag(Constants.JB_F);
		bSamplerf.setText(FeedbackCubeConfig.getSingleInstance().getSampler(Constants.JB_F).getmTitle());
		
		Button bSamplerg = (Button)vJukeBox.findViewWithTag(Constants.JB_G);
		bSamplerg.setText(FeedbackCubeConfig.getSingleInstance().getSampler(Constants.JB_G).getmTitle());
		
		Button bSamplerh = (Button)vJukeBox.findViewWithTag(Constants.JB_H);
		bSamplerh.setText(FeedbackCubeConfig.getSingleInstance().getSampler(Constants.JB_H).getmTitle());
		
		Button bSampleri = (Button)vJukeBox.findViewWithTag(Constants.JB_I);
		bSampleri.setText(FeedbackCubeConfig.getSingleInstance().getSampler(Constants.JB_I).getmTitle());
		
		Button bSamplerj = (Button)vJukeBox.findViewWithTag(Constants.JB_J);
		bSamplerj.setText(FeedbackCubeConfig.getSingleInstance().getSampler(Constants.JB_J).getmTitle());
		
		
	}
	
	
	/**
	 * Store properties into session
	 * 
	 * @return
	 */
	private int loadPropertiesToSession(Properties props) {

		try {

			FeedbackCubeConfig.getSingleInstance().setIp((String)props.get(Constants.CP_IP_ADDRESS));
			
			String samplers[] = {
					getString(R.string.jukebox_button_A),
					getString(R.string.jukebox_button_B),
					getString(R.string.jukebox_button_C),
					getString(R.string.jukebox_button_D),
					getString(R.string.jukebox_button_E),
					getString(R.string.jukebox_button_F),
					getString(R.string.jukebox_button_G),
					getString(R.string.jukebox_button_H),
					getString(R.string.jukebox_button_I),
					getString(R.string.jukebox_button_J)
			};
			
			for (int i = 0; i < samplers.length; i++) {
				
				
			
				// Load jukebox into session

				FeedbackCubeConfig.getSingleInstance().addSampler(
					samplers[i],
					new Sampler(
							new FCGeneric(
									(String)props.get(samplers[i] + "." +Constants.CP_COMMAND), 
									(String)props.get(samplers[i] + "." +Constants.CP_PARAMS), 
									(String)props.get(samplers[i] + "." +Constants.CP_METHOD)
									),
							(String)props.get(samplers[i] + "." +Constants.CP_TITLE)
							),
					this
					);
					
					
			}
			

		} catch (Exception e) {
			e.printStackTrace();

			return Constants.OPERATION_FAILED;
		}
		return Constants.OPERATION_SUCCESS;

	}	
	
	
	
	
	
	
	private Properties getJukeboxFromFile() {
		
		Properties props = new Properties();

			try {
				InputStream inputStream = openFileInput(Constants.JUKEBOX_PROPERTIES_FILE);

				if (inputStream != null) {

					props.load(inputStream);
					
				}
			} catch (FileNotFoundException e) {
				Log.e(CLASSNAME, "File not found: " + e.toString());
			} catch (IOException e) {
				Log.e(CLASSNAME, "Can not read file: " + e.toString());
			}

		return props;

	}	
	
	


	/**
	 * Write properties from session into properties file
	 * 
	 * @param samplers
	 */
	private void writeSamplersToFile(HashMap<String, Sampler> samplers){
		
	    try {
	    	
	    	String sFile = Constants.JUKEBOX_PROPERTIES_FILE;
	    	OutputStreamWriter outputStreamWriter = new OutputStreamWriter(openFileOutput(sFile, Context.MODE_PRIVATE));

	    	// This line creates a file in the following path:
	    	// /data/data/org.ounl.lifelonglearninghub.learntracker/files
	    	
	    	String sIP = Constants.CP_IP_ADDRESS+ "=" + FeedbackCubeConfig.getSingleInstance().getIp() + "\n";
	        outputStreamWriter.write(sIP);

	    	for (Iterator<Entry<String, Sampler>> iterator = samplers.entrySet().iterator(); iterator.hasNext();) {
	    		Entry<String, Sampler> type = (Entry<String, Sampler>) iterator.next();
				
	    		Sampler s = (Sampler) type.getValue();
	    		FCGeneric fc = (FCGeneric)s.getmFCCommand();
	    		
		        String sTitle = type.getKey() + "." + Constants.CP_TITLE+ "=" + s.getmTitle() + "\n";
		        outputStreamWriter.write(sTitle);
		        
		        String sComma = type.getKey() + "." + Constants.CP_COMMAND+ "=" + fc.getWSPath() + "\n";
		        outputStreamWriter.write(sComma);
		        
		        String sParams = type.getKey() + "." + Constants.CP_PARAMS+ "=" + fc.getParams() + "\n";
		        outputStreamWriter.write(sParams);
		        
		        String sMethod = type.getKey() + "." + Constants.CP_METHOD+ "=" + fc.getHttpMethod() + "\n";
		        outputStreamWriter.write(sMethod);

		        		
		        
			}
	    	
	        outputStreamWriter.close();

	    }
	    catch (IOException e) {
	        Log.e("Exception", "File write failed: " + e.toString());
	    } 
		
	}
	
	
	
	
	
	
	
    // Power Set IP
    public void onClickOnSetIp(View v) {

            LayoutInflater li = LayoutInflater.from(this);
            View promptsView = li.inflate(R.layout.prompts, null);
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
            alertDialogBuilder.setView(promptsView);

            TextView tvPrompt = (TextView) promptsView
                            .findViewById(R.id.textViewPrompt);
            tvPrompt.setText("Ip Address: ");
            final EditText userInput = (EditText) promptsView
                            .findViewById(R.id.editTextPrompt);
            if (FeedbackCubeConfig.getSingleInstance().getIp() != null) {
                    userInput.setText(FeedbackCubeConfig.getSingleInstance().getIp());
            }

            alertDialogBuilder.setCancelable(false).setPositiveButton("Save",
                            new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                            FeedbackCubeConfig.getSingleInstance().setIp(
                                                            userInput.getText().toString());
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
