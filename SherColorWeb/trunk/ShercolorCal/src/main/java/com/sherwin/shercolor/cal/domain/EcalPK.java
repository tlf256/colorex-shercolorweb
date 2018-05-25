package com.sherwin.shercolor.cal.domain;

import java.io.Serializable;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;


	@SuppressWarnings("serial")
	public class EcalPK implements Serializable{
		private String customerid;
		private String filename;
		

		private static final long serialVersionUID = 1L;
		
		
		public String getCustomerid() {
			return customerid;
		}

		public void setCustomerid(String customerid) {
			this.customerid = customerid;
		}

		public String getFilename() {
			return filename;
		}

		public void setFilename(String filename) {
			this.filename = filename;
		}

		@Override
		public boolean equals(Object obj) {
	        return EqualsBuilder.reflectionEquals(this, obj);
	    }

		@Override
	    public int hashCode() {
	        return HashCodeBuilder.reflectionHashCode(this);
	    }

	}