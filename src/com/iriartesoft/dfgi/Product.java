package com.iriartesoft.dfgi;

public class Product {
	 private int id=0;
	 private String name="";
	 private String cantity="";
	 private Boolean checked=false;

	    public Product(int id, String name, String cantity, Boolean checked) {
	        super();
	        this.id=id;
	        this.name = name;
	        this.cantity = cantity;
	        this.checked = checked;
	    }

	    public String getName() {
	        return name;
	    }

	    public void setName(String name) {
	        this.name = name;
	    }
	    
	    public String getCantity() {
	        return cantity;
	    }

	    public void setCantity(String cantity) {
	        this.cantity = cantity;
	    }
	    
	    public int getId(){
	    	return id;
	    }
	    public void setId(int id){
	    	this.id=id;
	    }
	    public Boolean getChecked(){
	    	return checked;
	    }
	    public void setChecked(Boolean checked){
	    	this.checked=checked;
	    }
	}