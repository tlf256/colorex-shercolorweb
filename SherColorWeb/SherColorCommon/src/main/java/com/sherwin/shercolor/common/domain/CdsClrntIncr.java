package com.sherwin.shercolor.common.domain;

import java.util.Comparator;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

@Entity
@Table(name="CDS_CLRNT_INCR")
@IdClass(CdsClrntIncrPK.class)
public class CdsClrntIncr {

	private String clrntSysId;
	private String incr;
	private double ozIncrRatio;
	private double rollupAmt;
	private String cdsAdlFld;

	@Id
	@Column(name="clrnt_sys_id")
	@NotNull
	public String getClrntSysId() {
		return clrntSysId;
	}

	@Id
	@Column(name="incr")
	public String getIncr() {
		return incr;
	}

	@Column(name="oz_incr_ratio")
	public double getOzIncrRatio() {
		return ozIncrRatio;
	}

	@Column(name="rollup_amt")
	public double getRollupAmt() {
		return rollupAmt;
	}

	@Column(name="cds_adl_fld")
	public String getCdsAdlFld() {
		return cdsAdlFld;
	}
	
	//////////////  SETTERS  ///////////////
	
	public void setClrntSysId(String clrntSysId) {
		this.clrntSysId = clrntSysId;
	}
	public void setIncr(String incr) {
		this.incr = incr;
	}
	public void setOzIncrRatio(double ozIncrRatio) {
		this.ozIncrRatio = ozIncrRatio;
	}
	public void setRollupAmt(double rollupAmt) {
		this.rollupAmt = rollupAmt;
	}
	public void setCdsAdlFld(String cdsAdlFld) {
		this.cdsAdlFld = cdsAdlFld;
	}
	
	public static Comparator<CdsClrntIncr> OzIncrRatioComparator = new Comparator<CdsClrntIncr>(){
		public int compare(CdsClrntIncr e1, CdsClrntIncr e2){
			//ascending order
			if(e1.getOzIncrRatio() < e2.getOzIncrRatio()) return -1;
			if(e1.getOzIncrRatio() > e2.getOzIncrRatio()) return 1;
			return 0;
		}
	};

}
