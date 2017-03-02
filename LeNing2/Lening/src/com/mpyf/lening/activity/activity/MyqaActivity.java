package com.mpyf.lening.activity.activity;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.mpyf.lening.R;
import com.mpyf.lening.Jutil.ListenerServer;
import com.mpyf.lening.Jutil.Xiaoxibeijing;
import com.mpyf.lening.activity.adapter.Vpadapter;
import com.mpyf.lening.activity.fragment.Fragment_myqaa;
import com.mpyf.lening.activity.fragment.Fragment_myqaq;

public class MyqaActivity extends FragmentActivity {

	private LinearLayout ll_mycourse_back;
	private RelativeLayout rl_mycourse_zixiu, rl_mycourse_banji,
			rl_mycourse_gangwei;
	private TextView tv_mycourse_title, tv_mycourse_zixiu, tv_mycourse_banji;

	private ImageView iv_mycourse_zixiu, iv_mycourse_banji;

	private ViewPager vp_mycourse_top;
	private List<Fragment> list;

	private Fragment_myqaq fragment_myqaq;
	private Fragment_myqaa fragment_myqaa;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		// new Xiaoxibeijing().changetop(this, R.color.main);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_mycourse);
		init();
		showdata();
		addlistener();
	}

	private void init() {
		ll_mycourse_back = (LinearLayout) findViewById(R.id.ll_mycourse_back);
		rl_mycourse_zixiu = (RelativeLayout) findViewById(R.id.rl_mycourse_zixiu);
		rl_mycourse_banji = (RelativeLayout) findViewById(R.id.rl_mycourse_banji);
		rl_mycourse_gangwei = (RelativeLayout) findViewById(R.id.rl_mycourse_gangwei);
		tv_mycourse_title = (TextView) findViewById(R.id.tv_mycourse_title);
		tv_mycourse_zixiu = (TextView) findViewById(R.id.tv_mycourse_zixiu);
		tv_mycourse_banji = (TextView) findViewById(R.id.tv_mycourse_banji);
		iv_mycourse_zixiu = (ImageView) findViewById(R.id.iv_mycourse_zixiu);
		iv_mycourse_banji = (ImageView) findViewById(R.id.iv_mycourse_banji);
		vp_mycourse_top = (ViewPager) findViewById(R.id.vp_mycourse_top);

		rl_mycourse_gangwei.setVisibility(View.GONE);
	}

	private void showdata() {

		tv_mycourse_title.setText("我的问答");
		tv_mycourse_zixiu.setText("我的提问");
		tv_mycourse_banji.setText("我的回复");

		list = new ArrayList<Fragment>();

		fragment_myqaq = new Fragment_myqaq();
		fragment_myqaa = new Fragment_myqaa();

		list.add(fragment_myqaq);
		list.add(fragment_myqaa);

		Vpadapter adapter = new Vpadapter(getSupportFragmentManager(), list);
		vp_mycourse_top.setAdapter(adapter);

		if (getIntent().getIntExtra("fragid", 0) == 0) {
			clearall(0);
			vp_mycourse_top.setCurrentItem(0);
		} else if (getIntent().getIntExtra("fragid", 0) == 1) {
			clearall(1);
			vp_mycourse_top.setCurrentItem(1);
		}

	}

	private void clearall(int index) {
		TextView[] texttop = { tv_mycourse_zixiu, tv_mycourse_banji };
		ImageView[] imagetop = { iv_mycourse_zixiu, iv_mycourse_banji };

		for (int i = 0; i < texttop.length; i++) {
			if (i == index) {
				texttop[i].setTextColor(getResources().getColor(R.color.my));
				imagetop[i].setBackgroundColor(getResources().getColor(
						R.color.my));
			} else {
				texttop[i].setTextColor(getResources().getColor(R.color.ciyao));
				imagetop[i].setBackgroundColor(getResources().getColor(
						R.color.ciyao));
			}
		}
	}

	private void addlistener() {
		ListenerServer.setfinish(MyqaActivity.this, ll_mycourse_back);

		rl_mycourse_zixiu.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				clearall(0);
				vp_mycourse_top.setCurrentItem(0);
			}
		});
		rl_mycourse_banji.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				clearall(1);
				vp_mycourse_top.setCurrentItem(1);
			}
		});

		vp_mycourse_top.setOnPageChangeListener(new OnPageChangeListener() {

			@Override
			public void onPageSelected(int arg0) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onPageScrollStateChanged(int arg0) {
				// TODO Auto-generated method stub
				if (arg0 == 2) {
					clearall(vp_mycourse_top.getCurrentItem());
				}
			}
		});
	}

}
