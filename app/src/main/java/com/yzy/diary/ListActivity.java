package com.yzy.diary;

import android.content.Intent;
import android.database.Cursor;
import android.database.CursorWrapper;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

import com.readystatesoftware.systembartint.SystemBarTintManager;
import com.yzy.diary.dao.DBManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SuppressWarnings("ALL")
public class ListActivity extends ActionBarActivity {
    private DBManager mgr;
    private ListView listView;
    private SimpleAdapter simpleAdapter;
    private DrawerLayout mDrawerLayout;
    private ListView lvLeftMenu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);

        /*界面中浮动按钮的设置，绑定新建日记activity*/
        ImageButton imageButton = (ImageButton) findViewById(R.id.fab);
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ListActivity.this, InsertActivity.class));
            }
        });

        /*判断当前android系统是否位于4.4之上，是的话，启动系统沉浸栏*/
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            SystemBarTintManager tintManager = new SystemBarTintManager(this);
            tintManager.setStatusBarTintEnabled(true);
            tintManager.setStatusBarTintResource(R.color.colorPrimary);
        }

        /*Toolbar的设置，用来取代Actionbar，并接管Actionbar中的功能*/
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);

        /*侧滑菜单的设置*/
        mDrawerLayout = (DrawerLayout) findViewById(R.id.dl_left);
        lvLeftMenu = (ListView) findViewById(R.id.lv_left_menu);
        //ActionBarDrawerToggle是一个开关，用于打开/关闭DrawerLayout抽屉
        ActionBarDrawerToggle mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, toolbar,
                R.string.open, R.string.close) {
            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
            }
        };
        mDrawerToggle.syncState();//该方法会自动和actionBar关联, 将开关的图片显示在了action上，
        // 如果不设置，也可以有抽屉的效果，不过是默认的图标
        mDrawerLayout.setDrawerListener(mDrawerToggle);//设置drawer的开关监听

        /*填充侧滑菜单列表*/
        simpleAdapter = new SimpleAdapter(this, getData(),
                R.layout.drawerlayout_left_item,
                new String[]{"title", "img"},
                new int[]{R.id.dl_left_item_label, R.id.dl_left_item_imageView});
        lvLeftMenu.setAdapter(simpleAdapter);

        /* 为侧滑菜单中的选项绑定将要启动的activity*/
        lvLeftMenu.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 0:
                        startActivity(new Intent(ListActivity.this, InsertActivity.class));
                        break;
                    case 1:
                        startActivity(new Intent(ListActivity.this, TrashActivity.class));
                        break;
                    case 2:
                        startActivity(new Intent(ListActivity.this, BackupActivity.class));
                        break;
                    case 3:
                        startActivity(new Intent(ListActivity.this, SettingActivity.class));
                        break;
                    case 4:
                        startActivity(new Intent(ListActivity.this, InfoActivity.class));
                        break;
                    case 5:
                        finish();
                        break;
                }
            }
        });

        /*初始化DBManager对象，为数据提供增删改查的功能*/
        mgr = new DBManager(this);

        /*将日记列表填充至listView*/
        inflater();
    }

    /*设置侧滑菜单List，并返回*/
    private List<Map<String, Object>> getData() {
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        String[] strings = new String[]{"新建日记", "垃圾桶", "日记备份",
                "设置", "关于", "退出"};
        int[] icons = new int[]{R.drawable.ic_1content_new,
                R.drawable.ic_1content_discard, R.drawable.ic_1device_sd_storage,
                R.drawable.ic_1action_settings, R.drawable.ic_1action_about,
                R.drawable.ic_1navigation_cancel};
        Map<String, Object> map;
        int i;
        for (i = 0; i < strings.length; i++) {
            map = new HashMap<String, Object>();
            map.put("title", strings[i]);
            map.put("img", icons[i]);
            list.add(map);
        }

        return list;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.list, menu);

        /*搜索栏的设置*/
        MenuItem search = menu.findItem(R.id.list_search);
        SearchView sv = (SearchView) search.getActionView();
        sv.setSubmitButtonEnabled(true);
        sv.setQueryHint("可搜索内容,标题或时间");
        sv.setIconifiedByDefault(true);
        sv.setOnCloseListener(new SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {
                TextView textView = (TextView) findViewById(R.id.textView);
                textView.setVisibility(View.INVISIBLE);
                inflater();
                return false;
            }
        });
        sv.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                final Cursor c1 = mgr.find(query);
                TextView textView = (TextView) findViewById(R.id.textView);
                //确保查询结果中有"_id"列
                SimpleCursorAdapter adapter = new SimpleCursorAdapter(ListActivity.this, R.layout.list_item, c1,
                        new String[]{"diary_label", "diary_content", "diary_date", "diary_mood", "diary_weather"},
                        new int[]{R.id.list_item_diary_label, R.id.list_item_diary_content, R.id.list_item_diary_date
                                , R.id.img_mood, R.id.img_weather});
                if (adapter.isEmpty()) {
                    listView.setAdapter(null);
                    textView.setVisibility(View.VISIBLE);
                } else {
                    textView.setVisibility(View.INVISIBLE);
                    listView.setAdapter(adapter);
                    listView.setOnItemClickListener(new OnItemClickListener() {
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            Intent intent = new Intent(ListActivity.this, DetailActivity.class);
                            String id1 = c1.getString(0);
                            intent.putExtra("id", id1);
                            startActivity(intent);
                        }
                    });
                }
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mgr.closeDB();
    }

    /*为listView填充日记表列的方法*/
    private void inflater() {
        listView = (ListView) findViewById(R.id.activity_list_listview);
        final Cursor c = mgr.queryTheCursor();
        startManagingCursor(c);    //托付给activity根据自己的生命周期去管理Cursor的生命周期
        final CursorWrapper cursorWrapper = new CursorWrapper(c);
        //使用SimpleCursorAdapter，必须确保查询结果中有"_id"列
        SimpleCursorAdapter adapter;
        adapter = new SimpleCursorAdapter(this, R.layout.list_item,
                cursorWrapper, new String[]{"diary_label", "diary_content", "diary_date", "diary_mood", "diary_weather"},
                new int[]{R.id.list_item_diary_label, R.id.list_item_diary_content,
                        R.id.list_item_diary_date, R.id.img_mood, R.id.img_weather});
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(ListActivity.this, DetailActivity.class);
                String id1 = cursorWrapper.getString(0);
                intent.putExtra("id", id1);
                startActivity(intent);
            }
        });
    }
}
