package com.sherwin.shercolor.customershercolorweb.web.action;

import com.sherwin.shercolor.customershercolorweb.annotation.SherColorWebTransactionalTest;
import com.sherwin.shercolor.customershercolorweb.web.model.RequestObject;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.entity.ContentType;
import org.apache.struts2.StrutsSpringJUnit4TestCase;
import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.springframework.test.context.junit4.rules.SpringClassRule;
import org.springframework.test.context.junit4.rules.SpringMethodRule;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

@RunWith(Parameterized.class)
@SherColorWebTransactionalTest
public class DownloadPdfActionParameterizedTest extends StrutsSpringJUnit4TestCase<DownloadPdfAction> {

	RequestObject reqObj = new RequestObject();
	private static final String reqGuid = "12345";
	private static final String customerId = "678910";

	private final String filename;

	public DownloadPdfActionParameterizedTest(String filename) {
		this.filename = filename;
	}

	@ClassRule
	public static final SpringClassRule SPRING_CLASS_RULE
			= new SpringClassRule();

	@Rule
	public final SpringMethodRule springMethodRule
			= new SpringMethodRule();

	@Parameterized.Parameters
	public static List<String> data() {
		return Arrays.asList("SherColor_Web_Customer_Guide",
				"SherColor_Web_XProtint_Tinter_Installation_Guide",
				"SherColor_Web_Accutinter_Installation_Guide",
				"SherColor_Web_Fluid_Management_Calibration",
				"SherColor_Web_Color_Eye_Installation",
				"SherColor_Web_Corob_Installation_Guide",
				"SherColor_Web_Corob_Calibration",
				"SherColor_Web_Dymo_Install",
				"SherColor_Web_Zebra_Install");
	}

	@Test
	public void execute_NoInputs_ProperPdfReturned() throws Exception {

		//arrange
		request.setParameter("reqGuid",reqGuid);
		request.setParameter("pdfType",filename);
		reqObj.setCustomerID(customerId);
		Objects.requireNonNull(request.getSession()).setAttribute(reqGuid, reqObj);

		//act
		String json = executeAction("/downloadPdfAction.action");

		//assert
		assertTrue(StringUtils.isNotBlank(json));
		assertEquals(response.getContentType(), ContentType.APPLICATION_OCTET_STREAM.getMimeType());
		String contentDisposition = response.getHeader("Content-Disposition");
		assertTrue(StringUtils.isNotBlank(contentDisposition));
		assertTrue(StringUtils.contains(contentDisposition,filename));
		assertTrue(StringUtils.contains(contentDisposition,".pdf"));
	}
}
