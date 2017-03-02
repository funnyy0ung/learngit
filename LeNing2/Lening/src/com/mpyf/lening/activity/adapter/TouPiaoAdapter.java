package com.mpyf.lening.activity.adapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.ListView;
import android.widget.TextView;

import com.mpyf.lening.R;
import com.mpyf.lening.Jutil.AsyncBitmapLoader;
import com.mpyf.lening.Jutil.Roundbitmap;
import com.mpyf.lening.Jutil.XCRoundImageView;
import com.mpyf.lening.interfaces.http.HttpUse;
import com.mpyf.lening.interfaces.http.Setting;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;

public class TouPiaoAdapter extends BaseAdapter {

	private List<Map<String, Object>> list;
	// private ArrayList<String> list1 = new ArrayList<String>();
	// private ArrayList<String> list2 = new ArrayList<String>();
	private Context context;

	private HashMap<String, ArrayList<String>> map = new HashMap<String, ArrayList<String>>();;
	private DisplayImageOptions options;

	public TouPiaoAdapter(Context context, List<Map<String, Object>> list) {
		this.context = context;
		this.list = list;

		options = new DisplayImageOptions.Builder()
				.showImageOnLoading(R.drawable.defaultimage)
				.showImageOnFail(R.drawable.defaultimage)
				.showImageForEmptyUri(R.drawable.defaultimage)
				.cacheInMemory(true).cacheOnDisk(true)
				.imageScaleType(ImageScaleType.EXACTLY_STRETCHED)
				.bitmapConfig(Bitmap.Config.ALPHA_8).build();

	}

	@Override
	public int getCount() {
		return list.size();
	}

