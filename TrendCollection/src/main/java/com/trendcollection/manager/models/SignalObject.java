package com.trendcollection.manager.models;

import java.sql.Timestamp;
import java.util.LinkedList;
import java.util.List;

public class SignalObject {
	private String category;
	private Integer categoryId;
	private String subCategory;
	private Integer subCategoryId;

	public SignalObject(String category, Integer categoryId,
			String subCategory, Integer subCategoryId) {
		this.category = category;
		this.categoryId = categoryId;
		this.subCategory = subCategory;
		this.subCategoryId = subCategoryId;
	}

	public String getCategory() {
		return category;
	}

	public Integer getCategoryId() {
		return categoryId;
	}

	public String getSubCategory() {
		return subCategory;
	}

	public Integer getSubCategoryId() {
		return subCategoryId;
	}
}