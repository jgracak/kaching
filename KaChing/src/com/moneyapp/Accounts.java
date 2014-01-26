package com.moneyapp;

public class Accounts {
	   private Integer accountId;
       private String accountDescription;
       private Float accountBalance;
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

       public Float getBalance() {
           return accountBalance;
       }
       
       public void setBalance(Float balance) {
           accountBalance = balance;
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
           accountBalance = balance;
           accountImage = image;
       }
}
