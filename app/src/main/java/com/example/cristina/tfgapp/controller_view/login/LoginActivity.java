package com.example.cristina.tfgapp.controller_view.login;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.app.LoaderManager.LoaderCallbacks;

import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;

import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.cristina.tfgapp.R;
import com.example.cristina.tfgapp.controller_view.MainActivity;
import com.example.cristina.tfgapp.controller_view.Utils;
import com.example.cristina.tfgapp.singleton.MyRequestQueueSingleton;
import com.example.cristina.tfgapp.singleton.MyToastSingleton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends AppCompatActivity implements LoaderCallbacks<Cursor> {

    private CheckBox saveLoginCheckBox;
    private SharedPreferences loginPreferences;
    private SharedPreferences.Editor loginPrefsEditor;
    private boolean saveLogin;

    // UI references.
    private AutoCompleteTextView mTsnView;
    private EditText mPasswordView;
    private View mProgressView;
    private View mLoginFormView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        saveLoginCheckBox = (CheckBox) findViewById(R.id.saveLoginCheckBox);
        loginPreferences = getSharedPreferences(getString(R.string.shar_prefs_name), MODE_PRIVATE);
        loginPrefsEditor = loginPreferences.edit();
        saveLogin = loginPreferences.getBoolean(getString(R.string.shar_prefs_saveLogin), false);
        mTsnView = (AutoCompleteTextView) findViewById(R.id.term_ser_num);

        mPasswordView = (EditText) findViewById(R.id.password);
        mPasswordView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == R.id.login || id == EditorInfo.IME_NULL) {
                    attemptLogin();
                    return true;
                }
                return false;
            }
        });

        if (saveLogin == true) {
            mTsnView.setText(String.valueOf(loginPreferences.getInt(getString(R.string.shar_prefs_terminal_serial_number), 0)));
            String encrypted_password = loginPreferences.getString(getString(R.string.shar_prefs_password), "");
            mPasswordView.setText(Utils.decryptSth(encrypted_password));
            saveLoginCheckBox.setChecked(true);
        }
        Button mTsnSignInButton = (Button) findViewById(R.id.email_sign_in_button);
        mTsnSignInButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptLogin();
            }
        });

        mLoginFormView = findViewById(R.id.login_form);
        mProgressView = findViewById(R.id.login_progress);
    }

    /**
     * Callback received when a permissions request has been completed.
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {

    }


    /**
     * Attempts to sign in or register the account specified by the login form.
     * If there are form errors (invalid email, missing fields, etc.), the
     * errors are presented and no actual login attempt is made.
     */
    private void attemptLogin() {
        mTsnView.setError(null);
        mPasswordView.setError(null);
        View focusView = null;

        if (TextUtils.isEmpty(mPasswordView.getText().toString())) {
            mPasswordView.setError(getString(R.string.error_field_required));
            focusView = mPasswordView;
        }

        if (TextUtils.isEmpty(mTsnView.getText().toString())) {
            mTsnView.setError(getString(R.string.error_field_required));
            focusView = mTsnView;
        }


        if (TextUtils.isEmpty(mTsnView.getText().toString()) || TextUtils.isEmpty(mPasswordView.getText().toString())) {
            focusView.requestFocus();
        } else {
            // Show a progress spinner, and kick off a background task to perform the user login attempt.
            showProgress(true);
            login();
        }
    }

    private void login ()
    {
        if (Utils.isConnected(this)) {
            final JSONObject request = new JSONObject();
            try
            {
                request.put(getString(R.string.ws_user), mTsnView.getText().toString());
                request.put(getString(R.string.ws_password), mPasswordView.getText().toString());
            }
            catch(Exception e)
            {
                e.printStackTrace();
                showProgress(false);
            }
            JsonObjectRequest postRequest = new JsonObjectRequest(Request.Method.POST, getString(R.string.URL_AUTHENTICATE), request,
                    new Response.Listener<JSONObject>()
                    {
                        @Override
                        public void onResponse(JSONObject response) {
                           if (validateResponse(response).equals(getString(R.string.VAL_SUCCESS))){
                               try {
                                   successfulLogin(response.getString(getString(R.string.ws_token)));
                               } catch (Exception e) {
                                   e.printStackTrace();
                               }
                           } else if (validateResponse(response).equals(getString(R.string.VAL_ERROR))){
                                MyToastSingleton.getInstance(LoginActivity.this).setError(getString(R.string.error_login));
                           }
                        }
                    },
                    new Response.ErrorListener()
                    {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Log.d(getString(R.string.error), getString(R.string.errorJsonResponse));
                            if (error.getClass().equals(AuthFailureError.class)) {
                                Utils.changeToken(LoginActivity.this, new Callable<String>() {
                                    public String call() {
                                        login();
                                        return null;
                                    }
                                });
                            } else showProgress(false);
                        }
                    }
            );
            MyRequestQueueSingleton.getInstance(this).addToRequestQueue(postRequest);
        }
        else {
            MyToastSingleton.getInstance(LoginActivity.this).setError(LoginActivity.this.getString(R.string.no_internet_connexion));
            showProgress(false);
        }
    }

    private String validateResponse (JSONObject response){
        String result = getString(R.string.VAL_ERROR);
        try {
            boolean error = response.getBoolean(getString(R.string.error));
            String code = response.getString(getString(R.string.code));
            if (!error) {
                result = getString(R.string.VAL_SUCCESS);
            } else if (code.equals(getString(R.string.CODE_WRONG_USER))) {
                mTsnView.setError(getString(R.string.error_invalid_tsn));
                mTsnView.requestFocus();
                result = getString(R.string.VAL_IGNORE);
            } else if (code.equals(getString(R.string.CODE_WRONG_PASSWORD))) {
                mPasswordView.setError(getString(R.string.error_incorrect_password));
                mPasswordView.requestFocus();
                result = getString(R.string.VAL_IGNORE);
            }
            showProgress(false);
        } catch (Exception e) {
            e.printStackTrace();
            showProgress(false);
        }
        return result;
    }

    private void successfulLogin (String token) throws Exception {
        byte[] encrypted_token = Utils.encryptToByte(token, Utils.generateKey());
        String base64 = Base64.encodeToString(encrypted_token, Base64.DEFAULT);
        loginPrefsEditor.putString(getString(R.string.shar_prefs_token), base64);
        loginPrefsEditor.putInt(getString(R.string.shar_prefs_terminal_serial_number), Integer.parseInt(mTsnView.getText().toString()));
        try {
            byte[] encrypted_password = Utils.encryptToByte(mPasswordView.getText().toString(), Utils.generateKey());
            String strbase64 = Base64.encodeToString(encrypted_password, Base64.DEFAULT);
            loginPrefsEditor.putString(getString(R.string.shar_prefs_password), strbase64);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (saveLoginCheckBox.isChecked()) {
            loginPrefsEditor.putBoolean(getString(R.string.shar_prefs_saveLogin), true);
        } else {
            //loginPrefsEditor.clear();
            loginPrefsEditor.putBoolean(getString(R.string.shar_prefs_saveLogin), false);
        }
        getTerminal(token);
    }

    private void getTerminal (final String token){
        JsonObjectRequest jsArrayRequest = new JsonObjectRequest(
            //Con el método get pide información del código del tag que se ha leído
            Request.Method.GET,
            getString(R.string.URL_TERMINALS)+"?"+getString(R.string.terminal_serial_number)+"="+mTsnView.getText().toString()+"&token="+token,
            null,
            new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    if (validateResponse(response).equals(getString(R.string.VAL_ERROR))){
                        MyToastSingleton.getInstance(LoginActivity.this).setError(getString(R.string.error_login));
                    } else if (validateResponse(response).equals(getString(R.string.VAL_SUCCESS))){
                        manageResponseTerminal(response, token);
                    }
                }
            },
            new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    if (error.getClass().equals(AuthFailureError.class)) {
                        Utils.changeToken(LoginActivity.this, new Callable<String>() {
                            public String call() throws AuthFailureError {
                                getTerminal(token);
                                return null;
                            }
                        });
                    }
                }
            }
        );
        MyRequestQueueSingleton.getInstance(this).addToRequestQueue(jsArrayRequest);
    }

    private void manageResponseTerminal (JSONObject jsonObject, String token){
        try {
            JSONArray jsonArray = jsonObject.getJSONArray(getString(R.string.data));
            for(int i=0; i<jsonArray.length(); i++){
                try {
                    JSONObject objeto= jsonArray.getJSONObject(i);
                    if (String.valueOf(objeto.getString(getString(R.string.terminal_description)))!="null") {
                        loginPrefsEditor.putString(getString(R.string.shar_prefs_terminal_description), objeto.getString(getString(R.string.terminal_description)));
                    }
                    else loginPrefsEditor.putString(getString(R.string.shar_prefs_terminal_description), "");
                    if (String.valueOf(objeto.get(getString(R.string.event_id)))!="null") {
                        loginPrefsEditor.putInt(getString(R.string.shar_prefs_event_id), objeto.getInt(getString(R.string.event_id)));
                        getEventDesc(objeto.getInt(getString(R.string.event_id)), token);
                    }
                    else {
                        loginPrefsEditor.putInt(getString(R.string.shar_prefs_event_id), 0);
                        mTsnView.setError(getString(R.string.no_event_associated));
                    }
                } catch (JSONException e) {
                    Log.d(getString(R.string.error), getString(R.string.parsingError)+ e.getMessage());
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void getEventDesc (final int event_id, final String token){
        JsonObjectRequest jsArrayRequest = new JsonObjectRequest(
            //Con el método get pide información del código del tag que se ha leído
            Request.Method.GET,
            getString(R.string.URL_EVENTS)+event_id+"?token="+token,
            null,
            new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    if (validateResponse(response).equals(getString(R.string.VAL_ERROR))){
                        MyToastSingleton.getInstance(LoginActivity.this).setError(getString(R.string.error_login));
                    } else if (validateResponse(response).equals(getString(R.string.VAL_SUCCESS))){
                        manageResponse(response);
                    }
                }
            },
            new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    if (error.getClass().equals(AuthFailureError.class)) {
                        Utils.changeToken(LoginActivity.this, new Callable<String>() {
                            public String call() throws AuthFailureError {
                                getEventDesc(event_id, token);
                                return null;
                            }
                        });
                    }
                }
            }
        );
        MyRequestQueueSingleton.getInstance(this).addToRequestQueue(jsArrayRequest);
    }

    private void manageResponse (JSONObject jsonObject){
        try {
            JSONArray jsonArray = jsonObject.getJSONArray(getString(R.string.data));
            for(int i=0; i<jsonArray.length(); i++){
                try {
                    JSONObject objeto= jsonArray.getJSONObject(i);
                    loginPrefsEditor.putString(getString(R.string.shar_prefs_event_description), objeto.getString(getString(R.string.event_description)));
                    loginPrefsEditor.commit();
                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                    startActivity(intent);

                } catch (JSONException e) {
                    Log.d(getString(R.string.error), getString(R.string.parsingError)+ e.getMessage());
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * Shows the progress UI and hides the login form.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgress(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
            mLoginFormView.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
                }
            });

            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mProgressView.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
        } else {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        return new CursorLoader(this,
                // Retrieve data rows for the device user's 'profile' contact.
                Uri.withAppendedPath(ContactsContract.Profile.CONTENT_URI,
                        ContactsContract.Contacts.Data.CONTENT_DIRECTORY), ProfileQuery.PROJECTION,
                ContactsContract.Contacts.Data.MIMETYPE +
                        " = ?", new String[]{ContactsContract.CommonDataKinds.Email
                .CONTENT_ITEM_TYPE},
                ContactsContract.Contacts.Data.IS_PRIMARY + " DESC");
    }

    @Override
    public void onLoadFinished(Loader<Cursor> cursorLoader, Cursor cursor) {
        List<String> tsns = new ArrayList<>();
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            tsns.add(cursor.getString(ProfileQuery.ADDRESS));
            cursor.moveToNext();
        }

        addTsnsToAutoComplete(tsns);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> cursorLoader) {

    }

    private void addTsnsToAutoComplete(List<String> tsnAddressCollection) {
        //Create adapter to tell the AutoCompleteTextView what to show in its dropdown list.
        ArrayAdapter<String> adapter =
                new ArrayAdapter<>(LoginActivity.this,
                        android.R.layout.simple_dropdown_item_1line, tsnAddressCollection);

        mTsnView.setAdapter(adapter);
    }


    private interface ProfileQuery {
        String[] PROJECTION = {
                ContactsContract.CommonDataKinds.Email.ADDRESS,
                ContactsContract.CommonDataKinds.Email.IS_PRIMARY,
        };

        int ADDRESS = 0;
    }
}