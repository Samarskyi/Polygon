package com.global.training.polygon.model;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.util.Date;

/**
 * @author eugenii.samarskyi on 14.11.2014.
 */
@DatabaseTable(tableName = "time_sheet")
public class RealWorksTime {

    public final static String ID_FIELD = "user_id";
    public final static String DATE_FIELD = "date";
    public final static String TOTAL_TIME_FIELD = "total_time";

    @DatabaseField(generatedId = true)
    private int id;

    @DatabaseField(index = true, canBeNull = false, columnName = ID_FIELD)
    private int user_id;

    @DatabaseField(columnName = DATE_FIELD)
    private Date date;

    @DatabaseField(columnName = TOTAL_TIME_FIELD)
    private long totalSpendTime;

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public long getTotalSpendTime() {
        return totalSpendTime;
    }

    public void setTotalSpendTime(long totalSpendTime) {
        this.totalSpendTime = totalSpendTime;
    }

    @Override
    public String toString() {

        long diffSeconds = totalSpendTime / 1000 % 60;
        long diffMinutes = totalSpendTime / (60 * 1000) % 60;
        long diffHours = totalSpendTime / (60 * 60 * 1000) % 24;
        long diffDays = totalSpendTime / (24 * 60 * 60 * 1000);

        return "RealWorksTime{" +
                "date = " + date +
                ", Hours : " + diffHours + " , Minutes : " + diffMinutes + " , Seconds: " + diffSeconds + '}';
    }
}
