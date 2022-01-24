package com.sherwin.shercolor.customershercolorweb.web.action;
import static org.junit.Assert.*;

import javax.servlet.http.HttpSession;

import com.sherwin.shercolor.customershercolorweb.annotation.SherColorWebTransactionalTest;
import org.apache.struts2.StrutsSpringJUnit4TestCase;

import org.junit.Test;

import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.google.gson.Gson;
import com.opensymphony.xwork2.ActionProxy;

import com.sherwin.shercolor.customershercolorweb.web.model.RequestObject;

import java.util.List;

@RunWith(SpringJUnit4ClassRunner.class)
@SherColorWebTransactionalTest
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
