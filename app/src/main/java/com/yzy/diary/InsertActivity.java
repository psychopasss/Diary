package com.yzy.diary;

import android.content.Context;
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

public class InsertActivity extends ActionBarActivity {
    private DBManager mgr;
    public ImageView mood, weather;
    public GridView gridView;
    private PopupWindow popupWindow;
    private int wh;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_insert);
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

        mood = (ImageView) findViewById(R.id.mood);
        mood.setImageResource(R.drawable.mood4);
        mood.setTag(R.drawable.mood4);
        mood.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                iniPopupWindow("mood");
                popupWindow.showAsDropDown(v);
            }
        });

        weather = (ImageView) findViewById(R.id.weather);
        weather.setImageResource(R.drawable.weather3);
        weather.setTag(R.drawable.weather3);
        weather.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                iniPopupWindow("weather");
                popupWindow.showAsDropDown(v);
            }
        });

    }

    private void iniPopupWindow(final String s) {
        LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View root = inflater.inflate(R.layout.popup, null);
        gridView = (GridView) root.findViewById(R.id.gridView);
        popupWindow = new PopupWindow(root, wh / 3, ViewGroup.LayoutParams.WRAP_CONTENT);
        SimpleAdapter adapter = new SimpleAdapter(InsertActivity.this,
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
        String mood = findViewById(R.id.mood).getTag().toString();
        String weather = findViewById(R.id.weather).getTag().toString();
        if (label.length() < 1) {
            Toast.makeText(InsertActivity.this, "标题不能为空！", Toast.LENGTH_SHORT).show();
            return;
        }
        try {
            Diary diary = new Diary(label, content, mood, weather);
            mgr.add(diary);
            Toast.makeText(InsertActivity.this, "日记保存成功！", Toast.LENGTH_SHORT).show();
            finish();
        } catch (Exception x) {
            Toast.makeText(InsertActivity.this, "日记保存失败！", Toast.LENGTH_SHORT).show();
            finish();
        }
    }
}
