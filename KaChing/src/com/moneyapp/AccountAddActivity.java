package com.moneyapp;

import com.kaching.R;
import com.moneyapp.database.Account;
import com.moneyapp.database.MoneyAppDatabaseHelper;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

public class AccountAddActivity extends Activity {
	Button buttonSave;
	Button buttonCancel;
	Spinner spinner;
	
	@Override
	public void onCreate(Bundle bundle) {
		super.onCreate(bundle);
		setContentView(R.layout.account_add);
		
		addListenerOnButtonSave();
		
		spinner = (Spinner) findViewById(R.id.accountTypeChoice);
		
		spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() 
	    {           
	        @Override
	            public void onItemSelected(AdapterView<?> parent, 
	                View view, int position, long id) {

	                if(position == 0) {
	                	ImageView image = (ImageView) findViewById(R.id.add_icon3);
	                	image.setImageResource(R.drawable.cash);
	                } else if (position == 1) {
	                	ImageView image = (ImageView) findViewById(R.id.add_icon3);
	                	image.setImageResource(R.drawable.bank);	                	
	                } else if (position == 2) {
	                	ImageView image = (ImageView) findViewById(R.id.add_icon3);
	                	image.setImageResource(R.drawable.credit_card);	                	
	                }
	                    
	            }

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {				
			}

	    });
	}
	
	public void addListenerOnButtonSave() {
		 
		buttonSave = (Button) findViewById(R.id.buttonSave);
 
		buttonSave.setOnClickListener(new View.OnClickListener() {
 
			@Override
			public void onClick(View arg0) { 
				EditText accountName = (EditText) findViewById(R.id.accountNameNew);
				EditText accountBalance = (EditText) findViewById(R.id.accountBalanceNew);
				CheckBox accountBalanceCheck = (CheckBox) findViewById(R.id.accountIncludeInBalanceCheck);
				CheckBox accountReportCheck = (CheckBox) findViewById(R.id.accountIncludeInReportsCheck);
				Spinner accountType = (Spinner) findViewById(R.id.accountTypeChoice);
				
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
					db.createAccount(new Account(accountName.getText().toString(),accountType.getSelectedItemPosition() + 1,1,
							Float.parseFloat(accountBalance.getText().toString()),accountBalanceCheckInt,accountReportCheckInt));
					
					db.close();
					finish();
				}
			}
		});
	}
} 