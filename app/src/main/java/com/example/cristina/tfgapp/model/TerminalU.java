package com.example.cristina.tfgapp.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Cristina on 20/11/17.
 */

public class TerminalU implements Parcelable{
    /*
    Clase terminal: móvil o dispositivo que usa la aplicación
     */
    private int terminal_serial_number; //número de terminal
    private String terminal_description; //descripción del terminal
    private EventU eventU; //evento asociado

    public TerminalU(int terminal_serial_number) {
        this.terminal_serial_number = terminal_serial_number;
    }

    public TerminalU(int terminal_serial_number, String terminal_description, EventU eventU) {
        this.terminal_serial_number = terminal_serial_number;
        this.terminal_description = terminal_description;
        this.eventU = eventU;
    }

    public int getTerminal_serial_number() {
        return terminal_serial_number;
    }

    public String getTerminal_description() {
        return terminal_description;
    }

    public EventU getEventU() {
        return eventU;
    }

    public void setTerminal_serial_number(int terminal_serial_number) {
        this.terminal_serial_number = terminal_serial_number;
    }

    public void setTerminal_description(String terminal_description) {
        this.terminal_description = terminal_description;
    }

    public void setEventU(EventU eventU) {
        this.eventU = eventU;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(terminal_serial_number);
        dest.writeString(terminal_description);
        dest.writeParcelable(eventU, flags);
    }

    public static final Parcelable.Creator<TerminalU> CREATOR
            = new Parcelable.Creator<TerminalU>() {
        public TerminalU createFromParcel(Parcel in) {
            return new TerminalU(in);
        }

        public TerminalU[] newArray(int size) {
            return new TerminalU[size];
        }
    };

    private TerminalU(Parcel in) {
        terminal_serial_number = in.readInt();
        terminal_description = in.readString();
        eventU = in.readParcelable(EventU.class.getClassLoader());
    }

}

