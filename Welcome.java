package com.niit.iweather;


import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

/**
 * 
 * @author hfutqy
 *
 */
public class Welcome extends Activity{
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
//		ActionBar actionBar = getActionBar();
//		actionBar.setDisplayShowHomeEnabled(false);
//		actionBar.setDisplayShowTitleEnabled(false);
		setContentView(R.layout.welcome);
		//线程控制显示welcome界面两秒跳转weather界面
		Runnable run = new Runnable(){
			public void run() {
				Intent intent = new Intent();
				intent.setClass(Welcome.this, Weather.class);
				Welcome.this.startActivity(intent);
				Welcome.this.finish();
			}
		};
		Handler handler = new Handler();
		handler.postDelayed(run, 2000);
	}

}
