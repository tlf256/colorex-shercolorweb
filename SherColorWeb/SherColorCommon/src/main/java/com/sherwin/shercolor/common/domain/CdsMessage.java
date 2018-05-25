package com.sherwin.shercolor.common.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;

@Entity
@Table(name="CDS_MESSAGE")
@IdClass(CdsMessagePK.class)
public class CdsMessage {

	private String cdsMessageId;
	private String module;
	private String messageText;
	private String cdsAdlFld;
	
	@Id
	@Column(name="cdsMessageId")
	public String getCdsMessageId() {
		return cdsMessageId;
	}

	@Id
	@Column(name="module")
	public String getModule() {
		return module;
	}
	
	@Column(name="messageText")
	public String getMessageText() {
		return messageText;
	}
	
	@Column(name="cds_adl_fld")
	public String getCdsAdlFld() {
		return cdsAdlFld;
	}
	
	//////////  SETTERS  ///////////////

	public void setCdsMessageId(String cdsMessageId) {
		this.cdsMessageId = cdsMessageId;
	}
	public void setModule(String module) {
		this.module = module;
	}
	public void setMessageText(String messageText) {
		this.messageText = messageText;
	}
	public void setCdsAdlFld(String cdsAdlFld) {
		this.cdsAdlFld = cdsAdlFld;
	}
	
	
}
