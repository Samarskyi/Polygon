package com.global.training.polygon.model;

/**
 * Created by eugenii.samarskyi on 14.11.2014.
 */

public class WorksTime {

    String timestamp;
    String direction;
    int locationid;
    String area;
    boolean working;

    public WorksTime() {
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getDirection() {
        return direction;
    }

    public void setDirection(String direction) {
        this.direction = direction;
    }

    public int getLocationid() {
        return locationid;
    }

    public void setLocationid(int locationid) {
        this.locationid = locationid;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public boolean isWorking() {
        return working;
    }

    public void setWorking(boolean working) {
        this.working = working;
    }

    @Override
    public String toString() {
        return "WorksTime{" +
                "timestamp='" + timestamp + '\'' +
                ", direction='" + direction + '\'' +
                ", locationid=" + locationid +
                ", area='" + area + '\'' +
                ", working=" + working +
                '}';
    }
}
