<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<!doctype html>


<html lang="en">
<head>
<meta name="viewport" content="width=device-width, initial-scale=1">
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">

<title><s:text name="tinterConfig.tinterConfig"/></title>
<!-- JQuery -->
<link rel="stylesheet" href="css/bootstrap.min.css" type="text/css">
<link rel="stylesheet" href="css/bootstrapxtra.css" type="text/css">
<link rel="stylesheet" href="js/smoothness/jquery-ui.min.css" type="text/css">
<link rel="stylesheet" href="css/CustomerSherColorWeb.css" type="text/css">
<!-- Custom styles for this template -->

<link rel="stylesheet" href="font-awesome-4.7.0/css/font-awesome.min.css" type="text/css">

<script type="text/javascript" charset="utf-8" src="js/jquery-3.4.1.min.js"></script>
<script type="text/javascript" charset="utf-8" src="js/jquery-ui.min.js"></script>
<script type="text/javascript" charset="utf-8" src="js/jquery.dataTables.min-1.10.16.js"></script>
<script type="text/javascript" charset="utf-8" src="js/popper.min.js"></script>
<script type="text/javascript" charset="utf-8" src="js/bootstrap.min.js"></script>

<script type="text/javascript" charset="utf-8" src="script/customershercolorweb-1.5.2.js"></script>
<script type="text/javascript" charset="utf-8" src="script/WSWrapper.js"></script>
<script type="text/javascript" charset="utf-8" src="script/tinter-1.4.8.js"></script>
<script type="text/javascript" charset="utf-8" src="js/jquery-ui.min.js"></script>





