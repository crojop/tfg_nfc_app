package org.upm.pregonacid.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v4.app.TaskStackBuilder;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import org.upm.pregonacid.PregonacidApplication;
import org.upm.pregonacid.R;
import org.upm.pregonacid.db.ListViewEventsAdapter;
import org.upm.pregonacid.db.tables.EventDb;

import java.util.ArrayList;
import java.util.HashMap;


public class EventListActivity extends AppCompatActivity {

	private ArrayList<HashMap> list;
	private ListView lview;
	private Intent intent;
	private ListViewEventsAdapter adapter;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.fragment_event_list);


		Toolbar myToolbar = (Toolbar) findViewById(R.id.tToolbar);
		myToolbar.setLogo(R.drawable.ic_launcher);
		myToolbar.setTitle("Pregonacid");
		myToolbar.setSubtitle("de la Sierra");
		setSupportActionBar(myToolbar);

        
		intent = new Intent(this, TimelineActivity.class);
		lview = (ListView) findViewById(R.id.listviewSubjects);

		lview.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

				intent.putExtra("POSITION", "" + position);
				startActivity(intent);


			}

		});

	}
	
	
	
	
	@Override	
	protected void onResume(){
		super.onResume();
		
		// Populate SQLlite list
		populateSubjectsFromLocal();

		adapter = new ListViewEventsAdapter(this,list);
		lview.setAdapter(adapter);
		
	}
	
	
	

	/**
	 * Populates list from local database
	 */
	private void populateSubjectsFromLocal() {
		list = new ArrayList<HashMap>();

		PregonacidApplication pa = (PregonacidApplication)getApplication();


		for (EventDb t : pa.getDb().getEvents()) {

			// Records for data
			HashMap temp = new HashMap<String, String>();
			temp.put(EventDb.KEY_ID, t.getlId());
			temp.put(EventDb.KEY_TIMESTAMP, t.getlTimeStamp());
			temp.put(EventDb.KEY_TITLE, t.getsTitle());
			temp.put(EventDb.KEY_SUBTITLE, t.getsSubTitle());
			temp.put(EventDb.KEY_SUBSUBTITLE, t.getsSubSubTitle());
			temp.put(EventDb.KEY_AUTHOR, t.getsAuthor());
			temp.put(EventDb.KEY_STATE, t.getiState());


		
			list.add(temp);
		}
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
/*
			case R.id.action_refresh:
				Toast.makeText(this, "Refresh selected", Toast.LENGTH_SHORT).show();

				break;
			case R.id.action_settings:

				Toast.makeText(this, "Action Settings selected", Toast.LENGTH_SHORT).show();
				break;

*/
			default:
				break;
		}


        return super.onOptionsItemSelected(item);
    }

	public void onClickSalir(View v) {
		finish();
		System.exit(0);
	}
	

}