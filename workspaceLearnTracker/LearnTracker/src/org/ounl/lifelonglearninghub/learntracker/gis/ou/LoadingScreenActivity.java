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
package org.ounl.lifelonglearninghub.learntracker.gis.ou;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.text.Collator;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.TreeSet;
import java.util.concurrent.ExecutionException;

import org.ounl.lifelonglearninghub.learntracker.gis.ou.db.tables.ActividadDb;
import org.ounl.lifelonglearninghub.learntracker.gis.ou.db.tables.SubjectDb;
import org.ounl.lifelonglearninghub.learntracker.gis.ou.db.ws.ActivityCourseWSGetAsyncTask;
import org.ounl.lifelonglearninghub.learntracker.gis.ou.db.ws.ActivityWSGetAsyncTask;
import org.ounl.lifelonglearninghub.learntracker.gis.ou.db.ws.SubjectWSGetAsyncTask;
import org.ounl.lifelonglearninghub.learntracker.gis.ou.db.ws.UserWSGetAsyncTask;
import org.ounl.lifelonglearninghub.learntracker.gis.ou.db.ws.dataobjects.ActivityDO;
import org.ounl.lifelonglearninghub.learntracker.gis.ou.db.ws.dataobjects.SubjectDO;
import org.ounl.lifelonglearninghub.learntracker.gis.ou.db.ws.dataobjects.UserDO;
import org.ounl.lifelonglearninghub.learntracker.gis.ou.session.ActivitySession;
import org.ounl.lifelonglearninghub.learntracker.gis.ou.session.Session;
import org.ounl.lifelonglearninghub.learntracker.gis.ou.swipe.SubjectsActivity;
import org.ounl.lifelonglearninghub.learntracker.gis.ou.R;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class LoadingScreenActivity extends Activity {
	
	private String CLASSNAME = this.getClass().getName();

	// Ordered list of users by name
	private Collection<String> cUserNames;
	
	// Key is the userName
	private HashMap<String, UserDO> hmUsers = new HashMap<String, UserDO>(); 
	
	// List of subjects from local database
	private List<SubjectDb> lSDb;
	
	// Selected user
//	private UserDO user;
	


	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.splash);

		Session.getSingleInstance().initDatabaseHandler(this);		
		lSDb = Session.getSingleInstance().getDatabaseHandler().getSubjects();

	}
	
	@Override	
	protected void onResume(){
		super.onResume();


		if (Session.getSingleInstance().isConfigured()) {
			Session.getSingleInstance().initActivitiesDb(lSDb);

		} else {
			
			Log.e(CLASSNAME, "Session not yet configured.");
			Session.getSingleInstance().printProperties();

			
			// Load properties file from asset folder
			try {
				Session.getSingleInstance().setProperties(loadConfigProperties(Constants.CONFIG_PROPERTIES_FILE));				
			} catch (IOException e) {
				Log.e(CLASSNAME, "Could not load properties file "+e.getMessage());
				e.printStackTrace();
			}
			
					
			// READ USER FROM SESSION
			if(!Session.getSingleInstance().hasUserName()){
				// IF NOT VALID, READ FROM PROPERTIES FILE
				// Load user values into session
				Session.getSingleInstance().setUserName(readFromFile(Constants.USER_PROPERTIES_FILE, Constants.UP_USER_ID));
				String sUType = readFromFile(Constants.USER_PROPERTIES_FILE, Constants.UP_USER_TYPE);
				Integer oI = 0;
				if(sUType.length()>0){
					oI = new Integer(sUType);
					Session.getSingleInstance().setUserType(oI.intValue());				
				}				

				
				if(!Session.getSingleInstance().hasUserName()){
					// IF NOT VALID, 
					//	READ LIST FROM BACKEND 
					//	AND PROMPT USER
					//readUsersFromBackend(Session.getSingleInstance().getCourse_id());
					
					setDefaultUser();
					
					
				}else{
					Log.d(CLASSNAME, "User configured in properties file: "+ Session.getSingleInstance().getUserName());
				}
				
	
			}else{
				Log.d(CLASSNAME, "User already configured in session"+ Session.getSingleInstance().getUserName());
			}
			
			//
			// Initialize local database with data from backend
			//
			
			// Subjects
			lSDb = Session.getSingleInstance().getDatabaseHandler().getSubjects();
			if (lSDb.size() == 0){
				String sCourseId = Session.getSingleInstance().getCourse_id();
				if(sCourseId!=null){
					if(sCourseId.length() > 0){
						Log.d(CLASSNAME, "Loading subjects for course idt "+sCourseId);
						populateSubjectsFromBackend(sCourseId);
					}				
				}				
			}else{
				Session.getSingleInstance().initActivitiesDb(lSDb);			
			}
			


			
			// Activities
			List<ActividadDb> lADb = Session.getSingleInstance().getDatabaseHandler().getActivities();
			if (lADb.size() == 0){
				String sUser =  Session.getSingleInstance().getUserName();
				if(sUser!=null){
					if(sUser.length() > 0){
						String sCourseId = Session.getSingleInstance().getCourse_id();
						if(sCourseId!=null){
							if(sCourseId.length() > 0){
								populateActivitiesFromBackend(sCourseId, sUser);
							}
						}
					}
				}				
			}

			Log.d(CLASSNAME, "Activities were already loaded. List of activities returned from local database "+lADb.size());
			
		}
		
		showDataSplash();
		
		// if social treatment, load activities summary into session
		if (Session.getSingleInstance().getUserType()==Constants.USER_TYPE_SOCIAL){
			loadSocialData();
		}		
		
	}
	
	/**
	 * Load from backend average to time devoted by student and task
	 * and store it into session
	 * 
	 */
	private void loadSocialData(){

		
	
		List<ActivitySession> las = Session.getSingleInstance().getActivities();
		String sCourseId = Session.getSingleInstance().getCourse_id();
		Log.d(CLASSNAME, "The course "+sCourseId+" has "+las.size()+" activities.");
		


		//
		// Key String is the subject id
		// Value HashMap<String,Long> is the list of users that have recorded time on this subject
		//
		HashMap<String,HashMap<String,Long>> hmSubjects = new HashMap<String, HashMap<String,Long>>();

		
		
		// Query all the activities registered for this course
		List<ActivityDO> listAllActivities;
		ActivityCourseWSGetAsyncTask wsat = new ActivityCourseWSGetAsyncTask();
		
		try {
			listAllActivities = wsat.execute(sCourseId).get();
			
			if (listAllActivities != null){
				Log.d(CLASSNAME, "Backend returned social list with "+listAllActivities.size()+" activities.");
				
				if(listAllActivities.size() < 1){
					Log.e(CLASSNAME, "Activities list is empty");
					Toast.makeText(getApplicationContext(),
							"Backend returned empty list of activities", Toast.LENGTH_LONG)
							.show();
				}else{
					// Save activities into session
					
					for (ActivityDO activityDO : listAllActivities) {
						
						//
						// Key String is the user id
						// Value Long is the accumulated time by the user on this task
						//
						HashMap<String,Long> hmUsers = hmSubjects.get(activityDO.getId_subject());
						if(hmUsers == null){
							// This subject has no users in hashmap yet. Hence this is the first entry.
							hmUsers = new HashMap<String, Long>();
							hmUsers.put(activityDO.getId_user(), activityDO.getActivity_date_checkout()-activityDO.getActivity_date_checkin());
							hmSubjects.put(activityDO.getId_subject(), hmUsers);
						}else{
							// This subject has users. Then look for this specific user entry
							Long lTimeAccumUserSubject =  (Long)hmUsers.get(activityDO.getId_user());
							if(lTimeAccumUserSubject == null){
								// Does not exist yet. First activity of the user in this subject. Accumulate record
								hmUsers.put(activityDO.getId_user(), activityDO.getActivity_date_checkout()-activityDO.getActivity_date_checkin());								
							}else{
								// This user has accumulated time for this specific subject. Then accumulate this record
								long lAccumulated = (Long)hmUsers.get(activityDO.getId_user());
								lAccumulated = lAccumulated + activityDO.getActivity_date_checkout()-activityDO.getActivity_date_checkin();
								hmUsers.put(activityDO.getId_user(), lAccumulated);
							}							
						}
						
					}					
				}				
			}else{
				Log.e(CLASSNAME, "Backend returned empty list of activities.");
			}		
			
			
			
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	
		
		// Save social activity into session
		Session.getSingleInstance().setSocialActivity(hmSubjects);
		
		
	}
	
	
	
	
	/**
	 * Populates list from backend database. 
	 * 1) Save list into session
	 * 2) Save list into local database
	 * 
	 * This method is only executed once when configuration is loaded for first time
	 */
	private void populateSubjectsFromBackend(String sCoursId) {

		Log.d(CLASSNAME, "Loading subjects for the course "+sCoursId+" .");
		
		List<SubjectDO> lista;

		SubjectWSGetAsyncTask wsat = new SubjectWSGetAsyncTask();
		
		try {
			lista = wsat.execute(sCoursId).get();
			
			if (lista != null){
				Log.d(CLASSNAME, "Backend returned list with "+lista.size()+" subjects.");
				
				// Save activities into local database
				Session.getSingleInstance().getDatabaseHandler().addSubjectDO(lista);
				// Save activities into session
				Session.getSingleInstance().initActivitiesDO(lista);
				
				if(lista.size() < 1){
					Log.e(CLASSNAME, "Subject list is empty");
					Toast.makeText(getApplicationContext(),
							"Backend returned empty list of subjects. Default course will be dispayed", Toast.LENGTH_LONG)
							.show();
					
					// Subjcts could not be loaded from backend. Load fake data
					// Init database with fake data
					List<SubjectDb> ldb = Session.getSingleInstance().getDatabaseHandler().addDefaultSubjects();
					// Init session with fake data
					Session.getSingleInstance().initActivitiesDb(ldb);
					
					
				}
				
			}else{
				Log.e(CLASSNAME, "Backend returned empty list of subjects.");
			}
			
			
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
		
		
	}
	
	
	
	
	private void populateActivitiesFromBackend(String sCoursId, String sUserId) {

		Log.d(CLASSNAME, "Loading activities for the course "+sCoursId+" and user id "+sUserId+" .");
		List<ActivityDO> lista;

		ActivityWSGetAsyncTask wsat = new ActivityWSGetAsyncTask();
		
		try {
			lista = wsat.execute(sCoursId, sUserId).get();
			
			if (lista != null){
				Log.d(CLASSNAME, "Backend returned list with "+lista.size()+" activities.");
				
				// Save activities into local database
				Session.getSingleInstance().getDatabaseHandler().addActivityDO(lista);
				
				// Save activities into session				
				if(lista.size() < 1){
					Log.e(CLASSNAME, "Activities list is empty");
//					Toast.makeText(getApplicationContext(),
//							"Backend returned empty list of activities", Toast.LENGTH_LONG)
//							.show();
				}
				
			}else{
				Log.e(CLASSNAME, "Backend returned empty list of activities.");
			}
			
			
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
		
		
	}
	

	
	/**
	 * Queries list of users from backend database into local variable
	 * to show it in the dialog 
	 * 
	 * This method is only executed once when configuration is loaded for first time
	 */
//	private void readUsersFromBackend(String sCoursId) {
//
//		Log.d(CLASSNAME, "Loading suers for the course "+sCoursId+" .");
//		
//		List<UserDO> listUsers;
//
//		UserWSGetAsyncTask wsat = new UserWSGetAsyncTask();
//		
//		try {
//			listUsers = wsat.execute(sCoursId).get();
//			
//			if (listUsers != null){
//				Log.d(CLASSNAME, "Backend returned list with "+listUsers.size()+" users.");
//				
//
//				// Order list and
//				cUserNames =  new TreeSet<String>(Collator.getInstance());
//
//				for (Iterator iterator = listUsers.iterator(); iterator.hasNext();) {
//					
//					UserDO u = (UserDO) iterator.next();
//					hmUsers.put(u.getUserName(), u); 
//					cUserNames.add(u.getUserName());
//					
//				}
//				promptUserAcronym();
//				
//
//				
//			}else{
//				Log.e(CLASSNAME, "Backend returned empty list of users.");
//			}
//			
//			
//		} catch (InterruptedException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} catch (ExecutionException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}		
//		
//		
//	}
	
	/**
	 * Set default user name (current time) and type (33)
	 */
	private void setDefaultUser() {
		
		Date d = new Date();		
		DateFormat formatter = new SimpleDateFormat("ddMMyyyyHHmmss");
		formatter.format(d);
		
		int iDefaultType = 33;
		
    	Session.getSingleInstance().setUserName(formatter.format(d));
    	Session.getSingleInstance().setUserType(iDefaultType);
    	Session.getSingleInstance().printProperties();
    	
    	
    	// Write into mobile for forthcoming sessions
    	writeUserPropertiesFile(formatter.format(d), ""+iDefaultType);
		
	}
	
	
	

	

	/**
	 * Prompt user to select his user name from a list 
	 * 
	 */
//	private void promptUserAcronym() {
//
//		
//		AlertDialog.Builder builderSingle = new AlertDialog.Builder(LoadingScreenActivity.this);
//        builderSingle.setIcon(R.drawable.ic_launcher);
//        builderSingle.setTitle("Select your user:");
//        final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(
//                this,
//                android.R.layout.select_dialog_singlechoice);
//        
//        for (Iterator it = cUserNames.iterator(); it.hasNext();) {
//			String u = (String) it.next();
//			arrayAdapter.add(u);
//			
//		}
//
//
//        builderSingle.setAdapter(arrayAdapter,
//                new DialogInterface.OnClickListener() {
//
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        String sUserName = arrayAdapter.getItem(which);
//                        user = hmUsers.get(sUserName);
//                        
//                    	Log.i(CLASSNAME, "Selected "+arrayAdapter.getItem(which));
//                    	                    	
//                    	promptPass();
//                    }
//                });
//        builderSingle.show();		
//
//	}
	
	private void promptCourses() {

		
		AlertDialog.Builder builderSingle = new AlertDialog.Builder(LoadingScreenActivity.this);
        builderSingle.setIcon(R.drawable.course_50x);
        builderSingle.setTitle("Select course:");
        final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(
                this,
                android.R.layout.select_dialog_singlechoice);

        // AQUI tendras que añadir cursos que leas de la query y si no hay conexion meter el curso po defecto
        arrayAdapter.add(Session.getSingleInstance().getCourse_id());
 
        
        
        builderSingle.setAdapter(arrayAdapter,
                new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        
                    	Log.i(CLASSNAME, "Selected course "+arrayAdapter.getItem(which));

                		TextView tvCourseId = (TextView) findViewById(R.id.tvPropCourseId);
                		tvCourseId.setText("Course: "+arrayAdapter.getItem(which));                		
                    	
                    	                    	

                    }
                });
        builderSingle.show();		

	}
	


//	public void promptPass() {
//		
//		LayoutInflater li = LayoutInflater.from(this);
//		View promptsView = li.inflate(R.layout.prompts, null);
//		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
//		alertDialogBuilder.setView(promptsView);
//
//		TextView tvPrompt = (TextView) promptsView.findViewById(R.id.textViewPrompt);
//		tvPrompt.setText("Enter password for user "+ user.getUserName());
//		final EditText userInput = (EditText) promptsView
//				.findViewById(R.id.editTextPrompt);
//		userInput.setText("123");
//
//		alertDialogBuilder.setCancelable(false).setPositiveButton(
//				"Ready", new DialogInterface.OnClickListener() {
//					public void onClick(DialogInterface dialog, int id) {
//						
//
//						
//						String sProp = Session.getSingleInstance().getCourse_id().toLowerCase();
//						// TODO Modify this hardcoded password
//						sProp = "ounl".toLowerCase();
//						String sEnter = userInput.getText().toString().toLowerCase();
//						
//// Password prompt commented temporarily						
////						if(sProp.compareTo(sEnter) == 0){
//							
//	                    	// Write into session
//	                    	Session.getSingleInstance().setUserName(user.getUserName());
//	                    	Session.getSingleInstance().setUserType(user.getUserType());
//	                    	Session.getSingleInstance().printProperties();
//	                    	
//	                    	// Write into mobile for forthcoming sessions
//	                    	writeUserPropertiesFile(user.getUserName(), ""+user.getUserType());
//
//
//	                    	showDataSplash();
//	                    	
//	                    	// Populate activities for this user
//	                    	populateActivitiesFromBackend(Session.getSingleInstance().getCourse_id(), user.getUserName());							
//							
//// Password prompt commented temporarily							
////						}else{
////							Toast.makeText(getApplicationContext(),
////									"Wrong password. Ask your teacher for the login information.", 
////									Toast.LENGTH_LONG)
////									.show();
////							showBlockingSplash();
////						}
//						
//						
//						
//						dialog.dismiss();
//
//					}
//				});
//
//		AlertDialog alertDialog = alertDialogBuilder.create();
//		alertDialog.show();
//	}	
	

	/**
	 * Prompt new user name
	 */
	public void promptCustomUser() {
		
		LayoutInflater li = LayoutInflater.from(this);
		View promptsView = li.inflate(R.layout.prompts, null);
		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
		alertDialogBuilder.setView(promptsView);

		TextView tvPrompt = (TextView) promptsView.findViewById(R.id.textViewPrompt);
		tvPrompt.setText("Update user name");
		final EditText userInput = (EditText) promptsView
				.findViewById(R.id.editTextPrompt);
				
		if(Session.getSingleInstance().getUserName() != null){
			userInput.setText(Session.getSingleInstance().getUserName());
		}
		

		alertDialogBuilder.setCancelable(true).setPositiveButton(
				"Done", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						
						String sEnter = userInput.getText().toString().toLowerCase();
						

	                    	// Write into session
	                    	Session.getSingleInstance().setUserName(sEnter);
	                    	Session.getSingleInstance().setUserType(0);
	                    	
	                    	
	                    	// Write into mobile properties for forthcoming sessions
	                    	writeUserPropertiesFile(sEnter, ""+0);
	                    	Session.getSingleInstance().printProperties();


	                    	showDataSplash();
	                    	
	                    	// Populate activities for this user
	                    	//populateActivitiesFromBackend(Session.getSingleInstance().getCourse_id(), user.getUserName());							
							
						
						dialog.dismiss();

					}
				});
		
