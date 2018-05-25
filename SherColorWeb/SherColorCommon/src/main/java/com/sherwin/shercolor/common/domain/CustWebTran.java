package com.sherwin.shercolor.common.domain;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;

@Entity
@Table(name="CUSTWEBTRAN")
@IdClass(CustWebTranPK.class)
public class CustWebTran {

	private String customerId; 
	private int controlNbr; 
	private int lineNbr;
	private String colorType;
	private String colorComp;
	private String colorId;
	private String colorName;
	private String salesNbr;
	private String prodNbr;
	private String sizeCode;
	private String clrntSysId;
	private String userId;
	private String tranCode;
	private Date initTranDate;
	private Date lastTranDate;
	private String formSource;
	private String formMethod;
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
	private int shotSize;
	private int formPct;
	private boolean vinylSafe;
	private String primerId;
	private String rgbHex;
	private double deltaEPrimary;
	private double deltaESecondary;
	private double deltaETertiary;
	private double averageDeltaE;
	private double crThick;
	private double crThin;
	private double engDecisionValue;
	private String formulaRule;
	private String illumPrimary;
	private String illumSecondary;
	private String illumTertiary;
	private String ColorEngVer;
	private double spd;
	private double metamerismIndex;
	private double formulationTime;
	private double[] projCurve;
	private double[] measCurve;
	private String jobField01;
	private String jobField02;
	private String jobField03;
	private String jobField04;
	private String jobField05;
	private String jobField06;
	private String jobField07;
	private String jobField08;
	private String jobField09;
	private String jobField10;
	private int quantityDispensed;
	private String origColorType;
	private String origColorComp;
	private String origColorId;
	private String origFormSource;
	private String origFormMethod;
	private String origClrnt1;
	private int origClrntAmt1;
	private String origClrnt2;
	private int origClrntAmt2;
	private String origClrnt3;
	private int origClrntAmt3;
	private String origClrnt4;
	private int origClrntAmt4;
	private String origClrnt5;
	private int origClrntAmt5;
	private String origClrnt6;
	private int origClrntAmt6;
	private String origClrnt7;
	private int origClrntAmt7;
	private String origClrnt8;
	private int origClrntAmt8;
	
	
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

	@Column(name="ColorType")
	public String getColorType() {
		return colorType;
	}

	@Column(name="ColorComp")
	public String getColorComp() {
		return colorComp;
	}

	@Column(name="ColorId")
	public String getColorId() {
		return colorId;
	}

	@Column(name="ColorName")
	public String getColorName() {
		return colorName;
	}

	@Column(name="SalesNbr")
	public String getSalesNbr() {
		return salesNbr;
	}

	@Column(name="ProdNbr")
	public String getProdNbr() {
		return prodNbr;
	}

	@Column(name="SizeCode")
	public String getSizeCode() {
		return sizeCode;
	}

	@Column(name="ClrntSysId")
	public String getClrntSysId() {
		return clrntSysId;
	}

	@Column(name="UserId")
	public String getUserId() {
		return userId;
	}

	@Column(name="TranCode")
	public String getTranCode() {
		return tranCode;
	}

	@Column(name="InitTranDate")
	public Date getInitTranDate() {
		return initTranDate;
	}

	@Column(name="LastTranDate")
	public Date getLastTranDate() {
		return lastTranDate;
	}

	@Column(name="FormSource")
	public String getFormSource() {
		return formSource;
	}

