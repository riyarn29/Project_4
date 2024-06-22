package com.rays.pro4.Bean;

import java.util.Date;

public class Abcbean extends  BaseBean{

	private String name;
	private int amount;
	private Date date;
	private String type;
	private String company;
	private String mobile;
	
	public String getMobile() {
		return mobile;
	}
	public void setMobile(String mobile) {
		this.mobile = mobile;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getAmount() {
		return amount;
	}
	public void setAmount(int amount) {
		this.amount = amount;
	}
	public Date getDate() {
		return date;
	}
	public void setDate(Date date) {
		this.date = date;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getCompany() {
		return company;
	}
	public void setCompany(String company) {
		this.company = company;
	}
	@Override
	public String getkey() {
		// TODO Auto-generated method stub
		return type+"";
	}
	@Override
	public String getValue() {
		// TODO Auto-generated method stub
		return type;
	}
	
}
