package com.example.cristina.tfgapp.controller_view.shop;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.widget.SearchView;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.cristina.tfgapp.controller_view.MainActivity;
import com.example.cristina.tfgapp.controller_view.Utils;
import com.example.cristina.tfgapp.controller_view.menus.MenuActivity;
import com.example.cristina.tfgapp.R;
import com.example.cristina.tfgapp.model.Product;
import com.example.cristina.tfgapp.singleton.MyRequestQueueSingleton;
import com.example.cristina.tfgapp.singleton.MyToastSingleton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.concurrent.Callable;

import static android.content.ContentValues.TAG;

/**
 * Created by Cristina on 07/05/17.
 */

public class ProductActivity extends MenuActivity implements SearchView.OnQueryTextListener, SearchView.OnCloseListener{
    //Activity que muestra la lista de productos y permite seleccionarlos
    public ArrayList<Product> datos; //arraylist con todos los productos para mostrarlos en el list view
    private SearchView searchView;
    private ArrayList<Product> productsAr = new ArrayList();
    private Button btnContinue;
    private ListView productList;
    private boolean reloadImage = true;
    private ProductAdapter adapter;
    private AdapterView.OnItemClickListener onItemClickListener;

    public void onCreate (Bundle bundle){
        super.onCreate(bundle);
        setContentView(R.layout.activity_products);

        datos = new ArrayList<Product>(){};
        btnContinue = (Button) findViewById(R.id.btnContinue);
        initAct();
    }

