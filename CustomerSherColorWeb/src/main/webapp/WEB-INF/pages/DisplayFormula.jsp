<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!doctype html>


<%@ taglib prefix="s" uri="/struts-tags"%>

<html lang="en">
<head>
<meta name="viewport" content="width=device-width, initial-scale=1">
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">

<title>Formula</title>
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
	src="script/customershercolorweb-1.4.2.js"></script>
<script type="text/javascript" charset="utf-8" src="script/WSWrapper.js"></script>
<script type="text/javascript" charset="utf-8" src="script/Printer.js?1"></script>
<script type="text/javascript" charset="utf-8" src="script/tinter-1.4.2.js"></script>
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
	var dispenseQuantity = 0;
	var numberOfDispenses = 0;
	var dispenseTracker = "Container: " + numberOfDispenses + " out of "
			+ dispenseQuantity;
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
function prePrintSave() {
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
		var myGuid = $("#formulaUserPrintAction_reqGuid").val();
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
		var myguid = $("#formulaUserPrintAction_reqGuid").val();

		var myPdf = new pdf(myguid);
		$("#printerInProgressMessage").text("Printer: In Progress ");
		var numLabels = null;

		numLabels = printerConfig.numLabels;
		print(myPdf, numLabels);
	}

}
function printButtonClickGetJson() {
	if (printerConfig && printerConfig.model) {
		var myguid = $("#formulaUserPrintAction_reqGuid").val();

		var myPdf = new pdf(myguid);
		$("#printerInProgressMessage").text("Printer: In Progress ");
		var numLabels = null;

		numLabels = printerConfig.numLabels;
		numLabelsVal = $("#numLabels").val();
		if(numLabelsVal && numLabelsVal !=0){
			numLabels = numLabelsVal;
		}
		print(myPdf, numLabels);
	}

}

