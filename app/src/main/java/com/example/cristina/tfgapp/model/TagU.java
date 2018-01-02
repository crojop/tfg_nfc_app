package com.example.cristina.tfgapp.model;

import android.content.Context;
import android.util.Log;

import com.example.cristina.tfgapp.controller_view.login.LoginActivity;
import com.example.cristina.tfgapp.controller_view.MainActivity;
import com.example.cristina.tfgapp.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Cristina on 16/11/17.
 */

public class TagU {
    /*
    Clase pulsera o tag. Se ha llamado TagU para distinguirlo de la clase Tag nfc o Tag de la liberaría de TagViews
    usada en BuyActivity.
     */
    private int tag_id; //id del tag
    private int tag_code; // código del tag
    private String tag_description; //descripción del tag
    private EventU eventU; //objeto evento asociado
    private User user; //objeto usuario asociado

    public TagU (){}

    public int removeTagCode () {
        int result = this.tag_code;
        this.tag_code = -1;
        return result;
    }

    public TagU(int tag_id) {
        this.tag_id = tag_id;
    }

    public TagU(int tag_id, int tag_code, String tag_description, EventU eventU, User user) {
        this.tag_id = tag_id;
        this.tag_code = tag_code;
        this.tag_description = tag_description;
        this.eventU = eventU;
        this.user = user;
    }

    public void setTag_id(int tag_id) {
        this.tag_id = tag_id;
    }

    public void setTag_code(int tag_code) {
        this.tag_code = tag_code;
    }

    public void setTag_description(String tag_description) {
        this.tag_description = tag_description;
    }

    public void setEventU(EventU eventU) {
        this.eventU = eventU;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public int getTag_id() {
        return tag_id;
    }

    public int getTag_code() {
        return tag_code;
    }

    public String getTag_description() {
        return tag_description;
    }

    public EventU getEventU() {
        return eventU;
    }

    public User getUser() {
        return user;
    }

    /*Función que recibe el json generado por un GET a un tag en concreto.
    Si es válido devuelve true y asigna los valores recogidos del json al objeto currentTag
    Si no es válido devuelve false.
    */
    public static boolean validateTag (JSONObject jsonObject, Context context){
        boolean result=false;
        try {
            boolean error = jsonObject.getBoolean(context.getString(R.string.error));
            String message = jsonObject.getString(context.getString(R.string.message));
            if (!error && message.equals(context.getString(R.string.success))){
                JSONArray jsonArray = jsonObject.getJSONArray(context.getString(R.string.data));
                for(int i=0; i<jsonArray.length(); i++){
                    try {
                        JSONObject objeto= jsonArray.getJSONObject(i);
                        if (String.valueOf(objeto.getInt(context.getString(R.string.tag_code)))!=null &&
                                String.valueOf(objeto.getInt(context.getString(R.string.user_id)))!=null
                                && objeto.getInt(context.getString(R.string.event_id))== LoginActivity.terminalU.getEventU().getEvent_id()){
                            User currentUser = new User(objeto.getInt(context.getString(R.string.user_id)));
                            currentUser.setUser_description(objeto.getString(context.getString(R.string.user_description)));
                            currentUser.setBalance(objeto.getDouble(context.getString(R.string.balance)));

                            MainActivity.currentTag.setTag_id(objeto.getInt(context.getString(R.string.tag_id)));
                            MainActivity.currentTag.setTag_code(objeto.getInt(context.getString(R.string.tag_code)));
                            MainActivity.currentTag.setTag_description(objeto.getString(context.getString(R.string.tag_description)));

                            MainActivity.currentTag.setEventU(LoginActivity.terminalU.getEventU());
                            MainActivity.currentTag.setUser(currentUser);
                            result = true;
                        }
                    } catch (JSONException e) {
                        Log.d(context.getString(R.string.error), context.getString(R.string.parsingError)+ e.getMessage());
                    }
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return result;
    }
}

