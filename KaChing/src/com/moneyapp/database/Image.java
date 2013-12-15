package com.moneyapp.database;

public class Image {
	int id;
	int image;
	
	// constructors
	public Image(){
		
	}
	
	public Image(int image){
		this.image = image;
	}
	
	public Image(int id, int image){
		this.id = id;
		this.image = image;
	}
	
	// setters
	public void setId(int id){
		this.id = id;
	}
	
	public void setImage(int image){
		this.image = image;
	}
	
	// getters
	public int getId(){
		return id;
	}
	
	public int getImage(){
		return image;
	}
}
