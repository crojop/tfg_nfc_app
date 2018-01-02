package com.example.cristina.tfgapp.charts.linecharts;

import android.os.Bundle;

import com.example.cristina.tfgapp.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

/**
 * Created by Cristina on 15/09/17.
 */

public class LineChartExample extends AbstractLineChart implements Serializable{

    private static final String QUERY_STRING_TRANS_ID = "&transactiontype_id=";
    private String graph_title;
    private String graph_legend;
    private int graph_color;
    private int transaction_type;

    public void setTransaction_type(int transaction_type) {
        this.transaction_type = transaction_type;
    }

    public int getTransaction_type() {
        return transaction_type;
    }

    public void setGraph_title(String graph_title) {
        this.graph_title = graph_title;
    }

    public void setGraph_legend(String graph_legend) {
        this.graph_legend = graph_legend;
    }

    public void setGraph_color(int graph_color) {
        this.graph_color = graph_color;
    }

    public String getGraph_title() {
        return graph_title;
    }

    public String getGraph_legend() {
        return graph_legend;
    }

    public int getGraph_color() {
        return graph_color;
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        array_hours = new double[24];
        for (int i=0 ; i<24; i++) array_hours[i] = 0.0;
        getStats(graph_title, graph_color, graph_legend);
    }

    //En el array se almacena el número de transacciones
    protected double getNumberArrayHours(JSONObject objeto){
        int number_transactions = 0;
        try {
            number_transactions = objeto.getInt(getString(R.string.number_of_transactions));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return number_transactions;
    }

    //Añade al query string del método GET el filtrado por tipo de transacción (pago o recarga)
    protected String getQueryStringCont (){
        return QUERY_STRING_TRANS_ID+transaction_type;
    }

    //Parsea los valores del array a enteros.
    protected void normalize (){
        for (int i=0; i<array_hours.length; i++) array_hours[i] = (int)array_hours[i];
    }
}
