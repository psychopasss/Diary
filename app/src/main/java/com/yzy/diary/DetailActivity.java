package com.yzy.diary;

import com.readystatesoftware.systembartint.SystemBarTintManager;
import com.yzy.diary.dao.DBManager;
import com.yzy.diary.model.Diary;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Build;
import android.support.v7.app.ActionBarActivity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class DetailActivity extends ActionBarActivity {
	  private DBManager mgr;
	  private Diary diary;
	  private String id;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_detail);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            SystemBarTintManager tintManager = new SystemBarTintManager(this);
            tintManager.setStatusBarTintEnabled(true);
            tintManager.setStatusBarTintResource(R.color.colorPrimary);
        }
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

		 mgr = new DBManager(this); 
		 TextView diary_content = (TextView) findViewById(R.id.activity_detail_content);
		 Intent intent = getIntent();
		 id=intent.getStringExtra("id");
		 diary = mgr.query(id);
         toolbar.setTitle(diary.getLabel());
		 diary_content.setText(diary.getContent());
//         ImageView  mood = (ImageView)findViewById(R.id.detail_mood);
//         ImageView  weather = (ImageView)findViewById(R.id.detail_weather);
//         mood.setImageResource(Integer.parseInt(diary.getMood()));
//         weather.setImageResource(Integer.parseInt(diary.getWeather()));
	}
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.detail, menu);
		 
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id1 = item.getItemId();
		switch (id1){
				case R.id.diary_delete :
                    AlertDialog.Builder builder = new AlertDialog.Builder(this).setTitle("要删除吗？").setMessage("将要删除此篇日记！")
                            .setPositiveButton("删除",new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    try{
                                        mgr.deleteDiary(id);
                                        Toast.makeText(DetailActivity.this, "删除成功！" , Toast.LENGTH_SHORT).show();
                                        finish();
                                    }catch(Exception x){
                                        Toast.makeText(DetailActivity.this, "删除失败！" , Toast.LENGTH_SHORT).show();
                                        finish();
                                    }
                                }
                            })
                            .setNegativeButton("取消", null);
                       builder.create().show();
					break;
				case R.id.diary_update :
					Bundle data = new Bundle() ;
					data.putSerializable("diary", diary);
					Intent intent = new Intent(DetailActivity.this,UpdateActivity.class);
					intent.putExtras(data);
					startActivity(intent);
                    finish();
                    break;
            case R.id.diary_list:
            finish();
			        	break;
		}  
		return super.onOptionsItemSelected(item);
	}
}
