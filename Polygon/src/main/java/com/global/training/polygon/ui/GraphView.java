package com.global.training.polygon.ui;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;

import com.global.training.polygon.R;
import com.global.training.polygon.model.RealWorksTime;
import com.global.training.polygon.model.TimeCounter;

import org.joda.time.MutablePeriod;

import java.util.Calendar;
import java.util.Collections;
import java.util.List;

/**
 * Created by sgv on 02.05.2015.
 */
public class GraphView extends View implements ViewTreeObserver.OnPreDrawListener {

    private int dayCount;
    private int maxVisibleDayCount;

    private int necessaryHourWork;
    private int maxHourPerDay;

    private Paint gridPaint;
    private Paint hoursPaint;
    private Paint necessaryWorkedColorLine;
    private int separator;
    private int average;
    private int maxW;
    private int maxH;
    private int squareHeight;
    private int squareMargin;
    private int color;
    private int graphMorgin;
    private int graphMorginBott;
    private int spaceBetweenHourRectangle;
    private int paddingFirstHourGraph;
    private int totalTimeSpendHeight;
    private float shift;
    private float hourSeparator;
    private float hourInPixel;
    private float totalStartX;
    private int workedDaysCount;
    private long workedHoursInCurrentTime;

    List<RealWorksTime> hoursWorked;
    String[] dyaNames = {"Mon", "Tue", "Wed", "Thu", "Fri"};

    public GraphView(Context context) {
        super(context);
        try {
            TypedArray typedArray = context.obtainStyledAttributes(R.styleable.GraphView);
            color = typedArray.getColor(R.styleable.GraphView_main_color, Color.BLUE);
            typedArray.recycle();
        } catch (Exception e) {
            e.printStackTrace();
        }
        initPaint();
        initFromResource();
        getViewTreeObserver().addOnPreDrawListener(this);
    }

