package com.sherwin.shercolor.customershercolorweb.interceptor;

import java.util.Map;

import org.slf4j.LoggerFactory;
import org.slf4j.Logger;
import org.apache.struts2.dispatcher.Parameter;

import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionInvocation;
import com.opensymphony.xwork2.interceptor.AbstractInterceptor;
import com.sherwin.shercolor.customershercolorweb.web.action.LoginAction;
import com.sherwin.shercolor.customershercolorweb.web.action.LoginRequired;
import com.sherwin.shercolor.customershercolorweb.web.model.RequestObject;


	@SuppressWarnings("serial")
	public class LoginInterceptor extends AbstractInterceptor {
		
		static Logger logger = LoggerFactory.getLogger(LoginInterceptor.class);
		
	    @Override
	    public String intercept(final ActionInvocation invocation) throws Exception {
	        Map<String, Object> session = ActionContext.getContext().getSession();
	        Map<String, Parameter> inParams = ActionContext.getContext().getParameters();
	        // sb: feel free to change this to some other type of an object which
	        // represents that the user is logged in. for this example, I am using
	        // an integer which would probably represent a primary key that I would
	        // look the user up by with Hibernate or some other mechanism.
//	        for (Map.Entry<String, Object> entry : inParams.entrySet()) {
//	        	logger.error("inParams Key : " + entry.getKey());
//	        	logger.error("inParams Value : " + entry.getValue());
//	        }
	        Object action = invocation.getAction();
	        
	        Class<? extends Object> actionclass = action.getClass();
	        String acct = "";
	        String reqGuid = "";
	        Parameter reqGuidObj;
	        //String[] reqGuidObj;
	        RequestObject reqObj;
	        int propcnt = 0;
	        
	        //logger.error("session keyset is " + session.keySet());
	        //logger.error("session.size is " + session.size());
	        //logger.error("inParams.size is " + inParams.size());
	        
	        try {
	        	
	        	reqGuidObj =   inParams.get("reqGuid");
	        	if (reqGuidObj== null || !reqGuidObj.isDefined()){
	        		reqGuid = null;
	        	} else {
	        		reqGuid = reqGuidObj.getValue();
	        	}
	        	logger.debug("reqGuid = " + reqGuid);
	        	if (reqGuid != null) {
	        		if (!reqGuid.isEmpty()) {
	        			reqObj = (RequestObject) session.get(reqGuid);
	        			
	        			if (reqObj == null) {
	        				logger.error("reqObj is null");
	        			}
	        			else{
	        				acct   = reqObj.getCustomerID();
	        			}
	        			//logger.error("acct is " + acct);
	        		} else {
		        		logger.error("reqGuid is empty");
	        		}
	        	} else {
	        		logger.error("reqGuid is null");
	        	}
	        } catch (Exception e) {
	        	logger.error(e.getMessage());
	        	e.printStackTrace();
	        	acct="";
	        }
	        
     
	        if(acct==null) {
	        	acct="";
	        }

	        // sb: if the user is already signed-in, then let the request through.
	        if (!acct.isEmpty()) {
	            return invocation.invoke();
	        }

	        

	        // sb: if the action doesn't require sign-in, then let it through.
	        if (!(action instanceof LoginRequired)) {
	            return invocation.invoke();
	        }

	        // sb: if this request does require login and the current action is
	        // not the login action, then redirect the user
	        if (!(action instanceof LoginAction)) {
	            return "loginRedirect";
	        }

	        // sb: they either requested the login page or are submitting their
	        // login now, let it through
	        return invocation.invoke();
	    }
}
