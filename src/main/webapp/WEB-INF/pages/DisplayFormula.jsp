<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!doctype html>


<%@ taglib prefix="s" uri="/struts-tags"%>

<html lang="en">
<head>
<meta name="viewport" content="width=device-width, initial-scale=1">
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">

<title><s:text name="global.formula"/></title>
<!-- JQuery -->
<link rel="stylesheet" href="css/bootstrap.min.css" type="text/css">
<link rel="stylesheet" href="css/bootstrapxtra.css" type="text/css">
<link rel="stylesheet" href="js/smoothness/jquery-ui.min.css" type="text/css">
<link rel="stylesheet" href="css/CustomerSherColorWeb.css" type="text/css">
<link rel="stylesheet" href="font-awesome-4.7.0/css/font-awesome.min.css" type="text/css">
<script type="text/javascript" charset="utf-8" src="js/jquery-3.4.1.min.js"></script>
<script type="text/javascript" charset="utf-8" src="js/jquery-ui.min.js"></script>
<script type="text/javascript" charset="utf-8" src="js/bootstrap.min.js"></script>
<script type="text/javascript" charset="utf-8" src="js/moment.min.js"></script>
<script type="text/javascript" charset="utf-8" src="script/customershercolorweb-1.5.2.js"></script>
<script type="text/javascript" charset="utf-8" src="script/WSWrapper.js"></script>
<script type="text/javascript" charset="utf-8" src="script/printer-1.4.8.js"></script>
<script type="text/javascript" charset="utf-8" src="script/tinter-1.4.8.js"></script>
<script type="text/javascript" charset="utf-8" src="script/dispense-1.5.3.js"></script>
<script type="text/javascript" charset="utf-8"	src="script/GetProductAutoComplete.js"></script>
<script type="text/javascript" charset="utf-8"	src="script/ProductChange.js"></script>
<s:set var="thisGuid" value="reqGuid" />
<style>
.sw-bg-main {
	background-color: ${sessionScope[thisGuid].rgbHex};
}
.inputError {
	font-weight: bold;
	color: red;
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
//global vars
	var myPrintLabelType = "";
	var myPrintOrientation = "";
	var dispenseQuantity = 0;
	var numberOfDispenses = 0;
	var isHandDispense = false;
	var dispenseTracker = '<s:text name="displayFormula.contOutOfTotal"><s:param>' + numberOfDispenses + '</s:param><s:param>' + dispenseQuantity + '</s:param></s:text>';
	var printerConfig;
	var isCorrectionDispense = false;
	var printJsonIN = "";
	var processingDispense = false;
	var sendingTinterCommand = "false";
	var ws_tinter = new WSWrapper("tinter");
	var tinterErrorList;
	var _rgbArr = [];
	var formSubmitting = false;
	var salesNbr = "${sessionScope[reqGuid].salesNbr}";
	var colorComp = "${sessionScope[reqGuid].colorComp}";
	var myGuid = "${reqGuid}";
	 
	// now build the dispense formula object
	var sendingDispCommand = "false";

	// using strut2 to build shotlist...
	var shotList = [];
	<s:iterator value="dispenseFormula" status="clrnt">
	var color<s:property value="#clrnt.index"/> = new Colorant(
			"<s:property value="clrntCode"/>", <s:property value="shots"/>,
			<s:property value="position"/>, <s:property value="uom"/>);
	shotList.push(color<s:property value="#clrnt.index"/>);
	</s:iterator>
	//setup rgb display for progress bars
	<s:iterator value="tinter.canisterList" status="i">
	
			_rgbArr["<s:property value="clrntCode"/>"]="<s:property value="rgbHex"/>";  //for colored progress bars
	</s:iterator>

</script>
<script type="text/javascript">
//printer scripts

function setLabelPrintEmbedContainer(labelType,orientation,printWithFormula) {
	if(printWithFormula === true) {
		var embedString = '<embed src="formulaUserPrintAction.action?reqGuid=<s:property value="reqGuid" escapeHtml="true"/>&printLabelType=' + labelType + '&printOrientation=' + orientation + '" frameborder="0" class="embed-responsive-item">';
	} else {
		var embedString = '<embed src="formulaUserPrintActionNoFormula.action?reqGuid=<s:property value="reqGuid" escapeHtml="true"/>&printLabelType=' + labelType + '&printOrientation=' + orientation + '" frameborder="0" class="embed-responsive-item">';
	}
	$("#printLabelEmbedContainer").html(embedString);
}

function printStoreLabel(printWithFormula) {
	myPrintLabelType = "storeLabel";
	myPrintOrientation = "PORTRAIT";
	setLabelPrintEmbedContainer(myPrintLabelType,myPrintOrientation,printWithFormula);
	setTimeout(function() {
	prePrintSave(myPrintLabelType,myPrintOrientation);
	}, 500);
}

function printSelfTintCustLabel(printWithFormula) {
	
	myPrintLabelType = "selfTintCustLabel";
	myPrintOrientation = "PORTRAIT";
	setLabelPrintEmbedContainer(myPrintLabelType,myPrintOrientation,printWithFormula);
	setTimeout(function() {
	prePrintSave(myPrintLabelType,myPrintOrientation);
	}, 500);
}


function printDrawdownStoreLabel(printWithFormula) {
	myPrintLabelType = "drawdownStoreLabel";
	myPrintOrientation = "PORTRAIT";
	setLabelPrintEmbedContainer(myPrintLabelType,myPrintOrientation,printWithFormula);
	setTimeout(function() {
	prePrintSave(myPrintLabelType,myPrintOrientation);
	}, 500);
}

function printDrawdownLabel() {
	myPrintLabelType = "drawdownLabel";
	myPrintOrientation = "LANDSCAPE";
	setLabelPrintEmbedContainer(myPrintLabelType,myPrintOrientation);
	setTimeout(function() {
	prePrintSave(myPrintLabelType,myPrintOrientation);
	}, 500);
}

function prePrintSave(labelType, orientation) {
	// check whether room dropdown needs to be set first
	if (verifyRoomSelected() == true && validateQtyOrderedForPrint()){
		// save before action
		var myCtlNbr = parseInt($.trim($("#controlNbr").text()));
		if (isNaN(myCtlNbr))
			myCtlNbr = 0;
		if (myCtlNbr == 0) {
			console.log("ctlNbr is zero");
		}
		var myDirty = parseInt($.trim($("#formulaUserPrintAction_recDirty")
				.val()));
		if (isNaN(myDirty))
			myDirty = 0;
		if (myDirty > 0) {
			console.log("dirty is true");
		}
		if (myCtlNbr == 0 || myDirty > 0) {
			//save needed before print
			// 			$("#tinterInProgressTitle").text("Saving Changes");
			// 			$("#tinterInProgressMessage").text("Saving Changes before print...");
			// 			$("#tinterInProgressModal").modal('show');
	
			var curDate = new Date();
			$("#formulaUserPrintAction_jsDateString").val(curDate.toString());
			var myGuid = $("#reqGuid").val();
			$
					.ajax({
						url : "saveBeforeAction.action",
						type : "POST",
						data : {
							reqGuid : myGuid,
							jsDateString : curDate.toString()
						},
						datatype : "json",
						async : true,
						success : function(data) {
							if (data.sessionStatus === "expired") {
								window.location.href = "./invalidLoginAction.action";
							} else {
								$("#controlNbr").text(data.controlNbr);
								$("#controlNbrDisplay").show();
								//    	 				$("#tinterInProgressModal").modal('hide');
								updateButtonDisplay();
								showPrintModal();
								var saveActionDirty = parseInt(data.recDirty);
								if (!isNaN(saveActionDirty)){
									$("#formulaUserPrintAction_recDirty").val(saveActionDirty);
								}
							}
						},
						error : function(err) {
							alert("failure: " + err);
						}
					});
	
		} else {
	
			showPrintModal();
		}
	}
}


function showPrintModal(){
	if(printerConfig && printerConfig.model){
		
		$("#printLabelPrint").css("visibility", "visible");
		$("#printLabelPrint").focus();
		
		
		$("#numLabels").val(printerConfig.numLabels);
		
		$("#numLabels").css("visibility", "visible");
	}
	else{
		$("#printLabelPrint").css("visibility", "hidden");
		$("#numLabels").css("visibility", "hidden");
	}
	
	$("#printLabelModal").modal('show');

	
}

function printButtonClickGetJson() {
	if (printerConfig && printerConfig.model) {
		var myguid = $("#reqGuid").val();
		str = { "reqGuid" : myguid, "printLabelType" : myPrintLabelType, "printOrientation" : myPrintOrientation, "printCorrectionLabel" : false, "shotList" : shotList};
		printJsonIN = JSON.stringify(str);
		var myPdf = new pdf(myguid,printJsonIN);
		$("#printerInProgressMessage").text('<s:text name="displayFormula.printerInProgress"/>');
		var numLabels = null;

		numLabels = printerConfig.numLabels;
		numLabelsVal = $("#numLabels").val();
		if(numLabelsVal && numLabelsVal !=0){
			numLabels = numLabelsVal;
		}
		printLabel(myPdf, numLabels, myPrintLabelType, myPrintOrientation);
		var isPrintWithLabelChecked = $('#labelWithFormula').is(":checked");
		if(isPrintWithLabelChecked === false) {
			$('#labelWithFormula').prop('checked', true);
		}
	}

}

function printFromManualDispense(dispenseQuantity) {
	if (printerConfig && printerConfig.model && printerConfig.printOnDispense) {
		//Added conditional so the proper label prints for stores and self tinting customers.
		if ($('#formulaUserPrintAction_accountIsSwStore').val() === 'true') {
			myPrintLabelType = "storeLabel";
		} else {
			myPrintLabelType = "selfTintCustLabel";
		}
		myPrintOrientation = "PORTRAIT";
		var myguid = $("#reqGuid").val();
		str = { "reqGuid" : myguid, "printLabelType" : myPrintLabelType, "printOrientation" : myPrintOrientation, "printCorrectionLabel" : false, "shotList" : shotList};
		printJsonIN = JSON.stringify(str);
		var myPdf = new pdf(myguid,printJsonIN);
		$("#printerInProgressMessage").text('<s:text name="displayFormula.printerInProgress"/>');
		printLabel(myPdf, dispenseQuantity, myPrintLabelType, myPrintOrientation);
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
		} else {
			//was hitting this on ws_tinter.wsmsg causing the modal to show.  was designed to show when SWDeviceHandler not installed 
			//$("#printLabelModal").modal('show'); //DJM switch to pdf popup as before

			/* DJM switch to this if 
			if(ws_printer && ws_printer.wserrormsg!=null && ws_printer.wserrormsg != ""){
			$("#printerInProgressMessage").text(
					ws_printer.wserrormsg);
			}
			else {
				$("#printerInProgressMessage").text(
						"Unknown error communicating with SWDeviceHandler");
			}
			 */
		}

	} catch (error) {
		console.log("Caught error is = " + error + " If response is for tinter message, this error trying to parse printer message is expected.");
		console.log("Message is junk, throw it out");
		//console.log("Junk Message is " + ws_tinter.wsmsg);
	}
	return parsed;
}
</script>


