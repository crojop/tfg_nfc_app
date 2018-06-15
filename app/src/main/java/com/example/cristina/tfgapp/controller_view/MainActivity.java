package com.example.cristina.tfgapp.controller_view;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.IntentFilter.MalformedMimeTypeException;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.database.sqlite.SQLiteDatabase;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.nfc.tech.Ndef;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.cristina.tfgapp.controller_view.login.LoginActivity;
import com.example.cristina.tfgapp.controller_view.logs.LogsActivity;
import com.example.cristina.tfgapp.controller_view.menus.MenuActivity;
import com.example.cristina.tfgapp.controller_view.menus.RecyclerActivity;
import com.example.cristina.tfgapp.controller_view.settings.BracManActivity;
import com.example.cristina.tfgapp.model.EventU;
import com.example.cristina.tfgapp.model.TerminalU;
import com.example.cristina.tfgapp.singleton.MyRequestQueueSingleton;
import com.example.cristina.tfgapp.model.database.ProductDataBaseHelper;
import com.example.cristina.tfgapp.R;
import com.example.cristina.tfgapp.model.TagU;
import com.example.cristina.tfgapp.singleton.MyToastSingleton;

import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.Arrays;
import java.util.concurrent.Callable;



public class MainActivity extends MyTerminal {

    public static TerminalU terminalU;
    private static final String MIME_TEXT_PLAIN = "text/plain";
    private static final String TAG = "NfcDemo";

    //currentTag almacena la información del tag nfc que está siendo leído
    public static TagU currentTag;

    //Una sola bbdd de tipo static para ser usada desde cualquier parte de la aplicación
    public static SQLiteDatabase db;

    //Tabla de productos
    public static ProductDataBaseHelper prodDataBaseHelper;

    private NfcAdapter mNfcAdapter;

    private String result;

    //Burguer icon
    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mDrawerToggle;
    private ListView mDrawerList;

