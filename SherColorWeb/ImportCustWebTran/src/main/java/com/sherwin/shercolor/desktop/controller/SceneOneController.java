package com.sherwin.shercolor.desktop.controller;

import java.net.URL;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.sherwin.shercolor.common.domain.CustWebTran;
import com.sherwin.shercolor.common.domain.PosProd;
import com.sherwin.shercolor.common.service.ProductService;
import com.sherwin.shercolor.desktop.domain.CdsTranAll;
import com.sherwin.shercolor.desktop.domain.CdsTranClrnt;
import com.sherwin.shercolor.desktop.domain.CdsTranDet;
import com.sherwin.shercolor.desktop.domain.CdsTranDetWithClrnt;
import com.sherwin.shercolor.desktop.domain.CdsTranMast;
import com.sherwin.shercolor.desktop.service.CdsTranService;
import com.sherwin.shercolor.desktop.service.CdsTranServiceImpl;

import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.Spinner;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;

@Controller
public class SceneOneController implements Initializable {
	
	@Autowired
	CdsTranService cdsTranService;
	
	@Autowired
	ProductService productService;
	
	List<CdsTranAll> cdsTranAllList;
	int CurrentPosition=-1;
	
	private ObservableList<CdsTranMast> displayTranMast = FXCollections.observableArrayList();
	private ObservableList<CdsTranDet> displayTranDet = FXCollections.observableArrayList();
	private ObservableList<CdsTranClrnt> displayTranClrnt = FXCollections.observableArrayList();
	private ObservableList<CustWebTran> displayCustWebTran = FXCollections.observableArrayList();

	@FXML
	public TextField inputFileDir;
	
	@FXML
	public Spinner<Integer> controlNbr;
	
	@FXML
	public Button uxLoad;
	
	@FXML
	public Button uxLookup;
	
	@FXML
	public ProgressBar loadProgress;
	
	@FXML
	public TableView<CdsTranMast> tranMastTable;
	
	@FXML
	private TableColumn<CdsTranMast, Integer> colControlNbr;
	
	@FXML
	private TableColumn<CdsTranMast, Integer> colLineNbr;
	
	@FXML
	private TableColumn<CdsTranMast, String> colColorComp;
	
	@FXML
	private TableColumn<CdsTranMast, String> colColorId;
	
	@FXML
	private TableColumn<CdsTranMast, String> colColorName;
	
	@FXML
	private TableColumn<CdsTranMast, String> colActBase;
	
	@FXML
	private TableColumn<CdsTranMast, String> colcFld1;
	
	@FXML
	private TableColumn<CdsTranMast, String> colcFld2;
	
	@FXML
	private TableColumn<CdsTranMast, String> colcFld3;
	
	@FXML
	private TableColumn<CdsTranMast, String> colcFld4;
	
	@FXML
	private TableColumn<CdsTranMast, String> colcFld5;
	
	@FXML	public TableView<CdsTranDet> tranDetTable;
	@FXML	private TableColumn<CdsTranDet, Integer> colTranDetSeq;
	@FXML	private TableColumn<CdsTranDet, Integer> colTranMastUnit;
	@FXML	private TableColumn<CdsTranDet, Date> colTranDate;
	@FXML	private TableColumn<CdsTranDet, String> colTranTime;
	@FXML	private TableColumn<CdsTranDet, String> colTranCode;
	@FXML	private TableColumn<CdsTranDet, String> colSourceInd;
	@FXML	private TableColumn<CdsTranDet, String> colFormSource;
	@FXML	private TableColumn<CdsTranDet, String> colDestId;
	@FXML	private TableColumn<CdsTranDet, Double> colDeltaePrimary;
	@FXML	private TableColumn<CdsTranDet, Double> colDeltaeSecondary;
	@FXML	private TableColumn<CdsTranDet, Double> colDeltaeTertiary;

	@FXML	public TableView<CdsTranClrnt> tranClrntTable;
	@FXML	private TableColumn <CdsTranClrnt, Integer> colTranClrntSeq;
	@FXML	private TableColumn <CdsTranClrnt, String> colClrntSys;
	@FXML	private TableColumn <CdsTranClrnt, String> colClrntCode;
	@FXML	private TableColumn <CdsTranClrnt, Integer> colClrntAmt;