    public void initAct(){
        JsonObjectRequest jsArrayRequest = new JsonObjectRequest(
                Request.Method.GET,
                this.getString(R.string.URL_PRODUCTS)+MainActivity.terminalU.getTerminal_serial_number()+Utils.getToken(this),
                null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        if (parseJson(response).equals(getString(R.string.VAL_SUCCESS))){
                            datos = productsAr;
                            //el adaptador y la lista
                            adapter = new ProductAdapter(ProductActivity.this, datos);
                            productList = (ListView) findViewById(R.id.listView1);
                            productList.setAdapter(adapter);
                            productList.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
                            onItemClickListener = new AdapterView.OnItemClickListener() {
                                public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                                    //Cuando se hace click en un elemento de la lista, se añade una unidad a ese producto
                                    int added_number = datos.get(position).getAdded();
                                    //Se actualiza la imagen del carrito
                                    datos.get(position).setImageCart(R.drawable.added_cart_32);
                                    //Se incrementa en una unidad el número de unidades seleccionadas de dicho producto
                                    datos.get(position).setAdded(added_number+1);
                                    //Se cambia a visible la imagen de retirar elemento del carro
                                    datos.get(position).setVisibility(View.VISIBLE);
                                    reloadImage = false;
                                    adapter.notifyDataSetChanged();
                                }
                            };
                            productList.setOnItemClickListener(onItemClickListener);

                            btnContinue.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                /*
                                Botón de finalizar compra. Se comprueba primero si se ha seleccionado algún producto. Si no
                                se hubiera seleccionado ninguno se muestra aviso y no se accede a la pantalla de compra.
                                 */
                                    boolean cartNotEmpty = false;
                                    for (Product product : datos){
                                        if (product.getAdded()>0) cartNotEmpty = true;
                                    }
                                    if (cartNotEmpty) {
                                        //Se accede a la pantalla de compra mediante un intent en el que se pasa el array de todos los productos
                                        Intent intentBuy = new Intent(getApplicationContext(), BuyActivity.class);
                                        intentBuy.putParcelableArrayListExtra(getResources().getString(R.string.PRODUCT), datos);
                                        startActivityForResult(intentBuy,1);
                                    } else MyToastSingleton.getInstance(ProductActivity.this).
                                            setError(getString(R.string.notProductsSelected));
                                }
                            });
                            search();
                        } else if (parseJson(response).equals(getString(R.string.VAL_ERROR))){
                            MyToastSingleton.getInstance(ProductActivity.this).setError(getString(R.string.error_charging_products));
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        if (error.getClass().equals(AuthFailureError.class)) {
                            Utils.changeToken(ProductActivity.this, new Callable<String>() {
                                public String call() {
                                    initAct();
                                    return null;
                                }
                            });
                        }
                        Log.d(TAG, getString(R.string.errorJsonResponse) + error.getMessage());
                    }
                }
        );
        MyRequestQueueSingleton.getInstance(this).addToRequestQueue(jsArrayRequest);
    }
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            if(resultCode == Activity.RESULT_OK) {
                datos = data.getParcelableArrayListExtra(getResources().getString(R.string.onResultResponse));
                reloadImage=true;
                adapter = new ProductAdapter(this, datos);
                productList.setAdapter(adapter);
                adapter.notifyDataSetChanged();
            }
        }
    }

    /*
    Función a la que se le pasa un objeto json con todos los productos devueltos por el método get
    y los mete en el array list que devuelve como respuesta
     */
    public String parseJson(JSONObject jsonObject){
        String result = this.getString(R.string.VAL_ERROR);
        try {
            boolean error = jsonObject.getBoolean(this.getString(R.string.error));
            String message = jsonObject.getString(this.getString(R.string.message));
            if (!error && message.equals(this.getString(R.string.success))){
                JSONArray jsonArray = jsonObject.getJSONArray(this.getString(R.string.data));
                for(int i=0; i<jsonArray.length(); i++){
                    try {
                        JSONObject objeto= jsonArray.getJSONObject(i);
                        Product product = new Product(
                                objeto.getInt(this.getString(R.string.resource_id)),
                                objeto.getString(this.getString(R.string.resource_title)),
                                objeto.getDouble(this.getString(R.string.resource_price)));
                        product.setImageCart(R.drawable.add_cart_32);
                        product.setDescription(objeto.getString(this.getString(R.string.resource_description)));
                        product.setRes_image(objeto.getString(this.getString(R.string.resource_img)));
                        productsAr.add(product);
                        result = this.getString(R.string.VAL_SUCCESS);
                    } catch (JSONException e) {
                        Log.d(this.getString(R.string.error), this.getString(R.string.parsingError)+ e.getMessage());
                    }
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return result;
    }

    //Se inicializa el search view para poder realizar búsquedas por productos
    private void search(){
        searchView = (SearchView) findViewById(R.id.search);
        searchView.setOnQueryTextListener(this);
        searchView.setOnCloseListener(this);
        searchView.setIconifiedByDefault(true);
    }
    @Override
    public boolean onClose() {
        reloadImage=true;
        adapter.notifyDataSetChanged();
        return false;
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        adapter.myFilter.filter(query);
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        adapter.myFilter.filter(newText);
        return false;
    }


    //Clase adaptador que implementa filterable para poder buscar por producto
    public class ProductAdapter extends ArrayAdapter implements Filterable{
        private Activity context;
        public AppFilter myFilter;
        public ArrayList<Product> arrayListX;

        ProductAdapter (Activity context, ArrayList<Product> arrayListX){
            super(context, R.layout.list_products, arrayListX);
            this.arrayListX = arrayListX;
            this.context = context;
            this.myFilter = new AppFilter(arrayListX);

        }
        class AppFilter extends Filter {

            private ArrayList<Product> sourceObjects;

            public AppFilter(ArrayList<Product> objects) {
                sourceObjects = new ArrayList<Product>();
                synchronized (this) {
                    sourceObjects.addAll(objects);
                }
            }
            @Override
            protected FilterResults performFiltering(CharSequence chars) {
                String filterSeq = chars.toString().trim().toLowerCase();
                FilterResults result = new FilterResults();
                if (filterSeq != null && filterSeq.length() > 0) {
                    ArrayList<Product> filter = new ArrayList<Product>();

                    for (Product object : sourceObjects) {
                        if (object.getName().trim().toLowerCase().contains(filterSeq))
                            filter.add(object);
                    }
                    result.count = filter.size();
                    result.values = filter;
                } else {
                    synchronized (this) {
                        result.values = sourceObjects;
                        result.count = sourceObjects.size();
                    }
                }
                return result;
            }

            @SuppressWarnings("unchecked")
            @Override
            protected void publishResults(CharSequence constraint,
                                          FilterResults results) {
                ArrayList<Product> filtered = (ArrayList<Product>) results.values;
                reloadImage=true;
                notifyDataSetChanged();
                clear();
                for (int i = 0; i < filtered.size(); i++)
                    add((Product) filtered.get(i));
                notifyDataSetInvalidated();
            }
        };

        public void loadImage(final View item, final int position){
            final ImageView imProduct = (ImageView) item.findViewById(R.id.imageViewProduct);
            ImageRequest imageRequest = new ImageRequest(
                    getString(R.string.URL_RESOURCES_IMAGES)+arrayListX.get(position).getRes_image()+Utils.getToken(ProductActivity.this),
                    new Response.Listener<Bitmap>() {
                        @Override
                        public void onResponse(Bitmap bitmap) {
                            imProduct.setImageBitmap(bitmap);
                        }
                    }, 0, 0, null, // maxWidth, maxHeight, decodeConfig
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            imProduct.setVisibility(View.INVISIBLE);
                            if (error.getClass().equals(AuthFailureError.class)) {
                                Utils.changeToken(ProductActivity.this, new Callable<String>() {
                                    public String call() {
                                        loadImage(item, position);
                                        return null;
                                    }
                                });
                            }
                        }
                    }
            );
            MyRequestQueueSingleton.getInstance(context).addToRequestQueue(imageRequest);
        }
        @SuppressWarnings("ResourceType")
        public View getView (final int position, View convertView, ViewGroup parent){
            //Función que actualiza la vista del adaptador
            LayoutInflater inflater = context.getLayoutInflater();
            View item;

            if (convertView==null || reloadImage){
                //Si aún no existe vista se cargan las imágenes
                item = inflater.inflate(R.layout.list_products, null, false);
                loadImage(item, position);
            }
            else {
                //Si ya se ha creado previamente la vista no se vuelven a cargar las imágenes
                item = (View) convertView;
            }

            TextView tvName = (TextView)item.findViewById(R.id.textViewName); //Nombre o descripción del producto
            TextView tvSku = (TextView)item.findViewById(R.id.textViewSku); //Sku
            TextView tvPrice = (TextView)item.findViewById(R.id.textViewPrice); //Precio
            ImageView imAdd = (ImageView) item.findViewById(R.id.addToCart); //Icono del carrito
            TextView tvNumberAdded = (TextView)item.findViewById(R.id.numberProducts); //Número de unidades de ese producto que han sido seleccionadas
            ImageView imRemove = (ImageView) item.findViewById(R.id.removeCart); //Icono que se pulsa si se desea decrementar el número de unidades seleccionadas

            //Se inicializan los elementos
            tvName.setText(arrayListX.get(position).getName());
            tvSku.setText(arrayListX.get(position).getDescription());
            tvPrice.setText(String.valueOf(arrayListX.get(position).getPrice())+getString(R.string.euro_divisa_symbol));
            imAdd.setImageResource(arrayListX.get(position).getImage());
            tvNumberAdded.setText(getResources().getString(R.string.X_opening_product)+ arrayListX.get(position).getAdded());

            imRemove.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //Si se pulsa sobre el icono de quitar unidades seleccionadas de un producto se decrementa el atributo added
                    arrayListX.get(position).setAdded(arrayListX.get(position).getAdded()-1);
                    /*Si added llega a 0 significa que no hay ninguna unidad seleccionada de dicho producto. Entonces se
                    cambia el icono del carrito al de carrito vacío y se pone a invisible el ImageView de retirar productos del carrito
                     */
                    if (arrayListX.get(position).getAdded()==0) {
                        arrayListX.get(position).setImageCart(R.drawable.add_cart_32);
                        arrayListX.get(position).setVisibility(View.INVISIBLE);
                    }
                    //Se refresca la vista del adaptador
                    reloadImage = false;
                    adapter.notifyDataSetChanged();
                }
            });
            tvNumberAdded.setVisibility(arrayListX.get(position).getVisibility());
            imRemove.setVisibility(arrayListX.get(position).getVisibility());

            return (item);
        }
    }
}
