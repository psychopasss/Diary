package com.yzy.diary;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.readystatesoftware.systembartint.SystemBarTintManager;
import com.yzy.diary.dao.DBManager;
import com.yzy.diary.model.Diary;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UpdateActivity extends ActionBarActivity {
    public ImageView mood, weather;
    public GridView gridView;
    int[] moods = new int[]{
            R.drawable.mood1, R.drawable.mood2, R.drawable.mood3,
            R.drawable.mood4, R.drawable.mood5, R.drawable.mood6,
            R.drawable.mood7, R.drawable.mood8, R.drawable.mood9,
            R.drawable.mood10, R.drawable.mood11, R.drawable.mood12
    };
    int[] weathers = new int[]{
            R.drawable.weather1, R.drawable.weather2, R.drawable.weather3,
            R.drawable.weather4, R.drawable.weather5, R.drawable.weather6,
            R.drawable.weather7, R.drawable.weather8, R.drawable.weather9,
            R.drawable.weather10, R.drawable.weather11, R.drawable.weather12
    };
    private DBManager mgr;
    private Diary diary;
    private PopupWindow popupWindow;
    private int wh;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update);
        WindowManager windowManager = getWindowManager();
        Display display = windowManager.getDefaultDisplay();
        wh = display.getWidth();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            SystemBarTintManager tintManager = new SystemBarTintManager(this);
            tintManager.setStatusBarTintEnabled(true);
            tintManager.setStatusBarTintResource(R.color.colorPrimary);
        }
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mgr = new DBManager(this);
        Intent intent = getIntent();
        diary = (Diary) intent.getSerializableExtra("diary");
        EditText label = (EditText) findViewById(R.id.activity_update_editText_diarylabel);
        EditText content = (EditText) findViewById(R.id.activity_update_editText_diarycontent);
        mood = (ImageView) findViewById(R.id.update_mood);
        weather = (ImageView) findViewById(R.id.update_weather);
        label.setText(diary.getLabel());
        content.setText(diary.getContent());
        mood.setImageResource(Integer.parseInt(diary.getMood()));
        mood.setTag(Integer.parseInt(diary.getMood()));
        weather.setImageResource(Integer.parseInt(diary.getWeather()));
        weather.setTag(Integer.parseInt(diary.getWeather()));
        mood.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                iniPopupWindow("mood");
                popupWindow.showAsDropDown(v);
            }
        });
        weather.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                iniPopupWindow("weather");
                popupWindow.showAsDropDown(v);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.insert, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        switch (id) {
            case R.id.diary_save:
                AlertDialog.Builder builder = new AlertDialog.Builder(this).setTitle("要修改吗？").setMessage("将要保存修改后的日记！")
                        .setPositiveButton("保存", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                diary_update();
                            }
                        })
                        .setNegativeButton("取消", null);
                builder.create().show();
                break;
            case R.id.diary_list:
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void diary_update() {
        String label = ((EditText) findViewById(R.id.activity_update_editText_diarylabel))
                .getText().toString();
        String content = ((EditText) findViewById(R.id.activity_update_editText_diarycontent))
                .getText().toString();
        String mood = findViewById(R.id.update_mood).getTag().toString();
        String weather = findViewById(R.id.update_weather).getTag().toString();
        diary.setLabel(label);
        diary.setContent(content);
        diary.setMood(mood);
        diary.setWeather(weather);
        if (label.length() < 1) {
            Toast.makeText(UpdateActivity.this, "标题不能为空！", Toast.LENGTH_SHORT).show();
            return;
        }
        try {
            mgr.updateDiary(diary);
            Toast.makeText(UpdateActivity.this, "修改成功！", Toast.LENGTH_SHORT).show();
            finish();
        } catch (Exception x) {
            Toast.makeText(UpdateActivity.this, "修改失败！", Toast.LENGTH_SHORT).show();
        }
    }

    private void iniPopupWindow(final String s) {
        LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View root = inflater.inflate(R.layout.popup, null);
        gridView = (GridView) root.findViewById(R.id.gridView);
        popupWindow = new PopupWindow(root, wh / 3, ViewGroup.LayoutParams.WRAP_CONTENT);
        SimpleAdapter adapter = new SimpleAdapter(UpdateActivity.this,
                getData(s), R.layout.popup_item, new String[]{"item_img"}
                , new int[]{R.id.popup_item});
        gridView.setAdapter(adapter);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (s.equals("mood")) {
                    mood.setImageResource(moods[position]);
                    mood.setTag(moods[position]);
                    popupWindow.dismiss();
                } else {
                    weather.setImageResource(weathers[position]);
                    weather.setTag(weathers[position]);
                    popupWindow.dismiss();
                }
            }
        });

        //设置popwindow获取焦点，不然item监听不到事件。在android4.4之上不需要设置，之下都要设置
        popupWindow.setFocusable(true);

        // 控制popupwindow点击屏幕其他地方消失
        popupWindow.setBackgroundDrawable(getResources().getDrawable(R.drawable.bg_popupwindow));// 设置背景图片，不能在布局中设置，要通过代码来设置
        popupWindow.setOutsideTouchable(true);// 触摸popupwindow外部，popupwindow消失。
        // 这个要求你的popupwindow要有背景图片才可以成功，如上
    }

    private List<Map<String, Object>> getData(String s) {
        List<Map<String, Object>> list = new ArrayList<>();
        int i;
        if (s.equals("mood")) {
            for (i = 0; i < 12; i++) {
                Map<String, Object> map = new HashMap<>();
                map.put("item_img", moods[i]);
                list.add(map);
            }
        } else {
            for (i = 0; i < 12; i++) {
                Map<String, Object> map = new HashMap<>();
                map.put("item_img", weathers[i]);
                list.add(map);
            }
        }
        return list;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mgr.closeDB();
    }
}
