package com.sherwin.shercolor.customershercolorweb.web.action;

import com.opensymphony.xwork2.Action;
import com.opensymphony.xwork2.ActionProxy;
import com.sherwin.shercolor.customershercolorweb.annotation.SherColorWebTransactionalTest;
import org.apache.struts2.StrutsSpringJUnit4TestCase;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@RunWith(SpringJUnit4ClassRunner.class)
@SherColorWebTransactionalTest
public class SaveNewJobActionTest extends StrutsSpringJUnit4TestCase<SaveNewJobAction> {

    @Test
    public void sizeAdjustment_RequestObjectNull_ErrorResultReturnedWithoutException() throws Exception {

        //Set params
        // No need, request object is already null by way of not setting it.

        //Execute action
        ActionProxy proxy = getActionProxy("/saveBeforeAction.action");
        assertNotNull(proxy);

        SaveNewJobAction action = (SaveNewJobAction) proxy.getAction();
        assertNotNull(action);

        //Assert
        String result = proxy.execute();
        // Would throw an exception if result mapping configuration was not present.
        assertEquals(Action.ERROR, result);
    }
}
