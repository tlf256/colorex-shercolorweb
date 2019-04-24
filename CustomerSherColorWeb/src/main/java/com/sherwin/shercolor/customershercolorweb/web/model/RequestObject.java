package com.sherwin.shercolor.customershercolorweb.web.model;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import org.owasp.encoder.Encode;

import com.sherwin.shercolor.common.domain.FormulaInfo;
import com.sherwin.shercolor.common.domain.FormulationResponse;
import com.sherwin.shercolor.util.domain.SwMessage;

public class RequestObject {
	private String guid;
	private String colorComp;
	private String colorID;
	private String colorName;
	private String salesNbr;
	private String prodNbr;
	private String quality;
	private String composite;
	private String base;
	private String finish;
	private String klass;
	private String intExt;
	private String sizeText;
	private String clrntSys;
	private String customerID;
	private String customerName;
	private String intBases;
	private String extBases;
	private FormulationResponse formResponse;
	private FormulaInfo displayFormula;
	private String rgbHex;
	private boolean vinylExclude;
	private String lightSource;
	private String colorType;
	private String primerId;
	private boolean colorVinylOnly;
	private boolean validationWarning;
	private String validationWarningSalesNbr;
	private Integer percentageFactor;
	private List<JobField> jobFieldList;
	private String firstName;
	private String lastName;
	private String userId;
	private int homeStore;
	private String territory;
	private boolean isStoreEmp;
	private String gemsEmpId;
	private boolean isSalesRep;
	private int controlNbr;
	private int lineNbr;
	private Date initTranDate;
	private Date lastTranDate;
	private String sizeCode;
	private List<SwMessage> displayMsgs;
	private List<SwMessage> canLabelMsgs;
	private String sherLinkURL;
	private String tinterSerialNbr;
	private String tinterModel;
	private String spectroSerialNbr;
	private String spectroModel;
	private BigDecimal[] curveArray;
	private String upc;
	private int quantityDispensed;
	private TinterInfo tinter;
	private SpectroInfo spectro;
	private int daysUntilPasswdExpire;

	public String getColorComp() {
		return colorComp;
	}
	public void setColorComp(String colorComp) {
		this.colorComp = colorComp;
	}
	
	public String getColorID() {
		return colorID;
	}
	public void setColorID(String colorID) {
		this.colorID = colorID;
	}
	
	public String getColorName() {
		return colorName;
	}
	public void setColorName(String colorName) {
		this.colorName = colorName;
	}
	
	public String getSalesNbr() {
		return salesNbr;
	}
	public void setSalesNbr(String salesNbr) {
		this.salesNbr = salesNbr;
	}
	
