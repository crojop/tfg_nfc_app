package com.example.cristina.tfgapp.controller_view.logs;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.Typeface;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.WindowManager;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.example.cristina.tfgapp.R;

import java.util.ArrayList;

/**
 * Created by Cristina on 03/06/17.
 */

public class TableLogs {
    //Crea una tabla de logs o registros

    private TableLayout table; // Layout donde se pintará la tabla
    private ArrayList<TableRow> rows; // Array de las filas de la tabla
    private Activity activity;
    private Resources rs;
    private Context context;
    private int ROWS, COLUMNS; // Filas y columnas de nuestra tabla
    private Display display;

    public TableLogs(Activity activity, TableLayout table, Context context)
    {
        this.activity = activity;
        this.table = table;
        rs = this.activity.getResources();
        ROWS = COLUMNS = 0;
        rows = new ArrayList<TableRow>();
        this.context = context;
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        this.display = wm.getDefaultDisplay();
    }

    //Función que añade la cabecera de la tabla
    public void addHeader(int headerResource)
    {
        TableRow.LayoutParams layoutCelda;
        TableRow fila = new TableRow(activity);
        TableRow.LayoutParams layoutFila = new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.MATCH_PARENT);
        fila.setLayoutParams(layoutFila);

        String[] arraycabecera = rs.getStringArray(headerResource);
        COLUMNS = arraycabecera.length;

        for(int i = 0; i < arraycabecera.length; i++)
        {
            TextView texto = new TextView(activity);

            texto.setText(arraycabecera[i]);
            texto.setGravity(Gravity.CENTER);
            layoutCelda = new TableRow.LayoutParams(display.getWidth()/3, TableRow.LayoutParams.WRAP_CONTENT);
            texto.setLayoutParams(layoutCelda);
            texto.setTextColor(Color.WHITE);
            texto.setTextSize(16);
            fila.addView(texto);
        }
        fila.setPadding(12, 20, 12, 20);
        table.addView(fila);
        rows.add(fila);

        ROWS++;
    }

    //Función que añade filas a la tabla con los datos del array list que se le pasa como parámetro
    public void addRowTable(ArrayList<String> elementos)
    {
        TableRow.LayoutParams layoutCelda;
        TableRow.LayoutParams layoutFila = new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.MATCH_PARENT);
        TableRow fila = new TableRow(activity);
        fila.setLayoutParams(layoutFila);
        //Si el registro es un pago se representará de color rojo y si es un ingreso, con color verde
        int color = context.getResources().getColor(R.color.colorTeja);
        if (elementos!=null){
            if (elementos.get(2).contains(context.getString(R.string.plusSymbol))) color = context.getResources().getColor(R.color.colorGreenApp);
            for(int i = 0; i< elementos.size(); i++)
            {
                TextView texto = new TextView(activity);
                texto.setText(String.valueOf(elementos.get(i)));
                texto.setGravity(Gravity.CENTER);
                layoutCelda = new TableRow.LayoutParams(display.getWidth()/3, TableRow.LayoutParams.WRAP_CONTENT);
                texto.setLayoutParams(layoutCelda);
                texto.setTextColor(color);
                fila.setPadding(12, 20, 12, 20);
                texto.setTextSize(14);
                texto.setTypeface(texto.getTypeface(), Typeface.BOLD);
                fila.addView(texto);
            }
            fila.setBackgroundColor(Color.WHITE);
            table.addView(fila);
            rows.add(fila);

            ROWS++;
        }
    }

}
