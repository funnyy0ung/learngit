package com.mpyf.lening.activity.activity;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.ActionBar.LayoutParams;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.mpyf.lening.R;
import com.mpyf.lening.Jutil.ImageOptions;
import com.mpyf.lening.Jutil.ListenerServer;
import com.mpyf.lening.interfaces.http.HttpUse;
import com.mpyf.lening.interfaces.http.Setting;
import com.mpyf.lening.service.ScratchView;
import com.mpyf.lening.service.ScratchView.OnShowListener;
import com.mpyf.lening.service.ScratchView.OnStartListener;

public class RenWuActivity extends Activity {
	private LinearLayout ll_zd_renwu_back, ll_zd_qian_content, ll_da_content,
			ll_cai_content, ll_lei_content, ll_denglu, ll_da, ll_cai, ll_lei;
	private TextView tv_zd_rwqian_jiang, tv_ling, tv_da_jiang, tv_da_shu,
			tv_da_ling, tv_cai_jiang, tv_cai_shu, tv_cai, tv_cai_ling,
			tv_lei_jiang, tv_lei_shu, tv_lei_ling, tv_da, tv_lei, tv_zd_rwqian,
			tv_da_taskname, tv_cai_taskname, tv_lei_taskname, tv_da_shuoming,
			tv_cai_shuoming, tv_lei_shuoming;
	private ImageView iv_zd_rwling, iv_da_ling, iv_cai_ling, iv_lei_ling;