    public GraphView(Context context, AttributeSet attrs) {
        super(context, attrs);
        try {
            TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.GraphView);
            color = typedArray.getColor(R.styleable.GraphView_main_color, Color.BLUE);
            typedArray.recycle();
        } catch (Exception e) {
            e.printStackTrace();
        }
        initPaint();
        initFromResource();
        getViewTreeObserver().addOnPreDrawListener(this);

    }

    public void setHoursWorked(List<RealWorksTime> hours) {
        hoursWorked = hours;
        TimeCounter.convertToFullWeek(hoursWorked);
        Collections.sort(hoursWorked, new RealWorksTime.CustomComparator());
        invalidate();

        workedDaysCount = 0;
        workedHoursInCurrentTime = 0;
        Calendar calendar = Calendar.getInstance();
        boolean first = true;
        MutablePeriod period = null;
        for (RealWorksTime realWorksTime : hoursWorked) {
            if (realWorksTime.getTotalSpendTime() > 0) {
                workedDaysCount++;
                if (first) {
//                    calendar.;
                    first = false;
//                    calendar.set(Calendar.HOUR_OF_DAY, (int) TimeCounter.getHours(realWorksTime.getTotalSpendTime()));
//                    calendar.set(Calendar.MINUTE, (int) TimeCounter.getMinutes(realWorksTime.getTotalSpendTime()));

//                    DateTime dateTime = new DateTime(calendar.getTimeInMillis());

                    int tempHours = (int) TimeCounter.getHours(realWorksTime.getTotalSpendTime());
                    int tempMinutes = (int) TimeCounter.getMinutes(realWorksTime.getTotalSpendTime());
                    period = new MutablePeriod(tempHours, tempMinutes, 0, 0);
//                    Log.d("XXX", "Init time : " + new Date(calendar.getTimeInMillis()));
                    Log.d("XXX", "Init time Joda - H: " + period.getHours() + ", M : " + period.getMinutes());

//                    Log.d("XXX", "Init time H: " + TimeCounter.getHours(realWorksTime.getTotalSpendTime()));
//                    Log.d("XXX", "Init time M: " + TimeCounter.getMinutes(realWorksTime.getTotalSpendTime()));
                } else {
//                    calendar.add(Calendar.HOUR_OF_DAY, (int) TimeCounter.getHours(realWorksTime.getTotalSpendTime()));
//                    calendar.add(Calendar.MINUTE, (int) TimeCounter.getMinutes(realWorksTime.getTotalSpendTime()));
//                    calendar.add(Calendar.MINUTE, (int) TimeUnit.MILLISECONDS(realWorksTime.getTotalSpendTime()));
//                    workedHoursInCurrentTime = TimeUnit.MILLISECONDS.toHours(calendar.getTimeInMillis());
                    int tempHours = (int) TimeCounter.getHours(realWorksTime.getTotalSpendTime());
                    int tempMinutes = (int) TimeCounter.getMinutes(realWorksTime.getTotalSpendTime());
//                    period.addHours(tempHours);
//                    period.addMinutes(tempMinutes);
                    period.add(realWorksTime.getTotalSpendTime());
//                    period.plusMillis((int) realWorksTime.getTotalSpendTime());
//                    period.addHours(period.getMinutes()%60);
//                    int minutes = period.getMinutes();
//                    period.addMinutes();
                    Log.d("XXX", "ADD Joda - H: " + period.getHours() + ", M : " + period.getMinutes());

//                    Log.d("XXX", "After added : " + Float.parseFloat((TimeCounter.convertToTimeRegular(calendar.getTimeInMillis()))));
                }
            }
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        drawSimpleGrid(canvas);
        drawHours(canvas);
        drawGraphic(canvas);
    }

    private void drawHours(Canvas canvas) {

        int left = separator;
        int right = left + average;
        Rect rect = new Rect();
        String text = "TOTAL HOURS WORKED 8:45 of 16";
        gridPaint.getTextBounds(text, 0, text.length() - 1, rect);

        // draw total time
        canvas.drawRect(squareMargin, squareMargin, maxW - squareMargin, totalTimeSpendHeight, hoursPaint);
        canvas.drawText(text, squareMargin * 2, (totalTimeSpendHeight - (squareMargin + rect.height()) / 2), gridPaint);

        // draw detail info
        Rect currInfoRect = new Rect();
        for (int i = 0; i < 3; i++) {
            currInfoRect.set(left, squareMargin + totalTimeSpendHeight, right, squareHeight + totalTimeSpendHeight);
            canvas.drawRect(currInfoRect, hoursPaint);
            if (i == 1) {
                String time = workedDaysCount + "/5";
                gridPaint.getTextBounds(time, 0, time.length() - 1, rect);
                float textSize = textSizeInPixels(time, gridPaint);
                canvas.drawText(time, currInfoRect.centerX() - (textSize / 2), currInfoRect.centerY() + (rect.height() / 2), gridPaint);
            }
            if (i == 0) {
                String timeText = TimeCounter.getShortDateFromLong(workedHoursInCurrentTime);
                gridPaint.getTextBounds(timeText, 0, timeText.length() - 1, rect);
                float textSize = textSizeInPixels(timeText, gridPaint);
                canvas.drawText(timeText, currInfoRect.centerX() - (textSize / 2), currInfoRect.centerY() + (rect.height() / 2), gridPaint);
            }
            left = left + average + separator;
            right = left + average;
        }
    }

    private void drawSimpleGrid(Canvas canvas) {
        canvas.drawColor(color);

        float startY = maxH - graphMorginBott;
        int startX = squareMargin + graphMorgin;
        int endX = maxW - squareMargin - graphMorgin;
        int hour = 0;

        // draw time lines
        for (int i = 0; i < 7; i++) {
            if (i == 4) {
                hoursPaint.setColor(Color.RED);
                canvas.drawLine(startX, startY, endX, startY, hoursPaint);
                hoursPaint.setColor(Color.WHITE);
            } else {
                canvas.drawLine(startX, startY, endX, startY, hoursPaint);
            }
            canvas.drawText(String.valueOf(hour), squareMargin, startY, hoursPaint);
            hour += 2;
            startY -= hourSeparator;
        }
    }

    private void drawGraphic(Canvas canvas) {
        float startY = maxH - graphMorginBott;
        float startX = totalStartX;
        int endX = maxW - squareMargin - graphMorgin;
        int totalSpaceForRectangles = (int) (endX - startX);

        int spaceForSingleRectangle = totalSpaceForRectangles / 5;

        float currentHours;

        // draw time graphic (rectangles)
        if (hoursWorked != null) {
            for (int days = 0; days < hoursWorked.size(); days++) {
                float hours = Float.parseFloat((TimeCounter.convertToTimeRegular(hoursWorked.get(days).getTotalSpendTime())));
                if (hours > -1) {
                    currentHours = hours * hourInPixel;
                    canvas.drawRect(startX, startY - currentHours, startX + spaceForSingleRectangle - spaceBetweenHourRectangle, startY, hoursPaint);
                }
                startX += spaceForSingleRectangle;
            }
        }

        // draw day names
        startX = totalStartX;
        for (int days = 0; days < 5; days++) {
            float textSize = textSizeInPixels(dyaNames[days], hoursPaint);
            float shift1 = (spaceForSingleRectangle - textSize - spaceBetweenHourRectangle) / 2;
            canvas.drawText(dyaNames[days], startX + shift1, startY + 25, hoursPaint);
            startX += spaceForSingleRectangle;
        }
    }

    private float textSizeInPixels(String text, Paint paint){
        float size = paint.measureText(text);
        return size;
    }

    private void initPaint() {
        gridPaint = new Paint();
        gridPaint.setColor(color);
        gridPaint.setTextSize(36);
        gridPaint.setAntiAlias(true);

        hoursPaint = new Paint();
        hoursPaint.setColor(Color.WHITE);
        hoursPaint.setStrokeWidth(3);
        hoursPaint.setTextSize(25);
        hoursPaint.setAntiAlias(true);
    }

    private void initFromResource() {
        separator = (int) getResources().getDimension(R.dimen.separator);
        squareHeight = (int) getResources().getDimension(R.dimen.square_height);
        squareMargin = (int) getResources().getDimension(R.dimen.margin_square_time);
        graphMorgin = (int) getResources().getDimension(R.dimen.graph_margin);
        graphMorginBott = (int) getResources().getDimension(R.dimen.graph_margin_bottom);
        spaceBetweenHourRectangle = (int) getResources().getDimension(R.dimen.space_between_hour_rectangle);
        paddingFirstHourGraph = (int) getResources().getDimension(R.dimen.first_hours_graph_padding);
        totalTimeSpendHeight = (int) getResources().getDimension(R.dimen.total_time_height);
    }

    private void initSizes() {
        maxH = getHeight();
        maxW = getWidth();
        average = (maxW - (separator * 4)) / 3;
        shift = maxH - graphMorginBott - squareHeight - squareMargin * 2 - totalTimeSpendHeight;
        hourSeparator = shift / 6;
        hourInPixel = shift / 12;
        totalStartX = squareMargin + graphMorgin + paddingFirstHourGraph;
    }

    @Override
    public boolean onPreDraw() {
        Log.d("XXX", "OnPreDraw");
        getViewTreeObserver().removeOnPreDrawListener(this);
        initSizes();
        return true;
    }

}

