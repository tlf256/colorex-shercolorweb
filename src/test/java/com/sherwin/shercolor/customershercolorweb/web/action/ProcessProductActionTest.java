package com.sherwin.shercolor.customershercolorweb.web.action;

import javax.servlet.ServletException;
import javax.servlet.http.HttpSession;

import com.opensymphony.xwork2.ActionProxy;
import com.sherwin.shercolor.common.domain.CdsProdCharzd;
import com.sherwin.shercolor.common.domain.CustWebTran;
import com.sherwin.shercolor.common.service.TranHistoryService;
import com.sherwin.shercolor.customershercolorweb.annotation.SherColorWebTransactionalTest;
import org.apache.struts2.StrutsSpringJUnit4TestCase;
import org.junit.Test;

import com.sherwin.shercolor.customershercolorweb.web.model.RequestObject;
import com.sherwin.shercolor.util.domain.SwMessage;

import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.AutoConfigureTestEntityManager;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.UnsupportedEncodingException;
import java.util.Arrays;



@RunWith(SpringJUnit4ClassRunner.class)
@SherColorWebTransactionalTest
@AutoConfigureTestEntityManager
public class ProcessProductActionTest extends StrutsSpringJUnit4TestCase<ProcessProductAction> {

	@Autowired
	private TestEntityManager testentitymanager;
	
	RequestObject reqObj = new RequestObject();
	String reqGuid = "12345";
	ProcessProductAction target;

	@Test
	public void testfillProdNbrActionA() throws UnsupportedEncodingException, ServletException {
		System.out.println("Start testfillProdNbrActionA");
		
		reqObj.setCustomerID("CCF");
		request.setParameter("shorthandProdNbr", "a6w151");
		request.setParameter("reqGuid",reqGuid);
		
		HttpSession session = request.getSession();
		session.setAttribute(reqGuid, reqObj);

		String json = executeAction("/fillProdNbrAction");
		assertTrue(json.contains("A06W00151"));
		System.out.println("End testfillProdNbrActionA");
	}
	
	@Test
	public void testfillProdNbrActionB() throws UnsupportedEncodingException, ServletException {
		System.out.println("Start testfillProdNbrActionB");
		
		reqObj.setCustomerID("CCF");
		request.setParameter("shorthandProdNbr", "KW7730-16");
		request.setParameter("reqGuid",reqGuid);
		
		HttpSession session = request.getSession();
		session.setAttribute(reqGuid, reqObj);

		String json = executeAction("/fillProdNbrAction");
		assertTrue(json.contains("KW0007730-16"));
		System.out.println("End testfillProdNbrActionB");
	}
	
	@Test
	public void testfillProdNbrActionC() throws UnsupportedEncodingException, ServletException {
		System.out.println("Start testfillProdNbrActionC");
		
		reqObj.setCustomerID("CCF");
		request.setParameter("shorthandProdNbr", "a8t154 20");
		request.setParameter("reqGuid",reqGuid);
		
		HttpSession session = request.getSession();
		session.setAttribute(reqGuid, reqObj);

		String json = executeAction("/fillProdNbrAction");
		assertTrue(json.contains("A08T00154-20"));
		System.out.println("End testfillProdNbrActionC");
	}
	
	@Test
	public void testListProductsFillProdA() throws UnsupportedEncodingException, ServletException {
		System.out.println("Start testListProductsFillProdA");
		
		reqObj.setCustomerID("CCF");
		reqObj.setColorType("SW");
		reqObj.setColorID("GYPSY RED");
		reqObj.setColorID("6865");
		reqObj.setColorComp("SHERWIN-WILLIAMS");
		reqObj.setIntBases("REAL RED,CHALKY UD,ULTRADEEP,TINT BASE");
		reqObj.setExtBases("ULTRADEEP,CHROMA,DEEPTONE,REAL RED,TINT BASE");
		request.setParameter("partialProductNameOrId", "a8t154");
		request.setParameter("reqGuid",reqGuid);
		
		HttpSession session = request.getSession();
		session.setAttribute(reqGuid, reqObj);

		String json = executeAction("/listProducts");
		assertTrue(json.contains("A08T00154"));	
		System.out.println("End testListProductsFillProdA");
	}
	
	@Test
	public void testListProductsFillProdB() throws UnsupportedEncodingException, ServletException {
		System.out.println("Start testListProductsFillProdB");
		
		reqObj.setCustomerID("CCF");
		reqObj.setColorType("CUSTOM");
		reqObj.setColorID("RED");
		reqObj.setColorID("MANUAL");
		reqObj.setColorComp("CUSTOM");
		reqObj.setIntBases("");
		reqObj.setExtBases("");
		request.setParameter("partialProductNameOrId", "a8t154");
		request.setParameter("reqGuid",reqGuid);
		
		HttpSession session = request.getSession();
		session.setAttribute(reqGuid, reqObj);


		String json = executeAction("/listProducts");
		assertTrue(json.contains("A08T00154"));	
		System.out.println("End testListProductsFillProdB");
	}
	
