package com.sherwin.shercolor.common.exception;

import java.util.Locale;

import org.springframework.beans.factory.annotation.Autowired;

import org.hibernate.HibernateException;
import org.hibernate.JDBCException;

import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;
import org.apache.log4j.Logger;

import static com.sherwin.shercolor.common.exception.SherColorErrorCodes.*;

@Component
public class SherColorExceptionBuilderImpl implements	SherColorExceptionBuilder {
	
	private static final Logger log = Logger.getLogger(SherColorExceptionBuilderImpl.class);
	
	@Autowired
	private Locale        locale;
	
	@Autowired
	private MessageSource messageSource;

	public SherColorException buildfrom(RuntimeException exception) {

		doLog(exception);
		
		SherColorException result = null;
				
		if(exception instanceof ServiceException){
			result = buildFromServiceException((ServiceException) exception);
		}
		else if(exception instanceof HibernateException){
			result = buildFromHibernateException((HibernateException) exception);
		}
		else if(exception instanceof JDBCException){
			result = buildFromJDBCException((JDBCException) exception);
		}
		else{
			result = buildFromRuntimeException(exception);
		}
		
		return result;
	}
	
	protected void doLog(RuntimeException exception){
		log.error(exception);
	}
	
	
	private SherColorException buildFromServiceException(ServiceException exception) {
		int code = exception.getCode();
		Object[] params = exception.getParams();
		
		return doBuild(code, params);
	}
	
	private SherColorException buildFromHibernateException(HibernateException exception) {
		return doBuild(HIBERNATE_ERROR, null);
	}

	private SherColorException buildFromRuntimeException(RuntimeException exception) {
		return doBuild(GENERAL_SERVER_ERROR, null);
	}

	private SherColorException buildFromJDBCException(JDBCException exception) {
		return doBuild(JDBC_ERROR, null);
	}

	
	private SherColorException doBuild(int code, Object[] params){
		String messageCode = Integer.toString(code);

		String message = messageSource.getMessage(messageCode, params, locale);
		
		return new SherColorException(code, message);
	}

	public void setMessageSource(MessageSource messageSource) {
		this.messageSource = messageSource;
	}

	public void setLocale(Locale locale) {
		this.locale = locale;
	}
}
