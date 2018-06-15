package com.example.cristina.tfgapp.controller_view.settings;

import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.cristina.tfgapp.controller_view.MainActivity;
import com.example.cristina.tfgapp.controller_view.Utils;
import com.example.cristina.tfgapp.singleton.MyRequestQueueSingleton;
import com.example.cristina.tfgapp.R;
import com.example.cristina.tfgapp.singleton.MyToastSingleton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.concurrent.Callable;

/**
 * Created by Cristina on 23/11/17.
 */

public class AddTagActivity extends BraceletManagement {
    private Button buttonAddSettings;
    private boolean pressedButton = false;
    private TextInputLayout inputLayoutTagCode;
    private TextInputLayout inputLayoutTagName;
    private TextInputLayout inputLayoutUserName;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_tag);

        inputLayoutTagCode = (TextInputLayout) findViewById(R.id.inputLayoutTagCode);
        inputLayoutTagName = (TextInputLayout) findViewById(R.id.inputLayoutTagName);
        inputLayoutUserName = (TextInputLayout) findViewById(R.id.inputLayoutUserName);

        inputLayoutTagCode.getEditText().addTextChangedListener(new TextWatcherInputs(inputLayoutTagCode));
        inputLayoutTagCode.getEditText().setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus && !inputLayoutTagCode.getEditText().getText().toString().isEmpty()) existTagCode();
            }
        });
        inputLayoutTagName.getEditText().addTextChangedListener(new TextWatcherInputs(inputLayoutTagName));
        inputLayoutUserName.getEditText().addTextChangedListener(new TextWatcherInputs(inputLayoutUserName));
        inputLayoutUserName.getEditText().setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                //if (!hasFocus && !inputLayoutUserName.getEditText().getText().toString().isEmpty()) existUser();
            }
        });

        buttonAddSettings = (Button) findViewById(R.id.buttonAddSettings);
        buttonAddSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pressedButton = true;
                if (!inputLayoutUserName.getEditText().getText().toString().isEmpty()) try {
                    if (validateInputFields()) goFindUser(AddTagActivity.this, inputLayoutUserName);
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                else if (!inputLayoutTagCode.getEditText().getText().toString().isEmpty()) doSomething(inputLayoutTagCode.getEditText().getText().toString().trim());
                else validateInputFields();
            }
        });
    }

    protected void manageResult (JSONObject response){
        try {
            JSONArray jsonArray = response.getJSONArray(getString(R.string.data));
            if (jsonArray.length()>0) this.inputLayoutUserName.getEditText().setError(getString(R.string.userAlreadyAssociatedToBracelet));
            else doSomething(this.inputLayoutTagCode.getEditText().getText().toString().trim());
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    protected void existTagCode() {
        String tag_code = inputLayoutTagCode.getEditText().getText().toString().trim();
        if (tag_code != null) {
            JsonObjectRequest jsArrayRequest = new JsonObjectRequest(
                    Request.Method.GET,
                    getString(R.string.URL_TAGS) + tag_code + Utils.getToken(this),
                    null,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            if (existTag(response).equals(getString(R.string.VAL_SUCCESS))){
                                inputLayoutTagCode.getEditText().setError(null);
                            } else if (existTag(response).equals(getString(R.string.VAL_ERROR))){
                                inputLayoutTagCode.getEditText().setError(getString(R.string.braceletAlreadyExists));
                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Log.d(getString(R.string.error), getString(R.string.errorJsonResponse) + error.getMessage());
                            if (error.getClass().equals(AuthFailureError.class)) {
                                Utils.changeToken(AddTagActivity.this, new Callable<String>() {
                                    public String call() {
                                        existTagCode();
                                        return null;
                                    }
                                });
                            } else MyToastSingleton.getInstance(AddTagActivity.this).
                                    setError(getString(R.string.errorAddBracelet));
                        }
                    }
            );
            MyRequestQueueSingleton.getInstance(AddTagActivity.this).addToRequestQueue(jsArrayRequest);
        }
    }

    protected boolean isNotEmpty (){
        return !inputLayoutTagCode.getEditText().getText().toString().trim().isEmpty();
    }

    protected boolean thereIsNotError (){
        return inputLayoutTagCode.getError()==null&&
                inputLayoutTagName.getError()==null
                &&inputLayoutUserName.getError()==null;
    }

    //Función que da de alta el tag
    public void addTag (){
        final JSONObject request = new JSONObject();
        try
        {
            request.put(getString(R.string.tag_code), inputLayoutTagCode.getEditText().getText().toString().trim());
            if (!inputLayoutTagName.getEditText().getText().toString().trim().isEmpty()) request.put(getString(R.string.tag_description), inputLayoutTagName.getEditText().getText().toString().trim());
            if (String.valueOf(user_id)!=null) request.put(getString(R.string.user_id), user_id);
            request.put(getString(R.string.event_id), MainActivity.terminalU.getEventU().getEvent_id());
            request.put(getString(R.string.token), Utils.decryptSth(getSharedPreferences(getString(R.string.shar_prefs_name), MODE_PRIVATE).getString(getString(R.string.shar_prefs_token), "")));
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
        JsonObjectRequest postRequest = new JsonObjectRequest(Request.Method.POST, getString(R.string.URL_TAGS), request,
                new Response.Listener<JSONObject>()
                {
                    @Override
                    public void onResponse(JSONObject response) {
                        if (manageResponse(response).equals(getString(R.string.VAL_SUCCESS))){
                            MyToastSingleton.getInstance(AddTagActivity.this).setSuccess(getString(R.string.successAddBracelet));
                            finish();
                            startActivity(getIntent());
                        } else if (manageResponse(response).equals(getString(R.string.VAL_ERROR))){
                            MyToastSingleton.getInstance(AddTagActivity.this).setError(getString(R.string.errorAddBracelet));
                        }
                    }
                },
                new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d(getString(R.string.error), getString(R.string.errorJsonResponse));
                        if (error.getClass().equals(AuthFailureError.class)) {
                            Utils.changeToken(AddTagActivity.this, new Callable<String>() {
                                public String call() {
                                    addTag();
                                    return null;
                                }
                            });
                        }
                        else MyToastSingleton.getInstance(AddTagActivity.this).
                            setError(getString(R.string.errorAddBracelet));
                    }
                }
        );
        MyRequestQueueSingleton.getInstance(this).addToRequestQueue(postRequest);
    }

    //Función que maneja la respuesta tras el alta del tag.
    private String manageResponse (JSONObject response){
        String result = getString(R.string.VAL_ERROR);
        try {
            String code = response.getString(getString(R.string.code));
            if(code.equals(getString(R.string.CODE_SUCCESSS))) result = getString(R.string.VAL_SUCCESS);
        }
        catch (JSONException e) {
            Log.d(getString(R.string.error), getString(R.string.parsingError));
        }
        return result;
    }

    protected void actionWhenValidPassword () {
        addTag();
    }

    /**
     * Función que determina si un tag existe ya o no en el sistema
     * @param jsonObject
     * @return true si ese código de tag no está registrado en el sistema, y false si ya existe
     */
    public String existTag (JSONObject jsonObject){
        String result=getString(R.string.VAL_ERROR);
        try {
            String code = jsonObject.getString(getString(R.string.code));
            if (code.equals(getString(R.string.CODE_REGISTER_NO_EXISTS))) result = getString(R.string.VAL_SUCCESS);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return result;
    }

    protected void doSomething(String tag_code){
        //inputLayoutUserName.getEditText().setError(getString(R.string.userAlreadyAssociatedToBracelet));
        if (pressedButton) clickButton();
        pressedButton = false;
    }
}