<script type="text/javascript">
	//global variables moved up above
	//tinter stuff moved to dispense-x.x.x.js
	
	function showSetOrderQuantityModal() {
		var myCtlNbr = parseInt($.trim($("#controlNbr").text()));
		if (isNaN(myCtlNbr))
			myCtlNbr = 0;
		if (myCtlNbr == 0) {
			console.log("ctlNbr is zero");
		}
		var myDirty = parseInt($.trim($("#formulaUserPrintAction_recDirty")
				.val()));
		if (isNaN(myDirty))
			myDirty = 0;
		if (myDirty > 0) {
			console.log("dirty is true");
		}
		if (myCtlNbr == 0 || myDirty > 0) {
			var curDate = new Date();
			$("#formulaUserPrintAction_jsDateString").val(curDate.toString());
			var myGuid = $("#reqGuid").val();
			$
					.ajax({
						url : "saveBeforeAction.action",
						type : "POST",
						data : {
							reqGuid : myGuid,
							jsDateString : curDate.toString()
						},
						datatype : "json",
						async : true,
						success : function(data) {
							if (data.sessionStatus === "expired") {
								window.location.href = "./invalidLoginAction.action";
							} else {
								$("#controlNbr").text(data.controlNbr);
								$("#controlNbrDisplay").show();
								$('#setOrderQuantityModal').modal('show');
								var saveActionDirty = parseInt(data.recDirty);
								if (!isNaN(saveActionDirty)){
									$("#formulaUserPrintAction_recDirty").val(saveActionDirty);
								}
							}
						},
						error : function(err) {
							alert("failure: " + err);
						}
					});
		} else {
			console.log("Job is not dirty. Save not needed.");
			$('#setOrderQuantityModal').modal('show');
		}	
	}
	
	function writeDispense(myReturnMessage) {
		var myValue = $("#reqGuid").val();
		var curDate = new Date();
		$("#dispenseStatus").text('<s:text name="global.lastDispenseComplete"/>');
		$
				.getJSON(
						"bumpDispenseCounterAction.action?reqGuid=" + myValue
								+ "&jsDateString=" + curDate.toString(),
						function(data) {
							processingDispense = false;
							startSessionTimeoutTimers();
							if (data.sessionStatus === "expired") {
								window.location.href = "./invalidLoginAction.action";
							} else {
								$("#controlNbr").text(data.controlNbr);
								$("#controlNbrDisplay").show();
								$("#qtyDispensed").text(data.qtyDispensed);
								updateButtonDisplay();
								
								qtyRemaining = $('#qtyRemaining');
								qtyRemaining.text(parseInt(qtyRemaining.text()-1));
								if (parseInt(qtyRemaining.text())<=0) {
									qtyRemaining.text(0);
								//	$("#qtyOrderedTextField").prop('disabled', true);
								}
								
								//$("#formulaUserPrintAction_qtyDispensed").val(data.qtyDispensed);
								// send tinter event (no blocking here)
								var myGuid = $(
										"#reqGuid")
										.val();
								var teDetail = new TintEventDetail(
										"ORDER NUMBER",
										$("#controlNbr").text(), 0);
								var tedArray = [ teDetail ];
								sendTinterEvent(myGuid, curDate,
										myReturnMessage, tedArray);

								if (printerConfig
										&& printerConfig.printOnDispense) {
									// Currently only storeLabels can be printed through dispense.
									//Added conditional so the proper label prints for stores and self tinting customers.
									if ($('#formulaUserPrintAction_accountIsSwStore').val() === 'true') {
										myPrintLabelType = "storeLabel";
									} else {
										myPrintLabelType = "selfTintCustLabel";
									}
									myPrintOrientation = "PORTRAIT";
									var myguid = $("#reqGuid").val();
									var correctionStr = { "reqGuid" : myguid, "printLabelType" : myPrintLabelType, "printOrientation" : myPrintOrientation, "printCorrectionLabel" : false, "shotList" : shotList};
									printJsonIN = JSON.stringify(correctionStr);
									printOnDispenseGetJson(myguid,printJsonIN);
								}
								waitForShowAndHide("#tinterInProgressModal");
								if (numberOfDispenses != dispenseQuantity) {
									numberOfDispenses++;
									console.log("Dispense Complete: Going to the next container.");
									preDispenseCheck();
								}
							}
						});
	}

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
		$("#verifyButton")
				.on(
						"click",
						function(event) {
							event.preventDefault();
							event.stopPropagation();

							//Attempt to fill the shorthand product
							let filledProdNbr = "";
							$.ajax({	
								url : "fillProdNbrAction",
								dataType : "json",
								data : {"shorthandProdNbr" : $("#verifyScanInput").val(), "reqGuid" : $('#reqGuid').val() },
								success : function(data){
									if(data.sessionStatus === "expired"){
				                		window.location.href = "./invalidLoginAction.action";
				                	}
									else {
										filledProdNbr = data.shorthandProdNbr;
										// verify scan
										if ($("#verifyScanInput").val() !== "" 
											&& ($("#verifyScanInput").val() == "${sessionScope[thisGuid].upc}" 
											|| $("#verifyScanInput").val() == "${sessionScope[thisGuid].salesNbr}"
									        || $("#verifyScanInput").val().toUpperCase() == "${sessionScope[thisGuid].prodNbr} ${sessionScope[thisGuid].sizeCode}" 
											|| filledProdNbr == "${sessionScope[thisGuid].prodNbr}-${sessionScope[thisGuid].sizeCode}"
											|| $("#verifyScanInput").val().toUpperCase() == "${sessionScope[thisGuid].prodNbr}-${sessionScope[thisGuid].sizeCode}")) {
												waitForShowAndHide("#verifyModal");
												$("#positionContainerModal").modal('show');
										} 
										else {
											$("#verifyScanInputError").text(
													'<s:text name="displayFormula.productScannedDoesNotMatch"/>');
											$("#verifyScanInput").select();
										}
									}
								}
							});
							
					
						});
		
		$(document).on("keypress", "#dispenseQuantityInput", function(event) {
			if (event.keyCode == 13) {
				event.preventDefault();
				$("#setDispenseQuantityButton").click();
			}
		});

		$("#setDispenseQuantityButton")
				.on(
						"click",
						function(event) {
							event.preventDefault();
							event.stopPropagation();
							// verify quantity input
							//var quantity = parseInt($("#dispenseQuantityInput").val);
							var quantity = parseInt($("#dispenseQuantityInput").val());
							var dispensed = parseInt($("#qtyDispensed").text());
							var ordered = parseInt($('#qtyOrderedTextField').val());
							
							//don't let them dispense if they're over the limit
							if(ordered < dispensed + quantity){
								console.log("Invalid input was entered. Input was: "+ quantity);
								$("#dispenseQuantityInputError").text('<s:text name="displayFormula.valueExceeded"/>');
								$("#dispenseQuantityInput").select();
							}
							else{
							//$("#dispenseQuantityInput").attr("value",quantity);
							if (quantity > 0 && quantity < 1000) {
								console
										.log("Number of containers to dispense: "
												+ quantity);
								dispenseQuantity = quantity;
								numberOfDispenses = 1;
								waitForShowAndHide("#setDispenseQuantityModal");
								//Set Order Quantity to Dispense Quantity if the user has leave the Qty Ordered field blank (or 0)
								var dispQtyOrdered = $('#qtyOrderedTextField');
								if (dispQtyOrdered.val() == "" || dispQtyOrdered.val() == 0) {
									dispQtyOrdered.val(dispenseQuantity);
									saveQtyOrdered(dispenseQuantity);
								}
								if (isHandDispense == true) {
									//TODO: manual qty disp counter and print num labels
									printFromManualDispense(dispenseQuantity);
									var i = 0
									var myValue = $("#reqGuid").val();
									var curDate = new Date();
									while (i < dispenseQuantity) {
										console.log("BUMPING DISPENSE #"+i);
										$
										.getJSON(
												"bumpDispenseCounterAction.action?reqGuid=" + myValue
														+ "&jsDateString=" + curDate.toString(),
												function(data) {
													if (data.sessionStatus === "expired") {
														window.location.href = "./invalidLoginAction.action";
													} else {
														$("#controlNbr").text(data.controlNbr);
														$("#controlNbrDisplay").show();
														$("#qtyDispensed").text(data.qtyDispensed);
														
														qtyRemaining = $('#qtyRemaining');
														qtyRemaining.text(parseInt(qtyRemaining.text()-1));
														if (parseInt(qtyRemaining.text())<=0) {
															qtyRemaining.text(0);
														//	$("#qtyOrderedTextField").prop('disabled', true);
														}

														updateButtonDisplay();
														console.log("UPDATED BUTTON DISPLAY")
														$("#formulaUserPrintAction_recDirty").val(0);
													}
												});
										i++;
									}
									console.log("END BUMP DISPENSE LOOP");
								} else {
									preDispenseCheck();
								}
							} else {
								console
										.log("Invalid input was entered. Input was: "
												+ quantity);
								$("#dispenseQuantityInputError")
										.text(
												'<s:text name="displayFormula.invalidInput"/>');
								$("#dispenseQuantityInput").select();
							}
						}
					});

		$(document).on("shown.bs.modal", "#positionContainerModal",
				function(event) {
					$("#startDispenseButton").focus();
				});

		$("#startDispenseButton")
				.on(
						"click",
						function(event) {
							if (processingDispense == false) {
								processingDispense = true;
								stopSessionTimeoutTimers(timeoutWarning, timeoutExpire);
								event.preventDefault();
								event.stopPropagation();
								waitForShowAndHide("#positionContainerModal");
								$("#tinterInProgressModal").modal('show');
								rotateIcon();
								$("#tinterInProgressTitle").text(
										'<s:text name="global.dispenseInProgress"/>');
								$("#tinterInProgressMessage")
										.text(
												'<s:text name="global.pleaseWaitTinterDispense"/>');

								// Call decrement colorants which will call dispense
								decrementColorantLevels();
								
							}
							// else do nothing

						});
		$("#formulaUserPrintAction_formulaUserSaveAction").on(
				"click",
				function() {
					var curDate = new Date();
					$("#formulaUserPrintAction_jsDateString").val(
							curDate.toString());
				});
	});
/*
	//Used to rotate loader icon in modals
	function rotateIcon() {
		let n = 0;
		$('#spinner').removeClass('d-none');
		let interval = setInterval(function() {
			n += 1;
			if (n >= 60000) {
				$('#spinner').addClass('d-none');
				clearInterval(interval);
			} else {
				$('#spinner').css("transform", "rotate(" + n + "deg)");
			}
		}, 5);

		$('#tinterInProgressModal').one('hide.bs.modal', function() {
			$('#spinner').addClass('d-none');
			if (interval) {
				clearInterval(interval);
			}
		});
		*/
	
