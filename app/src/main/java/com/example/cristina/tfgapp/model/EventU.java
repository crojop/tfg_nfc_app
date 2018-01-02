package com.example.cristina.tfgapp.model;


import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Cristina on 20/11/17.
 */

public class EventU implements Parcelable{
    //Clase EventU que tiene event_id y event_description. Por ejemplo 1 y FIB2017
    private int event_id;
    private String event_description;

    public EventU(int event_id) {
        this.event_id = event_id;
    }

    public EventU(int event_id, String event_description) {
        this.event_id = event_id;
        this.event_description = event_description;
    }

    public int getEvent_id() {
        return event_id;
    }

    public String getEvent_description() {
        return event_description;
    }

    public void setEvent_id(int event_id) {
        this.event_id = event_id;
    }

    public void setEvent_description(String event_description) {
        this.event_description = event_description;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(event_id);
        dest.writeString(event_description);
    }

    public static final Parcelable.Creator<EventU> CREATOR
            = new Parcelable.Creator<EventU>() {
        public EventU createFromParcel(Parcel in) {
            return new EventU(in);
        }

        public EventU[] newArray(int size) {
            return new EventU[size];
        }
    };

    private EventU(Parcel in) {
        event_id = in.readInt();
        event_description = in.readString();
    }
}
