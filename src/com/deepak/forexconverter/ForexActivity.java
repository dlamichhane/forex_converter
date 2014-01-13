package com.deepak.forexconverter;

import android.os.Bundle;
import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class ForexActivity extends Activity {
	private Database android_forex;
	public static final String EXTRA_MESSAGE = "Freaking Buddy";
	public static final String SUCCESS_MESSAGE = "Jail Break in Activity";
	private final String[] SELECT = { "username", "password" };
	private static final String DEEPAK = null;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_forex);
		android_forex = new Database(this);

		Button lgnBtn = (Button) findViewById(R.id.login);
		final Intent intent = new Intent(this, DisplayMessageActivity.class);

		lgnBtn.setOnClickListener(new View.OnClickListener() {

			public void onClick(View v) {
				EditText editText1 = (EditText) findViewById(R.id.user_name);
				EditText editText2 = (EditText) findViewById(R.id.password);

				String username = editText1.getText().toString();
				String password = editText2.getText().toString();

				Boolean checkPass = checkLoginPass(username, password);
				Boolean test = true;
				// if(checkPass){
				// comment only to test it
				if (test) {
					intent.putExtra(SUCCESS_MESSAGE, username);
					startActivity(intent);
				} else {

				}

			}

			private Boolean checkLoginPass(String username, String password) {
				SQLiteDatabase db = android_forex.getReadableDatabase();
				Cursor cursor = db.query(false, "users", SELECT, null, null,
						null, null, null, null);
				Boolean bool = false;

				if (cursor.moveToFirst() && cursor.getCount() >= 1) {
					do {
						String uname = cursor.getString(cursor
								.getColumnIndex("username"));
						String pword = cursor.getString(cursor
								.getColumnIndex("password"));

						if (username.equals(uname) && password.equals(pword)) {
							bool = true;
							break;
						}
					} while (cursor.moveToNext());
				}

				return bool;
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.activity_forex, menu);
		return true;
	}

	public void sendData(View view) {
		EditText editText1 = (EditText) findViewById(R.id.user_name);
		EditText editText2 = (EditText) findViewById(R.id.password);

		String username = editText1.getText().toString();
		String password = editText2.getText().toString();

		addData(username, password);

		Intent intent = new Intent(this, DisplayMessageActivity.class);
		intent.putExtra(EXTRA_MESSAGE, username);
		startActivity(intent);
	}

	private void addData(String username, String password) {
		SQLiteDatabase db = android_forex.getWritableDatabase();
		ContentValues datas = new ContentValues();
		datas.put("username", username);
		datas.put("password", password);
		db.insertOrThrow("users", null, datas);
		android_forex.close();
	}

	public void removeData(View view) {
		SQLiteDatabase db = android_forex.getWritableDatabase();
		db.delete("users", null, null);
	}

}
