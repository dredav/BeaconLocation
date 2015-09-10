package de.dhbwloerrach.beaconlocation.test.modles;

import android.test.AndroidTestCase;

import java.util.List;

import de.dhbwloerrach.beaconlocation.models.Beacon;
import de.dhbwloerrach.beaconlocation.models.BeaconList;
import de.dhbwloerrach.beaconlocation.models.FilterTyp;
import de.dhbwloerrach.beaconlocation.test.helpers.TestHelper;

/**
 * Created by David on 7/24/15.
 */
public class BeaconListTest extends AndroidTestCase {
    private TestHelper helper = new TestHelper();

    private BeaconList beacons;

    private static final int COUNT_BEACONS = 10;

    public BeaconListTest() {
        super();
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();

        List<Beacon> beacons = helper.createBeacons(COUNT_BEACONS);
        for(Beacon beacon : beacons) {
            beacon.setRssi(helper.createRandom(-150, 0));
        }

        this.beacons = new BeaconList();
        this.beacons.addAll(beacons);
    }

    public void testSortByRSSI() {
        beacons.Sort(FilterTyp.RSSI);

        int lastRssi = 0;
        for (int index = 0; index < beacons.size(); index ++) {
            int currentRssi = beacons.get(index).getRssi();

            assertTrue(currentRssi <= lastRssi);
            lastRssi = currentRssi;
        }
    }

    public void testSortByMinor() {
        beacons.Sort(FilterTyp.Minor);

        int lastMinor = 0;
        for (int index = 0; index < beacons.size(); index ++) {
            int currentMinor = beacons.get(index).getMinor();

            assertTrue(currentMinor >= lastMinor);
            lastMinor = currentMinor;
        }
    }

    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
    }
}
