package com.sherwin.shercolor.customershercolorweb.web.action;



import java.util.Calendar;
import java.util.Date;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;


import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.struts2.ServletActionContext;
import org.apache.struts2.interceptor.SessionAware;
import org.hibernate.HibernateException;
import org.owasp.encoder.Encode;

import com.opensymphony.xwork2.ActionSupport;
import com.sherwin.shercolor.customershercolorweb.web.model.RequestObject;
import com.sherwin.ecom.security.assertion.SsoHelper;
import com.sherwin.login.domain.SWUser;
import com.sherwin.login.domain.SWUserComments;
import com.sherwin.login.service.SWUserCommentService;
import com.sherwin.login.service.SWUserService;


// BKP 2/16/2018 - new action used for SherColor login page display and evaluation.
public class LoginUserAction  extends ActionSupport  implements SessionAware  {
	public static final String ROLE_SHERCOLOR = "SherColorRole";
	private static final long serialVersionUID = 1L;
	private Map<String, Object> sessionMap;
	private String userId;
	private String userPass;
	private String guid1;
	private String firstName;
	private String lastName;
	private String acctNbr;
	private int homeStore;
	private String territory;
	private boolean isStoreEmp;
	private boolean isSalesRep;
	private String gemsEmpId;
	private Date changePasswordDate;
	private RequestObject reqObj;
	
	private SWUserService swUserService;
	private SWUserCommentService swUserCommentService;

	
//	private static ResourceBundle SQL_BUNDLE;
//
//	private static DataSource DS_SECURITY_POOL;
//
//	private static String DS_SECURITY_JNDI_NAME;


	//private static String SQL_USER_GROUP_JOB_CHECK_STMT;
	public static final String TXT_ZERO = "0";
	
	static Logger logger = LogManager.getLogger(LoginUserAction.class);
	
	public String display() {
		
		try {
			logger.debug("passing through LogerUserAction display method");
			return SUCCESS;
		
		} catch (HibernateException he) {
			logger.error("HibernateException Caught: " + he.toString() + " " + he.getMessage());
			return ERROR;
		} catch (Exception e) {
			logger.error("Exception Caught: " + e.toString() +  " " + e.getMessage());
			return ERROR;
		}
	}
	
