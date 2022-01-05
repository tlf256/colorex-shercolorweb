package com.sherwin.login.util;

import java.util.regex.Pattern;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;


@Service
public class SWLoginValidator {
	static Logger logger = LogManager.getLogger(SWLoginValidator.class);
	private int minChars = 8;
	private int maxChars = 30;
	
	
    private static final Pattern [] passwordRegexes = new Pattern[4];
    {
    	//upper case regex
        passwordRegexes[0] = Pattern.compile(".*[A-Z].*");
        //lower case regex
        passwordRegexes[1] = Pattern.compile(".*[a-z].*");
        //numeric regex
        passwordRegexes[2] = Pattern.compile(".*\\d.*");
        //special character regex
        passwordRegexes[3] = Pattern.compile(".*[~!@#$%^&*].*");
    }

	
	public boolean validatePassword(String thePassword) {
		try {
			if (thePassword.length() < minChars) {
				return false;
			}
			if (thePassword.length() > maxChars) {
				return false;
			}
			
	        for(int i = 0; i < passwordRegexes.length; i++){
	            if(!passwordRegexes[i].matcher(thePassword).matches())
	                return false;
	        }
			
	        // matched all requirements, return true
	        return true;
		} catch (Exception e) {
			logger.error(e.getMessage());
			return false;
		}
		
	}

	public int getMinChars() {
		return minChars;
	}

	public void setMinChars(int minChars) {
		this.minChars = minChars;
	}

	public int getMaxChars() {
		return maxChars;
	}

	public void setMaxChars(int maxChars) {
		this.maxChars = maxChars;
	}
	
	
}
