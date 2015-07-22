package de.dhbwloerrach.beaconlocation;

import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.RemoteException;
import android.util.Log;

import org.altbeacon.beacon.BeaconConsumer;
import org.altbeacon.beacon.BeaconManager;
import org.altbeacon.beacon.BeaconParser;
import org.altbeacon.beacon.Identifier;
import org.altbeacon.beacon.MonitorNotifier;
import org.altbeacon.beacon.RangeNotifier;
import org.altbeacon.beacon.Region;

import java.util.ArrayList;
import java.util.Collection;

/**
 * Created by alirei on 20.07.2015.
 */
public class BeaconTools implements BeaconConsumer {
    Region mRegion = new Region("Region", Identifier.parse("01234567-89AB-CDEF-0123-000000000001"), null, null);
    BeaconManager beaconManager;
    MainActivity context;
    ArrayList beacons;

    public BeaconTools(MainActivity context){
        this.context = context;
        beaconManager = BeaconManager.getInstanceForApplication(this.context);
        beaconManager.getBeaconParsers().add(new BeaconParser().setBeaconLayout("m:2-3=0215,i:4-19,i:20-21,i:22-23,p:24-24"));
        beaconManager.bind(this);
    }

    public ArrayList GetBeacons(){
        return beacons;
    }

    public Beacon GetBeacon(String uuid){
        return null;
    }

    public Beacon GetBeacon(Integer major, Integer minor){
        return null;
    }

    @Override
    public void onBeaconServiceConnect() {
        try {
            //Scan lasts for SCAN_PERIOD time
            beaconManager.setForegroundScanPeriod(1000l);
//        mBeaconManager.setBackgroundScanPeriod(0l);
            //Wait every SCAN_PERIOD_INBETWEEN time
            beaconManager.setForegroundBetweenScanPeriod(0l);
            //Update default time with the new one
            beaconManager.updateScanPeriods();
        }catch (RemoteException e){
            e.printStackTrace();
        }

        BeaconNotifier notifier = new BeaconNotifier(this.context);
        beaconManager.setRangeNotifier(notifier);
        beaconManager.setMonitorNotifier(notifier);

        try {
            //Start Monitoring
            beaconManager.startRangingBeaconsInRegion(mRegion);
            beaconManager.startMonitoringBeaconsInRegion(mRegion);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Context getApplicationContext() {
        return this.context;
    }

    @Override
    public void unbindService(ServiceConnection serviceConnection) {
        context.unbindService(serviceConnection);
    }

    @Override
    public boolean bindService(Intent intent, ServiceConnection serviceConnection, int i) {
        return context.bindService(intent, serviceConnection, i);
    }

    public void unbind()
    {
        beaconManager.unbind(this);
    }
}
