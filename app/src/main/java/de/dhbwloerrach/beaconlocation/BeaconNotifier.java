package de.dhbwloerrach.beaconlocation;

import org.altbeacon.beacon.*;
import org.altbeacon.beacon.Beacon;

import java.util.ArrayList;
import java.util.Collection;

/**
 * Created by Lukas on 20.07.2015.
 */
public class BeaconNotifier implements RangeNotifier, MonitorNotifier {

    private ArrayList beaconList;

    public BeaconNotifier(ArrayList beaconList){
        this.beaconList = beaconList;
    }

    @Override
    public void didRangeBeaconsInRegion(Collection<Beacon> collection, Region region) {
        for(Beacon beacon : collection){
            beaconList.add(beacon);
        }
    }

    @Override
    public void didEnterRegion(Region region) {

    }

    @Override
    public void didExitRegion(Region region) {

    }

    @Override
    public void didDetermineStateForRegion(int i, Region region) {

    }
}
