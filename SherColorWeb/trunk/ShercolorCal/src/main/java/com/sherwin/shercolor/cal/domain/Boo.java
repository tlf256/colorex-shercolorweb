package com.sherwin.shercolor.cal.domain;



import java.io.Serializable;
import java.sql.Blob;

import javax.persistence.Entity;

import javax.persistence.Id;

import javax.persistence.Table;


@Entity
@Table(name="BOO")
public class Boo implements Serializable {
	
	
		private static final long serialVersionUID = 1L;
		
		
		@Id
		private String hoo;


		public String getHoo() {
			return hoo;
		}


		public void setHoo(String hoo) {
			this.hoo = hoo;
		}
		
		
		

	
	
}
