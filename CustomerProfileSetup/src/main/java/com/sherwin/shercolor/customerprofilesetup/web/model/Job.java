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
	
	public List<String> getScreenLabel() {
		return screenLabel;
	}
	public void setScreenLabel(List<String> screenLabel) {
		if(screenLabel != null) {
			List<String> escScreenLabel = new ArrayList<String>();
			for(String sl : screenLabel) {
				Encode.forHtml(sl.trim());
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
				Encode.forHtml(fd.trim());
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
				Encode.forHtml(er.trim());
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
				Encode.forHtml(act.trim());
				escActive.add(act);
				this.active = escActive;
			}
		} else {
			this.active = active;
		}
	}
	
}
