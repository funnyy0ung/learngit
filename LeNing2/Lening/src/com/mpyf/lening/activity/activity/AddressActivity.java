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
 * �����ջ���ַҳ��
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
	/** �״���������ҳ�� */
	private static int FIRST_PAGE = 1;
	/** ��������ҳ�� **/
	private int toPage = 1;
	/** ������������� **/
	private boolean isMore = true;;
	private List<Map<String, Object>> list;// �洢��������
	private PullToRefreshListView mPullRefreshListView;
	private AddressAdapter adapter;// listView��������
	private int selection = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_address);
		// FIRST_PAGE = 1;
		init();
		// ��ʼ������
		initEvent();
		// showinfo();
		getData();
		addlistener();

		// ��ȡ��ҳ���ݲ�����listView
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
		// ����listView�Ļ���ˢ�¼���
		mPullRefreshListView
				.setOnRefreshListener(new OnRefreshListener<ListView>() {
					@Override
					public void onRefresh(
							PullToRefreshBase<ListView> refreshView) {

						// ��ȡ��ǰʱ�䲢��ʽ��
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
								.getCurrentMode()) {// ����ˢ��

							mPullRefreshListView.getLoadingLayoutProxy()
									.setRefreshingLabel("���Ե�...");
							mPullRefreshListView.getLoadingLayoutProxy()
									.setPullLabel("����ˢ��...");
							mPullRefreshListView.getLoadingLayoutProxy()
									.setReleaseLabel("�ɿ��Զ�ˢ��");

							// ���ü�������
							mListData = new ArrayList<Map<String, Object>>();
							// new GetDataTask().execute(FIRST_PAGE);
							FIRST_PAGE = 1;
							// ��ԭtoPage��ʼֵ
							toPage = 1;
							// ��ԭ�������ؿ��Ʊ���
							isMore = true;

						} else if (PullToRefreshBase.Mode.PULL_FROM_END == mPullRefreshListView
								.getCurrentMode()) {// ����ˢ��

							// ����ˢ��ʱ���𲽼����½���
							toPage++;

							if (isMore) {// ��һ������������
								// �Զ�������header����

								mPullRefreshListView.getLoadingLayoutProxy()
										.setPullLabel("����ˢ��...");
								mPullRefreshListView.getLoadingLayoutProxy()
										.setRefreshingLabel("����Ϊ����ظ�������...");
								mPullRefreshListView.getLoadingLayoutProxy()
										.setReleaseLabel("�ɿ��Զ�ˢ��...");
							} else {
								// ��һ�������Ѿ�û��������
								mPullRefreshListView.getLoadingLayoutProxy()
										.setPullLabel("û�и�����...");
								mPullRefreshListView.getLoadingLayoutProxy()
										.setRefreshingLabel("û�и�����...");
								mPullRefreshListView.getLoadingLayoutProxy()
										.setReleaseLabel("û�и�����...");
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
						// listView���һ��item�ɼ�ʱ����
						Toast.makeText(AddressActivity.this, "End of List!",
								Toast.LENGTH_SHORT).show();
					}
				});
	}

	// private class GetDataTask extends AsyncTask<Integer, Void, Void> {
	//
	// @Override
	// protected Void doInBackground(Integer... params) {
	// // ������������ݼ���
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
	// mPullRefreshListView.onRefreshComplete();// ���ˢ�¶���
	//
	// super.onPostExecute(v);
	// }
	// }

	private void init() {
		ll_address_back = (LinearLayout) findViewById(R.id.ll_address_back);
		// lv_content = (ListView) findViewById(R.id.lv_content);
		tv_add_address = (TextView) findViewById(R.id.tv_add_address);

		mPullRefreshListView = (PullToRefreshListView) findViewById(R.id.pull_refresh_list);
		// ����ģʽ����Ϊ˫�򻬶�
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
							// �����ݷ���
							// ���ݼ��뼯����
							mListData.addAll(firstlist);

						} else {
							// û������
							isMore = false;
							// �����̷߳���֪ͨ
							// handler.sendEmptyMessage(0);
							// û������toPage--
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
				// TODO ��ӡ���󵽵�����
				System.out.println("=====�ջ���ַ�б�====" + result);

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
	// * �������̴߳��ݳ�������Ϣ
	// */
	// private Handler mHandler = new Handler() {
	// public void handleMessage(android.os.Message msg) {
	// Toast.makeText(AddressActivity.this, "û�и�����", Toast.LENGTH_SHORT)
	// .show();
	// }
	// };

	private void addlistener() {
		// ���ؼ�
		ListenerServer.setfinish(AddressActivity.this, ll_address_back);
		// �����µ�ַ
		tv_add_address.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO �������ӵ�ַ�б�
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
	 * ������
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
					mPullRefreshListView.onRefreshComplete();// ���ˢ�¶���

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
				// �����Զ����Item���ּ��ز���
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

				// �����úõĲ��ֱ��浽�����У�������������Tag��Ա���淽��ȡ��Tag
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
				holder.tv_moren.setText("Ĭ�ϵ�ַ");
			} else if (defAddr_pos == position) {
				holder.iv_moren.setBackgroundResource(R.drawable.zd_add_btn_se);
				holder.tv_moren.setText("Ĭ�ϵ�ַ");

			} else {
				holder.iv_moren
						.setBackgroundResource(R.drawable.zd_add_btn_nor);
				holder.tv_moren.setText("��ΪĬ��");

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
							// TODO ��ӡ���󵽵����� û�ж�data
							// System.out.println("=====����Ĭ�ϵ�ַ====" + result);

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

			// �����޸�ҳ��
			holder.ll_edit.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO �����޸�ҳ��
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

			// �����޸�ҳ��
			holder.ll_delete.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO ���� ��ַ

					final Handler handler = new Handler() {

						@Override
						public void handleMessage(Message msg) {
							if (msg.what == 1) {

								Toast.makeText(context, "ɾ���ɹ�",
										Toast.LENGTH_SHORT).show();
								list.remove(position);
								adapter.notifyDataSetChanged();
								mPullRefreshListView.onRefreshComplete();// ���ˢ�¶���

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

	// �������ȶ��壬ViewHolder��̬��
	static class ViewHolder {
		public ImageView iv_moren;
		private LinearLayout ll_delete, ll_edit, ll_default;
		public TextView tv_name, tv_guanli_phone, tv_address, tv_moren;
	}
}