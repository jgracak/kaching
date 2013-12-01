package com.moneyapp.database;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.text.format.Time;
import android.util.Log;

public class MoneyAppDatabaseHelper extends SQLiteOpenHelper { 
	
	private static MoneyAppDatabaseHelper sInstance = null;
	
	// Logcat tag
    private static final String LOG = "MoneyAppDatabaseHelper";
 
    // Database Version
    private static final int DATABASE_VERSION = 1;
 
    // Database Name
    private static final String DATABASE_NAME = "MoneyApp";
 
    // Table Names
    private static final String TABLE_ACCOUNTS = "Accounts";
    private static final String TABLE_TRANSACTIONS = "Transactions";
 
    // Common column names
    private static final String COLUMN_ID = "_id";
 
    // ACCOUNTS Table - column names
    public static final String COLUMN_DESCRIPTION = "description";
    public static final String COLUMN_TYPE = "type";    
    public static final String COLUMN_BOOKID = "bookid";
    public static final String COLUMN_STARTINGBALANCE = "startingbalance";
    public static final String COLUMN_INCLUDEINBALANCE = "excludefrombalance";
    public static final String COLUMN_INCLUDEINREPORTS = "excludefromreports";	
    
    // TRANSACTIONS Table - column names
    public static final String COLUMN_TRANSDATE = "transDate";
    public static final String COLUMN_IDCATEGORY  = "IdCategory";
    public static final String COLUMN_AMOUNT = "amount";
    public static final String COLUMN_NOTE = "note";
    public static final String COLUMN_IDACCOUNT = "idAccount";
 
    // Table Create Statements
    // Account table create statement
    private static final String CREATE_TABLE_ACCOUNT = "create table " 
    		+ TABLE_ACCOUNTS
	        + "(" 
	        + COLUMN_ID + " integer primary key autoincrement, " 
	        + COLUMN_DESCRIPTION + " text not null," 
	        + COLUMN_TYPE + " integer not null,"
	        + COLUMN_BOOKID + " integer not null," 
	        + COLUMN_STARTINGBALANCE + " real not null,"
	        + COLUMN_INCLUDEINBALANCE + " boolean not null,"
	        + COLUMN_INCLUDEINREPORTS + " boolean not null"
	        + ");";
 
    // Transaction table create statement
    private static final String CREATE_TABLE_TRANSACTIONS = "create table " 
    		+ TABLE_TRANSACTIONS
	        + "(" 
	        + COLUMN_ID + " integer primary key autoincrement, " 
	        + COLUMN_TRANSDATE + " integer not null," 
	        + COLUMN_IDCATEGORY + " integer not null,"
	        + COLUMN_AMOUNT + " real not null," 
	        + COLUMN_NOTE + " text not null,"
	        + COLUMN_IDACCOUNT + " integer not null"
	        + ");";
    
    public static MoneyAppDatabaseHelper getInstance(Context context) {
        
        // Use the application context, which will ensure that you 
        // don't accidentally leak an Activity's context.
        // See this article for more information: http://bit.ly/6LRzfx
        if (sInstance == null) {
          sInstance = new MoneyAppDatabaseHelper(context.getApplicationContext());
        }
        return sInstance;
      }
         
	public MoneyAppDatabaseHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	public MoneyAppDatabaseHelper() {
		super(null, DATABASE_NAME, null, DATABASE_VERSION);
	}
	
	// Method is called during creation of the database
    @Override
    public void onCreate(SQLiteDatabase db) {
    	
        // creating required tables
        db.execSQL(CREATE_TABLE_ACCOUNT);
        db.execSQL(CREATE_TABLE_TRANSACTIONS);
    }
 
	// Method is called during an upgrade of the database,
	// e.g. if you increase the database version
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Place code for upgrading tables here eg. new columns or droping tables 
    	//on upgrade drop older tables
    	if (db == null) {
    		db = this.getWritableDatabase();
    	}
    	
