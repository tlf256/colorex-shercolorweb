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
<link rel=StyleSheet href="css/bootstrap.min.css" type="text/css">
<link rel=StyleSheet href="css/bootstrapxtra.css" type="text/css">
<link rel=StyleSheet href="js/smoothness/jquery-ui.css"
	type="text/css">

<link rel=StyleSheet href="css/CustomerSherColorWeb.css" type="text/css">
<!-- Custom styles for this template -->

<link
	href="https://maxcdn.bootstrapcdn.com/font-awesome/4.7.0/css/font-awesome.min.css"
	rel="stylesheet">

<script type="text/javascript" charset="utf-8" src="js/jquery-3.4.1.min.js"></script>
<script type="text/javascript" charset="utf-8" src="js/jquery-ui.js"></script>
<script type="text/javascript" charset="utf-8" src="js/jquery.dataTables.min-1.10.16.js"></script>
<script type="text/javascript" charset="utf-8" src="js/popper.min.js"></script>
<script type="text/javascript" charset="utf-8" src="js/bootstrap.min.js"></script>

<script type="text/javascript" charset="utf-8" src="script/customershercolorweb-1.4.6.js"></script>
<script type="text/javascript" charset="utf-8" src="script/WSWrapper.js"></script>
<script type="text/javascript" charset="utf-8" src="script/tinter-1.4.7.js"></script>
<script type="text/javascript" charset="utf-8" src="js/jquery-ui.js"></script>





<script type="text/javascript">

	// now build the dispense formula object
	var ws_tinter = new WSWrapper("tinter");
	var myModelList = null;
	var reqGuid = "${reqGuid}";
	var initStarted = 0;
	var detectAttempt = 0;
	
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
		configuration = new Configuration(
				"<s:property value="tinter.clrntSysId" escapeHtml="true"/>",
				"<s:property value="tinter.model" escapeHtml="true"/>",
				"<s:property value="tinter.serialNbr" escapeHtml="true"/>", canister_layout);

	}
	function config_tinter(mycolorantid, mymodel, myserial, mycanister_layout) {
		var canister_layout = [];
		for (var i = 0, len = mycanister_layout.length; i < len; i++) {
			var code = mycanister_layout[i].clrntCode;
			var pos = i + 1;
			var canister1 = new Canister(pos, code);
			canister_layout.push(canister1);
		}
		var command = "Config"
		var configuration = new Configuration(mycolorantid, mymodel, myserial,
				canister_layout);

		var calibration = null;
		//alfa and santint do not have cal files that we manage
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
		var colorant = $('#selectClrntSysId').val();
		var model = $('#modelSelect').val();
		var serial = $('#tSerialNbr').val();
		console.log(model);
		if (colorant != -1 && model != -1 && serial != null && serial != "") {
			$
					.ajax({
						url : "GetCanisterList",
						context : document.body,
						data : {

							clrntSysId : colorant,
							tinterModel : model,
							tinterSerialNbr : serial,
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
                        		if (objs != null && objs.newtinter != null
    									&& objs.newtinter.serialNbr != null) {
    								config_tinter(objs.newtinter.clrntSysId,
    										objs.newtinter.model,
    										objs.newtinter.serialNbr,
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
		} else {
			alert('<s:text name="tinterConfig.emptyConfigSetting"/>');
			window.location.href = "tinterConfigAction?reqGuid=${reqGuid}";
		}
	}

	function ValidateSN() {
		var rc = 0;
		var SN = $('#tSerialNbr').val();

		if (SN.length < 5 || SN.length > 15) {
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

	function changeModel() {
		$('#tSerialNbr').focus();
	}
	function onSubmit(event){
		event.preventDefault();
		$('#tSerialNbr').val($('#tSerialNbr').val().toUpperCase());
		if (ValidateSN() == 0) {
			config();
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
		var colorant = $('#selectClrntSysId').val();
		var model = $('#modelSelect').val();
		var serial = $('#tSerialNbr').val();
		/*tinter.setClrntSysId("CCE");
		tinter.setModel("COROB UNITTEST");
		tinter.setSerialNbr("TESTSERIAL");
		*/
		var tinter = {
				clrntSysId:colorant,
				model:model,
				serialNbr:serial
					  };
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
			}
		});
	}
	function GetModelsForColorant(colorantId_obj) {
		var modellist = null;
		var colorantId = colorantId_obj.val();
		
		$
				.ajax({
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
		GetModelsForColorant($('#selectClrntSysId'));
		
		
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
		var model = $('#modelSelect').val();
			if(model.indexOf("COROB") >= 0 || model.indexOf("Corob") >= 0|| model.indexOf("SIMULATOR") >= 0){
			RecdMessageCorob();
		}
		else if(model.indexOf("FM X") >= 0){
			RecdMessageFMX();
		}
		else if(model.indexOf("SANTINT") >= 0){
			RecdMessageSantint();
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
				//status = 1, means, still trying serial ports so still in progress.
				if ((return_message.errorMessage.indexOf("Initialization Done") == -1) &&
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
				else if(return_message.errorMessage.indexOf("Initialization Done") >= 0){
					console.log("init done: " + return_message.errorMessage.indexOf("Initialization Done"));
					
					
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
				if (return_message.errorMessage.indexOf("Initialization Done") >= 0 || return_message.errorNumber < 0) {
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
				if ((return_message.errorMessage.indexOf("Initialization Done") == -1) && (return_message.errorNumber >= 0 ||
						 return_message.status == 1)) {
					 	//save		
					$("#progress-message").text(return_message.errorMessage);
					//console.log(return_message.errorMessage);
				}
				else if(return_message.errorMessage.indexOf("Initialization Done") >= 0){
					console.log("init done: " + return_message.errorMessage.indexOf("Initialization Done"));
					
					
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
				if (return_message.errorMessage.indexOf("Initialization Done") >= 0 || return_message.errorNumber < 0) {
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
			</div>

			<div class="form-label-group">

				<label class="sw-label" for="selectClrntSysId"><s:text name="tinterConfig.colorant"/></label>
				<s:select id="selectClrntSysId" name="newtinter.clrntSysId"
					headerKey="-1" headerValue="%{getText('global.selectColorant')}"
					list="defaultColorantList" onchange='GetModelsForColorant(this)' />


			</div>

			<div class="form-label-group">
				<label class="sw-label" for="modelSelect"><s:text name="tinterConfig.model"/></label>
				<s:select id="modelSelect" name="newtinter.Model"
					autofocus="autofocus" list="defaultModelList" headerKey="-1"
					headerValue="" onchange='changeModel()'>
				</s:select>

			</div>

			<div class="form-label-group">
				<label class="sw-label" for="tSerialNbr"><s:text name="tinterConfig.serialNumber"/></label>
				<s:textfield id="tSerialNbr" name="newtinter.serialNbr"></s:textfield>

				<p style="color: red; font-weight: bold" id="SNValidationError"></p>

				<br>

				<s:actionerror />
			</div>


			<div class="form-row">

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

	<br>




	<!-- Including footer -->
	<s:include value="Footer.jsp"></s:include>
</body>