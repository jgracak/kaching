package com.moneyapp.database;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class MoneyAppDatabaseHelper extends SQLiteOpenHelper { 
	// Logcat tag
    private static final String LOG = "MoneyAppDatabaseHelper";
 
    // Database Version
    private static final int DATABASE_VERSION = 1;
 
    // Database Name
    private static final String DATABASE_NAME = "MoneyApp";
 
    // Table Names
    private static final String TABLE_ACCOUNT = "Accounts";
 
    // Common column names
    private static final String COLUMN_ID = "_id";
 
    // ACCOUNTS Table - column names
    public static final String COLUMN_DESCRIPTION = "description";
    public static final String COLUMN_BOOKID = "bookid";
    public static final String COLUMN_STARTINGBALANCE = "startingbalance";
    public static final String COLUMN_EXCLUDEFROMBALANCE = "excludefrombalance";
    public static final String COLUMN_EXCLUDEFROMREPORTS = "excludefromreports";	
 
    // Table Create Statements
    // Account table create statement
    private static final String CREATE_TABLE_ACCOUNT = "create table " 
    		+ TABLE_ACCOUNT
	        + "(" 
	        + COLUMN_ID + " integer primary key autoincrement, " 
	        + COLUMN_DESCRIPTION + " text not null, " 
	        + COLUMN_BOOKID + " integer not null," 
	        + COLUMN_STARTINGBALANCE + " real not null,"
	        + COLUMN_EXCLUDEFROMBALANCE + " boolean not null,"
	        + COLUMN_EXCLUDEFROMREPORTS + " boolean not null"
	        + ");";
 
	public MoneyAppDatabaseHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}
 
	// Method is called during creation of the database
    @Override
    public void onCreate(SQLiteDatabase db) {
    	
        // creating required tables
        db.execSQL(CREATE_TABLE_ACCOUNT);
    }
 
	// Method is called during an upgrade of the database,
	// e.g. if you increase the database version
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Place code for upgrading tables here eg. new columns or droping tables 
    	//on upgrade drop older tables
        Log.w(LOG," Upgrading database from version " + oldVersion + " to " + newVersion
                + ", which will destroy all old data");
        //db.execSQL("DROP TABLE IF EXISTS " + TABLE_ACCOUNT);
 
        // create new tables
        //onCreate(db);
    }
	
	//Creating an account
	public long createAccount(Account account) {
	    SQLiteDatabase db = this.getWritableDatabase();
	 
	    ContentValues values = new ContentValues();
	    values.put(COLUMN_DESCRIPTION, account.getDescription());
	    values.put(COLUMN_BOOKID, account.getBookId());
	    values.put(COLUMN_STARTINGBALANCE, account.getStartingBalance());
	    values.put(COLUMN_EXCLUDEFROMBALANCE, account.getExcludeFromBalance());
	    values.put(COLUMN_EXCLUDEFROMREPORTS, account.getExcludeFromReports());
	 
	    // insert row
	    long account_id = db.insert(TABLE_ACCOUNT, null, values);
	    db.close(); // Closing database connection
	    
	    return account_id;
	}
	
	// Getting a single account based on id
	public Account getAccount(int id) {
		SQLiteDatabase db = this.getReadableDatabase();
	 
	    Cursor cursor = db.query(TABLE_ACCOUNT, new String[] { COLUMN_ID,
	            COLUMN_DESCRIPTION, COLUMN_BOOKID, COLUMN_STARTINGBALANCE,
	            COLUMN_EXCLUDEFROMBALANCE, COLUMN_EXCLUDEFROMREPORTS}, COLUMN_ID + "=?",
	            new String[] { String.valueOf(id) }, null, null, null, null);
	    if (cursor != null)
	        cursor.moveToFirst();
	 
	    Account account = new Account(Integer.parseInt(cursor.getString(0)),
	            cursor.getString(1), Integer.parseInt(cursor.getString(2)),
	            Float.parseFloat(cursor.getString(3)), Integer.parseInt(cursor.getString(4)),
	            Integer.parseInt(cursor.getString(5)));
	    
	    // return contact
	    return account;
	}
	
    // Getting all accounts
	public List<Account> getAllAccounts() {
	    List<Account> contactList = new ArrayList<Account>();
	    // Select All Query
	    String selectQuery = "SELECT  * FROM " + TABLE_ACCOUNT;
	 
	    SQLiteDatabase db = this.getWritableDatabase();
	    Cursor cursor = db.rawQuery(selectQuery, null);
	 
	    // looping through all rows and adding to list
	    if (cursor.moveToFirst()) {
	        do {
	            Account account = new Account();
	            account.setId(Integer.parseInt(cursor.getString(0)));
	            account.setDescription(cursor.getString(1));
	            account.setStartingBalance(Float.parseFloat(cursor.getString(3)));
	            account.setExcludeFromBalance(Integer.parseInt(cursor.getString(4)));
	            account.setExcludeFromReports(Integer.parseInt(cursor.getString(5)));
	            
	            // Adding contact to list
	            contactList.add(account);
	        } while (cursor.moveToNext());
	    }
	 
	    // return contact list
	    return contactList;
	}
	
	// Getting accounts count
    public int getAccountsCount() {
        String countQuery = "SELECT  * FROM " + TABLE_ACCOUNT;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        cursor.close();
 
        // return count
        return cursor.getCount();
    }
    
    // Updating single account
	public int updateAccount(Account account) {
	    SQLiteDatabase db = this.getWritableDatabase();
	 
	    ContentValues values = new ContentValues();
	    values.put(COLUMN_DESCRIPTION, account.getDescription());
	    values.put(COLUMN_BOOKID, account.getBookId());
	    values.put(COLUMN_STARTINGBALANCE, account.getStartingBalance());
	    values.put(COLUMN_EXCLUDEFROMBALANCE, account.getExcludeFromBalance());
	    values.put(COLUMN_EXCLUDEFROMREPORTS, account.getExcludeFromReports());
	 
	    // updating row
	    return db.update(TABLE_ACCOUNT, values, COLUMN_ID + " = ?",
	            new String[] { String.valueOf(account.getId()) });
	}
	
    // Deleting a single account
	public void deleteAccount(Account account) {
	    SQLiteDatabase db = this.getWritableDatabase();
	    
	    db.delete(TABLE_ACCOUNT, COLUMN_ID + " = ?",
	            new String[] { String.valueOf(account.getId()) });
	    db.close();
	}
}
