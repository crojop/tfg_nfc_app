package com.example.cristina.tfgapp.controller_view.settings;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TextInputLayout;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.RelativeLayout;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.example.cristina.tfgapp.singleton.MyRequestQueueSingleton;
import com.example.cristina.tfgapp.R;
import com.example.cristina.tfgapp.singleton.MyToastSingleton;

import org.json.JSONObject;

import ru.dimorinny.floatingtextbutton.FloatingTextButton;

/**
 * Created by Cristina on 23/11/17.
 */

public class DeleteTagActivity extends BraceletManagement {
    private TextInputLayout inputLayoutTag, inputLayoutSearchForUserName;
    private FloatingTextButton showUserSearchButton;
    private FloatingActionButton searchUserButton;
    private RelativeLayout layoutSearchForUserName;
    private Animation fab_open,fab_close;
    private Button buttonDeleteSettings;
    private final String URL_TAGS = "http://vpayment.perentec.com/API/V1/tags/";
    private boolean isFabOpen = true;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delete_tag);
        buttonDeleteSettings = (Button) findViewById(R.id.buttonDeleteSettings);
        buttonDeleteSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickButton();
            }
        });
        currentWrongPassAttempt = 0;

        inputLayoutTag = (TextInputLayout) findViewById(R.id.inputLayoutTagCode);
        inputLayoutSearchForUserName = (TextInputLayout) findViewById(R.id.inputLayoutSearchForUserName);

        showUserSearchButton = (FloatingTextButton) findViewById(R.id.action_button_two);
        searchUserButton = (FloatingActionButton) findViewById(R.id.searchButtonForUserName);

        layoutSearchForUserName = (RelativeLayout) findViewById(R.id.layoutSearchForUserName);

        fab_open = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fab_open);
        fab_close = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.fab_close);

        showUserSearchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isFabOpen){
                    showUserSearchButton.startAnimation(fab_close);
                    showUserSearchButton.setIconDrawable(getResources().getDrawable(R.drawable.arrow_up_64));
                    showUserSearchButton.startAnimation(fab_open);
                    layoutSearchForUserName.setVisibility(View.VISIBLE);
                    isFabOpen = false;
                } else {
                    showUserSearchButton.startAnimation(fab_open);
                    showUserSearchButton.setIconDrawable(getResources().getDrawable(R.drawable.arrow_down_64));
                    layoutSearchForUserName.setVisibility(View.INVISIBLE);
                    isFabOpen = true;
                }
            }
        });
        searchUserButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!inputLayoutSearchForUserName.getEditText().getText().toString().isEmpty()) {
                    JsonObjectRequest jsArrayRequest = new JsonObjectRequest(
                            Request.Method.GET, getUrlTags(),
                            null,
                            new Response.Listener<JSONObject>() {
                                @Override
                                public void onResponse(JSONObject response) {
                                    findUser(response, inputLayoutSearchForUserName);
                                }
                            },
                            new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {
                                    MyToastSingleton.getInstance(DeleteTagActivity.this, DeleteTagActivity.this).
                                            setError(getString(R.string.searchError));
                                }
                            }
                    );
                    MyRequestQueueSingleton.getInstance(getBaseContext()).addToRequestQueue(jsArrayRequest);
                }
            }
        });
        inputLayoutTag.getEditText().addTextChangedListener(new TextWatcherInputs(inputLayoutTag));
        inputLayoutTag.getEditText().setOnFocusChangeListener(new OnFocusChangeListenerInputs(inputLayoutSearchForUserName));
        inputLayoutSearchForUserName.getEditText().setOnFocusChangeListener(new OnFocusChangeListenerInputs(inputLayoutTag));

    }

    protected boolean isNotEmpty (){
        return !inputLayoutTag.getEditText().getText().toString().trim().isEmpty();
    }

    protected boolean thereIsNotError (){
        return inputLayoutTag.getEditText().getError() == null;
    }

    /*Si la autenticación es correcta se muestra confirmación de seguridad al usuario para asegurarse de que realmente quiere
    llevar a cabo la acción de borrado del tag */
    public void actionWhenValidPassword(){
        AlertDialog alertDialog = new AlertDialog.Builder(DeleteTagActivity.this)
                .setTitle(getString(R.string.titleAlertRemoveBracelet))
                .setMessage(getString(R.string.messageAlertRemoveBracelet))
                .setIcon(getDrawable(R.drawable.remove))
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        //Si lo confirma se procede a eliminar el tag del sistema
                        deleteTag();
                        MyToastSingleton.getInstance(DeleteTagActivity.this, DeleteTagActivity.this).
                                setSuccess(getString(R.string.successRemoveBracelet));
                        finish();
                        startActivity(getIntent());
                    }
                })
                .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        dialog.dismiss();
                    }
                }).create();
        alertDialog.show();
    }

    /*
    *Esta clase es el manejador de eventos que se activa cada vez que uno de los dos campos de entrada pierden el foco
    * Si un inputtext tiene el foco, el otro se vacía y se le quitan los errores que tuviese, EXPLICACIÓN:
    * se presentan dos situaciones: el usuario conoce el código de la pulsera. Entonces
    * si ha introducido cualquier valor previamente en búsqueda por nombre de usuario, se puede borrar porque ya sabe el
    * código de la pulsera. La otra situación es que rellene un código de pulsera erróneo y vea que no es. Entonces escribe abajo
    * el nombre de usuario para realizar la búsqueda de su código de tag.
    * Lo que había rellenado antes ya no coincide y se procede a eliminar el contenido del inputtext.
    */
    public class OnFocusChangeListenerInputs implements View.OnFocusChangeListener {
        private TextInputLayout textInputLayout;

        OnFocusChangeListenerInputs(TextInputLayout otherTextInputLayout){
            this.textInputLayout = otherTextInputLayout;
        }
        @Override
        public void onFocusChange(View v, boolean hasFocus) {
            if (hasFocus) {
                textInputLayout.getEditText().setText(""); //El otro textInputLayout lo
                if (textInputLayout==inputLayoutSearchForUserName) textInputLayout.getEditText().setError(null);
            }// vacía porque ya no tiene sentido si se está cambiando el otro campo
        }
    }

    //Función que da de baja la pulsera en el sistema
    protected void deleteTag(){
        StringRequest dr = new StringRequest(Request.Method.DELETE,
            URL_TAGS + inputLayoutTag.getEditText().getText().toString().trim(),
            new Response.Listener<String>()
            {
                @Override
                public void onResponse(String response) {
                    MyToastSingleton.getInstance(DeleteTagActivity.this, DeleteTagActivity.this).
                            setSuccess(getString(R.string.successRemoveBracelet));
                }
            },
            new Response.ErrorListener()
            {
                @Override
                public void onErrorResponse(VolleyError error) {
                    MyToastSingleton.getInstance(DeleteTagActivity.this, DeleteTagActivity.this).
                            setError(getString(R.string.errorRemoveBracelet));
                }
            }
        );
        MyRequestQueueSingleton.getInstance(getBaseContext()).addToRequestQueue(dr);
    }

    protected void doSomething (String tag_code){
        inputLayoutTag.getEditText().setText(tag_code);
    }
}
