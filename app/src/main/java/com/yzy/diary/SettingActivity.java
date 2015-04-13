package com.yzy.diary;

import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.Toast;

import com.readystatesoftware.systembartint.SystemBarTintManager;


public class SettingActivity extends ActionBarActivity {
     SharedPreferences preferences;
     SharedPreferences.Editor editor ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            SystemBarTintManager tintManager = new SystemBarTintManager(this);
            tintManager.setStatusBarTintEnabled(true);
            tintManager.setStatusBarTintResource(R.color.colorPrimary);
        }
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        preferences = getSharedPreferences("diary", MODE_PRIVATE);
        editor = preferences.edit();
        Button button = (Button) findViewById(R.id.changepass);
        Switch open = (Switch) findViewById(R.id.lock);

        if (preferences.getBoolean("lockable", false)) {
            open.setChecked(true);
        } else {
                open.setChecked(false);
        }

        open.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView,
                                         boolean isChecked) {
                // TODO Auto-generated method stub
                if (isChecked) {
                    editor.putBoolean("lockable", true);
                    editor.putString("key", preferences.getString("key",preferences.getString("key","123")));
                    editor.apply();
                } else {
                    editor.putBoolean("lockable", false);
                    editor.apply();
                }
            }
        });

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText editText1 = (EditText) findViewById(R.id.oldpassword);
                EditText editText2 = (EditText) findViewById(R.id.newpassword);
                EditText editText3 = (EditText) findViewById(R.id.renewpassword);
                String oldpassword = editText1.getText().toString();
                String newpassword = editText2.getText().toString();
                String renewpassword = editText3.getText().toString();
                if (oldpassword.equals(preferences.getString("key", null))) {
                    if (!(newpassword.equals(""))) {
                        if (newpassword.equals(renewpassword)) {
                            editor.putString("key", newpassword);
                            editor.apply();
                            Toast.makeText(SettingActivity.this, "密码修改成功!", Toast.LENGTH_SHORT).show();
                            finish();
                        } else {
                            Toast.makeText(SettingActivity.this, "新密码输入不一致，请重新输入!", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(SettingActivity.this, "新密码不能为空!", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(SettingActivity.this, "原密码不正确!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_setting, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        switch (id) {
            case R.id.diary_list:
                finish();
                break;
        }

        return super.onOptionsItemSelected(item);
    }
}
