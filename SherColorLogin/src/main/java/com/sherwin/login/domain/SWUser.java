package com.sherwin.login.domain;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;


@Entity
@Table(name="SW_USER")
@IdClass(SWUserPK.class)
public class SWUser {
	private String loginID; 
	private Integer acctNbr; 
	private String firstName;
	private String LastName;
	private String eMail;
	private String phoneNbr;
	private String faxNbr; 
	private String residential; 
	private String commercial; 
	private String industrial; 
	private String interiorPaints;
	private String exteriorPaints;
	private String interiorWoodPaints;
	private String masonryConcreteCoatings;
	private String heavyDutyFloorCoatings;
	private String highPerformanceIndustrial;
	private String sprayEquipment;
	private String wallcoveringTools;
	private String drywallFinishingTools;
	private String fauxTools;
	private String noFullTimeEmployees;
	private String noGallonsPurchasedPerYear;
	private String receiveProbuy;
	private Integer originalAccount;
	private String activeUser;
	private String registeredUser;
	private String territory;
	private String district;
	private Integer homeStore;
	private Date changePassword;
	private String managerNbr;
	private String timeZone;
	private String SSN;
	private String territoryType;
	private Integer customerId;
	private String activationKey;
	private String parentNbr; 
	private String deletedUser;
	private Date lastLoginTime;
	private Date lastLoginTimeColor;
	private Date convertedDate; 
	private String convertedCustomerID;
	private Date convertedPaymentDate; 
	private String corpUser;
	private Date convertedColorDate;
	private String isSalesRep;
	private String gemsEmpID;
	private String isStoreEmp;
	private String mobilePhone;
	private Integer jobNameID;
	private String gemsSyncLock;
	private Date gemsSyncLockDate; 
	private String updatedBy;
	private String clearTrustYN;
	private String supervisorName;
	private String isStoreMgr;
	private String organizationName;
	private String jobName;
	private String isStoreAsstMgr;
	private Integer actingHomeStore;
	private String supervisorEmpID;
	
	@Id
	@Column(name="LOGIN_ID")
	@NotNull
	public String getLoginID() {
		return loginID;
	}
	public void setLoginID(String loginID) {
		this.loginID = loginID;
	}
	
	@Column(name="ACCT_NBR")
	public Integer getAcctNbr() {
		return acctNbr;
	}
	public void setAcctNbr(Integer acctNbr) {
		this.acctNbr = acctNbr;
	}
	
	@Column(name="FIRSTNAME")
	public String getFirstName() {
		return firstName;
	}
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	
	@Column(name="LASTNAME")
	public String getLastName() {
		return LastName;
	}
	public void setLastName(String lastName) {
		LastName = lastName;
	}
	
	@Column(name="EMAIL")
	public String geteMail() {
		return eMail;
	}
	public void seteMail(String eMail) {
		this.eMail = eMail;
	}
	
	@Column(name="PHONE")
	public String getPhoneNbr() {
		return phoneNbr;
	}
	public void setPhoneNbr(String phoneNbr) {
		this.phoneNbr = phoneNbr;
	}
	
	@Column(name="FAX")
	public String getFaxNbr() {
		return faxNbr;
	}
	public void setFaxNbr(String faxNbr) {
		this.faxNbr = faxNbr;
	}
	
	@Column(name="RESIDENTIAL")
	public String getResidential() {
		return residential;
	}
	public void setResidential(String residential) {
		this.residential = residential;
	}
	
	@Column(name="COMMERCIAL")
	public String getCommercial() {
		return commercial;
	}
	public void setCommercial(String commercial) {
		this.commercial = commercial;
	}
	
	@Column(name="INDUSTRIAL")
	public String getIndustrial() {
		return industrial;
	}
	public void setIndustrial(String industrial) {
		this.industrial = industrial;
	}
	
	@Column(name="INTERIOR_PAINTS")
	public String getInteriorPaints() {
		return interiorPaints;
	}
	public void setInteriorPaints(String interiorPaints) {
		this.interiorPaints = interiorPaints;
	}
	