function printButtonClick() {
	var myValue = $("#formulaUserPrintAction_reqGuid").val();
	console.log("calling print window open for print action with guid "
			+ myValue);
	window.open('formulaUserPrintAction.action?reqGuid=' + myValue,
			'Print Label', 'width=500, height=1000');
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
							"Print Result: " + return_message.errorMessage);
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
							"Get Printer Result: "
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
	//tinter stuff

	    function fkey(e){
	    	if(sendingTinterCommand == "true"){
	        e = e || window.event;
	        
	        if (e.code === 'F4') {
	        	abort();
	            console.log(e);
	            e.preventDefault();
	        }
	    }
	}
	function getRGB(colorantCode){
		var rgb = "";
		if(colorantCode != null){
			rgb = _rgbArr[colorantCode];
		}
		return rgb;
	}
	
	function buildProgressBars(return_message) {
		var count = 1;
		var keys=[];
		$(".progress-wrapper").empty();
		keys = Object.keys(return_message.statusMessages);
		if (keys !=null && keys.length > 0) {
			return_message.statusMessages
					.forEach(function(item) {
						var colorList = item.message.split(" ");
						var color = colorList[0];
						var pct = colorList[1];
						//fix bug where we are done, but not all pumps report as 100%
						if (return_message.errorMessage.indexOf("done") > 1
								&& (return_message.errorNumber == 0 && return_message.status == 0)) {
							pct = "100%";
						}
						//$("#tinterProgressList").append("<li>" + item.message + "</li>");

						var $clone = $("#progress-0")
										.removeClass('d-none')
										.clone();
						$clone.attr("id", "progress-" + count);
						var $bar = $clone.children(".progress-bar");
						$bar.attr("id", "bar-" + count);
						$bar.attr("aria-valuenow", pct);
						$bar.css("width", pct);
						$clone.css("display", "block");
						var color_rgb = getRGB(color);
						//change color of text based on background color
						switch (color) {
						case "WHT":
						case "TW":
						case "W1":
							$bar.children("span").css("color", "black");
							$bar.css("background-color", "#efefef");
							break;
						case "OY":
						case "Y1":
						case "YGS":
							$bar.children("span").css("color", "black");
							$bar.css("background-color", color_rgb);
							break;
						default:
							$bar.css("background-color", color_rgb);
							$bar.children("span").css("color", "white");
							break;
						}

						$bar.children("span").text(color + " " + pct);
						console.log("barring " + item.message);
						//console.log($clone);

						$clone.appendTo(".progress-wrapper");

						count++;
					});
		}
	}
	function FMXDispenseProgress() {
		console.log('before dispense progress send');

		rotateIcon();
		var cmd = "DispenseProgress";
		var shotList = null;
		var configuration = null;
		var tintermessage = new TinterMessage(cmd, null, null, null, null);
		var json = JSON.stringify(tintermessage);
		sendingTinterCommand = "true";
		ws_tinter.send(json);
	}
	function dispense() {

		var cmd = "Dispense";

		var tintermessage = new TinterMessage(cmd, shotList, null, null, null);

		var json = JSON.stringify(tintermessage);
		sendingDispCommand = "true";
		if (ws_tinter != null && ws_tinter.isReady == "false") {
			console
					.log("WSWrapper connection has been closed (timeout is defaulted to 5 minutes). Make a new WSWrapper.");
			ws_tinter = new WSWrapper("tinter");
		}
		$("#dispenseStatus").text("Last Dispense: In Progress ");
		// Send to tinter
		ws_tinter.send(json);
	}
	function dispenseProgressResp(return_message) {

		//$("#progress-message").text(return_message.errorMessage);
		$("#abort-message").show();
		$('#progressok').addClass('d-none'); //hide ok button
		if (return_message.errorMessage.indexOf("done") == -1
				&& (return_message.errorNumber == 1 || return_message.status == 1)) {
			$("#tinterProgressList").empty();
			tinterErrorList = [];
			if(return_message.statusMessages!=null && return_message.statusMessages[0]!=null){
				//keep updating modal with status
				//$("#progress-message").text(return_message.errorMessage);
				if(return_message != null && return_message.statusMessages !=null && return_message.statusMessages.length > 0){
					buildProgressBars(return_message);
				}
				
				} else {
					tinterErrorList.push(return_message.errorMessage);
					$("#tinterProgressList").append("<li>" + return_message.errorMessage + "</li>");
				}

			console.log(return_message);
			setTimeout(function() {
				FMXDispenseProgress();
			}, 500); //send progress request after waiting 200ms.  No need to slam the SWDeviceHandler

		} else if (return_message.errorMessage.indexOf("done") > 0
				|| return_message.errorNumber != 0) {
			if (return_message.errorNumber == 4226) {
				return_message.errorMessage = "Tinter Driver busy.  Please re-initialize tinter and retry command."
			}
			FMXDispenseComplete(return_message);
		}

	}
	function FMXShowTinterErrorModal(myTitle, mySummary, my_return_message) {
		$("#tinterErrorList").empty();
		$("#tinterErrorListModal").modal('show');
		$("#abort-message").hide();
		processingDispense = false; // allow user to start another dispense after tinter error
		startSessionTimeoutTimers();
	    if(my_return_message.statusMessages!=null && my_return_message.statusMessages[0]!=null){
	    	if(my_return_message.statusMessages.length > 0){
	    		buildProgressBars(my_return_message);  // on an abort, for example, we will have a progress update to do.
	    	}
			/*
			my_return_message.errorList.forEach(function(item){
			    $("#tinterErrorList").append( '</li>' + item.message + '</li>');
			});
			 */
		}
		if (my_return_message.errorNumber == 4226) {
			my_return_message.errorMessage = "Tinter Driver busy.  Please re-initialize tinter and retry command."
		}
		$("#tinterErrorList").append(
				'<li class="alert alert-danger">'
						+ my_return_message.errorMessage + '</li>');

		if (myTitle != null)
			$("#tinterErrorListTitle").text(myTitle);
		else
			$("#tinterErrorListTitle").text("Tinter Error");
		if (mySummary != null)
			$("#tinterErrorListSummary").text(mySummary);
		else
			$("#tinterErrorListSummary").text("");

	}
	function showTinterErrorModal(myTitle, mySummary, my_return_message) {
		$("#tinterErrorList").empty();
		if (my_return_message.errorList != null
				&& my_return_message.errorList[0] != null) {
			my_return_message.errorList.forEach(function(item) {
				$("#tinterErrorList").append(
						'<li class="alert alert-danger">' + item.message
								+ '</li>');
			});
		} else {
			$("#tinterErrorList").append(
					'<li class="alert alert-danger">'
							+ my_return_message.errorMessage + '</li>');
		}
		if (myTitle != null)
			$("#tinterErrorListTitle").text(myTitle);
		else
			$("#tinterErrorListTitle").text("Tinter Error");
		if (mySummary != null)
			$("#tinterErrorListSummary").text(mySummary);
		else
			$("#tinterErrorListSummary").text("");
		$("#tinterErrorListModal").modal('show');
	}
	function FMXDispenseComplete(return_message) {
		if(return_message != null && return_message.statusMessages !=null && return_message.statusMessages.length > 0){
			buildProgressBars(return_message);
		}
		$("#abort-message").hide();

		if ((return_message.errorNumber == 0 && return_message.commandRC == 0)
				|| (return_message.errorNumber == -10500 && return_message.commandRC == -10500)) {
			// save a dispense (will bump the counter)

			$("#tinterInProgressDispenseStatus").text("");
			$("#dispenseStatus").text("Last Dispense: Complete ");
			rotateIcon();
			//$('#progressok').removeClass('d-none');
			$('#tinterInProgressTitle').text('Tinter Progress');
			$('#tinterInProgressMessage').text('');
			$("#tinterProgressList").empty();
			tinterErrorList = [];
			$(".progress-wrapper").empty();

			writeDispense(return_message); // will also send tinter event
			waitForShowAndHide("#tinterInProgressModal");
		} else {
			$("#tinterInProgressDispenseStatus").text(
					"Last Dispense: " + return_message.errorMessage);
			$("#dispenseStatus").text(
					"Last Dispense: " + return_message.errorMessage);
			waitForShowAndHide("#tinterInProgressModal");
			console.log('hide done');
			//Show a modal with error message to make sure the user is forced to read it.
			FMXShowTinterErrorModal("Dispense Error", null, return_message);
		}
		sendingTinterCommand = "false";
	}
		
	function writeDispense(myReturnMessage) {
		var myValue = $("#formulaUserPrintAction_reqGuid").val();
		var curDate = new Date();
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
										"#formulaUserPrintAction_reqGuid")
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

								if (numberOfDispenses != dispenseQuantity) {
									numberOfDispenses++;
									console
											.log("Dispense Complete: Going to the next container.");
									preDispenseCheck();
								}
							}
						});
	}
	function abort() {
		console.log('before abort');

		var cmd = "Abort";
		var shotList = null;
		var configuration = null;
		var tintermessage = new TinterMessage(cmd, null, null, null, null);
		var json = JSON.stringify(tintermessage);

		ws_tinter.send(json);
	}
	function RecdMessage() {
		var printMessageParsed = false;
		console.log("Received Message");
		//parse the spectro

		if (ws_printer) {
			printMessageParsed = ParsePrintMessage();
		}
		if (!printMessageParsed && typeof ws_tinter !== 'undefined'
				&& ws_tinter) {
			if (ws_tinter.wserrormsg != null && ws_tinter.wserrormsg != "") {
				console.log("isReady is " + ws_tinter.isReady + "BTW");
				if (sendingDispCommand == "true") {
					// received an error from WSWrapper so we won't get any JSON result
					// Since we are sending a dispense command, show as dispense error
					$("#dispenseStatus").text(
							"Last Dispense: " + ws_tinter.wserrormsg);
					//Show a modal with error message to make sure the user is forced to read it.
					$("#tinterSocketError").text(ws_tinter.wserrormsg);
					waitForShowAndHide("#tinterInProgressModal");
					processingDispense = false; // allow user to start another dispense after socket error
					startSessionTimeoutTimers();
					
					$("#tinterSocketErrorModal").modal('show');

				} else {
					console.log("Received unsolicited error "
							+ ws_tinter.wserrormsg);
					// so far this only happens when SWDeviceHandler is not running and we created a new WSWrapper when 
					// page intially loaded.  For now wait until they do a dispense to show the error (no everybody has a tinter)
				}
			} else {
				// is result (wsmsg) JSON?
				var isTintJSON = false;
				try {
					if (ws_tinter != null && ws_tinter.wsmsg != null) {
						var return_message = JSON.parse(ws_tinter.wsmsg);
						isTintJSON = true;
					}
				} catch (error) {
					console
							.log("Caught error is = "
									+ error
									+ " If response is for printer message, this error trying to parse tinter message is expected.");
					console.log("Message is junk, throw it out");
					//console.log("Junk Message is " + ws_tinter.wsmsg);
				}
				if (isTintJSON) {
					var return_message = JSON.parse(ws_tinter.wsmsg);
					switch (return_message.command) {
					case 'Dispense':
					case 'DispenseProgress':
					case 'Abort':
						var tinterModel = $("#tinterModel").val();
						if (tinterModel != null
								&& tinterModel.startsWith("FM X")) { //only FM X series has purge in progress % done
							dispenseProgressResp(return_message);
						} else if ((return_message.errorNumber == 0 && return_message.commandRC == 0)
								|| (return_message.errorNumber == -10500 && return_message.commandRC == -10500)) {
							// save a dispense (will bump the counter)
							$("#dispenseStatus").text(
									"Last Dispense: Complete ");
							writeDispense(return_message); // will also send tinter event
							waitForShowAndHide("#tinterInProgressModal");
						} else {
							processingDispense = false; // allow user to start another dispense after tinter error
							startSessionTimeoutTimers();
							// send tinter event
							var curDate = new Date();
							var myGuid = $("#formulaUserPrintAction_reqGuid")
									.val();
							var teDetail = new TintEventDetail("ORDER NUMBER",
									$("#controlNbr").text(), 0);
							var tedArray = [ teDetail ];
							sendTinterEvent(myGuid, curDate, return_message,
									tedArray);
							$("#dispenseStatus").text(
									"Last Dispense: "
											+ return_message.errorMessage);
							waitForShowAndHide("#tinterInProgressModal");
							//Show a modal with error message to make sure the user is forced to read it.
							showTinterErrorModal("Dispense Error", null,
									return_message);
						}
						sendingDispCommand = "false";
						break;
					default:
						//Not an response we expected...
						console
								.log("Message from different command is junk, throw it out");
					} // end switch statement
				} else {
					console.log("Message is junk, throw it out");
				}
			}
		}
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
										"Product Scanned does not match order");
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
												"Invalid input: Please enter a number of containers from 1 to 999");
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
										"Dispense In Progress");
								$("#tinterInProgressMessage")
										.text(
												"Please wait while tinter performs the dispense...");

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
		jQuery(document).on("keydown", fkey); // capture F4
	}
	
