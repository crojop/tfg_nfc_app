package com.example.cristina.tfgapp.model;

import android.os.Parcel;
import android.os.Parcelable;
import android.view.View;

/**
 * Created by Cristina on 07/05/17.
 */

public class Product implements Parcelable {
    // Implementa parcelable para poder ser pasado el array de productos de una activity a otra (de ProductActivity a BuyActiviy)

    private int id_product; //id del producto. Es único
    private String name; //Nombre del producto
    private double price; //Precio
    private int imageCart; //Imagen del carrito asociada: carrito vacío (gris) o carrito a rellenar más (carrito verde)
    private int quantity; //Cantidad de unidades que lleva vendidas ese producto en este terminal
    private int added=0; //Número de elementos seleccionados de este producto en el proceso de compra actual
    private int visibility = View.INVISIBLE; //Visibilidad de la imagen del carrito de retirar producto (rojo) asociada
    private String description;
    private String res_image;

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setRes_image(String res_image) {
        this.res_image = res_image;
    }

    public String getRes_image() {
        return res_image;
    }

    public Product(int id, String n, double p, int q, int i){
        this.id_product = id;
        this.name = n;
        this.price = p;
        this.quantity = q;
        this.imageCart = i;
    }

    public Product(int id, String n, double p, int q, int i, String d, String r){
        this.id_product = id;
        this.name = n;
        this.price = p;
        this.quantity = q;
        this.imageCart = i;
        this.description = d;
        this.res_image = r;
    }

    public int getQuantity() {
        return quantity;
    }

    public Product(int id, String strName, double strPrice){
        this.id_product = id;
        this.name = strName;
        this.price = strPrice;
    }

    public Product(){
    }

    public void setVisibility (int visib){
        this.visibility = visib;
    }

    public int getVisibility (){
        return this.visibility;
    }
    public void setName (String n){
        this.name = n;
    }

    public void setPrice (Double p){
        this.price = p;
    }

    public void setAdded (int num){
        if (added>=0) added = num;
    }

    public int getAdded (){
        return added;
    }

    public void setImageCart (int i){
        this.imageCart = i;
    }
    public int getImage (){
        return this.imageCart;
    }

    public String getName (){
        return this.name;
    }

    public double getPrice () {
        return this.price;
    }

    public void setId_product(int id_product) {
        this.id_product = id_product;
    }

    @Override

    public int describeContents() {
        return 0;
    }

    public int getId_product() {
        return id_product;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id_product);
        dest.writeString(name);
        dest.writeString(description);
        dest.writeDouble(price);
        dest.writeInt(imageCart);
        dest.writeString(res_image);
        dest.writeInt(added);
        dest.writeInt(visibility);
    }
    public static final Parcelable.Creator<Product> CREATOR
            = new Parcelable.Creator<Product>() {
        public Product createFromParcel(Parcel in) {
            return new Product(in);
        }

        public Product[] newArray(int size) {
            return new Product[size];
        }
    };

    private Product(Parcel in) {
        id_product = in.readInt();
        name = in.readString();
        description = in.readString();
        price = in.readDouble();
        imageCart = in.readInt();
        res_image = in.readString();
        added = in.readInt();
        visibility = in.readInt();
    }
}
