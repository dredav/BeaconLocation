package de.dhbwloerrach.beaconlocation.models;

/**
 * Created by Salvo on 23.07.2015.
 */
public class Machine {


    private String name;
    private Integer id;


    public Integer getId() {
        return id;
    }

    public Machine seIid(Integer id) {
        this.id = id;
        return this;

    }

    public String getName() {
        return name;
    }

    public Machine setName(String name) {
        this.name = name;
        return this;

    }




}
