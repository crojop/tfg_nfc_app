package com.example.cristina.tfgapp.singleton;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.cristina.tfgapp.R;


/**
 * Created by Cristina on 02/12/17.
 */

public final class MyToastSingleton extends Toast {
    /*
    Utilizamos el patrón singleton para el toast personalizado que utilizaremos en toda la aplicación.
    Limita el alcance de la clase a un solo objeto, restringiendo la instanciación de nuevos elementos.
     */
    private TextView textViewMyToast;
    private ImageView imageViewMyToast;
    private static MyToastSingleton ourInstance;

    public static MyToastSingleton getInstance(Context context, Activity activity) {
        if (ourInstance == null) {
            ourInstance = new MyToastSingleton(context, activity);
        }
        return ourInstance;
    }

    private MyToastSingleton(Context context, Activity activity) {
        super(context);
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View layout = inflater.inflate(R.layout.toast_layout, (ViewGroup) activity.findViewById(R.id.lytLayout));
        textViewMyToast = (TextView) layout.findViewById(R.id.txtMensaje);
        imageViewMyToast = (ImageView) layout.findViewById(R.id.imgIcono);
        this.setDuration(Toast.LENGTH_SHORT);
        this.setView(layout);
    }

    //En el caso de que sea un mensaje de validación o éxito, se utilizará el logo verde con el tick
    public void setSuccess(String textSuccess) {
        this.textViewMyToast.setText(textSuccess);
        this.imageViewMyToast.setImageResource(R.drawable.success_32);
        this.show();
    }

    //En el caso de que sea un mensaje de error, se utilizará el logo rojo con la x
    public void setError(String textError) {
        this.textViewMyToast.setText(textError);
        this.imageViewMyToast.setImageResource(R.drawable.error_32);
        this.show();
    }
}