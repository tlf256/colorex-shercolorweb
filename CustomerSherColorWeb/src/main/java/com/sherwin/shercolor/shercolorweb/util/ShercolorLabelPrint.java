package com.sherwin.shercolor.shercolorweb.util;

import java.util.List;
import java.util.Map;

import com.sherwin.shercolor.shercolorweb.web.model.RequestObject;

public interface ShercolorLabelPrint {

	public void CreateLabelPdf(String filename, RequestObject reqObj, String printLabelType, String printOrientation, String canType, String clrntAmtList, boolean isCorrectionDispense, List<Map<String,Object>> correctionShotList);
	
}
