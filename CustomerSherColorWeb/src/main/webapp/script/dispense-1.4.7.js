//Global Variables
"use strict";
var sendingTinterCommand = "false";
var ws_tinter = new WSWrapper("tinter");
var tinterErrorList;
var _rgbArr = [];

var shotList = [];
var processingDispense;
$(function(){ // on ready
	//capture F4 key to abort
	jQuery(document).on("keydown",fkey);
	});

function fkey(e) {
	if (sendingTinterCommand == "true") {
		e = e || window.event;

		if (e.code === 'F4') {
			if(processingDispense == true){
				abort();
				console.log(e);
				e.preventDefault();
			}
		}
	}
}
function getRGB(colorantCode) {
	var rgb = "";
	if (colorantCode != null) {
		rgb = _rgbArr[colorantCode];
	}
	return rgb;
}
function buildProgressBars(return_message) {
	var count = 1;
	var keys = [];
	$(".progress-wrapper").empty();
	keys = Object.keys(return_message.statusMessages);
	if (keys != null && keys.length > 0) {
		return_message.statusMessages.forEach(function(item) {
			var colorList = item.message.split(" ");
			var color = colorList[0];
			var pct = colorList[1];
			//fix bug where we are done, but not all pumps report as 100%
			if (return_message.errorMessage.indexOf("done") > 1 && (return_message.errorNumber == 0 &&
				return_message.status == 0)) {
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
			//				change color of text based on background color
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
function FMXAlfaDispenseProgress(tintermessage) {
	console.log('before dispense progress send');
	$('#tinterInProgressMessage').text('');
	rotateIcon();
	var cmd = "DispenseProgress";
	var shotList = null;
	var configuration = null;
	var tinterModel = sessionTinterInfo.model;
	if(tinterModel !=null && ( tinterModel.startsWith("FM X"))){ 

   		var tintermessage = new TinterMessage(cmd,null,null,null,null);  
	}
	else{

		var msgId = tintermessage.msgId;
		var tintermessage = new TinterMessage(cmd,null,null,null,null,msgId);  
}
	var msgId = tintermessage.msgId;
	var tintermessage = new TinterMessage(cmd, null, null, null, null, msgId);
	var json = JSON.stringify(tintermessage);
	sendingTinterCommand = "true";
	ws_tinter.send(json);
}
function dispense() {
	//dispense
	let cmd = "Dispense";
	let tintermessage = new TinterMessage(cmd, shotList, null, null, null);
	let json = JSON.stringify(tintermessage);

	sendingTinterCommand = "true";
	if (ws_tinter && ws_tinter.isReady == "false") {
		console.log("WSWrapper connection has been closed (timeout is defaulted to 5 minutes). Make a new WSWrapper.");
		ws_tinter = new WSWrapper("tinter");
	}
	$("#dispenseStatus").text(i18n['global.lastDispenseInProgress']);
	rotateIcon();
	// Send to tinter
	ws_tinter.send(json);
}
function alfaDispenseProgressResp(return_message) {
	$("#abort-message").show();
	$('#progressok').addClass('d-none');  //hide ok button
	if (return_message.errorMessage.indexOf("complete") == -1 && (return_message.errorNumber == 1 ||
		return_message.status == 1)) {

		if (return_message.commandRC == 33) {
			//keep updating modal with status

			$("#tinterProgressList").html("").append("<li>" + return_message.errorMessage + "</li>");
		}
		console.log(return_message);
		setTimeout(function() {
			FMXAlfaDispenseProgress(return_message);
		}, 500);  //send progress request after waiting 200ms.  No need to slam the SWDeviceHandler
	}
	else if (return_message.errorMessage.indexOf("complete") > 0 || return_message.errorNumber != 0) {
		alfaDispenseComplete(return_message);
	}
}
function dispenseProgressResp(return_message) {

	//$("#progress-message").text(return_message.errorMessage);
	$("#abort-message").show();
	$('#progressok').addClass('d-none');  //hide ok button
	if (return_message.errorMessage.indexOf("done") == -1 && (return_message.errorNumber == 1 ||
		return_message.status == 1)) {
		$("#tinterProgressList").empty();
		tinterErrorList = [];
		if (return_message.statusMessages != null && return_message.statusMessages[0] != null) {
			//keep updating modal with status
			var tinterModel = sessionTinterInfo.model;
			if (return_message.statusMessages.length > 0 && tinterModel.startsWith("FM X")) {
				buildProgressBars(return_message);
			}

		}
		if (return_message.errorList != null && return_message.errorList[0] != null) {
			// show errors
			return_message.errorList.forEach(function(item) {
				$("#tinterProgressList").append("<li>" + item.message + "</li>");
				tinterErrorList.push(item.message);
			});
		}
		if (return_message.errorMessage != null) {
			tinterErrorList.push(return_message.errorMessage);
			$("#tinterProgressList").append("<li>" + return_message.errorMessage + "</li>");
		}
		console.log(return_message);
		setTimeout(function() {
			FMXAlfaDispenseProgress();
		}, 500);  //send progress request after waiting 200ms.  No need to slam the SWDeviceHandler

	}
	else if (return_message.errorMessage.indexOf("done") > 0 || return_message.errorNumber != 0) {
		if (return_message.errorNumber == 4226) {
			return_message.errorMessage = i18n['global.tinterDriverBusyReinitAndRetry']
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

	if (my_return_message.statusMessages != null && my_return_message.statusMessages[0] != null) {

		if (my_return_message.statusMessages.length > 0) {
			buildProgressBars(my_return_message);  // on an abort, for example, we will have a progress update to do.
		}
    	/*
        my_return_message.errorList.forEach(function(item){
            $("#tinterErrorList").append( '</li>' + item.message + '</li>');
        });
        */
	}
	if (my_return_message.errorNumber == 4226) {
		my_return_message.errorMessage = '<s:text name="global.tinterDriverBusyReinitAndRetry" />';
	}
	$("#tinterErrorList").append('<li class="alert alert-danger">' + my_return_message.errorMessage + '</li>');

	if (myTitle != null) $("#tinterErrorListTitle").text(myTitle);
	else $("#tinterErrorListTitle").text(i18n['global.tinterError']);
	if (mySummary != null) $("#tinterErrorListSummary").text(mySummary);
	else $("#tinterErrorListSummary").text("");

}
function showTinterErrorModal(myTitle, mySummary, my_return_message) {
	$("#tinterErrorList").empty();
	if (my_return_message.errorList =  null && my_return_message.errorList[0] != null) {
		my_return_message.errorList.forEach(function(item) {
			$("#tinterErrorList").append('<li class="alert alert-danger">' + item.message + '</li>');
		});
	}
	if (my_return_message.statusMessages != null && my_return_message.statusMessages[0] != null) {
		my_return_message.statusMessages.forEach(function(item) {
			$("#tinterErrorList").append('<li class="alert alert-danger">' + item.message + '</li>');
		});
	}
	$("#tinterErrorList").append('<li class="alert alert-danger">' + my_return_message.errorMessage + '</li>');

	if (myTitle != null) $("#tinterErrorListTitle").text(myTitle);
	else $("#tinterErrorListTitle").text(i18n['global.tinterError']);
	if (mySummary != null) $("#tinterErrorListSummary").text(mySummary);
	else $("#tinterErrorListSummary").text("");
	$("#tinterErrorListModal").modal('show');
}
function FMXDispenseComplete(return_message) {
	processingDispense = false; // allow user to start another dispense after tinter error
	$('#spinner').addClass('d-none');
//	buildProgressBars(return_message);
	$("#abort-message").hide();

	if ((return_message.errorNumber == 0 && return_message.commandRC == 0) || (return_message.errorNumber == -10500 && return_message.commandRC == -10500)) {
		// save a dispense (will bump the counter)
		getSessionTinterInfo($("#reqGuid").val(), warningCheck);

		$("#dispenseStatus").text(i18n['global.lastDispenseComplete']);


		$('#tinterInProgressTitle').text(i18n['global.tinterProgress']);
		$('#tinterInProgressMessage').text('');
		$("#tinterProgressList").empty();
		tinterErrorList = [];
		if (return_message.statusMessages != null && return_message.statusMessages[0] != null) {
			buildProgressBars(return_message);
			/* DJM dont think we need this for FMX			return_message.errorList.forEach(function(item){
						//	$("#tinterProgressList").append("<li>" + item.message + "</li>");
							tinterErrorList.push(item.message);
						});
						*/
		} else {

			tinterErrorList.push(return_message.errorMessage);
			$("#tinterProgressList").append("<li>" + return_message.errorMessage + "</li>");
		}
		if ($('#progressok').length > 0 ) {
			$('#progressok').removeClass('d-none');
		}
		else {
			writeDispense(return_message); // will also send tinter event
			waitForShowAndHide("#tinterInProgressModal");
		}
	} else {
		if (return_message.errorNumber == 4226) {
			return_message.errorMessage = i18n['global.tinterDriverBustReinitRetry']
		}
		$("#dispenseStatus").text(i18n['global.lastDispense'] + return_message.errorMessage);
		waitForShowAndHide("#tinterInProgressModal");
		console.log('hide done');
		//Show a modal with error message to make sure the user is forced to read it.
		FMXShowTinterErrorModal(i18n['global.dispenseError'], null, return_message);
	}
	sendingTinterCommand = "false";
}

function alfaDispenseComplete(return_message) {
	processingDispense = false; // allow user to start another dispense after tinter error
	$('#spinner').addClass('d-none');
	if ((return_message.errorNumber == 0 && return_message.commandRC == 0)) {
		// save a dispense (will bump the counter)
		getSessionTinterInfo($("#reqGuid").val(), warningCheck);
		$("#dispenseStatus").text(i18n['global.lastDispenseComplete']);

		$('#tinterInProgressTitle').text(i18n['global.tinterProgress']);
		$('#tinterInProgressMessage').text('');
		$("#abort-message").hide();
		$('#tinterProgressList').html('');
		if ($('#progressok').length > 0)  {
			$('#progressok').removeClass('d-none');
		}
		else {
			writeDispense(return_message); // will also send tinter event
			waitForShowAndHide("#tinterInProgressModal");
		}

	} else {
		$("#dispenseStatus").text(i18n['global.lastDispense'] + return_message.errorMessage);
		waitForShowAndHide("#tinterInProgressModal");
		console.log('hide done');
		//Show a modal with error message to make sure the user is forced to read it.
		showTinterErrorModal(i18n['global.dispenseError'], null, return_message);
	}
	sendingTinterCommand = "false";
	startSessionTimeoutTimers();
}
function dispenseComplete(return_message) {

	return_message.command = "Dispense";
	var teDetail = new TintEventDetail("DISPENSE USER", sessionTinterInfo.lastPurgeUser, 0);
	var tedArray = [teDetail];
	let curDate = new Date();
	sendTinterEvent($('#reqGuid').val(), curDate, return_message, tedArray);

	if ((return_message.errorNumber == 0 && return_message.commandRC == 0) || (return_message.errorNumber == -10500 && return_message.commandRC == -10500)) {
		// save a dispense (will bump the counter)
		getSessionTinterInfo($("#reqGuid").val(), warningCheck);
		$("#dispenseStatus").text(i18n['global.lastDispenseComplete']);
		$('#tinterInProgressTitle').text(i18n['global.tinterProgress']);
		$('#tinterInProgressMessage').text('');
		if  ($('#progressok').length > 0) { //dispense.jsp
			$('#progressok').removeClass('d-none');
		}
		else {
			writeDispense(return_message); // will also send tinter event
			waitForShowAndHide("#tinterInProgressModal");
		}
	} else {
		$("#dispenseStatus").text(i18n['global.lastDispense'] + return_message.errorMessage);
		waitForShowAndHide("#tinterInProgressModal");
		console.log('hide done');
		//Show a modal with error message to make sure the user is forced to read it.
		showTinterErrorModal(i18n['global.dispenseError'], null, return_message);
	}
	sendingTinterCommand = "false";
	startSessionTimeoutTimers();
}
function abort() {
	console.log('before abort');
	processingDispense = false;

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
	if (typeof(ws_printer) !== 'undefined' && ws_printer !=null) {
		printMessageParsed = ParsePrintMessage();
	}
	if (!printMessageParsed && typeof ws_tinter !== 'undefined'
		&& ws_tinter) {
		console.log("Received Message");
		if (ws_tinter && ws_tinter.wserrormsg != null && ws_tinter.wserrormsg != "") {

			if (sendingTinterCommand == "true") {
				// received an error from WSWrapper so we won't get any JSON result
				// Since we are sending a dispense command, show as dispense error

				$("#dispenseStatus").text(i18n['global.lastDispense'] + ws_tinter.wserrormsg);
				//Show a modal with error message to make sure the user is forced to read it.
				$("#tinterSocketError").text(ws_tinter.wserrormsg);
				$('#progressok').removeClass('d-none');
				waitForShowAndHide("#tinterInProgressModal");

				startSessionTimeoutTimers();
				console.log('hide done');
				$("#tinterSocketErrorModal").modal('show');

			} else {
				console.log("Received unsolicited error " + ws_tinter.wserrormsg);

				// so far this only happens when SWDeviceHandler is not running and we created a new WSWrapper when 
				// page intially loaded.  For now wait until they do a dispense to show the error (no everybody has a tinter)
			}
		} else {
			// is result (wsmsg) JSON?
			let isTintJSON = false;
			try {
				if (ws_tinter && ws_tinter.wsmsg != null) {
					var return_message = JSON.parse(ws_tinter.wsmsg);
					isTintJSON = true;
				}
			}
			catch (error) {

				console.log("Caught error is = " + error);


				console.log("Message is junk, throw it out");
				//console.log("Junk Message is " + ws_tinter.wsmsg);
			}
			if (isTintJSON) {
				var tinterModel = sessionTinterInfo.model;
				var errorKey = return_message.errorMessage;
				if (tinterModel != null && tinterModel.startsWith("SANTINT")) {
					return_message.errorMessage = i18n[errorKey];
				}
				console.log("in istintJSON return message = ");
				console.log(return_message);
				switch (return_message.command) {
					case 'Dispense':
					case 'DispenseProgress':
					case 'Abort':
						$("#dispenseStatus").text('');
						if (tinterModel != null && tinterModel.startsWith("FM X")) { //only FM X series has purge in progress % done
							dispenseProgressResp(return_message);
						}
						else if (tinterModel != null && tinterModel.startsWith("ALFA")) { //alfa needs a progress check
							alfaDispenseProgressResp(return_message);
						}
						else if ((return_message.errorNumber == 0 && return_message.commandRC == 0) || (return_message.errorNumber == -10500 && return_message.commandRC == -10500)) {

							// save a dispense (will bump the counter)
							if (tinterModel != null && tinterModel.startsWith("SANTINT")) {
								return_message.errorMessage = log_english[errorKey];
							}
							writeDispense(return_message); // will also send tinter event
							waitForShowAndHide("#tinterInProgressModal");
						}
						else {

							waitForShowAndHide("#tinterInProgressModal");
							//Show a modal with error message to make sure the user is forced to read it.
							showTinterErrorModal("Dispense Error", null, return_message);
							processingDispense = false; // allow user to start another dispense after tinter error
							sendingDispCommand = "false";
							// send tinter event (no blocking here)
							var curDate = new Date();
							var myGuid = $("#reqGuid").val();

							var teDetail = new TintEventDetail("ORDER NUMBER", $("#controlNbr").text(), 0);

							var tedArray = [teDetail];
							if (tinterModel != null && tinterModel.startsWith("SANTINT")) {
								return_message.errorMessage = log_english[errorKey];
							}
							sendTinterEvent(myGuid, curDate, return_message, tedArray);

						}

						break;
					default:
						//Not an response we expected...
						console.log("Message from different command is junk, throw it out");

				} // end switch statement
			} else {
				console.log("Message is junk, throw it out");
			}
		}

		//Clearing  for dispense.jsp
		if ($('.table-bordered input:not([type=hidden])').length > 0) {
			//Clearing inputs
			$('.table-bordered input:not([type=hidden])').val('');
		}
	}

}
//pre Dispense
function preDispenseRoutine() {
	shotList = [];
	let inputFound = false;
	let invalidInput = false;
	let numClrntsDispensed = 0;
	$(".progress-wrapper").empty();
	//Validate form input values and show popovers if necessary, add shotList values
	$('.myinputs').each(function() {
		if ($(this).val() != "") {
			inputFound = true;
			return false;
		}
	});

	if (inputFound) {
		$('table.table-bordered tr').each(function() {
			let rowInputFound = false;
			const $rowInputs = $(this).find('input');

			$rowInputs.each(function() {
				if ($(this).val() !== "") {
					rowInputFound = true;
					console.log('row input found: ' + rowInputFound);
					numClrntsDispensed++;
					return false;
				}
			});

			if (rowInputFound) {
				if (numClrntsDispensed > 0 && numClrntsDispensed < 9) {
					if (validateInput($rowInputs)) {
						//Setting up utility vars
						let rowColorCode = $(this).find('#code').text();
						let rowPosition = parseInt($(this).find('#pos').text());
						let rowIncrList = [];

						$rowInputs.each(function() {
							let num = parseInt(this.value) || 0;
							rowIncrList.push(num);
						});

						if (rowIncrList.length === 4) {
							let str = { "incrementArray": rowIncrList, "reqGuid": $('#reqGuid').val() };
							let jsonIN = JSON.stringify(str);
							$.ajax({
								url: "dispenseConvertIncrementsAction.action",
								type: "POST",
								contentType: "application/json; charset=utf-8",
								dataType: "json",
								async: false,
								data: jsonIN,
								success: function(data) {
									console.log(encodeURI(data));
									//Building shotList of Colorants to dispense for row
									shotList.push(new Colorant(rowColorCode, data.shots, rowPosition, data.UOM));
									console.log(shotList);
								},
								error: function(textStatus, errorThrown) {
									console.log("JSON dispense failed here");
									console.log(textStatus + "" + errorThrown);
								}
							});
						} else { console.log("Row increments empty or incomplete."); }
					} else { console.log("Invalid inputs found"); invalidInput = true; }
				} else { popupInputError($rowInputs[0], i18n['dispense.cannotDispenseMoreThanEight']); }
			}
		});
	}
	else {
		console.log("No values given, dispense not executed.");
		$('#tinterInProgressTitle').text(i18n['dispense.qtyError']);
		$('#tinterInProgressMessage').text(i18n['dispense.noValuesEntered']);
		$('#progressok').removeClass('d-none');
		$(".progress-wrapper").empty();
		$("#tinterInProgressModal").modal('show');
	}


	if (!invalidInput && shotList.length > 0 && (numClrntsDispensed > 0 && numClrntsDispensed < 9)) {
		//Begin predispense Check
		preDispenseCheck();
	}
	else {
		console.log("ShotList empty or more than 8 colorants selected to dispense, dispense not executed.");
	}
}

function validateInput(inputTextArray) {
	let result = true;
	inputTextArray.each(function() {
		if (this.value && this.value !== "") {
			if (this.value.match(/^[0-9]\d{0,1}$/) === null) {
				popupInputError(this, i18n['global.positiveNbr']);
				result = false;
			}
		}
	});

	return result;
}

function popupInputError(input, outputMsg) {
	if ($('input[data-toggle="popover"]').length === 0) {
		console.log("Invalid entries detected.");
		$(input).attr("data-toggle", "popover");
		$(input).attr("data-placement", "bottom");
		$(input).attr("data-content", outputMsg);
		$(input).popover({ trigger: 'manual' });
		$(input).popover('toggle');
		$('.popover').addClass('popover-danger');
		$('html,body').animate({ scrollTop: $(input).offset().top -= 80 });
	}
}

function preDispenseCheck() {
	$("#tinterInProgressTitle").text(i18n['global.colorantLevelCheckInProgress']);
	$("#tinterInProgressMessage").text(i18n['global.pleaseWaitClrntLevelCheck']);
	$(".progress-wrapper").empty();
	$("#tinterInProgressModal").modal('show');
	// TODO Get SessionTinter, this is async ajax call so the rest of the logic is in the callback below
	getSessionTinterInfo($("#reqGuid").val(), preDispenseCheckCallback);
}

function preDispenseCheckCallback() {
	let preDispenseCheckFlag = false;
	// comes from getSessionTinterInfo

	// check if purge required...
	let dateFromString = new Date(sessionTinterInfo.lastPurgeDate);
	let today = new Date();
	if (dateFromString.getFullYear() < today.getFullYear() || dateFromString.getMonth() < today.getMonth() || dateFromString.getDate() < today.getDate()) {
		$("#tinterErrorList").empty();
		$("#tinterErrorList").append('<li class="alert alert-danger">' + i18n['displayFormula.tinterPurgeIsRequiredLastDoneOn'] + " " + moment(dateFromString).format('ddd MMM DD YYYY') + '</li>');
		waitForShowAndHide("#tinterInProgressModal");
		$("#tinterErrorListTitle").text(i18n['global.purgeRequired']);
		$("#tinterErrorListSummary").text(i18n['dispense.goHomeToPurge']);
		$("#tinterErrorListModal").modal('show');

		preDispenseCheckFlag = true;
	} else {
		// Check Levels
		console.log("about to check levels");
		// Check for STOP! because there is not enough colorant in the tinter
		let stopList = checkDispenseColorantEmpty(shotList, sessionTinterInfo.canisterList);
		if (stopList[0] != null) {
			$("#tinterErrorList").empty();
			stopList.forEach(function(item) {
				$("#tinterErrorList").append('<li class="alert alert-danger">' + item + "</li>");
			});
			//Show it in a modal they can't go on
			waitForShowAndHide("#tinterInProgressModal");
			$("#tinterErrorListTitle").text(i18n['global.colorantLevelTooLow']);
			$("#tinterErrorListSummary").text(i18n['dispense.fillEmptyCanister']);
			$("#tinterErrorListModal").modal('show');

			preDispenseCheckFlag = true;
		} else {
			let warnList = checkDispenseColorantLow(shotList, sessionTinterInfo.canisterList);
			if (warnList[0] != null) {
				$("#tinterWarningList").empty();
				warnList.forEach(function(item) {
					$("#tinterWarningList").append('<li class="alert alert-warning">' + item + '</li>');
				});
				//Show in modal, they can say OK to continue
				waitForShowAndHide("#tinterInProgressModal");
				console.log('hide done');
				$("#tinterWarningListTitle").text(i18n['global.lowColorantLevels']);
				$("#tinterWarningListModal").modal('show');


				preDispenseCheckFlag = true;
			} else {
				//OK to verify
			}
		} // end colorant level checks
	} // end purge check

	//Validate predispenseCheck pass, if fail, notify.
	if (!preDispenseCheckFlag) {
		//Dispensing if shotList contains values
		if (shotList.length > 0) {
			$("#tinterInProgressModal").modal('show');
			$('#tinterInProgressTitle').text(i18n['global.dispenseInProgress']);
			stopSessionTimeoutTimers(timeoutWarning, timeoutExpire);
			decrementColorantForDispense($('#reqGuid').val(), shotList, decrementCallback);
		} else {
			console.log("Shotlist empty, dispense not executed.");
			$('#tinterInProgressTitle').text(i18n['global.tinterProgress']);
			$('#tinterInProgressMessage').text(i18n['dispense.noValuesEntered']);
			$('#progressok').removeClass('d-none');
		}
	}
	else { console.log("Predispense Check failed with errors. Dispense not executed.") }
}

function decrementCallback(myPassFail) {
	console.log("checking decrement pass/fail " + myPassFail);
	if (myPassFail === true) {
		dispense(shotList);
	} else {
		//TODO show error on decrement, 
		waitForShowAndHide("#tinterInProgressModal");

		console.log('hide done');
	}
}





function warningCheck() {
	sessionTinterInfo.canisterList.forEach(function(value) {
		if (value.currentClrntAmount <= value.fillAlarmLevel) {
			if ($('#warning' + value.clrntCode).hasClass('d-none')) {
				$('#warning' + value.clrntCode).removeClass('d-none');
			}
		} else {
			if (!$('#warning' + value.clrntCode).hasClass('d-none')) {
				$('#warning' + value.clrntCode).addClass('d-none');
			}
		}
	});
}

//Used to rotate loader icon in modals
function rotateIcon(){
	let n = 0;
	$('#spinner').removeClass('d-none');
	let interval = setInterval(function(){
    	n += 1;
    	if(n >= 60000){
            $('#spinner').addClass('d-none');
        	clearInterval(interval);
        }else{
        	$('#spinner').css("transform","rotate(" + n + "deg)");
        }
	},5);
	
	$('#tinterInProgressModal').one('hide.bs.modal',function(){
		$('#spinner').addClass('d-none');
    	if(interval){clearInterval(interval);}
	});
}
