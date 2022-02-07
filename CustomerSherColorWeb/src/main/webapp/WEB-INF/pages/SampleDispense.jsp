<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<%@ taglib prefix="s" uri="/struts-tags"%>
<html>
<head>
<meta name="viewport" content="width=device-width, initial-scale=1">
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">

<title><s:text name="sampleDispense.sampleDispense"/></title>
	
<link rel="stylesheet" href="css/bootstrap.min.css" type="text/css">
<link rel="stylesheet" href="css/bootstrapxtra.css" type="text/css">
<link rel="stylesheet" href="js/smoothness/jquery-ui.min.css" type="text/css">
<link rel="stylesheet" href="css/CustomerSherColorWeb.css" type="text/css">
<link rel="stylesheet" href="font-awesome-4.7.0/css/font-awesome.min.css" type="text/css">
<script type="text/javascript" charset="utf-8" src="js/jquery-3.4.1.min.js"></script>
<script type="text/javascript" charset="utf-8" src="js/jquery-ui.min.js"></script>
<script type="text/javascript" charset="utf-8" src="js/bootstrap.min.js"></script>
<script type="text/javascript" charset="utf-8" src="js/moment.min.js"></script>
<script type="text/javascript" charset="utf-8" src="script/customershercolorweb-1.4.6.js"></script>
<script type="text/javascript" charset="utf-8" src="script/WSWrapper.js"></script>
<script type="text/javascript" charset="utf-8" src="script/tinter-1.4.8.js"></script>
<script type="text/javascript" charset="utf-8" src="script/dispense-1.4.7.js"></script>
<script type="text/javascript" charset="utf-8" src="script/printer-1.4.8.js"></script>
<s:set var="thisGuid" value="reqGuid" />

<style>
.sw-bg-main {
	background-color: ${sessionScope[thisGuid].rgbHex};
}
badge {
	font-size: 14px;
	margin: 5px;
}

.btn {
	margin-left: 3px;
	margin-right: 3px;
}
.chip {
  position: relative;
  display: -webkit-box;
  display: -ms-flexbox;
  display: flex;
  -webkit-box-orient: vertical;
  -webkit-box-direction: normal;
  -ms-flex-direction: column;
  flex-direction: column;
  min-width: 10px;
  min-height: 10px;
  height: 52px;
  width: 52px;
  border-radius: 50%;
  border: 1px solid rgba(0, 0, 0, 0.125);
}
</style>

