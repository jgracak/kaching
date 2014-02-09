package com.moneyapp;

import java.util.List;

import com.actionbarsherlock.app.SherlockFragment;
import com.kaching.R;
import com.moneyapp.database.TableCategory;
import com.moneyapp.database.MoneyAppDatabaseHelper;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.PopupMenu;
import android.widget.PopupMenu.OnMenuItemClickListener;

public class Tab1Fragment extends SherlockFragment {
	
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
	
	public void callDialog(TextView cat) {
		final TextView catText = cat;
		
		DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
		    @Override
		    public void onClick(DialogInterface dialog, int which) {
		        switch (which){
		        case DialogInterface.BUTTON_POSITIVE:
		        	MoneyAppDatabaseHelper db = MoneyAppDatabaseHelper.getInstance(getActivity());
		        	
		        	db.deleteTransactionCat(db.getCategory((Integer)catText.getTag()));
		        	db.deleteCategory(db.getCategory((Integer)catText.getTag()));
					
		        	db.close();
		        	initView();
		            break;
		        case DialogInterface.BUTTON_NEGATIVE:
		            //No button clicked
		            break;
		        }
		    }
		};
		AlertDialog.Builder builder = new AlertDialog.Builder(getSherlockActivity());
		builder.setMessage("Do you want to delete " + catText.getText() + " category? All transaction linked to this category will be deleted.")
		    .setNegativeButton("No", dialogClickListener).setPositiveButton("Yes", dialogClickListener).show();
	}

	private void initView() {	
		gridView = (GridView) getView().findViewById(R.id.gridView1);
        
        MoneyAppDatabaseHelper db = MoneyAppDatabaseHelper.getInstance(getActivity().getBaseContext().getApplicationContext());
        
        List<TableCategory> catList = db.getAllIncomeCategories(MoneyAppDatabaseHelper.GET_ALL_CAT_INCOME);
        
		gridView.setAdapter(new ImageAdapter(getActivity().getBaseContext().getApplicationContext(), 
				catList));
		
		gridView.setOnItemClickListener(new OnItemClickListener() {
			
			@Override
			public void onItemClick(AdapterView<?> arg0, View v, int position,
					long id) {
				final TextView catText = (TextView) v.findViewById(R.id.grid_item_label);
				
				PopupMenu popupMenu = new PopupMenu(getActivity().getApplicationContext(),v);
				
				popupMenu.getMenuInflater().inflate(R.menu.actions,popupMenu.getMenu());
				popupMenu.setOnMenuItemClickListener(new OnMenuItemClickListener() {

							@Override
							public boolean onMenuItemClick(MenuItem arg0) {
								
								if (arg0.getTitle().equals(getResources().getString(R.string.cnt_menu_edit))) {
						    		Intent i = new Intent(getActivity().getBaseContext().getApplicationContext(), CategoryEditActivity.class);
						    		i.putExtra("category_id",(Integer)catText.getTag());
						    		startActivity(i);
						        	return true;
								} else if (arg0.getTitle().equals(getResources().getString(R.string.cnt_menu_delete))) {
									callDialog(catText);
									return true;
								} else if (arg0.getTitle().equals(getResources().getString(R.string.cnt_menu_subcategories))){
						    		Intent i = new Intent(getActivity().getBaseContext().getApplicationContext(), SubCatActivity.class);
						    		i.putExtra("category_id",(Integer)catText.getTag());
						    		startActivity(i);
									return true;
								}

								return false;
							}
						});
				popupMenu.show();
			}
		});
	}
}
