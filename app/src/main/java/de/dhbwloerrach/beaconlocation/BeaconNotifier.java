package de.dhbwloerrach.beaconlocation;

import org.altbeacon.beacon.*;
import org.altbeacon.beacon.Beacon;

import java.util.ArrayList;
import java.util.Collection;

/**
 * Created by Lukas on 20.07.2015.
 */
public class BeaconNotifier implements RangeNotifier, MonitorNotifier {


    public BeaconNotifier(ArrayList beaconList){
        this.beaconList = beaconList;
    }

    @Override
    public void didRangeBeaconsInRegion(Collection<Beacon> collection, Region region) {
        ArrayList<de.dhbwloerrach.beaconlocation.Beacon> beaconList = new ArrayList<de.dhbwloerrach.beaconlocation.Beacon>();
        for(Beacon beacon : collection){
            de.dhbwloerrach.beaconlocation.Beacon current = new de.dhbwloerrach.beaconlocation.Beacon();
            current.setUuid(beacon.getId1().toString())
                    .setMajor(beacon.getId2().toString())
                    .setMinor(beacon.getId3().toString());
            beaconList.add(current);
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