	@Test
	public void testExecuteSuccess() throws UnsupportedEncodingException, ServletException { 
		ActionProxy proxy = getActionProxy("/ProcessProductAction");
		target = (ProcessProductAction) proxy.getAction();
		reqObj.setCustomerID("CCF");
		reqObj.setColorType("CUSTOM");
		reqObj.setColorID("RED");
		reqObj.setColorComp("CUSTOM");
		reqObj.setIntBases("");
		reqObj.setExtBases("");
		request.setParameter("partialProductNameOrId", "640389151");
		request.setParameter("reqGuid",reqGuid);
		
		HttpSession session = request.getSession();
		session.setAttribute(reqGuid, reqObj);

		String success = executeAction("/ProcessProductAction");
		assertNotNull(success);
	}
	
	@Test
	public void testExecuteInvalidProduct() throws UnsupportedEncodingException, ServletException { 
		ActionProxy proxy = getActionProxy("/ProcessProductAction");
		target = (ProcessProductAction) proxy.getAction();
		reqObj.setCustomerID("CCF");
		reqObj.setColorType("CUSTOM");
		reqObj.setColorID("RED");
		reqObj.setColorComp("CUSTOM");
		reqObj.setIntBases("");
		reqObj.setExtBases("");
		request.setParameter("partialProductNameOrId", "99ZZ99ZZ9");
		request.setParameter("reqGuid",reqGuid);
		
		HttpSession session = request.getSession();
		session.setAttribute(reqGuid, reqObj);

		String success = executeAction("/ProcessProductAction");
		assertNotNull(success);
	}
	
	@Test
	public void testExecuteInvalidSize() throws UnsupportedEncodingException, ServletException { 
		ActionProxy proxy = getActionProxy("/ProcessProductAction");
		target = (ProcessProductAction) proxy.getAction();
		reqObj.setCustomerID("CCF");
		reqObj.setColorType("SHERWIN-WILLIAMS");
		reqObj.setColorID("1001");
		reqObj.setColorComp("SHERWIN-WILLIAMS");
		reqObj.setIntBases("");
		reqObj.setExtBases("");
		request.setParameter("partialProductNameOrId", "100000157");
		request.setParameter("reqGuid",reqGuid);
		
		HttpSession session = request.getSession();
		session.setAttribute(reqGuid, reqObj);

		String success = executeAction("/ProcessProductAction");
		assertNotNull(success);
	}
	
	@Test
	public void testExecuteInvalidProductColor() throws UnsupportedEncodingException, ServletException { 
		ActionProxy proxy = getActionProxy("/ProcessProductAction");
		target = (ProcessProductAction) proxy.getAction();
		reqObj.setCustomerID("CCF");
		reqObj.setColorType("SHERWIN-WILLIAMS");
		reqObj.setColorID("VS301");
		reqObj.setColorComp("SHERWIN-WILLIAMS");
		reqObj.setIntBases("");
		reqObj.setExtBases("");
		request.setParameter("partialProductNameOrId", "640515755");
		request.setParameter("reqGuid",reqGuid);
		
		HttpSession session = request.getSession();
		session.setAttribute(reqGuid, reqObj);

		String success = executeAction("/ProcessProductAction");
		assertNotNull(success);
	}
	
	@Test
	public void testExecuteProductColorWarning() throws UnsupportedEncodingException, ServletException {
		ActionProxy proxy = getActionProxy("/ProcessProductAction");
		target = (ProcessProductAction) proxy.getAction();
		reqObj.setCustomerID("CCF");
		reqObj.setColorType("SHERWIN-WILLIAMS");
		reqObj.setColorID("0057");
		reqObj.setColorComp("SHERWIN-WILLIAMS");
		reqObj.setIntBases("");
		reqObj.setExtBases("");
		request.setParameter("partialProductNameOrId", "640515755");
		request.setParameter("reqGuid",reqGuid);
		
		HttpSession session = request.getSession();
		session.setAttribute(reqGuid, reqObj);

		String success = executeAction("/ProcessProductAction");
		assertNotNull(success);
	}
	
	@Test
	public void testExecuteProductWarning() throws UnsupportedEncodingException, ServletException {
		ActionProxy proxy = getActionProxy("/ProcessProductAction");
		target = (ProcessProductAction) proxy.getAction();
		reqObj.setCustomerID("CCF");
		reqObj.setColorType("CUSTOM");
		reqObj.setColorID("0057");
		reqObj.setColorComp("CUSTOM");
		reqObj.setIntBases("");
		reqObj.setExtBases("");
		request.setParameter("partialProductNameOrId", "100000157");
		request.setParameter("reqGuid",reqGuid);
		
		HttpSession session = request.getSession();
		session.setAttribute(reqGuid, reqObj);

		String success = executeAction("/ProcessProductAction");
		assertNotNull(success);
	}
	
