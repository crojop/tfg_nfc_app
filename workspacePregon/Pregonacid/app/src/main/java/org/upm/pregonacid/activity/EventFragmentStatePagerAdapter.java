package org.upm.pregonacid.activity;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.util.Log;

import org.upm.pregonacid.db.tables.EventDb;

import java.util.List;

/**
 * A {@link android.support.v4.app.FragmentStatePagerAdapter} that returns a fragment
 * representing an object in the collection.
 */
public class EventFragmentStatePagerAdapter extends FragmentStatePagerAdapter {

	private String CLASSNAME = this.getClass().getName();
	
	private List<EventFragment> mFragments;
	private List<EventDb> mEvents;
	

    public EventFragmentStatePagerAdapter(FragmentManager fm, List<EventFragment> fragments, List<EventDb> events) {
        super(fm);        
        mFragments = fragments;
        mEvents = events;
    }
    


    @Override
    public Fragment getItem(int i) {
    	
    	Log.d(CLASSNAME, "getItem EventFragmentStatePagerAdapter position["+i+"].");
    	
    	
//        Fragment fragment = mFragments.get(i);        
//        Bundle args = new Bundle();
//        args.putInt(EventFragment.ARG_POSITION, i);
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
    	
    	Log.d(CLASSNAME, "getPageTitle EventFragmentStatePagerAdapter position["+position+"].");
    	
    	if (position >= getCount()){
    		// Index out of bounds        	
            return "Index out of bounds. There is something wrong here :(";    		
    	}else{
    		return ""+mEvents.get(position).getFormattedTaskDateStart();
    	}
    	
    }

  
    @Override
    public int getItemPosition(Object item) {
    	EventFragment dayF = (EventFragment)item;
    	
    	Log.d(CLASSNAME, "getItemPosition EventFragmentStatePagerAdapter super.position["+super.getItemPosition(item)+"] positon["+dayF.getPosition()+"].");
    	
        return POSITION_NONE;
    }
    
    
    @Override
    public void notifyDataSetChanged() {
        super.notifyDataSetChanged();
        Log.d(CLASSNAME, "notifyDataSetChanged EventFragmentStatePagerAdapter.");
        
    }
    
    

	
}