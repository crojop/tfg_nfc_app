package com.example.cristina.tfgapp.charts.linecharts;

import android.os.Bundle;

import com.example.cristina.tfgapp.R;
import com.example.cristina.tfgapp.model.TransactionU;

/**
 * Created by Cristina on 14/12/17.
 */

public class RechargesLineChart extends LineChartExample {
    /*
    Activity que hereda de LineChartExample y que contiene la gráfica que representa el número de recargas
    realizadas por hora un día en concreto.
     */
    public void onCreate(Bundle savedInstanceState) {
        setGraph_color(R.color.colorGreenApp);
        setGraph_legend(getString(R.string.numberRechargesPerHour));
        setGraph_title(getString(R.string.rechargesPerHour));
        setTransaction_type(TransactionU.TRANSACTION_RECHARGE);
        super.onCreate(savedInstanceState);
    }
}
