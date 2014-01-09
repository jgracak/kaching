package com.moneyapp;

import com.kaching.R;
import com.moneyapp.database.Account;
import com.moneyapp.database.MoneyAppDatabaseHelper;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

public class AccountAddActivity extends Activity {
	Button buttonSave;
	Button buttonCancel;
	
	@Override
	public void onCreate(Bundle bundle) {
		super.onCreate(bundle);
		setContentView(R.layout.account_add);
		
	//	addListenerOnButtonCancel();
		addListenerOnButtonSave();		
	}
	
/*	public void addListenerOnButtonCancel() {
		 
		buttonCancel = (Button) findViewById(R.id.buttonCancel);
 
		buttonCancel.setOnClickListener(new View.OnClickListener() {
 
			@Override
			public void onClick(View arg0) {
				finish();
			}
		});
	}*/
	
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