<script type="text/javascript">

	// now build the dispense formula object
	var ws_tinter = new WSWrapper("tinter");
	var myModelList = null;
	var reqGuid = "${reqGuid}";
	var initStarted = 0;
	var detectAttempt = 0;
	
	const tinter = {
			clrntSysId:null,
			model:null,
			serialNbr:null,
			tinterIp:null
	};
	
	function buildEcal() {
		var dt_array1 = [];
		<s:iterator value="myEcalList">
		var ecal = new Ecal('${colorantid}', '${tintermodel}',
				'${tinterserial}', '${uploaddate}', '${uploadtime}',
				'${filename}');

		dt_array1.push(ecal);
		</s:iterator>
		return dt_array1;
	}

	function buildColorantOptions() {
		var colorants = getColorantIds();
		var $select = $('#colorantId');
		$select.find('option').remove();
		$('<option>').val("-1").text('<s:text name="tinterConfig.select"/>').appendTo($select);
		$.each(attributeActions, function(index, value) {
			$('<option>').val(value).text(value).appendTo($select);
		});
	}

	function setTinterConfig() {
		<s:iterator value="newtinter.canisterList" status="clrnt">
		var canister<s:property value="#clrnt.index"/> = new Canister(
				<s:property value="position"/>,
				"<s:property value="clrntCode"/>");
		</s:iterator>
		var canister_layout = [ canister0, canister1, canister2, canister3,
				canister4, canister5, canister6, canister7, canister8,
				canister9, canister10, canister11 ];
		// TODO add tinter ip here??
		configuration = new Configuration(
				"<s:property value="tinter.clrntSysId" escapeHtml="true"/>",
				"<s:property value="tinter.model" escapeHtml="true"/>",
				"<s:property value="tinter.serialNbr" escapeHtml="true"/>",
				"<s:property value=\"tinter.tinterIp\" escapeHtml=\"true\"/>", canister_layout);

	}
	function config_tinter(mycolorantid, mymodel, myserial, mytinter_ip, mycanister_layout) {
		var canister_layout = [];
		for (var i = 0, len = mycanister_layout.length; i < len; i++) {
			var code = mycanister_layout[i].clrntCode;
			var pos = i + 1;
			var canister1 = new Canister(pos, code);
			canister_layout.push(canister1);
		}
		var command = "Config";
		// add tinter IP to configuration
		var configuration = new Configuration(mycolorantid, mymodel, myserial,
				mytinter_ip, canister_layout);

		var calibration = null;
		//alfa and santint do not have cal files that we manage
		// TODO add AS 9500SW to this conditional?
		if(mymodel != null && (!mymodel.includes("ALFA") && !mymodel.includes("SANTINT"))){  //these models do not have cal files
			calibration =  new Calibration(mycolorantid, mymodel, myserial);
			//console.log("calibration");
			//console.log(calibration);
			if(calibration.data != null){
				var shotList = null;
				var gdata = null;  //for corob only
				if(mymodel.indexOf("Corob") >= 0 || mymodel.indexOf("COROB") >= 0){
					var gdata = new GData(mycolorantid);
				}
				var configMessage = new TinterMessage(command, shotList, configuration,
						calibration, gdata);
				sendTinterConfig(configMessage);

			}
			else{
					$("#configError").text('<s:text name="tinterConfig.couldNotFindDefaultCalib"><s:param>'+ mycolorantid +'</s:param><s:param>'+ mymodel +'</s:param></s:text>');
					$("#configErrorModal").modal('show');	
			}
		}
		else{
			configMessage = new TinterMessage(command, shotList, configuration,
					null, null);
			sendTinterConfig(configMessage);
		}

	};
	function sendTinterConfig(configMessage){
		var json = JSON.stringify(configMessage);
		
		if (ws_tinter && ws_tinter.isReady == "false") {
			console.log("WSWrapper connection has been closed (timeout is defaulted to 5 minutes). Make a new WSWrapper.")
			ws_tinter = new WSWrapper("tinter");
		}
		ws_tinter.send(json);
	}
	function init() {
		detectAttempt = 0;
		if(initStarted == 0){
			initStarted = 1;
			var command = "Detect"; //TODO change back to detect
			initmessage = new TinterMessage(command, null, null, null, null);
			var json = JSON.stringify(initmessage);
	
			if (ws_tinter && ws_tinter.isReady == "false") {
				console.log("WSWrapper connection has been closed (timeout is defaulted to 5 minutes). Make a new WSWrapper.")
				ws_tinter = new WSWrapper("tinter");
			}
			ws_tinter.send(json);
		}
	}

	function config() {
		console.log("config tinter");
		console.log(tinter.clrntSysId);
		console.log(tinter.model);
		console.log(tinter.serialNbr);
		if (tinter.clrntSysId == -1 || tinter.model == -1 || tinter.serialNbr == null || tinter.serialNbr == ""){
			alert('<s:text name="tinterConfig.emptyConfigSetting"/>');
			window.location.href = "tinterConfigAction?reqGuid=${reqGuid}";
			return;
		}
		
		//disable configure button while configuration in progress
		$('#btn_tinterConfig').prop('disabled', true);
		
		$.ajax({
			url : "GetCanisterList",
			context : document.body,
			data : {
	
				clrntSysId : tinter.clrntSysId,
				tinterModel : tinter.model,
				tinterSerialNbr : tinter.serialNbr,
				tinterIp : tinter.tinterIp,
				reqGuid : "${reqGuid}" //without this guid you will get a login exception and you won't even get an error
			},
			async : false,
			type : "POST",
			dataType : "json",
			success : function(objs) {
				console.log("getCanisterList resp");
				console.log(objs);
				if(objs.sessionStatus === "expired"){
					window.location = "/CustomerSherColorWeb/invalidLoginAction.action";
				}
				else{
					if (objs != null && objs.newtinter != null && objs.newtinter.model != null && 
							objs.newtinter.serialNbr != null && objs.newtinter.canisterList) {
						config_tinter(objs.newtinter.clrntSysId,
								objs.newtinter.model,
								objs.newtinter.serialNbr,
								objs.newTinter.tinterIp,
								objs.newtinter.canisterList);
					} else {
						
						alert('<s:text name="tinterConfig.invalidResponseFromServer"/>');
						window.location.href = "tinterConfigAction?reqGuid=${reqGuid}";
	
					}
				}
			},
			error : function() {
				var clrntSysId = $("[name='newtinter.clrntSysId']").val();
				alert('<s:text name="tinterConfig.couldNotFindCanisterLayout"><s:param>' + clrntSysId + '</s:param></s:text>');
			}
		});
	}

	function ValidateSN(SN) {
		var rc = 0;

		if (SN != null && (SN.length < 5 || SN.length > 15)) {
			$('#SNValidationError').text(
					'<s:text name="tinterConfig.invalidSerialNbrLength"/>');
			rc = -1;
		} 
		/* else if (SN.substring(0, 2) == SN.substring(3, 5)) {
			$('#SNValidationError')
					.text(
							'<s:text name="tinterConfig.invalidRepeatOfSame"/>');
			rc = -1;
		} else if (SN.substring(0, 1) == SN.substring(2, 3)
				|| SN.substring(4, 5) == SN.substring(6, 7)) {
			$('#SNValidationError')
					.text(
							'<s:text name="tinterConfig.invalidRepeatOfSame"/>');
			rc = -1;
		} else if (SN.charAt(0) == SN.charAt(1) && SN.charAt(1) == SN.charAt(2)
				&& SN.charAt(2) == SN.charAt(3) && SN.charAt(3) == SN.charAt(4)) {
			$('#SNValidationError')
					.text(
							'<s:text name="tinterConfig.invalidRepeatOfSame"/>');
			rc = -1;
		} else if (((Number(SN.charAt(4)) + 0) == (Number(SN.charAt(3)) + 1))
				&& ((Number(SN.charAt(3)) + 0) == (Number(SN.charAt(2)) + 1))
				&& ((Number(SN.charAt(2)) + 0) == (Number(SN.charAt(1)) + 1))
				&& ((Number(SN.charAt(1)) + 0) == (Number(SN.charAt(0)) + 1))) {
			$('#SNValidationError')
					.text(
							'<s:text name="tinterConfig.invalidConsecutiveNbrs"/>');
			rc = -1;
		}
		*/
		 else {
			for (var i = 0; i < SN.length; i++) {
				var code = SN.charCodeAt(i);
				if (code == 45 || // '-' is the only special char allowed
				(code >= 48 && code <= 57) || //0 to 9 valid
				(code >= 65 && code <= 90) || //A to Z valid
				(code >= 97 && code <= 122) //a to z valid
				) {
				}//valid
				else {
					$('#SNValidationError')
							.text(
									'<s:text name="tinterConfig.invalidNoSc"/>');
					rc = -1;
				}
			}
		}
		return rc;
	}

	function changeModel(model) {
		if(model.startsWith('AS')){
			// check for tinter-ip file 
			// send command that retrieves the hostname
			// if the file exists
			$('#tinterSerial').addClass('d-none');
			$('#tinterIp').removeClass('d-none');
			tinter.clrntSysId = $('#selectClrntSysId').val();
			tinter.model = $('#modelSelect').val();
			getHostName();
		} else {
			$('#tSerialNbr').focus();
		}
	}

	function getHostName(){
		var command = "GetHostName";
		var tinterModel = tinter.model;
		var clrntSys = tinter.clrntSysId;
		var configuration = new Configuration(clrntSys, tinterModel, null, null, null);
		var tinterMsg = new TinterMessage(command, null, configuration, null, null);
		var json = JSON.stringify(tinterMsg);

		if (ws_tinter && ws_tinter.isReady == "false") {
			console.log("WSWrapper connection has been closed (timeout is defaulted to 5 minutes). Make a new WSWrapper.")
			ws_tinter = new WSWrapper("tinter");
		}
		ws_tinter.send(json);
	}

	function getSerial(){
		var command = "GetSerialCode";
		var tinterMsg = new TinterMessage(command, null, null, null, null);
		var json = JSON.stringify(initmessage);

		if (ws_tinter && ws_tinter.isReady == "false") {
			console.log("WSWrapper connection has been closed (timeout is defaulted to 5 minutes). Make a new WSWrapper.")
			ws_tinter = new WSWrapper("tinter");
		}
		ws_tinter.send(json);
	}

	function checkCredentials(){
		var command = "CheckCredentials";
		var tinterMsg = new TinterMessage(command, null, null, null, null);
		var json = JSON.stringify(initmessage);

		if (ws_tinter && ws_tinter.isReady == "false") {
			console.log("WSWrapper connection has been closed (timeout is defaulted to 5 minutes). Make a new WSWrapper.")
			ws_tinter = new WSWrapper("tinter");
		}
		ws_tinter.send(json);
	}

	function updateCredentials(){
		var command = "UpdateCredentials";
		var tinterMsg = new TinterMessage(command, null, null, null, null);
		var json = JSON.stringify(initmessage);

		if (ws_tinter && ws_tinter.isReady == "false") {
			console.log("WSWrapper connection has been closed (timeout is defaulted to 5 minutes). Make a new WSWrapper.")
			ws_tinter = new WSWrapper("tinter");
		}
		ws_tinter.send(json);
	}
	
	function onSubmit(event){
		event.preventDefault();
		if($('#modelSelect').val().indexOf('AS') >= 0){
			// on submit of configure
			// populate tinterIp field with value read from file
			// show modal, retrieve serial number
			// then continue with credential process
			var ip = $('#tIpAddr').val().trim();
			tinter.tinterIp = ip;
			//$('#ipAddr').val(ip);
			// TODO validate IP address here

			// retrieve serial number and write hostname to file
			// first get credentials
			checkCredentials();

			
		} else {
			$('#tSerialNbr').val($('#tSerialNbr').val().toUpperCase());
			var serial = $('#tSerialNbr').val();
			if (ValidateSN(serial) == 0) {
				tinter.clrntSysId = $('#selectClrntSysId').val();
				tinter.model = $('#modelSelect').val();
				tinter.serialNbr = serial;
				config();
			}
		}
	}

	function togglePassword(element, showPassword){
		//toggle showPassword attribute for password input
		if(showPassword){
			$('#reveal_btn').html('<i class="text-muted fa fa-eye-slash"></i>');
			$(element).prop('showPassword', false);
		}
		else{
			$('#reveal_btn').html('<i class="text-muted fa fa-eye"></i>');
			$(element).prop('showPassword', true);
		}
	}

	/*prevent submission when the enter key is pressed, rather run config() which will perform the submit */
	$(document).on('keyup keypress', '#tSerialNbr', function(e) {
		  if(e.which == 13) {
			  onSubmit(e);
		    return false;
		  }
		});
	$(document).on("click", "#btn_tinterConfig", function(event) {
		onSubmit(event);
	});
	$(document).on("click", "#add_new_tinter", function(event) {
		event.preventDefault();
		$('#default_config').show();
		$('#ecals').hide();

	});
	$(document).on("click", "#detectErrorsClose", function(event) {
		event.preventDefault();
		// add action to return to main page or back to this page?		
		window.location.href = "tinterConfigAction?reqGuid=${reqGuid}";

	});
	$(document).on("click", "#detectErrorClose", function(event) {
		event.preventDefault();

		$("#frmSubmit").submit(); // action to save colorants txt and move to welcome page. // TODO.  This works for simulator.  Do we want to keep this?

	});

	//good detect- save
	$(document).on("click", "#detectStatusClose", function(event) {
		event.preventDefault();
		$("#frmSubmit").submit(); // action to save colorants txt and move to welcome page. // TODO.  This works for simulator.  Do we want to keep this?

	});

	/*$('#passwordModal').on('hide-bs-modal', function(e){
		var closeBtn = $('#pModalClose_btn');
		if(e.currentTarget != closeBtn){
			// get serial
		}
	});*/

	$(document).on('click', '#updateCreds_btn', function(){
		updateCredentials();
	});

	/***
	* @param myGuid - Session Guid from page (i.e. reqGuid) 
	* @param myMessage - TinterMessage object
	* @param teDetail - array of TintEventDetail
	* @param myConfig - Configuration object (canister_layout not required) 
	* @returns
	*/
	function sendTinterEventConfig(myGuid, myDate, myMessage, teDetail){
	/* use global variable if null sent for myGuid*/
		if(myGuid == null){
			if(reqGuid != null){
				myGuid=reqGuid;
			}
		}
	
		//var mydata = {reqGuid:myGuid, tinterMessage:myMessage, tintEventDetail:teDetail};
		var mydata = {reqGuid:myGuid, eventDate:myDate.toString(), newTinter:tinter,tintEventDetail:teDetail, tinterMessage:myMessage};
		var jsonIn = JSON.stringify(mydata);
		console.log("Logging Tinter Event " + jsonIn);
		$.ajax({
			url: "logTinterEventConfigAction.action",
			contentType : "application/json; charset=utf-8",
			type: "POST",
			data: jsonIn,
			datatype : "json",
			success: function (data) {
				if(data.sessionStatus === "expired"){
	        		window.location = "/CustomerSherColorWeb/invalidLoginAction.action";
	        	}
	        	else{
	        		//Handle success after checking expiration
	        	}
			},
			error: function () {
				alert('<s:text name="tinterConfig.invalidResponseFromServer"/>');
				window.location.href = "tinterConfigAction?reqGuid=${reqGuid}";
			}
		});
	}
	function GetModelsForColorant(colorantId) {
		var modellist = null;
		
		$.ajax({
				url : "GetTinterModelsAction",
				context : document.body,
				data : {

					customerId : "DEFAULT",
					clrntSysId : colorantId,
					reqGuid : "${reqGuid}" //without this guid you will get a login exception and you won't even get an error
				},
				async : false,
				type : "POST",
				dataType : "json",
				success : function(objs) {
					if(objs.sessionStatus === "expired"){
                   		window.location = "/CustomerSherColorWeb/invalidLoginAction.action";
                   	}
                   	else{
                   		modellist = objs.defaultModelList;
                   		$.each(modellist, function(index, value) {
                   			console.log("Ajax success: TINTER MODEL: " + value);
                   		});
                   	}
				},
				error : function() {
					modellist = [ '<s:text name="tinterConfig.couldNotFindTinterModels"/>'
							+ " " + colorantId ];
					alert('<s:text name="tinterConfig.couldNotFindCanisterLayout"><s:param>' + colorantId + '</s:param></s:text>');
				}
			});

		var box = $('#modelSelect');
		box.empty();
		setTimeout(function() {
			//wait 300ms.  Then!  draw table
			box.append($("<option></option>").attr("value", -1).text(
					'<s:text name="tinterConfig.selectModel"/>'));
			if (modellist != null) {
				$('#hidden_modellist').html("");
				$.each(modellist, function(index, value) {
					console.log("TINTER MODEL: " + value);
					box.append($("<option></option>").attr("value", value)
							.text(value));
					var name = "defaultModelList[" + index + "]";
					var $hiddenInput = $('<input/>', {
						type : 'hidden',
						name : name,
						value : value
					});

					$hiddenInput.appendTo('#hidden_modellist');

				});
			}
		}, 300);

		return modellist;
	}

	// 	
	$(document).ready(function() {
		$('#add_new_tinter').show(); // add new tinter
		$('#ecals').hide();
		$('#selectClrntSysId').val("CCE");
		GetModelsForColorant($('#selectClrntSysId').val());
		
		
		/*  var dt_arr = buildEcal();

		 var ecal_table = $('#ecals_table').DataTable({
		
		 "scrollY": "200px",
		 "scrollX": "760px",
		 scrollCollapse: true,
		 data:dt_arr,
		 dom: 'rt',
		 columns : [ {

			 data : 'colorantId'
		 }, {
			 data : 'tinterModel'
		 }, {
			 data : 'tinterSerial'
		 }, {
			 data : 'uploaddate'
		 }, {
			 data : 'uploadtime'
		 }, 
		 {
		     "render": function ( data,type,row) {
		    	 return '<input type="radio" name="filename" value=row.filename>';
		 	}
		}  // here's a radio button, modify to use data to populate it
		 ]
		});
		 */

	});
	function AfterDetectTinterGetStatus(){
		;
		var cmd = "InitStatus";
		rotateIcon();
		var shotList = null;
		var configuration = null;
		var tintermessage = new TinterMessage(cmd,null,null,null,null);  
    	var json = JSON.stringify(tintermessage);
		sendingTinterCommand = "true";
    	ws_tinter.send(json);
	}		
	function RecdMessage() {
		//verify that the tinter fields have value
		if(tinter.clrntSysId == null) {
			console.log("clrntSysId not set before receiving this message, getting current value selected");
			tinter.clrntSysId = $('#selectClrntSysId').val();
			console.log(tinter.clrntSysId);
		}
		if(tinter.model == null) {
			console.log("model not set before receiving this message, getting current value selected");
			tinter.model = $('#modelSelect').val();
			console.log(tinter.model);
		}
		if(tinter.serialNbr == null) {
			console.log("serialNbr not set before receiving this message, getting current value");
			tinter.serialNbr = $('#tSerialNbr').val();
			console.log(tinter.serialNbr);
		}
		
		if(tinter.model.indexOf("COROB") >= 0 || tinter.model.indexOf("Corob") >= 0|| tinter.model.indexOf("SIMULATOR") >= 0){
			RecdMessageCorob();
		}
		else if(tinter.model.indexOf("FM X") >= 0){
			RecdMessageFMX();
		}
		else if(tinter.model.indexOf("SANTINT") >= 0){
			RecdMessageSantint();
		}
		else if(tinter.model.indexOf("AS") >= 0){
			RecdMessageAS();
		}
		else{
			RecdMessageFMAlfa();
		}
	}
	
	function RecdMessageSantint() {
		var curDate = new Date();
		console.log("Received Santint Message");
		// parse the spectro
		if (ws_tinter && ws_tinter.wserrormsg != null && ws_tinter.wserrormsg != "") {
			console.log(ws_tinter.wsmsg);
			console.log("isReady is " + ws_tinter.isReady + "BTW");
			// Show a modal with error message to make sure the user is forced to read it.
			$("#configError").text(ws_tinter.wserrormsg);
			$("#configErrorModal").modal('show');	
		
		} else {
			var return_message = JSON.parse(ws_tinter.wsmsg);
			var errorKey = return_message.errorMessage;
			// update error with internationalized message
			return_message.errorMessage = i18n[errorKey];
			
			switch (return_message.command) {
			case 'Config':
				if (return_message.errorNumber == 0 && return_message.commandRC == 0) {
					init();
					$("#detectInProgressModal").modal('show');
					rotateIcon();
				} else {
					$("#configError").text(return_message.errorMessage);
					$("#configErrorModal").modal('show');
				}
				// update error message to english and log
				return_message.errorMessage = log_english[errorKey];
				sendTinterEventConfig(reqGuid, curDate, return_message, null);	
				break;
			case 'Detect':
				waitForShowAndHide("#detectInProgressModal");

				// got back success 
				if (return_message.errorNumber == 0 && return_message.commandRC == 0) {
					$("#detectStatusModal").modal('show');
					$("#detectStatus").text('<s:text name="global.tinterDetectConfigComplete"/>');
					console.log(return_message);

				} else {
					$("#detectErrorModal").modal('show');
					switch (return_message.errorNumber) {
					case -1:
					default:
						$("#errorModalTitle").text('<s:text name="tinterConfig.tinterInitErrors"/>');
						$("#detectErrorMessage").text(return_message.errorMessage);
						break;
					}
					
					if (return_message.errorList != undefined) {
						for (var i = 0, len = return_message.errorList.length; i < len; i++) {
							var error = return_message.errorList[i];
							var errorText = "<li>" + error.num + "\t" + error.message + "</li>";
							$("#detectErrorList").append(errorText);
						}
					}
				}
				// update error message to english and log
				return_message.errorMessage = log_english[errorKey];
				sendTinterEventConfig(reqGuid, curDate, return_message, null);
				break;
			default:
				// Not an response we expected
				console.log("Message from different command is junk, throw it out");
			}
		}
	}
	
	function RecdMessageCorob() {
		var curDate = new Date();
		console.log("Received Message");
		//parse the spectro
		if (ws_tinter && ws_tinter.wserrormsg != null && ws_tinter.wserrormsg != "") {
				console.log(ws_tinter.wsmsg);
				console.log("isReady is " + ws_tinter.isReady + "BTW");
				//Show a modal with error message to make sure the user is forced to read it.
				$("#configError").text(ws_tinter.wserrormsg);
				$("#configErrorModal").modal('show');			
		} else {

		
			var return_message = JSON.parse(ws_tinter.wsmsg);
			switch (return_message.command) {

			case 'Config':
				if (return_message.errorNumber == 0
						&& return_message.commandRC == 0) {
					init();
					$("#detectInProgressModal").modal('show');
					rotateIcon();
				} else {
					$("#configError").text(return_message.errorMessage);
					$("#configErrorModal").modal('show');
				}
				sendTinterEventConfig(reqGuid, curDate, return_message,null);
				break;
			case 'Detect':
			case 'Init':
			case 'InitStatus':
				waitForShowAndHide("#detectInProgressModal");
	           

				if (return_message.errorNumber == 0
						&& return_message.commandRC == 0) {
					//save
					$("#detectStatusModal").modal('show');
					$("#detectStatus")
							.text('<s:text name="global.tinterDetectConfigComplete"/>');
					console.log(return_message);

						//$("#frmSubmit").submit();  // action to save colorants txt and move to welcome page.
				}

				else {
					$("#detectErrorModal").modal('show');
					switch (return_message.errorNumber) {
					case -10500:
					case -3084:
						$("#errorModalTitle")
								.text(
										'<s:text name="global.tinterDetectConfigComplete"/>');
						$("#detectErrorMessage").text(
								return_message.errorMessage);
						break;
					default:
						$("#errorModalTitle").text(
								'<s:text name="global.tinterInitErrorsClickContinue"/>');
						$("#detectErrorMessage").text(
								return_message.errorMessage);
						break;
					}
					$("#detectErrorMessage").css("white-space", "pre");
					$("#detectErrorMessage").text(return_message.errorMessage);
					if (return_message.errorList != undefined) {

						for (var i = 0, len = return_message.errorList.length; i < len; i++) {
							var error = return_message.errorList[i];
							var errorText = "<li>" + error.num + "\t"
									+ error.message + "</li>";
							$("#detectErrorList").append(errorText);
						}
					}
				}
				sendTinterEventConfig(reqGuid, curDate, return_message,null);
				if(return_message.configuration != null && return_message.configuration.model != null 
						&& return_message.configuration.model.includes("COROB CUSTOM")){
					updateColorantsTxt(reqGuid, return_message, false, null);
				}
				break;
			default:
				//Not an response we expected...
				console
						.log("Message from different command is junk, throw it out");
			}
			
		}
	}
	function RecdMessageFMX() {
		var curDate = new Date();
		console.log("Received FMX Message");
		//parse the spectro
		if (ws_tinter && ws_tinter.wserrormsg != null && ws_tinter.wserrormsg != "") {
				console.log(ws_tinter.wsmsg);
				console.log("isReady is " + ws_tinter.isReady + "BTW");
				//Show a modal with error message to make sure the user is forced to read it.
				$("#configError").text(ws_tinter.wserrormsg);
				$("#configErrorModal").modal('show');			
		} else {

		
			var return_message = JSON.parse(ws_tinter.wsmsg);
			switch (return_message.command) {

			case 'Config':
				if (return_message.errorNumber == 0) {
					init();
					$("#detectInProgressModal").modal('show');
					rotateIcon();
				} else {
					$("#configError").text(return_message.errorMessage);
					$("#configErrorModal").modal('show');
				}
				sendTinterEventConfig(reqGuid, curDate, return_message,null);
				break;
			case 'Detect':
			case 'Init':
			case 'InitStatus':
				let ucErrorMessage = return_message.errorMessage.toUpperCase().trim()
				//status = 1, means, still trying serial ports so still in progress.
				if (( ucErrorMessage.trim() != initializationDone) &&
						 (return_message.errorNumber >= 0 ||
						 return_message.status == 1)) {
					 	//save		
					$("#progress-message").text(return_message.errorMessage);
					if(detectAttempt < 85){
						setTimeout(
							  function() 
							  { //make sure we are not slamming everything
									AfterDetectTinterGetStatus(); // keep sending init status until you get something.
							  }, 5000);

						detectAttempt++;
					}
					else{ // close up shop with this error.
						//sendTinterEvent(myGuid, curDate, return_message, null); 
						waitForShowAndHide('#detectInProgressModal');
						return_message.errorMessage = "Timeout Waiting for X-Tinter Detect";
						return_message.errorNumber = -1;
					
						
						$("#detectErrorList").append("<li>" + return_message.errorMessage + "</li>");
					
						$("#errorModalTitle").text('<s:text name="global.timeoutWaitingForXTinterDetect"/>');
						$("#detectErrorMessage").text('<s:text name="global.resolveIssuesBeforeDispense"/>');
						$("#detectErrorModal").modal('show');
					
				
						}
					//console.log(return_message.errorMessage);
				}
				else if(return_message.errorMessage.toUpperCase().trim() == initializationDone){
					console.log("init done: " + (return_message.errorMessage.toUpperCase().trim() == initializationDone));
					
					
					waitForShowAndHide("#detectInProgressModal");
		           
					$("#detectStatusModal").modal('show');
					
					$("#detectStatus") 
							.text(
									'<s:text name="global.tinterDetectConfigComplete"/>');
				}
				else {
					
					waitForShowAndHide("#detectInProgressModal");
		           
					$("#detectErrorModal").modal('show');
					
					switch (return_message.errorNumber) {
					case -10500:
					case -3084:
						$("#errorModalTitle")
								.text(
										'<s:text name="tinterConfig.tinterDetectConfigCompleteWithErrors"/>');
						$("#detectErrorMessage").text(
								return_message.errorMessage);
						break;
					default:
						$("#errorModalTitle").text(
								'<s:text name="tinterConfig.tinterInitErrors"/>');
						$("#detectErrorMessage").text(
								return_message.errorMessage);
						break;
					}
					$("#detectErrorMessage").text(return_message.errorMessage);
					if (return_message.errorList != undefined) {

						for (var i = 0, len = return_message.errorList.length; i < len; i++) {
							var error = return_message.errorList[i];
							var errorText = "<li>" + error.num + "\t"
									+ error.message + "</li>";
							$("#detectErrorList").append(errorText);
						}
					}
				}
				if ((return_message.errorMessage.toUpperCase().trim() == initializationDone) || return_message.errorNumber < 0) {
					sendTinterEventConfig(reqGuid, curDate, return_message,null);
				}
				break;
			default:
				//Not an response we expected...
				console
						.log("Message from different command is junk, throw it out");
			}
			
		}
	}
	// modify the following to accomodate the AS 9500...
	function RecdMessageAS() {
		var curDate = new Date();
		console.log("Received FM Message");
		if (ws_tinter && ws_tinter.wserrormsg != null && ws_tinter.wserrormsg != "") {
				console.log(ws_tinter.wsmsg);
				console.log("isReady is " + ws_tinter.isReady + " BTW");
				//Show a modal with error message to make sure the user is forced to read it.
				$("#configError").text(ws_tinter.wserrormsg);
				$("#configErrorModal").modal('show');			
		} else {
			var return_message = JSON.parse(ws_tinter.wsmsg);
			switch (return_message.command) {
			case 'Config':
				if (return_message.errorNumber == 0) {
					init();
					$("#detectInProgressModal").modal('show');
					rotateIcon();
				} else {
					$("#configError").text(return_message.errorMessage);
					$("#configErrorModal").modal('show');
				}
				sendTinterEventConfig(reqGuid, curDate, return_message,null);
				break;
			case 'Detect':
			case 'Init':
			case 'InitStatus':
				let ucErrorMessage = return_message.errorMessage.toUpperCase().trim()
				//status = 1, means, still trying serial ports so still in progress.
				if (( ucErrorMessage.trim() != initializationDone) &&
						 (return_message.errorNumber >= 0 ||
						 return_message.status == 1)) {
					 	//save		
					$("#progress-message").text(return_message.errorMessage);
					if(detectAttempt < 85){
						setTimeout(
							  function() 
							  { //make sure we are not slamming everything
									AfterDetectTinterGetStatus(); // keep sending init status until you get something.
							  }, 5000);

						detectAttempt++;
					}
					else{ // close up shop with this error.
						//sendTinterEvent(myGuid, curDate, return_message, null); 
						waitForShowAndHide('#detectInProgressModal');
						return_message.errorMessage = "Timeout Waiting for X-Tinter Detect";
						return_message.errorNumber = -1;
					
						
						$("#detectErrorList").append("<li>" + return_message.errorMessage + "</li>");
					
						$("#errorModalTitle").text('<s:text name="global.timeoutWaitingForXTinterDetect"/>');
						$("#detectErrorMessage").text('<s:text name="global.resolveIssuesBeforeDispense"/>');
						$("#detectErrorModal").modal('show');
					
				
						}
					//console.log(return_message.errorMessage);
				}
				else if(return_message.errorMessage.toUpperCase().trim() == initializationDone){
					console.log("init done: " + (return_message.errorMessage.toUpperCase().trim() == initializationDone));
					
					
					waitForShowAndHide("#detectInProgressModal");
		           
					$("#detectStatusModal").modal('show');
					
					$("#detectStatus") 
							.text(
									'<s:text name="global.tinterDetectConfigComplete"/>');
				}
				else {
					
					waitForShowAndHide("#detectInProgressModal");
		           
					$("#detectErrorModal").modal('show');
					
					switch (return_message.errorNumber) {
					case -10500:
					case -3084:
						$("#errorModalTitle")
								.text(
										'<s:text name="tinterConfig.tinterDetectConfigCompleteWithErrors"/>');
						$("#detectErrorMessage").text(
								return_message.errorMessage);
						break;
					default:
						$("#errorModalTitle").text(
								'<s:text name="tinterConfig.tinterInitErrors"/>');
						$("#detectErrorMessage").text(
								return_message.errorMessage);
						break;
					}
					$("#detectErrorMessage").text(return_message.errorMessage);
					if (return_message.errorList != undefined) {

						for (var i = 0, len = return_message.errorList.length; i < len; i++) {
							var error = return_message.errorList[i];
							var errorText = "<li>" + error.num + "\t"
									+ error.message + "</li>";
							$("#detectErrorList").append(errorText);
						}
					}
				}
				if ((return_message.errorMessage.toUpperCase().trim() == initializationDone) || return_message.errorNumber < 0) {
					sendTinterEventConfig(reqGuid, curDate, return_message,null);
				}
				break;
			case 'GetHostName':
				// check if the hostname was returned
				// if so, set the value of the textfield
				if(!return_message.errorMessage.isEmpty() && return_message.commandRC == 2){
					var ip = return_message.errorMessage.trim();
				}
				else if(!return_message.errorMessage.isEmpty() && return_message.commandRC == 0) {
					// error reading/writing hostname file
					var errorText = "<br><h5>" + return_message.errorMessage + "</h5>"
					$('#error').html(errorText);
				}
				else {
					// other unknown error
					var errorText = "<br><h5>Error retrieving hostname</h5>"
					$('#error').html(errorText);
				}
				break;
			case 'CheckCredentials':
				// check if credentials need updating
				if(return_message.commandRC == 2){
					// credentials do not need updating
					// continue with configuration
					getSerial();
				}
				else {
					// credentials need updated
					// prompt for password
					// maybe this is where the password modal should be shown?
					$('#ipAddr').val(tinter.tinterIp);
					$('#passwordModal').modal('show');
				}
				break;
			case 'UpdateCredentials':
				// if successful, get serial
				break;
			default:
				//Not an response we expected...
				console
						.log("Message from different command is junk, throw it out");
			}
			
		}
	}
	function RecdMessageFMAlfa() {
		var curDate = new Date();
		console.log("Received FM Message");
		//parse the spectro
		if (ws_tinter && ws_tinter.wserrormsg != null && ws_tinter.wserrormsg != "") {
				console.log(ws_tinter.wsmsg);
				console.log("isReady is " + ws_tinter.isReady + "BTW");
				//Show a modal with error message to make sure the user is forced to read it.
				$("#configError").text(ws_tinter.wserrormsg);
				$("#configErrorModal").modal('show');			
		} else {

		
			var return_message = JSON.parse(ws_tinter.wsmsg);
			switch (return_message.command) {

			case 'Config':
				if (return_message.errorNumber == 0) {
					init();
					$("#detectInProgressModal").modal('show');
					rotateIcon();
				} else {
					$("#configError").text(return_message.errorMessage);
					$("#configErrorModal").modal('show');
				}
				sendTinterEventConfig(reqGuid, curDate, return_message,null);
				break;
			case 'Detect':
			case 'Init':
				//status = 1, means, still trying serial ports so still in progress.
				if ((return_message.errorMessage.toUpperCase().trim() != initializationDone) && (return_message.errorNumber >= 0 ||
						 return_message.status == 1)) {
					 	//save		
					$("#progress-message").text(return_message.errorMessage);
					//console.log(return_message.errorMessage);
				}
				else if((return_message.errorMessage.toUpperCase().trim() == initializationDone)){
					console.log("init done: " + (return_message.errorMessage.toUpperCase().trim() == initializationDone));
					
					
					waitForShowAndHide("#detectInProgressModal");
		           
					$("#detectStatusModal").modal('show');
					
					$("#detectStatus") 
							.text(
									'<s:text name="global.tinterDetectConfigComplete"/>');
				}
				else {
					
					waitForShowAndHide("#detectInProgressModal");
		           
					$("#detectErrorModal").modal('show');
					
					switch (return_message.errorNumber) {
					case -10500:
					case -3084:
						$("#errorModalTitle")
								.text(
										'<s:text name="tinterConfig.tinterDetectConfigCompleteWithErrors"/>');
						$("#detectErrorMessage").text(
								return_message.errorMessage);
						break;
					default:
						$("#errorModalTitle").text(
								'<s:text name="tinterConfig.tinterInitErrors"/>');
						$("#detectErrorMessage").text(
								return_message.errorMessage);
						break;
					}
					$("#detectErrorMessage").text(return_message.errorMessage);
					if (return_message.errorList != undefined) {
						for (var i = 0, len = return_message.errorList.length; i < len; i++) {
							var error = return_message.errorList[i];
							var errorText = "<li>" + error.num + "\t"
									+ error.message + "</li>";
							$("#detectErrorList").append(errorText);
						}
					}
				}
				if ((return_message.errorMessage.toUpperCase().trim() == initializationDone) || return_message.errorNumber < 0) {
					sendTinterEventConfig(reqGuid, curDate, return_message,null);
				}
				break;
			default:
				//Not an response we expected...
				console
						.log("Message from different command is junk, throw it out");
			}
			
		}
	}
	
	/*$(document).on('keyup', '#tIpAddr', function(e){
		var val = $(this).val();
		var count = val.length;
		// can't use this - there can be less than 3 digits in the octets
		if(count == 3 || count == 7 || count == 11){
			val += ".";
		}
	});*/
	
	//$(document).on('click', '#btn_tinterIpNxt', function(){
		// get tinter serial by calling detect...
		//var displayMessage = 'Retrieving tinter serial...';
		//pleaseWaitModal_show(displayMessage, null);
		// check credentials
		// ajax call to action to verify the password is set and not expired
		// if it's expired, get new password. admin password will be expired.
		
	//});
	
	//$('#pleaseWaitModal').on('shown.bs.modal', function(){
		// detect tinter and get serial number
		//init();
	//});
	
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
		
		$('#detectInProgressModal').one('hide.bs.modal',function(){
			$('#spinner').addClass('d-none');
        	if(interval){clearInterval(interval);}
		});
	}
	
