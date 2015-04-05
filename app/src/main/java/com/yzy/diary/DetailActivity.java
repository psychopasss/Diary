package com.yzy.diary;

import com.yzy.dao.DBManager;
import com.yzy.diary.model.Diary;

import android.support.v7.app.ActionBarActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
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
		 mgr = new DBManager(this); 
		 TextView diary_label = (TextView) findViewById(R.id.activity_detail_label);
		 TextView diary_content = (TextView) findViewById(R.id.activity_detail_content);
		 Intent intent = getIntent();
		 id=intent.getStringExtra("id");
		 diary = mgr.query(id);
		 diary_label.setText(diary.label);
		 diary_content.setText(diary.content);
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
					try{
						mgr.deleteDiary(id);
						Toast.makeText(DetailActivity.this, "删除成功！" , Toast.LENGTH_SHORT).show();
						finish();
						}catch(Exception x){ 
							Toast.makeText(DetailActivity.this, "删除失败！" , Toast.LENGTH_SHORT).show();
							finish();
						}
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
