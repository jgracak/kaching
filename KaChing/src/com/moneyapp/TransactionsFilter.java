package com.moneyapp;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
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
import com.moneyapp.CategoryChooseTab1.CategoryListAdapter;
import com.moneyapp.TransactionsFragment.Transactions;
import com.moneyapp.database.MoneyAppDatabaseHelper;
import com.moneyapp.database.TableAccount;
import com.moneyapp.database.TableCategory;
import com.moneyapp.database.TableImage;
import com.moneyapp.database.TableTransaction;

public class TransactionsFilter extends Activity {
	public static final Integer ALL = 100;	
	public static final Integer INCOME = 101;
	public static final Integer EXPENSE = 102;
	public static final Integer AMOUNT_EXACT = 1;
	public static final Integer AMOUNT_OVER = 2;	
	public static final Integer AMOUNT_UNDER = 3;
	public static final Integer AMOUNT_BETWEEN = 4;
	public static final Integer AMOUNT_ADDED = 5;
	
	Button buttonSave;
	Integer id;
	Spinner categoryTypeChoice;
	Spinner categoryChoice;
	Spinner accountChoice;
	Spinner amountChoice;
	
	@Override
	public void onCreate(Bundle bundle) {
		super.onCreate(bundle);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);		
		setContentView(R.layout.transaction_filter);

		categoryTypeChoice = (Spinner) findViewById(R.id.categoryTypeChoice);
		categoryChoice = (Spinner) findViewById(R.id.categoryChoice);
		accountChoice = (Spinner) findViewById(R.id.accountChoice);
		amountChoice = (Spinner) findViewById(R.id.amountChoice);
		
		String[] catTypeStringArray = getResources().getStringArray(R.array.category_types_filter);
		
		ArrayList<String> catTypeList = new ArrayList<String>();
		
