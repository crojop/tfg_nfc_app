package com.example.cristina.tfgapp.controller_view.settings;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.example.cristina.tfgapp.R;
import com.example.cristina.tfgapp.controller_view.login.LoginActivity;
import com.example.cristina.tfgapp.controller_view.MyTerminal;
import com.example.cristina.tfgapp.singleton.MyToastSingleton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Cristina on 26/11/17.
 */

public abstract class BraceletManagement extends MyTerminal {
    //Clase abstracta de la que heredan AddTagActivity y DeleteTagActivity


    private static final String VALID_PASSWORD = "password"; //TODO Sistema de contraseñas
    private static final int MAX_WRONG_PASS_ATTEMPT = 3;
    protected int currentWrongPassAttempt;
    private static final String URL_TAGS = "http://vpayment.perentec.com/API/V1/tags";

    public static String getValidPassword() {
        return VALID_PASSWORD;
    }

    public static String getUrlTags() {
        return URL_TAGS;
    }

    public void setCurrentWrongPassAttempt(int currentWrongPassAttempt) {
        this.currentWrongPassAttempt = currentWrongPassAttempt;
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        currentWrongPassAttempt = 0;
        LayoutInflater inflater = getLayoutInflater();
        View layout = inflater.inflate(R.layout.toast_layout,
                (ViewGroup) findViewById(R.id.lytLayout));
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
                MyToastSingleton.getInstance(this, this).
                        setError(getString(R.string.pleaseReviseErrors));
            }
        } else {
            if (notError) {
                MyToastSingleton.getInstance(this, this).
                        setError(getString(R.string.pleaseFillFields));
            }
            else {
                MyToastSingleton.getInstance(this, this).
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
            if (s.length()==0) textInputLayout.setError(getString(R.string.requiredField));
            else if (count>32) textInputLayout.setError(getString(R.string.maxLength32));
            else textInputLayout.setError(null);
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
        final EditText userInput = (EditText) promptsView
                .findViewById(R.id.editTextPrompt);
        alertDialogBuilder
                .setCancelable(false)
                .setPositiveButton(R.string.accept,
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,int id) {
                                String user_text = (userInput.getText()).toString().trim();
                                if (user_text.equals(VALID_PASSWORD)) actionWhenValidPassword();
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
        builder.setPositiveButton(getString(R.string.accept), null);
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

    /*
    Función que recibe el array json que contiene la información de todos los tags del sistema y busca
    en él un usuario para saber si tiene ya una pulsera registrada para el evento actual o no.
    Si lo encuentra llama a la función doSomething pasándole el código del tag por parámetro
    y si no muestra una alerta en el campo de entrada indicando que el usuario no ha sido encontrado para el evento actual
     */
    protected void findUser (JSONObject jsonObject, TextInputLayout textInputLayoutUSER_NAME){
        try {
            boolean founded = false;
            boolean error = jsonObject.getBoolean(getString(R.string.error));
            String message = jsonObject.getString(getString(R.string.message));
            if (!error && message.equals(getString(R.string.success))){
                JSONArray jsonArray = jsonObject.getJSONArray(getString(R.string.data));
                int i=0;
                while (i<jsonArray.length()&&!founded){
                    try {
                        JSONObject objeto= jsonArray.getJSONObject(i);
                        String event_id = objeto.isNull(getString(R.string.event_id)) ? null : String.valueOf(objeto.getInt(getString(R.string.event_id)));
                        if (event_id!=null){
                            if (isUserFound(objeto.getString(getString(R.string.user_description)), objeto.getInt(getString(R.string.event_id)), textInputLayoutUSER_NAME)) {
                                founded = true;
                                doSomething(String.valueOf(objeto.getInt(getString(R.string.tag_code))));
                            }
                        }
                        else {
                           Log.d(getString(R.string.error),getString(R.string.nullEvent));
                        }
                    } catch (JSONException e) {
                        Log.e(getString(R.string.tag), getString(R.string.parsingError)+ e.getMessage());
                    }
                    i++;
                }
                if (!founded) textInputLayoutUSER_NAME.getEditText().setError(getString(R.string.userEventNotFound));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    protected abstract void doSomething (String tag_code);

    /*Función que devuelve true si el parámetro user_description coincide con el valor del campo de entrada textInputLayout,
    y el id del evento pasado como parámetro coincide con el del terminal actual (con el que se ha iniciado sesión en este dispositivo)*/
    private boolean isUserFound (String user_description, int event_id, TextInputLayout textInputLayout){
        boolean result=false;
        boolean equals_user_description = user_description.equals(textInputLayout.getEditText().getText().toString().trim());
        boolean equals_event_id = (event_id== LoginActivity.terminalU.getEventU().getEvent_id());
        if (equals_event_id&&equals_user_description) result= true;
        return result;
    }

    //Función que valida los campos de entrada llamando a los métodos abstractos isNotEmpty() y thereIsNotError()
    protected boolean validateInputFields (){
        return afterValidation(isNotEmpty(), thereIsNotError());
    }

    //Método abstracto que devolverá true si ningún campo de entrada está vacío
    protected abstract boolean isNotEmpty ();

    /* Método abstracto que devolverá true si en ningún campo de entrada se ha introducido un valor no permitido, o se ha superado
    el máximo número de caracteres a introducir */
    protected abstract boolean thereIsNotError ();
}
