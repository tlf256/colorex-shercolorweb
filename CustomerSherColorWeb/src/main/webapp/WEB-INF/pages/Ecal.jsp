<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!doctype html>


<%@ taglib prefix="s" uri="/struts-tags"%>

<html lang="en">
<head>
<meta name="viewport" content="width=device-width, initial-scale=1">
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">

<title><s:text name="global.calibrationManager"/></title>
<!-- JQuery -->
<link rel=StyleSheet href="css/bootstrap.min.css" type="text/css">
<link rel=StyleSheet href="css/bootstrapxtra.css" type="text/css">
<link rel=StyleSheet href="js/smoothness/jquery-ui.css"	type="text/css">
<link rel="StylesSheet" href="css/jquery.dataTables.min-1.10.16.css" type="text/css">
<link rel=StyleSheet href="css/CustomerSherColorWeb.css" type="text/css">
<link href="https://maxcdn.bootstrapcdn.com/font-awesome/4.7.0/css/font-awesome.min.css" rel="stylesheet">
<script type="text/javascript" charset="utf-8" src="js/jquery-3.4.1.min.js"></script>
<script type="text/javascript" charset="utf-8" src="js/jquery-ui.js"></script>
<script type="text/javascript" charset="utf-8" src="js/jquery.dataTables.min-1.10.16.js"></script>
<script type="text/javascript" charset="utf-8" src="js/bootstrap.min.js"></script>
<script type="text/javascript" charset="utf-8" src="script/customershercolorweb-1.4.5.js"></script>
<script type="text/javascript" charset="utf-8" src="script/WSWrapper.js"></script>
<script type="text/javascript" charset="utf-8" src="script/tinter-1.4.4.js"></script>



