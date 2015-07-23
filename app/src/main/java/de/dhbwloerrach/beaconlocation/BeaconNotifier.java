package de.dhbwloerrach.beaconlocation;

import org.altbeacon.beacon.*;
import org.altbeacon.beacon.Beacon;

import java.security.Timestamp;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Objects;

/**
 * Created by Lukas on 20.07.2015.
 */
public class BeaconNotifier implements RangeNotifier, MonitorNotifier {

    private ArrayList<de.dhbwloerrach.beaconlocation.Beacon> beaconList = new ArrayList<>();
    IBeaconListView listView;

    public BeaconNotifier(IBeaconListView listView){
        this.listView = listView;
    }

    @Override
    public void didRangeBeaconsInRegion(Collection<Beacon> collection, Region region) {
        for(Beacon beacon : collection){
            de.dhbwloerrach.beaconlocation.Beacon existing = GetBeacon(beacon.getId1(), beacon.getId2(), beacon.getId3());
            if(existing == null) {
                de.dhbwloerrach.beaconlocation.Beacon current = new de.dhbwloerrach.beaconlocation.Beacon();
                current.setUuid(beacon.getId1().toString())
                        .setMajor(beacon.getId2().toString())
                        .setMinor(beacon.getId3().toString())
                        .setDistance(beacon.getDistance())
                        .setBluetoothName(beacon.getBluetoothName())
                        .setTxpower(beacon.getTxPower())
                        .setRssi(beacon.getRssi())
                        .setBluetoothAddress(beacon.getBluetoothAddress())
                        .setLastSeen(new Date());
                if (current.getBluetoothName() == null || current.getBluetoothName().isEmpty()){
                    current.setBluetoothName("iBeacon/AltBeacon");
                }
                beaconList.add(current);
            } else {
                existing.setDistance(beacon.getDistance())
                        .setLastSeen(new Date());
            }
        }
        listView.RefreshList(beaconList);
    }

    private de.dhbwloerrach.beaconlocation.Beacon GetBeacon(Identifier uuid, Identifier major, Identifier minor){
        for(de.dhbwloerrach.beaconlocation.Beacon beacon : beaconList){
            if(Objects.equals(beacon.getUuid(), uuid.toString()) &&
                    Objects.equals(beacon.getMajor(), major.toString()) &&
                    Objects.equals(beacon.getMinor(), minor.toString())) {
                return beacon;
            }
        }
        return null;
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
