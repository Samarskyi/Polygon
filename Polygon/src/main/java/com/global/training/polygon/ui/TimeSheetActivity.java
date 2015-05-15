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
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SearchView;

import com.global.training.polygon.R;
import com.global.training.polygon.model.RealWorksTime;
import com.global.training.polygon.model.User;
import com.global.training.polygon.utils.Api;
import com.global.training.polygon.utils.PreferencesUtils;

import org.joda.time.DateTime;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;


public class TimeSheetActivity extends AppCompatActivity implements Api.OfficeTimeCallback, Api.EmployeesCallback {

    private static final int FIRST_DAY = 1;
    private Calendar mCalendarStart;
    private Calendar mCalendarEnd;

    private int mUserId;

    private List<User> mUserList;

    private GraphView graphView;
    private SearchView searchView;

    private String[] mPlanetTitles;
    private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;
    private ActionBarDrawerToggle mDrawerToggle;
    private MenuItem miActionProgressItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.e("XXX", "onCreate");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        setContentView(R.layout.a_timesheet);

        graphView = (GraphView) findViewById(R.id.graph);

        Api.getUsers(this);
        mCalendarEnd = new GregorianCalendar();

        mPlanetTitles = getResources().getStringArray(R.array.planets_array);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);

        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, R.drawable.ic_drawer, R.drawable.drawer_shadow);
        mDrawerList = (ListView) findViewById(R.id.left_drawer);

        // Set the adapter for the list view
        mDrawerList.setAdapter(new ArrayAdapter<String>(this, R.layout.drawer_list_item, mPlanetTitles));
        // Set the list's click listener
//        mDrawerList.setOnItemClickListener(new DrawerItemClickListener());
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
                return true;
            case R.id.refresh:
                Log.d("XXX", "Refresh");
                graphView.setHoursWorked(null);
                changeRefreshState(true);
//                getPreviousPeriod();
                getNextWeek();
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

    private void getPreviousPeriod() {
        DateTime endDate = new DateTime(mCalendarEnd);
        if (endDate.getDayOfMonth() > 15) {
            mCalendarStart = new GregorianCalendar(endDate.getYear(), endDate.getMonthOfYear() - 1, 16);
        } else {
            mCalendarStart = new GregorianCalendar(endDate.getYear(), endDate.getMonthOfYear() - 1, 1);
        }
        Api.timeWork(mCalendarStart.getTimeInMillis(), endDate.getMillis(), mUserId, this);
    }

    private void getNextWeek() {
        DateTime endDate = new DateTime(mCalendarEnd);
//        LocalDate localDate = new LocalDate(mCalendarEnd);

        mCalendarStart = new GregorianCalendar(endDate.getYear(), endDate.getMonthOfYear() - 1, getFirstDayOfWeek(endDate).getDayOfMonth());
        Log.d("XXX", "period from:" + new DateTime(mCalendarStart.getTimeInMillis()));
        Log.d("XXX", "period to  :" + new DateTime(endDate.getMillis()));
        Api.timeWork(mCalendarStart.getTimeInMillis(), endDate.getMillis(), mUserId, this);
    }

    private DateTime getFirstDayOfWeek(DateTime other) {
        return other.withDayOfWeek(FIRST_DAY);
    }

    @Override
    public void getUserList(List<User> list) {

        mUserList = list;
//		Log.d("XXX", "Size of original list = " + mUserListOriginal.size() + " , size of filtered list + " + mUserList.size());
        String[] infoSplit = PreferencesUtils.getCredentials().split(" ");
        Log.d("XXX", "Credentials: " + infoSplit[0]);
        if (list == null) {
            Log.e("XXX", "LIST IS EMPTY");
            return;
        }
        if (list.size() == 0) {
            Log.e("XXX", "LIST SIZE == 0");
            return;
        }
        for (User currentUser : mUserList) {
            try {
//				Log.d("XXX", "iterate users: " + (currentUser.getFirst_name() + "." + currentUser.getLast_name()).toLowerCase());
                if ((currentUser.getFirst_name() + "." + currentUser.getLast_name()).toLowerCase().contains(infoSplit[0])) {
                    mUserId = currentUser.getUid();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        getNextWeek();
//        getPreviousPeriod();
    }
}
