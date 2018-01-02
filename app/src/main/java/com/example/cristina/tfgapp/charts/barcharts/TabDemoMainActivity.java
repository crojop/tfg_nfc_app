package com.example.cristina.tfgapp.charts.barcharts;

import android.graphics.Paint;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TabHost;
import android.widget.TableLayout;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.cristina.tfgapp.controller_view.UtilsDate;
import com.example.cristina.tfgapp.controller_view.login.LoginActivity;
import com.example.cristina.tfgapp.controller_view.logs.Logs;
import com.example.cristina.tfgapp.controller_view.logs.TableLogs;
import com.example.cristina.tfgapp.controller_view.MainActivity;
import com.example.cristina.tfgapp.controller_view.menus.MenuActivity;
import com.example.cristina.tfgapp.singleton.MyRequestQueueSingleton;
import com.example.cristina.tfgapp.R;
import com.example.cristina.tfgapp.model.EventU;
import com.example.cristina.tfgapp.model.TagU;
import com.example.cristina.tfgapp.model.TerminalU;
import com.example.cristina.tfgapp.model.TransactionU;

import org.achartengine.ChartFactory;
import org.achartengine.chart.BarChart;
import org.achartengine.chart.LineChart;
import org.achartengine.chart.PointStyle;
import org.achartengine.model.XYMultipleSeriesDataset;
import org.achartengine.model.XYSeries;
import org.achartengine.renderer.XYMultipleSeriesRenderer;
import org.achartengine.renderer.XYSeriesRenderer;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;

import static android.content.ContentValues.TAG;

public class TabDemoMainActivity extends MenuActivity {

