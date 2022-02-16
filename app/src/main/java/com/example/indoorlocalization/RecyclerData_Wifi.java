package com.example.indoorlocalization;

public class RecyclerData_Wifi {

    private String name;
    private String rssi;

    public RecyclerData_Wifi(String name, String rssi) {
        this.name = name;
        this.rssi = rssi;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRssi() {
        return rssi;
    }

    public void setRssi(String rssi) {
        this.rssi = rssi;
    }

}
