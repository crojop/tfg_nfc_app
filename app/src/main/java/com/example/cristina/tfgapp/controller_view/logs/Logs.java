package com.example.cristina.tfgapp.controller_view.logs;

import android.app.ProgressDialog;
import android.content.Context;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.cristina.tfgapp.controller_view.MainActivity;
import com.example.cristina.tfgapp.controller_view.Utils;
import com.example.cristina.tfgapp.singleton.MyRequestQueueSingleton;
import com.example.cristina.tfgapp.R;
import com.example.cristina.tfgapp.model.EventU;
import com.example.cristina.tfgapp.model.TagU;
import com.example.cristina.tfgapp.model.TerminalU;
import com.example.cristina.tfgapp.model.TransactionU;
import com.example.cristina.tfgapp.model.User;
import com.example.cristina.tfgapp.singleton.MyToastSingleton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.concurrent.Callable;

import static android.content.ContentValues.TAG;
import static android.content.Context.MODE_PRIVATE;

/**
 * Created by Cristina on 25/12/17.
 */

public class Logs {
    /*
    Esta clase genera los logs
     */
    private Context context;
    private TableLogs table;
    private boolean justTheUser;
    private static final String QUERY_STRING_TAG = "&tag_code=";
    private ProgressDialog pDialog; //ProgressDialog que se muestra mientras se están cargando los datos
    private static final String QUERY_STRING_TER_SER_NUMBER = "?terminal_serial_number=";

    public Logs(TableLogs table, Context context, boolean justTheUser){
        this.context = context;
        this.table = table;
        this.justTheUser = justTheUser;
        pDialog = new ProgressDialog(context);
        pDialog.setMessage(context.getString(R.string.loadingData));
        pDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        pDialog.setCancelable(true);
        table.addHeader(R.array.cabecera_tabla);
        getLogs();
    }

    //Por si se necesitara filtrar con la query string por código del tag
    protected String getQueryStringCont (){
        String queryStringCont = "";
        if (justTheUser) queryStringCont = QUERY_STRING_TAG+ MainActivity.currentTag.getTag_code();
        return queryStringCont;
    }

    //Método que devuelve todos las transacciones que se mostrarán (los logs) para dicho número de terminal
    protected void getLogs () {
        JsonObjectRequest jsArrayRequest = new JsonObjectRequest(
                Request.Method.GET,
                context.getString(R.string.URL_TRANSACTIONS)+QUERY_STRING_TER_SER_NUMBER+ MainActivity.terminalU.getTerminal_serial_number()+getQueryStringCont()+"&token="+Utils.decryptSth(context.getSharedPreferences(context.getString(R.string.shar_prefs_name), MODE_PRIVATE).getString(context.getString(R.string.shar_prefs_token), "")),
                null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        if (validateResponse(response).equals(context.getString(R.string.VAL_SUCCESS))){
                            showTableLogs(response);
                        } else if (validateResponse(response).equals(context.getString(R.string.VAL_ERROR))){
                            MyToastSingleton.getInstance(context).setError(context.getString(R.string.error_charging_stats));
                        } else if (validateResponse(response).equals(context.getString(R.string.VAL_REFRESH_TOKEN))) {
                            getLogs();
                        }
                        pDialog.dismiss(); //que no se cierre el diálogo to do el rato y se abra
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d(context.getString(R.string.error), context.getString(R.string.errorJsonResponse) + error.getMessage());
                        if (error.getClass().equals(AuthFailureError.class)) {
                            Utils.changeToken(context, new Callable<String>() {
                                public String call() {
                                    getLogs();
                                    return null;
                                }
                            });
                        } else pDialog.dismiss(); //que no se cierre el diálogo to do el rato y se abra
                    }
                }
        );
        MyRequestQueueSingleton.getInstance(context).addToRequestQueue(jsArrayRequest);
        pDialog.show();
    }
    private String validateResponse (JSONObject jsonObject){
        String result = context.getString(R.string.VAL_ERROR);
        try {
            if (jsonObject.getString(context.getString(R.string.code)).equals(context.getString(R.string.CODE_SUCCESSS))) {
                result = context.getString(R.string.VAL_SUCCESS);
            } else if (jsonObject.getString(context.getString(R.string.code)).equals(context.getString(R.string.CODE_WRONG_TOKEN))) {
                //result = Utils.changeToken(context);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return result;
    }

    /*Método que recibe un objeto json como parámetro que contiene todas las transacciones, y genera a partir
    de él un array list de transacciones */
    private void showTableLogs (JSONObject jsonObject){
        try {
            JSONArray jsonArray = jsonObject.getJSONArray(context.getString(R.string.data));
            ArrayList<TransactionU> transactionUArrayList = new ArrayList<TransactionU>(){};
            for(int i=0; i<jsonArray.length(); i++){
                try {
                    JSONObject objeto= jsonArray.getJSONObject(i);
                    EventU eventU = new EventU(objeto.getInt(context.getString(R.string.event_id)));
                    TagU tagU = new TagU(objeto.getInt(context.getString(R.string.tag_code)));
                    if (objeto.getString(context.getString(R.string.user_id))!="null") {
                        tagU.setUser(new User(objeto.getInt(context.getString(R.string.user_id))));
                        tagU.getUser().setUser_description(objeto.getString(context.getString(R.string.user_description)));
                    }
                    TerminalU terminalU = new TerminalU(objeto.getInt(context.getString(R.string.terminal_serial_number)));
                    TransactionU transactionU = new TransactionU(objeto.getInt(context.getString(R.string.transactiontype_id)),
                            objeto.getDouble(context.getString(R.string.transaction_amount)),eventU,tagU,terminalU);
                    transactionU.setDateInMilliseconds(Utils.stringToMillisecondsNew(objeto.getString(context.getString(R.string.transaction_date)), Utils.PATTERN_DATE_WEB_SERVICE));
                    transactionUArrayList.add(transactionU);
                } catch (JSONException e) {
                    Log.e(TAG, context.getString(R.string.parsingError)+ e.getMessage());
                }
            }
            TransactionU[] arrayTransactionsU = transactionUArrayList.toArray(new TransactionU[transactionUArrayList.size()]);
            Arrays.sort(arrayTransactionsU); //se ordenan las transacciones de más recientes a más antiguas
            elementos(arrayTransactionsU);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /*
    Este método recibe el array de transacciones ya ordenado y al objeto TableLogs le pasa los array list
    que generarán cada fila de la tabla
     */
    private void elementos (TransactionU[] arrayTransactions){
        for (TransactionU transactionU : arrayTransactions){
            ArrayList<String> elementos = new ArrayList<String>() {};
            String sign = (transactionU.getTransactiontype_id()==TransactionU.TRANSACTION_RECHARGE) ? context.getString(R.string.plusSymbol) : context.getString(R.string.minusSymbol);
            String desc = "";
            if (transactionU.getTagU().getUser()!=null) desc = transactionU.getTagU().getUser().getUser_description();
            elementos.add(desc);
            elementos.add(Utils.urMillscsToDateFormatUWant(
                    transactionU.getDateInMilliseconds(), context.getString(R.string.pattern_logs)));
            elementos.add(sign + transactionU.getTransaction_amount() + " " + context.getString(R.string.euro_divisa_symbol));
            table.addRowTable(elementos);
        }
    }
}
