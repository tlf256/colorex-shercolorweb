package com.sherwin.shercolor.common.exception;

public interface SherColorErrorCodes {
	// General errors that should not happen.
	public static final int GENERAL_SERVER_ERROR = 505;
		
	// JDBC related errors
	public static final int GENERIC_JDBC_ERROR = 500;
		
	// JDBC related errors
	public static final int JDBC_ERROR = 503;
		
	// Hibernate related errors that are not JDBC
	public static final int HIBERNATE_ERROR = 504;

	// Client related errors
	public static final int GENERAL_ERROR   = 401;
}

