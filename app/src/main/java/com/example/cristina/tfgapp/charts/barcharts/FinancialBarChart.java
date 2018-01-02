package com.example.cristina.tfgapp.charts.barcharts;

import android.os.Bundle;

import com.example.cristina.tfgapp.R;
import com.example.cristina.tfgapp.model.Statistic;
import com.example.cristina.tfgapp.charts.linecharts.FinancialLineChart;

/**
 * Created by Cristina on 30/06/17.
 */

public class FinancialBarChart extends AbstractBarChart {

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        arrayDates = new DateElement[]{};
        getStats(getResources().getString(R.string.incomesPerDay), R.color.colorIndigo, getString(R.string.legend_finanBarChart_1)+" "+getString(R.string.euros_divisa)+
                getString(R.string.legend_finanBarChart_2), FinancialLineChart.class);
    }

    //Patrón de la fecha, así se guarda en arrayDates y así se visualizará en el eje x.
    protected String getPattern (){
        return getString(R.string.pattern_dd);
    }

    protected double getSecParamDateElement (Statistic statistic){
        return statistic.getTotal_amount();
    }
}
