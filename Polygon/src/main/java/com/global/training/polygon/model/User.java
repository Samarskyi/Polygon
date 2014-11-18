package com.global.training.polygon.model;

/**
 * @author yurii.ostrovskyi
 */
public class User {

    private int uid;
    private String zone;
    private String first_name;
    private String last_name;
    private boolean isWorksNow;

	public User(String first_name, String last_name) {
		this.first_name = first_name;
		this.last_name = last_name;
	}

	public int getUid() {
        return uid;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }

    public String getZone() {
        return zone;
    }

    public void setZone(String zone) {
        this.zone = zone;
    }

    public String getFirst_name() {
        return first_name;
    }

    public void setFirst_name(String first_name) {
        this.first_name = first_name;
    }

    public String getLast_name() {
        return last_name;
    }

    public void setLast_name(String last_name) {
        this.last_name = last_name;
    }

    public boolean isWorksNow() {
        return isWorksNow;
    }

    public void setWorksNow(boolean isWorksNow) {
        this.isWorksNow = isWorksNow;
    }

    @Override
    public String toString() {
        return "User{" +
                "uid=" + uid +
                ", zone='" + zone + '\'' +
                ", first_name='" + first_name + '\'' +
                ", last_name='" + last_name + '\'' +
                ", isWorksNow=" + isWorksNow +
                '}';
    }
}
