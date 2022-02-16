package com.example.indoorlocalization.EntityClass;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.io.Serializable;

@Entity(tableName = "Training")
public class TrainingEntity implements Serializable {

    @PrimaryKey(autoGenerate = true)
    private int ID;

    @ColumnInfo(name = "Location")
    private String location;

    @ColumnInfo(name = "Orientation")
    private String orientation;

    @ColumnInfo(name = "RSSI_1")
    private int rssi_1;

    @ColumnInfo(name = "RSSI_2")
    private int rssi_2;

    @ColumnInfo(name = "RSSI_3")
    private int rssi_3;

    public TrainingEntity(String location, String orientation, int rssi_1, int rssi_2, int rssi_3) {
        this.location = location;
        this.orientation = orientation;
        this.rssi_1 = rssi_1;
        this.rssi_2 = rssi_2;
        this.rssi_3 = rssi_3;
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getOrientation() {
        return orientation;
    }

    public void setOrientation(String orientation) {
        this.orientation = orientation;
    }

    public int getRssi_1() {
        return rssi_1;
    }

    public void setRssi_1(int rssi_1) {
        this.rssi_1 = rssi_1;
    }

    public int getRssi_2() {
        return rssi_2;
    }

    public void setRssi_2(int rssi_2) {
        this.rssi_2 = rssi_2;
    }

    public int getRssi_3() {
        return rssi_3;
    }

    public void setRssi_3(int rssi_3) {
        this.rssi_3 = rssi_3;
    }
}
