package com.moneyapp.slidingmenu;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.kaching.R;
import com.moneyapp.CategoriesFragment;
import com.moneyapp.MainActivity;
import com.moneyapp.TabAccountListFragment;
import com.moneyapp.TransactionsFragment;

public class SlidingMenuListFragment extends ListFragment {

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		return inflater.inflate(R.layout.list, null);
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		/*
		String[] listItems = getResources().getStringArray(R.array.nav_drawer_items);
		ArrayAdapter<String> colorAdapter = new ArrayAdapter<String>(getActivity(), 
				android.R.layout.simple_list_item_1, android.R.id.text1, listItems);
		setListAdapter(colorAdapter);
		*/
		/*
		SampleAdapter adapter = new SampleAdapter(getActivity());
		for (int i = 0; i < 20; i++) {
			adapter.add(new SampleItem("Sample List", android.R.drawable.ic_menu_search));
		}
		setListAdapter(adapter);
		*/
		NavListAdapter adapter = new NavListAdapter(getActivity());
		String[] listItems = getResources().getStringArray(R.array.nav_drawer_items);
		String[] listItemsIcons = getResources().getStringArray(R.array.nav_drawer_icons);
		int i = 0;
		for (String navListItem : listItems) {
			int imageResource = getResources().getIdentifier(listItemsIcons[i].toString(), null, getActivity().getApplicationContext().getPackageName());
		    
			adapter.add(new NavListItem(navListItem, imageResource));
			i++;
		}
		setListAdapter(adapter);
	}
	
	private class NavListItem {
		public String tag;
		public int iconRes;
		public NavListItem(String tag, int iconRes) {
			this.tag = tag; 
			this.iconRes = iconRes;
		}
	}
	
	public class NavListAdapter extends ArrayAdapter<NavListItem> {

		public NavListAdapter(Context context) {
			super(context, 0);
		}

		public View getView(int position, View convertView, ViewGroup parent) {
			if (convertView == null) {
				convertView = LayoutInflater.from(getContext()).inflate(R.layout.row, null);
			}
			ImageView icon = (ImageView) convertView.findViewById(R.id.row_icon);
			icon.setImageResource(getItem(position).iconRes);
			TextView title = (TextView) convertView.findViewById(R.id.row_title);
			title.setText(getItem(position).tag);

			return convertView;
		}

	}

	@Override
	public void onListItemClick(ListView lv, View v, int position, long id) {
		Fragment newContent = null;
		String[] listItems = getResources().getStringArray(R.array.nav_drawer_items);
		String[] listItemsIcons = getResources().getStringArray(R.array.nav_drawer_icons);
		int imageResource;
		
		switch (position) {
		case 0:
			imageResource = getResources().getIdentifier(listItemsIcons[0].toString(), null, getActivity().getApplicationContext().getPackageName());
			getActivity().getActionBar().setIcon(imageResource);
			getActivity().getActionBar().setTitle(listItems[0].toString());
			newContent = new TransactionsFragment();
			break;
		case 1:
			imageResource = getResources().getIdentifier(listItemsIcons[1].toString(), null, getActivity().getApplicationContext().getPackageName());
			getActivity().getActionBar().setIcon(imageResource);
			getActivity().getActionBar().setTitle(listItems[1].toString());			
			newContent = new TabAccountListFragment();
			break;
		case 2:
			break;
		case 3:
			break;
		case 4:
			break;
		case 5:
			break;
		case 6:
			break;
		case 7:
			break;
		case 8:
			break;
		case 9:
			imageResource = getResources().getIdentifier(listItemsIcons[9].toString(), null, getActivity().getApplicationContext().getPackageName());
			getActivity().getActionBar().setIcon(imageResource);
			getActivity().getActionBar().setTitle(listItems[9].toString());			
			newContent = new CategoriesFragment();					
			break;
		case 10:
			break;
		case 11:
			break;		
		}
		if (newContent != null)
			switchFragment(newContent);
	}

	// the meat of switching the above fragment
	private void switchFragment(Fragment fragment) {
		if (getActivity() == null)
			return;
		
		if (getActivity() instanceof MainActivity) {
			MainActivity fca = (MainActivity) getActivity();
			fca.switchContent(fragment);
		} 
	}
}
