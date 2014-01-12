package com.moneyapp.slidingmenu;

import com.kaching.R;
import com.moneyapp.CategoriesFragment;
import com.moneyapp.TabAccountListFragment;
import com.moneyapp.TransactionsFragment;
import com.moneyapp.slidingmenu.SlidingMenuListItem;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.view.View;
import android.widget.ListView;

public class SlidingMenuListFragmentConcrete extends
		SlidingMenuListFragmentBase {

	// We can define actions, which will be called, when we press on separate
	// list items. These actions can override default actions defined inside
	// base fragment. Also, you can create new actions, which will added to the
	// default ones.
	
	@Override
	public void onListItemClick(ListView l, View v, int position, long id) {
		SlidingMenuListItem item = slidingMenuList.get(position);
		FragmentActivity activity = this.getActivity();
		
		Fragment fragment = null;
		
		switch (item.Id) {
		case 1:
			fragment = new TransactionsFragment();
        	activity.getActionBar().setIcon(R.drawable.ic_home);
            break;
		case 2:
			fragment = new TabAccountListFragment();
			activity.getActionBar().setIcon(R.drawable.ic_people);
            break;
		case 3:
			activity.getActionBar().setIcon(R.drawable.ic_photos);
            break;            
		case 4:
			activity.getActionBar().setIcon(R.drawable.ic_communities);
            break;
        case 5:
        	activity.getActionBar().setIcon(R.drawable.ic_pages);
            break;
        case 6:
        	activity.getActionBar().setIcon(R.drawable.ic_whats_hot);
            break;
        case 7:
			activity.getActionBar().setIcon(R.drawable.ic_home);
            break;
        case 8:
        	activity.getActionBar().setIcon(R.drawable.ic_people);
            break;
        case 9:
        	activity.getActionBar().setIcon(R.drawable.ic_photos);
            break;
        case 10:
        	fragment = new CategoriesFragment();
        	activity.getActionBar().setIcon(R.drawable.ic_communities);
            break;
        case 11:
        	activity.getActionBar().setIcon(R.drawable.ic_pages);
            break;
        case 12:
        	activity.getActionBar().setIcon(R.drawable.ic_whats_hot);
            break;            
        default:
            break;            
		}

		if (fragment != null) {
            FragmentManager fragmentManager = activity.getSupportFragmentManager();
            fragmentManager.beginTransaction()
                    .replace(R.id.frame_container, fragment).commit();
            
            menu.toggle();
            activity.getActionBar().setTitle(item.Name);
		}
		
		
	}
}
