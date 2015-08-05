package de.dhbwloerrach.beaconlocation.models;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Salvo on 23.07.2015.
 */
public class Machine implements Parcelable {


    private String name;
    private Integer id;


    public Integer getId() {
        return id;
    }

    public Machine seIid(Integer id) {
        this.id = id;
        return this;

    }

    public String getName() {
        return name;
    }

    public Machine setName(String name) {
        this.name = name;
        return this;

    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(name);
    }
}
