package com.moneyapp;

import java.util.List;

import com.kaching.R;
import com.moneyapp.database.Category;
import com.moneyapp.database.Image;
import com.moneyapp.database.MoneyAppDatabaseHelper;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
 
public class ImageAdapter extends BaseAdapter {
	private Context context;
	private List<Category> catList;
 
	public ImageAdapter(Context context, List<Category> catList) {
		this.context = context;
		this.catList = catList;
	}
 
	public View getView(int position, View convertView, ViewGroup parent) {
 
		LayoutInflater inflater = (LayoutInflater) context
			.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
 
		View gridView;
 
		if (convertView == null) {
 
			gridView = new View(context);
 
			// get layout
			gridView = inflater.inflate(R.layout.cat_grid, null);
 
			// set value into textview
			TextView textView = (TextView) gridView
					.findViewById(R.id.grid_item_label);
			
			//textView.setText(mobileValues[position]);
			textView.setText(catList.get(position).getCatDesc());
			textView.setTag(catList.get(position).getId());
			
			// set image based on selected text
			ImageView imageView = (ImageView) gridView
					.findViewById(R.id.grid_item_image);
			
			MoneyAppDatabaseHelper db = MoneyAppDatabaseHelper.getInstance(null);
			
			Image image = db.getImage(catList.get(position).getIdImage());
			imageView.setImageResource(image.getImage());
 
		} else {
			gridView = (View) convertView;
		}
 
		return gridView;
	}
 
	@Override
	public int getCount() {
		return catList.size();
	}
 
	@Override
	public Object getItem(int position) {
		return null;
	}
 
	@Override
	public long getItemId(int position) {
		return 0;
	}
 
}
