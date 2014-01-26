package com.moneyapp;

import java.util.ArrayList;
import java.util.List;

import com.actionbarsherlock.app.SherlockListActivity;
import com.kaching.R;
import com.moneyapp.database.TableCategory;
import com.moneyapp.database.TableImage;
import com.moneyapp.database.MoneyAppDatabaseHelper;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.PopupMenu.OnMenuItemClickListener;

public class SubCatActivity extends SherlockListActivity  {
	int id;	
	ImageView imageView;
	TextView textView;
	TableCategory cat;
	ImageButton buttonAdd;
	
	@Override
	public void onCreate(Bundle bundle) {
		super.onCreate(bundle);
		setContentView(R.layout.subcategory_add);
		
		imageView = (ImageView) findViewById(R.id.categoryItemImage);
		textView = (TextView) findViewById(R.id.subcat_cat);
		
		MoneyAppDatabaseHelper db = MoneyAppDatabaseHelper.getInstance(this);
		
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
    	
    	db.close();
    	
    	SetSubCategoryList();
    	
    	addListenerOnButtonAdd();
	}
	
	@Override	
	public void onResume()
	{
	   super.onResume();
	   
	   SetSubCategoryList();
	}
	
	private void addListenerOnButtonAdd() {
		buttonAdd = (ImageButton) findViewById(R.id.buttonAdd);
		
		buttonAdd.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View arg0) { 
				
				Intent i = new Intent(getApplicationContext(), SubCatAddActivity.class);
        		i.putExtra("cat_id",cat.getId());
        		startActivity(i);
        		
			}
		});
	}
	
	public void callDialog(SubCategoryViewHolder subCategoryHolder) {
		
		final SubCategoryViewHolder subCategoryHolder2 = subCategoryHolder;
		
		DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
		    @Override
		    public void onClick(DialogInterface dialog, int which) {
		        switch (which){
		        case DialogInterface.BUTTON_POSITIVE:
		        	MoneyAppDatabaseHelper db = MoneyAppDatabaseHelper.getInstance(null);
		        	
		        	// delete transactions
		        	db.deleteTransactionSubCat(db.getSubCategory(subCategoryHolder2.catId, subCategoryHolder2.subCatId));
		        	
		        	// delete subcategory
		        	db.deleteCategory(db.getSubCategory(subCategoryHolder2.catId, subCategoryHolder2.subCatId));	
		        	
		        	db.close();
		        	SetSubCategoryList();
		            break;
		        case DialogInterface.BUTTON_NEGATIVE:
		            //No button clicked
		            break;
		        }
		    }
		};
		
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setMessage("Do you want to delete " + subCategoryHolder.description.getText().toString().trim() + 
				" subcategory? All transaction linked to this category will be deleted.").setPositiveButton("Yes", dialogClickListener)
		    .setNegativeButton("No", dialogClickListener).show();
	}
	
	private void SetSubCategoryList() {
	       // Reading all subcategories
		
	       MoneyAppDatabaseHelper db = MoneyAppDatabaseHelper.getInstance(null);
	       List<TableCategory> categories = db.getAllSubCategories(cat);    
	       ArrayList<Subcategories> SubcategoryList = new ArrayList<Subcategories>();
	        
	       for (TableCategory category : categories) {
	    	   SubcategoryList.add(new Subcategories(category.getIdCat(),category.getIdSubCat(),category.getSubCatDesc(),0));
	       }
	       
	       ListAdapter listAdapter = new SubCategoryAdapter(this, R.layout.subcategory_list_item, SubcategoryList);
	       
	       setListAdapter(listAdapter);
	       
	       ListView v = new ListView(getApplicationContext());
		    v = getListView();
		    
		    v.setOnItemClickListener(new OnItemClickListener() {
	            @Override
	            public void onItemClick(AdapterView<?> parent, View view, int position,
	                    long id) {	            	
	            	final SubCategoryViewHolder subCategoryHolder = (SubCategoryViewHolder) view.getTag();
	            	
	            	PopupMenu popupMenu = new PopupMenu(getApplicationContext(),view);
					
					popupMenu.getMenuInflater().inflate(R.menu.menu_subcat,popupMenu.getMenu());
					
					popupMenu.setOnMenuItemClickListener(new OnMenuItemClickListener() {

						@Override
						public boolean onMenuItemClick(MenuItem arg0) {
							
							if (arg0.getTitle().equals(getResources().getString(R.string.cnt_menu_edit))) {
					    		Intent i = new Intent(getApplicationContext(), SubCatEditActivity.class);
					    		i.putExtra("category_id",subCategoryHolder.catId);
					    		i.putExtra("subcategory_id", subCategoryHolder.subCatId);
					    		startActivity(i);
					        	return true;
					        	
							} else if (arg0.getTitle().equals(getResources().getString(R.string.cnt_menu_delete))) {							
								callDialog(subCategoryHolder);
								return true;
							}

							return false;
						}
					});
					popupMenu.show();
	            }
	        });
	}
	
   private class SubCategoryViewHolder {
	   Integer catId;	   
	   Integer subCatId;
       TextView description;
       ImageView image;
   }	
	
   public class SubCategoryAdapter extends ArrayAdapter<Subcategories> {
       private ArrayList<Subcategories> items;
       private SubCategoryViewHolder subCategoryHolder;

       public SubCategoryAdapter(Context context, int tvResId, ArrayList<Subcategories> items) {
           super(context, tvResId, items);
           this.items = items;
       }

       @Override
       public View getView(int pos, View convertView, ViewGroup parent) {
           View v = convertView;
           
           Subcategories  subCategory = items.get(pos);
           
           if (v == null) {
               LayoutInflater vi = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
               v = vi.inflate(R.layout.subcategory_list_item, null);
               subCategoryHolder = new SubCategoryViewHolder();
               subCategoryHolder.catId = subCategory.getCatId();
               subCategoryHolder.subCatId = subCategory.getSubCatId();
               subCategoryHolder.description = (TextView)v.findViewById(R.id.subcat_description);
               subCategoryHolder.image = (ImageView)v.findViewById(R.id.subcat_image);
               v.setTag(subCategoryHolder);
           } else subCategoryHolder = (SubCategoryViewHolder)v.getTag(); 
           
           subCategoryHolder.description.setText(subCategory.getSubCatDescription());
           subCategoryHolder.image.setImageResource(R.drawable.cash);

           return v;
       }
   }
	
   class Subcategories {
	    private Integer catId;
	    private Integer subCatId;
        private String subcatDescription;
        private Integer subcatImage;
        
        public Integer getCatId() {
        	return catId;
        }
        
        public Integer getSubCatId() {
        	return subCatId;
        }
       
        public String getSubCatDescription() {
            return subcatDescription;
        }

        public void setSubCatDescription(String description) {
        	subcatDescription = description;
        }
        
        public void setSubCatImage(Integer imageNo) {
        	subcatImage = imageNo;
        }
        
        public Integer getSubCatImage() {
        	return subcatImage;
        }

        public Subcategories(Integer catId,Integer subCatId, String description, Integer image) {
        	this.catId = catId;
        	this.subCatId = subCatId;
        	subcatDescription = description;
        	subcatImage = image;
        }
    }
}
