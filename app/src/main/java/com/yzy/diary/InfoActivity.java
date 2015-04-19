package com.yzy.diary;

import android.app.AlertDialog;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.text.method.LinkMovementMethod;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.readystatesoftware.systembartint.SystemBarTintManager;

public class InfoActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            SystemBarTintManager tintManager = new SystemBarTintManager(this);
            tintManager.setStatusBarTintEnabled(true);
            tintManager.setStatusBarTintResource(R.color.colorPrimary);
        }
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        TextView textview = (TextView) findViewById(R.id.activity_info_textView_weibo);
        textview.setMovementMethod(LinkMovementMethod.getInstance());
        TextView update_logs = (TextView) findViewById(R.id.activity_info_textView_update_logs);
        update_logs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(InfoActivity.this).setTitle(R.string.title_alertDialog).setMessage(R.string.update_logs);
                builder.setNegativeButton("退出", null);
                AlertDialog dialog = builder.create();
                dialog.show();
//                WindowManager.LayoutParams params = dialog.getWindow().getAttributes();
//                params.height = 1500;
//                dialog.getWindow().setAttributes(params);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.info, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        switch (id) {
            case R.id.diary_list:
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
