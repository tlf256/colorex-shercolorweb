package com.sherwin.shercolor.common.domain;

import java.util.Date;

import com.google.gson.annotations.SerializedName;

public class OeServiceLargeObjectStorage {

	@SerializedName("largeObjectStorageID")
	private String largeObjectStorageId;

	@SerializedName("description")
	private String description;
	
	@SerializedName("createDate")
	private Date createDate;
	
	@SerializedName("moduleID")
	private String moduleId;
	
	@SerializedName("clobObject")
	private String clobObject;
	
	@SerializedName("Prod-Nbr")
	private String prodNbr;
	
	@SerializedName("dbRowID")
	private String dbRowId;

	public String getLargeObjectStorageId() {
		return largeObjectStorageId;
	}

	public String getDescription() {
		return description;
	}

	public Date getCreateDate() {
		return createDate;
	}

	public String getModuleId() {
		return moduleId;
	}

	public String getClobObject() {
		return clobObject;
	}

	public String getProdNbr() {
		return prodNbr;
	}

	public String getDbRowId() {
		return dbRowId;
	}

	public void setLargeObjectStorageId(String largeObjectStorageId) {
		this.largeObjectStorageId = largeObjectStorageId;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	public void setModuleId(String moduleId) {
		this.moduleId = moduleId;
	}

	public void setClobObject(String clobObject) {
		this.clobObject = clobObject;
	}

	public void setProdNbr(String prodNbr) {
		this.prodNbr = prodNbr;
	}

	public void setDbRowId(String dbRowId) {
		this.dbRowId = dbRowId;
	}
	
	

}
