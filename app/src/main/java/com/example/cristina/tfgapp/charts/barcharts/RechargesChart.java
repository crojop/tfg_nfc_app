package com.example.cristina.tfgapp.charts.barcharts;

import android.os.Bundle;

import com.example.cristina.tfgapp.R;
import com.example.cristina.tfgapp.model.TransactionU;
import com.example.cristina.tfgapp.charts.linecharts.RechargesLineChart;

/**
 * Created by Cristina on 14/12/17.
 */

public class RechargesChart extends BarChartExample {
    /*
    Activity que hereda de BarChartExample y que contiene la gráfica que representa el número de recargas realizadas por día del festival
     */
    protected void onCreate(Bundle savedInstanceState) {
        setGraph_color(R.color.colorGreenApp);
        setGraph_legend(getString(R.string.numberRechargesPerDay));
        setGraph_title(getString(R.string.legend_rechargesChart));
        setLine_chart_class(RechargesLineChart.class);
        setTransaction_type(TransactionU.TRANSACTION_RECHARGE);
        super.onCreate(savedInstanceState);
    }
}
