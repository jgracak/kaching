package com.moneyapp;

import com.kaching.R;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;


// DELETE THIS CLASS
public class Tab1AddOnFragment extends Fragment {
	
	private static int sTab1AddOnCount = 0;
	
	private int mCurrentAddOnCount = 0;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		sTab1AddOnCount++;
		mCurrentAddOnCount = sTab1AddOnCount;
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		Log.e("test", "tab 1 add on oncreateview");
		return inflater.inflate(R.layout.tab_fragment, null);
	}
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		Log.e("test", "tab 1 add on onActivityCreated");
		initView();
	}
	
	@Override
	public void onDestroy() {
		super.onDestroy();
		sTab1AddOnCount--;
	}
	
	private void initView() {
		/*
		TextView textView = (TextView) getView().findViewById(R.id.tab_text);
		textView.setText("Tab 1 AddOn: " + mCurrentAddOnCount);
		Button button = (Button) getView().findViewById(R.id.tab_btn);
		button.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				replaceFragment();
			}
		});
		*/
	}
	
	private void replaceFragment() {
		((BaseContainerFragment)getParentFragment()).replaceFragment(new Tab1AddOnFragment(), true);
	}
	
}
