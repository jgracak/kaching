package com.moneyapp;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.kaching.R;
import com.moneyapp.database.Category;
import com.moneyapp.database.MoneyAppDatabaseHelper;

public class SubCatEditActivity extends Activity {
	Button buttonSave;
	Button buttonCancel;
	Integer catId;
	Integer subcatId;
	Category cat;
	EditText subcatText;
	
	@Override
	public void onCreate(Bundle bundle) {
		super.onCreate(bundle);
		setContentView(R.layout.subcatedit_activity);
		
		Bundle extras = getIntent().getExtras();
		if (extras != null) {
			catId = extras.getInt("category_id");
			subcatId = extras.getInt("subcategory_id");
		}
		
		subcatText = (EditText) findViewById(R.id.subcatName);
		
		MoneyAppDatabaseHelper db = MoneyAppDatabaseHelper.getInstance(this);
		
		// getting the category
    	cat = db.getSubCategory(catId, subcatId);
    	
    	subcatText.setText(cat.getSubCatDesc());
    	
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
					
					cat.setSubCatDesc(subcatName.getText().toString().trim());
					
					db.updateSubCategoryName(cat);

					db.close();
					finish();
				}
			}
		});
	}
}
