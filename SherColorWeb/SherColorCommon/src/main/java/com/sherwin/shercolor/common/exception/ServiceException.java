package com.sherwin.shercolor.common.exception;

public class ServiceException extends RuntimeException {

	private static final long serialVersionUID = 1L;
	
	private int code;
	private Object[] params;
	
	public ServiceException(int code, Object... params) {
		this.code = code;
		this.params = params;
	}

	public int getCode() {
		return code;
	}

	public Object[] getParams() {
		return params;
	}
	
}