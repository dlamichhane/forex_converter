package com.deepak.forexconverter;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.http.client.methods.HttpGet;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.deepak.forexconverter.HttpTask.HttpTaskHandler;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class DisplayMessageActivity extends Activity {
	private Database android_forex;
	private static final String DEEPAK = null;
	private String[] SELECT = { "id", "username", "password" };
	private String exchangeUri = "http://openexchangerates.org/api/latest.json?app_id=f256d9a2442c4cb088774c95bd3de311";
	ListView listView;
	ArrayList<String> data;
	ArrayList<String> currency;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_display_message);
		Intent intent = getIntent();
		String username = intent.getStringExtra(ForexActivity.SUCCESS_MESSAGE);
		android_forex = new Database(this);

		TextView textView1 = (TextView) findViewById(R.id.textView1);
		textView1.setText("Logged In " + username);
		/*
		 * try { Cursor cursor = getCursor(); showCursorData(cursor); } catch
		 * (Exception e) { e.getStackTrace(); }
		 */

		getDirectExchangeJson();
		Button exchgBtn = (Button) findViewById(R.id.exchange);
		getConvertCurrency(exchgBtn);
		// getExchangeJson(exchgBtn);

	}

	private void getConvertCurrency(Button exchgBtn) {
		exchgBtn.setOnClickListener(new View.OnClickListener() {
			
			public void onClick(View v) {
				EditText valueToConvertText = (EditText)findViewById(R.id.editText1);
				String valueToConvert = valueToConvertText.getText().toString();
				if(valueToConvert !="" && isNumeric(valueToConvert)){
					Spinner spinner1 = (Spinner)findViewById(R.id.spinner1);
					String currencyFrom = spinner1.getSelectedItem().toString();
					Spinner spinner2 = (Spinner)findViewById(R.id.spinner2);
					String currencyTo = spinner2.getSelectedItem().toString();
					String valFirst="1";
					String valSecond="1";
					Float result;
					
					ListView listView = (ListView)findViewById(R.id.rateCurrency);
					for(int i=0;i<listView.getCount();i++){
						String curr = listView.getItemAtPosition(i).toString();
						curr.trim();
						if(currencyFrom.equals(curr.substring(0, 3))){
							valFirst = curr.substring(5);
						}
						
						if(currencyTo.equals(curr.substring(0, 3))){
							valSecond = curr.substring(5);
						}
						
						//Toast.makeText(getApplicationContext(), curr.substring(5), Toast.LENGTH_SHORT).show();
					}
					result = (Float.valueOf(valSecond)/Float.valueOf(valFirst)*Float.valueOf(valueToConvert));
					TextView textView4 =(TextView)findViewById(R.id.textView4);
					textView4.setText(result.toString());
				}
				else{
					Toast.makeText(getApplicationContext(), "Put Valid Numeric", Toast.LENGTH_SHORT).show();
				}
				

			}
		});
		
	}
	
	public static boolean isNumeric(String str)
	{
	  return str.matches("-?\\d+(\\.\\d+)?");  //match a number with optional '-' and decimal.
	}

	private void getDirectExchangeJson() {
		HttpTask httpTask = new HttpTask();

		httpTask.setTaskHandler(new HttpTaskHandler() {

			public void taskSuccess(JSONObject json) {
				data = new ArrayList<String>();
				currency = new ArrayList<String>();
				listView = (ListView) findViewById(R.id.rateCurrency);

				try {
					if (json.has("rates")) {
						JSONObject rates = json.getJSONObject("rates");
						Iterator<Object> keys = rates.keys();
						StringBuilder currencyCountry = new StringBuilder();

						for (int i = 0; i < rates.length(); i++) {
							currencyCountry.setLength(0);
							if (keys.hasNext()) {
								String country = String.valueOf(keys.next());
								currencyCountry.append(country).append("::");
								currencyCountry.append(rates.getString(country)
										.toString());
								data.add(currencyCountry.toString());
								currency.add(country);
							}
						}
					}

					ArrayAdapter<String> listAdapter = new ArrayAdapter<String>(
							getApplicationContext(),
							android.R.layout.simple_list_item_1, data);
					ArrayAdapter<String> listCurrencyAdapter = new ArrayAdapter<String>(
							getApplicationContext(),
							android.R.layout.simple_spinner_item, currency);

					listCurrencyAdapter
							.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
					Spinner spinFirstCurrency = (Spinner) findViewById(R.id.spinner1);
					Spinner spinSecondCurrency = (Spinner) findViewById(R.id.spinner2);
					spinFirstCurrency.setAdapter(listCurrencyAdapter);
					spinSecondCurrency.setAdapter(listCurrencyAdapter);

					TextView oneDollar = (TextView) findViewById(R.id.usdollar);
					oneDollar.setText("1 USD equals");

					listView = (ListView) findViewById(R.id.rateCurrency);
					listView.setBackgroundColor(Color.BLUE);
					listView.setAdapter(listAdapter);

				} catch (JSONException e) {
					e.printStackTrace();
				}
			}

			public void taskFailed() {
				Log.d("INTERNET PROBLEM", "JSON is null");
				Toast.makeText(getBaseContext(), "No Exchange rate available",
						Toast.LENGTH_SHORT).show();
			}
		});

		httpTask.execute(new HttpGet(exchangeUri));

	}
