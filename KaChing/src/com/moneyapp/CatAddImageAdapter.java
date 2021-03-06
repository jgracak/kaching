package com.moneyapp;

import java.util.List;

import com.kaching.R;
import com.moneyapp.database.TableImage;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

public class CatAddImageAdapter extends BaseAdapter {
	private Context context;
	private List<TableImage> imageList;
	
	public CatAddImageAdapter(Context context, List<TableImage> imageList) {
		this.context = context;
		this.imageList = imageList;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		
		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	 
		View gridView;
		
		if (convertView == null) {
			 
			gridView = new View(context);
 
			// get layout
			gridView = inflater.inflate(R.layout.image_grid, null);
			
			ImageView imageView = (ImageView) gridView
					.findViewById(R.id.grid_item_image);
			
			imageView.setImageResource(imageList.get(position).getImage());
			imageView.setTag(imageList.get(position).getImage());
 
		} else {
			gridView = (View) convertView;
		}
		
		return gridView;
		
	}
	
	@Override
	public int getCount() {
		return imageList.size();
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
