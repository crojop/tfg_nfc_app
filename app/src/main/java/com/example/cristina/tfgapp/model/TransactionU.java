package com.example.cristina.tfgapp.model;


/**
 * Created by Cristina on 30/11/17.
 */

public class TransactionU implements Comparable<TransactionU>{
    /*
    Clase transacción: pago o recarga
     */
    public static final int TRANSACTION_RECHARGE = 1;
    public static final int TRANSACTION_PAYMENT = 2;
    private int transactiontype_id; //id del tipo de transacción
    private double transaction_amount; //cantidad total
    private long dateInMilliseconds; //fecha en milisegundos
    private EventU eventU; //evento asociado
    private TagU tagU; //tag que ha realizado la transacción
    private TerminalU terminalU; //terminal en el que se ha realizado la transación

    public TransactionU(int transactiontype_id, double transaction_amount, EventU eventU, TagU tagU, TerminalU terminalU) {
        this.transactiontype_id = transactiontype_id;
        this.transaction_amount = transaction_amount;
        this.eventU = eventU;
        this.tagU = tagU;
        this.terminalU = terminalU;
    }

    public int getTransactiontype_id() {
        return transactiontype_id;
    }

    public double getTransaction_amount() {
        return transaction_amount;
    }

    public EventU getEventU() {
        return eventU;
    }

    public TagU getTagU() {
        return tagU;
    }

    public TerminalU getTerminalU() {
        return terminalU;
    }

    public void setDateInMilliseconds(long dateInMilliseconds) {
        this.dateInMilliseconds = dateInMilliseconds;
    }

    public long getDateInMilliseconds (){
        return this.dateInMilliseconds;
    }

    //Implementa comparable para poder ordenar arrays de transacciones de mayor a menor por fecha
    public int compareTo(TransactionU o) {
        if (dateInMilliseconds > o.dateInMilliseconds) {
            return -1;
        }
        if (dateInMilliseconds < o.dateInMilliseconds) {
            return 1;
        }
        return 0;
    }
}
