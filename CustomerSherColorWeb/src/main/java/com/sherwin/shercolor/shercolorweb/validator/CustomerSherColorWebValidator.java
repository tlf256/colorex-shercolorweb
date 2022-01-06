package com.sherwin.shercolor.shercolorweb.validator;

import com.opensymphony.xwork2.validator.ValidationException;

public interface CustomerSherColorWebValidator {

	public void validateColor(String colorName) throws ValidationException; 
	
}
