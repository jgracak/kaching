package com.moneyapp;

import java.util.List;

import com.kaching.R;
import com.moneyapp.database.Category;
import com.moneyapp.database.Image;
import com.moneyapp.database.MoneyAppDatabaseHelper;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.AdapterView.OnItemClickListener;

public class CategoryAddActivity extends Activity {
	Integer id;
	GridView gridView;
	
	static final String[] numbers = new String[] { 
		"A", "B", "C", "D", "E",
		"F", "G", "H", "I", "J",
		"K", "L", "M", "N", "O",
		"P", "Q", "R", "S", "T",
		"U", "V", "W", "X", "Y", "Z"};
	
	static final Integer[] images = new Integer[] {
		R.drawable.accounts
	};
	
	@Override
	public void onCreate(Bundle bundle) {
		
		super.onCreate(bundle);
		setContentView(R.layout.category_add);
		
		gridView = (GridView) findViewById(R.id.categoryGridView);
		
        MoneyAppDatabaseHelper db = MoneyAppDatabaseHelper.getInstance(this);
        
        List<Image> imageList = db.getAllImagesType(0);
        
		gridView.setAdapter(new CatAddImageAdapter(this, imageList));
		
		/*
		gridView.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View v,
				int position, long id) {
			   Toast.makeText(getApplicationContext(),
				((TextView) v).getText(), Toast.LENGTH_SHORT).show();
			}
		});
		*/
		
		addListenerOnButtonSave();
		addListenerOnButtonCancel();	
	}
	
	
	private void addListenerOnButtonSave() {
		id = 0;
		
		Bundle extras = getIntent().getExtras();
		if (extras != null) {
		    id = extras.getInt("current_tab");
		}
		
		if (id == 0) {
			//Income
		}
		else {
			//Expense
		}
	}
	
	private void addListenerOnButtonCancel() {
		
	}
}
