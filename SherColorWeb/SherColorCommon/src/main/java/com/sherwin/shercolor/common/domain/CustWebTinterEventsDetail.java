package com.sherwin.shercolor.common.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

@Entity
@Table(name="CustWebTinterEventsDetail")
@IdClass(CustWebTinterEventsDetailPK.class)
public class CustWebTinterEventsDetail {
	private String guid;
	private String type;
	private String name;
	private float qty;
	
	@Id
	@Column(name="Guid")
	@NotNull
	public String getGuid() {
		return guid;
	}
	
	@Id
	@Column(name="Type")
	@NotNull
	public String getType() {
		return type;
	}
	
	@Id
	@Column(name="Name")
	@NotNull
	public String getName() {
		return name;
	}
	
	@Column(name="Qty")
	public float getQty() {
		return qty;
	}
	
	///////////////////  SETTERS  ////////////////////
	
	public void setGuid(String guid) {
		this.guid = guid;
	}
	public void setType(String type) {
		this.type = type;
	}
	public void setName(String name) {
		this.name = name;
	}
	public void setQty(float qty) {
		this.qty = qty;
	}

	
	


	
	
}
