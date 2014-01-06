package com.moneyapp;

import java.util.List;

import com.kaching.R;
import com.moneyapp.database.Category;
import com.moneyapp.database.MoneyAppDatabaseHelper;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class Tab1Fragment extends Fragment {
	
	GridView gridView;
	 
	static final String[] MOBILE_OS = new String[] { 
		"Android", "iOS","Windows", "Blackberry" };
	
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
		// DELETE
		//((TextView)getView().findViewById(R.id.tab_text)).setText("Tab 1");
		
		gridView = (GridView) getView().findViewById(R.id.gridView1);
        
        MoneyAppDatabaseHelper db = MoneyAppDatabaseHelper.getInstance(getActivity().getBaseContext().getApplicationContext());
        
        List<Category> catList = db.getAllIncomeCategories();
        
		gridView.setAdapter(new ImageAdapter(getActivity().getBaseContext().getApplicationContext(), 
				catList));
		
		gridView.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View v,
					int position, long id) {
				Toast.makeText(
						getActivity().getBaseContext().getApplicationContext(),
				   ((TextView) v.findViewById(R.id.grid_item_label))
				   .getText(), Toast.LENGTH_SHORT).show();
 
			}
		});
		
		/* DELETE THIS
		Button button = (Button) getView().findViewById(R.id.tab_btn);
		button.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				replaceFragment();
			}
		});
		*/
	}
	
	private void replaceFragment() {
		((BaseContainerFragment)getParentFragment()).replaceFragment(new Tab1AddOnFragment(), true);
	}
	
}
