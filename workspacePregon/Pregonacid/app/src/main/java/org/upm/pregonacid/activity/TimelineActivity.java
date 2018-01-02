package org.upm.pregonacid.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.NavUtils;
import android.support.v4.app.TaskStackBuilder;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import org.upm.pregonacid.PregonacidApplication;
import org.upm.pregonacid.R;
import org.upm.pregonacid.util.WhatsappUtils;

import java.util.ArrayList;
import java.util.List;

public class TimelineActivity extends FragmentActivity {
	
	private String CLASSNAME = this.getClass().getName();

	private EventFragmentStatePagerAdapter mDemoCollectionPagerAdapter;
    private ViewPager mViewPager;
    private List<EventFragment> lDayFragments;
    PregonacidApplication pa;
    

    private int iCurrentPos = 0;


    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.event_list_container);


        pa = (PregonacidApplication)getApplication();
        lDayFragments = new ArrayList<EventFragment>();
        
        Bundle extras = getIntent().getExtras();   
        if (extras != null) {
            String sPosition =  extras.getString("POSITION");
            Integer oiPosition = new Integer(sPosition);
            iCurrentPos = oiPosition.intValue();
        }                        
        
        for (int i = 0; i < pa.getDb().getEvents().size(); i++) {
        	EventFragment dayF = new EventFragment();
        	
            Bundle args = new Bundle();
            args.putInt(EventFragment.ARG_POSITION, i);
            args.putString(EventFragment.ARG_SUBTITLE, pa.getEvents().get(i).getsSubTitle());
            args.putString(EventFragment.ARG_SUBSUBTITLE, pa.getEvents().get(i).getsSubSubTitle());

            dayF.setArguments(args);
            
            lDayFragments.add(dayF);
            
		}
    }


    
	@Override	
	protected void onResume(){
		super.onResume();
		Log.d(CLASSNAME, "onResume TimelineActivity.");
		
        mDemoCollectionPagerAdapter = new EventFragmentStatePagerAdapter(getSupportFragmentManager(), lDayFragments, pa.getEvents());        
        mDemoCollectionPagerAdapter.notifyDataSetChanged();        
        
        mViewPager = (ViewPager) findViewById(R.id.pager);
        mViewPager.setAdapter(mDemoCollectionPagerAdapter);
        mViewPager.setCurrentItem(iCurrentPos);
       		
	}


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                // This is called when the Home (Up) button is pressed in the action bar.
                // Create a simple intent that starts the hierarchical parent activity and
                // use NavUtils in the Support Package to ensure proper handling of Up.
                Intent upIntent = new Intent(this, EventListActivity.class);
                if (NavUtils.shouldUpRecreateTask(this, upIntent)) {
                    // This activity is not part of the application's task, so create a new task
                    // with a synthesized back stack.
                    TaskStackBuilder.from(this)
                            // If there are ancestor activities, they should be added here.
                            .addNextIntent(upIntent)
                            .startActivities();
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

    
    
    
	public void onClickSwitchAction(View v) {

        //
        // Alternatively this data could be obtained from local database
        //
        View vRoot = (View) v.getParent().getParent();
        TextView tvTitle = (TextView)vRoot.findViewById(R.id.tvTitle);
        TextView tvSubTitle = (TextView)vRoot.findViewById(R.id.tvSubTitle);
        TextView tvSubSubTitle = (TextView)vRoot.findViewById(R.id.tvSubSubTitle);

        String sMessage = WhatsappUtils.toBold(tvTitle.getText().toString());
        sMessage += " | "+tvSubTitle.getText().toString();
        sMessage += " | "+tvSubSubTitle.getText().toString();
        sMessage += " | "+WhatsappUtils.getFirma();

		Intent whatsappIntent = new Intent(Intent.ACTION_SEND);
		whatsappIntent.setType("text/plain");
		whatsappIntent.setPackage("com.whatsapp");
		whatsappIntent.putExtra(Intent.EXTRA_TEXT, sMessage);

		try {
			startActivity(whatsappIntent);
		} catch (android.content.ActivityNotFoundException ex) {
			// This message should be returned to the user
			System.out.println("Whatsapp is not installed.!!!");
		}

	}


}
