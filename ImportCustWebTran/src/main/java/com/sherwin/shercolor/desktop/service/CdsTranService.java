package com.sherwin.shercolor.desktop.service;

import java.util.List;
import java.util.function.BiConsumer;

import com.sherwin.shercolor.common.domain.CustWebTran;
import com.sherwin.shercolor.desktop.domain.CdsTranAll;
import com.sherwin.shercolor.desktop.domain.CdsTranClrnt;
import com.sherwin.shercolor.desktop.domain.CdsTranDet;
import com.sherwin.shercolor.desktop.domain.CdsTranMast;

public interface CdsTranService {
	
	public void setProgressUpdate(BiConsumer<Integer, Integer> progressUpdate);
	
	public void setLinesComplete(int linesComplete);
	
	public void setTotalLines(int totalLines);	
	
	public List<CdsTranMast> ImportCdsTranMast(String inputDir);

	public List<CdsTranDet> ImportCdsTranDet(String inputDir);
	
	public List<CdsTranClrnt> ImportCdsTranClrnt(String inputDir);
	
	public int CountLinesInAllFiles(String inputDir);
	
	public List<CdsTranAll> BuildAllTran(List<CdsTranMast> tranMastList, List<CdsTranDet> tranDetList, List<CdsTranClrnt> tranClrntList);
	
	public CustWebTran ConvertToCustWeb(CdsTranAll cdsTranAll);
	
}
