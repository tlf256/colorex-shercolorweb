package com.sherwin.shercolor.cal.domain;



import java.io.Serializable;
import java.sql.Blob;

import javax.persistence.Entity;

import javax.persistence.Id;

import javax.persistence.Table;


@Entity
@Table(name="BLOB_TEST")
public class CalTemplate implements Serializable {
	
	
		private static final long serialVersionUID = 1L;
		
		
		@Id
		private String id;
		
		private Blob data;
	
		public String getId() {
			return id;
		}
	
		public void setId(String id) {
			this.id = id;
		}
	
		public Blob getData() {
			return data;
		}
	
		public void setData(Blob data) {
			this.data = data;
		}
	
		

	
	
}
