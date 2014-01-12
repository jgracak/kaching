package com.moneyapp.slidingmenu;

import android.support.v4.app.FragmentActivity;
import android.support.v4.app.Fragment;
import android.os.Bundle;


import com.kaching.R;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.moneyapp.slidingmenu.Constants;

public class SlidingMenuInitialiser {
	private FragmentActivity activity;
	private SlidingMenu menu;

	/**
	 * Initialiser constructor sets activity, which is going to have sliding
	 * menu created.
	 * 
	 * @param activity
	 *            This is Activity to which sliding menu is attached.
	 * 
	 */
	public SlidingMenuInitialiser(FragmentActivity activity) {
		this.activity = activity;
	}

	/**
	 * This method creates sliding out menu from the left screen side. It uses
	 * external "SlidingMenu" library for creation. When menu is attached to the
	 * activity, it places a list fragment inside the menu as it's content.
	 * Method requires to define a controlling class for that list fragment and
	 * an XML resource to be used as data to fill fragment list with items.
	 * 
	 */
	public void createSlidingMenu(Class<?> customClass, int xmlResourceId) {
		// For actual sliding menu creation we use an external open source
		// Android library called "SlidingMenu". It can be found at
		// "https://github.com/jfeinstein10/SlidingMenu".
		// We configure the SlidingMenu to our needs.
		menu = new SlidingMenu(activity);
		menu.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
		menu.setShadowWidthRes(R.dimen.sliding_menu_shadow_width);
		menu.setShadowDrawable(R.drawable.sliding_menu_shadow);
		menu.setBehindOffsetRes(R.dimen.sliding_menu_offset);
		menu.setFadeDegree(0.35f);
		menu.attachToActivity(activity, SlidingMenu.SLIDING_CONTENT);
		menu.setMenu(R.layout.sliding_menu_frame);

		// We create a bundle to store XML resource id value. This value needs
		// to be passed to our fragment.
		Bundle bundle = new Bundle();
		bundle.putInt(Constants.SLIDING_MENU_LIST_FRAGMENT_KEY, xmlResourceId);
		
		// Here we create a new instance of a SlidingMenuListFragmentBase with
		// the given class name. This is the same as calling its empty
		// constructor. We also pass bundle with XML resource id value.
		SlidingMenuListFragmentBase slidingMenuListFragment = (SlidingMenuListFragmentBase) Fragment
				.instantiate(activity, customClass.getName(), bundle);
		// We target sliding menu.
		slidingMenuListFragment.setSlidingMenu(menu);
		// We replace a FrameLayout, which is a content of sliding menu, with
		// our created list fragment filled with data from XML file.

		activity.getSupportFragmentManager().beginTransaction()
				.replace(R.id.sliding_menu_frame, slidingMenuListFragment)
				.commit();
	}
	
	public SlidingMenu getSlidingMenu() {
		return menu;
	}
}
