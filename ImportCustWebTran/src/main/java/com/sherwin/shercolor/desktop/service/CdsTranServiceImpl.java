package com.sherwin.shercolor.desktop.service;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Scanner;
import java.util.function.BiConsumer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import com.sherwin.shercolor.common.domain.CdsClrnt;
import com.sherwin.shercolor.common.domain.CdsColorMast;
import com.sherwin.shercolor.common.domain.CustWebTran;
import com.sherwin.shercolor.common.domain.PosProd;
import com.sherwin.shercolor.common.service.ColorMastService;
import com.sherwin.shercolor.common.service.ProductService;
import com.sherwin.shercolor.desktop.app.ImportCustWebTran;
import com.sherwin.shercolor.desktop.domain.CdsTranAll;
import com.sherwin.shercolor.desktop.domain.CdsTranClrnt;
import com.sherwin.shercolor.desktop.domain.CdsTranDet;
import com.sherwin.shercolor.desktop.domain.CdsTranDetWithClrnt;
import com.sherwin.shercolor.desktop.domain.CdsTranMast;

@Service
public class CdsTranServiceImpl implements CdsTranService {

	@Autowired
	private ProductService productService;
	
	@Autowired
	private ColorMastService colorMastService;
	
	private BiConsumer<Integer, Integer> progressUpdate ;
	
	public void setProgressUpdate(BiConsumer<Integer, Integer> progressUpdate) {
        this.progressUpdate = progressUpdate ;
    }
    
    private int linesComplete;
    
    public void setLinesComplete(int linesComplete){
    	this.linesComplete = linesComplete;
    }
    
    private int totalLines;
    
    public void setTotalLines(int totalLines){
    	this.totalLines = totalLines;
    }
    
	public List<CdsTranMast> ImportCdsTranMast(String inputDir){
		List<CdsTranMast> recordList = new ArrayList<CdsTranMast>();
		
		
		String fullFileAndPath = inputDir + "/" + "cds-tran-mast.d";
		try {
			File inFile = new File(fullFileAndPath);
			FileInputStream fis = new FileInputStream(inFile);
			BufferedReader br = new BufferedReader(new InputStreamReader(fis));
			String line = null;
			SimpleDateFormat df = new SimpleDateFormat("MM/dd/yy");
			//String regex = "[^\"\\s]+|\"(\\\\.|[^\\\\\"])*\"";
			int pos;
			int linecnt = 0;
			while ((line = br.readLine()) != null){
//				Scanner scanner = new Scanner(line);
//				CdsTranMast newRec = new CdsTranMast();
//				newRec.setControlNbr(Integer.parseInt(scanner.findInLine(regex)));
//				newRec.setLineNbr(Integer.parseInt(scanner.findInLine(regex)));
//				newRec.setSalesNbr(scanner.findInLine(regex));
//				newRec.setColorComp(scanner.findInLine(regex));
//				newRec.setColorId(scanner.findInLine(regex));
//				newRec.setColorName(scanner.findInLine(regex));
//				newRec.setPrefBase(scanner.findInLine(regex));
//				
//				System.out.println(newRec.getControlNbr() + "," + newRec.getLineNbr() + "," + newRec.getColorComp() + "," + newRec.getColorId() + "," + newRec.getColorName() + "," + newRec.getPrefBase() + "end");
				
				String[] splitted = line.split(" (?=([^\"]*\"[^\"]*\")*[^\"]*$)");
				CdsTranMast newRec = new CdsTranMast();
				pos = 0;
				newRec.setControlNbr(Integer.parseInt(splitted[pos])); pos++;
				newRec.setLineNbr(Integer.parseInt(splitted[pos])); pos++;
				newRec.setSalesNbr(splitted[pos].replaceAll("\"", "")); pos++;
				newRec.setColorComp(splitted[pos].replaceAll("\"", "")); pos++;
				newRec.setColorId(splitted[pos].replaceAll("\"", "")); pos++;
				newRec.setColorName(splitted[pos].replaceAll("\"", "")); pos++;
				newRec.setPrefBase(splitted[pos].replaceAll("\"", "")); pos++;
				newRec.setActBase(splitted[pos].replaceAll("\"", "")); pos++;
				
				newRec.setSpectroModel(splitted[pos].replaceAll("\"", "")); pos++;
				newRec.setSpectroMode(splitted[pos].replaceAll("\"", "")); pos++;
				newRec.setTranStatus(splitted[pos].replaceAll("\"", "")); pos++;
				newRec.setProdSource(splitted[pos].replaceAll("\"", "")); pos++;
				newRec.setQtyComplete(Integer.parseInt(splitted[pos])); pos++;
				newRec.setQtyInProg(Integer.parseInt(splitted[pos])); pos++;
				newRec.setNeedExtracted(ConvertYesNoToBoolean(splitted[pos].replaceAll("\"", ""))); pos++; 
				newRec.setCdsAdlField(splitted[pos].replaceAll("\"", "")); pos++;
				newRec.setQtyMistint(Integer.parseInt(splitted[pos])); pos++;
				newRec.setQtyRecon(Integer.parseInt(splitted[pos])); pos++;
				newRec.setColorAutoRetrieve(ConvertYesNoToBoolean(splitted[pos].replaceAll("\"", ""))); pos++;
				newRec.setOrigColorId(splitted[pos].replaceAll("\"", "")); pos++;
				newRec.setOrigColorComp(splitted[pos].replaceAll("\"", "")); pos++;
				newRec.setQtyNosale(Integer.parseInt(splitted[pos])); pos++;
				newRec.setQtyNosaleRecon(Integer.parseInt(splitted[pos])); pos++;
				newRec.setcFld1(splitted[pos].replaceAll("\"", "")); pos++;
				newRec.setcFld2(splitted[pos].replaceAll("\"", "")); pos++;
				newRec.setcFld3(splitted[pos].replaceAll("\"", "")); pos++;
				newRec.setcFld4(splitted[pos].replaceAll("\"", "")); pos++;
				newRec.setcFld5(splitted[pos].replaceAll("\"", "")); pos++;
				newRec.setcFld6(splitted[pos].replaceAll("\"", "")); pos++;
				newRec.setcFld7(splitted[pos].replaceAll("\"", "")); pos++;
				newRec.setcFld8(splitted[pos].replaceAll("\"", "")); pos++;
				newRec.setcFld9(splitted[pos].replaceAll("\"", "")); pos++;
				newRec.setcFld10(splitted[pos].replaceAll("\"", "")); pos++;
				newRec.setActiveStatus(splitted[pos].replaceAll("\"", "")); pos++;
				newRec.setProdRev(splitted[pos].replaceAll("\"", "")); pos++;
				newRec.setClrntSysSuffix(splitted[pos].replaceAll("\"", "")); pos++;
				newRec.setTintableSalesNbr(splitted[pos].replaceAll("\"", "")); pos++;
				newRec.setLocId(splitted[pos].replaceAll("\"", "")); pos++;
				newRec.setMqExtractedState(Integer.parseInt(splitted[pos])); pos++;
				// leave MqSentDate null newRec.setMqSentDate();
				pos++;
				newRec.setRoomUse(splitted[pos].replaceAll("\"", "")); pos++;
				newRec.setCustomFormulaScope(splitted[pos].replaceAll("\"", "")); pos++;
				
				linecnt++;
				if(linecnt%1000==0) {
					System.out.println("processed tranMast " + linecnt);
					if (progressUpdate != null) {
		            	progressUpdate.accept(linesComplete + linecnt, totalLines);
					}
				}
				//System.out.println(newRec.getControlNbr() + "," + newRec.getLineNbr() + "," + newRec.getColorComp() + "," + newRec.getColorId() + "," + newRec.getColorName() + "," + newRec.getPrefBase() + "<<end");
			
				recordList.add(newRec);
			} // end while reading 
			br.close();
			
		} catch (NullPointerException e) {
			System.out.println("Directory does not exist " + fullFileAndPath + "\n" + e.toString());
		} catch (SecurityException e){
			System.out.println("Do not have access to " + fullFileAndPath + "\n" + e.toString());
		} catch (FileNotFoundException e) {
			System.out.println("File does not exist in " + fullFileAndPath + "\n" + e.toString());
		} catch (IOException e) {
			System.out.println("Unable to read file in " + fullFileAndPath + "\n" + e.toString());
		}

		
		
		return recordList;
	}


