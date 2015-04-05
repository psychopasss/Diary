package com.yzy.diary;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import com.yzy.dao.DBManager;
import com.yzy.diary.model.Diary;


public class InsertActivity extends ActionBarActivity {
    private DBManager mgr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_insert);
        mgr = new DBManager(this);

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.insert, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.diary_save:
                diary_save();
                break;
            case R.id.diary_list:
                finish();
                break;
        }
        return true;
    }

    private void diary_save() {
        String label = ((EditText) findViewById(R.id.activity_insert_editText_diarylabel))
                .getText().toString();
        String content = ((EditText) findViewById(R.id.activity_insert_editText_diarycontent))
                .getText().toString();
        try {
            Diary diary = new Diary(label, content);
            mgr.add(diary);
            Toast.makeText(InsertActivity.this, "日记保存成功！", Toast.LENGTH_SHORT).show();
            finish();
        } catch (Exception x) {
            Toast.makeText(InsertActivity.this, "日记保存失败！", Toast.LENGTH_SHORT).show();
            finish();
        }
    }
}
