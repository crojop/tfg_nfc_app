package com.example.cristina.tfgapp.charts;

import android.app.ProgressDialog;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import com.example.cristina.tfgapp.R;
import com.example.cristina.tfgapp.controller_view.MyTerminal;
import com.example.cristina.tfgapp.controller_view.Utils;

import org.achartengine.model.XYMultipleSeriesDataset;
import org.achartengine.model.XYSeries;
import org.achartengine.renderer.XYMultipleSeriesRenderer;
import org.achartengine.renderer.XYSeriesRenderer;

/**
 * Created by Cristina on 20/10/17.
 */

public abstract class AbstractChart extends MyTerminal {
    protected double maxY;//Valor máximo que alcanzará el eje Y
    protected LinearLayout chartContainer;
    protected ProgressDialog pDialog; //Para mostrarlo mientras que se estén cargando los datos
    protected XYMultipleSeriesDataset dataset;
    protected XYMultipleSeriesRenderer multiRenderer;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        pDialog = new ProgressDialog(this);
        pDialog.setMessage(getString(R.string.loadingData));
        pDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        pDialog.setCancelable(true);
    }

    /*
    Función que devuelve el valor máximo que alcanzará el eje y. Para ello recorre todos los valores almacenados
    en el array de DateElements.
     */
    protected abstract void getMaxY();

    /*
    Función que crea y customiza el XYSeries. Inicializa el valor de cada punto x e y con los
    valores almacenados en arrayDates
     */
    protected abstract XYSeries createXYSeries(String graph_title);

    /*
    Función que crea y customiza el XYSeriesRenderer
     */
    protected abstract XYSeriesRenderer createXYSeriesRenderer(int colorGraph);

    /*
    Función que crea y customiza el XYMultipleSeriesRenderer
    */
    protected XYMultipleSeriesRenderer createXYmultipleSeriesRenderer(String chart_title){
        XYMultipleSeriesRenderer multiRenderer = new XYMultipleSeriesRenderer();

        multiRenderer.setChartTitle(chart_title);
        //Letra: tamaño y estilos
        multiRenderer.setChartTitleTextSize(26);
        multiRenderer.setLabelsTextSize(16);
        multiRenderer.setLegendTextSize(18);
        multiRenderer.setTextTypeface("sans-serif-smallcaps", Typeface.BOLD);

        //Colores
        multiRenderer.setBackgroundColor(Color.WHITE);
        multiRenderer.setMarginsColor(Color.WHITE);
        multiRenderer.setApplyBackgroundColor(true);
        multiRenderer.setYLabelsColor(0, Color.BLACK);
        multiRenderer.setLabelsColor(Color.BLACK);
        multiRenderer.setAxesColor(Color.BLACK);
        multiRenderer.setGridColor(Color.GRAY);

        //Grid
        multiRenderer.setShowGridX(true);

        //Legend
        multiRenderer.setFitLegend(true);
        multiRenderer.setShowLegend(true);

        multiRenderer.setAntialiasing(true);
        multiRenderer.setInScroll(true);

        //Zoom
        multiRenderer.setZoomEnabled(true);
        multiRenderer.setExternalZoomEnabled(true);
        multiRenderer.setPanEnabled(true, true); //Permitir desplazamiento sobre los ejes X e Y

        //X labels
        multiRenderer.setXLabelsAlign(Paint.Align.CENTER);
        multiRenderer.setXLabelsAngle(0);

        //Y labels
        multiRenderer.setYLabelsAlign(Paint.Align.LEFT);
        multiRenderer.setYAxisMax(maxY+(maxY*0.1));

        //Y
        multiRenderer.setYLabelsPadding(50);
        multiRenderer.setYAxisMin(-maxY*0.05);

        //setting the margin size for the graph in the order top, left, bottom, right
        multiRenderer.setMargins(new int[]{64, 54, 46, 12});

        return multiRenderer;
    }

    /*
    Función que devuelve el objeto gráfico customizado
     */
    protected abstract View getChart(XYMultipleSeriesDataset dataset, XYMultipleSeriesRenderer multiRenderer, final Class intentClass);

    /**
     * Función que dibuja el gráfico pasándole los parámetros a getChart
     */
    protected void drawChart(String chart_title, int colorGraph, String graph_title, boolean remove){
        XYSeries expenseSeries = createXYSeries(graph_title);
        dataset = new XYMultipleSeriesDataset();
        dataset.addSeries(expenseSeries);
        XYSeriesRenderer renderer = createXYSeriesRenderer(colorGraph);
        multiRenderer = createXYmultipleSeriesRenderer(chart_title);
        multiRenderer.addSeriesRenderer(renderer);
        chartContainer = (LinearLayout) findViewById(R.id.chart_container);
        if (remove) chartContainer.removeAllViews();
    }

    protected String getTokenCont(){
        return "&token="+ Utils.decryptSth(getSharedPreferences(getString(R.string.shar_prefs_name), MODE_PRIVATE).getString(getString(R.string.shar_prefs_token), ""));
    }
}