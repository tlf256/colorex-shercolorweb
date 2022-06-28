package com.sherwin.shercolor.customershercolorweb.util;

import java.util.List;
import java.util.Map;

import com.sherwin.shercolor.customershercolorweb.web.model.RequestObject;

public interface ShercolorLabelPrint {

	public boolean CreateLabelPdf(RequestObject reqObj, String printLabelType, String printOrientation, String canType, String clrntAmtList, boolean isCorrectionDispense, List<Map<String,Object>> correctionShotList);
	
}
