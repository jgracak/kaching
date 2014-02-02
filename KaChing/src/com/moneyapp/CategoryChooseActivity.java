package com.moneyapp;

import android.os.Bundle;
import android.support.v4.app.FragmentTabHost;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.kaching.R;

public class CategoryChooseActivity extends SherlockFragmentActivity {

	private static final String TAB_1_TAG = "tab_1";
	private static final String TAB_2_TAG = "tab_2";
	
	private FragmentTabHost mTabHost;
	View rootView;
	
	@Override
	public void onCreate(Bundle bundle) {
		super.onCreate(bundle);
		setContentView(R.layout.transaction_categories);

		rootView = (View)findViewById(R.id.transactionCategories);
		
        
        mTabHost = (FragmentTabHost)rootView.findViewById(android.R.id.tabhost);
        mTabHost.setup(this, getSupportFragmentManager(), R.id.realtabcontent);

        mTabHost.addTab(mTabHost.newTabSpec(TAB_1_TAG).setIndicator("INCOME"), Tab1ContainerFragment.class, null);
        mTabHost.addTab(mTabHost.newTabSpec(TAB_2_TAG).setIndicator("EXPENSE"), Tab2ContainerFragment.class, null);
        
        //TODO
        // Dodati gore desno + za dodavanje kategorija
    }
	
    public FragmentTabHost getTabHost() {
    	return mTabHost;
    }
}
