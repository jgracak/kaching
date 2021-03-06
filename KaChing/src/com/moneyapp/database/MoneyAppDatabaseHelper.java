package com.moneyapp.database;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import com.kaching.R;
import com.moneyapp.App;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.text.format.Time;
import android.util.Log;

public class MoneyAppDatabaseHelper extends SQLiteOpenHelper { 
	
	private static MoneyAppDatabaseHelper sInstance = null;
	public static final Integer GET_ALL_INCOME = 101;
	public static final Integer GET_ALL_CAT_INCOME = 102;
	public static final Integer GET_ALL_EXPENSE = 201;
	public static final Integer GET_ALL_CAT_EXPENSE = 202;	
	
	// Logcat tag
    private static final String LOG = "MoneyAppDatabaseHelper";
 
    // Database Version
    private static final int DATABASE_VERSION = 1;
 
    // Database Name
    private static final String DATABASE_NAME = "MoneyApp";
 
    // Table Names
    private static final String TABLE_ACCOUNTS = "Accounts";
    private static final String TABLE_TRANSACTIONS = "Transactions";
    private static final String TABLE_IMAGES = "Images";
    private static final String TABLE_CATEGORIES = "Categories";    
    private static final String TABLE_SETTINGS = "Settings";
 
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
 
    // IMAGES Table - column names
    public static final String COLUMN_IMAGE = "image";
    public static final String COLUMN_IMAGE_TYPE = "type";
    
    // CATEGORIES Table - column names
    public static final String COLUMN_IDIMAGE = "idImage";
    public static final String COLUMN_IDCAT = "IdCat";   
    public static final String COLUMN_CATDESC = "catDesc";   
    public static final String COLUMN_IDSUBCAT = "idSubCat";      
    public static final String COLUMN_SUBCATDESC = "subCatDesc";     
    public static final String COLUMN_CATTYPE = "type";
    
    // SETTINGS Table - column names
    public static final String COLUMN_TESTDATA = "testData";
    
    // Table Create Statements
    // Accounts table create statement
    private static final String CREATE_TABLE_ACCOUNTS = "create table " 
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
 
    // Transactions table create statement
    private static final String CREATE_TABLE_TRANSACTIONS = "create table " 
    		+ TABLE_TRANSACTIONS
	        + "(" 
	        + COLUMN_ID + " integer primary key autoincrement, " 
	        + COLUMN_TRANSDATE + " integer not null," 
	        + COLUMN_IDCATEGORY + " integer not null,"
	        + COLUMN_AMOUNT + " real not null," 
	        + COLUMN_NOTE + " text,"
	        + COLUMN_IDACCOUNT + " integer not null"
	        + ");";
    
    // Images table create statement
    private static final String CREATE_TABLE_IMAGES = "create table "
    		+ TABLE_IMAGES
	        + "(" 
	        + COLUMN_ID + " integer primary key autoincrement, " 
	        + COLUMN_IMAGE + " integer not null, "
	        + COLUMN_IMAGE_TYPE + " integer not null"
	        + ");";   
 
    // Categories table create statement
    private static final String CREATE_TABLE_CATEGORIES = "create table "
    		+ TABLE_CATEGORIES
	        + "(" 
	        + COLUMN_ID + " integer primary key autoincrement, " 
	        + COLUMN_IDIMAGE + " integer not null, " 
	        + COLUMN_IDCAT + " integer not null, "
	        + COLUMN_CATDESC + " text not null, "
	        + COLUMN_IDSUBCAT + " integer, "
	        + COLUMN_SUBCATDESC + " text, "
	        + COLUMN_CATTYPE + " integer not null"
	        + ");";  

