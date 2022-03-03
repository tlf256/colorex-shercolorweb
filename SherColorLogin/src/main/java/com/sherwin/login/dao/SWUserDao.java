package com.sherwin.login.dao;

import com.sherwin.login.domain.SWUser;

public interface SWUserDao {

	/***
	 * Read a SWUser record for the Login ID provided.
	 * @param loginID - Login ID
	 * @return Single SWUser record or null if not found
	 */
	public SWUser read(String loginID);
	
	/***
	 * Read a SWUser record for the email provided.
	 * @param email - the email
	 * @return Single SWUser record or null if not found
	 */
	public SWUser readByEmail(String email);
	
	/***
	 * Read a SWUser record for the Login ID provided
	 * and return the SWUser object. 
	 * @param loginID - Login ID
	 * @return the SWUser row data
	 */
	public SWUser getLoginUser(String inLoginId);
	
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
	
	
}
