package com.sherwin.shercolor.common.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
//import javax.persistence.JoinColumn;
//import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table(name="CUSTWEBACTIVEPRODS")
public class CustWebActiveProds {

	private String salesNbr;
	private String prodNbr;
	private String sizeCd;
	private String upc;
	private double gallons;
	private String comments;
	
	@Id
	@Column(name="salesnbr")
	public String getSalesNbr() {
		return salesNbr;
	}
	public void setSalesNbr(String salesNbr) {
		this.salesNbr = salesNbr;
	}
	@Column(name="prodnbr")
	public String getProdNbr() {
		return prodNbr;
	}
	public void setProdNbr(String prodNbr) {
		this.prodNbr = prodNbr;
	}
	@Column(name="sizecd")
	public String getSizeCd() {
		return sizeCd;
	}
	public void setSizeCd(String sizeCd) {
		this.sizeCd = sizeCd;
	}
	@Column(name="gallons")
	public double getGallons() {
		return gallons;
	}
	public void setGallons(double gallons) {
		this.gallons = gallons;
	}
	@Column(name="comments")
	public String getComments() {
		return comments;
	}
	public void setComments(String comments) {
		this.comments = comments;
	}
	@Column(name="upc")
	public String getUpc() {
		return upc;
	}
	public void setUpc(String upc) {
		this.upc = upc;
	}

}