	@FXML	public TableView<CustWebTran> custWebTranTable;
	@FXML	private TableColumn<CustWebTran, Integer> colCwtControlNbr;
	@FXML	private TableColumn<CustWebTran, Integer> colCwtLineNbr;
	@FXML	private TableColumn<CustWebTran, String> colCwtColorComp;
	@FXML	private TableColumn<CustWebTran, String> colCwtColorId;
	@FXML	private TableColumn<CustWebTran, String> colCwtColorName;
	@FXML	private TableColumn<CustWebTran, String> colCwtSalesNbr;
	@FXML	private TableColumn<CustWebTran, String> colCwtProdNbr;
	@FXML	private TableColumn<CustWebTran, String> colCwtSizeCode;
	@FXML	private TableColumn<CustWebTran, Integer> colCwtQty;
	@FXML	private TableColumn<CustWebTran, String> colCwtColorType;
	@FXML	private TableColumn<CustWebTran, String> colCwtTranCode;
	@FXML	private TableColumn<CustWebTran, String> colCwtFormSource;
	@FXML	private TableColumn<CustWebTran, String> colCwtFormMethod;
	@FXML	private TableColumn<CustWebTran, String> colCwtOrigColorType;
	@FXML	private TableColumn<CustWebTran, String> colCwtOrigFormSource;
	@FXML	private TableColumn<CustWebTran, String> colCwtOrigFormMethod;
	@FXML	private TableColumn<CustWebTran, Date> colCwtInitTranDate;
	@FXML	private TableColumn<CustWebTran, Date> colCwtLastTranDate;
	@FXML	private TableColumn<CustWebTran, Integer> colCwtFormPct;
	@FXML	private TableColumn<CustWebTran, String> colCwtPrimerId;
	@FXML   private TableColumn<CustWebTran, String> colCwtUserId;
	@FXML	private TableColumn<CustWebTran, String> colCwtFld1;
	@FXML	private TableColumn<CustWebTran, String> colCwtFld2;
	@FXML	private TableColumn<CustWebTran, String> colCwtFld3;
	@FXML	private TableColumn<CustWebTran, String> colCwtFld4;
	@FXML	private TableColumn<CustWebTran, String> colCwtFld5;
	@FXML	private TableColumn<CustWebTran, String> colCwtFld6;
	@FXML	private TableColumn<CustWebTran, String> colCwtFld7;
	@FXML	private TableColumn<CustWebTran, String> colCwtFld8;
	@FXML	private TableColumn<CustWebTran, String> colCwtFld9;
	@FXML	private TableColumn<CustWebTran, String> colCwtFld10;
	@FXML	private TableColumn<CustWebTran, Double> colCwtDe1;
	@FXML	private TableColumn<CustWebTran, Double> colCwtDe2;
	@FXML	private TableColumn<CustWebTran, Double> colCwtDe3;
	@FXML	private TableColumn<CustWebTran, Double> colCwtDeAvg;
	@FXML	private TableColumn<CustWebTran, String> colCwtClrntSysId;
	@FXML	private TableColumn<CustWebTran, Integer> colCwtShotSize;
	@FXML	private TableColumn<CustWebTran, String> colCwtC1;
	@FXML	private TableColumn<CustWebTran, Integer> colCwtA1;
	@FXML	private TableColumn<CustWebTran, String> colCwtC2;
	@FXML	private TableColumn<CustWebTran, Integer> colCwtA2;
	@FXML	private TableColumn<CustWebTran, String> colCwtC3;
	@FXML	private TableColumn<CustWebTran, Integer> colCwtA3;
	@FXML	private TableColumn<CustWebTran, String> colCwtC4;
	@FXML	private TableColumn<CustWebTran, Integer> colCwtA4;
	@FXML	private TableColumn<CustWebTran, String> colCwtC5;
	@FXML	private TableColumn<CustWebTran, Integer> colCwtA5;
	@FXML	private TableColumn<CustWebTran, String> colCwtC6;
	@FXML	private TableColumn<CustWebTran, Integer> colCwtA6;
	@FXML	private TableColumn<CustWebTran, String> colCwtC7;
	@FXML	private TableColumn<CustWebTran, Integer> colCwtA7;
	@FXML	private TableColumn<CustWebTran, String> colCwtC8;
	@FXML	private TableColumn<CustWebTran, Integer> colCwtA8;
	@FXML	private TableColumn<CustWebTran, String> colCwtVS;
	@FXML	private TableColumn<CustWebTran, Double> colCwtCrThick;
	@FXML	private TableColumn<CustWebTran, Double> colCwtCrThin;
	@FXML	private TableColumn<CustWebTran, Double> colCwtCost;
	@FXML	private TableColumn<CustWebTran, String> colCwtRule;
	@FXML	private TableColumn<CustWebTran, Double> colCwtIllum1;
	@FXML	private TableColumn<CustWebTran, Double> colCwtIllum2;
	@FXML	private TableColumn<CustWebTran, Double> colCwtIllum3;
	@FXML	private TableColumn<CustWebTran, Double> colCwtEngVer;
	@FXML	private TableColumn<CustWebTran, Double> colCwtSpd;
	@FXML	private TableColumn<CustWebTran, Double> colCwtMi;
	@FXML	private TableColumn<CustWebTran, String> colCwtOrigColorComp;
	@FXML	private TableColumn<CustWebTran, String> colCwtOrigColorId;
	@FXML	private TableColumn<CustWebTran, String> colOrigC1;
	@FXML	private TableColumn<CustWebTran, String> colOrigA1;
	@FXML	private TableColumn<CustWebTran, String> colOrigC2;
	@FXML	private TableColumn<CustWebTran, String> colOrigA2;
	@FXML	private TableColumn<CustWebTran, String> colOrigC3;
	@FXML	private TableColumn<CustWebTran, String> colOrigA3;
	@FXML	private TableColumn<CustWebTran, String> colOrigC4;
	@FXML	private TableColumn<CustWebTran, String> colOrigA4;
	@FXML	private TableColumn<CustWebTran, String> colOrigC5;
	@FXML	private TableColumn<CustWebTran, String> colOrigA5;
	@FXML	private TableColumn<CustWebTran, String> colCwtRgbHex;
	@FXML	private TableColumn<CustWebTran, String> colCwtProjCurve;
	@FXML	private TableColumn<CustWebTran, String> colCwtMeasCurve;
	
