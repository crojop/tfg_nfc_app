package com.example.cristina.tfgapp.model;

import android.content.Context;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.cristina.tfgapp.R;
import com.example.cristina.tfgapp.controller_view.MainActivity;
import com.example.cristina.tfgapp.singleton.MyRequestQueueSingleton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import static android.content.ContentValues.TAG;

/**
 * Created by Cristina on 16/11/17.
 */

public class ProductsWS {

    private Context context;
    private final String URL_PRODUCTS = "https://api.myjson.com/bins/lgwtn";

    /*
    Método que carga los productos del web service de productos y los inserta en la base de datos
     */
    public void chargeProducts (final Context context) {
        this.context=context;
        JsonObjectRequest jsArrayRequest;
        jsArrayRequest = new JsonObjectRequest(
                Request.Method.GET,
                URL_PRODUCTS,
                null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        ArrayList<Product> products = parseJson(response);
                        MainActivity.prodDataBaseHelper.insertProducts(products);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d(TAG, context.getString(R.string.errorJsonResponse) + error.getMessage());
                    }
                }
        );
        MyRequestQueueSingleton.getInstance(context).addToRequestQueue(jsArrayRequest);
    }

    /*
    Función a la que se le pasa un objeto json con todos los productos devueltos por el método get
    y los mete en el array list que devuelve como respuesta
     */
    public ArrayList<Product> parseJson(JSONObject jsonObject){
        ArrayList<Product> productsAr = new ArrayList();
        JSONArray jsonArray;
        try {
            jsonArray = jsonObject.getJSONArray(context.getString(R.string.json_products));
            for(int i=0; i<jsonArray.length(); i++){
                try {
                    JSONObject objeto= jsonArray.getJSONObject(i);
                    Product product = new Product(
                            objeto.getInt(context.getString(R.string.json_id_product)),
                            objeto.getString(context.getString(R.string.json_name)),
                            objeto.getDouble(context.getString(R.string.json_price)));
                    product.setImageCart(R.drawable.added_cart_32);
                    productsAr.add(product);

                } catch (JSONException e) {
                    Log.e(TAG, context.getString(R.string.parsingError)+ e.getMessage());
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return productsAr;
    }

}
