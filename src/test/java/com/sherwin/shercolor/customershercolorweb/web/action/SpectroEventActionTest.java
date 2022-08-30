package com.sherwin.shercolor.customershercolorweb.web.action;

import org.apache.struts2.StrutsSpringJUnit4TestCase;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import com.sherwin.shercolor.customershercolorweb.web.model.RequestObject;

@Transactional
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:config/spring/shercolorcommon-test.xml"})
public class SpectroEventActionTest extends StrutsSpringJUnit4TestCase<SpectroEventAction>{

	SpectroEventAction target;
	RequestObject reqObj = new RequestObject();
}