//		alertDialogBuilder.setCancelable(true).setPositiveButton(
//				"Cancel", new DialogInterface.OnClickListener() {
//					public void onClick(DialogInterface dialog, int id) {
//												
//						dialog.dismiss();
//
//					}
//				});
		

		AlertDialog alertDialog = alertDialogBuilder.create();
		alertDialog.show();
	}	
	
	
	

	public void onClickSwipeButton(View v) {
		Intent intent = new Intent(this, SubjectsActivity.class);
		startActivity(intent);

	}
	
	public void onClickMenu(View v) {
		Log.e(CLASSNAME, "User clicked Menu icon");
		
		promptCourses();
	}	

	
	public void onClickUser(View v) {
		Log.e(CLASSNAME, "User clicked User icon ");
		promptCustomUser();
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
				Log.e(CLASSNAME, "Ignoring missing property file " + file);
			}
		}
		return prop;
	}
	

	private void writeUserPropertiesFile(String sUserId, String sUserType){
		
		    try {
		    	
		    	String sFile = Constants.USER_PROPERTIES_FILE;
		    	
		    	// This line creates a file in the following path:
		    	// /data/data/org.ounl.lifelonglearninghub.learntracker/files
		    	// and writes
		    	// user_id=Bernd
		        OutputStreamWriter outputStreamWriter = new OutputStreamWriter(openFileOutput(sFile, Context.MODE_PRIVATE));
		        
		        // Write user id
		        String propertyId = Constants.UP_USER_ID + "=" + sUserId + "\n";
		        outputStreamWriter.write(propertyId);
		        
		        // Write user type
		        String propertyType = Constants.UP_USER_TYPE + "=" + sUserType + "\n";
		        outputStreamWriter.write(propertyType);
		        
		        outputStreamWriter.close();
		        Log.d(CLASSNAME, "NEW FILE " + sFile + " VALUES " + propertyId + " : " + propertyType);
		    }
		    catch (IOException e) {
		        Log.e("Exception", "File write failed: " + e.toString());
		    } 
		


		
		
		
	}

	private void printUserPropertiesFile(){
		
		File sdcard = Environment.getExternalStorageDirectory();

		//Get the text file
		File file = new File(sdcard,Constants.USER_PROPERTIES_FILE);

		//Read text from file
		StringBuilder text = new StringBuilder();

		try {
		    BufferedReader br = new BufferedReader(new FileReader(file));
		    String line;

		    while ((line = br.readLine()) != null) {
		        text.append(line);
		        text.append('\n');
		        Log.d(CLASSNAME, "UPF "+line);
		    }
		    br.close();
		}
		catch (IOException e) {
		    //You'll need to add proper error handling here
			e.printStackTrace();
		}		
	}
	
	
	
	private String readFromFile(String sFile, String sKey) {

	    String ret = "";

	    try {
	        InputStream inputStream = openFileInput(sFile);

	        if ( inputStream != null ) {
	        	
				Properties props=new Properties();
	
				props.load(inputStream);
				ret = props.getProperty(sKey);
	        	
	        }
	    }catch (FileNotFoundException e) {
	        Log.e(CLASSNAME, "File not found: " + e.toString());
	    } catch (IOException e) {
	        Log.e(CLASSNAME, "Can not read file: " + e.toString());
	    }

	    return ret;
	}
		
	
	private void showDataSplash(){
		TextView tvUserId = (TextView) findViewById(R.id.tvPropUserId);
		TextView tvCourseId = (TextView) findViewById(R.id.tvPropCourseId);
		TextView tvVers = (TextView) findViewById(R.id.tvPropVersion);
		
						
		tvUserId.setText("User: "+Session.getSingleInstance().getUserName());
		tvCourseId.setText("Course: "+Session.getSingleInstance().getCourse_id());
		tvVers.setText("Version: "+Session.getSingleInstance().getVersion());
		
	}

	
	private void showBlockingSplash(){

		ImageView ivBlock = (ImageView) findViewById(R.id.ivTapToScan);
		ivBlock.setImageResource(R.drawable.alert_367x);
		ivBlock.setOnClickListener(null);

		TextView tvCourseId = (TextView) findViewById(R.id.tvPropCourseId);
		tvCourseId.setText("Course: "+Session.getSingleInstance().getCourse_id());
		
		TextView tvVers = (TextView) findViewById(R.id.tvPropVersion);
		tvVers.setText("Version: "+Session.getSingleInstance().getVersion());
		
	}	
	
}