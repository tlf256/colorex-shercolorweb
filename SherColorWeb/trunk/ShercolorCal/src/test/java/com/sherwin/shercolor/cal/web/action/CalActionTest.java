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
import com.sherwin.shercolor.cal.web.action.CalTemplateAction;

@TransactionConfiguration(transactionManager = "transactionManager", defaultRollback = true)

@Transactional
public class CalActionTest extends StrutsSpringTestCase{

	CalTemplateAction target = new CalTemplateAction();
	
	@Override
	public String[] getContextLocations() {
		String[] arrStr =  {
				"classpath:spring.xml" 
		} ;  
		return arrStr;

	}

	@Test
	public void testAction(){
		ActionProxy proxy = getActionProxy("/getCalTemplate");

		 target = (CalTemplateAction) proxy.getAction();
		 
		 String filename = "CCE_COROBD600.zip";
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
}
