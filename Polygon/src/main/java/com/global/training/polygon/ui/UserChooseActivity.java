package com.global.training.polygon.ui;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.global.training.polygon.R;
import com.global.training.polygon.db.DatabaseManager;
import com.global.training.polygon.model.User;

import java.sql.SQLException;
import java.util.List;


public class UserChooseActivity extends Activity implements AdapterView.OnItemClickListener {

	private List<User> mUserList;
	private ListView mListView;
	private EditText editText;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.a_user_choose);

		initListView();
		initSearchView();

		InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);

	}

	private void initSearchView() {
		editText = (EditText) findViewById(R.id.user_search_field);
		editText.addTextChangedListener(new TextWatcher() {
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {
			}

			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				String textSearch = s.toString();
				try {
					List<User> newUsers = DatabaseManager.getByFieldValueLike(
							false, User.LAST_NAME_FIELD, textSearch, User.FIRST_NAME_FIELD, textSearch, User.class);
					if (newUsers != null) {
						Adapter adapter = new Adapter(newUsers);
						mListView.setAdapter(adapter);
					}
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}

			@Override
			public void afterTextChanged(Editable s) {
			}
		});
	}

	private void initListView() {
		mListView = (ListView) findViewById(R.id.filtered_user_list);
		try {
			mUserList = DatabaseManager.getAll(User.class);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		if (mUserList != null) {
			Log.d("XXX", "Add adapter and click listener");
			mListView.setAdapter(new Adapter(mUserList));
			mListView.setOnItemClickListener(this);
		}
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		Intent intent = new Intent(UserChooseActivity.this, TimeSheetActivity.class);
		intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
		User user = (User) view.getTag();
		intent.putExtra("userID", user.getUid());
		intent.putExtra("userName", user.toString());
		startActivity(intent);
		finish();
		Log.d("XXX", "Click on item, UserId : " + (Integer) view.getTag(1) + ", name: " + (String) view.getTag(2));
	}

	private class Adapter extends BaseAdapter {

		List<User> currentList;

		public Adapter(List<User> list) {
			currentList = list;
		}

		@Override
		public int getCount() {
			return currentList.size();
		}

		@Override
		public User getItem(int position) {
			return currentList.get(position);
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
				view = layoutInflater.inflate(android.R.layout.simple_list_item_2, null);
			}
			TextView nameSurname = (TextView) view.findViewById(android.R.id.text1);
			nameSurname.setText(currentList.get(position).toString());

			TextView nameSurname2 = (TextView) view.findViewById(android.R.id.text2);
			nameSurname2.setText(currentList.get(position).getZone());
			view.setTag(currentList.get(position));
			return view;
		}

	}
}