</script>
</head>

<body>
	<!-- including Header -->
	<s:include value="Header.jsp"></s:include>
	<!-- uncomment if we want to make a table of ecals that user can config from 		
    		<div id="ecals" class="col-xs-12 tleft ecals">
    		<h3>
    		<a href="#" data-toggle="tooltip" title="Here are the configurations you have used in the past.  On the upload date and time specified, we stored the config and calibration at SW.  Choose one of these to configure your computer with the exact same calibration, colorantid, model and serial number.  You do not need to calibrate your tinter."> Choose a configuration</a>
    		  	</h3>	
			<table id="ecals_table" class="table  table-striped table-bordered"  >
				<thead>
					<tr>
					    <th>Clrnt</th>
						<th>Tinter Model</th>
						<th>Tinter Serial</th>
						<th>Upload Date</th>
						<th>Upload Time</th>
						<th>Choose</th>	
					</tr>				
				</thead>
				<tbody></tbody>
				<tfoot></tfoot>
			</table>		
			<button id="add_new_tinter">Choose tinter</button>
		</div>
	-->
	<div class="container center-form-parent">



		<s:set var="thisGuid" value="reqGuid" />

		<s:form class="form-sw-centered" id="frmSubmit" action="SaveNewTinter"
			validate="true" theme="bootstrap">
			<div class="text-center mb-4">

				<h1 class="h3 mb-3 font-weight-normal"><s:text name="tinterConfig.configTinter"/></h1>
				<p><s:text name="tinterConfig.chooseClrntModelSerial"/></p>
				<div id="error" class="text-danger"></div>
			</div>

			<div class="form-label-group">

				<label class="sw-label" for="selectClrntSysId"><s:text name="tinterConfig.colorant"/></label>
				<s:select id="selectClrntSysId" name="newtinter.clrntSysId"
					headerKey="-1" headerValue="%{getText('global.selectColorant')}"
					list="defaultColorantList" onchange='GetModelsForColorant(this.value)' />


			</div>

			<div class="form-label-group">
				<label class="sw-label" for="modelSelect"><s:text name="tinterConfig.model"/></label>
				<s:select id="modelSelect" name="newtinter.Model"
					autofocus="autofocus" list="defaultModelList" headerKey="-1"
					headerValue="" onchange='changeModel(this.value)'>
				</s:select>

			</div>
			
			<div id="tinterIp" class="form-label-group d-none">
				<label class="sw-label" for="tIpAddr">Tinter IP Address</label>
				<s:textfield id="tIpAddr" class="form-control" name="newtinter.tinterIp"></s:textfield>

			</div>

			<div id="tinterSerial" class="form-label-group">
				<label class="sw-label" for="tSerialNbr"><s:text name="tinterConfig.serialNumber"/></label>
				<s:textfield id="tSerialNbr" name="newtinter.serialNbr"></s:textfield>

				<p style="color: red; font-weight: bold" id="SNValidationError"></p>
				
			</div>
			
			<br>

			<div class="form-row">
				<s:actionerror />
				
				<input type="button" class="btn btn-lg btn-primary btn-block"
					id="btn_tinterConfig" data-toggle="modal"
					data-target="#verifyModal" value='<s:text name="global.configure"/>' />
					

				<s:submit cssClass="btn btn-lg btn-secondary btn-block"
					value="%{getText('global.cancel')}" action="userCancelAction" />


			</div>
			<div class="form-row">
					<!-- save default colorantList for submission in case we need to come back here due to error -->

					<s:hidden name="reqGuid" />
					<s:iterator value="defaultColorantList" var="clrntSysId"
						status="colorant">

						<s:hidden name="defaultColorantList[%{#colorant.index}]"
							value="%{#clrntSysId}" />
					</s:iterator>
				</div>
			<div id="hidden_modellist" class="col-md-2"></div>
		</s:form>
	</div>

	<!-- Password Modal -->
	<div class="modal fade" aria-labelledby="" aria-hidden="true" id="passwordModal" role="dialog"
		data-backdrop="static" data-keyboard="false">
		<div class="modal-dialog" role="document">
			<div class="modal-content">
				<div class="modal-header">
					<h5 class="modal-title">Device Credentials</h5>
					<button type="button" id="pModalClose_btn" class="close" data-dismiss="modal" aria-label="%{getText('global.close')}">
						<span aria-hidden="true">&times;</span>
					</button>
				</div>
				<div class="modal-body">
					<div class="container">
						<s:form class="form-sw-centered" id="" action="" validate="true" theme="bootstrap">
						<div class="row">
						  <div class="col-sm-3"></div>
						  <div class="col-sm-6">
							<div id="ipAddress" class="form-label-group">
								<label class="sw-label" for="ipAddr">Tinter IP Address</label>
								<s:textfield id="ipAddr" class="form-control" name="newtinter.tinterIp" aria-disabled="true"></s:textfield>
							</div>
						  </div>
						  <div class="col-sm-3"></div>
						</div>
						<div class="row">
						  <div class="col-sm-3"></div>
						  <div class="col-sm-6">
							<div id="servicePwd" class="form-label-group">
								<label class="sw-label" for="">Service Password</label>
								<s:password id="srvPwd" class="form-control" name="">
									<button id="reveal_btn" onclick="togglePassword(this.parent.id, this.parent.showPassword)">
										<i class="text-muted fa fa-eye"></i>
									</button>
								</s:password>
							</div>
						  </div>
						  <div class="col-sm-3"></div>
						</div>
						</s:form>
					</div>
				</div>
				<div class="modal-footer">
					<button type="button" class="btn btn-primary" id="updateCreds_btn"
						aria-label="%{getText('global.continue')}"><s:text name="global.submit"/></button>
				</div>
			</div>
		</div>
	</div>

	<!-- Detect in Progress Modal Window -->
	<div class="modal fade" aria-labelledby="detectInProgressModal"
		aria-hidden="true" id="detectInProgressModal" role="dialog"
		data-backdrop="static" data-keyboard="false">
		<div class="modal-dialog" role="document">
			<div class="modal-content">
				<div class="modal-header">
					<i id="spinner" class="fa fa-refresh mr-3 mt-1 text-muted" style="font-size: 1.5rem;"></i>
					<h5 class="modal-title"><s:text name="global.tinterDetectionAndInitialization"/></h5>
					<button type="button" class="close" data-dismiss="modal"
						aria-label="%{getText('global.close')}">
						<span aria-hidden="true">&times;</span>
					</button>
				</div>
				<div class="modal-body">
					<p id="progress-message" style="white-space:pre-line"><s:text name="tinterConfig.pleaseWaitTinterInit"/></p>
				</div>
				<div class="modal-footer">
					<!-- 									<button type="button" class="btn btn-primary" id="startDispenseButton">Start Dispense</button> -->
				</div>
			</div>
		</div>
	</div>

	<!-- Config Error Modal Window -->
	<div class="modal fade" aria-labelledby="configErrorModal"
		aria-hidden="true" id="configErrorModal" role="dialog">
		<div class="modal-dialog" role="document">
			<div class="modal-content">
				<div class="modal-header">
					<h5 class="modal-title"><s:text name="global.configurationError"/></h5>
					<button type="button" class="close" data-dismiss="modal"
						aria-label="%{getText('global.close')}">
						<span aria-hidden="true">&times;</span>
					</button>
				</div>
				<div class="modal-body">
					<p id="configError" ></p>
				</div>
				<div class="modal-footer">
					<s:url var="troubleshootURL" action="swdhTroubleshootAction"
						escapeAmp="false">
						<s:param name="reqGuid" value="%{thisGuid}" />
					</s:url>
					<s:url var="installURL" action="swdhInstallAction"
						escapeAmp="false">
						<s:param name="reqGuid" value="%{thisGuid}" />
					</s:url>
					<a href="<s:property value="troubleshootURL"/>"
						class="btn btn-primary"><s:text name="global.troubleshoot"/></a> <a
						href="<s:property value="installURL"/>" class="btn btn-success"><s:text name="global.install"/></a>
					<button type="button" class="btn btn-secondary"
						id="configErrorButton" data-dismiss="modal" aria-label="%{getText('global.close')}"><s:text name="global.close"/></button>
				</div>
			</div>
		</div>
	</div>
	<!-- Detect Status Modal Window -->
	<div class="modal fade" aria-labelledby="detectStatusModal"
		aria-hidden="true" id="detectStatusModal" role="dialog">
		<div class="modal-dialog" role="document">
			<div class="modal-content">
				<div class="modal-header">

					<h5 class="modal-title"><s:text name="tinterConfig.tinterInitializationStatus"/></h5>
				</div>
				<div class="modal-body">
					<p id="detectStatus" ></p>
				</div>
				<div class="modal-footer">
					<button id="detectStatusClose" type="button"
						class="btn btn-primary" id="detectStatusButton"
						data-dismiss="modal" aria-label="%{getText('global.continue')}"><s:text name="global.continue"/></button>
				</div>
			</div>
		</div>
	</div>
	<!-- Detect Error Modal Window -->
	<div class="modal fade" aria-labelledby="detectErrorModal"
		aria-hidden="true" id="detectErrorModal" role="dialog">
		<div class="modal-dialog" role="document">
			<div class="modal-content">
				<div class="modal-header">

					<h5 id="errorModalTitle" class="modal-title"><s:text name="tinterConfig.tinterInitializationError"/></h5>
				</div>
				<div class="modal-body">
					<p id="detectErrorMessage" ></p>
					<div>
						<ul id="detectErrorList">
						</ul>
					</div>
				</div>
				<div class="modal-footer">
					<button id="detectErrorClose" type="button" class="btn btn-primary"
						data-dismiss="modal" aria-label="%{getText('global.close')}"><s:text name="global.close"/></button>
				</div>
			</div>
		</div>
	</div>
	<!-- AS 9500SW password modal -->
	<div class="modal fade" aria-labelledby="" data-backdrop="static"
		aria-hidden="true" id="password_modal" role="dialog">
		<div class="modal-dialog" role="document">
			<div class="modal-content">
				<div class="modal-header">
					<h5 class="modal-title">AS 9500SW Password Setup</h5>
				</div>
				<div class="modal-body">
					<p id=""></p>
					<div id="passwrd" class="form-label-group">
						<label class="sw-label" for="passwrd">Password:</label>
						<s:textfield id="passwrd" name="passwrd"></s:textfield>
					</div>
				</div>
				<div class="modal-footer">
					<button id="" type="button" class="btn btn-primary"
						data-dismiss="modal" aria-label="%{getText('global.continue')}"><s:text name="global.continue"/></button>
				</div>
			</div>
		</div>
	</div>

	<br>




	<!-- Including footer -->
	<s:include value="Footer.jsp"></s:include>
</body>