package com.sherwin.shercolor.customershercolorweb.web.action;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.opensymphony.xwork2.Action;
import com.opensymphony.xwork2.ActionProxy;
import org.apache.commons.lang3.StringUtils;
import org.apache.struts2.StrutsSpringJUnit4TestCase;
import org.apache.struts2.dispatcher.mapper.ActionMapping;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.http.converter.json.GsonBuilderUtils;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.ServletException;
import java.io.UnsupportedEncodingException;

import static org.junit.Assert.*;

@Transactional
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:config/spring/shercolorcommon-test.xml"})
public class ProcessManualFormulaActionTest extends StrutsSpringJUnit4TestCase<ProcessManualFormulaAction> {

    @Test
    public void sizeAdjustment_RequestObjectNull_ErrorResultReturnedWithoutException() throws Exception {

        //Set params
        // No need, request object is already null by way of not setting it.

        //Execute action
        ActionProxy proxy = getActionProxy("/sizeAdjustmentAction.action");
        assertNotNull(proxy);

        ProcessManualFormulaAction action = (ProcessManualFormulaAction) proxy.getAction();
        assertNotNull(action);

        //Assert
        String result = proxy.execute();
        // Would throw an exception if result mapping configuration was not present.
        assertEquals(Action.ERROR, result);
    }
}
