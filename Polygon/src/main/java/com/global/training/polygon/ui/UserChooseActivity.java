package com.global.training.polygon.ui;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.*;
import com.global.training.polygon.R;
import com.global.training.polygon.model.User;

import java.util.ArrayList;
import java.util.List;

/**
 * @author yurii.ostrovskyi
 */
public class UserChooseActivity extends Activity {

	private List<User> mUserList;
	private List<User> mUserListOriginal;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.a_user_choose);

		mUserList = new ArrayList<User>();
		mUserList.add(new User("John", "McLane"));
		mUserList.add(new User("Booby", "Dilan"));
		mUserList.add(new User("Elton", "John"));
		mUserList.add(new User("William", "Walles"));
		mUserList.add(new User("Holly", "McLane"));
		mUserList.add(new User("Sara", "McLane"));
		mUserList.add(new User("Sara", "Connor"));
		mUserList.add(new User("John", "Tacker"));
		mUserList.add(new User("Jason", "Bourne"));
		mUserList.add(new User("Marry", "Lewis"));
		mUserListOriginal = new ArrayList<User>(mUserList);

		AutoCompleteTextView textView = (AutoCompleteTextView)
				findViewById(R.id.user_search_field);
		textView.setAdapter(new Adapter());
		textView.setThreshold(1);
		textView.setOnEditorActionListener(new ImeActionListener());

		InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.toggleSoftInput(InputMethodManager.SHOW_FORCED,0);
	}

	private class Adapter extends BaseAdapter implements Filterable{

		@Override
		public int getCount() {
			return mUserList.size();
		}

		@Override
		public User getItem(int position) {
			return mUserList.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			View view = convertView;
			if (view == null) {
				LayoutInflater layoutInflater = (LayoutInflater) UserChooseActivity.this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				view = layoutInflater.inflate(android.R.layout.simple_list_item_1, null);
			}
			TextView nameSurname = (TextView) view.findViewById(android.R.id.text1);
			nameSurname.setText(mUserList.get(position).toString());
			return view;
		}

		@Override
		public Filter getFilter() {
			return new Filter() {
				@Override
				protected FilterResults performFiltering(CharSequence constraint) {
					FilterResults filterResults = new FilterResults();
					if (constraint != null) {
						List<User> filteredUsers = new ArrayList<User>();
						for (User currentUser : mUserList) {
							if ((currentUser.getFirst_name() + " " + currentUser.getLast_name()).toLowerCase().contains(constraint)) {
								filteredUsers.add(currentUser);
							}
						}
						filterResults.values = filteredUsers;
						filterResults.count = filteredUsers.size();
					} else {
						filterResults.values = mUserListOriginal;
						filterResults.count = mUserListOriginal.size();
					}
					return filterResults;
				}

				@Override
				protected void publishResults(CharSequence contraint, FilterResults results) {
					if (results != null && results.count > 0) {
						mUserList = (ArrayList<User>) results.values;
						notifyDataSetChanged();
					} else {
						notifyDataSetInvalidated();
					}
				}
			};
		}
	}

	private class ImeActionListener implements TextView.OnEditorActionListener {
		@Override
		public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
			if (actionId == EditorInfo.IME_ACTION_DONE) {
				startActivity(new Intent(UserChooseActivity.this, TimeSheetActivity.class));
				return true;
			}
			return false;
		}
	}
}
