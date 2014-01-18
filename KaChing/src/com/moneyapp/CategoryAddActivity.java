package com.moneyapp;

import java.util.ArrayList;
import java.util.List;

import com.kaching.R;
import com.moneyapp.TabAccountListFragment.AccountAdapter;
import com.moneyapp.TabAccountListFragment.Accounts;
import com.moneyapp.database.Account;
import com.moneyapp.database.Category;
import com.moneyapp.database.Image;
import com.moneyapp.database.MoneyAppDatabaseHelper;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ListAdapter;
import android.widget.Spinner;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.Toast;

public class CategoryAddActivity extends Activity {
	int id;
	GridView gridView;
	ImageView imageView;
	Button buttonSave;
	Button buttonCancel;	
	Spinner spinner;	
	
	@Override
	public void onCreate(Bundle bundle) {
		
		super.onCreate(bundle);
		setContentView(R.layout.category_add);

		id = 0;
		
		Bundle extras = getIntent().getExtras();
		if (extras != null) {
		    id = extras.getInt("current_tab");
		}
		
		gridView = (GridView) findViewById(R.id.categoryGridView);
		imageView = (ImageView) findViewById(R.id.categoryItemImage);
		spinner = (Spinner) findViewById(R.id.categoryTypeChoiceEdit);	
		
		spinner.setSelection(id);
		spinner.setTag(id);
		
        MoneyAppDatabaseHelper db = MoneyAppDatabaseHelper.getInstance(this);
        
        List<Image> imageList = db.getAllImagesType(0);
        
		gridView.setAdapter(new CatAddImageAdapter(this, imageList));
		
		gridView.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View v,
				int position, long id) {
				
				ImageView imageViewSource = (ImageView) v.findViewById(R.id.grid_item_image);
				
				imageView.setImageResource((Integer)imageViewSource.getTag());
				imageView.setTag((Integer)imageViewSource.getTag());

			}
		});
		
		addListenerOnButtonSave();
		addListenerOnButtonCancel();	
	}
	
	
	private void addListenerOnButtonSave() {
		buttonSave = (Button) findViewById(R.id.buttonCatSave);
		
		buttonSave.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View arg0) { 
				
				EditText categoryName = (EditText) findViewById(R.id.categoryNameNew);
				ImageView categoryImage = (ImageView) findViewById(R.id.categoryItemImage);
				Spinner categoryType = (Spinner) findViewById(R.id.categoryTypeChoiceEdit);					
				
				if (categoryName.getText().toString().trim().equals("")) {
					Toast.makeText(getApplicationContext(), R.string.category_name_blank, Toast.LENGTH_LONG).show();
				} else {
					MoneyAppDatabaseHelper db = MoneyAppDatabaseHelper.getInstance(null);
					
					// find if name already exists, if exists make toast message
					if (db.getCategoryName(categoryName.getText().toString().trim()) == true) {
						Toast.makeText(getApplicationContext(), R.string.category_name_exists, Toast.LENGTH_LONG).show();	
					} else {
						// find the last category id for income/expense
						int catId = db.getLastCategoryId();
						
						db.createCategory(new Category(db.getImageIdByRef((Integer) categoryImage.getTag()),
								catId + 1,categoryName.getText().toString(),0,null,categoryType.getSelectedItemPosition()));
					}
					
					db.close();
				}
				
				finish();
			}
		});
		
	}
	
	private void addListenerOnButtonCancel() {
		buttonCancel = (Button) findViewById(R.id.buttonCatCancel);
		 
		buttonCancel.setOnClickListener(new View.OnClickListener() {
 
			@Override
			public void onClick(View arg0) {
				finish();
			}
		});
	}
}