        Log.w(LOG," Upgrading database from version " + oldVersion + " to " + newVersion
                + ", which will destroy all old data");
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ACCOUNTS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_TRANSACTIONS);
        
        // create new tables
        onCreate(db);
    }
	
    // Account table methods
	//Creating an account
	public long createAccount(Account account) {
	    SQLiteDatabase db = this.getWritableDatabase();
	 
	    ContentValues values = new ContentValues();
	    values.put(COLUMN_DESCRIPTION, account.getDescription());
	    values.put(COLUMN_TYPE, account.getType()); 
	    values.put(COLUMN_BOOKID, account.getBookId());
	    values.put(COLUMN_STARTINGBALANCE, account.getStartingBalance());
	    values.put(COLUMN_INCLUDEINBALANCE, account.getExcludeFromBalance());
	    values.put(COLUMN_INCLUDEINREPORTS, account.getExcludeFromReports());
	 
	    // insert row
	    long account_id = db.insert(TABLE_ACCOUNTS, null, values);
	    db.close(); // Closing database connection
	    
	    return account_id;
	}
	
	// Getting a single account based on id
	public Account getAccount(int id) {
		SQLiteDatabase db = this.getReadableDatabase();
	 
	    Cursor cursor = db.query(TABLE_ACCOUNTS, new String[] { COLUMN_ID,
	            COLUMN_DESCRIPTION, COLUMN_TYPE, COLUMN_BOOKID, COLUMN_STARTINGBALANCE,
	            COLUMN_INCLUDEINBALANCE, COLUMN_INCLUDEINREPORTS}, COLUMN_ID + "=?",
	            new String[] { String.valueOf(id) }, null, null, null, null);
	    if (cursor != null)
	        cursor.moveToFirst();
	 
	    Account account = new Account(Integer.parseInt(cursor.getString(0)),
	            cursor.getString(1), Integer.parseInt(cursor.getString(2)), Integer.parseInt(cursor.getString(3)),
	            Float.parseFloat(cursor.getString(4)), Integer.parseInt(cursor.getString(5)),
	            Integer.parseInt(cursor.getString(6)));
	    
	    // return account
	    return account;
	}
	
    // Getting all accounts
	public List<Account> getAllAccounts() {
	    List<Account> accountList = new ArrayList<Account>();
	    // Select All Query
	    String selectQuery = "SELECT  * FROM " + TABLE_ACCOUNTS;
	 
	    SQLiteDatabase db = this.getWritableDatabase();
	    Cursor cursor = db.rawQuery(selectQuery, null);
	 
	    // looping through all rows and adding to list
	    if (cursor.moveToFirst()) {
	        do {
	            Account account = new Account();
	            account.setId(Integer.parseInt(cursor.getString(0)));
	            account.setDescription(cursor.getString(1));
	            account.setType(Integer.parseInt(cursor.getString(2)));
	            account.setBookId(Integer.parseInt(cursor.getString(3)));	            
	            account.setStartingBalance(Float.parseFloat(cursor.getString(4)));
	            account.setExcludeFromBalance(Integer.parseInt(cursor.getString(5)));
	            account.setExcludeFromReports(Integer.parseInt(cursor.getString(6)));
	            
	            // Adding account to list
	            accountList.add(account);
	        } while (cursor.moveToNext());
	    }
	 
	    // return account list
	    return accountList;
	}
	
	// Getting accounts count
    public int getAccountsCount() {
        String countQuery = "SELECT  * FROM " + TABLE_ACCOUNTS;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        int count = cursor.getCount();
        cursor.close();
        // return count
        return count;
    }
    
    // Updating single account
	public int updateAccount(Account account) {
	    SQLiteDatabase db = this.getWritableDatabase();
	 
	    ContentValues values = new ContentValues();
	    values.put(COLUMN_DESCRIPTION, account.getDescription());
	    values.put(COLUMN_TYPE, account.getType());	    
	    values.put(COLUMN_BOOKID, account.getBookId());
	    values.put(COLUMN_STARTINGBALANCE, account.getStartingBalance());
	    values.put(COLUMN_INCLUDEINBALANCE, account.getExcludeFromBalance());
	    values.put(COLUMN_INCLUDEINREPORTS, account.getExcludeFromReports());
	 
	    // updating row
	    return db.update(TABLE_ACCOUNTS, values, COLUMN_ID + " = ?",
	            new String[] { String.valueOf(account.getId()) });
	}
	
    // Deleting a single account
	public void deleteAccount(Account account) {
	    SQLiteDatabase db = this.getWritableDatabase();
	    
	    db.delete(TABLE_ACCOUNTS, COLUMN_ID + " = ?",
	            new String[] { String.valueOf(account.getId()) });
	    db.close();
	}
	
	// Transaction table methods
	//Creating a transaction
	public long createTransaction(Transaction transaction) {
		SQLiteDatabase db = this.getWritableDatabase();
		
	    ContentValues values = new ContentValues();
	    
	    values.put(COLUMN_TRANSDATE, (transaction.getTransDate().toMillis(false) / 1000L));	    
	    values.put(COLUMN_IDCATEGORY, transaction.getIdCategory()); 
	    values.put(COLUMN_AMOUNT, transaction.getAmount());
	    values.put(COLUMN_NOTE, transaction.getNote());
	    values.put(COLUMN_IDACCOUNT, transaction.getIdAccount());
	 
	    // insert row
	    long transaction_id = db.insert(TABLE_TRANSACTIONS, null, values);
	    db.close(); // Closing database connection
	    
	    return transaction_id;
	}
	
	// Getting a single transaction based on id
	public Transaction getTransaction(int id) {
		SQLiteDatabase db = this.getReadableDatabase();
	 
	    Cursor cursor = db.query(TABLE_TRANSACTIONS, new String[] { COLUMN_ID,
	    		COLUMN_TRANSDATE, COLUMN_IDCATEGORY, COLUMN_AMOUNT, COLUMN_NOTE,
	    		COLUMN_IDACCOUNT}, COLUMN_ID + "=?",
	            new String[] { String.valueOf(id) }, null, null, null, null);
	    if (cursor != null)
	        cursor.moveToFirst();
	 
	    Transaction transaction = new Transaction(Integer.parseInt(cursor.getString(0)),
	    		componentTimestampToTime(Integer.parseInt(cursor.getString(1))), 
	    		Integer.parseInt(cursor.getString(2)), 
	            Float.parseFloat((cursor.getString(3))),
	            cursor.getString(4), 
	            Integer.parseInt(cursor.getString(5))
	            );
	    
	    // return transaction
	    return transaction;
	}
	
	// Getting all transactions
	public List<Transaction> getAllTransactions() {
	    List<Transaction> transactionList = new ArrayList<Transaction>();
	    // Select All Query
	    String selectQuery = "SELECT  * FROM " + TABLE_TRANSACTIONS;
	 
	    SQLiteDatabase db = this.getWritableDatabase();
	    Cursor cursor = db.rawQuery(selectQuery, null);
	 
	    // looping through all rows and adding to list
	    if (cursor.moveToFirst()) {
	        do {
	        	Transaction transaction = new Transaction();
	        	transaction.setId(Integer.parseInt(cursor.getString(0)));
	        	transaction.setTransDate(componentTimestampToTime(Integer.parseInt(cursor.getString(1))));
	        	transaction.setIdCategory(Integer.parseInt(cursor.getString(2)));
	        	transaction.setAmount(Float.parseFloat((cursor.getString(3))));	            
	            transaction.setNote(cursor.getString(4));
	            transaction.setIdAccount(Integer.parseInt(cursor.getString(5)));
	            
	            // Adding transaction to list
	            transactionList.add(transaction);
	        } while (cursor.moveToNext());
	    }
	 
	    // return transaction list
	    return transactionList;
	}	

	// Getting transactions count
    public int getTransactionsCount() {
        String countQuery = "SELECT  * FROM " + TABLE_TRANSACTIONS;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        int count = cursor.getCount();
        cursor.close();
        // return count
        return count;
    }	
	
    // Updating single transaction
	public int updateTransaction(Transaction transaction) {
	    SQLiteDatabase db = this.getWritableDatabase();
		
	    ContentValues values = new ContentValues();
	    values.put(COLUMN_TRANSDATE, (transaction.getTransDate().toMillis(false) / 1000L));	   
	    values.put(COLUMN_IDCATEGORY, transaction.getIdCategory()); 
		values.put(COLUMN_AMOUNT, transaction.getAmount());
		values.put(COLUMN_NOTE, transaction.getNote());
		values.put(COLUMN_IDACCOUNT, transaction.getIdAccount());
	 
	    // updating row
	    return db.update(TABLE_TRANSACTIONS, values, COLUMN_ID + " = ?",
	            new String[] { String.valueOf( transaction.getId()) });
	}

    // Deleting a single transaction
	public void deleteTransaction(Transaction transaction) {
	    SQLiteDatabase db = this.getWritableDatabase();
	    
	    db.delete(TABLE_TRANSACTIONS, COLUMN_ID + " = ?",
	            new String[] { String.valueOf(transaction.getId()) });
	    db.close();
	}	
	
	// Help methods
	// Convert time to timestamp
	public int componentTimeToTimestamp(int year, int month, int day, int hour, int minute) {

	    Calendar c = Calendar.getInstance();
	    c.set(Calendar.YEAR, year);
	    c.set(Calendar.MONTH, month);
	    c.set(Calendar.DAY_OF_MONTH, day);
	    c.set(Calendar.HOUR, hour);
	    c.set(Calendar.MINUTE, minute);
	    c.set(Calendar.SECOND, 0);
	    c.set(Calendar.MILLISECOND, 0);

	    return (int) (c.getTimeInMillis() / 1000L);
	}
	
	// Convert timestamp to time
	public Time componentTimestampToTime(int timestamp) {
		Calendar c = Calendar.getInstance();
		c.setTimeInMillis(timestamp * 1000);
		
		Time time = new Time();
		time.set(timestamp * 1000L);
		/*
		time.year = c.get(Calendar.YEAR);
		time.month = c.get(Calendar.MONTH);
		time.monthDay = c.get(Calendar.DAY_OF_MONTH);
		time.hour = c.get(Calendar.HOUR);
		time.minute = c.get(Calendar.MINUTE);
		time.second = c.get(Calendar.SECOND);
		*/
		return time;
	}
}
