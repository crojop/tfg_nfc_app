package com.example.cristina.tfgapp.controller_view.shop;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.cunoraz.tagview.Constants;
import com.cunoraz.tagview.Tag;
import com.cunoraz.tagview.TagView;
import com.example.cristina.tfgapp.controller_view.login.LoginActivity;
import com.example.cristina.tfgapp.controller_view.MainActivity;
import com.example.cristina.tfgapp.controller_view.menus.MenuActivity;
import com.example.cristina.tfgapp.singleton.MyRequestQueueSingleton;
import com.example.cristina.tfgapp.R;
import com.example.cristina.tfgapp.model.Product;
import com.example.cristina.tfgapp.model.TransactionU;
import com.example.cristina.tfgapp.singleton.MyToastSingleton;

import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Cristina on 09/05/17.
 */

public class BuyActivity extends MenuActivity {
    //Activity que lleva a cabo a cabo el proceso de compra una vez se han seleccionado los productos en ProductActivity

    private Double productsPrice;
    private TextView textViewBalance;
    private TextView textViewPrice;
    private ArrayList<Product> datos;
    private Button buttonContinueBuy;
    private TransactionU transactionU;
    private static final String URL_TRANSACTIONS = "http://vpayment.perentec.com/API/V1/transactions";

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buy);
        buttonContinueBuy = (Button) findViewById(R.id.buttonContinueBuy);
        textViewPrice = (TextView) findViewById(R.id.totalPrice);
        textViewBalance = (TextView) findViewById(R.id.balanceAfter);
        if (savedInstanceState == null) {
            Bundle extras = getIntent().getExtras();
            if(extras == null) datos= null;
            else datos = extras.getParcelableArrayList(getResources().getString(R.string.PRODUCT));
        } else datos = (ArrayList<Product>) savedInstanceState.getSerializable(getResources().getString(R.string.PRODUCT));
        createTags(); //Genera los tags o etiquetas y sus manejadores de eventos (borrado, pulsación)
        //Se actualizan los text views textViewPrice y textViewBalance
        fillTextViews();
        LayoutInflater inflater = getLayoutInflater();
        View layout = inflater.inflate(R.layout.toast_layout, (ViewGroup) findViewById(R.id.lytLayout));
        buttonContinueBuy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean cartNotEmpty = false; //Variable necesaria para saber si hay productos para comprar o no (si el carrito está vacío)
                for (Product product : datos){
                    if (product.getAdded()>0) cartNotEmpty = true;
                }
                //Si el carrito está vacío muestra toast con alerta
                if (!cartNotEmpty) MyToastSingleton.getInstance(BuyActivity.this, BuyActivity.this).
                        setError(getString(R.string.notProductsSelected));
                /*Si no está vacío y tiene saldo suficiente llama a showAlert(), que muestra alerta de confirmación al usuario
                antes de pagar, y si este la acepta, se procede a realizar el pago. */
                else if (getDifference()>0) showAlert();
                //Si no está vacío pero no tiene saldo suficiente muestra alerta
                else MyToastSingleton.getInstance(BuyActivity.this, BuyActivity.this).
                            setError(getString(R.string.errorInsufficientBalance));
            }
        });
    }

    /* Función que muestra alerta de confirmación al usuario antes de pagar, y si este la acepta,
    se procede a realizar el pago */
    private void showAlert (){
        AlertDialog alertDialog = new AlertDialog.Builder(BuyActivity.this)
                .setTitle(getString(R.string.payment))
                .setMessage(getString(R.string.messageAlertPayment1) + " " + productsPrice + " " + getString(R.string.euro_divisa_symbol) +
                getString(R.string.messageAlertPayment2))
                .setIcon(getDrawable(R.drawable.payment_32))
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        //El usuario ha aceptado. Se procede a realizar el pago
                        for (Product product : datos){
                            if (product.getAdded()>0){
                                MainActivity.prodDataBaseHelper.updateProduct(product.getAdded(), product.getId_product());
                                /*
                                Se actualiza la base de datos de productos sumándole la cantidad comprada de cada producto en este pago.
                                Esto se hace para poder saber cuántas unidades se han comprado de cada producto, de cara a las estadísticas.
                                */
                            }
                        }
                        transactionU = new TransactionU(TransactionU.TRANSACTION_PAYMENT, productsPrice,
                                LoginActivity.terminalU.getEventU(), MainActivity.currentTag, LoginActivity.terminalU);

                        /*Se genera la variable global transactionU con los datos de la transacción y se llama al método pay,
                        que realizará el pago*/
                        pay();
                    }
                })
                .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        dialog.dismiss();
                    }
                }).create();
        alertDialog.show();
    }

    private void pay (){
        //Función que realiza el pago: realiza el POST de la transación
        JSONObject request = new JSONObject();
        try
        {
            request.put(getString(R.string.transactiontype_id), transactionU.getTransactiontype_id());
            request.put(getString(R.string.event_id), transactionU.getEventU().getEvent_id());
            request.put(getString(R.string.terminal_serial_number), transactionU.getTerminalU().getTerminal_serial_number());
            request.put(getString(R.string.tag_code), transactionU.getTagU().getTag_code());
            request.put(getString(R.string.transaction_amount), transactionU.getTransaction_amount());
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
        JsonObjectRequest postRequest = new JsonObjectRequest(Request.Method.POST, URL_TRANSACTIONS,request,
                new Response.Listener<JSONObject>()
                {
                    @Override
                    public void onResponse(JSONObject response) {
                        MyToastSingleton.getInstance(BuyActivity.this, BuyActivity.this).
                                setSuccess(getString(R.string.successPayment));
                        Intent intent = new Intent(BuyActivity.this, MenuActivity.class);
                        startActivity(intent);
                    }
                },
                new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        MyToastSingleton.getInstance(BuyActivity.this, BuyActivity.this).
                                setError(getString(R.string.errorPayment));
                    }
                }
        );
        MyRequestQueueSingleton.getInstance(this).addToRequestQueue(postRequest);
    }

    /*Función que genera los tags o etiquetas con los nombres de los productos a comprar que se muestran en la activity
    NOTA: No confundir con los tags NFC del sistema*/
    private void createTags (){
        final TagView tagGroup = (TagView) findViewById(R.id.tag_group);
        productsPrice = 0.0;
        for (int i=0; i<datos.size();i++){
            if (datos.get(i).getAdded()>0){
                String textTag = " (x"+datos.get(i).getAdded()+") "+datos.get(i).getName();
                if (textTag.length()>36) {
                    String text_cut = textTag.substring(0, 32);
                    textTag = text_cut.concat(getResources().getString(R.string.ELLIPSIS));
                }
                Tag tag = new Tag(textTag);
                productsPrice += datos.get(i).getPrice()*datos.get(i).getAdded();
                tag.layoutColor = ContextCompat.getColor(this, R.color.colorIndigo);
                tag.deleteIcon = Constants.DEFAULT_TAG_DELETE_ICON;
                tag.deleteIndicatorSize = 18;
                tag.tagTextSize = 18;
                tag.isDeletable = true;
                tagGroup.addTag(tag);
            }
        }
        tagGroup.setOnTagClickListener(new TagView.OnTagClickListener() {
            @Override
            public void onTagClick(Tag tag, int position) {}
        });

        //Cuando el usuario haga click en cerrar o eliminar tag se realizará lo siguiente
        tagGroup.setOnTagDeleteListener(new TagView.OnTagDeleteListener() {
            @Override
            public void onTagDeleted(final TagView view, final Tag tag, final int position) {

                /*Se crea un array list auxiliar de objetos Product al que se van añadiendo aquellos Product del array global datos
                que hayan sido añadidos a la lista de compra. De esta manera generamos un array list sólo con aquellos productos
                que estén en el carrito de compra.
                Si nos damos cuenta los índices de cada producto en este array coinciden con los índices
                en el array de tags o etiquetas.
                */
                ArrayList<Product> auxDatos = new ArrayList<Product>();
                for (Product product: datos){
                    if (product.getAdded()>0) auxDatos.add(product);
                }

                /*position nos indica el índice o posición del producto que se acaba de eliminar en el array de tags, que es el mismo que el array de productos
                . Por lo tanto preguntamos en el array de productos el precio del producto que se acaba de quitar de la cesta, procedemos a averiguar su precio y restárselo a la
                variable global productsPrice, que almacena el precio total a pagar por el usuario */
                productsPrice -= auxDatos.get(position).getPrice()*auxDatos.get(position).getAdded();

                //Se actualiza el array de datos, que recordemos que contiene la lista de todos los productos de la ProductActivity
                for (int i =0; i<datos.size(); i++){
                    if (datos.get(i)==auxDatos.get(position)) {
                        datos.get(i).setAdded(0);
                        datos.get(i).setVisibility(View.INVISIBLE);
                        datos.get(i).setImageCart(R.drawable.add_cart_32);
                    }
                }

                //Para terminar se elimina el tag del tagGroup
                tagGroup.remove(position);

                //Se actualizan los text views textViewPrice y textViewBalance
                fillTextViews();
            }
        });

        tagGroup.setOnTagLongClickListener(new TagView.OnTagLongClickListener() {
            @Override
            public void onTagLongClick(Tag tag, int position) {}
        });
    }

    /**
     * Si vuelve a ProductActivity pulsando la flecha del appbar, estará totalmente sincronizada
     * con esta, es decir si aquí ha borrado el tag de un producto, en ProductActivity ya no aparecerá
     * ese producto seleccionado.
     * Esto se consigue mediante el onResultResponse que recibe el array de productos seleccionados
     * actualizado de esta activity.
     * @param item
     * @return
     */
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                Intent intent = new Intent();
                intent.putParcelableArrayListExtra(getResources().getString(R.string.onResultResponse), datos);
                setResult(Activity.RESULT_OK, intent);
                this.finish();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    //Se actualizan los text views textViewPrice y textViewBalance
    private void fillTextViews(){
        productsPrice = Math.round(productsPrice*100.0)/100.0; //Se redondea a dos decimales
        textViewPrice.setText(productsPrice.toString() + getResources().getString(R.string.euro_divisa_symbol));
        textViewBalance.setText(String.valueOf(getDifference())+ getResources().getString(R.string.euro_divisa_symbol));
        if (productsPrice==0) textViewPrice.setTextColor(ContextCompat.getColor(this, R.color.colorTeja));
        else textViewPrice.setTextColor(ContextCompat.getColor(this, R.color.colorGreenApp));
        if (getDifference()<0) textViewBalance.setTextColor(ContextCompat.getColor(this, R.color.colorTeja));
        else textViewBalance.setTextColor(ContextCompat.getColor(this, R.color.colorGreenApp));
    }

    //Función que devuelve el saldo que tendría el usuario tras la compra
    private Double getDifference (){
        return Math.round((getUserBalance()-productsPrice)*100.0)/100.0;
    }

    //Función que devuelve el saldo actual del usuario
    private Double getUserBalance (){
        return MainActivity.currentTag.getUser().getBalance();
    }

}
