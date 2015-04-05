package com.yzy.diary;

import android.content.Intent;
import android.database.Cursor;
import android.database.CursorWrapper;
import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import com.yzy.dao.DBManager;

@SuppressWarnings("ALL")
public class ListActivity extends ActionBarActivity {
    private DBManager mgr;
    private ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);

        mgr = new DBManager(this);
        inflater();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.list, menu);
        MenuItem search = menu.findItem(R.id.list_search);
        SearchView sv = (SearchView) search.getActionView();
        sv.setQueryHint("请输入关键字");
        sv.setIconifiedByDefault(false);
        sv.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                final Cursor c1 = mgr.find(query);
                TextView textView = (TextView) findViewById(R.id.textView);
                //确保查询结果中有"_id"列
                SimpleCursorAdapter adapter = new SimpleCursorAdapter(ListActivity.this, R.layout.list_item, c1,
                        new String[]{"diary_label", "diary_date"}, new int[]{R.id.list_item_diary_label, R.id.list_item_diary_date});
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

        MenuItemCompat.setOnActionExpandListener(search, new MenuItemCompat.OnActionExpandListener() {
            @Override
            public boolean onMenuItemActionCollapse(MenuItem item) {
                inflater();
                return true;
            }
            @Override
            public boolean onMenuItemActionExpand(MenuItem item) {
                return true;
            }
        });


        MenuItem item = menu.findItem(R.id.insert_diary);
        item.setIntent(new Intent(this, InsertActivity.class));

        MenuItem item2 = menu.findItem(R.id.diary_about);
        item2.setIntent(new Intent(this, InfoActivity.class));

        MenuItem item3 = menu.findItem(R.id.diary_setting);
        item3.setIntent(new Intent(this, SettingActivity.class));

        return super.onCreateOptionsMenu(menu);
    }

    private void inflater() {
        listView = (ListView) findViewById(R.id.activity_list_listview);
        Cursor c = mgr.queryTheCursor();
        startManagingCursor(c);    //托付给activity根据自己的生命周期去管理Cursor的生命周期
        final CursorWrapper cursorWrapper = new CursorWrapper(c);
        //确保查询结果中有"_id"列
        SimpleCursorAdapter adapter = new SimpleCursorAdapter(this, R.layout.list_item,
                cursorWrapper, new String[]{"diary_label", "diary_date"},
                new int[]{R.id.list_item_diary_label, R.id.list_item_diary_date});
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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id) {
            case R.id.diary_exit:
                finish();
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mgr.closeDB();
    }

}