	public List<CdsTranDet> ImportCdsTranDet(String inputDir){
		List<CdsTranDet> recordList = new ArrayList<CdsTranDet>();
		
		
		String fullFileAndPath = inputDir + "/" + "cds-tran-det.d";
		try {
			File inFile = new File(fullFileAndPath);
			FileInputStream fis = new FileInputStream(inFile);
			BufferedReader br = new BufferedReader(new InputStreamReader(fis));
			SimpleDateFormat df = new SimpleDateFormat("MM/dd/yy");
			String line = null;
			//String regex = "[^\"\\s]+|\"(\\\\.|[^\\\\\"])*\"";
			int pos;
			int linecnt = 0;
			while ((line = br.readLine()) != null){
				String[] splitted = line.split(" (?=([^\"]*\"[^\"]*\")*[^\"]*$)");
				CdsTranDet newRec = new CdsTranDet();
				pos=0;
				newRec.setControlNbr(Integer.parseInt(splitted[pos])); pos++;
				newRec.setLineNbr(Integer.parseInt(splitted[pos])); pos++;
				newRec.setTranDetSeq(Integer.parseInt(splitted[pos])); pos++;
				newRec.setTranMastUnit(Integer.parseInt(splitted[pos])); pos++;
				newRec.setTranCode(splitted[pos].replaceAll("\"", "")); pos++;
				try {
					newRec.setTranDate(df.parse(splitted[pos]));
				} catch (Exception e) {
					// leave tran date null
				} finally {
					pos++;
				}
				newRec.setTranTime(splitted[pos].replaceAll("\"", "")); pos++;
				newRec.setSourceInd(splitted[pos].replaceAll("\"", "")); pos++;
				newRec.setFormSource(splitted[pos].replaceAll("\"", "")); pos++;
				newRec.setDestId(splitted[pos].replaceAll("\"", "")); pos++;
				newRec.setUserId(splitted[pos].replaceAll("\"", "")); pos++;
				newRec.setNeedExtracted(splitted[pos].replaceAll("\"", "")); pos++;
				newRec.setLotNbr(splitted[pos].replaceAll("\"", "")); pos++;
				
				double[] projCurve = {0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0};
				for(int i=0;i<40;i++){
					projCurve[i] = Double.parseDouble(splitted[pos]); pos++;
				}
				newRec.setProjectCurve(projCurve);

				double[] measCurve = {0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0};
				for(int i=0;i<40;i++){
					measCurve[i] = Double.parseDouble(splitted[pos]); pos++;
				}
				newRec.setMeasureCurve(measCurve);

				newRec.setIllumPrimary(splitted[pos].replaceAll("\"", "")); pos++;
				newRec.setIllumSecondary(splitted[pos].replaceAll("\"", "")); pos++;
				newRec.setIllumTertiary(splitted[pos].replaceAll("\"", "")); pos++;
				newRec.setSpecDev(Double.parseDouble(splitted[pos])); pos++;
				newRec.setDeltaePrimary(Double.parseDouble(splitted[pos])); pos++;
				newRec.setDeltaeSecondary(Double.parseDouble(splitted[pos])); pos++;
				newRec.setDeltaeTertiary(Double.parseDouble(splitted[pos])); pos++;
				newRec.setMetamerismIdx(Double.parseDouble(splitted[pos])); pos++;
				newRec.setDeltaeFilm2(Double.parseDouble(splitted[pos])); pos++;
				newRec.setOdDeltaeFilm1(Double.parseDouble(splitted[pos])); pos++;
				newRec.setOdDeltaeFilm2(Double.parseDouble(splitted[pos])); pos++;
				newRec.setCrFilm1(Double.parseDouble(splitted[pos])); pos++;
				newRec.setCrFilm2(Double.parseDouble(splitted[pos])); pos++;
				newRec.setCost(Double.parseDouble(splitted[pos])); pos++;
				newRec.setNotes(splitted[pos].replaceAll("\"", "")); pos++;
				newRec.setCdsAdlFld(splitted[pos].replaceAll("\"", "")); pos++;
				newRec.setQtyDispensed(Integer.parseInt(splitted[pos])); pos++;
				newRec.setCorrCycle(Integer.parseInt(splitted[pos])); pos++;
				newRec.setFormPct(Integer.parseInt(splitted[pos])); pos++;
				newRec.setRule(splitted[pos].replaceAll("\"", "")); pos++;
				newRec.setStoreNbr(Integer.parseInt(splitted[pos])); pos++;
				newRec.setColorEngVer(splitted[pos].replaceAll("\"", "")); pos++;
				newRec.setPercentSource(splitted[pos].replaceAll("\"", "")); pos++;
				newRec.setMqExtractedState(Integer.parseInt(splitted[pos])); pos++;
				// leave MqSentDate null newRec.setMqSentDate();
				pos++;
				newRec.setTinterSerialNbr(splitted[pos].replaceAll("\"", "")); pos++;
				newRec.setSpectroSerialNbr(splitted[pos].replaceAll("\"", "")); pos++;
				newRec.setSpectroModel(splitted[pos].replaceAll("\"", "")); pos++;
				//newRec.setMqSentGuid(splitted[pos].replaceAll("\"", "")); pos++;
				
				linecnt++;
				if(linecnt%1000==0) {
					System.out.println("processed tranDet " + linecnt);
					if (progressUpdate != null) {
		            	progressUpdate.accept(linesComplete + linecnt, totalLines);
					}
				}
				//System.out.println(newRec.getControlNbr() + "," + newRec.getLineNbr() + "," + newRec.getTranDetSeq() + "," + newRec.getTranMastUnit() + "," + newRec.getTranCode() + "," + newRec.getTranDate() + "<<end");
				
				recordList.add(newRec);
			} // end while reading
			
			br.close();
			
		} catch (NullPointerException e) {
			System.out.println("Directory does not exist " + fullFileAndPath + "\n" + e.toString());
		} catch (SecurityException e){
			System.out.println("Do not have access to " + fullFileAndPath + "\n" + e.toString());
		} catch (FileNotFoundException e) {
			System.out.println("File does not exist in " + fullFileAndPath + "\n" + e.toString());
		} catch (IOException e) {
			System.out.println("Unable to read file in " + fullFileAndPath + "\n" + e.toString());
		}
		
		return recordList;
	}

