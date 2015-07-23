package de.dhbwloerrach.beaconlocation;

import java.util.ArrayList;
import java.util.Date;
import java.util.concurrent.TimeUnit;

/**
 * Created by Lukas on 23.07.2015.
 */
public class BeaconList extends ArrayList<Beacon> {
    public BeaconList filterByLast(int seconds) {
        BeaconList result = new BeaconList();
        for(Beacon beacon : this) {
            if(beacon.hasDistanceIn(seconds)) {
                result.add(beacon);
            }
        }
        return result;
    }
}
