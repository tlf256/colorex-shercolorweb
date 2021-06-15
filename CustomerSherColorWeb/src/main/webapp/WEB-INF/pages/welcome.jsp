<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="/struts-tags" prefix="s" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<!doctype html>

<html lang="en">
	<head>
		<meta name="viewport" content="width=device-width, initial-scale=1">
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
		<title><s:text name="welcome.newOrExistingJob"/></title>
		<link rel=StyleSheet href="css/bootstrap.min.css" type="text/css">
		<link rel=StyleSheet href="css/bootstrapxtra.css" type="text/css">
		<link rel=StyleSheet href="css/CustomerSherColorWeb.css" type="text/css"> 
		<link href="https://maxcdn.bootstrapcdn.com/font-awesome/4.7.0/css/font-awesome.min.css" rel="stylesheet">
		<script type="text/javascript" charset="utf-8" src="js/jquery-3.4.1.min.js"></script>
		<script type="text/javascript" charset="utf-8"	src="js/jquery-ui.js"></script>
		<script type="text/javascript" charset="utf-8"	src="js/popper.min.js"></script>
		<script type="text/javascript" charset="utf-8"	src="js/bootstrap.min.js"></script>
		<script type="text/javascript" charset="utf-8"	src="js/moment.min.js"></script>
		<script type="text/javascript" charset="utf-8" src="script/customershercolorweb-1.4.6.js"></script>
		<script type="text/javascript" charset="utf-8"	src="script/WSWrapper.js"></script>
		<script type="text/javascript" charset="utf-8"	src="script/tinter-1.4.7.js"></script>
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
		var configSessionSpectro = "false";
		var wssCount = 0;
		var sendingTinterCommand = "false";
		var sendingSpectroCommand = "false";
		var detectAttempt = 0;
		var layoutUpdateChosen = false;

		<s:if test="%{tinter.model != null && tinter.model != ''}">
			// setup local host config
			var canister_layout = null;
			localhostConfig = new Configuration("<s:property value="tinter.clrntSysId" escapeHtml="true"/>","<s:property value="tinter.model" escapeHtml="true"/>","<s:property value="tinter.serialNbr" escapeHtml="true"/>",canister_layout);
		</s:if>
		
		<s:if test="%{spectro.model != null && spectro.model != ''}">
		// setup local host config			
			siteSpectroConfig = new SpectroConfig("<s:property value="spectro.model" escapeHtml="true"/>","<s:property value="spectro.serialNbr" escapeHtml="true"/>","USB");
			console.log("siteSpectroConfig model is "+ siteSpectroConfig.model);
			console.log("siteSpectroConfig serial is "+ siteSpectroConfig.serial);
			console.log("siteSpectroConfig port is "+ siteSpectroConfig.port);
			configSessionSpectro = "true";
			//localhostSpectroConfig = siteSpectroConfig;
		</s:if>

		function sessionTinterInfoCallback(){
			var alertErrors = [];
			var hasWarnings = false;
			// show tinter info and status in header tinter status popover
			$("#tinterStatusList").empty();
			if(sessionTinterInfo.tinterOnFile===true){
				$("#tinterStatusList").append("<li><strong>" + '<s:text name="welcome.colorantColon"/>' + "</strong> " + sessionTinterInfo.clrntSysId + "</li>");
				$("#tinterStatusList").append("<li><strong>" + '<s:text name="global.modelColon"/>' + "</strong> " + sessionTinterInfo.model + "</li>");
				$("#tinterStatusList").append("<li><strong>" + '<s:text name="welcome.serialNbrColon"/>'  + "</strong> " + sessionTinterInfo.serialNbr + "</li>");
				if(sessionTinterInfo.lastInitErrorList!=null && sessionTinterInfo.lastInitErrorList[0]!=null){
					sessionTinterInfo.lastInitErrorList.forEach(function(initError){
						$("#tinterStatusList").append('<li class="bg-danger"><strong>' + '<s:text name="welcome.initErrorColon"/>' + '</strong> '+initError+'</li>');
						//also add to alert message being built
						alertErrors.push('<s:text name="welcome.initErrorColon"/>' + " " + initError);
					});
				} else {
					$("#tinterStatusList").append("<li><strong>" + '<s:text name="welcome.lastInitColon"/>' + "</strong> " + '<s:text name="global.ok"/>' + "</li>");
				}

				// purge status
				if(sessionTinterInfo!=null && sessionTinterInfo.lastPurgeDate!=null){
					// convert session last purge date (which is a string) to a date
					var dateFromString = new Date(sessionTinterInfo.lastPurgeDate);
					var today = new Date();
					if (dateFromString.getFullYear()<today.getFullYear() || dateFromString.getMonth()<today.getMonth() || dateFromString.getDate()<today.getDate()){
						alertErrors.push('<s:text name="global.tinterPurgeIsRequiredLastDoneOnDate"><s:param>' + moment(dateFromString).format('ddd MMM DD YYYY') + '</s:param></s:text>');
						$("#tinterStatusList").append('<li class="bg-danger"><strong><s:text name="welcome.lastPurgeColon"/></strong> ' + moment(dateFromString).format('ddd MMM DD YYYY') + "</li>");
					} else {
						$("#tinterStatusList").append('<li><strong><s:text name="welcome.lastPurgeColon"/></strong> ' + moment(dateFromString).format('ddd MMM DD YYYY') + "</li>");
					}
				} else {
					$("#tinterStatusList").append('<li class="bg-danger"><strong><s:text name="welcome.lastPurgeColon"/></strong> <s:text name="welcome.purgeNeverDone"/></li>');
					alertErrors.push("Tinter Purge is Required");
				}
				
				// leave off Ecal and Colorant Levels indicators for Santint tinters
				if (sessionTinterInfo.model != null && !sessionTinterInfo.model.includes("SANTINT")){
					if(sessionTinterInfo.ecalOnFile===true)	$("#tinterStatusList").append('<li><strong><s:text name="welcome.ecalStatusColon"/></strong> <s:text name="global.ok"/></li>');
					else {
						hasWarnings=true;
						$("#tinterStatusList").append('<li class="bg-warning"><strong><s:text name="welcome.ecalStatusColon"/></strong> <s:text name="welcome.warningNoEcalOnFile"/></li>');
					}
					
					// Check Levels
					console.log("about to check levels");
					// Check for STOP! because there is not enough colorant in the tinter
					var stopList = checkColorantEmpty(sessionTinterInfo.canisterList);
					if(stopList[0]!=null){
						stopList.forEach(function(item){
							//add to alert message being built
							alertErrors.push('<s:text name="welcome.colorantColon"/>' + item);
						});
					}
					var warnList = checkColorantLow(sessionTinterInfo.canisterList);
					if(warnList!=null && warnList[0]!=null){
						hasWarnings=true;
						warnList.forEach(function(item){
							if(item.lastIndexOf("Error",0)===0)	$("#tinterStatusList").append('<li class="bg-danger"><strong><s:text name="welcome.colorantColon"/> </strong>'+item+"</li>");
							else $("#tinterStatusList").append('<li class="bg-warning"><strong><s:text name="welcome.colorantColon"/> </strong>'+item+"</li>");
						});
					} else {
						$("#tinterStatusList").append('<li><strong><s:text name="welcome.colorantLevelsColon"/></strong> <s:text name="global.ok"/></li>');
					}
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
			// show layout update progress modal if the call is a result of choosing Update Layout in menu
			if (layoutUpdateChosen){
				$("#layoutUpdateInProgressModal").modal('show');
			} else {
				$("#initTinterInProgressModal").modal('show');
			}
			rotateIcon();
			var cmd = "Detect";
			var shotList = null;
			var configuration = null;
			var tintermessage = new TinterMessage(cmd,null,null,null,null);
			var json = JSON.stringify(tintermessage);
			sendingTinterCommand = "true";
	    	ws_tinter.send(json);
		}
		function AfterDetectTinterGetStatus(){
			var cmd = "InitStatus";
			$("#initTinterInProgressModal").modal('show');
			rotateIcon();
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
	    	console.log("READING LOCALHOST SPECTRO CONFIG");
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
	    	console.log("DETECTING SPECTRO");
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
	    	console.log("GETTING SPECTRO CALIBRATION STATUS");
	    	console.log("ws_spectro.context is " + ws_spectro.deviceContext);
	    	
	    	var spectromessage = new SpectroMessage(cmd,clreyemodel, clreyeserial);
		    var json = JSON.stringify(spectromessage);
		    ws_spectro.send(json);
  		}
		
		function readLocalhostConfig(){
			detectAttempt = 0;
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
					$("#tinterErrorListTitle").text('<s:text name="global.tinterDetectandInitializationFailed"/>');
					$("#tinterErrorListSummary").text('<s:text name="global.resolveIssuesBeforeDispense"/>');
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
			// hide whichever in progress modal was shown when detect command went out
			if (layoutUpdateChosen){
				waitForShowAndHide('#layoutUpdateInProgressModal');
			} else {
				waitForShowAndHide('#initTinterInProgressModal');
			}
			
			/* if tinter is corob custom, update canister layout. If user chose this through the menu, 
			show layout modal and reset flag, otherwise show any detect/init errors */
			if(return_message.configuration != null && return_message.configuration.model != null 
					&& return_message.configuration.model.includes("COROB CUSTOM")){
				updateColorantsTxt(myGuid, return_message, layoutUpdateChosen, showUpdatedLayout);
			}
			if (layoutUpdateChosen){
				layoutUpdateChosen = false;
			} else {
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
						$("#tinterErrorListTitle").text('<s:text name="global.tinterDetectandInitializationFailed"/>');
						$("#tinterErrorListSummary").text('<s:text name="global.resolveIssuesBeforeDispense"/>');
						$("#tinterErrorListModal").modal('show');
						saveInitErrorsToSession($("#startNewJob_reqGuid").val(),initErrorList);
					}
				}
			}
    	}
		
		function DetectSantintResp(return_message){
			console.log("Processing Santint Detect Response");
			var initErrorList;
			var curDate = new Date();
			var myGuid = $( "#startNewJob_reqGuid" ).val();
			var errorKey = return_message.errorMessage;
			// update error to english for logging
			return_message.errorMessage = log_english[errorKey];
			// log event
			sendTinterEvent(myGuid, curDate, return_message, null);
			// update error with internationalized message
			return_message.errorMessage = i18n[errorKey];
			waitForShowAndHide('#initTinterInProgressModal');
			initErrorList = [];
			
			// Detected and no errors from tinter
			if(return_message.errorNumber == 0 && return_message.commandRC == 0){
				// clear init errors in session			
				saveInitErrorsToSession($("#startNewJob_reqGuid").val(), initErrorList);
			} else {
				// Show a modal with error message to make sure the user is forced to read it. 
				$("#tinterErrorList").empty();
				
				initErrorList.push(return_message.errorMessage);
				$("#tinterErrorList").append("<li>" + return_message.errorMessage + "</li>");
				$("#tinterErrorListTitle").text('<s:text name="global.tinterDetectandInitializationFailed"/>');
				$("#tinterErrorListSummary").text('<s:text name="global.resolveIssuesBeforeDispense"/>');
				$("#tinterErrorListModal").modal('show');
				
				saveInitErrorsToSession($("#startNewJob_reqGuid").val(), initErrorList);
			}
			// get session for tinter status
    		getSessionTinterInfo($("#startNewJob_reqGuid").val(), sessionTinterInfoCallback);
    		// remove Ecal Manager and ColorantLevels from menu if they weren't already removed on page load because localhostConfig was still null
			$("#tinterEcal").hide();
    		$("#colorantLevels").hide();
    	}
		
		function showUpdatedLayout(data){
			$(".progress-wrapper").empty();
			var count = 1;
			// make sure list is in numerical order by tank position 
			data.tinterCanisterList.sort(function(a,b) {return Number(a.position) - Number(b.position)});
			
			// build canister layout graphic 
			data.tinterCanisterList.forEach(function(item){
				// clone dummy div, update info and add to modal
				var $clone = $("#progress-0").clone().removeClass('d-none');
				$clone.attr("id","progress-" + count);
				var $bar = $clone.children(".progress-bar");
				$bar.attr("id","bar-" + count);
				$bar.css("width", "100%");
				$clone.css("display", "block");
				$bar.children("span").text( item.position + " - " + item.clrntCode + " " + item.maxCanisterFill + " oz");
				var color_rgb = item.rgbHex;
				
				// if rgbHex value wasn't set (probably an NA canister), show as blank  
				if (color_rgb == null || color_rgb == ""){
					$bar.css("background-color", "#ffffff");
					$bar.children("span").css("color", "black");
					if (item.clrntCode == "NA"){
						$bar.children("span").text(item.position + " - " + '<s:text name="welcome.notInUse"/>');
					}
				} else {
					//set up text and background color
					switch(item.clrntCode){
					case "WHT":
					case "TW":
					case "W1":
						$bar.css("background-color", "#efefef");
						$bar.children("span").css("color", "black");
						break;
					case "OY":
					case "Y1":
					case "YGS":
						$bar.css("background-color", color_rgb);
						$bar.children("span").css("color", "black");
						break;
					default:
						$bar.css("background-color", color_rgb);
						$bar.children("span").css("color", "white");
						break;
					}
				}
				
				$clone.appendTo(".progress-wrapper");
				count++;
			})
			// pause to give the other modal time to close, then show the updated canister layout graphic
			setTimeout(function() { 
				$("#updatedCanisterLayoutModal").modal('show');
			}, 500);
		}
		
		
    	function UnsolicitedDetectResp(return_message){
      
        		$("#progress-message").text(return_message.errorMessage);
				console.log("unsolicited detect resp" + return_message);
				readLocalhostConfig();
            	
        	}
		function DetectAlfaFMXResp(return_message){
			console.log("Processing FMX Detect Response");
			var initErrorList=[];
			// log event
			var curDate = new Date();
			var myGuid = $( "#startNewJob_reqGuid" ).val();
			if ((return_message.errorMessage.indexOf("Initialization done") == -1 &&
					 return_message.errorMessage.indexOf("Initialization Done") == -1 )&& 
					 (return_message.errorNumber >= 0 ||
					 return_message.status == 1)) {
				
				$("#progress-message").text(return_message.errorMessage);
				if(detectAttempt < 85){
					setTimeout(
					  function() 
					  { //make sure we are not slamming everything
							AfterDetectTinterGetStatus(); // keep sending init status until you get something.
					  }, 5000);					detectAttempt++;
				}
				else{ // close up shop with this error.
					sendTinterEvent(myGuid, curDate, return_message, null); 
					waitForShowAndHide('#initTinterInProgressModal');
					$("#tinterErrorList").empty();
					return_message.errorMessage = "Timeout Waiting for Tinter Detect";
				
					initErrorList.push(return_message.errorMessage);
					$("#tinterErrorList").append("<li>" + return_message.errorMessage + "</li>");
				
					$("#tinterErrorListTitle").text('<s:text name="global.tinterDetectandInitializationFailed"/>');
					$("#tinterErrorListSummary").text('<s:text name="global.resolveIssuesBeforeDispense"/>');
					$("#tinterErrorListModal").modal('show');
					saveInitErrorsToSession($("#startNewJob_reqGuid").val(),initErrorList);
			
					}
				console.log(return_message);
			}
			
			else if(return_message.errorMessage.indexOf("Initialization Done") >= 0 ||
					return_message.errorMessage.indexOf("Initialization done") >= 0){
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
    
			
				sendTinterEvent(myGuid, curDate, return_message, null); 
				waitForShowAndHide('#initTinterInProgressModal');
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
				$("#tinterErrorListTitle").text('<s:text name="global.tinterDetectandInitializationFailed"/>');
				$("#tinterErrorListSummary").text('<s:text name="global.resolveIssuesBeforeDispense"/>');
				$("#tinterErrorListModal").modal('show');
				saveInitErrorsToSession($("#startNewJob_reqGuid").val(),initErrorList);
			}
			getSessionTinterInfo($("#startNewJob_reqGuid").val(),sessionTinterInfoCallback);
    	}
    	function DispenseProgressResp(){
    		$("#progress-message").text(return_message.errorMessage);
    		if (return_message.errorMessage.indexOf("Done") == -1 && (return_message.errorNumber == 0 ||
					 return_message.status == 1)) {
				//keep updating modal with status
				$("#progress-message").text(return_message.errorMessage);
				console.log(return_message);
			}
			else if(return_message.errorMessage.indexOf("Done") >= 0){
				sendingTinterCommand = "false";
				// clear init error in session
				
				sendTinterEvent(myGuid, curDate, return_message, null); 
			
				// Detected and no errors from tinter 
				waitForShowAndHide('#PurgeInProgressModal');
			
			}
			else if  (return_message.errorNumber != 0 ) {
				//error
				$("#progress-message").text(return_message.errorMessage);
				console.log(return_message);
				// Detected and error from tinter 
				waitForShowAndHide('#PurgeInProgressModal');
			}
				
        	}
		function RecdMessage() {
			//Display WSS unsupported browser message
			wssBrowserCheck();

			var initErrorList = [];
			console.log("Received Message");
			//console.log("Message is " + ws_tinter.wsmsg);
			if(ws_tinter && ws_tinter.wserrormsg!=null && ws_tinter.wserrormsg != ""){
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
					if(ws_tinter && ws_tinter.wsmsg!=null && ws_tinter.wsmsg.length > 0){
						var return_message=JSON.parse(ws_tinter.wsmsg);
						isTintJSON = true;
					}
					else{
							console.log("tinter message is blank.  skipping.");
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
									$("#tinterErrorList").append('<li><s:text name="welcome.ifTinterPleaseRedo"/></li>');
									//Show it in a modal they can't go on
									$("#tinterErrorListTitle").text('<s:text name="welcome.tinterConfigurationError"/>');
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
									$('li#tinterRemove').show();
									
									if(localhostConfig!=null && localhostConfig.model!=null && localhostConfig.model!=""){
										//TODO check lastInit for tinter, if needed go detect
										checkTinterStatus();
										// only show the update layout option if tinter model is corob custom
										if (localhostConfig.model.includes("COROB CUSTOM")){
											$('li#updateCanisterLayout').show();
										}
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

							if(localhostConfig != null && localhostConfig.model != null && 
									( localhostConfig.model.indexOf("FM X") >= 0 || localhostConfig.model.indexOf("ALFA") >= 0)){
									DetectAlfaFMXResp(return_message);
							}
							else if(localhostConfig != null && localhostConfig.model != null && (localhostConfig.model.indexOf("SANTINT") >= 0)){
								DetectSantintResp(return_message);
							}
							else if (todayYYYYMMDD>return_message.lastInitDate){
								// detect is not from today, redo
								detectTinter();
								
							}else {				
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
										$("#tinterErrorListTitle").text('<s:text name="welcome.priorTinterInitializationSuccessfulwithWarnings"/>');
										$("#tinterErrorListSummary").text('<s:text name="welcome.priorAttemptAtInitialization"/>');
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
										$("#tinterErrorListTitle").text('<s:text name="welcome.priorTinterInitializationSuccessfulwithWarnings"/>');
										$("#tinterErrorListSummary").text('<s:text name="welcome.priorAttemptAtInitialization"/>');
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
							if(localhostConfig != null && localhostConfig.model != null && localhostConfig.model.length > 0){
								console.log("recd detect or init response for " + localhostConfig.model);
								if(localhostConfig.model.indexOf("COROB") >= 0 || localhostConfig.model.indexOf("Corob") >= 0|| localhostConfig.model.indexOf("SIMULATOR") >= 0){
									DetectCorobResp(return_message);
									sendingTinterCommand = "false";
						    		// get session for tinter status
						    		getSessionTinterInfo($("#startNewJob_reqGuid").val(),sessionTinterInfoCallback);
								}
								else if(localhostConfig.model.indexOf("FM X") >= 0 || localhostConfig.model.indexOf("ALFA") >= 0){
									DetectAlfaFMXResp(return_message);
								}
								else if(localhostConfig.model.indexOf("SANTINT") >= 0){
									DetectSantintResp(return_message);
								}
								else{
									DetectFMResp(return_message);								
								}
							}
							else{
								UnsolicitedDetectResp(return_message);
								}
							break;
						case 'Config':
							console.log("Processing return message for unconfig");
							// notify user of tinter removal or if any errors occurred
							$('#initTinterInProgressModal #spinner').addClass('d-none');
							$('#initTinterInProgressModal .modal-body').text("Tinter removed.");
							$('#initTinterInProgressModal .modal-footer').html(
									'<button type="button" class="btn btn-primary" id="closeModal" onclick="deleteTinter();"><s:text name="global.ok"/></button>');
							
							// log tinter event
							var curDate = new Date();
							var myGuid = $( "#startNewJob_reqGuid" ).val();
							sendTinterEvent(myGuid, curDate, return_message, null);
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
						//console.log("recd msg: " + encodeURI(evt.data));
						var spectro_return_message=JSON.parse(ws_spectro.wsmsg);
						//try blanking out wsmsg and let it be filled on next valid spectro return call.
						ws_spectro.wsmsg = "";
						isSpectroJSON = true;
					} else {
						console.log("recd a msg, maybe a tinter msg, while the spectro wrapper was alive, skipping.")
						
					}
				}
				catch(error){
                    console.log("Caught error is = " + error);
					console.log("Message is junk, throw it out");
					//console.log("Junk Message is " + ws_spectro.wsmsg);
				}
				
				if(isSpectroJSON){
					switch (spectro_return_message.command) {
						case 'GetCalStatusMinUntilCalExpiration':
							if (spectro_return_message.errorMessage!="") {
								// what to do here?
								//$("#errmsg").text(return_message.errorMessage);
								//DisplayError();
							} else {
								if (spectro_return_message.responseMessage.match(/^OK/)) {
									var goodMsg = spectro_return_message.responseMessage.split(" ");
									$("#calIntTemp").text(goodMsg[1]);
									var rmnTime = goodMsg[2];
									var theHrs = Math.floor(parseInt(rmnTime)/60);
									var theMin = parseInt(rmnTime) % 60;
									theHrs = theHrs.toString();
									theMin = theMin.toString();
									$("#calRemainTime").text(theHrs.concat(":",theMin));
									if (theHrs=="0") {
										$("#coloreyeStatusList").append('<li><strong><s:text name="welcome.calStatusColon"/></strong> <s:text name="welcome.minRemaining"><s:param>' + theMin + '</s:param></s:text></li>');
									} else {
										$("#coloreyeStatusList").append('<li><strong><s:text name="welcome.calStatusColon"/></strong> <s:text name="global.ok"/></li>');
									}
									$("#coloreyeStatusList").append('<li><strong><s:text name="welcome.internalTempColon"/></strong> ' + goodMsg[1] + " C</li>");
									$('#coloreyeNotify').find('i').css('color','limegreen');
								} else {
									$("#coloreyeStatusList").append('<li><strong><s:text name="welcome.calStatusColon"/></strong> <s:text name="global.expired"/></li>');
									$('#coloreyeNotify').find('i').css('color','yellow');
								}
							}
							break;
						case 'ReadConfig':
							// TODO nothing it detected and no errors from spectro 
							
							localhostSpectroConfig = new SpectroConfig();
							localhostSpectroConfig.port = "USB";
							localhostSpectroConfig.model = spectro_return_message.spectroConfig.model;
							localhostSpectroConfig.serial = spectro_return_message.spectroConfig.serial;
							console.log("localhostSpectroConfig is " +  localhostSpectroConfig.model + " " + localhostSpectroConfig.serial + " USB");
							// save config info to sesssion on app server
							saveSpectroConfigToSession(spectro_return_message.spectroConfig.model,spectro_return_message.spectroConfig.serial);
							//update the spectro popover
							$("#coloreyeStatusList").append('<li><strong><s:text name="global.modelColon"/></strong> ' + localhostSpectroConfig.model + "</li>");
							$("#coloreyeStatusList").append('<li><strong><s:text name="welcome.serialNbrColon"/></strong> ' + localhostSpectroConfig.serial + "</li>");
							
							//now detect the spectro and see if it is connected.
							console.log("ready to detect spctro as we received a ReadConfig");
							detectSpectro();
							break;
						case 'Detect':
							if(spectro_return_message.responseMessage==="true"){
								// Enable items in Spectro Menu
								if($('#colorEyeBar').hasClass('d-none')){$('#colorEyeBar').removeClass('d-none');}
								$('#coloreyeNotify').show();
								$('li#spectroCalibrate').show();
								var colorEyeModel = localhostSpectroConfig.model;
				  				if(colorEyeModel === "Ci62+SWW" || colorEyeModel === "Ci62+SWS"){
				  					$('#spectroManageStoredMeasurements').removeClass('d-none');
				  				}
								$('li#spectroGetInfo').show();
								$("#coloreyeStatusList").append('<li><strong><s:text name="welcome.commStatusColon"/></strong> <s:text name="welcome.connected"/></li>');
								console.log("ready to get spctro calibration status as we received a Detect");
								GetCalStatus();
							} else {
								//TODO Show a modal with error message to make sure the user is forced to read it.
//	 							$("#detectError").text(return_message.errorMessage);
//	 							$("#detectErrorModal").modal('show');

								if (spectro_return_message.responseMessage==="notconfigured") {
									console.log("detection error " + spectro_return_message.errorCode + ", hiding coloreyeNotify" );
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
									$("#coloreyeStatusList").append('<li><strong><s:text name="welcome.commStatusColon"/></strong> <s:text name="welcome.disconnected"/></li>');
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
			var spinner = $('#spinner');
			var modal = $('#initTinterInProgressModal');
			if (layoutUpdateChosen){
				spinner = $('#layoutUpdateSpinner');
				modal = $('#layoutUpdateInProgressModal');
			}
			
			console.log(status);
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
			
			modal.on('hide.bs.modal',function(){
	        	if(interval){clearInterval(interval);}
	        	spinner.addClass('d-none');
			});
		}
		
		function wssBrowserCheck(){
			
			var wssCountFlag = false;
		
			if(wssCount === 0){
				if (ws_tinter || ws_spectro && $("#startNewJob_newSession").val() === "true") {
					if(ws_tinter){
						if(!ws_tinter.validBrowser){
							wssCountFlag = true;
						}
					}else if (ws_spectro) {
						if(!ws_spectro.validBrowser){
							wssCountFlag = true;
						}
					}
				}
			}
			
			if(wssCountFlag){
				wssCount++;
				$("#unsupportedBrowserModal").modal('show');
			}
		}
		
		function showRemoveTinterModal(){
			$('#removeTinterTxt').html('<h5><strong>'+ sessionTinterInfo.model +':</strong> <strong>'+ 
					sessionTinterInfo.serialNbr +'</strong></h5><h6><s:text name="welcome.tinterWillBeRemoved"/></h6>');
			$('#removeTinterModal').modal('show');
		}
		
		function unconfig_tinter(){
			var command = "Config";
			var configuration = new Configuration(null, "Standalone", null,	null);
			var tinterMessage = new TinterMessage(command, null, configuration, null, null);
			var json = JSON.stringify(tinterMessage);
	
			if (ws_tinter && ws_tinter.isReady == "false") {
				console.log("WSWrapper connection has been closed (timeout is defaulted to 5 minutes). Make a new WSWrapper.")
				ws_tinter = new WSWrapper("tinter");
			}
			ws_tinter.send(json);
		}
		
		function removeTinter(){
			console.log("Removing tinterconfig");
			$('#removeTinterModal').modal('hide');
			$('#initTinterInProgressModal').modal('show');
			$('#initTinterInProgressModal .modal-title').text('<s:text name="welcome.removeTinterStatus"/>');
			$('#initTinterInProgressModal .modal-body').text('<s:text name="welcome.removingTinter"/>');
			rotateIcon();
			
			setTimeout(function(){
				unconfig_tinter();
			}, 1000);
		}
		
		function deleteTinter(){
			$('#initTinterInProgressModal').modal('hide');
			// delete tinter from custWebDevices
			// regardless of tinterconfig.conf deletion
			window.location.href = "removeTinter?reqGuid=${reqGuid}";
		}
		
		//update user's language preference
		function updateLanguage(){
			var selectedLang = $("select[id='languageList'] option:selected").val();
			console.log(selectedLang);
			
			$.ajax({
				url : "updateLocale.action",
				type : "POST",
				data : {
					request_locale : selectedLang,
				},
				datatype : "json",
				async : true,
				success : function(data) {
					if (data.sessionStatus === "expired") {
						window.location = "/CustomerSherColorWeb/invalidLoginAction.action";
					} else {
						// reload page to update the language
						location.reload();
					}
				},
				error : function(err) {
					alert('<s:text name="global.failureColon"/>' + err);
				}
			});
		}

		$(document).ready(function() {
		    // update dropdown to display the language that the user picked if they have done so
		    var userLanguage = "${session['WW_TRANS_I18N_LOCALE']}";
		    if (userLanguage != null && userLanguage != ""){
			    $("#languageList").val(userLanguage);
		    } else {
		    	$("#languageList").val("en_US");
		    }
		});
		
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
	        					<li class="dropdown-item"><a class="dropdown-item" tabindex="-1" href='<s:url action="loginAction"><s:param name="reqGuid" value="%{reqGuid}"/></s:url>'><span class='fa fa-home pr-1'></span><s:text name="welcome.colorLookup"/></a></li>
	        					<div class="dropdown-divider"></div>
	        					<li class="dropdown-item dropdown-submenu">
	        						<a class="sub dropdown-item pr-1" tabindex="-1" href="#"><s:text name="welcome.colorEyeMenu"/></a>
	        						<ul class="dropdown-menu" id="coloreyemenu">
	<%--         						 <c:if test="${sessionScope[thisGuid].spectroModel != ''}">   --%>
	<%-- 								    	<li><a href='<s:url action="spectroCalibrateAction"><s:param name="reqGuid" value="%{reqGuid}"/></s:url>'><span class='fa fa-screenshot'></span> Calibrate</a></li> --%>
	<%-- 								    </c:if>  --%>
	<%-- 									<li><a href='<s:url action="spectroConfigureAction"><s:param name="reqGuid" value="%{reqGuid}"/></s:url>'><span class='fa fa-cog'></span> Configure</a></li> --%>
									    <li id="spectroCalibrate"><a class="dropdown-item" tabindex="-1" href='<s:url action="spectroCalibrateAction"><s:param name="reqGuid" value="%{reqGuid}"/></s:url>'><span class='fa fa-bullseye pr-1'></span> <s:text name="global.calibrate"/></a></li>
										
										<!-- 6/24/2019 BXW: This menu item has been added to incorporate new Ci62 functionality, -->
									    <li id="spectroManageStoredMeasurements" class="d-none"><a class="dropdown-item" tabindex="-1" href='<s:url action="spectroManageStoredMeasurementsAction"><s:param name="reqGuid" value="%{reqGuid}"/></s:url>'><span class='fa fa-edit'></span>  <s:text name="global.manageRemoteMeasurements"/></a></li>
										<li id="spectroVerifyDefaultSettings"><a class="dropdown-item" tabindex="-1" href='<s:url action="spectroVerifyDefaultSettingsAction"><s:param name="reqGuid" value="%{reqGuid}"/></s:url>'><span class='fa fa-check-square-o'></span>  <s:text name="global.verifyDefaultSpectroSettings"/></a></li>
										<li id="spectroGetInfo"><a class="dropdown-item" tabindex="-1" href='<s:url action="spectroGetInfoAction"><s:param name="reqGuid" value="%{reqGuid}"/></s:url>'><span class='fa fa-info-circle pr-1'></span> <s:text name="global.colorEyeInformation"/></a></li>
										<li id="spectroConfig"><a class="dropdown-item" tabindex="-1" href='<s:url action="spectroConfigureAction"><s:param name="reqGuid" value="%{reqGuid}"/></s:url>'><span class='fa fa-cog pr-1'></span> <s:text name="global.configure"/></a></li>
										<li id="compareColors"><a class="dropdown-item" tabindex="-1" href='<s:url action="spectroCompareColorsAction"><s:param name="reqGuid" value="%{reqGuid}"/></s:url>'><span class='fa fa-adjust pr-1'></span> <s:text name="welcome.compareTwoColors"/></a></li>
				        			</ul>
	       						</li> 
	        					<li class="dropdown-item dropdown-submenu">
	        						<a class="sub dropdown-item pr-1" tabindex="-1" href="#"><s:text name="welcome.tinterMenu"/></a>
	        						<ul class="dropdown-menu" id="tintermenu">
								    	<li id="tinterPurge"><a class="dropdown-item" tabindex="-1" href='<s:url action="tinterPurgeAction"><s:param name="reqGuid" value="%{reqGuid}"/></s:url>'><span class='fa fa-tint pr-2'></span> <s:text name="global.purge"/></a></li>
								    	<li id="updateCanisterLayout" style="display:none;"><a class="dropdown-item" tabindex="-1" href="#" onclick="layoutUpdateChosen=true; detectTinter();"><span class='fa fa-refresh pr-1'></span> <s:text name="welcome.updateCanisterLayout"/></a></li>
				        				<li id="colorantLevels"><a class="dropdown-item" tabindex="-1" href='<s:url action="processColorantLevelsAction"><s:param name="reqGuid" value="%{reqGuid}"/></s:url>'><span class='fa fa-align-left pr-1'></span> <s:text name="global.colorantLevels"/></a></li>
				        				<li id="dispenseColorants"><a class="dropdown-item" tabindex="-1" href='<s:url action="displayDispenseColorantsAction"><s:param name="reqGuid" value="%{reqGuid}"/></s:url>'><span class='fa fa-random pr-1'></span>  <s:text name="global.dispenseColorants"/></a></li>
								    	<li id="tinterInit"><a class="dropdown-item" tabindex="-1" href="#" onclick=detectTinter();><span class='fa fa-retweet pr-1'></span> <s:text name="welcome.initializeTinter"/></a></li>
								        <li id="tinterEcal"><a class="dropdown-item" tabindex="-1" href='<s:url action="ecalManagerAction"><s:param name="reqGuid" value="%{reqGuid}"/></s:url>' ><span class='fa fa-exchange pr-1'></span> <s:text name="global.calibrationManager"/></a></li>
										<li id="tinterAdd"><a class="dropdown-item" tabindex="-1" href='<s:url action="tinterConfigAction"><s:param name="reqGuid" value="%{reqGuid}"/></s:url>'><span class='fa fa-cog pr-1'></span> <s:text name="welcome.addNewTinter"/></a></li>
										<li id="tinterRemove"><a class="dropdown-item" tabindex="-1" href="#" onclick="showRemoveTinterModal();"><span class='fa fa-eject pr-1'></span> <s:text name="welcome.removeTinter"/></a></li>
				        			</ul>
	       						</li>
	       						<li class="dropdown-item dropdown-submenu">
	        						<a class="sub dropdown-item pr-1" tabindex="-1" href="#"><s:text name="welcome.printerMenu"/></a>
	        						<ul class="dropdown-menu" id="tintermenu">
								    	<li id="printerConfig"><a class="dropdown-item" tabindex="-1" href='<s:url action="printerConfigureAction"><s:param name="reqGuid" value="%{reqGuid}"/></s:url>'><span class='fa fa-cog pr-1'></span> <s:text name="global.configure"/></a></li>
				   					</ul>
	       						</li>
	       						<li class="dropdown-item dropdown-submenu">
	        						<a class="sub dropdown-item pr-1" tabindex="-1" href="#"><s:text name="welcome.userMenu"/></a>
	        						<ul class="dropdown-menu" id="usermenu">
								    	<li id="changePwd"><a class="dropdown-item" tabindex="-1" href='<s:url action="passwordResetAction2"><s:param name="guid1" value="%{reqGuid}"/></s:url>'><span class='fa fa-cog pr-2'></span> <s:text name="global.changePassword"/></a></li>
				        			</ul>
				        <!-- edo78r Adding new menu items for Help -->			
				        		<li class="dropdown-item dropdown-submenu">
	        						<a class="sub dropdown-item pr-1" tabindex="-1" href="#"><s:text name="welcome.helpMenu"/></a>
	        						<ul class="dropdown-menu" id="helpMenu">
	        							<li id="useCSW"><a class="dropdown-item" tabindex="-1" href='<s:url action="downloadPdfAction"><s:param name="reqGuid" value="%{reqGuid}"/><s:param name="pdfFile">SherColor_Web_Customer_Guide.pdf</s:param></s:url>'><span class='fa fa-info-circle pr-1'></span> <s:text name="welcome.howToUseCustomerSherColorWeb"/></a></li>
	        							<li id="setupXProtint"><a class="dropdown-item" tabindex="-1" href='<s:url action="downloadPdfAction"><s:param name="reqGuid" value="%{reqGuid}"/><s:param name="pdfFile">SherColor_Web_XProtint_Tinter_Installation_Guide.pdf</s:param></s:url>'><span class='fa fa-info-circle pr-1'></span> <s:text name="welcome.howToSetUpAXProtint"/></a></li>
	        							<li id="setupAccutinter"><a class="dropdown-item" tabindex="-1" href='<s:url action="downloadPdfAction"><s:param name="reqGuid" value="%{reqGuid}"/><s:param name="pdfFile">SherColor_Web_Accutinter_Installation_Guide.pdf</s:param></s:url>'><span class='fa fa-info-circle pr-1'></span> <s:text name="welcome.howToSetUpAnAccutinter"/></a></li>
								    	<li id="calibrateAccutinter"><a class="dropdown-item" tabindex="-1" href='<s:url action="downloadPdfAction"><s:param name="reqGuid" value="%{reqGuid}"/><s:param name="pdfFile">SherColor_Web_Fluid_Management_Calibration.pdf</s:param></s:url>'><span class='fa fa-info-circle pr-1'></span> <s:text name="welcome.howToCalibrateAnAccutinter"/></a></li>
								    	<li id="setupXrite"><a class="dropdown-item" tabindex="-1" href='<s:url action="downloadPdfAction"><s:param name="reqGuid" value="%{reqGuid}"/><s:param name="pdfFile">SherColor_Web_Color_Eye_Installation.pdf</s:param></s:url>'><span class='fa fa-info-circle pr-1'></span> <s:text name="welcome.howToSetUpAColorEye"/></a></li>
								    	<li id="setupCorob"><a class="dropdown-item" tabindex="-1" href='<s:url action="downloadPdfAction"><s:param name="reqGuid" value="%{reqGuid}"/><s:param name="pdfFile">SherColor_Web_Corob_Installation_Guide.pdf</s:param></s:url>'><span class='fa fa-info-circle pr-1'></span> <s:text name="welcome.howToSetUpACorob"/></a></li>
								    	<li id="calibrateCorob"><a class="dropdown-item" tabindex="-1" href='<s:url action="downloadPdfAction"><s:param name="reqGuid" value="%{reqGuid}"/><s:param name="pdfFile">SherColor_Web_Corob_Calibration.pdf</s:param></s:url>'><span class='fa fa-info-circle pr-1'></span> <s:text name="welcome.howToCalibrateACorob"/></a></li>
								    	<li id="setupDymo"><a class="dropdown-item" tabindex="-1" href='<s:url action="downloadPdfAction"><s:param name="reqGuid" value="%{reqGuid}"/><s:param name="pdfFile">SherColor_Web_Dymo_Install.pdf</s:param></s:url>'><span class='fa fa-info-circle pr-1'></span> <s:text name="welcome.howToSetUpADymoLabelPrinter"/></a></li>
								    	<li id="setupZebra"><a class="dropdown-item" tabindex="-1" href='<s:url action="downloadPdfAction"><s:param name="reqGuid" value="%{reqGuid}"/><s:param name="pdfFile">SherColor_Web_Zebra_Install.pdf</s:param></s:url>'><span class='fa fa-info-circle pr-1'></span> <s:text name="welcome.howToSetUpAZebraLabelPrinter"/></a></li>
				        			</ul>
				        			
	       						</li>
	        				</ul>
	        			</li>
	        				<!-- Popover links -->
	        			<li class="nav-item p-2 pl-3 pr-3" id="tinterBar"><span class="bar"><strong style="color: dimgrey;">|</strong></span></li>
	        			<li class="nav-item"><a href="javascript:void(0)" class="navbar-text" id="tinterNotify" data-container="body" data-toggle="popover" data-placement="bottom" data-html="true" data-trigger="focus" data-popover-content="#tinterPopover" data-original-title="Tinter Status" data-animation="true"><span><i class="fa fa-certificate" style="color: limegreen;"></i></span>  <s:text name="welcome.tinterStatus"/></a></li>
	        			<li class="nav-item p-2 pl-3 pr-3" id="colorEyeBar"><span class="bar"><strong style="color: dimgrey;">|</strong></span></li>
	        			<li class="nav-item"><a href="javascript:void(0)" class="navbar-text" id="coloreyeNotify" data-container="body" data-toggle="popover" data-placement="bottom" data-html="true" data-trigger="focus" data-popover-content="#coloreyePopover" data-original-title="Color-Eye Status" data-animation="true"><span><i class="fa fa-exclamation-circle" style="color: red;"></i></span>  <s:text name="welcome.colorEyeStatus"/></a></li>
	        			
	        			
	        			<!-- Content for Popovers -->
						<div class="d-none" id="tinterPopover">
						  <div class="popover-heading">
						    <s:text name="welcome.tinterStatus"/>
						  </div>
						  <div class="popover-body">
							<ul class="list-unstyled" id="tinterStatusList">
							</ul>
	<%-- 				    	<p><s:text name="global.serialNumberColon"/>  <s:property value="tinter.serialNbr"/></p> --%>
	<%-- 				    	<p><s:text name="global.modelColon"/>  <s:property value="tinter.model"/></p> --%>
	<%-- 				    	<p><s:text name="welcome.lastDetectedColon"/>  <s:property value="tinter.lastDetected"/></p> --%>
	<%-- 						<p><s:text name="tinterPurge.lastPurgeDateUser"><s:param><s:property value="tinter.lastPurgeDate"/></s:param><s:param><s:property value="tinter.lastPurgeUser"/></s:param></s:text></p> --%>
						  </div>
						</div>
						<div class="d-none" id="coloreyePopover">
						  <div class="popover-heading">
						    <s:text name="welcome.colorEyeStatus"/>
						  </div>
						  <div class="popover-body">
						  	<ul class="list-unstyled" id="coloreyeStatusList">
							</ul>
	<%-- 				    	<p><s:text name="global.serialNumberColon"/>  <s:property value="localhostSpectroConfig.serial"/></p> --%>
	<%-- 				    	<p><s:text name="global.modelColon"/>  <s:property value="localhostSpectroConfig.model"/></p> --%>
	<%-- 				    	<p><s:text name="welcome.commStatusColon"/>  <s:property value="spectroCommStatus"/></p> --%>
	<%-- 				    	<p><s:text name="welcome.calStatusColon"/>  <s:property value="spectroCalStatus"/></p> --%>
	<%-- 				    	<p><s:text name="welcome.internalTempColon"/>  <s:property value="spectroCalTemp"/></p> --%>
						  </div>
						</div>
			      	</ul>
			   		<s:set var="thisGuid" value="reqGuid" />
			     	<ul class="navbar-nav ml-auto">
			     		<li class="nav-item">
			     			<span class='navbar-text'>
			     				<s:text name="global.loggedInAsFirstNameLastName">
									<s:param>${sessionScope[thisGuid].firstName}</s:param>
									<s:param>${sessionScope[thisGuid].lastName}</s:param>
								</s:text>
							</span>
						</li>
			     		<li class="nav-item p-2 pl-3 pr-3"><span id="bar"><strong style="color: dimgrey;">|</strong></span></li>
			     		<li class="nav-item"><span class='navbar-text'>${sessionScope[thisGuid].customerName}</span></li>
			     		<li class="nav-item p-2 pl-3 pr-3"><span id="bar"><strong style="color: dimgrey;">|</strong></span></li>
			     		<li class="nav-item"><select class="bg-dark navbar-text" id="languageList" onchange="updateLanguage();">
							    <option value="en_US">English</option>
							    <option value="es_ES" class="d-none">Español</option>
							    <option value="zh_CN">中文</option>
						    </select>
						</li>
						<s:url var="loUrl" action="logoutAction"><s:param name="reqGuid" value="%{thisGuid}"/></s:url>
			     		<li class="nav-item pl-3"><a class="nav-link" href="<s:property value="loUrl" />"><s:text name="global.logout"/> <span class='fa fa-sign-out' style="font-size: 18px;"></span></a></li> 
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
							<s:submit cssClass="btn btn-primary btn-lg pull-left" id="startNewJobFocus" autofocus="autofocus" value="%{getText('welcome.startNewJob')}" action="startNewJob"/>
							<s:submit cssClass="btn btn-secondary btn-lg pull-right" value="%{getText('welcome.lookupExistingJob')}" action="listJobsAction"/>
				    	</div>  
			    	
			    	<!-- Updated Canister Layout Modal Window -->
				    <div class="modal fade" aria-labelledby="updatedCanisterLayoutModal" aria-hidden="true"  id="updatedCanisterLayoutModal" role="dialog">
				    	<div class="modal-dialog" role="document">
							<div class="modal-content">
								<div class="modal-header">
									<h5 class="modal-title" id="updatedCanisterLayoutTitle"><s:text name="welcome.updatedCanisterLayout"/></h5>
									<button type="button" class="close" data-dismiss="modal" aria-label="Close" ><span aria-hidden="true">&times;</span></button>
								</div>
								<div class="modal-body">
									<div>
										<div class="progress-wrapper"></div>
									</div>
									<br>
									<p><s:text name="welcome.updatedCanisterLayoutMessage"/></p>
								</div>
								<div class="modal-footer">
									<button type="button" class="btn btn-primary" id="updatedCanisterLayoutOK" data-dismiss="modal" aria-label="Close" ><s:text name="global.ok"/></button>
								</div>
							</div>
						</div>
					</div>	
					<div id="progress-0" class="progress d-none" style="margin:10px;">
						<div id="bar-0" class="progress-bar" role="progressbar" aria-valuenow="0"
								 aria-valuemin="0" aria-valuemax="100" style="width: 0%; background-color: blue; text-align: left">
								 <span style="padding-left: 45%"></span>
				  		</div>
					</div>
					
				    <!-- Check Tinter Status in Progress Modal Window -->
				    <div class="modal fade" aria-labelledby="checkTinterStatusInProgressModal" aria-hidden="true"  id="checkTinterStatusInProgressModal" role="dialog" data-backdrop="static" data-keyboard="false">
				    	<div class="modal-dialog" role="document">
							<div class="modal-content">
								<div class="modal-header">
									<h5 class="modal-title"><s:text name="welcome.checkingTinterStatus"/></h5>
									<button type="button" class="close" data-dismiss="modal" aria-label="Close" ><span aria-hidden="true">&times;</span></button>
								</div>
								<div class="modal-body">
									<p font-size="4"><s:text name="welcome.tinterConfigured"/></p>
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
									<h5 class="modal-title"><s:text name="global.tinterDetectionAndInitialization"/></h5>
									<button type="button" class="close" data-dismiss="modal" aria-label="Close" ><span aria-hidden="true">&times;</span></button>
								</div>
								<div class="modal-body">
									<p id="progress-message" font-size="4"><s:text name="global.pleaseWaitDetectInitTinter"/></p>
								</div>
								<div class="modal-footer">
								</div>
							</div>
						</div>
					</div>
					
					<!-- Tinter Canister Layout Update in Progress Modal Window -->
				    <div class="modal fade" aria-labelledby="layoutUpdateInProgressModal" aria-hidden="true"  id="layoutUpdateInProgressModal" role="dialog" data-backdrop="static" data-keyboard="false">
				    	<div class="modal-dialog" role="document">
							<div class="modal-content">
								<div class="modal-header">
									<i id="layoutUpdateSpinner" class="fa fa-refresh mr-3 mt-1 text-muted" style="font-size: 1.5rem;"></i>
									<h5 class="modal-title"><s:text name="welcome.tinterDetectionAndLayoutUpdate"/></h5>
									<button type="button" class="close" data-dismiss="modal" aria-label="Close" ><span aria-hidden="true">&times;</span></button>
								</div>
								<div class="modal-body">
									<p id="progress-message" font-size="4"><s:text name="welcome.corobTECHIsClosedToSaveLayout"/></p>
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
									<h5 class="modal-title" id="tinterErrorListTitle"><s:text name="global.tinterError"/></h5>
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
									<button type="button" class="btn btn-primary" id="tinterErrorListOK" data-dismiss="modal" aria-label="Close" ><s:text name="global.ok"/></button>
								</div>
							</div>
						</div>
					</div>	
					<!-- Unsupported Browser Modal Window -->
				    <div class="modal fade" aria-labelledby="unsupportedBrowserModal" aria-hidden="true"  id="unsupportedBrowserModal" role="dialog">
				    	<div class="modal-dialog" role="document">
							<div class="modal-content">
								<div class="modal-header">
									<h5 class="modal-title" id="unsupportedBrowserTitle"><s:text name="global.unsupportedBrowser"/></h5>
									<button type="button" class="close" data-dismiss="modal" aria-label="Close" ><span aria-hidden="true">&times;</span></button>
								</div>
								<div class="modal-body">
									<div class="alert alert-danger" role="alert" id="wsserror"><s:text name="global.currentlyUsingUnsupportedBrowser"/></div>
								</div>
								<div class="modal-footer">
									<button type="button" class="btn btn-success" id="unsupportedBrowserOK" data-dismiss="modal" aria-label="Close" ><s:text name="global.ok"/></button>
								</div>
							</div>
						</div>
					</div>	
					<!-- Password Approaching Expiration Modal Window -->
				    <div class="modal fade" aria-labelledby="passwordExpirationModal" aria-hidden="true"  id="passwordExpirationModal" role="dialog">
				    	<div class="modal-dialog" role="document">
							<div class="modal-content">
								<div class="modal-header">
									<h5 class="modal-title" id="passwordExpirationTitle"><s:text name="welcome.passwordDuetoExpire"/></h5>
									<button type="button" class="close" data-dismiss="modal" aria-label="Close" ><span aria-hidden="true">&times;</span></button>
								</div>
								<div class="modal-body">
									<div class="alert alert-danger" role="alert" id="pswexperror">
										<s:text name="welcome.yourPasswordDueToExpire">
	 										<s:param>${sessionScope[thisGuid].daysUntilPasswdExpire}</s:param>
										</s:text>
										
									</div>
								</div>
								<div class="modal-footer">
									<button type="button" class="btn btn-success" id="passwordExpirationOK" data-dismiss="modal" aria-label="Close" ><s:text name="global.ok"/></button>
								</div>
							</div>
						</div>
					</div>
					<!-- Remove Tinter Modal -->
				    <div class="modal fade" aria-labelledby="removeTinterModal" aria-hidden="true"  id="removeTinterModal" role="dialog">
				    	<div class="modal-dialog" role="document">
							<div class="modal-content">
								<div class="modal-header">
									<h5 class="modal-title" id=""><s:text name="welcome.removeTinter"/></h5>
									<button type="button" class="close" data-dismiss="modal" aria-label="Close" ><span aria-hidden="true">&times;</span></button>
								</div>
								<div class="modal-body">
									<div id="removeTinterTxt"></div>
								</div>
								<div class="modal-footer">
									<button type="button" id="rmTinter" class="btn btn-danger" onclick="removeTinter();"><s:text name="global.yes"/></button>
									<button type="button" class="btn btn-secondary" id="closeModal" data-dismiss="modal" aria-label="Close" ><s:text name="global.no"/></button>
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

			$.ajax({
		  		url: "spectroObtainAllStoredMeasurements.action",
		  		type: "POST",
		  		data: {
		  			reqGuid : "${reqGuid}"
		  		},
		  		dataType: "json",
		  		async: true,
		  		success: function (data) {
		  			if(data.sessionStatus === "expired"){
		  	        	window.location = "/CustomerSherColorWeb/invalidLoginAction.action";
		  	        } else{
		  				if(data.measurementTable.length > 0){
		  					$('#spectroManageStoredMeasurements').removeClass('d-none');
		  				} else {
		  					$('#spectroManageStoredMeasurements').addClass('d-none');
		  				}
		  	   								
					}
		  		},
		  		error: function(err){
		  			console.log(JSON.stringify(err));
		  		}
		  	});
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
				$('li#tinterRemove').show();
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
				$('li#tinterRemove').hide();
			}
			// only show the update layout option if tinter model is corob custom
			if (localhostConfig != null && localhostConfig.model != null && localhostConfig.model.includes("COROB CUSTOM")){
				$('li#updateCanisterLayout').show();
			} else {
				$('li#updateCanisterLayout').hide();
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
 	 				// Get tinter session to post status info to header popover
 	 	 			getSessionTinterInfo($("#startNewJob_reqGuid").val(),sessionTinterInfoCallback);
 	 			}
			}
			
			if(localhostSpectroConfig!=null || configSessionSpectro=="true"){ 
				console.log("localhostSpectroConfig is NOT null");			
				$('#coloreyeNotify').show(); 
				if($('#colorEyeBar').hasClass('d-none')){$('#colorEyeBar').removeClass('d-none');}
				$('li#spectroCalibrate').show();
				$('li#spectroGetInfo').show();
				$('li#spectroVerifyDefaultSettings').show();
			} else {
				console.log("localhostSpectroConfig is null");
				$('#coloreyeNotify').hide(); 
				if(!$('#colorEyeBar').hasClass('d-none')){$('#colorEyeBar').addClass('d-none');}
				$('li#spectroCalibrate').hide();
				$('li#spectroGetInfo').hide();
				$('li#spectroVerifyDefaultSettings').hide;
			}
			//if($("#startNewJob_newSession").val()=="true" && $("#startNewJob_siteHasSpectro").val()=="true"){
			//PSCWEB-330 CSW - Color Eye Status does not display after configuration - BKP - added
			// an "or" to check if the spectroModel has been set in the session.  If so, perform a config/detect/get calibration.
			if($("#startNewJob_siteHasSpectro").val()=="true" || "${sessionScope[thisGuid].spectroModel}"!=null) {		
				ws_spectro = new WSWrapper("coloreye");
				console.log("ready to read local host spectro config");
				readLocalhostSpectroConfig();
			} else {
				console.log("#startNewJob_siteHasSpectro is false");
			}
			//Add code to check days until password expiration.
 			var daysUntilPwdExpire = $('#startNewJob_daysUntilPwdExp').val();   
			if (daysUntilPwdExpire <= 7 && $("#startNewJob_newSession").val()=="true") {
				$('#passwordExpirationModal').modal('show');
			}
			
			if (localhostConfig != null && localhostConfig.model != null && localhostConfig.model.includes("SANTINT")){
				// remove Ecal Manager and Colorant Levels links from menu
				$("#tinterEcal").hide();
				$("#colorantLevels").hide();
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
		-->
		</script>
		
		<!-- Including footer -->
		<s:include value="Footer.jsp"></s:include>
		
	</body>
</html>	