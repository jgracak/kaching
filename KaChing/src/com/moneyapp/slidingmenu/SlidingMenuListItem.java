package com.moneyapp.slidingmenu;

public class SlidingMenuListItem {
	public int Id;
	public String Name;
	public String IconResourceId;

	public SlidingMenuListItem() {
	}

	public SlidingMenuListItem(int id, String name, String iconResourceId) {
		this.Id = id;
		this.Name = name;
		this.IconResourceId = iconResourceId;
	}
}
