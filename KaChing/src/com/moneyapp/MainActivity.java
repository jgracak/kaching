package com.moneyapp;

import java.util.ArrayList;
import java.util.List;

import com.kaching.R;
import com.moneyapp.database.*;

import android.app.Activity;
import android.os.Bundle;
import android.app.Fragment;
import android.content.res.Configuration;
import android.content.res.TypedArray;
import android.support.v4.app.ActionBarDrawerToggle;
import android.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.text.format.Time;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

public class MainActivity extends Activity {

	private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;
    private ActionBarDrawerToggle mDrawerToggle;
    
    // nav drawer title
    private CharSequence mDrawerTitle;
 
    // used to store app title
    private CharSequence mTitle;
 
    // slide menu items
    private String[] navMenuTitles;
    private TypedArray navMenuIcons;
 
    private ArrayList<NavDrawerItem> navDrawerItems;
    private NavDrawerListAdapter adapter;
    
	List<Fragment> fragList = new ArrayList<Fragment>();
    private static final String STATE_SELECTED_NAVIGATION_ITEM = "selected_navigation_item";

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		/*
		// Set up the action bar to show tabs.
		final ActionBar actionBar = getActionBar();
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

		// For each of the sections in the app, add a tab to the action bar.
		actionBar.addTab(actionBar.newTab().setIcon(R.drawable.transactions_light)//setText(R.string.title_section1)
				.setTabListener(this));
		actionBar.addTab(actionBar.newTab().setIcon(R.drawable.accounts_light)//setText(R.string.title_section2)
				.setTabListener(this));
		actionBar.addTab(actionBar.newTab().setIcon(R.drawable.budgets_light)//setText(R.string.title_section3)
				.setTabListener(this));
		actionBar.addTab(actionBar.newTab().setIcon(R.drawable.more_light)//.setText(R.string.title_section4)
				.setTabListener(this));
		*/
		//TEST
		// Method used for testing
        insertDummyData();
        
        mTitle = mDrawerTitle = getTitle();
        
        // load slide menu items
        navMenuTitles = getResources().getStringArray(R.array.nav_drawer_items);
 
        // nav drawer icons from resources
        navMenuIcons = getResources()
                .obtainTypedArray(R.array.nav_drawer_icons);
 
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerList = (ListView) findViewById(R.id.list_slidermenu);
 
        navDrawerItems = new ArrayList<NavDrawerItem>();
 
        // adding nav drawer items to array
        // Home
        navDrawerItems.add(new NavDrawerItem(navMenuTitles[0], navMenuIcons.getResourceId(0, -1)));
        // Find People
        navDrawerItems.add(new NavDrawerItem(navMenuTitles[1], navMenuIcons.getResourceId(1, -1)));
        // Photos
        navDrawerItems.add(new NavDrawerItem(navMenuTitles[2], navMenuIcons.getResourceId(2, -1)));
        // Communities, Will add a counter here
        navDrawerItems.add(new NavDrawerItem(navMenuTitles[3], navMenuIcons.getResourceId(3, -1), true, "22"));
        // Pages
        navDrawerItems.add(new NavDrawerItem(navMenuTitles[4], navMenuIcons.getResourceId(4, -1)));
        // What's hot, We  will add a counter here
        navDrawerItems.add(new NavDrawerItem(navMenuTitles[5], navMenuIcons.getResourceId(5, -1), true, "50+"));
         
 
        // Recycle the typed array
        navMenuIcons.recycle();
 
        mDrawerList.setOnItemClickListener(new SlideMenuClickListener());
        
        // setting the nav drawer list adapter
        adapter = new NavDrawerListAdapter(getApplicationContext(),
                navDrawerItems);
        mDrawerList.setAdapter(adapter);
 
