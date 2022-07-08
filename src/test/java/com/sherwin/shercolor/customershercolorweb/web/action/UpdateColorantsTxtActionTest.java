package com.sherwin.shercolor.customershercolorweb.web.action;
import static org.junit.Assert.*;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.apache.struts2.StrutsSpringJUnit4TestCase;
import org.apache.struts2.StrutsSpringTestCase;
import org.apache.struts2.dispatcher.HttpParameters;
import org.apache.struts2.dispatcher.Parameter;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionTemplate;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionProxy;
import com.sherwin.shercolor.common.dao.CustWebColorantsTxtDao;
import com.sherwin.shercolor.common.domain.CustWebColorantsTxt;
import com.sherwin.shercolor.common.domain.CustWebEcal;
import com.sherwin.shercolor.customershercolorweb.web.action.EcalAction;
import com.sherwin.shercolor.customershercolorweb.web.model.RequestObject;
import com.sherwin.shercolor.customershercolorweb.web.model.TinterCanister;
import com.sherwin.shercolor.customershercolorweb.web.model.TinterInfo;



@Transactional
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:config/spring/shercolorcommon-test.xml"})
public class UpdateColorantsTxtActionTest extends StrutsSpringJUnit4TestCase<EcalAction> {

	UpdateColorantsTxtAction target = null;
	RequestObject reqObj = new RequestObject();
	String reqGuid = "12345";

	@Autowired
	CustWebColorantsTxtDao dao;

	@Autowired
	PlatformTransactionManager platformTransactionManager;

	
//create a colorantstxt to update
@Before
public void CreateOne() {
		CustWebColorantsTxt colorantsTxt = new CustWebColorantsTxt();
		
		colorantsTxt.setCustomerId("TEST");
		colorantsTxt.setClrntSysId("CCE");
		colorantsTxt.setTinterModel("COROB CUSTOM D200");
		colorantsTxt.setTinterSerialNbr("JUNIT");
		colorantsTxt.setClrntCode("B1");
		colorantsTxt.setPosition(5);
		colorantsTxt.setCurrentClrntAmount(55.2D);
		colorantsTxt.setFillAlarmLevel(32D);
		colorantsTxt.setMaxCanisterFill(232D);
		colorantsTxt.setFillStopLevel(1.2D);

		boolean result = new TransactionTemplate(platformTransactionManager).execute(ts -> {
			boolean results= dao.create(colorantsTxt);
			ts.flush();
			return results;
		});

		assertTrue(result);
	}

	
	
	@Test
	public void testExecuteAction(){

	
	    try {
	    	 setUp();
		} catch (Exception e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}
	    /*
	     * private String reqGuid;
	private String colorantSystem; 
	private String tinterModel; 
	private String tinterSerial;
	private ArrayList<CustWebColorantsTxt> colorantsTxtList;
	private ArrayList<TinterCanister> tinterCanisterList;
	     */
		ActionProxy proxy = getActionProxy("/updateColorantsTxtAction");
	
		
		
		target = (UpdateColorantsTxtAction) proxy.getAction();

		target.setReqGuid("12345");
	    String colorantid = "CCE";
	    reqObj.setCustomerID("TEST");
	    String tinterModel = "COROB CUSTOM D200";
	    String tinterSerial = "JUNIT";
	    target.setTinterModel(tinterModel);
	    target.setTinterSerial(tinterSerial);
	    target.setColorantSystem(colorantid);
	   
	    String json = "[{\"clrntCode\":\"R4\",\"position\":1,\"maxCanisterFill\":80},{\"clrntCode\":\"R3\",\"position\":2,\"maxCanisterFill\":80},{\"clrntCode\":\"Y1\",\"position\":3,\"maxCanisterFill\":80},{\"clrntCode\":\"NA\",\"position\":4,\"maxCanisterFill\":80},{\"clrntCode\":\"N1\",\"position\":5,\"maxCanisterFill\":80},{\"clrntCode\":\"G2\",\"position\":6,\"maxCanisterFill\":80},{\"clrntCode\":\"L1\",\"position\":7,\"maxCanisterFill\":80},{\"clrntCode\":\"R2\",\"position\":8,\"maxCanisterFill\":80},{\"clrntCode\":\"W1\",\"position\":9,\"maxCanisterFill\":80},{\"clrntCode\":\"Y3\",\"position\":10,\"maxCanisterFill\":80},{\"clrntCode\":\"B1\",\"position\":11,\"maxCanisterFill\":80},{\"clrntCode\":\"NA\",\"position\":12,\"maxCanisterFill\":80}]";
	    CustWebColorantsTxt[] arr = new Gson().fromJson(json, CustWebColorantsTxt[].class);
	    List<CustWebColorantsTxt> list = Arrays.asList(arr);
	    ArrayList<CustWebColorantsTxt> colorantsTxtList = new ArrayList<CustWebColorantsTxt>(list);
	    target.setColorantsTxtList(colorantsTxtList);
	  
		request.setParameter("reqGuid",reqGuid);
		HttpSession session = request.getSession();
		session.setAttribute(reqGuid, reqObj);
	   
	 	

		try {
			
			String jsonn = executeAction("/updateColorantsTxtAction");
			
			//now test to see that the amount is the 55.2 from the DB that we created above.
			JsonElement je = new JsonParser().parse(jsonn);
			JsonObject jo = je.getAsJsonObject();
			JsonArray ja = jo.getAsJsonArray("colorantsTxtList");
			for(JsonElement o : ja){
				JsonElement cc = o.getAsJsonObject().get("clrntCode");
				if(cc.getAsString().equals("B1")){
					double currentAmt = o.getAsJsonObject().get("currentClrntAmount").getAsDouble();
					assertEquals(55.2,currentAmt,.01);
				}
			}
			assertNotNull(jsonn);
			
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
