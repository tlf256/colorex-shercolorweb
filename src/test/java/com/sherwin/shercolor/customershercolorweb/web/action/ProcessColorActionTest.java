package com.sherwin.shercolor.customershercolorweb.web.action;

import java.io.PrintWriter;
import java.io.StringWriter;

import javax.servlet.http.HttpSession;

import org.apache.struts2.StrutsSpringJUnit4TestCase;
import org.junit.Before;

import static org.junit.Assert.*;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import com.opensymphony.xwork2.Action;
import com.opensymphony.xwork2.ActionProxy;
import com.sherwin.shercolor.customershercolorweb.web.model.RequestObject;
import com.sherwin.shercolor.customershercolorweb.web.model.SpectroInfo;

@Transactional
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:config/spring/shercolorcommon-test.xml")
public class ProcessColorActionTest extends StrutsSpringJUnit4TestCase<ProcessColorAction> {

	ProcessColorAction target;
	RequestObject reqObj = new RequestObject();
	SpectroInfo spectro = new SpectroInfo();
	String reqGuid = "12345";
	
	@Before
	public void setup() {
		reqObj.setGuid("123456789");
		reqObj.setClrntSys("CCE");
		reqObj.setCustomerID("LB6110");
		reqObj.setCustomerName("SW STORE");
		reqObj.setCustomerType("STORE");
		spectro.setSerialNbr(null);
		reqObj.setSpectro(spectro);
	}

	@Test
	public void testListColors() {
		ActionProxy proxy = getActionProxy("/listColors");
		target = (ProcessColorAction) proxy.getAction();
		
		//SW autocomplete
		target.setPartialColorNameOrId("6000");
		request.setParameter("selectedCoType", "SW");
		target.setColorName("SNOWFALL");
		request.setParameter("reqGuid", reqObj.getGuid());
		request.setParameter("colorData", "6000");
		HttpSession session = request.getSession();
		session.setAttribute(reqObj.getGuid(), reqObj);
		
		try {
			String success = executeAction("/listColors");
			assertNotNull(success);
			
			//Compet autocomplete w/ company
			target.setPartialColorNameOrId("101-5");
			target.setColorName("TINT OF TURQUOISE");
			request.setParameter("reqGuid", reqObj.getGuid());
			request.setParameter("selectedCoType", "COMPET");
			request.setParameter("selectedCompany", "PPG");
			request.setParameter("colorData", "101-5");
			request.setSession(session);
			
			success = executeAction("/listColors");
			assertNotNull(success);
			
			//Compet autocomplete w/o company
			target.setPartialColorNameOrId("101-5");
			target.setColorName("TINT OF TURQUOISE");
			request.setParameter("reqGuid", reqObj.getGuid());
			request.setParameter("selectedCoType", "COMPET");
			request.setParameter("selectedCompany", "ALL");
			request.setParameter("colorData", "101-5");
			request.setSession(session);
			
			success = executeAction("/listColors");
			assertNotNull(success);
			
			//Nat. Acct. autocomplete w/ company
			target.setPartialColorNameOrId("CL-5");
			target.setColorName("");
			request.setParameter("reqGuid", reqObj.getGuid());
			request.setParameter("colorData", "CL-5");
			request.setParameter("selectedCompany", "ALDI INC");
			request.setParameter("selectedCoType", "NAT");
			request.setSession(session);
			
			success = executeAction("/listColors");
			assertNotNull(success);
			
			//Nat. Acct. autocomplete w/ company
			target.setPartialColorNameOrId("CL-5");
			target.setColorName("");
			request.setParameter("reqGuid", reqObj.getGuid());
			request.setParameter("selectedCompany", "ALL");
			request.setParameter("colorData", "CL-5");
			request.setSession(session);
			
			success = executeAction("/listColors");
			assertNotNull(success);
			
			
		} catch (Exception e) {
			StringWriter sw = new StringWriter();
			PrintWriter pw = new PrintWriter(sw);
			e.printStackTrace(pw);
			System.out.println(sw.toString());
    }
 }

	@Test
	public void testColorUserNextAction_compareColors() {
		reqObj.setCustomerID("TEST");
		reqObj.setCustomerType("CUSTOMER");
		
		request.setParameter("reqGuid",reqObj.getGuid());
		request.setParameter("compareColors", "true");
		
		ActionProxy proxy = getActionProxy("/colorUserNextAction");
        assertNotNull(proxy);
        
        target = (ProcessColorAction) proxy.getAction();
        target.setCompareColors(true);
        target.setSelectedCoTypes("SW");
        target.setPartialColorNameOrId("SHERWIN-WILLIAMS 6385");
        target.setColorData("%5B%7B%22colorNumber%22%3A%226385%22%2C%22companyName%22%3A%22SHERWIN-WILLIAMS%22%2C%22label%22%3A%226385%20DOVER%20WHITE%20261-C2%22%2C%22value%22%3A%22SHERWIN-WILLIAMS%206385%22%7D%5D");        
        
        Map<String, Object> sessionMap = new HashMap<>();
        sessionMap.put(reqObj.getGuid(), reqObj);
        
        proxy.getInvocation().getInvocationContext().setSession(sessionMap);
        
		try {
			String result = proxy.execute();
			
			assertEquals("compareColors", result);
		} catch (Exception e) {
			StringWriter sw = new StringWriter();
			PrintWriter pw = new PrintWriter(sw);
			e.printStackTrace(pw);
			System.out.println(sw.toString());
		}
	}
	
