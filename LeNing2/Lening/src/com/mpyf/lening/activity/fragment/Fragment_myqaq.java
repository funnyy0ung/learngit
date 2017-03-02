package com.mpyf.lening.activity.fragment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.mpyf.lening.R;
import com.mpyf.lening.Jutil.Diaoxian;
import com.mpyf.lening.activity.activity.QadetilActivity;
import com.mpyf.lening.activity.adapter.Wenda1;
import com.mpyf.lening.interfaces.http.HttpUse;

public class Fragment_myqaq extends Fragment {

	private ListView lv_list;
	private List<Map<String, Object>> list ;
	private String[] from={"isanswered","context","from","answer"};
	private int[] to={R.id.tv_buyeddayi_isanswered,R.id.tv_buyeddayi_context,R.id.tv_buyeddayi_from,R.id.tv_buyeddayi_answer};
	private ArrayList<Map<String, Object>> data;
	private Wenda1 adapter;
	private static int page = 1;
	private boolean is_divpage;
	private int selection = 0;
	
	public Fragment_myqaq() {
	}
	@Override
	public View onCreateView(android.view.LayoutInflater inflater,
			android.view.ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_list, null);
		page=1;
		init(view);
//		showinfo();
		return view;
	};
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
	private void init(View view) {
		lv_list = (ListView) view.findViewById(R.id.lv_list);
	}
//	@Override
//	public void onResume() {
//		super.onResume();
//		
//		page = 1;
//		showinfo();
//		adapter.notifyDataSetChanged();
//	}

	private void showinfo() {

		data = new ArrayList<Map<String, Object>>();
		adapter = new Wenda1(getActivity(), data);
		getData();

		lv_list.setOnScrollListener(new OnScrollListener() {

			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {

				if (is_divpage && scrollState == 0) {
					is_divpage = true;
					selection = lv_list.getFirstVisiblePosition();
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
		list = new ArrayList<Map<String, Object>>();
		
		final Handler handler=new Handler(){
			@Override
			public void handleMessage(Message msg) {
				// TODO Auto-generated method stub
				if(msg.what==1){
					try {
						JSONArray ja=new JSONArray(msg.obj.toString());
						for(int i=0;i<ja.length();i++){
							Map<String,Object> map=new HashMap<String, Object>();
							JSONObject jo=ja.getJSONObject(i);
							map.put("id", jo.getString("PK_Que"));
							map.put("userid", jo.getInt("pk_user"));
							map.put("UserName", jo.getString("userName"));
							map.put("Nickname", jo.getString("nickname"));
							map.put("QUE_CONTENT", jo.getString("QUE_CONTENT"));
							map.put("REWARD_WAY", jo.getInt("REWARD_WAY"));
							map.put("REWARD_Num", jo.getString("REWARD_Num"));
							map.put("Ans_Num", jo.getString("ans_Num"));
							map.put("QUE_STATE", jo.getInt("QUE_STATE"));
							map.put("PIC_NUM", jo.getInt("pic_num"));
							map.put("trueName", jo.getString("trueName"));
							//�Ƿ��ղع�
							map.put("isCollection", jo.getInt("isCollection"));
							//�����ƺ�ͼƬ��ַ
							map.put("honor_pic", jo.getString("honor_pic"));
							// �����ƺ�
							map.put("honor_name", jo.getString("honor_name"));
							
							list.add(map);
						}
						//MyqaAdapter adapter=new MyqaAdapter(getActivity(), list);
						data.addAll(list);
						if (page == 1) {

							lv_list.setAdapter(adapter);
						}
						adapter.notifyDataSetChanged();
						page++;
						
						// listview ��Ŀ����¼�
						lv_list.setOnItemClickListener(new OnItemClickListener() {

							@Override
							public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
									long arg3) {
								// TODO Auto-generated method stub
								Intent intent =new Intent(getActivity(),QadetilActivity.class);
								intent.putExtra("id",data.get(arg2).get("id").toString());
								startActivity(intent);
							}
						});
						
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						Diaoxian.showerror(getActivity(), e.getMessage());
					}
				}else{
					Diaoxian.showerror(getActivity(), msg.obj.toString());
				}
			}
		};
		
		new Thread(){
			@Override
			public void run() {
				Map<String,Object> map=new HashMap<String, Object>();
				map.put("page", page);
				map.put("pageSize", 5);
				
				Message msg=new Message();
				
				String result=HttpUse.messageget("QueAndAns", "myQue", map);
				try {
					JSONObject jo=new JSONObject(result);
					if(jo.getBoolean("result")){
						msg.what=1;
						msg.obj=jo.getString("data");
					}else{
						msg.obj=jo.getString("message");
					}
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					msg.obj=e.getMessage();
				}
				
				handler.sendMessage(msg);
				
			};
		}.start();
	}
	
}