	private int checkedItem = -1;
	private PopupWindow mPopWindow;
	private LinearLayout ll_prizepic;
	private ScratchView scr_view;
	private TextView tv_prizename;
	private TextView tv_guamore;
	private Bitmap bitmap;
	private int cardNum;
	private String pkTask1;
	private String pkTask2;
	private String pkTask3;
	private String prizePic;
	private String prizeName;
	private Handler handler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			if (msg.what == 1) {
				try {
					/*
					 * taskState任务状态[Int32]1未完成， 2完成未领取，3已领 ，4全天任务完成;这两种状态没判断啊
					 */
					JSONObject jo = new JSONObject(msg.obj.toString());
					tv_da_taskname.setText(jo.getString("taskName"));
					tv_da_shu.setText(jo.getString("taskInfo"));
					pkTask1 = jo.getString("pkTask");
					if (jo.getInt("taskState") == 1) {
						tv_da_shu.setVisibility(View.VISIBLE);
						iv_da_ling.setVisibility(View.GONE);

						tv_da_jiang.setText("奖励:" + jo.getInt("lcoin")
								+ "乐币   " + jo.getInt("gcoin") + "金币   "
								+ jo.getInt("hcoin") + "经验值");
						// 任务说明
						tv_da_shuoming.setText("1.当天在APP中"
								+ jo.getString("taskName")
								+ ",即可完成任务\n2.每日零点任务重置,完成后记得领取经验");
					} else if (jo.getInt("taskState") == 2) {
						// TODO 2完成未领取
						tv_da_shu.setVisibility(View.GONE);
						iv_da_ling.setVisibility(View.VISIBLE);
						tv_da_jiang.setText("奖励:" + jo.getInt("lcoin")
								+ "乐币   " + jo.getInt("gcoin") + "金币   "
								+ jo.getInt("hcoin") + "经验值");
						// 任务说明
						tv_da_shuoming.setText("1.当天在APP中"
								+ jo.getString("taskName")
								+ ",即可完成任务\n2.每日零点任务重置,完成后记得领取经验");
					} else if (jo.getInt("taskState") == 4) {
						tv_da_jiang.setText("奖励:" + jo.getInt("lcoin")
								+ "乐币   " + jo.getInt("gcoin") + "金币   "
								+ jo.getInt("hcoin") + "经验值");
						// 任务说明
						tv_da_shuoming.setText("1.当天在APP中"
								+ jo.getString("taskName")
								+ ",即可完成任务\n2.每日零点任务重置,完成后记得领取经验");

						tv_da_shu.setVisibility(View.VISIBLE);
						tv_da_shu.setText("已完成");
						iv_da_ling.setVisibility(View.GONE);
						tv_da_ling.setText("已领取");
						tv_da_ling.setOnClickListener(null);
					} else if (jo.getInt("taskState") == 3) {
						tv_da_jiang.setText("奖励:" + jo.getInt("lcoin")
								+ "乐币   " + jo.getInt("gcoin") + "金币   "
								+ jo.getInt("hcoin") + "经验值");
						// 任务说明
						tv_da_shuoming.setText("1.当天在APP中"
								+ jo.getString("taskName")
								+ ",即可完成任务\n2.每日零点任务重置,完成后记得领取经验");

						tv_da_shu.setVisibility(View.VISIBLE);
						tv_da_shu.setText("已完成");
						iv_da_ling.setVisibility(View.GONE);
						tv_da_ling.setText("已领取");
						tv_da_ling.setOnClickListener(null);
					}

				} catch (JSONException e) {
					e.printStackTrace();
				}
			} else if (msg.what == 2) {
				try {
					JSONObject jo = new JSONObject(msg.obj.toString());
					tv_cai_taskname.setText(jo.getString("taskName"));
					tv_cai_shu.setText(jo.getString("taskInfo"));
					pkTask2 = jo.getString("pkTask");
					if (jo.getInt("taskState") == 1) {

						tv_cai_shu.setVisibility(View.VISIBLE);
						iv_cai_ling.setVisibility(View.GONE);

						tv_cai_jiang.setText("奖励:" + jo.getInt("lcoin")
								+ "乐币   " + jo.getInt("gcoin") + "金币   "
								+ jo.getInt("hcoin") + "经验值");
						// 任务说明
						tv_cai_shuoming.setText("1." + jo.getString("taskName")
								+ ",即可完成任务\n2.每日零点任务重置,完成后记得领取经验");
					} else if (jo.getInt("taskState") == 2) {
						// TODO 2完成未领取
						tv_cai_shu.setVisibility(View.GONE);
						iv_cai_ling.setVisibility(View.VISIBLE);

						tv_cai_jiang.setText("奖励:" + jo.getInt("lcoin")
								+ "乐币   " + jo.getInt("gcoin") + "金币   "
								+ jo.getInt("hcoin") + "经验值");
						// 任务说明
						tv_cai_shuoming.setText("1." + jo.getString("taskName")
								+ ",即可完成任务\n2.每日零点任务重置,完成后记得领取经验");
					} else if (jo.getInt("taskState") == 4) {

						tv_cai_jiang.setText("奖励:" + jo.getInt("lcoin")
								+ "乐币   " + jo.getInt("gcoin") + "金币   "
								+ jo.getInt("hcoin") + "经验值");
						// 任务说明
						tv_cai_shuoming.setText("1." + jo.getString("taskName")
								+ ",即可完成任务\n2.每日零点任务重置,完成后记得领取经验");
						tv_cai_shu.setVisibility(View.VISIBLE);
						tv_cai_shu.setText("已完成");
						tv_cai_ling.setText("已领取");
						iv_cai_ling.setVisibility(View.GONE);
						// 你设置的是第一个，这是第二个
						tv_cai_ling.setOnClickListener(null);

					} else if (jo.getInt("taskState") == 3) {

						tv_cai_jiang.setText("奖励:" + jo.getInt("lcoin")
								+ "乐币   " + jo.getInt("gcoin") + "金币   "
								+ jo.getInt("hcoin") + "经验值");
						// 任务说明
						tv_cai_shuoming.setText("1." + jo.getString("taskName")
								+ ",即可完成任务\n2.每日零点任务重置,完成后记得领取经验");
						tv_cai_shu.setVisibility(View.VISIBLE);
						tv_cai_shu.setText("已完成");
						tv_cai_ling.setText("已领取");
						iv_cai_ling.setVisibility(View.GONE);
						// 你设置的是第一个，这是第二个
						tv_cai_ling.setOnClickListener(null);

					}
				} catch (JSONException e) {
					e.printStackTrace();
				}
			} else if (msg.what == 3) {
				try {
					JSONObject jo = new JSONObject(msg.obj.toString());
					tv_lei_taskname.setText(jo.getString("taskName"));
					tv_lei_shu.setText(jo.getString("taskInfo"));
					pkTask3 = jo.getString("pkTask");
					if (jo.getInt("taskState") == 1) {
						tv_lei_shu.setVisibility(View.VISIBLE);
						iv_lei_ling.setVisibility(View.GONE);

						tv_lei_jiang.setText("奖励:" + jo.getInt("lcoin")
								+ "乐币   " + jo.getInt("gcoin") + "金币   "
								+ jo.getInt("hcoin") + "经验值");
						// 任务说明
						tv_lei_shuoming.setText("1.在APP中"
								+ jo.getString("taskName")
								+ ",即可完成任务\n2.每日零点任务重置,完成后记得领取经验");
					} else if (jo.getInt("taskState") == 2) {
						// TODO 2完成未领取
						tv_lei_shu.setVisibility(View.GONE);
						iv_lei_ling.setVisibility(View.VISIBLE);

						tv_lei_jiang.setText("奖励:" + jo.getInt("lcoin")
								+ "乐币   " + jo.getInt("gcoin") + "金币   "
								+ jo.getInt("hcoin") + "经验值");
						// 任务说明
						tv_lei_shuoming.setText("1.在APP中"
								+ jo.getString("taskName")
								+ ",即可完成任务\n2.每日零点任务重置,完成后记得领取经验");
					} else if (jo.getInt("taskState") == 4) {

						tv_lei_jiang.setText("奖励:" + jo.getInt("lcoin")
								+ "乐币   " + jo.getInt("gcoin") + "金币   "
								+ jo.getInt("hcoin") + "经验值");

						// 任务说明
						tv_lei_shuoming.setText("1.在APP中"
								+ jo.getString("taskName")
								+ ",即可完成任务\n2.每日零点任务重置,完成后记得领取经验");

						tv_lei_shu.setVisibility(View.VISIBLE);
						tv_lei_shu.setText("已完成");
						tv_lei_ling.setText("已领取");
						tv_lei_ling.setOnClickListener(null);
						iv_lei_ling.setVisibility(View.GONE);

					} else if (jo.getInt("taskState") == 3) {

						tv_lei_jiang.setText("奖励:" + jo.getInt("lcoin")
								+ "乐币   " + jo.getInt("gcoin") + "金币   "
								+ jo.getInt("hcoin") + "经验值");

						// 任务说明
						tv_lei_shuoming.setText("1.在APP中"
								+ jo.getString("taskName")
								+ ",即可完成任务\n2.每日零点任务重置,完成后记得领取经验");

						tv_lei_shu.setVisibility(View.VISIBLE);
						tv_lei_shu.setText("已完成");
						tv_lei_ling.setText("已领取");
						tv_lei_ling.setOnClickListener(null);
						iv_lei_ling.setVisibility(View.GONE);

					}

				} catch (JSONException e) {
					e.printStackTrace();
				}
			} else if (msg.what == 4) {
				try {

					JSONObject jo = new JSONObject(msg.obj.toString());
					if (jo.getBoolean("data")) {
						iv_zd_rwling.setOnClickListener(null);
						iv_zd_rwling
								.setBackgroundResource(R.drawable.zd_know_btn_receive_sel);
						tv_ling.setText("已领取");
					} else {
						iv_zd_rwling
								.setBackgroundResource(R.drawable.zd_know_btn_receive_nor);
						tv_ling.setText("点击领取");

						/**
						 * 点击领取的点击事件
						 */
						iv_zd_rwling.setOnClickListener(new OnClickListener() {
							@Override
							public void onClick(View v) {
								iv_zd_rwling
										.setBackgroundResource(R.drawable.zd_know_btn_receive_sel);
								tv_ling.setText("已领取");
								getCard();
							}
						});
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}
			} else if (msg.what == 5) {
				tv_zd_rwqian_jiang.setText("卡数:" + cardNum + "张刮刮卡");
				// 点击卡数弹出刮刮卡
				tv_zd_rwqian_jiang.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						// 弹出刮刮卡
						if (cardNum > 0) {
							showPopWindow();
						} else {
							Toast.makeText(getApplicationContext(),
									"很抱歉，您没有卡了~", Toast.LENGTH_SHORT).show();
						}

					}
				});

			} else if (msg.what == 6) {

				try {
					JSONObject jo = new JSONObject(msg.obj.toString());
					if (jo.getBoolean("result")) {
						cardNum = cardNum + 1;
						handler.sendEmptyMessage(5);
						iv_zd_rwling.setOnClickListener(null);
						iv_zd_rwling
								.setBackgroundResource(R.drawable.zd_know_btn_receive_sel);
						// 弹出刮刮卡
						showPopWindow();

					}
				} catch (JSONException e) {
					e.printStackTrace();
				}

			} else if (msg.what == 7) {

				try {
					JSONObject jo = new JSONObject(msg.obj.toString());
					if (jo.getBoolean("result")) {
						System.out.println("==获取popWindow数据源==="
								+ msg.obj.toString());
						cardNum = cardNum - 1;
						tv_guamore.setText("再刮一次" + "(还剩" + cardNum + "张卡)");
						handler.sendEmptyMessage(5);
						JSONObject ja = jo.getJSONObject("data");
						prizePic = ja.getString("prizePic");
						prizeName = ja.getString("prizeName");
						new Thread() {
							public void run() {
								bitmap = ImageOptions.getBitmap(Setting.apiUrl
										+ prizePic);
								handler.sendEmptyMessage(9);
							}
						}.start();

					}
				} catch (JSONException e) {
					e.printStackTrace();
				}

			} else if (msg.what == 8) {
				String name = prizeName;
				if ("未中".equals(name)) {
					ll_prizepic
							.setBackgroundResource(R.drawable.zd_scratch_regreat_bg);
				} else if ("获取一张刮刮卡".equals(name)) {
					cardNum = cardNum + 1;
					handler.sendEmptyMessage(5);
					ll_prizepic
							.setBackgroundResource(R.drawable.zd_scratch_con_bg);
				} else {
					ll_prizepic
							.setBackgroundResource(R.drawable.zd_scratch_con_bg);
				}
				tv_prizename.setText(name);
			} else if (msg.what == 9) {
				// 把奖品图片设置上去
				scr_view.setScratchBackground(bitmap);
			} else if (msg.what == 10) {
				try {
					// 隐藏领取
					iv_da_ling.setVisibility(View.GONE);
					tv_da_shu.setVisibility(View.VISIBLE);
					JSONObject jo = new JSONObject(msg.obj.toString());
					tv_da_taskname.setText(jo.getString("taskName"));
					tv_da_jiang.setText("奖励:" + jo.getInt("lcoin") + "乐币   "
							+ jo.getInt("gcoin") + "金币   " + jo.getInt("hcoin")
							+ "经验值");
					// 任务说明
					tv_da_shuoming.setText("1.当天在APP中"
							+ jo.getString("taskName")
							+ ",即可完成任务\n2.每日零点任务重置,完成后记得领取经验");
					tv_da_shu.setText(jo.getString("taskInfo"));
				} catch (JSONException e) {
					e.printStackTrace();
				}
			} else if (msg.what == 11) {
				try {
					// 隐藏领取
					System.out.println("点击了领取");
					iv_cai_ling.setVisibility(View.GONE);
					tv_cai_shu.setVisibility(View.VISIBLE);
					JSONObject jo = new JSONObject(msg.obj.toString());
					tv_cai_taskname.setText(jo.getString("taskName"));
					tv_cai_jiang.setText("奖励:" + jo.getInt("lcoin") + "乐币   "
							+ jo.getInt("gcoin") + "金币   " + jo.getInt("hcoin")
							+ "经验值");
					// 任务说明
					tv_cai_shuoming.setText("1." + jo.getString("taskName")
							+ ",即可完成任务\n2.每日零点任务重置,完成后记得领取经验");
					tv_cai_shu.setText(jo.getString("taskInfo"));
				} catch (JSONException e) {
					e.printStackTrace();
				}
			} else if (msg.what == 12) {
				try {
					// 隐藏领取
					System.out.println("点击了领取累计奖励");
					iv_lei_ling.setVisibility(View.GONE);
					tv_lei_shu.setVisibility(View.VISIBLE);
					JSONObject jo = new JSONObject(msg.obj.toString());
					tv_lei_taskname.setText(jo.getString("taskName"));
					tv_lei_jiang.setText("奖励:" + jo.getInt("lcoin") + "乐币   "
							+ jo.getInt("gcoin") + "金币   " + jo.getInt("hcoin")
							+ "经验值");
					// 任务说明
					tv_lei_shuoming.setText("1.在APP中"
							+ jo.getString("taskName")
							+ ",即可完成任务\n2.每日零点任务重置,完成后记得领取经验");

					tv_lei_shu.setText(jo.getString("taskInfo"));
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_myrenwu);
		init();
		showinfo();
		addlistener();
	}

	private void showinfo() {

		/**
		 * 
		 * 获取答题赚经验的数据
		 */
		new Thread() {
			public void run() {

				Map<String, Object> map = new HashMap<String, Object>();
				map.put("taskType", 1);
				Message msg = new Message();
				String result = HttpUse.messageget("QueAndAns", "getUserTask",
						map);
				// TODO
				System.out.println("====类型一++获取答题赚经验的数据====" + result);
				try {
					JSONObject jo = new JSONObject(result);
					if (jo.getBoolean("result")) {
						msg.what = 1;
						msg.obj = jo.getString("data");
					} else {
						msg.obj = jo.getString("message");
					}
				} catch (JSONException e) {
					msg.obj = e.getMessage();
				}

				handler.sendMessage(msg);

			};
		}.start();
		/**
		 * 获取采纳赚经验的数据
		 */
		new Thread() {
			public void run() {

				Map<String, Object> map = new HashMap<String, Object>();
				map.put("taskType", 2);
				Message msg = new Message();
				String result = HttpUse.messageget("QueAndAns", "getUserTask",
						map);
				// TODO
				System.out.println("====类型二++获取采纳赚经验的数据====" + result);
				try {
					JSONObject jo = new JSONObject(result);
					if (jo.getBoolean("result")) {
						msg.what = 2;
						msg.obj = jo.getString("data");
					} else {
						msg.obj = jo.getString("message");
					}
				} catch (JSONException e) {
					msg.obj = e.getMessage();
				}

				handler.sendMessage(msg);

			};
		}.start();
		/**
		 * 获取累计采纳的数据
		 */
		new Thread() {
			public void run() {

				Map<String, Object> map = new HashMap<String, Object>();
				map.put("taskType", 3);
				Message msg = new Message();
				String result = HttpUse.messageget("QueAndAns", "getUserTask",
						map);
				// TODO
				System.out.println("====类型三++ 获取累计采纳的数据====" + result);
				try {
					JSONObject jo = new JSONObject(result);
					if (jo.getBoolean("result")) {
						msg.what = 3;
						msg.obj = jo.getString("data");
					} else {
						msg.obj = jo.getString("message");
					}
				} catch (JSONException e) {
					msg.obj = e.getMessage();
				}

				handler.sendMessage(msg);

			};
		}.start();

		/***
		 * 查询是否已经领取过刮刮卡
		 */
		new Thread() {
			public void run() {
				Map<String, Object> map = new HashMap<String, Object>();
				Message msg = new Message();
				String result = HttpUse.messageget("QueAndAns", "isReceive",
						map);
				System.out.println("===查询是否领刮刮卡===" + result);
				try {
					JSONObject jo = new JSONObject(result);
					if (jo.getBoolean("result")) {
						msg.what = 4;
						msg.obj = result;
						// msg.obj根本没赋值
					} else {
						msg.obj = jo.getString("message");
					}
				} catch (JSONException e) {
					msg.obj = e.getMessage();
				}

				handler.sendMessage(msg);

			};
		}.start();

		/**
		 * 获取刮刮卡的张数
		 */

		new Thread() {
			public void run() {
				Map<String, Object> map = new HashMap<String, Object>();
				Message msg = new Message();
				String result = HttpUse.messageget("QueAndAns", "getUserCard",
						map);
				System.out.println("===获取刮刮卡张数===" + result + "zhang");
				try {
					JSONObject jo = new JSONObject(result);
					if (jo.getBoolean("result")) {
						msg.what = 5;
						cardNum = jo.getInt("data");
					} else {
						msg.obj = jo.getString("message");
					}
				} catch (JSONException e) {
					msg.obj = e.getMessage();
				}

				handler.sendMessage(msg);

			};
		}.start();

	}

	private void init() {
		ll_zd_renwu_back = (LinearLayout) findViewById(R.id.ll_zd_renwu_back);
		ll_zd_qian_content = (LinearLayout) findViewById(R.id.ll_zd_qian_content);
		ll_da_content = (LinearLayout) findViewById(R.id.ll_da_content);
		ll_cai_content = (LinearLayout) findViewById(R.id.ll_cai_content);
		ll_lei_content = (LinearLayout) findViewById(R.id.ll_lei_content);
		/**
		 * 添加的
		 */
		ll_denglu = (LinearLayout) findViewById(R.id.ll_denglu);
		ll_da = (LinearLayout) findViewById(R.id.ll_da);
		ll_cai = (LinearLayout) findViewById(R.id.ll_cai);
		ll_lei = (LinearLayout) findViewById(R.id.ll_lei);

		tv_zd_rwqian_jiang = (TextView) findViewById(R.id.tv_zd_rwqian_jiang);
		tv_ling = (TextView) findViewById(R.id.tv_ling);
		tv_da_jiang = (TextView) findViewById(R.id.tv_da_jiang);
		tv_da_shu = (TextView) findViewById(R.id.tv_da_shu);
		tv_da_ling = (TextView) findViewById(R.id.tv_da_ling);
		tv_cai_jiang = (TextView) findViewById(R.id.tv_cai_jiang);
		tv_cai_shu = (TextView) findViewById(R.id.tv_cai_shu);
		tv_cai = (TextView) findViewById(R.id.tv_cai);
		tv_cai_ling = (TextView) findViewById(R.id.tv_cai_ling);
		tv_lei_jiang = (TextView) findViewById(R.id.tv_lei_jiang);
		tv_lei_shu = (TextView) findViewById(R.id.tv_lei_shu);
		tv_lei_ling = (TextView) findViewById(R.id.tv_lei_ling);
		tv_zd_rwqian = (TextView) findViewById(R.id.tv_zd_rwqian);
		tv_da = (TextView) findViewById(R.id.tv_da);
		tv_lei = (TextView) findViewById(R.id.tv_lei);
		// 任务主干
		tv_da_taskname = (TextView) findViewById(R.id.tv_da_taskname);
		tv_cai_taskname = (TextView) findViewById(R.id.tv_cai_taskname);
		tv_lei_taskname = (TextView) findViewById(R.id.tv_lei_taskname);
		// 任务说明
		tv_da_shuoming = (TextView) findViewById(R.id.tv_da_shuoming);
		tv_cai_shuoming = (TextView) findViewById(R.id.tv_cai_shuoming);
		tv_lei_shuoming = (TextView) findViewById(R.id.tv_lei_shuoming);

		iv_zd_rwling = (ImageView) findViewById(R.id.iv_zd_rwling);
		iv_da_ling = (ImageView) findViewById(R.id.iv_da_ling);
		iv_cai_ling = (ImageView) findViewById(R.id.iv_cai_ling);
		iv_lei_ling = (ImageView) findViewById(R.id.iv_lei_ling);
	}

	// 请求刮奖卡的数据
	private void getCardMessage() {

		new Thread() {
			public void run() {
				Map<String, Object> map = new HashMap<String, Object>();
				Message msg = new Message();
				String result = HttpUse.messageget("QueAndAns", "scratchCard",
						map);
				System.out.println("===刮卡===" + result);
				try {
					JSONObject jo = new JSONObject(result);
					if (jo.getBoolean("result")) {
						msg.what = 7;
						msg.obj = result;
						// msg.obj根本没赋值
					} else {
						msg.obj = jo.getString("message");
					}
				} catch (JSONException e) {
					msg.obj = e.getMessage();
				}

				handler.sendMessage(msg);

			};
		}.start();
	}

	private void getCard() {
		// 调用领取的接口
		new Thread() {
			@Override
			public void run() {
				Map<String, Object> map = new HashMap<String, Object>();
				Message msg = new Message();
				String result = HttpUse.messageget("QueAndAns", "receiveCard",
						map);
				System.out.println("===领刮刮卡===" + result);
				try {
					JSONObject jo = new JSONObject(result);
					if (jo.getBoolean("result")) {
						msg.what = 6;
						msg.obj = result;
					} else {
						msg.obj = jo.getString("message");
					}
				} catch (JSONException e) {
					msg.obj = e.getMessage();
				}

				handler.sendMessage(msg);

			}
		}.start();
	}

	private void showPopWindow() {
		// 设置contentView
		View contentView = LayoutInflater.from(RenWuActivity.this).inflate(
				R.layout.popuplayout, null);
		ll_prizepic = (LinearLayout) contentView.findViewById(R.id.ll_prizepic);
		ll_prizepic.setBackgroundResource(R.drawable.zd_scratch_bg);
		scr_view = (ScratchView) contentView.findViewById(R.id.scr_view);
		tv_prizename = (TextView) contentView.findViewById(R.id.tv_prizename);
		tv_prizename.setText("赶紧刮开刮奖区看看这张幸运\n		卡里藏着什么吧!");
		// 随便写个图测试一下，好了，运行看看
		// scr_view.setScratchBackground((Bitmap) map.get("bitmap"));
		scr_view.addStartListener(new OnStartListener() {

			@Override
			public void startListener() {
				// 开始刮卡，请求卡片数据
				getCardMessage();
			}

		});
		scr_view.addShowListener(new OnShowListener() {

			@Override
			public void showListener() {
				// TODO 已刮开
				System.out.println("测试一下是否监听到刮了50%");
				handler.sendEmptyMessage(8);
			}

		});
		mPopWindow = new PopupWindow(contentView, LayoutParams.MATCH_PARENT,
				LayoutParams.MATCH_PARENT, true);
		mPopWindow.setContentView(contentView);
		mPopWindow.setFocusable(true); // 设置PopupWindow可获得焦点
		mPopWindow.setTouchable(true); // 设置PopupWindow可触摸
		// mPopWindow.setOutsideTouchable(true); // 设置非PopupWindow区域可触摸
		mPopWindow.setBackgroundDrawable(new BitmapDrawable());
		// 显示PopupWindow
		View rootview = LayoutInflater.from(RenWuActivity.this).inflate(
				R.layout.activity_myrenwu, null);
		mPopWindow.showAtLocation(rootview, Gravity.CENTER, 5, 5);
		// 关闭按钮
		ImageView iv_close = (ImageView) contentView
				.findViewById(R.id.iv_close);
		/**
		 * 再刮一次
		 */
		tv_guamore = (TextView) contentView.findViewById(R.id.tv_guamore);
		tv_guamore.setText("再刮一次" + "(还剩" + cardNum + "张卡)");
		tv_guamore.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (cardNum > 0) {
					ll_prizepic.setBackgroundResource(R.drawable.zd_scratch_bg);
					tv_prizename.setText("赶紧刮开刮奖区看看这张幸运\n		卡里藏着什么吧!");
					// 应该调用那个方法
					getCardMessage();
				} else {
					Toast.makeText(getApplicationContext(), "很抱歉，您没有卡了~",
							Toast.LENGTH_SHORT).show();
				}

			}
		});
		/**
		 * 点击关闭popWindow
		 */
		iv_close.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				mPopWindow.dismiss();
				iv_zd_rwling
						.setBackgroundResource(R.drawable.zd_know_btn_receive_sel);
				tv_ling.setText("已领取");
			}
		});
	}

	private void addlistener() {
		// 返回键
		ListenerServer.setfinish(RenWuActivity.this, ll_zd_renwu_back);
		/**
		 * 签到点击出现下拉内容
		 */
		ll_denglu.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (checkedItem == 0) {
					checkedItem = -1;
				} else {
					checkedItem = 0;
				}
				hideAll();
			}
		});

		/**
		 * 答题赚经验点击出现下拉内容
		 */
		ll_da.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (checkedItem == 1) {
					checkedItem = -1;
				} else {
					checkedItem = 1;
				}
				hideAll();

			}
		});
		tv_da_ling.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(RenWuActivity.this,
						ZhidaoActivity.class);
				startActivity(intent);
			}
		});
		/**
		 * 采纳赚经验点击出现下拉内容
		 */
		ll_cai.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (checkedItem == 2) {
					checkedItem = -1;
				} else {
					checkedItem = 2;
				}
				hideAll();

			}
		});
		tv_cai_ling.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(RenWuActivity.this,
						ZhidaoActivity.class);
				startActivity(intent);

			}
		});
		/**
		 * 累计采纳点击出现下拉内容
		 */
		ll_lei.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (checkedItem == 3) {
					checkedItem = -1;
				} else {
					checkedItem = 3;
				}
				hideAll();
			}
		});
		tv_lei_ling.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(RenWuActivity.this,
						ZhidaoActivity.class);
				startActivity(intent);
			}
		});

		// 采纳领取的点击事件
		iv_cai_ling.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				new Thread() {
					public void run() {
						// TODO 重新请求数据调用 getUserTask//领任务奖

						Map<String, Object> map = new HashMap<String, Object>();
						// 需要传
						map.put("pkTask", pkTask2);
						System.out.println("看看pkTask2" + pkTask2);
						map.put("taskType", 2);
						Message msg = new Message();
						String result = HttpUse.messageget("QueAndAns",
								"taskaward", map);
						System.out.print("获取点击结果" + result);
						try {
							JSONObject jo = new JSONObject(result);
							if (jo.getBoolean("result")) {
								msg.what = 11;
								msg.obj = jo.getString("data");
							} else {
								msg.obj = jo.getString("message");
							}
						} catch (JSONException e) {
							msg.obj = e.getMessage();
						}

						handler.sendMessage(msg);

					};
				}.start();
			}
		});

		// 答题领取的点击事件
		iv_da_ling.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				new Thread() {
					public void run() {
						// TODO 重新请求数据调用 taskaward//领任务奖

						Map<String, Object> map = new HashMap<String, Object>();
						// 需要传
						map.put("pkTask", pkTask1);
						System.out.println("看看pkTask1" + pkTask1);
						map.put("taskType", 1);
						Message msg = new Message();
						String result = HttpUse.messageget("QueAndAns",
								"taskaward", map);
						try {
							JSONObject jo = new JSONObject(result);
							if (jo.getBoolean("result")) {
								msg.what = 10;
								msg.obj = jo.getString("data");
							} else {
								msg.obj = jo.getString("message");
							}
						} catch (JSONException e) {
							msg.obj = e.getMessage();
						}

						handler.sendMessage(msg);

					};
				}.start();
			}
		});
		// 累计采纳领取的点击事件
		iv_lei_ling.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				new Thread() {
					public void run() {
						// TODO 重新请求数据调用 getUserTask//领任务奖

						Map<String, Object> map = new HashMap<String, Object>();
						// 需要传
						map.put("pkTask", pkTask3);
						System.out.println("看看pkTask3" + pkTask3);
						map.put("taskType", 3);
						Message msg = new Message();
						String result = HttpUse.messageget("QueAndAns",
								"taskaward", map);
						System.out.print("获取点击结果" + result);
						try {
							JSONObject jo = new JSONObject(result);
							if (jo.getBoolean("result")) {
								msg.what = 12;
								msg.obj = jo.getString("data");
							} else {
								msg.obj = jo.getString("message");
							}
						} catch (JSONException e) {
							msg.obj = e.getMessage();
						}

						handler.sendMessage(msg);

					};
				}.start();
			}
		});
	}

	// 判断是否展示详情界面
	private void hideAll() {
		tv_zd_rwqian.setBackgroundResource(R.drawable.zd_task_icon_down);
		tv_da.setBackgroundResource(R.drawable.zd_task_icon_down);
		tv_cai.setBackgroundResource(R.drawable.zd_task_icon_down);
		tv_lei.setBackgroundResource(R.drawable.zd_task_icon_down);
		ll_zd_qian_content.setVisibility(View.GONE);
		ll_da_content.setVisibility(View.GONE);
		ll_cai_content.setVisibility(View.GONE);
		ll_lei_content.setVisibility(View.GONE);
		switch (checkedItem) {
		case -1:
			break;
		case 0:
			tv_zd_rwqian.setBackgroundResource(R.drawable.zd_task_icon_up);
			ll_zd_qian_content.setVisibility(View.VISIBLE);
			break;
		case 1:
			tv_da.setBackgroundResource(R.drawable.zd_task_icon_up);
			ll_da_content.setVisibility(View.VISIBLE);
			break;
		case 2:
			tv_cai.setBackgroundResource(R.drawable.zd_task_icon_up);
			ll_cai_content.setVisibility(View.VISIBLE);
			break;
		case 3:
			tv_lei.setBackgroundResource(R.drawable.zd_task_icon_up);
			ll_lei_content.setVisibility(View.VISIBLE);
			break;
		default:
			break;
		}
	}

}
