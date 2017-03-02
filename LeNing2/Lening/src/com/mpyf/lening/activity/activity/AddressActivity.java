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
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnLastItemVisibleListener;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.mpyf.lening.R;
import com.mpyf.lening.Jutil.Diaoxian;
import com.mpyf.lening.Jutil.ListenerServer;
import com.mpyf.lening.interfaces.http.HttpUse;

/**
 * 管理收货地址页面
 * 
 * @author s
 * 
 */
public class AddressActivity extends Activity {
	protected static final int REQUEST_ADD_CODE = 11;
	protected static final int RESULT_ADD_CODE = 12;
	private LinearLayout ll_address_back;
	private ListView lv_content;
	private TextView tv_add_address;

	private ArrayList<Map<String, Object>> mListData;
	private List<Map<String, Object>> firstlist = new ArrayList<Map<String, Object>>();;
	/** 首次网络请求页码 */
	private static int FIRST_PAGE = 1;
	/** 数据请求页码 **/
	private int toPage = 1;
	/** 更多的网络数据 **/
	private boolean isMore = true;;
	private List<Map<String, Object>> list;// 存储网络数据
	private PullToRefreshListView mPullRefreshListView;
	private AddressAdapter adapter;// listView的适配器
	private int selection = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_address);
		// FIRST_PAGE = 1;
		init();
		// 初始化监听
		initEvent();
		// showinfo();
		getData();
		addlistener();

		// 获取首页数据并设置listView
		mListData = new ArrayList<Map<String, Object>>();
		// new GetDataTask().execute(FIRST_PAGE);
		FIRST_PAGE = 1;
	}

	// @Override
	// public void onResume() {
	// super.onResume();
	// FIRST_PAGE = 1;
	// showinfo();
	// }

	// @Override
	// public void onPause() {
	// super.onPause();
	//
	// }
	//
	// @Override
	// protected void onRestart() {
	// // TODO Auto-generated method stub
	// super.onRestart();
	// showinfo();
	// }

	private void initEvent() {
		// 设置listView的滑动刷新监听
		mPullRefreshListView
				.setOnRefreshListener(new OnRefreshListener<ListView>() {
					@Override
					public void onRefresh(
							PullToRefreshBase<ListView> refreshView) {

						// 获取当前时间并格式化
						String label = DateUtils.formatDateTime(
								getApplicationContext(),
								System.currentTimeMillis(),
								DateUtils.FORMAT_SHOW_TIME
										| DateUtils.FORMAT_SHOW_DATE
										| DateUtils.FORMAT_ABBREV_ALL);

						// Update the LastUpdatedLabel
						refreshView.getLoadingLayoutProxy()
								.setLastUpdatedLabel(label);

						if (PullToRefreshBase.Mode.PULL_FROM_START == mPullRefreshListView
								.getCurrentMode()) {// 下拉刷新

							mPullRefreshListView.getLoadingLayoutProxy()
									.setRefreshingLabel("请稍等...");
							mPullRefreshListView.getLoadingLayoutProxy()
									.setPullLabel("下拉刷新...");
							mPullRefreshListView.getLoadingLayoutProxy()
									.setReleaseLabel("松开自动刷新");

							// 重置集合数据
							mListData = new ArrayList<Map<String, Object>>();
							// new GetDataTask().execute(FIRST_PAGE);
							FIRST_PAGE = 1;
							// 还原toPage初始值
							toPage = 1;
							// 还原上拉加载控制变量
							isMore = true;

						} else if (PullToRefreshBase.Mode.PULL_FROM_END == mPullRefreshListView
								.getCurrentMode()) {// 上拉刷新

							// 上拉刷新时，逐步加载新界面
							toPage++;

							if (isMore) {// 上一次请求有数据
								// 自定义上拉header内容

								mPullRefreshListView.getLoadingLayoutProxy()
										.setPullLabel("上拉刷新...");
								mPullRefreshListView.getLoadingLayoutProxy()
										.setRefreshingLabel("正在为你加载更多内容...");
								mPullRefreshListView.getLoadingLayoutProxy()
										.setReleaseLabel("松开自动刷新...");
							} else {
								// 上一次请求已经没有数据了
								mPullRefreshListView.getLoadingLayoutProxy()
										.setPullLabel("没有更多了...");
								mPullRefreshListView.getLoadingLayoutProxy()
										.setRefreshingLabel("没有更多了...");
								mPullRefreshListView.getLoadingLayoutProxy()
										.setReleaseLabel("没有更多了...");
							}
							// new GetDataTask().execute(toPage);
							toPage++;
						}

					}
				});

		// Add an end-of-list listener
		mPullRefreshListView
				.setOnLastItemVisibleListener(new OnLastItemVisibleListener() {

					@Override
					public void onLastItemVisible() {
						// listView最后一个item可见时触发
						Toast.makeText(AddressActivity.this, "End of List!",
								Toast.LENGTH_SHORT).show();
					}
				});
	}

	// private class GetDataTask extends AsyncTask<Integer, Void, Void> {
	//
	// @Override
	// protected Void doInBackground(Integer... params) {
	// // 本次请求的数据集合
	// // List<ItemAttri> currData = new ArrayList<ItemAttri>();
	// // currData = new GetNetJsonData().getDataFromJson(params[0]);
	//
	// getData();
	// return null;
	// }
	//
	// @Override
	// protected void onPostExecute(Void v) {
	// // AddressAdapter adapter
	// if (adapter == null) {
	// adapter = new AddressAdapter(null, list);
	//
	// // You can also just use setListAdapter(mAdapter) or
	// // mPullRefreshListView.setAdapter(mAdapter)
	// mPullRefreshListView.setAdapter(adapter);
	// } else {
	// adapter.notifyDataSetChanged();
	// }
	//
	// // Call onRefreshComplete when the list has been refreshed.
	// mPullRefreshListView.onRefreshComplete();// 完成刷新动作
	//
	// super.onPostExecute(v);
	// }
	// }

	private void init() {
		ll_address_back = (LinearLayout) findViewById(R.id.ll_address_back);
		// lv_content = (ListView) findViewById(R.id.lv_content);
		tv_add_address = (TextView) findViewById(R.id.tv_add_address);

		mPullRefreshListView = (PullToRefreshListView) findViewById(R.id.pull_refresh_list);
		// 滑动模式设置为双向滑动
		mPullRefreshListView
				.setMode(mPullRefreshListView.getMode() == Mode.BOTH ? Mode.PULL_FROM_START
						: Mode.BOTH);
	}

	// private void showinfo() {
	//
	//
	// adapter = new AddressAdapter(AddressActivity.this, mListData);
	// lv_content.setAdapter(adapter);
	// getData();
	// lv_content.setOnScrollListener(new OnScrollListener() {
	//
	// @Override
	// public void onScrollStateChanged(AbsListView view, int scrollState) {
	//
	// if (isMore && scrollState == 0) {
	// isMore = true;
	// selection = lv_content.getFirstVisiblePosition();
	// FIRST_PAGE++;
	// getData();
	// }
	// }
	//
	// @Override
	// public void onScroll(AbsListView arg0, int arg1, int arg2, int arg3) {
	// if ((arg1 + arg2) == arg3) {
	// isMore = true;
	// } else {
	// isMore = false;
	// }
	// }
	// });
	// }

	protected void getData() {

		final Handler handler = new Handler() {

			@Override
			public void handleMessage(Message msg) {
				if (msg.what == 1) {
					try {
						JSONArray ja = new JSONArray(msg.obj.toString());
						// list = new ArrayList<Map<String, Object>>();
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
						if (!firstlist.isEmpty()) {
							// 有数据返回
							// 数据加入集合中
							mListData.addAll(firstlist);

						} else {
							// 没有数据
							isMore = false;
							// 向主线程发送通知
							// handler.sendEmptyMessage(0);
							// 没有数据toPage--
							toPage--;
						}
						mPullRefreshListView.setAdapter(adapter);
						// adapter.notifyDataSetChanged();
						// mListData.addAll(firstlist);
						// if (page == 1) {

						// lv_content.setAdapter(adapter);
						// }

						// page++;
						// adapter = new AddressAdapter(AddressActivity.this,
						// list);
						// lv_content.setAdapter(adapter);
					} catch (JSONException e) {
						e.printStackTrace();
					}
				} else {
					// if (FIRST_PAGE > 1) {
					// FIRST_PAGE--;
					// }
					Toast.makeText(AddressActivity.this, msg.obj.toString(),
							Toast.LENGTH_SHORT).show();
				}

			}
		};

		new Thread() {

			public void run() {

				Map<String, Object> map = new HashMap<String, Object>();
				map.put("page", FIRST_PAGE);
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

	// /**
	// * 接收子线程传递出来的信息
	// */
	// private Handler mHandler = new Handler() {
	// public void handleMessage(android.os.Message msg) {
	// Toast.makeText(AddressActivity.this, "没有更多了", Toast.LENGTH_SHORT)
	// .show();
	// }
	// };

	private void addlistener() {
		// 返回键
		ListenerServer.setfinish(AddressActivity.this, ll_address_back);
		// 添加新地址
		tv_add_address.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO 跳到添加地址列表
				Intent intent = new Intent(AddressActivity.this,
						Add_addressActivity.class);
				startActivityForResult(intent, REQUEST_ADD_CODE);
			}
		});
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		if (resultCode == RESULT_ADD_CODE) {
			// showinfo();
			getData();
		}

	}

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
		private Handler handler = new Handler() {
			@Override
			public void handleMessage(Message msg) {
				if (msg.what == 1) {
					defAddr_pos = (Integer) msg.obj;
					notifyDataSetChanged();
					mPullRefreshListView.onRefreshComplete();// 完成刷新动作

				} else {

					Toast.makeText(AddressActivity.this, msg.obj.toString(),
							Toast.LENGTH_SHORT).show();
				}
			}
		};

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

					new Thread() {
						public void run() {
							Map<String, Object> map = new HashMap<String, Object>();
							map.put("pk_adr", list.get(position).get("pk_adr"));
							Message msg = new Message();
							String result = HttpUse.messageget("QueAndAns",
									"setDefaultShippingAddress", map);
							// TODO 打印请求到的数据 没判断data
							// System.out.println("=====设置默认地址====" + result);

							try {
								JSONObject jo = new JSONObject(result);
								if (jo.getBoolean("result")) {
									msg.what = 1;
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
					intent.putExtra("is_default", (Integer) list.get(position)
							.get("is_default"));

					startActivity(intent);
				}
			});

			// 跳到修改页面
			holder.ll_delete.setOnClickListener(new OnClickListener() {

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
								mPullRefreshListView.onRefreshComplete();// 完成刷新动作

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
