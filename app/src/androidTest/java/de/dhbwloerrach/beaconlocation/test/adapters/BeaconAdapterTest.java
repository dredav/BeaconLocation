package de.dhbwloerrach.beaconlocation.test.adapters;

import android.test.AndroidTestCase;
import android.view.View;
import android.widget.TextView;

import java.util.List;

import de.dhbwloerrach.beaconlocation.R;
import de.dhbwloerrach.beaconlocation.adapters.BeaconAdapter;
import de.dhbwloerrach.beaconlocation.models.Beacon;
import de.dhbwloerrach.beaconlocation.models.BeaconList;
import de.dhbwloerrach.beaconlocation.test.helpers.TestHelper;

/**
 * Created by David on 7/24/15.
 */
public class BeaconAdapterTest extends AndroidTestCase {
    private TestHelper helper = new TestHelper();
    private BeaconList beacons;
    private BeaconAdapter adapter;

    private static final int COUNT_BEACONS = 10;

    public BeaconAdapterTest() {
        super();
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();

        List<Beacon> beacons = helper.createBeacons(COUNT_BEACONS);

        adapter = new BeaconAdapter(getContext());
        adapter.addItems(beacons);

        this.beacons = new BeaconList();
        this.beacons.addAll(beacons);
    }

    public void testClearItems() throws Exception {
        adapter.clearItems();
        assertTrue(adapter.getCount() == 0);
    }

    public void testGetCount() throws Exception {
        assertEquals(adapter.getCount(), COUNT_BEACONS);
        assertEquals(adapter.getCount(), beacons.size());
    }

    public void testGetItem() throws Exception {
        beacons.SortByDistance();
        for (int index = 0; index < beacons.size(); index++) {
            assertEquals(adapter.getItem(index), beacons.get(index));
        }
    }

    public void testDistanceSorting() throws Exception {

    }

    public void testGetPosition() throws Exception {
        beacons.SortByDistance();
        for (int index = 0; index < beacons.size(); index++) {
            assertEquals(adapter.getPosition(beacons.get(index)), index);
        }
    }

    public void testGetView() {
        beacons.SortByDistance();
        for (int index = 0; index < beacons.size(); index++) {
            View view = adapter.getView(index, null, null);

            TextView viewMinor = (TextView) view.findViewById(R.id.minor);
            TextView viewDistance = (TextView) view.findViewById(R.id.distance);
            TextView viewUuid = (TextView) view.findViewById(R.id.uuid);
            TextView viewBluetoothName = (TextView) view.findViewById(R.id.bluetoothname);
            TextView viewTxpower = (TextView) view.findViewById(R.id.txpower);
            TextView viewRssi = (TextView) view.findViewById(R.id.rssi);
            TextView viewMajor = (TextView) view.findViewById(R.id.major);
            TextView viewBluetoothAddress = (TextView) view.findViewById(R.id.bluetoothaddress);

            assertNotNull(viewMinor);
            assertNotNull(viewDistance);
            assertNotNull(viewUuid);
            assertNotNull(viewBluetoothName);
            assertNotNull(viewTxpower);
            assertNotNull(viewRssi);
            assertNotNull(viewMajor);
            assertNotNull(viewBluetoothAddress);

            assertEquals(viewMinor.getText(), beacons.get(index).getMinor().toString());
            // Disable checks for distance value, distance is formatted "#m"
            // assertEquals(Double.parseDouble(viewDistance.getText().toString()), beacons.get(index).getDistance());
            assertEquals(viewUuid.getText(), beacons.get(index).getUuid());
            assertEquals(viewBluetoothName.getText(), beacons.get(index).getBluetoothName());
            assertEquals(viewTxpower.getText(), beacons.get(index).getTxpower().toString());
            assertEquals(viewRssi.getText(), beacons.get(index).getRssi().toString());
            assertEquals(viewMajor.getText(), beacons.get(index).getMajor().toString());
            assertEquals(viewBluetoothAddress.getText(), beacons.get(index).getBluetoothAddress());
        }
    }

    @Override
    protected void tearDown() throws Exception {
        super.tearDown();

        beacons.clear();
        adapter = null;
    }
}
