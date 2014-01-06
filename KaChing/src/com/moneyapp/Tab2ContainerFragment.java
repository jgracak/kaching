package com.moneyapp;

import com.kaching.R;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class Tab2ContainerFragment extends BaseContainerFragment {
	
	private boolean mIsViewInited;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		Log.e("test", "tab 2 oncreateview");
		return inflater.inflate(R.layout.container_fragment, null);
	}
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		Log.e("test", "tab 2 container on activity created");
		if (!mIsViewInited) {
			mIsViewInited = true;
			initView();
		}
	}
	
	private void initView() {
		Log.e("test", "tab 2 init view");
		replaceFragment(new Tab2Fragment(), false);
	}

}
