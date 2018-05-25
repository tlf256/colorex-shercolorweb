package com.sherwin.shercolor.common.domain;

import com.google.gson.annotations.SerializedName;

public class OeFormRespMessage {

	@SerializedName("scRequestID")
	private String scRequestId;

	@SerializedName("messgeID")
	private String messageId;
	
	@SerializedName("dbRowID")
	private String dbRowId;
	
	@SerializedName("messageType")
	private String messageType;
	
	@SerializedName("messageText")
	private String messageText;
	
	@SerializedName("messageCode")
	private String messageCode;
	
	@SerializedName("messageDTZ")
	private String messageDTZ;
	
	
	public String getScRequestId() {
		return scRequestId;
	}
	public String getMessageId() {
		return messageId;
	}
	public String getDbRowId() {
		return dbRowId;
	}
	public String getMessageType() {
		return messageType;
	}
	public String getMessageText() {
		return messageText;
	}
	public String getMessageCode() {
		return messageCode;
	}
	public String getMessageDTZ() {
		return messageDTZ;
	}
	
	////////////////  SETTERS  //////////////////
	
	public void setScRequestId(String scRequestId) {
		this.scRequestId = scRequestId;
	}
	public void setMessageId(String messageId) {
		this.messageId = messageId;
	}
	public void setDbRowId(String dbRowId) {
		this.dbRowId = dbRowId;
	}
	public void setMessageType(String messageType) {
		this.messageType = messageType;
	}
	public void setMessageText(String messageText) {
		this.messageText = messageText;
	}
	public void setMessageCode(String messageCode) {
		this.messageCode = messageCode;
	}
	public void setMessageDTZ(String messageDTZ) {
		this.messageDTZ = messageDTZ;
	}
	
	
	
}
