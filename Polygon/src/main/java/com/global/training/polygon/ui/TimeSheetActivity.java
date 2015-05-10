package com.global.training.polygon.ui;

import android.content.res.Configuration;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBarDrawerToggle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;

import com.global.training.polygon.R;
import com.global.training.polygon.model.RealWorksTime;
import com.global.training.polygon.model.TimeCounter;
import com.global.training.polygon.model.User;
import com.global.training.polygon.utils.Api;
import com.global.training.polygon.utils.PreferencesUtils;

import org.joda.time.DateTime;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;


public class TimeSheetActivity extends ActionBarActivity implements
        AdapterView.OnClickListener, Api.OfficeTimeCallback, Api.EmployeesCallback {

    private static final long WORKING_DAY_LENGTH_MILLIS = 8 * 60 * 60 * 1000;
    private static final int THREASHHOLD = 5;

    private Calendar mCalendarStart;
    private Calendar mCalendarEnd;

    private int mUserId;

    private Adapter mAdapter;
    private SimpleDateFormat mSimpleDateFormat = new SimpleDateFormat("EE dd.MM.yyyy");
    private List<RealWorksTime> mTimeSheetList = new ArrayList<RealWorksTime>();

    private List<User> mUserList;
    private List<User> mUserListOriginal;

    private boolean mIsLoading = false;
    private boolean mIsMoreData = true;
    private GraphView graphView;
    private SearchView searchView;

    private String[] mPlanetTitles;
    private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;
    private ActionBarDrawerToggle mDrawerToggle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.a_timesheet);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

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
        // Sync the toggle state after onRestoreInstanceState has occurred.
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
        searchView = (SearchView) menu.findItem(R.id.search).getActionView();
        return true;
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
                getPreviousPeriod();
                return true;
            case R.id.search:
                Log.d("XXX", "Search");
                searchView.setIconified(false);
                return true;

        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
//        Log.d("MyLog", "is selected " + mTimeOracle.isSelected());
//		switch (v.getId()) {
            /*case R.id.profile_textview:
                Intent intent = new Intent(this, UserChooseActivity.class);
				intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				startActivity(intent);
				break;*/
//			case R.id.time_or_oracle:
//				if (mTimeOracle.isSelected()) {
//					mTimeOracle.setText(R.string.time_oracle_selected_oracle);
//					mTimeOracle.setSelected(false);
//					mAdapter.notifyDataSetChanged();
//				} else {
//					mTimeOracle.setText(R.string.time_oracle_selected_time);
//					mTimeOracle.setSelected(true);
//					mAdapter.notifyDataSetChanged();
//				}
//		}
    }

    @Override
    public void getTimeList(List<RealWorksTime> list) {
        Log.d("XXX", "getTimeList");
//        if (list.isEmpty() && mCounter < 5) {
////            getPreviousPeriod();
//			mCounter++;
//			mCalendarStart.add(Calendar.SECOND, -1);
//			mCalendarEnd = mCalendarStart;
//			mAdapter.notifyDataSetChanged();
//			mIsLoading = false;
//			return;
//		}
//		if (mCounter > 5) {
//			mIsMoreData = false;
//			return;
//		}
//		mCounter = 0;
//		List<RealWorksTime>tempList = TimeCounter.getRealTime(list);
        mTimeSheetList.clear();
        for (int i = list.size() - 1; i >= 0; i--) {
            mTimeSheetList.add(list.get(i));
        }

//		float[] times = new float[mTimeSheetList.size()];
        for (int i = 0; i < mTimeSheetList.size(); i++) {
            Log.d("XXX", "Works time r:" + Float.parseFloat(TimeCounter.convertToTimeRegular(mTimeSheetList.get(i).getTotalSpendTime())));
            Log.d("XXX", "Works time o:" + Float.parseFloat(TimeCounter.convertToTimeOracle(mTimeSheetList.get(i).getTotalSpendTime())));
//			times[i] = Math.abs(Float.parseFloat((convertToTimeRegular(mTimeSheetList.get(i).getTotalSpendTime()))));
        }
        graphView.setHoursWorked(mTimeSheetList);

//		mTimeSheetList.add(new RealWorksTime());
//		mCalendarStart.add(Calendar.SECOND, -1);
//		mCalendarEnd = mCalendarStart;
//		mAdapter.notifyDataSetChanged();
//		mIsLoading = false;
    }


    private void getPreviousPeriod() {
//		if (!mIsLoading) {
        DateTime endDate = new DateTime(mCalendarEnd);
        if (endDate.getDayOfMonth() > 15) {
            mCalendarStart = new GregorianCalendar(endDate.getYear(), endDate.getMonthOfYear() - 1, 16);
        } else {
            mCalendarStart = new GregorianCalendar(endDate.getYear(), endDate.getMonthOfYear() - 1, 1);
        }
        Api.timeWork(mCalendarStart.getTimeInMillis(), endDate.getMillis(), mUserId, this);
//			mIsLoading = true;
//		}
    }

    @Override
    public void getUserList(List<User> list) {

        mUserList = list;
//		mUserListOriginal = new ArrayList<User>(mUserList);
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
        getPreviousPeriod();
    }

    private class Adapter extends BaseAdapter {

        @Override
        public int getCount() {
            if (mTimeSheetList.size() <= 1) {
                Log.d("XXX", "Adapter > getCount");
//                getPreviousPeriod();
            }
            return mTimeSheetList.size();
        }

        @Override
        public RealWorksTime getItem(int position) {
            return mTimeSheetList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View view = convertView;
            if (mTimeSheetList.get(position).getDate() == null) {
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.v_timesheet_row, parent, false);
                view.setOnClickListener(null);
                view.setOnLongClickListener(null);
                view.setClickable(false);

                return view;
            }
            if (view == null) {
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.v_timesheet_row, parent, false);
            }
            TextView dateField = (TextView) view.findViewById(R.id.date_in_row);
            TextView workedHours = (TextView) view.findViewById(R.id.time_or_oracle_in_row);
            TextView difference = (TextView) view.findViewById(R.id.difference_in_row);

            Long timeSpentAtWork = mTimeSheetList.get(position).getTotalSpendTime();
            Long differenceMillis = timeSpentAtWork - WORKING_DAY_LENGTH_MILLIS;

            dateField.setText(mSimpleDateFormat.format(mTimeSheetList.get(position).getDate()));
            difference.setText(TimeCounter.convertToTimeRegular(differenceMillis));

            if (differenceMillis > 0) {
                difference.setTextColor(Color.GREEN);
            } else {
                difference.setTextColor(Color.RED);
            }

           /* if (mTimeOracle.isSelected()) {
                workedHours.setText(TimeCounter.convertToTimeRegular(timeSpentAtWork));
            } else {
                workedHours.setText(TimeCounter.convertToTimeOracle(timeSpentAtWork));
            }*/

            if (getCount() < position + THREASHHOLD && mIsMoreData) {
//                Log.d("XXX", "getView");
//                getPreviousPeriod();
            }
            return view;
        }
    }
}
