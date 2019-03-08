package com.sherwin.shercolor.customerprofilesetup.web.model;

import java.util.List;

import com.sherwin.shercolor.customerprofilesetup.web.dto.JobFields;

public class Job {
	private List<String> screenLabel;
	private List<String> fieldDefault;
	private List<String> entryRequired;
	private List<String> active;
	private List<JobFields> jobFieldList;
	
	public List<String> getScreenLabel() {
		return screenLabel;
	}
	public void setScreenLabel(List<String> screenLabel) {
		this.screenLabel = screenLabel;
	}
	public List<String> getFieldDefault() {
		return fieldDefault;
	}
	public void setFieldDefault(List<String> fieldDefault) {
		this.fieldDefault = fieldDefault;
	}
	public List<String> getEntryRequired() {
		return entryRequired;
	}
	public void setEntryRequired(List<String> entryRequired) {
		this.entryRequired = entryRequired;
	}
	public List<String> getActive() {
		return active;
	}
	public void setActive(List<String> active) {
		this.active = active;
	}
	public List<JobFields> getJobFieldList() {
		return jobFieldList;
	}
	public void setJobFieldList(List<JobFields> jobFieldList) {
		this.jobFieldList = jobFieldList;
	}
	
	public void reset() {
		this.fieldDefault = null;
		this.entryRequired = null;
		this.screenLabel = null;
		this.active = null;
		this.jobFieldList = null;
	}
}