<script type="text/javascript">
	// global vars 
	var printerConfig;
	var clrntAmtList = "";
	var canType = "${sessionScope[thisGuid].canType}";
	var shotList = [];
	var processingDispense = false;
	var baseDispense = null;
	var printJsonIN = "";
	var tinterModel = "${tinter.model}";

	$(function() {	
		// set up shotList; shots and decimalOunces start off zeroed out and update based on canType
		<s:iterator value="dispenseFormula">
			var color = new Colorant("<s:property value='clrntCode'/>", <s:property value="shots"/>,
						<s:property value="position"/>, <s:property value="uom"/>, 0.0);
			shotList.push(color);
		</s:iterator>
		
		<s:if test="%{baseDispense != null}">
			baseDispense = new Colorant("<s:property value='baseDispense.clrntCode'/>", <s:property value="baseDispense.shots"/>,
				<s:property value="baseDispense.position"/>, <s:property value="baseDispense.uom"/>, 0.0);
			console.log(baseDispense);
		</s:if>
		
		// tinter can base dispense, base is loaded in a canister, and job is profiled for base dispense
		if ("${tinterDoesBaseDispense}" == "true" && baseDispense != null && "${sessionScope[thisGuid].dispenseBase}" == "1"){
			// show checkbox
			$("#baseDispenseRow").removeClass("d-none");
			// add base to shotList
			shotList.push(baseDispense);
		}
		
		// set default sample fill from can type
		updateSampleFill();
		// convert shots to decimal ounces
		calculateDecimalOunces();
	});

	
	function updateSampleFill(){
		var factoryFill = "${factoryFill}";
		var sampleSize = "${sampleSize}"; 
		var sampleSizeFloat = parseFloat(sampleSize);
		
		if (!isNaN(sampleSizeFloat)){
			// calculate amount of base in the sample:   Sample Size / 128 * Factory Fill
			// don't need to do size conversion because factory fill is already scaled to a gallon
			var productSampleFill = sampleSizeFloat / 128 * factoryFill;
			// round to 5 digits without trailing zeroes
			$("#sampleFill").val(parseFloat(productSampleFill.toFixed(5)));
			
			// calculate shots in case user wants to dispense the base
			if (baseDispense != null){
				var baseShots = Math.round(productSampleFill * baseDispense.uom); 
				baseDispense.shots = baseShots;
				// only set decimal ounces field for alfa and corob tinters
				if (tinterModel != null && (tinterModel.includes("ALFA") || tinterModel.includes("COROB") || tinterModel.includes("SIMULATOR"))){
					baseDispense.decimalOunces = productSampleFill;
				}
			}

		} else {
			console.log("problem parsing the sample size");
		}
	}
	
	
	function calculateDecimalOunces(){
		clrntAmtList = "";
		var sampleSize = "${sampleSize}"; 
		
		var sampleSizeFloat = parseFloat(sampleSize);
		var sizeConversion = "${sizeConversion}";
		var dispenseFloor = "${dispenseFloor}";
		var dispenseFloorFloat = parseFloat(dispenseFloor);
		var sizeConvFloat = parseFloat(sizeConversion);
		
		// re-enable dispense and remove error text when user updates dropdown, then re-check colorant amounts
		$("#dispenseFloorErrorText").addClass('d-none');
		$("#dispenseSampleButton").prop('disabled', false);
		$('.decimalOuncesDisplay').css("color", "black");
		$('.colorantName').css("color", "black");
		
		if (!isNaN(sampleSizeFloat) && !isNaN(sizeConvFloat)){
			$(".formulaRow").each(function(){
				var colorantName = $(this).find('.colorantName').text();
				var numShots = $(this).find('.numShots').text();
				var shotSize = $(this).find('.shotSize').text();
				var shotsToOunces = Number(numShots) / Number(shotSize);
				if (!isNaN(shotsToOunces)){
					// calculate amount of each colorant:   Sample Size / 128 * ((Number of Shots / Shot Size) * Gallon Conversion) 
					var decimalOunces = sampleSizeFloat / 128 * (shotsToOunces * sizeConvFloat);
					
					// update shotlist
					var colorantName = $(this).find('.colorantName').text();
					shotList.forEach(function (item, index) {
						if (colorantName.includes(item.code)){
							// only set decimal ounces field for alfa and corob tinters
							if (tinterModel != null && (tinterModel.includes("ALFA") || tinterModel.includes("COROB")|| tinterModel.includes("SIMULATOR"))){
								item.decimalOunces = decimalOunces;
							}
							//console.log(colorantName + " unrounded: " + decimalOunces);
							
							// calculate number of shots based on uom, round to nearest int
							var updatedNumShots = Math.round(decimalOunces * item.uom);
							item.shots = updatedNumShots;
							//console.log("rounded for tinter: " + colorantName + ", " + updatedNumShots / item.uom); 
						 }
					});
					
					// warn user if colorant amount is lower than tinter dispense floor, make them choose a larger can type
					if (!isNaN(dispenseFloorFloat) && decimalOunces < dispenseFloorFloat){
						$("#dispenseFloorErrorText").removeClass('d-none');
						$("#dispenseSampleButton").prop('disabled', true);
						// highlight colorant name and amount in red that is too low
						$(this).find('.decimalOuncesDisplay').css("color", "red");
						$(this).find('.colorantName').css("color", "red");
					}
					
					// round colorant amount to 5 decimal places for screen display and labels
					decimalOunces = decimalOunces.toFixed(5);
					clrntAmtList += colorantName + "," + decimalOunces.toString() + ",";
					
					// calculated amount displayed to the user
					$(this).find('.decimalOuncesDisplay').text(decimalOunces);
					
				} else {
					console.log("problem calculating shots to ounces");
				}
			});
		} else {
			console.log("problem parsing the sample size or conversion size");
		}
		console.log("CLRNT AMT LIST: " + clrntAmtList);
		console.log(shotList);
	}
	
	
	function showPrintModal(){
		if(printerConfig && printerConfig.model){
			
			$("#printLabelPrint").show();
			$("#printLabelPrint").focus();
			
			
			$("#numLabels").val(printerConfig.numLabels);
			$("#numLabels").show();
		}
		else{
			$("#printLabelPrint").hide();
			$("#numLabels").hide();
		}
		
		$("#printLabelModal").modal('show');

		
	}
	
	function printButtonClickGetJson() {
		if (printerConfig && printerConfig.model) {
			var myguid = $("#reqGuid").val();
			var correctionStr = { "reqGuid" : myguid, "printLabelType" : printLabelType, "printOrientation" : printOrientation, "canType" : canType, "clrntAmtList" : clrntAmtList, "printCorrectionLabel" : false, "shotList" : shotList};
			printJsonIN = JSON.stringify(correctionStr);
			var myPdf = new pdf(myguid,printJsonIN);
			$("#printerInProgressMessage").text('<s:text name="displayFormula.printerInProgress"/>');
			var numLabels = null;

			numLabels = printerConfig.numLabels;
			numLabelsVal = $("#numLabels").val();
			if(numLabelsVal && numLabelsVal !=0){
				numLabels = numLabelsVal;
			}
			printLabel(myPdf, numLabels, printLabelType, printOrientation);
		}

	}
	
	function printButtonClick() {
		var myValue = $("#reqGuid").val();
		console.log("calling print window open for print action with guid "
				+ myValue);
		window.open('formulaUserPrintAction.action?reqGuid=' + myValue,
				'<s:text name="displayFormula.printLabel"/>', 'width=500, height=1000');
	}
	
	function ParsePrintMessage() {
		var parsed = false;
		try {
			if (ws_printer != null && ws_printer.wsmsg != null
					&& ws_printer.wserror == null) {
				var return_message = JSON.parse(ws_printer.wsmsg);
				if(return_message){
				switch (return_message.command) {
				case 'Print':
					parsed = true;
					ws_printer.wsmsg = null; //set to null so it can't be read twice
					if (return_message.errorNumber != 0) {
						// save a dispense (will bump the counter)
						$("#printerInProgressModal").modal('show');
						$("#printerInProgressMessage").text(
								'<s:text name="displayFormula.printResultColon"/>' + return_message.errorMessage);
						console.log(return_message);
						//waitForShowAndHide("#tinterInProgressModal");
					}
					
					break;
				case 'GetConfig':
					parsed = true;
					ws_printer.wsmsg = null; //set to null so it can't be read twice
					if (return_message.errorNumber != 0) {
						// save a dispense (will bump the counter)
						$("#printerResponseModal").modal('show');
						$("#printerResponseMessage").text(
								'<s:text name="displayFormula.getPrinterResult"/>'
										+ return_message.errorMessage);
						console.log(return_message);
					} else {
						printerConfig = return_message.printerConfig;

					}
					break;
				default:
					//Not an response we expected...
					console
							.log("Message from different command is junk, throw it out");
				}
				} // end switch statement
			} else {}

		} catch (error) {
			console.log("Caught error is = " + error + " If response is for tinter message, this error trying to parse printer message is expected.");
			console.log("Message is junk, throw it out");
			//console.log("Junk Message is " + ws_tinter.wsmsg);
		}
		return parsed;
	}
	
	function printDrawdownCanLabel() {
		printLabelType = "sampleCanLabel";
		printOrientation = "PORTRAIT";
		setLabelPrintEmbedContainer(printLabelType,printOrientation,clrntAmtList,canType);
		showPrintModal();
	}
	
	function printDrawdownStoreLabel() {
		printLabelType = "drawdownStoreLabel";
		printOrientation = "PORTRAIT";
		setLabelPrintEmbedContainer(printLabelType,printOrientation);
		showPrintModal();

	}

	function printDrawdownLabel() {
		printLabelType = "drawdownLabel";
		printOrientation = "LANDSCAPE";
		setLabelPrintEmbedContainer(printLabelType,printOrientation);
		showPrintModal();

	}
	
	function setLabelPrintEmbedContainer(labelType,orientation,clrntAmtList,canType) {
		var embedString = '<embed src="formulaUserPrintAction.action?reqGuid=<s:property value="reqGuid" escapeHtml="true"/>&printLabelType=' +
							labelType + '&printOrientation=' + orientation +
							'&clrntAmtList=' + clrntAmtList + '&canType=' + canType + '&printCorrectionLabel=' + false + '" frameborder="0" class="embed-responsive-item">';
		$("#printLabelEmbedContainer").html(embedString);
	}
	
	//Enable enter key to print.  No need for mouse click
	$("#printLabelPrint").keypress(function(event){
	    var keycode = (event.keyCode ? event.keyCode : event.which);
	    if(keycode == '13'){
			if ( $("#printLabelPrint").css('display') != 'none' && $("#printLabelPrint").css("visibility") != "hidden"){
			    // print button is visible
				 printButtonClickGetJson(); 
			}
	       
	    }
	});

	
	/* Dispense validation methods */
	
	$(function() {
		$("#tinterWarningListOK").on("click", function(event) {
			event.preventDefault();
			event.stopPropagation();
			waitForShowAndHide("#tinterWarningListModal");
			$("#verifyModal").modal('show');
		});
		
		$(document).on("shown.bs.modal", "#verifyModal", function(event) {
			$("#verifyScanInput").val("");
			$("#verifyScanInputError").text("");
			$("#verifyScanInput").focus();
		});

		$(document).on("keypress", "#verifyScanInput", function(event) {
			if (event.keyCode == 13) {
				event.preventDefault();
				$("#verifyButton").click();
			}
		});
		
		$(document).on("shown.bs.modal", "#positionContainerModal", function(event) {
			$("#startDispenseButton").focus();
		});
		
		$("#verifyButton").on("click", function(event) {
			event.preventDefault();
			event.stopPropagation();
			// verify scan
			if ($("#verifyScanInput").val() !== "" && 
					($("#verifyScanInput").val() == "${sessionScope[thisGuid].upc}" || 
					$("#verifyScanInput").val() == "${sessionScope[thisGuid].salesNbr}" || 
					$("#verifyScanInput").val().toUpperCase() == "${sessionScope[thisGuid].prodNbr} ${sessionScope[thisGuid].sizeCode}" || 
					$("#verifyScanInput").val().toUpperCase() == "${sessionScope[thisGuid].prodNbr}-${sessionScope[thisGuid].sizeCode}")) {
				
				waitForShowAndHide("#verifyModal");
				$("#positionContainerModal").modal('show');
			} else {
				$("#verifyScanInputError").text('<s:text name="displayFormula.productScannedDoesNotMatch"/>');
				$("#verifyScanInput").select();
			}
		});
		
		$("#startDispenseButton").on("click", function(event) {
			if (processingDispense == false) {
				processingDispense = true;
				stopSessionTimeoutTimers(timeoutWarning, timeoutExpire);
				event.preventDefault();
				event.stopPropagation();
				waitForShowAndHide("#positionContainerModal");
				$("#tinterInProgressModal").modal('show');
				rotateIcon();
				$("#tinterInProgressTitle").text('<s:text name="global.dispenseInProgress"/>');
				$("#tinterInProgressMessage").text('<s:text name="global.pleaseWaitTinterDispense"/>');

				// Call decrement colorants which will call dispense
				decrementColorantLevels();
			}
			// else do nothing
		});
		
	});
	
	
	function preDispenseCheck() {
		console.log(shotList);
		
		$("#tinterInProgressTitle").text('<s:text name="global.colorantLevelCheckInProgress"/>');
		$("#tinterInProgressMessage").text('<s:text name="global.pleaseWaitClrntLevelCheck"/>');
		$("#tinterInProgressModal").modal('show');
		rotateIcon();
		// Get SessionTinter, this is async ajax call so the rest of the logic is in the callback below
		getSessionTinterInfo("${reqGuid}", preDispenseCheckCallback);		
	}
	
	
	function preDispenseCheckCallback() {
		// check if purge required...
		var dateFromString = new Date(sessionTinterInfo.lastPurgeDate);
		var today = new Date();
		if (dateFromString.getFullYear() < today.getFullYear()
				|| dateFromString.getMonth() < today.getMonth()
				|| dateFromString.getDate() < today.getDate()) {
			$("#tinterErrorList").empty();
			$("#tinterErrorList").append('<li class="alert alert-danger"><s:text name="global.tinterPurgeIsRequiredLastDoneOnDate">'
										+ '<s:param>' + moment(dateFromString).format('ddd MMM DD YYYY') + '</s:param></s:text></li>');
			waitForShowAndHide("#tinterInProgressModal");
			$("#tinterErrorListModal").modal('show');
			$("#tinterErrorListTitle").text('<s:text name="global.purgeRequired"/>');
			$("#tinterErrorListSummary").text('<s:text name="global.saveGoHomeToPurge"/>');
		// Check Levels
		} else {
			// Check fill stop level
			var stopList = checkDispenseColorantEmpty(shotList,	sessionTinterInfo.canisterList);
			if (stopList[0] != null) {
				$("#tinterErrorList").empty();
				stopList.forEach(function(item) {
					$("#tinterErrorList").append('<li class="alert alert-danger">' + item + '</li>');
				});
				// show that colorant levels are too low to go on
				waitForShowAndHide("#tinterInProgressModal");
				$("#tinterErrorListModal").modal('show');
				$("#tinterErrorListTitle").text('<s:text name="global.colorantLevelTooLow"/>');
				$("#tinterErrorListSummary").text('<s:text name="global.saveFillGoHomeToUpdateClrnts"/>');
			} else {
				// check fill alarm level
				var warnList = checkDispenseColorantLow(shotList, sessionTinterInfo.canisterList);
				if (warnList[0] != null) {
					$("#tinterWarningList").empty();
					warnList.forEach(function(item) {
						$("#tinterWarningList").append('<li class="alert alert-warning">' + item + '</li>');
					});
					// Show in modal, they can say OK to continue
					waitForShowAndHide("#tinterInProgressModal");
					$("#tinterWarningListTitle").text('<s:text name="global.lowColorantLevels"/>');
					$("#tinterWarningListModal").modal('show');
				} else {
					//OK to verify
					waitForShowAndHide("#tinterInProgressModal");
					$("#verifyModal").modal('show');
				}
			} // end colorant level checks
		} // end purge check
	}
	
	
	function decrementColorantLevels() {
		console.log("Calling decrementColorantLevels");
		decrementColorantForDispense("${reqGuid}", shotList, decrementCallback);
	}

	
	function decrementCallback(myPassFail) {
		console.log("checking decrement pass/fail " + myPassFail);
		if (myPassFail === true) {
			dispense();
			$("#formulaUserPrintAction_recDirty").val(0);
		} else {
			waitForShowAndHide("#tinterInProgressModal");
		}
	}
	
	
	function writeDispense(myReturnMessage) {
		var curDate = new Date();
		var mydata = {
			reqGuid : "${reqGuid}",
			jsDateString : curDate.toString()
		};
		var jsonIn = JSON.stringify(mydata);
		
		$.ajax({
			url : "bumpDispenseCounterAction.action",
			contentType : "application/json; charset=utf-8",
			type : "POST",
			data : jsonIn,
			datatype : "json",
			async : true,
			success : function(data) {
				processingDispense = false;
				startSessionTimeoutTimers();
				if (data.sessionStatus === "expired") {
					window.location = "/CustomerSherColorWeb/invalidLoginAction.action";
				} else {
					$("#controlNbr").text(data.controlNbr);
					$("#controlNbrDisplay").show();
					$("#qtyDispensed").text(data.qtyDispensed);
					qtyRemaining = $('#qtyRemaining');
					qtyRemaining.text(parseInt(qtyRemaining.text()-1));
					if (parseInt(qtyRemaining.text())<0) {
						qtyRemaining.text(0);
					}
					// send tinter event (no blocking here)
					var teDetail = new TintEventDetail("ORDER NUMBER", $("#controlNbr").text(), 0);
					var tedArray = [ teDetail ];
					sendTinterEvent("${reqGuid}", curDate, myReturnMessage, tedArray);

					waitForShowAndHide("#tinterInProgressModal");
				}
			},
			error: function(err){
				console.log(err);
			}
		});
	}
	
