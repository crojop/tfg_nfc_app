/*******************************************************************************
 * Copyright (C) 2014 Open University of The Netherlands
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
package org.ounl.lifelonglearninghub.nfclearntracker.fcube.navigate;

import org.ounl.lifelonglearninghub.nfclearntracker.R;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.NumberPicker;
import android.widget.RadioButton;
import android.widget.SeekBar;
import android.widget.TextView;

public class VisualSectionFragment extends Fragment {
	
	NumberPicker npLedStart, npLedEnd;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.activity_display_visual,
				container, false);

		//
		// Init seekbars
		//
		final TextView tvColorSample = (TextView) rootView.findViewById(R.id.tvColorSample);
		
		SeekBar sbRed = (SeekBar) rootView.findViewById(R.id.seekBarRed);
		sbRed.setMax(255);
		final TextView tvRValue = (TextView) rootView.findViewById(R.id.textViewRedValue);
		
		SeekBar sbG = (SeekBar) rootView.findViewById(R.id.seekBarGreen);
		sbG.setMax(255);
		final TextView tvGValue = (TextView) rootView.findViewById(R.id.textViewGreenValue);
		
		SeekBar sbB = (SeekBar) rootView.findViewById(R.id.seekBarBlue);
		sbB.setMax(255);
		final TextView tvBValue = (TextView) rootView.findViewById(R.id.textViewBlueValue);
		
		

		sbRed.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

			@Override
			public void onProgressChanged(SeekBar seekBar, int progress,
					boolean fromUser) {

				tvRValue.setText(String.valueOf(progress));
				
				String sR = (String)tvRValue.getText();
				Integer IsR = new Integer(sR);

				String sG = (String)tvGValue.getText();
				Integer IsG = new Integer(sG);
				
				String sB = (String)tvBValue.getText();
				Integer IsB = new Integer(sB);				
				
				
				String hex = String.format("#%02x%02x%02x", IsR, IsG, IsB);
				
				tvColorSample.setBackgroundColor(Color.parseColor(hex));
			}

			@Override
			public void onStartTrackingTouch(SeekBar seekBar) {
				// TODO Auto-generated method stub
			}

			@Override
			public void onStopTrackingTouch(SeekBar seekBar) {
				// TODO Auto-generated method stub
			}
		});
		
		
		sbG.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

			@Override
			public void onProgressChanged(SeekBar seekBar, int progress,
					boolean fromUser) {

				tvGValue.setText(String.valueOf(progress));
				
				String sR = (String)tvRValue.getText();
				Integer IsR = new Integer(sR);

				String sG = (String)tvGValue.getText();
				Integer IsG = new Integer(sG);
				
				String sB = (String)tvBValue.getText();
				Integer IsB = new Integer(sB);				
				
				
				String hex = String.format("#%02x%02x%02x", IsR, IsG, IsB);
				
				tvColorSample.setBackgroundColor(Color.parseColor(hex));				
			}

			@Override
			public void onStartTrackingTouch(SeekBar seekBar) {
				// TODO Auto-generated method stub
			}

			@Override
			public void onStopTrackingTouch(SeekBar seekBar) {
				// TODO Auto-generated method stub
			}
		});
		
		
		sbB.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

			@Override
			public void onProgressChanged(SeekBar seekBar, int progress,
					boolean fromUser) {

				tvBValue.setText(String.valueOf(progress));
				
				String sR = (String)tvRValue.getText();
				Integer IsR = new Integer(sR);

				String sG = (String)tvGValue.getText();
				Integer IsG = new Integer(sG);
				
				String sB = (String)tvBValue.getText();
				Integer IsB = new Integer(sB);				
				
				
				String hex = String.format("#%02x%02x%02x", IsR, IsG, IsB);
				
				tvColorSample.setBackgroundColor(Color.parseColor(hex));				
			}

			@Override
			public void onStartTrackingTouch(SeekBar seekBar) {
				// TODO Auto-generated method stub
			}

			@Override
			public void onStopTrackingTouch(SeekBar seekBar) {
				// TODO Auto-generated method stub
			}
		});		
		
		
		//
		// Init number pickers
		// 
		final RadioButton rbF = (RadioButton) rootView
				.findViewById(R.id.rbFullColor);
		final RadioButton rbP = (RadioButton) rootView
				.findViewById(R.id.rbPartColor);
				
		npLedStart = (NumberPicker) rootView.findViewById(R.id.npLedStart);
		npLedStart.setMaxValue(15);
		npLedStart.setMinValue(0);
		npLedStart.setValue(0);
		//npLedStart.setWrapSelectorWheel(false);
		npLedStart.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
			@Override
			public void onValueChange(NumberPicker numberPicker, int i, int i2) {
				rbP.setChecked(true);
				rbF.setChecked(false);

			}
		});

		npLedEnd = (NumberPicker) rootView.findViewById(R.id.npLedStop);
		npLedEnd.setMaxValue(15);
		npLedEnd.setMinValue(0);
		npLedEnd.setValue(15);
		//npLedEnd.setWrapSelectorWheel(false);
		npLedEnd.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
			@Override
			public void onValueChange(NumberPicker numberPicker, int i, int i2) {
				rbP.setChecked(true);
				rbF.setChecked(false);
			}
		});		
		
		
		return rootView;
	}

}