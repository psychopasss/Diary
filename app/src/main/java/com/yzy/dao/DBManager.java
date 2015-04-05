package com.yzy.dao;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.yzy.diary.model.Diary;

import java.sql.Timestamp;
import java.util.ArrayList;

public class DBManager {
    private DBHelper helper;
    private SQLiteDatabase db;

    public DBManager(Context context) {
        helper = new DBHelper(context);
        db = helper.getReadableDatabase();
    }

    /**
     * add diary
     *
     * @param diary
     */
    public void add(Diary diary) {
        db.beginTransaction();    //开始事务
        try {
            db.execSQL("insert into diary(diary_label,diary_content) values(?,?)",
                    new Object[]{diary.label, diary.content});
            db.setTransactionSuccessful();    //设置事务成功完成
        } finally {
            db.endTransaction();    //结束事务
        }
    }

    /**
     * update diary
     *
     * @param diary
     */
    public void updateDiary(Diary diary) {
            db.execSQL("update diary set diary_label = ? , diary_content = ? where _id = ? ",
                    new String[]{diary.label, diary.content, String.valueOf(diary._id)});
    }

    /**
     * delete diary
     *
     * @param id
     */
    public void deleteDiary(String id) {
        db.delete("diary", "_id = ?", new String[]{id});
    }


    /**
     * query all diaries, return list
     *
     * @return List<Diary>
     */
    public Diary query(String id) {
        ArrayList<Diary> diaries = new ArrayList<>();
        Cursor c = queryId(id);
        while (c.moveToNext()) {
            Diary diary = new Diary();
            diary._id = c.getInt(c.getColumnIndex("_id"));
            diary.label = c.getString(c.getColumnIndex("diary_label"));
            diary.content = c.getString(c.getColumnIndex("diary_content"));
            diary.date = Timestamp.valueOf(c.getString(c.getColumnIndex("diary_date")));
            diaries.add(diary);
        }
        c.close();
        return diaries.get(0);
    }

    /**
     * query all diaries, return cursor
     *
     * @return Cursor
     */
    public Cursor queryTheCursor() {
        Cursor c = db.rawQuery("SELECT * FROM diary ORDER BY diary_date DESC", null);
        return c;
    }

    /**
     * query the diaries from the key, return cursor
     *
     * @return Cursor
     */
    public Cursor find(String key) {
        Cursor c =db.rawQuery("SELECT * FROM diary where diary_content like ? or diary_label like ? ", new String[]{"%"+key+"%","%"+key+"%"});
        return c;
    }

    /**
     * query all diaries, return cursor
     *
     * @return Cursor
     */
    public Cursor queryId(String id) {
        Cursor c = db.rawQuery("SELECT * FROM diary where _id = ? ", new String[]{id});
        return c;
    }

    /**
     * close database
     */
    public void closeDB() {
        db.close();
    }

}
            