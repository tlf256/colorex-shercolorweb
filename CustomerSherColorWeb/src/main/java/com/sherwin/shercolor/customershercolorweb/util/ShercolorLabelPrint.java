package com.sherwin.shercolor.customershercolorweb.util;

import java.io.IOException;


import com.sherwin.shercolor.customershercolorweb.web.model.RequestObject;

public interface ShercolorLabelPrint {

	public void CreateLabelPdf(String filename, RequestObject reqObj, String printLabelType, String printOrientation, String canType, String clrntAmtList);
	
}
