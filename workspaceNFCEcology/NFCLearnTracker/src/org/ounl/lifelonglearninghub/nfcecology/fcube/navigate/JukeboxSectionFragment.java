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

import org.ounl.lifelonglearninghub.nfcecology.fcube.commands.FCGeneric;
import org.ounl.lifelonglearninghub.nfcecology.fcube.config.FeedbackCubeConfig;
import org.ounl.lifelonglearninghub.nfcecology.fcube.jukebox.Sampler;
import org.ounl.lifelonglearninghub.nfcecology.R;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;


/**
 * A fragment that launches other parts of the demo application.
 */
public class JukeboxSectionFragment extends Fragment {

	View rootView;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		rootView = inflater.inflate(R.layout.activity_display_jukebox,
				container, false);
		

		
		OnLongClickListener olclButton = new View.OnLongClickListener() {
			
			@Override
			public boolean onLongClick(View v) {


				AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());
				View promptsView = getActivity().getLayoutInflater().inflate(R.layout.prompt_sampler, null);
				alertDialogBuilder.setView(promptsView);
				
				final String sTag = (String)v.getTag();
				Sampler s = FeedbackCubeConfig.getSingleInstance().getSampler(sTag);



//				TextView tvPrompt = (TextView) promptsView.findViewById(R.id.textViewPrompt);
//				tvPrompt.setText("Rename sampler");
				final EditText etTitle = (EditText) promptsView.findViewById(R.id.editTextPromptSamplerTitle);
				etTitle.setText(s.getmTitle());
				
				final EditText etM = (EditText) promptsView.findViewById(R.id.editTextPromptSamplerMethod);
				etM.setText(s.getmFCCommand().getHttpMethod());				
				
				final EditText etC = (EditText) promptsView.findViewById(R.id.editTextPromptSamplerCommand);
				//etC.setText(s.getmFCCommand().getUrlCommand().toString());
				etC.setText(s.getmFCCommand().getWSPath());
				
				final EditText etP = (EditText) promptsView.findViewById(R.id.editTextPromptSamplerParams);
				etP.setText(s.getmFCCommand().getParams());

				


				
				
				
				alertDialogBuilder.setCancelable(false).setPositiveButton(
						"Save", new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int id) {
								

								// Update titles
								Button b = (Button)rootView.findViewWithTag(sTag);
								b.setText(etTitle.getText().toString());								
								
								
								
			FeedbackCubeConfig.getSingleInstance().addSampler(
					sTag,
					new Sampler(
							new FCGeneric(
									etC.getText().toString(),
									etP.getText().toString(),
									etM.getText().toString()
									),
							etTitle.getText().toString()
							),
					getActivity().getApplicationContext()
					);								
								
								// TODO PENDING TO WRITE HERE TO SAVE INTO FILE PROPERTIES FOR FORTHCOMING SESSIONS
								dialog.dismiss();

							}
						});

				AlertDialog alertDialog = alertDialogBuilder.create();
				alertDialog.show();								
				
				
				return false;
			}
		}; 
		
		
		
		rootView.findViewById(R.id.buttonA).setOnLongClickListener(olclButton);
		Button ba = (Button)rootView.findViewById(R.id.buttonA);
		ba.setText(FeedbackCubeConfig.getSingleInstance().getSampler(getString(R.string.jukebox_button_A)).getmTitle());
		
		rootView.findViewById(R.id.buttonB).setOnLongClickListener(olclButton);
		Button bb = (Button)rootView.findViewById(R.id.buttonB);
		bb.setText(FeedbackCubeConfig.getSingleInstance().getSampler(getString(R.string.jukebox_button_B)).getmTitle());
		
		rootView.findViewById(R.id.buttonC).setOnLongClickListener(olclButton);
		Button bc = (Button)rootView.findViewById(R.id.buttonC);
		bc.setText(FeedbackCubeConfig.getSingleInstance().getSampler(getString(R.string.jukebox_button_C)).getmTitle());		
		
		rootView.findViewById(R.id.buttonD).setOnLongClickListener(olclButton);
		Button bd = (Button)rootView.findViewById(R.id.buttonD);
		bd.setText(FeedbackCubeConfig.getSingleInstance().getSampler(getString(R.string.jukebox_button_D)).getmTitle());
		
		rootView.findViewById(R.id.buttonE).setOnLongClickListener(olclButton);
		Button be = (Button)rootView.findViewById(R.id.buttonE);
		be.setText(FeedbackCubeConfig.getSingleInstance().getSampler(getString(R.string.jukebox_button_E)).getmTitle());		
		
		rootView.findViewById(R.id.buttonF).setOnLongClickListener(olclButton);
		Button bf = (Button)rootView.findViewById(R.id.buttonF);
		bf.setText(FeedbackCubeConfig.getSingleInstance().getSampler(getString(R.string.jukebox_button_F)).getmTitle());
		
		rootView.findViewById(R.id.buttonG).setOnLongClickListener(olclButton);
		Button bg = (Button)rootView.findViewById(R.id.buttonG);
		bg.setText(FeedbackCubeConfig.getSingleInstance().getSampler(getString(R.string.jukebox_button_G)).getmTitle());
		
		rootView.findViewById(R.id.buttonH).setOnLongClickListener(olclButton);
		Button bh = (Button)rootView.findViewById(R.id.buttonH);
		bh.setText(FeedbackCubeConfig.getSingleInstance().getSampler(getString(R.string.jukebox_button_H)).getmTitle());		

		rootView.findViewById(R.id.buttonI).setOnLongClickListener(olclButton);
		Button bi = (Button)rootView.findViewById(R.id.buttonI);
		bi.setText(FeedbackCubeConfig.getSingleInstance().getSampler(getString(R.string.jukebox_button_I)).getmTitle());
		
		rootView.findViewById(R.id.buttonJ).setOnLongClickListener(olclButton);
		Button bj = (Button)rootView.findViewById(R.id.buttonJ);
		bj.setText(FeedbackCubeConfig.getSingleInstance().getSampler(getString(R.string.jukebox_button_J)).getmTitle());		
		
		

		return rootView;
	}
	

	

}