package com.example.cristina.tfgapp.controller_view.menus;

import android.app.ActionBar;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;

import com.example.cristina.tfgapp.R;
import com.example.cristina.tfgapp.charts.barcharts.AbstractBarChart;
import com.example.cristina.tfgapp.controller_view.AdapterGraph;
import com.example.cristina.tfgapp.controller_view.MainActivity;
import com.example.cristina.tfgapp.charts.barcharts.TabDemoMainActivity;
import com.example.cristina.tfgapp.controller_view.settings.RechargeBalanceActivity;
import com.example.cristina.tfgapp.controller_view.settings.SettingsActivity;
import com.example.cristina.tfgapp.controller_view.shop.ProductActivity;

import java.util.HashMap;

/**
 * Created by Cristina on 20/09/17.
 */

public class MenuActivity extends AbstractBarChart {

    /*
    Activity que muestra el menú de opciones que puede realizar el usuario, una vez ha sido autenticado
     */

    //RecyclerView para los card views que mostrarán las opciones
    private RecyclerView recMainView;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        //Añade título e imagen a la app bar con el nombre de usuario y el icono de usuario validado
        initializeTagAppBar();

        recMainView = (RecyclerView) findViewById(R.id.RecMenuView);
        recMainView.setHasFixedSize(true);

        //Opciones del menú
        HashMap<String, Integer> mapaMain = new HashMap<String, Integer>();
        mapaMain.put(getString(R.string.SHOP),getResources().getIdentifier("shopping_cart", getString(R.string.drawable), getPackageName()));
        mapaMain.put(getString(R.string.MOVEMENTS),getResources().getIdentifier("transaction", getString(R.string.drawable), getPackageName()));
        mapaMain.put(getString(R.string.BALANCE),getResources().getIdentifier("piggybank128", getString(R.string.drawable), getPackageName()));
        mapaMain.put(getString(R.string.SETTINGS),getResources().getIdentifier("settings", getString(R.string.drawable), getPackageName()));

        final String[] main_titles = {getString(R.string.SHOP), getString(R.string.MOVEMENTS), getString(R.string.BALANCE), getString(R.string.SETTINGS)};

        int[] fotos = new int[main_titles.length];
        for(int posicion=0; posicion<fotos.length; posicion++) fotos[posicion] = (int)mapaMain.get(main_titles[posicion]);

        final AdapterGraph adaptador = new AdapterGraph(main_titles, fotos);

        adaptador.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int posicion = recMainView.getChildAdapterPosition(v);
                switch (posicion){
                    case 0:
                        //Tienda
                        Intent intent = new Intent(v.getContext(), ProductActivity.class);
                        startActivity(intent);
                        break;
                    case 1:
                        //Moviemientos: registros y estadísticas de usuario
                        Intent intentTransactions = new Intent(v.getContext(), TabDemoMainActivity.class);
                        startActivity(intentTransactions);
                        break;
                    case 2:
                        //Saldo: consulta y recarga de saldo
                        Intent intentRechargeBalance = new Intent(v.getContext(), RechargeBalanceActivity.class);
                        startActivity(intentRechargeBalance);
                        break;
                    case 3:
                        //Configuración
                        Intent intentSettings = new Intent(v.getContext(), SettingsActivity.class);
                        startActivity(intentSettings);
                        break;
                    default:
                        break;
                }
            }
        });

        recMainView.setLayoutManager(new GridLayoutManager(this,2));
        recMainView.setAdapter(adaptador);
    }

    /*
    Añade título e imagen a la app bar con el nombre de usuario y el icono de usuario validado
     */
    public final void initializeTagAppBar (){
        if (MainActivity.currentTag!=null){
            setTitle(MainActivity.currentTag.getUser().getUser_description());
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setDisplayOptions(getSupportActionBar().getDisplayOptions()
                    | ActionBar.DISPLAY_SHOW_CUSTOM);
            ImageView imageView = new ImageView(getSupportActionBar().getThemedContext());
            imageView.setScaleType(ImageView.ScaleType.CENTER);
            imageView.setImageResource(R.drawable.user_32);
            ActionBar.LayoutParams layoutParams = new ActionBar.LayoutParams(
                    ActionBar.LayoutParams.WRAP_CONTENT,
                    ActionBar.LayoutParams.WRAP_CONTENT, Gravity.RIGHT
                    | Gravity.CENTER_VERTICAL);
            layoutParams.leftMargin = 40;
            imageView.setLayoutParams(layoutParams);
            getSupportActionBar().setCustomView(imageView);
        }
    }

    protected String getPattern (){
        return getString(R.string.pattern_dd_MM);
    }

}
