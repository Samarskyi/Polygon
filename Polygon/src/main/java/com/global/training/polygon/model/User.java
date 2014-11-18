package com.global.training.polygon.model;

import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * @author yurii.ostrovskyi
 */
@DatabaseTable(tableName = "users")
public class User {

    public final static String ID_FIELD = "user_id";
    public final static String ZONE_FIELD = "zone";
    public final static String FIRST_NAME_FIELD = "first_name";
    public final static String LAST_NAME_FIELD = "last_name";

    public User() {
    }

    @DatabaseField(generatedId = true)
    private int id;

    @DatabaseField(index = true, canBeNull = false, columnName = ID_FIELD)
    private int uid;

    @DatabaseField(dataType = DataType.STRING, columnName = ZONE_FIELD)
    private String zone;

    @DatabaseField(columnName = FIRST_NAME_FIELD)
    private String first_name;

    @DatabaseField(columnName = LAST_NAME_FIELD)
    private String last_name;

    private boolean isWorksNow;

    public User(String first_name, String last_name) {
        this.first_name = first_name;
        this.last_name = last_name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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
        return first_name + " " + last_name;
    }
}
