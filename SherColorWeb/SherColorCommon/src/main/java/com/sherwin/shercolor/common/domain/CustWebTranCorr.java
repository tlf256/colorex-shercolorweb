package com.sherwin.shercolor.common.domain;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;

@Entity
@Table(name="CustWebTranCorr")
@IdClass(CustWebTranCorrPK.class)
public class CustWebTranCorr {
	private String customerId; 
	private int controlNbr; 
	private int lineNbr;
	private int cycle;
	private int unitNbr;
	private int step;
	private String reason;
	private String status;
	private String userId;
	private Date dateTime;
	private String corrMethod;
	private boolean mergedWithOrig;
	private String clrntSysId;
	private int shotSize;
	private String clrnt1;
	private int clrntAmt1;
	private String clrnt2;
	private int clrntAmt2;
	private String clrnt3;
	private int clrntAmt3;
	private String clrnt4;
	private int clrntAmt4;
	private String clrnt5;
	private int clrntAmt5;
	private String clrnt6;
	private int clrntAmt6;
	private String clrnt7;
	private int clrntAmt7;
	private String clrnt8;
	private int clrntAmt8;
	
	@Id
	@Column(name="CustomerId")
	public String getCustomerId() {
		return customerId;
	}
	
	@Id
	@Column(name="ControlNbr")
	public int getControlNbr() {
		return controlNbr;
	}
	
	@Id
	@Column(name="LineNbr")
	public int getLineNbr() {
		return lineNbr;
	}
	
	@Id
	@Column(name="Cycle")
	public int getCycle() {
		return cycle;
	}

	@Id
	@Column(name="UnitNbr")
	public int getUnitNbr() {
		return unitNbr;
	}
	
	@Id
	@Column(name="Step")
	public int getStep() {
		return step;
	}
	
	@Column(name="Reason")
	public String getReason() {
		return reason;
	}
	
	@Column(name="Status")
	public String getStatus() {
		return status;
	}
	
	@Column(name="UserId")
	public String getUserId() {
		return userId;
	}
	
	@Column(name="DateTime")
	public Date getDateTime() {
		return dateTime;
	}
	
	@Column(name="CorrMethod")
	public String getCorrMethod() {
		return corrMethod;
	}

	@Column(name="MergedWithOrig")
	public boolean isMergedWithOrig() {
		return mergedWithOrig;
	}

	@Column(name="ClrntSysId")
	public String getClrntSysId() {
		return clrntSysId;
	}

	@Column(name="ShotSize")
	public int getShotSize() {
		return shotSize;
	}

	@Column(name="Clrnt1")
	public String getClrnt1() {
		return clrnt1;
	}

	@Column(name="ClrntAmt1")
	public int getClrntAmt1() {
		return clrntAmt1;
	}

	@Column(name="Clrnt2")
	public String getClrnt2() {
		return clrnt2;
	}

	@Column(name="ClrntAmt2")
	public int getClrntAmt2() {
		return clrntAmt2;
	}

	@Column(name="Clrnt3")
	public String getClrnt3() {
		return clrnt3;
	}

	@Column(name="ClrntAmt3")
	public int getClrntAmt3() {
		return clrntAmt3;
	}

	@Column(name="Clrnt4")
	public String getClrnt4() {
		return clrnt4;
	}

	@Column(name="ClrntAmt4")
	public int getClrntAmt4() {
		return clrntAmt4;
	}

	@Column(name="Clrnt5")
	public String getClrnt5() {
		return clrnt5;
	}

	@Column(name="ClrntAmt5")
	public int getClrntAmt5() {
		return clrntAmt5;
	}

	@Column(name="Clrnt6")
	public String getClrnt6() {
		return clrnt6;
	}

	@Column(name="ClrntAmt6")
	public int getClrntAmt6() {
		return clrntAmt6;
	}

	@Column(name="Clrnt7")
	public String getClrnt7() {
		return clrnt7;
	}

	@Column(name="ClrntAmt7")
	public int getClrntAmt7() {
		return clrntAmt7;
	}

	@Column(name="Clrnt8")
	public String getClrnt8() {
		return clrnt8;
	}

	@Column(name="ClrntAmt8")
	public int getClrntAmt8() {
		return clrntAmt8;
	}

	///////////////    SETTERS    ///////////////////
	
	public void setCustomerId(String customerId) {
		this.customerId = customerId;
	}
	public void setControlNbr(int controlNbr) {
		this.controlNbr = controlNbr;
	}
	public void setLineNbr(int lineNbr) {
		this.lineNbr = lineNbr;
	}
	public void setCycle(int cycle) {
		this.cycle = cycle;
	}
	public void setUnitNbr(int unitNbr) {
		this.unitNbr = unitNbr;
	}
	public void setStep(int step) {
		this.step = step;
	}
	public void setReason(String reason) {
		this.reason = reason;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public void setDateTime(Date dateTime) {
		this.dateTime = dateTime;
	}
	public void setCorrMethod(String corrMethod) {
		this.corrMethod = corrMethod;
	}
	public void setMergedWithOrig(boolean mergedWithOrig) {
		this.mergedWithOrig = mergedWithOrig;
	}
	public void setClrntSysId(String clrntSysId) {
		this.clrntSysId = clrntSysId;
	}
	public void setShotSize(int shotSize) {
		this.shotSize = shotSize;
	}
	public void setClrnt1(String clrnt1) {
		this.clrnt1 = clrnt1;
	}
	public void setClrntAmt1(int clrntAmt1) {
		this.clrntAmt1 = clrntAmt1;
	}
	public void setClrnt2(String clrnt2) {
		this.clrnt2 = clrnt2;
	}
	public void setClrntAmt2(int clrntAmt2) {
		this.clrntAmt2 = clrntAmt2;
	}
	public void setClrnt3(String clrnt3) {
		this.clrnt3 = clrnt3;
	}
	public void setClrntAmt3(int clrntAmt3) {
		this.clrntAmt3 = clrntAmt3;
	}
	public void setClrnt4(String clrnt4) {
		this.clrnt4 = clrnt4;
	}
	public void setClrntAmt4(int clrntAmt4) {
		this.clrntAmt4 = clrntAmt4;
	}
	public void setClrnt5(String clrnt5) {
		this.clrnt5 = clrnt5;
	}
	public void setClrntAmt5(int clrntAmt5) {
		this.clrntAmt5 = clrntAmt5;
	}
	public void setClrnt6(String clrnt6) {
		this.clrnt6 = clrnt6;
	}
	public void setClrntAmt6(int clrntAmt6) {
		this.clrntAmt6 = clrntAmt6;
	}
	public void setClrnt7(String clrnt7) {
		this.clrnt7 = clrnt7;
	}
	public void setClrntAmt7(int clrntAmt7) {
		this.clrntAmt7 = clrntAmt7;
	}
	public void setClrnt8(String clrnt8) {
		this.clrnt8 = clrnt8;
	}
	public void setClrntAmt8(int clrntAmt8) {
		this.clrntAmt8 = clrntAmt8;
	}
	
	

}
