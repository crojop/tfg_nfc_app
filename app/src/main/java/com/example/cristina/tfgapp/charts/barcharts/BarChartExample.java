package com.example.cristina.tfgapp.charts.barcharts;

import android.os.Bundle;

import com.example.cristina.tfgapp.R;
import com.example.cristina.tfgapp.controller_view.Utils;
import com.example.cristina.tfgapp.model.Statistic;

/**
 * Created by Cristina on 30/06/17.
 */

public class BarChartExample extends AbstractBarChart {
    /*
    Clase que hereda de AbstractBarChart (que a su vez hereda de AbstractChart).
    De esta clase heredan RechargesChart y PaymentsChart, que son las clases que van a generar los gráfico de barras
    del número de recargas y pagos por día de evento respectivamente.
     */

    private final String QUERY_STRING_TRANS_ID = "&transactiontype_id=";
    private String graph_title;
    private String graph_legend;
    private int graph_color;
    private Class line_chart_class;
    private int transaction_type;

    public String getGraph_title() {
        return graph_title;
    }

    public String getGraph_legend() {
        return graph_legend;
    }

    public int getGraph_color() {
        return graph_color;
    }

    public Class getLine_chart_class() {
        return line_chart_class;
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

    public void setLine_chart_class(Class line_chart_class) {
        this.line_chart_class = line_chart_class;
    }

    public void setTransaction_type(int transaction_type) {
        this.transaction_type = transaction_type;
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        arrayDates = new DateElement[]{};
        getStats(graph_title, graph_color, graph_legend, line_chart_class);
    }

    //Patrón de la fecha, así se guarda en arrayDates y así se visualizará en el eje x.
    protected String getPattern (){
        return getString(R.string.pattern_dd);
    }

    protected double getSecParamDateElement (Statistic statistic){
        return statistic.getNumber_of_transactions();
    }

    /* Para que el método GET devuelva sólo las transacciones del tipo que se esté mostrando. */
    protected String getQueryStringCont(){
        return QUERY_STRING_TRANS_ID+transaction_type;
    }

    //Se hace un cast a enteros por sea caso
    protected void normalize (){
        for (int i=0; i<arrayDates.length; i++) arrayDates[i].setNum((int)arrayDates[i].getNum());
    }
}