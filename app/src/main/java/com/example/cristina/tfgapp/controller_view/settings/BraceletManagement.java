package com.example.cristina.tfgapp.controller_view.settings;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.cristina.tfgapp.R;
import com.example.cristina.tfgapp.controller_view.MainActivity;
import com.example.cristina.tfgapp.controller_view.Utils;
import com.example.cristina.tfgapp.controller_view.login.LoginActivity;
import com.example.cristina.tfgapp.controller_view.MyTerminal;
import com.example.cristina.tfgapp.singleton.MyRequestQueueSingleton;
import com.example.cristina.tfgapp.singleton.MyToastSingleton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.concurrent.Callable;

/**
 * Created by Cristina on 26/11/17.
 */

public abstract class BraceletManagement extends MyTerminal {
    //Clase abstracta de la que heredan AddTagActivity y DeleteTagActivity

    protected int user_id;
    private static final int MAX_WRONG_PASS_ATTEMPT = 3;
    protected int currentWrongPassAttempt;

    private String getValidPassword(Context context) {
        String password = "";
        SharedPreferences loginPreferences = context.getSharedPreferences(context.getString(R.string.shar_prefs_name), MODE_PRIVATE);
        String encrypted_password = loginPreferences.getString(context.getString(R.string.shar_prefs_password), "");
        password = Utils.decryptSth(encrypted_password);
        return password;
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        currentWrongPassAttempt = 0;
    }

    /*
    Función que se ejecuta cuando se pulsa el botón de cada activity
    Primero comprueba que no se haya superado el máximo número de intentos permitidos de autenticación,
    y luego se validan los campos de tipo entrada de texto, osea que no estén vacíos ni hayan detectado error.
    Si se superan estas dos validaciones, se procede a iniciar la autenticación.
     */
    protected void clickButton (){
        if (currentWrongPassAttempt>=MAX_WRONG_PASS_ATTEMPT) errorIdentification();
        else if (validateInputFields()) startIdentification(); //Que no haya errores ni esté vacío
    }

    /*
    Este método recibe como parámetro dos valores booleanos: si no es nulo ninguno de los inputtext o si lo es,
    o si está to do correcto (si no han generado error).
    Si los dos valores son true, la función devuelve true y se procede a realizar el alta o el borrado,
    pero si alguno o ninguno de los dos son true, se muestran mensajes de error al usuario en forma de toast.
    */
    protected boolean afterValidation (boolean notEmpty, boolean notError){
        boolean result = false;
        if (notEmpty){
            if (notError) result = true;
            else {
                MyToastSingleton.getInstance(this).
                        setError(getString(R.string.pleaseReviseErrors));
            }
        } else {
            if (notError) {
                MyToastSingleton.getInstance(this).
                        setError(getString(R.string.pleaseFillFields));
            }
            else {
                MyToastSingleton.getInstance(this).
                        setError(getString(R.string.pleaseFillFieldsAndReviseError));
            }
        }
        return result;
    }

    /*
    Clase personalizada que hereda de TextWatcher, manejador de eventos que responde a cambios en el texto introducido
     */
    public class TextWatcherInputs implements TextWatcher
    {
        private TextInputLayout textInputLayout;

        TextWatcherInputs (TextInputLayout textInputLayout){
            this.textInputLayout = textInputLayout;
        }
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            if (textInputLayout.getHint().toString().equals(getString(R.string.bracelet_code_form))){
                if (s.length()==0) textInputLayout.setError(getString(R.string.requiredField));
                else if (s.length()>10) textInputLayout.setError(getString(R.string.maxLength10));
                else textInputLayout.setError(null);
            }
            else {
                if (s.length()>32) textInputLayout.setError(getString(R.string.maxLength32));
                else textInputLayout.setError(null);
            }
        }

