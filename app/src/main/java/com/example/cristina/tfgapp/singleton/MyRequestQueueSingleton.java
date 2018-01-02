package com.example.cristina.tfgapp.singleton;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

/**
 * Created by Cristina on 15/11/17.
 */

public final class MyRequestQueueSingleton {
    /*
    Utilizamos el patrón singleton para la cola de peticiones.
    Limita el alcance de la clase a un solo objeto, restringiendo la instanciación de nuevos elementos.
     */
    private RequestQueue requestQueue;
    private static Context context;
    private static MyRequestQueueSingleton ourInstance;

    public static synchronized MyRequestQueueSingleton getInstance(Context context) {
        if (ourInstance == null) {
            ourInstance = new MyRequestQueueSingleton(context);
        }
        return ourInstance;
    }

    private MyRequestQueueSingleton(Context context) {
        MyRequestQueueSingleton.context = context;
        requestQueue = getRequestQueue();
    }

    public RequestQueue getRequestQueue() {
        if (requestQueue == null) {
            requestQueue = Volley.newRequestQueue(context.getApplicationContext());
        }
        return requestQueue;
    }

    public void addToRequestQueue(Request req) {
        getRequestQueue().add(req);
    }

}