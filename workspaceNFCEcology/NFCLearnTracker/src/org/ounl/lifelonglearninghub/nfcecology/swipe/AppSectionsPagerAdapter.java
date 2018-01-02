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
package org.ounl.lifelonglearninghub.nfcecology.swipe;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.util.Log;

public class AppSectionsPagerAdapter extends FragmentPagerAdapter {

	public AppSectionsPagerAdapter(FragmentManager fm) {
		super(fm);
	}

	@Override
	public Fragment getItem(int i) {
		Log.d("TAB", "Tab clicked: " + i);
		switch (i) {
		case 0:
			Log.d("TAB", "Return goal " + i);
			return new GoalsSectionFragment();
		case 1:
			Log.d("TAB", "Return acti " + i);
			return new ActivitiesSectionFragment();
		case 2:
			Log.d("TAB", "Return tags " + i);
			return new TagsSectionFragment();
		case 3:
			Log.d("TAB", "Return notif " + i);
			return new NotificationsSectionFragment();			
		default:
			Log.e("TAB", "Return PETAMIENTO. REVISA ESTA LINEA DE CODIGO" + i);
			return new GoalsSectionFragment();
			
			
			
//			switch (i) {
//			case 1:
//				Log.d("TAB", "Return goal " + i);
//				return new GoalsSectionFragment();
//			case 2:
//				Log.d("TAB", "Return acti " + i);
//				return new ActivitiesSectionFragment();
//			case 3:
//				Log.d("TAB", "Return tags " + i);
//				return new TagsSectionFragment();
//			case 4:
//				Log.d("TAB", "Return notif " + i);
//				return new NotificationsSectionFragment();			
//			default:
//				Log.e("TAB", "Return PETAMIENTO. REVISA ESTA LINEA DE CODIGO" + i);
//				return new GoalsSectionFragment();			

		}

	}

	@Override
	public int getCount() {
		return 4;
	}

	@Override
	public CharSequence getPageTitle(int position) {
		Log.d("TAB", "GetTitle " + position);
		switch (position) {
		case 0:
			Log.d("TAB", "Return GetTitle " + position);
			return "Goals";
		case 1:
			Log.d("TAB", "Return GetTitle " + position);
			return "Activities";
		case 2:
			Log.d("TAB", "Return GetTitle " + position);
			return "Tags";
		case 3:
			Log.d("TAB", "Return GetTitle " + position);
			return "Notifications";			
		default:
			Log.d("TAB", "Return GetTitle Return PETAMIENTO. REVISA ESTA LINEA DE CODIGO" + position);
			return "Section " + (position + 1);
		}
	}
}