package com.moneyapp.database;

public class Account {
    int id;
    String description;
    int book_id;
    float starting_balance;
    int exclude_from_balance;
    int exclude_from_reports;
 
    // constructors
    public Account() {
    }
 
    public Account(String description, int book_id,float starting_balance,
    		int exclude_from_balance, int exclude_from_reports) {
        this.description = description;
        this.book_id = book_id;
        this.starting_balance = starting_balance;
        this.exclude_from_balance = exclude_from_balance;
        this.exclude_from_reports = exclude_from_reports;
    }
 
    public Account(int id, String description, int book_id,float starting_balance,
    		int exclude_from_balance, int exclude_from_reports) {
        this.id = id;
        this.description = description;
        this.book_id = book_id;
        this.starting_balance = starting_balance;
        this.exclude_from_balance = exclude_from_balance;
        this.exclude_from_reports = exclude_from_reports;
    }
 
    // setters
    public void setId(int id) {
        this.id = id;
    }
 
    public void setDescription(String description) {
        this.description = description;
    }
 
    public void setBookId(int book_id) {
        this.book_id = book_id;
    }
     
    public void setStartingBalance(float starting_balance){
        this.starting_balance = starting_balance;
    }
 
    public void setExcludeFromBalance(int exclude_from_balance) {
        this.exclude_from_balance = exclude_from_balance;
    }
    
    public void setExcludeFromReports(int exclude_from_reports) {
        this.exclude_from_reports = exclude_from_reports;
    }
    
    // getters
    public long getId() {
        return this.id;
    }
 
    public String getDescription() {
        return this.description;
    }
 
    public int getBookId() {
        return this.book_id;
    }
    
    public float getStartingBalance() {
    	return this.starting_balance;
    }
    
    public int getExcludeFromBalance() {
    	return this.exclude_from_balance;
    }
    
    public int getExcludeFromReports() {
    	return this.exclude_from_reports;
    }
}
