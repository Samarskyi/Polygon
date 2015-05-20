package com.global.training.polygon.model;

import android.util.Log;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

/**
 * @author eugenii.samarskyi on 14.11.2014.
 */
public class TimeCounter {

    static SimpleDateFormat formatter = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss", Locale.getDefault());
    static SimpleDateFormat shortFormatter = new SimpleDateFormat("yyyy/MM/dd");
    static SimpleDateFormat hourMinutesFormatter = new SimpleDateFormat("HH:mm");
    static int[] days = {Calendar.MONDAY, Calendar.TUESDAY, Calendar.WEDNESDAY, Calendar.THURSDAY, Calendar.FRIDAY};

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

    public static String getAverage(int minutes, int days) {
        int fullHours = 0;
        int res = 0;
        if (days > 0) {
            int averageMinutes = minutes / days;
            fullHours = averageMinutes / 60;
            double minutes2 = averageMinutes % 60;
            minutes2 = minutes2 / 100 * 60;
            res = (int) minutes2;
        }
        return fullHours + "h " + res + "m";
    }

    private static double round(double value, int places) {
        if (places < 0) throw new IllegalArgumentException();

        long factor = (long) Math.pow(10, places);
        value = value * factor;
        long tmp = Math.round(value);
        return (double) tmp / factor;
    }

    public static Date getShortDateFromString(String dateString) {
        Date date = null;
        try {
            date = shortFormatter.parse(dateString);
        } catch (ParseException e) {
            Log.e(TimeCounter.class.getSimpleName(), dateString);
            e.printStackTrace();
        }
        return date;
    }

    public static String getShortDateFromLong(long dateString) {
        Date date = null;
        String dateText = null;
        try {
            date = new Date(dateString);
            dateText = hourMinutesFormatter.format(date);
        } catch (Exception e) {

            e.printStackTrace();
        }
        return dateText;
    }
    public static List<RealWorksTime> getRealTime(List<WorksTime> list, final int userId) {

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
            realWorksTime.setUser_id(userId);
            realWorksTime.setDate(date);

            String timeStampOut = null;
            String timeStampIn = null;
            boolean out = false;
            boolean in = false;
            String lastMove = "";
            long totalWorks = 0;
            int teleports = 0;

            for (int i = 0; i <= times.size() - 1 ; i++) {
                WorksTime worksTime = times.get(i);
                Log.d("XXX","Pasing real worked time: " + worksTime.getTimestamp());

                if (worksTime.getDirection().equals("out")) {
                    timeStampOut = worksTime.getTimestamp();
                    out = true;

                } else if (worksTime.getDirection().equals("in")) {

                    in = true;

                    if(lastMove.equals("in")){
                        timeStampOut = worksTime.getTimestamp();
                        out = true;
                        teleports++;
                    }else{
                        timeStampIn = worksTime.getTimestamp();
                        lastMove = "in";
                    }
                }

                if (out && in) {
                    Log.d("XXX", "Pasing real worked time, minus : " + getDateFromString(timeStampOut) +" - " +getDateFromString(timeStampIn));
                    long diff = (getDateFromString(timeStampOut).getTime() - getDateFromString(timeStampIn).getTime());
                    totalWorks += diff;
                    in = false;
                    out = false;
                    lastMove = "";
                    String s = getWorkedTime(diff);
                }
            }
            Log.d("XXX", "total spend time per " + getHours(totalWorks) + ":"+ getMinutes(totalWorks));
            realWorksTime.setTotalSpendTime(totalWorks);
            realWorksTime.setTeleport(teleports);
            realWorksTimeList.add(realWorksTime);
        }

        return realWorksTimeList;
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

    private static String getWorkedTime(long totalWorks){
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(totalWorks);
        return   " "+calendar.get(Calendar.HOUR_OF_DAY) + ":" + calendar.get(Calendar.MINUTE);
    }

    private static String diff(long time){
        long diff = time;
        long diffSeconds = diff / 1000 % 60;
        long diffMinutes = diff / (60 * 1000) % 60;
        long diffHours = diff / (60 * 60 * 1000);
       return ""+ diffHours + ":" + diffMinutes;
    }

    public static int getHoursFromMinutes(int min) {
        int h = min / 60;
        return h;
    }

    public static int getMinutesFromSeconds(int sec) {
        Log.d("XXX", "convert sec to min, src : " + sec + ", result : " + (sec / 60));
        return sec / 60;
    }
    public static int getMinutesWithoutHours(int min) {
        int m = min % 60;
        return m;
    }

    public static String convertToTimeRegular(long millis) {
        if (millis < 0) {
            return String.format("-%d.%02d",
                    Math.abs(TimeUnit.MILLISECONDS.toHours(millis)),
                    Math.abs(TimeUnit.MILLISECONDS.toMinutes(millis) - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(millis)) - 1)
            );
        }
        return String.format("%d.%02d",
                TimeUnit.MILLISECONDS.toHours(millis),
                TimeUnit.MILLISECONDS.toMinutes(millis) - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(millis))
        );
    }

    public static long getHours(long millis) {
        return TimeUnit.MILLISECONDS.toHours(millis);
    }

    public static long getMinutes(long millis){
        return  TimeUnit.MILLISECONDS.toMinutes(millis) - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(millis));
    }

    public static String convertToTimeOracle(long millis) {
        return String.format("%d.%02d",
                TimeUnit.MILLISECONDS.toHours(millis),
                Math.abs((int) ((TimeUnit.MILLISECONDS.toMinutes(millis) -
                        TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(millis))) * 1.67)));
    }


    public static void convertToFullWeek(List<RealWorksTime> mTimeSheetList) {

        if (mTimeSheetList != null && mTimeSheetList.size() < 5) {
            Calendar calendar = Calendar.getInstance();

            for (int day : days) {
                boolean needAdd = true;
                for (RealWorksTime worksTime : mTimeSheetList) {
                    calendar.setTime(worksTime.getDate());
                    int dayId = calendar.get(Calendar.DAY_OF_WEEK);
                    if (day == dayId) {
                        needAdd = false;
                    }
                }

                if (needAdd) {
                    Calendar dayOfWeek = Calendar.getInstance();
                    dayOfWeek.set(Calendar.DAY_OF_WEEK, day);
                    mTimeSheetList.add(new RealWorksTime(dayOfWeek.getTime(), -1));
                }
            }
        }
    }
}