	public void initialize(URL location, ResourceBundle resources) {
		inputFileDir.setText("/Users/bms90r/Documents/CSW Installs/Pacific Rainbow");
//		uxLoad.setOnAction((event)-> {
//			
//		});
//		uxLoad.addEventHandler(MouseEvent.MOUSE_CLICKED, (MouseEvent event) -> {
//			@Override
//			public void handle(MouseEvent event) {
//				System.out.println("Handler: " + event.getEventType().getName());
//			}
//		});
		
		//uxLoad.setOnAction(this::handlLoadClick);
		loadProgress.setProgress(0);
		
		controlNbr.valueProperty().addListener((obs, oldValue, newValue) -> lookupTranMast(newValue));
		tranDetTable.getSelectionModel().selectedIndexProperty().addListener((obs, oldValue, newValue) -> lookupTranDet(newValue.intValue()));
		
		tranMastTable.setItems(displayTranMast);
		colControlNbr.setCellValueFactory(rowData -> new ReadOnlyObjectWrapper(rowData.getValue().getControlNbr()));
		colLineNbr.setCellValueFactory(rowData -> new ReadOnlyObjectWrapper(rowData.getValue().getLineNbr()));
		colColorComp.setCellValueFactory(rowData -> new ReadOnlyObjectWrapper(rowData.getValue().getColorComp()));
		colColorId.setCellValueFactory(rowData -> new ReadOnlyObjectWrapper(rowData.getValue().getColorId()));
		colColorName.setCellValueFactory(rowData -> new ReadOnlyObjectWrapper(rowData.getValue().getColorName()));
		colActBase.setCellValueFactory(rowData -> new ReadOnlyObjectWrapper(rowData.getValue().getActBase()));
		colcFld1.setCellValueFactory(rowData -> new ReadOnlyObjectWrapper(rowData.getValue().getcFld1()));
		colcFld2.setCellValueFactory(rowData -> new ReadOnlyObjectWrapper(rowData.getValue().getcFld2()));
		colcFld3.setCellValueFactory(rowData -> new ReadOnlyObjectWrapper(rowData.getValue().getcFld3()));
		colcFld4.setCellValueFactory(rowData -> new ReadOnlyObjectWrapper(rowData.getValue().getcFld4()));
		colcFld5.setCellValueFactory(rowData -> new ReadOnlyObjectWrapper(rowData.getValue().getcFld5()));
		
		tranDetTable.setItems(displayTranDet);
		colTranDetSeq.setCellValueFactory(rowData -> new ReadOnlyObjectWrapper(rowData.getValue().getTranDetSeq()));
		colTranMastUnit.setCellValueFactory(rowData -> new ReadOnlyObjectWrapper(rowData.getValue().getTranMastUnit()));
		colTranDate.setCellValueFactory(rowData -> new ReadOnlyObjectWrapper(rowData.getValue().getTranDate()));
		colTranTime.setCellValueFactory(rowData -> new ReadOnlyObjectWrapper(rowData.getValue().getTranTime()));
		colTranCode.setCellValueFactory(rowData -> new ReadOnlyObjectWrapper(rowData.getValue().getTranCode()));
		colSourceInd.setCellValueFactory(rowData -> new ReadOnlyObjectWrapper(rowData.getValue().getSourceInd()));
		colFormSource.setCellValueFactory(rowData -> new ReadOnlyObjectWrapper(rowData.getValue().getFormSource()));
		colDestId.setCellValueFactory(rowData -> new ReadOnlyObjectWrapper(rowData.getValue().getDestId()));
		colDeltaePrimary.setCellValueFactory(rowData -> new ReadOnlyObjectWrapper(rowData.getValue().getDeltaePrimary()));
		colDeltaeSecondary.setCellValueFactory(rowData -> new ReadOnlyObjectWrapper(rowData.getValue().getDeltaeSecondary()));
		colDeltaeTertiary.setCellValueFactory(rowData -> new ReadOnlyObjectWrapper(rowData.getValue().getDeltaeTertiary()));

		tranClrntTable.setItems(displayTranClrnt);
		colTranClrntSeq.setCellValueFactory(rowData -> new ReadOnlyObjectWrapper(rowData.getValue().getTranClrntSeq()));
		colClrntSys.setCellValueFactory(rowData -> new ReadOnlyObjectWrapper(rowData.getValue().getClrntSys()));
		colClrntCode.setCellValueFactory(rowData -> new ReadOnlyObjectWrapper(rowData.getValue().getClrntCode()));
		colClrntAmt.setCellValueFactory(rowData -> new ReadOnlyObjectWrapper(rowData.getValue().getClrntAmt()));

		custWebTranTable.setItems(displayCustWebTran);
		colCwtControlNbr.setCellValueFactory(rowData -> new ReadOnlyObjectWrapper(rowData.getValue().getControlNbr()));
		colCwtLineNbr.setCellValueFactory(rowData -> new ReadOnlyObjectWrapper(rowData.getValue().getLineNbr()));
		colCwtColorComp.setCellValueFactory(rowData -> new ReadOnlyObjectWrapper(rowData.getValue().getColorComp()));
		colCwtColorId.setCellValueFactory(rowData -> new ReadOnlyObjectWrapper(rowData.getValue().getColorId()));
		colCwtColorName.setCellValueFactory(rowData -> new ReadOnlyObjectWrapper(rowData.getValue().getColorName()));
		colCwtColorType.setCellValueFactory(rowData -> new ReadOnlyObjectWrapper(rowData.getValue().getColorType()));
		colCwtOrigColorType.setCellValueFactory(rowData -> new ReadOnlyObjectWrapper(rowData.getValue().getOrigColorType()));
		colCwtSalesNbr.setCellValueFactory(rowData -> new ReadOnlyObjectWrapper(rowData.getValue().getSalesNbr()));
		colCwtProdNbr.setCellValueFactory(rowData -> new ReadOnlyObjectWrapper(rowData.getValue().getProdNbr()));
		colCwtSizeCode.setCellValueFactory(rowData -> new ReadOnlyObjectWrapper(rowData.getValue().getSizeCode()));
		colCwtQty.setCellValueFactory(rowData -> new ReadOnlyObjectWrapper(rowData.getValue().getQuantityDispensed()));
		colCwtTranCode.setCellValueFactory(rowData -> new ReadOnlyObjectWrapper(rowData.getValue().getTranCode()));
		colCwtInitTranDate.setCellValueFactory(rowData -> new ReadOnlyObjectWrapper(rowData.getValue().getInitTranDate()));
		colCwtLastTranDate.setCellValueFactory(rowData -> new ReadOnlyObjectWrapper(rowData.getValue().getLastTranDate()));
		colCwtFormSource.setCellValueFactory(rowData -> new ReadOnlyObjectWrapper(rowData.getValue().getFormSource()));
		colCwtFormMethod.setCellValueFactory(rowData -> new ReadOnlyObjectWrapper(rowData.getValue().getFormMethod()));
		colCwtOrigFormSource.setCellValueFactory(rowData -> new ReadOnlyObjectWrapper(rowData.getValue().getOrigFormSource()));
		colCwtOrigFormMethod.setCellValueFactory(rowData -> new ReadOnlyObjectWrapper(rowData.getValue().getOrigFormMethod()));
		colCwtFormPct.setCellValueFactory(rowData -> new ReadOnlyObjectWrapper(rowData.getValue().getFormPct()));
		colCwtPrimerId.setCellValueFactory(rowData -> new ReadOnlyObjectWrapper(rowData.getValue().getPrimerId()));
		colCwtUserId.setCellValueFactory(rowData -> new ReadOnlyObjectWrapper(rowData.getValue().getUserId()));
		colCwtFld1.setCellValueFactory(rowData -> new ReadOnlyObjectWrapper(rowData.getValue().getJobField01()));
		colCwtFld2.setCellValueFactory(rowData -> new ReadOnlyObjectWrapper(rowData.getValue().getJobField02()));
		colCwtFld3.setCellValueFactory(rowData -> new ReadOnlyObjectWrapper(rowData.getValue().getJobField03()));
		colCwtFld4.setCellValueFactory(rowData -> new ReadOnlyObjectWrapper(rowData.getValue().getJobField04()));
		colCwtFld5.setCellValueFactory(rowData -> new ReadOnlyObjectWrapper(rowData.getValue().getJobField05()));
		colCwtFld6.setCellValueFactory(rowData -> new ReadOnlyObjectWrapper(rowData.getValue().getJobField06()));
		colCwtFld7.setCellValueFactory(rowData -> new ReadOnlyObjectWrapper(rowData.getValue().getJobField07()));
		colCwtFld8.setCellValueFactory(rowData -> new ReadOnlyObjectWrapper(rowData.getValue().getJobField08()));
		colCwtFld9.setCellValueFactory(rowData -> new ReadOnlyObjectWrapper(rowData.getValue().getJobField09()));
		colCwtFld10.setCellValueFactory(rowData -> new ReadOnlyObjectWrapper(rowData.getValue().getJobField10()));
		colCwtDe1.setCellValueFactory(rowData -> new ReadOnlyObjectWrapper(rowData.getValue().getDeltaEPrimary()));
		colCwtDe2.setCellValueFactory(rowData -> new ReadOnlyObjectWrapper(rowData.getValue().getDeltaESecondary()));
		colCwtDe3.setCellValueFactory(rowData -> new ReadOnlyObjectWrapper(rowData.getValue().getDeltaETertiary()));
		colCwtDeAvg.setCellValueFactory(rowData -> new ReadOnlyObjectWrapper(rowData.getValue().getAverageDeltaE()));
		colCwtClrntSysId.setCellValueFactory(rowData -> new ReadOnlyObjectWrapper(rowData.getValue().getClrntSysId()));
		colCwtShotSize.setCellValueFactory(rowData -> new ReadOnlyObjectWrapper(rowData.getValue().getShotSize()));
		colCwtC1.setCellValueFactory(rowData -> new ReadOnlyObjectWrapper(rowData.getValue().getClrnt1()));
		colCwtA1.setCellValueFactory(rowData -> new ReadOnlyObjectWrapper(rowData.getValue().getClrntAmt1()));
		colCwtC2.setCellValueFactory(rowData -> new ReadOnlyObjectWrapper(rowData.getValue().getClrnt2()));
		colCwtA2.setCellValueFactory(rowData -> new ReadOnlyObjectWrapper(rowData.getValue().getClrntAmt2()));
		colCwtC3.setCellValueFactory(rowData -> new ReadOnlyObjectWrapper(rowData.getValue().getClrnt3()));
		colCwtA3.setCellValueFactory(rowData -> new ReadOnlyObjectWrapper(rowData.getValue().getClrntAmt3()));
		colCwtC4.setCellValueFactory(rowData -> new ReadOnlyObjectWrapper(rowData.getValue().getClrnt4()));
		colCwtA4.setCellValueFactory(rowData -> new ReadOnlyObjectWrapper(rowData.getValue().getClrntAmt4()));
		colCwtC5.setCellValueFactory(rowData -> new ReadOnlyObjectWrapper(rowData.getValue().getClrnt5()));
		colCwtA5.setCellValueFactory(rowData -> new ReadOnlyObjectWrapper(rowData.getValue().getClrntAmt5()));
		colCwtC6.setCellValueFactory(rowData -> new ReadOnlyObjectWrapper(rowData.getValue().getClrnt6()));
		colCwtA6.setCellValueFactory(rowData -> new ReadOnlyObjectWrapper(rowData.getValue().getClrntAmt6()));
		colCwtC7.setCellValueFactory(rowData -> new ReadOnlyObjectWrapper(rowData.getValue().getClrnt7()));
		colCwtA7.setCellValueFactory(rowData -> new ReadOnlyObjectWrapper(rowData.getValue().getClrntAmt7()));
		colCwtC8.setCellValueFactory(rowData -> new ReadOnlyObjectWrapper(rowData.getValue().getClrnt8()));
		colCwtA8.setCellValueFactory(rowData -> new ReadOnlyObjectWrapper(rowData.getValue().getClrntAmt8()));
		colCwtVS.setCellValueFactory(rowData -> new ReadOnlyObjectWrapper(rowData.getValue().isVinylSafe()));
		colCwtCrThick.setCellValueFactory(rowData -> new ReadOnlyObjectWrapper(rowData.getValue().getCrThick()));
		colCwtCrThin.setCellValueFactory(rowData -> new ReadOnlyObjectWrapper(rowData.getValue().getCrThin()));
		colCwtCost.setCellValueFactory(rowData -> new ReadOnlyObjectWrapper(rowData.getValue().getEngDecisionValue()));
		colCwtRule.setCellValueFactory(rowData -> new ReadOnlyObjectWrapper(rowData.getValue().getFormulaRule()));
		colCwtIllum1.setCellValueFactory(rowData -> new ReadOnlyObjectWrapper(rowData.getValue().getIllumPrimary()));
		colCwtIllum2.setCellValueFactory(rowData -> new ReadOnlyObjectWrapper(rowData.getValue().getIllumSecondary()));
		colCwtIllum3.setCellValueFactory(rowData -> new ReadOnlyObjectWrapper(rowData.getValue().getIllumTertiary()));
		colCwtEngVer.setCellValueFactory(rowData -> new ReadOnlyObjectWrapper(rowData.getValue().getColorEngVer()));
		colCwtSpd.setCellValueFactory(rowData -> new ReadOnlyObjectWrapper(rowData.getValue().getSpd()));
		colCwtMi.setCellValueFactory(rowData -> new ReadOnlyObjectWrapper(rowData.getValue().getMetamerismIndex()));
		colCwtOrigColorComp.setCellValueFactory(rowData -> new ReadOnlyObjectWrapper(rowData.getValue().getOrigColorComp()));
		colCwtOrigColorId.setCellValueFactory(rowData -> new ReadOnlyObjectWrapper(rowData.getValue().getOrigColorId()));
		colOrigC1.setCellValueFactory(rowData -> new ReadOnlyObjectWrapper(rowData.getValue().getOrigClrnt1()));
		colOrigA1.setCellValueFactory(rowData -> new ReadOnlyObjectWrapper(rowData.getValue().getOrigClrntAmt1()));
		colOrigC2.setCellValueFactory(rowData -> new ReadOnlyObjectWrapper(rowData.getValue().getOrigClrnt2()));
		colOrigA2.setCellValueFactory(rowData -> new ReadOnlyObjectWrapper(rowData.getValue().getOrigClrntAmt2()));
		colOrigC3.setCellValueFactory(rowData -> new ReadOnlyObjectWrapper(rowData.getValue().getOrigClrnt3()));
		colOrigA3.setCellValueFactory(rowData -> new ReadOnlyObjectWrapper(rowData.getValue().getOrigClrntAmt3()));
		colOrigC4.setCellValueFactory(rowData -> new ReadOnlyObjectWrapper(rowData.getValue().getOrigClrnt4()));
		colOrigA4.setCellValueFactory(rowData -> new ReadOnlyObjectWrapper(rowData.getValue().getOrigClrntAmt4()));
		colOrigC5.setCellValueFactory(rowData -> new ReadOnlyObjectWrapper(rowData.getValue().getOrigClrnt5()));
		colOrigA5.setCellValueFactory(rowData -> new ReadOnlyObjectWrapper(rowData.getValue().getOrigClrntAmt5()));
		colCwtRgbHex.setCellValueFactory(rowData -> new ReadOnlyObjectWrapper(rowData.getValue().getRgbHex()));
		colCwtProjCurve.setCellValueFactory(rowData -> new ReadOnlyObjectWrapper(Arrays.toString(rowData.getValue().getProjCurve())));
		colCwtMeasCurve.setCellValueFactory(rowData -> new ReadOnlyObjectWrapper(Arrays.toString(rowData.getValue().getMeasCurve())));
	}
	
