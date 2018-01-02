package com.example.cristina.tfgapp.charts.linecharts;

import android.os.Bundle;

import com.example.cristina.tfgapp.R;
import com.example.cristina.tfgapp.model.TransactionU;

import org.json.JSONException;
import org.json.JSONObject;


/**
 * Created by Cristina on 15/09/17.
 */

public class FinancialLineChart extends AbstractLineChart {

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        array_hours = new double[24];
        for (int i=0 ; i<24; i++) array_hours[i] = 0.0;
        getStats(getString(R.string.incomes), R.color.colorIndigo, getString(R.string.legend_finanLineChart_1)+" "+getString(R.string.euros_divisa)+
                getString(R.string.legend_finanLineChart_2));
    }

    /*En el array se almacena la cantidad de dinero que se ha movido en las transacciones, a la cantidad total acumulada se le van restando los pagos,
    y se le van sumando los ingresos*/
    protected double getNumberArrayHours(JSONObject objeto){
        int transactiontype_id;
        double total_amount = 0.0;
        try {
            transactiontype_id = objeto.getInt(getString(R.string.transactiontype_id));
            total_amount = (transactiontype_id== TransactionU.TRANSACTION_PAYMENT)
                    ? -objeto.getDouble(getString(R.string.total_amount)) : objeto.getDouble(getString(R.string.total_amount));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return total_amount;
    }
}
