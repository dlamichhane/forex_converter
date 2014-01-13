package com.deepak.forexconverter;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class Database extends SQLiteOpenHelper{
	private static final String MYDATABASE = "android_forex";
	private static final int VERSION = 1;
	
	public Database(Context connection) {
		super(connection, MYDATABASE, null, VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL("CREATE TABLE users(id INTEGER PRIMARY KEY AUTOINCREMENT, username Text, password Text)");
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL("DROP TABLE IF EXISTS users");
		onCreate(db);
	}
}