	public List<CdsTranClrnt> ImportCdsTranClrnt(String inputDir){
		List<CdsTranClrnt> recordList = new ArrayList<CdsTranClrnt>();
		
		
		String fullFileAndPath = inputDir + "/" + "cds-tran-clrnt.d";
		try {
			File inFile = new File(fullFileAndPath);
			FileInputStream fis = new FileInputStream(inFile);
			BufferedReader br = new BufferedReader(new InputStreamReader(fis));
			SimpleDateFormat df = new SimpleDateFormat("MM/dd/yy");
			String line = null;
			//String regex = "[^\"\\s]+|\"(\\\\.|[^\\\\\"])*\"";
			int pos;
			int linecnt = 0;
			while ((line = br.readLine()) != null){
				String[] splitted = line.split(" (?=([^\"]*\"[^\"]*\")*[^\"]*$)");
				CdsTranClrnt newRec = new CdsTranClrnt();
				pos=0;

				newRec.setControlNbr(Integer.parseInt(splitted[pos])); pos++;
				newRec.setLineNbr(Integer.parseInt(splitted[pos])); pos++;
				newRec.setTranDetSeq(Integer.parseInt(splitted[pos])); pos++;
				newRec.setTranClrntSeq(Integer.parseInt(splitted[pos])); pos++;

				newRec.setClrntSys(splitted[pos].replaceAll("\"", "")); pos++;
				newRec.setClrntCode(splitted[pos].replaceAll("\"", "")); pos++;
				newRec.setClrntAmt(Integer.parseInt(splitted[pos])); pos++;
				newRec.setClrntQual(splitted[pos].replaceAll("\"", "")); pos++;
				newRec.setNeedExtracted(ConvertYesNoToBoolean(splitted[pos].replaceAll("\"", ""))); pos++;
				newRec.setCdsAdlFld(splitted[pos].replaceAll("\"", "")); pos++;
				newRec.setMqExtractedState(Integer.parseInt(splitted[pos])); pos++;
				// leave MqSentDate null newRec.setMqSentDate();
				pos++;
				
				linecnt++;
				if(linecnt%1000==0){
					System.out.println("processed tranClrnt " + linecnt);
					if (progressUpdate != null) {
		            	progressUpdate.accept(linesComplete + linecnt, totalLines);
					}
				}
				//System.out.println(newRec.getControlNbr() + "," + newRec.getLineNbr() + "," + newRec.getTranDetSeq() + "," + newRec.getTranClrntSeq() + "," + newRec.getClrntSys() + "," + newRec.getClrntCode() + "<<end");
				
				recordList.add(newRec);
			} // end while reading
			
			br.close();
			
		} catch (NullPointerException e) {
			System.out.println("Directory does not exist " + fullFileAndPath + "\n" + e.toString());
		} catch (SecurityException e){
			System.out.println("Do not have access to " + fullFileAndPath + "\n" + e.toString());
		} catch (FileNotFoundException e) {
			System.out.println("File does not exist in " + fullFileAndPath + "\n" + e.toString());
		} catch (IOException e) {
			System.out.println("Unable to read file in " + fullFileAndPath + "\n" + e.toString());
		}
		
		return recordList;
	}

	private boolean ConvertYesNoToBoolean(String YesNo){
		if(YesNo.equalsIgnoreCase("yes")){
			return true;
		} else {
			return false;
		}
	}
	
	public int CountLinesInAllFiles(String inputDir){
		int retVal = 0;
		
		try {
			String fullFileAndPath = inputDir + "/" + "cds-tran-mast.d";
			File inFile = new File(fullFileAndPath);
			FileInputStream fis = new FileInputStream(inFile);
			BufferedReader br = new BufferedReader(new InputStreamReader(fis));
			String line = null;
			while ((line = br.readLine()) != null){
				retVal++;
			}
			br.close();
		} catch (Exception e) {
			retVal = 0;
		}

		if(retVal>0){
			try {
				String fullFileAndPath = inputDir + "/" + "cds-tran-det.d";
				File inFile = new File(fullFileAndPath);
				FileInputStream fis = new FileInputStream(inFile);
				BufferedReader br = new BufferedReader(new InputStreamReader(fis));
				String line = null;
				while ((line = br.readLine()) != null){
					retVal++;
				}
				br.close();
			} catch (Exception e) {
				retVal = 0;
			}
		}

		if(retVal>0){
			try {
				String fullFileAndPath = inputDir + "/" + "cds-tran-det.d";
				File inFile = new File(fullFileAndPath);
				FileInputStream fis = new FileInputStream(inFile);
				BufferedReader br = new BufferedReader(new InputStreamReader(fis));
				String line = null;
				while ((line = br.readLine()) != null){
					retVal++;
				}
				br.close();
			} catch (Exception e) {
				retVal = 0;
			}
		}
		return retVal;
	}
	
