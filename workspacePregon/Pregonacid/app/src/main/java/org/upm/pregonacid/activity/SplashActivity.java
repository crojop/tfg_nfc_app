package org.upm.pregonacid.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.ActionMenuView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import org.upm.pregonacid.EventPoolingService;
import org.upm.pregonacid.PregonacidApplication;
import org.upm.pregonacid.PregonacidConstants;
import org.upm.pregonacid.R;
import org.upm.pregonacid.db.DatabaseHandler;
import org.upm.pregonacid.db.tables.EventDb;
import org.upm.pregonacid.db.ws.EventWSGetAsyncTask;
import org.upm.pregonacid.db.ws.dataobjects.EventDO;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.ExecutionException;



public class SplashActivity extends AppCompatActivity {
	
	private String CLASSNAME = this.getClass().getName();
	
	PregonacidApplication pa;
	List<EventDb> listEventsDb;
	private ActionMenuView amvMenu;



	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.splash);

		Toolbar myToolbar = (Toolbar) findViewById(R.id.tToolbar);
		myToolbar.setLogo(R.drawable.ic_launcher);
		myToolbar.setTitle("Pregonacid");
		myToolbar.setSubtitle("de la Sierra");
		setSupportActionBar(myToolbar);


				
		pa = (PregonacidApplication)getApplication();
		
		// Load properties file from asset folder
		try {
			pa.setConfig(loadConfigProperties(PregonacidConstants.CONFIG_PROPERTIES_FILE));
		} catch (IOException e) {
			Log.e(CLASSNAME, "Could not load properties file "+e.getMessage());
			e.printStackTrace();
		}
		
		// Init data base handler
		DatabaseHandler db = new DatabaseHandler(this);
		pa = (PregonacidApplication)getApplicationContext();
		pa.setDb(db);
		
		
		// Start notification service
		startService(new Intent(this, EventPoolingService.class));
		

	}
	
	@Override
	protected void onResume() {
		super.onResume();

		listEventsDb = pa.getDb().getEvents();
		if (listEventsDb.size() == 0) {
			// Load data from backend into local db and session
			populateEventsFromBackend(pa.getConfig().getProperty(PregonacidConstants.CP_COURSE_ID));
		} else {
			// Load into session
			pa.setEvents(listEventsDb);
		}	

		
		
		// Fill in form data
		TextView tvVers = (TextView) findViewById(R.id.tvPropVersion);								
		tvVers.setText("Version "+pa.getConfig().getProperty(PregonacidConstants.CP_VERSION));

	}
	

	/**
	 * Populates list from backend database. 
	 * 1) Save list into session
	 * 2) Save list into local database
	 * 
	 * This method is only executed once when configuration is loaded for first time
	 */
	private void populateEventsFromBackend(String sCoursId) {

		Log.d(CLASSNAME, "Loading events "+sCoursId+" .");		
		List<EventDO> lista;		
		EventWSGetAsyncTask wsat = new EventWSGetAsyncTask();
		
		try {

			String sURL = pa.getConfig().getProperty(PregonacidConstants.CP_WS_GET_EVENTS_PATH);
			//String sURL = pa.getConfig().getProperty(PregonacidConstants.CP_WS_GET_EVENTS_PATH)+sCoursId;
			lista = wsat.execute(sURL).get();
			
			if (lista != null){
				Log.e(CLASSNAME, "Backend returned list with "+lista.size()+" events.");
				
				// Save events into local database
				pa.getDb().addListEventsDO(lista);
								
				// Save activities into session
				pa.setEventsDO(lista);				
				
				if(lista.size() < 1){
					Log.e(CLASSNAME, "Backend returned empty list of events.");
					Toast.makeText(getApplicationContext(),
							"Backend returned empty list of events.", Toast.LENGTH_LONG)
							.show();
					
//					// Subjcts could not be loaded from backend. Load fake data
//					// Init database with fake data
//					List<EventDb> ldb = Session.getSingleInstance().getDatabaseHandler().addDefaultSubjects();
//					// Init session with fake data
//					Session.getSingleInstance().initActivitiesDb(ldb);
					
				}
				
			}else{
				Log.e(CLASSNAME, "Backend returned empty list of subjects.");
			}

			Intent intent = new Intent(this, EventListActivity.class);
			startActivity(intent);
			this.finish();


		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
				
	}


	/**
	 * Load properties
	 * 
	 * @return list of poperties loaded
	 * @throws IOException
	 */
	private Properties loadConfigProperties(String sFile) throws IOException {

		String[] fileList = { sFile };
		Properties prop = new Properties();
		for (int i = fileList.length - 1; i >= 0; i--) {
			String file = fileList[i];
			try {
				InputStream fileStream = getAssets().open(file);
				prop.load(fileStream);
				fileStream.close();
			} catch (FileNotFoundException e) {
				Log.e(CLASSNAME, "Config file not found. " + file);
			}
		}
		return prop;
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		Toast.makeText(getApplicationContext(),
				"Execute onCreateOptionsMenu.", Toast.LENGTH_LONG)
				.show();

		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		Toast.makeText(getApplicationContext(),
				"Execute onOptionsItemSelected.", Toast.LENGTH_LONG)
				.show();

		return true;
	}


	public void onClickSalir(View v) {
		finish();
		System.exit(0);
	}


	public void onClickSwipeButton(View v) {
		Intent intent = new Intent(this, EventListActivity.class);
		startActivity(intent);
		this.finish();
	}
	
}