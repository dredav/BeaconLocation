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

    public void testFilterByLast() throws Exception {
        beacons.clear();
        beacons.addAll(helper.createBeacons(5));

        BeaconList filteredList = beacons.filterByLast(2);
        assertEquals(2, filteredList.size());
    }

    public void testSortByRssi() throws Exception {
        beacons.Sort(FilterTyp.RSSI);
        for(int i = 1; i < beacons.size(); i++){
            assertTrue(beacons.get(i).getRssi() >= beacons.get(i-1).getRssi());
        }
    }

    public void testSortByMinor() throws Exception {
        beacons.Sort(FilterTyp.Minor);
        for(int i = 1; i < beacons.size(); i++){
            assertTrue(beacons.get(i).getRssi() <= beacons.get(i-1).getRssi());
        }
    }

    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
    }
}
