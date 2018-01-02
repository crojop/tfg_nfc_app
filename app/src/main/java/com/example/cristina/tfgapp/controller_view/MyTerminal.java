package com.example.cristina.tfgapp.controller_view;

import android.app.ActionBar;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.example.cristina.tfgapp.controller_view.login.LoginActivity;

/**
 * Created by Cristina on 21/11/17.
 */

public abstract class MyTerminal extends AppCompatActivity{
/*
Esta activity está pensada para mostrar en la app bar la descripción del evento actual y la descripción del terminal actual
De ella heredan todas las clases del navigation drawer y la main activity, que son las que van a mostrar este título.
 */
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initializeTerminalAppBar();
    }
    public final void initializeTerminalAppBar (){
        if (LoginActivity.terminalU!=null){
            setTitle(LoginActivity.terminalU.getEventU().getEvent_description() + " " +
                    LoginActivity.terminalU.getTerminal_description());
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setDisplayOptions(getSupportActionBar().getDisplayOptions()
                    | ActionBar.DISPLAY_SHOW_CUSTOM);
        }
    }

}
