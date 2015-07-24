package de.dhbwloerrach.beaconlocation.models;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

/**
 * Created by Lukas on 23.07.2015.
 */
public class BeaconList extends ArrayList<Beacon> {
    public BeaconList filterByLast(int seconds) {
        BeaconList result = new BeaconList();
        for (Beacon beacon : this) {
            if (beacon.hasDistanceIn(seconds)) {
                result.add(beacon);
            }
        }
        return result;
    }

    public BeaconList SortByDistance() {
        Collections.sort(this, new Comparator<Beacon>() {
            @Override
            public int compare(Beacon lhs, Beacon rhs) {
                double tempDiff = lhs.getDistance() - rhs.getDistance();
                if (tempDiff == 0) {
                    return 0;
                }
                if (tempDiff < 0) {
                    return -1;
                } else {
                    return 1;
                }
            }
        });
        return this;
    }
}

