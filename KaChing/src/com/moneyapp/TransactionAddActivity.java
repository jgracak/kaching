package com.moneyapp;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import com.actionbarsherlock.app.SherlockDialogFragment;
import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.app.SherlockListFragment;
import com.kaching.R;
import com.moneyapp.database.MoneyAppDatabaseHelper;
import com.moneyapp.database.TableAccount;
import com.moneyapp.database.TableCategory;
import com.moneyapp.database.TableImage;
import com.moneyapp.database.TableTransaction;

import android.accounts.Account;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.text.format.Time;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class TransactionAddActivity extends SherlockFragmentActivity implements OnFragmentClickListener  {
	public static final int DATE_PICKER_ACTION = 100;
	private static final int ACCOUNT_REQ_ID = 666;	
	private static final int CATEGORY_REQ_ID = 777;	
	
	Integer transactionId;
	Integer mode;
	Button buttonSave;
	TextView textViewDate;
	Calendar calendar;
	TextView textViewAccount;
	ImageView imageViewAccount;
	TextView textViewCategory;
	ImageView imageViewCategory;
	TableTransaction transaction;	
	EditText textViewNote;
	
	@Override
	public void onCreate(Bundle bundle) {
		super.onCreate(bundle);
		
		transactionId = 0;
		
		Bundle extras = getIntent().getExtras();
		if (extras != null) {
			transactionId = extras.getInt("transaction_id");
			mode = extras.getInt("mode");
		}
		
		if (transactionId == 0) {
			setContentView(R.layout.transaction_add);
		} else if (mode == TransactionsFragment.EDIT_MODE) {
			setContentView(R.layout.transaction_edit);
		} else {
			setContentView(R.layout.transaction_copy);
		}
		
		if (transactionId != 0) {
			MoneyAppDatabaseHelper db = MoneyAppDatabaseHelper.getInstance(null);
			
			transaction = db.getTransaction(transactionId);
			
			// Set amount
			EditText transactionAmount = (EditText) findViewById(R.id.transactionAmount);
			transactionAmount.setText(String.valueOf(transaction.getAmount()));
			
			// Set account
			textViewAccount = (TextView) findViewById(R.id.transactionAccount);
	    	imageViewAccount = (ImageView) findViewById(R.id.addIconAccount);

	    	TableAccount account = db.getAccount(transaction.getIdAccount());
	    	
	    	if (account != null) {
	    		textViewAccount.setText(account.getDescription());
	    		textViewAccount.setTag(account.getId());
	    		
	    		TableImage image = db.getImage(account.getType());
	    		imageViewAccount.setImageResource(image.getImage());
	    	}
	    	
			// Set category
	    	textViewCategory = (TextView) findViewById(R.id.transactionCategory);
	  	    imageViewCategory = (ImageView) findViewById(R.id.addIconCategory);
	  	  
	  	    TableCategory category = db.getCategory(transaction.getIdCategory());
	  	  
	  	    if (category != null) {
	  	    	if (category.getIdSubCat() == 0) {
	  	    		textViewCategory.setText(category.getCatDesc());
	  	    	} else {
	  	    		textViewCategory.setText(category.getCatDesc() + " > " + category.getSubCatDesc());
	  	    	}
	  	    	textViewCategory.setTag(category.getId());
	  		  
	  	    	TableImage image = db.getImage(category.getIdImage());
	  	    	imageViewCategory.setImageResource(image.getImage());
	  	    }
	  	    
			// Set date
	  	    Time time = new Time();
	  	    time.set(transaction.getTransDate());
	  	    
	  	    calendar = Calendar.getInstance();
	  	    calendar.set(Calendar.YEAR, time.year);
	  	    calendar.set(Calendar.MONTH, time.month);
	  	    calendar.set(Calendar.DAY_OF_MONTH, time.monthDay);
	  	    
	  	    textViewDate = (TextView) findViewById(R.id.transactionDate);
	  	    
	  	    Date date = new Date(transaction.getTransDate());
	  	    SimpleDateFormat fmtOut = new SimpleDateFormat("dd-MM-yyyy");
	  	  
			textViewDate.setText(fmtOut.format(date));
			textViewDate.setTag(transaction.getTransDate());
			
			// Set note
			textViewNote = (EditText) findViewById(R.id.transactionNote);
			textViewNote.setText(transaction.getNote());
			
			db.close();
		} else {
			calendar = Calendar.getInstance();
			
			textViewDate = (TextView) findViewById(R.id.transactionDate);
			
			textViewDate.setText(calendar.get(Calendar.DAY_OF_MONTH) + "-" + (calendar.get(Calendar.MONTH) + 1) + "-" + calendar.get(Calendar.YEAR));
			
			Time time = new Time();
			time.set(calendar.get(Calendar.DAY_OF_MONTH),calendar.get(Calendar.MONTH),calendar.get(Calendar.YEAR));
			
			textViewDate.setTag(time.toMillis(false));
		}
		
		addListenerOnTextViewDate();
		addListenerOnButtonSave();
		addListenerOnTextViewAccount();
		addListenerOnTextViewCategory();
	}
	
	private void addListenerOnTextViewCategory() {
		textViewCategory = (TextView) findViewById(R.id.transactionCategory);
		
		textViewCategory.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
        		Intent i = new Intent(getApplicationContext(), CategoryChooseActivity.class);
        		startActivityForResult(i, CATEGORY_REQ_ID);
			}
		});
	}

	private void addListenerOnTextViewAccount() {
		textViewAccount = (TextView) findViewById(R.id.transactionAccount);
		
		textViewAccount.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
        		Intent i = new Intent(getApplicationContext(), AccountChooseActivity.class);
        		startActivityForResult(i, ACCOUNT_REQ_ID);
			}
		});
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode,
	          Intent data) {
	      if (requestCode == ACCOUNT_REQ_ID) {
	          if (resultCode == RESULT_OK) {
	        	
	        	textViewAccount = (TextView) findViewById(R.id.transactionAccount);
	        	imageViewAccount = (ImageView) findViewById(R.id.addIconAccount);

	        	MoneyAppDatabaseHelper db = MoneyAppDatabaseHelper.getInstance(null);
	        	TableAccount account = db.getAccount(data.getIntExtra("accountId", 0));
	        	
	        	if (account != null) {
	        		textViewAccount.setText(account.getDescription());
	        		textViewAccount.setTag(account.getId());
	        		
	        		TableImage image = db.getImage(account.getType());
	        		imageViewAccount.setImageResource(image.getImage());
	        		
	        	}
	        	
	        	db.close();
	          }
	      } else if (requestCode == CATEGORY_REQ_ID) {
	    	  if (resultCode == RESULT_OK) {
		    	  textViewCategory = (TextView) findViewById(R.id.transactionCategory);
		    	  imageViewCategory = (ImageView) findViewById(R.id.addIconCategory);
		    	  
		    	  MoneyAppDatabaseHelper db = MoneyAppDatabaseHelper.getInstance(null);
		    	  TableCategory category = db.getCategory(data.getIntExtra("categoryId", 0));
		    	  
		    	  if (category != null) {
		    		  if (category.getIdSubCat() == 0) {
		    			  textViewCategory.setText(category.getCatDesc());
		    		  } else {
		    			  textViewCategory.setText(category.getCatDesc() + " > " + category.getSubCatDesc());
		    		  }
		    		  textViewCategory.setTag(category.getId());
		    		  
		    		  TableImage image = db.getImage(category.getIdImage());
		    		  imageViewCategory.setImageResource(image.getImage());
		    	  }
		    	  
		    	  db.close();
	    	  }
	      }
	}

	private void addListenerOnTextViewDate() {
		textViewDate = (TextView) findViewById(R.id.transactionDate);
		
		textViewDate.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                // Create and show the dialog.
                DatePickerFragment newFragment = new DatePickerFragment();
                
            	Bundle bundle = new Bundle();
                bundle.putLong("longDate", (Long)textViewDate.getTag());
                newFragment.setArguments(bundle);

                newFragment.show(ft, "datePicker");				
			}
		});
		
	}
	
	public void showDatePicker(View v) {
		SherlockDialogFragment newFragment = new DatePickerFragment();
	    newFragment.show(getSupportFragmentManager(), "datePicker");
	}

	@Override
	public void onFragmentClick(int action, Object object) {
		calendar = (Calendar) object;
		textViewDate = (TextView) findViewById(R.id.transactionDate);
		textViewDate.setText(calendar.get(Calendar.DAY_OF_MONTH) + "-" + (calendar.get(Calendar.MONTH) + 1) + "-" + calendar.get(Calendar.YEAR));
		
		Time time = new Time();
		time.set(calendar.get(Calendar.DAY_OF_MONTH),calendar.get(Calendar.MONTH),calendar.get(Calendar.YEAR));
		
		textViewDate.setTag(time.toMillis(false));
	}

	public void addListenerOnButtonSave() {
		buttonSave = (Button) findViewById(R.id.buttonSave);
		
		buttonSave.setOnClickListener(new View.OnClickListener() {
 
			@Override
			public void onClick(View arg0) { 	
				EditText transactionAmount = (EditText) findViewById(R.id.transactionAmount);
				textViewNote = (EditText) findViewById(R.id.transactionNote);
				textViewAccount = (TextView) findViewById(R.id.transactionAccount);
				float amount;	
				
				//Amount check
				try {
					amount = Float.parseFloat(transactionAmount.getText().toString());
				} catch (Exception e) {
					Toast.makeText(getApplicationContext(), R.string.transaction_amount_check, Toast.LENGTH_LONG).show();	
					return;
				}
				
				//Category check
				if (textViewCategory.getText().toString().equals("")) {
					Toast.makeText(getApplicationContext(), R.string.transaction_category_check, Toast.LENGTH_LONG).show();	
					return;
				}
					
				//Account check
				if (textViewAccount.getText().toString().equals("")) {
					Toast.makeText(getApplicationContext(), R.string.transaction_account_check, Toast.LENGTH_LONG).show();	
					return;
				}
				
				//Date check
				Time time = new Time();
				if (calendar == null) {
					Toast.makeText(getApplicationContext(), R.string.transaction_date_check, Toast.LENGTH_LONG).show();	
					return;
				}
				
				time.set(calendar.get(Calendar.DAY_OF_MONTH), calendar.get(Calendar.MONTH), calendar.get(Calendar.YEAR));
				
				MoneyAppDatabaseHelper db = MoneyAppDatabaseHelper.getInstance(null);
				
				if (transactionId == 0) {
					db.createTransaction(new TableTransaction((Long)textViewDate.getTag(),(Integer) textViewCategory.getTag(),amount,
							textViewNote.getText().toString(),(Integer) textViewAccount.getTag())); 
				} else if (mode == TransactionsFragment.EDIT_MODE) {
					db.updateTransaction(new TableTransaction(transactionId,(Long)textViewDate.getTag(),(Integer) textViewCategory.getTag(),amount,
							textViewNote.getText().toString(),(Integer) textViewAccount.getTag())); 
				} else {
					db.createTransaction(new TableTransaction((Long)textViewDate.getTag(),(Integer) textViewCategory.getTag(),amount,
							textViewNote.getText().toString(),(Integer) textViewAccount.getTag())); 
				}
				
				db.close();
				finish();
			}
		});
		
	}
}
