package de.dhbwloerrach.beaconlocation.bluetooth;

import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.RemoteException;

import org.altbeacon.beacon.BeaconConsumer;
import org.altbeacon.beacon.BeaconManager;
import org.altbeacon.beacon.BeaconParser;
import org.altbeacon.beacon.Region;

import java.util.ArrayList;

/**
 * Created by alirei on 20.07.2015.
 */
public class BeaconTools implements BeaconConsumer {
    Region mRegion = new Region("Region", null, null, null);
    BeaconManager beaconManager;
    Context context;
    ArrayList<IBeaconListView> listViews = new ArrayList<>();

    public BeaconTools(Context context, IBeaconListView listView){
        this.context = context;
        this.listViews.add(listView);
        beaconManager = BeaconManager.getInstanceForApplication(this.context);
        beaconManager.getBeaconParsers().add(new BeaconParser().setBeaconLayout("m:2-3=0215,i:4-19,i:20-21,i:22-23,p:24-24"));
        beaconManager.bind(this);
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

        BeaconNotifier notifier = new BeaconNotifier(this.listViews);
        beaconManager.setRangeNotifier(notifier);

        try {
            //Start Monitoring
            beaconManager.startRangingBeaconsInRegion(mRegion);
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

    public void addView(IBeaconListView view) {
        this.listViews.add(view);
    }
}
