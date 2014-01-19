package com.moneyapp;

import com.kaching.R;
import com.moneyapp.database.Category;
import com.moneyapp.database.MoneyAppDatabaseHelper;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class SubCatAddActivity extends Activity {
	Button buttonSave;
	Button buttonCancel;
	Integer id;
	Category cat;
	
	@Override
	public void onCreate(Bundle bundle) {
		super.onCreate(bundle);
		setContentView(R.layout.subcatadd_activity);
		
		Bundle extras = getIntent().getExtras();
		if (extras != null) {
		    id = extras.getInt("cat_id");
		}
		
		MoneyAppDatabaseHelper db = MoneyAppDatabaseHelper.getInstance(this);
		// getting the category
    	cat = db.getCategory(id);
    	
    	db.close();
    	
		addListenerOnButtonSave();
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

	public void addListenerOnButtonSave() {
		 
		buttonSave = (Button) findViewById(R.id.buttonSave);
		
		final EditText subcatName = (EditText) findViewById(R.id.subcatName);
 
		buttonSave.setOnClickListener(new View.OnClickListener() {
 
			@Override
			public void onClick(View arg0) {
				if (subcatName.getText().toString().trim().equals("")) {
					Toast.makeText(getApplicationContext(), R.string.subcat_name_empty, Toast.LENGTH_LONG).show();	
				} else {
					MoneyAppDatabaseHelper db = MoneyAppDatabaseHelper.getInstance(null);
					
					Category lastSubCat = db.getLastSubCategory(cat.getIdCat());
					
					db.createCategory(new Category(cat.getIdImage(),cat.getIdCat(),cat.getCatDesc(),lastSubCat.getIdSubCat() + 1,subcatName.getText().toString().trim(),cat.getType()));
					
					db.close();
					finish();
				}
			}
		});
	}
}