	@Column(name="EXTERIOR_PAINTS")
	public String getExteriorPaints() {
		return exteriorPaints;
	}
	public void setExteriorPaints(String exteriorPaints) {
		this.exteriorPaints = exteriorPaints;
	}
	
	@Column(name="INTERIOR_WOOD_PAINTS")
	public String getInteriorWoodPaints() {
		return interiorWoodPaints;
	}
	public void setInteriorWoodPaints(String interiorWoodPaints) {
		this.interiorWoodPaints = interiorWoodPaints;
	}
	
	@Column(name="MASONRY_CONCRETE_COATINGS")
	public String getMasonryConcreteCoatings() {
		return masonryConcreteCoatings;
	}
	public void setMasonryConcreteCoatings(String masonryConcreteCoatings) {
		this.masonryConcreteCoatings = masonryConcreteCoatings;
	}
	
	@Column(name="HEAVY_DUTY_FLOOR_COATINGS")
	public String getHeavyDutyFloorCoatings() {
		return heavyDutyFloorCoatings;
	}
	public void setHeavyDutyFloorCoatings(String heavyDutyFloorCoatings) {
		this.heavyDutyFloorCoatings = heavyDutyFloorCoatings;
	}
	
	@Column(name="HIGH_PERFORMANCE_INDUSTRIAL")
	public String getHighPerformanceIndustrial() {
		return highPerformanceIndustrial;
	}
	public void setHighPerformanceIndustrial(String highPerformanceIndustrial) {
		this.highPerformanceIndustrial = highPerformanceIndustrial;
	}
	
	@Column(name="SPRAY_EQUIPMENT")
	public String getSprayEquipment() {
		return sprayEquipment;
	}
	public void setSprayEquipment(String sprayEquipment) {
		this.sprayEquipment = sprayEquipment;
	}
	
	@Column(name="WALLCOVERING_TOOLS")
	public String getWallcoveringTools() {
		return wallcoveringTools;
	}
	public void setWallcoveringTools(String wallcoveringTools) {
		this.wallcoveringTools = wallcoveringTools;
	}
	
	@Column(name="DRYWALL_FINISHING_TOOLS")
	public String getDrywallFinishingTools() {
		return drywallFinishingTools;
	}
	public void setDrywallFinishingTools(String drywallFinishingTools) {
		this.drywallFinishingTools = drywallFinishingTools;
	}
	
	@Column(name="FAUX_TOOLS")
	public String getFauxTools() {
		return fauxTools;
	}
	public void setFauxTools(String fauxTools) {
		this.fauxTools = fauxTools;
	}
	
	@Column(name="NO_FULL_TIME_EMPLOYEES")
	public String getNoFullTimeEmployees() {
		return noFullTimeEmployees;
	}
	public void setNoFullTimeEmployees(String noFullTimeEmployees) {
		this.noFullTimeEmployees = noFullTimeEmployees;
	}
	
	@Column(name="NO_GALLONS_PURCHASED_PER_YEAR")
	public String getNoGallonsPurchasedPerYear() {
		return noGallonsPurchasedPerYear;
	}
	public void setNoGallonsPurchasedPerYear(String noGallonsPurchasedPerYear) {
		this.noGallonsPurchasedPerYear = noGallonsPurchasedPerYear;
	}
	
	@Column(name="RECEIVE_PROBUY")
	public String getReceiveProbuy() {
		return receiveProbuy;
	}
	public void setReceiveProbuy(String receiveProbuy) {
		this.receiveProbuy = receiveProbuy;
	}
	
	@Column(name="ORIGINALACCOUNT")
	public Integer getOriginalAccount() {
		return originalAccount;
	}
	public void setOriginalAccount(Integer originalAccount) {
		this.originalAccount = originalAccount;
	}
	
	@Column(name="ACTIVE_USER")
	public String getActiveUser() {
		return activeUser;
	}
	public void setActiveUser(String activeUser) {
		this.activeUser = activeUser;
	}
	
