package de.dhbwloerrach.beaconlocation.bluetooth;

import org.altbeacon.beacon.*;
import org.altbeacon.beacon.Beacon;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Objects;

/**
 * Created by Lukas on 20.07.2015.
 */
public class BeaconNotifier implements RangeNotifier {

    private ArrayList<de.dhbwloerrach.beaconlocation.models.Beacon> beaconList = new ArrayList<>();
    IBeaconListView listView;

    public BeaconNotifier(IBeaconListView listView){
        this.listView = listView;
    }

    @Override
    public void didRangeBeaconsInRegion(Collection<Beacon> collection, Region region) {
        for(Beacon beacon : collection){
            de.dhbwloerrach.beaconlocation.models.Beacon existing;
            synchronized (beaconList) {
                existing = GetBeacon(beacon.getId1(), beacon.getId2(), beacon.getId3());
                if (existing == null) {
                    existing = new de.dhbwloerrach.beaconlocation.models.Beacon();
                    existing.setUuid(beacon.getId1().toString())
                            .setMajor(beacon.getId2().toInt())
                            .setMinor(beacon.getId3().toInt())
                            .setBluetoothName(beacon.getBluetoothName())
                            .setBluetoothAddress(beacon.getBluetoothAddress());
                    if (existing.getBluetoothName() == null || existing.getBluetoothName().isEmpty()) {
                        existing.setBluetoothName("iBeacon/AltBeacon");
                    }
                    beaconList.add(existing);
                }
            }
            existing.setDistance(beacon.getDistance())
                    .setTxpower(beacon.getTxPower())
                    .setRssi(beacon.getRssi())
                    .setLastSeen(new Date());
        }
        listView.RefreshList(beaconList);
    }

    private de.dhbwloerrach.beaconlocation.models.Beacon GetBeacon(Identifier uuid, Identifier major, Identifier minor){
        for(de.dhbwloerrach.beaconlocation.models.Beacon beacon : beaconList){
            if(Objects.equals(beacon.getUuid(), uuid.toString()) &&
                    beacon.getMajor() == major.toInt() &&
                    beacon.getMinor() == minor.toInt()) {
                return beacon;
            }
        }
        return null;
    }
}