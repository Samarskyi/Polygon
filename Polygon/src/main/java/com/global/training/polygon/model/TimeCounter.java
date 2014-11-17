package com.global.training.polygon.model;

import android.util.Log;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

/**
 * Created by eugenii.samarskyi on 14.11.2014.
 */
public class TimeCounter {

    static SimpleDateFormat formatter = new SimpleDateFormat("yyyy/MM/dd hh:mm:ss");

    private static int getDayNumber(String time) {
        Date date = null;
        try {
            date = formatter.parse(time);
        } catch (ParseException e) {
            Log.e(TimeCounter.class.getSimpleName(), time);
            e.printStackTrace();
        }

        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        int day = cal.get(Calendar.DAY_OF_MONTH);

        return day;
    }

    public static Date getDateFromString(String dateString) {
        Date date = null;
        try {
            date = formatter.parse(dateString);
        } catch (ParseException e) {
            Log.e(TimeCounter.class.getSimpleName(), dateString);
            e.printStackTrace();
        }
        return date;
    }

    public static List<RealWorksTime> getRealTime(List<WorksTime> list) {

        List<WorksTime> newList = list;

        for (final Iterator iterator = newList.iterator(); iterator.hasNext(); ) {
            WorksTime worksTime = (WorksTime) iterator.next();
            if (!worksTime.isWorking()) {
                iterator.remove();
            }
        }

        List<List<WorksTime>> fullList = new ArrayList<List<WorksTime>>();
        int day = 0;
        int lastDay;
        for (WorksTime worksTimeL : newList) {
            lastDay = getDayNumber(worksTimeL.getTimestamp());
            if (day == lastDay) {
                continue;
            }

            List<WorksTime> bunchOfDate = new ArrayList<WorksTime>();

            for (WorksTime worksTimeInner : newList) {
                if (getDayNumber(worksTimeInner.getTimestamp()) == lastDay) {
                    bunchOfDate.add(worksTimeInner);
                }
            }

            if (bunchOfDate.size() != 0) {
                fullList.add(bunchOfDate);
            }
            day = lastDay;
        }

        List<RealWorksTime> realWorksTimeList = new ArrayList<RealWorksTime>();

        for (List<WorksTime> times : fullList) {
            Date date = getDateFromString(times.get(0).getTimestamp());
            RealWorksTime realWorksTime = new RealWorksTime();
            realWorksTime.setDate(date);

            String timeStampOut = null;
            String timeStampIn = null;
            boolean out = false;
            boolean in = false;

            long totalWorks = 0;

            for (int i = times.size()-1; i >= 0; i--) {
                WorksTime worksTime = times.get(i);

                if (worksTime.getDirection().equals("out")) {
                    timeStampOut = worksTime.getTimestamp();
                    out = true;
                } else if (worksTime.getDirection().equals("in")) {
                    timeStampIn = worksTime.getTimestamp();
                    in = true;
                }

                if (out && in) {
                    totalWorks += (getDateFromString(timeStampOut).getTime() - getDateFromString(timeStampIn).getTime());
                    in = false;
                    out = false;
                }
            }
            realWorksTime.setTotalSpendTime(totalWorks);
            realWorksTimeList.add(realWorksTime);
        }

        return realWorksTimeList;
    }

}