<script type="text/javascript">
	// now build the dispense formula object
		ws_tinter = new WSWrapper("tinter");
	var sendingTinterCommand = "false";
	var myModelList = null;
	reqGuid = "${reqGuid}";
	configuration = setTinterConfig();

	function setTinterConfig() {
		<s:iterator value="tinter.canisterList" status="clrnt">
		var canister<s:property value="#clrnt.index"/> = new Canister(
				<s:property value="position"/>,
				"<s:property value="clrntCode"/>");
		</s:iterator>
		var canister_layout = [ canister0, canister1, canister2, canister3,
				canister4, canister5, canister6, canister7, canister8,
				canister9, canister10, canister11 ];
		var configuration = new Configuration(
				"<s:property value="tinter.clrntSysId" escapeHtml="true"/>",
				"<s:property value="tinter.model" escapeHtml="true"/>",
				"<s:property value="tinter.serialNbr" escapeHtml="true"/>", canister_layout);
		return configuration;

	}
	function buildEcal() {
		var dt_array1 = [];
		<s:iterator value="ecalList">
	
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
		$('<option>').val("-1").text("Select").appendTo($select);
		$.each(attributeActions, function(index, value) {
			$('<option>').val(value).text(value).appendTo($select);
		});
	}
	function init() {
		var command = "Detect"; //TODO change back to detect
		initmessage = new TinterMessage(command, null, null, null,null);
		var json = JSON.stringify(initmessage);

		if (ws_tinter != null && ws_tinter.isReady == "false") {
			console
					.log("WSWrapper connection has been closed (timeout is defaulted to 5 minutes). Make a new WSWrapper.")
			ws_tinter = new WSWrapper("tinter");
		}
		sendingTinterCommand = true;
		ws_tinter.send(json);
	}

	function DetectFMResp(return_message){
		var initErrorList;
		var curDate = new Date();
	
		//status = 1, means, still trying serial ports so still in progress.
		if (return_message.errorMessage.indexOf("Initialization Done") == -1 && (return_message.errorNumber >= 0 ||
				 return_message.status == 1)) {
			 	//save				
			$("#progress-message").text(return_message.errorMessage);
			console.log(return_message);
		}
		else if(return_message.errorMessage.indexOf("Initialization Done") >=0){
			
			sendTinterEvent(reqGuid, curDate, return_message, null); 
		
			// Detected and no errors from tinter 
            $("#initTinterInProgressModal").modal('hide');
			
			$("#tinterAlertList").append("<li>Tinter Initialized.</li>");
			if($("#tinterAlert").hasClass("d-none")) $("#tinterAlert").removeClass("d-none");
			if($("#tinterAlert").hasClass("alert-danger")) $("#tinterAlert").removeClass("alert-danger");
			if(!$("#tinterAlert").hasClass("alert-success")) $("#tinterAlert").addClass("alert-success");
		
			 initErrorList = [];
		
		} else {
			if (return_message.errorNumber == -10500 ){
				// show warnings?
				sendTinterEvent(reqGuid, curDate, return_message, null); 
				initErrorList = [];
			
				waitForShowAndHide("#initTinterInProgressModal");
			} else {
				sendTinterEvent(reqGuid, curDate, return_message, null); 
				waitForShowAndHide("#initTinterInProgressModal");
				$("#tinterErrorListModal").modal('show');
				//Show a modal with error message to make sure the user is forced to read it.
				$("#tinterErrorList").empty();
				initErrorList = [];
				if(return_message.errorList!=null && return_message.errorList[0]!=null){
					return_message.errorList.forEach(function(item){
						$("#tinterAlertList").append("<li>"+thisError+"</li>");
						$("#tinterErrorList").append("<li>" + return_message.errorMessage + "</li>");
						initErrorList.push(item.message);
					});
				} else {
					initErrorList.push(return_message.errorMessage);
					$("#tinterErrorList").append("<li>" + return_message.errorMessage + "</li>");
				}
				$("#tinterErrorListTitle").text('<s:text name="global.tinterDetectandInitializationFailed"/>');
				$("#tinterErrorListSummary").text('<s:text name="global.resolveIssueBeforeDispense"/>');
				
				
				//Show alerts in main alert section in middle of screen
				if(initErrorList[0]!=null){
					$("#tinterAlertList").empty();
					initErrorList.forEach(function(thisError){
						$("#tinterAlertList").append("<li>"+thisError+"</li>");
					});
					if($("#tinterAlert").hasClass("d-none")) $("#tinterAlert").removeClass("d-none");
					if(!$("#tinterAlert").hasClass("alert-danger")) $("#tinterAlert").addClass("alert-danger");
					if($("#tinterAlert").hasClass("alert-success")) $("#tinterAlert").removeClass("alert-success");
				} else {
					if(!$("#tinterAlert").hasClass("d-none")) $("#tinterAlert").addClass("d-none");
				}
			
			}
		}
		sendingTinterCommand = "false";
		
		
	}
	function DetectCorobResp(return_message){
		var initErrorList;
		var curDate = new Date();
	
		sendTinterEvent(reqGuid, curDate, return_message, null); 
		// update canister layout if tinter is corob custom
		if(return_message.configuration != null && return_message.configuration.model != null 
				&& return_message.configuration.model.includes("COROB CUSTOM")){
			updateColorantsTxt(reqGuid, return_message, false, null);
		}
		
		if(return_message.errorNumber == 0 && return_message.commandRC == 0){
			// Detected and no errors from tinter 
            waitForShowAndHide("#initTinterInProgressModal");
			$("#tinterAlertList").empty();
			$("#tinterAlertList").append('<li><s:text name="ecal.tinterInitSuccessful"/></li>');
			if($("#tinterAlert").hasClass("d-none")) $("#tinterAlert").removeClass("d-none");
			if($("#tinterAlert").hasClass("alert-danger")) $("#tinterAlert").removeClass("alert-danger");
			if(!$("#tinterAlert").hasClass("alert-success")) $("#tinterAlert").addClass("alert-success");
			initErrorList = [];
		
		} else {
			if (return_message.errorNumber == -10500 && return_message.commandRC == -10500){
				// show warnings?
				initErrorList = [];
				waitForShowAndHide("#initTinterInProgressModal");
			} else {
				waitForShowAndHide("#initTinterInProgressModal");
				$("#tinterErrorListModal").modal('show');
				//Show a modal with error message to make sure the user is forced to read it.
				$("#tinterErrorList").empty();
				initErrorList = [];
				if(return_message.errorList!=null && return_message.errorList[0]!=null){
					return_message.errorList.forEach(function(item){
						$("#tinterAlertList").append("<li>"+thisError+"</li>");
						$("#tinterErrorList").append("<li>" + return_message.errorMessage + "</li>");
						initErrorList.push(item.message);
					});
				} else {
					initErrorList.push(return_message.errorMessage);
					$("#tinterErrorList").append("<li>" + return_message.errorMessage + "</li>");
				}
				$("#tinterErrorListTitle").text('<s:text name="global.tinterDetectandInitializationFailed"/>');
				$("#tinterErrorListSummary").text('<s:text name="global.resolveIssuesBeforeDispense"/>');
				
				
				if(initErrorList[0]!=null){
					$("#tinterAlertList").empty();
					initErrorList.forEach(function(thisError){
						$("#tinterAlertList").append("<li>"+thisError+"</li>");
					});
					if($("#tinterAlert").hasClass("d-none")) $("#tinterAlert").removeClass("d-none");
					if(!$("#tinterAlert").hasClass("alert-danger")) $("#tinterAlert").addClass("alert-danger");
					if($("#tinterAlert").hasClass("alert-success")) $("#tinterAlert").removeClass("alert-success");
					
				} else {
					if(!$("#tinterAlert").hasClass("d-none")) $("#tinterAlert").addClass("d-none");
				}
			}
		}
		sendingTinterCommand = "false";
	
	}
	function putECal(msg,myGuid){
		if(reqGuid !=null){
			var date = new Date();
			var month = (date.getMonth() + 1).toString();
			if (month.length == 1){
				month = "0" + month;
			}
			var day = date.getDate().toString();
			if(day.length == 1){
				day = "0" + day;
			}
			var time = date.getHours() + "" + date.getMinutes();
			var fulldate = date.getFullYear()+ "" + month + "" + day;
			
			$.ajax({
				url: "uploadEcal",
				context: document.body,
				//processData: false,
				traditional: true,
				data: { 
					reqGuid : myGuid, //without this guid you will get a login exception and you won't even get an error
					filename: msg.calibration.filename,
					uploaddate:fulldate,
					uploadtime: time,
					data: (msg.calibration.data)
				},
				async: true,
				type: "POST",
				dataType: "json",
				success: function (result) {
					if(result.sessionStatus === "expired"){
                		window.location = "/CustomerSherColorWeb/invalidLoginAction.action";
                	}
                	else{
                		complete = true;
    					//alert("Cal Upload complete for " + msg.calibration.filename);
    					$("#ecalStatusModal").modal('show');
    					$("#ecalStatus")
    							.text(
    									'<s:text name="ecal.calUploadComplete"><s:param>'+ msg.calibration.filename +'</s:param></s:text>');
    					console.log(result);
    					window.location.reload();

                	}
				},
				error: function(event){
					calobj="error";
					//alert("Could not upload calibration  for " + msg.calibration.filename + " " + event.statusText);
					console.log(event);
					$("#ecalError").text('<s:text name="ecal.couldNotUploadCalibration"><s:param>'+ 
										msg.calibration.filename +'</s:param><s:param>'+
										event.statusText +'</s:param></s:text>');
					$("#ecalErrorModal").modal('show');	
				}
			});
			}
			else {
				//alert("Not logged in.  ReqGuid not found");
				$("#ecalError").text('<s:text name="global.notLoggedInReqGuidNotFound"/>');
				$("#ecalErrorModal").modal('show');	
			}
		}
	function RecdMessage() {
		var curDate = new Date();
		console.log("Received Message");
		//parse the spectro
		//console.log(ws_tinter.wsmsg);
		console.log("isReady is " + ws_tinter.isReady + "BTW");
		if (ws_tinter.wserrormsg != null && ws_tinter.wserrormsg != "") {
			if (sendingTinterCommand == "true") {
				// received an error from WSWrapper so we won't get any JSON result

				//Show a modal with error message to make sure the user is forced to read it.
				$("#swDeviceError").text(ws_tinter.wserrormsg);

				$("#errorModal").modal('show');
			} else {
				console.log("Received unsolicited error "
						+ ws_tinter.wserrormsg);
				// so far this only happens when SWDeviceHandler is not running and we created a new WSWrapper when 
				// page intially loaded.  For now wait until they do a configense to show the error (no everybody has a tinter)
			}
		} else {

			sendingTinterCommand = "false";
			var return_message = JSON.parse(ws_tinter.wsmsg);
			switch (return_message.command) {
			case 'CalDownload':
				if (return_message.errorNumber == 0
						&& return_message.commandRC == 0) {
					init();
					$("#initTinterInProgressModal").modal('show');
				} else {
					$("#Error").text(return_message.errorMessage);
					$("#errorModal").modal('show');
				}
				var evMessage = new TinterMessage(return_message.command, null, null, null,null);
				evMessage.errorMessage=return_message.errorMessage + " for " + $('#filename').val();
				evMessage.errorNumber=return_message.errorNumber;
				evMessage.errorSeverity= return_message.errorSeverity;
				
				sendTinterEvent(reqGuid, curDate, evMessage, null); 
				break;
			case 'CalUpload':
			 	putECal(return_message,reqGuid);
			 	var evMessage = new TinterMessage(return_message.command, null, null, null,null);
				evMessage.errorMessage=return_message.errorMessage + " for " + return_message.calibration.filename;
				evMessage.errorNumber=return_message.errorNumber;
				evMessage.errorSeverity= return_message.errorSeverity;
			
				sendTinterEvent(reqGuid, curDate, evMessage,null); 
			 	  $('#upload_ecal').prop('disabled', false); // re-enable button
				break;
			case 'Detect':
			case 'Init':
				if(configuration.model.indexOf("COROB") >= 0 ||
						configuration.model.indexOf("Corob") >=0 ){
					DetectCorobResp(return_message);
				}
				else{
					DetectFMResp(return_message);								
				}
	    	
				break;
				  waitForShowAndHide("#detectInProgressModal");
				  $('#btn_downloadEcal').prop('disabled', false); //reenable button
				if (return_message.errorNumber == 0
						&& return_message.commandRC == 0) {
					//save
					$('#detectInProgressModal').on('hidden.bs.modal', function(e) {
						$("#detectStatusModal").modal('show');
		            });
					
					$("#detectStatus")
							.text(
									'<s:text name="global.tinterDetectConfigurationComplete"/>');
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
										'<s:text name="global.tinterDetectConfigurationComplete"/>');
						$("#detectErrorMessage").text(
								return_message.errorMessage);
						break;
					default:
						$("#errorModalTitle").text(
								'<s:text name="global.tinterInitErrorsClickContinue"/>')
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
				break;
			default:
				//Not an response we expected...
				console
						.log("Message from different command is junk, throw it out");
			
			}
		}
	}


	
	$(document).on("click", "#upload_ecal", function(event) {
		event.preventDefault();
		  $('#upload_ecal').prop('disabled', true);
		var cmd = "CalUpload";
		var tintermessage = new TinterMessage(cmd,null,null,null,null); 
		var json = JSON.stringify(tintermessage);
		sendingTinterCommand = "true";
		if (ws_tinter != null && ws_tinter.isReady == "false") {
			console
					.log("WSWrapper connection has been closed (timeout is defaulted to 5 minutes). Make a new WSWrapper.")
			ws_tinter = new WSWrapper("tinter");
		}
		ws_tinter.send(json);

	});
	$(document).on("click", "#btn_downloadEcal", function(event) {
		event.preventDefault();
		
		var filename = $("input[name='filename']:checked"). val();
		
			if(filename != null && filename !== undefined){
			    $('#btn_downloadEcal').prop('disabled', true);
				
				calibration = new Calibration_Download( filename);
				var split_fn = filename.split("_");
				var mycolorantid = split_fn[0];
				var gdata = new GData(mycolorantid);
				var cmd = "CalDownload";
				var tintermessage = new TinterMessage(cmd,null,null,calibration,gdata); 
				var json = JSON.stringify(tintermessage);
				sendingTinterCommand = "true";
				if (ws_tinter != null && ws_tinter.isReady == "false") {
					console
							.log("WSWrapper connection has been closed (timeout is defaulted to 5 minutes). Make a new WSWrapper.")
					ws_tinter = new WSWrapper("tinter");
				}
				ws_tinter.send(json);
				//alert("Calibration for " + filename + " sent to tinter.");
				$("#tinterAlertList").empty();
				$("#tinterAlertList").append('<li><s:text name="ecal.calibrationSentToTinter"><s:param>'+ filename +'</s:param></s:text>');
				if($("#tinterAlert").hasClass("d-none")) $("#tinterAlert").removeClass("d-none");
				if($("#tinterAlert").hasClass("alert-danger")) $("#tinterAlert").removeClass("alert-danger");
				if(!$("#tinterAlert").hasClass("alert-success")) $("#tinterAlert").addClass("alert-success");
		
				
				}
			else{
					//alert("There is no filename for the selected line. Please choose again.");
					$("#ecalError").text('<s:text name="ecal.noValidFileSelected"/>');
					$("#ecalErrorModal").modal('show');	
				}

	});
	
	// 	
	$(document)
			.ready(
					function() {
						$('#ecals').show();

						var dt_arr = buildEcal();

						var ecal_table = $('#ecals_table')
								.DataTable(
										{
											"scrollY" : "200px",
											"scrollX" : "460px",
											scrollCollapse : true,
											data : dt_arr,
											dom : 'rt',
											columns : [
													{

														data : 'colorantId'
													},
													{
														data : 'tinterModel'
													},
													{
														data : 'tinterSerial'
													},
													{
														data : 'uploaddate'
													},
													{
														data : 'uploadtime'
													},
													{
														"render" : function(
																data, type, row,meta) {
															if(meta.row == 0){
																return '<input id="filename" checked="checked" type="radio" name="filename" value=' +row.filename+'>';
															}
															else{
																return '<input id="filename" type="radio" name="filename" value=' +row.filename+'>';
																}
															
														}
													} // here's a radio button, modify to use data to populate it
											]
										});

					});
