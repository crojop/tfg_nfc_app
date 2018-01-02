package com.example.cristina.tfgapp.controller_view.settings;

import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.cristina.tfgapp.controller_view.login.LoginActivity;
import com.example.cristina.tfgapp.singleton.MyRequestQueueSingleton;
import com.example.cristina.tfgapp.R;
import com.example.cristina.tfgapp.singleton.MyToastSingleton;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Cristina on 23/11/17.
 */

public class AddTagActivity extends BraceletManagement {
    private Button buttonAddSettings;
    private final String URL_TAGS = "http://vpayment.perentec.com/API/V1/tags/";
    private boolean pressedButton = false;
    private TextInputLayout inputLayoutTagCode;
    private TextInputLayout inputLayoutTagName;
    private TextInputLayout inputLayoutUserName;
    private static final int SUCCS_EX_DB_QUERY = 000;
    private static final int ERR_REG_NOT_EXIST = 903;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_tag);
        buttonAddSettings = (Button) findViewById(R.id.buttonAddSettings);
        buttonAddSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pressedButton = true;
                existUser();
            }
        });
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
                if (!hasFocus && !inputLayoutUserName.getEditText().getText().toString().isEmpty()) existUser();
            }
        });
    }
    protected void existTagCode() {
        String tag_code = inputLayoutTagCode.getEditText().getText().toString().trim();
        if (tag_code != null) {
            JsonObjectRequest jsArrayRequest = new JsonObjectRequest(
                    Request.Method.GET,
                    URL_TAGS + tag_code,
                    null,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            if (!existTag(response)) {
                                inputLayoutTagCode.getEditText().setError(getString(R.string.braceletAlreadyExists));
                            } else inputLayoutTagCode.getEditText().setError(null);
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Log.d(getString(R.string.error), getString(R.string.errorJsonResponse) + error.getMessage());
                            MyToastSingleton.getInstance(AddTagActivity.this, AddTagActivity.this).
                                    setError(getString(R.string.errorAddBracelet));
                        }
                    }
            );
            MyRequestQueueSingleton.getInstance(AddTagActivity.this).addToRequestQueue(jsArrayRequest);
        }
    }

    protected boolean isNotEmpty (){
        return !inputLayoutTagCode.getEditText().getText().toString().trim().isEmpty()&&
                !inputLayoutUserName.getEditText().getText().toString().trim().isEmpty() &&
                !inputLayoutTagName.getEditText().getText().toString().trim().isEmpty();
    }

    protected boolean thereIsNotError (){
        return inputLayoutTagCode.getEditText().getError()==null&&
                inputLayoutTagName.getEditText().getError()==null
                &&inputLayoutUserName.getEditText().getError()==null;
    }

    //Función que da de alta el tag
    public void addTag (){
        JSONObject request = new JSONObject();
        try
        {
            request.put(getString(R.string.tag_code), inputLayoutTagCode.getEditText().getText().toString().trim());
            request.put(getString(R.string.tag_description), inputLayoutTagName.getEditText().getText().toString().trim());
            request.put(getString(R.string.user_description), inputLayoutUserName.getEditText().getText().toString().trim());
            request.put(getString(R.string.user_id), 2); //ManuCarrasco TODO Averiguar el id del usuario sabiendo su descripción
            request.put(getString(R.string.event_id), LoginActivity.terminalU.getEventU().getEvent_id());
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
        JsonObjectRequest postRequest = new JsonObjectRequest(Request.Method.POST, URL_TAGS, request,
                new Response.Listener<JSONObject>()
                {
                    @Override
                    public void onResponse(JSONObject response) {
                        manageResponse(response);
                        finish();
                        startActivity(getIntent());
                    }
                },
                new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        MyToastSingleton.getInstance(AddTagActivity.this, AddTagActivity.this).
                                setError(getString(R.string.errorAddBracelet));
                        Log.d(getString(R.string.error), getString(R.string.errorJsonResponse));
                    }
                }
        );
        MyRequestQueueSingleton.getInstance(this).addToRequestQueue(postRequest);
    }

    //Función que maneja la respuesta tras el alta del tag.
    private void manageResponse (JSONObject response){
        try {
            int code = response.getInt(getString(R.string.code));
            if(code==SUCCS_EX_DB_QUERY) {
                MyToastSingleton.getInstance(AddTagActivity.this, AddTagActivity.this).
                        setSuccess(getString(R.string.successAddBracelet));
            }
            else {
                MyToastSingleton.getInstance(AddTagActivity.this, AddTagActivity.this).
                        setError(getString(R.string.errorAddBracelet));
            }
        }
        catch (JSONException e) {
            Log.d(getString(R.string.error), getString(R.string.parsingError));
        }
    }

    protected void actionWhenValidPassword () {
        addTag();
    }

    public void existUser (){
        if (!inputLayoutUserName.getEditText().getText().toString().isEmpty()){
            JsonObjectRequest jsArrayRequest = new JsonObjectRequest(
                    Request.Method.GET,
                    getUrlTags(),
                    null,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            findUser(response, inputLayoutUserName);
                            validateInputFields();
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Log.d(getString(R.string.error), getString(R.string.errorJsonResponse) + error.getMessage());
                            MyToastSingleton.getInstance(AddTagActivity.this, AddTagActivity.this).
                                    setError(getString(R.string.errorAddBracelet));
                        }
                    }
            );
            MyRequestQueueSingleton.getInstance(this).addToRequestQueue(jsArrayRequest);
        } else validateInputFields();
    }

    /**
     * Función que determina si un tag existe ya o no en el sistema
     * @param jsonObject
     * @return true si ese código de tag no está registrado en el sistema, y false si ya existe
     */
    public boolean existTag (JSONObject jsonObject){
        boolean result=false;
        try {
            int code = jsonObject.getInt(getString(R.string.code));
            if (code==ERR_REG_NOT_EXIST) result = true;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return result;
    }

    protected void doSomething(String tag_code){
        inputLayoutUserName.getEditText().setError(getString(R.string.userAlreadyAssociatedToBracelet));
        if (pressedButton) clickButton();
        pressedButton = false;
    }
}

