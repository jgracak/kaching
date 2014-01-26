package com.moneyapp;

import java.util.List;

import com.actionbarsherlock.app.SherlockActivity;
import com.kaching.R;
import com.moneyapp.database.TableCategory;
import com.moneyapp.database.TableImage;
import com.moneyapp.database.MoneyAppDatabaseHelper;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Spinner;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.Toast;

public class CategoryAddActivity extends SherlockActivity {
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
        
        List<TableImage> imageList = db.getAllImagesType(0);
        
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
						
						db.createCategory(new TableCategory(db.getImageIdByRef((Integer) categoryImage.getTag()),
								catId + 1,categoryName.getText().toString(),0,null,categoryType.getSelectedItemPosition()));
					}
					
					db.close();
				}
				
				finish();
			}
		});
		
	}
}
