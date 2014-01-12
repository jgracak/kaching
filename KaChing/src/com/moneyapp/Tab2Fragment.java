package com.moneyapp;

import java.util.List;

import com.kaching.R;
import com.moneyapp.database.Category;
import com.moneyapp.database.MoneyAppDatabaseHelper;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.PopupMenu;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.PopupMenu.OnMenuItemClickListener;

public class Tab2Fragment extends Fragment {
	
	GridView gridView;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		return inflater.inflate(R.layout.tab_fragment, null);
	}
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		initView();
	}
	
	private void initView() {
        gridView = (GridView) getView().findViewById(R.id.gridView1);
        
        MoneyAppDatabaseHelper db = MoneyAppDatabaseHelper.getInstance(getActivity().getBaseContext().getApplicationContext());
        
        List<Category> catList = db.getAllExpenseCategories();
        
		gridView.setAdapter(new ImageAdapter(getActivity().getBaseContext().getApplicationContext(), 
							catList));
 
		gridView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View v, int position,
					long id) {
				PopupMenu popupMenu = new PopupMenu(getActivity().getApplicationContext(),
						v);
				popupMenu.getMenuInflater().inflate(R.menu.actions,
						popupMenu.getMenu());
				popupMenu
						.setOnMenuItemClickListener(new OnMenuItemClickListener() {

							@Override
							public boolean onMenuItemClick(MenuItem arg0) {
								Toast.makeText(getActivity().getApplicationContext(),
										"Do something!" + arg0.getTitle(), Toast.LENGTH_SHORT)
										.show();

								return false;
							}
						});
				popupMenu.show();
			}
		});
	}

}
