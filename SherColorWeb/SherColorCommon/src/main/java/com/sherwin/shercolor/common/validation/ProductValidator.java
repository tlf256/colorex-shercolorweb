package com.sherwin.shercolor.common.validation;

import javax.validation.ValidationException;

public interface ProductValidator {

	public void checkPosProd(String salesNbr) throws ValidationException;
	
	public String checkPosProdByUpc(String salesNbr) throws ValidationException;
	
	public String checkPosProdByRex(String salesNbr) throws ValidationException;
	
	public void checkCdsProd(String salesNbr) throws ValidationException;
	
	public void checkSizeCode(String salesNbr) throws ValidationException;
	
}