    // Settings table create statement
    private static final String CREATE_TABLE_SETTINGS = "create table "
    		+ TABLE_SETTINGS
	        + "(" 
	        + COLUMN_ID + " integer primary key autoincrement, " 
	        + COLUMN_TESTDATA + " integer not null "
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
    	if (db == null) {
    		db = this.getWritableDatabase();
    	}
        // creating required tables
    	try {
    		db.execSQL(CREATE_TABLE_ACCOUNTS);
            db.execSQL(CREATE_TABLE_TRANSACTIONS);
            db.execSQL(CREATE_TABLE_IMAGES);
            db.execSQL(CREATE_TABLE_CATEGORIES);
            db.execSQL(CREATE_TABLE_SETTINGS);
            
            insertAllImages(db);
            insertAllCategories(db);   
        } catch (Exception e) {
            e.printStackTrace();
        } 
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
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_IMAGES);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CATEGORIES);
        
        // create new tables
        //onCreate(db);
    }
	
    // ------------------ Account table methods ------------------
	//Creating an account
	public long createAccount(TableAccount account) {
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
	public TableAccount getAccount(int id) {
		SQLiteDatabase db = this.getReadableDatabase();
	 
	    Cursor cursor = db.query(TABLE_ACCOUNTS, new String[] { COLUMN_ID,
	            COLUMN_DESCRIPTION, COLUMN_TYPE, COLUMN_BOOKID, COLUMN_STARTINGBALANCE,
	            COLUMN_INCLUDEINBALANCE, COLUMN_INCLUDEINREPORTS}, COLUMN_ID + "=?",
	            new String[] { String.valueOf(id) }, null, null, null, null);
	    
	    if (cursor != null) {
	    	cursor.moveToFirst();
	    	
		    TableAccount account = new TableAccount(Integer.parseInt(cursor.getString(0)),
		            cursor.getString(1), Integer.parseInt(cursor.getString(2)), Integer.parseInt(cursor.getString(3)),
		            Float.parseFloat(cursor.getString(4)), Integer.parseInt(cursor.getString(5)),
		            Integer.parseInt(cursor.getString(6)));
		    
		    // return account
		    return account;
	    }
	    else {
	    	return null;
	    }
	}
	
    // Getting all accounts
	public ArrayList<TableAccount> getAllAccounts() {
	    ArrayList<TableAccount> accountList = new ArrayList<TableAccount>();
	    // Select All Query
	    String selectQuery = "SELECT  * FROM " + TABLE_ACCOUNTS;
	 
	    SQLiteDatabase db = this.getWritableDatabase();
	    Cursor cursor = db.rawQuery(selectQuery, null);
	 
	    // looping through all rows and adding to list
	    if (cursor.moveToFirst()) {
	        do {
	            TableAccount account = new TableAccount();
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
	public int updateAccount(TableAccount account) {
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
	public void deleteAccount(TableAccount account) {
	    SQLiteDatabase db = this.getWritableDatabase();
	    
	    db.delete(TABLE_ACCOUNTS, COLUMN_ID + " = ?",
	            new String[] { String.valueOf(account.getId()) });
	    db.close();
	}
	
	// All total balance
    public Float getAllAccountsBalance() {
        String countQuery = "SELECT  SUM(startingbalance) FROM " + TABLE_ACCOUNTS + " WHERE " + COLUMN_INCLUDEINBALANCE + " = 1";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        Float startingbalance = (float) 0;
        if (cursor.moveToFirst()) {
        	startingbalance = cursor.getFloat(0);
        }
        cursor.close();
        
        countQuery = "SELECT SUM(" + COLUMN_AMOUNT + ") FROM " + TABLE_TRANSACTIONS + 
        				" JOIN " + TABLE_ACCOUNTS + " ON " + TABLE_TRANSACTIONS + "." + COLUMN_IDACCOUNT + "=" + TABLE_ACCOUNTS + "." + COLUMN_ID;
        
        cursor = db.rawQuery(countQuery, null);
        
        Float balance = (float) 0;
        
        if (cursor.moveToFirst()) {
        	balance = cursor.getFloat(0);
        }
        cursor.close();
        
        db.close();
        // return count
        return balance + startingbalance;
    }
	
    // Get total assets
    public Float getAssetsBalance() {
    	SQLiteDatabase db = this.getReadableDatabase();
    	List<TableAccount> accountList = getAllAccounts();
    	
    	Float totalAssetsBalance = (float) 0;
    	
    	for (TableAccount account : accountList) {
    		Float accountBalance = getAccountBalance(account.getId());
    		if (accountBalance < 0) {
    			totalAssetsBalance += accountBalance;
    		}
    	}
    	
    	db.close();
    	
    	return totalAssetsBalance;
    }
    
    // Get total liabilities
    public Float getLiabilitiesBalance() {
    	SQLiteDatabase db = this.getReadableDatabase();
    	List<TableAccount> accountList = getAllAccounts();
    	
    	Float totalLiabilitiesBalance = (float) 0;
    	
    	for (TableAccount account : accountList) {
    		Float accountBalance = getAccountBalance(account.getId());
    		if (accountBalance >= 0) {
    			totalLiabilitiesBalance += accountBalance;
    		}
    	}
    	
    	db.close();
    	
    	return totalLiabilitiesBalance;
    }
    
    // Get balance of a single account
    public Float getAccountBalance(Integer accountId) {    	
    	SQLiteDatabase db = this.getReadableDatabase();
    	String countQuery = "SELECT  SUM(startingbalance) FROM " + TABLE_ACCOUNTS + " WHERE " + COLUMN_ID + " = " + accountId;
        
        Cursor cursor = db.rawQuery(countQuery, null);
        Float startingbalance = (float) 0;
        if (cursor.moveToFirst()) {
        	startingbalance = cursor.getFloat(0);
        }
        cursor.close();
        
        countQuery = "SELECT SUM(" + COLUMN_AMOUNT + ") FROM " + TABLE_TRANSACTIONS + " WHERE " + COLUMN_IDACCOUNT + " = " + accountId;
        
        cursor = db.rawQuery(countQuery, null);
        
        Float balance = (float) 0;
        
        if (cursor.moveToFirst()) {
        	balance = cursor.getFloat(0);
        }
        cursor.close();
        
        db.close();
        // return count
        return balance + startingbalance;
    }
    
	// ------------------ Transaction table methods ------------------
	//Creating a transaction
	public long createTransaction(TableTransaction transaction) {
		SQLiteDatabase db = this.getWritableDatabase();
		
	    ContentValues values = new ContentValues();
	    
	    values.put(COLUMN_TRANSDATE, (transaction.getTransDate()));	    
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
	public TableTransaction getTransaction(int id) {
		SQLiteDatabase db = this.getReadableDatabase();
	 
	    Cursor cursor = db.query(TABLE_TRANSACTIONS, new String[] { COLUMN_ID,
	    		COLUMN_TRANSDATE, COLUMN_IDCATEGORY, COLUMN_AMOUNT, COLUMN_NOTE,
	    		COLUMN_IDACCOUNT}, COLUMN_ID + "=?",
	            new String[] { String.valueOf(id) }, null, null, null, null);
	    if (cursor != null)
	        cursor.moveToFirst();
	 
	    TableTransaction transaction = new TableTransaction(Integer.parseInt(cursor.getString(0)),
	    		cursor.getLong(1), 
	    		Integer.parseInt(cursor.getString(2)), 
	            Float.parseFloat((cursor.getString(3))),
	            cursor.getString(4), 
	            Integer.parseInt(cursor.getString(5))
	            );
	    
	    // return transaction
	    return transaction;
	}
	
	// Getting all transactions
	public List<TableTransaction> getAllTransactions() {
	    List<TableTransaction> transactionList = new ArrayList<TableTransaction>();
	    // Select All Query
	    String selectQuery = "SELECT  * FROM " + TABLE_TRANSACTIONS;
	 
	    SQLiteDatabase db = this.getWritableDatabase();
	    Cursor cursor = db.rawQuery(selectQuery, null);
	 
	    // looping through all rows and adding to list
	    if (cursor.moveToFirst()) {
	        do {
	        	TableTransaction transaction = new TableTransaction();
	        	transaction.setId(Integer.parseInt(cursor.getString(0)));
	        	transaction.setTransDate(cursor.getLong(1));
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
	public int updateTransaction(TableTransaction transaction) {
	    SQLiteDatabase db = this.getWritableDatabase();
		
	    ContentValues values = new ContentValues();
	    values.put(COLUMN_TRANSDATE, (transaction.getTransDate()));	   
	    values.put(COLUMN_IDCATEGORY, transaction.getIdCategory()); 
		values.put(COLUMN_AMOUNT, transaction.getAmount());
		values.put(COLUMN_NOTE, transaction.getNote());
		values.put(COLUMN_IDACCOUNT, transaction.getIdAccount());
	 
	    // updating row
	    return db.update(TABLE_TRANSACTIONS, values, COLUMN_ID + " = ?",
	            new String[] { String.valueOf( transaction.getId()) });
	}

    // Deleting a single transaction
	public void deleteTransaction(TableTransaction transaction) {
	    SQLiteDatabase db = this.getWritableDatabase();
	    
	    db.delete(TABLE_TRANSACTIONS, COLUMN_ID + " = ?",
	            new String[] { String.valueOf(transaction.getId()) });
	    db.close();
	}	

    // Deleting multiple transactions by category/subcategory
	public void deleteTransactionCat(TableCategory cat) {
	    SQLiteDatabase db = this.getWritableDatabase();
	    
	    List<TableCategory> categoryList = new ArrayList<TableCategory>();
	    // Select All Query
	    String selectQuery = "SELECT  * FROM " + TABLE_CATEGORIES + " WHERE (idCat = " + cat.getIdCat() + ")";
	    Cursor cursor = db.rawQuery(selectQuery, null);
	 
	    // looping through all rows and adding to list
	    if (cursor.moveToFirst()) {
	        do {
	        	TableCategory category = new TableCategory();
	        	category.setId(cursor.getInt(0));
	        	category.setIdImage(cursor.getInt(1));
	            category.setIdCat(cursor.getInt(2));
	            category.setCatDesc(cursor.getString(3));
	            category.setIdSubCat(cursor.getInt(4));
	            category.setSubCatDesc(cursor.getString(5));
	            category.setType(cursor.getInt(6));
	            
	            // Adding category to list
	        	categoryList.add(category);
	        } while (cursor.moveToNext());
	    }
	    
	    for (TableCategory category : categoryList) {
	    	db.delete(TABLE_TRANSACTIONS, COLUMN_IDCATEGORY + " = ?",
		            new String[] { String.valueOf(category.getId()) });
	    }
	    
	    db.close();
	}	
	
    // Deleting multiple transactions by subcategory
	public void deleteTransactionSubCat(TableCategory cat) {
	    SQLiteDatabase db = this.getWritableDatabase();

	    db.delete(TABLE_TRANSACTIONS, COLUMN_IDCATEGORY + " = ?",
	            new String[] { String.valueOf(cat.getId()) });
	    db.close();
	}		
	
	// Delete multiple transactions by account
	public void deleteTransactionAccount(TableAccount acc) {
	    SQLiteDatabase db = this.getWritableDatabase();

	    db.delete(TABLE_TRANSACTIONS, COLUMN_IDACCOUNT + " = ?",
	            new String[] { String.valueOf(acc.getId()) });
	    db.close();
	}
	
	// Get Transaction dates in range
	// TODO Add range argument
	public ArrayList<Long> getTransactionDates() {
		ArrayList<Long> dateList = new ArrayList<Long>();
	    // Select All Query
	    String selectQuery = "SELECT DISTINCT " + COLUMN_TRANSDATE + "  FROM " + TABLE_TRANSACTIONS + " ORDER BY " +
	    						COLUMN_TRANSDATE + " ASC";
	 
	    SQLiteDatabase db = this.getWritableDatabase();
	    Cursor cursor = db.rawQuery(selectQuery, null);
	 
	    // looping through all rows and adding to list
	    if (cursor.moveToFirst()) {
	        do {
	            // Adding transaction to list
	        	dateList.add(cursor.getLong(0));
	        } while (cursor.moveToNext());
	    }
	 
	    // return transaction list
	    return dateList;
	}
	
	// Get Balance by date
	public String getBalanceByDate(Long date) {
	    // Select All Query
	    String selectQuery = "SELECT SUM(" + COLUMN_AMOUNT + ") FROM " + TABLE_TRANSACTIONS + " WHERE " +
	    						COLUMN_TRANSDATE + "= " + date;
	 
	    SQLiteDatabase db = this.getWritableDatabase();
	    Cursor cursor = db.rawQuery(selectQuery, null);
	 
	    Float balance = (float) 0;
	    
	    // looping through all rows and adding to list
	    if (cursor.moveToFirst()) {
	    	balance = cursor.getFloat(0);
	    }
	    
	    // return transaction list
	    return balance.toString();
	}
	
	// Get transactions by date
	public List<TableTransaction> getTransactionByDate(Long date) {
	    List<TableTransaction> transactionList = new ArrayList<TableTransaction>();
	    // Select All Query
	    String selectQuery = "SELECT  * FROM " + TABLE_TRANSACTIONS + " WHERE " + COLUMN_TRANSDATE + "= " + date;
	 
	    SQLiteDatabase db = this.getWritableDatabase();
	    Cursor cursor = db.rawQuery(selectQuery, null);
	 
	    // looping through all rows and adding to list
	    if (cursor.moveToFirst()) {
	        do {
	        	TableTransaction transaction = new TableTransaction();
	        	transaction.setId(Integer.parseInt(cursor.getString(0)));
	        	transaction.setTransDate(cursor.getLong(1));
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
	
	// ------------------ Images table methods ------------------
	// Creating an image
	public long createImage(TableImage image, int type) {
		SQLiteDatabase db = this.getWritableDatabase();
		
	    ContentValues values = new ContentValues();
	    
	    values.put(COLUMN_IMAGE, image.getImage());	    
	    values.put(COLUMN_IMAGE_TYPE,type);
	 
	    // insert row
	    long transaction_id = db.insert(TABLE_IMAGES, null, values);
	    db.close(); // Closing database connection
	    
	    return transaction_id;
	}	
	
	// Getting a single image based on id
	public TableImage getImage(int id) {
		SQLiteDatabase db = this.getReadableDatabase();
	 
	    Cursor cursor = db.query(TABLE_IMAGES, new String[] { COLUMN_ID,
	    		COLUMN_IMAGE,COLUMN_IMAGE_TYPE}, COLUMN_ID + "=?",
	            new String[] { String.valueOf(id) }, null, null, null, null);
	    if (cursor != null)
	        cursor.moveToFirst();
	 
	    TableImage image = new TableImage(cursor.getInt(0),cursor.getInt(1),cursor.getInt(2));
	    
	    // return image
	    return image;
	}
	
	// Getting image ID by ref number
	public int getImageIdByRef(int image) {
		SQLiteDatabase db = this.getReadableDatabase();
		 
	    Cursor cursor = db.query(TABLE_IMAGES, new String[] { COLUMN_ID,
	    		COLUMN_IMAGE,COLUMN_IMAGE_TYPE}, COLUMN_IMAGE + "=?",
	            new String[] { String.valueOf(image) }, null, null, null, null);
	    if (cursor != null)
	        cursor.moveToFirst();
	    
	    // return id
	    return cursor.getInt(0);
	}

	// Getting all images
	public List<TableImage> getAllImages() {
	    List<TableImage> imageList = new ArrayList<TableImage>();
	    // Select All Query
	    String selectQuery = "SELECT  * FROM " + TABLE_IMAGES;
	 
	    SQLiteDatabase db = this.getWritableDatabase();
	    Cursor cursor = db.rawQuery(selectQuery, null);
	 
	    // looping through all rows and adding to list
	    if (cursor.moveToFirst()) {
	        do {
	        	TableImage image = new TableImage();
	        	image.setId(cursor.getInt(0));
	        	image.setImage(cursor.getInt(1));
	        	image.setType(cursor.getInt(2));
	            
	            // Adding image to list
	        	imageList.add(image);
	        } while (cursor.moveToNext());
	    }
	 
	    // return image list
	    return imageList;
	}
	
	public List<TableImage> getAllImagesType(int type) {
	    List<TableImage> imageList = new ArrayList<TableImage>();
	    // Select All Query
	    String selectQuery = "SELECT  * FROM " + TABLE_IMAGES + " WHERE type = " + type;
	 
	    SQLiteDatabase db = this.getWritableDatabase();
	    Cursor cursor = db.rawQuery(selectQuery, null);
	 
	    // looping through all rows and adding to list
	    if (cursor.moveToFirst()) {
	        do {
	        	TableImage image = new TableImage();
	        	image.setId(cursor.getInt(0));
	        	image.setImage(cursor.getInt(1));
	        	image.setType(cursor.getInt(2));
	            
	            // Adding image to list
	        	imageList.add(image);
	        } while (cursor.moveToNext());
	    }
	 
	    // return image list
	    return imageList;
	}
	
	// Getting images count
    public int getImagesCount() {
        String countQuery = "SELECT  * FROM " + TABLE_IMAGES;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        int count = cursor.getCount();
        cursor.close();
        // return count
        return count;
    }	
    
    public int getImagesCountType(int type) {
		String countQuery = "SELECT  * FROM " + TABLE_IMAGES + " WHERE type = " + type;
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.rawQuery(countQuery, null);
		int count = cursor.getCount();
		cursor.close();
		// return count
		return count;
    }

    // Updating single image
	public int updateImage(TableImage image) {
	    SQLiteDatabase db = this.getWritableDatabase();
		
	    ContentValues values = new ContentValues();
	    values.put(COLUMN_IMAGE, image.getImage());	   
	 
	    // updating row
	    return db.update(TABLE_IMAGES, values, COLUMN_ID + " = ?",
	            new String[] { String.valueOf( image.getId()) });
	}
    
	// Deleting a single image
	public void deleteImage(TableImage image) {
	    SQLiteDatabase db = this.getWritableDatabase();
	    
	    db.delete(TABLE_IMAGES, COLUMN_ID + " = ?",
	            new String[] { String.valueOf(image.getId()) });
	    db.close();
	}	
	
	// Method used for inserting all images into the table
	public void insertAllImages(SQLiteDatabase db){
	    ContentValues values = new ContentValues();
	    
	    values.put(COLUMN_IMAGE, R.drawable.cash);	    
	    values.put(COLUMN_IMAGE_TYPE,0); //Type 0 is for categories
	    db.insert(TABLE_IMAGES, null, values);
	    values.clear();

	    values.put(COLUMN_IMAGE, R.drawable.bank);
	    values.put(COLUMN_IMAGE_TYPE,0); //Type 0 is for categories
	    db.insert(TABLE_IMAGES, null, values);
	    values.clear();
	    
	    values.put(COLUMN_IMAGE, R.drawable.credit_card);
	    values.put(COLUMN_IMAGE_TYPE,0); //Type 0 is for categories
	    db.insert(TABLE_IMAGES, null, values);
	    values.clear();
	    
	    values.put(COLUMN_IMAGE, R.drawable.bench);	    
	    values.put(COLUMN_IMAGE_TYPE,0); //Type 0 is for categories
	    db.insert(TABLE_IMAGES, null, values);
	    values.clear();
	    
	    values.put(COLUMN_IMAGE, R.drawable.bicycle);	    
	    values.put(COLUMN_IMAGE_TYPE,0); //Type 0 is for categories
	    db.insert(TABLE_IMAGES, null, values);
	    values.clear();
	    
	    values.put(COLUMN_IMAGE, R.drawable.camera);	    
	    values.put(COLUMN_IMAGE_TYPE,0); //Type 0 is for categories
	    db.insert(TABLE_IMAGES, null, values);
	    values.clear();
	    
	    values.put(COLUMN_IMAGE, R.drawable.car);	    
	    values.put(COLUMN_IMAGE_TYPE,0); //Type 0 is for categories
	    db.insert(TABLE_IMAGES, null, values);
	    values.clear();
	    
	    values.put(COLUMN_IMAGE, R.drawable.card_file);	    
	    values.put(COLUMN_IMAGE_TYPE,0); //Type 0 is for categories
	    db.insert(TABLE_IMAGES, null, values);
	    values.clear();
	    
	    values.put(COLUMN_IMAGE, R.drawable.cart_empty);	    
	    values.put(COLUMN_IMAGE_TYPE,0); //Type 0 is for categories
	    db.insert(TABLE_IMAGES, null, values);
	    values.clear();
	    
	    values.put(COLUMN_IMAGE, R.drawable.cart_full);	    
	    values.put(COLUMN_IMAGE_TYPE,0); //Type 0 is for categories
	    db.insert(TABLE_IMAGES, null, values);
	    values.clear();
	    
	    values.put(COLUMN_IMAGE, R.drawable.coffe);	    
	    values.put(COLUMN_IMAGE_TYPE,0); //Type 0 is for categories
	    db.insert(TABLE_IMAGES, null, values);
	    values.clear();
	    
	    values.put(COLUMN_IMAGE, R.drawable.company);	    
	    values.put(COLUMN_IMAGE_TYPE,0); //Type 0 is for categories
	    db.insert(TABLE_IMAGES, null, values);
	    values.clear();
	    
	    values.put(COLUMN_IMAGE, R.drawable.delivery);	    
	    values.put(COLUMN_IMAGE_TYPE,0); //Type 0 is for categories
	    db.insert(TABLE_IMAGES, null, values);
	    values.clear();
	    
	    values.put(COLUMN_IMAGE, R.drawable.dice);	    
	    values.put(COLUMN_IMAGE_TYPE,0); //Type 0 is for categories
	    db.insert(TABLE_IMAGES, null, values);
	    values.clear();
	    
	    values.put(COLUMN_IMAGE, R.drawable.headphones);	    
	    values.put(COLUMN_IMAGE_TYPE,0); //Type 0 is for categories
	    db.insert(TABLE_IMAGES, null, values);
	    values.clear();
	    
	    values.put(COLUMN_IMAGE, R.drawable.home);	    
	    values.put(COLUMN_IMAGE_TYPE,0); //Type 0 is for categories
	    db.insert(TABLE_IMAGES, null, values);
	    values.clear();
	    
	    values.put(COLUMN_IMAGE, R.drawable.house);	    
	    values.put(COLUMN_IMAGE_TYPE,0); //Type 0 is for categories
	    db.insert(TABLE_IMAGES, null, values);
	    values.clear();
	    
	    values.put(COLUMN_IMAGE, R.drawable.lock);	    
	    values.put(COLUMN_IMAGE_TYPE,0); //Type 0 is for categories
	    db.insert(TABLE_IMAGES, null, values);
	    values.clear();
	    
	    values.put(COLUMN_IMAGE, R.drawable.muffin);	    
	    values.put(COLUMN_IMAGE_TYPE,0); //Type 0 is for categories
	    db.insert(TABLE_IMAGES, null, values);
	    values.clear();
	    
	    values.put(COLUMN_IMAGE, R.drawable.spray);	    
	    values.put(COLUMN_IMAGE_TYPE,0); //Type 0 is for categories
	    db.insert(TABLE_IMAGES, null, values);
	    values.clear();
	    
	    values.put(COLUMN_IMAGE, R.drawable.telephone);	    
	    values.put(COLUMN_IMAGE_TYPE,0); //Type 0 is for categories
	    db.insert(TABLE_IMAGES, null, values);
	    values.clear();
	    
	    values.put(COLUMN_IMAGE, R.drawable.time);	    
	    values.put(COLUMN_IMAGE_TYPE,0); //Type 0 is for categories
	    db.insert(TABLE_IMAGES, null, values);
	    values.clear();
	    
	    values.put(COLUMN_IMAGE, R.drawable.violin);	    
	    values.put(COLUMN_IMAGE_TYPE,0); //Type 0 is for categories
	    db.insert(TABLE_IMAGES, null, values);
	    values.clear();
	    
	    values.put(COLUMN_IMAGE, R.drawable.wallet);	    
	    values.put(COLUMN_IMAGE_TYPE,0); //Type 0 is for categories
	    db.insert(TABLE_IMAGES, null, values);
	    values.clear();
	    
	    values.put(COLUMN_IMAGE, R.drawable.world);	    
	    values.put(COLUMN_IMAGE_TYPE,0); //Type 0 is for categories
	    db.insert(TABLE_IMAGES, null, values);
	    values.clear();
	}
	
	// ------------------ Categories table methods ------------------
	// Creating a category
	public long createCategory(TableCategory category) {
		SQLiteDatabase db = this.getWritableDatabase();
		
	    ContentValues values = new ContentValues();
	    
	    values.put(COLUMN_IDIMAGE, category.getIdImage());	    
	    values.put(COLUMN_IDCAT, category.getIdCat());
	    values.put(COLUMN_CATDESC, category.getCatDesc());
	    values.put(COLUMN_IDSUBCAT, category.getIdSubCat());
	    values.put(COLUMN_SUBCATDESC, category.getSubCatDesc());
	    values.put(COLUMN_CATTYPE, category.getType());
	    
	    // insert row
	    long transaction_id = db.insert(TABLE_CATEGORIES, null, values);
	    db.close(); // Closing database connection
	    
	    return transaction_id;
	}	
	
	// Getting a single category based on id
	public TableCategory getCategory(int id) {
		SQLiteDatabase db = this.getReadableDatabase();
	 
	    Cursor cursor = db.query(TABLE_CATEGORIES, new String[] { COLUMN_ID,
	    		COLUMN_IDIMAGE,COLUMN_IDCAT,COLUMN_CATDESC,COLUMN_IDSUBCAT,
	    		COLUMN_SUBCATDESC,COLUMN_CATTYPE}, COLUMN_ID + "=?",
	            new String[] { String.valueOf(id) }, null, null, null, null);
	    if (cursor != null)
	        cursor.moveToFirst();
	 
	    TableCategory category = new TableCategory(cursor.getInt(0),cursor.getInt(1),cursor.getInt(2),
	    								cursor.getString(3),cursor.getInt(4),cursor.getString(5),
	    								cursor.getInt(6));
	    
	    // return category
	    return category;
	}
	
	// Get subcategory based on category id and subcategory id
	public TableCategory getSubCategory(int idCat,int idSubcat) {
		TableCategory cat = new TableCategory();
	    // Select All Query
	    String selectQuery = "SELECT  * FROM " + TABLE_CATEGORIES + " WHERE IdCat = " + idCat + " AND idSubCat = " + idSubcat;
	 
	    SQLiteDatabase db = this.getWritableDatabase();
	    Cursor cursor = db.rawQuery(selectQuery, null);
	 
	    // looping through all rows and adding to list
	    if (cursor.moveToFirst()) {
	        	cat.setId(cursor.getInt(0));
	        	cat.setIdImage(cursor.getInt(1));
	        	cat.setIdCat(cursor.getInt(2));
	        	cat.setCatDesc(cursor.getString(3));
	        	cat.setIdSubCat(cursor.getInt(4));
	        	cat.setSubCatDesc(cursor.getString(5));
	        	cat.setType(cursor.getInt(6));
	    }
	 
	    // return image list
	    return cat;
	}
	
	// Get last subcategory in a category
	public TableCategory getLastSubCategory(int catId) {
	    SQLiteDatabase db = this.getWritableDatabase();
	    
	    Cursor cursor = db.query(TABLE_CATEGORIES, new String[] { COLUMN_ID,
	    		COLUMN_IDIMAGE,COLUMN_IDCAT,COLUMN_CATDESC,COLUMN_IDSUBCAT,
	    		COLUMN_SUBCATDESC,COLUMN_CATTYPE}, COLUMN_IDCAT + "=?",
	            new String[] { String.valueOf(catId) }, null, null, null, null);

	    if (cursor != null)
	        cursor.moveToLast();
	 
	    TableCategory category = new TableCategory(cursor.getInt(0),cursor.getInt(1),cursor.getInt(2),
	    								cursor.getString(3),cursor.getInt(4),cursor.getString(5),
	    								cursor.getInt(6));
	    
	    // return category
	    return category;
	}

	// Getting all categories
	public List<TableCategory> getAllCategories() {
	    List<TableCategory> categoryList = new ArrayList<TableCategory>();
	    // Select All Query
	    String selectQuery = "SELECT  * FROM " + TABLE_CATEGORIES  + " order by idCat asc, idSubCat asc";
	 
	    SQLiteDatabase db = this.getWritableDatabase();
	    Cursor cursor = db.rawQuery(selectQuery, null);
	 
	    // looping through all rows and adding to list
	    if (cursor.moveToFirst()) {
	        do {
	        	TableCategory category = new TableCategory();
	        	category.setId(cursor.getInt(0));
	        	category.setIdImage(cursor.getInt(1));
	            category.setIdCat(cursor.getInt(2));
	            category.setCatDesc(cursor.getString(3));
	            category.setIdSubCat(cursor.getInt(4));
	            category.setSubCatDesc(cursor.getString(5));
	            category.setType(cursor.getInt(6));
	            
	            // Adding category to list
	        	categoryList.add(category);
	        } while (cursor.moveToNext());
	    }
	 
	    // return image list
	    return categoryList;
	}
	
	// Getting all income categories
	// This method has 2 modes
	public List<TableCategory> getAllIncomeCategories(Integer mode) {
	    List<TableCategory> categoryList = new ArrayList<TableCategory>();
	    String selectQuery;
	    
	    // Select All Query
	    if (mode == GET_ALL_INCOME) {
	    	 selectQuery = "SELECT  * FROM " + TABLE_CATEGORIES + " WHERE (type = 0) order by idCat asc, idSubCat asc";
	    } else if (mode == GET_ALL_CAT_INCOME ) {
	    	selectQuery = "SELECT  * FROM " + TABLE_CATEGORIES + " WHERE (type = 0) AND (idSubCat = 0) order by idCat asc, idSubCat asc";
	    } else {
	    	return null;
	    }
	 
	    SQLiteDatabase db = this.getWritableDatabase();
	    Cursor cursor = db.rawQuery(selectQuery, null);
	 
	    // looping through all rows and adding to list
	    if (cursor.moveToFirst()) {
	        do {
	        	TableCategory category = new TableCategory();
	        	category.setId(cursor.getInt(0));
	        	category.setIdImage(cursor.getInt(1));
	            category.setIdCat(cursor.getInt(2));
	            category.setCatDesc(cursor.getString(3));
	            category.setIdSubCat(cursor.getInt(4));
	            category.setSubCatDesc(cursor.getString(5));
	            category.setType(cursor.getInt(6));
	            
	            // Adding category to list
	        	categoryList.add(category);
	        } while (cursor.moveToNext());
	    }
	 
	    // return image list
	    return categoryList;
	}	
	
	// Getting all expense categories
	public List<TableCategory> getAllExpenseCategories(Integer mode) {
	    List<TableCategory> categoryList = new ArrayList<TableCategory>();
	    String selectQuery;
	    
	    // Select All Query
	    
	    if (mode == GET_ALL_EXPENSE) {
	    	 selectQuery = "SELECT  * FROM " + TABLE_CATEGORIES + " WHERE (type = 1) order by idCat asc, idSubCat asc";
	    } else if (mode == GET_ALL_CAT_EXPENSE ) {
	    	selectQuery = "SELECT  * FROM " + TABLE_CATEGORIES + " WHERE (type = 1) AND (idSubCat = 0) order by idCat asc, idSubCat asc";
	    } else {
	    	return null;
	    }
	 
	    SQLiteDatabase db = this.getWritableDatabase();
	    Cursor cursor = db.rawQuery(selectQuery, null);
	 
	    // looping through all rows and adding to list
	    if (cursor.moveToFirst()) {
	        do {
	        	TableCategory category = new TableCategory();
	        	category.setId(cursor.getInt(0));
	        	category.setIdImage(cursor.getInt(1));
	            category.setIdCat(cursor.getInt(2));
	            category.setCatDesc(cursor.getString(3));
	            category.setIdSubCat(cursor.getInt(4));
	            category.setSubCatDesc(cursor.getString(5));
	            category.setType(cursor.getInt(6));
	            
	            // Adding category to list
	        	categoryList.add(category);
	        } while (cursor.moveToNext());
	    }
	 
	    // return image list
	    return categoryList;
	}	

	// Getting all categories by Id
	public List<TableCategory> getAllCategoriesById(int id) {
	    List<TableCategory> categoryList = new ArrayList<TableCategory>();
	    // Select All Query
	    String selectQuery = "SELECT  * FROM " + TABLE_CATEGORIES + " WHERE (idCat = " + id + ")";
	 
	    SQLiteDatabase db = this.getWritableDatabase();
	    Cursor cursor = db.rawQuery(selectQuery, null);
	 
	    // looping through all rows and adding to list
	    if (cursor.moveToFirst()) {
	        do {
	        	TableCategory category = new TableCategory();
	        	category.setId(cursor.getInt(0));
	        	category.setIdImage(cursor.getInt(1));
	            category.setIdCat(cursor.getInt(2));
	            category.setCatDesc(cursor.getString(3));
	            category.setIdSubCat(cursor.getInt(4));
	            category.setSubCatDesc(cursor.getString(5));
	            category.setType(cursor.getInt(6));
	            
	            // Adding category to list
	        	categoryList.add(category);
	        } while (cursor.moveToNext());
	    }
	 
	    // return image list
	    return categoryList;
	}	
	
	// Getting all subcategories
	public List<TableCategory> getAllSubCategories(TableCategory cat) {
	    List<TableCategory> categoryList = new ArrayList<TableCategory>();
	    // Select All subcategories query
	    String selectQuery = "SELECT  * FROM " + TABLE_CATEGORIES + " WHERE (idCat = " + cat.getIdCat() + ")"
	    						+ "AND (idSubCat > 0)";
	 
	    SQLiteDatabase db = this.getWritableDatabase();
	    Cursor cursor = db.rawQuery(selectQuery, null);
	 
	    // looping through all rows and adding to list
	    if (cursor.moveToFirst()) {
	        do {
	        	TableCategory category = new TableCategory();
	        	category.setId(cursor.getInt(0));
	        	category.setIdImage(cursor.getInt(1));
	            category.setIdCat(cursor.getInt(2));
	            category.setCatDesc(cursor.getString(3));
	            category.setIdSubCat(cursor.getInt(4));
	            category.setSubCatDesc(cursor.getString(5));
	            category.setType(cursor.getInt(6));
	            
	            // Adding category to list
	        	categoryList.add(category);
	        } while (cursor.moveToNext());
	    }
	 
	    // return image list
	    return categoryList;
	}	
	
	// Getting categories count
    public int getCategoriesCount() {
        String countQuery = "SELECT  * FROM " + TABLE_CATEGORIES;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        int count = cursor.getCount();
        cursor.close();
        // return count
        return count;
    }	

    // Get category by name
    public boolean getSubCategoryByName(String name,int catId) {
	    String selectQuery = "SELECT  * FROM " + TABLE_CATEGORIES + " WHERE (idCat = " + catId + ")"
				+ " AND (subCatDesc = '" + name + "')";

	    SQLiteDatabase db = this.getWritableDatabase();
	    Cursor cursor = db.rawQuery(selectQuery, null);
	    
	    if (cursor.getCount() > 0) {
	    	return true;
	    } else {
	    	return false;
	    }
    }
    
    // Get subcategory by name and catid
    public boolean getCategoryName(String name) {
		SQLiteDatabase db = this.getReadableDatabase();
		
	    Cursor cursor = db.query(TABLE_CATEGORIES, new String[] { COLUMN_ID,
	    		COLUMN_IDIMAGE,COLUMN_IDCAT,COLUMN_CATDESC,COLUMN_IDSUBCAT,
	    		COLUMN_SUBCATDESC,COLUMN_CATTYPE}, COLUMN_CATDESC + "=?",
	            new String[] { name }, null, null, null, null);
		
	    if (cursor.getCount() > 0) {
	    	return true;
	    } else {
	    	return false;
	    }
    }    
    
    // Get last category id by type
    public int getCategoryIdByType(int type) {
		SQLiteDatabase db = this.getReadableDatabase();
	    Cursor cursor = db.query(TABLE_CATEGORIES, new String[] { COLUMN_ID,
	    		COLUMN_IDIMAGE,COLUMN_IDCAT,COLUMN_CATDESC,COLUMN_IDSUBCAT,
	    		COLUMN_SUBCATDESC,COLUMN_CATTYPE}, COLUMN_CATTYPE + "=?",
	            new String[] { String.valueOf(type) }, null, null, null, null);
	    if (cursor != null) {
	    	cursor.moveToLast();
	    	return cursor.getInt(2) + 1;
	    } else {
	    	return 0;
	    }
    }
    
    // Get last category id 
	public int getLastCategoryId() {
	    // Select All Query
	    String selectQuery = "SELECT MAX(IdCat) FROM " + TABLE_CATEGORIES;
	 
	    SQLiteDatabase db = this.getWritableDatabase();
	    Cursor cursor = db.rawQuery(selectQuery, null);
	 
	    if (cursor.moveToFirst()) {
	    	return cursor.getInt(0);
	    } else {
	    	return 0;
	    }  
	}
    
    // Updating single category
	public int updateCategory(TableCategory category) {
	    SQLiteDatabase db = this.getWritableDatabase();
	    
	    ContentValues values = new ContentValues();
	    
	    values.put(COLUMN_IDIMAGE, category.getIdImage());	    
	    values.put(COLUMN_CATDESC, category.getCatDesc());
	    values.put(COLUMN_CATTYPE, category.getType());
	    
	    // updating row
	    return db.update(TABLE_CATEGORIES, values, COLUMN_IDCAT + " = ?",
	            new String[] { String.valueOf(category.getIdCat()) });
	}
	
    // Updating single subcategory
	public int updateSubCategoryName(TableCategory category) {
	    SQLiteDatabase db = this.getWritableDatabase();
	    
	    ContentValues values = new ContentValues();
	    
	    values.put(COLUMN_SUBCATDESC, category.getSubCatDesc());
	    
	    // updating row
	    return db.update(TABLE_CATEGORIES, values, COLUMN_ID + " = ?",
	            new String[] { String.valueOf(category.getId()) });
	}
    
	// Deleting a single category
	public void deleteCategory(TableCategory category) {
	    SQLiteDatabase db = this.getWritableDatabase();
	    
	    db.delete(TABLE_CATEGORIES, COLUMN_ID + " = ?",
	            new String[] { String.valueOf(category.getId()) });
	    db.close();
	}	
	
	// Method used for inserting all categories into the table
	private void insertAllCategories(SQLiteDatabase db) {
	    ContentValues values = new ContentValues();
	    
	    //EXPENSE
	    //CATEGORY 1	    
	    values.put(COLUMN_IDIMAGE, 1);	    
	    values.put(COLUMN_IDCAT, 1);
	    values.put(COLUMN_CATDESC,App.context.getResources().getString(R.string.categoryExpense1));
	    values.put(COLUMN_IDSUBCAT, 0);
	    values.put(COLUMN_CATTYPE, 1);
	    db.insert(TABLE_CATEGORIES, null, values);
	    
	    //CATEGORY 1 - SUBCATEGORY 1	    	    
	    values.clear();
	    values.put(COLUMN_IDIMAGE, 1);	    
	    values.put(COLUMN_IDCAT, 1);
	    values.put(COLUMN_CATDESC,App.context.getResources().getString(R.string.categoryExpense1));
	    values.put(COLUMN_IDSUBCAT, 1);
	    values.put(COLUMN_SUBCATDESC,App.context.getResources().getString(R.string.subCategoryExpense101));
	    values.put(COLUMN_CATTYPE, 1);
	    db.insert(TABLE_CATEGORIES, null, values);
	    
	    //CATEGORY 1 - SUBCATEGORY 2		    
	    values.clear();
	    values.put(COLUMN_IDIMAGE, 1);	    
	    values.put(COLUMN_IDCAT, 1);
	    values.put(COLUMN_CATDESC,App.context.getResources().getString(R.string.categoryExpense1));
	    values.put(COLUMN_IDSUBCAT, 2);
	    values.put(COLUMN_SUBCATDESC,App.context.getResources().getString(R.string.subCategoryExpense102));
	    values.put(COLUMN_CATTYPE, 1);
	    db.insert(TABLE_CATEGORIES, null, values);
	    
	    //CATEGORY 1 - SUBCATEGORY 3		    
	    values.clear();
	    values.put(COLUMN_IDIMAGE, 1);	    
	    values.put(COLUMN_IDCAT, 1);
	    values.put(COLUMN_CATDESC,App.context.getResources().getString(R.string.categoryExpense1));
	    values.put(COLUMN_IDSUBCAT, 3);
	    values.put(COLUMN_SUBCATDESC, App.context.getResources().getString(R.string.subCategoryExpense103));
	    values.put(COLUMN_CATTYPE, 1);
	    db.insert(TABLE_CATEGORIES, null, values);
        
	    //CATEGORY 2		    
	    values.clear();
	    values.put(COLUMN_IDIMAGE, 2);	    
	    values.put(COLUMN_IDCAT, 2);
	    values.put(COLUMN_CATDESC,App.context.getResources().getString(R.string.categoryExpense2));
	    values.put(COLUMN_IDSUBCAT, 0);
	    values.put(COLUMN_CATTYPE, 1);
	    db.insert(TABLE_CATEGORIES, null, values);
	    
	    //CATEGORY 2 - SUBCATEGORY 1	    
	    values.clear();
	    values.put(COLUMN_IDIMAGE, 2);	    
	    values.put(COLUMN_IDCAT, 2);
	    values.put(COLUMN_CATDESC,App.context.getResources().getString(R.string.categoryExpense2));
	    values.put(COLUMN_IDSUBCAT, 1);
	    values.put(COLUMN_SUBCATDESC, App.context.getResources().getString(R.string.subCategoryExpense201));
	    values.put(COLUMN_CATTYPE, 1);
	    db.insert(TABLE_CATEGORIES, null, values);
	    
	    //CATEGORY 2 - SUBCATEGORY 2	    
	    values.clear();
	    values.put(COLUMN_IDIMAGE, 2);	    
	    values.put(COLUMN_IDCAT, 2);
	    values.put(COLUMN_CATDESC,App.context.getResources().getString(R.string.categoryExpense2));
	    values.put(COLUMN_IDSUBCAT, 2);
	    values.put(COLUMN_SUBCATDESC, App.context.getResources().getString(R.string.subCategoryExpense202));
	    values.put(COLUMN_CATTYPE, 1);
	    db.insert(TABLE_CATEGORIES, null, values);
	    
	    //CATEGORY 2 - SUBCATEGORY 3	    
	    values.clear();
	    values.put(COLUMN_IDIMAGE, 2);	    
	    values.put(COLUMN_IDCAT, 2);
	    values.put(COLUMN_CATDESC,App.context.getResources().getString(R.string.categoryExpense2));
	    values.put(COLUMN_IDSUBCAT, 3);
	    values.put(COLUMN_SUBCATDESC, App.context.getResources().getString(R.string.subCategoryExpense203));
	    values.put(COLUMN_CATTYPE, 1);
	    db.insert(TABLE_CATEGORIES, null, values);

	    //INCOME
	    //CATEGORY 1
	    values.clear();
	    values.put(COLUMN_IDIMAGE, 3);	    
	    values.put(COLUMN_IDCAT, 3);
	    values.put(COLUMN_CATDESC,App.context.getResources().getString(R.string.categoryIncome1));
	    values.put(COLUMN_IDSUBCAT, 0);
	    values.put(COLUMN_CATTYPE, 0);
	    db.insert(TABLE_CATEGORIES, null, values);
	    
	    //CATEGORY 1 - SUBCATEGORY 1
	    values.clear();
	    values.put(COLUMN_IDIMAGE, 3);	    
	    values.put(COLUMN_IDCAT, 3);
	    values.put(COLUMN_CATDESC,App.context.getResources().getString(R.string.categoryIncome1));
	    values.put(COLUMN_IDSUBCAT, 1);
	    values.put(COLUMN_SUBCATDESC, App.context.getResources().getString(R.string.subCategoryIncome101));
	    values.put(COLUMN_CATTYPE, 0);
	    db.insert(TABLE_CATEGORIES, null, values);
	    
	    //CATEGORY 1 - SUBCATEGORY 2
	    values.clear();
	    values.put(COLUMN_IDIMAGE, 3);	    
	    values.put(COLUMN_IDCAT, 3);
	    values.put(COLUMN_CATDESC,App.context.getResources().getString(R.string.categoryIncome1));
	    values.put(COLUMN_IDSUBCAT, 2);
	    values.put(COLUMN_SUBCATDESC, App.context.getResources().getString(R.string.subCategoryIncome102));
	    values.put(COLUMN_CATTYPE, 0);
	    db.insert(TABLE_CATEGORIES, null, values);

	    //CATEGORY 1 - SUBCATEGORY 3
	    values.clear();
	    values.put(COLUMN_IDIMAGE, 3);	    
	    values.put(COLUMN_IDCAT, 3);
	    values.put(COLUMN_CATDESC,App.context.getResources().getString(R.string.categoryIncome1));
	    values.put(COLUMN_IDSUBCAT, 3);
	    values.put(COLUMN_SUBCATDESC, App.context.getResources().getString(R.string.subCategoryIncome103));
	    values.put(COLUMN_CATTYPE, 0);
	    db.insert(TABLE_CATEGORIES, null, values);	    

	    //CATEGORY 2
	    values.clear();
	    values.put(COLUMN_IDIMAGE, 4);	    
	    values.put(COLUMN_IDCAT, 4);
	    values.put(COLUMN_CATDESC,App.context.getResources().getString(R.string.categoryIncome2));
	    values.put(COLUMN_IDSUBCAT, 0);
	    values.put(COLUMN_CATTYPE, 0);
	    db.insert(TABLE_CATEGORIES, null, values);
	    
	    //CATEGORY 2 - SUBCATEGORY 1
	    values.clear();
	    values.put(COLUMN_IDIMAGE, 4);	    
	    values.put(COLUMN_IDCAT, 4);
	    values.put(COLUMN_CATDESC,App.context.getResources().getString(R.string.categoryIncome2));
	    values.put(COLUMN_IDSUBCAT, 1);
	    values.put(COLUMN_SUBCATDESC, App.context.getResources().getString(R.string.subCategoryIncome201));
	    values.put(COLUMN_CATTYPE, 0);
	    db.insert(TABLE_CATEGORIES, null, values);	 	
	    
	    //CATEGORY 2 - SUBCATEGORY 2
	    values.clear();
	    values.put(COLUMN_IDIMAGE, 4);	    
	    values.put(COLUMN_IDCAT, 4);
	    values.put(COLUMN_CATDESC,App.context.getResources().getString(R.string.categoryIncome2));
	    values.put(COLUMN_IDSUBCAT, 2);
	    values.put(COLUMN_SUBCATDESC, App.context.getResources().getString(R.string.subCategoryIncome202));
	    values.put(COLUMN_CATTYPE, 0);
	    db.insert(TABLE_CATEGORIES, null, values);	 	  
	    
	    //CATEGORY 2 - SUBCATEGORY 3
	    values.clear();
	    values.put(COLUMN_IDIMAGE, 4);	    
	    values.put(COLUMN_IDCAT, 4);
	    values.put(COLUMN_CATDESC,App.context.getResources().getString(R.string.categoryIncome2));
	    values.put(COLUMN_IDSUBCAT, 3);
	    values.put(COLUMN_SUBCATDESC, App.context.getResources().getString(R.string.subCategoryIncome203));
	    values.put(COLUMN_CATTYPE, 0);
	    db.insert(TABLE_CATEGORIES, null, values);		 
	    
	    //CATEGORY 3
	    values.clear();
	    values.put(COLUMN_IDIMAGE, 5);	    
	    values.put(COLUMN_IDCAT, 5);
	    values.put(COLUMN_CATDESC,App.context.getResources().getString(R.string.categoryIncome3));
	    values.put(COLUMN_IDSUBCAT, 0);
	    values.put(COLUMN_CATTYPE, 0);
	    db.insert(TABLE_CATEGORIES, null, values);
	    
	    //CATEGORY 3 - SUBCATEGORY 1
	    values.clear();
	    values.put(COLUMN_IDIMAGE, 5);	    
	    values.put(COLUMN_IDCAT, 5);
	    values.put(COLUMN_CATDESC,App.context.getResources().getString(R.string.categoryIncome3));
	    values.put(COLUMN_IDSUBCAT, 1);
	    values.put(COLUMN_SUBCATDESC, App.context.getResources().getString(R.string.subCategoryIncome301));
	    values.put(COLUMN_CATTYPE, 0);
	    db.insert(TABLE_CATEGORIES, null, values);		    
	    
	    //CATEGORY 3 - SUBCATEGORY 2
	    values.clear();
	    values.put(COLUMN_IDIMAGE, 5);	    
	    values.put(COLUMN_IDCAT, 5);
	    values.put(COLUMN_CATDESC,App.context.getResources().getString(R.string.categoryIncome3));
	    values.put(COLUMN_IDSUBCAT, 2);
	    values.put(COLUMN_SUBCATDESC, App.context.getResources().getString(R.string.subCategoryIncome302));
	    values.put(COLUMN_CATTYPE, 0);
	    db.insert(TABLE_CATEGORIES, null, values);		    
	    
	    //CATEGORY 3 - SUBCATEGORY 3
	    values.clear();
	    values.put(COLUMN_IDIMAGE, 5);	    
	    values.put(COLUMN_IDCAT, 5);
	    values.put(COLUMN_CATDESC,App.context.getResources().getString(R.string.categoryIncome3));
	    values.put(COLUMN_IDSUBCAT, 3);
	    values.put(COLUMN_SUBCATDESC, App.context.getResources().getString(R.string.subCategoryIncome303));
	    values.put(COLUMN_CATTYPE, 0);
	    db.insert(TABLE_CATEGORIES, null, values);		    
	    
	    //CATEGORY 4
	    values.clear();
	    values.put(COLUMN_IDIMAGE, 6);	    
	    values.put(COLUMN_IDCAT, 6);
	    values.put(COLUMN_CATDESC,App.context.getResources().getString(R.string.categoryIncome4));
	    values.put(COLUMN_IDSUBCAT, 0);
	    values.put(COLUMN_CATTYPE, 0);
	    db.insert(TABLE_CATEGORIES, null, values);
	    
	    //CATEGORY 4 - SUBCATEGORY 1
	    values.clear();
	    values.put(COLUMN_IDIMAGE, 6);	    
	    values.put(COLUMN_IDCAT, 6);
	    values.put(COLUMN_CATDESC,App.context.getResources().getString(R.string.categoryIncome4));
	    values.put(COLUMN_IDSUBCAT, 1);
	    values.put(COLUMN_SUBCATDESC, App.context.getResources().getString(R.string.subCategoryIncome401));
	    values.put(COLUMN_CATTYPE, 0);
	    db.insert(TABLE_CATEGORIES, null, values);		    
	    
	    //CATEGORY 4 - SUBCATEGORY 2
	    values.clear();
	    values.put(COLUMN_IDIMAGE, 6);	    
	    values.put(COLUMN_IDCAT, 6);
	    values.put(COLUMN_CATDESC,App.context.getResources().getString(R.string.categoryIncome4));
	    values.put(COLUMN_IDSUBCAT, 2);
	    values.put(COLUMN_SUBCATDESC, App.context.getResources().getString(R.string.subCategoryIncome402));
	    values.put(COLUMN_CATTYPE, 0);
	    db.insert(TABLE_CATEGORIES, null, values);		    
	    
	    //CATEGORY 4 - SUBCATEGORY 3
	    values.clear();
	    values.put(COLUMN_IDIMAGE, 6);	    
	    values.put(COLUMN_IDCAT, 6);
	    values.put(COLUMN_CATDESC,App.context.getResources().getString(R.string.categoryIncome4));
	    values.put(COLUMN_IDSUBCAT, 3);
	    values.put(COLUMN_SUBCATDESC, App.context.getResources().getString(R.string.subCategoryIncome403));
	    values.put(COLUMN_CATTYPE, 0);
	    db.insert(TABLE_CATEGORIES, null, values);		    
	    
	    //CATEGORY 5
	    values.clear();
	    values.put(COLUMN_IDIMAGE, 7);	    
	    values.put(COLUMN_IDCAT, 7);
	    values.put(COLUMN_CATDESC,App.context.getResources().getString(R.string.categoryIncome5));
	    values.put(COLUMN_IDSUBCAT, 0);
	    values.put(COLUMN_CATTYPE, 0);
	    db.insert(TABLE_CATEGORIES, null, values);
	    
	    //CATEGORY 5 - SUBCATEGORY 1
	    values.clear();
	    values.put(COLUMN_IDIMAGE, 7);	    
	    values.put(COLUMN_IDCAT, 7);
	    values.put(COLUMN_CATDESC,App.context.getResources().getString(R.string.categoryIncome5));
	    values.put(COLUMN_IDSUBCAT, 1);
	    values.put(COLUMN_SUBCATDESC, App.context.getResources().getString(R.string.subCategoryIncome501));
	    values.put(COLUMN_CATTYPE, 0);
	    db.insert(TABLE_CATEGORIES, null, values);		    
	    
	    //CATEGORY 5 - SUBCATEGORY 2
	    values.clear();
	    values.put(COLUMN_IDIMAGE, 7);	    
	    values.put(COLUMN_IDCAT, 7);
	    values.put(COLUMN_CATDESC,App.context.getResources().getString(R.string.categoryIncome5));
	    values.put(COLUMN_IDSUBCAT, 2);
	    values.put(COLUMN_SUBCATDESC, App.context.getResources().getString(R.string.subCategoryIncome502));
	    values.put(COLUMN_CATTYPE, 0);
	    db.insert(TABLE_CATEGORIES, null, values);		    
	    
	    //CATEGORY 5 - SUBCATEGORY 3
	    values.clear();
	    values.put(COLUMN_IDIMAGE, 7);	    
	    values.put(COLUMN_IDCAT, 7);
	    values.put(COLUMN_CATDESC,App.context.getResources().getString(R.string.categoryIncome5));
	    values.put(COLUMN_IDSUBCAT, 3);
	    values.put(COLUMN_SUBCATDESC, App.context.getResources().getString(R.string.subCategoryIncome503));
	    values.put(COLUMN_CATTYPE, 0);
	    db.insert(TABLE_CATEGORIES, null, values);		    
	}
	
	// ------------------ Settings table methods ------------------	
	// Insert dummy data check
	public long insertDummyData(Integer inserted) {
		SQLiteDatabase db = this.getWritableDatabase();
		
	    ContentValues values = new ContentValues();
	    
	    values.put(COLUMN_TESTDATA, 1);	    
	    
	    // insert row
	    long transaction_id = db.insert(TABLE_SETTINGS, null, values);
	    db.close(); // Closing database connection
	    
	    return transaction_id;
	}
	
	// Check dummy data
	public boolean checkDummyData() {
		String selectQuery = "SELECT " + COLUMN_TESTDATA + " FROM " + TABLE_SETTINGS;

	    SQLiteDatabase db = this.getWritableDatabase();
	    Cursor cursor = db.rawQuery(selectQuery, null);
	    
	    if (cursor.moveToFirst()) {
	    	if (cursor.getInt(0) == 1) {
	    		db.close();
		    	return true;
	    	} else {
	    		db.close();
		    	return false;
	    	}
	    } else {
	    	db.close();
	    	return false;
	    }
	}	
	
	// ------------------ Help methods ------------------
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
		//Calendar c = Calendar.getInstance();
		//c.setTimeInMillis(timestamp * 1000);
		
		Time time = new Time();
		time.set(timestamp * 1000L);
		return time;
	}
	
	public void insertDummyData() {
		if (checkDummyData() == true) {
			return;
		}

		// Inserting Accounts
		Log.d("Insert: ", "Inserting .."); 
        createAccount(new TableAccount("Cash",1,1,123,1,1));        
        createAccount(new TableAccount("Bank",2,1,155,1,1));   
        createAccount(new TableAccount("Credit Card",3,1,167,1,1));   
        createAccount(new TableAccount("Savings",1,1,-178,1,1));   
		
        // Reading all accounts
        Log.d("Reading: ", "Reading all accounts.."); 
        List<TableAccount> accounts = getAllAccounts();       
         
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
		time.set(1, 1, 2014);

        createTransaction(new TableTransaction(time.toMillis(false),1,1,"test1",1));     
        time.month += 1;       
        createTransaction(new TableTransaction(time.toMillis(false),2,2,"test2",1));  
        time.month -= 5;
        createTransaction(new TableTransaction(time.toMillis(false),3,3,"test3",3));    
        createTransaction(new TableTransaction(time.toMillis(false),4,-4,"test4",1));             
        createTransaction(new TableTransaction(time.toMillis(false),5,5,null,1));  
        createTransaction(new TableTransaction(time.toMillis(false),6,6,null,1));  
        createTransaction(new TableTransaction(time.toMillis(false),7,-7,null,1));  
        time.monthDay += 3;
        createTransaction(new TableTransaction(time.toMillis(false),8,8,null,2));  
        time.monthDay += 1;
        createTransaction(new TableTransaction(time.toMillis(false),9,-9,null,3));  
        createTransaction(new TableTransaction(time.toMillis(false),10,10,null,1));  
        time.monthDay += 1;
        createTransaction(new TableTransaction(time.toMillis(false),11,-11,null,3));  
        time.monthDay += 1;
        createTransaction(new TableTransaction(time.toMillis(false),12,12,null,1));  
        time.monthDay += 1;
        createTransaction(new TableTransaction(time.toMillis(false),13,13,null,3));  
        createTransaction(new TableTransaction(time.toMillis(false),14,-14,null,3));          
        
        // Reading all transaction
        Log.d("Reading: ", "Reading all transactions.."); 
        List<TableTransaction> transactions = getAllTransactions();       
         
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
        
        Log.d("Balance: ",getAllAccountsBalance().toString());
        Log.d("Balance: ",getAssetsBalance().toString());
        Log.d("Balance: ",getLiabilitiesBalance().toString());
        
        // We added dummy data and we don't want to add it again
        insertDummyData(1);
	}
}
