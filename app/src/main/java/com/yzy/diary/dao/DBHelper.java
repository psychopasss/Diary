package com.yzy.diary.dao;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper {
	private static final String DATABASE_NAME = "/mnt/sdcard/diary/diary.db3";
	private static final int DATABASE_VERSION = 2;
	private static final String CREATE_TABLE_SQL =
			"create table diary(_id integer primary key autoincrement ,diary_label varchar(255)," +
                    " diary_content varchar(2000), diary_date timestamp NOT NULL DEFAULT (datetime('now', 'localtime')),diary_weather varchar(20)" +
                    ",diary_mood varchar(20))";
	
	public DBHelper(Context context) 
	{
		super(context, DATABASE_NAME , null, DATABASE_VERSION);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(CREATE_TABLE_SQL);
		// TODO Auto-generated method stub

	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub
        db.execSQL("alter table diary add diary_weather varchar(20)");
        db.execSQL("alter table diary add diary_mood varchar(20)");
		System.out.println("--------onUpdate Called--------"
				+ oldVersion + "--->" + newVersion);
	}
}
