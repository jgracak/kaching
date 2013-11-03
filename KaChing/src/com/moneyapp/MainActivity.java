package com.moneyapp;

import java.util.ArrayList;
import java.util.List;

import com.kaching.R;
import com.moneyapp.database.*;

import android.app.ActionBar;
import android.app.Activity;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.app.Fragment;
import android.app.ActionBar.Tab;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class MainActivity extends Activity implements
		ActionBar.TabListener {

	List<Fragment> fragList = new ArrayList<Fragment>();

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//setContentView(R.layout.activity_main);

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
		
        /**
         * CRUD Operations
         * */		
        MoneyAppDatabaseHelper db = new MoneyAppDatabaseHelper(this);
        
        // Inserting Accounts
        /*
        Log.d("Insert: ", "Inserting .."); 
        db.createAccount(new Account("Test",1,123,1,1));        
        db.createAccount(new Account("Test2",1,155,1,1));   
        db.createAccount(new Account("Test3",1,167,1,1));   
        db.createAccount(new Account("Test4",1,178,1,1));   
        */
        
        // Reading all accounts
        Log.d("Reading: ", "Reading all accounts.."); 
        List<Account> accounts = db.getAllAccounts();       
         
        for (Account account : accounts) {
            String log = "Id: "+account.getId()+" ,Description: " + account.getDescription() + 
            		" ,Book ID: " + account.getBookId() + " ,Starting balance: " + account.getStartingBalance() +
            		" ,Exclude from balance: " + account.getExcludeFromBalance() + " ,Exclude from reports: " +
            		account.getExcludeFromReports();
                // Writing Accounts to log
            
            Log.d("Name: ", log);
        }
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
	}

	@Override
	public void onTabSelected(ActionBar.Tab tab,
			FragmentTransaction fragmentTransaction) {
		Fragment f = null;
		TabAccountFragment tf = null;
		
		if (fragList.size() > tab.getPosition())
				fragList.get(tab.getPosition());
		
		tf = new TabAccountFragment();
		Bundle data = new Bundle();
		data.putInt("idx",  tab.getPosition());
		tf.setArguments(data);
		fragList.add(tf);
		
		fragmentTransaction.replace(android.R.id.content, tf);
	}

	@Override
	public void onTabUnselected(ActionBar.Tab tab,
			FragmentTransaction fragmentTransaction) {
		if (fragList.size() > tab.getPosition()) {
			fragmentTransaction.remove(fragList.get(tab.getPosition()));;
		}
	}

	@Override
	public void onTabReselected(ActionBar.Tab tab,
			FragmentTransaction fragmentTransaction) {
	}
}
