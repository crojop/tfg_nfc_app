package com.example.cristina.tfgapp.controller_view.menus;

/**
 * Created by Cristina on 16/09/17.
 */

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.example.cristina.tfgapp.R;
import com.example.cristina.tfgapp.charts.barcharts.FinancialBarChart;
import com.example.cristina.tfgapp.charts.barcharts.PaymentsChart;
import com.example.cristina.tfgapp.charts.piecharts.PieChart;
import com.example.cristina.tfgapp.charts.barcharts.RechargesChart;
import com.example.cristina.tfgapp.controller_view.AdapterGraph;
import com.example.cristina.tfgapp.controller_view.MyTerminal;

import java.util.HashMap;


public class RecyclerActivity extends MyTerminal {
/*
Activity que muestra el menú de las estadísticas de la navigation drawer list
 */
    private RecyclerView recView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recycler);

        recView = (RecyclerView) findViewById(R.id.RecView);
        recView.setHasFixedSize(true);

        HashMap<String, Integer> mapaGraphs = new HashMap<String, Integer>();
        mapaGraphs.put(getString(R.string.recyclerCardTitle1),getResources().getIdentifier(getString(R.string.id_drawable_piechart), getString(R.string.drawable), getPackageName()));
        mapaGraphs.put(getString(R.string.recyclerCardTitle2),getResources().getIdentifier(getString(R.string.id_drawable_presentation), getString(R.string.drawable), getPackageName()));
        mapaGraphs.put(getString(R.string.recyclerCardTitle3),getResources().getIdentifier(getString(R.string.id_drawable_stats_recharge), getString(R.string.drawable), getPackageName()));
        mapaGraphs.put(getString(R.string.recyclerCardTitle4),getResources().getIdentifier(getString(R.string.id_drawable_stats_payment), getString(R.string.drawable), getPackageName()));

        final String[] graph_titles = {getString(R.string.recyclerCardTitle1), getString(R.string.recyclerCardTitle2), getString(R.string.recyclerCardTitle3), getString(R.string.recyclerCardTitle4)};

        int[] fotos = new int[graph_titles.length];
        for(int posicion=0; posicion<fotos.length; posicion++) fotos[posicion] = (int)mapaGraphs.get(graph_titles[posicion]);

        final AdapterGraph adaptador = new AdapterGraph(graph_titles, fotos);

        adaptador.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int posicion = recView.getChildAdapterPosition(v);
                switch (posicion){
                    case 0:
                        //Gráfico de tarta sobre los productos
                        Intent intentPieChart = new Intent(v.getContext(), PieChart.class);
                        startActivity(intentPieChart);
                        break;
                    case 1:
                        //Gráficos de ingresos
                        Intent intentFinancialBarChart = new Intent(v.getContext(), FinancialBarChart.class);
                        startActivity(intentFinancialBarChart);
                        break;
                    case 2:
                        //Gráficos de recargas
                        Intent intentRechargesChart = new Intent(v.getContext(), RechargesChart.class);
                        startActivity(intentRechargesChart);
                        break;
                    case 3:
                        //Gráficos de pagos
                        Intent intentPaymentsChart = new Intent(v.getContext(), PaymentsChart.class);
                        startActivity(intentPaymentsChart);
                        break;
                    default:
                        break;
                }
            }
        });

        recView.setLayoutManager(new GridLayoutManager(this,2));
        recView.setAdapter(adaptador);
    }

}
