package com.yzy.diary.dao;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Environment;

public class DBHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = Environment.getExternalStorageDirectory() + "/diary/diary.db3";
    private static final int DATABASE_VERSION = 3;
    private static final String CREATE_TABLE_SQL =
            "create table diary(_id integer primary key autoincrement ,diary_label varchar(255)," +
                    " diary_content varchar(2000), diary_date timestamp NOT NULL DEFAULT (datetime('now', 'localtime')),diary_weather varchar(20)" +
                    ",diary_mood varchar(20),diary_type varchar(20))";

    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_SQL);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion == 2) {
            db.execSQL("alter table diary add diary_type varchar(20)");
            db.execSQL("update diary set diary_type = ?", new String[]{"useful"});
        }
        if (oldVersion == 1) {
            db.execSQL("alter table diary add diary_weather varchar(20)");
            db.execSQL("alter table diary add diary_mood varchar(20)");
            db.execSQL("alter table diary add diary_type varchar(20)");
            db.execSQL("update diary set diary_type = ?", new String[]{"useful"});
        }
        System.out.println("--------onUpdate Called--------"
                + oldVersion + "--->" + newVersion);
    }
}
