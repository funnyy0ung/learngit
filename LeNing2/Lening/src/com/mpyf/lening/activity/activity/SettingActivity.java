package com.mpyf.lening.activity.activity;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.mpyf.lening.R;
import com.mpyf.lening.Jutil.Diaoxian;
import com.mpyf.lening.Jutil.MyDialog;
import com.mpyf.lening.Jutil.Writesaved;
import com.mpyf.lening.interfaces.http.HttpUse;

public class SettingActivity extends Activity implements OnClickListener {

	private LinearLayout ll_setting_back;
	private RelativeLayout rl_setting_changeinfo, rl_setting_changepawd,
			rl_setting_showversion, rl_setting_logoff;

	private ImageView iv_setting_newversion;
	private TextView tv_setting_version;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//new Xiaoxibeijing().changetop(this, R.color.main);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_setting);
		init();
		addlistener();
	}

	private void init() {
		ll_setting_back = (LinearLayout) findViewById(R.id.ll_setting_back);
		rl_setting_changeinfo = (RelativeLayout) findViewById(R.id.rl_setting_changeinfo);
		rl_setting_changepawd = (RelativeLayout) findViewById(R.id.rl_setting_changepawd);
		rl_setting_showversion = (RelativeLayout) findViewById(R.id.rl_setting_showversion);
		rl_setting_logoff = (RelativeLayout) findViewById(R.id.rl_setting_logoff);
		
		iv_setting_newversion=(ImageView) findViewById(R.id.iv_setting_newversion);
		
		tv_setting_version=(TextView) findViewById(R.id.tv_setting_version);
		
		tv_setting_version.setText("当前版本： "+nowversion());
		iv_setting_newversion.setVisibility(View.GONE);
	}

	private void addlistener() {
		ll_setting_back.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				finish();
			}
		});
		rl_setting_changeinfo.setOnClickListener(this);
		rl_setting_changepawd.setOnClickListener(this);
		rl_setting_showversion.setOnClickListener(this);
		rl_setting_logoff.setOnClickListener(this);
	}

	@Override
	public void onClick(View arg0) {
		switch (arg0.getId()) {
		case R.id.rl_setting_changeinfo:
			startActivity(new Intent(SettingActivity.this, ChangeinfoActivity.class));
			break;
		case R.id.rl_setting_changepawd:
			startActivity(new Intent(SettingActivity.this, ChangepwdActivity.class));
			break;
		case R.id.rl_setting_showversion:
			getversion();
			break;
		case R.id.rl_setting_logoff:
			logoff();
			break;
		default:
			break;
		}
	} 
	private void logoff(){//退出登录
		final Dialog dialog=MyDialog.MyDialogloading(this);
		dialog.show();
		final Handler handler=new Handler(){
			@Override
			public void handleMessage(Message msg) {
				dialog.dismiss();
//				Diaoxian.showerror(SettingActivity.this, msg.obj.toString());
//				if(msg.what==1){
//				Writesaved.write(SettingActivity.this,"username" ,"");
//				Writesaved.write(SettingActivity.this,"password" ,"");
				Writesaved.write(SettingActivity.this,"off" ,"off");
					startActivity(new Intent(SettingActivity.this,LoginActivity.class));
					finish();
//				}
			}
		};
		
		new Thread(){
			@Override
			public void run() {
				Map<String,Object> map=new HashMap<String, Object>();
				Message msg=new Message();
				String result=HttpUse.messageget("Account", "logOff", map);
				try {
					JSONObject jo=new JSONObject(result);
					if(jo.getBoolean("result")){
						msg.what=1;
					}
					msg.obj=jo.getString("message");
					handler.sendMessage(msg);
				} catch (JSONException e) {
					msg.obj=e.getMessage();
				}
				handler.sendMessage(msg);
			};
		}.start();
	}
	
	private String nowversion(){
        String version="";
		try {
			// 获取packagemanager的实例
	        PackageManager packageManager = getPackageManager();
	        // getPackageName()是你当前类的包名，0代表是获取版本信息
	        PackageInfo packInfo = packageManager.getPackageInfo("com.mpyf.lening",0);
	        version = packInfo.versionName;
		} catch (NameNotFoundException e) {
			// TODO Auto-generated catch block
			new Diaoxian().showerror(getApplicationContext(),e.getMessage());
		}
		
		return version;
	}
	
	
	private void getversion(){
		if (iv_setting_newversion.getVisibility()==View.GONE) {
			return;
		}else{
			final Dialog dialog=MyDialog.MyDialogShow(this, R.layout.pop_version, 0.8f);
			
			TextView tv_version_main=(TextView) dialog.findViewById(R.id.tv_version_main);
			LinearLayout ll_version_buttons=(LinearLayout) dialog.findViewById(R.id.ll_version_buttons);
			Button bt_version_quit=(Button) dialog.findViewById(R.id.bt_version_quit);
			Button bt_version_download=(Button) dialog.findViewById(R.id.bt_version_download);
			
			bt_version_quit.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub
					dialog.dismiss();
				}
			});
			dialog.show();
		}
	}
}