	@Override
	public Object getItem(int position) {
		return list.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {

		final ViewHolder holder;
		// 如果缓存convertView为空，则需要创建View
		if (convertView == null) {
			holder = new ViewHolder();
			convertView = LayoutInflater.from(context).inflate(
					R.layout.item_toupiao, null);

			holder.iv_tou_touxiang = (XCRoundImageView) convertView
					.findViewById(R.id.iv_tou_touxiang);
			holder.tv_tou_name = (TextView) convertView
					.findViewById(R.id.tv_tou_name);
			holder.tv_tou_honor = (TextView) convertView
					.findViewById(R.id.tv_tou_honor);
			holder.iv_tou_payway = (ImageView) convertView
					.findViewById(R.id.iv_tou_payway);
			holder.tv_tou_cost = (TextView) convertView
					.findViewById(R.id.tv_tou_cost);
			holder.tv_tou_time = (TextView) convertView
					.findViewById(R.id.tv_tou_time);
			holder.tv_xuan = (TextView) convertView.findViewById(R.id.tv_xuan);
			holder.tv_title = (TextView) convertView
					.findViewById(R.id.tv_title);
			holder.iv_btn_sure = (ImageView) convertView
					.findViewById(R.id.iv_btn_sure);
			// 如果是图片 需要显示
			holder.iv_image = (ImageView) convertView
					.findViewById(R.id.iv_image);
			holder.lv_xuanxiang = (ListView) convertView
					.findViewById(R.id.lv_xuanxiang);

			holder.tv_tou_num = (TextView) convertView
					.findViewById(R.id.tv_tou_num);

			holder.tv_shengyu = (TextView) convertView
					.findViewById(R.id.tv_shengyu);

			holder.tv_total = (TextView) convertView
					.findViewById(R.id.tv_total);

			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		// 设置数据
		holder.tv_tou_name.setText(list.get(position).get("trueName") + "");
		holder.tv_tou_honor.setText("(" + list.get(position).get("honor_name")
				+ ")");
		holder.tv_tou_time.setText(list.get(position).get("vote_time") + "");
		holder.tv_title.setText(list.get(position).get("vote_title") + "");

		/**
		 * 每人可得
		 */
		holder.tv_tou_cost.setText(list.get(position).get("reward_every") + "");
		/**
		 * 总的
		 */

		holder.tv_total.setText(list.get(position).get("reward_num") + "");
		/**
		 * 剩余
		 */

		// holder.pk_vote = (String) list.get(position).get("pk_vote");

		holder.tv_tou_num.setText(list.get(position).get("vote_num") + "人参与");
		holder.tv_shengyu.setText(list.get(position).get("reward_residue")
				+ "个");

		/**
		 * 设置头像
		 */
		// 设置头像
		holder.iv_tou_touxiang.setImageResource(R.drawable.icon_defualt);
		holder.iv_tou_touxiang.setImageBitmap(Roundbitmap
				.toRoundBitmap(holder.iv_tou_touxiang));
		AsyncBitmapLoader.setRoundImage(holder.iv_tou_touxiang, Setting.apiUrl
				+ "new-pages/PersonalPhoto/"
				+ list.get(position).get("pk_user") + ".jpg");
		// AsyncBitmapLoader.setRoundImage(holder.iv_tou_touxiang,
		// Setting.apiUrl
		// + "new-pages/PersonalPhoto/"
		// + list.get(position).get("pk_user") + ".jpg");
		/**
		 * 如果有图片设置图片
		 */
		if (!TextUtils.isEmpty(list.get(position).get("vote_pic").toString())) {
			holder.iv_image.setVisibility(View.VISIBLE);
			// http://125.35.5.187/votePic/0692898d36744246951b94952c375cb1.jpg
			holder.iv_image.setScaleType(ScaleType.FIT_XY);
			AsyncBitmapLoader.sethoneImage(holder.iv_image, Setting.apiUrl
					+ "votePic/" + list.get(position).get("pk_vote") + ".jpg");

			// ImageLoader imageLoader = ImageLoader.getInstance();
			// imageLoader.displayImage(
			// Setting.apiUrl + "new-pages/PersonalPhoto/"
			// + list.get(position).get("vote_pic").toString()
			// + ".jpg", holder.iv_image, options);

		} else {
			holder.iv_image.setVisibility(View.GONE);
		}

		/**
		 * reward_type 悬赏方式0不悬赏1乐币2金币
		 */
		int s = (Integer) list.get(position).get("reward_type");
		if ((Integer) list.get(position).get("reward_type") == 1) {
			holder.iv_tou_payway.setBackgroundResource(R.drawable.know_icon_le);

		} else if ((Integer) list.get(position).get("reward_type") == 2) {
			holder.iv_tou_payway
					.setBackgroundResource(R.drawable.know_icon_jin);
		} else if ((Integer) list.get(position).get("reward_type") == 0) {
			holder.iv_tou_payway.setVisibility(View.GONE);
			holder.tv_tou_cost.setVisibility(View.GONE);
		}
		/**
		 * option_type选项类型[Int32] 0单选，1多选
		 */

		if ((Integer) list.get(position).get("option_type") == 0) {
			holder.tv_xuan.setText("(单选)");

		} else {
			holder.tv_xuan.setText("(多选)");
		}

		final Toupiao_XuanxiangAdapter adapter = new Toupiao_XuanxiangAdapter(
				context, list, map, position);
		holder.lv_xuanxiang.setAdapter(adapter);

		int totalHeight = 0;

		ArrayList<String> list1 = new ArrayList<String>();
		ArrayList<String> list2 = new ArrayList<String>();
		list2.add(list.get(position).get("option_a").toString());
		list2.add(list.get(position).get("option_b").toString());
		list2.add(list.get(position).get("option_c").toString());
		list2.add(list.get(position).get("option_d").toString());
		list2.add(list.get(position).get("option_e").toString());
		list2.add(list.get(position).get("option_f").toString());
		list2.add(list.get(position).get("option_g").toString());
		list2.add(list.get(position).get("option_h").toString());
		list2.add(list.get(position).get("option_i").toString());
		list2.add(list.get(position).get("option_j").toString());

		for (int i = 0; i < list2.size(); i++) {
			String it = list2.get(i);
			if (!TextUtils.isEmpty(it)) {
				list1.add(it);
			}
		}

		// int option_num = (Integer) list.get(position).get("option_num");
		// if (option_num > 10) {
		// option_num = 10;
		// }

		for (int i = 0; i < list1.size(); i++) {

			View item = adapter.getView(i, null, holder.lv_xuanxiang);
			item.measure(0, 0);
			totalHeight += item.getMeasuredHeight();
		}

		ViewGroup.LayoutParams params = holder.lv_xuanxiang.getLayoutParams();

		params.height = totalHeight
				+ (holder.lv_xuanxiang.getDividerHeight() * (adapter.getCount() - 1));

		holder.lv_xuanxiang.setLayoutParams(params);
		// 运行看看，如果不行换上lv_xuanxiang.requestLayout();试试
		convertView.requestLayout();

		final Handler handler = new Handler() {
			public void handleMessage(android.os.Message msg) {

				if (msg.what == 1) {

					if (map.containsKey(position + "")
							&& (((Integer) list.get(position)
									.get("option_type") == 0 && map.get(
									position + "").size() > 0))
							|| ((Integer) list.get(position).get("option_type") == 1 && map
									.get(position + "").size() > 1)) {

						holder.iv_btn_sure
								.setBackgroundResource(R.drawable.zd_leaderb_btn_det_h);

						holder.iv_btn_sure
								.setOnClickListener(new OnClickListener() {

									@Override
									public void onClick(View v) {
										// TODO 点击确定执行
										// requestTouPiao(position);

										final Handler handler = new Handler() {
											public void handleMessage(
													Message msg) {
												if (msg.what == 1) {
													// 保存成功
													holder.iv_btn_sure
															.setVisibility(View.INVISIBLE);
													// TODO 请求投票结果数据

													RequestResult();

												} else {
													// 保存失败，提示
													holder.iv_btn_sure
															.setVisibility(View.VISIBLE);
												}
											}

										};

										new Thread() {
											public void run() {

												String str = "";
												if (map.get(position + "")
														.contains("0")) {
													str += "a,";
												}
												if (map.get(position + "")
														.contains("1")) {
													str += "b,";
												}
												if (map.get(position + "")
														.contains("2")) {
													str += "c,";
												}
												if (map.get(position + "")
														.contains("3")) {
													str += "d,";
												}
												if (map.get(position + "")
														.contains("4")) {
													str += "e,";
												}
												if (map.get(position + "")
														.contains("5")) {
													str += "f,";
												}
												if (map.get(position + "")
														.contains("6")) {
													str += "g,";
												}
												if (map.get(position + "")
														.contains("7")) {
													str += "h,";
												}
												if (map.get(position + "")
														.contains("8")) {
													str += "i,";
												}
												if (map.get(position + "")
														.contains("9")) {
													str += "j,";
												}

												if (str.contains(",")) {
													str = str.substring(0,
															str.length() - 1);
												}

												Map<String, Object> map = new HashMap<String, Object>();
												map.put("pk_vote",
														list.get(position).get(
																"pk_vote"));
												map.put("userOptions", str);
												Message msg = new Message();

												String result = HttpUse
														.messageget(
																"QueAndAns",
																"saveUserVoteOption",
																map);
												// TODO 打印请求到的数据
												System.out
														.println("=====保存用户投票结果===="
																+ result);

												try {
													JSONObject jo = new JSONObject(
															result);

													if (jo.getBoolean("result")) {
														msg.what = 1;
													} else {
														msg.obj = jo
																.getString("message");
													}
												} catch (JSONException e) {
													msg.obj = e.getMessage();
												}

												handler.sendMessage(msg);

											};
										}.start();

									}

									private void RequestResult() {

										final Handler handler = new Handler() {
											@Override
											public void handleMessage(
													Message msg) {
												if (msg.what == 2) {
													try {

														Map<String, Object> map = new HashMap<String, Object>();
														JSONObject jo = (JSONObject) msg.obj;
														// 投票标题
														map.put("vote_title",
																jo.getString("vote_title"));
														// 图片地址
														map.put("vote_pic",
																jo.getString("vote_pic"));
														// 投票时间
														map.put("vote_time",
																jo.getString("vote_time"));
														// 用户的操作
														map.put("user_option",
																jo.getString("user_option"));
														// 投票人数
														map.put("vote_num",
																jo.getInt("vote_num"));
														// 投票主键
														map.put("pk_vote",
																jo.getString("pk_vote"));
														// 悬赏数
														map.put("reward_num",
																jo.getInt("reward_num"));
														// 悬赏剩余
														map.put("reward_residue",
																jo.getInt("reward_residue"));
														// 每人悬赏
														map.put("reward_every",
																jo.getInt("reward_every"));
														// 选项个数
														map.put("option_num",
																jo.getInt("option_num"));
														// 创建者
														map.put("pk_user",
																jo.getInt("pk_user"));
														// 真实姓名
														map.put("trueName",
																jo.getString("trueName"));
														// 荣誉称号
														map.put("honor_name",
																jo.getString("honor_name"));
														// 是否投票[Int32]
														// 0是否 1是是
														map.put("isVote",
																jo.getInt("isVote"));
														// 悬赏方式0不悬赏1乐币2金币
														map.put("reward_type",
																jo.getInt("reward_type"));

														// 选项类型[Int32]
														// 0单选，1多选
														map.put("option_type",
																jo.getInt("option_type"));

														// 没有投票的选项
														map.put("option_a",
																jo.getString("option_a"));
														map.put("option_b",
																jo.getString("option_b"));
														map.put("option_c",
																jo.getString("option_c"));
														map.put("option_d",
																jo.getString("option_d"));
														map.put("option_e",
																jo.getString("option_e"));
														map.put("option_f",
																jo.getString("option_f"));
														map.put("option_g",
																jo.getString("option_g"));
														map.put("option_h",
																jo.getString("option_h"));
														map.put("option_i",
																jo.getString("option_i"));
														map.put("option_j",
																jo.getString("option_j"));
														// 投过票的选项
														map.put("votes_a",
																jo.getString("votes_a"));
														map.put("votes_b",
																jo.getString("votes_b"));
														map.put("votes_c",
																jo.getString("votes_c"));
														map.put("votes_d",
																jo.getString("votes_d"));
														map.put("votes_e",
																jo.getString("votes_e"));
														map.put("votes_f",
																jo.getString("votes_f"));
														map.put("votes_g",
																jo.getString("votes_g"));
														map.put("votes_h",
																jo.getString("votes_h"));
														map.put("votes_i",
																jo.getString("votes_i"));
														map.put("votes_j",
																jo.getString("votes_j"));

														list.remove(position);
														list.add(position, map);
														notifyDataSetChanged();

													} catch (JSONException e) {
														e.printStackTrace();
													}
												}
											}
										};

										new Thread() {
											@Override
											public void run() {
												Map<String, Object> map = new HashMap<String, Object>();
												map.put("pk_vote",
														list.get(position).get(
																"pk_vote"));
												Message msg = new Message();

												String result = HttpUse
														.messageget(
																"QueAndAns",
																"showVote", map);
												// TODO 打印请求到的数据
												System.out
														.println("=====查看某个投票结果===="
																+ result);

												try {
													JSONObject jo = new JSONObject(
															result);

													if (jo.getBoolean("result")) {
														msg.what = 2;
														msg.obj = jo
																.getJSONObject("data");
													} else {
														msg.obj = jo
																.getString("message");
													}
												} catch (JSONException e) {
													msg.obj = e.getMessage();
												}

												handler.sendMessage(msg);

											};
										}.start();
									}
								});
					} else {
						holder.iv_btn_sure
								.setBackgroundResource(R.drawable.zd_leaderb_btn_det_d);

						holder.iv_btn_sure.setOnClickListener(null);
					}

					adapter.notifyDataSetChanged();
					// notifyDataSetChanged();
				}

			};
		};

		if ((Integer) list.get(position).get("isVote") == 0) {

			// 能投票，显示确定按钮
			holder.iv_btn_sure.setVisibility(View.VISIBLE);
			// TODO 点击进行投票
			holder.lv_xuanxiang
					.setOnItemClickListener(new OnItemClickListener() {

						@Override
						public void onItemClick(AdapterView<?> arg0, View arg1,
								int arg2, long arg3) {

							// TextView tv = (TextView) arg1
							// .findViewById(R.id.tv_xuan);
							ArrayList<String> hs = new ArrayList<String>();
							if (map.containsKey(position + "")) {
								hs.addAll(map.get(position + ""));
							}

							if ((Integer) list.get(position).get("option_type") == 0) {

								if (hs.contains(arg2 + "")) {
									hs.remove(arg2 + "");
								} else {
									hs.clear();
									hs.add(arg2 + "");
								}

							} else {

								if (hs.contains(arg2 + "")) {
									hs.remove(arg2 + "");
									// tv.setTextColor(context.getResources()
									// .getColor(R.color.main));
								} else {
									hs.add(arg2 + "");
									// tv.setTextColor(Color.BLACK);
								}
							}

							map.clear();
							map.put(position + "", hs);
							handler.sendEmptyMessage(1);

						}
					});

		} else {
			// TODO 已经投过票的显示方法
			holder.iv_btn_sure.setVisibility(View.INVISIBLE);
			holder.lv_xuanxiang.setOnItemClickListener(null);

		}

		return convertView;

	}

	static class ViewHolder {
		public String pk_vote;
		public TextView tv_tou_name, tv_tou_honor, tv_tou_cost, tv_tou_time,
				tv_xuan, tv_title, tv_tou_num, tv_total, tv_shengyu;
		public XCRoundImageView iv_tou_touxiang;
		public ImageView iv_tou_payway, iv_btn_sure, iv_image;
		public ListView lv_xuanxiang;
	}
}
