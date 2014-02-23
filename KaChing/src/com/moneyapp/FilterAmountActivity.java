package com.moneyapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.TextView;

import com.actionbarsherlock.app.SherlockActivity;
import com.kaching.R;

public class FilterAmountActivity extends SherlockActivity {
	public static final int RESULT_CANCEL=666;
	
	TextView enterAmountText;
	Button buttonSave;	
	Button buttonCancel;
	EditText editTextAmount;
	EditText editTextAmount2;
	int id;	
	
	@Override
	public void onCreate(Bundle bundle) {
		
		super.onCreate(bundle);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		this.setFinishOnTouchOutside(false);
		
		id = 0;
		
		Bundle extras = getIntent().getExtras();
		if (extras != null) {
		    id = extras.getInt("mode");
		}
		
		if (id == TransactionsFilter.AMOUNT_BETWEEN) {
			setContentView(R.layout.amount_between_filter);
		} else {
			setContentView(R.layout.amount_filter);
		}
		
		enterAmountText = (TextView) findViewById(R.id.enterAmountText);
		
		if (id == TransactionsFilter.AMOUNT_EXACT) {
			enterAmountText.setText(getResources().getString(R.string.amount_exact));
		} else if (id == TransactionsFilter.AMOUNT_OVER) {
			enterAmountText.setText(getResources().getString(R.string.amount_over));
		} else if (id == TransactionsFilter.AMOUNT_UNDER) {
			enterAmountText.setText(getResources().getString(R.string.amount_under));
		} else if (id == TransactionsFilter.AMOUNT_BETWEEN) {
			enterAmountText.setText(getResources().getString(R.string.amount_between));
		}
		
		addListenerOnButtonSave();	
		addListenerOnButtonCancel();	
	}	
	
	private void addListenerOnButtonSave() {
		buttonSave = (Button) findViewById(R.id.buttonSave);
		
		buttonSave.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				editTextAmount = (EditText) findViewById(R.id.editTextAmount);
				if (id == TransactionsFilter.AMOUNT_BETWEEN) {
					editTextAmount2 = (EditText) findViewById(R.id.editTextAmount2);
				}
				
				Intent intent = new Intent();
				intent.putExtra("amountEntered",editTextAmount.getText().toString());
				
				if (id == TransactionsFilter.AMOUNT_BETWEEN) {
					intent.putExtra("amountEntered2",editTextAmount2.getText().toString());
				}
				
				setResult(RESULT_OK, intent);       
				
				finish();
			}
		});
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
	    if (keyCode == KeyEvent.KEYCODE_BACK ) {
	    	Intent intent = new Intent();   
			setResult(RESULT_CANCEL,intent);
			finish();
	    }
	    return super.onKeyDown(keyCode, event);
	}
	
	private void addListenerOnButtonCancel() {
		buttonCancel = (Button) findViewById(R.id.buttonCancel);
		
		buttonCancel.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
		    	Intent intent = new Intent();   
				setResult(RESULT_CANCEL,intent);
				finish();
			}
		});
	}
}
