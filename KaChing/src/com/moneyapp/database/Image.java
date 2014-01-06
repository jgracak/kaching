package com.moneyapp.database;

public class Image {
	int id;
	int image;
	int type;
	
	// constructors
	public Image(){
		
	}
	
	public Image(int image, int type){
		this.image = image;
		this.type = type;
	}
	
	public Image(int id, int image, int type){
		this.id = id;
		this.image = image;
		this.type = type;
	}
	
	// setters
	public void setId(int id){
		this.id = id;
	}
	
	public void setImage(int image){
		this.image = image;
	}
	
	public void setType(int type){
		this.type = type;
	}
	
	// getters
	public int getId(){
		return id;
	}
	
	public int getImage(){
		return image;
	}
	
	public int getType(){
		return type;
	}
}
