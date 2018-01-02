package com.example.cristina.tfgapp.controller_view.settings;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.cristina.tfgapp.controller_view.MainActivity;
import com.example.cristina.tfgapp.controller_view.menus.MenuActivity;
import com.example.cristina.tfgapp.singleton.MyRequestQueueSingleton;
import com.example.cristina.tfgapp.R;
import com.example.cristina.tfgapp.singleton.MyToastSingleton;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Cristina on 21/11/17.
 */

public class SettingsActivity extends MenuActivity {
    /*
    Activity que permite al usuario gestionar o configurar su pulsera. Actualmente sólo permite editar el nombre de la pulsera.
     */

    //TextInputLayout con el nombre de la pulsera
    private TextInputLayout inputLayoutTag;
    private final String URL_TAGS = "http://vpayment.perentec.com/API/V1/tags/";

    //Botón de guardar cambios
    private Button buttonEditSettings;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        inputLayoutTag = (TextInputLayout) findViewById(R.id.inputLayoutTag);
        inputLayoutTag.getEditText().setText(MainActivity.currentTag.getTag_description());
        inputLayoutTag.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                //No puede estar vacío ni tener más de 32 caracteres
                if (s.toString().trim().isEmpty()) inputLayoutTag.setError(getString(R.string.requiredField));
                else if (count>32) inputLayoutTag.setError(getString(R.string.maxLength32));
                else inputLayoutTag.setError(null);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        buttonEditSettings = (Button) findViewById(R.id.buttonEditSettings);
        buttonEditSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (inputLayoutTag.getError()==null) {
                    //Si no hay errores se llama a la función putTag
                    putTag();
                }
            }
        });
    }

    //Esta función se encarga de realizar el método PUT
    private void putTag(){
        String url = URL_TAGS+MainActivity.currentTag.getTag_code();
        StringRequest putRequest = new StringRequest(Request.Method.PUT, url,
                new Response.Listener<String>()
                {
                    @Override
                    public void onResponse(String response) {
                        /*Si to do ha ido correctamente se muestra un toast al usuario, se actualiza el nombre de la pulsera
                        en el objeto currentTag, y se lleva al usuario otra vez al menú*/
                        MyToastSingleton.getInstance(SettingsActivity.this, SettingsActivity.this).
                                setSuccess(getString(R.string.successSavingChanges));
                        MainActivity.currentTag.setTag_description(inputLayoutTag.getEditText().getText().toString());
                        Intent intent = new Intent(SettingsActivity.this, MenuActivity.class);
                        startActivity(intent);
                    }
                },
                new Response.ErrorListener()
                {
                    public void onErrorResponse(VolleyError error) {
                        //Si no ha habido éxito se muestra mensaje de error
                        MyToastSingleton.getInstance(SettingsActivity.this, SettingsActivity.this).
                                setError(getString(R.string.errorHasOcurred));
                        Log.d(getString(R.string.error), getString(R.string.errorSavingChanges));
                    }
                }
        ) {

            protected Map<String, String> getParams()
            {
                Map<String, String> params = new HashMap<String, String>();
                params.put(getString(R.string.tag_description), inputLayoutTag.getEditText().getText().toString().trim());
                return params;
            }

        };
        MyRequestQueueSingleton.getInstance(this).addToRequestQueue(putRequest);
    }
}