	@Column(name="FormMethod")
	public String getFormMethod() {
		return formMethod;
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

	@Column(name="ShotSize")
	public int getShotSize() {
		return shotSize;
	}

	@Column(name="FormPct")
	public int getFormPct() {
		return formPct;
	}

	@Column(name="VinylSafe")
	public boolean isVinylSafe() {
		return vinylSafe;
	}

	@Column(name="PrimerId")
	public String getPrimerId() {
		return primerId;
	}

	@Column(name="RgbHex")
	public String getRgbHex() {
		return rgbHex;
	}

	@Column(name="DeltaEPrimary")
	public double getDeltaEPrimary() {
		return deltaEPrimary;
	}

	@Column(name="DeltaESecondary")
	public double getDeltaESecondary() {
		return deltaESecondary;
	}

	@Column(name="DeltaETertiary")
	public double getDeltaETertiary() {
		return deltaETertiary;
	}

	@Column(name="AverageDeltaE")
	public double getAverageDeltaE() {
		return averageDeltaE;
	}

	@Column(name="CrThick")
	public double getCrThick() {
		return crThick;
	}

	@Column(name="CrThin")
	public double getCrThin() {
		return crThin;
	}

	@Column(name="EngDecisionValue")
	public double getEngDecisionValue() {
		return engDecisionValue;
	}

	@Column(name="FormulaRule")
	public String getFormulaRule() {
		return formulaRule;
	}

	@Column(name="IllumPrimary")
	public String getIllumPrimary() {
		return illumPrimary;
	}

	@Column(name="IllumSecondary")
	public String getIllumSecondary() {
		return illumSecondary;
	}

	@Column(name="IllumTertiary")
	public String getIllumTertiary() {
		return illumTertiary;
	}

	@Column(name="ColorEngVer")
	public String getColorEngVer() {
		return ColorEngVer;
	}

	@Column(name="Spd")
	public double getSpd() {
		return spd;
	}

	@Column(name="MetamerismIndex")
	public double getMetamerismIndex() {
		return metamerismIndex;
	}

	@Column(name="FormulationTime")
	public double getFormulationTime() {
		return formulationTime;
	}

	@org.hibernate.annotations.Type(type="com.sherwin.shercolor.common.domain.usertype.CurveType_double")
	@Column(name="ProjCurve")
	public double[] getProjCurve() {
		return projCurve;
	}

	@org.hibernate.annotations.Type(type="com.sherwin.shercolor.common.domain.usertype.CurveType_double")
	@Column(name="MeasCurve")
	public double[] getMeasCurve() {
		return measCurve;
	}

	@Column(name="JobField01")
	public String getJobField01() {
		return jobField01;
	}

	@Column(name="JobField02")
	public String getJobField02() {
		return jobField02;
	}

	@Column(name="JobField03")
	public String getJobField03() {
		return jobField03;
	}

	@Column(name="JobField04")
	public String getJobField04() {
		return jobField04;
	}

	@Column(name="JobField05")
	public String getJobField05() {
		return jobField05;
	}

	@Column(name="JobField06")
	public String getJobField06() {
		return jobField06;
	}

	@Column(name="JobField07")
	public String getJobField07() {
		return jobField07;
	}

	@Column(name="JobField08")
	public String getJobField08() {
		return jobField08;
	}

	@Column(name="JobField09")
	public String getJobField09() {
		return jobField09;
	}

	@Column(name="JobField10")
	public String getJobField10() {
		return jobField10;
	}
	
	@Column(name="QuantityDispensed")
	public int getQuantityDispensed() {
		return quantityDispensed;
	}

	@Column(name="OrigColorType")
	public String getOrigColorType() {
		return origColorType;
	}

	@Column(name="OrigColorComp")
	public String getOrigColorComp() {
		return origColorComp;
	}

	@Column(name="OrigColorId")
	public String getOrigColorId() {
		return origColorId;
	}

	@Column(name="OrigFormSource")
	public String getOrigFormSource() {
		return origFormSource;
	}

	@Column(name="OrigFormMethod")
	public String getOrigFormMethod() {
		return origFormMethod;
	}

	@Column(name="OrigClrnt1")
	public String getOrigClrnt1() {
		return origClrnt1;
	}

	@Column(name="OrigClrntAmt1")
	public int getOrigClrntAmt1() {
		return origClrntAmt1;
	}

	@Column(name="OrigClrnt2")
	public String getOrigClrnt2() {
		return origClrnt2;
	}

	@Column(name="OrigClrntAmt2")
	public int getOrigClrntAmt2() {
		return origClrntAmt2;
	}

	@Column(name="OrigClrnt3")
	public String getOrigClrnt3() {
		return origClrnt3;
	}

	@Column(name="OrigClrntAmt3")
	public int getOrigClrntAmt3() {
		return origClrntAmt3;
	}

	@Column(name="OrigClrnt4")
	public String getOrigClrnt4() {
		return origClrnt4;
	}

	@Column(name="OrigClrntAmt4")
	public int getOrigClrntAmt4() {
		return origClrntAmt4;
	}

	@Column(name="OrigClrnt5")
	public String getOrigClrnt5() {
		return origClrnt5;
	}

	@Column(name="OrigClrntAmt5")
	public int getOrigClrntAmt5() {
		return origClrntAmt5;
	}

	@Column(name="OrigClrnt6")
	public String getOrigClrnt6() {
		return origClrnt6;
	}

	@Column(name="OrigClrntAmt6")
	public int getOrigClrntAmt6() {
		return origClrntAmt6;
	}

	@Column(name="OrigClrnt7")
	public String getOrigClrnt7() {
		return origClrnt7;
	}

	@Column(name="OrigClrntAmt7")
	public int getOrigClrntAmt7() {
		return origClrntAmt7;
	}

	@Column(name="OrigClrnt8")
	public String getOrigClrnt8() {
		return origClrnt8;
	}

	@Column(name="OrigClrntAmt8")
	public int getOrigClrntAmt8() {
		return origClrntAmt8;
	}


	////////////////////////////  SETTERS  ////////////////////////////////
	
	public void setCustomerId(String customerId) {
		this.customerId = customerId;
	}
	public void setControlNbr(int controlNbr) {
		this.controlNbr = controlNbr;
	}
	public void setLineNbr(int lineNbr) {
		this.lineNbr = lineNbr;
	}
	public void setColorType(String colorType) {
		this.colorType = colorType;
	}
	public void setColorComp(String colorComp) {
		this.colorComp = colorComp;
	}
	public void setColorId(String colorId) {
		this.colorId = colorId;
	}
	public void setColorName(String colorName) {
		this.colorName = colorName;
	}
	public void setSalesNbr(String salesNbr) {
		this.salesNbr = salesNbr;
	}
	public void setProdNbr(String prodNbr) {
		this.prodNbr = prodNbr;
	}
	public void setSizeCode(String sizeCode) {
		this.sizeCode = sizeCode;
	}
	public void setClrntSysId(String clrntSysId) {
		this.clrntSysId = clrntSysId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public void setTranCode(String tranCode) {
		this.tranCode = tranCode;
	}
	public void setInitTranDate(Date initTranDate) {
		this.initTranDate = initTranDate;
	}
	public void setLastTranDate(Date lastTranDate) {
		this.lastTranDate = lastTranDate;
	}
	public void setFormSource(String formSource) {
		this.formSource = formSource;
	}
	public void setFormMethod(String formMethod) {
		this.formMethod = formMethod;
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
	public void setShotSize(int shotSize) {
		this.shotSize = shotSize;
	}
	public void setFormPct(int formPct) {
		this.formPct = formPct;
	}
	public void setVinylSafe(boolean vinylSafe) {
		this.vinylSafe = vinylSafe;
	}
	public void setPrimerId(String primerId) {
		this.primerId = primerId;
	}
	public void setRgbHex(String rgbHex) {
		this.rgbHex = rgbHex;
	}
	public void setDeltaEPrimary(double deltaEPrimary) {
		this.deltaEPrimary = deltaEPrimary;
	}
	public void setDeltaESecondary(double deltaESecondary) {
		this.deltaESecondary = deltaESecondary;
	}
	public void setDeltaETertiary(double deltaETertiary) {
		this.deltaETertiary = deltaETertiary;
	}
	public void setAverageDeltaE(double averageDeltaE) {
		this.averageDeltaE = averageDeltaE;
	}
	public void setCrThick(double crThick) {
		this.crThick = crThick;
	}
	public void setCrThin(double crThin) {
		this.crThin = crThin;
	}
	public void setEngDecisionValue(double engDecisionValue) {
		this.engDecisionValue = engDecisionValue;
	}
	public void setFormulaRule(String formulaRule) {
		this.formulaRule = formulaRule;
	}
	public void setIllumPrimary(String illumPrimary) {
		this.illumPrimary = illumPrimary;
	}
	public void setIllumSecondary(String illumSecondary) {
		this.illumSecondary = illumSecondary;
	}
	public void setIllumTertiary(String illumTertiary) {
		this.illumTertiary = illumTertiary;
	}
	public void setColorEngVer(String colorEngVer) {
		ColorEngVer = colorEngVer;
	}
	public void setSpd(double spd) {
		this.spd = spd;
	}
	public void setMetamerismIndex(double metamerismIndex) {
		this.metamerismIndex = metamerismIndex;
	}
	public void setFormulationTime(double formulationTime) {
		this.formulationTime = formulationTime;
	}
	public void setProjCurve(double[] projCurve) {
		this.projCurve = projCurve;
	}
//	public void setMeasCurve(BigDecimal[] measCurve) {
//		double[] tempCurve = new double[40];
//		Arrays.fill(tempCurve, 0D);
//		if(measCurve!=null) {
//			for(int i=0;i<40;i++){
//				tempCurve[i]=measCurve[i].doubleValue();
//			}
//		}
//
//		this.measCurve = tempCurve;
//	}
	public void setMeasCurve(double[] measCurve) {
		//System.out.println("inside setMeasCurve double");
		this.measCurve = measCurve;
	}
	public void setJobField01(String jobField01) {
		this.jobField01 = jobField01;
	}
	public void setJobField02(String jobField02) {
		this.jobField02 = jobField02;
	}
	public void setJobField03(String jobField03) {
		this.jobField03 = jobField03;
	}
	public void setJobField04(String jobField04) {
		this.jobField04 = jobField04;
	}
	public void setJobField05(String jobField05) {
		this.jobField05 = jobField05;
	}
	public void setJobField06(String jobField06) {
		this.jobField06 = jobField06;
	}
	public void setJobField07(String jobField07) {
		this.jobField07 = jobField07;
	}
	public void setJobField08(String jobField08) {
		this.jobField08 = jobField08;
	}
	public void setJobField09(String jobField09) {
		this.jobField09 = jobField09;
	}
	public void setJobField10(String jobField10) {
		this.jobField10 = jobField10;
	}
	public void setQuantityDispensed(int quantityDispensed) {
		this.quantityDispensed = quantityDispensed;
	}
	public void setOrigColorType(String origColorType) {
		this.origColorType = origColorType;
	}

	public void setOrigColorComp(String origColorComp) {
		this.origColorComp = origColorComp;
	}

	public void setOrigColorId(String origColorId) {
		this.origColorId = origColorId;
	}

	public void setOrigFormSource(String origFormSource) {
		this.origFormSource = origFormSource;
	}

	public void setOrigFormMethod(String origFormMethod) {
		this.origFormMethod = origFormMethod;
	}

	public void setOrigClrnt1(String origClrnt1) {
		this.origClrnt1 = origClrnt1;
	}

	public void setOrigClrntAmt1(int origClrntAmt1) {
		this.origClrntAmt1 = origClrntAmt1;
	}

	public void setOrigClrnt2(String origClrnt2) {
		this.origClrnt2 = origClrnt2;
	}

	public void setOrigClrntAmt2(int origClrntAmt2) {
		this.origClrntAmt2 = origClrntAmt2;
	}

	public void setOrigClrnt3(String origClrnt3) {
		this.origClrnt3 = origClrnt3;
	}

	public void setOrigClrntAmt3(int origClrntAmt3) {
		this.origClrntAmt3 = origClrntAmt3;
	}

	public void setOrigClrnt4(String origClrnt4) {
		this.origClrnt4 = origClrnt4;
	}

	public void setOrigClrntAmt4(int origClrntAmt4) {
		this.origClrntAmt4 = origClrntAmt4;
	}

	public void setOrigClrnt5(String origClrnt5) {
		this.origClrnt5 = origClrnt5;
	}

	public void setOrigClrntAmt5(int origClrntAmt5) {
		this.origClrntAmt5 = origClrntAmt5;
	}

	public void setOrigClrnt6(String origClrnt6) {
		this.origClrnt6 = origClrnt6;
	}

	public void setOrigClrntAmt6(int origClrntAmt6) {
		this.origClrntAmt6 = origClrntAmt6;
	}

	public void setOrigClrnt7(String origClrnt7) {
		this.origClrnt7 = origClrnt7;
	}

	public void setOrigClrntAmt7(int origClrntAmt7) {
		this.origClrntAmt7 = origClrntAmt7;
	}

	public void setOrigClrnt8(String origClrnt8) {
		this.origClrnt8 = origClrnt8;
	}

	public void setOrigClrntAmt8(int origClrntAmt8) {
		this.origClrntAmt8 = origClrntAmt8;
	}

	
	
}
