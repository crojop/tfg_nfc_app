package com.example.cristina.tfgapp.model.database;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.cristina.tfgapp.R;
import com.example.cristina.tfgapp.model.Product;
import com.example.cristina.tfgapp.model.ProductsWS;
import com.example.cristina.tfgapp.controller_view.MainActivity;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by Cristina on 17/05/17.
 */

public class ProductDataBaseHelper extends SQLiteOpenHelper{
    /*
    Clase que hereda de SQLiteOpenHelper que gestiona la tabla Product donde se almacenan los productos a mostrar en ese terminal
     */
    private Context context;
    /*
    Cada elemento de la tabla tiene un id único, un nombre o descripción de producto,
    un precio y una cantidad asociada que se corresponde con el número de veces que ha sido comprado.
     */
    private String sqlQueryCreate = "CREATE TABLE Product (id_product NUMERIC, nameProduct TEXT, price REAL, quantity NUMERIC)";
    public ProductDataBaseHelper (Context context, String name, SQLiteDatabase.CursorFactory factory, int v){
        super(context, name, factory, v);
        this.context = context;
    }
    public void onCreate(SQLiteDatabase db){
        db.execSQL(sqlQueryCreate);
        //Se cargan los productos del web service
        ProductsWS productsWS = new ProductsWS();
        productsWS.chargeProducts(context);
    }

    public void onUpgrade (SQLiteDatabase db, int old_version, int new_version){
        db.execSQL("DROP TABLE IF EXISTS Product");
        db.execSQL(sqlQueryCreate);
    }

    /*
    Método que dado un id devuelve el producto asociado en forma de objeto Product.
     */
    public Product selectMethodById (String idProduct) throws IOException {
        Product product = new Product();
        MainActivity.db = this.getReadableDatabase();
        String[] args_d = new String[]{idProduct};
        Cursor d = MainActivity.db.rawQuery("SELECT * FROM Product WHERE id_product=?", args_d);
        if (d.moveToNext()) {
            do {
                int id_product = d.getInt(0);
                String name_Product = d.getString(1);
                double price = d.getDouble(2);
                int quantity = d.getInt(3);
                int i= R.drawable.add_cart_32;
                product = new Product(id_product, name_Product, price, quantity, i);
            } while (d.moveToNext());
        }
        MainActivity.db.close();
        return product;
    }

    //Método que devuelve un array list de todos los productos de la tabla
    public ArrayList<Product> selectAll (){
        ArrayList<Product> arrayList = new ArrayList<Product> (){};

        MainActivity.db = this.getReadableDatabase();
        String[] args_d = new String[]{};
        Cursor d = MainActivity.db.rawQuery("SELECT * FROM Product", args_d);
        if (d.moveToNext()) {
            Product product;
            int id, q;
            double p;
            String n;
            int i= R.drawable.add_cart_32;
            do {
                id = d.getInt(0);
                n = d.getString(1);
                p = d.getDouble(2);
                q = d.getInt(3);
                product=new Product(id, n, p, q, i);
                arrayList.add(product);
            } while (d.moveToNext());
        }
        MainActivity.db.close();
        return arrayList;
    }

    //Método que inserta en la tabla una serie de productos pasados por parámetro dentro de un array list
    public void insertProducts(ArrayList<Product> products){

        MainActivity.db = this.getWritableDatabase();
        if (MainActivity.db!=null && products.size()>0){
            for (final Product product : products) {
                MainActivity.db.execSQL("INSERT INTO Product (id_product, nameProduct, price, quantity) " +
                        "VALUES ('" + product.getId_product() + "', '" + product.getName() + "', '" + product.getPrice() + "', 0)");

            }
        }
        MainActivity.db.close();
    }

    /*Método que modifica un producto determinado. Se pasa como parámetro el id del producto y un número.
    Dicho número ha de ser sumado al valor de su atributo quantity que representa el número de veces que ha sido comprado.
    Este método se llama desde BuyActivity tras haber realizado una compra. Se le pasa el número de unidades vendidas de cada producto
    para sumárselas a su contador total quantity. */
    public void updateProduct (int addQuantity, int id_product){
        Product product = null;
        try {
            product = selectMethodById(String.valueOf(id_product));
        } catch (IOException e) {
            e.printStackTrace();
        }
        int finalQuantity = product.getQuantity() + addQuantity;
        MainActivity.db = this.getWritableDatabase();
        try {
            if (MainActivity.db!=null) MainActivity.db.execSQL("UPDATE Product SET quantity = '"+finalQuantity+"' WHERE id_product =?", new String[]{String.valueOf(id_product)});
        }
        catch (SQLException sqlException){
           sqlException.printStackTrace();
        }
        MainActivity.db.close();
    }

}