package com.sherwin.shercolor.customershercolorweb.validator;

import org.springframework.util.StringUtils;

import com.opensymphony.xwork2.validator.ValidationException;

public class CustomerSherColorWebValidatorImpl implements CustomerSherColorWebValidator {

	public void validateColor(String colorData) throws ValidationException {
		if(StringUtils.isEmpty(colorData)) {
			// "Customer name is not entered."
			throw new ValidationException();
		}
		return;
	}
}
