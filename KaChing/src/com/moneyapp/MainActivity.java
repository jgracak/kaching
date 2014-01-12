package com.moneyapp;

import java.util.ArrayList;
import java.util.List;

import com.kaching.R;
import com.moneyapp.database.*;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.format.Time;
import android.util.Log;

import com.moneyapp.slidingmenu.ActivityBase;
import com.moneyapp.slidingmenu.SlidingMenuInitialiser;
import com.moneyapp.slidingmenu.SlidingMenuListFragmentConcrete;

public class MainActivity extends ActivityBase {
	
    // used to store app title
    private CharSequence mTitle;
    
	List<Fragment> fragList = new ArrayList<Fragment>();
    
    @Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
        
		// These two lines of code adds left side sliding out menu to your
		// activity. First we create new SlidingMenuInitializer instance and
		// pass our activity as a constructor parameter.
		slidingMenuInitialiser = new SlidingMenuInitialiser(this);
		// Secondly we call a method which creates sliding menu from our given
		// XML file and a fragment to be used as content for our menu.
		slidingMenuInitialiser.createSlidingMenu(
				SlidingMenuListFragmentConcrete.class,
				R.raw.sliding_menu_list_items);
		
		//TEST
		// Method used for testing
        insertDummyData();
        
        // Show the menu od startup
        slidingMenuInitialiser.getSlidingMenu().toggle();
	}
    
    @Override
	public boolean enableHomeIconActionSlidingMenu() {
		return true;
	}
    
	public void insertDummyData(){
		MoneyAppDatabaseHelper db = MoneyAppDatabaseHelper.getInstance(this);
		
		db.onUpgrade(db.getWritableDatabase(), 1, 1);
		
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
		
        db.createTransaction(new Transaction(time,1,-120,"test1",1));     
        time.month += 1;       
        db.createTransaction(new Transaction(time,2,-50,"test2",2));  
        time.month -= 5;
        db.createTransaction(new Transaction(time,3,-1,"test3",3));    
        time.monthDay += 2;
        db.createTransaction(new Transaction(time,4,-2,"test4",4));             
        time.monthDay += 5;
        db.createTransaction(new Transaction(time,5,-155,null,1));  
        time.monthDay += 3;
        db.createTransaction(new Transaction(time,6,-76,null,2));  
        time.monthDay += 1;
        db.createTransaction(new Transaction(time,7,-23,null,3));  
        time.monthDay += 3;
        db.createTransaction(new Transaction(time,8,-76,null,2));  
        time.monthDay += 1;
        db.createTransaction(new Transaction(time,9,2000,null,3));  
        
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
    public void setTitle(CharSequence title) {
        mTitle = title;
        getActionBar().setTitle(mTitle);
    }
}