</script>
</head>

<body>
	<!-- including Header -->
	<s:include value="Header.jsp"></s:include>
	<div class="container center-form-parent">



		<s:set var="thisGuid" value="reqGuid" />

		<s:form class="form-sw-ecal-centered" id="frmSubmit"
			 validate="true" theme="bootstrap">
			<div class="text-center mb-4">
				<s:hidden name="reqGuid" value="%{reqGuid}" />
				<s:hidden name="reReadLocalHostTinter" value="true" />
				<!--  <img class="mb-4"
				src="graphics/shercolor-lg.jpg"
				alt="" width="72" height="72"> -->
				<h1 class="h3 mb-3 font-weight-normal"><s:text name="global.calibrationManager"/></h1>

				<p>
					<a href="#" data-toggle="tooltip"
						title="Use 'Upload' to save your calibration to SW.  Then you can download it later if you change computers or 
						you need fix a tinter error caused by a bad calibration">
						<s:text name="ecal.yourCalibrationFiles"/></a>
				</p>
			</div>

			<div class="form-label-group">

				<table id="ecals_table" class="table  table-striped table-bordered">
					<thead>
						<tr>
							<th><s:text name="ecal.clrnt"/></th>
							<th><s:text name="ecal.tinterModel"/></th>
							<th><s:text name="ecal.tinterSerial"/></th>
							<th><s:text name="ecal.uploadDate"/></th>
							<th><s:text name="ecal.uploadTime"/></th>
							<th><s:text name="ecal.choose"/></th>
						</tr>
					</thead>
					<tbody></tbody>
					<tfoot></tfoot>
				</table>

			</div>



			<div class="form-row">
				<div
					class="alert alert-danger d-none"
					id="tinterAlert" style="width: 100%">
					<ul class="list-unstyled" id="tinterAlertList">
					</ul>
					<!-- Put text here -->
				</div>

			</div>
			<!-- d-flex flex-row justify-content-around -->
			<div>
				<button  type="button" class="btn btn-primary " id="btn_downloadEcal"
					data-toggle="modal" data-target="#verifyModal"
					autofocus="autofocus"><s:text name="ecal.downloadCalibration"/></button>
			
				<button style="float:right" class="btn btn-primary mr-auto" id="upload_ecal">
					<s:text name="ecal.uploadCalibration"/></button>
			</div>
				
			<div class="form-row">
				
			</div>
			<hr/>
			<div class="form-row">

				<s:submit class="btn btn-secondary btn-block" id="btn_done"  action="loginAction" value="%{getText('global.done')}" />
			</div>

		</s:form>
	</div>
	<br>

	<!-- form-group -->
	<!-- Detect in Progress Modal Window -->
	<div class="modal fade" aria-labelledby="detectInProgressModal"
		aria-hidden="true" id="detectInProgressModal" role="dialog"
		data-backdrop="static" data-keyboard="false">
		<div class="modal-dialog" role="document">
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal"
						aria-label="Close">
						<span aria-hidden="true">&times;</span>
					</button>
					<h4 class="modal-title"><s:text name="ecal.detectInProgress"/></h4>
				</div>
				<div class="modal-body">
					<p ><s:text name="ecal.pleaseWaitTinterDetect"/></p>
				</div>
				<div class="modal-footer">
					<!-- 									<button type="button" class="btn btn-primary" id="startDispenseButton">Start Dispense</button> -->
				</div>
			</div>
		</div>
	</div>
	<!-- Tinter Detect in Progress Modal Window -->
	<div class="modal fade" aria-labelledby="initTinterInProgressModal"
		aria-hidden="true" id="initTinterInProgressModal" role="dialog"
		data-backdrop="static" data-keyboard="false">
		<div class="modal-dialog" role="document">
			<div class="modal-content">
				<div class="modal-header">
					
					<h4 class="modal-title"><s:text name="global.tinterDetectionAndInitialization"/></h4>
					<button type="button" class="close" data-dismiss="modal"
						aria-label="Close">
						<span aria-hidden="true">&times;</span>
					</button>
				</div>
				<div class="modal-body">
					<p id="progress-message" ><s:text name="global.pleaseWaitDetectInitTinter"/></p>
				</div>
				<div class="modal-footer"></div>
			</div>
		</div>
	</div>
	<!--  Error Modal Window -->
	<div class="modal fade" aria-labelledby="errorModal" aria-hidden="true"
		id="errorModal" role="dialog">
		<div class="modal-dialog" role="document">
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal"
						aria-label="Close">
						<span aria-hidden="true">&times;</span>
					</button>
					<h4 class="modal-title"><s:text name="ecal.error"/></h4>
				</div>
				<div class="modal-body">
					<p id="swDeviceError" ></p>
				</div>
				<div class="modal-footer">
					<button type="button" class="btn btn-default" id="errorButton"
						data-dismiss="modal" aria-label="Close"><s:text name="global.close"/></button>
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

					<h4 class="modal-title"><s:text name="ecal.tinterInitStatus"/></h4>
				</div>
				<div class="modal-body">
					<p id="detectStatus"></p>

				</div>
				<div class="modal-footer">
					<button id="detectStatusClose" type="button"
						class="btn btn-primary" id="detectStatusButton"
						data-dismiss="modal" aria-label="Close"><s:text name="global.continue"/></button>
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

					<h4 id="errorModalTitle" class="modal-title"><s:text name="ecal.tinterInitError"/></h4>
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
						 data-dismiss="modal" aria-label="Close"><s:text name="global.close"/></button>
				</div>
			</div>
		</div>
	</div>


	
	<!-- Ecal Error Modal Window -->
	<div class="modal fade" aria-labelledby="ecalErrorModal"
		aria-hidden="true" id="ecalErrorModal" role="dialog">
		<div class="modal-dialog" role="document">
			<div class="modal-content">
				<div class="modal-header">
					<h5 class="modal-title"><s:text name="global.calibrationTransferError"/></h5>
					<button type="button" class="close" data-dismiss="modal"
						aria-label="Close">
						<span aria-hidden="true">&times;</span>
					</button>
				</div>
				<div class="modal-body">
					<p id="ecalError" ></p>
				</div>
				<div class="modal-footer">
					<button type="button" class="btn btn-primary" id="ecalErrorButton"
						data-dismiss="modal" aria-label="Close"><s:text name="global.close"/></button>
				</div>
			</div>
		</div>
	</div>
	<!-- Detect Status Modal Window -->
	<div class="modal fade" aria-labelledby="ecalStatusModal"
		aria-hidden="true" id="ecalStatusModal" role="dialog">
		<div class="modal-dialog" role="document">
			<div class="modal-content">
				<div class="modal-header">
					<h5 class="modal-title"><s:text name="ecal.calibrationTransferStatus"/></h5>
				</div>
				<div class="modal-body">
					<p id="ecalStatus" ></p>

				</div>
				<div class="modal-footer">
					<button id="ecalStatusClose" type="button" class="btn btn-primary"
						 data-dismiss="modal" aria-label="Close"><s:text name="global.continue"/></button>
				</div>
			</div>
		</div>
	</div>
	<!-- Tinter ErrorList Modal Window -->
	<div class="modal fade" aria-labelledby="tinterErrorListModal"
		aria-hidden="true" id="tinterErrorListModal" role="dialog">
		<div class="modal-dialog" role="document">
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal"
						aria-label="Close">
						<span aria-hidden="true">&times;</span>
					</button>
					<h4 class="modal-title" id="tinterErrorListTitle"><s:text name="global.tinterError"/></h4>
				</div>
				<div class="modal-body">
					<div>
						<ul class="list-unstyled" id="tinterErrorList">
						</ul>
					</div>
					<p id="tinterErrorListSummary" ></p>
				</div>
				<div class="modal-footer">
					<button type="button" class="btn btn-primary"
						id="tinterErrorListOK" data-dismiss="modal" aria-label="Close"><s:text name="global.ok"/></button>
				</div>
			</div>
		</div>
	</div>


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
	</script>

	<!-- Including footer -->
	<s:include value="Footer.jsp"></s:include>

</body>