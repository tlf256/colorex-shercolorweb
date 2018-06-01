package com.sherwin.login.domain;


import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import javax.persistence.GeneratedValue;
import javax.persistence.SequenceGenerator;


@Entity
@Table(name="SW_USER_COMMENTS")
@IdClass(SWUserCommentsPK.class)
public class SWUserComments {
	private int id;
	private String loginID; 
	private String createdBy; 
	private Date createdDate;
	private String comments;
	
	@Id
	@Column(name="ID")
	@GeneratedValue(generator="SW_USER_COMMENTS_SEQ")
	@SequenceGenerator(name="SW_USER_COMMENTS_SEQ", sequenceName="SW_USER_COMMENTS_SEQ", allocationSize=1)
	@NotNull
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	
	
	@Id
	@Column(name="LOGIN_ID")
	@NotNull
	public String getLoginID() {
		return loginID;
	}
	public void setLoginID(String loginID) {
		this.loginID = loginID;
	}

	@Column(name="CREATED_BY")	
	public String getCreatedBy() {
		return createdBy;
	}
	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}
	
	@Column(name="CREATED_TS")
	public Date getCreatedDate() {
		return createdDate;
	}
	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}
	
	@Column(name="COMMENTS")
	public String getComments() {
		return comments;
	}
	public void setComments(String comments) {
		this.comments = comments;
	}
	
	
}
