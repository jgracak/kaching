package com.moneyapp;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.text.format.Time;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.kaching.R;
import com.moneyapp.TransactionsFragment.Transactions;
import com.moneyapp.database.MoneyAppDatabaseHelper;
import com.moneyapp.database.TableCategory;
import com.moneyapp.database.TableImage;
import com.moneyapp.database.TableTransaction;

public class CategoryChooseSubcatAddActivity extends Activity {
	public static final Integer INCOME = 101;
	public static final Integer EXPENSE = 102;
	
	Button buttonSave;
	Integer id;
	Spinner categoryTypeChoice;
	Spinner categoryChoice;
	
	String[] spinnerValues = { "Blur", "NFS", "Burnout","GTA IV", "Racing", };

	@Override
	public void onCreate(Bundle bundle) {
		super.onCreate(bundle);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);		
		setContentView(R.layout.category_choose_subcat_add);

		categoryTypeChoice = (Spinner) findViewById(R.id.categoryTypeChoice);
		categoryChoice = (Spinner) findViewById(R.id.categoryChoice);
		
		categoryTypeChoice.setOnItemSelectedListener(new OnItemSelectedListener() {

            public void onItemSelected(AdapterView<?> parentView,
                    View selectedItemView, int position, long id) {
                // Object item = parentView.getItemAtPosition(position);

                // Depend on first spinner value set adapter to 2nd spinner
            	
	            if(position == 0){
	            	//TODO spajam spinner s income vrijednostima
	            	initSpinner(INCOME);
	            	
	            } else {
	            	//TODO spajam spinner s expense vrijednostima
	            	initSpinner(EXPENSE);
	            } 
            }
            
            public void onNothingSelected(AdapterView<?> arg0) {
            	// do nothing
            }
        });
    	
		addListenerOnButtonSave();
	}
	
	public void initSpinner (Integer mode) {
		if ((mode != INCOME) && (mode != EXPENSE)) {
			return;
		}
		
		MoneyAppDatabaseHelper db = MoneyAppDatabaseHelper.getInstance(this);
		List<TableCategory> categoryList = null;
		
		if (mode == INCOME) {
			categoryList = db.getAllIncomeCategories(MoneyAppDatabaseHelper.GET_ALL_CAT_INCOME);
		} else if (mode == EXPENSE) {
			categoryList = db.getAllExpenseCategories(MoneyAppDatabaseHelper.GET_ALL_CAT_EXPENSE);
		}
		
		if (categoryList != null) {
			ArrayList<Category> categories = new ArrayList<Category>();
			
			for (TableCategory category : categoryList) {
				TableImage image = db.getImage(category.getIdImage());
				categories.add(new Category(category.getCatDesc(), category.getIdCat(),image.getImage()));
			}
			
			categoryChoice.setAdapter(new MyAdapter(getApplicationContext(), R.layout.spinner_category,categories));
		}
		
		db.close();
	}
	
	public void initExpenseSpinner () {
		
	}
	
	class Category {
		private Integer catId;
		private String catTxt;
		private Integer iconRes;
		
		public Integer getCategoryId() {
			return catId;
		}
		
		public String getCategoryTxt() {
			return catTxt;
		}
		
		public Integer getIconRes() {
			return iconRes;
		}
		
		public  void setCategoryId(String catTxt){
			this.catTxt = catTxt;
		}
		
		public void setIconRes(Integer iconRes) {
			this.iconRes = iconRes;
		}
		
		public void setCatId(Integer catId) {
			this.catId = catId;
		}
		
		public Category(String catTxt, Integer catId, Integer iconRes) {
			this.catTxt = catTxt;
			this.catId = catId;
			this.iconRes = iconRes;
		}
	}
	
	private class CategoryViewHolder {
		Integer catId;
		TextView catTxt;
		ImageView catImg;
   }
	
    public class MyAdapter extends ArrayAdapter<Category> {
    	private ArrayList<Category> items;
    	private CategoryViewHolder categoryViewHolder;
    	 
        public MyAdapter(Context ctx, int txtViewResourceId, ArrayList<Category> items) {
            super(ctx, txtViewResourceId, items);
            this.items  = items;
        }
 
        @Override
        public View getDropDownView(int position, View cnvtView, ViewGroup prnt) {
            return getCustomView(position, cnvtView, prnt);
        }
        
        @Override
        public View getView(int pos, View cnvtView, ViewGroup prnt) {
            return getCustomView(pos, cnvtView, prnt);
        }
        
        public View getCustomView(int position, View convertView,
                ViewGroup parent) {
        	View v = convertView;
        	
        	Category category = items.get(position);
        	
        	if (v == null) {
        		LayoutInflater inflater = getLayoutInflater();
                v = inflater.inflate(R.layout.spinner_category, parent,false);
                categoryViewHolder = new CategoryViewHolder();
                categoryViewHolder.catTxt = (TextView)v.findViewById(R.id.categoryName);
                categoryViewHolder.catImg = (ImageView)v.findViewById(R.id.imageCategory);
                v.setTag(categoryViewHolder);
        	} else categoryViewHolder = (CategoryViewHolder)v.getTag(); 
        	
        	categoryViewHolder.catId = category.catId;
        	categoryViewHolder.catTxt.setText(category.catTxt);
        	categoryViewHolder.catTxt.setTag(category.catTxt);
        	
        	categoryViewHolder.catImg.setImageResource(category.iconRes);
            
            return v;
        }
    }

	public void addListenerOnButtonSave() {
		 
		buttonSave = (Button) findViewById(R.id.buttonSave);

		buttonSave.setOnClickListener(new View.OnClickListener() {
 
			@Override
			public void onClick(View arg0) {
				EditText subcatName = (EditText) findViewById(R.id.subcatName);
				
				Spinner categoryChoiceSpinner = (Spinner) findViewById(R.id.categoryChoice);
				
				Category categoryViewHolder = (Category) categoryChoiceSpinner.getSelectedItem();
				
				if (subcatName.getText().toString().trim().equals("")) {
					Toast.makeText(getApplicationContext(), R.string.subcat_name_empty, Toast.LENGTH_LONG).show();	
				} else {				
					MoneyAppDatabaseHelper db = MoneyAppDatabaseHelper.getInstance(null);
					
					if (db.getSubCategoryByName(subcatName.getText().toString().trim(), categoryViewHolder.catId) == true) {
						Toast.makeText(getApplicationContext(), R.string.subcategory_name_exists, Toast.LENGTH_LONG).show();		
					} else {
						TableCategory lastSubCat = db.getLastSubCategory(categoryViewHolder.catId);
						
						db.createCategory(new TableCategory(lastSubCat.getIdImage(),lastSubCat.getIdCat(),
											lastSubCat.getCatDesc(),lastSubCat.getIdSubCat() + 1,subcatName.getText().toString().trim(),lastSubCat.getType()));
						
						db.close();
						finish();
					}
				}
				
			}
		});
	}
}
