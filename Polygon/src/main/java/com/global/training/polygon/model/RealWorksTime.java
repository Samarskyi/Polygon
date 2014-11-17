package com.global.training.polygon.model;

import java.util.Date;

/**
 * Created by eugenii.samarskyi on 14.11.2014.
 */
public class RealWorksTime {

   private Date date;
   private long totalSpendTime;


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
                ", Hours : " + diffHours + " , Minutes : " + diffMinutes +  " , Seconds: " + diffSeconds + '}';
    }
}
