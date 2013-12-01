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

public class AccountEditActivity extends Activity {
	Button buttonSave;
	Button buttonDelete;
	Button buttonCancel;
	Integer id;
	
	@Override
	public void onCreate(Bundle bundle) {
		super.onCreate(bundle);
		setContentView(R.layout.account_edit);
		
		id = 0;
		
		Bundle extras = getIntent().getExtras();
		if (extras != null) {
		    id = extras.getInt("account_id");
		}
		
		MoneyAppDatabaseHelper db = MoneyAppDatabaseHelper.getInstance(null);
		if (id != 0) {
			Account account = db.getAccount(id);
			
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
		addListenerOnButtonCancel();			
	}

	private void addListenerOnButtonCancel() {
		buttonCancel = (Button) findViewById(R.id.buttonCancel);
		 
		buttonCancel.setOnClickListener(new View.OnClickListener() {
 
			@Override
			public void onClick(View arg0) {
				finish();
			}
		});
	}

	private void addListenerOnButtonDelete() {
		buttonDelete = (Button) findViewById(R.id.buttonDelete);
		 
		buttonDelete.setOnClickListener(new View.OnClickListener() {
 
			@Override
			public void onClick(View arg0) {
				MoneyAppDatabaseHelper db = MoneyAppDatabaseHelper.getInstance(null);
				
				Account account = new Account(id);
				
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
					Account account = new Account(id,accountName.getText().toString(),accountType.getSelectedItemPosition()+1,
							1,Float.parseFloat(accountBalance.getText().toString()),accountBalanceCheckInt,accountReportCheckInt);
					
					db.updateAccount(account);
					
					db.close();
					
					finish();
				}
			}
		});
	}	
}
