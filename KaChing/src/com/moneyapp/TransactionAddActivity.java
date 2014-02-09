package com.moneyapp;

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
	
	Button buttonSave;
	TextView textViewDate;
	Calendar calendar;
	TextView textViewAccount;
	ImageView imageViewAccount;
	TextView textViewCategory;
	ImageView imageViewCategory;
	
	@Override
	public void onCreate(Bundle bundle) {
		super.onCreate(bundle);
		setContentView(R.layout.transaction_add);
		
		addListenerOnTextViewDate();
		addListenerOnButtonSave();
		addListenerOnTextViewAccount();
		addListenerOnTextViewCategory();
		
		calendar = Calendar.getInstance();
		
		textViewDate = (TextView) findViewById(R.id.transactionDate);
		textViewDate.setText(calendar.get(Calendar.YEAR) + " - " + (calendar.get(Calendar.MONTH) + 1) + " - " + calendar.get(Calendar.DAY_OF_MONTH));
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
		textViewDate = (TextView) findViewById(R.id.transactionAccount);
		
		textViewDate.setOnClickListener(new View.OnClickListener() {
			
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
		textViewDate.setText(calendar.get(Calendar.YEAR) + " - " + (calendar.get(Calendar.MONTH) + 1) + " - " + calendar.get(Calendar.DAY_OF_MONTH));
		//Toast.makeText(getApplicationContext(), c.get(Calendar.YEAR) + " - " + c.get(Calendar.MONTH), Toast.LENGTH_LONG).show();
	}

	public void addListenerOnButtonSave() {
		buttonSave = (Button) findViewById(R.id.buttonSave);
		
		buttonSave.setOnClickListener(new View.OnClickListener() {
 
			@Override
			public void onClick(View arg0) { 	
				EditText transactionAmount = (EditText) findViewById(R.id.transactionAmount);
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
					Toast.makeText(getApplicationContext(), R.string.transaction_account_check, Toast.LENGTH_LONG).show();	
					return;
				}
				
				time.set(calendar.get(Calendar.DAY_OF_MONTH), calendar.get(Calendar.MONTH) + 1, calendar.get(Calendar.YEAR));
				
				MoneyAppDatabaseHelper db = MoneyAppDatabaseHelper.getInstance(null);
				
				db.createTransaction(new TableTransaction(time,(Integer) textViewCategory.getTag(),amount,"test1",(Integer) textViewAccount.getTag())); 
				
				db.close();
				finish();
			}
		});
		
	}
}
