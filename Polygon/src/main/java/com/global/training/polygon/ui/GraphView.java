package com.global.training.polygon.ui;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;

import com.global.training.polygon.R;
import com.global.training.polygon.model.RealWorksTime;
import com.global.training.polygon.model.TimeCounter;

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
    int separator;
    int average;
    int maxW;
    int maxH;
    int squareHeight;
    int squareMargin;
    int color;
    int graphMorgin;
    int graphMorginBott;
    int spaceBetweenHourRectangle;
    int paddingFirstHourGraph;
    int totalTimeSpendHeight;

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
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        drawSimpleGrid(canvas);
        drawHours(canvas);
    }

    private void drawHours(Canvas canvas) {

        int left = separator;
        int right = left + average;
        Log.d("XXX", "W : " + maxW);
        Log.d("XXX", "average : " + average);

        for (int i = 0; i < 3; i++) {
            Log.d("XXX", "i : " + i + ", L : " + left + ", R : " + right);
            canvas.drawRect(left, squareMargin + totalTimeSpendHeight, right, squareHeight + totalTimeSpendHeight, hoursPaint);
            left = left + average + separator;
            right = left + average;
        }
    }

    private void drawSimpleGrid(Canvas canvas) {
        canvas.drawColor(color);

        float startY = maxH - graphMorginBott;
        float shift = startY - squareHeight - squareMargin*2 - totalTimeSpendHeight;
        float hourSeparator = shift / 6;
        float hourInPixel = shift / 12;

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

        startY = maxH - graphMorginBott;
        startX = squareMargin + graphMorgin + paddingFirstHourGraph;
        int totalSpaceForRectangles = endX - startX;

        int spaceForSingleRectangle = totalSpaceForRectangles / 5;

        float currentHours;

        // draw time graphic (rectangles)
        if (hoursWorked != null) {
            for (int days = 0; days < hoursWorked.size(); days++) {
                float hours = Math.abs(Float.parseFloat((TimeCounter.convertToTimeRegular(hoursWorked.get(days).getTotalSpendTime()))));
                if(hours > -1){
                    currentHours = hours * hourInPixel;
                    canvas.drawRect(startX, startY - currentHours, startX + spaceForSingleRectangle - spaceBetweenHourRectangle, startY, hoursPaint);
                }
                startX += spaceForSingleRectangle;
            }
        }

        // draw day names
        startX = squareMargin + graphMorgin + paddingFirstHourGraph;
        for (int days = 0; days < 5; days++) {
            float textSize = textSizeInPixels(dyaNames[days], hoursPaint);
            float shift1 = (spaceForSingleRectangle - textSize - spaceBetweenHourRectangle ) / 2;
            canvas.drawText(dyaNames[days], startX + shift1, startY + 25, hoursPaint);
            startX += spaceForSingleRectangle;
        }
    }

//    private void draw

    private float textSizeInPixels(String text, Paint paint){
        float size = paint.measureText(text);
        return size;
    }

    private void initPaint() {
        gridPaint = new Paint();
        gridPaint.setColor(color);

        hoursPaint = new Paint();
        hoursPaint.setColor(Color.WHITE);
        hoursPaint.setStrokeWidth(3);
        hoursPaint.setTextSize(25);
        hoursPaint.setAntiAlias(true);
    }

    private void initFromResource() {
        separator = (int) getResources().getDimension(R.dimen.separator);
        squareHeight = (int) getResources().getDimension(R.dimen.square_height);
        squareMargin = (int) getResources().getDimension(R.dimen.morgin_square_time);
        graphMorgin = (int) getResources().getDimension(R.dimen.graph_morgin);
        graphMorginBott = (int) getResources().getDimension(R.dimen.graph_morgin_bott);
        spaceBetweenHourRectangle = (int) getResources().getDimension(R.dimen.space_between_hour_rectangle);
        paddingFirstHourGraph = (int) getResources().getDimension(R.dimen.first_hours_graph_padding);
        totalTimeSpendHeight = (int) getResources().getDimension(R.dimen.total_time_height);
    }

    private void initSizes() {
        maxH = getHeight();
        maxW = getWidth();
        average = (maxW - (separator * 4)) / 3;
    }

    @Override
    public boolean onPreDraw() {
        Log.d("XXX", "OnPreDraw");
        getViewTreeObserver().removeOnPreDrawListener(this);
        initSizes();
        return true;
    }

}

