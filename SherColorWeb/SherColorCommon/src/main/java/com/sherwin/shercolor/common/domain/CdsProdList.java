package com.sherwin.shercolor.common.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

@Entity
@Table(name="CDS_PROD_LIST")
@IdClass(CdsProdListPK.class)
public class CdsProdList {
	private String prodListId;
	private String salesNbr;
	private String descr;
	private String cdsAdlFld;
	
	@Id
	@Column(name="prod_list_id")
	@NotNull
	public String getProdListId() {
		return prodListId;
	}
	
	@Id
	@Column(name="sales_nbr")
	@NotNull
	public String getSalesNbr() {
		return salesNbr;
	}
	
	@Column(name="descr")
	public String getDescr() {
		return descr;
	}
	
	@Column(name="cds_adl_fld")
	public String getCdsAdlFld() {
		return cdsAdlFld;
	}
	
	/////////  SETTERS  ///////////////
	
	public void setProdListId(String prodListId) {
		this.prodListId = prodListId;
	}
		public void setSalesNbr(String salesNbr) {
		this.salesNbr = salesNbr;
	}
	public void setDescr(String descr) {
		this.descr = descr;
	}
	public void setCdsAdlFld(String cdsAdlFld) {
		this.cdsAdlFld = cdsAdlFld;
	}
	

}
