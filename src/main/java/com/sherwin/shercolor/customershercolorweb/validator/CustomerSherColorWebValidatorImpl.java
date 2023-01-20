package com.sherwin.shercolor.customershercolorweb.validator;


import com.opensymphony.xwork2.validator.ValidationException;
import org.apache.commons.lang3.StringUtils;

public class CustomerSherColorWebValidatorImpl implements CustomerSherColorWebValidator {

	public void validateColor(String colorData) throws ValidationException {
		if(StringUtils.isEmpty(colorData)) {
			// "Customer name is not entered."
			throw new ValidationException();
		}
	}
}
