package com.yzy.diary;

import com.yzy.dao.DBManager;
import com.yzy.diary.model.Diary;

import android.support.v7.app.ActionBarActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

public class UpdateActivity extends ActionBarActivity {
	 	private DBManager mgr;
	    private Diary diary;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_update);
		
		mgr = new DBManager(this); 
		Intent intent = getIntent();
		diary=(Diary) intent.getSerializableExtra("diary");
		EditText label = (EditText) findViewById(R.id.activity_update_editText_diarylabel);
		EditText content = (EditText) findViewById(R.id.activity_update_editText_diarycontent);
		label.setText(diary.label);
		content.setText(diary.content);
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
		switch(id){
		        case R.id.diary_save:
		        	diary_update();
		        	break;
		        case R.id.diary_list:
		        	finish();
		        	break;
		}
		return super.onOptionsItemSelected(item);
	}

	private void diary_update() {
		// TODO Auto-generated method stub
		String label = ((EditText)findViewById(R.id.activity_update_editText_diarylabel))
				.getText().toString();
        String content = ((EditText)findViewById(R.id.activity_update_editText_diarycontent))
				.getText().toString();
        diary.setLabel(label);
        diary.setContent(content);
        try{
				mgr.updateDiary(diary);
				Toast.makeText(UpdateActivity.this, "修改成功！" , Toast.LENGTH_SHORT).show();
				finish();
        }catch(Exception x){
    			Toast.makeText(UpdateActivity.this, "修改失败！" , Toast.LENGTH_SHORT).show();
        }
	}
}
