package com.sherwin.shercolor.customershercolorweb.web.action;

import static org.junit.Assert.*;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.apache.struts2.StrutsSpringJUnit4TestCase;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import com.opensymphony.xwork2.Action;
import com.opensymphony.xwork2.ActionProxy;
import com.sherwin.shercolor.colormath.domain.ColorCoordinates;
import com.sherwin.shercolor.colormath.domain.ColorDifference;
import com.sherwin.shercolor.common.service.ColorService;
import com.sherwin.shercolor.customershercolorweb.web.model.RequestObject;

@Transactional
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:config/spring/shercolorcommon-test.xml"})
public class ProcessCompareColorsActionTest extends StrutsSpringJUnit4TestCase<ProcessCompareColorsAction> {
	
	ProcessCompareColorsAction target;
	RequestObject reqObj = new RequestObject();
	String reqGuid = "12345";
	BigDecimal[] curve = {new BigDecimal(8.5454),new BigDecimal(9.8891),new BigDecimal(13.0628),new BigDecimal(20.4314),
			new BigDecimal(31.6782),new BigDecimal(39.3078),new BigDecimal(41.9917),new BigDecimal(43.7609),new BigDecimal(45.9493),
			new BigDecimal(47.5962),new BigDecimal(48.1183),new BigDecimal(48.3551),new BigDecimal(48.7558),new BigDecimal(49.5168),
			new BigDecimal(50.8593),new BigDecimal(52.5787),new BigDecimal(54.5094),new BigDecimal(56.5196),new BigDecimal(58.3763),
			new BigDecimal(59.9248),new BigDecimal(61.3052),new BigDecimal(62.8414),new BigDecimal(64.1409),new BigDecimal(64.8976),
			new BigDecimal(65.1403),new BigDecimal(65.0295),new BigDecimal(64.8936),new BigDecimal(64.6806),new BigDecimal(64.5444),
			new BigDecimal(64.4543),new BigDecimal(64.3829),new BigDecimal(64.3577),new BigDecimal(64.229),new BigDecimal(64.0975),
			new BigDecimal(63.898),new BigDecimal(63.7873),new BigDecimal(63.8011),new BigDecimal(63.9099),new BigDecimal(63.9851),new BigDecimal(63.8023)};
	
	@Autowired
	private transient ColorService service;
	
	@Test
	public void testSpectroMeasureSampleAction_success() {
		reqObj.setCustomerID("TEST");
		
		request.setParameter("reqGuid",reqGuid);
		request.setParameter("compareColors", "true");
		
		ActionProxy proxy = getActionProxy("/spectroMeasureSampleAction");
        assertNotNull(proxy);
		
		target = (ProcessCompareColorsAction) proxy.getAction();
		
		Map<String, Object> sessionMap = new HashMap<>();
        sessionMap.put(reqGuid, reqObj);
        
        proxy.getInvocation().getInvocationContext().setSession(sessionMap);
        
        try {
			String result = proxy.execute();
			
			assertEquals(Action.SUCCESS, result);
		} catch (Exception e) {
			try {
				throw(e.getCause());
			} catch (Throwable e1) {
				e1.printStackTrace();
			}
		}
	}
	
	@Test
	public void testSpectroCompareColorsResultAction() {
		reqObj.setCustomerID("TEST");

		Map<String, ColorCoordinates> coordMap = new HashMap<>();
		ColorCoordinates colorCoord = service.getColorCoordinates(curve, "D65");

		coordMap.put("standard", colorCoord);
		coordMap.put("sample", colorCoord);
		reqObj.setColorCoordMap(coordMap);
		
		request.setParameter("reqGuid",reqGuid);
		request.setParameter("compareColors", "true");
		HttpSession session = request.getSession();
		session.setAttribute(reqGuid, reqObj);
		
		try {
			executeAction("/spectroCompareColorsResultAction");
			String actionGuid = (String) findValueAfterExecute("reqGuid");
			assertEquals(actionGuid, reqGuid);
			
			ColorDifference diff = (ColorDifference) findValueAfterExecute("colorDiff");
			
			assertTrue(diff != null);
		} catch (Exception e) {
			try {
				throw(e.getCause());
			} catch (Throwable e1) {
				e1.printStackTrace();
			}
		}
	}

}
