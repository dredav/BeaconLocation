package de.dhbwloerrach.beaconlocation.models;

import java.util.Date;

/**
 * Created by alirei on 20.07.2015.
 */
public class Beacon {
    private String uuid;
    private double distance;
    private String bluetoothName;
    private Integer txpower;
    private Integer rssi;
    private RssiList rssis = new RssiList();
    private String bluetoothAddress;
    private DistanceList distances = new DistanceList();
    private Date lastSeen;
    private Integer major;
    private Integer minor;
    private Integer id;
    private Integer machineId;

    public String getUuid() {
        return uuid;
    }

    public Beacon setUuid(String uuid) {
        this.uuid = uuid;
        return this;
    }

    public Integer getMajor() {
        return major;
    }

    public Beacon setMajor(Integer major) {
        this.major = major;
        return this;
    }

    public Integer getMinor() {
        return minor;
    }

    public Beacon setMinor(Integer minor) {
        this.minor = minor;
        return this;
    }

    public double getDistance() {
        return distance;
    }

    public Beacon setDistance(double distance) {
        this.distance = distance;
        this.distances.add(new TimedDistance(distance));
        return this;
    }

    public String getBluetoothName() {
        return bluetoothName;
    }

    public Beacon setBluetoothName(String bluetoothName) {
        this.bluetoothName = bluetoothName;
        return this;
    }

    public Integer getTxpower() {
        return txpower;
    }

    public Beacon setTxpower(Integer txpower) {
        this.txpower = txpower;
        return this;
    }

    public Integer getRssi() {
        return rssi;
    }

    public Beacon setRssi(Integer rssi) {
        this.rssi = rssi;
        this.rssis.add(new TimedRssi(rssi));
        return this;
    }

    public String getBluetoothAddress() {
        return bluetoothAddress;
    }

    public Beacon setBluetoothAddress(String bluetoothAddress) {
        this.bluetoothAddress = bluetoothAddress;
        return this;
    }

    public boolean hasDistanceIn(int seconds) {
        return !this.distances.getLast(seconds).isEmpty();
    }

    public boolean hasRssiIn(int seconds) {
        return !this.rssis.getLast(seconds).isEmpty();
    }

    public double getAverageDistance(int seconds) {
        DistanceList last = this.distances.getLast(seconds);
        if(last.size() > 0)
            return last.getAverageDistance();
        else {
            return this.getDistance();
        }
    }

    public RssiList getRssis(int seconds) {
        return this.rssis.getLast(seconds);
    }

    public Date getLastSeen() {
        return lastSeen;
    }

    public Beacon setLastSeen(Date lastSeen) {
        this.lastSeen = lastSeen;
        return this;
    }

    public Integer getId() {
        return id;
    }

    public Beacon setId(Integer id) {
        this.id = id;
        return this;
    }

    public Integer getMachineId() {
        return machineId;
    }

    public Beacon setMachineId(Integer machineId) {
        this.machineId = machineId;
        return this;
    }

    @Override
    public String toString() {
        return this.getUuid() + " " + this.getMajor() + " " + this.getMinor();
    }
}
