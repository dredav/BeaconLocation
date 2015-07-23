package de.dhbwloerrach.beaconlocation;

/**
 * Created by alirei on 20.07.2015.
 */
public class Beacon {
    private String uuid;
    private String major;
    private String minor;
    private double distance;
    private String blutoothname;
    private Integer txpower;
    private Integer rssi;


    public String getUuid() {
        return uuid;
    }

    public Beacon setUuid(String uuid) {
        this.uuid = uuid;
        return this;
    }

    public String getMajor() {
        return major;
    }

    public Beacon setMajor(String major) {
        this.major = major;
        return this;
    }

    public String getMinor() {
        return minor;
    }

    public Beacon setMinor(String minor) {
        this.minor = minor;
        return this;
    }

    public double getDistance() {
        return distance;
    }

    public Beacon setDistance(double distance) {
        this.distance = distance;
        return this;
    }

    public String getBlutoothname() {
        return blutoothname;
    }

    public Beacon setBlutoothname(String blutoothname) {
        this.blutoothname = blutoothname;
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
        return this;
    }
}
