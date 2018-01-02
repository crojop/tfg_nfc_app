package com.example.cristina.tfgapp.controller_view.settings;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.example.cristina.tfgapp.R;
import com.example.cristina.tfgapp.controller_view.AdapterGraph;
import com.example.cristina.tfgapp.controller_view.MyTerminal;

import java.util.HashMap;

/**
 * Created by Cristina on 22/11/17.
 */

public class BracManActivity extends MyTerminal {
    //Activity menú de "Gestión de pulseras". Presenta un recycler view con dos card view: "Alta pulsera" y "Baja pulsera"

    private RecyclerView recConfigView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_brac_man);

        recConfigView = (RecyclerView) findViewById(R.id.RecConfigView);
        recConfigView.setHasFixedSize(true);

        HashMap<String, Integer> mapaMain = new HashMap<String, Integer>();
        mapaMain.put(getString(R.string.addBracelet),getResources().getIdentifier(getString(R.string.id_drawable_add_bracelet), getString(R.string.drawable), getPackageName()));
        mapaMain.put(getString(R.string.removeBracelet),getResources().getIdentifier(getString(R.string.id_drawable_delete_bracelet), getString(R.string.drawable), getPackageName()));

        final String[] main_titles = {getString(R.string.addBracelet), getString(R.string.removeBracelet)};

        int[] fotos = new int[main_titles.length];
        for(int posicion=0; posicion<fotos.length; posicion++) fotos[posicion] = (int)mapaMain.get(main_titles[posicion]);

        final AdapterGraph adaptador = new AdapterGraph(main_titles, fotos);

        adaptador.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int posicion = recConfigView.getChildAdapterPosition(v);
                switch (posicion){
                    case 0:
                        Intent addTagIntent = new Intent (getBaseContext(), AddTagActivity.class);
                        startActivity(addTagIntent);
                        break;
                    case 1:
                        Intent deleteTagIntent = new Intent (getBaseContext(), DeleteTagActivity.class);
                        startActivity(deleteTagIntent);
                        break;
                    default:
                        break;
                }
            }
        });

        recConfigView.setLayoutManager(new GridLayoutManager(this,2));
        recConfigView.setAdapter(adaptador);
    }
}