	@FXML
	private void handleLoadClick(ActionEvent event){
		System.out.println("load button clicked");
		try {
			
			Task task = new Task<Void>() {
			    @Override public Void call() {
					
			    	int totalLineCnt = cdsTranService.CountLinesInAllFiles(inputFileDir.getText());
					
			    	cdsTranService.setTotalLines(totalLineCnt);
					cdsTranService.setLinesComplete(0);
					cdsTranService.setProgressUpdate((workDone,totalWork) -> updateProgress(workDone,totalWork));
					List<CdsTranMast> cdsTranMastList = cdsTranService.ImportCdsTranMast(inputFileDir.getText());
					//updateProgress(cdsTranMastList.size(), totalLineCnt);

					cdsTranService.setLinesComplete(cdsTranMastList.size());
					List<CdsTranDet> cdsTranDetList = cdsTranService.ImportCdsTranDet(inputFileDir.getText());
					//updateProgress(cdsTranMastList.size() + cdsTranDetList.size(), totalLineCnt);

					cdsTranService.setLinesComplete(cdsTranMastList.size() + cdsTranDetList.size());
					List<CdsTranClrnt> cdsTranClrntList = cdsTranService.ImportCdsTranClrnt(inputFileDir.getText());
					//updateProgress(totalLineCnt, totalLineCnt);
					
					cdsTranAllList = cdsTranService.BuildAllTran(cdsTranMastList, cdsTranDetList, cdsTranClrntList);

			        return null;
			    }
			};
			//ProgressBar bar = new ProgressBar();
			loadProgress.progressProperty().bind(task.progressProperty());
			new Thread(task).start();

			
			System.out.println("Done Loading");
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}

	@FXML
	private void handleLookupClick(ActionEvent event){
		System.out.println("lookup button clicked");
		displayTranMast.clear();
		displayTranDet.clear();
		displayTranClrnt.clear();
		try {
			// did they enter a number?
			int targetControlNbr=0;
			try {
				targetControlNbr = controlNbr.getValue();
			} catch (Exception e){
				System.out.println("must enter a control number");
				//TODO show message box
			}
			if(targetControlNbr>0) {
				int foundPos = -1;
				int listSize = 0;
				if(cdsTranAllList==null || cdsTranAllList.size()==0){
					System.out.println("list is empty");
					//TODO show message box
				} else {
					listSize = cdsTranAllList.size();
					for(int pos=0; pos<listSize; pos++){
						if(cdsTranAllList.get(pos).getTranMast().getControlNbr()==targetControlNbr){
							foundPos = pos;
							break;
						}
					}
					if(foundPos>-1){
						CurrentPosition = foundPos;
						System.out.println(cdsTranAllList.get(foundPos).getTranMast().getControlNbr() + "," + cdsTranAllList.get(foundPos).getTranMast().getLineNbr() + "," + cdsTranAllList.get(foundPos).getTranMast().getColorComp() + "," + cdsTranAllList.get(foundPos).getTranMast().getColorId() + "," + cdsTranAllList.get(foundPos).getTranMast().getColorName() + "," + cdsTranAllList.get(foundPos).getTranMast().getPrefBase() + "<<end" + cdsTranAllList.get(foundPos).getTranDetWithClrntList().size());
						displayTranMast.add(cdsTranAllList.get(foundPos).getTranMast());
						for(CdsTranDetWithClrnt detWithClrnt : cdsTranAllList.get(foundPos).getTranDetWithClrntList()){
							displayTranDet.add(detWithClrnt.getTranDet());
							System.out.println("-->" + detWithClrnt.getTranDet().getControlNbr() + "," + detWithClrnt.getTranDet().getLineNbr() + "," + detWithClrnt.getTranDet().getTranDetSeq() + "," + detWithClrnt.getTranDet().getTranMastUnit() + "," + detWithClrnt.getTranDet().getTranCode() + "," + detWithClrnt.getTranDet().getTranDate() + "<<end" + detWithClrnt.getTranClrntList());
							displayTranClrnt.clear();
							for(CdsTranClrnt tranClrnt: detWithClrnt.getTranClrntList()){
								displayTranClrnt.add(tranClrnt);
								System.out.println("---->" + tranClrnt.getControlNbr() + "," + tranClrnt.getLineNbr() + "," + tranClrnt.getTranDetSeq() + "," + tranClrnt.getTranClrntSeq() + "," + tranClrnt.getClrntSys() + "," + tranClrnt.getClrntCode() + "," + tranClrnt.getClrntAmt() + "<<end");
							}
						}
						
					} // end test for found control number
				} // end test for list empty
				
			} // end if controlnbr > 0
			
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		
	} // end handleLookupClick

	@FXML
	private void handleTranslateClick(ActionEvent event){
		PosProd posProd = productService.readPosProd("650323256");
		System.out.println("prodnbr is" + posProd.getProdNbr());
		if(CurrentPosition>-1){
			CdsTranAll cdsTranAll = cdsTranAllList.get(CurrentPosition);
			CustWebTran custWebTran = cdsTranService.ConvertToCustWeb(cdsTranAll);
			displayCustWebTran.clear();
			displayCustWebTran.add(custWebTran);
		}
		
	} //end handleTranslateClick
	
	private void lookupTranMast(Integer controlNbr){
		displayTranMast.clear();
		displayTranDet.clear();
		displayTranClrnt.clear();
		if(controlNbr>0) {
			int foundPos = -1;
			int listSize = 0;
			if(cdsTranAllList==null || cdsTranAllList.size()==0){
				System.out.println("list is empty");
				//TODO show message box
			} else {
				listSize = cdsTranAllList.size();
				for(int pos=0; pos<listSize; pos++){
					if(cdsTranAllList.get(pos).getTranMast().getControlNbr()==controlNbr){
						foundPos = pos;
						break;
					}
				}
				if(foundPos>-1){
					CurrentPosition = foundPos;
					System.out.println(cdsTranAllList.get(foundPos).getTranMast().getControlNbr() + "," + cdsTranAllList.get(foundPos).getTranMast().getLineNbr() + "," + cdsTranAllList.get(foundPos).getTranMast().getColorComp() + "," + cdsTranAllList.get(foundPos).getTranMast().getColorId() + "," + cdsTranAllList.get(foundPos).getTranMast().getColorName() + "," + cdsTranAllList.get(foundPos).getTranMast().getPrefBase() + "<<end" + cdsTranAllList.get(foundPos).getTranDetWithClrntList().size());
					displayTranMast.add(cdsTranAllList.get(foundPos).getTranMast());
					for(CdsTranDetWithClrnt detWithClrnt : cdsTranAllList.get(foundPos).getTranDetWithClrntList()){
						displayTranDet.add(detWithClrnt.getTranDet());
						System.out.println("-->" + detWithClrnt.getTranDet().getControlNbr() + "," + detWithClrnt.getTranDet().getLineNbr() + "," + detWithClrnt.getTranDet().getTranDetSeq() + "," + detWithClrnt.getTranDet().getTranMastUnit() + "," + detWithClrnt.getTranDet().getTranCode() + "," + detWithClrnt.getTranDet().getTranDate() + "<<end" + detWithClrnt.getTranClrntList());
						displayTranClrnt.clear();
						for(CdsTranClrnt tranClrnt: detWithClrnt.getTranClrntList()){
							displayTranClrnt.add(tranClrnt);
							System.out.println("---->" + tranClrnt.getControlNbr() + "," + tranClrnt.getLineNbr() + "," + tranClrnt.getTranDetSeq() + "," + tranClrnt.getTranClrntSeq() + "," + tranClrnt.getClrntSys() + "," + tranClrnt.getClrntCode() + "," + tranClrnt.getClrntAmt() + "<<end");
						}
					}
					
				} // end test for found control number
			} // end test for list empty
			
		} // end if controlnbr > 0
		
	} // end lookupTranMast
	
	private void lookupTranDet(Integer tranDetPos){
		displayTranClrnt.clear();
		if(tranDetPos < cdsTranAllList.get(CurrentPosition).getTranDetWithClrntList().size()){
			for(CdsTranClrnt tranClrnt: cdsTranAllList.get(CurrentPosition).getTranDetWithClrntList().get(tranDetPos).getTranClrntList()){
				displayTranClrnt.add(tranClrnt);
				System.out.println("---->" + tranClrnt.getControlNbr() + "," + tranClrnt.getLineNbr() + "," + tranClrnt.getTranDetSeq() + "," + tranClrnt.getTranClrntSeq() + "," + tranClrnt.getClrntSys() + "," + tranClrnt.getClrntCode() + "," + tranClrnt.getClrntAmt() + "<<end");
			}
		}
	} //end lookupTranDet


}