		for (String string : catTypeStringArray) {
			catTypeList.add(string);
		}
		
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.category_type_item_list,catTypeList);
		
		categoryTypeChoice.setAdapter(adapter);
		
		categoryTypeChoice.setOnItemSelectedListener(new OnItemSelectedListener() {

            public void onItemSelected(AdapterView<?> parentView,
                    View selectedItemView, int position, long id) {
                // Object item = parentView.getItemAtPosition(position);

                // Depend on first spinner value set adapter to 2nd spinner
            	
	            if(position == 0){
	            	initCategorySpinner(ALL);
	            } else if (position == 1) {
	            	initCategorySpinner(INCOME);
	            } else {
	            	initCategorySpinner(EXPENSE);
	            }
            }
            
            public void onNothingSelected(AdapterView<?> arg0) {
            	// do nothing
            }
        });
		
		amountChoice.setOnItemSelectedListener(new OnItemSelectedListener() {

            public void onItemSelected(AdapterView<?> parentView,
                    View selectedItemView, int position, long id) {
            	
	            if(position == AMOUNT_EXACT){ 
	            	Intent i = new Intent(getApplicationContext(), FilterAmountActivity.class);
		    		i.putExtra("mode",position);
		    		startActivityForResult(i,AMOUNT_EXACT);
	            } else if (position == AMOUNT_OVER) { 
	            	Intent i = new Intent(getApplicationContext(), FilterAmountActivity.class);
		    		i.putExtra("mode",position);
		    		startActivityForResult(i,AMOUNT_OVER);
	            } else if (position == AMOUNT_UNDER) { 
	            	Intent i = new Intent(getApplicationContext(), FilterAmountActivity.class);
		    		i.putExtra("mode",position);
		    		startActivityForResult(i,AMOUNT_UNDER);
	            } else if (position == AMOUNT_BETWEEN) { 
	            	Intent i = new Intent(getApplicationContext(), FilterAmountActivity.class);
		    		i.putExtra("mode",position);
		    		startActivityForResult(i,AMOUNT_BETWEEN);
	            }
            }
            
            public void onNothingSelected(AdapterView<?> arg0) {
            	// do nothing
            }
        });
    	
		initAccountSpinner();
		initAmountSpinner(false,0,"","");
		
		addListenerOnButtonSave();
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		
		if(resultCode == RESULT_OK){
			String amountEntered = data.getStringExtra("amountEntered");
			if (requestCode == AMOUNT_EXACT) {
				initAmountSpinner(true,AMOUNT_EXACT,amountEntered,"");
				amountChoice.setSelection(AMOUNT_ADDED);
			} else if (requestCode == AMOUNT_OVER) {
				initAmountSpinner(true,AMOUNT_OVER,amountEntered,"");
				amountChoice.setSelection(AMOUNT_ADDED);
			} else if (requestCode == AMOUNT_UNDER) {
				initAmountSpinner(true,AMOUNT_UNDER,amountEntered,"");
				amountChoice.setSelection(AMOUNT_ADDED);
			} else if (requestCode == AMOUNT_BETWEEN) {
				//TODO
				String amountEntered2 = data.getStringExtra("amountEntered2");
				initAmountSpinner(true,AMOUNT_BETWEEN,amountEntered,amountEntered2);
				amountChoice.setSelection(AMOUNT_ADDED);
			}
		} else if (resultCode == FilterAmountActivity.RESULT_CANCEL) {
			amountChoice.setSelection(0);
		}
	} 
	
	public void initAmountSpinner(boolean addItem, Integer position, String lastItem, String lastItem2) {
		MoneyAppDatabaseHelper db = MoneyAppDatabaseHelper.getInstance(this);
		
		String[] amountStringArray = getResources().getStringArray(R.array.amount_filters);
		
		ArrayList<String> amountList = new ArrayList<String>();
		
		for (String string : amountStringArray) {
			amountList.add(string);
		}
		
		if (addItem == true) {
			if (position == AMOUNT_BETWEEN) {
				amountList.add(amountStringArray[position].toString() + " " + lastItem + " - " + lastItem2);
			} else {
				amountList.add(amountStringArray[position].toString() + " " + lastItem);
			}
		}

		AmountListAdapter adapter = new AmountListAdapter(getApplicationContext(),R.layout.amount_list_item, amountList);
		
		amountChoice.setAdapter(adapter);
		
		db.close();
	}
	
	// Set accounts spinner
	public void initAccountSpinner() {
		MoneyAppDatabaseHelper db = MoneyAppDatabaseHelper.getInstance(this);
		
		ArrayList<TableAccount> accountList = db.getAllAccounts();
		
		accountList.add(0,new TableAccount(0,getResources().getString(R.string.account_all),0,0,0,0,0));

		AccountListAdapter adapter = new AccountListAdapter(getApplicationContext(),R.layout.account_choose_list_item, accountList);
		
		accountChoice.setAdapter(adapter);
		
		db.close();
	}
	
	public void initCategorySpinner (Integer mode) {
		if ((mode != INCOME) && (mode != EXPENSE) && (mode != ALL)) {
			return;
		}
		
		ArrayList<CategoryListItem> categoryListItems = new ArrayList<CategoryListItem>();
		
		MoneyAppDatabaseHelper db = MoneyAppDatabaseHelper.getInstance(this);
		List<TableCategory> categoryList = null;
		
		if (mode == INCOME) {
			categoryList = db.getAllIncomeCategories(MoneyAppDatabaseHelper.GET_ALL_INCOME);
		} else if (mode == EXPENSE) {
			categoryList = db.getAllExpenseCategories(MoneyAppDatabaseHelper.GET_ALL_EXPENSE);
		} else if (mode == ALL) {
			categoryList = db.getAllCategories();
		}
		
		for (TableCategory category : categoryList) {
			TableImage image = new TableImage();
			image = db.getImage(category.getIdImage());
			
			categoryListItems.add(new CategoryListItem(category.getId(),category.getIdCat(),category.getIdSubCat(),
													   image.getImage(),category.getCatDesc(),category.getSubCatDesc()));
		}
		
		categoryListItems.add(0, new CategoryListItem(0,0,0,R.drawable.world_all,getResources().getString(R.string.category_all),""));
		
		CategoryListAdapter adapter = new CategoryListAdapter(getApplicationContext(),
				R.layout.category_choose_list_item, categoryListItems);
		
		categoryChoice.setAdapter(adapter);
		
		db.close();
	}

	private class CategoryListItem {
		public Integer id;
		public Integer catId;
		public Integer subCatId;
		public Integer iconRes;		
		public String catName;
		public String subCatName;

		public CategoryListItem(Integer id, Integer catId,Integer subCatId, Integer iconRes,
								String catName, String subCatName) {
			this.id = id;
			this.catId = catId;
			this.subCatId = subCatId;
			this.iconRes = iconRes;
			this.catName = catName;
			this.subCatName = subCatName;
		}
	}	

	private class CategoryListItemHolder {
		Integer id;
		TextView catName;
		ImageView iconRes;
	}
	
	private class AccountListItemHolder {
		Integer id;
		TextView accountName;
		ImageView iconRes;
	}
	
	private class AmountListItemHolder {
		TextView amountName;
	}
	
    public class AmountListAdapter extends ArrayAdapter<String> {
		private ArrayList<String> items;
		private AmountListItemHolder amountListItemHolder;
    	 
        public AmountListAdapter(Context ctx, int txtViewResourceId, ArrayList<String> items) {
            super(ctx, txtViewResourceId, items);
            this.items  = items;
        }
        
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
        	View v = convertView;
        	
        	String amountText = items.get(position);
        	
        	if (v == null) {
        		LayoutInflater inflater = getLayoutInflater();
                v = inflater.inflate(R.layout.amount_list_item, parent,false);
                amountListItemHolder = new AmountListItemHolder();
                amountListItemHolder.amountName = (TextView)v.findViewById(R.id.amountText);
                v.setTag(amountListItemHolder);
        	} else amountListItemHolder = (AmountListItemHolder)v.getTag(); 
        	
        	amountListItemHolder.amountName.setText(amountText);
        	amountListItemHolder.amountName.setTag(amountText);
        	
            return v;
        }
    }
	
    public class AccountListAdapter extends ArrayAdapter<TableAccount> {
		private ArrayList<TableAccount> items;
		private AccountListItemHolder accountListItemHolder;
    	 
        public AccountListAdapter(Context ctx, int txtViewResourceId, ArrayList<TableAccount> items) {
            super(ctx, txtViewResourceId, items);
            this.items  = items;
        }
        
		public AccountListAdapter(Context context) {
			super(context, 0);
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
        	
        	TableAccount account = items.get(position);
        	
        	if (v == null) {
        		LayoutInflater inflater = getLayoutInflater();
                v = inflater.inflate(R.layout.account_choose_list_item, parent,false);
                accountListItemHolder = new AccountListItemHolder();
                accountListItemHolder.accountName = (TextView)v.findViewById(R.id.account_description);
                accountListItemHolder.iconRes = (ImageView)v.findViewById(R.id.account_image);
                v.setTag(accountListItemHolder);
        	} else accountListItemHolder = (AccountListItemHolder)v.getTag(); 
        	
        	accountListItemHolder.id = account.getId();
        	accountListItemHolder.accountName.setText(account.getDescription());
        	accountListItemHolder.accountName.setTag(account.getDescription());
        	
        	MoneyAppDatabaseHelper db = MoneyAppDatabaseHelper.getInstance(null);
        	
        	if (account.getId() != 0) {
        		TableImage image = db.getImage(account.getType());
        		accountListItemHolder.iconRes.setImageResource(image.getImage());
        	} else {
        		accountListItemHolder.iconRes.setImageResource(R.drawable.world_all);
        	}
        	
        	db.close();
        	
            return v;
        }
        
        @Override
		public int getCount() {
			return items!=null ? items.size() : 0;
		}
    }
	
	private class SubCategoryListItemHolder {
		Integer id;
		TextView subCatName;
		ImageView iconRes;
	}
	
    public class CategoryListAdapter extends ArrayAdapter<CategoryListItem> {
		private ArrayList<CategoryListItem> items;
		private CategoryListItemHolder categoryListItemHolder;
		private SubCategoryListItemHolder subCategoryListItemHolder;
    	 
        public CategoryListAdapter(Context ctx, int txtViewResourceId, ArrayList<CategoryListItem> items) {
            super(ctx, txtViewResourceId, items);
            this.items  = items;
        }
        
		public CategoryListAdapter(Context context) {
			super(context, 0);
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
			categoryListItemHolder = null;
			subCategoryListItemHolder = null;
			
			CategoryListItem categoryListItem = items.get(position);
			View v = convertView;
			
			if (categoryListItem.subCatId == 0) {
				
				boolean checkTagCat = false;
				try {
					categoryListItemHolder = (CategoryListItemHolder)v.getTag();
				} catch (Exception e) {
					checkTagCat = true;
	        	}
				
				if ((v == null) || (checkTagCat == true)) {
					v = LayoutInflater.from(getContext()).inflate(R.layout.category_choose_list_item, null);
					categoryListItemHolder = new CategoryListItemHolder();
					categoryListItemHolder.id = categoryListItem.id;
					categoryListItemHolder.iconRes = (ImageView) v.findViewById(R.id.category_image);
					categoryListItemHolder.catName = (TextView) v.findViewById(R.id.category_description);
					v.setTag(categoryListItemHolder);
				} else categoryListItemHolder = (CategoryListItemHolder)v.getTag();
				
				categoryListItemHolder.id = categoryListItem.id;
				categoryListItemHolder.iconRes.setImageResource(categoryListItem.iconRes);
				categoryListItemHolder.catName.setText(categoryListItem.catName);
			} else {
				
				boolean checkTagCat = false;
				try {
					subCategoryListItemHolder = (SubCategoryListItemHolder)v.getTag();
				} catch (Exception e) {
					checkTagCat = true;
					
	        	}
				
				if ((v == null) || (checkTagCat == true)) {
					v = LayoutInflater.from(getContext()).inflate(R.layout.subcategory_choose_list_item, null);
					subCategoryListItemHolder = new SubCategoryListItemHolder();
					subCategoryListItemHolder.id = categoryListItem.id;
					subCategoryListItemHolder.iconRes = (ImageView) v.findViewById(R.id.category_image);
					subCategoryListItemHolder.subCatName = (TextView) v.findViewById(R.id.category_description);
					v.setTag(subCategoryListItemHolder);
							
				} else subCategoryListItemHolder = (SubCategoryListItemHolder)v.getTag();
				
				subCategoryListItemHolder.id = categoryListItem.id;
				subCategoryListItemHolder.iconRes.setImageResource(R.drawable.navigation_forward);
				subCategoryListItemHolder.subCatName.setText(categoryListItem.subCatName);
			}
			
			return v;
        }
        
        @Override
		public int getCount() {
			return items!=null ? items.size() : 0;
		}
		
		@Override
        public int getViewTypeCount() {
            return 2;
        }
        @Override
        public int getItemViewType(int position) {
            if (items.get(position).subCatId == 0) return 0;
            else return 1;
        }
        
    }

	public void addListenerOnButtonSave() {
		 
		buttonSave = (Button) findViewById(R.id.buttonSave);

		buttonSave.setOnClickListener(new View.OnClickListener() {
 
			@Override
			public void onClick(View arg0) {
				/*
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
				*/
			}
		});
	}
}
