package com.moneyapp.database;


import android.text.format.Time;

public class Transaction {
	int id;
	Time transDate;
	int idCategory;
	float amount;
	String note;
	int idAccount;
	
	//constructors
	public Transaction() {
		
	}
	
	public Transaction (int id, Time transDate, int idCategory, float amount, String note, int idAccount){
		this.id = id;
		this.transDate = transDate;
		this.idCategory = idCategory;
		this.amount = amount;
		this.note = note;
		this.idAccount = idAccount;
	}

	public Transaction (Time transDate, int idCategory, float amount, String note, int idAccount){
		this.transDate = transDate;
		this.idCategory = idCategory;
		this.amount = amount;
		this.note = note;
		this.idAccount = idAccount;
	}
	
	//setters
	public void setId (int id) {
		this.id = id;
	}

	public void setTransDate(Time transDate){
		this.transDate = transDate;
	}

	public void setIdCategory(int idCategory){
		this.idCategory = idCategory;
	}
	
	public void setAmount(float amount){
		this.amount = amount;	
	}
	
	public void setNote(String note){
		this.note = note;
	}
	
	public void setIdAccount(int idAccount){
		this.idAccount = idAccount;
	}
	
	//getters
	public int getId(){
		return id;
	}
	
	public Time getTransDate(){
		return transDate;
	}
	
	public int getIdCategory(){
		return idCategory;
	}
	
	public float getAmount(){
		return amount;
	}
	
	public String getNote(){
		return note;
	}
	
	public int getIdAccount(){
		return idAccount;
	}
}

