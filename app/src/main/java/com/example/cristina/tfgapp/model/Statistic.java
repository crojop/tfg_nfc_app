package com.example.cristina.tfgapp.model;

/**
 * Created by Cristina on 12/12/17.
 */

public class Statistic {
    //día - ej: 8
    private String day;
    //hora - ej: 12
    private String hour;
    //id del tipo de transacción : 1(recarga) u 2(pago)
    private int transactiontype_id;
    //cantidad total
    private double total_amount;
    //número de transacciones
    private int number_of_transactions;

    public Statistic(String day, int transactiontype_id, int number_of_transactions) {
        this.day = day;
        this.transactiontype_id = transactiontype_id;
        this.number_of_transactions = number_of_transactions;
    }

    public String getHour() {
        return hour;
    }

    public void setHour(String hour) {
        this.hour = hour;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public void setTransactiontype_id(int transactiontype_id) {
        this.transactiontype_id = transactiontype_id;
    }

    public void setTotal_amount(double total_amount) {
        this.total_amount = total_amount;
    }

    public void setNumber_of_transactions(int number_of_transactions) {
        this.number_of_transactions = number_of_transactions;
    }

    public String getDay() {
        return day;
    }

    public int getTransactiontype_id() {
        return transactiontype_id;
    }

    public double getTotal_amount() {
        return total_amount;
    }

    public int getNumber_of_transactions() {
        return number_of_transactions;
    }
}