	@Test
	public void testColorUserNextAction_compareColors_input() {
		reqObj.setCustomerID("TEST");
		reqObj.setCustomerType("CUSTOMER");
		
		SpectroInfo spectro = new SpectroInfo();
		reqObj.setSpectro(spectro);
		
		request.setParameter("reqGuid",reqGuid);
		request.setParameter("compareColors", "true");
		
		ActionProxy proxy = getActionProxy("/colorUserNextAction");
	    assertNotNull(proxy);
	    
	    target = (ProcessColorAction) proxy.getAction();
	    target.setCompareColors(true);
	    target.setSelectedCoTypes("SW");
	    target.setPartialColorNameOrId("SHERWIN-WILLIAMS 4040Legacy");
	    target.setColorData("%5B%7B%22colorNumber%22%3A%220006%22%2C%22companyName%22%3A%22SHERWIN-WILLIAMS%22%2C%22label%22%3A%220006%20TOILE%20RED%22%2C%22value%22%3A%22SHERWIN-WILLIAMS%200006%22%7D%2C%7B%22colorNumber%22%3A%220008%22%2C%22companyName%22%3A%22SHERWIN-WILLIAMS%22%2C%22label%22%3A%220008%20CAJUN%20RED%22%2C%22value%22%3A%22SHERWIN-WILLIAMS%200008%22%7D%2C%7B%22colorNumber%22%3A%220057%22%2C%22companyName%22%3A%22SHERWIN-WILLIAMS%22%2C%22label%22%3A%220057%20CHINESE%20RED%22%2C%22value%22%3A%22SHERWIN-WILLIAMS%200057%22%7D%2C%7B%22colorNumber%22%3A%221068%22%2C%22companyName%22%3A%22SHERWIN-WILLIAMS%22%2C%22label%22%3A%221068%20FIRED%20CLAY%22%2C%22value%22%3A%22SHERWIN-WILLIAMS%201068%22%7D%2C%7B%22colorNumber%22%3A%221301%22%2C%22companyName%22%3A%22SHERWIN-WILLIAMS%22%2C%22label%22%3A%221301%20ROUGE%20RED%22%2C%22value%22%3A%22SHERWIN-WILLIAMS%201301%22%7D%2C%7B%22colorNumber%22%3A%221461%22%2C%22companyName%22%3A%22SHERWIN-WILLIAMS%22%2C%22label%22%3A%221461%20WEATHERED%20COPPER%22%2C%22value%22%3A%22SHERWIN-WILLIAMS%201461%22%7D%2C%7B%22colorNumber%22%3A%221581%22%2C%22companyName%22%3A%22SHERWIN-WILLIAMS%22%2C%22label%22%3A%221581%20BOTTICELLI%20RED%22%2C%22value%22%3A%22SHERWIN-WILLIAMS%201581%22%7D%2C%7B%22colorNumber%22%3A%221588%22%2C%22companyName%22%3A%22SHERWIN-WILLIAMS%22%2C%22label%22%3A%221588%20MING%20RED%22%2C%22value%22%3A%22SHERWIN-WILLIAMS%201588%22%7D%2C%7B%22colorNumber%22%3A%221602%22%2C%22companyName%22%3A%22SHERWIN-WILLIAMS%22%2C%22label%22%3A%221602%20THEATER%20RED%22%2C%22value%22%3A%22SHERWIN-WILLIAMS%201602%22%7D%2C%7B%22colorNumber%22%3A%221609%22%2C%22companyName%22%3A%22SHERWIN-WILLIAMS%22%2C%22label%22%3A%221609%20RENOIR%20RED%22%2C%22value%22%3A%22SHERWIN-WILLIAMS%201609%22%7D%2C%7B%22colorNumber%22%3A%221612%22%2C%22companyName%22%3A%22SHERWIN-WILLIAMS%22%2C%22label%22%3A%221612%20POWDERED%20PEACH%22%2C%22value%22%3A%22SHERWIN-WILLIAMS%201612%22%7D%2C%7B%22colorNumber%22%3A%221646%22%2C%22companyName%22%3A%22SHERWIN-WILLIAMS%22%2C%22label%22%3A%221646%20FILTERED%20SUN%22%2C%22value%22%3A%22SHERWIN-WILLIAMS%201646%22%7D%2C%7B%22colorNumber%22%3A%221937%22%2C%22companyName%22%3A%22SHERWIN-WILLIAMS%22%2C%22label%22%3A%221937%20PAMPERED%20PEACH%22%2C%22value%22%3A%22SHERWIN-WILLIAMS%201937%22%7D%2C%7B%22colorNumber%22%3A%22200-1024%22%2C%22companyName%22%3A%22SHERWIN-WILLIAMS%22%2C%22label%22%3A%22200-1024%20WEATHERED%20CEDAR%22%2C%22value%22%3A%22SHERWIN-WILLIAMS%20200-1024%22%7D%2C%7B%22colorNumber%22%3A%22200-1043%22%2C%22companyName%22%3A%22SHERWIN-WILLIAMS%22%2C%22label%22%3A%22200-1043%20MONTANA%20RED%22%2C%22value%22%3A%22SHERWIN-WILLIAMS%20200-1043%22%7D%2C%7B%22colorNumber%22%3A%22200-1044%22%2C%22companyName%22%3A%22SHERWIN-WILLIAMS%22%2C%22label%22%3A%22200-1044%20RED%20PINE%22%2C%22value%22%3A%22SHERWIN-WILLIAMS%20200-1044%22%7D%2C%7B%22colorNumber%22%3A%222173%22%2C%22companyName%22%3A%22SHERWIN-WILLIAMS%22%2C%22label%22%3A%222173%20SHAKER%20RED%22%2C%22value%22%3A%22SHERWIN-WILLIAMS%202173%22%7D%2C%7B%22colorNumber%22%3A%222281%22%2C%22companyName%22%3A%22SHERWIN-WILLIAMS%22%2C%22label%22%3A%222281%20REDDISH%20DUSK%22%2C%22value%22%3A%22SHERWIN-WILLIAMS%202281%22%7D%2C%7B%22colorNumber%22%3A%222307%22%2C%22companyName%22%3A%22SHERWIN-WILLIAMS%22%2C%22label%22%3A%222307%20RED%20BARN%22%2C%22value%22%3A%22SHERWIN-WILLIAMS%202307%22%7D%2C%7B%22colorNumber%22%3A%222314%22%2C%22companyName%22%3A%22SHERWIN-WILLIAMS%22%2C%22label%22%3A%222314%20AWNING%20RED%22%2C%22value%22%3A%22SHERWIN-WILLIAMS%202314%22%7D%2C%7B%22colorNumber%22%3A%222719%22%2C%22companyName%22%3A%22SHERWIN-WILLIAMS%22%2C%22label%22%3A%222719%20RUSTIC%20RED%22%2C%22value%22%3A%22SHERWIN-WILLIAMS%202719%22%7D%2C%7B%22colorNumber%22%3A%222728%22%2C%22companyName%22%3A%22SHERWIN-WILLIAMS%22%2C%22label%22%3A%222728%20RED%20BRUSH%22%2C%22value%22%3A%22SHERWIN-WILLIAMS%202728%22%7D%2C%7B%22colorNumber%22%3A%222801%22%2C%22companyName%22%3A%22SHERWIN-WILLIAMS%22%2C%22label%22%3A%222801%20ROOKWOOD%20DARK%20RED%22%2C%22value%22%3A%22SHERWIN-WILLIAMS%202801%22%7D%2C%7B%22colorNumber%22%3A%222802%22%2C%22companyName%22%3A%22SHERWIN-WILLIAMS%22%2C%22label%22%3A%222802%20ROOKWOOD%20RED%22%2C%22value%22%3A%22SHERWIN-WILLIAMS%202802%22%7D%2C%7B%22colorNumber%22%3A%222839%22%2C%22companyName%22%3A%22SHERWIN-WILLIAMS%22%2C%22label%22%3A%222839%20ROYCROFT%20COPPER%20RED%22%2C%22value%22%3A%22SHERWIN-WILLIAMS%202839%22%7D%2C%7B%22colorNumber%22%3A%222840%22%2C%22companyName%22%3A%22SHERWIN-WILLIAMS%22%2C%22label%22%3A%222840%20HAMMERED%20SILVER%22%2C%22value%22%3A%22SHERWIN-WILLIAMS%202840%22%7D%2C%7B%22colorNumber%22%3A%222841%22%2C%22companyName%22%3A%22SHERWIN-WILLIAMS%22%2C%22label%22%3A%222841%20WEATHERED%20SHINGLE%22%2C%22value%22%3A%22SHERWIN-WILLIAMS%202841%22%7D%2C%7B%22colorNumber%22%3A%222906%22%2C%22companyName%22%3A%22SHERWIN-WILLIAMS%22%2C%22label%22%3A%222906%20CRIMSON%20RED%22%2C%22value%22%3A%22SHERWIN-WILLIAMS%202906%22%7D%2C%7B%22colorNumber%22%3A%222909%22%2C%22companyName%22%3A%22SHERWIN-WILLIAMS%22%2C%22label%22%3A%222909%20REDWING%22%2C%22value%22%3A%22SHERWIN-WILLIAMS%202909%22%7D%2C%7B%22colorNumber%22%3A%222910%22%2C%22companyName%22%3A%22SHERWIN-WILLIAMS%22%2C%22label%22%3A%222910%20RED%20DOOR%22%2C%22value%22%3A%22SHERWIN-WILLIAMS%202910%22%7D%2C%7B%22colorNumber%22%3A%222911%22%2C%22companyName%22%3A%22SHERWIN-WILLIAMS%22%2C%22label%22%3A%222911%20POMPEII%20RED%22%2C%22value%22%3A%22SHERWIN-WILLIAMS%202911%22%7D%2C%7B%22colorNumber%22%3A%222913%22%2C%22companyName%22%3A%22SHERWIN-WILLIAMS%22%2C%22label%22%3A%222913%20CLASSY%20RED%22%2C%22value%22%3A%22SHERWIN-WILLIAMS%202913%22%7D%2C%7B%22colorNumber%22%3A%222916%22%2C%22companyName%22%3A%22SHERWIN-WILLIAMS%22%2C%22label%22%3A%222916%20RED%20PRAIRIE%22%2C%22value%22%3A%22SHERWIN-WILLIAMS%202916%22%7D%2C%7B%22colorNumber%22%3A%223018%22%2C%22companyName%22%3A%22SHERWIN-WILLIAMS%22%2C%22label%22%3A%223018%20SALEM%20RED%22%2C%22value%22%3A%22SHERWIN-WILLIAMS%203018%22%7D%2C%7B%22colorNumber%22%3A%223020%22%2C%22companyName%22%3A%22SHERWIN-WILLIAMS%22%2C%22label%22%3A%223020%20CAPE%20COD%20RED%22%2C%22value%22%3A%22SHERWIN-WILLIAMS%203020%22%7D%2C%7B%22colorNumber%22%3A%223043%22%2C%22companyName%22%3A%22SHERWIN-WILLIAMS%22%2C%22label%22%3A%223043%20CHEYENNE%20RED%22%2C%22value%22%3A%22SHERWIN-WILLIAMS%203043%22%7D%2C%7B%22colorNumber%22%3A%223044%22%2C%22companyName%22%3A%22SHERWIN-WILLIAMS%22%2C%22label%22%3A%223044%20RANCHERO%20RED%22%2C%22value%22%3A%22SHERWIN-WILLIAMS%203044%22%7D%2C%7B%22colorNumber%22%3A%223134%22%2C%22companyName%22%3A%22SHERWIN-WILLIAMS%22%2C%22label%22%3A%223134%20WEATHERED%20TEAK%22%2C%22value%22%3A%22SHERWIN-WILLIAMS%203134%22%7D%2C%7B%22colorNumber%22%3A%223501%22%2C%22companyName%22%3A%22SHERWIN-WILLIAMS%22%2C%22label%22%3A%223501%20REDWOOD%20(Woodscapes)%22%2C%22value%22%3A%22SHERWIN-WILLIAMS%203501%22%7D%2C%7B%22colorNumber%22%3A%223501SS%22%2C%22companyName%22%3A%22SHERWIN-WILLIAMS%22%2C%22label%22%3A%223501SS%20REDWOOD%22%2C%22value%22%3A%22SHERWIN-WILLIAMS%203501SS%22%7D%2C%7B%22colorNumber%22%3A%223508%22%2C%22companyName%22%3A%22SHERWIN-WILLIAMS%22%2C%22label%22%3A%223508%20COVERED%20BRIDGE%22%2C%22value%22%3A%22SHERWIN-WILLIAMS%203508%22%7D%2C%7B%22colorNumber%22%3A%223508SS%22%2C%22companyName%22%3A%22SHERWIN-WILLIAMS%22%2C%22label%22%3A%223508SS%20COVERED%20BRIDGE%22%2C%22value%22%3A%22SHERWIN-WILLIAMS%203508SS%22%7D%2C%7B%22colorNumber%22%3A%223553%22%2C%22companyName%22%3A%22SHERWIN-WILLIAMS%22%2C%22label%22%3A%223553%20REDWOOD%20TONER%22%2C%22value%22%3A%22SHERWIN-WILLIAMS%203553%22%7D%2C%7B%22colorNumber%22%3A%223557%22%2C%22companyName%22%3A%22SHERWIN-WILLIAMS%22%2C%22label%22%3A%223557%20REDWOOD%22%2C%22value%22%3A%22SHERWIN-WILLIAMS%203557%22%7D%2C%7B%22colorNumber%22%3A%223558%22%2C%22companyName%22%3A%22SHERWIN-WILLIAMS%22%2C%22label%22%3A%223558%20HEART%20REDWOOD%22%2C%22value%22%3A%22SHERWIN-WILLIAMS%203558%22%7D%2C%7B%22colorNumber%22%3A%223563%22%2C%22companyName%22%3A%22SHERWIN-WILLIAMS%22%2C%22label%22%3A%223563%20REDWOOD%20(Superdeck)%22%2C%22value%22%3A%22SHERWIN-WILLIAMS%203563%22%7D%2C%7B%22colorNumber%22%3A%223568%22%2C%22companyName%22%3A%22SHERWIN-WILLIAMS%22%2C%22label%22%3A%223568%20WEATHERED%20GRAY%22%2C%22value%22%3A%22SHERWIN-WILLIAMS%203568%22%7D%2C%7B%22colorNumber%22%3A%223571%22%2C%22companyName%22%3A%22SHERWIN-WILLIAMS%22%2C%22label%22%3A%223571%20CLASSIC%20BARN%20RED%22%2C%22value%22%3A%22SHERWIN-WILLIAMS%203571%22%7D%2C%7B%22colorNumber%22%3A%223571SS%22%2C%22companyName%22%3A%22SHERWIN-WILLIAMS%22%2C%22label%22%3A%223571SS%20CLASSIC%20BARN%20RED%22%2C%22value%22%3A%22SHERWIN-WILLIAMS%203571SS%22%7D%2C%7B%22colorNumber%22%3A%223572%22%2C%22companyName%22%3A%22SHERWIN-WILLIAMS%22%2C%22label%22%3A%223572%20NEW%20BARN%20RED%22%2C%22value%22%3A%22SHERWIN-WILLIAMS%203572%22%7D%2C%7B%22colorNumber%22%3A%223572SS%22%2C%22companyName%22%3A%22SHERWIN-WILLIAMS%22%2C%22label%22%3A%223572SS%20NEW%20BARN%20RED%22%2C%22value%22%3A%22SHERWIN-WILLIAMS%203572SS%22%7D%2C%7B%22colorNumber%22%3A%223804%22%2C%22companyName%22%3A%22SHERWIN-WILLIAMS%22%2C%22label%22%3A%223804%20RED%20MESA%22%2C%22value%22%3A%22SHERWIN-WILLIAMS%203804%22%7D%2C%7B%22colorNumber%22%3A%224040%22%2C%22companyName%22%3A%22SHERWIN-WILLIAMS%22%2C%22label%22%3A%224040%20DECK%20RED%22%2C%22value%22%3A%22SHERWIN-WILLIAMS%204040%22%7D%2C%7B%22colorNumber%22%3A%224040LEGACY%22%2C%22companyName%22%3A%22SHERWIN-WILLIAMS%22%2C%22label%22%3A%224040LEGACY%20DECK%20RED%22%2C%22value%22%3A%22SHERWIN-WILLIAMS%204040LEGACY%22%7D%2C%7B%22colorNumber%22%3A%224047%22%2C%22companyName%22%3A%22SHERWIN-WILLIAMS%22%2C%22label%22%3A%224047%20FIREDUST%22%2C%22value%22%3A%22SHERWIN-WILLIAMS%204047%22%7D%2C%7B%22colorNumber%22%3A%224047LEGACY%22%2C%22companyName%22%3A%22SHERWIN-WILLIAMS%22%2C%22label%22%3A%224047LEGACY%20FIREDUST%22%2C%22value%22%3A%22SHERWIN-WILLIAMS%204047LEGACY%22%7D%2C%7B%22colorNumber%22%3A%224073%22%2C%22companyName%22%3A%22SHERWIN-WILLIAMS%22%2C%22label%22%3A%224073%20RECYCLED%20RED%22%2C%22value%22%3A%22SHERWIN-WILLIAMS%204073%22%7D%2C%7B%22colorNumber%22%3A%224073LEGACY%22%2C%22companyName%22%3A%22SHERWIN-WILLIAMS%22%2C%22label%22%3A%224073LEGACY%20RECYCLED%20RED%22%2C%22value%22%3A%22SHERWIN-WILLIAMS%204073LEGACY%22%7D%2C%7B%22colorNumber%22%3A%224081%22%2C%22companyName%22%3A%22SHERWIN-WILLIAMS%22%2C%22label%22%3A%224081%20SAFETY%20RED%22%2C%22value%22%3A%22SHERWIN-WILLIAMS%204081%22%7D%2C%7B%22colorNumber%22%3A%224081LEGACY%22%2C%22companyName%22%3A%22SHERWIN-WILLIAMS%22%2C%22label%22%3A%224081LEGACY%20SAFETY%20RED%22%2C%22value%22%3A%22SHERWIN-WILLIAMS%204081LEGACY%22%7D%2C%7B%22colorNumber%22%3A%22507%22%2C%22companyName%22%3A%22SHERWIN-WILLIAMS%22%2C%22label%22%3A%22507%20SAFETY%20RED%22%2C%22value%22%3A%22SHERWIN-WILLIAMS%20507%22%7D%2C%7B%22colorNumber%22%3A%22511%22%2C%22companyName%22%3A%22SHERWIN-WILLIAMS%22%2C%22label%22%3A%22511%20APPLE%20RED%205%2F02%22%2C%22value%22%3A%22SHERWIN-WILLIAMS%20511%22%7D%2C%7B%22colorNumber%22%3A%22520M%22%2C%22companyName%22%3A%22SHERWIN-WILLIAMS%22%2C%22label%22%3A%22520M%20RED%20CLAY%22%2C%22value%22%3A%22SHERWIN-WILLIAMS%20520M%22%7D%2C%7B%22colorNumber%22%3A%22530%22%2C%22companyName%22%3A%22SHERWIN-WILLIAMS%22%2C%22label%22%3A%22530%20RED%20BRICK%22%2C%22value%22%3A%22SHERWIN-WILLIAMS%20530%22%7D%2C%7B%22colorNumber%22%3A%226028%22%2C%22companyName%22%3A%22SHERWIN-WILLIAMS%22%2C%22label%22%3A%226028%20CULTURED%20PEARL%20260-C3%22%2C%22value%22%3A%22SHERWIN-WILLIAMS%206028%22%7D%2C%7B%22colorNumber%22%3A%226053%22%2C%22companyName%22%3A%22SHERWIN-WILLIAMS%22%2C%22label%22%3A%226053%20REDDENED%20EARTH%20194-C5%22%2C%22value%22%3A%22SHERWIN-WILLIAMS%206053%22%7D%2C%7B%22colorNumber%22%3A%226130%22%2C%22companyName%22%3A%22SHERWIN-WILLIAMS%22%2C%22label%22%3A%226130%20MANNERED%20GOLD%20140-C5%22%2C%22value%22%3A%22SHERWIN-WILLIAMS%206130%22%7D%2C%7B%22colorNumber%22%3A%226303%22%2C%22companyName%22%3A%22SHERWIN-WILLIAMS%22%2C%22label%22%3A%226303%20ROSE%20COLORED%20111-C2%22%2C%22value%22%3A%22SHERWIN-WILLIAMS%206303%22%7D%2C%7B%22colorNumber%22%3A%226312%22%2C%22companyName%22%3A%22SHERWIN-WILLIAMS%22%2C%22label%22%3A%226312%20REDBUD%20112-C5%22%2C%22value%22%3A%22SHERWIN-WILLIAMS%206312%22%7D%2C%7B%22colorNumber%22%3A%226313%22%2C%22companyName%22%3A%22SHERWIN-WILLIAMS%22%2C%22label%22%3A%226313%20KIRSCH%20RED%20112-C6%22%2C%22value%22%3A%22SHERWIN-WILLIAMS%206313%22%7D%2C%7B%22colorNumber%22%3A%226314%22%2C%22companyName%22%3A%22SHERWIN-WILLIAMS%22%2C%22label%22%3A%226314%20LUXURIOUS%20RED%20112-C7%22%2C%22value%22%3A%22SHERWIN-WILLIAMS%206314%22%7D%2C%7B%22colorNumber%22%3A%226319%22%2C%22companyName%22%3A%22SHERWIN-WILLIAMS%22%2C%22label%22%3A%226319%20REDDISH%20113-C5%22%2C%22value%22%3A%22SHERWIN-WILLIAMS%206319%22%7D%2C%7B%22colorNumber%22%3A%226320%22%2C%22companyName%22%3A%22SHERWIN-WILLIAMS%22%2C%22label%22%3A%226320%20BRAVADO%20RED%20113-C6%22%2C%22value%22%3A%22SHERWIN-WILLIAMS%206320%22%7D%2C%7B%22colorNumber%22%3A%226321%22%2C%22companyName%22%3A%22SHERWIN-WILLIAMS%22%2C%22label%22%3A%226321%20RED%20BAY%20113-C7%22%2C%22value%22%3A%22SHERWIN-WILLIAMS%206321%22%7D%2C%7B%22colorNumber%22%3A%226335%22%2C%22companyName%22%3A%22SHERWIN-WILLIAMS%22%2C%22label%22%3A%226335%20FIRED%20BRICK%20115-C7%22%2C%22value%22%3A%22SHERWIN-WILLIAMS%206335%22%7D%2C%7B%22colorNumber%22%3A%226341%22%2C%22companyName%22%3A%22SHERWIN-WILLIAMS%22%2C%22label%22%3A%226341%20RED%20CENT%20124-C6%22%2C%22value%22%3A%22SHERWIN-WILLIAMS%206341%22%7D%2C%7B%22colorNumber%22%3A%226564%22%2C%22companyName%22%3A%22SHERWIN-WILLIAMS%22%2C%22label%22%3A%226564%20RED%20CLOVER%20102-C4%22%2C%22value%22%3A%22SHERWIN-WILLIAMS%206564%22%7D%2C%7B%22colorNumber%22%3A%226600%22%2C%22companyName%22%3A%22SHERWIN-WILLIAMS%22%2C%22label%22%3A%226600%20ENTICING%20RED%20107-C5%22%2C%22value%22%3A%22SHERWIN-WILLIAMS%206600%22%7D%2C%7B%22colorNumber%22%3A%226607%22%2C%22companyName%22%3A%22SHERWIN-WILLIAMS%22%2C%22label%22%3A%226607%20RED%20TOMATO%20108-C5%22%2C%22value%22%3A%22SHERWIN-WILLIAMS%206607%22%7D%2C%7B%22colorNumber%22%3A%226608%22%2C%22companyName%22%3A%22SHERWIN-WILLIAMS%22%2C%22label%22%3A%226608%20RAVE%20RED%20108-C6%22%2C%22value%22%3A%22SHERWIN-WILLIAMS%206608%22%7D%2C%7B%22colorNumber%22%3A%226820%22%2C%22companyName%22%3A%22SHERWIN-WILLIAMS%22%2C%22label%22%3A%226820%20INSPIRED%20LILAC%20181-C1%22%2C%22value%22%3A%22SHERWIN-WILLIAMS%206820%22%7D%2C%7B%22colorNumber%22%3A%226863%22%2C%22companyName%22%3A%22SHERWIN-WILLIAMS%22%2C%22label%22%3A%226863%20LUSTY%20RED%22%2C%22value%22%3A%22SHERWIN-WILLIAMS%206863%22%7D%2C%7B%22colorNumber%22%3A%226865%22%2C%22companyName%22%3A%22SHERWIN-WILLIAMS%22%2C%22label%22%3A%226865%20GYPSY%20RED%22%2C%22value%22%3A%22SHERWIN-WILLIAMS%206865%22%7D%2C%7B%22colorNumber%22%3A%226868%22%2C%22companyName%22%3A%22SHERWIN-WILLIAMS%22%2C%22label%22%3A%226868%20REAL%20RED%20101-C6%22%2C%22value%22%3A%22SHERWIN-WILLIAMS%206868%22%7D%2C%7B%22colorNumber%22%3A%226871%22%2C%22companyName%22%3A%22SHERWIN-WILLIAMS%22%2C%22label%22%3A%226871%20POSITIVE%20RED%20%20101-C7%22%2C%22value%22%3A%22SHERWIN-WILLIAMS%206871%22%7D%2C%7B%22colorNumber%22%3A%226882%22%2C%22companyName%22%3A%22SHERWIN-WILLIAMS%22%2C%22label%22%3A%226882%20DAREDEVIL%22%2C%22value%22%3A%22SHERWIN-WILLIAMS%206882%22%7D%2C%7B%22colorNumber%22%3A%227027%22%2C%22companyName%22%3A%22SHERWIN-WILLIAMS%22%2C%22label%22%3A%227027%20HICKORY%20SMOKE%20241-C7%22%2C%22value%22%3A%22SHERWIN-WILLIAMS%207027%22%7D%2C%7B%22colorNumber%22%3A%227028%22%2C%22companyName%22%3A%22SHERWIN-WILLIAMS%22%2C%22label%22%3A%227028%20INCREDIBLE%20WHITE%20256-C4%22%2C%22value%22%3A%22SHERWIN-WILLIAMS%207028%22%7D%2C%7B%22colorNumber%22%3A%227256%22%2C%22companyName%22%3A%22SHERWIN-WILLIAMS%22%2C%22label%22%3A%227256%20FLOOR%20ENAMEL%20TILE%20RED%201%2F03%22%2C%22value%22%3A%22SHERWIN-WILLIAMS%207256%22%7D%2C%7B%22colorNumber%22%3A%227261%22%2C%22companyName%22%3A%22SHERWIN-WILLIAMS%22%2C%22label%22%3A%227261%20ARMORSEAL%20TILE%20RED%201%2F03%22%2C%22value%22%3A%22SHERWIN-WILLIAMS%207261%22%7D%2C%7B%22colorNumber%22%3A%227405%22%2C%22companyName%22%3A%22SHERWIN-WILLIAMS%22%2C%22label%22%3A%227405%20INTERNATIONAL%20RED%22%2C%22value%22%3A%22SHERWIN-WILLIAMS%207405%22%7D%2C%7B%22colorNumber%22%3A%227417%22%2C%22companyName%22%3A%22SHERWIN-WILLIAMS%22%2C%22label%22%3A%227417%20ALLEN%20RED%22%2C%22value%22%3A%22SHERWIN-WILLIAMS%207417%22%7D%2C%7B%22colorNumber%22%3A%227422%22%2C%22companyName%22%3A%22SHERWIN-WILLIAMS%22%2C%22label%22%3A%227422%20MACHINERY%20RED%22%2C%22value%22%3A%22SHERWIN-WILLIAMS%207422%22%7D%2C%7B%22colorNumber%22%3A%227443%22%2C%22companyName%22%3A%22SHERWIN-WILLIAMS%22%2C%22label%22%3A%227443%20RED%20METAL%22%2C%22value%22%3A%22SHERWIN-WILLIAMS%207443%22%7D%2C%7B%22colorNumber%22%3A%227455%22%2C%22companyName%22%3A%22SHERWIN-WILLIAMS%22%2C%22label%22%3A%227455%20CONTAINER%20RED%22%2C%22value%22%3A%22SHERWIN-WILLIAMS%207455%22%7D%2C%7B%22colorNumber%22%3A%227584%22%2C%22companyName%22%3A%22SHERWIN-WILLIAMS%22%2C%22label%22%3A%227584%20RED%20THEATRE%20276-C3%22%2C%22value%22%3A%22SHERWIN-WILLIAMS%207584%22%7D%2C%7B%22colorNumber%22%3A%227587%22%2C%22companyName%22%3A%22SHERWIN-WILLIAMS%22%2C%22label%22%3A%227587%20ANTIQUE%20RED%20107-C7%22%2C%22value%22%3A%22SHERWIN-WILLIAMS%207587%22%7D%2C%7B%22colorNumber%22%3A%227590%22%2C%22companyName%22%3A%22SHERWIN-WILLIAMS%22%2C%22label%22%3A%227590%20RED%20OBSESSION%22%2C%22value%22%3A%22SHERWIN-WILLIAMS%207590%22%7D%2C%7B%22colorNumber%22%3A%227591%22%2C%22companyName%22%3A%22SHERWIN-WILLIAMS%22%2C%22label%22%3A%227591%20RED%20BARN%20275-C7%22%2C%22value%22%3A%22SHERWIN-WILLIAMS%207591%22%7D%2C%7B%22colorNumber%22%3A%227593%22%2C%22companyName%22%3A%22SHERWIN-WILLIAMS%22%2C%22label%22%3A%227593%20RUSTIC%20RED%20275-C6%22%2C%22value%22%3A%22SHERWIN-WILLIAMS%207593%22%7D%5D");        
	    
	    Map<String, Object> sessionMap = new HashMap<>();
	    sessionMap.put(reqGuid, reqObj);
	    
	    proxy.getInvocation().getInvocationContext().setSession(sessionMap);
	    
		try {
			String result = proxy.execute();
			
			assertEquals(Action.INPUT, result);
			
		} catch (Exception e) {
			try {
				throw(e.getCause());
			} catch (Throwable e1) {
				e1.printStackTrace();
			}
	   }
	}


