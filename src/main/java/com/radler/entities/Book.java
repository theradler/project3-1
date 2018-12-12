package com.radler.entities;

import org.hibernate.validator.constraints.NotBlank;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;


import static javax.persistence.GenerationType.IDENTITY;


@SuppressWarnings("serial")
@Entity 
@Table(name = "BOOK")
public class Book implements Serializable {
	
@Id
@GeneratedValue(strategy = IDENTITY)
@Column(name = "ID")
private Long id;

@NotBlank(message="{validation.category_Name.NotBlank.message}")
@Column(name = "CATEGORY_NAME")
private String categoryName;

@NotBlank(message="{validation.isbn.NotBlank.message}")
@Size(min=10, max=10, message="{validation.isbn.Size.message}")
@Column(name = "ISBN")
private String isbn;

@NotBlank(message="{validation.title.NotBlank.message}")
@Column(name = "TITLE")
private String title;

@NotNull
@Column(name = "PRICE")
private Double price;

@NotBlank
@Column(name = "PUBLISHER")
private String publisher;
 

public Long getId() {
	return this.id;
}

public void setId(Long iD) {
	this.id = iD;
}

public String getCategoryName() {
	return this.categoryName; 
}

public void setCategoryName(String categoryName) {
	this.categoryName = categoryName;
}

public String getIsbn() {
	return this.isbn; 
}

public void setIsbn(String isbn) {
	this.isbn = isbn; 
}


public String getTitle() {
	return this.title;
}

public void setTitle(String title) {
	this.title = title;
}


public Double getPrice() {
	return this.price; 
}
 
public void setPrice(Double price) {
	this.price = price; 
}

public String  getPublisher() {
	return this.publisher;
}

public void setPublisher(String publisher) {
	this.publisher = publisher;
}

@Override
public String toString() {
	return "Book ID: " + id + ", Category: " + categoryName + ", ISBM: " + isbn + ", Title: " + title + ", Price: " + price; 
}
}
