package com.sherwin.shercolor.customershercolorweb.web.action;

import static org.junit.Assert.assertEquals;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.apache.struts2.StrutsSpringJUnit4TestCase;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import com.google.gson.Gson;
import com.opensymphony.xwork2.ActionProxy;
import com.sherwin.shercolor.customershercolorweb.web.model.RequestObject;
import com.sherwin.shercolor.customershercolorweb.web.model.SpectroInfo;

@Transactional
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:config/spring/shercolorcommon-test.xml"})
public class SpectroEventActionTest extends StrutsSpringJUnit4TestCase<SpectroEventAction>{

	SpectroEventAction target;
	RequestObject reqObj = new RequestObject();
	String reqGuid = "12345";
	
	@Test
	public void testSpectroEventsAction() {
		ActionProxy proxy = getActionProxy("/logSpectroEventAction");
		SpectroInfo spectro = new SpectroInfo();
		Map<String,Object> spectroMessage = new HashMap<String,Object>();
		
		target = (SpectroEventAction) proxy.getAction();
		target.setReqGuid(reqGuid);
		
		request.setParameter("reqGuid", reqGuid);
		HttpSession session = request.getSession();
		session.setAttribute(reqGuid, reqObj);
		
		spectro.setModel("JUnitTester");
		spectro.setPort("USB");
		spectro.setSerialNbr("TestSerial");
		reqObj.setSpectro(spectro);
		
		spectroMessage.put("command", "GetCalStatusMinUntilCalExpiration");
		spectroMessage.put("responseMessage", "EXPIRED WHITE");
		spectroMessage.put("rc", "0");
		spectroMessage.put("errorCode", "0");
		spectroMessage.put("errorMessage", "");
		spectroMessage.put("deltaE", "0");
		//spectroMessage.put("spectroConfig", "");
		target.setSpectroMessage(spectroMessage);
		
		try {
			String json = executeAction("/logSpectroEventAction");
			VerifyDefaultSpectroSettingsAction result = new Gson().fromJson(json,VerifyDefaultSpectroSettingsAction.class);
			assertEquals("12345",result.getReqGuid());
		} catch (Exception e) {
			try {
				throw(e.getCause());
			} catch (Throwable e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
		
	}
}
