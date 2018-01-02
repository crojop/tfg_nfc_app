package com.example.cristina.tfgapp.charts.barcharts;

import android.os.Bundle;

import com.example.cristina.tfgapp.R;
import com.example.cristina.tfgapp.model.TransactionU;
import com.example.cristina.tfgapp.charts.linecharts.PaymentsLineChart;

/**
 * Created by Cristina on 14/12/17.
 */

public class PaymentsChart extends BarChartExample {
    /*
    Activity que hereda de BarChartExample y que contiene la gráfica que representa el número de pagos realizados por día del festival
     */
    protected void onCreate(Bundle savedInstanceState) {
        setGraph_color(R.color.colorTeja);
        setGraph_legend(getString(R.string.numberUnitsPerDay));
        setGraph_title(getString(R.string.legend_barChartExample));
        setLine_chart_class(PaymentsLineChart.class);
        setTransaction_type(TransactionU.TRANSACTION_PAYMENT);
        super.onCreate(savedInstanceState);
    }
}
