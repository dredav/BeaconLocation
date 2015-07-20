package de.dhbwloerrach.beaconlocation;

import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;

import org.altbeacon.beacon.BeaconConsumer;
import org.altbeacon.beacon.BeaconManager;

import java.util.ArrayList;

/**
 * Created by alirei on 20.07.2015.
 */
public class BeaconTools implements BeaconConsumer {
    BeaconManager beaconManager;
    Context context;

    public BeaconTools(Context context){
        this.context = context;
        beaconManager = BeaconManager.getInstanceForApplication(this.context);
        beaconManager.bind(this);
    }

    public ArrayList GetBeacons(){
        return null;
    }

    public Beacon GetBeacon(String uuid){
        return null;
    }

    public Beacon GetBeacon(Integer major, Integer minor){
        return null;
    }

    @Override
    public void onBeaconServiceConnect() {

    }

    @Override
    public Context getApplicationContext() {
        return this.context;
    }

    @Override
    public void unbindService(ServiceConnection serviceConnection) {

    }

    @Override
    public boolean bindService(Intent intent, ServiceConnection serviceConnection, int i) {
        return false;
    }
}
