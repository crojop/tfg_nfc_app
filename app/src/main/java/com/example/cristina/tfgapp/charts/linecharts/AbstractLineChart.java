package com.example.cristina.tfgapp.charts.linecharts;

import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.cristina.tfgapp.singleton.MyRequestQueueSingleton;
import com.example.cristina.tfgapp.R;
import com.example.cristina.tfgapp.controller_view.login.LoginActivity;
import com.example.cristina.tfgapp.charts.AbstractChart;

import org.achartengine.ChartFactory;
import org.achartengine.chart.PointStyle;
import org.achartengine.model.XYMultipleSeriesDataset;
import org.achartengine.model.XYSeries;
import org.achartengine.renderer.XYMultipleSeriesRenderer;
import org.achartengine.renderer.XYSeriesRenderer;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import static android.content.ContentValues.TAG;

/**
 * Created by Cristina on 20/10/17.
 */

public abstract class AbstractLineChart extends AbstractChart {
    //Activity que genera gráficos de línea. De ella heredan LineChartExample y FinancialLineChart
    protected static final String SUCCESS_CODE = "000";
    private static final String URL_GET_STATS = "http://vpayment.perentec.com/API/V1/stats";
    private static final String QUERY_STRING_TER_NUM = "?terminal_serial_number=";
    private static final String QUERY_STRING_DAY = "&day=";
    protected String day;//fecha a representar. Llega como extra de la bar chart activity
    protected double[] array_hours;/*array cuyos índices coinciden con las horas del día y almacenan
    los valores numéricos (ingresos, o unidades de recargas o pagos) asociados a esa hora*/

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_linechart);
        //Recuperan el valor del día a representar que les llega como extra, para hacer un GET de las estadísticas de ese día
        if (savedInstanceState == null) {
            Bundle extras = getIntent().getExtras();
            if (extras == null) day = null;
            else day = extras.getString(getResources().getString(R.string.DAY));
        } else day = (String) savedInstanceState.getSerializable(getResources().getString(R.string.DAY));
    }
    /*
    Función que crea y customiza el XYSeriesRenderer
     */
    protected XYSeriesRenderer createXYSeriesRenderer(int colorGraph){
        XYSeriesRenderer renderer = new XYSeriesRenderer();
        renderer.setColor(ContextCompat.getColor(this, colorGraph));
        renderer.setPointStyle(PointStyle.CIRCLE);
        renderer.setFillPoints(true);
        renderer.setChartValuesTextSize(16);
        renderer.setChartValuesTextAlign(Paint.Align.CENTER);
        renderer.setChartValuesSpacing(12);
        renderer.setLineWidth(3);
        renderer.setDisplayChartValues(false);
        return renderer;
    }

    /*
    Función que crea y customiza el XYMultipleSeriesRenderer
    */
    protected XYMultipleSeriesRenderer createXYmultipleSeriesRenderer(String chart_title){
        XYMultipleSeriesRenderer multiRenderer = super.createXYmultipleSeriesRenderer(chart_title+" - "+getString(R.string.dayGraph)+" "+day);

        multiRenderer.setXLabelsColor(Color.BLACK);
        //multiRenderer.setShowLabels(true);

        //Otros
        multiRenderer.setPointSize(4); //Tamaño del punto

        //X
        multiRenderer.setXLabels(24);
        multiRenderer.setXAxisMin(-1.5);
        multiRenderer.setXAxisMax(23.5);

        //setting the margin size for the graph in the order top, left, bottom, right
        multiRenderer.setMargins(new int[]{84, 54, 46, 12});
        return multiRenderer;
    }

    /*
    Función que crea y customiza el XYSeries. Inicializa el valor de cada punto x e y con los
    valores almacenados en arrayDates
     */
    protected XYSeries createXYSeries (String graph_title){
        XYSeries expenseSeries = new XYSeries(graph_title);

        // Adding data to Expense Series
        for(int i=0;i<24;i++){
            expenseSeries.add(i, array_hours[i]);
        }
        return expenseSeries;
    }

    /*
    Función que devuelve el objeto gráfico customizado
     */
    protected View getChart (XYMultipleSeriesDataset dataset, XYMultipleSeriesRenderer multiRenderer, final Class intentClass){
        View chart = ChartFactory.getLineChartView(getBaseContext(), dataset, multiRenderer);
        return chart;
    }

    /**
     * Función que dibuja el gráfico pasándole los parámetros a getChart
     */
    protected void drawChart(String chart_title, int colorGraph, String graph_title){
        super.drawChart(chart_title, colorGraph, graph_title, false);
        chartContainer.addView(getChart(dataset, multiRenderer, null));
    }

    /*
    Función que devuelve el valor máximo que alcanzará el eje y. Para ello recorre todos los valores almacenados
    en el array de DateElements. En este caso el valor máximo de y es el mayor pico de ingresos
     */
    protected void getMaxY(){
        maxY=0.0;
        for (int i=0; i<array_hours.length; i++) if (maxY<array_hours[i]) maxY=array_hours[i];
    }

    /*
    Esta función recibe por parámetro el objeto JSON con todas las estadísticas de ese número de terminal un día en concreto.
    A medida que lee el objeto json, almacena las cantidades: ingresos por hora, o bien pagos o recargas realizadas por hora.
    Las va insertando en un array que tiene 24 posiciones, que se corresponden con las horas.
     */
    protected void gatherData (JSONObject jsonObject){
        try {
            if (jsonObject.getString(getString(R.string.code)).equals(SUCCESS_CODE)) {
                JSONArray jsonArray = jsonObject.getJSONArray(getString(R.string.data));
                for(int i=0; i<jsonArray.length(); i++){
                    try {
                        JSONObject objeto= jsonArray.getJSONObject(i);
                        array_hours[objeto.getInt(getString(R.string.hour))]+=getNumberArrayHours(objeto);
                    } catch (JSONException e) {
                        Log.e(TAG, getString(R.string.parsingError)+ e.getMessage());
                    }
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    protected abstract double getNumberArrayHours(JSONObject objeto);

    /*
    Mediante el método GET se obtienen las estadísticas para ese número de terminal y ese día en concreto. Se pueden añadir
    más condiciones al query string con getQueryStringCont() (para filtrar por id de transacción)
     */
    protected void getStats (final String graph_title, final int graph_color, final String graph_legend){
        JsonObjectRequest jsArrayRequest = new JsonObjectRequest(
                Request.Method.GET,
                URL_GET_STATS+QUERY_STRING_TER_NUM+ LoginActivity.terminalU.getTerminal_serial_number()+QUERY_STRING_DAY+day+getQueryStringCont(),
                null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        gatherData(response);
                        getMaxY();
                        normalize();
                        drawChart(graph_title, graph_color, graph_legend);
                        pDialog.dismiss(); //que no se cierre el diálogo to do el rato y se abra
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        pDialog.dismiss(); //que no se cierre el diálogo to do el rato y se abra
                        Log.d(getString(R.string.error), getString(R.string.errorJsonResponse) + error.getMessage());
                    }
                }
        );
        MyRequestQueueSingleton.getInstance(this).addToRequestQueue(jsArrayRequest);
        pDialog.show();
    }

    /*
    Función que devuelve la continuación del query string.
    Por defecto devuelve una cadena vacía.
     */
    protected String getQueryStringCont (){
        return "";
    }

    protected void normalize (){}
}