	public String getClrntSys() {
		return clrntSys;
	}
	public void setClrntSys(String clrntSys) {
		this.clrntSys = clrntSys;
	}
	public String getGuid() {
		return guid;
	}
	public void setGuid(String guid) {
		this.guid = guid;
	}
	public String getCustomerID() {
		return customerID;
	}
	public void setCustomerID(String customerID) {
		this.customerID = Encode.forHtml(customerID);
	}
	public String getCustomerName() {
		return customerName;
	}
	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}
	public String getIntBases() {
		return intBases;
	}
	public void setIntBases(String intBases) {
		this.intBases = intBases;
	}
	public String getExtBases() {
		return extBases;
	}
	public void setExtBases(String extBases) {
		this.extBases = extBases;
	}
	public FormulationResponse getFormResponse() {
		return formResponse;
	}
	public void setFormResponse(FormulationResponse formResponse) {
		this.formResponse = formResponse;
	}
	public FormulaInfo getDisplayFormula() {
		return displayFormula;
	}
	public void setDisplayFormula(FormulaInfo displayFormula) {
		this.displayFormula = displayFormula;
	}
	public String getQuality() {
		return quality;
	}
	public void setQuality(String quality) {
		this.quality = quality;
	}

	public String getProdNbr() {
		return prodNbr;
	}
	public void setProdNbr(String prodNbr) {
		this.prodNbr = prodNbr;
	}
	public String getBase() {
		return base;
	}
	public void setBase(String base) {
		this.base = base;
	}
	public String getComposite() {
		return composite;
	}
	public void setComposite(String composite) {
		this.composite = composite;
	}
	public String getFinish() {
		return finish;
	}
	public void setFinish(String finish) {
		this.finish = finish;
	}
	public String getSizeText() {
		return sizeText;
	}
	public void setSizeText(String sizeText) {
		this.sizeText = sizeText;
	}
	public String getKlass() {
		return klass;
	}
	public void setKlass(String klass) {
		this.klass = klass;
	}
	public String getIntExt() {
		return intExt;
	}
	public void setIntExt(String intExt) {
		this.intExt = intExt;
	}
	public String getRgbHex() {
		return rgbHex;
	}
	public void setRgbHex(String rgbHex) {
		this.rgbHex = rgbHex;
	}

	public String getLightSource() {
		return lightSource;
	}
	public void setLightSource(String lightSource) {
		this.lightSource = lightSource;
	}
	public String getColorType() {
		return colorType;
	}
	public void setColorType(String colorType) {
		this.colorType = colorType;
	}
	public boolean isVinylExclude() {
		return vinylExclude;
	}
	public void setVinylExclude(boolean vinylExclude) {
		this.vinylExclude = vinylExclude;
	}
	public String getPrimerId() {
		return primerId;
	}
	public void setPrimerId(String primerId) {
		this.primerId = primerId;
	}
	public boolean isColorVinylOnly() {
		return colorVinylOnly;
	}
	public void setColorVinylOnly(boolean colorVinylOnly) {
		this.colorVinylOnly = colorVinylOnly;
	}
	public boolean isValidationWarning() {
		return validationWarning;
	}
	public void setValidationWarning(boolean validationWarning) {
		this.validationWarning = validationWarning;
	}
	public String getValidationWarningSalesNbr() {
		return validationWarningSalesNbr;
	}
	public void setValidationWarningSalesNbr(String validationWarningSalesNbr) {
		this.validationWarningSalesNbr = validationWarningSalesNbr;
	}
	public Integer getPercentageFactor() {
		return percentageFactor;
	}
	public void setPercentageFactor(Integer percentageFactor) {
		this.percentageFactor = percentageFactor;
	}
	public List<JobField> getJobFieldList() {
		return jobFieldList;
	}
	public void setJobFieldList(List<JobField> jobFieldList) {
		this.jobFieldList = jobFieldList;
	}
	public String getFirstName() {
		return firstName;
	}
	public void setFirstName(String firstName) {
		this.firstName = Encode.forHtml(firstName);
	}
	public String getLastName() {
		return lastName;
	}
	public void setLastName(String lastName) {
		this.lastName = Encode.forHtml(lastName);
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public int getHomeStore() {
		return homeStore;
	}
	public void setHomeStore(int homeStore) {
		this.homeStore = homeStore;
	}
	public String getTerritory() {
		return territory;
	}
	public void setTerritory(String territory) {
		this.territory = territory;
	}
	public boolean isStoreEmp() {
		return isStoreEmp;
	}
	public void setStoreEmp(boolean isStoreEmp) {
		this.isStoreEmp = isStoreEmp;
	}
	public String getGemsEmpId() {
		return gemsEmpId;
	}
	public void setGemsEmpId(String gemsEmpId) {
		this.gemsEmpId = gemsEmpId;
	}
	public boolean isSalesRep() {
		return isSalesRep;
	}
	public void setSalesRep(boolean isSalesRep) {
		this.isSalesRep = isSalesRep;
	}
	public int getControlNbr() {
		return controlNbr;
	}
	public int getLineNbr() {
		return lineNbr;
	}
	public Date getInitTranDate() {
		return initTranDate;
	}
	public Date getLastTranDate() {
		return lastTranDate;
	}
	public void setControlNbr(int controlNbr) {
		this.controlNbr = controlNbr;
	}
	public void setLineNbr(int lineNbr) {
		this.lineNbr = lineNbr;
	}
	public void setInitTranDate(Date initTranDate) {
		this.initTranDate = initTranDate;
	}
	public void setLastTranDate(Date lastTranDate) {
		this.lastTranDate = lastTranDate;
	}
	public String getSizeCode() {
		return sizeCode;
	}
	public void setSizeCode(String sizeCode) {
		this.sizeCode = sizeCode;
	}
	public List<SwMessage> getDisplayMsgs() {
		return displayMsgs;
	}
	public void setDisplayMsgs(List<SwMessage> displayMsgs) {
		this.displayMsgs = displayMsgs;
	}
	public List<SwMessage> getCanLabelMsgs() {
		return canLabelMsgs;
	}
	public void setCanLabelMsgs(List<SwMessage> canLabelMsgs) {
		this.canLabelMsgs = canLabelMsgs;
	}

	//Added a reset function to reset the object for another pass, rather than creating a new object each time.
	public void reset() {
		this.guid = "";
		this.colorComp = "";
		this.colorID = "";
		this.colorName = "";
		this.salesNbr = "";
		this.prodNbr = "";
		this.quality = "";
		this.composite = "";
		this.base = "";
		this.finish = "";
		this.klass = "";
		this.intExt = "";
		this.sizeText = "";
		this.clrntSys = "";
		this.customerID = "";
		this.customerName = "";
		this.intBases = "";
		this.extBases = "";
		this.formResponse = null;
		this.displayFormula = null;
		this.rgbHex = "";
		this.vinylExclude = false;
		this.lightSource = "";
		this.colorType = "";
		this.primerId = "";
		this.colorVinylOnly = false;
		this.validationWarning = false;
		this.validationWarningSalesNbr = "";
		this.percentageFactor = 100;
		this.jobFieldList = null;
		this.firstName = "";
		this.lastName = "";
		this.userId = "";
		this.homeStore = 0;
		this.territory = "";
		this.gemsEmpId = "";
		this.isSalesRep = false;
		this.isStoreEmp = false;
		this.controlNbr = 0;
		this.lineNbr = 0;
		this.initTranDate = null;
		this.lastTranDate = null;
		this.sizeCode = "";
		this.displayMsgs = null;
		this.canLabelMsgs = null;
		this.upc = "";
		this.quantityDispensed = 0;
		this.daysUntilPasswdExpire = 90;
	}
	public String getSherLinkURL() {
		return sherLinkURL;
	}
	public void setSherLinkURL(String sherLinkURL) {
		this.sherLinkURL = sherLinkURL;
	}
	public String getTinterSerialNbr() {
		return tinterSerialNbr;
	}
	public void setTinterSerialNbr(String tinterSerialNbr) {
		this.tinterSerialNbr = tinterSerialNbr;
	}
	public String getTinterModel() {
		return tinterModel;
	}
	public void setTinterModel(String tinterModel) {
		this.tinterModel = tinterModel;
	}
	public String getSpectroSerialNbr() {
		return spectroSerialNbr;
	}
	public void setSpectroSerialNbr(String spectroSerialNbr) {
		this.spectroSerialNbr = spectroSerialNbr;
	}
	public String getSpectroModel() {
		return spectroModel;
	}
	public void setSpectroModel(String spectroModel) {
		this.spectroModel = spectroModel;
	}
	public BigDecimal[] getCurveArray() {
		return curveArray;
	}
	public void setCurveArray(BigDecimal[] curveArray) {
		this.curveArray = curveArray;
	}
	public String getUpc() {
		return upc;
	}
	public int getQuantityDispensed() {
		return quantityDispensed;
	}
	public TinterInfo getTinter() {
		return tinter;
	}
	public void setUpc(String upc) {
		this.upc = upc;
	}
	public void setQuantityDispensed(int quantityDispensed) {
		this.quantityDispensed = quantityDispensed;
	}
	public void setTinter(TinterInfo tinter) {
		this.tinter = tinter;
	}
	public SpectroInfo getSpectro() {
		return spectro;
	}
	public void setSpectro(SpectroInfo spectro) {
		this.spectro = spectro;
	}
	public int getDaysUntilPasswdExpire() {
		return daysUntilPasswdExpire;
	}
	public void setDaysUntilPasswdExpire(int daysUntilPasswdExpire) {
		this.daysUntilPasswdExpire = daysUntilPasswdExpire;
	}


}
