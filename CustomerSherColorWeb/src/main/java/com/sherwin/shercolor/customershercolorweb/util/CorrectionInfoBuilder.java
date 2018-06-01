package com.sherwin.shercolor.customershercolorweb.util;

import java.util.List;

import com.sherwin.shercolor.common.domain.CustWebTranCorr;
import com.sherwin.shercolor.customershercolorweb.web.model.CorrectionInfo;
import com.sherwin.shercolor.customershercolorweb.web.model.RequestObject;

public interface CorrectionInfoBuilder {

	public CorrectionInfo getCorrectionInfo(RequestObject reqObj, List<CustWebTranCorr> tranCorrList);
	
}
