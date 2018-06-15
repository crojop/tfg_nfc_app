package com.example.cristina.tfgapp.controller_view;

import android.app.ActionBar;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.example.cristina.tfgapp.R;
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
        SharedPreferences sharedPreferences = getSharedPreferences(getString(R.string.shar_prefs_name), MODE_PRIVATE);
        setTitle(sharedPreferences.getString(getString(R.string.shar_prefs_event_description), getString(R.string.unknown_event)) + " " + sharedPreferences.getString(getString(R.string.shar_prefs_terminal_description), getString(R.string.unknown_term_desc)));
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayOptions(getSupportActionBar().getDisplayOptions() | ActionBar.DISPLAY_SHOW_CUSTOM);
    }

}