	@Column(name="REGISTERED_USER")
	public String getRegisteredUser() {
		return registeredUser;
	}
	public void setRegisteredUser(String registeredUser) {
		this.registeredUser = registeredUser;
	}
	
	@Column(name="TERRITORY")
	public String getTerritory() {
		return territory;
	}
	public void setTerritory(String territory) {
		this.territory = territory;
	}
	
	@Column(name="DISTRICT")
	public String getDistrict() {
		return district;
	}
	public void setDistrict(String district) {
		this.district = district;
	}
	
	@Column(name="HOMESTORE")
	public Integer getHomeStore() {
		return homeStore;
	}
	public void setHomeStore(Integer homeStore) {
		this.homeStore = homeStore;
	}
	
	@Column(name="CHANGE_PASSWORD")
	public Date getChangePassword() {
		return changePassword;
	}
	public void setChangePassword(Date changePassword) {
		this.changePassword = changePassword;
	}
	
	@Column(name="MANAGER_NBR")
	public String getManagerNbr() {
		return managerNbr;
	}
	public void setManagerNbr(String managerNbr) {
		this.managerNbr = managerNbr;
	}
	
	@Column(name="TIME_ZONE")
	public String getTimeZone() {
		return timeZone;
	}
	public void setTimeZone(String timeZone) {
		this.timeZone = timeZone;
	}
	
	@Column(name="SSN")
	public String getSSN() {
		return SSN;
	}
	public void setSSN(String sSN) {
		SSN = sSN;
	}
	
	@Column(name="TERRITORY_TYPE")
	public String getTerritoryType() {
		return territoryType;
	}
	public void setTerritoryType(String territoryType) {
		this.territoryType = territoryType;
	}
	
	@Column(name="CUSTOMER_ID")
	public Integer getCustomerId() {
		return customerId;
	}
	public void setCustomerId(Integer customerId) {
		this.customerId = customerId;
	}
	
	@Column(name="ACTIVATION_KEY")
	public String getActivationKey() {
		return activationKey;
	}
	public void setActivationKey(String activationKey) {
		this.activationKey = activationKey;
	}
	
	@Column(name="PARENT_NUMBER")
	public String getParentNbr() {
		return parentNbr;
	}
	public void setParentNbr(String parentNbr) {
		this.parentNbr = parentNbr;
	}
	
	@Column(name="DELETED_USER")
	public String getDeletedUser() {
		return deletedUser;
	}
	public void setDeletedUser(String deletedUser) {
		this.deletedUser = deletedUser;
	}
	
	@Column(name="LAST_LOGIN_TIME")
	public Date getLastLoginTime() {
		return lastLoginTime;
	}
	public void setLastLoginTime(Date lastLoginTime) {
		this.lastLoginTime = lastLoginTime;
	}
	
	@Column(name="LAST_LOGIN_TIME_COLOR")
	public Date getLastLoginTimeColor() {
		return lastLoginTimeColor;
	}
	public void setLastLoginTimeColor(Date lastLoginTimeColor) {
		this.lastLoginTimeColor = lastLoginTimeColor;
	}
	
	@Column(name="CONVERTED_DATE")
	public Date getConvertedDate() {
		return convertedDate;
	}
	public void setConvertedDate(Date convertedDate) {
		this.convertedDate = convertedDate;
	}
	
	@Column(name="CONVERTED_CUSTOMER_ID")
	public String getConvertedCustomerID() {
		return convertedCustomerID;
	}
	public void setConvertedCustomerID(String convertedCustomerID) {
		this.convertedCustomerID = convertedCustomerID;
	}
	
	@Column(name="CONVERTED_PAYMENT_DATE")
	public Date getConvertedPaymentDate() {
		return convertedPaymentDate;
	}
	public void setConvertedPaymentDate(Date convertedPaymentDate) {
		this.convertedPaymentDate = convertedPaymentDate;
	}
	
	@Column(name="CORP_USER")
	public String getCorpUser() {
		return corpUser;
	}
	public void setCorpUser(String corpUser) {
		this.corpUser = corpUser;
	}
	
