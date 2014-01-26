package com.moneyapp;

import com.actionbarsherlock.app.SherlockActivity;
import com.kaching.R;
import com.moneyapp.database.TableAccount;
import com.moneyapp.database.MoneyAppDatabaseHelper;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;
import android.widget.TextView;

public class AccountEditActivity extends SherlockActivity {
	Button buttonSave;
	Button buttonDelete;
	Spinner spinner;
	Integer id;
	
	@Override
	public void onCreate(Bundle bundle) {
		super.onCreate(bundle);
		setContentView(R.layout.account_edit);
		
		spinner = (Spinner) findViewById(R.id.accountTypeChoiceEdit);
		
		spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() 
	    {           
	        @Override
	            public void onItemSelected(AdapterView<?> parent, 
	                View view, int position, long id) {
        			
	        		((TextView) parent.getChildAt(0)).setTextSize(20);
	        		
	                if(position == 0) {
	                	ImageView image = (ImageView) findViewById(R.id.edit_icon3);
	                	image.setImageResource(R.drawable.cash);
	                } else if (position == 1) {
	                	ImageView image = (ImageView) findViewById(R.id.edit_icon3);
	                	image.setImageResource(R.drawable.bank);	                	
	                } else if (position == 2) {
	                	ImageView image = (ImageView) findViewById(R.id.edit_icon3);
	                	image.setImageResource(R.drawable.credit_card);	                	
	                }
	                    
	            }
	        @Override
			public void onNothingSelected(AdapterView<?> arg0) {				
			}

	    });
		
		id = 0;
		
		Bundle extras = getIntent().getExtras();
		if (extras != null) {
		    id = extras.getInt("account_id");
		}
		
		MoneyAppDatabaseHelper db = MoneyAppDatabaseHelper.getInstance(null);
		if (id != 0) {
			TableAccount account = db.getAccount(id);
			
			EditText accountName = (EditText) findViewById(R.id.accountNameEditNew);
			accountName.setText(account.getDescription());
			Spinner accountType = (Spinner) findViewById(R.id.accountTypeChoiceEdit);
			accountType.setSelection(account.getType()-1);
			EditText accountBalance = (EditText) findViewById(R.id.accountBalanceNewEdit);
			accountBalance.setText(String.valueOf(account.getStartingBalance()));
			CheckBox accountBalanceCheck = (CheckBox) findViewById(R.id.accountIncludeInBalanceCheckEdit);
			if (account.getExcludeFromBalance() != 0){
				accountBalanceCheck.setChecked(Boolean.TRUE);
			}else {
				accountBalanceCheck.setChecked(Boolean.FALSE);
			}
			CheckBox accountReportCheck = (CheckBox) findViewById(R.id.accountIncludeInReportsCheckEdit);
			if (account.getExcludeFromReports() != 0){
				accountReportCheck.setChecked(Boolean.TRUE);
			} else {
				accountReportCheck.setChecked(Boolean.FALSE);
			}
		}
		
		addListenerOnButtonSave();
		addListenerOnButtonDelete();			
	}

	private void addListenerOnButtonDelete() {
		buttonDelete = (Button) findViewById(R.id.buttonDelete);
		 
		buttonDelete.setOnClickListener(new View.OnClickListener() {
 
			@Override
			public void onClick(View arg0) {
				MoneyAppDatabaseHelper db = MoneyAppDatabaseHelper.getInstance(null);
				
				TableAccount account = new TableAccount(id);
				
				db.deleteAccount(account);
				
				db.close();
				
				finish();
			}
		});
		
	}

	private void addListenerOnButtonSave() {
		buttonSave = (Button) findViewById(R.id.buttonSave);
		 
		buttonSave.setOnClickListener(new View.OnClickListener() {
 
			@Override
			public void onClick(View arg0) { 
				EditText accountName = (EditText) findViewById(R.id.accountNameEditNew);
				EditText accountBalance = (EditText) findViewById(R.id.accountBalanceNewEdit);
				CheckBox accountBalanceCheck = (CheckBox) findViewById(R.id.accountIncludeInBalanceCheckEdit);
				CheckBox accountReportCheck = (CheckBox) findViewById(R.id.accountIncludeInReportsCheckEdit);
				Spinner accountType = (Spinner) findViewById(R.id.accountTypeChoiceEdit);
				
				int accountBalanceCheckInt = 0;
				int accountReportCheckInt = 0;
				
				if (accountBalanceCheck.isChecked()) {
					accountBalanceCheckInt = 1;
				}
				
				if (accountReportCheck.isChecked()) {
					accountReportCheckInt = 1;
				}
				
				if (accountBalance.getText().toString().equals("")){
					accountBalance.setText("0");
				}
				
				if (accountName.getText().toString().equals("")) {
					Toast.makeText(getApplicationContext(), R.string.account_name_blank, Toast.LENGTH_LONG).show();
				} else {
					MoneyAppDatabaseHelper db = MoneyAppDatabaseHelper.getInstance(null);
					TableAccount account = new TableAccount(id,accountName.getText().toString(),accountType.getSelectedItemPosition()+1,
							1,Float.parseFloat(accountBalance.getText().toString()),accountBalanceCheckInt,accountReportCheckInt);
					
					db.updateAccount(account);
					
					db.close();
					
					finish();
				}
			}
		});
	}	
}
