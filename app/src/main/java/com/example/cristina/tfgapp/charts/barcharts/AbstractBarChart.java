package com.example.cristina.tfgapp.charts.barcharts;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.cristina.tfgapp.controller_view.MainActivity;
import com.example.cristina.tfgapp.singleton.MyRequestQueueSingleton;
import com.example.cristina.tfgapp.R;
import com.example.cristina.tfgapp.model.Statistic;
import com.example.cristina.tfgapp.model.TransactionU;
import com.example.cristina.tfgapp.controller_view.Utils;
import com.example.cristina.tfgapp.charts.AbstractChart;
import com.example.cristina.tfgapp.singleton.MyToastSingleton;

import org.achartengine.ChartFactory;
import org.achartengine.GraphicalView;
import org.achartengine.chart.BarChart;
import org.achartengine.model.SeriesSelection;
import org.achartengine.model.XYMultipleSeriesDataset;
import org.achartengine.model.XYSeries;
import org.achartengine.renderer.XYMultipleSeriesRenderer;
import org.achartengine.renderer.XYSeriesRenderer;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.concurrent.Callable;

import static android.content.ContentValues.TAG;

/**
 * Created by Cristina on 18/10/17.
 */

public abstract class AbstractBarChart extends AbstractChart {
    protected ArrayList<DateElement> arrayListDates;
    protected final String QUERY_STRING_TER_NUM = "?terminal_serial_number=";
    protected DateElement[] arrayDates;
    public GraphicalView gvchart;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_barchart);
        arrayListDates = new ArrayList<DateElement>();
    }

    /*
    Función que devuelve el valor máximo que alcanzará el eje y. Para ello recorre todos los valores almacenados
    en el array de DateElements.
     */
    protected void getMaxY() {
        maxY = 0.0;
        for (DateElement de : arrayDates) if (maxY < de.getNum()) maxY = de.getNum();
    }

    /*
    Función que crea y customiza el XYSeriesRenderer
     */
    protected XYSeriesRenderer createXYSeriesRenderer(int colorGraph) {
        XYSeriesRenderer renderer = new XYSeriesRenderer();
        renderer.setColor(ContextCompat.getColor(this, colorGraph));
        renderer.setFillPoints(true);
        renderer.setLineWidth(3);
        renderer.setChartValuesTextAlign(Paint.Align.RIGHT);
        renderer.setDisplayChartValues(true);
        return renderer;
    }

    /*
    Función que crea y customiza el XYMultipleSeriesRenderer
    */
    protected XYMultipleSeriesRenderer createXYmultipleSeriesRenderer(String chart_title) {
        XYMultipleSeriesRenderer multiRenderer = super.createXYmultipleSeriesRenderer(chart_title);

        //Click
        multiRenderer.setClickEnabled(true);

        //Barras
        multiRenderer.setBarWidth(22);

        //X
        multiRenderer.setXAxisMin(-1);
        multiRenderer.setXAxisMax(arrayDates.length);
        multiRenderer.setXLabels(0);
        multiRenderer.setXLabelsColor(Color.DKGRAY);

        for (int i = 0; i < arrayDates.length; i++)
            multiRenderer.addXTextLabel(i, arrayDates[i].getTitleDate());

        return multiRenderer;
    }

    /*
    Función que crea y customiza el XYSeries. Inicializa el valor de cada punto x e y con los
    valores almacenados en arrayDates
     */
    protected XYSeries createXYSeries(String graph_title) {
        XYSeries expenseSeries = new XYSeries(graph_title);
        for (int i = 0; i < arrayDates.length; i++) expenseSeries.add(i, arrayDates[i].getNum());
        return expenseSeries;
    }

    /**
     * Función que dibuja el gráfico pasándole los parámetros a getChart
     */
    protected void drawChart(String chart_title, int colorGraph, String graph_title, final Class intentClass) {
        super.drawChart(chart_title, colorGraph, graph_title, true);
        chartContainer.addView(getChart(dataset, multiRenderer, intentClass));
    }

    /*
    Función que devuelve el objeto gráfico customizado
     */
    protected View getChart(XYMultipleSeriesDataset dataset, final XYMultipleSeriesRenderer multiRenderer, final Class intentClass) {
        gvchart = ChartFactory.getBarChartView(getBaseContext(), dataset, multiRenderer, BarChart.Type.DEFAULT);
        gvchart.setClickable(true);
        gvchart.setOnClickListener((new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SeriesSelection seriesSelection = gvchart.getCurrentSeriesAndPoint();
                if (seriesSelection != null) {
                    Intent intentFinancialLineChart = new Intent(getApplicationContext(), intentClass);
                    intentFinancialLineChart.putExtra(getResources().getString(R.string.DAY),
                            multiRenderer.getXTextLabel(seriesSelection.getXValue()));
                    startActivity(intentFinancialLineChart);
                } /*Cuando el usuario haga click en una barra del gráfico, es decir, a los valores correspondientes a un día del evento en concreto,
                * se le abrirá el line chart correspondiente a ese día para visualizar la información por horas.
                * Con el putExtra se le pasa el día concreto a visualizar.
                */
            }
        }));
        return gvchart;
    }

    /**
     * Clase DateElement cuyos atributos son una fecha y los ingresos asociados
     */
    protected class DateElement implements Comparable<DateElement> {
        protected String date;
        protected double num;

        protected DateElement(String d, double n) {
            date = d;
            num = n;
        }

        public void setDate(String date) {
            this.date = date;
        }

        protected String getTitleDate() {
            return date;
        }

        protected double getNum() {
            return num;
        }

        protected void setNum(double n) {
            num = n;
        }

        @Override
        public int compareTo(@NonNull DateElement o) {
            if (Utils.stringToMillisecondsNew(date, getPattern()) < Utils.stringToMillisecondsNew(o.date, getPattern())) {
                return -1;
            }
            if (Utils.stringToMillisecondsNew(date, getPattern()) > Utils.stringToMillisecondsNew(o.date, getPattern())) {
                return 1;
            }
            return 0;
        }
    }

    /*
    Para poder añadir y borrar elementos de un array lists que estamos recorriendo.
    Si no hiciéramos esto saltaría error porque no podríamos recorrer un array que está siendo editado a la vez
     */
    public class DateElementIterator implements Iterator<DateElement> {

        private final ArrayList<DateElement> dateElementArrayList;
        private int index;

        public DateElementIterator(ArrayList<DateElement> arrayList) {
            this.dateElementArrayList = arrayList;
            index = 0;
        }

        @Override
        public DateElement next() {
            if (hasNext()) {
                return dateElementArrayList.get(index++);
            } else {
                throw new NoSuchElementException(getString(R.string.noSuchElemExcep_message));
            }
        }

        @Override
        public boolean hasNext() {
            return (dateElementArrayList.size() != index);
        }

        @Override
        public void remove() {
            if (index <= 0) {
                throw new IllegalStateException(getString(R.string.illStateExcep_message));
            }
            dateElementArrayList.remove(--index);
        }
    }

    //Devuelve el patrón que usa para la fecha
    protected abstract String getPattern();

    protected void getStats (final String graph_title, final int graph_color, final String graph_legend, final Class line_chart_class){
        JsonObjectRequest jsArrayRequest = new JsonObjectRequest(
                Request.Method.GET,
                getString(R.string.URL_STATS)+QUERY_STRING_TER_NUM+ MainActivity.terminalU.getTerminal_serial_number()+getQueryStringCont()+getTokenCont(),
                null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        /*response aquí contiene todas las estadísticas para ese número de terminal, y si getQueryCont() contuviera algo,
                        también para ese tipo de transacción; pago o recarga */
                        if (gatherData(response).equals(getString(R.string.VAL_SUCCESS))){
                            getMaxY(); //Calcula el mayor valor de y que se va a representar (para generar la altura del eje)
                            normalize();
                        } else if (gatherData(response).equals(getString(R.string.VAL_ERROR))){
                            MyToastSingleton.getInstance(AbstractBarChart.this).setError(getString(R.string.error_charging_stats));
                        }
                        //Si hay info, se genera el gráfico, si no, se muestra pantalla de "No hay datos"
                        if (arrayDates.length!=0)
                            drawChart(graph_title, graph_color, graph_legend, line_chart_class);
                        else setContentView(R.layout.nodata_layout);
                        pDialog.dismiss(); //Se cierra diálogo de cargando..
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d(getString(R.string.error), getString(R.string.errorJsonResponse) + error.getMessage());
                        if (error.getClass().equals(AuthFailureError.class)) {
                            Utils.changeToken(AbstractBarChart.this, new Callable<String>() {
                                public String call() {
                                    getStats(graph_title, graph_color, graph_legend, line_chart_class);
                                    return null;
                                }
                            });
                        } else pDialog.dismiss(); //que no se cierre el diálogo to do el rato y se abra
                    }
                }
        );
        MyRequestQueueSingleton.getInstance(this).addToRequestQueue(jsArrayRequest);
        pDialog.show();
    }

    /*
    Esta función recibe por parámetro el objeto JSON con todas las estadísticas del sistema y las convierte en un array list de objetos Statistic,
    que luego le pasa a la función orderByDay para que la ordene por horas
     */
    protected String gatherData (JSONObject jsonObject){
        String result = getString(R.string.VAL_ERROR);
        try {
            if (jsonObject.getString(getString(R.string.code)).equals(getString(R.string.CODE_SUCCESSS))) {
                JSONArray jsonArray = jsonObject.getJSONArray(getString(R.string.data));
                ArrayList<Statistic> statisticArrayList = new ArrayList<Statistic>(){};
                for(int i=0; i<jsonArray.length(); i++){
                    try {
                        JSONObject objeto= jsonArray.getJSONObject(i);
                        String day = String.valueOf(objeto.getInt(getString(R.string.day)));
                        int transactiontype_id = objeto.getInt(getString(R.string.transactiontype_id));
                        double total_amount = (transactiontype_id== TransactionU.TRANSACTION_PAYMENT) ? objeto.getDouble(getString(R.string.total_amount)) : 0;
                        int number_of_transactions = objeto.getInt(getString(R.string.number_of_transactions));
                        Statistic statistic = new Statistic(day, transactiontype_id, number_of_transactions);
                        statistic.setTotal_amount(total_amount);
                        statisticArrayList.add(statistic);
                        result = getString(R.string.VAL_SUCCESS);
                    } catch (JSONException e) {
                        Log.e(TAG, getString(R.string.parsingError)+ e.getMessage());
                    }
                }
                orderByDay(statisticArrayList);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return result;
    }

    /*
    Esta función se encarga de crear un array list de objetos DateElement, que tienen un atributo de tipo String donde almacena el día o la fecha, y otro atributo
    de tipo numérico que almacena o bien el dinero acumulado ese día entre pagos y recargas, o el número de transacciones de un tipo (pago
    o recarga) ese día.
    De tal manera que por ejemplo un objeto de este array podría tener como atributos titleDate 9 y num 456.

    Este proceso lo hace recorriendo el array de estadísticas que le llega de gatherData, juntando todos los objetos de ese arraylist por misma fecha,
    sumando y restando su cantidad de euros asociada (si es una recarga se suma al num del objeto DateElement, y si es de tipo pago, la cantidad asociada
    se resta al num del objeto DateElement).

    Al final se ordena el array en orden decreciente en función de la fecha
     */
    protected void orderByDay(ArrayList<Statistic> statisticArrayList) {
        ArrayList<DateElement> auxArrayList = new ArrayList<DateElement>() {
        };
        for (Statistic statistic : statisticArrayList) {
            boolean founded = false;
            for (DateElementIterator iterator = new DateElementIterator(auxArrayList); iterator.hasNext(); ) {
                DateElement dateElement = iterator.next();
                if (statistic.getDay().equals(dateElement.getTitleDate())) {
                    founded = true;
                    DateElement de = new DateElement(statistic.getDay(), dateElement.getNum() + getSecParamDateElement(statistic));
                    auxArrayList.remove(dateElement);
                    auxArrayList.add(de);
                }
            }
            if (!founded) {
                DateElement de = new DateElement(statistic.getDay(), getSecParamDateElement(statistic));
                auxArrayList.add(de);
            }
        }
        arrayDates = auxArrayList.toArray(new DateElement[auxArrayList.size()]);
        Arrays.sort(arrayDates);
    }

    /*
    Función que devuelve la segunda parte del query string. Está pensado para las activities que muestran la gráfica
    de número de transacciones de un determinado tipo, RechargesChart y PaymentsChart, para que devuelva sólo las transacciones
    del tipo que se esté mostrando.
    Por defecto devuelve una cadena vacía.
     */
    protected String getQueryStringCont(){
        return "";
    }

    /*
    Por defecto redondea los valores a 2 decimales
     */
    protected void normalize (){
        for (int i=0; i<arrayDates.length; i++) arrayDates[i].setNum(Math.round(arrayDates[i].getNum()*100.0)/100.0);
    }

    protected double getSecParamDateElement (Statistic statistic){
        return 0.0;
    }
}
