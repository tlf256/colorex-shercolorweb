package com.sherwin.shercolor.cal.web.action;
import static org.junit.Assert.*;

import java.util.List;

import org.apache.struts2.StrutsSpringTestCase;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;

import com.opensymphony.xwork2.ActionProxy;
import com.sherwin.shercolor.cal.domain.CalTemplate;
import com.sherwin.shercolor.cal.domain.Ecal;
import com.sherwin.shercolor.cal.web.action.CalTemplateAction;

@TransactionConfiguration(transactionManager = "transactionManager", defaultRollback = true)

@Transactional
public class ECalActionTest extends StrutsSpringTestCase{

	EcalAction target = new EcalAction();
	
	@Override
	public String[] getContextLocations() {
		String[] arrStr =  {
				"classpath:spring.xml" 
		} ;  
		return arrStr;

	}

	@Test
	public void testSelectEcalAction(){
		ActionProxy proxy = getActionProxy("/selectECal");

		 target = (EcalAction) proxy.getAction();
		 
		 String filename = "CCE_COROBD600_434344_20170912_1131.zip";
		target.setFilename(filename);
		
		
	


		try {
			String result = proxy.execute();
			//assertEquals("success",result);
			
			byte[] data = target.getData();
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
		ActionProxy proxy = getActionProxy("/getECalList");

		 target = (EcalAction) proxy.getAction();
		 
		 String filename = "CCE_COROBD600_434344_20170912_1131.zip";
		target.setFilename(filename);
		target.setCustomerid("TEST");
		target.setColorantid("CCE");
		target.setTintermodel("COROBD600");
		target.setTinterserial("434344");
		
	


		try {
			String result = proxy.execute();
			//assertEquals("success",result);
			
			List<Ecal> list = target.getEcalList();
			assertNotNull(list.get(0));
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
	public void testUploadEcalAction(){
		ActionProxy proxy = getActionProxy("/uploadEcal");

		 target = (EcalAction) proxy.getAction();
		 
		 String filename = "CCE_COROBD600_434344_20170912_1131.zip";
		target.setFilename(filename);
		target.setCustomerid("TEST");
		target.setColorantid("CCE");
		target.setTintermodel("COROBD600");
		target.setTinterserial("434344");
		
	


		try {
			String result = proxy.execute();
			//assertEquals("success",result);
			
			
			assertEquals("success",result);
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
