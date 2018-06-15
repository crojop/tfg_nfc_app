package com.example.cristina.tfgapp.singleton;

import android.content.Context;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.example.cristina.tfgapp.R;
import com.example.cristina.tfgapp.controller_view.Utils;

import java.util.HashMap;

import static android.content.Context.MODE_PRIVATE;

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


    public void addToRequestQueue(Request req){
      getRequestQueue().add(req);
    }

}