	public String execute(){
		
		int loginAttemptCnt = 0;
		int sleepSeconds = 0;
		int sleepPower = 0;
		HttpServletRequest request = ServletActionContext.getRequest();
		Pattern pattern = Pattern.compile("[A-Za-z0-9_]+");
		
		try {
			//createSqlResources();
			// Get the number of Login attempts
			String loginAttempt = String.valueOf(sessionMap.get("sher-link-login-attempt"));
			if(loginAttempt != null){
				try {
					loginAttemptCnt = Integer.parseInt(loginAttempt);
				} catch (Exception e) {
					loginAttemptCnt = 1;
				}
			}
			
			// Get the User Name and Password from the form

			if (userId != null) {
				userId = userId.trim();
			}
			if (logger.isDebugEnabled()) {
				logger.debug("userId -> " + userId);
			}

			try {
				
				// BKP 2018-02-23 - don't think we need the below.  This confirms the login does or does not exist,
				// which, by way of the request.login, wouldn't we already know in our case?  Also, the below code
				// (GetSherwinLoginId) confirms that the login exists IGNORING CASE - which I don't think we want to do,
				// right?  Case should matter for a login id.
				
				// BKP 2018-03-22 - we do need to do this, apparently.  Not fond of the implementation method, and 
				// current Sher-Link behaviour allows for possible access of wrong SW_User record if multiple records
				// with same case-sensitive login_id (i.e. shercolortest, SherColorTest)
				
				String loginId = swUserService.getLoginId(userId);	//Get the loginID from the table instead of getting from the login page after authentication;
				if(loginId == null){
					throw new Exception("User "+  Encode.forHtml(userId)  + " does not exist.");
				}
				userId = loginId;  
				
				// Does userID match valid user ID pattern
				if (!pattern.matcher(userId).matches()) {
					throw new Exception("Login failed. This login ID failed pattern matching (A-Z/a-z/0-9).");
				}
				
				// Is Login Active
				if (isloginActive(userId) != true) {
					if (logger.isDebugEnabled())
						logger.debug("authentication failed, inactive login  -> " + Encode.forHtml(userId));
					throw new Exception("Login failed. This login has been disabled.");
				}
				
				// Do Login
				request.login(userId, userPass);
				
				// Check for SherColor Role.  If SherColor role, then re-direct to a SherColor URL
				if(request.isUserInRole(ROLE_SHERCOLOR)) {
					// Check Password Expiration
					//BKP 2018-02-23 - Sher-Link would check and possibly return a warning that the 
					//password was going to expire, and forward to another page.  Not sure what we
					//want to do here with that.

					// Redirect to SherColor URL
					// BKP 2018-02-19 Changed to just look up user id, first name, last name, account number,
					// and to post into session/or pass securely to login page.
					// BKP 2018-02-26 The 'isLoginActive' routine looked up the first and last names and acct number.
					// Return SUCCESS at this point.
					// Stuff these items in a session object and pass the guid to the LoginAction.
						reqObj = new RequestObject();
						guid1 = UUID.randomUUID().toString().replace("-", "");
						reqObj.setGuid(guid1);
						reqObj.setCustomerID(Encode.forHtml(acctNbr));
						reqObj.setFirstName(Encode.forHtml(firstName));
						reqObj.setLastName(Encode.forHtml(lastName));
						reqObj.setUserId(Encode.forHtml(userId));
						reqObj.setHomeStore(homeStore);
						reqObj.setTerritory(Encode.forHtml(territory));
						reqObj.setGemsEmpId(Encode.forHtml(gemsEmpId));
						reqObj.setSalesRep(isSalesRep);
						reqObj.setStoreEmp(isStoreEmp);
						sessionMap.put(reqObj.getGuid(), reqObj);
						String expiredPassResponse = checkExpiredPassword();
						if (!expiredPassResponse.isEmpty()) {
							return expiredPassResponse;
						} else {
							return SUCCESS;
						}
				} else {
					request.logout();
					throw new Exception("Login failed.  This is not a valid SherColor login.");
				}
			
			} catch (Exception e) {
				logger.error("sher-link authentication error for user -> " + userId);
				logger.error("sher-link authentication error is -> " + e.getMessage());
				
				// Check Number of Login Attempts
				loginAttemptCnt = checkNumberOfLoginAttempts(loginAttemptCnt, userId, request, e);
				
				loginAttemptCnt++;
				//to slow down DOS/DDOS, add a logarithmic sleep based on login attempts.
				sleepPower = loginAttemptCnt - 1;
				if (sleepPower < 0) {
					sleepPower = 0;
				}
	
				sleepSeconds = (int) java.lang.Math.pow(2,sleepPower);
				logger.info("sleeping for " + sleepSeconds + " seconds after login failure attempt " + loginAttemptCnt); 
				TimeUnit.SECONDS.sleep(sleepSeconds);
				sessionMap.put("sher-link-login-attempt", loginAttemptCnt);
				addFieldError("userId","Login/password combination failed. Please retry your request.");
				return INPUT;
			}			
		} catch (Exception e) {
			logger.error("Exception Caught: " + e.toString() +  " " + e.getMessage());
			return ERROR;
		}
		
	}
	
	private String checkExpiredPassword() throws Exception {
		String returnStatus = "";
		
		// Check Password Expiration (Unless they logged in with RSA or System Token or Clear Trust Only
//		if (loginMethod.equals(LoginHelper.LOCAL) && !sherwinUser.isClearTrustLoginRequired()){
			try {

				if (changePasswordDate != null) {
					Date todayDate = Calendar.getInstance().getTime();
					String ssoToken = SsoHelper.getInstance().getSystemToken();
					if ( (changePasswordDate.before(todayDate)) && (!userPass.equals(ssoToken))) {
						if (logger.isDebugEnabled())
							logger.debug("password expired");
						//addActionMessage("Password expired for user " + userId + ". Please retry your request.");
						//return INPUT;
						returnStatus = "expirepass";
					}
				}
				//what if the change password date is null? Is this initialized when the user is created?
			} catch (Exception e) {
				logger.error("Exception Caught: " + e.toString() +  " " + e.getMessage());
				returnStatus = INPUT;
			}
//		}
		return returnStatus;
	}
	

	
	private void disableActiveUser(String theUserId) {
		try {
			if(!swUserService.disableActiveUser(theUserId)) {
				//log an error that something happened (odds are, the DAO also logged it too)
				//then continue on. 
				logger.error("Attempt to disable active user " + theUserId + " failed");
			}
		} catch (Exception e) {
			logger.error(e.getMessage());
		}
	}

