package com.example.cristina.tfgapp.model.database;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.cristina.tfgapp.model.Product;
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
    private String sqlQueryCreate = "CREATE TABLE ProductQ (id_product NUMERIC, quantity NUMERIC, name TEXT)";
    public ProductDataBaseHelper (Context context, String name, SQLiteDatabase.CursorFactory factory, int v){
        super(context, name, factory, v);
        this.context = context;
    }
    public void onCreate(SQLiteDatabase db){
        db.execSQL(sqlQueryCreate);
    }

    public void onUpgrade (SQLiteDatabase db, int old_version, int new_version){
        db.execSQL("DROP TABLE IF EXISTS ProductQ");
        db.execSQL(sqlQueryCreate);
    }

    /*
    Método que dado un id devuelve el producto asociado en forma de objeto Product.
     */
    public Product selectMethodById (String idProduct) throws IOException {
        Product product = new Product();
        MainActivity.db = this.getReadableDatabase();
        String[] args_d = new String[]{idProduct};
        Cursor d = MainActivity.db.rawQuery("SELECT * FROM ProductQ WHERE id_product=?", args_d);
        if (d.moveToFirst()) {
            do {
                int id_product = d.getInt(0);
                int quantity = d.getInt(1);
                String name = d.getString(2);
                product.setId_product(id_product);
                product.setQuantity(quantity);
                product.setName(name);
            } while (d.moveToNext());
        }
        MainActivity.db.close();
        return product;
    }

    /*Método que modifica un producto determinado. Se pasa como parámetro el id del producto y un número.
    Dicho número ha de ser sumado al valor de su atributo quantity que representa el número de veces que ha sido comprado.
    Este método se llama desde BuyActivity tras haber realizado una compra. Se le pasa el número de unidades vendidas de cada producto
    para sumárselas a su contador total quantity. */
    public void updateProduct (int addQuantity, int id_product, String name){
        Product product = null;
        try {
            product = selectMethodById(String.valueOf(id_product));
        } catch (IOException e) {
            e.printStackTrace();
        }
        MainActivity.db = this.getWritableDatabase();
        if (product.getName() != null){
            int finalQuantity = product.getQuantity() + addQuantity;
            try {
                if (MainActivity.db!=null) MainActivity.db.execSQL("UPDATE ProductQ SET quantity = '"+finalQuantity+"' WHERE id_product =?", new String[]{String.valueOf(id_product)});
            }
            catch (SQLException sqlException){
                sqlException.printStackTrace();
            }
        }
        else {
            if (MainActivity.db!=null){
                MainActivity.db.execSQL("INSERT INTO ProductQ (id_product, quantity, name) " +
                            "VALUES ('" + id_product + "', '" + addQuantity + "', '" + name + "')");
            }
        }
        MainActivity.db.close();
    }

    //Método que devuelve un array list de todos los productos de la tabla
    public ArrayList<Product> selectAll (){
        ArrayList<Product> arrayList = new ArrayList<Product> (){};
        MainActivity.db = this.getReadableDatabase();
        String[] args_d = new String[]{};
        Cursor d = MainActivity.db.rawQuery("SELECT * FROM ProductQ", null);
        if (d.moveToFirst()) {
            Product product;
            int id, q;
            String n;
            do {
                id = d.getInt(0);
                q = d.getInt(1);
                n = d.getString(2);
                product=new Product();
                product.setId_product(id);
                product.setQuantity(q);
                product.setName(n);
                arrayList.add(product);
            } while (d.moveToNext());
        }
        MainActivity.db.close();
        return arrayList;
    }

}