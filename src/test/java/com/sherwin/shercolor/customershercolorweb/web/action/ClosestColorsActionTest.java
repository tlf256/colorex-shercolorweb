package com.sherwin.shercolor.customershercolorweb.web.action;

import java.io.UnsupportedEncodingException;
import static org.junit.Assert.assertNotNull;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpSession;

import com.sherwin.shercolor.common.domain.ClosestColor;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.struts2.StrutsSpringJUnit4TestCase;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import com.sherwin.shercolor.colormath.domain.ColorCoordinates;
import com.sherwin.shercolor.common.service.ColorService;
import com.sherwin.shercolor.customershercolorweb.web.model.RequestObject;

import static org.junit.Assert.*;

@Transactional
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:config/spring/shercolorcommon-test.xml"})
public class ClosestColorsActionTest extends StrutsSpringJUnit4TestCase<ClosestColorsAction> {
	
	ClosestColorsAction target;
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
	
//	@Test
//	public void testGetSwAndCompetClosestColors() {

//		reqObj.setCustomerID("TEST");
//
//		Map<String, ColorCoordinates> coordMap = new HashMap<>();
//		ColorCoordinates colorCoord = service.getColorCoordinates(curve, "D65");
//
//		coordMap.put("colorCoord", colorCoord);
//		reqObj.setColorCoordMap(coordMap);
//
//		request.setParameter("reqGuid",reqGuid);
//		HttpSession session = request.getSession();
//		session.setAttribute(reqGuid, reqObj);
//
//		try {
//			String result = executeAction("/closestColorsResultAction");
//			assertNotNull(result);
//		} catch (Exception e) {
//			try {
//				throw(e.getCause());
//			} catch (Throwable e1) {
//				e1.printStackTrace();
//			}
//		}
//	}
//	}

	@Test
	public void testGetSwAndCompetClosestColors() throws ServletException, UnsupportedEncodingException {

		//arrange
		Map<String, ColorCoordinates> coordMap = new HashMap<>();
		ColorCoordinates colorCoord = service.getColorCoordinates(curve, "D65");
		assertNotNull(colorCoord);
		coordMap.put("colorCoord", colorCoord);

		reqObj.setColorCoordMap(coordMap);
		reqObj.setCustomerID("TEST");
		HttpSession session = request.getSession();
		session.setAttribute(reqGuid, reqObj);

		request.setParameter("reqGuid",reqGuid);
		request.setParameter("swactive","false");
		request.setParameter("closestColors","true");

		//act
		String json = executeAction("/closestColorsResultAction");
		String result = (String) findValueAfterExecute("reqGuid");
		List<ClosestColor> closestSwColors = (List<ClosestColor>) findValueAfterExecute("closestSwColors");
		List<ClosestColor> closestCmptColors = (List<ClosestColor>) findValueAfterExecute("closestCmptColors");

		//assert
		assertEquals("12345",result);
		assertTrue(CollectionUtils.isNotEmpty(closestSwColors));
		assertTrue(CollectionUtils.isNotEmpty(closestCmptColors));
	}
	
	@Test
	public void testClosestColorDisplay() {

		reqObj.setCustomerID("TEST");
		
		request.setParameter("reqGuid",reqGuid);
		HttpSession session = request.getSession();
		session.setAttribute(reqGuid, reqObj);
		
		try {
			String result = executeAction("/closestColorsDisplayAction");
			assertNotNull(result);
		} catch (Exception e) {
			try {
				throw(e.getCause());
			} catch (Throwable e1) {
				e1.printStackTrace();
			}
		}
	}
}
