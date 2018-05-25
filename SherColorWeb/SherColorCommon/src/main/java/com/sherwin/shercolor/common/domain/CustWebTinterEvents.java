package com.sherwin.shercolor.common.domain;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

@Entity
@Table(name="CustWebTinterEvents")
@IdClass(CustWebTinterEventsPK.class)
public class CustWebTinterEvents {
    private String guid; 
    private String customerId;
    private String clrntSysId;
    private String tinterModel;
    private String tinterSerialNbr;
    private Date dateTime;
    private String function;
    private String appVersion;
    private String tinterDriverVersion;
    private String eventDetails;
    private String errorStatus;
    private String errorSeverity;
    private String errorNumber;
    private String errorMessage;
	
	@Id
	@Column(name="Guid")
	@NotNull
	public String getGuid() {
		return guid;
	}
	
	@Column(name="CustomerId")
	@NotNull
	public String getCustomerId() {
		return customerId;
	}

	@Column(name="ClrntSysId")
	@NotNull
	public String getClrntSysId() {
		return clrntSysId;
	}

	@Column(name="TinterModel")
	@NotNull
	public String getTinterModel() {
		return tinterModel;
	}

	@Column(name="TinterSerialNbr")
	@NotNull
	public String getTinterSerialNbr() {
		return tinterSerialNbr;
	}

	@Column(name="DateTime")
	public Date getDateTime() {
		return dateTime;
	}

	@Column(name="Function")
	public String getFunction() {
		return function;
	}

	@Column(name="AppVersion")
	public String getAppVersion() {
		return appVersion;
	}

	@Column(name="TinterDriverVersion")
	public String getTinterDriverVersion() {
		return tinterDriverVersion;
	}

	@Column(name="EventDetails")
	public String getEventDetails() {
		return eventDetails;
	}

	@Column(name="ErrorStatus")
	public String getErrorStatus() {
		return errorStatus;
	}

	@Column(name="ErrorSeverity")
	public String getErrorSeverity() {
		return errorSeverity;
	}

	@Column(name="ErrorNumber")
	public String getErrorNumber() {
		return errorNumber;
	}

	@Column(name="ErrorMessage")
	public String getErrorMessage() {
		return errorMessage;
	}

	/////////////// SETTERS ////////////////////

	public void setGuid(String guid) {
		this.guid = guid;
	}
	public void setCustomerId(String customerId) {
		this.customerId = customerId;
	}
	public void setClrntSysId(String clrntSysId) {
		this.clrntSysId = clrntSysId;
	}
	public void setTinterModel(String tinterModel) {
		this.tinterModel = tinterModel;
	}
	public void setTinterSerialNbr(String tinterSerialNbr) {
		this.tinterSerialNbr = tinterSerialNbr;
	}
	public void setDateTime(Date dateTime) {
		this.dateTime = dateTime;
	}
	public void setFunction(String function) {
		this.function = function;
	}
	public void setAppVersion(String appVersion) {
		this.appVersion = appVersion;
	}
	public void setTinterDriverVersion(String tinterDriverVersion) {
		this.tinterDriverVersion = tinterDriverVersion;
	}
	public void setEventDetails(String eventDetails) {
		this.eventDetails = eventDetails;
	}
	public void setErrorStatus(String errorStatus) {
		this.errorStatus = errorStatus;
	}
	public void setErrorSeverity(String errorSeverity) {
		this.errorSeverity = errorSeverity;
	}
	public void setErrorNumber(String errorNumber) {
		this.errorNumber = errorNumber;
	}
	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}
	
	

}
