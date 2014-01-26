package com.moneyapp.database;

public class TableCategory {
    int id;
    int idImage;
    int idCat;
    String catDesc;
    int idSubCat;
    String subCatDesc;
    int type;
    
 // constructors
    public TableCategory() {
    	
    }
    
    public TableCategory(int id) {
    	this.id = id;
    }

    public TableCategory(int idImage, int idCat, String catDesc, int idSubCat, String subCatDesc, int type){
    	this.idImage = idImage;
    	this.idCat = idCat;
    	this.catDesc = catDesc;
    	this.idSubCat = idSubCat;
    	this.subCatDesc = subCatDesc;
    	this.type = type;
    }
    
    public TableCategory(int id, int idImage, int idCat, String catDesc, int idSubCat, String subCatDesc, int type){
    	this.id = id;
    	this.idImage = idImage;
    	this.idCat = idCat;
    	this.catDesc = catDesc;
    	this.idSubCat = idSubCat;
    	this.subCatDesc = subCatDesc;
    	this.type = type;
    }
    
    // setters
    public void setId(int id){
    	this.id = id;
    }
    
    public void setIdImage(int idImage){
    	this.idImage = idImage;
    }
    
    public void setIdCat(int idCat){
    	this.idCat = idCat;
    }
    
    public void setCatDesc(String catDesc){
    	this.catDesc = catDesc;
    }
    
    public void setIdSubCat(int idSubCat){
    	this.idSubCat = idSubCat;
    }
    
    public void setSubCatDesc(String subCatDesc){
    	this.subCatDesc = subCatDesc;
    }
    
    public void setType(int type){
    	this.type = type;
    }
    
    //getters
    public int getId(){
    	return id;
    }
    
    public int getIdImage(){
    	return idImage;
    }
    
    public int getIdCat(){
    	return idCat;
    }
    
    public String getCatDesc(){
    	return catDesc;
    }
    
    public int getIdSubCat(){
    	return idSubCat;
    }
    
    public String getSubCatDesc(){
    	return subCatDesc;
    }
    
    public int getType(){
    	return type;
    }
}
