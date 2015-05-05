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
    float[] hoursWorked = {2.0f, 4.0f, 8.0f, 10.0f, 12.0f};

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

    public void setHoursWorked(float[] hours) {
        hoursWorked = hours;
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
            canvas.drawRect(left, squareMargin, right, squareHeight, hoursPaint);
            left = left + average + separator;
            right = left + average;
        }
    }

    private void drawSimpleGrid(Canvas canvas) {
        canvas.drawColor(color);

        float y = maxH - graphMorginBott;
        float separator = (y - squareHeight - squareMargin*2) / 6;
        int startX = squareMargin + graphMorgin;
        int endX = maxW - squareMargin - graphMorgin;
        int hour = 0;
        for (int i = 0; i < 7; i++) {
            if (i == 4) {
                hoursPaint.setColor(Color.RED);
                canvas.drawLine(startX, y, endX, y, hoursPaint);
                hoursPaint.setColor(Color.WHITE);
            } else {
                canvas.drawLine(startX, y, endX, y, hoursPaint);
            }
            canvas.drawText(String.valueOf(hour), squareMargin, y, hoursPaint);
            hour += 2;
            y -= separator;
        }

        y = maxH - graphMorginBott;
        startX = squareMargin + graphMorgin + paddingFirstHourGraph;
        int totalSpaceForRectangles = endX - startX;

        int spaceForSingleRectangle = totalSpaceForRectangles / 5;

        float hourInPixel = (y - squareHeight - squareMargin*2) / 12;
        float currentHours = 0;

        if (hoursWorked != null) {
            for (int days = 0; days < hoursWorked.length; days++) {
                currentHours = (int) (hoursWorked[days] * hourInPixel);
                Log.d("XXX", "in view worked time:" + hoursWorked[days]);
                Log.d("XXX", "in view worked time in pixels:" + (y - currentHours ));
                canvas.drawRect(startX, y - currentHours  , startX + spaceForSingleRectangle - spaceBetweenHourRectangle, y, hoursPaint);
                startX += spaceForSingleRectangle;
            }
        }
    }

    private void initPaint() {
        gridPaint = new Paint();
        gridPaint.setColor(color);

        hoursPaint = new Paint();
        hoursPaint.setColor(Color.WHITE);
        hoursPaint.setStrokeWidth(3);
        hoursPaint.setTextSize(22);
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

