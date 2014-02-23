package com.moneyapp;

import java.util.ArrayList;
import java.util.List;

import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.kaching.R;
import com.moneyapp.database.*;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.format.Time;
import android.util.Log;

import com.moneyapp.slidingmenu.BaseActivity;
import com.moneyapp.slidingmenu.SlidingMenuListFragment;

public class MainActivity extends BaseActivity {
	
	private Fragment mContent;
	
	List<Fragment> fragList = new ArrayList<Fragment>();
    
	public MainActivity() {
		super(R.string.app_name);
	}
	
    @Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		// Set title and icon for the selected fragment
		String[] listItems = getResources().getStringArray(R.array.nav_drawer_items);
		String[] listItemsIcons = getResources().getStringArray(R.array.nav_drawer_icons);
		int imageResource = getResources().getIdentifier(listItemsIcons[1].toString(), null, getPackageName());
		getActionBar().setIcon(imageResource);
		getActionBar().setTitle(listItems[1].toString());
		
		// Here we set the initial fragment
		// set the Above View
		if (savedInstanceState != null)
			mContent = getSupportFragmentManager().getFragment(savedInstanceState, "mContent");
		if (mContent == null)
			mContent = new TransactionsFragment();
		setContentView(R.layout.content_frame);
		getSupportFragmentManager()
		.beginTransaction()
		.replace(R.id.content_frame, mContent)
		.commit();
		
		MoneyAppDatabaseHelper db = new MoneyAppDatabaseHelper(this);
		//Test
		db.insertDummyData();
		
		db.close();
		
		// set the Behind View
		setBehindContentView(R.layout.menu_frame);
		getSupportFragmentManager()
		.beginTransaction()
		.replace(R.id.menu_frame, new SlidingMenuListFragment())
		.commit();
		
		// customize the SlidingMenu
		getSlidingMenu().setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
        
        getSlidingMenu().setOnOpenListener(new SlidingMenu.OnOpenListener() {

            @Override
            public void onOpen() {
                getSupportActionBar().setDisplayHomeAsUpEnabled(false);
            }

        });
        
        getSlidingMenu().setOnCloseListener(new SlidingMenu.OnCloseListener() {
			
			@Override
			public void onClose() {
				getSupportActionBar().setDisplayHomeAsUpEnabled(true);
			}
		});
	}

    @Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		getSupportFragmentManager().putFragment(outState, "mContent", mContent);
	}
    
    @Override
	public void toggle() {
		if (getSlidingMenu().isShown() == true) {
			getActionBar().setDisplayHomeAsUpEnabled(false);
		} 
		super.toggle();
	}
    
    @Override    
    public void showContent() {
		if (getSlidingMenu().isShown() == false) {
			getActionBar().setDisplayHomeAsUpEnabled(true);
		} 
		super.showContent();
	}
    
    @Override      
	public void showMenu() {
    	super.showMenu();
	}
	
	public void switchContent(Fragment fragment) {
		mContent = fragment;
		getSupportFragmentManager()
		.beginTransaction()
		.replace(R.id.content_frame, fragment)
		.commit();
		getSlidingMenu().showContent();
		
	}
}
