package com.global.training.polygon.ui;

import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;

import com.global.training.polygon.R;
import com.global.training.polygon.model.RealWorksTime;
import com.global.training.polygon.utils.Api;
import com.global.training.polygon.utils.PreferencesUtils;

import org.joda.time.DateTime;
import org.joda.time.DateTimeConstants;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.util.List;


public class TimeSheetActivity extends AppCompatActivity implements Api.OfficeTimeCallback {

    private int mUserId;
    private GraphView graphView;
    private SearchView searchView;
    private ImageButton mPreviousPeriodButton;
    private ImageButton mNextPeriodButton;
    private String[] mPlanetTitles;
    private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;
    private ActionBarDrawerToggle mDrawerToggle;
    private MenuItem miActionProgressItem;
    private PervNextClickListener timeListener;
    private DateTime currentDatePosition;
    private TextView mPeriodTextView;

    private DateTimeFormatter dtf = DateTimeFormat.forPattern("dd/MM/yyyy");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        setContentView(R.layout.a_timesheet);

        graphView = (GraphView) findViewById(R.id.graph);

        mPlanetTitles = getResources().getStringArray(R.array.planets_array);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, R.string.open_drawable, R.string.close_drawable);

        mDrawerList = (ListView) findViewById(R.id.left_drawer);
        mDrawerList.setAdapter(new ArrayAdapter<String>(this, R.layout.drawer_list_item, mPlanetTitles));
//        mDrawerList.setOnItemClickListener(new DrawerItemClickListener());

        timeListener = new PervNextClickListener();

        mPreviousPeriodButton = (ImageButton) findViewById(R.id.left_button);
        mNextPeriodButton = (ImageButton) findViewById(R.id.right_button);

        mPreviousPeriodButton.setOnClickListener(timeListener);
        mNextPeriodButton.setOnClickListener(timeListener);

        mPeriodTextView = (TextView) findViewById(R.id.period);

        currentDatePosition = new DateTime();
        mUserId = PreferencesUtils.getLastSeenUserId();
        getCurrentWeek();
    }


    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.new_menu, menu);
        Log.e("XXX", "onCreateOptionsMenu");
        searchView = (SearchView) MenuItemCompat.getActionView(menu.findItem(R.id.search));
        miActionProgressItem = menu.findItem(R.id.refresh);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        Log.e("XXX", "onPrepareOptionsMenu");
        changeRefreshState(true);
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        switch (item.getItemId()) {
            case R.id.change_user:
                return true;

            case R.id.logout:
                Log.d("XXX", "Logout");
                PreferencesUtils.logout();
                return true;

            case R.id.refresh:
                Log.d("XXX", "Refresh");
                graphView.setHoursWorked(null);
                changeRefreshState(true);
                getCurrentWeek();
                return true;

            case R.id.search:
                Log.d("XXX", "Search");
                searchView.setIconified(false);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void getTimeList(List<RealWorksTime> list) {
        Log.d("XXX", "getTimeList");
        graphView.setHoursWorked(list);
        changeRefreshState(false);
    }

    private void changeRefreshState(boolean inProgress) {
        if (inProgress) {
            miActionProgressItem.setActionView(R.layout.m_progress);
        } else {
            miActionProgressItem.setActionView(null);
        }
    }

    private void getCurrentWeek() {
        DateTime endDate = new DateTime(currentDatePosition);
        DateTime startDate = new DateTime(endDate).withDayOfWeek(DateTimeConstants.MONDAY).withHourOfDay(0).withMinuteOfHour(0);

        Log.d("XXX", "period from:" + startDate.getMillis());
        Log.d("XXX", "period to  :" + endDate.getMillis());

        Api.timeWork(startDate.getMillis(), endDate.getMillis(), mUserId, this);
        mPeriodTextView.setText(dtf.print(startDate.getMillis()) + " - " + dtf.print(endDate.getMillis()));
    }

    private void getWeekPeriod(Constants flag) {
        DateTime endDate = new DateTime(currentDatePosition);
        DateTime start = new DateTime(endDate);
        long startMill = 0;
        long endMill = 0;

        switch (flag) {
            case NEXT:
                startMill = start.plusWeeks(1).withHourOfDay(0).withMinuteOfHour(0).withDayOfWeek(DateTimeConstants.MONDAY).getMillis();
                endMill = endDate.plusWeeks(1).withHourOfDay(23).withMinuteOfHour(59).withDayOfWeek(DateTimeConstants.FRIDAY).getMillis();
                currentDatePosition = new DateTime(start.plusWeeks(1));
                break;
            case PREVIOUS:
                startMill = start.minusWeeks(1).withHourOfDay(0).withMinuteOfHour(0).withDayOfWeek(DateTimeConstants.MONDAY).getMillis();
                endMill = endDate.minusWeeks(1).withHourOfDay(23).withMinuteOfHour(59).withDayOfWeek(DateTimeConstants.FRIDAY).getMillis();
                currentDatePosition = new DateTime(start.minusWeeks(1));
                break;
        }
        Log.d("XXX", "prev period from:" + new DateTime(startMill));
        Log.d("XXX", "prev period to  :" + new DateTime(endMill));

        mPeriodTextView.setText(dtf.print(startMill) + " - " + dtf.print(endMill));
        Api.timeWork(startMill, endMill, mUserId, this);
    }

    class PervNextClickListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.left_button:
                    Log.d("XXX", "Previous period");
                    getWeekPeriod(Constants.PREVIOUS);
                    break;
                case R.id.right_button:
                    Log.d("XXX", "Next period");
                    getWeekPeriod(Constants.NEXT);
                    break;
            }
        }
    }

    private enum Constants {
        NEXT, PREVIOUS;
    }
}