	@Test
	public void testcheckIlluminatedProduct() throws UnsupportedEncodingException, ServletException {
		ActionProxy proxy = getActionProxy("/checkForIlluminationAction");
		target = (ProcessProductAction) proxy.getAction();
		CustWebTran custWebTran = new CustWebTran();
		custWebTran.setIllumPrimary("D65");
		custWebTran.setCustomerId("CCF");
		custWebTran.setControlNbr(1000);
		custWebTran.setLineNbr(1);
		testentitymanager.persistAndFlush(custWebTran);
		target.setReqGuid(reqGuid);
		reqObj.setCustomerID("CCF");
		reqObj.setControlNbr(1000);
		reqObj.setLineNbr(1);
		
		HttpSession session = request.getSession();
		session.setAttribute(reqGuid, reqObj);
		
		String success = executeAction("/checkForIlluminationAction");
		assertTrue(success.contains("D65"));
	}
	
	@Test
	public void testcheckIlluminatedProductMissingIllum() throws UnsupportedEncodingException, ServletException {
		ActionProxy proxy = getActionProxy("/checkForIlluminationAction");
		target = (ProcessProductAction) proxy.getAction();
		CustWebTran custWebTran = new CustWebTran();
		custWebTran.setCustomerId("CCF");
		custWebTran.setControlNbr(1000);
		custWebTran.setLineNbr(1);
		testentitymanager.persistAndFlush(custWebTran);
		target.setReqGuid(reqGuid);
		reqObj.setCustomerID("CCF");
		reqObj.setControlNbr(1000);
		reqObj.setLineNbr(1);
		
		HttpSession session = request.getSession();
		session.setAttribute(reqGuid, reqObj);
		
		String success = executeAction("/checkForIlluminationAction");
		assertTrue(success.contains("\"customerId\":null"));
	}
	
	@Test
	public void testcheckIlluminatedProductReadFail() throws UnsupportedEncodingException, ServletException {
		ActionProxy proxy = getActionProxy("/checkForIlluminationAction");
		target = (ProcessProductAction) proxy.getAction();
		target.setReqGuid(reqGuid);
		reqObj.setCustomerID("CCF");
		reqObj.setControlNbr(1000);
		reqObj.setLineNbr(1);
		
		HttpSession session = request.getSession();
		session.setAttribute(reqGuid, reqObj);
		
		String success = executeAction("/checkForIlluminationAction");
		assertTrue(success.contains("\"customerId\":null"));
	}
	
	@Test
	public void testcheckCharacterizedProduct() throws UnsupportedEncodingException, ServletException {
		ActionProxy proxy = getActionProxy("/checkForCharacterizationAction");
		target = (ProcessProductAction) proxy.getAction();
		CdsProdCharzd cdsprodcharzd = new CdsProdCharzd();
		cdsprodcharzd.setProdNbr("temp");
		cdsprodcharzd.setClrntSysId("test");
		cdsprodcharzd.setIsWhite(true);

		testentitymanager.persistAndFlush(cdsprodcharzd);
		target.setReqGuid(reqGuid);
		reqObj.setProdNbr("temp");
		reqObj.setClrntSys("test");
		
		HttpSession session = request.getSession();
		session.setAttribute(reqGuid, reqObj);
		
		String success = executeAction("/checkForCharacterizationAction");
		assertTrue(success.contains("\"chard\":true") & success.contains("\"wht\":true"));
		//assertEquals(success,"");
	}
	
	@Test
	public void testcheckCharacterizedProductMissingWhite() throws UnsupportedEncodingException, ServletException {
		ActionProxy proxy = getActionProxy("/checkForCharacterizationAction");
		target = (ProcessProductAction) proxy.getAction();
		CdsProdCharzd cdsprodcharzd = new CdsProdCharzd();
		cdsprodcharzd.setProdNbr("temp");
		cdsprodcharzd.setClrntSysId("test");

		testentitymanager.persistAndFlush(cdsprodcharzd);
		target.setReqGuid(reqGuid);
		reqObj.setProdNbr("temp");
		reqObj.setClrntSys("test");
		
		HttpSession session = request.getSession();
		session.setAttribute(reqGuid, reqObj);
		
		String success = executeAction("/checkForCharacterizationAction");
		assertTrue(success.contains("\"chard\":true") & success.contains("\"wht\":false"));
		//assertEquals(success,"");
	}
	
	@Test
	public void testcheckCharacterizedProductReadFail() throws UnsupportedEncodingException, ServletException {
		ActionProxy proxy = getActionProxy("/checkForCharacterizationAction");
		target = (ProcessProductAction) proxy.getAction();
		target.setReqGuid(reqGuid);
		reqObj.setProdNbr("temp");
		reqObj.setClrntSys("test");
		
		HttpSession session = request.getSession();
		session.setAttribute(reqGuid, reqObj);
		
		String success = executeAction("/checkForCharacterizationAction");
		assertTrue(success.contains("\"chard\":false") & success.contains("\"wht\":false"));
	}
}
