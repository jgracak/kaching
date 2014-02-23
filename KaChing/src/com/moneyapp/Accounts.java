package com.moneyapp;

public class Accounts {
	   private Integer accountId;
       private String accountDescription;
       private Float accountStartingBalance;
       private Integer accountImage;

       public Integer getAccountId() {
       	return accountId;
       }
      
       public String getDescription() {
           return accountDescription;
       }

       public void setDescription(String description) {
           accountDescription = description;
       }

       public Float getStartingBalance() {
           return accountStartingBalance;
       }
       
       public void setBalance(Float balance) {
           accountStartingBalance = balance;
       }
       
       public void setImage(Integer imageNo) {
       	accountImage = imageNo;
       }
       
       public Integer getImage() {
       	return accountImage;
       }

       public Accounts(Integer id, String description, Float balance, Integer image) {
       	accountId = id;
           accountDescription = description;
           accountStartingBalance = balance;
           accountImage = image;
       }
}