</script>
<script type="text/javascript">
//callback stuff
	function setDispenseQuantity(handDispense) {
		var quantityOrdered = parseInt($('#qtyOrderedTextField').val());
		var quantityDispensed = parseInt($("#qtyDispensed").text());
		var quantityToDispense = quantityOrdered - quantityDispensed;
		// check that user doesn't need to set rooms dropdown
		if (verifyRoomSelected() == true && validateQtyOrdered() == true){
			isHandDispense = handDispense;
			$("#dispenseQuantityInputError").text("");
			$("#dispenseQuantityInput").val(quantityToDispense);
			$("#dispenseQuantityInput").attr("value", quantityToDispense);
			$("#setDispenseQuantityModal").modal('show');
			$("#dispenseQuantityInput").select();
		}
	}
	

	function preDispenseCheck() {
		$(".progress-wrapper").empty();
		$("#tinterInProgressTitle").text('<s:text name="global.colorantLevelCheckInProgress"/>');
		$("#tinterInProgressMessage")
				.text(
						'<s:text name="global.pleaseWaitClrntLevelCheck"/>');
		$("#tinterInProgressModal").modal('show');
		rotateIcon();
		// Get SessionTinter, this is async ajax call so the rest of the logic is in the callback below
		getSessionTinterInfo($("#reqGuid").val(),
				preDispenseCheckCallback);

	}

	function preDispenseCheckCallback() {
		dispenseNumberTracker = '<s:text name="displayFormula.contOutOfTotal"><s:param>' + numberOfDispenses + '</s:param><s:param>' + dispenseQuantity + '</s:param></s:text>';
		$(".dispenseNumberTracker").text(dispenseNumberTracker);
		// comes from getSessionTinterInfo
		// check if purge required...
		var dateFromString = new Date(sessionTinterInfo.lastPurgeDate);
		var today = new Date();
		if (dateFromString.getFullYear() < today.getFullYear()
				|| dateFromString.getMonth() < today.getMonth()
				|| dateFromString.getDate() < today.getDate()) {
			$("#tinterErrorList").empty();
			$("#tinterErrorList").append(
					'<li class="alert alert-danger"><s:text name="global.tinterPurgeIsRequiredLastDoneOnDate">'
							+ '<s:param>'+ moment(dateFromString).format('ddd MMM DD YYYY')
							+ '</s:param></s:text></li>');
			waitForShowAndHide("#tinterInProgressModal");
			$("#tinterErrorListModal").modal('show');
			$("#tinterErrorListTitle").text('<s:text name="global.purgeRequired"/>');
			$("#tinterErrorListSummary")
					.text(
							'<s:text name="global.saveGoHomeToPurge"/>');

		} else {
			// Check Levels
			console.log("about to check levels");
			// Check for STOP! because there is not enough colorant in the tinter
			var stopList = checkDispenseColorantEmpty(shotList,
					sessionTinterInfo.canisterList);
			if (stopList[0] != null) {
				$("#tinterErrorList").empty();
				stopList
						.forEach(function(item) {
							$("#tinterErrorList").append(
									'<li class="alert alert-danger">' + item
											+ '</li>');
						});
				//Show it in a modal they can't go on
				waitForShowAndHide("#tinterInProgressModal");
				$("#tinterErrorListModal").modal('show');
				$("#tinterErrorListTitle").text('<s:text name="global.colorantLevelTooLow"/>');
				$("#tinterErrorListSummary")
						.text(
								'<s:text name="global.saveFillGoHomeToUpdateClrnts"/>');

			} else {
				var warnList = checkDispenseColorantLow(shotList,
						sessionTinterInfo.canisterList);
				if (warnList[0] != null) {
					$("#tinterWarningList").empty();
					warnList.forEach(function(item) {
						$("#tinterWarningList").append(
								'<li class="alert alert-warning">' + item
										+ '</li>');
					});
					//Show in modal, they can say OK to continue
					waitForShowAndHide("#tinterInProgressModal");
					$("#tinterWarningListTitle").text('<s:text name="global.lowColorantLevels"/>');
					$("#tinterWarningListModal").modal('show');
				} else {
					console.log("about to show verify modal");
					//OK to verify
					console.log("in progress shown");
					waitForShowAndHide("#tinterInProgressModal");
					console.log("in progress hidden");
					$("#verifyModal").modal('show');
					console.log("end of else");

				}
			} // end colorant level checks
		} // end purge check
	}

	function decrementColorantLevels() {
		console.log("Calling decrementColorantLevels");
		decrementColorantForDispense(
				$("#reqGuid").val(), shotList,
				decrementCallback);
	}

	function decrementCallback(myPassFail) {
		console.log("checking decrement pass/fail " + myPassFail);
		if (myPassFail === true) {
			dispense();
			$("#formulaUserPrintAction_recDirty").val(0);
		} else {
			//TODO show error on decrement, 
			waitForShowAndHide("#tinterInProgressModal");
		}
	}
	
	
	
	
	/* ------ Drawdown / Room By Room functions --------- */
	
	
	$(function() {
		// if account is profiled as room by room and a room choice is in session, show it in dropdown
		var roomByRoomFlag = "${accountUsesRoomByRoom}";
		var userRoomChoice = "${roomByRoom}";
		var optionSelected = $("select[id='roomsList'] option:selected").text();
		
		// account uses room by room, and user already has a room choice stored in session
		if (roomByRoomFlag == "true" && userRoomChoice != null && userRoomChoice != ""){
			// room choice was a customized one; otherwise they match because the dropdown has already been updated
			if (optionSelected != userRoomChoice || userRoomChoice == "Other"){
				$("select[id='roomsList']").val('Other');
				$("#otherRoom").removeClass('d-none');
				$("#otherRoom").val(userRoomChoice);
			}
		}
		
		if ("${accountIsDrawdownCenter}"){			
			// warn user if tinter has no can types profiled for it
			var canTypesLength = $("select[id='canTypesList']").children('option').length;
			if (canTypesLength == 0){ 
				$("#canTypesErrorText").removeClass('d-none');
				$("#dispenseSampleButton").prop('disabled', true);
			} else {
				
				// check if saved option is still available in dropdown
				var canTypeMatch = $('#canTypesList option').filter(function() { 
				    return $(this).text() === "${canType}"; 
				}).length;
				
				// job has a saved can type
				if ("${canType}" != null && "${canType}" != ""){
					// set previously saved can type
					if (canTypeMatch > 0){		
						$("select[id='canTypesList']").val("${canType}");
						// update table
						canTypesUpdate();	
					
					// the job's saved can type isn't available for this tinter
					} else {
						// blank out formula, disallow save or dispense, show error
						$("select[id='canTypesList']").val("${canType}");
						$("#sampleFill").val("");
						$("#dispenseSampleButton").prop('disabled', true);
						$("#drawdownSaveButton").prop('disabled', true);
						$("#savedCanTypeError").text('<s:text name="displayFormula.canTypeNotAvailable"><s:param>' + "${canType}" + '</s:param></s:text>'); 
						$("#savedCanTypeError").removeClass('d-none');
					}
				// no can type saved yet
				} else {
					// update table, save default to session
					canTypesUpdate();
				}
					
				// if they've already dispensed, don't let them change the can type
				if ("${qtyDispensed}" != null  && "${qtyDispensed}" != "" && "${qtyDispensed}" > 0){
					$("#canTypesList").prop('disabled', true);
					$("#includeBaseCheckBox").prop('disabled', true);
				}
				
				// tinter does base dispense, and the base is loaded in a canister
				if ("${tinterDoesBaseDispense}" == "true" && "${baseDispense != null}" == "true"){
					// show checkbox
					$("#baseDispenseRow").removeClass("d-none");
					
					// uncheck base dispense because user saved it as false or unchecked it in this session
					if ("${dispenseBase}" == "0"){
						$("#includeBaseCheckBox").prop("checked", false);
					}
				}
			}
		}
		//When page comes up. Focus on roomList if possible.
		if($("#formulaUserPrintAction_accountUsesRoomByRoom").val() === 'true') {
			$("#roomsList").focus();
		} 
	});
	
	
	function canTypesUpdate(){
		// update colorant and base amounts
		calculateDecimalOunces();
		updateSampleFill();
		
		// save can type to session
		saveCanType($("select[id='canTypesList'] option:selected").text());
	}
				
	
	function calculateDecimalOunces(){
		clrntAmtList = "";
		var selectedIndex = $("select[id='canTypesList'] option:selected").index();
		var sampleSize = $("#canTypeSampleSizes li").eq(selectedIndex).text();
		
		var sampleSizeFloat = parseFloat(sampleSize);
		var sizeConversion = "${sizeConversion}";
		var dispenseFloor = "${dispenseFloor}";
		var dispenseFloorFloat = parseFloat(dispenseFloor);
		var sizeConvFloat = parseFloat(sizeConversion);
		
		// re-enable dispense and remove error text when user updates dropdown, then re-check colorant amounts
		$("#dispenseFloorErrorText").addClass('d-none');
		$("#savedCanTypeError").addClass('d-none');
		$("#dispenseSampleButton").prop('disabled', false);
		$("#drawdownSaveButton").prop('disabled', false);
		$('.decimalOuncesDisplay').css("color", "black");
		$('.colorantName').css("color", "black");
		
		if (!isNaN(sampleSizeFloat) && !isNaN(sizeConvFloat)){
			$(".formulaRow").each(function(){
				var colorantName = $(this).find('.colorantName').text();
				var numShots = $(this).find('.origNumShots').text();
				var shotSize = $(this).find('.shotSize').val();
				var shotsToOunces = Number(numShots) / Number(shotSize);
				if (!isNaN(shotsToOunces)){
					// calculate amount of each colorant:   Sample Size / 128 * ((Number of Shots / Shot Size) * Gallon Conversion) 
					var decimalOunces = sampleSizeFloat / 128 * (shotsToOunces * sizeConvFloat);
				
					// update table for save submission
					var updatedNumShots = Math.round(decimalOunces * shotSize);
					$(this).find('.numShots').val(updatedNumShots);
					
					// warn user if colorant amount is lower than tinter dispense floor, make them choose a larger can type
					if (!isNaN(dispenseFloorFloat) && decimalOunces < dispenseFloorFloat){
						$("#dispenseFloorErrorText").removeClass('d-none');
						$("#dispenseSampleButton").prop('disabled', true);
						$("#drawdownSaveButton").prop('disabled', true);
						// highlight colorant name and amount in red that is too low
						$(this).find('.decimalOuncesDisplay').css("color", "red");
						$(this).find('.colorantName').css("color", "red");
					}
					
					// round colorant amount to 5 decimal places for screen display and labels
					decimalOunces = decimalOunces.toFixed(5);
					clrntAmtList += colorantName + "," + decimalOunces.toString() + ",";
					
					// calculated amount displayed to the user and saved to table
					$(this).find('.decimalOuncesDisplay').text(decimalOunces);
					$(this).find('.decimalOunces').val(decimalOunces);
					
				} else {
					console.log("problem calculating shots to ounces");
				}
			});
		} else {
			console.log("problem parsing the sample size or conversion size");
		}
		//console.log("CLRNT AMT LIST: " + clrntAmtList);
	}
	

	function updateSampleFill(){
		var selectedIndex = $("select[id='canTypesList'] option:selected").index();
		canType = $("select[id='canTypesList'] option:selected").text();
		var factoryFill = "${factoryFill}";
		// get the sample size for this can type
		var sampleSize = $("#canTypeSampleSizes li").eq(selectedIndex).text();
		var sampleSizeFloat = parseFloat(sampleSize);
		
		if (!isNaN(sampleSizeFloat)){
			// calculate amount of base in the sample:   Sample Size / 128 * Factory Fill
			// don't need to do size conversion because factory fill is already scaled to a gallon
			var productSampleFill = sampleSizeFloat / 128 * factoryFill;
			// round to 5 digits without trailing zeroes
			$("#sampleFill").val(parseFloat(productSampleFill.toFixed(5)));
			
			// calculate shots in case user wants to dispense the base
			if ("${baseDispense != null}"){
				var baseShots = Math.round(productSampleFill * $("#baseUom").val()); 
				$("#baseShots").val(baseShots);
				$("#baseOunces").val(productSampleFill);
			}
		} else {
			console.log("problem parsing the sample size");
		}
	}
	
	
	function saveCanType(canType){
		var myGuid = "${reqGuid}";
		$.ajax({
			url : "saveCanType.action",
			type : "POST",
			data : {
				reqGuid : myGuid,
				canType : canType
			},
			datatype : "json",
			async : true,
			success : function(data) {
				if (data.sessionStatus === "expired") {
					window.location.href = "./invalidLoginAction.action";
				} else {
					//console.log("successfully saved can type");
				}
			},
			error : function(err) {
				alert('<s:text name="global.failureColon"/>' + err);
			}
		});
	}
	
	
	function saveDispenseBase(){
		var myGuid = "${reqGuid}";
		// set 1 if checked, 0 if unchecked 
		var dispenseBase = $("#includeBaseCheckBox").prop("checked") ? 1 : 0;
		
		$.ajax({
			url : "saveDispenseBase.action",
			type : "POST",
			data : {
				reqGuid : myGuid,
				dispenseBase : dispenseBase
			},
			datatype : "json",
			async : true,
			success : function(data) {
				if (data.sessionStatus === "expired") {
					window.location.href = "./invalidLoginAction.action";
				} else {
					//console.log("successfully saved dispense base");
				}
			},
			error : function(err) {
				alert('<s:text name="global.failureColon"/>' + err);
			}
		});
	}
	
	
	function saveColorNotes(){
		var myGuid = "${reqGuid}";
		var colorNotes = $("#colorNotes").val();
		$.ajax({
			url : "saveColorNotes.action",
			type : "POST",
			data : {
				reqGuid : myGuid,
				colorNotes : colorNotes
			},
			datatype : "json",
			async : true,
			success : function(data) {
				if (data.sessionStatus === "expired") {
					window.location.href = "./invalidLoginAction.action";
				}
			},
			error : function(err) {
				alert('<s:text name="global.failureColon"/>' + err);
			}
		});
	}
	
	
	function saveRoomSelection(roomChoice){
		var myGuid = "${reqGuid}";
		$.ajax({
			url : "saveRoomByRoom.action",
			type : "POST",
			data : {
				reqGuid : myGuid,
				roomByRoom : roomChoice
			},
			datatype : "json",
			async : true,
			success : function(data) {
				if (data.sessionStatus === "expired") {
					window.location.href = "./invalidLoginAction.action";
				} else {
					//console.log("successfully saved room");
				}
			},
			error : function(err) {
				alert('<s:text name="global.failureColon"/>' + err);
			}
		});
	}
	
	function saveQtyOrdered(qtyOrdered){
		var myGuid = "${reqGuid}";
		$.ajax({
			url : "saveQuantityOrdered.action",
			type : "POST",
			data : {
				reqGuid : myGuid,
				qtyOrdered : qtyOrdered
			},
			datatype : "json",
			async : true,
			success : function(data) {
				if (data.sessionStatus === "expired") {
					window.location.href = "./invalidLoginAction.action";
				} else {
					//console.log("successfully saved room");
					var qtyRemaining = qtyOrdered - parseInt($('#qtyDispensed').text());
					$("#qtyRemaining").text(qtyRemaining);
					//if (qtyRemaining == 0) {
					//	$("#qtyOrderedTextField").prop('disabled', true);
					//}
					$("#formulaUserPrintAction_recDirty").val(1);
				}
			},
			error : function(err) {
				alert('<s:text name="global.failureColon"/>' + err);
			}
		});
	}
	
	/* -------- Validation functions ----------- */
	
	function validateQtyOrdered(){
		var qtyOrdered = parseInt($("#qtyOrderedTextField").val());
		$("#qtyOrderedTextField").val(qtyOrdered);
		var qtyDisp = parseInt($.trim($("#qtyDispensed").text()));
		var qtyOrderedErrText = $("#qtyOrderedErrorText");
			//$("#savedCanTypeError").text('<s:text name="displayFormula.canTypeNotAvailable"><s:param>' + "${canType}" + '</s:param></s:text>');
		// clear out old error messages
		$("#qtyOrderedErrorText").addClass("d-none");
		
		 if (qtyOrdered > 0 && qtyOrdered < 1000){
			if (qtyDisp > qtyOrdered) {
				qtyOrderedErrText.text('<s:text name="displayFormula.invalidInputDispQty"></s:text>');
				qtyOrderedErrText.removeClass("d-none");
				return false;
			} else {
				saveQtyOrdered(qtyOrdered);
				return true;
			}
		} else {
			qtyOrderedErrText.text('<s:text name="displayFormula.invalidInput"></s:text>');
			qtyOrderedErrText.removeClass("d-none");
			return false;
		}
	}
	
	function validateQtyOrderedForPrint() {
		var qtyOrdered = parseInt($("#qtyOrderedTextField").val());
		$("#qtyOrderedTextField").val(qtyOrdered);
		var qtyDisp = parseInt($.trim($("#qtyDispensed").text()));
		var qtyOrderedErrText = $("#qtyOrderedErrorText");

		$("#qtyOrderedErrorText").addClass("d-none");
		
		 if (qtyOrdered > 0 && qtyOrdered < 1000){
			if (qtyDisp > qtyOrdered) {
				qtyOrderedErrText.text('<s:text name="displayFormula.invalidInputDispQty"></s:text>');
				qtyOrderedErrText.removeClass("d-none");
				return false;
			} else {
				return true;
			}
		} else {
			qtyOrderedErrText.text('<s:text name="displayFormula.invalidInput"></s:text>');
			qtyOrderedErrText.removeClass("d-none");
			return false;
		}
	}
	
	function validateRoomChoice(){
		// clear out old error messages
		$("#roomsDropdownErrorText").addClass("d-none");
		$("#otherRoomErrorText").addClass("d-none");
		
		// if user chose Other in room list, show textfield to enter custom name
		var selectedRoom = $("select[id='roomsList'] option:selected").text();
		if (selectedRoom == "Other"){
			$("#otherRoom").removeClass('d-none');
			$("#otherRoom").focus();
		// otherwise hide custom room textbox
		} else {
			$("#otherRoom").addClass('d-none');
			//  save the room choice if it's not the default blank option
			if (selectedRoom != ""){
				// call ajax method to save room choice to session
				saveRoomSelection(selectedRoom);
			}
		}
	}
	
	
	function validateCustomRoom(){
		var clickedElement;
		$("#otherRoomErrorText").addClass("d-none");
		// need the timeout for the event processing so we can grab the element that was clicked on
		setTimeout(function(){
	        // let the user click away from the rooms textfield to update the dropdown, 
	        // otherwise focus back on the textfield if they haven't entered text
	        if (document.activeElement != null){
	        	clickedElement = document.activeElement.id;
	        }
	     	// check textfield input, make sure it is not left blank
	        if (clickedElement == null || clickedElement != "roomsList"){
				var enteredText = $("#otherRoom").val();
				if (enteredText == null || enteredText.trim() == ""){
					$("#otherRoomErrorText").removeClass("d-none");
					$("#otherRoom").focus();
				} else {
					// call ajax method to save room choice to session
					saveRoomSelection(enteredText);
				}
			}
	    }, 0);
	}
	
	
	function verifyRoomSelected(){
		if ("${accountUsesRoomByRoom}" == "true"){
			// main room by room selection dropdown
			var roomText = $("select[id='roomsList'] option:selected").text();
			// custom text if they chose Other
			var enteredText = $("#otherRoom").val();
			
			// they left the room dropdown blank
			if (roomText == null || roomText == ""){
				$("#roomsList").focus();
				$("#roomsDropdownErrorText").removeClass("d-none");
				return false;
			// they picked Other but didn't enter text or put >30 characters
			} else if (roomText == "Other" && (enteredText == null || enteredText.trim() == "" || enteredText.length > 30)){
				$("#otherRoomErrorText").removeClass("d-none");
				$("#otherRoom").focus();
				return false;
			// input is validated
			} else {
				return true;
			}
		// skip this validation if user doesn't use room by room option
		} else {
			return true;
		}
	}			
	
		
	/* check that the user has set the room by room dropdown 
	and allow them to leave the page with unsaved changes
	*/
	function validationWithoutModal(){
		// check if rooms dropdown is set first, if applicable
		var retVal = verifyRoomSelected();
		//console.log("verifyRoomSelected: " + retVal);
		var retVal2 = validateQtyOrdered();
		// set the flag which lets user navigate away from 
		// the page without being prompted to save changes
		if (retVal == true && retVal2 == true){
			setFormSubmitting();
			return true;
		}
		$('#promptToSaveModal').modal('hide');
		return false;			
	}
	
	
	function setFormSubmitting() { formSubmitting = true; };
    
    window.onload = function() {
    window.addEventListener("beforeunload", function (e) {
            var recDirty = parseInt($.trim($("#formulaUserPrintAction_recDirty").val()));
            // let the user navigate away from the page
            if (formSubmitting || recDirty == 0) {
                return undefined;
            }
            // confirmation dialog
            e.preventDefault();
            // chrome requires returnValue to be set
            e.returnValue = ''; 
        });
    };
    


