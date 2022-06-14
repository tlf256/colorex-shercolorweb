package com.sherwin.shercolor.customershercolorweb.web.action;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

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
import com.sherwin.shercolor.colormath.domain.ColorCoordinates;
import com.sherwin.shercolor.customershercolorweb.web.model.RequestObject;

@Transactional
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:config/spring/shercolorcommon-test.xml"})
public class ClosestColorsActionTest extends StrutsSpringJUnit4TestCase<ClosestColorsAction> {
	
	ClosestColorsAction target;
	RequestObject reqObj = new RequestObject();
	String reqGuid = "12345";
	
	@Test
	public void getSwAndCompetClosestColors() {
		ActionProxy proxy = getActionProxy("/closestColorsResultAction");
		target = (ClosestColorsAction) proxy.getAction();
		
		target.setSwactive(false);
		target.setIntExt("E");
		target.setReqGuid(reqGuid);
		
		Map<String, ColorCoordinates> coordMap = new HashMap<>();
		ColorCoordinates coord = new ColorCoordinates();
		coord.setCieX(10.741714537);
		coord.setCieY(12.870018718);
		coord.setCieZ(17.288416401);
		coord.setCieL(42.56673975309436);
		coord.setCieA(-10.502497705042913);
		coord.setCieB(-7.851476080618114);
		coord.setCieC(13.112899553071777);
		coord.setCieH(-2.212747986774148);
		coord.setRgbRed(72);
		coord.setRgbGreen(106);
		coord.setRgbBlue(113);
		coord.setRgbHex("#486a71");
		
		coordMap.put("colorCoord", coord);
		reqObj.setColorCoordMap(coordMap);
		
		request.setParameter("reqGuid",reqGuid);
		HttpSession session = request.getSession();
		session.setAttribute(reqGuid, reqObj);
		
		try {
			String json = executeAction("/closestColorsResultAction");
			assertNotNull(json);
			ClosestColorsAction result = new Gson().fromJson(json,ClosestColorsAction.class);
			System.out.println(json);
			assertEquals("12345",result.getReqGuid());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			try {
				throw(e.getCause());
			} catch (Throwable e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
	}
	
	@Test
	public void getSwActiveClosestColorsOnly() {
		ActionProxy proxy = getActionProxy("/closestColorsResultAction");
		target = (ClosestColorsAction) proxy.getAction();
		
		target.setSwactive(true);
		target.setIntExt("I");
		target.setReqGuid(reqGuid);
		
		Map<String, ColorCoordinates> coordMap = new HashMap<>();
		ColorCoordinates coord = new ColorCoordinates();
		coord.setCieX(10.741714537);
		coord.setCieY(12.870018718);
		coord.setCieZ(17.288416401);
		coord.setCieL(42.56673975309436);
		coord.setCieA(-10.502497705042913);
		coord.setCieB(-7.851476080618114);
		coord.setCieC(13.112899553071777);
		coord.setCieH(-2.212747986774148);
		coord.setRgbRed(72);
		coord.setRgbGreen(106);
		coord.setRgbBlue(113);
		coord.setRgbHex("#486a71");
		
		coordMap.put("colorCoord", coord);
		reqObj.setColorCoordMap(coordMap);
		
		request.setParameter("reqGuid",reqGuid);
		HttpSession session = request.getSession();
		session.setAttribute(reqGuid, reqObj);
		
		try {
			String json = executeAction("/closestColorsResultAction");
			assertNotNull(json);
			ClosestColorsAction result = new Gson().fromJson(json,ClosestColorsAction.class);
			System.out.println(json);
			assertEquals("12345",result.getReqGuid());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			try {
				throw(e.getCause());
			} catch (Throwable e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
	}
}
