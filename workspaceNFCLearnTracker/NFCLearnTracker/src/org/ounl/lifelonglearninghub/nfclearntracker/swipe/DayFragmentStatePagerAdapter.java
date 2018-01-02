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
package org.ounl.lifelonglearninghub.nfclearntracker.swipe;

import java.util.List;

import org.ounl.lifelonglearninghub.nfclearntracker.db.tables.Tagctivity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

/**
 * A {@link android.support.v4.app.FragmentStatePagerAdapter} that returns a fragment
 * representing an object in the collection.
 */
public class DayFragmentStatePagerAdapter extends FragmentStatePagerAdapter {

	private List<Tagctivity> listTagactivity;
	
    public DayFragmentStatePagerAdapter(FragmentManager fm, List<Tagctivity> lt) {
        super(fm);
        
        listTagactivity = lt;
    }

    @Override
    public Fragment getItem(int i) {
        Fragment fragment = new DayFragment();
        Bundle args = new Bundle(); 
        args.putInt(DayFragment.ARG_DURATION, listTagactivity.get(i + 1).durationMins());
        // AQUI HABRA QUE PASARLE EL OBJETO ENTERO
        fragment.setArguments(args);
        
        
        return fragment;
    }

    @Override
    public int getCount() {
        // For this contrived example, we have a 100-object collection.
        return listTagactivity.size()-1;
    }

    @Override
    public CharSequence getPageTitle(int position) {
    	if (position >= getCount()){
    		// Index out of bounds
        	//listTagactivity.get(position).getlDateCheckIn();        	
            return "This the end, my only friend";    		
    	}else{
        	//listTagactivity.get(position).getlDateCheckIn();        	
            return "" + listTagactivity.get(position).getFormatedLongCheckIn();	
    	}
    	

    }
}