        // enabling action bar app icon and behaving it as toggle button
        getActionBar().setDisplayHomeAsUpEnabled(true);
        getActionBar().setHomeButtonEnabled(true);
 
        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout,
                R.drawable.ic_drawer, //nav menu toggle icon
                R.string.app_name, // nav drawer open - description for accessibility
                R.string.app_name // nav drawer close - description for accessibility
        ){
            public void onDrawerClosed(View view) {
                getActionBar().setTitle(mTitle);
                // calling onPrepareOptionsMenu() to show action bar icons
                invalidateOptionsMenu();
            }
 
            public void onDrawerOpened(View drawerView) {
                getActionBar().setTitle(mDrawerTitle);
                // calling onPrepareOptionsMenu() to hide action bar icons
                invalidateOptionsMenu();
            }
        };
        mDrawerLayout.setDrawerListener(mDrawerToggle);
 
        if (savedInstanceState == null) {
            // on first time display view for first nav item
            displayView(0);
        }
	}
		 
    /**
     * Slide menu item click listener
     * */
    private class SlideMenuClickListener implements
            ListView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position,
                long id) {
            // display view for selected nav drawer item
            displayView(position);
        }
    }
 
     /**
     * Diplaying fragment view for selected nav drawer list item
     * */
    private void displayView(int position) {
        // update the main content by replacing fragments
        Fragment fragment = null;
        switch (position) {
        case 0:
            fragment = new HomeFragment();
            break;
        case 1:
            //fragment = new FindPeopleFragment();
            break;
        case 2:
            //fragment = new PhotosFragment();
            break;
        case 3:
            //fragment = new CommunityFragment();
            break;
        case 4:
            //fragment = new PagesFragment();
            break;
        case 5:
            //fragment = new WhatsHotFragment();
            break;
 
        default:
            break;
        }
 
        if (fragment != null) {
            FragmentManager fragmentManager = getFragmentManager();
            fragmentManager.beginTransaction()
                    .replace(R.id.frame_container, fragment).commit();
 
            // update selected item and title, then close the drawer
            mDrawerList.setItemChecked(position, true);
            mDrawerList.setSelection(position);
            setTitle(navMenuTitles[position]);
            mDrawerLayout.closeDrawer(mDrawerList);
        } else {
            // error in creating fragment
            Log.e("MainActivity", "Error in creating fragment");
        }
    }

	public void insertDummyData(){
		MoneyAppDatabaseHelper db = MoneyAppDatabaseHelper.getInstance(this);
		
		/*
		if (db.getAccountsCount() > 0) {
			return;
		}
		*/
		
		db.onUpgrade(null, 1, 1);
		
		// Inserting Accounts
		Log.d("Insert: ", "Inserting .."); 
        db.createAccount(new Account("Test",1,1,123,1,1));        
        db.createAccount(new Account("Test2",2,1,155,1,1));   
        db.createAccount(new Account("Test3",3,1,167,1,1));   
        db.createAccount(new Account("Test4",1,1,-178,1,1));   
		
        // Reading all accounts
        Log.d("Reading: ", "Reading all accounts.."); 
        List<Account> accounts = db.getAllAccounts();       
         
        for (Account account : accounts) {
            String log = "Id: "+account.getId()+"| Description: " + account.getDescription() + "| Type: " + account.getType() +
            		"| Book ID: " + account.getBookId() + "| Starting balance: " + account.getStartingBalance() +
            		"| Exclude from balance: " + account.getExcludeFromBalance() + "| Exclude from reports: " +
            		account.getExcludeFromReports();
                // Writing Accounts to log
            
            Log.d("Name: ", log);
        }
        
        // Inserting Transactions
		Log.d("Insert: ", "Inserting .."); 
		Time time = new Time();
		time.setToNow();
		
        db.createTransaction(new Transaction(time,1,120,"test1",1));        
        db.createTransaction(new Transaction(time,1,50,"test2",1));     
        db.createTransaction(new Transaction(time,1,1,"test3",1));     
        db.createTransaction(new Transaction(time,1,2,"test4",1));             
        
        // Reading all transaction
        Log.d("Reading: ", "Reading all transactions.."); 
        List<Transaction> transactions = db.getAllTransactions();       
         
        for (Transaction transaction : transactions) {
            String log = "Id: " + transaction.getId()+
            		      "| transDate: " + transaction.getTransDate().toString() + 
            		      "| idCategory: " + transaction.getIdCategory() +
            		      "| amount: " + transaction.getAmount() + 
            		      "| note: " + transaction.getNote() +
            		      "| idAccount: " + transaction.getIdAccount();
                // Writing Transactions to log
            
            Log.d("Name: ", log);
        }
	}
	
    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {
        if (savedInstanceState.containsKey(STATE_SELECTED_NAVIGATION_ITEM)) {
            getActionBar().setSelectedNavigationItem(savedInstanceState.getInt(STATE_SELECTED_NAVIGATION_ITEM));
        }
    }
    
    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putInt(STATE_SELECTED_NAVIGATION_ITEM, getActionBar().getSelectedNavigationIndex());
    }
    
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
	}
	
    /**
     * A dummy fragment representing a section of the app, but that simply displays dummy text.
     */
    public static class DummySectionFragment extends Fragment {
        public DummySectionFragment() {
        }

        public static final String ARG_SECTION_NUMBER = "section_number";

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                Bundle savedInstanceState) {
            TextView textView = new TextView(getActivity());
            textView.setGravity(Gravity.CENTER);
            Bundle args = getArguments();
            textView.setText(Integer.toString(args.getInt(ARG_SECTION_NUMBER)));
            return textView;
        }
    }
 
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // toggle nav drawer on selecting action bar app icon/title
        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        
        // Handle action bar actions click
        switch (item.getItemId()) {
        case R.id.menu_settings:
            return true;
        default:
            return super.onOptionsItemSelected(item);
        }
    }
    
    /***
     * Called when invalidateOptionsMenu() is triggered
     */
    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        // if nav drawer is opened, hide the action items
        boolean drawerOpen = mDrawerLayout.isDrawerOpen(mDrawerList);
        menu.findItem(R.id.menu_settings).setVisible(!drawerOpen);
        return super.onPrepareOptionsMenu(menu);
    }
 
    @Override
    public void setTitle(CharSequence title) {
        mTitle = title;
        getActionBar().setTitle(mTitle);
    }
 
    /**
     * When using the ActionBarDrawerToggle, you must call it during
     * onPostCreate() and onConfigurationChanged()...
     */
 
    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        mDrawerToggle.syncState();
    }
 
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        // Pass any configuration change to the drawer toggls
        mDrawerToggle.onConfigurationChanged(newConfig);
    }
}
