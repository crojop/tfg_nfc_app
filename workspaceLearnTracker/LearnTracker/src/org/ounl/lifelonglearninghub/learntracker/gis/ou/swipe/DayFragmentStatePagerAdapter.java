 /*******************************************************************************
 * Copyright (C) 2014 Open University of The Netherlands
 * Author: Bernardo Tabuenca Archilla
 * LearnTracker project 
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
package org.ounl.lifelonglearninghub.learntracker.gis.ou.swipe;

import java.util.List;

import org.ounl.lifelonglearninghub.learntracker.gis.ou.session.Session;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.util.Log;

/**
 * A {@link android.support.v4.app.FragmentStatePagerAdapter} that returns a fragment
 * representing an object in the collection.
 */
public class DayFragmentStatePagerAdapter extends FragmentStatePagerAdapter {

	private String CLASSNAME = this.getClass().getName();
	
	private List<DayFragment> mFragments;
	

    public DayFragmentStatePagerAdapter(FragmentManager fm, List<DayFragment> fragments) {
        super(fm);        
        mFragments = fragments;
    }
    


    @Override
    public Fragment getItem(int i) {
    	
    	Log.d(CLASSNAME, "getItem DayFragmentStatePagerAdapter position["+i+"].");
    	
//        Fragment fragment = mFragments.get(i);        
//        Bundle args = new Bundle();
//        args.putInt(DayFragment.ARG_POSITION, i);
//        fragment.setArguments(args);
        
        
        return mFragments.get(i);
    }

    @Override
    public int getCount() {
        return mFragments.size();
    }

    
    /**
     * This is the header on top of the screen
     */
    @Override
    public CharSequence getPageTitle(int position) {
    	
    	Log.d(CLASSNAME, "getPageTitle DayFragmentStatePagerAdapter position["+position+"].");
    	
    	if (position >= getCount()){
    		// Index out of bounds        	
            return "This the end, my only friend the end";    		
    	}else{
    		return Session.getSingleInstance().getActivity(position).getSubject_task_desc();
    	}    	
    }
//    
//    @Override
//    public Object instantiateItem(View collection, int position) {
//    	
//    	Log.d(CLASSNAME, "instantiateItem DayFragmentStatePagerAdapter View["+collection.getClass().getName()+"] positon["+position+"].");
//    	
//    	return this.getItem(position);
//    }
//    
    @Override
    public int getItemPosition(Object item) {
    	DayFragment dayF = (DayFragment)item;
    	
    	Log.d(CLASSNAME, "getItemPosition DayFragmentStatePagerAdapter super.position["+super.getItemPosition(item)+"] positon["+dayF.getPosition()+"].");
    	
//    	this.getItem(dayF.getPosition());
//    	
//    	this.getItem(super.getItemPosition(item));
    	
    	
    	
        return POSITION_NONE;
    }
    
    
    @Override
    public void notifyDataSetChanged() {
        super.notifyDataSetChanged();
        Log.d(CLASSNAME, "notifyDataSetChanged DayFragmentStatePagerAdapter.");
        
        
        
        
    }
    
    

	
}