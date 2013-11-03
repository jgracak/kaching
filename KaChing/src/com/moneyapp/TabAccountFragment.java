package com.moneyapp;

import com.kaching.R;

import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class TabAccountFragment extends Fragment{

	private int index;

	@Override
	public void onCreate(Bundle savedInstanceState) {		
		super.onCreate(savedInstanceState);
		Bundle data = getArguments();
		index = data.getInt("idx");
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		
		View v = inflater.inflate(R.layout.account_fragment, null);
		TextView tv = (TextView) v.findViewById(R.id.msg);
		tv.setText("Fragment " + (index + 1));
		
		return v;
		
	}
}