	public List<CdsTranAll> BuildAllTran(List<CdsTranMast> tranMastList, List<CdsTranDet> tranDetList, List<CdsTranClrnt> tranClrntList){
		List<CdsTranAll> recordList = new ArrayList<CdsTranAll>();
		
		// put together TranDet with TranClrnt
		List<CdsTranDetWithClrnt> tranDetWithClrntList = new ArrayList<CdsTranDetWithClrnt>();
		int clrntPos = 0;
		int clrntListSize = tranClrntList.size();
		
		// assuming list are order lowest to highest with key controlNbr + lineNbr + tranDetSeq
		for(CdsTranDet tranDet : tranDetList){
			//System.out.println("Processing Det/Clrnt for " + tranDet.getControlNbr());
			CdsTranDetWithClrnt addDetWithClrnt = new CdsTranDetWithClrnt();
			addDetWithClrnt.setTranDet(tranDet);
			
			List<CdsTranClrnt> addClrntList = new ArrayList<CdsTranClrnt>();
			boolean stopClrntSearch = false;
			while (!stopClrntSearch){
				if (tranClrntList.get(clrntPos).getControlNbr()==tranDet.getControlNbr() &&
						tranClrntList.get(clrntPos).getLineNbr()==tranDet.getLineNbr() &&
						tranClrntList.get(clrntPos).getTranDetSeq()==tranDet.getTranDetSeq()) 
				{
						addClrntList.add(tranClrntList.get(clrntPos));
						clrntPos++;
						if(clrntPos>=clrntListSize) stopClrntSearch = true;
				} else {
						stopClrntSearch = true;
				}
			}
			addDetWithClrnt.setTranClrntList(addClrntList);
			
			tranDetWithClrntList.add(addDetWithClrnt);
			
		} // end creating tranDetWith
		
		
		// put together TranMast with TranDetWithClrnt
		int detPos = 0;
		int detListSize = tranDetWithClrntList.size();
		
		// assuming list are order lowest to highest with key controlNbr + lineNbr
		for(CdsTranMast tranMast : tranMastList){
			//System.out.println("Processing Mast for " + tranMast.getControlNbr());
			CdsTranAll addTranAll = new CdsTranAll();
			addTranAll.setTranMast(tranMast);
			
			List<CdsTranDetWithClrnt> addDetWithClrntList = new ArrayList<CdsTranDetWithClrnt>();
			boolean stopDetSearch = false;
			while (!stopDetSearch){
				if (tranDetWithClrntList.get(detPos).getTranDet().getControlNbr()==tranMast.getControlNbr() &&
						tranDetWithClrntList.get(detPos).getTranDet().getLineNbr()==tranMast.getLineNbr())
				{
						addDetWithClrntList.add(tranDetWithClrntList.get(detPos));
						detPos++;
						if(detPos>=detListSize) stopDetSearch = true;
				} else {
						stopDetSearch = true;
				}
			}
			addTranAll.setTranDetWithClrntList(addDetWithClrntList);
			
			recordList.add(addTranAll);
			
		} // end creating tranDetWith
		
		
		return recordList;
	}
	
