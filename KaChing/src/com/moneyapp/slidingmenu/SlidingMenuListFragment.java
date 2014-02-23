package com.moneyapp.slidingmenu;

import java.util.ArrayList;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.kaching.R;
import com.moneyapp.CategoriesFragment;
import com.moneyapp.MainActivity;
import com.moneyapp.AccountsFragment;
import com.moneyapp.TransactionsFragment;

public class SlidingMenuListFragment extends ListFragment {
	public static final Integer FIRST_MENU_ITEM = 0;
	public static final Integer LAST_MENU_ITEM = 2;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		return inflater.inflate(R.layout.list, null);
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

		ArrayList<NavListItem> NavListItemList = new ArrayList<NavListItem>();

		String[] listItems = getResources().getStringArray(
				R.array.nav_drawer_items);
		String[] listItemsIcons = getResources().getStringArray(
				R.array.nav_drawer_icons);
		String[] listItemsType = getResources().getStringArray(
				R.array.naw_drawer_type);
		int i = 0;
		for (String navListItem : listItems) {
			int imageResource;
			int type = Integer.parseInt(listItemsType[i].toString());
			if ((type == 0) || (type == 2)) {
				imageResource = getResources().getIdentifier(
						listItemsIcons[i].toString(), null,
						getActivity().getApplicationContext().getPackageName());

				NavListItemList.add(new NavListItem(i, type, navListItem,
						imageResource));
			} else {
				NavListItemList.add(new NavListItem(i, type, navListItem, null));
			}

			i++;
		}

		NavListAdapter adapter = new NavListAdapter(getActivity(),
				R.layout.row, NavListItemList);

		setListAdapter(adapter);
	}

	private class NavListItem {
		public Integer id;
		public Integer type;
		public String tag;
		public Integer iconRes;

		public NavListItem(Integer id, Integer type, String tag, Integer iconRes) {
			this.id = id;
			this.type = type;
			this.tag = tag;
			this.iconRes = iconRes;
		}
	}

	private class NavListItemHolder {
		Integer id;
		Integer type;
		TextView tag;
		ImageView iconRes;
	}

	private class NavListGroupHolder {
		Integer id;
		Integer type;
		TextView tag;
	}

	public class NavListAdapter extends ArrayAdapter<NavListItem> {
		private ArrayList<NavListItem> items;
		private NavListItemHolder navListItemHolder;
		private NavListGroupHolder navListGroupHolder;

		public NavListAdapter(Context context, int tvResId,
				ArrayList<NavListItem> items) {
			super(context, tvResId, items);
			this.items = items;
		}

		public NavListAdapter(Context context) {
			super(context, 0);
		}

		public View getView(int position, View convertView, ViewGroup parent) {
			navListItemHolder = null;
			navListGroupHolder = null;
			
			NavListItem navListItem = items.get(position);
			
			if (navListItem.iconRes != null) {
				View v = convertView;
				
				if (v == null) {
					v = LayoutInflater.from(getContext()).inflate(R.layout.row, null);
					navListItemHolder = new NavListItemHolder();
					navListItemHolder.tag = (TextView)v.findViewById(R.id.row_title);
					navListItemHolder.iconRes = (ImageView) v.findViewById(R.id.row_icon);
					v.setTag(navListItemHolder);
				} else navListItemHolder = (NavListItemHolder)v.getTag();
				navListItemHolder.id = navListItem.id;
				navListItemHolder.type = navListItem.type;
				navListItemHolder.tag.setText(navListItem.tag);
				navListItemHolder.iconRes.setImageResource(navListItem.iconRes);
				
				// Sliding menu dividers
				
				float density = v.getResources().getDisplayMetrics().density;	//conversion from px to dp
			
				if(navListItemHolder.type == LAST_MENU_ITEM) {
					navListItemHolder.tag.setBackgroundResource(R.drawable.row_border_transparent);
					navListItemHolder.iconRes.setBackgroundResource(R.drawable.row_border_transparent);
					navListItemHolder.iconRes.setPadding((int)(5*density), (int)(12*density), (int)(12 * density), (int)(12*density));
				}
				else {
					navListItemHolder.tag.setBackgroundResource(R.drawable.row_border);
					navListItemHolder.iconRes.setBackgroundResource(R.drawable.row_border);				
					navListItemHolder.iconRes.setPadding((int)(5*density), (int)(12*density), (int)(12 * density), (int)(12*density));
				}
				
				return v;
				
			} else {
				View v = convertView;
				
				if (v == null) {
					v = LayoutInflater.from(getContext()).inflate(R.layout.row_group, null);
					navListGroupHolder = new NavListGroupHolder();
					navListGroupHolder.tag = (TextView)v.findViewById(R.id.row_title);		
					v.setTag(navListGroupHolder);
					v.setOnClickListener(null);
				} else navListGroupHolder = (NavListGroupHolder)v.getTag();
				
				navListGroupHolder.id = navListItem.id;
				navListGroupHolder.id = navListItem.type;
				navListGroupHolder.tag.setText(navListItem.tag);	
				LinearLayout.LayoutParams params = (LinearLayout.LayoutParams)navListGroupHolder.tag.getLayoutParams();
				if (navListGroupHolder.id == FIRST_MENU_ITEM) {
					params.setMargins(0, 0, 0, 0); //substitute parameters for left, top, right, bottom
				} else {
					params.setMargins(0, 20, 0, 0); //substitute parameters for left, top, right, bottom
				}
				navListGroupHolder.tag.setLayoutParams(params);
				return v;
			}
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
            if (items.get(position).iconRes != null) return 0;
            else return 1;
        }

	}

	@Override
	public void onListItemClick(ListView lv, View v, int position, long id) {
		Fragment newContent = null;
		String[] listItems = getResources().getStringArray(R.array.nav_drawer_items);
		String[] listItemsIcons = getResources().getStringArray(R.array.nav_drawer_icons);
		int imageResource;
		
		switch (position) {
		//case 0: MAIN
		case 1: //Entries
			imageResource = getResources().getIdentifier(listItemsIcons[1].toString(), null, getActivity().getApplicationContext().getPackageName());
			getActivity().getActionBar().setIcon(imageResource);
			getActivity().getActionBar().setTitle(listItems[1].toString());
			newContent = new TransactionsFragment();
			break;
		case 2: //Recurring Entries
			break;
		case 3: //Accounts
			imageResource = getResources().getIdentifier(listItemsIcons[3].toString(), null, getActivity().getApplicationContext().getPackageName());
			getActivity().getActionBar().setIcon(imageResource);
			getActivity().getActionBar().setTitle(listItems[3].toString());			
			newContent = new AccountsFragment();
			break;
		case 4: //Categories
			imageResource = getResources().getIdentifier(listItemsIcons[4].toString(), null, getActivity().getApplicationContext().getPackageName());
			getActivity().getActionBar().setIcon(imageResource);
			getActivity().getActionBar().setTitle(listItems[4].toString());			
			newContent = new CategoriesFragment();	
			break;
		//case 5: REPORTS
		case 6: //Reports
			break;
		case 7: //Loans
			break;
		case 8: //Forecast
			break;
		//case 9:PLANNING
		case 10: //Budgets				
			break;
		case 11: //Projects
			break;
		case 12: //Savings
			break;
		//case 13: OPTIONS
		case 14: //Database
			break;
		case 15: //Settings
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
