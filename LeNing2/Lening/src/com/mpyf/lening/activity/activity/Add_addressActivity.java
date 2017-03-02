package com.mpyf.lening.activity.activity;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import com.mpyf.lening.R;
import com.mpyf.lening.Jutil.Diaoxian;
import com.mpyf.lening.Jutil.ListenerServer;
import com.mpyf.lening.interfaces.bean.Parame.ShippingAddress;
import com.mpyf.lening.interfaces.http.HttpUse;
import com.mpyf.lening.interfaces.http.Setting;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

/**
 * 添加新地址页面
 * 
 * @author s
 * 
 */
public class Add_addressActivity extends Activity {
	private LinearLayout ll_add_back;
	private TextView tv_save;
	private EditText et_name, et_phone, et_address;
	private ImageView iv_moren;
	private int is_default = 0;
	private int RESULT_ADD_CODE = 12;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_add_address);
		init();
		showinfo();
		addlistener();
	}

	private void init() {
		ll_add_back = (LinearLayout) findViewById(R.id.ll_add_back);
		tv_save = (TextView) findViewById(R.id.tv_save);
		et_name = (EditText) findViewById(R.id.et_name);
		et_phone = (EditText) findViewById(R.id.et_phone);
		et_address = (EditText) findViewById(R.id.et_address);
		iv_moren = (ImageView) findViewById(R.id.iv_moren);
	}

	private void showinfo() {

	}

	private void addlistener() {
		// 返回键
		ListenerServer.setfinish(Add_addressActivity.this, ll_add_back);
		// 保存按钮的点击事件
		tv_save.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO 保存 地址

				if (TextUtils.isEmpty(et_address.getText().toString())
						|| TextUtils.isEmpty(et_name.getText().toString())
						|| TextUtils.isEmpty(et_phone.getText().toString())) {

					Toast.makeText(Add_addressActivity.this, "请完善信息",
							Toast.LENGTH_SHORT).show();

				} else if (!(et_phone.getText().length() == 11)) {
					Toast.makeText(Add_addressActivity.this, "请输入正确的号码",
							Toast.LENGTH_SHORT).show();
				} else {
					final Handler handler = new Handler() {

						@Override
						public void handleMessage(Message msg) {
							if (msg.what == 1) {

								Toast.makeText(Add_addressActivity.this,
										"保存成功", Toast.LENGTH_SHORT).show();

								// setResult(RESULT_ADD_CODE);
								finish();

							} else {
								Diaoxian.showerror(Add_addressActivity.this,
										msg.obj.toString());
							}
						}
					};

					new Thread() {
						public void run() {
							Message msg = new Message();
							ShippingAddress sa = new ShippingAddress();
							sa.setAddress(et_address.getText().toString());
							sa.setConsignee(et_name.getText().toString());
							sa.setIs_default(is_default);
							sa.setMphone(et_phone.getText().toString());
							sa.setPk_user(Setting.currentUser.getPk_user());
							String result = HttpUse.messagepost("QueAndAns",
									"saveShippingAddress", sa);

							try {
								JSONObject jo = new JSONObject(result);
								if (jo.getBoolean("result")) {
									msg.what = 1;
								}
								msg.obj = jo.getString("message");

							} catch (JSONException e) {
								msg.obj = e.getMessage();
							}
							handler.sendMessage(msg);
						};
					}.start();
				}

			}
		});
		// TODO 设置为默认地址
		iv_moren.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				is_default = 1;
				iv_moren.setBackgroundResource(R.drawable.zd_add_btn_se);

			}

		});
	}
}
