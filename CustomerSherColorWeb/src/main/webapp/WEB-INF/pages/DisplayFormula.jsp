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
<link rel=StyleSheet href="css/bootstrap.min.css" type="text/css">
<link rel=StyleSheet href="css/bootstrapxtra.css" type="text/css">
<link rel=StyleSheet href="js/smoothness/jquery-ui.css" type="text/css">
<link rel=StyleSheet href="css/CustomerSherColorWeb.css" type="text/css">
<link
	href="https://maxcdn.bootstrapcdn.com/font-awesome/4.7.0/css/font-awesome.min.css"
	rel="stylesheet">
<script src="https://code.jquery.com/jquery-3.4.1.min.js"></script>
<script type="text/javascript" charset="utf-8" src="js/jquery-ui.js"></script>
<script type="text/javascript" charset="utf-8" src="js/bootstrap.min.js"></script>
<script type="text/javascript" charset="utf-8" src="js/moment.min.js"></script>
<script type="text/javascript" charset="utf-8"
	src="script/customershercolorweb-1.4.6.js"></script>
<script type="text/javascript" charset="utf-8" src="script/WSWrapper.js"></script>
<script type="text/javascript" charset="utf-8" src="script/printer-1.4.6.js"></script>
<script type="text/javascript" charset="utf-8" src="script/tinter-1.4.6.js"></script>
<script type="text/javascript" charset="utf-8" src="script/dispense-1.4.6.js"></script>
<s:set var="thisGuid" value="reqGuid" />
<style>
.sw-bg-main {
	background-color: ${sessionScope[thisGuid].rgbHex};
}
#dispenseQuantityInputError {
	font-weight: bold;
	color: red;
}

#verifyScanInputError {
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
	var dispenseTracker = '<s:text name="displayFormula.contOutOfTotal"><s:param>' + numberOfDispenses + '</s:param><s:param>' + dispenseQuantity + '</s:param></s:text>';
	var printerConfig;
	var processingDispense = false;
	var sendingTinterCommand = "false";
	 var ws_tinter = new WSWrapper("tinter");
	 var tinterErrorList;
	 var _rgbArr = [];
	 var formSubmitting = false;
	 
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

function setLabelPrintEmbedContainer(labelType,orientation) {
	var embedString = '<embed src="formulaUserPrintAction.action?reqGuid=<s:property value="reqGuid" escapeHtml="true"/>&printLabelType=' + labelType + '&printOrientation=' + orientation + '" frameborder="0" class="embed-responsive-item">';
	$("#printLabelEmbedContainer").html(embedString);
}

function printStoreLabel() {
	
	myPrintLabelType = "storeLabel";
	myPrintOrientation = "PORTRAIT";
	setLabelPrintEmbedContainer(myPrintLabelType,myPrintOrientation);
	prePrintSave(myPrintLabelType,myPrintOrientation);
}

function printDrawdownStoreLabel() {
	myPrintLabelType = "drawdownStoreLabel";
	myPrintOrientation = "PORTRAIT";
	setLabelPrintEmbedContainer(myPrintLabelType,myPrintOrientation);
	prePrintSave(myPrintLabelType,myPrintOrientation);
}

function printDrawdownLabel() {
	myPrintLabelType = "drawdownLabel";
	myPrintOrientation = "LANDSCAPE";
	setLabelPrintEmbedContainer(myPrintLabelType,myPrintOrientation);
	prePrintSave(myPrintLabelType,myPrintOrientation);
}