	private boolean isloginActive(String theUserId) {
		String activeUser = "";
		SWUser theUserRec;
		
		try {
			logger.info("in isLoginActive, theUserId is " + theUserId);
		
			theUserRec = swUserService.readSWUser(theUserId);
			
			if (theUserRec!=null) {
				activeUser = theUserRec.getActiveUser().toUpperCase().trim();
				if (activeUser.equals("Y")) {
					firstName = theUserRec.getFirstName();
					lastName = theUserRec.getLastName();
					acctNbr = Integer.toString(theUserRec.getAcctNbr());
					homeStore = theUserRec.getHomeStore();
					territory = theUserRec.getTerritory();
					gemsEmpId = theUserRec.getGemsEmpID();
					String theSalesRep = theUserRec.getIsSalesRep();
					
					if (theSalesRep==null) {
						theSalesRep="N";
					}
					//logger.error("theSalesRep is " + theSalesRep);
					String theStoreEmp = theUserRec.getIsStoreEmp();
					
					if (theStoreEmp==null) {
						theStoreEmp="N";
					}
					//logger.error("theStoreEmp is " + theStoreEmp);
					if (theSalesRep.trim().toUpperCase().equals("Y")) {
						isSalesRep = true;
					} else {
						isSalesRep = false;
					}
					if (theStoreEmp.trim().toUpperCase().equals("Y")) {
						isStoreEmp = true;
					} else {
						isStoreEmp = false;
					}
					changePasswordDate = theUserRec.getChangePassword();
					return true;
				} else {
					firstName = "";
					lastName = "";
					acctNbr = "";
					changePasswordDate = null;
					homeStore = 0;
					territory = "";
					gemsEmpId = "";
					isSalesRep = false;
					isStoreEmp = false;
					return false;
				}
			} else {
				firstName = "";
				lastName = "";
				acctNbr = "";
				changePasswordDate = null;
				homeStore = 0;
				territory = "";
				gemsEmpId = "";
				isSalesRep = false;
				isStoreEmp = false;
				return false;
			} 
		}
		catch(Exception e) {
			logger.error("LoginUserAction ERROR: user " + theUserId + " lookup failed", e);
			return false;
		}
	}
	

	
	private int checkNumberOfLoginAttempts(int loginAttemptCnt, String userName, HttpServletRequest request, Exception e) {
		
		try {
			if(loginAttemptCnt > 5) {
				disableActiveUser(userName);
				logger.error("Too many failed login attempts, account " + userName + " has been deactivated.");

				SWUserComments comment = new SWUserComments();
				comment.setLoginID(userName);
				comment.setCreatedBy("system");
				comment.setCreatedDate(new Date());
				comment.setComments("Too many failed login attempts, Login Id has been deactivated. IP Address : "+ request.getRemoteAddr() );
				swUserCommentService.createOrUpdateEntry(comment);

				loginAttemptCnt = 0;
			} 
		} catch (Exception e1) {
			e1.printStackTrace();
			//reset the login attempts if there's an error - don't want to end up having a timeout.
			loginAttemptCnt = 0;
		}		
		
		return loginAttemptCnt;
	}



	@Override
	public void setSession(Map<String, Object> sessionMap) {
		this.sessionMap = sessionMap;
	}
	
	public String getUserId() {
		return userId;
	}
	
	public void setUserId(String userId) {
		this.userId = Encode.forHtml(userId.trim());
	}
	
	public String getUserPass() {
		return userPass;
	}
	
	public void setUserPass(String userPass) {
		this.userPass = Encode.forHtml(userPass);
	}
	
	public String getGuid1() {
		return guid1;
	}

	public void setGuid1(String guid1) {
		this.guid1 = guid1;
	}

	public SWUserCommentService getSwUserCommentService() {
		return swUserCommentService;
	}

	public void setSwUserCommentService(SWUserCommentService swUserCommentService) {
		this.swUserCommentService = swUserCommentService;
	}

	public SWUserService getSwUserService() {
		return swUserService;
	}

	public void setSwUserService(SWUserService swUserService) {
		this.swUserService = swUserService;
	}


	


}
