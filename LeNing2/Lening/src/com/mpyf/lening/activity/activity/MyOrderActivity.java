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
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.mpyf.lening.R;
import com.mpyf.lening.Jutil.ListenerServer;
import com.mpyf.lening.interfaces.http.HttpUse;

public class MyOrderActivity extends Activity {
	private LinearLayout ll_order_back;
	private ListView lv_ordercontent;
	private List<Map<String, Object>> list;

	private ArrayList<Map<String, Object>> data;
	private List<Map<String, Object>> firstlist;
	private OrderAdapter adapter;
	private static int page = 1;
	private boolean is_divpage;
	private int selection = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_myorder);
		page = 1;
		init();
		// getdate();
		addlistenr();

	}

	@Override
	public void onResume() {
		super.onResume();
		page = 1;
		getdate();
	}

	@Override
	public void onPause() {
		super.onPause();

	}

	// @Override
	// protected void onRestart() {
	// super.onRestart();
	// getdate();
	// }

	private void init() {
		ll_order_back = (LinearLayout) findViewById(R.id.ll_order_back);
		lv_ordercontent = (ListView) findViewById(R.id.lv_ordercontent);

	}

	private void getdate() {

		data = new ArrayList<Map<String, Object>>();
		adapter = new OrderAdapter(MyOrderActivity.this, data);
		getData();
		lv_ordercontent.setOnScrollListener(new OnScrollListener() {

			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {

				if (is_divpage && scrollState == 0) {
					is_divpage = true;
					selection = lv_ordercontent.getFirstVisiblePosition();
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
				if (msg.what == 1) {
					try {
						JSONArray ja = new JSONArray(msg.obj.toString());
						// list = new ArrayList<Map<String, Object>>();
						for (int i = 0; i < ja.length(); i++) {
							Map<String, Object> map = new HashMap<String, Object>();
							JSONObject jo = ja.getJSONObject(i);
							// 收货地址
							map.put("address", jo.getString("addresss"));
							// 购买金额
							map.put("amount", jo.getInt("amount"));
							// 购买日期
							map.put("buyDate", jo.getString("buyDate"));
							// 支付方式
							map.put("buyWay", jo.getInt("buyWay"));
							// 收货人
							map.put("person", jo.getString("consignee"));
							// 快递名称[String] 虚拟商品时为卡号
							map.put("courierName", jo.getString("courierName"));
							// 快递号[String] 虚拟商品时为密码
							map.put("courierNo", jo.getString("courierNo"));
							// 商品名称
							map.put("goodsName", jo.getString("goodsName"));
							//  商品类型[Int32]1虚拟商品 2实物商品3实物商品（不需邮寄）
							map.put("goods_type", jo.getInt("goods_type"));
							// 是否评价[Int32] 0未评价 1已评价
							map.put("isCom", jo.getInt("isCom"));
							// 联系方式
							map.put("mphone", jo.getString("mphone"));
							// 订单状态[Int32] 0买家已支付 1发货 2完成
							map.put("order_type", jo.getInt("order_type"));
							// 订单主键
							map.put("pk_order", jo.getString("pk_order"));
							// 商品主键
							map.put("pk_goods", jo.getString("pk_goods"));
							firstlist.add(map);

						}

						data.addAll(firstlist);
						if (page == 1) {

							lv_ordercontent.setAdapter(adapter);
						}
						adapter.notifyDataSetChanged();
						page++;

						// OrderAdapter adapter = new OrderAdapter(
						// MyOrderActivity.this, list);
						// lv_ordercontent.setAdapter(adapter);
					} catch (JSONException e) {
						e.printStackTrace();
					}
				} else {
					Toast.makeText(MyOrderActivity.this, msg.obj.toString(),
							Toast.LENGTH_SHORT).show();
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
						"listMyGoodsOrder", map);
				// TODO 打印请求到的数据
				 System.out.println("=====我的订单====" + result);

				try {
					JSONObject jo = new JSONObject(result);
					if (jo.getBoolean("result")) {
						msg.what = 1;
						msg.obj = jo.getJSONArray("data");
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

	private void addlistenr() {
		// 返回键
		ListenerServer.setfinish(this, ll_order_back);
	}

	public class OrderAdapter extends BaseAdapter {

		private LayoutInflater mInflater = null;
		private Context context;
		private List<Map<String, Object>> list;

		private OrderAdapter(Context context, List<Map<String, Object>> list) {
			// 根据context上下文加载布局，这里的是Demo17Activity本身，即this
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
				convertView = mInflater.inflate(R.layout.adapter_order, null);
				holder.rl_comments = (RelativeLayout) convertView
						.findViewById(R.id.rl_comments);
				holder.iv_comments = (ImageView) convertView
						.findViewById(R.id.iv_comments);
				holder.tv_ordernum = (TextView) convertView
						.findViewById(R.id.tv_ordernum);
				holder.tv_ordertype = (TextView) convertView
						.findViewById(R.id.tv_ordertype);
				holder.tv_goodsname = (TextView) convertView
						.findViewById(R.id.tv_goodsname);
				holder.tv_time = (TextView) convertView
						.findViewById(R.id.tv_time);
				holder.tv_amount = (TextView) convertView
						.findViewById(R.id.tv_amount);
				holder.tv_buyway = (TextView) convertView
						.findViewById(R.id.tv_buyway);
				holder.tv_chakan = (TextView) convertView
						.findViewById(R.id.tv_chakan);

				// 将设置好的布局保存到缓存中，并将其设置在Tag里，以便后面方便取出Tag
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}
			// TODO 设置数据
			// 订单号
			if (!TextUtils.isEmpty(String.valueOf(list.get(position).get(
					"pk_order")))) {

				holder.tv_ordernum.setText(String.valueOf(list.get(position)
						.get("pk_order")));
			}
			// 商品名称
			holder.tv_goodsname.setText(list.get(position).get("goodsName")
					+ "");
			holder.tv_time.setText(list.get(position).get("buyDate") + "");
			holder.tv_amount.setText(list.get(position).get("amount") + "");

			// 评论的状态
			int isCom = (Integer) list.get(position).get("isCom");

			if (isCom == 0) {
				holder.iv_comments
						.setBackgroundResource(R.drawable.zd_order_btn_assess);

			} else if (isCom == 1) {
				holder.iv_comments
						.setBackgroundResource(R.drawable.zd_order_btn_modify);
			}

			// 支付方式buyWay 1乐币商品 2 金币商品
			int buyWay = (Integer) list.get(position).get("buyWay");
			if (buyWay == 1) {
				holder.tv_buyway.setText("乐币支付");

			} else if (buyWay == 2) {

				holder.tv_buyway.setText("金币支付");
			}
			// 订单状态 order_type订单状态[Int32] 0买家已支付 1发货 2完成
			int order_type = (Integer) list.get(position).get("order_type");
			if (order_type == 0) {
				holder.tv_ordertype.setText("买家已支付");

			} else if (order_type == 1) {
				holder.tv_ordertype.setText("发货");

			} else if (order_type == 2) {

				holder.tv_ordertype.setText("完成");
			}

			holder.tv_chakan.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					int goods_type = (Integer) list.get(position).get(
							"goods_type");
					if (goods_type == 2) {

						Intent intent = new Intent(context,
								OrederQadetil_ShiActivity.class);
						intent.putExtra(
								"goodsName",
								String.valueOf(list.get(position).get(
										"goodsName")));
						intent.putExtra("buyWay", holder.tv_buyway.getText()
								.toString());
						intent.putExtra("amount", String.valueOf(list.get(
								position).get("amount")));
						intent.putExtra("buyDate", String.valueOf(list.get(
								position).get("buyDate")));
						intent.putExtra("order_type", holder.tv_ordertype
								.getText().toString());
						intent.putExtra("name", String.valueOf(list.get(
								position).get("person")));
						intent.putExtra("phone", String.valueOf(list.get(
								position).get("mphone")));
						intent.putExtra("address", String.valueOf(list.get(
								position).get("address")));
						startActivity(intent);
					} else if (goods_type == 1) {
						Intent intent = new Intent(context,
								OrederQadetil_XuActivity.class);
						intent.putExtra(
								"goodsName",
								String.valueOf(list.get(position).get(
										"goodsName")));
						intent.putExtra("buyWay", holder.tv_buyway.getText()
								.toString());
						intent.putExtra("amount", String.valueOf(list.get(
								position).get("amount")));
						intent.putExtra("buyDate", String.valueOf(list.get(
								position).get("buyDate")));
						intent.putExtra("order_type", holder.tv_ordertype
								.getText().toString());
						intent.putExtra(
								"courierName",
								String.valueOf(list.get(position).get(
										"courierName")));
						intent.putExtra(
								"courierNo",
								String.valueOf(list.get(position).get(
										"courierNo")));

						startActivity(intent);
					}

				}
			});
			// 点击评论
			holder.rl_comments.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO 跳到评价页面或修改页面 isCom是否评价[Int32] 0未评价 1已评价
					// 评论的状态
					int isCom = (Integer) list.get(position).get("isCom");
					// System.out.println("是否评价：" + isCom);

					if (isCom == 0) {

						Intent intent = new Intent(context,
								CommentsActivity.class);
						intent.putExtra("pk_order",
								list.get(position).get("pk_order").toString());
						intent.putExtra("pk_goods",
								list.get(position).get("pk_goods").toString());

						startActivity(intent);
					} else if (isCom == 1) {
						// 已评价 跳到编辑页面
						RequestComments();
					}

				}

				private void RequestComments() {
					// TODO 请求网络 商品评价

					final Handler handler = new Handler() {
						@Override
						public void handleMessage(Message msg) {
							if (msg.what == 1) {
								// TODO 跳转 activity
								JSONObject jo = (JSONObject) msg.obj;
								try {
									Intent intent = new Intent(context,
											EditCommentsedActivity.class);

									intent.putExtra("pk_Com",
											jo.getString("pk_Com"));
									intent.putExtra("pk_goods",
											jo.getString("pk_goods"));
									intent.putExtra("pk_order",
											jo.getString("pk_order"));
									intent.putExtra("com_con",
											jo.getString("com_Con"));
									intent.putExtra("com_Level",
											jo.getInt("com_Level"));
									startActivity(intent);

								} catch (JSONException e) {
									e.printStackTrace();
								}

							} else {
								Toast.makeText(context, msg.obj.toString(),
										Toast.LENGTH_SHORT).show();
							}
						}
					};

					new Thread() {
						public void run() {
							Map<String, Object> map = new HashMap<String, Object>();

							map.put("pk_order",
									list.get(position).get("pk_order"));

							Message msg = new Message();

							String result = HttpUse.messageget("QueAndAns",
									"orderGoodsCom", map);
							// System.out.println("商品评价" + result);
							try {
								JSONObject jo = new JSONObject(result);
								if (jo.getBoolean("result")) {
									msg.what = 1;
									msg.obj = jo.getJSONObject("data");
								} else {
									msg.obj = jo.getString("message");
								}
							} catch (JSONException e) {
								e.printStackTrace();
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
		public RelativeLayout rl_comments;
		private ImageView iv_comments;
		public TextView tv_ordernum, tv_ordertype, tv_goodsname, tv_time,
				tv_amount, tv_buyway, tv_chakan;
	}

}
