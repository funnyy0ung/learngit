package com.mpyf.lening.activity.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AbsListView.OnScrollListener;

import com.mpyf.lening.R;
import com.mpyf.lening.Jutil.Diaoxian;
import com.mpyf.lening.Jutil.ListenerServer;
import com.mpyf.lening.activity.adapter.ShangCheng_LeAdapter;
import com.mpyf.lening.interfaces.http.HttpUse;

/**
 * 管理收货地址页面
 * 
 * @author s
 * 
 */
public class YF_AddressActivity extends Activity {
	// protected static final int REQUEST_ADD_CODE = 11;
	// protected static final int RESULT_ADD_CODE = 12;
	private LinearLayout ll_address_back;
	private TextView tv_add_address;
	private AddressAdapter adapter;

	private ListView lv_content;
	private List<Map<String, Object>> list;
	private ArrayList<Map<String, Object>> data;
	private List<Map<String, Object>> firstlist;
	private static int page = 1;
	private boolean is_divpage;
	private int selection = 0;
	private int isdefault;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_yf_address);
		page = 1;
		init();
		// showinfo();
		addlistener();
	}

	// @Override
	// protected void onRestart() {
	// // TODO Auto-generated method stub
	// super.onRestart();
	// showinfo();
	// }
	@Override
	public void onResume() {
		super.onResume();
		page = 1;
		showinfo();
	}

	@Override
	public void onPause() {
		super.onPause();

	}

	private void init() {
		ll_address_back = (LinearLayout) findViewById(R.id.ll_address_back);
		lv_content = (ListView) findViewById(R.id.lv_content);
		tv_add_address = (TextView) findViewById(R.id.tv_add_address);
	}

	private void showinfo() {

		data = new ArrayList<Map<String, Object>>();
		adapter = new AddressAdapter(YF_AddressActivity.this, data);
		getData();
		lv_content.setOnScrollListener(new OnScrollListener() {

			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {

				if (is_divpage && scrollState == 0) {
					is_divpage = true;
					selection = lv_content.getFirstVisiblePosition();
					getData();
				}
			}

			@Override
			public void onScroll(AbsListView arg0, int arg1, int arg2, int arg3) {
				if ((arg1 + arg2) == arg3) {
					is_divpage = true;
				} else {
					is_divpage = false;
				}
			}
		});
	}

	private void getData() {
		firstlist = new ArrayList<Map<String, Object>>();
		final Handler handler = new Handler() {

			@Override
			public void handleMessage(Message msg) {
				try {
					JSONArray ja = new JSONArray(msg.obj.toString());
					list = new ArrayList<Map<String, Object>>();
					for (int i = 0; i < ja.length(); i++) {
						Map<String, Object> map = new HashMap<String, Object>();
						JSONObject jo = ja.getJSONObject(i);
						map.put("address", jo.getString("address"));
						map.put("consignee", jo.getString("consignee"));
						map.put("is_default", jo.getInt("is_default"));
						map.put("mphone", jo.getString("mphone"));
						map.put("pk_adr", jo.getString("pk_adr"));
						firstlist.add(map);

					}
					data.addAll(firstlist);
					if (page == 1) {

						lv_content.setAdapter(adapter);
					}
					adapter.notifyDataSetChanged();
					page++;
					// adapter = new AddressAdapter(YF_AddressActivity.this,
					// list);
					// lv_content.setAdapter(adapter);
				} catch (JSONException e) {
					e.printStackTrace();
				}

			}
		};

		new Thread() {

			public void run() {

				Map<String, Object> map = new HashMap<String, Object>();
				map.put("page", page);
				map.put("pageSize", 20);
				Message msg = new Message();

				String result = HttpUse.messageget("QueAndAns",
						"listShippingAddress", map);
				// TODO 打印请求到的数据
				System.out.println("=====收货地址列表====" + result);

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
	}

	private void addlistener() {
		// 返回键
		ListenerServer.setfinish(YF_AddressActivity.this, ll_address_back);
		// 添加新地址
		tv_add_address.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO 跳到添加地址列表
				Intent intent = new Intent(YF_AddressActivity.this,
						Add_addressActivity.class);
				startActivity(intent);
				// startActivityForResult(intent, REQUEST_ADD_CODE);
			}
		});
	}

	//
	// @Override
	// protected void onActivityResult(int requestCode, int resultCode, Intent
	// data) {
	// super.onActivityResult(requestCode, resultCode, data);
	//
	// if (resultCode == RESULT_ADD_CODE) {
	// showinfo();
	// }
	//
	// }

	/**
	 * 适配器
	 * 
	 * @author s
	 * 
	 */
	public class AddressAdapter extends BaseAdapter {

		private LayoutInflater mInflater = null;
		private Context context;
		private List<Map<String, Object>> list;
		private int defAddr_pos = -1;

		private AddressAdapter(Context context, List<Map<String, Object>> list) {
			this.mInflater = LayoutInflater.from(context);
			this.context = context;
			this.list = list;
		}

		@Override
		public int getCount() {
			return list.size();
		}

		@Override
		public Object getItem(int arg0) {
			return list.get(arg0);
		}

		@Override
		public long getItemId(int arg0) {
			return arg0;
		}

		@Override
		public View getView(final int position, View convertView,
				ViewGroup parent) {
			final ViewHolder holder;
			if (convertView == null) {
				// 根据自定义的Item布局加载布局
				holder = new ViewHolder();
				convertView = mInflater.inflate(R.layout.adapter_address, null);
				holder.iv_moren = (ImageView) convertView
						.findViewById(R.id.iv_moren);
				holder.ll_delete = (LinearLayout) convertView
						.findViewById(R.id.ll_delete);
				holder.ll_edit = (LinearLayout) convertView
						.findViewById(R.id.ll_edit);
				holder.ll_default = (LinearLayout) convertView
						.findViewById(R.id.ll_default);

				holder.tv_name = (TextView) convertView
						.findViewById(R.id.tv_name);
				holder.tv_guanli_phone = (TextView) convertView
						.findViewById(R.id.tv_guanli_phone);
				holder.tv_address = (TextView) convertView
						.findViewById(R.id.tv_address);
				holder.tv_moren = (TextView) convertView
						.findViewById(R.id.tv_moren);

				// 将设置好的布局保存到缓存中，并将其设置在Tag里，以便后面方便取出Tag
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}

			holder.tv_name.setText((list.get(position).get("consignee") + "")
					.trim());
			holder.tv_guanli_phone.setText(list.get(position).get("mphone")
					.toString().trim());
			holder.tv_address.setText((list.get(position).get("address") + "")
					.trim());

			int is_default = (Integer) list.get(position).get("is_default");

			if (is_default == 1 && defAddr_pos == -1) {
				defAddr_pos = position;
				holder.iv_moren.setBackgroundResource(R.drawable.zd_add_btn_se);
				holder.tv_moren.setText("默认地址");
			} else if (defAddr_pos == position) {
				holder.iv_moren.setBackgroundResource(R.drawable.zd_add_btn_se);
				holder.tv_moren.setText("默认地址");

			} else {
				holder.iv_moren
						.setBackgroundResource(R.drawable.zd_add_btn_nor);
				holder.tv_moren.setText("设为默认");

			}

			holder.ll_default.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {

					final Handler handler = new Handler() {
						@Override
						public void handleMessage(Message msg) {
							if (msg.what == 2) {
								// defAddr_pos = (Integer) msg.obj;
								defAddr_pos = position;
								Toast.makeText(YF_AddressActivity.this,
										"设置默认地址成功", Toast.LENGTH_SHORT).show();
								adapter.notifyDataSetChanged();

							} else {

								Toast.makeText(YF_AddressActivity.this,
										msg.obj.toString(), Toast.LENGTH_SHORT)
										.show();
							}
						}
					};

					new Thread() {
						public void run() {
							Map<String, Object> map = new HashMap<String, Object>();
							map.put("pk_adr", list.get(position).get("pk_adr"));
							Message msg = new Message();
							String result = HttpUse.messageget("QueAndAns",
									"setDefaultShippingAddress", map);
							// TODO 打印请求到的数据 没判断data
							System.out.println("=====设置默认地址====" + result);
							isdefault = 1;

							try {
								JSONObject jo = new JSONObject(result);
								if (jo.getBoolean("result")) {
									msg.what = 2;
									msg.obj = position;
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

			// 跳到修改页面
			holder.ll_edit.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					adapter.notifyDataSetChanged();
					// TODO 跳到修改页面
					Intent intent = new Intent(context,
							EditAddressActivity.class);
					intent.putExtra("pk_adr",
							String.valueOf(list.get(position).get("pk_adr")));
					intent.putExtra("name",
							String.valueOf(list.get(position).get("consignee")));
					intent.putExtra("phone",
							String.valueOf(list.get(position).get("mphone")));
					intent.putExtra("address",
							String.valueOf(list.get(position).get("address")));
					intent.putExtra("is_default", isdefault);
					startActivity(intent);
				}
			});
			// 跳到修改页面
			holder.ll_delete.setOnClickListener(new OnClickListener() {

				// @Override
				// public void onClick(View v) {
				// // TODO 跳到修改页面
				// Intent intent = new Intent(context,
				// EditAddressActivity.class);
				// startActivity(intent);
				// }

				@Override
				public void onClick(View v) {
					// TODO 保存 地址

					final Handler handler = new Handler() {

						@Override
						public void handleMessage(Message msg) {
							if (msg.what == 1) {

								Toast.makeText(context, "删除成功",
										Toast.LENGTH_SHORT).show();
								list.remove(position);
								adapter.notifyDataSetChanged();

							} else {
								Diaoxian.showerror(context, msg.obj.toString());
							}
						}
					};

					new Thread() {
						public void run() {
							Message msg = new Message();
							Map<String, Object> map = new HashMap<String, Object>();
							map.put("pk_adr", list.get(position).get("pk_adr"));
							String result = HttpUse.messageget("QueAndAns",
									"deleteShippingAddress", map);

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

			});

			return convertView;
		}
	}

	// 在外面先定义，ViewHolder静态类
	static class ViewHolder {
		public ImageView iv_moren;
		private LinearLayout ll_delete, ll_edit, ll_default;
		public TextView tv_name, tv_guanli_phone, tv_address, tv_moren;
	}
}