	public void testDisplay() {
		ActionProxy proxy = getActionProxy("/displayColorAction");
		target = (ProcessColorAction) proxy.getAction();
		
		request.setParameter("reqGuid", reqObj.getGuid());
		HttpSession session = request.getSession();
		session.setAttribute(reqObj.getGuid(), reqObj);
		
		try {
			String success = executeAction("/displayColorAction");
			assertNotNull(success);
			
		} catch (Exception e) {
			StringWriter sw = new StringWriter();
			PrintWriter pw = new PrintWriter(sw);
			e.printStackTrace(pw);
			System.out.println(sw.toString());
		}
	}
	
	@Test
	public void testExecute() {
		ActionProxy proxy = getActionProxy("/colorUserNextAction");
		target = (ProcessColorAction) proxy.getAction();
		
		//SW test execute
		target.setSelectedCoTypes("SW");
		target.setPartialColorNameOrId("1015");
		target.setColorName("SKYLINE STEEL");
		
		request.setParameter("reqGuid", reqObj.getGuid());
		request.setParameter("colorData", "1015");
		HttpSession session = request.getSession();
		session.setAttribute(reqObj.getGuid(), reqObj);
		
		try {
			String success = executeAction("/colorUserNextAction");
			assertNotNull(success);
			
			//SW test execute simulating autocomplete selection
			target.setColorName("SILVERPLATE");
			target.setPartialColorNameOrId("SHERWIN-WILLIAMS 1001");
			request.setParameter("reqGuid", reqObj.getGuid());
			request.setParameter("colorData", "[{\"colorNumber\":\"1001\",\"companyName\":\"SHERWIN-WILLIAMS\",\"label\":\"1001 SILVERPLATE\",\"value\":\"SHERWIN-WILLIAMS 1001\"}]");
			request.setSession(session);
			
			success = executeAction("/colorUserNextAction");
			assertNotNull(success);
			
			//SW test execute simulating autocomplete selection failure on match
			target.setColorName("SILVERPLATE");
			target.setPartialColorNameOrId(" 1001");
			request.setParameter("reqGuid", reqObj.getGuid());
			request.setParameter("colorData", "[{\"colorNumber\":\"1001\",\"companyName\":\"SHERWIN-WILLIAMS\",\"label\":\"1001 SILVERPLATE\",\"value\":\"SHERWIN-WILLIAMS 1001\"}]");
			request.setSession(session);
			
			success = executeAction("/colorUserNextAction");
			assertNotNull(success);
			
			//Nat Acct. execute w/ interior forced product.
			target.setPartialColorNameOrId("CL-5");
			
			request.setParameter("reqGuid", reqObj.getGuid());
			request.setParameter("colorData", "CL-5");
			request.setParameter("selectedCompany", "ALDI INC");
			request.setParameter("selectedCoTypes", "NAT");
			request.setSession(session);
			
			success = executeAction("/colorUserNextAction");
			assertNotNull(success);
			
			//Nat Acct. execute w/ Int/Ext forced product.
			target.setPartialColorNameOrId("CL-1");
			request.setParameter("reqGuid", reqObj.getGuid());
			request.setParameter("colorData", "CL-1");
			session.setAttribute(reqObj.getGuid(), reqObj);
			request.setSession(session);
			
			success = executeAction("/colorUserNextAction");
			assertNotNull(success);
			
			//Nat. Acct. execute w/ Exterior forced product.
			target.setPartialColorNameOrId("EP3");
			request.setParameter("reqGuid", reqObj.getGuid());
			request.setParameter("colorData", "EP3");
			request.setParameter("selectedCompany", "CHILIS RESTAURANTS");
			session.setAttribute(reqObj.getGuid(), reqObj);
			request.setSession(session);
			
			success = executeAction("/colorUserNextAction");
			assertNotNull(success);
			
			//Nat. Acct. execute with xref-id xref-comp values.
			target.setPartialColorNameOrId("C-2");
			request.setParameter("reqGuid", reqObj.getGuid());
			request.setParameter("colorData", "C-2");
			request.setParameter("selectedCompany", "BUFFALO WILD WINGS");
			session.setAttribute(reqObj.getGuid(), reqObj);
			request.setSession(session);
			
			success = executeAction("/colorUserNextAction");
			assertNotNull(success);
			
			//Custom execute.
			target.setPartialColorNameOrId("CUSTOM");
			request.setParameter("selectedCoTypes", "CUSTOM");
			request.setParameter("reqGuid", reqObj.getGuid());
			session.setAttribute(reqObj.getGuid(), reqObj);
			request.setSession(session);
			
			success = executeAction("/colorUserNextAction");
			assertNotNull(success);
			
			//Competitive execute
			target.setPartialColorNameOrId("101-5");
			request.setParameter("reqGuid", reqObj.getGuid());
			request.setParameter("colorData", "101-5");
			request.setParameter("selectedCoTypes", "COMPET");
			request.setParameter("selectedCompany", "PPG");
			session.setAttribute(reqObj.getGuid(), reqObj);
			request.setSession(session);
			
			success = executeAction("/colorUserNextAction");
			assertNotNull(success);

		} catch (Exception e) {
			StringWriter sw = new StringWriter();
			PrintWriter pw = new PrintWriter(sw);
			e.printStackTrace(pw);
			System.out.println(sw.toString());
    }
  }

