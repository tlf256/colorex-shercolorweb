package com.sherwin.shercolor.shercolorweb.util;

import java.util.List;


import com.sherwin.shercolor.common.domain.CustWebTranCorr;
import com.sherwin.shercolor.shercolorweb.web.model.CorrectionInfo;
import com.sherwin.shercolor.shercolorweb.web.model.RequestObject;

public interface CorrectionInfoBuilder {

	public CorrectionInfo getCorrectionInfo(RequestObject reqObj, List<CustWebTranCorr> tranCorrList);
	
}
