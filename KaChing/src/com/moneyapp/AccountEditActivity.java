package com.moneyapp;

import com.kaching.R;
import com.moneyapp.database.Account;
import com.moneyapp.database.MoneyAppDatabaseHelper;

import android.app.Activity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

public class AccountEditActivity extends Activity {
	Button buttonSave;
	Button buttonDelete;
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
			//TODO
			// Dohvatit account i ispisat ga u kontrole
			// Preko ID-a æe se updejtat
			Account account = db.getAccount(id);
			
			EditText accountName = (EditText) findViewById(R.id.accountNameEditNew);
			accountName.setText(account.getDescription());
		}
		
		addListenerOnButtonSave();
		addListenerOnButtonDelete();		
	}

	private void addListenerOnButtonDelete() {
		// TODO Auto-generated method stub
		
	}

	private void addListenerOnButtonSave() {
		// TODO Auto-generated method stub
		
	}	
}
