package com.example.cristina.tfgapp.controller_view;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by Cristina on 05/12/17.
 */

public class UtilsDate {
    public static final String PATTERN_DATE_WEB_SERVICE = "yyyy-MM-dd'T'HH:mm:ss'.000Z'";
    /*
    Esta clase contiene métodos personalizados para manejar y convertir fechas, que pueden ser
    utilizados desde cualquier parte de la aplicación
    */

    //Este método convierte una fecha dada en milisegundos a un string que se corresponde con
    // dicha fecha en el formato o patrón introducido por parámetro
    public static String urMillscsToDateFormatUWant(long milliSeconds, String dateFormat)
    {
        SimpleDateFormat formatter = new SimpleDateFormat(dateFormat);
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(milliSeconds);
        return formatter.format(calendar.getTime());
    }

    /*
    Este método convierte una fecha de tipo string a milisegundos. El parámetro pattern indica el patrón del string fecha.
    "yyyy-MM-dd'T'HH:mm:ss'.000Z'"
     */
    public static long stringToMillisecondsNew (String dateString, String pattern){
        Calendar fecha_calendar = Calendar.getInstance();
        SimpleDateFormat formatter = new SimpleDateFormat(pattern);
        try {
            Date fecha_date = formatter.parse(dateString);
            fecha_calendar.setTime(fecha_date);
            return fecha_calendar.getTimeInMillis();
        } catch (ParseException e) {
            e.printStackTrace();
            return 0;
        }
    }

    /*
    Este método convierte una fecha en milisegundos en una cadena de caracteres de formato dd/mm
     */
    public static String chainDateShort (long miliseconds){
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(miliseconds);
        String day_of_month = String.valueOf(calendar.get(Calendar.DATE));
        String month = String.valueOf(calendar.get(Calendar.MONTH)+1);
        String chain_date = day_of_month + "/" + month;
        return chain_date;
    }
}
