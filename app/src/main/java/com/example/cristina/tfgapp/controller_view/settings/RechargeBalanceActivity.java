package com.example.cristina.tfgapp.controller_view.settings;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.text.Editable;
import android.text.InputFilter;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextWatcher;
import android.text.method.DigitsKeyListener;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.cristina.tfgapp.controller_view.Utils;
import com.example.cristina.tfgapp.controller_view.MainActivity;
import com.example.cristina.tfgapp.controller_view.menus.MenuActivity;
import com.example.cristina.tfgapp.singleton.MyRequestQueueSingleton;
import com.example.cristina.tfgapp.R;
import com.example.cristina.tfgapp.model.TransactionU;
import com.example.cristina.tfgapp.singleton.MyToastSingleton;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.concurrent.Callable;

/**
 * Created by Cristina on 30/11/17.
 */

public class RechargeBalanceActivity extends MenuActivity {
    /*
    Activity que muestra el saldo actual al usuario y permite recargarlo
     */

    //TextInputLayout donde se inserta el saldo a recargar
    private TextInputLayout inputQuantity;
    //TextView que muestra el saldo actual
    private TextView textViewCurrentBalance;
    //Botón de recarga de saldo
    private Button buttonRechargeBalance;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_recharge_balance);
        textViewCurrentBalance = (TextView) findViewById(R.id.tv_currentBalance);
        textViewCurrentBalance.setText(String.valueOf(MainActivity.currentTag.getUser().getBalance())+getString(R.string.euro_divisa_symbol));
        inputQuantity = (TextInputLayout) findViewById(R.id.inputQuantityRecharge);
        buttonRechargeBalance = (Button) findViewById(R.id.buttonRechargeBalance);
        inputQuantity.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                //No puede estar vacío ni contener más de 9 dígitos
                if (s.length()==0) inputQuantity.setError(getString(R.string.requiredField));
                else if (start>10) inputQuantity.setError(getString(R.string.maxLength9));
                else inputQuantity.setError(null);
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        InputFilter[] filtros = new InputFilter[] { new MoneyValueFilter() };
        inputQuantity.getEditText().setFilters(filtros);

        buttonRechargeBalance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            //Si no está vacío el campo de entrada, y no contiene ningún error, se procede con el proceso de recarga
            if (inputQuantity.getError()==null && !inputQuantity.getEditText().getText().toString().isEmpty()) {
                showAlert();
            }
            }
        });
    }

    /*
    Filtro que no permite introducir más de 2 dígitos
    */
    public class MoneyValueFilter extends DigitsKeyListener {
        public MoneyValueFilter() {
            super(false, true);
        }

        private int digits = 2;

        public void setDigits(int d) {
            digits = d;
        }

        @Override
        public CharSequence filter(CharSequence source, int start, int end,
                                   Spanned dest, int dstart, int dend) {
            CharSequence out = super.filter(source, start, end, dest, dstart, dend);

            if (out != null) {
                source = out;
                start = 0;
                end = out.length();
            }

            int len = end - start;
            if (len == 0) {
                return source;
            }

            int dlen = dest.length();

            for (int i = 0; i < dstart; i++) {
                if (dest.charAt(i) == '.') {
                    return (dlen-(i+1) + len > digits) ?
                            "" :
                            new SpannableStringBuilder(source, start, end);
                }
            }

            for (int i = start; i < end; ++i) {
                if (source.charAt(i) == '.') {
                    if ((dlen-dend) + (end-(i + 1)) > digits)
                        return "";
                    else
                        break;
                }
            }

            return new SpannableStringBuilder(source, start, end);
        }
    }

    /*
    Método que muestra una alerta de confirmación al usuario antes de realizar la recarga. Si lo acepta se produce la recarga, es decir,
    se crea un objeto transacción y se llama al método rechargeBalance que realizará el POST.
    */
    private void showAlert (){
        AlertDialog alertDialog = new AlertDialog.Builder(RechargeBalanceActivity.this)
                .setTitle(getString(R.string.balanceRecharge))
                .setMessage(getString(R.string.messageAlertRechargeBalance1)+ " " + inputQuantity.getEditText().getText().toString() + " " + getString(R.string.euro_divisa_symbol) + getString(R.string.messageAlertRechargeBalance2))
                .setIcon(this.getResources().getDrawable(R.drawable.piggybank128))
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        TransactionU transactionU = new TransactionU(TransactionU.TRANSACTION_RECHARGE, Double.parseDouble(inputQuantity.getEditText().getText().toString()),
                                MainActivity.terminalU.getEventU(), MainActivity.currentTag, MainActivity.terminalU);
                        rechargeBalance(transactionU);
                    }
                })
                .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        dialog.dismiss();
                    }
                }).create();
        alertDialog.show();
    }

    //Método que realiza el POST de la transacción
    private void rechargeBalance (final TransactionU transactionU){
        JSONObject request = new JSONObject();
        try
        {
            request.put(getString(R.string.transactiontype_id), transactionU.getTransactiontype_id());
            request.put(getString(R.string.event_id), transactionU.getEventU().getEvent_id());
            request.put(getString(R.string.terminal_serial_number), transactionU.getTerminalU().getTerminal_serial_number());
            request.put(getString(R.string.tag_code), transactionU.getTagU().getTag_code());
            request.put(getString(R.string.transaction_amount), transactionU.getTransaction_amount());
            request.put(getString(R.string.token), Utils.decryptSth(getSharedPreferences(getString(R.string.shar_prefs_name), MODE_PRIVATE).getString(getString(R.string.shar_prefs_token), "")));
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
        JsonObjectRequest postRequest = new JsonObjectRequest(Request.Method.POST, getString(R.string.URL_TRANSACTIONS),request,
                new Response.Listener<JSONObject>()
                {
                    @Override
                    public void onResponse(JSONObject response) {
                        if (validateResponse(response).equals(getString(R.string.VAL_ERROR))){
                            MyToastSingleton.getInstance(RechargeBalanceActivity.this).setError(getString(R.string.errorRechargeBalance));
                        } else if (validateResponse(response).equals(getString(R.string.VAL_SUCCESS))){
                            /*Si ha habido éxito se muestra toast informando de que to do ha ido bien,
                            se actualiza el saldo del usuario en el objeto currentTag, y se dirige al usuario a la activity del menú,
                            */
                            MyToastSingleton.getInstance(RechargeBalanceActivity.this).setSuccess(getString(R.string.successRechargeBalance));
                            MainActivity.currentTag.getUser().setBalance(MainActivity.currentTag.getUser().getBalance()+transactionU.getTransaction_amount());
                            Intent intent = new Intent(RechargeBalanceActivity.this, MenuActivity.class);
                            startActivity(intent);
                        }
                    }
                },
                new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //Si no ha habido éxito se muestra mensaje de error
                        if (error.getClass().equals(AuthFailureError.class)) {
                            Utils.changeToken(RechargeBalanceActivity.this, new Callable<String>() {
                                public String call() {
                                    rechargeBalance(transactionU);
                                    return null;
                                }
                            });
                        } else MyToastSingleton.getInstance(RechargeBalanceActivity.this).setError(getString(R.string.errorRechargeBalance));
                    }
                }
        );
        MyRequestQueueSingleton.getInstance(this).addToRequestQueue(postRequest);
    }

    private String validateResponse (JSONObject response){
        String result = getString(R.string.VAL_ERROR);
        try {
            String code = response.getString(getString(R.string.code));
            if(code.equals(getString(R.string.CODE_SUCCESSS))) result = getString(R.string.VAL_SUCCESS);
        }
        catch (JSONException e) {
            Log.d(getString(R.string.error), getString(R.string.parsingError));
        }
        return result;
    }
}