</script>
<script type="text/javascript">
//callback stuff

	function setDispenseQuantity() {
		$("#dispenseQuantityInputError").text("");
		$("#dispenseQuantityInput").val("1");
		$("#dispenseQuantityInput").attr("value", "1");
		$("#setDispenseQuantityModal").modal('show');
		$("#dispenseQuantityInput").select();
	}

	function preDispenseCheck() {
		$("#tinterInProgressTitle").text("Colorant Level Check In Progress");
		$("#tinterInProgressMessage")
				.text(
						"Please wait while we Check the Colorant Levels for your tinter...");
		$("#tinterInProgressModal").modal('show');
		rotateIcon();
		// Get SessionTinter, this is async ajax call so the rest of the logic is in the callback below
		getSessionTinterInfo($("#formulaUserPrintAction_reqGuid").val(),
				preDispenseCheckCallback);

	}

	function preDispenseCheckCallback() {
		dispenseNumberTracker = "Container: " + numberOfDispenses + " out of "
				+ dispenseQuantity;
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
					'<li class="alert alert-danger">Tinter Purge is Required. Last done on '
							+ moment(dateFromString).format('ddd MMM DD YYYY')
							+ '</li>');
			waitForShowAndHide("#tinterInProgressModal");
			$("#tinterErrorListModal").modal('show');
			$("#tinterErrorListTitle").text("Purge Required");
			$("#tinterErrorListSummary")
					.text(
							"Save your formula and go to the SherColor Home page to perform Tinter Purge. ");

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
				$("#tinterErrorListTitle").text("Colorant Level Too Low");
				$("#tinterErrorListSummary")
						.text(
								"Save your formula, fill your empty canister and go to the SherColor Home page to update Colorant Levels. ");

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
					$("#tinterWarningListTitle").text("Low Colorant Levels");
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
				$("#formulaUserPrintAction_reqGuid").val(), shotList,
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
			<strong>Job Number:</strong>
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
			<strong>Color Company:</strong>
		</div>
		<div class="col-lg-4 col-md-6 col-sm-7 col-xs-8">
			${sessionScope[thisGuid].colorComp}</div>
		<div class="col-lg-4 col-md-2 col-sm-1 col-xs-0"></div>
	</div>
	<div class="row">
		<div class="col-lg-2 col-md-2 col-sm-1 col-xs-0"></div>
		<div class="col-lg-2 col-md-2 col-sm-3 col-xs-4">
			<strong>Color ID:</strong>
		</div>
		<div class="col-lg-4 col-md-6 col-sm-7 col-xs-8">
			<s:property value="#session[reqGuid].colorID" /></div>
		<div class="col-lg-4 col-md-2 col-sm-1 col-xs-0"></div>
	</div>
	<div class="row">
		<div class="col-lg-2 col-md-2 col-sm-1 col-xs-0"></div>
		<div class="col-lg-2 col-md-2 col-sm-3 col-xs-4">
			<strong>Color Name:</strong>
		</div>
		<div class="col-lg-2 col-md-3 col-sm-4 col-xs-6 mb-1">
			<s:property value="#session[reqGuid].colorName" /><br>
			<div class="chip sw-bg-main mt-1"></div>
		</div>
		<div class="col-lg-6 col-md-5 col-sm-4 col-xs-2"></div>
	</div>
	<div class="row">
		<div class="col-lg-2 col-md-2 col-sm-1 col-xs-0"></div>
		<div class="col-lg-2 col-md-2 col-sm-3 col-xs-4">
			<strong>Sales Number:</strong>
		</div>
		<div class="col-lg-4 col-md-6 col-sm-7 col-xs-8">
			${sessionScope[thisGuid].salesNbr}<br>
		</div>
		<div class="col-lg-4 col-md-2 col-sm-1 col-xs-0"></div>
	</div>
	<div class="row">
		<div class="col-lg-2 col-md-2 col-sm-1 col-xs-0"></div>
		<div class="col-lg-2 col-md-2 col-sm-3 col-xs-4">
			<strong>Product Number:</strong>
		</div>
		<div class="col-lg-4 col-md-6 col-sm-7 col-xs-8">
			${sessionScope[thisGuid].prodNbr} -
			${sessionScope[thisGuid].sizeText}</div>
		<div class="col-lg-4 col-md-2 col-sm-1 col-xs-0"></div>
	</div>
	<div class="row">
		<div class="col-lg-2 col-md-2 col-sm-1 col-xs-0"></div>
		<div class="col-lg-2 col-md-2 col-sm-3 col-xs-4">
			<strong>Product Descr:</strong>
		</div>
		<div class="col-lg-4 col-md-6 col-sm-7 col-xs-8">
			${sessionScope[thisGuid].intExt} ${sessionScope[thisGuid].quality}
			${sessionScope[thisGuid].composite} ${sessionScope[thisGuid].finish}<br>
			${sessionScope[thisGuid].base}<br>
		</div>
		<div class="col-lg-4 col-md-2 col-sm-1 col-xs-0"></div>
	</div>
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
		test="%{#session[reqGuid].displayFormula.deltaEWarning == '' || #session[reqGuid].displayFormula.deltaEWarning == null}">
		<s:form action="formulaUserPrintAction" validate="true"
			theme="bootstrap">
			<div class="row">
				<div class="col-lg-2 col-md-2 col-sm-1 col-xs-0">
					<s:hidden name="reqGuid" value="%{reqGuid}" />
					<s:hidden name="jsDateString" value="" />
					<s:hidden name="siteHasTinter" value="%{siteHasTinter}" />
					<s:hidden name="siteHasPrinter" value="%{siteHasPrinter}" />
					<s:hidden name="sessionHasTinter" value="%{sessionHasTinter}" />
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
								<th class="bg-light"><strong>${sessionScope[thisGuid].displayFormula.clrntSysId}*COLORANT</strong></th>
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
						<strong>Qty Dispensed: </strong> <span
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
						<strong>Qty Dispensed: </strong> <span
							class="dispenseInfo d-none badge badge-secondary"
							style="font-size: .8rem;" id="qtyDispensed">${sessionScope[thisGuid].quantityDispensed}</span>
						<strong class="dispenseInfo d-none pull-right" id="dispenseStatus"></strong>
					</div>
					<div class="col-lg-4 col-md-2 col-sm-1 col-xs-0"></div>
				</div>
			</s:else>
			<br>
			<div class="d-flex flex-row justify-content-around mt-3">
				<div class="col-lg-2 col-md-2 col-sm-1 col-xs-0 p-2"></div>
				<div class="col-lg-6 col-md-8 col-sm-10 col-xs-12 p-2">
					<button type="button" class="btn btn-primary" id="formulaDispense"
						onclick="setFormSubmitting; setDispenseQuantity()" autofocus="autofocus">Dispense</button>
					<s:submit cssClass="btn btn-secondary" value="Save" onclick="setFormSubmitting();"
						action="formulaUserSaveAction" autofocus="autofocus" />
					<%-- 								<s:submit cssClass="btn " value="Print" onclick="prePrintSave();return false;" /> --%>
					<button type="button" class="btn btn-secondary" id="formulaPrint"
						onclick="prePrintSave();return false;">Print</button>
					<s:submit cssClass="btn btn-secondary" value="Edit Formula" onclick="setFormSubmitting();"
						action="formulaUserEditAction" />
					<s:submit cssClass="btn btn-secondary" value="Correct" onclick="setFormSubmitting();"
						action="formulaUserCorrectAction" />
					<s:submit cssClass="btn btn-secondary" value="Copy to New Job"
						action="displayJobFieldUpdateAction" />
					<s:submit cssClass="btn btn-secondary pull-right" value="Next Job"
                        action="userCancelAction" onclick="return promptToSave();" />
				</div>
				<div class="col-lg-4 col-md-2 col-sm-1 col-xs-0 p-2"></div>
			</div>
		</div>
		<!-- Including footer -->
		<s:include value="Footer.jsp"></s:include>

			<!-- Set Dispense Quantity Modal Window -->
			<div class="modal" aria-labelledby="setDispenseQuantityModal"
				aria-hidden="true" id="setDispenseQuantityModal" role="dialog">
				<div class="modal-dialog" role="document">
					<div class="modal-content">
						<div class="modal-content">
							<div class="modal-header">
								<h5 class="modal-title">Enter Number of Containers to
									Dispense</h5>
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
									data-dismiss="modal" id="setDispenseQuantityButton">Next</button>
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
							<h5 class="modal-title">Scan Product to Verify Dispense</h5>
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
								data-dismiss="modal" id="verifyButton">Verify</button>
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
							<h5 class="modal-title">Prepare for Dispense</h5>
							<button type="button" class="close" data-dismiss="modal"
								aria-label="Close">
								<span aria-hidden="true">&times;</span>
							</button>
						</div>
						<div class="modal-body">
							<p font-size="4">Position Container and Click Start Dispense
								when Ready</p>
						</div>
						<div class="modal-body">
							<span class="dispenseNumberTracker mx-auto"
								style="background-color: #FF0; font-size: 125%;"></span>
						</div>
						<div class="modal-footer">
							<button type="button" class="btn btn-primary"
								id="startDispenseButton">Start Dispense</button>
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
									<h5 class="modal-title" id="tinterInProgressTitle">Dispense In Progress</h5>
									<button type="button" class="close" data-dismiss="modal" aria-label="Close" ><span aria-hidden="true">&times;</span></button>
								</div>
								<div class="modal-body">
									<p id="tinterInProgressDispenseStatus" font-size="4"></p>
									<p id="tinterInProgressMessage" font-size="4"></p>
									<p id="abort-message" font-size="4" style="display:none;color:purple;font-weight:bold"> Press F4 to abort </p>
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
							<h5 class="modal-title">Dispense Error</h5>
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
								aria-label="Close">Close</button>
						</div>
					</div>
				</div>
			</div>

				<!-- Tinter Error List Modal Window -->
				    <div class="modal fade" aria-labelledby="tinterErrorListModal" aria-hidden="true"  id="tinterErrorListModal" role="dialog">
				    	<div class="modal-dialog">
							<div class="modal-content">
								<div class="modal-header">
									<h5 class="modal-title" id="tinterErrorListTitle">Tinter Error</h5>
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
									<button type="button" class="btn btn-primary" id="tinterErrorListOK" data-dismiss="modal" aria-label="Close" >OK</button>
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
							<h5 class="modal-title" id="tinterWarningListTitle">Tinter
								Error</h5>
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
							<p id="tinterWarningListSummary" font-size="4">Click OK to
								continue or Cancel to return to formula page.</p>
						</div>
						<div class="modal-footer">
							<button type="button" class="btn btn-primary"
								id="tinterWarningListOK" data-dismiss="modal" aria-label="Close">OK</button>
							<button type="button" class="btn btn-secondary"
								id="tinterWarningListCancel" data-dismiss="modal"
								aria-label="Close">Cancel</button>
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
							<h5 class="modal-title" id="printerInProgressTitle">Label
								Printer Error</h5>
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
							<h5 class="modal-title" id="printLabelTitle">Print Label</h5>
							<button type="button" class="close" data-dismiss="modal"
								aria-label="Close">
								<span aria-hidden="true">&times;</span>
							</button>
						</div>
						<div class="modal-body">
							<div class="embed-responsive embed-responsive-1by1">
								<embed
									src="formulaUserPrintAction.action?reqGuid=<s:property value="reqGuid" escapeHtml="true"/>"
									frameborder="0" class="embed-responsive-item">
							</div>

						</div>
						<div class="modal-footer">
							<div class="col-xs-6">
								<button type="button" class="btn btn-primary pull-left"
									id="printLabelPrint" data-dismiss="modal" aria-label="Print"  onclick="printButtonClickGetJson()">Print</button>
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
								id="printLabelClose" data-dismiss="modal" aria-label="Close">Close</button>
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
						<h5 class="modal-title">Continue To Next Job</h5>
						<button type="button" class="close" data-dismiss="modal" aria-label="Close" ><span aria-hidden="true">&times;</span>
						</button>
					</div>
					<div class="modal-body">
						<p id="skipConfirmText" font-size="4">Do you want to save the formula before continuing to the Next Job?</p>
					</div>
					<div class="modal-footer">
						<s:submit cssClass="btn btn-primary" value="Yes" onclick="setFormSubmitting();" action="formulaUserSaveAction" autofocus="autofocus" />
						<s:submit cssClass="btn btn-secondary" id="noSaveFormulaBtn" value="No" onclick="setFormSubmitting();" action="userCancelAction"/>
						<s:submit cssClass="btn btn-secondary" id="btnCancel" data-dismiss="modal" value="Cancel"/>
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
					<s:hidden name="reqGuid" value="%{reqGuid}" />

					<Strong>${sessionScope[thisGuid].displayFormula.deltaEWarning}</Strong>
				</div>
			</div>

			<br>
			<br>
			<div class="row">
				<div class="col-sm-2"></div>
				<div class="col-sm-2">
					<strong>Still Use (Yes/No)?</strong>
				</div>
			</div>
			<br>
			<br>
			<div class="row">

				<div class="col-sm-2"></div>
				<div class="col-sm-2">
					<s:submit cssClass="btn btn-primary" value="No" onclick="setFormSubmitting();"
						action="deltaENoAction" autofocus="autofocus" />
				</div>
				<div class="col-sm-2">
					<s:submit cssClass="btn btn-secondary" value="Yes" onclick="setFormSubmitting();"
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
								"#formulaUserPrintAction_formulaClrntSysId")
								.val()) {
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
						console.log(">>" + $.trim($("#qtyDispensed").text())
								+ "<<");
						// has been dispensed hide Save and Edit, show Copy and Correct
						$("#formulaUserPrintAction_formulaUserSaveAction")
								.hide();
						$("#formulaUserPrintAction_formulaUserEditAction")
								.hide();
						$("#formulaUserPrintAction_displayJobFieldUpdateAction")
								.show();
						$("#formulaUserPrintAction_formulaUserCorrectAction")
								.show();
						console.log("Value of Dirty is");
						console
								.log(">>"
								+ $
												.trim($(
														"#formulaUserPrintAction_recDirty")
														.val()) + "<<"); 
						btnCount += 2;
					} else {
						console.log("qDisped is zero");
						console.log(">>" + $.trim($("#qtyDispensed").text())
								+ "<<");
						// Has not been dispensed, never show Correct always show Edit
						$("#formulaUserPrintAction_formulaUserCorrectAction")
								.hide();
						$("#formulaUserPrintAction_formulaUserEditAction")
								.show();
						btnCount += 1;
						var myint2 = parseInt($.trim($("#controlNbr").text()));
						if (isNaN(myint2))
							myint2 = 0;
						if (myint2 == 0) {
							console.log("ctlNbr is zero");
							// has not been saved, hide Copy
							$(
									"#formulaUserPrintAction_displayJobFieldUpdateAction")
									.hide();
							$("#formulaUserPrintAction_formulaUserSaveAction")
									.show();
							btnCount += 1;
						} else {
							console.log("ctlNbr is not zero");
							// has been saved show Copy
							$(
									"#formulaUserPrintAction_displayJobFieldUpdateAction")
									.show();
							btnCount += 1;
							//We can also hide the Save button if the record is not dirty
							var myint3 = parseInt($.trim($(
									"#formulaUserPrintAction_recDirty").val()));
							if (isNaN(myint3))
								myint3 = 0;
							if (myint3 > 0) {
								console.log("dirty is true");
								console
										.log(">>"
												+ $
														.trim($(
																"#formulaUserPrintAction_recDirty")
																.val()) + "<<");
								$(
										"#formulaUserPrintAction_formulaUserSaveAction")
										.show();
								btnCount += 1;
							} else {
								console.log("dirty is false");
								console
										.log(">>"
												+ $
														.trim($(
																"#formulaUserPrintAction_recDirty")
																.val()) + "<<");
								$(
										"#formulaUserPrintAction_formulaUserSaveAction")
										.hide();
							} // end else not dirty
						} // end else saved 
					} //end else not dispensed
				} else {
					console.log("button on/off");
					console.log("hasTinter is false");
					// No Tinter, hide dispense and correct button
					$("#formulaDispense").hide();
					$("#formulaUserPrintAction_formulaUserCorrectAction")
							.hide();
					// make Save primary
					makeSavePrimary()

					// if dispensed (could have been done at another station)
					var myint = parseInt($.trim($("#qtyDispensed").text()));
					if (isNaN(myint))
						myint = 0;
					if (myint > 0) {
						// has been dispensed hide Save and Edit, show Copy
						$("#formulaUserPrintAction_formulaUserSaveAction")
								.hide();
						$("#formulaUserPrintAction_formulaUserEditAction")
								.hide();
						$("#formulaUserPrintAction_displayJobFieldUpdateAction")
								.show();
						btnCount += 1;
						// make Print primary
						makePrintPrimary()
					} else {
						// Has not been dispensed, always show Edit
						$("#formulaUserPrintAction_formulaUserEditAction")
								.show();
						btnCount += 1;
						var myint2 = parseInt($.trim($("#controlNbr").text()));
						if (isNaN(myint2))
							myint2 = 0;
						if (myint2 == 0) {
							// has not been saved, hide Copy
							$(
									"#formulaUserPrintAction_displayJobFieldUpdateAction")
									.hide();
							$("#formulaUserPrintAction_formulaUserSaveAction")
									.show();
							btnCount += 1;
						} else {
							// has been saved show Copy
							$(
									"#formulaUserPrintAction_displayJobFieldUpdateAction")
									.show();
							btnCount += 1;
							// has been saved, we can hide the Save button if the record is not dirty
							var myint3 = parseInt($.trim($(
									"#formulaUserPrintAction_recDirty").val()));
							if (isNaN(myint3))
								myint3 = 0;
							if (myint3 > 0) {
								$(
										"#formulaUserPrintAction_formulaUserSaveAction")
										.show();
								btnCount += 1;
								// make Save primary
								makeSavePrimary()
							} else {
								$(
										"#formulaUserPrintAction_formulaUserSaveAction")
										.hide();
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
</body>
</html>