package com.sherwin.shercolor.cal.web.action;

import java.sql.SQLException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.opensymphony.xwork2.ActionSupport;
import com.sherwin.shercolor.cal.domain.CalTemplate;
import com.sherwin.shercolor.cal.service.CalService;

@Component
public class CalTemplateAction extends ActionSupport{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
    private final static Logger log = LogManager.getLogger(CalTemplateAction.class);

	@Autowired
	CalService service;
	
	String filename;
	byte[] data;
	
	public String GetCalTemplate(){

	
		CalTemplate cal = service.getCalTemplate(filename);
		if(cal != null){
			try {
				data = cal.getData().getBytes(1, (int)cal.getData().length());
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return SUCCESS;
		}
		else{
			return com.opensymphony.xwork2.Action.ERROR;
		}
		
	}

	
	public String getFilename() {
		return filename;
	}

	public void setFilename(String filename) {
		this.filename = filename;
	}

	public byte[] getData() {
		return data;
	}

	public void setData(byte[] data) {
		this.data = data;
	}

	
	
}
