<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="/struts-tags" prefix="s" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<!doctype html>

<html lang="en">
	<head>
		<meta name="viewport" content="width=device-width, initial-scale=1">
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
		<title>New or Existing Job?</title>
		<link rel=StyleSheet href="css/bootstrap.min.css" type="text/css">
		<link rel=StyleSheet href="css/bootstrapxtra.css" type="text/css">
		<link rel=StyleSheet href="css/CustomerSherColorWeb.css" type="text/css"> 
		<link href="https://maxcdn.bootstrapcdn.com/font-awesome/4.7.0/css/font-awesome.min.css" rel="stylesheet">
		<script type="text/javascript" charset="utf-8" src="js/jquery-3.2.1.min.js"></script>
		<script type="text/javascript" charset="utf-8"	src="js/jquery-ui.js"></script>
		<script type="text/javascript" charset="utf-8"	src="js/popper.min.js"></script>
		<script type="text/javascript" charset="utf-8"	src="js/bootstrap.min.js"></script>
		<script type="text/javascript" charset="utf-8"	src="js/moment.min.js"></script>
		<script type="text/javascript" charset="utf-8" src="script/CustomerSherColorWeb.js"></script>
		<script type="text/javascript" charset="utf-8"	src="script/WSWrapper.js"></script>
		<script type="text/javascript" charset="utf-8"	src="script/Tinter.js"></script>
		<script type="text/javascript" charset="utf-8"	src="script/spectro.js"></script>
	
	<style>
		.dropdown-submenu {
		    position: relative;
		}
		.dropdown-submenu>.dropdown-menu {
		    top: 0;
		    left: 100%;
		    margin-top: -6px;
		    margin-left: -1px;
		    -webkit-border-radius: 0 6px 6px 6px;
		    -moz-border-radius: 0 6px 6px;
		    border-radius: 0 6px 6px 6px;
		}
		.dropdown-submenu:hover>.dropdown-menu {
	 	   display: block;
		}
		.dropdown-submenu>a:after {
		    display: block;
		    content: " ";
		    float: right;
		    width: 0;
		    height: 0;
		    border-color: transparent;
		    border-style: solid;
		    border-width: 5px 0 5px 5px;
		    border-left-color: #ccc;
		    margin-top: 5px;
		    margin-right: -10px;
		}
		.dropdown-submenu:hover>a:after {
		    border-left-color: #fff;
		}
		.dropdown-submenu.pull-left {
		    float: none;
		}
		.dropdown-submenu.pull-left>.dropdown-menu {
		    left: -100%;
		    margin-left: 10px;
		    -webkit-border-radius: 6px 0 6px 6px;
		    -moz-border-radius: 6px 0 6px 6px;
		    border-radius: 6px 0 6px 6px;
		}
		.bar {
		    margin-bottom: 14px;
		    margin-top: 13.5px;
		}
	 	.navbar-text{
	 		padding-bottom: 0px;
	 	}
	 	.dropdown-toggle::after {
		    display: none;
		}
	</style>
	<script type="text/javascript">
		
		var ws_spectro;
		var ws_tinter;
		var localhostConfig = null;
		var localhostSpectroConfig = null;
		var siteSpectroConfig = null;
		var wssCount = 0;
		var sendingTinterCommand = "false";
		var sendingSpectroCommand = "false";

		<s:if test="%{tinter.model != null && tinter.model != ''}">
			// setup local host config
			var canister_layout = null;
			localhostConfig = new Configuration("<s:property value="tinter.clrntSysId"/>","<s:property value="tinter.model"/>","<s:property value="tinter.serialNbr"/>",canister_layout);
		</s:if>
		
		<s:if test="%{spectro.model != null && spectro.model != ''}">
		// setup local host config			
			siteSpectroConfig = new SpectroConfig("<s:property value="spectro.model"/>","<s:property value="spectro.serialNbr"/>","USB");
			console.log("siteSpectroConfig model is "+ siteSpectroConfig.model);
			console.log("siteSpectroConfig serial is "+ siteSpectroConfig.serial);
			console.log("siteSpectroConfig port is "+ siteSpectroConfig.port);
			//localhostSpectroConfig = siteSpectroConfig;
		</s:if>

		function sessionTinterInfoCallback(){
			var alertErrors = [];
			var hasWarnings = false;
			// show tinter info and status in header tinter status popover
			$("#tinterStatusList").empty();
			if(sessionTinterInfo.tinterOnFile===true){
				$("#tinterStatusList").append("<li><strong>Colorant:</strong> " + sessionTinterInfo.clrntSysId + "</li>");
				$("#tinterStatusList").append("<li><strong>Model:</strong> " + sessionTinterInfo.model + "</li>");
				$("#tinterStatusList").append("<li><strong>S/N:</strong> " + sessionTinterInfo.serialNbr + "</li>");
				if(sessionTinterInfo.lastInitErrorList!=null && sessionTinterInfo.lastInitErrorList[0]!=null){
					sessionTinterInfo.lastInitErrorList.forEach(function(initError){
						$("#tinterStatusList").append('<li class="bg-danger"><strong>Init Error:</strong> '+initError+'</li>');
						//also add to alert message being built
						alertErrors.push("Init Error: "+initError);
					});
				} else {
					$("#tinterStatusList").append("<li><strong>Last Init:</strong> OK</li>");
				}

				// purge status
				if(sessionTinterInfo!=null && sessionTinterInfo.lastPurgeDate!=null){
					// convert session last purge date (which is a string) to a date
					var dateFromString = new Date(sessionTinterInfo.lastPurgeDate);
					var today = new Date();
					if (dateFromString.getFullYear()<today.getFullYear() || dateFromString.getMonth()<today.getMonth() || dateFromString.getDate()<today.getDate()){
						alertErrors.push("Tinter Purge is Required: Last done on " + moment(dateFromString).format('ddd MMM DD YYYY'));
						$("#tinterStatusList").append('<li class="bg-danger"><strong>Last Purge:</strong> ' + moment(dateFromString).format('ddd MMM DD YYYY') + "</li>");
					} else {
						$("#tinterStatusList").append("<li><strong>Last Purge:</strong> " + moment(dateFromString).format('ddd MMM DD YYYY') + "</li>");
					}
				} else {
					$("#tinterStatusList").append('<li class="bg-danger"><strong>Last Purge:</strong> Purge Never Done</li>');
					alertErrors.push("Tinter Purge is Required");
				}
				
				if(sessionTinterInfo.ecalOnFile===true)	$("#tinterStatusList").append("<li><strong>Ecal Status:</strong> OK</li>");
				else {
					hasWarnings=true;
					$("#tinterStatusList").append('<li class="bg-warning"><strong>Ecal Status:</strong> Warning! No Ecal on file</li>');
				}
				
				// Check Levels
				console.log("about to check levels");
				// Check for STOP! because there is not enough colorant in the tinter
				var stopList = checkColorantEmpty(sessionTinterInfo.canisterList);
				if(stopList[0]!=null){
					stopList.forEach(function(item){
						//add to alert message being built
						alertErrors.push("Colorant: " + item);
					});
				}
				var warnList = checkColorantLow(sessionTinterInfo.canisterList);
				if(warnList!=null && warnList[0]!=null){
					hasWarnings=true;
					warnList.forEach(function(item){
						if(item.lastIndexOf("Error",0)===0)	$("#tinterStatusList").append('<li class="bg-danger"><strong>Colorant: </strong>'+item+"</li>");
						else $("#tinterStatusList").append('<li class="bg-warning"><strong>Colorant: </strong>'+item+"</li>");
					});
				} else {
					$("#tinterStatusList").append("<li><strong>Colorant Levels:</strong> OK</li>");
				}
					
				//Show alerts in main alert section in middle of screen
				if(alertErrors[0]!=null){
					$("#tinterAlertList").empty();
					alertErrors.forEach(function(thisError){
						$("#tinterAlertList").append("<li>"+thisError+"</li>");
					});
					if($("#tinterAlert").hasClass("d-none")) $("#tinterAlert").removeClass("d-none");
				} else {
					if(!$("#tinterAlert").hasClass("d-none")) $("#tinterAlert").addClass("d-none");
				}

				//Change color from green to red or yellow if errors or warnings
				if(alertErrors[0]!=null) $('#tinterNotify').find('i').css('color','red');
				else if(hasWarnings===true) $('#tinterNotify').find('i').css('color','yellow');
				else $('#tinterNotify').find('i').css('color','lime green');
					
			} else {
				// no tinter on file
				$('#tinterNotify').hide();
				if(!$('#tinterBar').hasClass('d-none')){$('#tinterBar').addClass('d-none');}
			}
			
		}
		
		function checkTinterStatus(){
			console.log('before tinter status modal show');
			$("#checkTinterStatusInProgressModal").modal('show');
			var cmd = "InitStatus";
			var shotList = null;
			var configuration = null;
	    	var tintermessage = new TinterMessage(cmd,null,null,null,null);  
	    	var json = JSON.stringify(tintermessage);
			sendingTinterCommand = "true";
	    	ws_tinter.send(json);
		}
		
		function detectTinter(){
			$("#initTinterInProgressModal").modal('show');
			rotateIcon();
			var cmd = "Detect";
			var shotList = null;
			var configuration = null;
			var tintermessage = new TinterMessage(cmd,null,null,null,null);  
	    	var json = JSON.stringify(tintermessage);
			sendingTinterCommand = "true";
	    	ws_tinter.send(json);
		}
		
		function readLocalhostSpectroConfig(){
			var cmd = "ReadConfig";
	    	var clreyemodel = $('#spectroModel').val();
	    	var clreyeserial = $('#spectroSerial').val();
	    	console.log("ws_spectro.context is " + ws_spectro.deviceContext);
	    	
			var spectromessage = new SpectroMessage(cmd,clreyemodel, clreyeserial);
			spectromessage.messageName = "SpectroMessage";

			spectromessage.spectroConfig.port  = " USB";
			var json = JSON.stringify(spectromessage);
			sendingSpectroCommand = "true";
	    	ws_spectro.send(json);
		}
		
		function detectSpectro(){
			var cmd = "Detect";
	    	var clreyemodel = localhostSpectroConfig.model;
	    	var clreyeserial = localhostSpectroConfig.serial;
	    	console.log("ws_spectro.context is " + ws_spectro.deviceContext);
	    	
			var spectromessage = new SpectroMessage(cmd,clreyemodel, clreyeserial);
			spectromessage.messageName = "SpectroMessage";

			spectromessage.spectroConfig.port  = " USB";
			var json = JSON.stringify(spectromessage);
			sendingSpectroCommand = "true";
	    	ws_spectro.send(json);
		}
		
  	  	function GetCalStatus() {
  		  	var cmd = "GetCalStatusMinUntilCalExpiration";
	    	var clreyemodel = localhostSpectroConfig.model;
	    	var clreyeserial = localhostSpectroConfig.serial;
	    	console.log("ws_spectro.context is " + ws_spectro.deviceContext);
	    	
	    	var spectromessage = new SpectroMessage(cmd,clreyemodel, clreyeserial);
		    var json = JSON.stringify(spectromessage);
		    ws_spectro.send(json);
  		}
		
		function readLocalhostConfig(){

			var cmd = "ReadConfig";
			var shotList = null;
			var configuration = null;
			var tintermessage = new TinterMessage(cmd,null,null,null,null);  
	    	var json = JSON.stringify(tintermessage);
			sendingTinterCommand = "true";
	    	ws_tinter.send(json);
		}
		
		function saveSpectroConfigToSession(mymodel, myserial) {
			var myGuid = $( "#startNewJob_reqGuid" ).val();
			console.log("in saveSpectroConfigToSession, model is " + mymodel);
 			$.getJSON("stampSessionSpectroAction.action?reqGuid="+encodeURIComponent(myGuid)+"&spectroModel="+encodeURIComponent(mymodel)+"&spectroSerial="+encodeURIComponent(myserial), function(data){
 				//TODO anything to do here?  maybe check result of stamp...
 			});
		}
		function DetectFMResp(return_message){
			console.log("Processing FM Detect Response");
			var initErrorList=[];
			// log event
			var curDate = new Date();
			var myGuid = $( "#startNewJob_reqGuid" ).val();
			console.log(return_message);
			//status = 1, means, still trying serial ports so still in progress.
			if (return_message.errorMessage.indexOf("Initialization Done") == -1 && (return_message.errorNumber >= 0 ||
					 return_message.status == 1)) {
				//save				
				//no need to keep showing $("#initTinterInProgressModal").modal('show');
				$("#progress-message").text(return_message.errorMessage);
				console.log(return_message);
			}
			else if(return_message.errorMessage.indexOf("Initialization Done") >= 0){
				sendingTinterCommand = "false";
				// clear init error in session
				initErrorList = [];
				saveInitErrorsToSession($("#startNewJob_reqGuid").val(),initErrorList);
	    	
				sendTinterEvent(myGuid, curDate, return_message, null); 
			
				// Detected and no errors from tinter 
				waitForShowAndHide('#initTinterInProgressModal');
				
				// get session for tinter status
	    		getSessionTinterInfo($("#startNewJob_reqGuid").val(),sessionTinterInfoCallback);
				
			} else {
				sendingTinterCommand = "false";
				initErrorList = [];
	    		// get session for tinter status
	    
				if (return_message.errorNumber == -10500 ){
					// show warnings?
					sendTinterEvent(myGuid, curDate, return_message, null); 
					
					saveInitErrorsToSession($("#startNewJob_reqGuid").val(),initErrorList);
					waitForShowAndHide('#initTinterInProgressModal');

				} else {
					sendTinterEvent(myGuid, curDate, return_message, null); 
					waitForShowAndHide('#initTinterInProgressModal');
					//Show a modal with error message to make sure the user is forced to read it.
					$("#tinterErrorList").empty();
					initErrorList = [];
					if(return_message.errorList!=null && return_message.errorList[0]!=null){
						return_message.errorList.forEach(function(item){
							$("#tinterErrorList").append("<li>" + item.message + "</li>");
							initErrorList.push(item.message);
						});
					} else {
						initErrorList.push(return_message.errorMessage);
						$("#tinterErrorList").append("<li>" + return_message.errorMessage + "</li>");
					}
					$("#tinterErrorListTitle").text("Tinter Detect and Initialization Failed");
					$("#tinterErrorListSummary").text("Issues need to be resolved before you try to dispense formulas.");
					$("#tinterErrorListModal").modal('show');
					saveInitErrorsToSession($("#startNewJob_reqGuid").val(),initErrorList);
				}
				getSessionTinterInfo($("#startNewJob_reqGuid").val(),sessionTinterInfoCallback);
			}
    	}
		function DetectCorobResp(return_message){
			console.log("Processing Corob Detect Response");
			var initErrorList;
			// log event
			var curDate = new Date();
			var myGuid = $( "#startNewJob_reqGuid" ).val();
			sendTinterEvent(myGuid, curDate, return_message, null); 
			waitForShowAndHide('#initTinterInProgressModal');
			initErrorList = [];
			if(return_message.errorNumber == 0 && return_message.commandRC == 0){
				// Detected and no errors from tinter 			
				// clear init error in session			
				saveInitErrorsToSession($("#startNewJob_reqGuid").val(),initErrorList);
			} else {
				if (return_message.errorNumber == -10500 && return_message.commandRC == -10500){
					// show warnings?	
					saveInitErrorsToSession($("#startNewJob_reqGuid").val(),initErrorList);
				} else {
					//Show a modal with error message to make sure the user is forced to read it.
					$("#tinterErrorList").empty();
					
					if(return_message.errorList!=null && return_message.errorList[0]!=null){
						return_message.errorList.forEach(function(item){
							$("#tinterErrorList").append("<li>" + item.message + "</li>");
							initErrorList.push(item.message);
						});
					} else {
						initErrorList.push(return_message.errorMessage);
						$("#tinterErrorList").append("<li>" + return_message.errorMessage + "</li>");
					}
					$("#tinterErrorListTitle").text("Tinter Detect and Initialization Failed");
					$("#tinterErrorListSummary").text("Issues need to be resolved before you try to dispense formulas.");
					$("#tinterErrorListModal").modal('show');
					saveInitErrorsToSession($("#startNewJob_reqGuid").val(),initErrorList);
				}
			}
    	}
		function RecdMessage() {
			//Display WSS unsupported browser message
			wssBrowserCheck();

			var initErrorList = [];
			console.log("Received Message");
			//console.log("Message is " + ws_tinter.wsmsg);
			if(ws_tinter && ws_tinter.wserrormsg!=null && ws_tinter.wserrormsg!=""){
				if(sendingTinterCommand == "true"){
					// received an error from WSWrapper so we won't get any JSON result (probably no SWDeviceHandler)
					// If we are sending a ReadConfig command don't show any error (localhost has no devices)
					// If we are sending a Detect command, show as detect error
					//TODO Show a modal with error message to make sure the user is forced to read it.
// 					$("#detectError").text(ws_tinter.wserrormsg);
// 					$("#detectErrorModal").modal('show');
				} else {
					console.log("Received unsolicited error " + ws_tinter.wserrormsg);
					// so far this only happens when SWDeviceHandler is not running on localhost and when we created a new
					// ws wrapper it raises an error while the page is intially loaded.  For now wait until the command is 
					// sent to show the error (not everybody has a tinter)
				}
			} else {
				// is result (wsmsg) JSON?
				var isTintJSON = false;
				try{
					if(ws_tinter && ws_tinter.wsmsg!=null){
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
					switch (return_message.command) {
						case 'ReadConfig':
							if(return_message.errorNumber==-22){
								// -22 means no localhost configuration, so this station does not have a tinter
								$('li#tinterAdd').show();
								
							} else {
								// we have config, stuff in var
								localhostConfig = new Configuration();
								localhostConfig.colorantSystem = return_message.configuration.colorantSystem;
								localhostConfig.model = return_message.configuration.model;
								localhostConfig.serial = return_message.configuration.serial;
								console.log("localhostConfig is " + return_message.configuration.colorantSystem + " " + return_message.configuration.model + " " + return_message.configuration.serial);
								// save config info to sesssion on app server
								saveConfigToSession($("#startNewJob_reqGuid").val(),return_message.configuration.colorantSystem,return_message.configuration.model,return_message.configuration.serial);
								if(sessionTinterInfo.tinterOnFile===false){
									//Tell User Bad Config and to redo Add Tinter
									$("#tinterErrorList").empty();
									$("#tinterErrorList").append("<li>If you have a tinter on this station please redo Tinter Configure process.</li>");
									//Show it in a modal they can't go on
									$("#tinterErrorListTitle").text("Tinter Configuration Error");
									$("#tinterErrorListSummary").text("");
									$("#tinterErrorListModal").modal('show');
								} else {
									// Enable items in Tinter Menu
									$('#tinterNotify').show(); 
									if($('#tinterBar').hasClass('d-none')){$('#tinterBar').removeClass('d-none');}
									$('li#tinterInit').show();
									$('li#tinterPurge').show();
									$('li#colorantLevels').show();
									$('li#dispenseColorants').show();
									$('li#tinterEcal').show();
									if(localhostConfig!=null && localhostConfig.model!=null && localhostConfig.model!=""){
										//TODO check lastInit for tinter, if needed go detect
										checkTinterStatus();
									}
								}
							}
				    		sendingTinterCommand = "false";
							break;
						case 'InitStatus':
							// log event
							var curDate = new Date();
							var myGuid = $( "#startNewJob_reqGuid" ).val();
							sendTinterEvent(myGuid, curDate, return_message, null);
							console.log('before tinter status modal hide');
							waitForShowAndHide('#checkTinterStatusInProgressModal');

				    		sendingTinterCommand = "false";

							//Check Tinter Status to see if detect/init needed
							var today = new Date();
						    var YYYY = today.getFullYear();
						    var paddedMM = "0" + (today.getMonth()+1);
						    var MM = paddedMM.substring(paddedMM.length - 2);
						    var paddedDD = "0" + (today.getDate());
						    var DD = paddedDD.substring(paddedDD.length -2);
							var todayYYYYMMDD = YYYY+MM+DD;
							console.log("Compare " + todayYYYYMMDD + " to " + return_message.lastInitDate);
							if (todayYYYYMMDD>return_message.lastInitDate){
								// detect is not from today, redo
								detectTinter();
								
							} else {				
								// current detect, see if there were errors			}
								if(return_message.errorNumber == 0 && return_message.commandRC == 0){
									// Detected and no errors from tinter 
									//Update session to clear error in status area
									initErrorList = [];
									saveInitErrorsToSession($("#startNewJob_reqGuid").val(),initErrorList);
								} else {
									if (return_message.errorNumber == -10500 && return_message.commandRC == -10500){
										// TODO show warnings in status area
										// ... for now display in modal
										$("#tinterErrorList").empty();
										if(return_message.errorList!=null && return_message.errorList[0]!=null){
											return_message.errorList.forEach(function(item){
												$("#tinterErrorList").append("<li>" + item.message + "</li>");
											});
										} else {
											$("#tinterErrorList").append("<li>" + return_message.errorMessage + "</li>");
										}
										$("#tinterErrorListTitle").text("Prior Tinter Initialization Successful with Warnings");
										$("#tinterErrorListSummary").text("This was a prior attempt at initialization. You should try Initializing the tinter again.");
										$("#tinterErrorListModal").modal('show');
										
										initErrorList = [];
										saveInitErrorsToSession($("#startNewJob_reqGuid").val(),initErrorList);
									} else {
										//Show a modal with error message to make sure the user is forced to read it.
										$("#tinterErrorList").empty();
										initErrorList = [];
										if(return_message.errorList!=null && return_message.errorList[0]!=null){
											return_message.errorList.forEach(function(item){
												$("#tinterErrorList").append("<li>" + item.message + "</li>");
												initErrorList.push(item.message);
											});
										} else {
											$("#tinterErrorList").append("<li>" + return_message.errorMessage + "</li>");
											initErrorList.push(return_message.errorMessage);
										}
										$("#tinterErrorListTitle").text("Prior Tinter Initialization Failure");
										$("#tinterErrorListSummary").text("This was a prior attempt at initialization. Please try Initializing the tinter again.");
										$("#tinterErrorListModal").modal('show');
										//Also update session to show error in status area
										saveInitErrorsToSession($("#startNewJob_reqGuid").val(),initErrorList);
									} // end warning or error
								} // end success or no success test
								// get session for success or not
								getSessionTinterInfo($("#startNewJob_reqGuid").val(),sessionTinterInfoCallback);
							} // end else init current
							break;
						case 'Detect':
						case 'Init':
							console.log("recd detect or init response for " + localhostConfig.model);
							if(localhostConfig.model.indexOf("COROB") >= 0 || localhostConfig.model.indexOf("Corob") >= 0|| localhostConfig.model.indexOf("SIMULATOR") >= 0){
								DetectCorobResp(return_message);
								sendingTinterCommand = "false";
					    		// get session for tinter status
					    		getSessionTinterInfo($("#startNewJob_reqGuid").val(),sessionTinterInfoCallback);
							}
							else{
								DetectFMResp(return_message);								
							}
				    	
							break;
						default:
							//Not a response for our command...
							console.log("Message from different command is junk, throw it out");
						break;
					}
				}else console.log("Tinter Message was not JSON, threw it out");
			}
				
			//now process it if it is a spectro response message
			if(ws_spectro && ws_spectro.wserrormsg!=null && ws_spectro.wserrormsg!=""){
				if(sendingSpectroCommand == "true"){
					// received an error from WSWrapper so we won't get any JSON result (probably no SWDeviceHandler)
					// If we are sending a ReadConfig command don't show any error (localhost has no devices)
					// If we are sending a Detect command, show as detect error
					//TODO Show a modal with error message to make sure the user is forced to read it.
//	 				$("#detectError").text(ws_tinter.wserrormsg);
//	 				$("#detectErrorModal").modal('show');
				} else {
					console.log("Received unsolicited error " + ws_spectro.wserrormsg);
					// so far this only happens when SWDeviceHandler is not running on localhost and when we created a new
					// ws wrapper it raises an error while the page is intially loaded.  For now wait until the command is 
					// sent to show the error (not everybody has a tinter)
				}
			} else {
				// is result (wsmsg) JSON?
				var isSpectroJSON = false;
				try{
					if(ws_spectro && ws_spectro.wsmsg!=null && ws_spectro.wsmsg.length !=  0){
						var return_message=JSON.parse(ws_spectro.wsmsg);
						isSpectroJSON = true;
					}
				}
				catch(error){
                    console.log("Caught error is = " + error);
					console.log("Message is junk, throw it out");
					//console.log("Junk Message is " + ws_spectro.wsmsg);
				}
				
				if(isSpectroJSON){
					switch (return_message.command) {
						case 'GetCalStatusMinUntilCalExpiration':
							if (return_message.errorMessage!="") {
								// what to do here?
								//$("#errmsg").text(return_message.errorMessage);
								//DisplayError();
							} else {
								if (return_message.responseMessage.match(/^OK/)) {
									var goodMsg = return_message.responseMessage.split(" ");
									$("#calIntTemp").text(goodMsg[1]);
									var rmnTime = goodMsg[2];
									var theHrs = Math.floor(parseInt(rmnTime)/60);
									var theMin = parseInt(rmnTime) % 60;
									theHrs = theHrs.toString();
									theMin = theMin.toString();
									$("#calRemainTime").text(theHrs.concat(":",theMin));
									if (theHrs=="0") {
										$("#coloreyeStatusList").append("<li><strong>Calibration Status:</strong> " + theMin + " remaining</li>");
									} else {
										$("#coloreyeStatusList").append("<li><strong>Calibration Status:</strong> OK</li>");
									}
									$("#coloreyeStatusList").append("<li><strong>Internal Temp:</strong> " + goodMsg[1] + " C</li>");
									$('#coloreyeNotify').find('i').css('color','limegreen');
								} else {
									$("#coloreyeStatusList").append("<li><strong>Calibration Status:</strong> EXPIRED</li>");
									$('#coloreyeNotify').find('i').css('color','yellow');
								}
							}
							break;
						case 'ReadConfig':
							// TODO nothing it detected and no errors from spectro 
							
							localhostSpectroConfig = new SpectroConfig();
							localhostSpectroConfig.port = "USB";
							localhostSpectroConfig.model = return_message.spectroConfig.model;
							localhostSpectroConfig.serial = return_message.spectroConfig.serial;
							console.log("localhostSpectroConfig is " +  localhostSpectroConfig.model + " " + localhostSpectroConfig.serial + " USB");
							// save config info to sesssion on app server
							saveSpectroConfigToSession(return_message.spectroConfig.model,return_message.spectroConfig.serial);
							//update the spectro popover
							$("#coloreyeStatusList").append("<li><strong>Model:</strong> " + localhostSpectroConfig.model + "</li>");
							$("#coloreyeStatusList").append("<li><strong>S/N:</strong> " + localhostSpectroConfig.serial + "</li>");
							
							//now detect the spectro and see if it is connected.
							
							detectSpectro();
							break;
						case 'Detect':
							if(return_message.responseMessage==="true"){
								// Enable items in Spectro Menu
								if($('#colorEyeBar').hasClass('d-none')){$('#colorEyeBar').removeClass('d-none');}
								$('#coloreyeNotify').show();
								$('li#spectroCalibrate').show();
								$('li#spectroGetInfo').show();
								$("#coloreyeStatusList").append("<li><strong>Comm Status:</strong> CONNECTED</li>");
								GetCalStatus();
							} else {
								//TODO Show a modal with error message to make sure the user is forced to read it.
//	 							$("#detectError").text(return_message.errorMessage);
//	 							$("#detectErrorModal").modal('show');

								if (return_message.responseMessage==="notconfigured") {
									console.log("detection error " + return_message.errorCode + ", hiding coloreyeNotify" );
									$('#coloreyeNotify').hide();
									if(!$('#colorEyeBar').hasClass('d-none')){$('#colorEyeBar').addClass('d-none');}
								} else {
									//it was not detected, but IS configured. Show the popover, but do not show the Calibrate and GetInfo items.
									// save config info to sesssion on app server
// 									localhostSpectroConfig = new SpectroConfig();
// 									localhostSpectroConfig.port = "USB";
// 									localhostSpectroConfig.model = return_message.spectroConfig.model;
// 									localhostSpectroConfig.serial = return_message.spectroConfig.serial;
// 									saveSpectroConfigToSession(return_message.spectroConfig.model,return_message.spectroConfig.serial);
									$("#coloreyeStatusList").append("<li><strong>Comm Status:</strong> DISCONNECTED</li>");
									$('#coloreyeNotify').show();
									if($('#colorEyeBar').hasClass('d-none')){$('#colorEyeBar').removeClass('d-none');}
								}
							}
				    		sendingSpectroCommand = "false";
							break;
						
						default:
							//Not a response for our command...
							console.log("Message from different command is junk, throw it out");
					}
				} else {
					console.log("Spectro Message was not JSON, thrown out");
				}
			}
			
		} // end function RecdMessage
		
		//Enable popovers
		$(function(){
		    $("[data-toggle=popover]").popover({
		        html : true,
		        content: function() {
		          var content = $(this).attr("data-popover-content");
		          return $(content).children(".popover-body").html();
		        },
		        title: function() {
		          var title = $(this).attr("data-popover-content");
		          return $(title).children(".popover-heading").html();
		        }
		    });
		});
		
		//Adjust screen if notification message is hidden
		$(function(){
			if($('#tinterAlert').hasClass('d-none')){
				$('#shercolorimg').css('margin-bottom','90px');
			}
			
			//modifications for mobile device width
			if (screen && screen.width <= 768) {
			  $('.btn-lg').removeClass('btn-lg');
			  $('.bar').remove();
			  $('#logout').parent('li').before('<div class="dropdown-divider"></div>');
			  $('#coloreyemenu,#tintermenu').css('margin-left','30px');
			}
			
			//Allow dropdown third level to be opened onclick 
			  $('.dropdown-submenu a.sub').on("click", function(e){
			    $(this).next('ul').toggle();
			    e.stopPropagation();
			    e.preventDefault();	    
			  });
			  
			  
		});
		
		//Used to rotate loader icon in modals
		function rotateIcon(){
			let n = 0;
			console.log(status);
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
			
			$('#initTinterInProgressModal').one('hide.bs.modal',function(){
				$('#spinner').addClass('d-none');
	        	if(interval){clearInterval(interval);}
			});
		}
		
		function wssBrowserCheck(){
			if(wssCount === 0){
				if (ws_tinter || ws_spectro && $("#startNewJob_newSession").val() === "true") {
					if(!ws_tinter.validBrowser || !ws_spectro.validBrowser){
						wssCount++;
						$("#unsupportedBrowserModal").modal('show');
					}
				}
			}
		}

	</script>
  </head>
		    
	    <nav class="navbar navbar-dark bg-dark navbar-expand-md">			 
			   <button type="button" class="navbar-toggler" data-toggle="collapse" data-target="#navbarContent" aria-controls="navbarContent" aria-expanded="false">
		      		<span class="navbar-toggler-icon"></span>
		   		</button>
			   <div class="collapse navbar-collapse" id="navbarContent"> 
			   		<ul id="menuPanelL" class="nav navbar-nav navbar-left">
	         			<li class="nav-item dropdown">
	         				<button class="btn dropdown-toggle bg-dark pt-2" data-toggle="dropdown" type="button" aria-haspopup="true"
	        				aria-expanded="false"><i class="fa fa-bars" style="color: white;font-size: 1.4rem;"></i></button>
	        				<ul class="dropdown-menu">
	        					<li class="dropdown-item"><a class="dropdown-item" tabindex="-1" href='<s:url action="loginAction"><s:param name="reqGuid" value="%{reqGuid}"/></s:url>'><span class='fa fa-home pr-1'></span>Color Lookup</a></li>
	        					<div class="dropdown-divider"></div>
	        					<li class="dropdown-item dropdown-submenu">
	        						<a class="sub dropdown-item pr-1" tabindex="-1" href="#">Color Eye Menu</a>
	        						<ul class="dropdown-menu" id="coloreyemenu">
	<%--         						 <c:if test="${sessionScope[thisGuid].spectroModel != ''}">   --%>
	<%-- 								    	<li><a href='<s:url action="spectroCalibrateAction"><s:param name="reqGuid" value="%{reqGuid}"/></s:url>'><span class='fa fa-screenshot'></span> Calibrate</a></li> --%>
	<%-- 								    </c:if>  --%>
	<%-- 									<li><a href='<s:url action="spectroConfigureAction"><s:param name="reqGuid" value="%{reqGuid}"/></s:url>'><span class='fa fa-cog'></span> Configure</a></li> --%>
									    <li id="spectroCalibrate"><a class="dropdown-item" tabindex="-1" href='<s:url action="spectroCalibrateAction"><s:param name="reqGuid" value="%{reqGuid}"/></s:url>'><span class='fa fa-bullseye pr-1'></span> Calibrate</a></li>
										<li id="spectroGetInfo"><a class="dropdown-item" tabindex="-1" href='<s:url action="spectroGetInfoAction"><s:param name="reqGuid" value="%{reqGuid}"/></s:url>'><span class='fa fa-info-circle pr-1'></span> Color Eye Information</a></li>
										<li id="spectroConfig"><a class="dropdown-item" tabindex="-1" href='<s:url action="spectroConfigureAction"><s:param name="reqGuid" value="%{reqGuid}"/></s:url>'><span class='fa fa-cog pr-1'></span> Configure</a></li>
				        			</ul>
	       						</li> 
	        					<li class="dropdown-item dropdown-submenu">
	        						<a class="sub dropdown-item pr-1" tabindex="-1" href="#">Tinter Menu</a>
	        						<ul class="dropdown-menu" id="tintermenu">
								    	<li id="tinterPurge"><a class="dropdown-item" tabindex="-1" href='<s:url action="tinterPurgeAction"><s:param name="reqGuid" value="%{reqGuid}"/></s:url>'><span class='fa fa-tint pr-2'></span> Purge</a></li>
				        				<li id="colorantLevels"><a class="dropdown-item" tabindex="-1" href='<s:url action="processColorantLevelsAction"><s:param name="reqGuid" value="%{reqGuid}"/></s:url>'><span class='fa fa-align-left pr-1'></span> Colorant Levels</a></li>
				        				<li id="dispenseColorants"><a class="dropdown-item" tabindex="-1" href='<s:url action="displayDispenseColorantsAction"><s:param name="reqGuid" value="%{reqGuid}"/></s:url>'><span class='fa fa-random pr-1'></span>  Dispense Colorants</a></li>
								    	<li id="tinterInit"><a class="dropdown-item" tabindex="-1" href="#" onclick=detectTinter();><span class='fa fa-retweet pr-1'></span> Initialize Tinter</a></li>
								        <li id="tinterEcal"><a class="dropdown-item" tabindex="-1" href='<s:url action="ecalManagerAction"><s:param name="reqGuid" value="%{reqGuid}"/></s:url>' ><span class='fa fa-exchange pr-1'></span> Calibration Manager</a></li>
										<li id="tinterAdd"><a class="dropdown-item" tabindex="-1" href='<s:url action="tinterConfigAction"><s:param name="reqGuid" value="%{reqGuid}"/></s:url>'><span class='fa fa-cog pr-1'></span> Add New Tinter</a></li>
				        			</ul>
	       						</li>
	       						<li class="dropdown-item dropdown-submenu">
	        						<a class="sub dropdown-item pr-1" tabindex="-1" href="#">User Menu</a>
	        						<ul class="dropdown-menu" id="usermenu">
								    	<li id="changePwd"><a class="dropdown-item" tabindex="-1" href='<s:url action="passwordResetAction2"><s:param name="guid1" value="%{reqGuid}"/></s:url>'><span class='fa fa-cog pr-2'></span> Change Password</a></li>
				        			</ul>
	       						</li>
	        				</ul>
	        			</li>
	        				<!-- Popover links -->
	        			<li class="nav-item p-2 pl-3 pr-3" id="tinterBar"><span class="bar"><strong style="color: dimgrey;">|</strong></span></li>
	        			<li class="nav-item"><a href="javascript:void(0)" class="navbar-text" id="tinterNotify" data-container="body" data-toggle="popover" data-placement="bottom" data-html="true" data-trigger="focus" data-popover-content="#tinterPopover" data-original-title="Tinter Status" data-animation="true"><span><i class="fa fa-certificate" style="color: limegreen;"></i></span>  Tinter Status</a></li>
	        			<li class="nav-item p-2 pl-3 pr-3" id="colorEyeBar"><span class="bar"><strong style="color: dimgrey;">|</strong></span></li>
	        			<li class="nav-item"><a href="javascript:void(0)" class="navbar-text" id="coloreyeNotify" data-container="body" data-toggle="popover" data-placement="bottom" data-html="true" data-trigger="focus" data-popover-content="#coloreyePopover" data-original-title="Color-Eye Status" data-animation="true"><span><i class="fa fa-exclamation-circle" style="color: red;"></i></span>  Color-Eye Status</a></li>
	        			
	        			
	        			<!-- Content for Popovers -->
						<div class="d-none" id="tinterPopover">
						  <div class="popover-heading">
						    Tinter Status
						  </div>
						  <div class="popover-body">
							<ul class="list-unstyled" id="tinterStatusList">
							</ul>
	<%-- 				    	<p>Serial Number:  <s:property value="tinter.serialNbr"/></p> --%>
	<%-- 				    	<p>Model:  <s:property value="tinter.model"/></p> --%>
	<%-- 				    	<p>Last Detected:  <s:property value="tinter.lastDetected"/></p> --%>
	<%-- 						<p>Last Purged on  <s:property value="tinter.lastPurgeDate"/>  by  <s:property value="tinter.lastPurgeUser"/></p> --%>
						  </div>
						</div>
						<div class="d-none" id="coloreyePopover">
						  <div class="popover-heading">
						    Color-Eye Status
						  </div>
						  <div class="popover-body">
						  	<ul class="list-unstyled" id="coloreyeStatusList">
							</ul>
	<%-- 				    	<p>Serial Number:  <s:property value="localhostSpectroConfig.serial"/></p> --%>
	<%-- 				    	<p>Model:  <s:property value="localhostSpectroConfig.model"/></p> --%>
	<%-- 				    	<p>Comm Status:  <s:property value="spectroCommStatus"/></p> --%>
	<%-- 				    	<p>Calibration Status:  <s:property value="spectroCalStatus"/></p> --%>
	<%-- 				    	<p>Internal Temp:  <s:property value="spectroCalTemp"/></p> --%>
						  </div>
						</div>
			      	</ul>
			   		<s:set var="thisGuid" value="reqGuid" />
			     	<ul class="navbar-nav ml-auto">
			     		<li class="nav-item"><span class='navbar-text'>Logged in as ${sessionScope[thisGuid].firstName} ${sessionScope[thisGuid].lastName}</span></li>
			     		<li class="nav-item p-2 pl-3 pr-3"><span id="bar"><strong style="color: dimgrey;">|</strong></span></li>
			     		<li class="nav-item"><span class='navbar-text'>${sessionScope[thisGuid].customerName}</span></li>
			     		<s:url var="loUrl" action="logoutAction"><s:param name="reqGuid" value="%{thisGuid}"/></s:url>
			     		<li class="nav-item pl-3"><a class="nav-link" href="<s:property value="loUrl" />">Logout <span class='fa fa-sign-out' style="font-size: 18px;"></span></a></li> 
			     	</ul>
			   </div><!--/.nav-collapse -->
		</nav>

	  	</div>
	</div>
	
	<body>
		<div class="container-fluid">
		<div class="row mt-5">
				<div class="col-lg-2 col-md-2 col-xs-2 col-xs-0">
					
				</div>
				<div class="col-lg-8 col-md-8 col-sm-8 col-xs-12">
						<div class="text-center"><img id="shercolorimg" src="graphics/shercolor-lg.jpg" class="img-fluid" style="width: 33rem;"/></div>
				</div>
				<div class="col-lg-2 col-md-2 col-xs-2 col-xs-0">

				</div>
			</div>
			<div class="row">
				<div class="col-lg-4 col-md-3 col-xs-2 col-xs-0">
					
				</div>
				<div class="col-lg-4 col-md-6 col-sm-8 col-xs-12">
					<div class="alert alert-danger d-none" id="tinterAlert">
						<ul class="list-unstyled mb-0
						" id="tinterAlertList">
						</ul>
					 <!-- Put text here -->
					</div>
				</div>
				<div class="col-lg-4 col-md-3 col-xs-2 col-xs-0">

				</div>
			</div>
			<br>
			<div class="row" style="margin-bottom: 30px;">
				<div class="col-xl-4 col-lg-3 col-sm-1 col-xs-0">
				</div>
				<div class="col-xl-4 col-lg-6 col-sm-10 col-xs-12">
					<s:form action="startNewJob" method="post" align="center" theme="bootstrap" focusElement="startNewJobFocus">
						<div class="form-group" style="margin-top: 20px;">
							<s:hidden name="reqGuid" value="%{reqGuid}"/>
							<s:hidden name="newSession" value="%{newSession}"/>
							<s:hidden name="siteHasTinter" value="%{siteHasTinter}"/>
							<s:hidden name="sessionHasTinter" value="%{sessionHasTinter}"/>
							<s:hidden name="reReadLocalHostTinter" value="%{reReadLocalHostTinter}"/>
							<s:hidden name="siteHasSpectro" value="%{siteHasSpectro}"/>
							<s:hidden name="daysUntilPwdExp" value="%{daysUntilPwdExp}"/>
							<s:submit cssClass="btn btn-primary btn-lg pull-left" id="startNewJobFocus" autofocus="autofocus" value="Start New Job" action="startNewJob"/>
							<s:submit cssClass="btn btn-secondary btn-lg pull-right" value="Lookup Existing Job" action="listJobsAction"/>
				    	</div>  
			    	
				    <!-- Check Tinter Status in Progress Modal Window -->
				    <div class="modal fade" aria-labelledby="checkTinterStatusInProgressModal" aria-hidden="true"  id="checkTinterStatusInProgressModal" role="dialog" data-backdrop="static" data-keyboard="false">
				    	<div class="modal-dialog" role="document">
							<div class="modal-content">
								<div class="modal-header">
									<h5 class="modal-title">Checking Tinter Status</h5>
									<button type="button" class="close" data-dismiss="modal" aria-label="Close" ><span aria-hidden="true">&times;</span></button>
								</div>
								<div class="modal-body">
									<p font-size="4">A tinter has been configured for this station. Checking the status of the tinter now...</p>
								</div>
								<div class="modal-footer">
								</div>
							</div>
						</div>
					</div>			    
	
				    <!-- Tinter Detect in Progress Modal Window -->
				    <div class="modal fade" aria-labelledby="initTinterInProgressModal" aria-hidden="true"  id="initTinterInProgressModal" role="dialog" data-backdrop="static" data-keyboard="false">
				    	<div class="modal-dialog" role="document">
							<div class="modal-content">
								<div class="modal-header">
									<i id="spinner" class="fa fa-refresh mr-3 mt-1 text-muted" style="font-size: 1.5rem;"></i>
									<h5 class="modal-title">Tinter Detection and Initialization</h5>
									<button type="button" class="close" data-dismiss="modal" aria-label="Close" ><span aria-hidden="true">&times;</span></button>
								</div>
								<div class="modal-body">
									<p id="progress-message" font-size="4">Please wait while we detect and initialize the tinter...</p>
								</div>
								<div class="modal-footer">
								</div>
							</div>
						</div>
					</div>			    
	
				    <!-- Tinter ErrorList Modal Window -->
				    <div class="modal fade" aria-labelledby="tinterErrorListModal" aria-hidden="true"  id="tinterErrorListModal" role="dialog">
				    	<div class="modal-dialog" role="document">
							<div class="modal-content">
								<div class="modal-header">
									<h5 class="modal-title" id="tinterErrorListTitle">Tinter Error</h5>
									<button type="button" class="close" data-dismiss="modal" aria-label="Close" ><span aria-hidden="true">&times;</span></button>
								</div>
								<div class="modal-body">
									<div>
										<ul class="list-unstyled" id="tinterErrorList">
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
					<!-- Unsupported Browser Modal Window -->
				    <div class="modal fade" aria-labelledby="unsupportedBrowserModal" aria-hidden="true"  id="unsupportedBrowserModal" role="dialog">
				    	<div class="modal-dialog" role="document">
							<div class="modal-content">
								<div class="modal-header">
									<h5 class="modal-title" id="unsupportedBrowserTitle">Unsupported Browser</h5>
									<button type="button" class="close" data-dismiss="modal" aria-label="Close" ><span aria-hidden="true">&times;</span></button>
								</div>
								<div class="modal-body">
									<div class="alert alert-danger" role="alert" id="wsserror">You are currently using an unsupported browser, if you are using a <strong>Tinter</strong> or <strong>Color-Eye</strong>, please use <strong>Google Chrome</strong> (Version 43 and above) in order to assure proper Tinter/Color-Eye communication.</div>
								</div>
								<div class="modal-footer">
									<button type="button" class="btn btn-success" id="unsupportedBrowserOK" data-dismiss="modal" aria-label="Close" >OK</button>
								</div>
							</div>
						</div>
					</div>	
					<!-- Password Approaching Expiration Modal Window -->
				    <div class="modal fade" aria-labelledby="passwordExpirationModal" aria-hidden="true"  id="passwordExpirationModal" role="dialog">
				    	<div class="modal-dialog" role="document">
							<div class="modal-content">
								<div class="modal-header">
									<h5 class="modal-title" id="passwordExpirationTitle">Password Due to Expire</h5>
									<button type="button" class="close" data-dismiss="modal" aria-label="Close" ><span aria-hidden="true">&times;</span></button>
								</div>
								<div class="modal-body">
									<div class="alert alert-danger" role="alert" id="pswexperror">Your password is due to expire in <strong>${sessionScope[thisGuid].daysUntilPasswdExpire}</strong> days.  Please consider changing your password via the User Menu.</div>
								</div>
								<div class="modal-footer">
									<button type="button" class="btn btn-success" id="passwordExpirationOK" data-dismiss="modal" aria-label="Close" >OK</button>
								</div>
							</div>
						</div>
					</div>	    
					</s:form>
				</div>
				<div class="col-xl-4 col-lg-3 col-sm-1 col-xs-0">
					
				</div>
			</div>
		</div>
		<br>
		<br>
		<br>
		<script>
		$(document).ready(function(){
// 			$('.dropdown-submenu a.dropdown-toggle').on("click", function(e){
// 				$(this).next('ul').toggle();
// 				e.stopPropagation();
// 				e.preventDefault();
// 			});

			//localhostConfig will be set if they have returned to landing page and have a tinter attached
			if($("#startNewJob_sessionHasTinter").val()=="true"){
				console.log("session has tinter");
				$('#tinterNotify').show(); 
				if($('#tinterBar').hasClass('d-none')){$('#tinterBar').removeClass('d-none');}
				$('li#tinterInit').show();
				$('li#tinterPurge').show();
				$('li#colorantLevels').show();
				$('li#dispenseColorants').show();
				$('li#tinterEcal').show();
			} else {
				console.log("session DOES NOT HAVE tinter");
				$('#tinterNotify').hide(); 
				if(!$('#tinterBar').hasClass('d-none')){$('#tinterBar').addClass('d-none');}
				console.log("tinter notify should be hidden");
				$('li#tinterInit').hide();
				$('li#tinterPurge').hide();
				$('li#colorantLevels').hide();
				$('li#dispenseColorants').hide();
				$('li#tinterEcal').hide();
			}
			//reread last detect from websocket if new session or flag is set from previous init.
 			if( ($("#startNewJob_newSession").val()=="true" && $("#startNewJob_siteHasTinter").val()=="true") || $("#startNewJob_reReadLocalHostTinter").val()=="true" ) {
 	 			// new login with possible tinter or notified to re-read localhost tinter info
				ws_tinter = new WSWrapper("tinter");
				readLocalhostConfig();
 			} else {
 	 			// check if session has tinter, if so setup ws tinter
 	 			if ( $("#startNewJob_sessionHasTinter").val()=="true"){
 	 				ws_tinter = new WSWrapper("tinter");
 	 			}
 	 			// Get tinter session to post status info to header popover
 	 			getSessionTinterInfo($("#startNewJob_reqGuid").val(),sessionTinterInfoCallback);
			}
			
			if(localhostSpectroConfig!=null){ 
				console.log("localhostSpectroConfig is NOT null");			
				$('#coloreyeNotify').show(); 
				if($('#colorEyeBar').hasClass('d-none')){$('#colorEyeBar').removeClass('d-none');}
				$('li#spectroCalibrate').show();
				$('li#spectroGetInfo').show();
			} else {
				console.log("localhostSpectroConfig is null");
				$('#coloreyeNotify').hide(); 
				if(!$('#colorEyeBar').hasClass('d-none')){$('#colorEyeBar').addClass('d-none');}
				$('li#spectroCalibrate').hide();
				$('li#spectroGetInfo').hide();
			}
			//if($("#startNewJob_newSession").val()=="true" && $("#startNewJob_siteHasSpectro").val()=="true"){
			if($("#startNewJob_siteHasSpectro").val()=="true"){
				ws_spectro = new WSWrapper("coloreye");
				readLocalhostSpectroConfig();
			}
			//Add code to check days until password expiration.
 			var daysUntilPwdExpire = $('#startNewJob_daysUntilPwdExp').val();   
			if (daysUntilPwdExpire <= 7 && $("#startNewJob_newSession").val()=="true") {
				$('#passwordExpirationModal').modal('show');
			}
		});
		</script>
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
		
		<!-- Including footer -->
		<s:include value="Footer.jsp"></s:include>
		
	</body>
</html>	