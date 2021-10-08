package com.friertech.pcremote;

public class DevicesModel {
    private int id;
    private String name;
    private String mac;
    private String ip;
    private String broadcast;
    private boolean isActive;

    //constructors

    public DevicesModel(int id, String name, String mac, String ip, String broadcast, boolean isActive) {
        this.id = id;
        this.name = name;
        this.mac = mac;
        this.ip = ip;
        this.broadcast = broadcast;
        this.isActive = isActive;
    }
    public DevicesModel(){

    }


    //toString is necessary for printing the contents of a class object
    @Override
    public String toString() {
        return "Device Name: "+name + '\n' +
                "MAC: " +mac+'\n'+
                "IPv4: "+ip+'\n'+
                "Broadcast: "+broadcast+'\n'+
                "Active: "+isActive;
    }


    //getters and setters

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMac() {
        return mac;
    }

    public void setMac(String mac) {
        this.mac = mac;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getBroadcast() {
        return broadcast;
    }

    public void setBroadcast(String broadcast) {
        this.broadcast = broadcast;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }
}
