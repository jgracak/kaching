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
import android.widget.ListView;
import android.widget.TextView;

import com.kaching.R;
import com.moneyapp.CategoriesFragment;
import com.moneyapp.MainActivity;
import com.moneyapp.TabAccountListFragment;
import com.moneyapp.TransactionsFragment;

public class SlidingMenuListFragment extends ListFragment {

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		return inflater.inflate(R.layout.list, null);
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

		ArrayList<NavListItem> navListItemList = new ArrayList<NavListItem>();

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
			if (type == 0) {
				imageResource = getResources().getIdentifier(
						listItemsIcons[i].toString(), null,
						getActivity().getApplicationContext().getPackageName());

				navListItemList.add(new NavListItem(navListItem,
						imageResource));
			} else {
				navListItemList.add(new NavListItem(navListItem, null));
			}

			i++;
		}

		NavListAdapter adapter = new NavListAdapter(getActivity(),
				R.layout.row, navListItemList);

		setListAdapter(adapter);
	}

	private class NavListItem {
		public String tag;
		public Integer iconRes;

		public NavListItem(String tag, Integer iconRes) {
			this.tag = tag;
			this.iconRes = iconRes;
		}
	}

	private class NavListItemHolder {
		TextView tag;
		ImageView iconRes;
	}

	private class NavListGroupHolder {
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
				
				navListItemHolder.tag.setText(navListItem.tag);
				navListItemHolder.iconRes.setImageResource(navListItem.iconRes);
				
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
				
				navListGroupHolder.tag.setText(navListItem.tag);				
				
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
		String[] listItems = getResources().getStringArray(
				R.array.nav_drawer_items);
		String[] listItemsIcons = getResources().getStringArray(
				R.array.nav_drawer_icons);
		int imageResource;

		switch (position) {
		case 0:
			break;
		case 1:
			imageResource = getResources().getIdentifier(
					listItemsIcons[1].toString(), null,
					getActivity().getApplicationContext().getPackageName());
			getActivity().getActionBar().setIcon(imageResource);
			getActivity().getActionBar().setTitle(listItems[1].toString());
			newContent = new TransactionsFragment();
			break;
		case 2:
			imageResource = getResources().getIdentifier(
					listItemsIcons[2].toString(), null,
					getActivity().getApplicationContext().getPackageName());
			getActivity().getActionBar().setIcon(imageResource);
			getActivity().getActionBar().setTitle(listItems[2].toString());
			newContent = new TabAccountListFragment();
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
			break;
		case 10:
			break;
		case 11:
			imageResource = getResources().getIdentifier(
					listItemsIcons[11].toString(), null,
					getActivity().getApplicationContext().getPackageName());
			getActivity().getActionBar().setIcon(imageResource);
			getActivity().getActionBar().setTitle(listItems[11].toString());
			newContent = new CategoriesFragment();
			break;
		case 12:
			break;
		case 13:
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
