package com.moneyapp;

import java.util.ArrayList;
import java.util.List;

import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.app.SherlockListFragment;
import com.kaching.R;
import com.moneyapp.database.TableCategory;
import com.moneyapp.database.MoneyAppDatabaseHelper;
import com.moneyapp.database.TableImage;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

public class CategoryChooseTab1 extends SherlockListFragment {
	
	GridView gridView;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		return inflater.inflate(R.layout.category_choose_list, null);
	}
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		initView();
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
	
	private class SubCategoryListItemHolder {
		Integer id;
		TextView subCatName;
		ImageView iconRes;
	}
	
	public class CategoryListAdapter extends ArrayAdapter<CategoryListItem> {
		private ArrayList<CategoryListItem> items;
		private CategoryListItemHolder categoryListItemHolder;
		private SubCategoryListItemHolder subCategoryListItemHolder;
		
		public CategoryListAdapter(Context context, int tvResId,
				ArrayList<CategoryListItem> items) {
			super(context, tvResId, items);
			this.items = items;
		}
		
		public CategoryListAdapter(Context context) {
			super(context, 0);
		}
		
		public View getView(int position, View convertView, ViewGroup parent) {
			categoryListItemHolder = null;
			subCategoryListItemHolder = null;
			
			CategoryListItem categoryListItem = items.get(position);
			View v = convertView;
			
			if (categoryListItem.subCatId == 0) {
				
				if (v == null) {
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
				if (v == null) {
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
	
	@Override
	public void onListItemClick(ListView lv, View v, int position, long id) {
		CategoryListItemHolder categoryListItemHolder = null;
		SubCategoryListItemHolder subCategoryListItemHolder = null;
		
		try {
			categoryListItemHolder = (CategoryListItemHolder) v.getTag();
		} catch (Exception e) {
			subCategoryListItemHolder = (SubCategoryListItemHolder) v.getTag();
		}

		Intent resultData = new Intent();
		if (categoryListItemHolder != null) {
			resultData.putExtra("categoryId", categoryListItemHolder.id);
		} else {
			resultData.putExtra("categoryId", subCategoryListItemHolder.id);
		}
		
		getActivity().setResult(SherlockActivity.RESULT_OK, resultData);
		getActivity().finish();
	}

	private void initView() {	
		//Add items to list
		ArrayList<CategoryListItem> categoryListItems = new ArrayList<CategoryListItem>();
		
		MoneyAppDatabaseHelper db = MoneyAppDatabaseHelper.getInstance(getActivity().getBaseContext().getApplicationContext());
		
		List<TableCategory> categoryList = db.getAllIncomeCategories(MoneyAppDatabaseHelper.GET_ALL_INCOME);
		
		for (TableCategory category : categoryList) {
			TableImage image = new TableImage();
			image = db.getImage(category.getIdImage());
			
			categoryListItems.add(new CategoryListItem(category.getId(),category.getIdCat(),category.getIdSubCat(),
													   image.getImage(),category.getCatDesc(),category.getSubCatDesc()));
		}
		
		//Add list to adapter
		CategoryListAdapter adapter = new CategoryListAdapter(getActivity(),
										R.layout.category_choose_list_item, categoryListItems);

		setListAdapter(adapter);
	}
}
