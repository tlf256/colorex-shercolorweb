<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!doctype html>


<%@ taglib prefix="s" uri="/struts-tags" %>

<html lang="en">
	<head>
		<meta name="viewport" content="width=device-width, initial-scale=1">
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
		
		<title><s:text name="tinterPurge.tinterPurge"/></title>
			<!-- JQuery -->
		<link rel="stylesheet" href="css/bootstrap.min.css" type="text/css">
		<link rel="stylesheet" href="css/bootstrapxtra.css" type="text/css">
		<link rel="stylesheet" href="js/smoothness/jquery-ui.min.css" type="text/css">
		<link rel="stylesheet" href="css/CustomerSherColorWeb.css" type="text/css"> 
		<link rel="stylesheet" href="font-awesome-4.7.0/css/font-awesome.min.css" type="text/css">
		<script type="text/javascript" charset="utf-8" src="js/jquery-3.4.1.min.js"></script>
		<script type="text/javascript" charset="utf-8"	src="js/jquery-ui.min.js"></script>
		<script type="text/javascript" charset="utf-8"	src="js/bootstrap.min.js"></script>
		<script type="text/javascript" charset="utf-8"	src="js/moment.min.js"></script>
		<script type="text/javascript" charset="utf-8" src="script/customershercolorweb-1.5.3.js"></script>
		<script type="text/javascript" charset="utf-8"	src="script/WSWrapper.js"></script>
		<script type="text/javascript" charset="utf-8"	src="script/tinter-1.5.0.js"></script>
		<s:set var="thisGuid" value="reqGuid" />
		<style>
	        .sw-bg-main {
	            background-color: ${sessionScope[thisGuid].rgbHex};
	        }
	        
	        #verifyScanInputError {
  				font-weight: bold;
  				color: red;
			}
	        
	    </style>
	    
	<script type="text/javascript">
	 //Global Variables
	"use strict"; 
	var ws_tinter = new WSWrapper("tinter");
	var sendingTinterCommand = "false";
	var _rgbArr = [];
	var dispenseErrorList = [];
	<s:iterator value="canList" status="i">
	_rgbArr["<s:property value="clrntCode"/>"]="<s:property value="rgbHex"/>";  //for colored progress bars
	</s:iterator>
	var platform = navigator.platform;
	var cleanNozzleBeforePurge = false;

	$(function(){ // on ready
		//capture A key to abort
		jQuery(document).on("keydown",akey);
	});

	function akey(e) {
		if (sendingTinterCommand == "true") {
			e = e || window.event;
			console.log("KEY CODE: " + e.code);
			if (e.code === 'KeyA') {
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

	//function parses status messages to display progress bars
	//for individual colorant or overall purge progress
	function buildProgressBars(return_message){
		var count = 1;
		var keys=[];
		$(".progress-wrapper").empty();
		keys = Object.keys(return_message.statusMessages);
		if (keys !=null && keys.length > 0) {			
			return_message.statusMessages.forEach(function(item){
				var colorList = item.message.split(" ");
				var colorPct = true; //flag for individual colorant progress
				var color;
				var pct;

				if(colorList[1] == undefined){
					//overall purge progress
					color = null;
					pct = colorList[0];
					colorPct = false;
				} else {
					//individual colorant progress
					color = colorList[0];
					pct = colorList[1];
				}
				
				//fix bug where we are done, but not all pumps report as 100%
				if (return_message.errorMessage.indexOf("done") > 1 && (return_message.errorNumber == 0 &&
						 return_message.status == 0)) {
					  pct = "100%";
				  }

				//$("#tinterProgressList").append("<li>" + item.message + "</li>");
				
				var $clone = $("#progress-0").clone();
				$clone.attr("id","progress-" + count);
				var $bar = $clone.children(".progress-bar");
				$bar.attr("id","bar-" + count);
				$bar.attr("aria-valuenow",pct);
				$bar.css("width", pct);
				$clone.css("display", "block");
				var color_rgb = getRGB(color);

				//change color of text based on background color
				switch(color){
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
				
				if(colorPct){
					$bar.children("span").text(color + " " + pct);
				} else{
					$bar.children("span").text(pct);
				}
				
				console.log("barring " + item.message);
				//console.log($clone);
				
				$clone.appendTo(".progress-wrapper");
				
				count++;
			});
		}
	}

	function buildEvoProgressBars(return_message) {
		var count = 1;
		var keys=[];
		$(".progress-wrapper").empty();
		keys = Object.keys(return_message.statusMessages);
		if (keys !=null && keys.length > 0) {			
			return_message.statusMessages.forEach(function(item){
				var colorList = item.message.split(" ");
				var color;
				var pct;
				if(colorList.length > 1){
					color= colorList[0];
					pct = colorList[1];
				} else {
					color= "";
					pct = item.message;
				}
				
				var $clone = $("#progress-0").clone();
				$clone.attr("id","progress-" + count);
				var $bar = $clone.children(".progress-bar");
				$bar.attr("id","bar-" + count);
				$bar.attr("aria-valuenow",pct);
				$bar.css("width", pct);
				$clone.css("display", "block");
				var color_rgb = getRGB(color);
				if(color_rgb === ""){
					$bar.children("span").css("color", "white");
					$bar.css("background-color", "blue");
				}
				else {
					//change color of text based on background color
					switch(color){
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
				}
				
				$bar.children("span").text(pct);
				
				console.log("barring " + item.message);
				//console.log($clone);
				
				$clone.appendTo(".progress-wrapper");
				
				count++;
			});
		}
	}
	
	function PurgeProgress(tintermessage){
		console.log('before purge status modal show');
		$("#PurgeInProgressModal").modal('show');
		rotateIcon($("#PurgeInProgressModal"), $("#purgeSpinner"));
		var shotList = null;
		var configuration = null;
		var tinterModel = $("#tinterPurgeAction_tinterModel").val();
		if(tinterModel !=null && (tinterModel.startsWith("AS") || tinterModel.startsWith("FM X"))){ 
			var cmd = "PurgeProgress";
			if(!platform.startsWith("Win") || tinterModel.startsWith("AS")){
				cmd = "PurgeStatus";
			}
		   	var tintermessage = new TinterMessage(cmd,null,null,null,null);
		}
		else if(tinterModel !=null && ( tinterModel.startsWith("COROB EVO"))){ 
			var cmd = "PurgeStatus";
			var tintermessage = new TinterMessage(cmd,null,null,null,null);  
		}
		else{
			var cmd = "DispenseStatus";
			  
		}
		var msgId = tintermessage.msgId;
    	var tintermessage = new TinterMessage(cmd,null,null,null,null,msgId);

    	var json = JSON.stringify(tintermessage);
		sendingTinterCommand = "true";
    	ws_tinter.send(json);
	}
	/*
	function FMXPurgeProgress(){
		console.log('before purge status modal show');
		$("#PurgeInProgressModal").modal('show');
		rotateIcon();
		var cmd = "PurgeProgress";
		var shotList = null;
		var configuration = null;
    	var tintermessage = new TinterMessage(cmd,null,null,null,null);  
    	var json = JSON.stringify(tintermessage);
		sendingTinterCommand = "true";
    	ws_tinter.send(json);
	}
	*/
	function purge(){
		var cmd = "PurgeAll";
		var shotList = null;
    	var tintermessage = new TinterMessage(cmd,shotList,null,null,null);  
    	var json = JSON.stringify(tintermessage);
		sendingTinterCommand = "true";
		if(ws_tinter!=null && ws_tinter.isReady=="false") {
    		console.log("WSWrapper connection has been closed (timeout is defaulted to 5 minutes). Make a new WSWrapper.");
    		ws_tinter = new WSWrapper("tinter");
		}
    	ws_tinter.send(json);
	}

	function dispenseProgressResp(myGuid, curDate,return_message, tedArray){
		$("#abort-message").show();
		if ((platform.startsWith("Win") && return_message.errorMessage.indexOf("Done") == -1 || 
				!platform.startsWith("Win") && return_message.errorMessage.indexOf("Purge Job Complete") == -1) 
				&& (return_message.errorNumber == 1 || return_message.status == 1)) {
			//keep updating modal with status
			$("#tinterProgressList").empty();
			dispenseErrorList = [];
			if(return_message.statusMessages!=null && return_message.statusMessages[0]!=null){
				return_message.statusMessages.forEach(function(item){
					buildProgressBars(return_message);
					dispenseErrorList.push(item.message);
				});
			} else {
				dispenseErrorList.push(return_message.errorMessage);
				$("#tinterProgressList").append("<li>" + return_message.errorMessage + "</li>");
			}
			console.log(return_message);
			//setTimeout(function(){
				PurgeProgress();
		//	}, 200);  //send progress request after waiting 200ms.  No need to slam the SWDeviceHandler
			
		}
		else{
			var tinterModel = $("#tinterPurgeAction_tinterModel").val();
			var fmx = (tinterModel.startsWith('FM X')) ? "fmx" : "";
			purgeComplete(myGuid, curDate,return_message, tedArray, fmx);
			$(".progress-wrapper").empty();
		}
			
    }
	function evoDispenseProgressResp(myGuid, curDate,return_message, tedArray){
		$("#abort-message").show();
		if(return_message.commandRC == 3){
			//just started
			if(return_message.errorNumber == -10500){
				//warnings present
				return_message.errorList.forEach(function(item){
					$("#tinterWarningList").append('<li class="alert alert-warning">'+item.message+'</li>');
				});
				//Show in modal, they can say OK to continue
				waitForShowAndHide("#tinterInProgressModal");
				$("#tinterWarningListModal").modal('show');
			}

			console.log(return_message);
			PurgeProgress(return_message);
		} else if(return_message.commandRC == 33){
			//in progress
			//keep updating modal with status
			$("#tinterProgressList").empty();
			buildEvoProgressBars(return_message)
			//buildProgressBars(return_message);
			console.log(return_message);
			PurgeProgress(return_message);
		} else if(return_message.commandRC == 2){
			//done
			purgeComplete(myGuid, curDate,return_message, tedArray, "fmx");
			$(".progress-wrapper").empty();
			console.log("done purging")
			console.log(return_message)
		} else {
			//assume it is an error
			purgeComplete(myGuid, curDate,return_message, tedArray, "fmx");
			console.log(return_message)
		}
	}
	function alfaDispenseProgressResp(myGuid, curDate,return_message, tedArray){
		$("#abort-message").show();
		$('#progressok').addClass('d-none');  //hide ok button
		if (return_message.errorMessage.indexOf("complete") == -1 && (return_message.errorNumber == 1 ||
				 return_message.status == 1)) {
					
			if(return_message.commandRC == 33){
			//keep updating modal with status
			
				$("#tinterProgressList").html("").append("<li>" + return_message.errorMessage + "</li>");
			}
			console.log(return_message);
			setTimeout(function(){
				PurgeProgress(return_message);
			}, 500);  //send progress request after waiting 200ms.  No need to slam the SWDeviceHandler
		}
		else {
			purgeComplete(myGuid, curDate,return_message, tedArray, "alfa");
			$(".progress-wrapper").empty();
		}
	}

	function FMXShowTinterErrorModal(myTitle, mySummary, my_return_message){
	    $("#tinterErrorList").empty();
	    $("#tinterErrorListModal").modal('show');
	    $("#abort-message").hide();
	    
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
	    if(my_return_message.errorNumber == 4226){
	    	my_return_message.errorMessage = '<s:text name="global.tinterDriverBusyReinitAndRetry"/>';
				    }
	    $("#tinterErrorList").append('<li class="alert alert-danger">' + my_return_message.errorMessage + '</li>');
	    
	    if(myTitle!=null) $("#tinterErrorListTitle").text(myTitle);
	    else $("#tinterErrorListTitle").text('<s:text name="global.tinterError"/>');
	    if(mySummary!=null) $("#tinterErrorListSummary").text(mySummary);
	    else $("#tinterErrorListSummary").text("");
	  
	}
	function showTinterErrorModal(myTitle, mySummary, my_return_message){
		$("#tinterErrorList").empty();
		if(my_return_message.errorList!=null && my_return_message.errorList[0]!=null){
			my_return_message.errorList.forEach(function(item){
				$("#tinterErrorList").append("<li>" + item.message + "</li>");
			});
		} else {
			$("#tinterErrorList").append("<li>" + my_return_message.errorMessage + "</li>");
		}
		if(myTitle!=null) $("#tinterErrorListTitle").text(myTitle);
		else $("#tinterErrorListTitle").text('<s:text name="global.tinterError"/>');
		if(mySummary!=null) $("#tinterErrorListSummary").text(mySummary);
		else $("#tinterErrorListSummary").text("");
		$("#tinterErrorListModal").modal('show');
	}
    function purgeComplete(myGuid, curDate,return_message, tedArray, fmx){
    	$("#abort-message").hide();
    	var errorKey = return_message.errorMessage;
    	var tinterModel = $("#tinterPurgeAction_tinterModel").val();
		if (tinterModel != null && tinterModel.startsWith("SANTINT")){
			return_message.errorMessage = i18n[errorKey];
		}
		 
		if(return_message.errorNumber == 0 || return_message.errorNumber == -10500){
			// show purge
			//var displayDate = (curDate.getMonth()+1) + "/" + curDate.getDate() + "/" + curDate.getFullYear();
			//var displayTime = curDate.getHours() + ":" + curDate.getMinutes() + ":" + curDate.getSeconds();
			//$("#lastPurgeText").text("Last purge was done on " + displayDate + " at " + displayTime + " by " + $("#tinterPurgeAction_currUser").val())
			
			// reach back to the server to get the internationalized Last Purge string 		
			$.ajax({
				url: "getUpdatedPurgeDateAction",
				context : document.body,
				type: "POST",
				data: {
					reqGuid: myGuid,
					updatedTime: moment(curDate).format('LT')
				},
				dataType : "json",
				async: false,
				success: function (data) {
					//console.log(data);		
					if(data.sessionStatus === "expired"){
	            		window.location.href = "./invalidLoginAction.action";
	            	}
	            	else{
	            		if (data != null && data.updatedLastPurgeMsg != null){
		            		$("#lastPurgeText").text(data.updatedLastPurgeMsg);
	            		}
	            	}
				},
				error: function(error){
					console.log(error);
				}
			});
			waitForShowAndHide("#purgeInProgressModal");
			// show success message in alert area
			$("#tinterAlertList").empty();
			$("#tinterAlertList").append('<li><s:text name="tinterPurge.purgeSuccessful"/></li>');
			if($("#tinterAlert").hasClass("d-none")) $("#tinterAlert").removeClass("d-none");
			if($("#tinterAlert").hasClass("alert-danger")) $("#tinterAlert").removeClass("alert-danger");
			if(!$("#tinterAlert").hasClass("alert-success")) $("#tinterAlert").addClass("alert-success");
		} else {
			waitForShowAndHide("#purgeInProgressModal");
			$("#tinterAlertList").empty();
			$("#tinterAlertList").append('<li><s:text name="tinterPurge.purgeFailedErrMsg"/>'+ return_message.errorMessage + "</li>");
			if($("#tinterAlert").hasClass("d-none")) $("#tinterAlert").removeClass("d-none");
			if(!$("#tinterAlert").hasClass("alert-danger")) $("#tinterAlert").addClass("alert-danger");
			if($("#tinterAlert").hasClass("alert-success")) $("#tinterAlert").removeClass("alert-success");
			//Show a modal with error message to make sure the user is forced to acknowledge it.
			if(fmx == "fmx"){
				FMXShowTinterErrorModal(null,null,return_message);
			}
			else{				
				showTinterErrorModal(null,null,return_message);
			}
		}
		return_message.command = "PurgeAll";
		// update error message to english for logging
		if (tinterModel != null && tinterModel.startsWith("SANTINT")){
			return_message.errorMessage = log_english[errorKey];
		}
		sendTinterEvent(myGuid, curDate, return_message, tedArray);
    }
    
    function cleanNozzleRequired(){
    	var purgeDate = $("#lastPurgeDate").val();
    	var purgeTime = $("#lastPurgeTime").val();
    	var lastPurgeDateTime = purgeDate + " " + purgeTime;
    	var dateFromString = new Date(lastPurgeDateTime);
    	console.log("last purge datetime: " + dateFromString);
		var today = new Date();
		console.log("today's date: " + today);
		if (dateFromString.getFullYear()<today.getFullYear() || dateFromString.getMonth()<today.getMonth() 
				|| dateFromString.getDate()<today.getDate()){
			console.log("nozzle cleaning required before purge");
			cleanNozzleBeforePurge = true;
			return true;
		} else {
			console.log("nozzle cleaning not required before purge");
			return false;
		}
    }
        
    function abort(){
    	console.log('before abort');
    	var cmd = "Abort";
    	var shotList = null;
    	var configuration = null;
    	var tintermessage = new TinterMessage(cmd,null,null,null,null);  
    	var json = JSON.stringify(tintermessage);
    	ws_tinter.send(json);
    }
	function openNozzle(){
		var cmd = "OpenNozzle";
		$("#cleanNozzleButton").prop('disabled', true);
		var shotList = null;
    	var tintermessage = new TinterMessage(cmd,shotList,null,null,null);  
    	var json = JSON.stringify(tintermessage);
		sendingTinterCommand = "true";
		if(ws_tinter!=null && ws_tinter.isReady=="false") {
    		console.log("WSWrapper connection has been closed (timeout is defaulted to 5 minutes). Make a new WSWrapper.")
    		ws_tinter = new WSWrapper("tinter");
		}
    	ws_tinter.send(json);
	}

	function closeNozzle(){
		var cmd = "CloseNozzle";
		var shotList = null;
    	var tintermessage = new TinterMessage(cmd,shotList,null,null,null);  
    	var json = JSON.stringify(tintermessage);
		sendingTinterCommand = "true";
		if(ws_tinter!=null && ws_tinter.isReady=="false") {
    		console.log("WSWrapper connection has been closed (timeout is defaulted to 5 minutes). Make a new WSWrapper.")
    		ws_tinter = new WSWrapper("tinter");
		}
    	ws_tinter.send(json);
	}
	
	function cleanNozzle(){
		var cmd = "CleanNozzles";
		if(!platform.startsWith("Win")){
			cmd = "CleanNozzle";
		}
		var shotList = null;
    	var tintermessage = new TinterMessage(cmd,shotList,null,null,null);  
    	var json = JSON.stringify(tintermessage);
		sendingTinterCommand = "true";
		if(ws_tinter!=null && ws_tinter.isReady=="false") {
    		console.log("WSWrapper connection has been closed (timeout is defaulted to 5 minutes). Make a new WSWrapper.")
    		ws_tinter = new WSWrapper("tinter");
		}
    	ws_tinter.send(json);
	}

	function RecdMessage() {
		console.log("Received Message");
		//parse the spectro
		console.log("isReady is " + ws_tinter.isReady + "BTW");
		if(ws_tinter.wserrormsg!=null && ws_tinter.wserrormsg!=""){
			if(sendingTinterCommand == "true"){
				// received an error from WSWrapper so we won't get any JSON result
				// Since we are sending a tinter command, show as tinter error
				//Show a modal with error message to make sure the user is forced to read it.
				waitForShowAndHide("#purgeInProgressModal");
				$("#tinterSocketErrorModal").modal('show');
				$("#tinterSocketError").text(ws_tinter.wserrormsg);
				
			} else {
				console.log("Received unsolicited error " + ws_tinter.wserrormsg);
				// so far this only happens when SWDeviceHandler is not running and we created a new WSWrapper when 
				// page intially loaded.  For now wait until they do a dispense to show the error (no everybody has a tinter)
			}
		} else {
			// is result (wsmsg) JSON?
			var isTintJSON = false;
			try{
				if(ws_tinter!=null && ws_tinter.wsmsg!=null){
					var return_message=JSON.parse(ws_tinter.wsmsg);
					isTintJSON = true;
				}
			}
			catch(error){
                console.log("Caught error is = " + error);
				console.log("Message is junk, throw it out");
				//console.log("Junk Message is " + ws_tinter.wsmsg);
			}
			if(isTintJSON){
				var return_message=JSON.parse(ws_tinter.wsmsg);
				
				switch (return_message.command) {
					case 'PurgeAll':
					case 'PurgeProgress':
					case 'PurgeStatus':
					case 'DispenseStatus':
			    		sendingTinterCommand = "false";
						// log tinter event...
						var curDate = new Date();
						var myGuid = $( "#tinterPurgeAction_reqGuid" ).val();
						var teDetail = new TintEventDetail("PURGE USER", $("#tinterPurgeAction_currUser").val(), 0);
						var tedArray = [teDetail];
						var tinterModel = $("#tinterPurgeAction_tinterModel").val();
						
						if(tinterModel == null ) {
							//do we really want this behavior?
							purgeComplete(myGuid, curDate, return_message, tedArray);
						}
						else if(tinterModel.startsWith("FM X") || tinterModel.startsWith("AS")){
							 if(return_message.errorNumber == 4226){
							    	return_message.errorMessage = '<s:text name="global.tinterDriverBusyReinitAndRetry"/>';
							    }
							dispenseProgressResp(myGuid, curDate,return_message, tedArray); 
						}
						else if(tinterModel.startsWith("COROB EVO")){
							evoDispenseProgressResp(myGuid, curDate,return_message, tedArray);
						}
						else if(tinterModel.startsWith("ALFA")){
							alfaDispenseProgressResp(myGuid, curDate,return_message, tedArray);
						}
						else{
							purgeComplete(myGuid, curDate, return_message, tedArray);
						}
						break;
					
					
					case 'OpenNozzle':
			    		sendingTinterCommand = "false";
						// log tinter event...
						var curDate = new Date();
						var myGuid = $( "#tinterPurgeAction_reqGuid" ).val();
						var teDetail = new TintEventDetail("NOZZLE USER", $("#tinterPurgeAction_currUser").val(), 0);
						var tedArray = [teDetail];
						sendTinterEvent(myGuid, curDate, return_message, tedArray); 
						if((return_message.errorNumber == 0 && (return_message.commandRC == 0 || return_message.commandRC == 2)) || (return_message.errorNumber == -10500 && return_message.commandRC == -10500)){
							// Enable Done button
							$("#cleanNozzleButton").prop('disabled', false);
						} else {
							waitForShowAndHide("#cleanNozzleModal");
							//Show a modal with error message to make sure the user is forced to read it.
							showTinterErrorModal('<s:text name="tinterPurge.openNozzleCoverError"/>', null, return_message);
						}
						break;
					case 'CloseNozzle':
			    		sendingTinterCommand = "false";
						// log tinter event...
						var curDate = new Date();
						var myGuid = $( "#tinterPurgeAction_reqGuid" ).val();
						var teDetail = new TintEventDetail("NOZZLE USER", $("#tinterPurgeAction_currUser").val(), 0);
						var tedArray = [teDetail];
						sendTinterEvent(myGuid, curDate, return_message, tedArray); 
						if((return_message.errorNumber == 0 && (return_message.commandRC == 0 || return_message.commandRC == 2)) || (return_message.errorNumber == -10500 && return_message.commandRC == -10500)){
							waitForShowAndHide("#closeNozzleInProgressModal");
							//removeTinterAlertDangerWarnings();
						} else {
							waitForShowAndHide("#closeNozzleInProgressModal");
							//Show a modal with error message to make sure the user is forced to read it.
							showTinterErrorModal(null,null,return_message);
						}
						break;
					case 'CleanNozzle':
					case 'CleanNozzles':
			    		sendingTinterCommand = "false";
						// log tinter event...
						var curDate = new Date();
						var myGuid = $( "#tinterPurgeAction_reqGuid" ).val();
						var teDetail = new TintEventDetail("NOZZLE USER", $("#tinterPurgeAction_currUser").val(), 0);
						var tedArray = [teDetail];
						sendTinterEvent(myGuid, curDate, return_message, tedArray); 
						
						if((return_message.errorNumber == 0 && (platform.startsWith("Win") && return_message.commandRC == 0 ||
								!platform.startsWith("Win") && return_message.commandRC == 2))){			
							$("#cleanNozzle-message").text(return_message.errorMessage);
							waitForShowAndHide("#cleanNozzleInProgress");
							if(!cleanNozzleBeforePurge){
					    		$("#fmxCleanNozzleModal").modal('show');
					    	}
						} else {
							waitForShowAndHide("#cleanNozzleInProgress");
							//Show a modal with error message to make sure the user is forced to read it.
							showTinterErrorModal(null,null,return_message);
						}
						break;
					default:
						//Not an response we expected...
						console.log("Message from different command is junk, throw it out");
				}
			} else {
				console.log("Message is junk, throw it out");
			}
		}
	}

 

//     $("nozzleCleanX").click(function(){
//         $("#cleanNozzleVid").get(0).pause();
//     });
    

	//Used to rotate loader icon in modals
	function rotateIcon(modal, spinner){
		let n = 0;
		spinner.removeClass('d-none');
		let interval = setInterval(function(){
	    	n += 1;
	    	if(n >= 60000){
	    		spinner.addClass('d-none');
	        	clearInterval(interval);
	        }else{
	        	spinner.css("transform","rotate(" + n + "deg)");
	        }
		},5);
		
		$(modal).one('hide.bs.modal',function(){
			spinner.addClass('d-none');
        	if(interval){clearInterval(interval);}
		});
	}

	$(function(){
		var tinterModel = $("#tinterPurgeAction_tinterModel").val();
		
		$(document).on("shown.bs.modal", "#purgeInProgressModal", function(event){
			purge();
	    });
	    
	    $(document).on("shown.bs.modal", "#cleanNozzleModal", function(event){
	        $("#cleanNozzleVid").get(0).play();
			openNozzle();
	    });

	    $(document).on("hidden.bs.modal", "#cleanNozzleModal", function(event){
	        $("#cleanNozzleVid").get(0).pause();
			closeNozzle();
	    });

		$(document).on("hidden.bs.modal", "#closeNozzleInProgressModal", function(event){
	        console.log("done cleaning nozzles");
	    	if(cleanNozzleBeforePurge && !$("#tinterErrorListModal").is(":visible")){
	    		$("#purgeInProgressModal").modal('show');
	    	}
	    });
	    
	    $(document).on("shown.bs.modal", "#cleanNozzleInProgress", function(event){
			cleanNozzle();
	    });
	    
	    $(document).on("show.bs.modal", "#cleanNozzleInProgress", function(event){
	    	console.log("begin cleaning nozzles");
	    	rotateIcon($("#cleanNozzleInProgress"), $("#nozzleSpinner"));
	    });
	    
	    $(document).on("hidden.bs.modal", "#cleanNozzleInProgress", function(event){
	    	console.log("done cleaning nozzles");
	    	if(cleanNozzleBeforePurge){
	    		$("#purgeInProgressModal").modal('show');
	    	} else {
	    		if(!$("#tinterErrorListModal").is(":visible")){
	    			$("#fmxCleanNozzleModal").modal('show');
	    		}
	    	}
	    });
	    
	    $(document).on("click", "#cleanNozzleButton", function(event){
	    	$("#cleanNozzleVid").get(0).pause();
			event.preventDefault();
			event.stopPropagation();
			waitForShowAndHide("#cleanNozzleModal");
			$("#closeNozzleInProgressModal").modal('show');
			
			//closeNozzle();  now done on hidden.bs.modal 
		});
	    
	    $(document).on("click", "#tinterPurgeButton", function(event) {	  
	    	if(cleanNozzleRequired()){
				if(tinterModel.startsWith("FM X")){
					$("#cleanNozzleInProgress").modal('show');
				}
				else{
					// fire event for nozzle cleaning
					$('#tinterCleanNozzle').click();
				}
	    	} else {
	    		$("#purgeInProgressModal").modal('show');
	    	}
	    });
	    
	    $(document).on("click", "#fmxCleanNozzle", function(event) {	
	    	console.log("clean nozzle started");
			$("#cleanNozzleInProgress").modal('show');
			rotateIcon($("#cleanNozzleInProgress"), $("#nozzleSpinner"));
	    });
	    
		//localhostConfig will be set if they have returned to landing page and have a tinter attached
		if($("#tinterPurgeAction_hasAutoNozzle").val()=="false"){ 
			$('#tinterCleanNozzle').hide();
		}
		
		$('#purgeInProgressModal').on('show.bs.modal',function(){
			console.log("starting purge");
			rotateIcon($("#purgeInProgressModal"), $("#purgeSpinner"));
		});
		
		if (tinterModel != null && tinterModel.startsWith("SANTINT")){
			// hide Clean Nozzle button for Santint tinters
			$("#tinterCleanNozzle").hide();
		}
		
		if (tinterModel != null && tinterModel.startsWith("FM X")){
			// show clean nozzle button
			$("#fmxCleanNozzle").removeClass("d-none");
		}
		
	});

	function addTinterAlertDangerWarnings(warningMsgs){
		if($("#tinterAlert").hasClass("d-none")) $("#tinterAlert").removeClass("d-none");
		if(!$("#tinterAlert").hasClass("alert-danger")) $("#tinterAlert").addClass("alert-danger");
		warningMsgs.forEach(function(msg){
			$("#tinterAlertList").append('<li>' + msg + '</li>');
		});
	}

	function removeTinterAlertDangerWarnings(){
		if(!$("#tinterAlert").hasClass("d-none")) $("#tinterAlert").addClass("d-none");
		if($("#tinterAlert").hasClass("alert-danger")) $("#tinterAlert").removeClass("alert-danger");
		$("#tinterAlertList").empty();
	}
    
</script>
</head>
	
	<body>
		<!-- including Header -->
		<s:include value="Header.jsp"></s:include>  
		
		<div class="container-fluid">
			<s:form action="tinterPurgeAction" validate="true"  theme="bootstrap">
				<div class="row mt-5 mb-2">
					<div class="col-sm-3">
 						<s:hidden name="reqGuid" value="%{reqGuid}"/>  
			 	    	<s:hidden name="hasAutoNozzle" value="%{autoNozzleCover}" />
			 	    	<s:hidden name="currUser" value="%{currentUser}" />
			 	    	<input id="tinterPurgeAction_tinterModel" type="hidden" name="tinterModel" value="${sessionScope[thisGuid].tinter.model}" />
					</div>
					<div class="col-sm-6">
						<div class="card card-body bg-light">
							<div><span class="badge badge-secondary mb-1" style="font-size: 1.2rem;"><s:text name="tinterPurge.purgeTinter"/></span></div>
							<h4 class="text-primary"><strong>${sessionScope[thisGuid].tinter.model}</strong></h4>
						</div>	
					</div>
					<div class="col-sm-3">
					</div>
				</div>
				<!-- do not display nozzle instructions for x - series -->
				<div class="row">
						<div class="col-sm-3">
						</div>
						<div class="col-sm-6">
							<div class="card card-body bg-light mb-3">
								<div class="text-danger" id="warnUser"></div>
								<s:if test="#session[reqGuid].tinter.model != null">
									<s:if test="#session[reqGuid].tinter.model.startsWith('FM X') && (#session[reqGuid].tinter.clrntSysId == 'CCE' || #session[reqGuid].tinter.clrntSysId == 'BAC')">
										<p class="lead"><s:text name="tinterPurge.tinterPrepXProTintWater"/></p>
										<p></p>
									</s:if>
									<s:if test="#session[reqGuid].tinter.model.startsWith('FM X') && (#session[reqGuid].tinter.clrntSysId == '844' || #session[reqGuid].tinter.clrntSysId == 'GIC')">
										<p class="lead"><s:text name="tinterPurge.tinterPrepXProTintThinner"/></p>
										<p></p>
									</s:if>
									<s:if test="!#session[reqGuid].tinter.model.startsWith('FM X')"> 
										<p class="lead"><s:text name="tinterPurge.cleanNozzleWithTool2"/></p>
										<p class="lead"><s:text name="tinterPurge.moistenSponge"/></p>
										<p class="lead"><s:text name="tinterPurge.clickPurge"/></p>
										<p></p>
									</s:if>
								</s:if>
						        
								<p class="lead" id="lastPurgeText">
									<s:hidden id="lastPurgeDate" name="lastPurgeDate" value="%{lastPurgeDate}"/>
									<s:hidden id="lastPurgeTime" name="lastPurgeTime" value="%{lastPurgeTime}"/>
									<s:text name="tinterPurge.lastPurgeDateTimeUser">
										<s:param><s:property value="lastPurgeDate" escapeHtml="true"/></s:param>
										<s:param><s:property value="lastPurgeTime" escapeHtml="true"/></s:param>
										<s:param><s:property value="lastPurgeUser" escapeHtml="true"/></s:param>
									</s:text>
								</p>
								<p></p>
							</div>
						</div>
						<div class="col-sm-3">
						</div>
					</div>
					<div class="row">
						<div class="col-sm-3">
							
						</div>
						<div class="col-sm-6">
							<div class="alert d-none" id="tinterAlert">
								<ul class="list-unstyled mb-0" id="tinterAlertList">
								</ul>
							</div>
						</div>
						<div class="col-sm-3">
		
						</div>
					</div>
				
				
				<div class="row">
					<div class="col-sm-3">
					</div>
					<div class="col-sm-2">	
						<button type="button" class="btn btn-primary" id="tinterCleanNozzle" data-toggle="modal" data-target="#cleanNozzleModal"><s:text name="tinterPurge.cleanNozzle"/></button>
  						<button type="button" class="btn btn-primary d-none" id="fmxCleanNozzle" data-toggle="modal" data-target="#cleanNozzleInProgress"><s:text name="tinterPurge.cleanNozzle"/></button>
  					</div>
					<div class="col-sm-2">	
						<button type="button" class="btn btn-primary center-block" id="tinterPurgeButton" autofocus="autofocus"><s:text name="global.purge"/></button>
  					</div>
					<div class="col-sm-2">	
		    			<s:submit cssClass="btn btn-secondary pull-right" value="%{getText('global.done')}" action="userCancelAction"/>
					</div>
					<div class="col-sm-3">
					</div>
		    	</div>
				<br>	
				<br>	
				<s:if test="%{#session[reqGuid].tinter.model != null && #session[reqGuid].tinter.model.startsWith('FM X')}"> 
					<div class="row">
						<div class="col-sm-3">
						</div>
						<div class="col-sm-6">
							<p class="bg-warning text-center"><strong><s:text name="tinterPurge.nozzleCleaningToolOrder"/></strong></p>
						</div>
						<div class="col-sm-3">
						</div>
					</div>
			    </s:if>
			    <!-- Clean Nozzle Modal Window -->
			    <div class="modal fade" aria-labelledby="cleanNozzleModal" aria-hidden="true"  id="cleanNozzleModal" role="dialog" data-backdrop="static" data-keyboard="false">
			    	<div class="modal-dialog" role="document">
						<div class="modal-content">
							<div class="modal-header">
								<h5 class="modal-title"><s:text name="tinterPurge.cleanNozzle"/></h5>
								<button type="button" class="close" data-dismiss="modal" aria-label="%{getText('global.close')}"  id="cleanNozzleX"><span aria-hidden="true">&times;</span></button>
							</div>
							<div class="modal-body">
								<video id="cleanNozzleVid" width="320" height="240" loop muted>
<%-- 									<source src="<s:url action='getNozzleVideoAction'/>" type="video/mp4">>Browser does not support video. Sorry --%>
									<source src="video/nozzle_open.mp4" type="video/mp4"><s:text name="tinterPurge.browserDoesNotSupportVideo"/>
									</source>
								</video>
								<p class="cleanNozzleText h5 pt-3"><s:text name="tinterPurge.cleanNozzleWithTool"/></p>
								<p class="cleanNozzleText h5"><s:text name="tinterPurge.moistenSponge2"/></p>
								<p class="cleanNozzleText h5"><s:text name="tinterPurge.removeCleaningTools"/></p>
								<br/>
								<p class="cleanNozzleText h5 bg-warning text-center pb-3" ><s:text name="tinterPurge.clickingDoneNozzleClose"/></p>
							</div>
							<div class="modal-footer">
								<button type="button" class="btn btn-primary center-block" id="cleanNozzleButton"><s:text name="global.done"/></button>
							</div>
						</div>
					</div>
				</div>
				
				<!-- FXM Clean Nozzle in Progress Modal Window -->
			    <div class="modal fade" aria-labelledby="cleanNozzleInProgress" aria-hidden="true"  id="cleanNozzleInProgress" role="dialog" data-backdrop="static" data-keyboard="false">
			    	<div class="modal-dialog" role="document">
						<div class="modal-content">
							<div class="modal-header">
								<em id="nozzleSpinner" class="fa fa-refresh mr-3 mt-1 text-muted" style="font-size: 1.5rem;"></em>
								<h5 class="modal-title"><s:text name="tinterPurge.cleanNozzle"/></h5>
								<button type="button" class="close" data-dismiss="modal" aria-label="%{getText('global.close')}" ><span aria-hidden="true">&times;</span></button>
							</div>
							<div class="modal-body">
								<p id="nozzle-message" class="h5"><s:text name="tinterPurge.cleanNozzleInProgress"/></p>
							</div>
							<div class="modal-footer">
							</div>
						</div>
					</div>
				</div>
				<div class="modal fade" aria-labelledby="fmxCleanNozzleModal" aria-hidden="true"  id="fmxCleanNozzleModal" role="dialog" data-backdrop="static" data-keyboard="false">
			    	<div class="modal-dialog" role="document">
						<div class="modal-content">
							<div class="modal-header">
								<h5 class="modal-title"><s:text name="tinterPurge.cleanNozzle"/></h5>
								<button type="button" class="close" data-dismiss="modal" aria-label="%{getText('global.close')}" ><span aria-hidden="true">&times;</span></button>
							</div>
							<div class="modal-body">
								<p id="cleanNozzle-message" class="h5"></p>
							</div>
							<div class="modal-footer">
								<button type="button" data-dismiss="modal" class="btn btn-primary center-block" id="fmxCleanNozzleButton"><s:text name="global.ok"/></button>
							</div>
						</div>
					</div>
				</div>			    

			    <!-- Close Nozzle in Progress Modal Window -->
			    <div class="modal fade" aria-labelledby="closeNozzleInProgressModal" aria-hidden="true"  id="closeNozzleInProgressModal" role="dialog" data-backdrop="static" data-keyboard="false">
			    	<div class="modal-dialog" role="document">
						<div class="modal-content">
							<div class="modal-header">
								<h5 class="modal-title"><s:text name="tinterPurge.closingNozzleCover"/></h5>
								<button type="button" class="close" data-dismiss="modal" aria-label="%{getText('global.close')}" ><span aria-hidden="true">&times;</span></button>
							</div>
							<div class="modal-body">
								<p font-size="4"><s:text name="tinterPurge.pleaseWaitNozzleCoverClose"/></p>
							</div>
							<div class="modal-footer">
							</div>
						</div>
					</div>
				</div>			    

			    <!-- Purge in Progress Modal Window -->
			    <div class="modal fade" aria-labelledby="purgeInProgressModal" aria-hidden="true"  id="purgeInProgressModal" role="dialog" data-backdrop="static" data-keyboard="false">
			    	<div class="modal-dialog" role="document">
						<div class="modal-content">
							<div class="modal-header">
								<em id="purgeSpinner" class="fa fa-refresh mr-3 mt-1 text-muted" style="font-size: 1.5rem;"></em>
								<h5 class="modal-title"><s:text name="tinterPurge.purgeAllInProgress"/></h5>
								<button type="button" class="close" data-dismiss="modal" aria-label="%{getText('global.close')}" ><span aria-hidden="true">&times;</span></button>
							</div>
							<div class="modal-body">
							
								<p id="progress-message" font-size="4"><s:text name="tinterPurge.pleaseWaitPurgeAll"/></p>
								<p id="abort-message" font-size="4" style="display:none; color:purple;font-weight:bold">
									<s:text name="global.pressAkeyToAbort"/>
								</p>
								<div class="progress-wrapper "></div>
								<ul class="list-unstyled" id="tinterProgressList">
										</ul>
							</div>
							<div class="modal-footer">
							</div>
						</div>
					</div>
				</div>			    

			    <!-- Tinter Socket Error Modal Window -->
			    <div class="modal fade" aria-labelledby="tinterSocketErrorModal" aria-hidden="true"  id="tinterSocketErrorModal" role="dialog">
			    	<div class="modal-dialog" role="document">
						<div class="modal-content">
							<div class="modal-header">
								<h5 class="modal-title"><s:text name="global.tinterError"/></h5>
								<button type="button" class="close" data-dismiss="modal" aria-label="%{getText('global.close')}" ><span aria-hidden="true">&times;</span></button>
							</div>
							<div class="modal-body">
								<p id="tinterSocketError" font-size="4"></p>
							</div>
							<div class="modal-footer">
								<button type="button" class="btn btn-primary" id="tinterSocketErrorButton" data-dismiss="modal" aria-label="%{getText('global.close')}" ><s:text name="global.close"/></button>
							</div>
						</div>
					</div>
				</div>			    
			    <!-- Tinter ErrorList Modal Window -->
			    <div class="modal fade" aria-labelledby="tinterErrorListModal" aria-hidden="true"  id="tinterErrorListModal" role="dialog">
			    	<div class="modal-dialog" role="document">
						<div class="modal-content">
							<div class="modal-header">
								<h5 class="modal-title" id="tinterErrorListTitle"><s:text name="global.tinterError"/></h5>
								<button type="button" class="close" data-dismiss="modal" aria-label="%{getText('global.close')}" ><span aria-hidden="true">&times;</span></button>
							</div>
							<div class="modal-body">
								<div class="progress-wrapper "></div>
								<div>
									<ul class="list-unstyled" id="tinterErrorList">
									</ul>
								</div>
								<p id="tinterErrorListSummary" font-size="4"></p>
							</div>
							<div class="modal-footer">
								<button type="button" class="btn btn-primary" id="tinterErrorListOK" data-dismiss="modal" aria-label="%{getText('global.close')}" ><s:text name="global.ok"/></button>
							</div>
						</div>
					</div>
				</div>
				<!-- Tinter Warning List Modal Window -->
			    <div class="modal fade" aria-labelledby="tinterWarningListModal" aria-hidden="true"  id="tinterWarningListModal" role="dialog">
			    	<div class="modal-dialog" role="document">
						<div class="modal-content">
							<div class="modal-header">
								<h5 class="modal-title" id="tinterWarningListTitle"><s:text name="global.tinterWarning"></s:text></h5>
								<button type="button" class="close" data-dismiss="modal" aria-label="%{getText('global.close)}"><span aria-hidden="true">&times;</span></button>
							</div>
							<div class="modal-body">
								<div>
									<ul class="p-0" id="tinterWarningList" style="list-style: none;">
									</ul>
								</div>
							</div>
							<div class="modal-footer">
								<button type="button" class="btn btn-primary" id="tinterWarningListOK" data-dismiss="modal" aria-label="%{getText('global.close)}" >
									<s:text name="global.ok"></s:text>
								</button>
							</div>
						</div>
					</div>
				</div>			    		    
			</s:form>
	
		</div>
		
				<br>
		<br>
		<br>

		<script>
		<!--
		  function HF_openSherwin() {
		    var popupWin = window.open("http://www.sherwin-williams.com", "Sherwin", "resizable=yes,toolbar=yes,menubar=yes,statusbar=yes,directories=no,location=yes,scrollbars=yes,width=800,height=600,left=10,top=10");
		    popupWin.focus();
		  }  
		  function HF_openLegal() {
		    var popupWin = window.open("http://www.sherwin-williams.com/terms/", "legal", "resizable=no,toolbar=no,menubar=yes,statusbar=no,directories=no,location=no,scrollbars=yes,width=800,height=600,left=10,top=10");
		    popupWin.focus();
		  }
		  function HF_openPrivacy() {
		    var popupWin = window.open("http://privacy.sherwin-williams.com/", "privacy", "resizable=yes,toolbar=no,menubar=yes,statusbar=no,directories=no,location=no,scrollbars=yes,width=640,height=480,left=10,top=10");
		    popupWin.focus();
		  }
		//-->
		</script>
		<!-- dummy div to clone -->
		<div id="progress-0" class="progress" style="display:none; margin:10px;">
	        <div id="bar-0" class="progress-bar" role="progressbar" aria-valuenow="0"
					 aria-valuemin="0" aria-valuemax="100" style=" width: 0%; background-color: blue">
					 <span></span>
	  		</div>
	  		<br/>
		</div>
		<!-- Including footer -->
		<s:include value="Footer.jsp"></s:include>
		
	</body>

