package com.sherwin.shercolor.common.domain;


import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="CUSTWEBECAL")
public class CustWebEcal  {
		
		private String customerid;
		private String colorantid;
		private String tintermodel;
		private String tinterserial; 
		private String uploaddate; 
		private String uploadtime;
		@Id
		private String filename;
		private byte[] data;
		
		
		public String getCustomerid() {
			return customerid;
		}
		public void setCustomerid(String customerid) {
			this.customerid = customerid;
		}
	
		public String getColorantid() {
			return colorantid;
		}
		public void setColorantid(String colorantid) {
			this.colorantid = colorantid;
		}
		public String getTintermodel() {
			return tintermodel;
		}
		public void setTintermodel(String tintermodel) {
			this.tintermodel = tintermodel;
		}
		public String getTinterserial() {
			return tinterserial;
		}
		public void setTinterserial(String tinterserial) {
			this.tinterserial = tinterserial;
		}
		public String getUploaddate() {
			return uploaddate;
		}
		public void setUploaddate(String uploaddate) {
			this.uploaddate = uploaddate;
		}
		public String getUploadtime() {
			return uploadtime;
		}
		public void setUploadtime(String uploadtime) {
			this.uploadtime = uploadtime;
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
