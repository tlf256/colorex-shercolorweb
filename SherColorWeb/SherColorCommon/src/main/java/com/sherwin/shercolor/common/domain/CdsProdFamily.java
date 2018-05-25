package com.sherwin.shercolor.common.domain;

import java.util.Comparator;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

@Entity
@Table(name="CDS_PROD_FAMILY")
@IdClass(CdsProdFamilyPK.class)
public class CdsProdFamily {

	private String name;
	private String prodNbr;
	private boolean primay;
	private String baseType;
	private Integer procOrder;
	private String cdsAdlFld;
	
	
	@Id
	@Column(name="name")
	@NotNull
	public String getName() {
		return name;
	}

	@Id
	@Column(name="prod_nbr")
	@NotNull
	public String getProdNbr() {
		return prodNbr;
	}
	
	@Column(name="primay")
	public boolean isPrimay() {
		return primay;
	}
	@Column(name="base_type")
	public String getBaseType() {
		return baseType;
	}

	@Column(name="proc_order")
	public Integer getProcOrder() {
		return procOrder;
	}

	@Column(name="cds_adl_fld")
	public String getCdsAdlFld() {
		return cdsAdlFld;
	}
	
	////////  SETTERS  //////////////

	public void setName(String name) {
		this.name = name;
	}
	public void setProdNbr(String prodNbr) {
		this.prodNbr = prodNbr;
	}
	public void setPrimay(boolean primay) {
		this.primay = primay;
	}
	public void setBaseType(String baseType) {
		this.baseType = baseType;
	}
	public void setProcOrder(Integer procOrder) {
		this.procOrder = procOrder;
	}
	public void setCdsAdlFld(String cdsAdlFld) {
		this.cdsAdlFld = cdsAdlFld;
	}
	
	
	public static Comparator<CdsProdFamily> ProcOrderComparator = new Comparator<CdsProdFamily>(){
		public int compare(CdsProdFamily e1, CdsProdFamily e2){
			//ascending order
			return e1.getProcOrder().compareTo(e2.getProcOrder());
		}
	};
	


}