    private ElementListDrawer[] arrayElementListDrawer;
    private class DrawerItemClickListener implements ListView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView parent, View view, int position, long id) {
            selectItem(position);
        }
    }

    private void selectItem(int position) {
        mDrawerList.setItemChecked(position, true);

        switch (position) {
            case 0:
                //Estadísticas
                Intent graphIntent = new Intent(this, RecyclerActivity.class);
                startActivity(graphIntent);
                break;
            case 1:
                //Logs o registros
                Intent logsIntent = new Intent(this, LogsActivity.class);
                startActivity(logsIntent);
                break;
            case 2:
                //Gestión de pulseras
                Intent bracManagementIntent = new Intent(this, BracManActivity.class);
                startActivity(bracManagementIntent);
                break;
            case 3:
                //Salir
                Intent logOutIntent = new Intent(this, LoginActivity.class);
                startActivity(logOutIntent);
                finish();
                break;
            default:
                break;
        }
    }

    class ElementListDrawer {
        private String title;
        private int image;
        public ElementListDrawer (String t, int i){
            this.title=t;
            this.image=i;
        }
        public String getTitle(){ return title; }
        public int getImage(){ return image; }
    }

    class ElementListDrawerAdapter extends ArrayAdapter {
        private Activity context;

        ElementListDrawerAdapter (Activity context){
            super(context, R.layout.list_drawer, arrayElementListDrawer);
            this.context = context;
        }

        public View getView (int position, View convertView, ViewGroup parent){
            LayoutInflater inflater = context.getLayoutInflater();

            View item = inflater.inflate(R.layout.list_drawer, null);

            TextView textListDrawer = (TextView) item.findViewById(R.id.titleListDrawer);
            textListDrawer.setText(arrayElementListDrawer[position].getTitle());

            ImageView imageListDrawer = (ImageView) item.findViewById(R.id.imageListDrawer);
            imageListDrawer.setImageResource(arrayElementListDrawer[position].getImage());

            return (item);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        currentTag = new TagU();
        SharedPreferences loginPreferences = getSharedPreferences(getString(R.string.shar_prefs_name), MODE_PRIVATE);
        String encrypted_password = loginPreferences.getString(getString(R.string.shar_prefs_password), "");
        if (encrypted_password==null) {
            Intent showLogin = new Intent(this, LoginActivity.class);
            startActivity(showLogin);
        }
        terminalU = new TerminalU(loginPreferences.getInt(getString(R.string.terminal_serial_number), 0));
        EventU eventU = new EventU(loginPreferences.getInt(getString(R.string.shar_prefs_event_id), 0), loginPreferences.getString(getString(R.string.shar_prefs_event_description), ""));
        terminalU.setTerminal_description(loginPreferences.getString(getString(R.string.shar_prefs_terminal_description), ""));
        terminalU.setEventU(eventU);

        //Código de prueba para limpiar la base de datos
/*
        String [] databases = this.databaseList();
        if(databases != null) {
            for (String database : databases) {
                this.deleteDatabase(database);
            }
        }*/
        prodDataBaseHelper = new ProductDataBaseHelper(this, "DBProducts", null, 1);

        arrayElementListDrawer = new ElementListDrawer[]{
                new ElementListDrawer(getString(R.string.statistics), getResources().getIdentifier(getString(R.string.id_drawable_listdrawer_statistics), getString(R.string.drawable), getPackageName())),
                new ElementListDrawer(getString(R.string.logs), getResources().getIdentifier(getString(R.string.id_drawable_listdrawer_logs), getString(R.string.drawable), getPackageName())),
                new ElementListDrawer(getString(R.string.bracelet_management), getResources().getIdentifier(getString(R.string.id_drawable_listdrawer_nfcbracelet), getString(R.string.drawable), getPackageName())),
                new ElementListDrawer(getString(R.string.exit), getResources().getIdentifier(getString(R.string.id_drawable_listdrawer_logout), getString(R.string.drawable), getPackageName())),
        };
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerList = (ListView) findViewById(R.id.left_drawer);
        mDrawerList.setAdapter(new ElementListDrawerAdapter(this));
        mDrawerList.setOnItemClickListener(new DrawerItemClickListener());
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerToggle = new ActionBarDrawerToggle(
                this,                  /* host Activity */
                mDrawerLayout,         /* DrawerLayout object */
                R.string.openDrawerIcon,  /* "open drawer" description */
                R.string.closeDrawerIcon/* "close drawer" description */
        )

        {
            /** Called when a drawer has settled in a completely closed state. */
            public void onDrawerClosed(View view) {
                super.onDrawerClosed(view);
            }

            /** Called when a drawer has settled in a completely open state. */
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
            }
        };

        // Set the drawer toggle as the DrawerListener
        mDrawerLayout.setDrawerListener(mDrawerToggle);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        mNfcAdapter = NfcAdapter.getDefaultAdapter(this);
        if (mNfcAdapter == null) {
            //Si no tiene lector nfc
            MyToastSingleton.getInstance(this).setError(getString(R.string.no_nfc_support_in_device));
            finish();
            return;
        }
        if (!mNfcAdapter.isEnabled()) {
            //Si lo tiene desactivado, muestra mensaje para que el usuario lo active
            MyToastSingleton.getInstance(this).setError(getString(R.string.nfc_is_disabled));
        } else {
            //mTextView.setText(R.string.explanation);
        }

        handleIntent(getIntent());
    }

    @Override
    protected void onResume() {
        super.onResume();

        /**
         * It's important, that the activity is in the foreground (resumed). Otherwise
         * an IllegalStateException is thrown.
         */
        setupForegroundDispatch(this, mNfcAdapter);
    }

    @Override
    protected void onPause() {
        /**
         * Call this before onPause, otherwise an IllegalArgumentException is thrown as well.
         */
        stopForegroundDispatch(this, mNfcAdapter);

        super.onPause();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        /**
         * This method gets called, when a new Intent gets associated with the current activity instance.
         * Instead of creating a new activity, onNewIntent will be called. For more information have a look
         * at the documentation.
         *
         * In our case this method gets called, when the user attaches a Tag to the device.
         */
        handleIntent(intent);
    }

    public static void setupForegroundDispatch(final Activity activity, NfcAdapter adapter) {
        final Intent intent = new Intent(activity.getApplicationContext(), activity.getClass());
        intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        final PendingIntent pendingIntent = PendingIntent.getActivity(activity.getApplicationContext(),
                0, intent, 0);

        IntentFilter[] filters = new IntentFilter[1];
        String[][] techList = new String[][]{};

        // Notice that this is the same filter as in our manifest.
        filters[0] = new IntentFilter();
        filters[0].addAction(NfcAdapter.ACTION_NDEF_DISCOVERED);
        filters[0].addCategory(Intent.CATEGORY_DEFAULT);
        try {
            filters[0].addDataType(MIME_TEXT_PLAIN);
        } catch (MalformedMimeTypeException e) {
            throw new RuntimeException(activity.getApplicationContext().getString(R.string.checkYourMimeType));
        }

        adapter.enableForegroundDispatch(activity, pendingIntent, filters, techList);
    }

    public static void stopForegroundDispatch(final Activity activity, NfcAdapter adapter) {
        adapter.disableForegroundDispatch(activity);
    }

    private void handleIntent(Intent intent) {
        String action = intent.getAction();
        if (NfcAdapter.ACTION_NDEF_DISCOVERED.equals(action)) {

            String type = intent.getType();
            if (MIME_TEXT_PLAIN.equals(type)) {

                Tag tag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);

                //El siguiente código es para pruebas, stringBuilder contiene la id del tag
                StringBuilder stringBuilder = new StringBuilder("0x");

                char[] buffer = new char[2];
                for (int i = 0; i < tag.getId().length; i++) {
                    buffer[0] = Character.forDigit((tag.getId()[i] >>> 4) & 0x0F, 16);
                    buffer[1] = Character.forDigit(tag.getId()[i] & 0x0F, 16);
                    stringBuilder.append(buffer);
                }
                new NdefReaderTask().execute(tag);

            } else {
                Log.d(TAG, getString(R.string.wrongMimeType) + type);
            }
        } else if (NfcAdapter.ACTION_TECH_DISCOVERED.equals(action)) {

            // In case we would still use the Tech Discovered Intent
            Tag tag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
            String[] techList = tag.getTechList();
            String searchedTech = Ndef.class.getName();

            for (String tech : techList) {
                if (searchedTech.equals(tech)) {
                    new NdefReaderTask().execute(tag);
                    break;
                }
            }
        }
    }
    public void validateTag () {
        final JsonObjectRequest jsArrayRequest = new JsonObjectRequest(
                //Con el método get pide información del código del tag que se ha leído
                Request.Method.GET,
                getString(R.string.URL_TAGS)+result+Utils.getToken(this),
                null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                    if (TagU.validateTag(response, MainActivity.this).equals(getString(R.string.VAL_ERROR))){
                        MyToastSingleton.getInstance(MainActivity.this).setError(getString(R.string.not_found_user));
                    } else if (TagU.validateTag(response, MainActivity.this).equals(getString(R.string.VAL_SUCCESS))){
                        /*Si es válido accede a MenuActivity, donde se muestran las distintas acciones
                        que puede llevar a cabo el usuario */
                        TagU.setTagU(response, MainActivity.this);
                        Intent intentMenu = new Intent(getBaseContext(), MenuActivity.class);
                        startActivity(intentMenu);
                    }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        if (error.getClass().equals(AuthFailureError.class)) {
                            Utils.changeToken(MainActivity.this, new Callable<String>() {
                                public String call() throws AuthFailureError {
                                    validateTag();
                                    return null;
                                }
                            });
                        }
                        Log.d(TAG, getString(R.string.errorJsonResponse) + error.getLocalizedMessage());
                    }
                }
        );
        MyRequestQueueSingleton.getInstance(MainActivity.this).addToRequestQueue(jsArrayRequest);
    }


    //Lee la pulsera nfc
    private class NdefReaderTask extends AsyncTask<Tag, Void, String> {

        @Override
        protected String doInBackground(Tag... params) {
            Tag tag = params[0];

            Ndef ndef = Ndef.get(tag);
            if (ndef == null) {
                // NDEF is not supported by this Tag.
                return null;
            }

            NdefMessage ndefMessage = ndef.getCachedNdefMessage();

            NdefRecord[] records = ndefMessage.getRecords();
            for (NdefRecord ndefRecord : records) {
                if (ndefRecord.getTnf() == NdefRecord.TNF_WELL_KNOWN && Arrays.equals(ndefRecord.getType(), NdefRecord.RTD_TEXT)) {
                    try {
                        return readText(ndefRecord);
                    } catch (UnsupportedEncodingException e) {
                        Log.e(TAG, getString(R.string.unsupportedEncoding), e);
                    }
                }
            }

            return null;
        }

        private String readText(NdefRecord record) throws UnsupportedEncodingException {
        /*
         * See NFC forum specification for "Text Record Type Definition" at 3.2.1
         *
         * http://www.nfc-forum.org/specs/
         *
         * bit_7 defines encoding
         * bit_6 reserved for future use, must be 0
         * bit_5..0 length of IANA language code
         */

            byte[] payload = record.getPayload();

            // Get the Text Encoding
            String textEncoding = ((payload[0] & 128) == 0) ? "UTF-8" : "UTF-16";

            // Get the Language Code
            int languageCodeLength = payload[0] & 0063;

            // String languageCode = new String(payload, 1, languageCodeLength, "US-ASCII");
            // e.g. "en"

            // Get the Text
            return new String(payload, languageCodeLength + 1, payload.length - languageCodeLength - 1, textEncoding);
        }

        @Override
        protected void onPostExecute(final String result) {
            //Recibe el texto que está almacenado de la pulsera nfc
            if (result != null) {
                MainActivity.this.result = result;
                validateTag();
            }
        }

    }

    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Pass the event to ActionBarDrawerToggle, if it returns
        // true, then it has handled the app icon touch event
        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        // Handle your other action bar items...

        return super.onOptionsItemSelected(item);
    }

}