	@Column(name="CONVERTED_COLOR_DATE")
	public Date getConvertedColorDate() {
		return convertedColorDate;
	}
	public void setConvertedColorDate(Date convertedColorDate) {
		this.convertedColorDate = convertedColorDate;
	}
	
	@Column(name="IS_SALES_REP")
	public String getIsSalesRep() {
		return isSalesRep;
	}
	public void setIsSalesRep(String isSalesRep) {
		this.isSalesRep = isSalesRep;
	}
	
	@Column(name="GEMS_EMP_ID")
	public String getGemsEmpID() {
		return gemsEmpID;
	}
	public void setGemsEmpID(String gemsEmpID) {
		this.gemsEmpID = gemsEmpID;
	}
	
	@Column(name="IS_STORE_EMP")
	public String getIsStoreEmp() {
		return isStoreEmp;
	}
	public void setIsStoreEmp(String isStoreEmp) {
		this.isStoreEmp = isStoreEmp;
	}
	
	@Column(name="MOBILE_PHONE")
	public String getMobilePhone() {
		return mobilePhone;
	}
	public void setMobilePhone(String mobilePhone) {
		this.mobilePhone = mobilePhone;
	}
	
	@Column(name="JOB_NAME_ID")
	public Integer getJobNameID() {
		return jobNameID;
	}
	public void setJobNameID(Integer jobNameID) {
		this.jobNameID = jobNameID;
	}
	
	@Column(name="GEMS_SYNC_LOCK")
	public String getGemsSyncLock() {
		return gemsSyncLock;
	}
	public void setGemsSyncLock(String gemsSyncLock) {
		this.gemsSyncLock = gemsSyncLock;
	}
	
	@Column(name="GEMS_SYNC_LOCK_DATE")
	public Date getGemsSyncLockDate() {
		return gemsSyncLockDate;
	}
	public void setGemsSyncLockDate(Date gemsSyncLockDate) {
		this.gemsSyncLockDate = gemsSyncLockDate;
	}
	
	@Column(name="UPDATED_BY")
	public String getUpdatedBy() {
		return updatedBy;
	}
	public void setUpdatedBy(String updatedBy) {
		this.updatedBy = updatedBy;
	}
	
	@Column(name="CLEAR_TRUST_YN")
	public String getClearTrustYN() {
		return clearTrustYN;
	}
	public void setClearTrustYN(String clearTrustYN) {
		this.clearTrustYN = clearTrustYN;
	}
	
	@Column(name="SUPERVISOR_NAME")
	public String getSupervisorName() {
		return supervisorName;
	}
	public void setSupervisorName(String supervisorName) {
		this.supervisorName = supervisorName;
	}
	
	@Column(name="IS_STORE_MGR")
	public String getIsStoreMgr() {
		return isStoreMgr;
	}
	public void setIsStoreMgr(String isStoreMgr) {
		this.isStoreMgr = isStoreMgr;
	}
	
	@Column(name="ORGANIZATION_NAME")
	public String getOrganizationName() {
		return organizationName;
	}
	public void setOrganizationName(String organizationName) {
		this.organizationName = organizationName;
	}
	
	@Column(name="JOB_NAME")
	public String getJobName() {
		return jobName;
	}
	public void setJobName(String jobName) {
		this.jobName = jobName;
	}
	
	@Column(name="IS_STORE_ASST_MGR")
	public String getIsStoreAsstMgr() {
		return isStoreAsstMgr;
	}
	public void setIsStoreAsstMgr(String isStoreAsstMgr) {
		this.isStoreAsstMgr = isStoreAsstMgr;
	}
	
	@Column(name="ACTING_HOMESTORE")
	public Integer getActingHomeStore() {
		return actingHomeStore;
	}
	public void setActingHomeStore(Integer actingHomeStore) {
		this.actingHomeStore = actingHomeStore;
	}
	
	@Column(name="SUPERVISOR_EMP_ID")
	public String getSupervisorEmpID() {
		return supervisorEmpID;
	}
	public void setSupervisorEmpID(String supervisorEmpID) {
		this.supervisorEmpID = supervisorEmpID;
	}
}