</script>
</head>
<body onLoad="window.scroll(0, document.body.scrollHeight)">
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

		<s:if
			test="%{#session[reqGuid].controlNbr != null && #session[reqGuid].controlNbr > 0}">
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
				<strong><s:property value="screenLabel" />:</strong>
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
		<div id="colorCompLabel" class="col-lg-2 col-md-2 col-sm-3 col-xs-4">
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
		<div class="col-lg-4 col-md-6 col-sm-7 col-xs-8">
			<s:property value="#session[reqGuid].colorName" />
		</div>
		<div class="col-lg-4 col-md-2 col-sm-1 col-xs-0"></div>
	</div>
	<div class="row">
		<div class="col-lg-2 col-md-2 col-sm-1 col-xs-0"></div>
		<div class="col-lg-2 col-md-2 col-sm-3 col-xs-4">
			<strong><s:text name="global.notesColon"/></strong>
		</div>
		<div class="col-lg-2 col-md-2 col-sm-4 col-xs-6 mt-1"> 
			<s:textfield name="colorNotes" id="colorNotes" size="20" maxlength="35" onblur="saveColorNotes()"/>
			
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
	<div class="row mt-1">
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
	<!-- 
	<s:if test="%{displayDeltaEColumn==true}">
		<div class="row">
			<div class="col-lg-2 col-md-2 col-sm-1 col-xs-0"></div>
			<div class="col-lg-2 col-md-2 col-sm-3 col-xs-4">
				<strong><s:text name="compareColorsResult.deltaEcolon"/></strong>
			</div>
			<div class="col-lg-4 col-md-6 col-sm-7 col-xs-8">
				<span style="color: red; font-weight: bold">${sessionScope[thisGuid].displayFormula.averageDeltaE}</span>
			</div>
			<div class="col-lg-4 col-md-2 col-sm-1 col-xs-0"></div>
		</div>
	</s:if> -->
	<s:if 
		test="%{
		#session[reqGuid].displayFormula.deltaEWarning == null ||
		#session[reqGuid].displayFormula.deltaEWarning == '' ||
		#session[reqGuid].productChosenFromDifferentBase == true
		}">
		<s:form action="formulaUserPrintAction" validate="true"
			theme="bootstrap">
			<s:if test = "%{accountIsDrawdownCenter==true}">
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
			</s:if>

			<s:if test = "%{accountIsDrawdownCenter==true && sessionHasTinter == true}">
				<div class="row mt-3">
					<div class="col-lg-2 col-md-2 col-sm-1 col-xs-0"></div>
					<div class="col-lg-2 col-md-2 col-sm-3 col-xs-4">
						<strong><s:text name="displayFormula.roomByRoomColon"/></strong>
					</div>
					<div class="col-lg-3 col-md-6 col-sm-7 col-xs-8">
						<s:select id="roomsList" onchange="validateRoomChoice()" list="roomByRoomList" 
							listKey="roomUse" listValue="roomUse" headerKey="-1" headerValue="" value="%{roomByRoom}"/>
						<div id="roomsDropdownErrorText" style="color:red" class="d-none">
							<s:text name="displayFormula.pleaseSelectARoom"/>
						</div>
						<s:textfield id="otherRoom" class="d-none" placeholder="%{getText('displayFormula.pleaseSpecifyRoom')}" maxlength="30" onblur="validateCustomRoom()"/>
						<s:hidden name="roomChoice" value="" />
						<div id="otherRoomErrorText" style="color:red" class="d-none">
							<s:text name="displayFormula.pleaseEnterWithinThirty"/>
						</div>
					</div>
					<div class="col-lg-5 col-md-5 col-sm-1 col-xs-0"></div>
				</div>
				<div class="row mt-3">
					<div class="col-lg-2 col-md-2 col-sm-1 col-xs-0"></div>
					<div class="col-lg-2 col-md-2 col-sm-3 col-xs-4">
						<strong><s:text name="sampleDispense.productSampleFill"/></strong>
					</div>
					<div class="col-lg-3 col-md-6 col-sm-7 col-xs-8">
						<s:textfield id="sampleFill" readonly="true" />
					</div>
					<div class="col-lg-5 col-md-2 col-sm-1 col-xs-0"></div>
				</div>
				<div class="row mt-3">
					<div class="col-lg-2 col-md-2 col-sm-1 col-xs-0"></div>
					<div class="col-lg-2 col-md-2 col-sm-3 col-xs-4">
						<strong><s:text name="sampleDispense.canTypeColon"/></strong>
					</div>
					<div class="col-lg-3 col-md-6 col-sm-7 col-xs-8">
						<s:select id="canTypesList" onchange="canTypesUpdate();" list="canTypesList" autofocus="true"
							listKey="canType" listValue="canType" />
						<ul class="d-none" id="canTypeSampleSizes">
						<s:iterator value="canTypesList" status="outerStat">
							<li class="sampleSize"><s:property value="sampleSize" /></li>
						</s:iterator>
						</ul>
						<div id="canTypesErrorText" style="color:red" class="d-none">
							<s:text name="sampleDispense.noCanTypesProfiledForTinter"/>
						</div>
						<div>
							<p id="savedCanTypeError" style="color:red" class="d-none"></p>
						</div>
					</div>
					<div class="col-lg-5 col-md-2 col-sm-1 col-xs-0"></div>
				</div>
				<div class="row mt-3 d-none" id="baseDispenseRow">
					<div class="col-lg-2 col-md-2 col-sm-1 col-xs-0"></div>
					<div class="col-lg-5 col-md-8 col-sm-10 col-xs-12">
						<input type="checkbox" id="includeBaseCheckBox" name="dispenseBase" onchange="saveDispenseBase();" checked>
						<label for="includeBaseCheckBox"><s:text name="sampleDispense.includeBaseInDispense"/></label>
					</div>	
					<div>
					<s:if test="%{baseDispense != null}">
						<s:hidden name="baseDispense.clrntCode" />
						<s:hidden id="baseShots" name="baseDispense.shots" />
						<s:hidden id="baseUom" name="baseDispense.uom" />
						<s:hidden id="baseOunces" name="baseDispense.decimalOunces" />
					</s:if>
					</div>	
					<div class="col-lg-5 col-md-2 col-sm-1 col-xs-0"></div>
				</div>
			</s:if>
			<br>
			
			
			<div class="row mt-3">
				<div class="col-lg-2 col-md-2 col-sm-1 col-xs-0"></div>
				<div class="col-lg-1 col-md-1 col-sm-2 col-xs-2"></div>
				<div class="col-lg-4 col-md-6 col-sm-7 col-xs-10">
				<s:if test="%{#session[reqGuid].colorType.equals('COMPETITIVE') || #session[reqGuid].colorType.equals('CUSTOMMATCH') || #session[reqGuid].colorType.equals('SAVEDMEASURE')}">
					${sessionScope[thisGuid].displayFormula.sourceDescr}  ${sessionScope[thisGuid].lightSourceName}<br>
				</s:if>
				<s:else>
					${sessionScope[thisGuid].displayFormula.sourceDescr}<br>
				</s:else>
				</div>
				<div class="col-lg-5 col-md-3 col-sm-2 col-xs-0"></div>
			</div>
			
	
			<div class="row">
				<div class="col-lg-2 col-md-2 col-sm-1 col-xs-0">
					<s:hidden name="reqGuid" value="%{reqGuid}"  id="reqGuid"/>
					<s:hidden name="jsDateString" value="" />
					<s:hidden name="siteHasTinter" value="%{siteHasTinter}" />
					<s:hidden name="siteHasPrinter" value="%{siteHasPrinter}" />
					<s:hidden name="displayDeltaEColumn" value="%{displayDeltaEColumn}" />
					<s:hidden name="sessionHasTinter" value="%{sessionHasTinter}" />
					<s:hidden name="accountIsDrawdownCenter" value="%{accountIsDrawdownCenter}" />	
			 		<s:hidden name="accountUsesRoomByRoom" value="%{accountUsesRoomByRoom}" /> 
					<s:hidden name="tinterClrntSysId"
						value="%{#session[reqGuid].tinter.clrntSysId}" />
					<s:hidden name="formulaClrntSysId"
						value="%{#session[reqGuid].displayFormula.clrntSysId}" />
					<s:hidden name="recDirty" value="%{recDirty}" />
					<s:hidden name="midCorrection" value="%{midCorrection}" />
					<s:hidden value="%{tinter.model}" id="tinterModel"></s:hidden>
					<s:hidden value="%{#session[reqGuid].packageColor}" id="isPackageColor" />
					<s:hidden value="%{#session[reqGuid].pkgClrTintable}" id="isTintable" />
					<s:hidden value="%{#session[reqGuid].lightSource}" id="lightSource" />
					<s:hidden value="%{#session[reqGuid].lightSourceName}" id="lightSourceName" />
					<s:hidden name="accountIsSwStore" value="%{accountIsSwStore}"/>
				</div>
				
				
				
	
				<div class="col-lg-4 col-md-6 col-sm-6 col-xs-12">
					<s:if test="hasActionErrors()">
						<s:actionerror />
					</s:if>
					<s:if test="hasActionMessages()">
						<s:actionmessage />
					</s:if>
				</div>
				<div class="col-lg-6 col-md-4 col-sm-5 col-xs-0"></div>
			</div>
			<br>
			<div class="row">
				<div class="col-lg-2 col-md-2 col-sm-1 col-xs-0"></div>
				<div class="col-lg-4 col-md-6 col-sm-10 col-xs-12">
				<s:if test="%{!accountIsDrawdownCenter || !sessionHasTinter}">
					<table class="table" id="ingredients_table">
						<thead>
							<tr>
								<th class="bg-light"><strong>${sessionScope[thisGuid].displayFormula.clrntSysId}*<s:text name="global.colorant"/></strong></th>
								<th class="bg-light"><strong>${sessionScope[thisGuid].displayFormula.incrementHdr[0]}</strong></th>
								<th class="bg-light"><strong>${sessionScope[thisGuid].displayFormula.incrementHdr[1]}</strong></th>
								<th class="bg-light"><strong>${sessionScope[thisGuid].displayFormula.incrementHdr[2]}</strong></th>
								<th class="bg-light"><strong>${sessionScope[thisGuid].displayFormula.incrementHdr[3]}</strong></th>
							</tr>
						</thead>
						<tbody>
							<s:iterator value="displayFormula.ingredients" status="i">
								<tr id="<s:property value="%{#i.index}"/>">
									<td class="col-lg-5 col-md-6 col-sm-4 col-xs-4"
										id="col1row<s:property value="%{#i.index}"/>"><s:property
											value="tintSysId" />-<s:property value="name" /></td>
									<s:iterator value="increment" status="stat">
										<td><s:property value="top" /></td>
									</s:iterator>
								</tr>
							</s:iterator>
						</tbody>
					</table>
				</s:if>
				
				<s:if test = "%{accountIsDrawdownCenter && sessionHasTinter}">
					<table class="table" id="decimalOuncesTable">
						<thead>
							<tr>
								<th class="bg-light"><strong>${sessionScope[thisGuid].displayFormula.clrntSysId}*<s:text name="global.colorant"/></strong></th>
								<th class="bg-light"><strong>${sessionScope[thisGuid].displayFormula.incrementHdr[0]}</strong></th>
							</tr>
						</thead>
						<tbody>
							<s:iterator value="drawdownShotList" status="i">
								<tr id="<s:property value="%{#i.index}"/>" class="formulaRow">
									<td class="colorantName col-lg-5 col-md-6 col-sm-4 col-xs-4">
										<s:property value="clrntCode" />-<s:property value="clrntName"/>
									</td>
									<s:hidden name="drawdownShotList[%{#i.index}].clrntCode" />
									<s:hidden name="drawdownShotList[%{#i.index}].clrntName" />
									<td class="origNumShots d-none"><s:property value="shots" /></td>
									<s:hidden class="numShots" name="drawdownShotList[%{#i.index}].shots" />
									<s:hidden class="shotSize" name="drawdownShotList[%{#i.index}].uom" />
									<td class="decimalOuncesDisplay"></td>
									<s:hidden class="decimalOunces" name="drawdownShotList[%{#i.index}].decimalOunces" />
								</tr>
							</s:iterator>
						</tbody>
					</table>
					<div id="dispenseFloorErrorText" style="color:red" class="d-none">
						<s:text name="sampleDispense.colorantLowerThanDispenseFloor"/>
					</div>
				</s:if>
				
				</div>
			</div>
			
			<s:if test="%{(accountUsesRoomByRoom==true && !accountIsDrawdownCenter) || (accountUsesRoomByRoom==true && accountIsDrawdownCenter && !sessionHasTinter)}">
				<div class="row mt-3">
					<div class="col-lg-2 col-md-2 col-sm-1 col-xs-0"></div>
					<div class="col-lg-1 col-md-1 col-sm-2 col-xs-3">
						<strong><s:text name="displayFormula.roomByRoomColon"/></strong>
					</div>
					<div class="col-lg-3 col-md-6 col-sm-7 col-xs-8">
						<s:select id="roomsList" onchange="validateRoomChoice()" list="roomByRoomList" 
							listKey="roomUse" listValue="roomUse" headerKey="-1" headerValue="" value="%{roomByRoom}"/>
						<div id="roomsDropdownErrorText" style="color:red" class="d-none">
							<s:text name="displayFormula.pleaseSelectARoom"/>
						</div>
						<s:textfield id="otherRoom" class="d-none" placeholder="%{getText('displayFormula.pleaseSpecifyRoom')}" maxlength="30" onblur="validateCustomRoom()"/>
						<s:hidden name="roomChoice" value="" />
						<div id="otherRoomErrorText" style="color:red" class="d-none">
							<s:text name="displayFormula.pleaseEnterWithinThirty"/>
						</div>
					</div>
					<div class="col-lg-6 col-md-3 col-sm-2 col-xs-1"></div>
				</div>
			</s:if>
			
			<!-- new stuff -->
			<div class="row mt-3">
				<div class="col-lg-2 col-md-2 col-sm-1 col-xs-0"></div>
				<div class="col-lg-1 col-md-2 col-sm-3 col-xs-4">
					<strong><s:text name="displayFormula.qtyOrderedColon"/></strong>
				</div>
				<div class="col-lg-2 col-md-6 col-sm-7 col-xs-8">
					<s:textfield type="number" id="qtyOrderedTextField" onblur="validateQtyOrdered()"/>
					<p id="qtyOrderedErrorText" style="color:red" class="d-none"></p>
				</div>
				<br>
				<s:if test="%{siteHasTinter==true}">
					<div class="row" id="dispenseInfoRow">
						<div class="col-lg-12 col-md-8 col-sm-10 col-xs-12">
							<strong><s:text name="displayFormula.qtyDispensedColon"/></strong> <span
								class="dispenseInfo badge badge-secondary"
								style="font-size: .9rem;" id="qtyDispensed">${sessionScope[thisGuid].quantityDispensed}</span>
							<div class="row" id="remainingInfoRow">
								<div class="col-lg-12 col-md-8 col-sm-10 col-xs-12">
									<strong><s:text name="displayFormula.qtyRemainingColon"/></strong> <span
										class="dispenseInfo badge badge-secondary"
										style="font-size: .9rem;" id="qtyRemaining"></span>
								</div>
							</div>
							<div class="row">
								<div class="col-lg-12 col-md-8 col-sm-10 col-xs-12">
									<strong class="dispenseInfo pull-right" id="dispenseStatus"></strong>
								</div>
							</div>
						</div>
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
			</div>
			<br>	
			<!-- end -->

			<s:if test = "%{accountIsDrawdownCenter==true}">
			<!-- validation methods: validationWithoutModal() verifies room by room dropdown is set and lets the user nav away from the page
			without modal prompt for unsaved changes. verifyRoomSelected() checks dropdown and browser shows unsaved changes dialog box.  
			promptToSave() shows modal asking user to save their unsaved changes before leaving the page, without checking rooms dropdown -->
				<div class="d-flex flex-row justify-content-around mt-3">
					<div class="col-lg-2 col-md-2 col-sm-1 col-xs-0 p-2"></div>
					<div class="col-lg-6 col-md-8 col-sm-10 col-xs-12 p-2">
						<s:if test = "%{sessionHasTinter}">
							<s:submit cssClass="btn btn-primary" id="dispenseSampleButton" value="%{getText('displayFormula.goToDispensePage')}"
								onclick="return validationWithoutModal();" action="saveDrawdownAction" />
						</s:if>
						<s:submit cssClass="btn btn-secondary" id="drawdownSaveButton" value="%{getText('global.save')}" 
							onclick="return validationWithoutModal();" action="formulaUserSaveAction" />
						<s:submit cssClass="btn btn-secondary" value="%{getText('editFormula.editFormula')}" 
							onclick="setFormSubmitting();" action="formulaUserEditAction" />
						<s:submit cssClass="btn btn-secondary" value="%{getText('displayFormula.copytoNewJob')}"
							onclick="return verifyRoomSelected();" action="displayJobFieldUpdateAction" />
						<s:submit cssClass="btn btn-secondary ml-5" value="%{getText('displayFormula.nextJob')}"
                        	onclick="return promptToSave();" action="userCancelAction" />
                	</div>
					<div class="col-lg-4 col-md-2 col-sm-1 col-xs-0 p-2"></div>
				</div>
				<div class="d-flex flex-row justify-content-around mt-2">
					<div class="col-lg-2 col-md-2 col-sm-1 col-xs-0 p-2"></div>
					<div class="col-lg-6 col-md-8 col-sm-10 col-xs-12 p-2">
						<s:submit cssClass="btn btn-secondary" id="changeProductBtn" action="changeProductAction" 
							onclick="return true;" value="%{getText('displayFormula.changeProduct')}"/>
						<s:if test="%{#session[reqGuid].colorType.equals('COMPETITIVE') || #session[reqGuid].colorType.equals('CUSTOMMATCH') || #session[reqGuid].colorType.equals('SAVEDMEASURE')}">	
							<s:submit cssClass="btn btn-secondary" id="changeLightSourceBtn" action="changeLightSourceAction" 
								onclick="return true;" value="%{getText('displayFormula.changeLightSource')}"/>
						</s:if>
						<button type="button" class="btn btn-secondary" id="drawdownLabelPrint"
							onclick="printDrawdownLabel();return false;"><s:text name="global.drawdownLabel"/></button>
						<button type="button" class="btn btn-secondary" id="storeLabelPrint"
							onclick="printDrawdownStoreLabel(true);return false;"><s:text name="global.storeLabel"/></button>
                	</div>
					<div class="col-lg-4 col-md-2 col-sm-1 col-xs-0 p-2"></div>
				</div>
			</s:if>
			<s:else>
				<div class="d-flex flex-row justify-content-around mt-3">
					<div class="col-lg-2 col-md-2 col-sm-1 col-xs-0 p-2"></div>
					<div class="col-lg-6 col-md-8 col-sm-10 col-xs-12 p-2">
						<button type="button" class="btn btn-primary" id="formulaDispense"
							onclick="setDispenseQuantity(false)" autofocus="autofocus"><s:text name="global.dispense"/></button>
						<s:submit cssClass="btn btn-secondary" value="%{getText('global.save')}" 
							onclick="return validationWithoutModal();" action="formulaUserSaveAction" autofocus="autofocus" />
						<s:if test = "%{accountIsSwStore==true}">	
							<button type="button" class="btn btn-secondary" id="formulaPrint"
								onclick="printStoreLabel(true);return false;"><s:text name="global.print"/></button>
						</s:if>
						<s:else>
							<button type="button" class="btn btn-secondary" id="formulaPrint"
								onclick="printSelfTintCustLabel(true);return false;"><s:text name="global.print"/></button>
						</s:else>
						<s:submit cssClass="btn btn-secondary" value="%{getText('editFormula.editFormula')}"
							onclick="setFormSubmitting();" action="formulaUserEditAction" />
						<s:submit cssClass="btn btn-secondary" value="%{getText('displayFormula.correct')}"
							onclick="return validationWithoutModal();" action="formulaUserCorrectAction" />
						<s:submit cssClass="btn btn-secondary" value="%{getText('displayFormula.copytoNewJob')}"
							onclick="return verifyRoomSelected();" action="displayJobFieldUpdateAction" />
						<s:submit cssClass="btn btn-secondary ml-5" value="%{getText('displayFormula.nextJob')}"
	                        onclick="return promptToSave();" action="userCancelAction" />
					</div>
					<div class="col-lg-4 col-md-2 col-sm-1 col-xs-0 p-2"></div>
				</div>
				<div class="d-flex flex-row justify-content-around mt-3">
					<div class="col-lg-2 col-md-2 col-sm-1 col-xs-0 p-2"></div>
					<div class="col-lg-6 col-md-8 col-sm-10 col-xs-12 p-2">
						<s:submit cssClass="btn btn-secondary" id="changeProductBtn" action="changeProductAction" 
							onclick="return true;" value="%{getText('displayFormula.changeProduct')}"/>
						<button type="button" class="btn btn-secondary" id="formulaManualDispense"
							onclick="setDispenseQuantity(true)" autofocus="autofocus"><s:text name="global.handDispense"/></button>
						<s:if test="%{#session[reqGuid].colorType.equals('COMPETITIVE') || #session[reqGuid].colorType.equals('CUSTOMMATCH') || #session[reqGuid].colorType.equals('SAVEDMEASURE')}">	
							<s:submit cssClass="btn btn-secondary" id="changeLightSourceBtn" action="changeLightSourceAction" 
								onclick="return true;" value="%{getText('displayFormula.changeLightSource')}"/>
						</s:if>
					</div>
					<div class="col-lg-4 col-md-2 col-sm-1 col-xs-0 p-2"></div>
				</div>
			</s:else>

			<!-- Set Dispense Quantity Modal Window -->
			<div class="modal" aria-labelledby="setDispenseQuantityModal"
				aria-hidden="true" id="setDispenseQuantityModal" role="dialog">
				<div class="modal-dialog" role="document">
					<div class="modal-content">
						<div class="modal-content">
							<div class="modal-header">
								<h5 class="modal-title"><s:text name="displayFormula.enterNumberofContainersToDispense"/></h5>
								<button type="button" class="close" data-dismiss="modal"
									aria-label="Close">
									<span aria-hidden="true">&times;</span>
								</button>
							</div>
							<div class="modal-body">
								<input type="text" class="form-control"
									id="dispenseQuantityInput" autofocus="autofocus"> <strong
									id="dispenseQuantityInputError" class="inputError"></strong>
							</div>
							<div class="modal-footer">
								<button type="button" class="btn btn-primary"
									data-dismiss="modal" id="setDispenseQuantityButton"><s:text name="global.next"/></button>
							</div>
						</div>
					</div>
				</div>

			</div>
			
			<!-- Dispense Verify Modal Window -->
			<div class="modal" aria-labelledby="verifyModal" aria-hidden="true"
				id="verifyModal" role="dialog">
				<div class="modal-dialog" role="document">
					<div class="modal-content">
						<div class="modal-header">
							<h5 class="modal-title"><s:text name="displayFormula.scanProductToVerifyDispense"/></h5>
							<button type="button" class="close" data-dismiss="modal"
								aria-label="Close">
								<span aria-hidden="true">&times;</span>
							</button>
						</div>
						<div class="modal-body">
							<input type="text" class="form-control" id="verifyScanInput"
								autofocus="autofocus"> <strong id="verifyScanInputError" class="inputError"></strong>
						</div>
						<div class="modal-body">
							<span class="dispenseNumberTracker mx-auto"
								style="background-color: #FF0; font-size: 125%;"></span>
						</div>
						<div class="modal-footer">
							<button type="button" class="btn btn-primary"
								data-dismiss="modal" id="verifyButton"><s:text name="displayFormula.verify"/></button>
						</div>
					</div>
				</div>
			</div>

			<!-- Position Container Modal Window -->
			<div class="modal" aria-labelledby="positionContainerModal"
				aria-hidden="true" id="positionContainerModal" role="dialog">
				<div class="modal-dialog" role="document">
					<div class="modal-content">
						<div class="modal-header">
							<h5 class="modal-title"><s:text name="global.prepareforDispense"/></h5>
							<button type="button" class="close" data-dismiss="modal"
								aria-label="Close">
								<span aria-hidden="true">&times;</span>
							</button>
						</div>
						<div class="modal-body">
							<p font-size="4"><s:text name="global.positionContainerandClickStartDispensewhenReady"/></p>
						</div>
						<div class="modal-body">
							<span class="dispenseNumberTracker mx-auto"
								style="background-color: #FF0; font-size: 125%;"></span>
						</div>
						<div class="modal-footer">
							<button type="button" class="btn btn-primary"
								id="startDispenseButton"><s:text name="global.startDispense"/></button>
						</div>
					</div>
				</div>
			</div>

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
									<p id="abort-message" font-size="4" style="display:none;color:purple;font-weight:bold">
										<s:text name="global.pressAkeyToAbort"/>
									</p>
									<ul class="list-unstyled" id="tinterProgressList"></ul> 
								
									<div class="progress-wrapper "></div>
					        	</div>
								<div class="modal-footer">
									<!-- <button id="progressok" type="button" class="btn btn-primary d-none" data-dismiss="modal" aria-label="Close" >OK</button> -->
								</div>
							</div>
						</div>
					</div> 
					
			<!-- Dispense Error Modal Window -->
			<div class="modal" aria-labelledby="tinterSocketErrorModal"
				aria-hidden="true" id="tinterSocketErrorModal" role="dialog">
				<div class="modal-dialog" role="document">
					<div class="modal-content">
						<div class="modal-header">
							<h5 class="modal-title"><s:text name="global.dispenseError"/></h5>
							<button type="button" class="close" data-dismiss="modal"
								aria-label="Close">
								<span aria-hidden="true">&times;</span>
							</button>
						</div>
						<div class="modal-body">
							<p id="tinterSocketError" font-size="4"></p>
						</div>
						<div class="modal-footer">
							<button type="button" class="btn btn-primary"
								id="tinterSocketErrorButton" data-dismiss="modal"
								aria-label="Close"><s:text name="global.close"/></button>
						</div>
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
			<div class="modal" aria-labelledby="tinterWarningListModal"
				aria-hidden="true" id="tinterWarningListModal" role="dialog">
				<div class="modal-dialog" role="document">
					<div class="modal-content">
						<div class="modal-header">
							<h5 class="modal-title" id="tinterWarningListTitle"><s:text name="global.tinterError"/></h5>
							<button type="button" class="close" data-dismiss="modal"
								aria-label="Close">
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
							<button type="button" class="btn btn-primary"
								id="tinterWarningListOK" data-dismiss="modal" aria-label="Close"><s:text name="global.ok"/></button>
							<button type="button" class="btn btn-secondary"
								id="tinterWarningListCancel" data-dismiss="modal"
								aria-label="Close"><s:text name="global.cancel"/></button>
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
							<div class="container">
								<div class="row">
									<div class="col-xs-2 mr-3">
										<button type="button" class="btn btn-primary pull-left"
												id="printLabelPrint" data-dismiss="modal" aria-label="Print"  onclick="printButtonClickGetJson()"><s:text name="global.print"/></button>
									</div>
									<div class="col-xs-2 mr-5">
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
									<div class="col-xs-6 mr-5">
										<input type="checkbox" class="form-check-input" id="labelWithFormula" checked>
    									<label class="form-check-label" for="labelWithFormula" id="labelWithFormulaLabel"><s:text name="global.formulaOnLabel"/></label>
									</div>
									<div class="col-xs-2">
										<button type="button" class="btn btn-secondary"
												id="printLabelClose" data-dismiss="modal" aria-label="Close"><s:text name="global.close"/></button>
									</div>
								</div>
							</div>
						</div>
					</div>
				</div>
			</div>
			
						    <!-- added by edo78r for PCSWEB-168 - 4/24/2020 -->
		<!-- Prompt To Save Modal -->	    
		<div class="modal" aria-labelledby="promptToSaveModal" aria-hidden="true"  id="promptToSaveModal" role="dialog">
	    	<div class="modal-dialog" role="document">
				<div class="modal-content">
					<div class="modal-header">
						<h5 class="modal-title"><s:text name="displayFormula.continueToNextJob"/></h5>
						<button type="button" class="close" data-dismiss="modal" aria-label="Close" ><span aria-hidden="true">&times;</span>
						</button>
					</div>
					<div class="modal-body">
						<p id="skipConfirmText" font-size="4"><s:text name="displayFormula.saveFormula"/></p>
					</div>
					<div class="modal-footer">
						<s:submit cssClass="btn btn-primary" value="%{getText('global.yes')}" onclick="return validationWithoutModal();" action="formulaUserSaveAndContinueAction" autofocus="autofocus" />
						<s:submit cssClass="btn btn-secondary" id="noSaveFormulaBtn" value="%{getText('global.no')}" onclick="setFormSubmitting();" action="userCancelAction"/>
						<s:submit cssClass="btn btn-secondary" id="btnCancel" data-dismiss="modal" value="%{getText('global.cancel')}"/>
					</div>
				</div>
			</div>
		</div>
		
		
		<!-- Change Product Modal -->
	    <div class="modal fade" aria-labelledby="changeProductModal" aria-hidden="true"  id="changeProductModal" role="dialog">
	    	<div class="modal-dialog" role="document" id="changeProdDialogModal">
				<div class="modal-content">
					<div class="modal-header">
						<h5 class="modal-title"><s:text name="displayFormula.changeProduct"/></h5>
						<button type="button" class="close" data-dismiss="modal" aria-label="%{getText('global.close')}" ><span aria-hidden="true">&times;</span></button>
					</div>
					<div class="modal-body ui-front mt-3">
						<div>
							<s:hidden id="reqGuid" value="%{reqGuid}"/>
							<s:textfield name="partialProductNameOrId" id="partialProductNameOrId" placeholder="%{getText('displayFormula.enterProduct')}" autofocus="autofocus" 
										 size="30" maxlength="20" cssStyle="font-size: 16px;"/>
						</div>
						<div id="actionMessageDisplay"></div>
						<div id="vinylSafePrompt" class="mt-4 d-none">
							<strong><s:text name="getVinylSafeOption.canBeUsedForVinylSafe"/><br><br><s:text name="getVinylSafeOption.createVinylSafe"/></strong>
						</div>
						<div id="prodChangeStatusMsg" class="mt-4 mx-1" style="font-weight: bold">
						</div>
						<div id="unavailableForVinylWarning" class="mt-4 mx-1 d-none" style="font-weight: bold">
							<s:text name="displayFormula.unavailableForVinyl"/>
						</div>
						<div class="d-none mt-4" id="changeProductMenu">
							<div class="form-check mt-1 nondefaultOptions d-none" id="optionAdjustSize">
								<input class="form-check-input" type="radio" name="radioProdChoice" value="adjustSize" id="radioAdjustSize">
								<label class="form-check-label" for="radioAdjustSize">
								 <s:text name="displayFormula.adjustFormulaToSize"/>
								</label>
							</div>
							<div class="form-check mt-1 nondefaultOptions d-none" id="optionRematch">
								<input class="form-check-input" type="radio" name="radioProdChoice" value="rematch" id="radioRematch">
								<label class="form-check-label" for="radioRematch">
								 <s:text name="displayFormula.rematchUsingColorEye"/>
								</label>
							</div>
 							<div class="form-check mt-1 nondefaultOptions d-none" id="optionReformulate">
								<input class="form-check-input" type="radio" name="radioProdChoice" value="reformulate" id="radioReformulate">
								<label class="form-check-label" for="radioReformulate">
								 <s:text name="displayFormula.reformulateUsingSherColor"/>
								</label>
							</div>
							<div class="form-check mt-1 nondefaultOptions d-none" id="optionTintStrength">
								<input class="form-check-input" type="radio" name="radioProdChoice" value="tintStrength" id="radioTintStrength">
								<label class="form-check-label" for="radioTintStrength">
								 <s:text name="displayFormula.adjustTintStrength"/>
								</label>
							</div>
							<div class="form-check mt-1 nondefaultOptions d-none" id="optionTintStrengthSize">
								<input class="form-check-input" type="radio" name="radioProdChoice" value="tintStrengthSize" id="radioTintStrengthSize">
								<label class="form-check-label" for="radioTintStrengthSize">
								 <s:text name="displayFormula.adjustTintStrengthAndSize"/>
								</label>
							</div>
							<div class="form-check mt-1">
								<input class="form-check-input" type="radio" name="radioProdChoice" value="manualAdjustment" id="radioManualAdjustment">
								<label class="form-check-label" for="radioManualAdjustment">
								 <s:text name="displayFormula.makeManualAdjustment"/>
								</label>
							</div>
							<div class="form-check mt-1">
								<input class="form-check-input" type="radio" name="radioProdChoice" value="doNotAdjust" id="radioDoNotAdjust">
								<label class="form-check-label" for="radioDoNotAdjust">
								  <s:text name="displayFormula.doNotAdjustFormula"/>
								</label>
							</div>
						</div>	
 						<div class="d-none mt-3" id="userIllumMenu">
							<h5 class="mt-4 mb-3"><s:text name="displayFormula.chooseLightSource"/></h5>
							<div class="form-check mt-1" id="optionIncandescent">
								<input class="form-check-input" type="radio" name="radioIllumChoice" value="A" id="radioIncandescent">
								<label class="form-check-label" for="radioIncandescent">
								 <s:text name="displayFormula.a-incandescent"/>
								</label>
							</div>
							<div class="form-check mt-1" id="optionDaylight">
								<input class="form-check-input" type="radio" name="radioIllumChoice" value="D65" id="radioDaylight">
								<label class="form-check-label" for="radioDaylight">
								 <s:text name="displayFormula.d65-daylight"/>
								</label>
							</div>
							<div class="form-check mt-1" id="optionFluorescent">
								<input class="form-check-input" type="radio" name="radioIllumChoice" value="F2" id="radioFluorescent">
								<label class="form-check-label" for="radioFluorescent">
								 <s:text name="displayFormula.f2-fluorescent"/>
								</label>
							</div>
						</div>
												
						<div id="prodChangeTable" class="d-none">
							<table class="table table-bordered">
								<thead>
									<tr>
										<th colspan="4" class="bg-light" id="prodChangeTableHeader" style="text-align: center"></th>
									</tr>
									<tr>
										<th scope="col"></th>
										<th scope="col"><s:text name="global.old"/></th>
										<th scope="col"><s:text name="global.new"/></th>
									</tr>
								</thead>
								<tbody>
									<tr id="row">
										<th scope="row"><s:text name="global.product"/></th>
										<td id="oldProdNbr"></td>
										<td id="newProdNbr"></td>
									</tr>
									<tr id="row">
										<th scope="row"><s:text name="global.size"/></th>
										<td id="oldSizeCode"></td>
										<td id="newSizeCode"></td>
									</tr>
									<tr id="row">
										<th scope="row"><s:text name="displayFormula.tintStrength"/></th>
										<td id="oldTintStrength"></td>
										<td id="newTintStrength"></td>
									</tr>
								</tbody>
							</table>
							<br>
							<p><strong><s:text name="displayFormula.clickNextToConvert"/></strong></p>
						</div>
						 
						<div id="prodFamilyRow" class="d-none">
							<table id="prodFamilyTable" class="table table-bordered table-hover mt-4">
								<caption style="caption-side:top;"><strong><s:text name="getProdFamily.betterPerformanceFoundinDifferentBase"/></strong></caption>
								<thead class="thead-light">
									<tr>
										<th></th>
										<th><s:text name="global.product"/></th>
										<th><s:text name="getProdFamily.quality"/></th>
										<th><s:text name="getProdFamily.base"/></th>
										<th><s:text name="getProdFamily.deltaE"/></th>
										<th><s:text name="getProdFamily.contrastRatio"/></th>
										<th><s:text name="global.comment"/></th>
										<th><s:text name="displayJobs.formulaHdr"/></th>
									</tr>
								</thead>
								<tbody>
									<tr class="border-bottom-1 border-dark table-success" id="betterPerformanceRow">
										<td><input type="radio" class="prodFamRadio" name="prodFamily" id="betterPerfRadio"/></td>
										<td class="prodDetail"></td>
										<td class="quality"></td>
										<td class="base"></td>
										<td class="deltaE"></td>
										<td class="contrastRatio"></td>
										<td class="comment"></td>
										<td class="formula">
											<table class="table-plain">
												<tbody id="bestPerformanceFormula"></tbody>
											</table>
										</td>		
									</tr>
									<tr class="border-bottom-1 border-dark" id="productEnteredRow">
										<td><input type="radio" class="prodFamRadio" name="prodFamily" id="prodEnteredRadio"/></td>
										<td class="prodDetail"></td>
										<td class="quality"></td>
										<td class="base"></td>
										<td class="deltaE"></td>
										<td class="contrastRatio"></td>
										<td class="comment"></td>
										<td class="formula">
											<table id="prodEnteredTable" class="table-plain">
												<tbody id="prodEnteredFormula"></tbody>
											</table>
										</td>	
									</tr>
								</tbody>
							</table>
							<p><strong><s:text name="getProdFamily.deltaEGreaterThanOneWarning"/><br><br><s:text name="global.pleaseSelectAProduct"/></strong></p>
						</div>
						
						
					</div>
					<div class="modal-footer mt-3">
						<button type="button" class="btn btn-primary" id="lookupProductNext" onclick="lookupNewProduct();"><s:text name="global.next"/></button>
						<button type="button" class="btn btn-primary d-none" id="vinylSafeYes"><s:text name="global.yes"/></button>
						<button type="button" class="btn btn-primary d-none" id="vinylSafeNo"><s:text name="global.no"/></button>
						<button type="button" class="btn btn-secondary ml-auto" id="changeProdCancel" data-dismiss="modal" aria-label="%{getText('global.cancel')}" ><s:text name="global.cancel"/></button>
					</div>
				</div>
			</div>
		</div>
			
		</s:form>
	</s:if>
	<s:else>
	<br><br>
		<s:form action="formulaUserPrintAsJsonAction" validate="true"
			theme="bootstrap">
			<div class="row">
				<div class="col-sm-2"></div>

				<div class="col-sm-8">
					<s:if test="hasActionMessages()">
						<s:actionmessage />
					</s:if>
				</div>
			</div>
			<div class="row">
				<div class="col-sm-2"></div>
				<div class="col-sm-8">
					<s:hidden name="reqGuid" value="%{reqGuid}"  id="reqGuid"/>

					<Strong>${sessionScope[thisGuid].displayFormula.deltaEWarning}</Strong>
				</div>
			</div>

			<br>
			<br>
			<div class="row">
				<div class="col-sm-2"></div>
				<div class="col-sm-2">
					<strong><s:text name="displayFormula.stillUse"/></strong>
				</div>
			</div>
			<br>
			<br>
			<div class="row">

				<div class="col-sm-2"></div>
				<div class="col-sm-2">
					<s:submit cssClass="btn btn-primary" value="%{getText('global.no')}" onclick="setFormSubmitting();"
						action="deltaENoAction" autofocus="autofocus" />
				</div>
				<div class="col-sm-2">
					<s:submit cssClass="btn btn-secondary" value="%{getText('global.yes')}" onclick="setFormSubmitting();"
						action="deltaEYesAction" />
				</div>

			</div>
		</s:form>
	</s:else>
		<!-- dummy div to clone -->
	<div id="progress-0" class="progress d-none" style="margin:10px;">
        <div id="bar-0" class="progress-bar" role="progressbar" aria-valuenow="0"
				 aria-valuemin="0" aria-valuemax="100" style="width: 0%; background-color: blue">
				 <span></span>
  		</div>
  		<br/>
	</div>
	<br>
	<br>
	<br>
	<br>
	<script>
	<!--
		function HF_openSherwin() {
			var popupWin = window
					.open(
							"http://www.sherwin-williams.com",
							"Sherwin",
							"resizable=yes,toolbar=yes,menubar=yes,statusbar=yes,directories=no,location=yes,scrollbars=yes,width=800,height=600,left=10,top=10");
			popupWin.focus();
		}
		function HF_openLegal() {
			var popupWin = window
					.open(
							"http://www.sherwin-williams.com/terms/",
							"legal",
							"resizable=no,toolbar=no,menubar=yes,statusbar=no,directories=no,location=no,scrollbars=yes,width=800,height=600,left=10,top=10");
			popupWin.focus();
		}
		function HF_openPrivacy() {
			var popupWin = window
					.open(
							"http://privacy.sherwin-williams.com/",
							"privacy",
							"resizable=yes,toolbar=no,menubar=yes,statusbar=no,directories=no,location=no,scrollbars=yes,width=640,height=480,left=10,top=10");
			popupWin.focus();
		}
		//-->
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
			
			if(qtyOrdered != 0) {
				$("#qtyOrderedTextField").val(qtyOrdered);
				//console.log("QTY ORDER TEXTFIELD VALUE = " + $("#qtyOrderedTextField").val());
				//if (remaining == 0 ) {
				//	$("#qtyOrderedTextField").prop('disabled', true);
				//}
			} else {
				$("#qtyOrderedTextField").val("");
			}
			
			$("#qtyRemaining").text(remaining);
			
			//init comms to device handler for tinter
			if ($("#formulaUserPrintAction_sessionHasTinter").val() == "true") {
				ws_tinter = new WSWrapper("tinter");
			}
			if ($("#formulaUserPrintAction_siteHasPrinter").val() == "true") {
				getPrinterConfig();
			}
			// init which buttons user can see
			
			updateButtonDisplay();
			
			//Enable enter key to print
			$("#qtyOrderedTextField").keypress(function(event){
			    var keycode = (event.keyCode ? event.keyCode : event.which);
			    if(keycode == '13'){
				    event.preventDefault();
					if( $('#formulaUserPrintAction_sessionHasTinter').val() === 'true' && $('#formulaUserPrintAction_accountIsDrawdownCenter').val() === 'true' ) {
						$('#dispenseSampleButton').click();
					} else if ( $('#formulaUserPrintAction_sessionHasTinter').val() === 'false' && $('#formulaUserPrintAction_accountIsDrawdownCenter').val() === 'true') {
						$('#drawdownSaveButton').click();
					} else if ($('#formulaUserPrintAction_sessionHasTinter').val() === 'true' && $('#formulaUserPrintAction_accountIsDrawdownCenter').val() === 'false' ) {
						$('#formulaDispense').click();
					} else {
						$('#formulaUserPrintAction_formulaUserSaveAction').click();
					}
			    }
			});
			
			
			//Enable enter key to print.  No need for mouse click
			$("#printLabelPrint").keypress(function(event){
			    var keycode = (event.keyCode ? event.keyCode : event.which);
			    if(keycode == '13'){
					if (qtyOrdered != 0){
					    // print button is visible
						 printButtonClickGetJson();
					}
			       
			    }
			});
			
			$('#printLabelModal').on('shown.bs.modal', function () {
				$("#printLabelPrint").focus();
			}); 
			
			$('#colorNotes').on('click', function () {
				// they are editing notes; display save button if it isn't already
				$("#formulaUserPrintAction_recDirty").val(1);
				updateButtonDisplay();
			}); 

			//print with/without formula logic
			$('#labelWithFormula').on('click', function() {
				var isPrintWithLabelChecked = $('#labelWithFormula').is(":checked");
				if(isPrintWithLabelChecked === true) {
					 if ($('#formulaUserPrintAction_accountIsSwStore').val() === 'true') {
						printStoreLabel(true);
					} else {
						printSelfTintCustLabel(true);
					}
				} else {
					if ($('#formulaUserPrintAction_accountIsSwStore').val() === 'true') {
						printStoreLabel(false);
					} else {
						printSelfTintCustLabel(false);
					}
				}
				
			});
			
			//Make sure the label with formula checkbox is 'checked' when open the print modal is opened
			$('#printLabelClose').on('click', function() {
				var isPrintWithLabelChecked = $('#labelWithFormula').is(":checked");
				if(isPrintWithLabelChecked === false) {
					$('#labelWithFormula').prop('checked', true);
				}
			});
			
			//Ensure that the label with formula checkbox is not displayed in the print modal for drawdowns.
			$('#drawdownLabelPrint').on('click', function() {
				$('#labelWithFormula').css("visibility", "hidden");
				$('#labelWithFormulaLabel').css("visibility", "hidden");
			});
			
			$('#storeLabelPrint').on('click', function() {
				$('#labelWithFormula').css("visibility", "hidden");
				$('#labelWithFormulaLabel').css("visibility", "hidden");
			});
			
			//Check if tinter is present. If so, make sure the dispense button has a primary color.
			if( $('#formulaUserPrintAction_sessionHasTinter').val() === 'true' && $('#formulaUserPrintAction_accountIsDrawdownCenter').val() === 'true' ) {
				$('#dispenseSampleButton').attr('class', 'btn btn-primary');
				$('#drawdownSaveButton').attr('class', 'btn btn-secondary');
			} else if ( $('#formulaUserPrintAction_sessionHasTinter').val() === 'false' && $('#formulaUserPrintAction_accountIsDrawdownCenter').val() === 'true' ) {
				$('#dispenseSampleButton').attr('class', 'btn btn-secondary');
				$('#drawdownSaveButton').attr('class', 'btn btn-primary');
			} else if ( $('#formulaUserPrintAction_sessionHasTinter').val() === 'true' && $('#formulaUserPrintAction_accountIsDrawdownCenter').val() === 'false' ) {
				$('#formulaDispense').attr('class', 'btn btn-primary');
				$('#formulaUserPrintAction_formulaUserSaveAction').attr('class', 'btn btn-secondary');
			} else {
				$('#formulaDispense').attr('class', 'btn btn-secondary');
				$('#formulaUserPrintAction_formulaUserSaveAction').attr('class', 'btn btn-primary');
			}
			
			//Verify contents of Room-By-Room when tabbing off of field.
			$('#roomsList').on('keydown', function(event) {
				if(event.which === 9) {
					event.preventDefault();
					var roomByRoomValue = $('#roomsList option:selected').text().trim();
					if(roomByRoomValue === '') {
						$('#roomsDropdownErrorText').attr('class', '');
						$("#roomsList").focus();
					} else {
						$('#roomsDropdownErrorText').attr('class', 'd-none');
						if($('#formulaUserPrintAction_accountIsDrawdownCenter').val() === 'true' && $('#formulaUserPrintAction_sessionHasTinter').val() === 'true') {
							$('#canTypesList').focus();
						} else {
							$('#qtyOrderedTextField').focus();
						}
					}
				}
			});
			
			//Scroll screen so that the color company label is at the top for drawdowns.
			if($('#formulaUserPrintAction_accountIsDrawdownCenter').val() === 'true') {
				$('html, body').animate({
					scrollTop: $("#colorCompLabel").offset().top
				}, 750);
			}
			
			//TODO if in correction, go to correction screen.
		});

		function updateButtonDisplay() {
			// update button display only if account is customer, not drawdown center
			if ($("#formulaUserPrintAction_accountIsDrawdownCenter").val() == "false"){
				// hide dispense button unless conditions for dispense are met
				$("#formulaDispense").hide();
				// make Save primary unless dispense is available
				makeSavePrimary();
				//console.log("is package color? " + $("#isPackageColor").val());
				//console.log("is package color tintable? " + $("#isTintable").val());
				// check if color/product is package color and is tintable
				if($("#isPackageColor").val() == "true" && $("#isTintable").val() == "false"){
					console.log("package color is not tintable");
					$("#formulaUserPrintAction_formulaUserCorrectAction").hide();
					$("#formulaUserPrintAction_formulaUserEditAction").hide();
					// hide dispense info row and ingredients table as well
					$("#dispenseInfoRow").hide();
					$("#ingredients_table").hide();
				}
				var btnCount = 2; //Next Job and Print always shown so start at 2
				if ($("#formulaUserPrintAction_midCorrection").val() == "true") {
					$("#formulaUserPrintAction_formulaUserCorrectAction").show();
					
					$("#formulaUserPrintAction_formulaUserSaveAction").hide();
					$("#formulaPrint").hide(); //print button
					$("#formulaUserPrintAction_formulaUserEditAction").hide();
					$("#formulaUserPrintAction_displayJobFieldUpdateAction").hide(); // copy button
					btnCount += 1;
				} else {
					if ($("#formulaUserPrintAction_sessionHasTinter").val() == "true"
							&& $("#formulaUserPrintAction_tinterClrntSysId").val() == $(
									"#formulaUserPrintAction_formulaClrntSysId").val()) {
						console.log("button on/off");
						console.log("hasTinter is true");
						// Has a tinter at this station
						// Show Dispense button and make it Primary
						// first check if product is package color
						if($("#isPackageColor").val() == "true"){
							//console.log("ingredients table has data? " + $("#ingredients_table tbody td").is(":visible"));
							// check if any colorant has been added for dispense
							if($("#ingredients_table tbody td").is(":visible")){
								console.log("formula is available for dispense");
								$("#formulaDispense").show();
								btnCount += 1;
								makeDispensePrimary();
							} else {
								console.log("no formula to dispense");
								$("#formulaDispense").hide();
							}
						} else {
							//console.log("not a package color");
							$("#formulaDispense").show();
							btnCount += 1;
							makeDispensePrimary();
						}
						// has it been dispensed?
						var myint = parseInt($.trim($("#qtyDispensed").text()));
						if (isNaN(myint))
							myint = 0;
						if (myint > 0) {
							console.log("qDisped is not zero");
							console.log(">>" + $.trim($("#qtyDispensed").text()) + "<<");
							// has been dispensed hide Save and Edit, show Copy and Correct
							$("#formulaUserPrintAction_formulaUserSaveAction").hide();
							$("#formulaUserPrintAction_formulaUserEditAction").hide();
							$("#formulaUserPrintAction_displayJobFieldUpdateAction").show();
							btnCount += 1;
							// only show correct if product is not package color
							if($("#isPackageColor").val() == "false"){
								$("#formulaUserPrintAction_formulaUserCorrectAction").show();
								btnCount += 1;
							}
							console.log("Value of Dirty is");
							console.log(">>" + $.trim($("#formulaUserPrintAction_recDirty").val()) + "<<"); 
						} else {
							console.log("qDisped is zero");
							console.log(">>" + $.trim($("#qtyDispensed").text()) + "<<");
							// check if color/product is package color and is tintable
							if($("#isPackageColor").val() == "true" && $("#isTintable").val() == "false"){
								console.log("package color cannot be edited");
							} else {
								// Has not been dispensed, never show Correct always show Edit
								$("#formulaUserPrintAction_formulaUserCorrectAction").hide();
								$("#formulaUserPrintAction_formulaUserEditAction").show();
								btnCount += 1;
							}
							var myint2 = parseInt($.trim($("#controlNbr").text()));
							if (isNaN(myint2))
								myint2 = 0;
							if (myint2 == 0) {
								console.log("ctlNbr is zero");
								// has not been saved, hide Copy
								$("#formulaUserPrintAction_displayJobFieldUpdateAction").hide();
								$("#formulaUserPrintAction_formulaUserSaveAction").show();
								btnCount += 1;
							} else {
								console.log("ctlNbr is not zero");
								// has been saved show Copy
								$("#formulaUserPrintAction_displayJobFieldUpdateAction").show();
								btnCount += 1;
								//We can also hide the Save button if the record is not dirty
								var myint3 = parseInt($.trim($("#formulaUserPrintAction_recDirty").val()));
								if (isNaN(myint3))
									myint3 = 0;
								if (myint3 > 0) {
									console.log("dirty is true");
									console.log(">>" + $.trim($("#formulaUserPrintAction_recDirty").val()) + "<<");
									$("#formulaUserPrintAction_formulaUserSaveAction").show();
									btnCount += 1;
								} else {
									console.log("dirty is false");
									console.log(">>" + $.trim($("#formulaUserPrintAction_recDirty").val()) + "<<");
									$("#formulaUserPrintAction_formulaUserSaveAction").hide();
								} // end else not dirty
							} // end else saved 
						} //end else not dispensed
					} else {
						console.log("button on/off");
						console.log("hasTinter is false");
	
						// if dispensed (could have been done at another station)
						var myint = parseInt($.trim($("#qtyDispensed").text()));
						if (isNaN(myint))
							myint = 0;
						if (myint > 0) {
							// has been dispensed hide Save and Edit, show Copy
							$("#formulaUserPrintAction_formulaUserSaveAction").hide();
							$("#formulaUserPrintAction_formulaUserEditAction").hide();
							$("#formulaUserPrintAction_displayJobFieldUpdateAction").show();
							btnCount += 1;
							// only show correct if product is not package color
							if($("#isPackageColor").val() == "false"){
								$("#formulaUserPrintAction_formulaUserCorrectAction").show();
								btnCount += 1;
							}
							// make Print primary
							makePrintPrimary()
						} else {
							$("#formulaUserPrintAction_formulaUserCorrectAction").hide();
							// Has not been dispensed, always show Edit
							// unless product is package color and cannot be tinted
							if($("#isPackageColor").val() == "true" && $("#isTintable").val() == "false"){
								console.log("package color cannot be tinted");
								//$("#formulaUserPrintAction_formulaUserEditAction").hide();
							} else {
								$("#formulaUserPrintAction_formulaUserEditAction").show();
								btnCount += 1;
							}
							var myint2 = parseInt($.trim($("#controlNbr").text()));
							if (isNaN(myint2))
								myint2 = 0;
							if (myint2 == 0) {
								// has not been saved, hide Copy
								$("#formulaUserPrintAction_displayJobFieldUpdateAction").hide();
								$("#formulaUserPrintAction_formulaUserSaveAction").show();
								btnCount += 1;
							} else {
								// has been saved show Copy
								$("#formulaUserPrintAction_displayJobFieldUpdateAction").show();
								btnCount += 1;
								// has been saved, we can hide the Save button if the record is not dirty
								var myint3 = parseInt($.trim($("#formulaUserPrintAction_recDirty").val()));
								if (isNaN(myint3))
									myint3 = 0;
								if (myint3 > 0) {
									$("#formulaUserPrintAction_formulaUserSaveAction").show();
									btnCount += 1;
									// make Save primary
									makeSavePrimary()
								} else {
									$("#formulaUserPrintAction_formulaUserSaveAction").hide();
									// make Print primary
									makePrintPrimary()
								} // end else (not dirty)
							} // end else (saved) 
						} // end else (not dispensed)
					} // end else (no tinter)
				}
				console.log("button count is " + btnCount);
				// adjust margin-left on all buttons based on number of buttons shown
				/* var pct = '5%';
				if(btnCount===6) pct = '1%';
				else if(btnCount===5) pct = '2%';
				else if(btnCount===4) pct='3%';
				else if(btnCount===3) pct = '10%';
				else if(btnCount===2) pct = '10%';
				if(!$("#formulaDispense").hasClass("pull-left")) $("#formulaDispense").css('margin-left',pct);
				if(!$("#formulaUserPrintAction_formulaUserSaveAction").hasClass("pull-left")) $("#formulaUserPrintAction_formulaUserSaveAction").css('margin-left',pct);
				if(!$("#formulaPrint").hasClass("pull-left")) $("#formulaPrint").css('margin-left',pct);
				$("#formulaUserPrintAction_formulaUserEditAction").css('margin-left',pct);
				$("#formulaUserPrintAction_formulaUserCorrectAction").css('margin-left',pct);
				$("#formulaUserPrintAction_displayJobFieldUpdateAction").css('margin-left',pct); */
			// account is drawdown center
			} else {
				// check if color/product is package color and is tintable
				//console.log("is package color? " + $("#isPackageColor").val());
				//console.log("is package color tintable? " + $("#isTintable").val());
				if($("#isPackageColor").val() == "true" && $("#isTintable").val() == "false"){
					console.log("package color is not tintable");
					$("#formulaUserPrintAction_formulaUserEditAction").hide();
					// hide dispense info row and ingredients table as well
					$("#dispenseInfoRow").hide();
					$("#decimalOuncesTable").hide();
					$("#ingredients_table").hide();
					$("#dispenseSampleButton").hide();
				} else {
					// session doesn't have tinter or it's an incompatible colorant system for formula, so hide dispense button
					if ($("#formulaUserPrintAction_sessionHasTinter").val() != "true" ||
							$("#formulaUserPrintAction_tinterClrntSysId").val() != $("#formulaUserPrintAction_formulaClrntSysId").val()) {
						$("#dispenseSampleButton").hide();
					} else {
						// session does have tinter
						// check if color/product is package color
						if($("#isPackageColor").val() == "true"){
							if($("#ingredients_table tbody td").is(":visible") || $("#decimalOuncesTable tbody td").is(":visible")){
								console.log("formula is available for dispense");
								$("#dispenseSampleButton").show();
							} else {
								console.log("no formula to dispense");
								$("#dispenseSampleButton").hide();
							}
						} 
					}
				}
			}
			// update button display regardless of user account type 
			var qtyDispensed = parseInt($.trim($("#qtyDispensed").text()));
			if (isNaN(qtyDispensed)){
				qtyDispensed = 0;
			}
			// don't let user change the product if the job has been dispensed 
			if (qtyDispensed > 0) {
				$("#changeProductBtn").hide();
			}
		}

		function makeDispensePrimary() {
			// Dispense button
			/* if(!$("#formulaDispense").hasClass("pull-left")) $("#formulaDispense").addClass("pull-left"); */
			if ($("#formulaDispense").hasClass(""))
				$("#formulaDispense").removeClass("");
			if ($("#formulaDispense").hasClass("btn-secondary"))
				$("#formulaDispense").removeClass("btn-secondary");
			if (!$("#formulaDispense").hasClass("btn-primary"))
				$("#formulaDispense").addClass("btn-primary");
			// Save button
			/* if($("#formulaUserPrintAction_formulaUserSaveAction").hasClass("pull-left")) $("#formulaUserPrintAction_formulaUserSaveAction").removeClass("pull-left"); */
			if ($("#formulaUserPrintAction_formulaUserSaveAction").hasClass(
					"btn-primary"))
				$("#formulaUserPrintAction_formulaUserSaveAction").removeClass(
						"btn-primary");
			if (!$("#formulaUserPrintAction_formulaUserSaveAction")
					.hasClass(""))
				$("#formulaUserPrintAction_formulaUserSaveAction").addClass("");
			if (!$("#formulaUserPrintAction_formulaUserSaveAction").hasClass(
					"btn-secondary"))
				$("#formulaUserPrintAction_formulaUserSaveAction").addClass(
						"btn-secondary");
			// Print button
			/* if($("#formulaPrint").hasClass("pull-left")) $("#formulaPrint").removeClass("pull-left"); */
			if ($("#formulaPrint").hasClass("btn-primary"))
				$("#formulaPrint").removeClass("btn-primary");
			if (!$("#formulaPrint").hasClass(""))
				$("#formulaPrint").addClass("");
			if (!$("#formulaPrint").hasClass("btn-secondary"))
				$("#formulaPrint").addClass("btn-secondary");
		}

		function makeSavePrimary() {
			// Dispense button
			/* if($("#formulaDispense").hasClass("pull-left")) $("#formulaDispense").removeClass("pull-left"); */
			if ($("#formulaDispense").hasClass("btn-primary"))
				$("#formulaDispense").removeClass("btn-primary");
			if (!$("#formulaDispense").hasClass("btn-secondary"))
				$("#formulaDispense").addClass("btn-secondary");
			if (!$("#formulaDispense").hasClass(""))
				$("#formulaDispense").addClass("");
			// Save button
			/* if(!$("#formulaUserPrintAction_formulaUserSaveAction").hasClass("pull-left")) $("#formulaUserPrintAction_formulaUserSaveAction").addClass("pull-left"); */
			if ($("#formulaUserPrintAction_formulaUserSaveAction").hasClass(""))
				$("#formulaUserPrintAction_formulaUserSaveAction").removeClass(
						"");
			if (!$("#formulaUserPrintAction_formulaUserSaveAction").hasClass(
					"btn-primary"))
				$("#formulaUserPrintAction_formulaUserSaveAction").addClass(
						"btn-primary");
			if ($("#formulaUserPrintAction_formulaUserSaveAction").hasClass(
					"btn-secondary"))
				$("#formulaUserPrintAction_formulaUserSaveAction").removeClass(
						"btn-secondary");
			// Print button
			/* if($("#formulaPrint").hasClass("pull-left")) $("#formulaPrint").removeClass("pull-left"); */
			if ($("#formulaPrint").hasClass("btn-primary"))
				$("#formulaPrint").removeClass("btn-primary");
			if (!$("#formulaPrint").hasClass(""))
				$("#formulaPrint").addClass("");
			if (!$("#formulaPrint").hasClass("btn-secondary"))
				$("#formulaPrint").addClass("btn-secondary");
		}

		function makePrintPrimary() {
			// Dispense button
			/* if($("#formulaDispense").hasClass("pull-left")) $("#formulaDispense").removeClass("pull-left"); */
			if ($("#formulaDispense").hasClass("btn-primary"))
				$("#formulaDispense").removeClass("btn-primary");
			if (!$("#formulaDispense").hasClass("btn-secondary"))
				$("#formulaDispense").addClass("btn-secondary");
			if (!$("#formulaDispense").hasClass(""))
				$("#formulaDispense").addClass("");
			// Save button
			/* if($("#formulaUserPrintAction_formulaUserSaveAction").hasClass("pull-left")) $("#formulaUserPrintAction_formulaUserSaveAction").removeClass("pull-left"); */
			if ($("#formulaUserPrintAction_formulaUserSaveAction").hasClass(
					"btn-primary"))
				$("#formulaUserPrintAction_formulaUserSaveAction").removeClass(
						"btn-primary");
			if (!$("#formulaUserPrintAction_formulaUserSaveAction").hasClass(
					"btn-secondary"))
				$("#formulaUserPrintAction_formulaUserSaveAction").addClass(
						"btn-secondary");
			if (!$("#formulaUserPrintAction_formulaUserSaveAction")
					.hasClass(""))
				$("#formulaUserPrintAction_formulaUserSaveAction").addClass("");
			// Print button
			/* if(!$("#formulaPrint").hasClass("pull-left")) $("#formulaPrint").addClass("pull-left"); */
			if ($("#formulaPrint").hasClass(""))
				$("#formulaPrint").removeClass("");
			if (!$("#formulaPrint").hasClass("btn-primary"))
				$("#formulaPrint").addClass("btn-primary");
			if ($("#formulaPrint").hasClass("btn-secondary"))
				$("#formulaPrint").removeClass("btn-secondary");
		}
		
		function promptToSave(){
	        var myDirty = parseInt($.trim($("#formulaUserPrintAction_recDirty").val()));
	        var myCtlNbr = parseInt($.trim($("#controlNbr").text()));
	        if (isNaN(myDirty)){
	            myDirty = 0;
	        }
	       
	        if (myDirty == 0 && myCtlNbr != 0) {
	            console.log("dirty is false or job has been saved yet");
	            return true;
	        }
	        else
	       {
	            console.log("dirty is true or job has not been saved yet");
	            $("#promptToSaveModal").modal('show');
	            return false;
	        }
	    }
		
	</script>
	<!-- Including footer -->
	<s:include value="Footer.jsp"></s:include>
</body>
</html>