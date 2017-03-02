package com.mpyf.lening.activity.activity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.mpyf.lening.R;
import com.mpyf.lening.Jutil.Diaoxian;
import com.mpyf.lening.Jutil.ListenerServer;
import com.mpyf.lening.activity.adapter.ZX_XuanAdapter;
import com.mpyf.lening.interfaces.http.HttpUse;

/**
 * 真相开始页面
 * 
 * @author s
 * 
 */
public class StartZxActivity extends Activity {

	private ImageView iv_start_zhenxiang_back, iv_zx_ans;
	private String sn_aux = "";
	private String labelId = "";
	private TextView tv_zx_wenti, tv_zx_looktruth, tv_zx_next, tv_trueans;
	private ListView lv_zx_xuan;
	private JSONArray ja;
	private ArrayList<String> list1 = new ArrayList<String>();
	private ArrayList<String> list2 = new ArrayList<String>();
	private int qNo;
	private ZX_XuanAdapter adapter;
	private ArrayList<String> checkedItems = new ArrayList<String>();
	private boolean isShow;
	private int eNo;
	private String trueOption;
	private int chooseType;
	String doChoose = "";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_startzx);

		Intent intent = getIntent();
		Bundle bundle = intent.getExtras();
		sn_aux = bundle.getString("sn_aux");
		labelId = bundle.getString("labelId");

		// System.out.println("点击了===========" + sn_aux);
		// System.out.println("点击了===========" + labelId);

		init();
		showinfo();
		addlistener();
	}

	private void init() {
		iv_start_zhenxiang_back = (ImageView) findViewById(R.id.iv_start_zhenxiang_back);
		// 问题
		tv_zx_wenti = (TextView) findViewById(R.id.tv_zx_wenti);
		// 选项
		lv_zx_xuan = (ListView) findViewById(R.id.lv_zx_xuan);
		// 查看真相
		tv_zx_looktruth = (TextView) findViewById(R.id.tv_zx_looktruth);
		// 对错的图标
		iv_zx_ans = (ImageView) findViewById(R.id.iv_zx_ans);
		// 下一题
		tv_zx_next = (TextView) findViewById(R.id.tv_zx_next);
		// 正确答案
		tv_trueans = (TextView) findViewById(R.id.tv_trueans);

		adapter = new ZX_XuanAdapter(StartZxActivity.this, list1, checkedItems);
		lv_zx_xuan.setAdapter(adapter);
	}

	private void saveUser() {

		// TODO 保存答案标签，调用接口
		final Handler handler = new Handler() {
			@Override
			public void handleMessage(Message msg) {
				if (msg.what == 1) {
					// 显示下一题,放到保存标签的接口请求成功之后，上面调用返回false的话，不执行，给出提示
					boolean data = (Boolean) msg.obj;
					if (data) {
						// TODO 判断错误次数小于3,调用接口，置为正确

						if (eNo < 3) {
							editTruth();
						} else {
							Toast.makeText(StartZxActivity.this,
									"你已经错误三次，请稍后再来。", Toast.LENGTH_SHORT)
									.show();
							tv_zx_next.setVisibility(View.GONE);
							finish();
						}

					}
				} else {
					// 弹出吐司msg.obj，提示没有保存成功
					Toast.makeText(StartZxActivity.this, msg.obj.toString(),
							Toast.LENGTH_SHORT).show();
				}
			}

		};

		new Thread() {
			public void run() {
				Map<String, Object> map = new HashMap<String, Object>();
				map.put("labelId", labelId);
				// Message msg = new Message();
				// String result =
				HttpUse.messageget("QueAndAns", "saveUserTruthQALabel", map);
				// // TODO 打印请求到的数据，如果不需要给什么提示这两个接口中，下面这些东西都可以不要
				// System.out.println("=====保存答题标签====" + result);
				//
				// try {
				// JSONObject jo = new JSONObject(result);
				// if (jo.getBoolean("result")) {
				// msg.what = 1;

				// msg.obj = jo.getBoolean("data");
				// } else {
				// msg.obj = jo.getString("message");
				// }
				// } catch (JSONException e) {
				// msg.obj = e.getMessage();
				// }
				//
				// handler.sendMessage(msg);
			};
		}.start();

	}

	private void editTruth() {

		// final Handler handler = new Handler() {
		// @Override
		// public void handleMessage(Message msg) {
		// // TODO 修改成功
		// }
		// };

		new Thread() {
			public void run() {
				Map<String, Object> map = new HashMap<String, Object>();
				map.put("labelId", labelId);
				// Message msg = new Message();
				// String result =
				HttpUse.messageget("QueAndAns", "editTruthQALabelTrue", map);
				// TODO 打印请求到的数据
				// System.out.println("=====保存答题标签====" + result);
				//
				// try {
				// JSONObject jo = new JSONObject(result);
				// if (jo.getBoolean("result")) {
				// msg.what = 1;
				// msg.obj = jo.getBoolean("data");
				// } else {
				// msg.obj = jo.getString("message");
				// }
				// } catch (JSONException e) {
				// msg.obj = e.getMessage();
				// }
				//
				// handler.sendMessage(msg);
			};
		}.start();
	}

	private void showinfo() {
		final Handler handler = new Handler() {
			@Override
			public void handleMessage(Message msg) {
				if (msg.what == 1) {
					setQue(qNo);
					// 在这里调用saveUserTruthQALabel
					saveUser();
				}
			}

		};

		new Thread() {
			public void run() {
				Map<String, Object> map = new HashMap<String, Object>();
				map.put("sn_aux", sn_aux);

				Message msg = new Message();
				String result = HttpUse.messageget("QueAndAns", "listTruthQA",
						map);
				// TODO 打印请求到的数据
				// System.out.println("=====获取真相问答====" + result);

				try {
					JSONObject jo = new JSONObject(result);
					if (jo.getBoolean("result")) {
						msg.what = 1;
						qNo = 0;
						ja = jo.getJSONArray("data");
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

	private void addlistener() {
		// 返回键
		ListenerServer.setfinish(StartZxActivity.this, iv_start_zhenxiang_back);
		// 下一题的点击事件
		tv_zx_next.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO 点击下一题时，查询是否错三次以上如果错三次以上不能再答题，如果，三次以内置为ture

				isShow = false;
				checkedItems.clear();
				hideNext();
				qNo++;
				setQue(qNo);
				tv_zx_looktruth.setVisibility(View.VISIBLE);
			}

		});

		// ListView的条目点击事件
		lv_zx_xuan.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// 记录点击位置

				System.out.println("===打印点击的位置===" + arg2);
				if (arg2 == 0) {
					doChoose = "A";
				} else if (arg2 == 1) {
					doChoose = "B";
				} else if (arg2 == 2) {
					doChoose = "C";
				} else if (arg2 == 3) {
					doChoose = "D";
				} else if (arg2 == 4) {
					doChoose = "E";
				} else if (arg2 == 5) {
					doChoose = "F";
				}

				if (chooseType == 0) {
					// 单选
					if (isShow) {
						// 如果已经选择过，不能再选
						return;
					}

					if (checkedItems.isEmpty()) {
						checkedItems.add(doChoose);
						// tv_zx_looktruth.setVisibility(View.VISIBLE);
					} else if (checkedItems.contains(doChoose)) {
						checkedItems.remove(doChoose);
						// tv_zx_looktruth.setVisibility(View.GONE);
					} 
//					else {
//						// tv_zx_looktruth.setVisibility(View.VISIBLE);
//					}

					adapter.notifyDataSetChanged();
				} else {
					// 多选,查看真相
					if (isShow) {
						// 已经看过答案，不可更改
						return;
					}
					if (checkedItems.contains(doChoose)) {
						checkedItems.remove(doChoose);
					} else {
						checkedItems.add(doChoose);
					}
					adapter.notifyDataSetChanged();
				}

			}

		});

		tv_zx_looktruth.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				System.out.println("====点击查看真相====");
				tv_zx_looktruth.setVisibility(View.GONE);
				isShow = true;
				showNext();

				if (chooseType == 0) {
					showTrueAns(doChoose, trueOption);
				} else {
					Collections.sort(checkedItems);
					String multiChoose = "";
					for (int i = 0; i <= checkedItems.size() - 1; i++) {
						multiChoose += checkedItems.get(i);
						if (i != checkedItems.size() - 1) {
							multiChoose += ",";
						}
					}
					// 运行看看
					// System.out.println("===打印选择的答案==="
					// + multiChoose);
					showTrueAns(multiChoose, trueOption);
				}

				if (qNo == ja.length() - 1) {
					if (eNo < 3) {
						editTruth();
					}
					// 如果是最后一题，把下一题隐藏掉
					tv_zx_next.setVisibility(View.GONE);
					finish();
				} else {
					if (eNo == 3) {
						// 错误三次，自动退出
						Toast.makeText(StartZxActivity.this, "你已经错误三次，请稍后再来。",
								Toast.LENGTH_SHORT).show();
						finish();
					}
					tv_zx_next.setVisibility(View.VISIBLE);
				}

			}
		});

		// try {
		// JSONObject ji = new JSONObject(ja.get(qNo).toString());
		// final int chooseType = ji.getInt("option_type");
		// final String trueOption = ji.getString("true_option");
		// if (chooseType == 0) {
		// 单选
		// if (isShow) {
		// // 如果已经选择过，不能再选
		// return;
		// }
		//
		// if (checkedItems.isEmpty()) {
		// checkedItems.add(doChoose);
		// // tv_zx_looktruth.setVisibility(View.VISIBLE);
		// } else if (checkedItems.contains(doChoose)) {
		// checkedItems.remove(doChoose);
		// // tv_zx_looktruth.setVisibility(View.GONE);
		// } else {
		// // tv_zx_looktruth.setVisibility(View.VISIBLE);
		// }
		//
		// adapter.notifyDataSetChanged();

		// } else {
		// // 多选,查看真相
		// if (isShow) {
		// // 已经看过答案，不可更改
		// return;
		// }
		// if (checkedItems.contains(doChoose)) {
		// checkedItems.remove(doChoose);
		// } else {
		// checkedItems.add(doChoose);
		// }
		// adapter.notifyDataSetChanged();
		// if (checkedItems.isEmpty()) {
		// tv_zx_looktruth.setVisibility(View.GONE);
		// } else {
		// tv_zx_looktruth.setVisibility(View.VISIBLE);
		// }
		// tv_zx_looktruth
		// .setOnClickListener(new OnClickListener() {
		//
		// @Override
		// public void onClick(View v) {
		// System.out.println("====点击查看真相2====");
		// tv_zx_looktruth
		// .setVisibility(View.GONE);
		// isShow = true;
		// showNext();
		// Collections.sort(checkedItems);
		// String multiChoose = "";
		// for (int i = 0; i <= checkedItems
		// .size() - 1; i++) {
		// multiChoose += checkedItems.get(i);
		// if (i != checkedItems.size() - 1) {
		// multiChoose += ",";
		// }
		// }
		// // 运行看看
		// // System.out.println("===打印选择的答案==="
		// // + multiChoose);
		// showTrueAns(multiChoose, trueOption);
		//
		// if (qNo == ja.length() - 1) {
		// if (eNo < 3) {
		// editTruth();
		// }
		// // 如果是最后一题，把下一题隐藏掉
		// tv_zx_next.setVisibility(View.GONE);
		// finish();
		// } else {
		// tv_zx_next
		// .setVisibility(View.VISIBLE);
		// }
		//
		// }
		//
		// });
		// }

		// } catch (JSONException e) {
		// e.printStackTrace();
		// }

	}

	// 显示下一题
	public void showNext() {
		iv_zx_ans.setVisibility(View.VISIBLE);
		tv_zx_next.setVisibility(View.VISIBLE);
		tv_trueans.setVisibility(View.VISIBLE);
	}

	// 隐藏下一题
	public void hideNext() {
		iv_zx_ans.setVisibility(View.GONE);
		tv_zx_next.setVisibility(View.GONE);
		tv_trueans.setVisibility(View.GONE);
	}

	// 显示结果
	public void showTrueAns(String doChoose, String trueOption) {
		if (doChoose.equalsIgnoreCase(trueOption)) {
			// 答对了
			iv_zx_ans.setBackgroundResource(R.drawable.zd_truth_icon_dui);
			tv_trueans.setVisibility(View.GONE);

		} else {
			// 答错了
			eNo++;
			iv_zx_ans.setBackgroundResource(R.drawable.zd_truth_icon_cuo);
			tv_trueans.setText("正确答案：" + trueOption);
		}
	}

	// 显示问题及选项
	private void setQue(int queNo) {
		try {
			JSONObject ji = new JSONObject(ja.get(queNo).toString());
			tv_zx_wenti.setText(ji.getString("qa_title"));

			trueOption = ji.getString("true_option");
			chooseType = ji.getInt("option_type");
			if (chooseType == 0) {
				// 单选
				// tv_zx_looktruth.setVisibility(View.GONE);
				tv_zx_wenti.append("(单选)");
			} else {
				// 多选
				// tv_zx_looktruth.setVisibility(View.VISIBLE);
				tv_zx_wenti.append("(多选)");
			}
			list1.clear();
			checkedItems.clear();
			if (!TextUtils.isEmpty(ji.getString("option_a")))
				list1.add(0, "A、" + ji.getString("option_a"));
			if (!TextUtils.isEmpty(ji.getString("option_b")))
				list1.add(1, "B、" + ji.getString("option_b"));
			if (!TextUtils.isEmpty(ji.getString("option_c")))
				list1.add(2, "C、" + ji.getString("option_c"));
			if (!TextUtils.isEmpty(ji.getString("option_d")))
				list1.add(3, "D、" + ji.getString("option_d"));
			if (!TextUtils.isEmpty(ji.getString("option_e")))
				list1.add(4, "E、" + ji.getString("option_e"));
			if (!TextUtils.isEmpty(ji.getString("option_f")))
				list1.add(5, "F、" + ji.getString("option_f"));

			adapter.notifyDataSetChanged();

		} catch (JSONException e) {
			Diaoxian.showerror(StartZxActivity.this, e.getMessage());
		}
	}

}
