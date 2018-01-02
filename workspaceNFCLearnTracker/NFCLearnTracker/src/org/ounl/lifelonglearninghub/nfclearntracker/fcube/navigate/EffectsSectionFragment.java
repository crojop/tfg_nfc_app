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

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.NumberPicker;
import android.widget.RadioButton;

public class EffectsSectionFragment extends Fragment {

	NumberPicker npDelay, npNumber;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.activity_display_effects,
				container, false);

		
		// Init number pickers
		final RadioButton rbF = (RadioButton) rootView
				.findViewById(R.id.rbFade);
		final RadioButton rbRainbow = (RadioButton) rootView
				.findViewById(R.id.rbRainbow);
		final RadioButton rbRainbowC = (RadioButton) rootView
				.findViewById(R.id.rbRainbowCircle);
		
		npDelay = (NumberPicker) rootView.findViewById(R.id.npDelay);
		npDelay.setMaxValue(30);
		npDelay.setMinValue(1);
		//npDelay.setWrapSelectorWheel(false);
		npDelay.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
			@Override
			public void onValueChange(NumberPicker numberPicker, int i, int i2) {
				rbRainbow.setChecked(false);
				rbRainbowC.setChecked(false);
				rbF.setChecked(true);

			}
		});

		npNumber = (NumberPicker) rootView.findViewById(R.id.npNumber);
		npNumber.setMaxValue(10);
		npNumber.setMinValue(1);
		//npNumber.setWrapSelectorWheel(false);
		npNumber.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
			@Override
			public void onValueChange(NumberPicker numberPicker, int i, int i2) {
				rbRainbow.setChecked(false);
				rbRainbowC.setChecked(false);
				rbF.setChecked(true);
			}
		});

		return rootView;
	}

}