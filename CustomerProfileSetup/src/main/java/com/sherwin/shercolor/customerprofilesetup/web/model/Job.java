package com.sherwin.shercolor.customerprofilesetup.web.model;

import java.util.ArrayList;
import java.util.List;

import org.owasp.encoder.Encode;

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
		if(screenLabel != null) {
			List<String> escScreenLabel = new ArrayList<String>();
			for(String sl : screenLabel) {
				if(sl != null) {
					Encode.forHtml(sl.trim());
				} else {
					continue;
				}
				escScreenLabel.add(sl);
				this.screenLabel = escScreenLabel;
			}
		} else {
			this.screenLabel = screenLabel;
		}
	}
	public List<String> getFieldDefault() {
		return fieldDefault;
	}
	public void setFieldDefault(List<String> fieldDefault) {
		if(fieldDefault != null) {
			List<String> escFieldDefault = new ArrayList<String>();
			for(String fd : fieldDefault) {
				if(fd != null) {
					Encode.forHtml(fd.trim());
				} else {
					continue;
				}
				escFieldDefault.add(fd);
				this.fieldDefault = escFieldDefault;
			}
		} else {
			this.fieldDefault = fieldDefault;
		}
	}
	public List<String> getEntryRequired() {
		return entryRequired;
	}
	public void setEntryRequired(List<String> entryRequired) {
		if(entryRequired != null) {
			List<String> escEntryRequired = new ArrayList<String>();
			for(String er : entryRequired) {
				if(er != null) {
					Encode.forHtmlAttribute(er.trim());
				} else {
					continue;
				}
				escEntryRequired.add(er);
				this.entryRequired = escEntryRequired;
			}
		} else {
			this.entryRequired = entryRequired;
		}
	}
	public List<String> getActive() {
		return active;
	}
	public void setActive(List<String> active) {
		if(active != null) {
			List<String> escActive = new ArrayList<String>();
			for(String act : active) {
				if(act != null) {
					Encode.forHtmlAttribute(act.trim());
				} else {
					continue;
				}
				escActive.add(act);
				this.entryRequired = escActive;
			}
		} else {
			this.active = active;
		}
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
