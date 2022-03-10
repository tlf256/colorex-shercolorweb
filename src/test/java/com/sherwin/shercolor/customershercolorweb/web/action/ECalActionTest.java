package com.sherwin.shercolor.customershercolorweb.web.action;
import static org.junit.Assert.*;

import java.util.List;

import javax.servlet.http.HttpSession;

import com.sherwin.shercolor.customershercolorweb.annotation.SherColorWebTransactionalTest;
import org.apache.struts2.StrutsSpringJUnit4TestCase;
import org.junit.Ignore;
import org.junit.Test;

import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.google.gson.Gson;

import com.sherwin.shercolor.common.domain.CustWebEcal;
import com.sherwin.shercolor.customershercolorweb.web.model.RequestObject;

@RunWith(SpringJUnit4ClassRunner.class)
@SherColorWebTransactionalTest
public class ECalActionTest extends StrutsSpringJUnit4TestCase<EcalAction> {

	EcalAction target = new EcalAction();
	RequestObject reqObj = new RequestObject();
	String reqGuid = "12345";

	@Test
	public void testSelectGDataAction(){

	
	    try {
	    	 setUp();
		} catch (Exception e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}
	    String colorantid = "CCE";
	    reqObj.setCustomerID("TEST");
	    request.setParameter("colorantid",colorantid);
	    request.setParameter("reqGuid",reqGuid);
	    HttpSession session = request.getSession();
	    session.setAttribute(reqGuid, reqObj);
	 	

		try {
			
			String json = executeAction("/selectGData");
			assertNotNull(json);
			EcalAction result = new Gson().fromJson(json,EcalAction.class);
			byte[] data = result.getData();
			assertNotNull(data);
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
	
	@Ignore("ignore testSelectEcalAction")
	@Test
	public void JUNKtestSelectEcalAction(){

	
	    try {
	    	 setUp();
		} catch (Exception e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}
	    String filename = "CCE_COROBD600_434344_20170912_1131.zip";
	    reqObj.setCustomerID("TEST");
	    request.setParameter("filename",filename);
	    request.setParameter("reqGuid",reqGuid);
	    HttpSession session = request.getSession();
	    session.setAttribute(reqGuid, reqObj);
	 	

		try {
			
			String json = executeAction("/selectECal");
			assertNotNull(json);
			EcalAction result = new Gson().fromJson(json,EcalAction.class);
			byte[] data = result.getData();
			assertNotNull(data);
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
	
	@Test
	public void testGetEcalListAction(){
		 try {
	    	 setUp();
		} catch (Exception e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}
	    String filename = "CCE_COROBD600_434344_20170912_1131.zip";
	    reqObj.setCustomerID("TEST");
	    request.setParameter("filename",filename);
	    request.setParameter("reqGuid",reqGuid);
	    HttpSession session = request.getSession();
	    session.setAttribute(reqGuid, reqObj);
		
		
		request.setParameter("customerId","TEST");
		request.setParameter("colorantId","CCE");
		request.setParameter("tintermodel", "COROBD600");
		request.setParameter("tinterserial", "434344");
		
		// target.setReqGuid("12345");
	


	try {
			
			String json = executeAction("/getECalList");
			assertNotNull(json);
			EcalAction result = new Gson().fromJson(json,EcalAction.class);
			
			
			List<CustWebEcal> list = result.getEcalList();
			assertNotNull(list);
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
	/*  DJM cannot find a way to set parameter that is not a String, i.e. data
	@Test
	public void testUploadEcalAction(){
		//ActionProxy proxy = getActionProxy("/uploadEcal");

		// target = (EcalAction) proxy.getAction();
		 
		// String filename = "CCE_COROBD600_434344_20170912_1131.zip";
		 Calendar cal = Calendar.getInstance();
			final DateFormat sdf = new SimpleDateFormat("yyyyMMdd");
			final DateFormat ttt = new SimpleDateFormat("HHmmss");
			String date = (sdf.format(cal.getTime()));
			String time = (ttt.format(cal.getTime()));
			String filename = "CCE_COROBD600_434344_" + date + "_" + time + ".zip";
			System.out.println("filename=" +filename);
			 try {
		    	 setUp();
			} catch (Exception e2) {
				// TODO Auto-generated catch block
				e2.printStackTrace();
			}
		   
		    reqObj.setCustomerID("TEST");
		    request.setParameter("filename",filename);
		    request.setParameter("reqGuid",reqGuid);
		    HttpSession session = request.getSession();
		    session.setAttribute(reqGuid, reqObj);
			
			
			request.setParameter("customerid","TEST");
			request.setParameter("colorantid","CCE");
			request.setParameter("tintermodel", "COROBD600");
			request.setParameter("tinterserial", "434344");
			request.setParameter("uploaddate", date);
			request.setParameter("uploadtime", time);
		final String UPLOADDIR="\\SWUploads\\";
	
		Path path = Paths.get(UPLOADDIR + "CCE_COROBD600_434344_20170912_1131.zip");
		byte[] data=null;
		try {
			data = Files.readAllBytes(path);
			
			request.setParameter("data", data.toString());
			
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	


try {
			
			String json = executeAction("/uploadEcal");
			
			assertNotNull(json);
			System.out.println(json);
			//assertEquals("success",result);
			
			
			//assertEquals("success",result);
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
	
	*/
}