    //URL de get transacciones
    private static final String URL_GET_TRANSACTIONS = "http://vpayment.perentec.com/API/V1/transactions?limit=100000&terminal_serial_number=";
    //Código de éxito
    private static final String SUCCESS_CODE = "000";
    //Parte del query string correspondiente al código del tag
    private static final String QUERY_STRING_TAG = "&tag_code=";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transactions);
        //(Método heredado de MenuActivity) añade título e imagen a la app bar con el nombre de usuario y el icono de usuario validado
        initializeTagAppBar();
        getStats();
        TableLogs table = new TableLogs(TabDemoMainActivity.this, (TableLayout)findViewById(R.id.tabla),
                this);
        Logs userRegisters = new Logs(table, this, true);
        tabs();
    }

    private void tabs (){
        final TabHost tabs = (TabHost)findViewById(android.R.id.tabhost);
        tabs.setup();

        TabHost.TabSpec spec;

        spec = tabs.newTabSpec(getString(R.string.nameTab1));
        spec.setIndicator(getString(R.string.registers), getResources().getDrawable(android.R.drawable.ic_btn_speak_now));
        spec.setContent(R.id.tab1);
        tabs.addTab(spec);

        spec = tabs.newTabSpec(getString(R.string.nameTab2));
        spec.setIndicator(getString(R.string.statistics), getResources().getDrawable(android.R.drawable.ic_dialog_map));
        spec.setContent(R.id.tab2);
        tabs.addTab(spec);

        tabs.setCurrentTab(0);
    }

    /*
    Método que realiza el GET de todas las transacciones realizadas por el usuario actual en este número de terminal
    */
    protected void getStats (){
        JsonObjectRequest jsArrayRequest = new JsonObjectRequest(
                Request.Method.GET,
                URL_GET_TRANSACTIONS+ LoginActivity.terminalU.getTerminal_serial_number()+QUERY_STRING_TAG+ MainActivity.currentTag.getTag_code(),
                null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        //Ha habido éxito
                        gatherData(response);
                        getMaxY();
                        if (arrayDates.length!=0)
                            drawChart();
                        else setContentView(R.layout.nodata_layout);
                        pDialog.dismiss(); //que no se cierre el diálogo to do el rato y se abra
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //No ha habido éxito
                        pDialog.dismiss(); //que no se cierre el diálogo to do el rato y se abra
                        Log.d(getString(R.string.error), getString(R.string.errorJsonResponse) + error.getMessage());
                    }
                }
        );
        MyRequestQueueSingleton.getInstance(this).addToRequestQueue(jsArrayRequest);
        pDialog.show();
    }

    /*
    Método que gestiona el objeto json que contiene todas las transacciones devueltas.
    1. Crea un array list transacciones donde introduce los objetos transacciones que obtiene del array data json.
    2. Una vez ha metido todas las transacciones en el array list, crea un array de transacciones a partir del array list.
    Esto lo hace para poder ordenar las transacciones por fecha con Array.sort
    3. Recorre este array de transacciones ya ordenado para generar el array list de objetos DateElement. Cada objeto DateElement
    tiene una fecha y un valor numérico asociado, que en este caso es la cantidad de la transacción (con signo negativo si se trata de un
    pago y con signo positivo si se trata de una recarga).
    4. Se llama a la función orderByDayD que ordena el array list de DateElement
    */
    protected void gatherData (JSONObject jsonObject){
        try {
            //Si to do ha ido bien
            if (jsonObject.getString(getString(R.string.code)).equals(SUCCESS_CODE)) {
                JSONArray jsonArray = jsonObject.getJSONArray(getString(R.string.data));
                ArrayList<TransactionU> transactionUArrayList = new ArrayList<TransactionU>(){};
                ArrayList<DateElement> dateElementArrayList = new ArrayList<DateElement>(){};
                for(int i=0; i<jsonArray.length(); i++){
                    try {
                        JSONObject objeto= jsonArray.getJSONObject(i);
                        EventU eventU = new EventU(objeto.getInt(getString(R.string.event_id)));
                        TagU tagU = new TagU(objeto.getInt(getString(R.string.tag_code)));
                        TerminalU terminalU = new TerminalU(objeto.getInt(getString(R.string.terminal_serial_number)));
                        TransactionU transactionU = new TransactionU(objeto.getInt(getString(R.string.transactiontype_id)),
                                objeto.getDouble(getString(R.string.transaction_amount)),eventU,tagU,terminalU);
                        transactionU.setDateInMilliseconds(UtilsDate.stringToMillisecondsNew(objeto.getString(getString(R.string.transaction_date)), UtilsDate.PATTERN_DATE_WEB_SERVICE));
                        transactionUArrayList.add(transactionU);
                    } catch (JSONException e) {
                        Log.e(TAG, getString(R.string.parsingError)+ e.getMessage());
                    }
                }
                TransactionU[] arrayTransactionsU = transactionUArrayList.toArray(new TransactionU[transactionUArrayList.size()]);
                Arrays.sort(arrayTransactionsU);
                for (TransactionU transactionU : arrayTransactionsU){
                    double amount = (transactionU.getTransactiontype_id()==TransactionU.TRANSACTION_PAYMENT) ? -transactionU.getTransaction_amount() : transactionU.getTransaction_amount();
                    DateElement dateElement = new DateElement(UtilsDate.chainDateShort(transactionU.getDateInMilliseconds()), amount);
                    dateElementArrayList.add(dateElement);
                }
                orderByDayD(dateElementArrayList);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /*
    Esta función se encarga de crear un array list de objetos DateElement, que tienen un atributo de tipo String donde almacena el día o la fecha, y otro atributo
    de tipo numérico que almacena el dinero acumulado ese día en transacciones.
    De tal manera que por ejemplo un objeto de este array podría tener como atributos titleDate 9 y num 456.

    Recorre el arraylist de DateElement que le llega como parámetro, juntando todos los objetos que los componen por misma fecha,
    sumando y restando su cantidad de euros asociada (si es una recarga se suma al num del objeto DateElement, y si es de tipo pago, la cantidad asociada
    se resta al num del objeto DateElement).

    Al final se ordena el array en orden decreciente en función de la fecha
     */
    protected void orderByDayD (ArrayList<DateElement> dateElementArrayList){
        ArrayList<DateElement> auxArrayList = new ArrayList<DateElement>(){};
        for (DateElement dateElement : dateElementArrayList){
            boolean founded = false;
            for (DateElementIterator iterator = new DateElementIterator(auxArrayList); iterator.hasNext(); ) {
                DateElement dateElementAux = iterator.next();
                if (dateElement.getTitleDate().equals(dateElementAux.getTitleDate())) {
                    founded = true;
                    DateElement de = new DateElement(dateElement.getTitleDate(), dateElementAux.getNum()+dateElement.getNum());
                    auxArrayList.remove(dateElementAux);
                    auxArrayList.add(de);
                }
            }
            if (!founded) {
                DateElement de = new DateElement(dateElement.getTitleDate(), dateElement.getNum());
                auxArrayList.add(de);
            }
        }
        arrayDates = auxArrayList.toArray(new DateElement[auxArrayList.size()]);
        Arrays.sort(arrayDates);
    }

    /*
    Función que crea el XYSeries con los datos a representar
     */
    protected XYSeries createXYSeries (String graph_title){
        XYSeries expenseSeries = new XYSeries(graph_title);
        for (int i = 0; i < arrayDates.length; i++) {
            //Se redondea a dos decimales el valor antes de representarlo
            expenseSeries.add(i, (Math.round((arrayDates[i].getNum()) * 100.0) / 100.0));
        }
        return expenseSeries;
    }

    /*
    Función que dibuja el gráfico
     */
    protected void drawChart(){
        // Creating an  XYSeries for Expense
        XYSeries expenseSeriesBar = createXYSeries(getString(R.string.titleGraph_balanceMovements));
        XYSeries expenseSeries = createXYSeries(getString(R.string.titleGraph_balanceMovements));

        // Creating a dataset to hold each series
        XYMultipleSeriesDataset xyMultipleSeriesDataset = new XYMultipleSeriesDataset();
        // Adding Expense Series to the dataset

        xyMultipleSeriesDataset.addSeries(expenseSeriesBar);
        xyMultipleSeriesDataset.addSeries(expenseSeries);

        // Creating XYSeriesRenderer to customize expenseSeries
        XYSeriesRenderer renderer = createXYSeriesRenderer(R.color.colorAccent);
        XYSeriesRenderer renderer_bar = createXYSeriesRenderer(R.color.colorIndigo);

        renderer.setDisplayChartValues(true);
        renderer.setPointStyle(PointStyle.CIRCLE);
        renderer.setChartValuesTextSize(14);
        renderer.setChartValuesTextAlign(Paint.Align.CENTER);
        renderer.setDisplayChartValuesDistance(26);
        renderer.setLineWidth(2);
        renderer.setShowLegendItem(false);

        renderer_bar.setShowLegendItem(false);
        renderer_bar.setDisplayChartValues(false);

        // Creating a XYMultipleSeriesRenderer to customize the whole chart
        XYMultipleSeriesRenderer multiRenderer = createXYmultipleSeriesRenderer(getString(R.string.titleGraph_balanceMovements)+" ("+getString(R.string.euro_divisa_symbol)+")");
        //Tamaño y estilo del texto
        //multiRenderer.setAxisTitleTextSize(20);
        multiRenderer.setTextTypeface("sans-serif-smallcaps", Typeface.BOLD);
        //Barras
        multiRenderer.setClickEnabled(false);
        multiRenderer.setBarSpacing(2);
        multiRenderer.setBarWidth(44);
        multiRenderer.setPointSize(2); //Tamaño del punto
        multiRenderer.setZoomEnabled(true);
        // Adding expenseRenderer to multipleRenderer
        multiRenderer.addSeriesRenderer(renderer);
        multiRenderer.addSeriesRenderer(renderer_bar);
        multiRenderer.setInScroll(true);

        //Zoom
        multiRenderer.setZoomEnabled(true);
        multiRenderer.setExternalZoomEnabled(true);
        multiRenderer.setPanEnabled(true, true); //Permitir desplazamiento sobre los ejes X e Y

        //setting the margin size for the graph in the order top, left, bottom, right
        multiRenderer.setMargins(new int[]{60, 50, 12, 20});

        // Getting a reference to LinearLayout of the MainActivity Layout
        LinearLayout chartContainer = (LinearLayout) findViewById(R.id.chart_container);

        // Creating a Line Chart
        View chart = ChartFactory.getCombinedXYChartView(getBaseContext(), xyMultipleSeriesDataset,
                multiRenderer, new String[] { LineChart.TYPE, BarChart.TYPE });

        // Adding the Line Chart to the LinearLayout
        chartContainer.addView(chart);
    }

}
