<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!doctype html>


<%@ taglib prefix="s" uri="/struts-tags"%>

<html lang="en">
<head>
<meta name="viewport" content="width=device-width, initial-scale=1">
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">

<title><s:text name="configureSpectro.configureColorEye" /></title>
<!-- JQuery -->
<link rel="stylesheet" href="css/bootstrap.min.css" type="text/css">
<link rel="stylesheet" href="css/bootstrapxtra.css" type="text/css">
<link rel="stylesheet" href="js/smoothness/jquery-ui.min.css" type="text/css">
<link rel="stylesheet" href="css/CustomerSherColorWeb.css" type="text/css">
<link rel="stylesheet" href="css/font-awesome.css" type="text/css">
<script type="text/javascript" charset="utf-8" src="js/jquery-3.4.1.min.js"></script>
<script type="text/javascript" charset="utf-8" src="js/jquery-ui.min.js"></script>
<script type="text/javascript" charset="utf-8" src="js/bootstrap.min.js"></script>
<script type="text/javascript" charset="utf-8" src="script/customershercolorweb-1.4.6.js"></script>
<script type="text/javascript" src="script/spectro.js"></script>
<script type="text/javascript" src="script/WSWrapper.js"></script>
<script>
			var calibrate_step = "start";		
		  	var ws_coloreye = new WSWrapper('coloreye');
		  	
		  	function InitializeConfigureScreen() {
	  		    console.log("InitializeConfigureScreen");
	  		  	var y = document.getElementById("nextButton").style.display = "none";
				$(".pleasewait").show();
				$(".success").hide();
				waitForShowAndHide("#confirmConfigModal");
				$(".error").hide();
	  		}
		  	
	  	  	function DisplaySuccess() {
	  		  	console.log("DisplaySuccess")	
	  		  	var x = document.getElementById("cancelButton").style.display = "none";
	  		  	var y = document.getElementById("configureButton").style.display = "none";
	  		  	var z = document.getElementById("nextButton").style.display = "inline";
			    $(".pleasewait").hide();
			    waitForShowAndHide("#confirmConfigModal");
		  		$(".success").show();
		  		$(".error").hide();
	  		}

	  	  	function DisplayConfirm() {
	  		  	console.log("DisplayConfirm");
	  		  	var y = document.getElementById("nextButton").style.display = "none";
			    $(".pleasewait").hide();
		  		$(".success").hide();
		  		$(".error").hide();
		  		$("#confirmConfigModal").modal('show');
	  		}

	  	  	
	  	  	function DisplayError() {
	  		  	console.log("DisplayError")
	  		  	var x = document.getElementById("configureButton").style.display = "none";
	  		  	var y = document.getElementById("nextButton").style.display = "none";
	  		  waitForShowAndHide("#confirmConfigModal");
			    $(".pleasewait").hide();
		  		$(".success").hide();
		  		$(".error").show();
		  		
	  		}
	  	  	
			function readLocalhostSpectroConfig(){
				var cmd = "ReadConfig";
		    	console.log("ReadConfig ws_coloreye.context is " + ws_coloreye.deviceContext);
		    	
				var spectromessage = new SpectroMessage(cmd,getCheckedButton("selectedSpectroTypes"), "");
				spectromessage.messageName = "SpectroMessage";

				spectromessage.spectroConfig.port  = " USB";
				var json = JSON.stringify(spectromessage);
				sendingSpectroCommand = "true";
				ws_coloreye.send(json);
			}
	  	  	
			function configureSpectro(){
				var spectromessage = new SpectroMessage("Configure",getCheckedButton("selectedSpectroTypes"), "");
				var json = JSON.stringify(spectromessage);
				if(ws_coloreye && ws_coloreye.isReady=="false") {
		    		console.log("WSWrapper connection has been closed (timeout is defaulted to 5 minutes). Make a new WSWrapper.")
		    		ws_coloreye = new WSWrapper("coloreye");
				}
				console.log("json Configure msg is " + json);
				ws_coloreye.send(json);
			}
			
			function configureOverrideSpectro(){
				var spectromessage = new SpectroMessage("ConfigureOverride",getCheckedButton("selectedSpectroTypes"), "");
				var json = JSON.stringify(spectromessage);
				console.log("json ConfigureOverride msg is " + json);
				if(ws_coloreye && ws_coloreye.isReady=="false") {
		    		console.log("WSWrapper connection has been closed (timeout is defaulted to 5 minutes). Make a new WSWrapper.")
		    		ws_coloreye = new WSWrapper("coloreye");
				}
				ws_coloreye.send(json);
			}
			
			function getCheckedButton(name) {
				var buttons = document.getElementsByName(name);
				for (var i=0; i<buttons.length; i++) {
					if (buttons[i].checked) {
					    return buttons[i].value;
					}
				}
			}
			
			function saveSpectroConfigToSession(mymodel, myserial) {
				var myGuid = $( "#configureSpectroNextAction_reqGuid" ).val();
				console.log("in ConfigureSpectro.jsp, saveSpectroConfigToSession, model is " + mymodel);
	 			$.getJSON("stampSessionSpectroAction.action?reqGuid="+encodeURIComponent(myGuid)+"&spectroModel="+encodeURIComponent(mymodel)+"&spectroSerial="+encodeURIComponent(myserial), function(data){
	 				//TODO anything to do here?  maybe check result of stamp...
	 			});
			}
	  	  	
	  	  
	  	  	function RecdMessage() {
	  		  	console.log("Received Message");
	  		  	//parse the spectro
	  		  	console.log("Message is " + ws_coloreye.wsmsg);
	  		  	console.log("isReady is " + ws_coloreye.isReady + "BTW");
	  		  if(ws_coloreye && ws_coloreye.wserrormsg!=null && ws_coloreye.wserrormsg!=""){
					// received an error from WSWrapper so we won't get any JSON result (probably no SWDeviceHandler)
					// If we are sending a ReadConfig command don't show any error (localhost has no devices)
					// If we are sending a Detect command, show as detect error
					//TODO Show a modal with error message to make sure the user is forced to read it.
					$("#detectError").text(ws_coloreye.wserrormsg);
					$("#detectErrorModal").modal('show');
	  			  
	  		  } else {
	  			var return_message=JSON.parse(ws_coloreye.wsmsg);
	  			switch (return_message.command) {
	  				case 'Configure':
	  					if (return_message.errorMessage!="") {
	  						$("#errmsg").text(return_message.errorMessage);
	  						DisplayError();
	  					} else {
							if (return_message.responseMessage=="" || return_message.responseMessage=="Success") {
								//a successful configure will also update the localhost spectroconfig file with 
								//the correct model and serial number.  Call that to get the model and serial number.
								readLocalhostSpectroConfig();
								//saveSpectroConfigToSession(return_message.spectroConfig.model, return_message.spectroConfig.serial);
		  						//DisplaySuccess();
							} else {
								$("#confirmmsg").text(return_message.responseMessage);
								DisplayConfirm();
							}
	  					}
	  					break;
	  				case 'ConfigureOverride':
	  					if (return_message.errorMessage!="") {
	  						$("#errmsg").text(return_message.errorMessage);
	  						DisplayError();
	  					} else {
							if (return_message.responseMessage=="" || return_message.responseMessage=="Success") {
								//a successful configure will also update the localhost spectroconfig file with 
								//the correct model and serial number.  Call that to get the model and serial number.
								readLocalhostSpectroConfig();
								//saveSpectroConfigToSession(return_message.spectroConfig.model, return_message.spectroConfig.serial);
		  						//DisplaySuccess();
							} else {
		  						$("#errmsg").text(return_message.responseMessage);
		  						DisplayError();
							}
	  					}
	  					break;
	  				case 'ReadConfig':
	  					if (return_message.errorMessage!="") {
	  						$("#errmsg").text(return_message.errorMessage);
	  						DisplayError();
	  					} else {
							if (return_message.responseMessage=="" || return_message.responseMessage=="Success") {
								saveSpectroConfigToSession(return_message.spectroConfig.model, return_message.spectroConfig.serial);
		  						DisplaySuccess();
							} else {
		  						$("#errmsg").text(return_message.responseMessage);
		  						DisplayError();
							}
	  					}
	  					break;
  					default:
  						//Not an response we expected...
  						$("#errmsg").text("<s:text name='global.unexpectedCallToErr'><s:param>" + return_message.command + "</s:param></s:text>");
		  		  		DisplayError();
	  			}
	  		  }	
	  	  	}
	  	  	
	  		$(document).on("click", "#configureButton", function(event){
	  			event.preventDefault();
	  			configureSpectro();
	  		});
	  		
	  		$(document).on("click", "#configureOverrideButton", function(event){
	  			event.preventDefault();
	  			configureOverrideSpectro();
	  		});
				
			$(document).ready(function() {	
				console.log("in docready");
				InitializeConfigureScreen();
			});

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

		<%-- 			this guid is <s:property value="thisGuid"/> --%>
		<%-- 			this guid is <s:property value="%{reqGuid}"/> --%>
		<!-- 			<br> -->
		<%-- 			this sess is <s:property value="#session"/> --%>
		<!-- 			<br> -->
		<%-- 			jf obj is <s:property value="#session[reqGuid].jobFieldList"/> --%>


		<br>
		<s:form action="configureSpectroNextAction" validate="true"
			theme="bootstrap">
			<s:hidden name="reqGuid" value="%{reqGuid}" />
			<div class="row">

				<div class="col-sm-2">
					<s:hidden name="spectroModel" id="spectroModel"
						value="%{#session[reqGuid].spectro.model}" />
					<s:hidden name="spectroSerial" id="spectroSerial"
						value="%{#session[reqGuid].spectro.serialNbr}" />
				</div>
				<div class="col-sm-4">
					<div class="form-group">
           				<strong><s:text name="configureSpectro.colorEyeModel" /></strong>
           				<div class="controls">
           					<s:iterator value="spectrotypes" status="i">
            					<div class="form-check">
            					  <s:if test="%{#i.index == 0}">
            					  	<input class="form-check-input" type="radio" name="selectedSpectroTypes" value='<s:property value="key"/>' id="selectedSpectroTypes-<s:property value="%{#i.index}"/>" checked>
            					  </s:if>
            					  <s:else>
            					  	<input class="form-check-input" type="radio" name="selectedSpectroTypes" value='<s:property value="key"/>' id="selectedSpectroTypes-<s:property value="%{#i.index}"/>">
            					  </s:else>
								  <label class="form-check-label font-weight-normal" for="selectedSpectroTypes-<s:property value="%{#i.index}"/>">
								    <s:property value="value"/>
								  </label>
								</div>
            				</s:iterator>
           				</div>
           			</div>
					<%-- <s:radio label="Color Eye Model" name="selectedSpectroTypes"
						list="spectrotypes" value="defaultSpectroTypeValue" /> --%> 
				</div>

			</div>

			<div class="row">

				<div class="col-sm-2"></div>
				<div class="col-sm-8"></div>

			</div>

			<div class="row">

				<div class="col-sm-2"></div>
				<div class="col-sm-8"></div>

			</div>

			<div class="row">

				<div class="col-sm-2"></div>
				<div class="col-sm-8">
					<h2 class="success"></h2>
					<h2 class="error"></h2>
					<h2 class="pleasewait"><s:text name="configureSpectro.connectColorEye" /></h2>
				</div>

			</div>

			<div class="row">

				<div class="col-sm-2"></div>
				<div class="col-sm-8">
					<h2 class="success"><s:text name="configureSpectro.colorEyeConfigurationSuccessful" /></h2>
					<h2 class="error" id="errmsg"></h2>
					<h2 class="pleasewait"><s:text name="configureSpectro.selectColorEye" /></h2>
				</div>

			</div>

			<div class="row">

				<div class="col-sm-2"></div>
				<div class="col-sm-8">
					<h2 class="success"></h2>
					<h2 class="error"></h2>
					<h2 class="pleasewait"><s:text name="configureSpectro.clickNext" /></h2>
				</div>

			</div>

			<div class="row">

				<div class="col-sm-2"></div>
				<div class="col-sm-8"></div>

			</div>

			<div class="row">

				<div class="col-sm-2"></div>
				<div class="col-sm-4">
					<s:submit cssClass="btn btn-primary center-block btn-lg"
						id="configureButton" value="%{getText('global.next')}" />
					<s:submit cssClass="btn btn-primary center-block btn-lg"
						id="nextButton" value="%{getText('global.next')}" action="spectroSaveAction" />
				</div>
				<div class="col-sm-4">
					<s:submit cssClass="btn btn-secondary center-block btn-lg"
						id="cancelButton" value="%{getText('global.cancel')}" action="userCancelAction" />
				</div>

			</div>
			<!-- Spectro Confirmation in Progress Modal Window -->
			<div class="modal fade" aria-labelledby="confirmConfigModal"
				aria-hidden="true" id="confirmConfigModal" role="dialog"
				data-backdrop="static" data-keyboard="false">
				<div class="modal-dialog" role="document">
					<div class="modal-content">
						<div class="modal-header">
							<h5 class="modal-title"><s:text name="configureSpectro.confirmColorEyeConfiguration" /></h5>
							<button type="button" class="close" data-dismiss="modal"
								aria-label="%{getText('global.close')}">
								<span aria-hidden="true">&times;</span>
							</button>
						</div>
						<div class="modal-body">
							<p font-size="4" id="confirmmsg"></p>
							<p font-size="4" id="confirmmsg2"><s:text name="configureSpectro.continueWithConfig" /></p>
						</div>
						<div class="modal-footer">
							<button type="button" class="btn btn-primary"
								id="configureOverrideButton"><s:text name="configureSpectro.confirm" /></button>
						</div>
					</div>
				</div>
			</div>
			<!-- Connection Error Modal Window -->
			<div class="modal fade" aria-labelledby="detectErrorModal"
				aria-hidden="true" id="detectErrorModal" role="dialog">
				<div class="modal-dialog" role="document">
					<div class="modal-content">
						<div class="modal-header">
							<h5 class="modal-title" id="detectErrorTitle"><s:text name="configureSpectro.colorEyeError" /></h5>
							<button type="button" class="close" data-dismiss="modal"
								aria-label="%{getText('global.close')}">
								<span aria-hidden="true">&times;</span>
							</button>
						</div>
						<div class="modal-body">
							<p font-size="4" id="detectError"></p>
						</div>
						<div class="modal-footer">
							<s:submit cssClass="btn btn-primary" id="detectErrorTroubleShoot"
								value="%{getText('global.troubleshoot')}" action="swdhTroubleshootAction" />
							<s:submit cssClass="btn btn-success" id="detectErrorInstall"
								value="%{getText('global.install')}" action="swdhInstallAction" />
							<button type="button" class="btn btn-secondary"
								id="configErrorButton" data-dismiss="modal" aria-label="%{getText('global.close')}"><s:text name="global.close" /></button>
							<!-- 									<button type="button" class="btn btn-primary" id="detectErrorTroubleShoot" data-dismiss="modal" aria-label="Close" >Troubleshoot</button> -->
							<!-- 									<button type="button" class="btn btn-secondary" id="detectErrorInstall" data-dismiss="modal" aria-label="Close" >Install</button> -->
						</div>
					</div>
				</div>
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