	public CustWebTran ConvertToCustWeb(CdsTranAll cdsTranAll){
		CustWebTran retVal = new CustWebTran();
		int firstDispPos = -1;
		int lastGood = -1;
		int lastCorr = -1;

		if(cdsTranAll==null){
			System.out.println("cdsTranAll is null");
			retVal = null;
			return retVal;
		}
			
		if(cdsTranAll.getTranDetWithClrntList()==null){
			System.out.println("TranDetWithClrntList is null");
			retVal = null;
			return retVal;
		}
		
		if(cdsTranAll.getTranDetWithClrntList().size()==0){
			System.out.println("TranDetWithClrntList is empty (size = 0");
			retVal = null;
			return retVal;
		}
		
		try {
			// get first disp
			for (int i=0; i < cdsTranAll.getTranDetWithClrntList().size(); i++) {
				if(cdsTranAll.getTranDetWithClrntList().get(i).getTranDet().getTranCode().equalsIgnoreCase("DISP")){
					firstDispPos = i;
					break;
				}
			}

			for (int i =0; i < cdsTranAll.getTranDetWithClrntList().size(); i++) {
				if(lastGood>-1){
					if(cdsTranAll.getTranDetWithClrntList().get(i).getTranDet().getTranCode().equalsIgnoreCase("MTCH") ||
					   cdsTranAll.getTranDetWithClrntList().get(i).getTranDet().getTranCode().equalsIgnoreCase("DISP")){
						if (cdsTranAll.getTranDetWithClrntList().get(i).getTranDet().getSourceInd().equalsIgnoreCase("CORR")) {
							lastCorr = i;
						} else {
							lastGood = i;
						}
					}
				} else {
					if(cdsTranAll.getTranDetWithClrntList().get(i).getTranDet().getTranCode().equalsIgnoreCase("SAVE") ||
					   cdsTranAll.getTranDetWithClrntList().get(i).getTranDet().getTranCode().equalsIgnoreCase("MTCH") ||
					   cdsTranAll.getTranDetWithClrntList().get(i).getTranDet().getTranCode().equalsIgnoreCase("DISP")){
						if (!cdsTranAll.getTranDetWithClrntList().get(i).getTranDet().getSourceInd().equalsIgnoreCase("CORR")) {
							lastGood = i;
						}
					}
				} // end if stdDisp set
			}
			
			// ControlNbr and LineNbr (from tranMast)
			retVal.setControlNbr(cdsTranAll.getTranMast().getControlNbr());
			retVal.setLineNbr(cdsTranAll.getTranMast().getLineNbr());
			
			// ColorComp, ColorId, ColorName (all from tranMast)
			retVal.setColorComp(cdsTranAll.getTranMast().getColorComp());
			retVal.setColorId(cdsTranAll.getTranMast().getColorId());
			retVal.setColorName(cdsTranAll.getTranMast().getColorName());
			
			//OrigColorComp & OrigColorId (both from tranMast)
			retVal.setOrigColorComp(cdsTranAll.getTranMast().getOrigColorComp());
			retVal.setOrigColorId(cdsTranAll.getTranMast().getOrigColorId());
			
			// SalesNbr (from tranMast)
			retVal.setSalesNbr(cdsTranAll.getTranMast().getSalesNbr());

			// get prodNbr and size code from database
			if(productService==null) System.out.println("productService is null");
			
			PosProd posProd = productService.readPosProd(retVal.getSalesNbr());
			//System.out.println("salesnbr [" + retVal.getSalesNbr() + "] has a prodnbr of " + posProd.getProdNbr());
			retVal.setProdNbr(posProd.getProdNbr());
			retVal.setSizeCode(posProd.getSzCd());
			
			//clrntSysId
			int recPtr = 0;
			if (firstDispPos > -1) recPtr = firstDispPos;
			else recPtr = lastGood;
			retVal.setClrntSysId(cdsTranAll.getTranDetWithClrntList().get(recPtr).getTranClrntList().get(0).getClrntSys());
			retVal.setUserId("Imported");
			
			// ColorType (calculated from lastCorr else lastGood)
			recPtr = 0;
			if(lastCorr>-1) recPtr = lastCorr;
			else recPtr = lastGood;
			if((cdsTranAll.getTranDetWithClrntList().get(recPtr).getTranDet().getTranCode().equalsIgnoreCase("SAVE")||
				cdsTranAll.getTranDetWithClrntList().get(recPtr).getTranDet().getTranCode().equalsIgnoreCase("DISP")) &&
				cdsTranAll.getTranDetWithClrntList().get(recPtr).getTranDet().getSourceInd().equalsIgnoreCase("SW")) {
				// SW with SAVE or DISP
				if(cdsTranAll.getTranDetWithClrntList().get(recPtr).getTranDet().getFormSource().equalsIgnoreCase("ENG")||
				   cdsTranAll.getTranDetWithClrntList().get(recPtr).getTranDet().getFormSource().equalsIgnoreCase("FB") ||
				   cdsTranAll.getTranDetWithClrntList().get(recPtr).getTranDet().getFormSource().equalsIgnoreCase("HP") ||
				   cdsTranAll.getTranDetWithClrntList().get(recPtr).getTranDet().getFormSource().equalsIgnoreCase("FBVS") ||
				   cdsTranAll.getTranDetWithClrntList().get(recPtr).getTranDet().getFormSource().equalsIgnoreCase("VNL") ||
				   cdsTranAll.getTranDetWithClrntList().get(recPtr).getTranDet().getFormSource().equalsIgnoreCase("SCPCT") ||
				   cdsTranAll.getTranDetWithClrntList().get(recPtr).getTranDet().getFormSource().equalsIgnoreCase("PCT")){
					retVal.setColorType("SHERWIN-WILLIAMS");
				}
			}
			if((cdsTranAll.getTranDetWithClrntList().get(recPtr).getTranDet().getTranCode().equalsIgnoreCase("SAVE")||
				cdsTranAll.getTranDetWithClrntList().get(recPtr).getTranDet().getTranCode().equalsIgnoreCase("DISP")) &&
				cdsTranAll.getTranDetWithClrntList().get(recPtr).getTranDet().getSourceInd().equalsIgnoreCase("COMP")) {
				// COMP with SAVE or DISP
				if(cdsTranAll.getTranDetWithClrntList().get(recPtr).getTranDet().getFormSource().equalsIgnoreCase("ENG")||
				   cdsTranAll.getTranDetWithClrntList().get(recPtr).getTranDet().getFormSource().equalsIgnoreCase("FB") ||
				   cdsTranAll.getTranDetWithClrntList().get(recPtr).getTranDet().getFormSource().equalsIgnoreCase("HP") ||
				   cdsTranAll.getTranDetWithClrntList().get(recPtr).getTranDet().getFormSource().equalsIgnoreCase("FBVS") ||
				   cdsTranAll.getTranDetWithClrntList().get(recPtr).getTranDet().getFormSource().equalsIgnoreCase("VNL") ||
				   cdsTranAll.getTranDetWithClrntList().get(recPtr).getTranDet().getFormSource().equalsIgnoreCase("SCPCT") ||
				   cdsTranAll.getTranDetWithClrntList().get(recPtr).getTranDet().getFormSource().equalsIgnoreCase("PCT")){
					retVal.setColorType("COMPETITVE");
				}
			}
			if((cdsTranAll.getTranDetWithClrntList().get(recPtr).getTranDet().getTranCode().equalsIgnoreCase("SAVE")||
				cdsTranAll.getTranDetWithClrntList().get(recPtr).getTranDet().getTranCode().equalsIgnoreCase("DISP") ||
				cdsTranAll.getTranDetWithClrntList().get(recPtr).getTranDet().getTranCode().equalsIgnoreCase("MTCH")) &&
			   (cdsTranAll.getTranDetWithClrntList().get(recPtr).getTranDet().getSourceInd().equalsIgnoreCase("CUST") ||
				cdsTranAll.getTranDetWithClrntList().get(recPtr).getTranDet().getSourceInd().equalsIgnoreCase("CORR"))) {
					// Cust with SAVE or DISP or MTCH
					if(cdsTranAll.getTranDetWithClrntList().get(recPtr).getTranDet().getFormSource().equalsIgnoreCase("ENG")||
					   cdsTranAll.getTranDetWithClrntList().get(recPtr).getTranDet().getFormSource().equalsIgnoreCase("HP") ||
					   cdsTranAll.getTranDetWithClrntList().get(recPtr).getTranDet().getFormSource().equalsIgnoreCase("VNL") ||
					   cdsTranAll.getTranDetWithClrntList().get(recPtr).getTranDet().getFormSource().equalsIgnoreCase("SCPCT")){
						retVal.setColorType("CUSTOMMATCH");
					}
					if(cdsTranAll.getTranDetWithClrntList().get(recPtr).getTranDet().getFormSource().equalsIgnoreCase("MAN")||
					   cdsTranAll.getTranDetWithClrntList().get(recPtr).getTranDet().getFormSource().equalsIgnoreCase("MANV")){
						retVal.setColorType("CUSTOM");
					}
			}
			
			//Orig ColorType (calculated from firstDisp else first)
			recPtr = 0;
			if(firstDispPos>-1) recPtr = firstDispPos;
			if(cdsTranAll.getTranDetWithClrntList().get(recPtr).getTranDet().getSourceInd().equalsIgnoreCase("SW")) {
				// SW
				if(cdsTranAll.getTranDetWithClrntList().get(recPtr).getTranDet().getFormSource().equalsIgnoreCase("ENG")||
				   cdsTranAll.getTranDetWithClrntList().get(recPtr).getTranDet().getFormSource().equalsIgnoreCase("FB") ||
				   cdsTranAll.getTranDetWithClrntList().get(recPtr).getTranDet().getFormSource().equalsIgnoreCase("HP") ||
				   cdsTranAll.getTranDetWithClrntList().get(recPtr).getTranDet().getFormSource().equalsIgnoreCase("FBVS") ||
				   cdsTranAll.getTranDetWithClrntList().get(recPtr).getTranDet().getFormSource().equalsIgnoreCase("VNL") ||
				   cdsTranAll.getTranDetWithClrntList().get(recPtr).getTranDet().getFormSource().equalsIgnoreCase("SCPCT") ||
				   cdsTranAll.getTranDetWithClrntList().get(recPtr).getTranDet().getFormSource().equalsIgnoreCase("PCT")){
					retVal.setOrigColorType("SHERWIN-WILLIAMS");
				}
			}
			if(cdsTranAll.getTranDetWithClrntList().get(recPtr).getTranDet().getSourceInd().equalsIgnoreCase("COMP")) {
				// COMP
				if(cdsTranAll.getTranDetWithClrntList().get(recPtr).getTranDet().getFormSource().equalsIgnoreCase("ENG")||
				   cdsTranAll.getTranDetWithClrntList().get(recPtr).getTranDet().getFormSource().equalsIgnoreCase("FB") ||
				   cdsTranAll.getTranDetWithClrntList().get(recPtr).getTranDet().getFormSource().equalsIgnoreCase("HP") ||
				   cdsTranAll.getTranDetWithClrntList().get(recPtr).getTranDet().getFormSource().equalsIgnoreCase("FBVS") ||
				   cdsTranAll.getTranDetWithClrntList().get(recPtr).getTranDet().getFormSource().equalsIgnoreCase("VNL") ||
				   cdsTranAll.getTranDetWithClrntList().get(recPtr).getTranDet().getFormSource().equalsIgnoreCase("SCPCT") ||
				   cdsTranAll.getTranDetWithClrntList().get(recPtr).getTranDet().getFormSource().equalsIgnoreCase("PCT")){
					retVal.setOrigColorType("COMPETITVE");
				}
			}
			if(cdsTranAll.getTranDetWithClrntList().get(recPtr).getTranDet().getSourceInd().equalsIgnoreCase("CUST")) {
					// Cust
					if(cdsTranAll.getTranDetWithClrntList().get(recPtr).getTranDet().getFormSource().equalsIgnoreCase("ENG")||
					   cdsTranAll.getTranDetWithClrntList().get(recPtr).getTranDet().getFormSource().equalsIgnoreCase("HP") ||
					   cdsTranAll.getTranDetWithClrntList().get(recPtr).getTranDet().getFormSource().equalsIgnoreCase("VNL") ||
					   cdsTranAll.getTranDetWithClrntList().get(recPtr).getTranDet().getFormSource().equalsIgnoreCase("SCPCT")){
						retVal.setOrigColorType("CUSTOMMATCH");
					}
					if(cdsTranAll.getTranDetWithClrntList().get(recPtr).getTranDet().getFormSource().equalsIgnoreCase("MAN")||
					   cdsTranAll.getTranDetWithClrntList().get(recPtr).getTranDet().getFormSource().equalsIgnoreCase("MANV")){
						retVal.setOrigColorType("CUSTOM");
					}
			}
			
			//TranCode  CSW only has SAVE or DISP
			if(cdsTranAll.getTranDetWithClrntList().get(lastGood).getTranDet().getTranCode().equalsIgnoreCase("SAVE")){
				retVal.setTranCode("SAVE");
			} else {
				retVal.setTranCode("DISP");
			};
			
			// InitTranDate
			retVal.setInitTranDate(cdsTranAll.getTranDetWithClrntList().get(0).getTranDet().getTranDate());
			
			//LastTranDate (lastCorr else lastGood)
			recPtr = 0;
			if(lastCorr>-1) recPtr = lastCorr;
			else recPtr = lastGood;
			retVal.setLastTranDate(cdsTranAll.getTranDetWithClrntList().get(recPtr).getTranDet().getTranDate());
			
			//FormSource (laseCorr else lastGood)
			recPtr = 0;
			if(lastCorr>-1) recPtr = lastCorr;
			else recPtr = lastGood;
			retVal.setFormSource(cdsTranAll.getTranDetWithClrntList().get(recPtr).getTranDet().getFormSource());
			
			//OrigFormSourcepe (calculated from firstDisp else first)
			recPtr = 0;
			if(firstDispPos>-1) recPtr = firstDispPos;
			retVal.setOrigFormSource(cdsTranAll.getTranDetWithClrntList().get(recPtr).getTranDet().getFormSource());
			
			//FormMethod (calc from lastGood)
			recPtr = 0;
			if(lastCorr>-1) recPtr = lastCorr;
			else recPtr = lastGood;
			switch(retVal.getFormSource()){
			case "ENG":
				if(retVal.getColorType().equalsIgnoreCase("SHERWIN-WILLIAMS")) retVal.setFormMethod("SHER-COLOR FORMULA");
				else retVal.setFormMethod("CUSTOM SHER-COLOR MATCH");
				break;
			case "FB":
				if(retVal.getColorType().equalsIgnoreCase("SHERWIN-WILLIAMS")) retVal.setFormMethod("FORMULA BOOK");
				else retVal.setFormMethod("CUSTOM MANUAL MATCH");
				break;
			case "FBV":
				if(retVal.getColorType().equalsIgnoreCase("SHERWIN-WILLIAMS")) retVal.setFormMethod("FORMULA BOOK");
				else retVal.setFormMethod("CUSTOM MANUAL MATCH");
				break;
			case "MAN":
				retVal.setFormMethod("MANUAL FORMULA");
				break;
			case "MANV":
				retVal.setFormMethod("CUSTOM MANUAL VINYL SAFE MATCH");
				break;
			case "PCT":
				int pct = cdsTranAll.getTranDetWithClrntList().get(recPtr).getTranDet().getFormPct();
				if(retVal.getColorType().equalsIgnoreCase("SHERWIN-WILLIAMS") && pct>0) retVal.setFormMethod(pct + "% OF FORMULA BOOK");
				else retVal.setFormMethod("CUSTOM MANUAL MATCH");
				break;
			case "SCPCT":
				int scpct = cdsTranAll.getTranDetWithClrntList().get(recPtr).getTranDet().getFormPct();
				if(retVal.getColorType().equalsIgnoreCase("SHERWIN-WILLIAMS") && scpct>0) retVal.setFormMethod(scpct + "% FORMULA");
				else retVal.setFormMethod("CUSTOM MANUAL MATCH");
				break;
			case "VNL":
				if(retVal.getColorType().equalsIgnoreCase("SHERWIN-WILLIAMS")) retVal.setFormMethod("VINYL SAFE FORMULA");
				else retVal.setFormMethod("CUSTOM MANUAL MATCH");
				break;
			}
			
			//OrigFormMethod(calculated from firstDisp else first record)
			recPtr = 0;
			if(firstDispPos>-1) recPtr = firstDispPos;
			switch(retVal.getOrigFormSource()){
			case "ENG":
				if(retVal.getOrigColorType().equalsIgnoreCase("SHERWIN-WILLIAMS")) retVal.setOrigFormMethod("SHER-COLOR FORMULA");
				else retVal.setOrigFormMethod("CUSTOM SHER-COLOR MATCH");
				break;
			case "FB":
				if(retVal.getOrigColorType().equalsIgnoreCase("SHERWIN-WILLIAMS")) retVal.setOrigFormMethod("FORMULA BOOK");
				else retVal.setOrigFormMethod("CUSTOM MANUAL MATCH");
				break;
			case "FBV":
				if(retVal.getOrigColorType().equalsIgnoreCase("SHERWIN-WILLIAMS")) retVal.setOrigFormMethod("FORMULA BOOK");
				else retVal.setOrigFormMethod("CUSTOM MANUAL MATCH");
				break;
			case "MAN":
				retVal.setOrigFormMethod("MANUAL FORMULA");
				break;
			case "MANV":
				retVal.setOrigFormMethod("CUSTOM MANUAL VINYL SAFE MATCH");
				break;
			case "PCT":
				int pct = cdsTranAll.getTranDetWithClrntList().get(recPtr).getTranDet().getFormPct();
				if(retVal.getOrigColorType().equalsIgnoreCase("SHERWIN-WILLIAMS") && pct>0) retVal.setOrigFormMethod(pct + "% OF FORMULA BOOK");
				else retVal.setOrigFormMethod("CUSTOM MANUAL MATCH");
				break;
			case "SCPCT":
				int scpct = cdsTranAll.getTranDetWithClrntList().get(recPtr).getTranDet().getFormPct();
				if(retVal.getOrigColorType().equalsIgnoreCase("SHERWIN-WILLIAMS") && scpct>0) retVal.setOrigFormMethod(scpct + "% FORMULA");
				else retVal.setOrigFormMethod("CUSTOM MANUAL MATCH");
				break;
			case "VNL":
				if(retVal.getOrigColorType().equalsIgnoreCase("SHERWIN-WILLIAMS")) retVal.setOrigFormMethod("VINYL SAFE FORMULA");
				else retVal.setOrigFormMethod("CUSTOM MANUAL MATCH");
				break;
			}
			

			//Clrnt1 - Clrnt8
			//ClrntAmt1 - ClrntAmt8
			// Get initial clrnt list and unit nbr
			recPtr = 0;
			if(firstDispPos>-1) recPtr = firstDispPos;
			List<CdsTranClrnt> totalClrntList = new ArrayList<CdsTranClrnt>();
			for(CdsTranClrnt addme : cdsTranAll.getTranDetWithClrntList().get(recPtr).getTranClrntList()){
				CdsTranClrnt newClrnt = new CdsTranClrnt();
				newClrnt.setClrntCode(addme.getClrntCode());
				newClrnt.setClrntAmt(addme.getClrntAmt());
				totalClrntList.add(newClrnt);
			}
			int unitNbr = cdsTranAll.getTranDetWithClrntList().get(recPtr).getTranDet().getTranMastUnit();
			//walk through tran det, get all DISP CORR for this unitNbr and add to totalClrntList
			for(int i=recPtr+1; i<cdsTranAll.getTranDetWithClrntList().size();i++){
				if(cdsTranAll.getTranDetWithClrntList().get(i).getTranDet().getTranMastUnit()==unitNbr &&
					cdsTranAll.getTranDetWithClrntList().get(i).getTranDet().getTranCode().equalsIgnoreCase("DISP") &&
					cdsTranAll.getTranDetWithClrntList().get(i).getTranDet().getSourceInd().equalsIgnoreCase("CORR")){
					// got one, add to totalClrntList
					for(CdsTranClrnt addTranClrnt : cdsTranAll.getTranDetWithClrntList().get(i).getTranClrntList()){
						boolean found = false;
						for(int j=0;j<totalClrntList.size();j++){
							if(totalClrntList.get(j).getClrntCode().equalsIgnoreCase(addTranClrnt.getClrntCode())){
								// matching colorant code add amount to existing amount
								found=true;
								totalClrntList.get(j).setClrntAmt(totalClrntList.get(j).getClrntAmt()+addTranClrnt.getClrntAmt());
							}
						} // end for j
						if(!found) totalClrntList.add(addTranClrnt); // not found add to end
					}
				}
			}
			// now totalClrntList just needs to be mapped to retVal colorant fields
			for(int i=0; i<totalClrntList.size();i++){
				switch(i){
				case 0:
					retVal.setClrnt1(totalClrntList.get(i).getClrntCode());
					retVal.setClrntAmt1(totalClrntList.get(i).getClrntAmt());
					break;
				case 1:
					retVal.setClrnt2(totalClrntList.get(i).getClrntCode());
					retVal.setClrntAmt2(totalClrntList.get(i).getClrntAmt());
					break;
				case 2:
					retVal.setClrnt3(totalClrntList.get(i).getClrntCode());
					retVal.setClrntAmt3(totalClrntList.get(i).getClrntAmt());
					break;
				case 3:
					retVal.setClrnt4(totalClrntList.get(i).getClrntCode());
					retVal.setClrntAmt4(totalClrntList.get(i).getClrntAmt());
					break;
				case 4:
					retVal.setClrnt5(totalClrntList.get(i).getClrntCode());
					retVal.setClrntAmt5(totalClrntList.get(i).getClrntAmt());
					break;
				case 5:
					retVal.setClrnt6(totalClrntList.get(i).getClrntCode());
					retVal.setClrntAmt6(totalClrntList.get(i).getClrntAmt());
					break;
				case 6:
					retVal.setClrnt7(totalClrntList.get(i).getClrntCode());
					retVal.setClrntAmt7(totalClrntList.get(i).getClrntAmt());
					break;
				case 7:
					retVal.setClrnt8(totalClrntList.get(i).getClrntCode());
					retVal.setClrntAmt8(totalClrntList.get(i).getClrntAmt());
					break;
				default:
					System.out.println("too many CdsTranClrnt records in list ("+i+") for controlNbr "+cdsTranAll.getTranMast().getControlNbr());
					break;
				}
			}
			
			//Orig Clrnts and ClrntAmts (firstDisp else firstRecord)
			recPtr = 0;
			if(firstDispPos>-1) recPtr = firstDispPos;
			for(int i=0; i<cdsTranAll.getTranDetWithClrntList().get(recPtr).getTranClrntList().size();i++){
				switch(i){
				case 0:
					retVal.setOrigClrnt1(cdsTranAll.getTranDetWithClrntList().get(recPtr).getTranClrntList().get(i).getClrntCode());
					retVal.setOrigClrntAmt1(cdsTranAll.getTranDetWithClrntList().get(recPtr).getTranClrntList().get(i).getClrntAmt());
					break;
				case 1:
					retVal.setOrigClrnt2(cdsTranAll.getTranDetWithClrntList().get(recPtr).getTranClrntList().get(i).getClrntCode());
					retVal.setOrigClrntAmt2(cdsTranAll.getTranDetWithClrntList().get(recPtr).getTranClrntList().get(i).getClrntAmt());
					break;
				case 2:
					retVal.setOrigClrnt3(cdsTranAll.getTranDetWithClrntList().get(recPtr).getTranClrntList().get(i).getClrntCode());
					retVal.setOrigClrntAmt3(cdsTranAll.getTranDetWithClrntList().get(recPtr).getTranClrntList().get(i).getClrntAmt());
					break;
				case 3:
					retVal.setOrigClrnt4(cdsTranAll.getTranDetWithClrntList().get(recPtr).getTranClrntList().get(i).getClrntCode());
					retVal.setOrigClrntAmt4(cdsTranAll.getTranDetWithClrntList().get(recPtr).getTranClrntList().get(i).getClrntAmt());
					break;
				case 4:
					retVal.setOrigClrnt5(cdsTranAll.getTranDetWithClrntList().get(recPtr).getTranClrntList().get(i).getClrntCode());
					retVal.setOrigClrntAmt5(cdsTranAll.getTranDetWithClrntList().get(recPtr).getTranClrntList().get(i).getClrntAmt());
					break;
				case 5:
					retVal.setOrigClrnt6(cdsTranAll.getTranDetWithClrntList().get(recPtr).getTranClrntList().get(i).getClrntCode());
					retVal.setOrigClrntAmt6(cdsTranAll.getTranDetWithClrntList().get(recPtr).getTranClrntList().get(i).getClrntAmt());
					break;
				case 6:
					retVal.setOrigClrnt7(cdsTranAll.getTranDetWithClrntList().get(recPtr).getTranClrntList().get(i).getClrntCode());
					retVal.setOrigClrntAmt7(cdsTranAll.getTranDetWithClrntList().get(recPtr).getTranClrntList().get(i).getClrntAmt());
					break;
				case 7:
					retVal.setOrigClrnt8(cdsTranAll.getTranDetWithClrntList().get(recPtr).getTranClrntList().get(i).getClrntCode());
					retVal.setOrigClrntAmt8(cdsTranAll.getTranDetWithClrntList().get(recPtr).getTranClrntList().get(i).getClrntAmt());
					break;
				default:
					System.out.println("too many CdsTranClrnt records in list ("+i+") for controlNbr "+cdsTranAll.getTranMast().getControlNbr());
					break;
				}
			}
			
			//ShotSize (firstDisp else firstRec)
			recPtr = 0;
			if(firstDispPos>-1) recPtr = firstDispPos;
			retVal.setShotSize(Integer.parseInt(cdsTranAll.getTranDetWithClrntList().get(recPtr).getTranClrntList().get(0).getClrntQual()));
			
			//FormPct (firstDisp else firstRec)
			recPtr = 0;
			if(firstDispPos>-1) recPtr = firstDispPos;
			retVal.setFormPct(cdsTranAll.getTranDetWithClrntList().get(recPtr).getTranDet().getFormPct());
			
			//VinylSafe (based on calculated FormSource)
			if(retVal.getFormSource().equalsIgnoreCase("VNL") || retVal.getFormSource().equalsIgnoreCase("MANV")){
				retVal.setVinylSafe(true);
			} else {
				retVal.setVinylSafe(false);
			}

			//PrimerId (lookup colorMast from ColorComp + ColorId)
			CdsColorMast cdsColorMast = colorMastService.read(retVal.getColorComp(), retVal.getColorId());
			if (cdsColorMast!=null){
				retVal.setPrimerId(cdsColorMast.getPrimerId());
			}
			
			//TODO RgbHex (calc from curve using shercolorcommon)
			
			//DE Primary/Secondary/Tertiary/Average (lastCorr else lastGood)
			recPtr = 0;
			if(lastCorr>-1) recPtr = lastCorr;
			else recPtr = lastGood;
			retVal.setDeltaEPrimary(cdsTranAll.getTranDetWithClrntList().get(recPtr).getTranDet().getDeltaePrimary());
			retVal.setDeltaESecondary(cdsTranAll.getTranDetWithClrntList().get(recPtr).getTranDet().getDeltaeSecondary());
			retVal.setDeltaETertiary(cdsTranAll.getTranDetWithClrntList().get(recPtr).getTranDet().getDeltaeTertiary());
			retVal.setAverageDeltaE((retVal.getDeltaEPrimary()+retVal.getDeltaESecondary()+retVal.getDeltaETertiary())/3.0D);
			
			//CR Thick/Thin (lastCorr else lastGood)
			//EngineDecisionValue (lastCorr else lastGood)
			//FormulaRule (lastCorr else lastGood)
			//Illum Primary/Secondary/Tertiary (lastCorr else lastGood)
			//ColorEngVer (lastCorr else lastGood)
			//Spd (lastCorr else lastGood)
			//MetamerismIndex (lastCorr else lastGood)
			//FormulationTime (lastCorr else lastGood)
			recPtr = 0;
			if(lastCorr>-1) recPtr = lastCorr;
			else recPtr = lastGood;
			retVal.setCrThick(cdsTranAll.getTranDetWithClrntList().get(recPtr).getTranDet().getCrFilm1());
			retVal.setCrThin(cdsTranAll.getTranDetWithClrntList().get(recPtr).getTranDet().getCrFilm2());
			retVal.setEngDecisionValue(cdsTranAll.getTranDetWithClrntList().get(recPtr).getTranDet().getCost());
			retVal.setFormulaRule(cdsTranAll.getTranDetWithClrntList().get(recPtr).getTranDet().getRule());
			retVal.setIllumPrimary(cdsTranAll.getTranDetWithClrntList().get(recPtr).getTranDet().getIllumPrimary());
			retVal.setIllumSecondary(cdsTranAll.getTranDetWithClrntList().get(recPtr).getTranDet().getIllumSecondary());
			retVal.setIllumTertiary(cdsTranAll.getTranDetWithClrntList().get(recPtr).getTranDet().getIllumTertiary());
			retVal.setColorEngVer(cdsTranAll.getTranDetWithClrntList().get(recPtr).getTranDet().getColorEngVer());
			retVal.setSpd(cdsTranAll.getTranDetWithClrntList().get(recPtr).getTranDet().getSpecDev());
			retVal.setMetamerismIndex(cdsTranAll.getTranDetWithClrntList().get(recPtr).getTranDet().getMetamerismIdx());
			retVal.setFormulationTime(0D); //Not Available in SherColorPOS
			
			//ProjectedCurve and MeasuredCurve (lastCorr else lastGood)
			recPtr = 0;
			if(lastCorr>-1) recPtr = lastCorr;
			else recPtr = lastGood;
			retVal.setProjCurve(cdsTranAll.getTranDetWithClrntList().get(recPtr).getTranDet().getProjectCurve());
			retVal.setMeasCurve(cdsTranAll.getTranDetWithClrntList().get(recPtr).getTranDet().getMeasureCurve());
			
			// JobFields (tranMast)
			retVal.setJobField01(cdsTranAll.getTranMast().getcFld1());
			retVal.setJobField02(cdsTranAll.getTranMast().getcFld2());
			retVal.setJobField03(cdsTranAll.getTranMast().getcFld3());
			retVal.setJobField04(cdsTranAll.getTranMast().getcFld4());
			retVal.setJobField05(cdsTranAll.getTranMast().getcFld5());
			retVal.setJobField06(cdsTranAll.getTranMast().getcFld6());
			retVal.setJobField07(cdsTranAll.getTranMast().getcFld7());
			retVal.setJobField08(cdsTranAll.getTranMast().getcFld8());
			retVal.setJobField09(cdsTranAll.getTranMast().getcFld9());
			retVal.setJobField10(cdsTranAll.getTranMast().getcFld10());
			
			//QuantityDispensed
			int qty=0;
			for (int i =0; i < cdsTranAll.getTranDetWithClrntList().size(); i++) {
				if(cdsTranAll.getTranDetWithClrntList().get(i).getTranDet().getTranCode().equalsIgnoreCase("DISP")){
					if(!cdsTranAll.getTranDetWithClrntList().get(i).getTranDet().getSourceInd().equalsIgnoreCase("CORR")) qty++;
				}
			}
			retVal.setQuantityDispensed(qty);
		} catch (Exception e) {
			System.out.println("ConvertToWeb threw an exception ");
			e.printStackTrace();
			retVal = null;
		}
			
		return retVal;
	}
}
