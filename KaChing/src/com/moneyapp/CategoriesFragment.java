package com.moneyapp;

import com.kaching.R;

import android.support.v4.app.FragmentTabHost;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.actionbarsherlock.app.SherlockFragment;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;

public class CategoriesFragment extends SherlockFragment {

	private static final String TAB_1_TAG = "tab_1";
	private static final String TAB_2_TAG = "tab_2";
	
	private FragmentTabHost mTabHost;
	View rootView;
	
	public CategoriesFragment(){}
    
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
  
        rootView = inflater.inflate(R.layout.fragment_categories, container, false);

        mTabHost = (FragmentTabHost)rootView.findViewById(android.R.id.tabhost);
        mTabHost.setup(getActivity(), getChildFragmentManager(), R.id.realtabcontent);

        mTabHost.addTab(mTabHost.newTabSpec(TAB_1_TAG).setIndicator("INCOME"), Tab1ContainerFragment.class, null);
        mTabHost.addTab(mTabHost.newTabSpec(TAB_2_TAG).setIndicator("EXPENSE"), Tab2ContainerFragment.class, null);
        
        setHasOptionsMenu(true);
        
        return rootView;
    }
	
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
    	getSherlockActivity().getSupportMenuInflater().inflate(R.menu.activity_category, menu);
    }
    
	@Override	
	public void onResume()
	{
	   super.onResume();
	   
	   // a very lazy refresh
	   if (mTabHost.getCurrentTab() == 0) {
		   mTabHost.setCurrentTab(1);
		   mTabHost.setCurrentTab(0);
	   } else {
		   mTabHost.setCurrentTab(0);
		   mTabHost.setCurrentTab(1);
	   }
	}
	
	
	@Override
	public void onCreate(Bundle savedInstanceState) {		
		super.onCreate(savedInstanceState);
	}
	
    @Override    
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar actions click
        switch (item.getItemId()) {
        case R.id.action_add:
    		Intent i = new Intent(getActivity().getBaseContext().getApplicationContext(), CategoryAddActivity.class);
    		i.putExtra("current_tab",mTabHost.getCurrentTab());
    		startActivity(i);
        	return true;
        default:
            return super.onOptionsItemSelected(item);
        }
    }
    
    public FragmentTabHost getTabHost() {
    	return mTabHost;
    }
}