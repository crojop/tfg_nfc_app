package com.example.cristina.tfgapp.model;

/**
 * Created by Cristina on 03/11/17.
 */

public class User {
    //Clase usuario
    private int user_id; //id de usuario
    private String user_description; //descripción o nombre de usuario
    private double balance; //Saldo
    private TagU tagU; //tag asociado a ese usuario

    public User(int user_id, String user_description, double openingBalance, double balance, TagU tagU) {
        this.user_id = user_id;
        this.user_description = user_description;
        this.balance = balance;
        this.tagU = tagU;
    }

    public User() {
        TagU tagUinvalid = new TagU(-1);
        this.user_id = -1;
        this.user_description = null;
        this.balance = -1;
        this.tagU = tagUinvalid;
    }

    public User(int user_id) {
        this.user_id = user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public void setUser_description(String user_description) {
        this.user_description = user_description;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    public void setTagU(TagU tagU) {
        this.tagU = tagU;
    }

    public int getUser_id() {
        return user_id;
    }

    public String getUser_description() {
        return user_description;
    }

    public double getBalance() {
        return balance;
    }

    public TagU getTagU() {
        return tagU;
    }
}
