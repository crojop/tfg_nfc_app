package com.example.cristina.tfgapp.controller_view;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Base64;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.cristina.tfgapp.R;
import com.example.cristina.tfgapp.singleton.MyRequestQueueSingleton;
import com.example.cristina.tfgapp.singleton.MyToastSingleton;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidParameterSpecException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.Callable;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by Cristina on 05/12/17.
 */

public class Utils {
    public static final String PATTERN_DATE_WEB_SERVICE = "yyyy-MM-dd'T'HH:mm:ss'.000Z'";
    public static String result;
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

    private static final String KEY = "1Hbfh667adfDEJ78";
    public static SecretKey generateKey() throws Exception
    {
        SecretKey key = new SecretKeySpec(KEY.getBytes(), "AES");
        return key;
    }

    public static byte[] encryptToByte(String message, SecretKey secret) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, InvalidParameterSpecException, IllegalBlockSizeException, BadPaddingException, UnsupportedEncodingException
    {
        Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
        cipher.init(Cipher.ENCRYPT_MODE, secret);
        byte[] cipherText = cipher.doFinal(message.getBytes("UTF-8"));
        return cipherText;
    }

    public static String decryptByte(byte[] cipherText, SecretKey secret) throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidParameterSpecException, InvalidAlgorithmParameterException, InvalidKeyException, BadPaddingException, IllegalBlockSizeException, UnsupportedEncodingException
    {
    /* Decrypt the message, given derived encContentValues and initialization vector. */
        Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
        cipher.init(Cipher.DECRYPT_MODE, secret);
        String decryptString = new String(cipher.doFinal(cipherText), "UTF-8");
        return decryptString;
    }

    public static boolean isConnected (Context context){
        ConnectivityManager manager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = manager.getActiveNetworkInfo();
        boolean isAvailable = false;
        if (networkInfo != null && networkInfo.isConnected()) {
            // Network is present and connected
            isAvailable = true;
        }
        return isAvailable;
    }

    public static String decryptSth (String sth_encrypted){
        String sth_decrypted = "";
        if (sth_encrypted != null) {
            byte[] bytesArray = Base64.decode(sth_encrypted, Base64.DEFAULT);
            try {
                sth_decrypted = Utils.decryptByte(bytesArray, Utils.generateKey());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return sth_decrypted;
    }

    public static String changeToken(final Context context, final Callable<String> func){
        SharedPreferences loginPreferences = context.getSharedPreferences(context.getString(R.string.shar_prefs_name), MODE_PRIVATE);
        int user = loginPreferences.getInt(context.getString(R.string.shar_prefs_terminal_serial_number), 0);
        String encrypted_password = loginPreferences.getString(context.getString(R.string.shar_prefs_password), "");
        JSONObject request = new JSONObject();
        try
        {
            request.put(context.getString(R.string.ws_user), user);
            request.put(context.getString(R.string.ws_password), Utils.decryptSth(encrypted_password));
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
        JsonObjectRequest postRequest = new JsonObjectRequest(Request.Method.POST, context.getString(R.string.URL_AUTHENTICATE), request,
                new Response.Listener<JSONObject>()
                {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            login(response, context, func);
                        } catch (Exception e) {
                            e.printStackTrace();
                            MyToastSingleton.getInstance(context).setError(context.getString(R.string.error_authentication));
                        }
                    }
                },
                new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d(context.getString(R.string.error), context.getString(R.string.errorJsonResponse));
                    }
                }
        );
        MyRequestQueueSingleton.getInstance(context).addToRequestQueue(postRequest);
        return null;
    }

    public static String login (JSONObject jsonObject, Context context, Callable<String> func) throws Exception {
        try {
            boolean error = jsonObject.getBoolean(context.getString(R.string.error));
            if (!error){
                String token= jsonObject.getString(context.getString(R.string.ws_token));
                byte[] encrypted_token = Utils.encryptToByte(token, Utils.generateKey());
                String base64 = Base64.encodeToString(encrypted_token, Base64.DEFAULT);
                SharedPreferences sharedPreferences = context.getSharedPreferences(context.getString(R.string.shar_prefs_name), MODE_PRIVATE);
                SharedPreferences.Editor loginPrefsEditor = sharedPreferences.edit();
                loginPrefsEditor.putString(context.getString(R.string.shar_prefs_token), base64);
                loginPrefsEditor.commit();
                return func.call();
            } else {
                MyToastSingleton.getInstance(context).setError(context.getString(R.string.error_authentication));
                return null;
            }
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String getToken (Context context){
        return "?token="+Utils.decryptSth(context.getSharedPreferences(context.getString(R.string.shar_prefs_name), MODE_PRIVATE).getString(context.getString(R.string.shar_prefs_token), ""));
    }
}
