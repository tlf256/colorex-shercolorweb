package com.sherwin.shercolor.common.domain;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;

@Entity
@Table(name="CDS_FB_COLOR")
@IdClass(CdsFbColorPK.class)
public class CdsFbColor {
	private String colorComp;
	private String colorId;
	private String clrntSysId;
	private String fbBase;
	private String fbSubBase;
	private String clrntFbSysId1;
	private String clrntFbSysId2;
	private String clrntFbSysId3;
	private String clrntFbSysId4;
	private String clrntFbSysId5;
	private String clrntFbSysId6;
	private int clrntShots1;
	private int clrntShots2;
	private int clrntShots3;
	private int clrntShots4;
	private int clrntShots5;
	private int clrntShots6 ;
	private boolean twoPartMatchReqd;
	private String cdsAdlFld;
	private boolean noQuart;
	private Date lastUpdate;
	
	
	@Id
	@Column(name="color_comp")
	public String getColorComp() {
		return colorComp;
	}
	
	@Id
	@Column(name="color_id")
	public String getColorId() {
		return colorId;
	}
	
	@Id
	@Column(name="clrnt_sys_id")
	public String getClrntSysId() {
		return clrntSysId;
	}
	
	@Id
	@Column(name="fb_base")
	public String getFbBase() {
		return fbBase;
	}
	
	@Column(name="fb_sub_base")
	public String getFbSubBase() {
		return fbSubBase;
	}
	
	@Column(name="clrnt_fb_sys_id_1")
	public String getClrntFbSysId1() {
		return clrntFbSysId1;
	}
	
	@Column(name="clrnt_fb_sys_id_2")
	public String getClrntFbSysId2() {
		return clrntFbSysId2;
	}
	
	@Column(name="clrnt_fb_sys_id_3")
	public String getClrntFbSysId3() {
		return clrntFbSysId3;
	}
	
	@Column(name="clrnt_fb_sys_id_4")
	public String getClrntFbSysId4() {
		return clrntFbSysId4;
	}
	
	@Column(name="clrnt_fb_sys_id_5")
	public String getClrntFbSysId5() {
		return clrntFbSysId5;
	}
	
	@Column(name="clrnt_fb_sys_id_6")
	public String getClrntFbSysId6() {
		return clrntFbSysId6;
	}
	
	@Column(name="clrnt_shots_1")
	public int getClrntShots1() {
		return clrntShots1;
	}
	
	@Column(name="clrnt_shots_2")
	public int getClrntShots2() {
		return clrntShots2;
	}
	
	@Column(name="clrnt_shots_3")
	public int getClrntShots3() {
		return clrntShots3;
	}
	
	@Column(name="clrnt_shots_4")
	public int getClrntShots4() {
		return clrntShots4;
	}
	
	@Column(name="clrnt_shots_5")
	public int getClrntShots5() {
		return clrntShots5;
	}
	
	@Column(name="clrnt_shots_6")
	public int getClrntShots6() {
		return clrntShots6;
	}
	
	@Column(name="two_part_match_reqd")
	public boolean isTwoPartMatchReqd() {
		return twoPartMatchReqd;
	}
	
	@Column(name="cds_adl_fld")
	public String getCdsAdlFld() {
		return cdsAdlFld;
	}
	
	@Column(name="no_quart")
	public boolean isNoQuart() {
		return noQuart;
	}
	
	@Column(name="last_update")
	public Date getLastUpdate() {
		return lastUpdate;
	}
	

///////////////////  SETTERS   //////////////////////////////////
	
	public void setColorComp(String colorComp) {
		this.colorComp = colorComp;
	}
	public void setColorId(String colorId) {
		this.colorId = colorId;
	}
	public void setClrntSysId(String clrntSysId) {
		this.clrntSysId = clrntSysId;
	}
	public void setFbBase(String fbBase) {
		this.fbBase = fbBase;
	}
	public void setFbSubBase(String fbSubBase) {
		this.fbSubBase = fbSubBase;
	}
	public void setClrntFbSysId1(String clrntFbSysId1) {
		this.clrntFbSysId1 = clrntFbSysId1;
	}
	public void setClrntFbSysId2(String clrntFbSysId2) {
		this.clrntFbSysId2 = clrntFbSysId2;
	}
	public void setClrntFbSysId3(String clrntFbSysId3) {
		this.clrntFbSysId3 = clrntFbSysId3;
	}
	public void setClrntFbSysId4(String clrntFbSysId4) {
		this.clrntFbSysId4 = clrntFbSysId4;
	}
	public void setClrntFbSysId5(String clrntFbSysId5) {
		this.clrntFbSysId5 = clrntFbSysId5;
	}
	public void setClrntFbSysId6(String clrntFbSysId6) {
		this.clrntFbSysId6 = clrntFbSysId6;
	}
	public void setClrntShots1(int clrntShots1) {
		this.clrntShots1 = clrntShots1;
	}
	public void setClrntShots2(int clrntShots2) {
		this.clrntShots2 = clrntShots2;
	}
	public void setClrntShots3(int clrntShots3) {
		this.clrntShots3 = clrntShots3;
	}
	public void setClrntShots4(int clrntShots4) {
		this.clrntShots4 = clrntShots4;
	}
	public void setClrntShots5(int clrntShots5) {
		this.clrntShots5 = clrntShots5;
	}
	public void setClrntShots6(int clrntShots6) {
		this.clrntShots6 = clrntShots6;
	}
	public void setTwoPartMatchReqd(boolean twoPartMatchReqd) {
		this.twoPartMatchReqd = twoPartMatchReqd;
	}
	public void setCdsAdlFld(String cdsAdlFld) {
		this.cdsAdlFld = cdsAdlFld;
	}
	public void setNoQuart(boolean noQuart) {
		this.noQuart = noQuart;
	}
	public void setLastUpdate(Date lastUpdate) {
		this.lastUpdate = lastUpdate;
	}

}
