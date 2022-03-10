package com.sherwin.shercolor.customershercolorweb.util;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import com.sherwin.shercolor.customershercolorweb.web.model.RequestObject;

public interface ShercolorLabelPrint {

	public void CreateLabelPdf(String filename, RequestObject reqObj, String printLabelType, String printOrientation, String canType, String clrntAmtList, boolean isCorrectionDispense, List<Map<String,Object>> correctionShotList);
	
}