/*
	private void getExchangeJson(Button exchgBtn) {
		exchgBtn.setOnClickListener(new View.OnClickListener() {

			public void onClick(View v) {
				HttpTask httpTask = new HttpTask();

				httpTask.setTaskHandler(new HttpTaskHandler() {

					public void taskSuccess(JSONObject json) {
						data = new ArrayList<String>();
						currency = new ArrayList<String>();
						listView = (ListView) findViewById(R.id.rateCurrency);
						// listView.setAdapter(listAdapter);
						try {
							if (json.has("rates")) {
								JSONObject rates = json.getJSONObject("rates");
								Iterator<Object> keys = rates.keys();
								StringBuilder currencyCountry = new StringBuilder();

								for (int i = 0; i < rates.length(); i++) {
									currencyCountry.setLength(0);
									if (keys.hasNext()) {
										String country = String.valueOf(keys
												.next());
										currencyCountry.append(country).append(
												"::");
										currencyCountry.append(rates.getString(
												country).toString());
										data.add(currencyCountry.toString());
										currency.add(country);
									}
								}
							}

							ArrayAdapter<String> listAdapter = new ArrayAdapter<String>(
									getApplicationContext(),
									android.R.layout.simple_list_item_1, data);
							ArrayAdapter<String> listCurrencyAdapter = new ArrayAdapter<String>(
									getApplicationContext(),
									android.R.layout.simple_spinner_item,
									currency);

							listCurrencyAdapter
									.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
							Spinner spinFirstCurrency = (Spinner) findViewById(R.id.spinner1);
							Spinner spinSecondCurrency = (Spinner) findViewById(R.id.spinner2);
							spinFirstCurrency.setAdapter(listCurrencyAdapter);
							spinSecondCurrency.setAdapter(listCurrencyAdapter);

							TextView oneDollar = (TextView) findViewById(R.id.usdollar);
							oneDollar.setText("1 USD equals");

							listView = (ListView) findViewById(R.id.rateCurrency);
							listView.setBackgroundColor(Color.BLUE);
							listView.setAdapter(listAdapter);

							
							//  Toast.makeText(getApplicationContext(),
							// data.toString(), Toast.LENGTH_SHORT).show();
							 

						} catch (JSONException e) {
							e.printStackTrace();
						}
					}

					public void taskFailed() {
						Log.d("INTERNET PROBLEM", "JSON is null");
						Toast.makeText(getBaseContext(),
								"No Exchange rate available",
								Toast.LENGTH_SHORT).show();
					}
				});

				httpTask.execute(new HttpGet(exchangeUri));
			}
		});

	}*/

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.activity_display_message, menu);
		return true;
	}

	private void showCursorData(Cursor cursor) {
		StringBuilder results = new StringBuilder("Results:\n");
		while (cursor.moveToNext()) {
			String username = cursor.getString(cursor
					.getColumnIndex("username"));
			String password = cursor.getString(cursor
					.getColumnIndex("password"));

			results.append(username);
			results.append("::::");
			results.append(password);
			results.append("\n");
		}

		TextView textView1 = (TextView) findViewById(R.id.textView1);
		textView1.setText(results);

	}

	private Cursor getCursor() {
		SQLiteDatabase db = android_forex.getReadableDatabase();
		Cursor cursor = db.query(false, "users", SELECT, null, null, null,
				null, null, null);
		return cursor;
	}
}
