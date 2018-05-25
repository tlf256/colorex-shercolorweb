package com.sherwin.shercolor.common.exception;

public class SherColorException extends RuntimeException{

	private static final long serialVersionUID = 1L;

	private int code;
	private String message;
	

	public SherColorException(){
		super();
	}
	
	public SherColorException (int code, String message) {
		super();
		this.code = code;
		this.message = message;
	}

	public int getCode() {
		return code;
	}

	public String getMessage() {
		return message;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public void setCode(int code) {
		this.code = code;
	}

	public void setMessage(String message) {
		this.message = message;
	}
	
}
