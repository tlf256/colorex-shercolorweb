package com.sherwin.shercolor.customershercolorweb.web.action;
import static org.junit.Assert.*;

import java.io.IOException;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import java.util.Optional;

import javax.servlet.http.HttpSession;

import com.sherwin.shercolor.customershercolorweb.annotations.SherColorWebTest;
import org.apache.commons.lang3.StringUtils;
import org.apache.struts2.StrutsSpringJUnit4TestCase;
import org.apache.struts2.StrutsSpringTestCase;

import org.junit.Test;

import org.junit.runner.RunWith;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import com.google.gson.Gson;
import com.opensymphony.xwork2.ActionProxy;

import com.sherwin.shercolor.common.domain.CustWebEcal;
import com.sherwin.shercolor.customershercolorweb.web.action.EcalAction;
import com.sherwin.shercolor.customershercolorweb.web.model.RequestObject;

@RunWith(SpringJUnit4ClassRunner.class)
@SherColorWebTest
public class ConfigActionTest extends StrutsSpringJUnit4TestCase<ProcessTinterConfigAction> {

	ProcessTinterConfigAction target;
	RequestObject reqObj = new RequestObject();
	String reqGuid = "12345";

	@Test
	public void testGetTinterModelsAction(){

		ActionProxy proxy = getActionProxy("/GetTinterModelsAction");

		 target = (ProcessTinterConfigAction) proxy.getAction();
		 
		 target.setReqGuid("12345");
		
		target.setCustomerId("DEFAULT");
		target.setClrntSysId("CCE");
		
		 reqObj.setCustomerID("TEST");
		   
		    request.setParameter("reqGuid",reqGuid);
		    HttpSession session = request.getSession();
		    session.setAttribute(reqGuid, reqObj);
		
	


		try {
			String json = executeAction("/GetTinterModelsAction");
			assertNotNull(json);
			ProcessTinterConfigAction result = new Gson().fromJson(json,ProcessTinterConfigAction.class);
			//String result = proxy.execute();
			assertNotNull(result);
			
			List<String> list = result.getDefaultModelList();
			assertNotNull(list);
			for(String model: list){
				System.out.println("Model:" + model);
			}
			//System.out.println("Got my Cal for " + cal.getId());
			}
		 catch (Exception e) {
			// TODO Auto-generated catch block
			try {
				throw(e.getCause());
			} catch (Throwable e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			//e.printStackTrace();
		}		
	}	
	

}
