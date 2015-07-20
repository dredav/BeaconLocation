package de.dhbwloerrach.beaconlocation;

/**
 * Created by alirei on 20.07.2015.
 */
public class Beacon {
    private String uuid;
    private Integer major;
    private Integer minor;

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
}
