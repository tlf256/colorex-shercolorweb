package com.sherwin.shercolor.common.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

@Entity
@Table(name="CDS_PROD_NOTES")
@IdClass(CdsProdNotesPK.class)
public class CdsProdNotes {
	private String salesNbr;
	private Integer seqNbr;
	private String checkExpr;
	private String note;
	private String typ;
	private String cdsAdlFld;
	
	@Id
	@Column(name="sales_nbr")
	@NotNull
	public String getSalesNbr() {
		return salesNbr;
	}

	@Id
	@Column(name="seq_nbr")
	@NotNull
	public Integer getSeqNbr() {
		return seqNbr;
	}

	@Column(name="check_expr")
	public String getCheckExpr() {
		return checkExpr;
	}

	@Column(name="note")
	public String getNote() {
		return note;
	}

	@Column(name="typ")
	public String getTyp() {
		return typ;
	}

	@Column(name="cds_adl_fld")
	public String getCdsAdlFld() {
		return cdsAdlFld;
	}
	
	//////////////////  SETTERS  ////////////////////
	
	public void setSalesNbr(String salesNbr) {
		this.salesNbr = salesNbr;
	}
	public void setSeqNbr(Integer seqNbr) {
		this.seqNbr = seqNbr;
	}
	public void setCheckExpr(String checkExpr) {
		this.checkExpr = checkExpr;
	}
	public void setNote(String note) {
		this.note = note;
	}
	public void setTyp(String typ) {
		this.typ = typ;
	}
	public void setCdsAdlFld(String cdsAdlFld) {
		this.cdsAdlFld = cdsAdlFld;
	}

	
	

}
