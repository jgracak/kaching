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
		getActionBar().setTitle(listItems[0].toString());
		
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
		
		//
		// set the Behind View
		setBehindContentView(R.layout.menu_frame);
		getSupportFragmentManager()
		.beginTransaction()
		.replace(R.id.menu_frame, new SlidingMenuListFragment())
		.commit();
		
		// customize the SlidingMenu
		getSlidingMenu().setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
		
		//TEST
		// Method used for testing
        insertDummyData();
        
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
    
	public void insertDummyData(){
		MoneyAppDatabaseHelper db = MoneyAppDatabaseHelper.getInstance(this);
		
		db.onUpgrade(db.getWritableDatabase(), 1, 1);
		
		// Inserting Accounts
		Log.d("Insert: ", "Inserting .."); 
        db.createAccount(new TableAccount("Account1",1,1,123,1,1));        
        db.createAccount(new TableAccount("Account2",2,1,155,1,1));   
        db.createAccount(new TableAccount("Account3",3,1,167,1,1));   
        db.createAccount(new TableAccount("Account4",1,1,-178,1,1));   
		
        // Reading all accounts
        Log.d("Reading: ", "Reading all accounts.."); 
        List<TableAccount> accounts = db.getAllAccounts();       
         
        for (TableAccount account : accounts) {
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
		
        db.createTransaction(new TableTransaction(time,1,1,"test1",1));     
        time.month += 1;       
        db.createTransaction(new TableTransaction(time,1,2,"test2",1));  
        time.month -= 5;
        db.createTransaction(new TableTransaction(time,2,3,"test3",3));    
        time.monthDay += 2;
        db.createTransaction(new TableTransaction(time,3,-4,"test4",1));             
        time.monthDay += 5;
        db.createTransaction(new TableTransaction(time,4,5,null,1));  
        time.monthDay += 3;
        db.createTransaction(new TableTransaction(time,5,6,null,1));  
        time.monthDay += 1;
        db.createTransaction(new TableTransaction(time,6,-7,null,1));  
        time.monthDay += 3;
        db.createTransaction(new TableTransaction(time,6,8,null,2));  
        time.monthDay += 1;
        db.createTransaction(new TableTransaction(time,7,-9,null,3));  
        time.monthDay += 1;
        db.createTransaction(new TableTransaction(time,7,10,null,1));  
        time.monthDay += 1;
        db.createTransaction(new TableTransaction(time,8,-11,null,3));  
        time.monthDay += 1;
        db.createTransaction(new TableTransaction(time,9,12,null,1));  
        time.monthDay += 1;
        db.createTransaction(new TableTransaction(time,9,13,null,3));  
        time.monthDay += 1;
        db.createTransaction(new TableTransaction(time,9,-14,null,3));          
        
        // Reading all transaction
        Log.d("Reading: ", "Reading all transactions.."); 
        List<TableTransaction> transactions = db.getAllTransactions();       
         
        for (TableTransaction transaction : transactions) {
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
}