  @Test
	public void testColorUserNextAction_measure() {
		reqObj.setCustomerID("TEST");
		reqObj.setCustomerType("CUSTOMER");
		
		request.setParameter("reqGuid",reqObj.getGuid());
		request.setParameter("compareColors", "true");
		
		ActionProxy proxy = getActionProxy("/colorUserNextAction");
        assertNotNull(proxy);
        
        target = (ProcessColorAction) proxy.getAction();
        target.setCompareColors(true);
        target.setSelectedCoTypes("CUSTOMMATCH");
        target.setPartialColorNameOrId("MATCH");
        
        Map<String, Object> sessionMap = new HashMap<>();
        sessionMap.put(reqObj.getGuid(), reqObj);
        
        proxy.getInvocation().getInvocationContext().setSession(sessionMap);
        
		try {
			String result = proxy.execute();
			
			assertEquals("measure", result);
		} catch (Exception e) {
			StringWriter sw = new StringWriter();
			PrintWriter pw = new PrintWriter(sw);
			e.printStackTrace(pw);
			System.out.println(sw.toString());
		}
	}
	
	@Test
	public void testColorLookupAction() {
		ActionProxy proxy = getActionProxy("/colorLookupSearchAction");
		target = (ProcessColorAction) proxy.getAction();
		
		//Competitive lookup
		target.setReqGuid("123456789");
		target.setSelectedCompany("VYTEC");
		target.setSelectedCoTypes("COMPET");
		target.setColorID("VP30");
		target.setColorName("BUTTERCREAM");
		
		request.setParameter("reqGuid", reqObj.getGuid());
		HttpSession session = request.getSession();
		session.setAttribute(reqObj.getGuid(), reqObj);
		
		try {
			String success = executeAction("/colorLookupSearchAction");
			assertNotNull(success);	
			
			//Nat. Acct. lookup
			target.setColorID("1631");
			target.setColorName("MIDNIGHT OIL");
			request.setParameter("reqGuid", reqObj.getGuid());
			request.setParameter("selectedCompany", "BEST BUY");
			request.setParameter("selectedCoTypes", "NAT");
			session.setAttribute(reqObj.getGuid(), reqObj);
			request.setSession(session);
			
			success = executeAction("/colorLookupSearchAction");
			assertNotNull(success);
			
			//SW Lookup
			target.setColorID("1015");
			target.setColorName("SKYLINE STEEL");
			request.setParameter("reqGuid", reqObj.getGuid());
			request.setParameter("locatorId", "283-C3");
			request.setParameter("selectedCoTypes", "SW");
			session.setAttribute(reqObj.getGuid(), reqObj);
			request.setSession(session);
			
			success = executeAction("/colorLookupSearchAction");
			assertNotNull(success);
			
			
			//Invalid Lookup
			target.setColorID("1631");
			target.setColorName("MIDNIGHT OIL");
			request.setParameter("reqGuid", reqObj.getGuid());
			request.setParameter("selectedCompany", "ALL");
			request.setParameter("selectedCoTypes", "INVALID");
			session.setAttribute(reqObj.getGuid(), reqObj);
			request.setSession(session);
			
			success = executeAction("/colorLookupSearchAction");
			assertNotNull(success);
						
		} catch (Exception e) {
			StringWriter sw = new StringWriter();
			PrintWriter pw = new PrintWriter(sw);
			e.printStackTrace(pw);
			System.out.println(sw.toString());
		}
	}

  @Test
	public void testColorUserNextAction_existingMatch() {
		reqObj.setCustomerID("TEST");
		reqObj.setCustomerType("CUSTOMER");
		
		request.setParameter("reqGuid",reqObj.getGuid());
		request.setParameter("compareColors", "true");
		
		ActionProxy proxy = getActionProxy("/colorUserNextAction");
        assertNotNull(proxy);
        
        target = (ProcessColorAction) proxy.getAction();
        target.setCompareColors(true);
        target.setSelectedCoTypes("EXISTING_MATCH");
        target.setPartialColorNameOrId("MATCH");
        
        Map<String, Object> sessionMap = new HashMap<>();
        sessionMap.put(reqObj.getGuid(), reqObj);
        
        proxy.getInvocation().getInvocationContext().setSession(sessionMap);
        
		try {
			String result = proxy.execute();
			
			assertEquals("existingMatch", result);
		} catch (Exception e) {
			StringWriter sw = new StringWriter();
			PrintWriter pw = new PrintWriter(sw);
			e.printStackTrace(pw);
			System.out.println(sw.toString());
		}
	}
}