function prePrintSave(labelType, orientation) {
	// check whether room dropdown needs to be set first
	if (verifyRoomSelected() == true){
		// save before print
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
						url : "saveOnPrintAction.action",
						type : "POST",
						data : {
							reqGuid : myGuid,
							jsDateString : curDate.toString()
						},
						datatype : "json",
						async : true,
						success : function(data) {
							if (data.sessionStatus === "expired") {
								window.location = "/CustomerSherColorWeb/invalidLoginAction.action";
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

function printOnDispenseGetJson() {
	if (printerConfig && printerConfig.model) {
		// Currently only storeLabels can be printed through dispense
		myPrintLabelType = "storeLabel";
		myPrintOrientation = "PORTRAIT";
		var myguid = $("#reqGuid").val();
		var myPdf = new pdf(myguid);
		$("#printerInProgressMessage").text('<s:text name="displayFormula.printerInProgress"/>');
		var numLabels = null;
		numLabels = printerConfig.numLabels;
		print(myPdf, numLabels, myPrintLabelType, myPrintOrientation);
	}

}
function printButtonClickGetJson() {
	if (printerConfig && printerConfig.model) {
		var myguid = $("#reqGuid").val();

		var myPdf = new pdf(myguid);
		$("#printerInProgressMessage").text('<s:text name="displayFormula.printerInProgress"/>');
		var numLabels = null;

		numLabels = printerConfig.numLabels;
		numLabelsVal = $("#numLabels").val();
		if(numLabelsVal && numLabelsVal !=0){
			numLabels = numLabelsVal;
		}
		print(myPdf, numLabels, myPrintLabelType, myPrintOrientation);
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
								window.location = "/CustomerSherColorWeb/invalidLoginAction.action";
							} else {
								$("#controlNbr").text(data.controlNbr);
								$("#controlNbrDisplay").show();
								$("#qtyDispensed").text(data.qtyDispensed);
								updateButtonDisplay();
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
									printOnDispenseGetJson(); //new print on dispense
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
							// verify scan
							if ($("#verifyScanInput").val() !== ""
									&& ($("#verifyScanInput").val() == "${sessionScope[thisGuid].upc}"
											|| $("#verifyScanInput").val() == "${sessionScope[thisGuid].salesNbr}"
											|| $("#verifyScanInput").val()
													.toUpperCase() == "${sessionScope[thisGuid].prodNbr} ${sessionScope[thisGuid].sizeCode}" || $(
											"#verifyScanInput").val()
											.toUpperCase() == "${sessionScope[thisGuid].prodNbr}-${sessionScope[thisGuid].sizeCode}")) {
								waitForShowAndHide("#verifyModal");

								$("#positionContainerModal").modal('show');
							} else {
								$("#verifyScanInputError").text(
										'<s:text name="displayFormula.productScannedDoesNotMatch"/>');
								$("#verifyScanInput").select();
							}
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
							var quantity = parseInt($("#dispenseQuantityInput")
									.val());

							//$("#dispenseQuantityInput").attr("value",quantity);
							if (quantity > 0 && quantity < 1000) {
								console
										.log("Number of containers to dispense: "
												+ quantity);
								dispenseQuantity = quantity;
								numberOfDispenses = 1;
								waitForShowAndHide("#setDispenseQuantityModal");
								preDispenseCheck();
							} else {
								console
										.log("Invalid input was entered. Input was: "
												+ quantity);
								$("#dispenseQuantityInputError")
										.text(
												'<s:text name="displayFormula.invalidInput"/>');
								$("#dispenseQuantityInput").select();
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
		jQuery(document).on("keydown", fkey); // capture F4
	
</script>
<script type="text/javascript">
//callback stuff
	function setDispenseQuantity() {
		// check that user doesn't need to set rooms dropdown
		if (verifyRoomSelected() == true){
			$("#dispenseQuantityInputError").text("");
			$("#dispenseQuantityInput").val("1");
			$("#dispenseQuantityInput").attr("value", "1");
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
	function validateRoomChoice(){
		$("#roomsDropdownErrorText").addClass("d-none");
		// if user chose Other in room list, show textfield to enter custom name
		var selectedRoom = $("select[id='roomsList'] option:selected").text();
		if (selectedRoom == "Other"){
			$("#otherRoom").removeClass('d-none');
			$("#otherRoom").focus();
		// otherwise save the room choice unless it is the default blank option
		} else if (selectedRoom != ""){
			$("#otherRoom").addClass('d-none');
			// call ajax method to save room choice to session
			saveRoomSelection(selectedRoom);
		}
	}
	
	
	function validateOtherRoomBlur(){
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
			// require room choice if user hasn't already 
			var roomText = $("select[id='roomsList'] option:selected").text();
			if (roomText == null || roomText == ""){
				$("#roomsList").focus();
				$("#roomsDropdownErrorText").removeClass("d-none");
				return false;
			} else {
				return true;
			}
		// skip this validation if user doesn't use room by room option
		} else {
			return true;
		}
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
					window.location = "/CustomerSherColorWeb/invalidLoginAction.action";
				} else {
					//console.log("successfully saved room");
				}
			},
			error : function(err) {
				alert('<s:text name="global.failureColon"/>' + err);
			}
		});
	}
	
	
	$(function() {
		// if account is profiled as room by room and a room choice is in session, show it in dropdown
		var roomByRoomFlag = "${accountUsesRoomByRoom}";
		var userRoomChoice = "${roomByRoom}";
		var optionSelected = $("select[id='roomsList'] option:selected").text();
		
		// account uses room by room, and user already has a room choice stored in session
		if (roomByRoomFlag == "true" && userRoomChoice != null && userRoomChoice != ""){
			// room choice was a customized one; otherwise they match because the dropdown has already been updated
			if (optionSelected != userRoomChoice){
				$("select[id='roomsList']").val('Other');
				$("#otherRoom").removeClass('d-none');
				$("#otherRoom").val(userRoomChoice);
			}
		}
	});
	
	
	/* check that the user has set the room by room dropdown 
	and doesn't have any unsaved changes before leaving
	*/
	function validationWithModal(){
		// check if rooms dropdown is set first, if applicable
		var retVal = verifyRoomSelected();
		// get the return value of the function 
		// that displays the Prompt To Save modal
		if (retVal == true){
			retVal = promptToSave();
		}
		return retVal;
	}
	
	
	/* check that the user has set the room by room dropdown 
	and allow them to leave the page with unsaved changes
	*/
	function validationWithoutModal(){
		// check if rooms dropdown is set first, if applicable
		var retVal = verifyRoomSelected();
		// set the flag which lets user navigate away from 
		// the page without being prompted to save changes
		if (retVal == true){
			setFormSubmitting();
		}
		return retVal;
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
		<div class="col-lg-3 col-md-3 col-sm-4 col-xs-6 mb-1">
			<s:property value="#session[reqGuid].colorName" /><br>
			<div class="chip sw-bg-main mt-1"></div>
			<s:if test="%{#session[reqGuid].closestSwColorId != null && #session[reqGuid].closestSwColorId != ''}">
				<em>
					<s:text name="global.closestSWColorIs">
	 					<s:param><s:property value="#session[reqGuid].closestSwColorId" /></s:param>
						<s:param><s:property value="#session[reqGuid].closestSwColorName" /></s:param>
					</s:text>
				</em>
			</s:if>
		</div>
		<div class="col-lg-5 col-md-5 col-sm-4 col-xs-2"></div>
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
					<s:textfield id="otherRoom" class="d-none" placeholder="%{getText('displayFormula.pleaseSpecifyRoom')}" onblur="validateOtherRoomBlur()"/>
					<s:hidden name="roomChoice" value="" />
					<div id="otherRoomErrorText" style="color:red" class="d-none">
						<s:text name="displayFormula.thisFieldCannotBeBlank"/>
					</div>
				</div>
				<div class="col-lg-5 col-md-2 col-sm-1 col-xs-0"></div>
			</div>
		</s:if>
	<br>
	<div class="row mt-3">
		<div class="col-lg-2 col-md-2 col-sm-1 col-xs-0"></div>
		<div class="col-lg-1 col-md-1 col-sm-2 col-xs-2"></div>
		<div class="col-lg-4 col-md-6 col-sm-7 col-xs-10">
			${sessionScope[thisGuid].displayFormula.sourceDescr}<br>
		</div>
		<div class="col-lg-5 col-md-3 col-sm-2 col-xs-0"></div>
	</div>
	<s:if 
		test="%{
		#session[reqGuid].displayFormula.deltaEWarning == null ||
		#session[reqGuid].displayFormula.deltaEWarning == '' ||
		#session[reqGuid].productChoosenFromDifferentBase == true
		}">
		<s:form action="formulaUserPrintAction" validate="true"
			theme="bootstrap">
			<div class="row">
				<div class="col-lg-2 col-md-2 col-sm-1 col-xs-0">
					<s:hidden name="reqGuid" value="%{reqGuid}"  id="reqGuid"/>
					<s:hidden name="jsDateString" value="" />
					<s:hidden name="siteHasTinter" value="%{siteHasTinter}" />
					<s:hidden name="siteHasPrinter" value="%{siteHasPrinter}" />
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
				</div>
				<div class="col-lg-4 col-md-6 col-sm-6 col-xs-12">
					<s:if test="hasActionMessages()">
						<s:actionmessage />
					</s:if>
				</div>
				<div class="col-lg-6 col-md-4 col-sm-5 col-xs-0"></div>
			</div>
			<br>
			<div class="row">
				<div class="col-lg-2 col-md-2 col-sm-1 col-xs-0"></div>
				<div class="col-lg-4 col-md-6 col-sm-6 col-xs-12">
					<table class="table">
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
				</div>
			</div>
			<div class="col-lg-6 col-md-4 col-sm-5 col-xs-0"></div>
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
			<s:if test = "%{accountIsDrawdownCenter==true}">
			<!-- validation methods: validationWithModal() checks that the dropdown has been set, if applicable, and shows the 
			Prompt To Save modal if user has unsaved changes. validationWithoutModal() checks the dropdown and lets the user nav
			away from the page without modal prompt. verifyRoomSelected() checks dropdown and browser shows unsaved changes dialog box. 
			Use either validationWithoutModal or verifyRoomSelected since the Prompt to Save modal is only set up for Next Job action. -->
				<div class="d-flex flex-row justify-content-around mt-3">
					<div class="col-lg-2 col-md-2 col-sm-1 col-xs-0 p-2"></div>
					<div class="col-lg-6 col-md-8 col-sm-10 col-xs-12 p-2">
						<!-- add actions to go to sample dispense page and to save drawdown center job -->
						<s:submit cssClass="btn btn-primary" autofocus="autofocus" id="dispenseSampleButton" value="%{getText('global.dispenseSample')}"
							onclick="return validationWithoutModal();" action="displaySampleDispenseAction" />
						<s:submit cssClass="btn btn-secondary" value="%{getText('global.save')}" 
							onclick="return validationWithoutModal();" action="" />
						<s:submit cssClass="btn btn-secondary" value="%{getText('editFormula.editFormula')}" 
							onclick="return validationWithoutModal();" action="formulaUserEditAction" />
						<s:submit cssClass="btn btn-secondary" value="%{getText('displayFormula.copytoNewJob')}"
							onclick="return verifyRoomSelected();" action="displayJobFieldUpdateAction" />
						<s:submit cssClass="btn btn-secondary pull-right" value="%{getText('displayFormula.nextJob')}"
                        	onclick="return validationWithModal();" action="userCancelAction" />
                	</div>
					<div class="col-lg-4 col-md-2 col-sm-1 col-xs-0 p-2"></div>
				</div>
				<div class="d-flex flex-row justify-content-around mt-2">
					<div class="col-lg-2 col-md-2 col-sm-1 col-xs-0 p-2"></div>
					<div class="col-lg-6 col-md-8 col-sm-10 col-xs-12 p-2">
						<!-- update these onclick attributes with method calls for the label printing 
						and move the verifyRoomSelected() calls to inside those methods and handle conditionally,
						i.e., don't print the label if the verifyRoomSelected returns false -->
						<button type="button" class="btn btn-secondary" id="drawdownLabelPrint"
							onclick="printDrawdownLabel();return false;"><s:text name="global.drawdownLabel"/></button>
						<button type="button" class="btn btn-secondary" id="storeLabelPrint"
							onclick="printDrawdownStoreLabel();return false;"><s:text name="global.storeLabel"/></button>
                	</div>
					<div class="col-lg-4 col-md-2 col-sm-1 col-xs-0 p-2"></div>
				</div>
			</s:if>
			<s:else>
				<div class="d-flex flex-row justify-content-around mt-3">
					<div class="col-lg-2 col-md-2 col-sm-1 col-xs-0 p-2"></div>
					<div class="col-lg-6 col-md-8 col-sm-10 col-xs-12 p-2">
						<button type="button" class="btn btn-primary" id="formulaDispense"
							onclick="setDispenseQuantity()" autofocus="autofocus"><s:text name="global.dispense"/></button>
						<s:submit cssClass="btn btn-secondary" value="%{getText('global.save')}" 
							onclick="return validationWithoutModal();" action="formulaUserSaveAction" autofocus="autofocus" />
						<button type="button" class="btn btn-secondary" id="formulaPrint"
							onclick="printStoreLabel();return false;"><s:text name="global.print"/></button>
						<s:submit cssClass="btn btn-secondary" value="%{getText('editFormula.editFormula')}"
							onclick="return validationWithoutModal();" action="formulaUserEditAction" />
						<s:submit cssClass="btn btn-secondary" value="%{getText('displayFormula.correct')}"
							onclick="return validationWithoutModal();" action="formulaUserCorrectAction" />
						<s:submit cssClass="btn btn-secondary" value="%{getText('displayFormula.copytoNewJob')}"
							onclick="return verifyRoomSelected();" action="displayJobFieldUpdateAction" />
						<s:submit cssClass="btn btn-secondary pull-right" value="%{getText('displayFormula.nextJob')}"
	                        onclick="return validationWithModal();" action="userCancelAction" />
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
									id="dispenseQuantityInputError"></strong>
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
								autofocus="autofocus"> <strong id="verifyScanInputError"></strong>
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
									<p id="abort-message" font-size="4" style="display:none;color:purple;font-weight:bold"> <s:text name="global.pressF4ToAbort"/> </p>
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
						<s:submit cssClass="btn btn-primary" value="%{getText('global.yes')}" onclick="setFormSubmitting();" action="formulaUserSaveAction" autofocus="autofocus" />
						<s:submit cssClass="btn btn-secondary" id="noSaveFormulaBtn" value="%{getText('global.no')}" onclick="setFormSubmitting();" action="userCancelAction"/>
						<s:submit cssClass="btn btn-secondary" id="btnCancel" data-dismiss="modal" value="%{getText('global.cancel')}"/>
					</div>
				</div>
			</div>
		</div>
			
		</s:form>
	</s:if>
	<s:else>
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
			//init comms to device handler for tinter
			if ($("#formulaUserPrintAction_sessionHasTinter").val() == "true") {
				ws_tinter = new WSWrapper("tinter");
			}
			if ($("#formulaUserPrintAction_siteHasPrinter").val() == "true") {
				getPrinterConfig();
			}
			// init which buttons user can see
			updateButtonDisplay();
			
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
			$('#printLabelModal').on('shown.bs.modal', function () {
				$("#printLabelPrint").focus();
			});  

			//TODO if in correction, go to correction screen.
		});

		function updateButtonDisplay() {
			// update button display only if account is customer, not drawdown center
			if ($("#formulaUserPrintAction_accountIsDrawdownCenter").val() == "false"){
				var btnCount = 2; //Next Job and Print always shown so start at 2
				if ($("#formulaUserPrintAction_midCorrection").val() == "true") {
					$("#formulaUserPrintAction_formulaUserCorrectAction").show();
					$("#formulaDispense").hide();
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
						$("#formulaDispense").show();
						btnCount += 1;
						makeDispensePrimary();
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
							$("#formulaUserPrintAction_formulaUserCorrectAction").show();
							console.log("Value of Dirty is");
							console.log(">>" + $.trim($("#formulaUserPrintAction_recDirty").val()) + "<<"); 
							btnCount += 2;
						} else {
							console.log("qDisped is zero");
							console.log(">>" + $.trim($("#qtyDispensed").text()) + "<<");
							// Has not been dispensed, never show Correct always show Edit
							$("#formulaUserPrintAction_formulaUserCorrectAction").hide();
							$("#formulaUserPrintAction_formulaUserEditAction").show();
							btnCount += 1;
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
						// No Tinter, hide dispense and correct button
						$("#formulaDispense").hide();
						$("#formulaUserPrintAction_formulaUserCorrectAction").hide();
						// make Save primary
						makeSavePrimary()
	
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
							// make Print primary
							makePrintPrimary()
						} else {
							// Has not been dispensed, always show Edit
							$("#formulaUserPrintAction_formulaUserEditAction").show();
							btnCount += 1;
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
				// session doesn't have tinter or it's an incompatible colorant system for formula, so hide dispense button
				if ($("#formulaUserPrintAction_sessionHasTinter").val() != "true" ||
						$("#formulaUserPrintAction_tinterClrntSysId").val() != $("#formulaUserPrintAction_formulaClrntSysId").val()) {
					$("#dispenseSampleButton").hide();
				}
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
	        if (isNaN(myDirty)){
	            myDirty = 0;
	        }
	       
	        if (myDirty == 0) {
	            console.log("dirty is false");
	            return true;
	        }
	        else
	       {
	            console.log("dirty is true");
	            $("#promptToSaveModal").modal('show');
	            return false;
	        }
	    }
		
	</script>
	<!-- Including footer -->
	<s:include value="Footer.jsp"></s:include>
</body>
</html>