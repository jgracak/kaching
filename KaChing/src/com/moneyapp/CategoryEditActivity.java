package com.moneyapp;

import java.util.List;

import com.actionbarsherlock.app.SherlockActivity;
import com.kaching.R;
import com.moneyapp.database.TableCategory;
import com.moneyapp.database.TableImage;
import com.moneyapp.database.MoneyAppDatabaseHelper;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class CategoryEditActivity extends SherlockActivity {
	int id;
	GridView gridView;
	ImageView imageView;
	TextView textView;
	Button buttonSave;
	Button buttonCancel;
	TableCategory cat;
	Spinner spinner;
	
	@Override
	public void onCreate(Bundle bundle) {
		super.onCreate(bundle);
		setContentView(R.layout.category_edit);
		
		gridView = (GridView) findViewById(R.id.categoryGridView);
		imageView = (ImageView) findViewById(R.id.categoryItemImage);
		textView = (TextView) findViewById(R.id.categoryNameNew);
		spinner = (Spinner) findViewById(R.id.categoryTypeChoiceEdit);		
		
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
		
		id = 0;
		
		Bundle extras = getIntent().getExtras();
		if (extras != null) {
		    id = extras.getInt("category_id");
		}
		
		// getting the category
    	cat = db.getCategory(id);
    	
    	TableImage image = db.getImage(cat.getIdImage());
    	
    	imageView.setImageResource(image.getImage());
    	imageView.setTag(image.getImage());
    	
    	textView.setText(cat.getCatDesc());
    	
    	spinner.setSelection(cat.getType());
    	spinner.setTag(cat.getType());
		
    	db.close();
    	
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
					
					// get Category by name
					cat.setCatDesc(categoryName.getText().toString().trim());
					cat.setIdImage(db.getImageIdByRef((Integer) categoryImage.getTag()));
					cat.setType(categoryType.getSelectedItemPosition());
					
					db.updateCategory(cat);
					
					db.close();
					
					finish();
				}
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
