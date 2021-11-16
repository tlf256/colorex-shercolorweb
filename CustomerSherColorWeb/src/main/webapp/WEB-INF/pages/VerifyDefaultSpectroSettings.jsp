<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!doctype html>


<%@ taglib prefix="s" uri="/struts-tags" %>

<html lang="en">
	<head>
		<meta name="viewport" content="width=device-width, initial-scale=1">
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
		
		<title><s:text name="global.verifyDefaultSpectroSettings"/></title>
			<!-- JQuery -->
		<link rel="stylesheet" href="css/bootstrap.min.css" type="text/css">
		<link rel="stylesheet" href="css/bootstrapxtra.css" type="text/css">
		<link rel="stylesheet" href="js/smoothness/jquery-ui.min.css" type="text/css">
		<link rel="stylesheet" href="css/CustomerSherColorWeb.css" type="text/css"> 
		<link rel="stylesheet" href="css/font-awesome.css" type="text/css">
		<script type="text/javascript" charset="utf-8" src="js/jquery-3.4.1.min.js"></script>
		<script type="text/javascript" charset="utf-8"	src="js/jquery-ui.min.js"></script>
		<script type="text/javascript" charset="utf-8"	src="js/bootstrap.min.js"></script>
		<script type="text/javascript" charset="utf-8" src="script/customershercolorweb-1.4.6.js"></script>
		<script type="text/javascript" charset="utf-8"	src="script/GetProductAutoComplete.js"></script>
		<script type="text/javascript" charset="utf-8"	src="script/WSWrapper.js"></script>
		<script type="text/javascript" charset="utf-8"	src="script/spectro.js"></script>
		<s:set var="thisGuid" value="reqGuid" />
		<style>
	        .sw-bg-main {
	            background-color: ${sessionScope[thisGuid].rgbHex};
	        }
	        .nbb-td {
	        	border-bottom: 0 !important
	        }
	        .green {
	        	color: #0D0;
	        }
	        .red {
	        	color: #D00;
	        }
	        .odd-row {
	        	background-color: #F5F5F5;
	        }
	    </style>
	    <script type="text/javascript">
	    
	    var ws_coloreye = new WSWrapper('coloreye');
	    var settingsTable;
	    var spectroSettings;
	    var clreyemodel;
	    var match = "fa fa-check fa-lg green";
	    var notMatch = "fa fa-times fa-lg red";
	    
	    
	    function RetrieveAllDeviceSettings() {
	    	clreyemodel = $('#spectroModel').val();
		 	var spectromessage = new SpectroMessage('GetVerifySettings',clreyemodel);
		    var json = JSON.stringify(spectromessage);
		    ws_coloreye.send(json);
	    }
	    
	    function SetAllDeviceSettingsToDefault() {
	    	clreyemodel = $('#spectroModel').val();
	    	var spectromessage = new SpectroMessage('SetVerifySettings',clreyemodel);
		    var json = JSON.stringify(spectromessage);
		    ws_coloreye.send(json);
	    }
	    
	    function SetSpectroSettings(spectroSettings) {
	    	settingsTable.find("#spectroTransformMode").text(spectroSettings[0]);
	    	settingsTable.find("#spectroCalTimeOut").text(spectroSettings[1]);
	    	settingsTable.find("#spectroMeasTrigMode").text(spectroSettings[2]);
	    	settingsTable.find("#spectroReflValueMode").text(spectroSettings[3]);
	    	settingsTable.find("#spectroBeeperVolume").text(spectroSettings[4]);
	    	settingsTable.find("#spectroGUIMode").text(spectroSettings[5]);
	    	settingsTable.find("#spectroUserInterface").text('<s:text name="verifyDefaultSpectroSettings.disable"/>');
	    	
	    	if (spectroSettings.length == 8) {
		    	settingsTable.find("#spectroSampleStorage").text(spectroSettings[7]);
		    	settingsTable.find("#sampleStorageRow").removeClass("d-none");
	    	}

	    }
	    
	    function SetAllToMatch() {
	    	var transformMode = settingsTable.find("#defaultTransformMode").text();
	    	var calTimeOut = settingsTable.find("#defaultCalTimeOut").text();
	    	var measTrigMode = settingsTable.find("#defaultMeasTrigMode").text();
	    	var reflValueMode = settingsTable.find("#defaultReflValueMode").text();
	    	var beeperVolume = settingsTable.find("#defaultBeeperVolume").text();
	    	var guiMode = settingsTable.find("#defaultGUIMode").text();
	    	var sampleStorage = settingsTable.find("#defaultSampleStorage").text();
	    	
	    	settingsTable.find("#matchTransformMode").removeClass(notMatch);
	    	settingsTable.find("#matchTransformMode").addClass(match);
	    	settingsTable.find("#spectroTransformMode").text(transformMode);
	    	
	    	settingsTable.find("#matchCalTimeOut").removeClass(notMatch);
			settingsTable.find("#matchCalTimeOut").addClass(match);
			settingsTable.find("#spectroCalTimeOut").text(calTimeOut);
			
			settingsTable.find("#matchMeasTrigMode").removeClass(notMatch);
			settingsTable.find("#matchMeasTrigMode").addClass(match);
			settingsTable.find("#spectroMeasTrigMode").text(measTrigMode);
			
			settingsTable.find("#matchReflValueMode").removeClass(notMatch);
			settingsTable.find("#matchReflValueMode").addClass(match);
			settingsTable.find("#spectroReflValueMode").text(reflValueMode);

			settingsTable.find("#matchBeeperVolume").removeClass(notMatch);
			settingsTable.find("#matchBeeperVolume").addClass(match);
			settingsTable.find("#spectroBeeperVolume").text(beeperVolume);
			
			settingsTable.find("#matchGUIMode").removeClass(notMatch);
			settingsTable.find("#matchGUIMode").addClass(match);
			settingsTable.find("#spectroGUIMode").text(guiMode);
			
			settingsTable.find("#matchSampleStorage").removeClass(notMatch);
			settingsTable.find("#matchSampleStorage").addClass(match);
			settingsTable.find("#spectroSampleStorage").text(sampleStorage);
			
	    }
	    
	    function CompareSpectroToDefault(spectroSettings) {
	    	var transformMode = settingsTable.find("#defaultTransformMode").text();
	    	var calTimeOut = settingsTable.find("#defaultCalTimeOut").text();
	    	var measTrigMode = settingsTable.find("#defaultMeasTrigMode").text();
	    	var reflValueMode = settingsTable.find("#defaultReflValueMode").text();
	    	var beeperVolume = settingsTable.find("#defaultBeeperVolume").text();
	    	var guiMode = settingsTable.find("#defaultGUIMode").text();
	    	var userInterface = settingsTable.find("#defaultUserInterface").text();
	    	var sampleStorage = settingsTable.find("#defaultSampleStorage").text();
	
	    	//console.log("Comparing ("+transformMode+") to ("+spectroSettings[0]+")");
	    	if(transformMode.localeCompare(spectroSettings[0]) == 0){
	    		settingsTable.find("#matchTransformMode").addClass(match);
	    	} else{ settingsTable.find("#matchTransformMode").addClass(notMatch); }
	    	
			if(calTimeOut.localeCompare(spectroSettings[1]) == 0){
				settingsTable.find("#matchCalTimeOut").addClass(match);
	    	} else{ settingsTable.find("#matchCalTimeOut").addClass(notMatch); }
			
			if(measTrigMode.localeCompare(spectroSettings[2]) == 0){
				settingsTable.find("#matchMeasTrigMode").addClass(match);
	    	} else{ settingsTable.find("#matchMeasTrigMode").addClass(notMatch); }
			
			if(reflValueMode.localeCompare(spectroSettings[3]) == 0){
				settingsTable.find("#matchReflValueMode").addClass(match);
	    	} else{ settingsTable.find("#matchReflValueMode").addClass(notMatch); }
			
			if(beeperVolume.localeCompare(spectroSettings[4]) == 0){
				settingsTable.find("#matchBeeperVolume").addClass(match);
	    	} else{ settingsTable.find("#matchBeeperVolume").addClass(notMatch); }
			
			if(guiMode.localeCompare(spectroSettings[5]) == 0){
				settingsTable.find("#matchGUIMode").addClass(match);
	    	} else{ settingsTable.find("#matchGUIMode").addClass(notMatch); }
			
			// No getter for User Interface, so this setting is always set to Disable
			// when getting these settings (which would cause it to always match)
			settingsTable.find("#matchUserInterface").addClass(match);
			
			
			if (spectroSettings.length == 8) {
				if(sampleStorage.localeCompare(spectroSettings[7]) == 0){
					settingsTable.find("#matchSampleStorage").addClass(match);
		    	} else{ settingsTable.find("#matchSampleStorage").addClass(notMatch); }
			}

	    }
	    
	    function RecdMessage() {
			console.log("Received Message");
		  	//parse the spectro
		  	//console.log("Message is " + ws_coloreye.wsmsg);
		  	//console.log("isReady is " + ws_coloreye.isReady + " BTW");
		  	var return_message=JSON.parse(ws_coloreye.wsmsg);
		  	switch (return_message.command) {
		  	case 'GetVerifySettings':
		  		if (return_message.errorMessage!="") {
		  		} else {
		  			console.log("Response: " + return_message.responseMessage);
		  			myGuid = "${reqGuid}";
		  			var settings = return_message.responseMessage;
		  			
		  			var currentSpectroHeading = settingsTable.find("#spectroName");
		  			currentSpectroHeading.text('<s:text name="verifyDefaultSpectroSettings.currentSpectro"><s:param>'+ return_message.model +'</s:param></s:text>');
		  			spectroSettings = settings.split(",");
		  			SetSpectroSettings(spectroSettings);
		  			CompareSpectroToDefault(spectroSettings);
		  			$("#setToDefaultButton").removeClass("d-none");
			    	$(".loadingText").remove();
			    	settingsTable.removeClass("d-none");
		  			}
		  			break;
		  	case 'SetVerifySettings':
				if (return_message.errorMessage!="") {
		  			console.log(return_message.errorMessage);
		  		} else {
		  			myGuid = "${reqGuid}";
		  			var settings = return_message.responseMessage;
		  			SetAllToMatch();
		  			}
		  			break;
		  		default:
		  		//Not an response we expected...
		  	}
		  	  	
		}
	    
	    $(document).ready(function() {
	    	
	    	settingsTable = $("#device_settings_table");
	    	RetrieveAllDeviceSettings();
	    	
	    	$(document).on("click", "#setToDefaultButton", function(event) {
	    		SetAllDeviceSettingsToDefault();
	    	});
	    });
	    </script>
	</head>
	
	<body>
		<!-- including Header -->
		<s:include value="Header.jsp"></s:include>
		
		<div class="container-fluid">
			
			<div class="row">
				<div class="col-sm-3">
				</div>
				<div class="col-sm-6">
				</div>
				<div class="col-sm-3">
					<s:set var="thisGuid" value="reqGuid" />
				</div>
			</div>
			<br>
			<div class="row">
			<div class="col-sm-1"></div>
			<div class="col-sm-10 mb-2	">
				<h1><s:text name="global.verifyDefaultSpectroSettings"/></h1>
			</div>
			<div class="col-sm-1"></div>
			</div>
			<div class="row">
				<div class="col-sm-2">
				</div>
			</div>
			
			<s:form action="verifyDeviceSettingsAction" validate="true" theme="bootstrap">
				<div class="row">
					<div class="col-sm-2">
						<s:hidden name="reqGuid" value="%{reqGuid}" />
					</div>
					<div class="col-sm-2">
					</div>
					<div class="col-sm-4">
					</div>
				</div>
				
				<div class="d-flex flex-row ">
					<div class="col-sm-1"></div>
					<div class="col-sm-10">
						<h2 class="text-success d-none"><s:text name="verifyDefaultSpectroSettings.deviceSettings"/></h2>
						<br>
						<table id="device_settings_table" class="table table-bordered d-none" style="width:100%">
							<thead>
								<tr>
									<th><s:text name="global.description"/></th>
									<th id="spectroName"></th>
									<th><s:text name="verifyDefaultSpectroSettings.sherwinDefault"/></th>
									<th><s:text name="verifyDefaultSpectroSettings.match"/></th>
								</tr>
								<tr class="odd-row">
									<td><s:text name="verifyDefaultSpectroSettings.transformMode"/></td>
									<td id="spectroTransformMode"></td>
									<td id="defaultTransformMode">i7PAINT41_v1</td>
									<td id="matchTransformMode" class="d-flex justify-content-center nbb-td"></td>
								</tr>
								<tr>
									<td><s:text name="verifyDefaultSpectroSettings.calTimeOut"/></td>
									<td id="spectroCalTimeOut"></td>
									<td id="defaultCalTimeOut">24</td>
									<td id="matchCalTimeOut" class="d-flex justify-content-center nbb-td"></td>
								</tr>
								<tr class="odd-row">
									<td><s:text name="verifyDefaultSpectroSettings.measTrigMode"/></td>
									<td id="spectroMeasTrigMode"></td>
									<td id="defaultMeasTrigMode">1</td>
									<td id="matchMeasTrigMode" class="d-flex justify-content-center nbb-td"></td>
								</tr>
								<tr>
									<td><s:text name="verifyDefaultSpectroSettings.reflValueMode"/></td>
									<td id="spectroReflValueMode"></td>
									<td id="defaultReflValueMode">0</td>
									<td id="matchReflValueMode" class="d-flex justify-content-center nbb-td"></td>
								</tr>
								<tr class="odd-row">
									<td><s:text name="verifyDefaultSpectroSettings.beeperVolume"/></td>
									<td id="spectroBeeperVolume"></td>
									<td id="defaultBeeperVolume">2</td>
									<td id="matchBeeperVolume" class="d-flex justify-content-center nbb-td"></td>
								</tr>
								<tr>
									<td><s:text name="verifyDefaultSpectroSettings.guiMode"/></td>
									<td id="spectroGUIMode"></td>
									<td id="defaultGUIMode"><s:text name="verifyDefaultSpectroSettings.measure"/></td>
									<td id="matchGUIMode" class="d-flex justify-content-center nbb-td"></td>
								</tr>
								<tr class="odd-row">
									<td><s:text name="verifyDefaultSpectroSettings.userInterface"/></td>
									<td id="spectroUserInterface"></td>
									<td id="defaultUserInterface"><s:text name="verifyDefaultSpectroSettings.disable"/></td>
									<td id="matchUserInterface" class="d-flex justify-content-center nbb-td"></td>
								</tr>
								<tr id="sampleStorageRow" class="d-none">
									<td><s:text name="verifyDefaultSpectroSettings.sampleStorage"/></td>
									<td id="spectroSampleStorage"></td>
									<td id="defaultSampleStorage">1</td>
									<td id="matchSampleStorage" class="d-flex justify-content-center nbb-td"></td>
								</tr>
							</thead>
						</table>
					</div>
					<div class="col-sm-1"></div>
				</div>
				<div class="row">
					<div class="col-sm-8"></div>
					<div class="col-sm-3">
						<button type="button" id="setToDefaultButton" class="btn btn-primary mb-5 mt-2 mx-1 d-none"><s:text name="verifyDefaultSpectroSettings.setSpectroToDefault"/></button>
						<s:submit cssClass="btn btn-secondary mb-5 mt-2 mx-1" value="%{getText('global.cancel')}" action="userCancelAction"/>
					</div>
					<div class="col-sm-1"></div>
				</div>
			</s:form>
		</div>
		<br>
		<br>
		<br>
		<!-- Including footer -->
		<s:include value="Footer.jsp"></s:include>
	</body>
</html>