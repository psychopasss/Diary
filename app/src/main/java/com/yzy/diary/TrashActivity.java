package com.yzy.diary;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.CursorWrapper;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.readystatesoftware.systembartint.SystemBarTintManager;
import com.yzy.diary.dao.DBManager;


public class TrashActivity extends ActionBarActivity {
    private DBManager mgr;
    private ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trash);

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

         /*初始化DBManager对象，为数据提供增删改查的功能*/
        mgr = new DBManager(this);

        /*将日记列表填充至listView*/
        inflater();
    }

    /*为listView填充日记表列的方法*/
    private void inflater() {
        listView = (ListView) findViewById(R.id.activity_trash_listview);
        final Cursor c = mgr.queryTrash();
        startManagingCursor(c);    //托付给activity根据自己的生命周期去管理Cursor的生命周期
        final CursorWrapper cursorWrapper = new CursorWrapper(c);
        //使用SimpleCursorAdapter，必须确保查询结果中有"_id"列
        SimpleCursorAdapter adapter;
        adapter = new SimpleCursorAdapter(this, R.layout.list_item,
                cursorWrapper, new String[]{"diary_label", "diary_content", "diary_date", "diary_mood", "diary_weather"},
                new int[]{R.id.list_item_diary_label, R.id.list_item_diary_content,
                        R.id.list_item_diary_date, R.id.img_mood, R.id.img_weather});

        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String id1 = cursorWrapper.getString(0);
                mgr.recoverDiary(id1);
                Toast.makeText(TrashActivity.this, "日记恢复成功!", Toast.LENGTH_LONG).show();
                inflater();
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_trash, menu);
      /*搜索栏的设置*/
        MenuItem search = menu.findItem(R.id.trash_search);
        SearchView sv = (SearchView) search.getActionView();
        sv.setSubmitButtonEnabled(true);
        sv.setQueryHint("请输入关键字");
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
                final Cursor c1 = mgr.findTrash(query);
                TextView textView = (TextView) findViewById(R.id.textView);
                //确保查询结果中有"_id"列
                SimpleCursorAdapter adapter = new SimpleCursorAdapter(TrashActivity.this, R.layout.list_item, c1,
                        new String[]{"diary_label", "diary_content", "diary_date", "diary_mood", "diary_weather"},
                        new int[]{R.id.list_item_diary_label, R.id.list_item_diary_content, R.id.list_item_diary_date
                                , R.id.img_mood, R.id.img_weather});
                if (adapter.isEmpty()) {
                    listView.setAdapter(null);
                    textView.setVisibility(View.VISIBLE);
                } else {
                    textView.setVisibility(View.INVISIBLE);
                    listView.setAdapter(adapter);
                    listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            Intent intent = new Intent(TrashActivity.this, DetailActivity.class);
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
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        switch (id) {
            case R.id.diary_list:
                finish();
                break;
            case R.id.trash_delete:
                AlertDialog.Builder builder = new AlertDialog.Builder(this).setTitle("要清空吗？").setMessage("将要清空垃圾桶内所有日记！")
                        .setPositiveButton("清空", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                try {
                                    mgr.deleteAllDiary();
                                    inflater();
                                    Toast.makeText(TrashActivity.this, "已清空！", Toast.LENGTH_SHORT).show();
                                } catch (Exception x) {
                                    Toast.makeText(TrashActivity.this, "清空失败！", Toast.LENGTH_SHORT).show();
                                    finish();
                                }
                            }
                        })
                        .setNegativeButton("取消", null);
                builder.create().show();
                break;
        }
        return true;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mgr.closeDB();
    }
}
