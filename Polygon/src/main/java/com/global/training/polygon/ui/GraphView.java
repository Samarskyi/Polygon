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

import java.util.Collections;
import java.util.List;

/**
 * Created by sgv on 02.05.2015.
 */
public class GraphView extends View implements ViewTreeObserver.OnPreDrawListener {

    private Paint gridPaint;
    private Paint hoursPaint;
    private Paint textPaint;
    private Paint smallTextPaint;
    private Paint timeGraphicPaint;
    private int separator;
    private int average;
    private int maxW;
    private int maxH;
    private int squareHeight;
    private int squareMargin;
    private int color;
    private int graphMargin;
    private int graphMarginBott;
    private int spaceBetweenHourRectangle;
    private int paddingFirstHourGraph;
    private int totalTimeSpendHeight;
    private int workedDaysCount = -1;
    private int teleports;
    private float shift;
    private float hourSeparator;
    private float hourInPixel;
    private float totalStartX;

    private MutablePeriod period;
    private List<RealWorksTime> hoursWorked;
    private String[] dyaNames = {"Mon", "Tue", "Wed", "Thu", "Fri"};
    private Rect currInfoRect = new Rect();
    private Rect totalRect = new Rect();
    private Rect textBounds = new Rect();

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
        teleports = 0;
        if (hoursWorked != null) {

            Log.d("XXX", "Before converting to full week and sorting, size:" + hoursWorked.size());
            for (RealWorksTime realWorksTime : hoursWorked) {
                Log.d("XXX", "order : " + realWorksTime.getDate());
            }
            TimeCounter.convertToFullWeek(hoursWorked);
            Collections.sort(hoursWorked, new RealWorksTime.CustomComparator());

            Log.d("XXX", "After converting to full week and sorting, size:" + hoursWorked.size());
            for (RealWorksTime realWorksTime : hoursWorked) {
                Log.d("XXX", "order : " + realWorksTime.getDate());
            }
            invalidate();

            workedDaysCount = 0;
            boolean first = true;

            for (RealWorksTime realWorksTime : hoursWorked) {
                if (realWorksTime.getTotalSpendTime() > 0) {
                    workedDaysCount++;
                    teleports += realWorksTime.getTeleport();
                    if (first) {
                        first = false;

                        int tempHours = (int) TimeCounter.getHours(realWorksTime.getTotalSpendTime());
                        int tempMinutes = (int) TimeCounter.getMinutes(realWorksTime.getTotalSpendTime());
                        period = new MutablePeriod(tempHours, tempMinutes, 0, 0);
                        Log.d("XXX", "Init time Joda - H: " + period.getHours() + ", M : " + period.getMinutes());

                    } else {
                        period.add(realWorksTime.getTotalSpendTime());
                        Log.d("XXX", "ADD Joda - H: " + period.getHours() + ", M : " + period.getMinutes() + ", S : " + period.getSeconds());
                    }
                }
            }
            if (period != null && period.getSeconds() > 59) {
                int seconds = period.getSeconds();
                period.addMinutes(TimeCounter.getMinutesFromSeconds(seconds));
                period.setSeconds(0);
                Log.d("XXX", "Convert Sec to Min Joda - H: " + period.getHours() + ", M : " + period.getMinutes() + ", S : " + period.getSeconds());
            }
            if (period != null && period.getMinutes() > 59) {
                int minutes = period.getMinutes();
                period.addHours(TimeCounter.getHoursFromMinutes(minutes));
                period.setMinutes(TimeCounter.getMinutesWithoutHours(minutes));
                Log.d("XXX", "Convert Min to Hour Joda - H: " + period.getHours() + ", M : " + period.getMinutes() + ", S : " + period.getSeconds());
            }
            try {
                Log.d("XXX", "Result Joda - H: " + period.getHours() + ", M : " + period.getMinutes() + ", S : " + period.getSeconds());
            } catch (Exception e) {
                e.printStackTrace();
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

        totalRect.set(squareMargin, squareMargin, maxW - squareMargin, totalTimeSpendHeight);
        // draw total time
        canvas.drawRect(totalRect, hoursPaint);
        if (period != null && workedDaysCount > 0) {
            String text = "TOTAL HOURS WORKED " + period.getHours() + ":" + period.getMinutes() + " of " + (workedDaysCount * 8);
            gridPaint.getTextBounds(text, 0, text.length() - 1, textBounds);
            int size = totalRect.width();
            int totalNeedHoursWorks = workedDaysCount * 8;
            int oneHourInPixels = size / totalNeedHoursWorks;
            oneHourInPixels *= period.getHours();
            canvas.drawRect(squareMargin, squareMargin, maxW - squareMargin - (size - oneHourInPixels), totalTimeSpendHeight, timeGraphicPaint);
            canvas.drawText(text, squareMargin * 2, (totalTimeSpendHeight - (squareMargin + textBounds.height()) / 2), textPaint);
        }

        // draw detail info

        for (int i = 0; i < 3; i++) {
            currInfoRect.set(left, squareMargin + totalTimeSpendHeight, right, squareHeight + totalTimeSpendHeight);
            canvas.drawRect(currInfoRect, hoursPaint);

            if (i == 0 && period != null) {
                String timeText = TimeCounter.getAverage(period.getHours() * 60 + period.getMinutes(), workedDaysCount);
                gridPaint.getTextBounds(timeText, 0, timeText.length() - 1, textBounds);
                float textSize = textSizeInPixels(timeText, gridPaint);
                canvas.drawText(timeText, currInfoRect.centerX() - (textSize / 2), currInfoRect.centerY() + (textBounds.height() / 2), textPaint);
                textSize = textSizeInPixels("DAILY AVERAGE", smallTextPaint);
                canvas.drawText("DAILY AVERAGE", currInfoRect.centerX() - (textSize / 2), currInfoRect.centerY() + (textBounds.height()) + 10, smallTextPaint);
            } else if (i == 1 && workedDaysCount != -1) {
                String time = workedDaysCount + "/5";
                gridPaint.getTextBounds(time, 0, time.length() - 1, textBounds);
                float textSize = textSizeInPixels(time, gridPaint);
                canvas.drawText(time, currInfoRect.centerX() - (textSize / 2), currInfoRect.centerY() + (textBounds.height() / 2), textPaint);
                String daysWorked = "DAYS WORKED";
                textSize = textSizeInPixels(daysWorked, smallTextPaint);
                canvas.drawText(daysWorked, currInfoRect.centerX() - (textSize / 2), currInfoRect.centerY() + (textBounds.height()) + 10, smallTextPaint);
            } else if (i == 2 && period != null) {
                String teleportsValue = teleports + " ";
                gridPaint.getTextBounds(teleportsValue, 0, teleportsValue.length() - 1, textBounds);
                float textSize = textSizeInPixels(teleportsValue, gridPaint);
                canvas.drawText(teleportsValue, currInfoRect.centerX() - (textSize / 2), currInfoRect.centerY() + (textBounds.height() / 2), textPaint);
                String teleportsText = "TELEPORTS";
                textSize = textSizeInPixels(teleportsText, smallTextPaint);
                canvas.drawText(teleportsText, currInfoRect.centerX() - (textSize / 2), currInfoRect.centerY() + (textBounds.height()) + 10, smallTextPaint);
            }
            left = left + average + separator;
            right = left + average;
        }
    }

    private void drawSimpleGrid(Canvas canvas) {
        canvas.drawColor(color);

        float startY = maxH - graphMarginBott;
        int startX = squareMargin + graphMargin;
        int endX = maxW - squareMargin - graphMargin;
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
        float startY = maxH - graphMarginBott;
        float startX = totalStartX;
        int endX = maxW - squareMargin - graphMargin;
        int totalSpaceForRectangles = (int) (endX - startX);

        int spaceForSingleRectangle = totalSpaceForRectangles / 5;

        float currentHours = 0;

        // draw time graphic (rectangles)
        if (hoursWorked != null) {
            for (int days = 0; days < hoursWorked.size(); days++) {
                float hours = Float.parseFloat((TimeCounter.convertToTimeRegular(hoursWorked.get(days).getTotalSpendTime())));
                if (hours < 0) {
                    hours = 0;
                }
                currentHours = hours * hourInPixel;
                canvas.drawRect(startX, startY - currentHours, startX + spaceForSingleRectangle - spaceBetweenHourRectangle, startY, timeGraphicPaint);
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

    private float textSizeInPixels(String text, Paint paint) {
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

        textPaint = new Paint();
        textPaint.setColor(Color.BLACK);
        textPaint.setTextSize(36);
        textPaint.setAntiAlias(true);

        smallTextPaint = new Paint();
        smallTextPaint.setColor(Color.BLACK);
        smallTextPaint.setTextSize(25);
        smallTextPaint.setAntiAlias(true);

        timeGraphicPaint = new Paint();
        timeGraphicPaint.setColor(Color.GREEN);
    }

    private void initFromResource() {
        separator = (int) getResources().getDimension(R.dimen.separator);
        squareHeight = (int) getResources().getDimension(R.dimen.square_height);
        squareMargin = (int) getResources().getDimension(R.dimen.margin_square_time);
        graphMargin = (int) getResources().getDimension(R.dimen.graph_margin);
        graphMarginBott = (int) getResources().getDimension(R.dimen.graph_margin_bottom);
        spaceBetweenHourRectangle = (int) getResources().getDimension(R.dimen.space_between_hour_rectangle);
        paddingFirstHourGraph = (int) getResources().getDimension(R.dimen.first_hours_graph_padding);
        totalTimeSpendHeight = (int) getResources().getDimension(R.dimen.total_time_height);
    }

    private void initSizes() {
        maxH = getHeight();
        maxW = getWidth();
        average = (maxW - (separator * 4)) / 3;
        shift = maxH - graphMarginBott - squareHeight - squareMargin * 2 - totalTimeSpendHeight;
        hourSeparator = shift / 6;
        hourInPixel = shift / 12;
        totalStartX = squareMargin + graphMargin + paddingFirstHourGraph;
    }

    @Override
    public boolean onPreDraw() {
        Log.d("XXX", "OnPreDraw");
        getViewTreeObserver().removeOnPreDrawListener(this);
        initSizes();
        return true;
    }

}

