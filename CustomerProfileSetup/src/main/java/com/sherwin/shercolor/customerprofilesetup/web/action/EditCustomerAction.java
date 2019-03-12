package com.sherwin.shercolor.customerprofilesetup.web.action;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.struts2.interceptor.SessionAware;
import org.hibernate.HibernateException;
import com.opensymphony.xwork2.ActionSupport;
import com.sherwin.shercolor.customerprofilesetup.web.dto.CustParms;
import com.sherwin.shercolor.customerprofilesetup.web.dto.JobFields;
import com.sherwin.shercolor.customerprofilesetup.web.dto.LoginTrans;
import com.sherwin.shercolor.customerprofilesetup.web.model.Customer;
import com.sherwin.shercolor.customerprofilesetup.web.model.Job;
import com.sherwin.shercolor.customerprofilesetup.web.model.Login;

public class EditCustomerAction extends ActionSupport implements SessionAware {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	static Logger logger = LogManager.getLogger(EditCustomerAction.class);
	private Job job;
	private Login login;
	private Customer cust;
	private boolean updateMode;
	private Map<String, Object> sessionMap;
	
	public String execute() {
		try {
			List<CustParms> editCustList = new ArrayList<CustParms>();
			List<String> editclrntlist = new ArrayList<String>();
			List<LoginTrans> editLoginList = new ArrayList<LoginTrans>();
			List<JobFields> editJobList = new ArrayList<JobFields>();
			
			Customer customer = (Customer) sessionMap.get("CustomerDetail");
			for(int i = 0; i < cust.getClrntList().size(); i++) {
				editclrntlist.add(cust.getClrntList().get(i));
			}
			if(cust.getCce()!=null) {
				editclrntlist.add(cust.getCce());
			}
			if(cust.getBac()!=null) {
				editclrntlist.add(cust.getBac());
			}
			if(cust.getEef()!=null) {
				editclrntlist.add(cust.getEef());
			}
			
			for(int i = 0; i < editclrntlist.size(); i++) {
				//set values for custwebparms record
				CustParms newcust = new CustParms();
				newcust.setCustomerId(customer.getCustomerId());
				newcust.setSwuiTitle(cust.getSwuiTitle().trim());
				newcust.setClrntSysId(editclrntlist.get(i));
				newcust.setSeqNbr(i+1);
				newcust.setCdsAdlFld(cust.getCdsAdlFld().trim());
				newcust.setActive(cust.isActive());
				editCustList.add(newcust);
			}
			
			customer.setSwuiTitle(cust.getSwuiTitle().trim());
			customer.setCdsAdlFld(cust.getCdsAdlFld().trim());
			customer.setActive(cust.isActive());
			customer.setClrntList(editclrntlist);
			customer.setCustList(editCustList);
			sessionMap.put("CustomerDetail", customer);
			
			if(login.getKeyField()!=null) {
				for(int i = 0; i < login.getKeyField().size(); i++) {
					if(!login.getKeyField().get(i).equals("")) {
						//set values for custweblogintrans record
						LoginTrans newlogin = new LoginTrans();
						newlogin.setKeyField(login.getKeyField().get(i).trim());
						newlogin.setMasterAcctName(login.getMasterAcctName().get(i).trim());
						newlogin.setAcctComment(login.getAcctComment().get(i).trim());
						editLoginList.add(newlogin);
					}
				}
				if(sessionMap.containsKey("LoginDetail")) {
					Login logins = (Login) sessionMap.get("LoginDetail");
					logins.setLoginList(editLoginList);
					sessionMap.put("LoginDetail", logins);
				}else {
					login.setLoginList(editLoginList);
					sessionMap.put("LoginDetail", login);
				}
			}
			
			if(job.getScreenLabel()!=null) {
				Job jobs = (Job) sessionMap.get("JobDetail");
				String[] reqlist = new String[10];
				String[] actvlist = new String[10];
				int start = 0;
				int end = 9;
				int seqnbr = 1;
				//initial fill of string arrays with default false
				Arrays.fill(reqlist, start, end, "false");
				Arrays.fill(actvlist, start, end, "false");
				//insert true at checked index
				if(job.getEntryRequired()!=null) {
					for(int index = 0; index < job.getEntryRequired().size(); index++) {
						reqlist[Integer.parseInt(job.getEntryRequired().get(index))] = "true";
					}
				}
				if(job.getActive()!=null) {
					for(int index = 0; index < job.getActive().size(); index++) {
						actvlist[Integer.parseInt(job.getActive().get(index))] = "true";
					}
				}
				for(int i = 0; i < job.getScreenLabel().size(); i++) {
					if(!job.getScreenLabel().get(i).equals("")) {
						//set values for custwebjobfields record
						JobFields newjob = new JobFields();
						newjob.setSeqNbr(seqnbr);
						newjob.setScreenLabel(job.getScreenLabel().get(i).trim());
						newjob.setFieldDefault(job.getFieldDefault().get(i).trim());
						if(reqlist[i].contains("true")) {
							newjob.setEntryRequired(true);
						} else {
							newjob.setEntryRequired(false);
						}
						if(actvlist[i].contains("true")) {
							newjob.setActive(true);
						} else {
							newjob.setActive(false);
						}
						editJobList.add(newjob);
						seqnbr++;
					}
				}
				jobs.setJobFieldList(editJobList);
				sessionMap.put("JobDetail", jobs);
			}
			
			return SUCCESS;
			
		} catch (HibernateException he) {
			logger.error("HibernateException Caught: " + he.toString() + " " + he.getMessage());
			return ERROR;
		} catch (Exception e) {
			logger.error(e.getMessage());
			return ERROR;
		}
	}

	@Override
	public void setSession(Map<String, Object> sessionMap) {
		this.setSessionMap(sessionMap);
	}
	
	public Map<String, Object> getSessionMap() {
		return sessionMap;
	}
	
	public void setSessionMap(Map<String, Object> sessionMap) {
		this.sessionMap = sessionMap;
	}
	
	public Job getJob() {
		return job;
	}

	public void setJob(Job job) {
		this.job = job;
	}

	public Login getLogin() {
		return login;
	}

	public void setLogin(Login login) {
		this.login = login;
	}

	public Customer getCust() {
		return cust;
	}

	public void setCust(Customer cust) {
		this.cust = cust;
	}

	public boolean isUpdateMode() {
		return updateMode;
	}

	public void setUpdateMode(boolean updateMode) {
		this.updateMode = updateMode;
	}

}
