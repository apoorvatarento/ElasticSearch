package com.elasticSearch.domain;

import java.util.Date;

import org.springframework.hateoas.ResourceSupport;

import com.fasterxml.jackson.annotation.JsonGetter;

public class Person extends ResourceSupport {
	
	private String name;	
	private String domain;
	private int hrs;
	private String date;
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getDomain() {
		return domain;
	}
	public void setDomain(String domain) {
		this.domain = domain;
	}
	public int getHrs() {
		return hrs;
	}
	public void setHrs(int hrs) {
		this.hrs = hrs;
	}
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	public Person(String name, String domain, int hrs, String date) {
		super();
		this.name = name;
		this.domain = domain;
		this.hrs = hrs;
		this.date = date;
	}
	
public Person() {
	
}
	
	
	

	
	
}