        @Override
        public void afterTextChanged(Editable s) {}
    }

    //Método abstracto en el que las clases hijas especificarán que hacer en caso de autenticación correcta
    protected abstract void actionWhenValidPassword();

    /*
    Método que inicia la identificación: muestra el alertDialog con el prompt para introducir la contraseña
     */
    public void startIdentification(){
        LayoutInflater li = LayoutInflater.from(BraceletManagement.this);
        View promptsView = li.inflate(R.layout.prompts, null);
        final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(BraceletManagement.this);
        alertDialogBuilder.setView(promptsView);
        final EditText userInput = (EditText) promptsView.findViewById(R.id.editTextPrompt);
        alertDialogBuilder
                .setCancelable(false)
                .setPositiveButton(R.string.accept,
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,int id) {
                                String user_text = (userInput.getText()).toString().trim();
                                if (user_text.equals(getValidPassword(BraceletManagement.this))) actionWhenValidPassword();
                                else errorPassword();
                            }
                        })
                .setNegativeButton(getString(R.string.cancel),
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,int id) {
                                dialog.dismiss();
                            }

                        }

                );
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    /**
     * Método que muestra una alerta al usuario cuando se ejecuta cuando se supera el máximo número de intentos de autenticación.
     */
    public void errorIdentification(){
        AlertDialog.Builder builder = new AlertDialog.Builder(BraceletManagement.this);
        builder.setTitle(getString(R.string.titleAlertIdentificationError));
        builder.setMessage(getString(R.string.messageAlertIdentificationError1)+
                "\n \n" +getString(R.string.messageAlertIdentificationError2));
        builder.setPositiveButton(R.string.accept,
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,int id) {
                        Intent logOutIntent = new Intent(BraceletManagement.this, LoginActivity.class);
                        startActivity(logOutIntent);
                        finish();
                    }
                });
        builder.create().show();
    }

    /**
     * Método que se ejecuta cuando la contraseña introducida es incorrecta.
     * Muestra un alert e incrementa una unidad al valor de la variable currentWrongPassAttempt,
     * que almacena el número de intentos erróneos de autenticación.
     */
    public void errorPassword(){
        String message = getString(R.string.titleAlertInvalidPassword);
        AlertDialog.Builder builder = new AlertDialog.Builder(BraceletManagement.this);
        builder.setTitle(getString(R.string.messageAlertInvalidPassword));
        builder.setMessage(message);
        builder.setPositiveButton(R.string.accept, null);
        builder.create().show();
        currentWrongPassAttempt++;
    }

    protected String validateResponse (JSONObject jsonObject){
        String result = getString(R.string.VAL_ERROR);
        try {
            String code = jsonObject.getString(getString(R.string.code));
            if (code.equals(getString(R.string.CODE_SUCCESSS))){
                result = getString(R.string.VAL_SUCCESS);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return result;
    }


    protected abstract void doSomething (String tag_code);


    //Función que valida los campos de entrada llamando a los métodos abstractos isNotEmpty() y thereIsNotError()
    protected boolean validateInputFields (){
        return afterValidation(isNotEmpty(), thereIsNotError());
    }

    //Método abstracto que devolverá true si ningún campo de entrada está vacío
    protected abstract boolean isNotEmpty ();

    /* Método abstracto que devolverá true si en ningún campo de entrada se ha introducido un valor no permitido, o se ha superado
    el máximo número de caracteres a introducir */
    protected abstract boolean thereIsNotError ();

    /*
    Método que, dado un TextInputLayout con la descripción del usuario procede a buscar su id y ver si hay alguna pulsera del evento actual que ya
    esté asociada a dicho usuario.
     */
    protected void goFindUser(final Context context, final TextInputLayout textInputLayout) throws UnsupportedEncodingException {
        String user_enc = URLEncoder.encode(textInputLayout.getEditText().getText().toString().trim(), "utf-8");
        JsonObjectRequest jsArrayRequest = new JsonObjectRequest(
                Request.Method.GET,
                getString(R.string.URL_USERS)+"?user_description="+user_enc+"&token="+Utils.decryptSth(getSharedPreferences(getString(R.string.shar_prefs_name), MODE_PRIVATE).getString(getString(R.string.shar_prefs_token), "")),
                null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        if (validateResponse(response).equals(getString(R.string.VAL_SUCCESS))) {
                            goManageResponseUsers(response, textInputLayout, context);
                        }
                        else {
                            MyToastSingleton.getInstance(context).setError(getString(R.string.error_searching_user));
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d(getString(R.string.error), getString(R.string.errorJsonResponse) + error.getMessage());
                        if (error.getClass().equals(AuthFailureError.class)) {
                            Utils.changeToken(context, new Callable<String>() {
                                public String call() throws UnsupportedEncodingException {
                                    goFindUser(context, textInputLayout);
                                    return null;
                                }
                            });
                        } else MyToastSingleton.getInstance(context).
                                setError(getString(R.string.errorAddBracelet));
                    }
                }
        );
        MyRequestQueueSingleton.getInstance(this).addToRequestQueue(jsArrayRequest);
    }

    //Obtiene el user_id del array data
    protected void goManageResponseUsers (JSONObject jsonObject, TextInputLayout textInputLayout, Context context){
        try {
            JSONArray jsonArray = jsonObject.getJSONArray(getString(R.string.data));
            if (jsonArray.length()==0) textInputLayout.getEditText().setError(getString(R.string.userNotFound));
            else {
                try {
                    JSONObject objeto= jsonArray.getJSONObject(0);
                    user_id = objeto.getInt(getString(R.string.user_id));
                    goManageResponseUserId(context);
                } catch (JSONException e) {
                    Log.d(getString(R.string.error), getString(R.string.parsingError)+ e.getMessage());
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    //Una vez ya obtenido el user_id que buscamos, realizamos una consulta para obtener todos los tags de este evento que tengan asociados este user_id
    protected void goManageResponseUserId (final Context context){
        JsonObjectRequest jsArrayRequest = new JsonObjectRequest(
                Request.Method.GET,
                getString(R.string.URL_TAGS)+"?user_id="+user_id+"&event_id="+ MainActivity.terminalU.getEventU().getEvent_id()+"&token="+Utils.decryptSth(getSharedPreferences(getString(R.string.shar_prefs_name), MODE_PRIVATE).getString(getString(R.string.shar_prefs_token), "")),
                null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        if (validateResponse(response).equals(getString(R.string.VAL_SUCCESS))) {
                            manageResult(response);
                        }
                        else {
                            MyToastSingleton.getInstance(context).setError(getString(R.string.error_searching_user));
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d(getString(R.string.error), getString(R.string.errorJsonResponse) + error.getMessage());
                        if (error.getClass().equals(AuthFailureError.class)) {
                            Utils.changeToken(context, new Callable<String>() {
                                public String call() {
                                    goManageResponseUserId(context);
                                    return null;
                                }
                            });
                        } else MyToastSingleton.getInstance(context).
                                setError(getString(R.string.errorAddBracelet));
                    }
                }
        );
        MyRequestQueueSingleton.getInstance(this).addToRequestQueue(jsArrayRequest);
    }

    //Cada clase decide que hacer con resultado, si mostrar el código del tag, o si decir que ese usuario tiene ya un tag asociado para este evento.
    protected abstract void manageResult (JSONObject jsonObject);
}
