package com.sherwin.login.service;

import com.sherwin.login.domain.SWUser;

public interface SWUserService {
	/***
	 * Returns an SWUser given the login ID.
	 * @param login ID
	 * @return SWUser for the given login ID.
	 */
	public SWUser readSWUser(String loginID);
	
	/***
	 * disable a SWUser record for the Login ID provided.
	 * this involves a read, setting the ACTIVE_USER flag to N, 
	 * then updating it.
	 * @param loginID - Login ID
	 * @return boolean true if successfully updated, false if not.
	 */
	public boolean disableActiveUser(String loginID);
	
	/***
	 * Read a SWUser record for the Login ID provided
	 * and return the login ID.  Needed as the login user entered
	 * may have case issues compared to the SWUser entry.
	 * @param loginID - Login ID
	 * @return the loginID, or null if not found OR MULTIPLES FOUND
	 */
	public String getLoginId(String loginID);
	
	/***
	 * updates the password change date for the Login ID provided.
	 * this involves a read, calculating the change date based on
	 * the data read for the user, then updating it.
	 * @param loginID - Login ID
	 * @return boolean true if successfully updated, false if not.
	 */
	public boolean updatePasswordChangeDate(String loginID);

	/***
	 * Returns an SWUser given the login info.
	 * @param loginInfo Can be email or SW Id
	 * @return SWUser for the given login ID.
	 */
	public SWUser readUserByEmailOrLogin(String loginInfo);
}
