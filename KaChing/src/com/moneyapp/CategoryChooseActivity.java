package com.moneyapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentTabHost;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.ImageButton;
import android.widget.PopupMenu;
import android.widget.PopupMenu.OnMenuItemClickListener;

import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.kaching.R;

public class CategoryChooseActivity extends SherlockFragmentActivity {

	private static final String TAB_1_TAG = "tab_1";
	private static final String TAB_2_TAG = "tab_2";
	
	private FragmentTabHost mTabHost;
	View rootView;
	ImageButton buttonAdd;	
	
	@Override
	public void onCreate(Bundle bundle) {
		super.onCreate(bundle);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.transaction_categories);

		rootView = (View)findViewById(R.id.transactionCategories);
		
        mTabHost = (FragmentTabHost)rootView.findViewById(android.R.id.tabhost);
        mTabHost.setup(this, getSupportFragmentManager(), R.id.realtabcontent);

        mTabHost.addTab(mTabHost.newTabSpec(TAB_1_TAG).setIndicator("INCOME"), CategoryChooseTab1.class, null);
        mTabHost.addTab(mTabHost.newTabSpec(TAB_2_TAG).setIndicator("EXPENSE"), CategoryChooseTab2.class, null);
        
        addListenerOnButtonNew();	
        
        initView();
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
	
	private void addListenerOnButtonNew() {
		buttonAdd = (ImageButton) findViewById(R.id.buttonAdd);
		
		buttonAdd.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				
				PopupMenu popupMenu = new PopupMenu(getApplicationContext(),arg0);
				
				popupMenu.getMenuInflater().inflate(R.menu.categories_choose_new,popupMenu.getMenu());
				popupMenu.setOnMenuItemClickListener(new OnMenuItemClickListener() {
						
					@Override
					public boolean onMenuItemClick(MenuItem arg0) {
						if (arg0.getTitle().equals(getResources().getString(R.string.cnt_menu_cat_new))) {
				    		Intent i = new Intent(getApplicationContext(), CategoryAddActivity.class);
				    		startActivity(i);
				        	return true;
						} else if (arg0.getTitle().equals(getResources().getString(R.string.cnt_menu_subcat_new))) {
				    		Intent i = new Intent(getApplicationContext(), CategoryChooseSubcatAddActivity.class);
				    		startActivity(i);
				        	return true;	
						} 
	
						return false;
					}
				});
				popupMenu.show();
			}
		});
	}	
	
	private void initView() {
		
	}
	
    public FragmentTabHost getTabHost() {
    	return mTabHost;
    }
}