</script>
</head>

<body>

<!-- including Header -->
<s:include value="Header.jsp"></s:include>

<div class="container-fluid">
	<div class="row">
		<div class="col-sm-3"></div>
		<div class="col-sm-6"></div>
		<div class="col-sm-3">
			<s:set var="thisGuid" value="reqGuid" />
		</div>
	</div>
	<br>
	<div class="row">
		<div class="col-sm-2"></div>
	</div>

	<s:if test="%{#session[reqGuid].controlNbr != null && #session[reqGuid].controlNbr > 0}">
	<div class="row" id="controlNbrDisplay">
	</s:if>
	<s:else>
	<div class="row" id="controlNbrDisplay" hidden="true">
	</s:else>

		<div class="col-lg-2 col-md-2 col-sm-1 col-xs-0"></div>
		<div class="col-lg-2 col-md-2 col-sm-3 col-xs-4">
			<strong><s:text name="global.jobNumber"/></strong>
		</div>
		<div class="col-lg-4 col-md-6 col-sm-7 col-xs-8" id="controlNbr">
			${sessionScope[thisGuid].controlNbr}</div>
		<div class="col-lg-4 col-md-2 col-sm-1 col-xs-0"></div>
	</div>
	
	<div class="row">
		<div class="col-lg-2 col-md-2 col-sm-1 col-xs-0"></div>
		<div class="col-lg-2 col-md-2 col-sm-3 col-xs-4">
			<s:iterator value="#session[reqGuid].jobFieldList" status="stat">
				<strong><s:property value="screenLabel" /><s:text name="global.colonDelimiter"/></strong>
				<br>
			</s:iterator>
		</div>
		<div class="col-lg-4 col-md-6 col-sm-7 col-xs-8">
			<s:iterator value="#session[reqGuid].jobFieldList" status="stat">
				<s:property value="enteredValue" />
				<br>
			</s:iterator>
		</div>
		<div class="col-lg-4 col-md-2 col-sm-1 col-xs-0"></div>
	</div>
	<br>

	<div class="row">
		<div class="col-lg-2 col-md-2 col-sm-1 col-xs-0"></div>
		<div class="col-lg-2 col-md-2 col-sm-3 col-xs-4">
			<strong><s:text name="global.colorCompanyColon"/></strong>
		</div>
		<div class="col-lg-4 col-md-6 col-sm-7 col-xs-8">
			${sessionScope[thisGuid].colorComp}</div>
		<div class="col-lg-4 col-md-2 col-sm-1 col-xs-0"></div>
	</div>
	<div class="row">
		<div class="col-lg-2 col-md-2 col-sm-1 col-xs-0"></div>
		<div class="col-lg-2 col-md-2 col-sm-3 col-xs-4">
			<strong><s:text name="global.colorIdColon"/></strong>
		</div>
		<div class="col-lg-4 col-md-6 col-sm-7 col-xs-8">
			<s:property value="#session[reqGuid].colorID" /></div>
		<div class="col-lg-4 col-md-2 col-sm-1 col-xs-0"></div>
	</div>
	<div class="row">
		<div class="col-lg-2 col-md-2 col-sm-1 col-xs-0"></div>
		<div class="col-lg-2 col-md-2 col-sm-3 col-xs-4">
			<strong><s:text name="global.colorNameColon"/></strong>
		</div>
		
		<div class="col-lg-4 col-md-2 col-sm-1 col-xs-0"></div>
	</div>
	<div class="row">
		<div class="col-lg-2 col-md-2 col-sm-1 col-xs-0"></div>
		<div class="col-lg-2 col-md-2 col-sm-3 col-xs-4">
			<strong><s:text name="global.notesColon"/></strong>
		</div>
		<div class="col-lg-2 col-md-2 col-sm-4 col-xs-6 mt-1"> 
			<s:property value="#session[reqGuid].colorNotes" />
			
			<div class="chip sw-bg-main"></div>
			<s:if test="%{#session[reqGuid].closestSwColorId != null && #session[reqGuid].closestSwColorId != ''}">
				<em>
					<s:text name="global.closestSWColorIs">
	 					<s:param><s:property value="#session[reqGuid].closestSwColorId" /></s:param>
						<s:param><s:property value="#session[reqGuid].closestSwColorName" /></s:param>
					</s:text>
				</em>
			</s:if>
		</div> 
		<div class="col-lg-6 col-md-6 col-sm-4 col-xs-2"></div>
	</div>
	<div class="row">
		<div class="col-lg-2 col-md-2 col-sm-1 col-xs-0"></div>
		<div class="col-lg-2 col-md-2 col-sm-3 col-xs-4">
			<strong><s:text name="global.salesNumberColon"/></strong>
		</div>
		<div class="col-lg-4 col-md-6 col-sm-7 col-xs-8">
			${sessionScope[thisGuid].salesNbr}<br>
		</div>
		<div class="col-lg-4 col-md-2 col-sm-1 col-xs-0"></div>
	</div>
	<div class="row">
		<div class="col-lg-2 col-md-2 col-sm-1 col-xs-0"></div>
		<div class="col-lg-2 col-md-2 col-sm-3 col-xs-4">
			<strong><s:text name="global.productNumberColon"/></strong>
		</div>
		<div class="col-lg-4 col-md-6 col-sm-7 col-xs-8">
			${sessionScope[thisGuid].prodNbr} -
			${sessionScope[thisGuid].sizeText}</div>
		<div class="col-lg-4 col-md-2 col-sm-1 col-xs-0"></div>
	</div>
	<div class="row">
		<div class="col-lg-2 col-md-2 col-sm-1 col-xs-0"></div>
		<div class="col-lg-2 col-md-2 col-sm-3 col-xs-4">
			<strong><s:text name="global.productDescrColon"/></strong>
		</div>
		<div class="col-lg-4 col-md-6 col-sm-7 col-xs-8">
			${sessionScope[thisGuid].intExt} ${sessionScope[thisGuid].quality}
			${sessionScope[thisGuid].composite} ${sessionScope[thisGuid].finish}<br>
			${sessionScope[thisGuid].base}<br>
		</div>
		<div class="col-lg-4 col-md-2 col-sm-1 col-xs-0"></div>
	</div>
	
	<s:if test="%{accountUsesRoomByRoom==true}">
		<div class="row">
			<div class="col-lg-2 col-md-2 col-sm-1 col-xs-0"></div>
			<div class="col-lg-2 col-md-2 col-sm-3 col-xs-4">
				<strong><s:text name="displayFormula.roomByRoomColon"/></strong>
			</div>
			<div class="col-lg-3 col-md-6 col-sm-7 col-xs-8">
				${sessionScope[thisGuid].roomByRoom}
			</div>
			<div class="col-lg-5 col-md-2 col-sm-1 col-xs-0"></div>
		</div>
	</s:if>
	<div class="row">
		<div class="col-lg-2 col-md-2 col-sm-1 col-xs-0"></div>
		<div class="col-lg-2 col-md-2 col-sm-3 col-xs-4">
			<strong><s:text name="sampleDispense.factoryFillColon"/></strong>
		</div>
		<div class="col-lg-3 col-md-6 col-sm-7 col-xs-8">
			<s:property value="%{factoryFill}"/>
		</div>
		<div class="col-lg-5 col-md-2 col-sm-1 col-xs-0"></div>
	</div>
	<div class="row">
		<div class="col-lg-2 col-md-2 col-sm-1 col-xs-0"></div>
		<div class="col-lg-2 col-md-2 col-sm-3 col-xs-4">
			<strong><s:text name="sampleDispense.canTypeColon"/></strong>
		</div>
		<div class="col-lg-3 col-md-6 col-sm-7 col-xs-8">
			${sessionScope[thisGuid].canType}
			<s:hidden id="sampleSize" value="%{sampleSize}" />
		</div>
		<div class="col-lg-5 col-md-2 col-sm-1 col-xs-0"></div>
	</div>
	<br>

	<s:form action="formulaUserPrintAction" validate="true"
			theme="bootstrap">
		<s:hidden name="siteHasPrinter" value="%{siteHasPrinter}" />
	</s:form>
	 
	<s:form validate="true" theme="bootstrap">
	<s:hidden name="reqGuid" value="%{reqGuid}"  id="reqGuid"/> 

	
	<div class="row mt-3">
		<div class="col-lg-2 col-md-2 col-sm-1 col-xs-0"></div>
		<div class="col-lg-5 col-md-8 col-sm-10 col-xs-12">
			<s:textfield id="sampleFill" readonly="true" label="%{getText('sampleDispense.productSampleFill')}"/>
		</div>
		<div class="col-lg-5 col-md-2 col-sm-1 col-xs-0"></div>
	</div>
	<div class="row mt-2 d-none" id="baseDispenseRow">
		<div class="col-lg-2 col-md-2 col-sm-1 col-xs-0"></div>
		<div class="col-lg-5 col-md-8 col-sm-10 col-xs-12">
			<input type="checkbox" id="includeBaseCheckBox" value="includeBase" checked disabled>
			<label for="includeBaseCheckBox"><s:text name="sampleDispense.includeBaseInDispense"/></label>
		</div>
		<div class="col-lg-5 col-md-2 col-sm-1 col-xs-0"></div>
	</div>
	
	<br>
	<div class="row">
		<div class="col-lg-2 col-md-2 col-sm-1 col-xs-0"></div>
		<div class="col-lg-4 col-md-6 col-sm-6 col-xs-12">
			<s:if test="hasActionMessages()">
				<s:actionmessage />
			</s:if>
		</div>
		<div class="col-lg-6 col-md-4 col-sm-5 col-xs-0"></div>
	</div>
	<div class="row mt-2">
		<div class="col-lg-2 col-md-2 col-sm-1 col-xs-0"></div>
		<div class="col-lg-5 col-md-8 col-sm-10 col-xs-12">
			<table class="table">
				<thead>
					<tr>
						<th class="bg-light"><strong>${sessionScope[thisGuid].displayFormula.clrntSysId}*<s:text name="global.colorant"/></strong></th>
						<th class="bg-light"><strong>${sessionScope[thisGuid].displayFormula.incrementHdr[0]}</strong></th>
					</tr>
				</thead>
				<tbody>
					<s:iterator value="displayFormula.ingredients" status="i">
						<tr id="<s:property value="%{#i.index}"/>" class="formulaRow">
							<td class="colorantName col-lg-5 col-md-6 col-sm-4 col-xs-4">
									<s:property value="tintSysId" />-<s:property value="name" /></td>
							<td class="numShots d-none"><s:property value="shots" /></td>
							<td class="shotSize d-none"><s:property value="shotSize" /></td>
							<td class="decimalOuncesDisplay"></td>
						</tr>
					</s:iterator>
				</tbody>
			</table>
			<div id="dispenseFloorErrorText" style="color:red" class="d-none">
				<s:text name="sampleDispense.colorantLowerThanDispenseFloor"/>
			</div>
		</div>
	</div>
	<br>
	
	<s:if test="%{siteHasTinter==true}">
				<div class="row" id="dispenseInfoRow">
					<div class="col-lg-2 col-md-2 col-sm-1 col-xs-0"></div>
					<div class="col-lg-6 col-md-8 col-sm-10 col-xs-12">
						<strong><s:text name="displayFormula.qtyDispensedColon"/></strong> <span
							class="dispenseInfo badge badge-secondary"
							style="font-size: .9rem;" id="qtyDispensed">${sessionScope[thisGuid].quantityDispensed}</span>
						<strong class="dispenseInfo pull-right" id="dispenseStatus"></strong>
					</div>
					<div class="col-lg-4 col-md-2 col-sm-1 col-xs-0"></div>
				</div>
				<div class="row" id="quantityInfoRow">
					<div class="col-lg-2 col-md-2 col-sm-1 col-xs-0"></div>
					<div class="col-lg-6 col-md-8 col-sm-10 col-xs-12">
						<strong><s:text name="displayFormula.qtyOrderedColon"/></strong> <span
							class="dispenseInfo badge badge-secondary"
							style="font-size: .9rem;" id="qtyOrdered">${sessionScope[thisGuid].quantityOrdered}</span>
					</div>
					<div class="col-lg-4 col-md-2 col-sm-1 col-xs-0"></div>
				</div>
				<div class="row" id="remainingInfoRow">
					<div class="col-lg-2 col-md-2 col-sm-1 col-xs-0"></div>
					<div class="col-lg-6 col-md-8 col-sm-10 col-xs-12">
						<strong><s:text name="displayFormula.qtyRemainingColon"/></strong> <span
							class="dispenseInfo badge badge-secondary"
							style="font-size: .9rem;" id="qtyRemaining"></span>
					</div>
					<div class="col-lg-4 col-md-2 col-sm-1 col-xs-0"></div>
				</div>
			</s:if>
			<s:else>
				<div class="row" id="dispenseInfoRow">
					<div class="col-lg-2 col-md-2 col-sm-1 col-xs-0"></div>
					<div class="col-lg-6 col-md-8 col-sm-10 col-xs-12">
						<strong><s:text name="displayFormula.qtyDispensedColon"/></strong> <span
							class="dispenseInfo d-none badge badge-secondary"
							style="font-size: .8rem;" id="qtyDispensed">${sessionScope[thisGuid].quantityDispensed}</span>
						<strong class="dispenseInfo d-none pull-right" id="dispenseStatus"></strong>
					</div>
					<div class="col-lg-4 col-md-2 col-sm-1 col-xs-0"></div>
				</div>
			</s:else>
			<br>
			
	<br>
	
	<div class="d-flex flex-row justify-content-around mt-3">
		<div class="col-lg-2 col-md-2 col-sm-1 col-xs-0 p-2"></div>
		<div class="col-lg-6 col-md-8 col-sm-10 col-xs-12 p-2">
			<s:submit cssClass="btn btn-primary" autofocus="autofocus" id="dispenseSampleButton" value="%{getText('global.dispenseSample')}"
					onclick="preDispenseCheck(); return false;" />
			<button type="button" class="btn btn-secondary" id="drawdownLabelPrint"
					onclick="printDrawdownLabel();return false;"><s:text name="global.drawdownLabel"/></button>
			<button type="button" class="btn btn-secondary" id="sampleCanLabelPrint"
					onclick="printDrawdownCanLabel();return false;"><s:text name="sampleDispense.canLabel"/></button>
			<button type="button" class="btn btn-secondary" id="storeLabelPrint"
					onclick="printDrawdownStoreLabel();return false;"><s:text name="global.storeLabel"/></button>
			<s:submit cssClass="btn btn-secondary pull-right" value="%{getText('global.done')}"
                   	action="userCancelAction" />
        </div>
		<div class="col-lg-4 col-md-2 col-sm-1 col-xs-0 p-2"></div>
	</div>
	</s:form>
	<br><br><br><br>
		
	<!-- Including footer -->
	<s:include value="Footer.jsp"></s:include>
		
		
		
	<!-- Tinter In Progress Modal Window -->
    <div class="modal fade" aria-labelledby="tinterInProgressModal" aria-hidden="true"  id="tinterInProgressModal" role="dialog" data-backdrop="static" data-keyboard="false">
    	<div class="modal-dialog" role="document">
			<div class="modal-content">
				<div class="modal-header">
					<i id="spinner" class="fa fa-refresh mr-3 mt-1 text-muted" style="font-size: 1.5rem;"></i>
					<h5 class="modal-title" id="tinterInProgressTitle"><s:text name="global.dispenseInProgress"/></h5>
					<button type="button" class="close" data-dismiss="modal" aria-label="Close" ><span aria-hidden="true">&times;</span></button>
				</div>
				<div class="modal-body">
					<p id="tinterInProgressDispenseStatus" font-size="4"></p>
					<p id="tinterInProgressMessage" font-size="4"></p>
					<p id="abort-message" font-size="4" style="display:none;color:purple;font-weight:bold"> <s:text name="global.pressF4ToAbort"/> </p>
					<ul class="list-unstyled" id="tinterProgressList"></ul> 
					<div class="progress-wrapper "></div>
	        	</div>
				<div class="modal-footer"></div>
			</div>
		</div>
	</div> 
	
	<!-- Tinter Error List Modal Window -->
    <div class="modal fade" aria-labelledby="tinterErrorListModal" aria-hidden="true"  id="tinterErrorListModal" role="dialog">
    	<div class="modal-dialog">
			<div class="modal-content">
				<div class="modal-header">
					<h5 class="modal-title" id="tinterErrorListTitle"><s:text name="global.tinterError"/></h5>
					<button type="button" class="close" data-dismiss="modal" aria-label="Close" ><span aria-hidden="true">&times;</span></button>
				</div>
				<div class="modal-body">	
					<div class="progress-wrapper "></div>
					<div>
						<ul class="p-0" id="tinterErrorList" style="list-style: none;">
						</ul>
					</div>
					<p id="tinterErrorListSummary" font-size="4"></p>
				</div>
				<div class="modal-footer">
					<button type="button" class="btn btn-primary" id="tinterErrorListOK" data-dismiss="modal" aria-label="Close" ><s:text name="global.ok"/></button>
				</div>
			</div>
		</div> 
	</div>			    

	<!-- Tinter Warning List Modal Window -->
	<div class="modal" aria-labelledby="tinterWarningListModal" aria-hidden="true" id="tinterWarningListModal" role="dialog">
		<div class="modal-dialog" role="document">
			<div class="modal-content">
				<div class="modal-header">
					<h5 class="modal-title" id="tinterWarningListTitle"><s:text name="global.tinterError"/></h5>
					<button type="button" class="close" data-dismiss="modal" aria-label="Close">
						<span aria-hidden="true">&times;</span>
					</button>
				</div>
				<div class="modal-body">
					<div>
						<ul class="p-0" id="tinterWarningList" style="list-style: none;">
						</ul>
					</div>
					<p id="tinterWarningListSummary" font-size="4"><s:text name="global.clickOKtoContinue"/></p>
				</div>
				<div class="modal-footer">
					<button type="button" class="btn btn-primary" id="tinterWarningListOK" data-dismiss="modal" 
						aria-label="Close"><s:text name="global.ok"/></button>
					<button type="button" class="btn btn-secondary" id="tinterWarningListCancel" 
						data-dismiss="modal" aria-label="Close"><s:text name="global.cancel"/></button>
				</div>
			</div>
		</div>
	</div>
		
	<!-- Dispense Verify Modal Window -->
	<div class="modal" aria-labelledby="verifyModal" aria-hidden="true" id="verifyModal" role="dialog">
		<div class="modal-dialog" role="document">
			<div class="modal-content">
				<div class="modal-header">
					<h5 class="modal-title"><s:text name="displayFormula.scanProductToVerifyDispense"/></h5>
					<button type="button" class="close" data-dismiss="modal" aria-label="Close">
						<span aria-hidden="true">&times;</span>
					</button>
				</div>
				<div class="modal-body">
					<input type="text" class="form-control" id="verifyScanInput" autofocus="autofocus">
					<strong id="verifyScanInputError"></strong>
				</div>
				<div class="modal-body">
					<span class="dispenseNumberTracker mx-auto" style="background-color: #FF0; font-size: 125%;"></span>
				</div>
				<div class="modal-footer">
					<button type="button" class="btn btn-primary" data-dismiss="modal" id="verifyButton">
						<s:text name="displayFormula.verify"/></button>
				</div>
			</div>
		</div>
	</div>
	
	<!-- Position Container Modal Window -->
	<div class="modal" aria-labelledby="positionContainerModal" aria-hidden="true" id="positionContainerModal" role="dialog">
		<div class="modal-dialog" role="document">
			<div class="modal-content">
				<div class="modal-header">
					<h5 class="modal-title"><s:text name="global.prepareforDispense"/></h5>
					<button type="button" class="close" data-dismiss="modal" aria-label="Close">
						<span aria-hidden="true">&times;</span>
					</button>
				</div>
				<div class="modal-body">
					<p font-size="4"><s:text name="global.positionContainerandClickStartDispensewhenReady"/></p>
				</div>
				<div class="modal-body">
					<span class="dispenseNumberTracker mx-auto" style="background-color: #FF0; font-size: 125%;"></span>
				</div>
				<div class="modal-footer">
					<button type="button" class="btn btn-primary" id="startDispenseButton"><s:text name="global.startDispense"/></button>
				</div>
			</div>
		</div>
	</div>
	
		
	<!-- Printer In Progress Modal Window -->
	<div class="modal" aria-labelledby="printerInProgressModal"
		aria-hidden="true" id="printerInProgressModal" role="dialog">
		<div class="modal-dialog" role="document">
			<div class="modal-content">
				<div class="modal-header">
					<!-- 	<i id="spinner" class="fa fa-refresh mr-3 mt-1 text-muted" style="font-size: 1.5rem;"></i> -->
					<h5 class="modal-title" id="printerInProgressTitle"><s:text name="displayFormula.labelPrinterError"/></h5>
					<button type="button" class="close" data-dismiss="modal"
						aria-label="Close">
						<span aria-hidden="true">&times;</span>
					</button>
				</div>
				<div class="modal-body">
					<p id="printerInProgressMessage" font-size="4"></p>
				</div>
				<div class="modal-footer"></div>
			</div>
		</div>
	</div>
	
					
	<!-- Print Label Modal Window -->
	<div class="modal" aria-labelledby="printLabelModal"
		aria-hidden="true" id="printLabelModal" role="dialog">
		<div class="modal-dialog modal-md">
			<div class="modal-content">
				<div class="modal-header">
					<h5 class="modal-title" id="printLabelTitle"><s:text name="displayFormula.printLabel"/></h5>
					<button type="button" class="close" data-dismiss="modal"
						aria-label="Close">
						<span aria-hidden="true">&times;</span>
					</button>
				</div>
				<div class="modal-body">
					<div id="printLabelEmbedContainer" class="embed-responsive embed-responsive-1by1">
						
					</div>
				</div>
				<div class="modal-footer">
					<div class="col-xs-6">
						<button type="button" class="btn btn-primary pull-left"
							id="printLabelPrint" data-dismiss="modal" aria-label="Print"  onclick="printButtonClickGetJson()"><s:text name="global.print"/></button>
					</div>
					<div class="col-xs-4">
						
						<select id="numLabels" name="numLabels"  >
							<option value="1">1</option>
							<option value="2">2</option>
							<option value="3">3</option>
							<option value="4">4</option>
							<option value="5">5</option>
							<option value="6">6</option>
							<option value="7">7</option>
							<option value="8">8</option>
							<option value="9">9</option>
							<option value="10">10</option>
							
						 </select>
					</div>
					 <div class="col-xs-2 ">
						<button type="button" class="btn btn-secondary"
						id="printLabelClose" data-dismiss="modal" aria-label="Close"><s:text name="global.close"/></button>
					</div>
				</div>
			</div>
		</div>
	</div>

	<script>
	$(document).ready(function() {
		
		// Display remaining qty. Don't display negative values if the user dispenses more than what was ordered.
		var qtyOrdered = parseInt(${sessionScope[thisGuid].quantityOrdered});
		var qtyDispensed = parseInt(${sessionScope[thisGuid].quantityDispensed});
		var remaining = (qtyOrdered - qtyDispensed);
		if (remaining < 0) {
			remaining = 0;
		}
		if(remaining == 0 && qtyOrdered != 0) {
			$("#formulaSetOrderQty").hide();
		}
		$("#qtyRemaining").text(remaining);
		
		if ($("#formulaUserPrintAction_siteHasPrinter").val() == "true") {
			getPrinterConfig();
		}
		$('#printLabelModal').on('shown.bs.modal', function () {
			$("#printLabelPrint").focus();
		}); 
	});
	</script>

</body>
</html>