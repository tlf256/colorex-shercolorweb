<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!doctype html>


<%@ taglib prefix="s" uri="/struts-tags" %>

<html lang="en">
	<head>
		<meta name="viewport" content="width=device-width, initial-scale=1">
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
		
		<title><s:text name="calibrateSpectro.calibrateColorEye" /></title>
			<!-- JQuery -->
		<link rel="stylesheet" href="css/bootstrap.min.css" type="text/css">
		<link rel="stylesheet" href="css/bootstrapxtra.css" type="text/css">
		<link rel="stylesheet" href="js/smoothness/jquery-ui.min.css" type="text/css">
		<link rel="stylesheet" href="css/CustomerSherColorWeb.css" type="text/css"> 
		<link rel="stylesheet" href="font-awesome-4.7.0/css/font-awesome.min.css" type="text/css">
		<script type="text/javascript" charset="utf-8" src="js/jquery-3.4.1.min.js"></script>
		<script type="text/javascript" charset="utf-8"	src="js/jquery-ui.min.js"></script>
		<script type="text/javascript" charset="utf-8"	src="js/bootstrap.min.js"></script>
		<script type="text/javascript" charset="utf-8" src="script/customershercolorweb-1.4.6.js"></script>
		<script type="text/javascript" src="script/spectro.js"></script>
		<script type="text/javascript" src="script/WSWrapper.js"></script>
		<script>
		
		var calibrate_step = "start";		
		var ws_coloreye = new WSWrapper('coloreye');

		function InitializeCalibrationScreen() {
		    console.log("InitializeCalibrationScreen");
			$(".whitecal").hide();
			$(".blackcal").hide();
			$(".greenmeas").hide();
			$('#closeModal').hide();
			$('#spectroCalModal').modal('show');
		}

		function GetCalSteps() {
		  	console.log("GetCalSteps")
		  	calibrate_step = "GetCalSteps";
			var clreyemodel = $('#spectroModel').val();
			var spectromessage = new SpectroMessage('GetCalSteps',clreyemodel);
			    var json = JSON.stringify(spectromessage);
			    ws_coloreye.send(json);
		}
		  
		function CalibrateWhite() {
			$('#calcrcl')
				.removeClass('d-none')
				.css('background-color', 'white');
			console.log("CalibrateWhite")
		  	calibrate_step = "CalibrateWhite";
			var clreyemodel = $('#spectroModel').val();
			var spectromessage = new SpectroMessage('CalibrateWhite',clreyemodel);
		    var json = JSON.stringify(spectromessage);
		    ws_coloreye.send(json);
			$(".pleasewait").hide();
			$(".blackcal").hide();
			$(".greenmeas").hide();
			$(".whitecal").show();
			$('#closeModal').show();
		}

		function CalibrateBlack() {
			$('#calcrcl')
				.removeClass('d-none')
				.css('background-color', 'black');
		  	console.log("CalibrateBlack")
		  	calibrate_step = "CalibrateBlack";
			var clreyemodel = $('#spectroModel').val();
			var spectromessage = new SpectroMessage('CalibrateBlack',clreyemodel);
		    var json = JSON.stringify(spectromessage);
		    ws_coloreye.send(json);
		    $(".pleasewait").hide();
			$(".whitecal").hide();
			$(".greenmeas").hide();
			$(".blackcal").show();
			$('#closeModal').show();
		}

		function MeasureGreen() {
			$('#calcrcl')
				.removeClass('d-none')
				.css('background-color', 'green');
		  	console.log("MeasureGreen")
		  	calibrate_step = "MeasureGreen";
			var clreyemodel = $('#spectroModel').val();
			var spectromessage = new SpectroMessage('MeasureGreen',clreyemodel);
		    var json = JSON.stringify(spectromessage);
		    ws_coloreye.send(json);
		    $(".pleasewait").hide();
			$(".whitecal").hide();
			$(".blackcal").hide();
			$(".greenmeas").show();
			$('#closeModal').show();
		}

		function DisplayError() {
			$('#spectroCalModal').modal('hide');
		  	console.log("DisplayError")
		  	calibrate_step = "DisplayError";
		  	
			$(".calsuccess").addClass('d-none');
			$(".calincomplete").addClass('d-none');
			$(".error").removeClass('d-none');
			$(".done").removeClass('d-none');
		}
			  	  	  	
		function CalibrateSuccess() {
			$('#spectroCalModal').modal('hide');
			console.log("CalibrateSuccess")
		  	calibrate_step = "CalibrateSuccess";
		  	
			$(".calincomplete").addClass('d-none');
			$(".error").addClass('d-none');
			$(".calsuccess").removeClass('d-none');
			$(".done").removeClass('d-none');
		}
		
		function CalibrateIncomplete() {
			console.log("CalibrateIncomplete")
		  	calibrate_step = "CalibrateIncomplete";
		  	
			$(".calsuccess").addClass('d-none');
			$(".error").addClass('d-none');
			$(".calincomplete").removeClass('d-none');
			$(".done").removeClass('d-none');
		}
		  	
		function RecdMessage() {
			console.log("Received Message");
		  	//parse the spectro
		  	console.log("Message is " + ws_coloreye.wsmsg);
		  	console.log("isReady is " + ws_coloreye.isReady + "BTW");
		  	if (ws_coloreye.wserrormsg != "") {
			  	$("#errmsg").text('<s:text name="global.webSocketErrorPlusErr"><s:param>' + ws_coloreye.wserrormsg + '</s:param></s:text>');
		  		DisplayError();
		  		return;
		  	}
		  		
			var return_message=JSON.parse(ws_coloreye.wsmsg);
			switch (return_message.command) {
				case 'GetCalSteps':
					//TO DO: Evaluate the responseMssage and process in appropriate order.
					//       In our case for now, we know it's white then black.  Do that
					//       temporarily until everything is working procedurally.
					CalibrateWhite();
					break;
				case 'CalibrateWhite':
					if (return_message.responseMessage=="true") {
						CalibrateBlack();
					} else {
						$("#errmsg").text(return_message.errorMessage);
		  		  		DisplayError();
					}
					break;
				case 'CalibrateBlack':
					if (return_message.responseMessage=="true") {
						MeasureGreen();
					} else {
						$("#errmsg").text(return_message.errorMessage);
		  		  		DisplayError();
					}
					break;
				case 'MeasureGreen':
					if (return_message.responseMessage=="true") {
						CalibrateSuccess();						
					} else {
						$("#errmsg").text(return_message.errorMessage);
		  		  		DisplayError();
					}
					break;
				default:
					//Not an response we expected...
					$("#errmsg").text('<s:text name="global.unexpectedCallToErr"><s:param>' + return_message.command + '</s:param></s:text>');
			  		DisplayError();
			}
		  	
		}
			
		$(document).ready(function() {	
			console.log("in docready");
			//this loads on startup! 
			
			var cntr = 1;

			InitializeCalibrationScreen();

			console.log("docready, between check and calibrate, isReady is " + ws_coloreye.isReady);
			
			//send the calibrate white message.
			GetCalSteps();
			
			//CalibrateSuccess(); //test calibrateSuccess
			
			//TODO set a timer
			
		});
		
		</script>
	</head>
	
	<body>
		<!-- including Header -->
		<s:include value="Header.jsp"></s:include>
		<s:set var="thisGuid" value="reqGuid" />
		<s:form action="colorUserNextAction" validate="true" theme="bootstrap">
			<div class="container-fluid">
				<div class="row">
					<div class="col-sm-3">
						<%-- 			this guid is <s:property value="thisGuid"/> --%>
						<%-- 			this guid is <s:property value="%{reqGuid}"/> --%>
						<!-- 			<br> -->
						<%-- 			this sess is <s:property value="#session"/> --%>
						<!-- 			<br> -->
						<%-- 			jf obj is <s:property value="#session[reqGuid].jobFieldList"/> --%>
					</div>
					<div class="col-sm-6"></div>
					<div class="col-sm-3">
						<s:hidden name="spectroModel" id="spectroModel" value="%{#session[reqGuid].spectroModel}"/>
						<s:hidden name="reqGuid" id="reqGuid" value="%{reqGuid}"/>
						<s:hidden name="compare" id="compareColors" value="%{compare}"/>
						<s:hidden name="measure" id="measureSample" value="%{measure}"/>
						<%-- <s:hidden name="measureColor" id="measureColor" value="%{measureColor}" /> --%>
					</div>
				</div>
				<br>
				<br>
				<div class="row">
					<div class="col-sm-3"></div>
					<div class="col-sm-6">
						<h2 class="error d-none"><s:text name="calibrateSpectro.calibrationError" /></h2>
		 	            <h2 class="calsuccess d-none"><s:text name="calibrateSpectro.successfulCalibration" /></h2>
		 	            <h2 class="calincomplete d-none"><s:text name="calibrateSpectro.cancelCalibration" /></h2>
					</div>
					<div class="col-sm-3"></div>
				</div>
				<div class="row">
					<div class="col-sm-3"></div>
					<div class="col-sm-6">
						<h2 class="error"></h2>
					</div>
					<div class="col-sm-3"></div>
				</div>
				<div class="row">
					<div class="col-sm-3"></div>
					<div class="col-sm-6">
						<h2 class="error" id="errmsg"></h2>
					</div>
					<div class="col-sm-3"></div>
				</div>
				<br>
				<br>
				<div class="row">
					<div class="col-sm-3"></div>
					<div class="col-sm-4">
						<div class="calincomplete d-none">
							<s:submit class="btn btn-primary" id="calibrate" value="%{getText('global.calibrate')}" action="spectroCalibrateAction"></s:submit>
						</div>
						<div class="calsuccess d-none">
						<s:if test="measureColor">
							<s:submit cssClass="btn btn-primary" value="%{getText('global.next')}" action="measureColorReturnAction"/>
						</s:if>
						<s:else>
							<s:submit cssClass="btn btn-primary" value="%{getText('global.next')}" action="goodCalibrateAction"/>
						</s:else>
						</div>
					</div>
					<div class="col-sm-5">
						<div class="done d-none">
							<s:submit cssClass="btn btn-secondary" value="%{getText('global.cancel')}" action="userCancelAction"/>
						</div>
					</div>
				</div>
			</div>
		</s:form>
		<div class="modal fade modal-xl" tabindex="-1" role="dialog" id="spectroCalModal" data-backdrop="static">
		  <div class="modal-dialog modal-lg">
		    <div class="modal-content">
		      <div class="modal-header bg-light clearfix">
		        <h2 class="modal-title ml-3"><s:text name="calibrateSpectro.calibrateColorEye" /></h2>
		        <span class="dot d-none" id="calcrcl"></span>
		        <button type="button" class="close" data-dismiss="modal" aria-label="Close" onclick="CalibrateIncomplete();">
		          <span aria-hidden="true">&times;</span>
		        </button>
		      </div>
		      <div class="modal-body">
		        <div class="container-fluid">
					<div class="row">
						<div class="col-sm-1"></div>
					</div>
					<div class="row">
						<div class="col-sm-1"></div>
	            		<div class="col-sm-10">
	            			<h3 class="pleasewait"></h3>
 	            			<h3 class="whitecal"><s:text name="calibrateSpectro.removeWhitePlasticCap" /></h3>
 	            			<h3 class="blackcal"><s:text name="calibrateSpectro.replaceWhitePlasticCap" /></h3>
 	            			<h3 class="greenmeas"><s:text name="calibrateSpectro.flipBase" /></h3>
						</div>
					</div>
					<div class="row">
						<div class="col-sm-2"></div>
	            		<div class="col-sm-10"></div>
					</div>
					<div class="row">
						<div class="col-sm-1"></div>
	            		<div class="col-sm-10">
	            			<h3 class="pleasewait"></h3>
 	            			<h3 class="whitecal"><s:text name="calibrateSpectro.positionWhite" /></h3>
 	            			<h3 class="blackcal"><s:text name="calibrateSpectro.positionBlack" /></h3>
 	            			<h3 class="greenmeas"><s:text name="calibrateSpectro.positionGreen" /></h3>
 	            		</div>
					</div>
					<div class="row">
						<div class="col-sm-1"></div>
	            		<div class="col-sm-10"></div>
					</div>
					<div class="row">
						<div class="col-sm-1"></div>
	            		<div class="col-sm-10">
	            			<h3 class="pleasewait"><s:text name="calibrateSpectro.connectingColorEye" /></h3>
 	            			<h3 class="whitecal"><s:text name="calibrateSpectro.pressFirmly" /></h3>
 	            			<h3 class="blackcal"><s:text name="calibrateSpectro.pressFirmly" /></h3>
 	            			<h3 class="greenmeas"><s:text name="calibrateSpectro.pressFirmly" /></h3>
						</div>
					</div>
					<div class="row">
						<div class="col-sm-1"></div>
	            		<div class="col-sm-10"></div>
					</div>
					<br>
					<div class="row">
						<div class="col-sm-1"></div>
	            		<div class="col-sm-10">
	            			<h5 class="pleasewait"></h5>
 	            			<h5 class="whitecal"><s:text name="calibrateSpectro.fromRedToGreenSuccess" /></h5>
 	            			<h5 class="blackcal"><s:text name="calibrateSpectro.toYellowOnSuccess" /></h5>
 	            			<h5 class="greenmeas"><s:text name="calibrateSpectro.fromRedToGreenSuccess" /></h5>
						</div>
					</div>
				</div>
		      </div>
		      <div class="modal-footer">
			       <button type="button" id="closeModal" class="btn btn-secondary" data-dismiss="modal" onclick="CalibrateIncomplete();"><s:text name="global.close"></s:text></button>
			  </div>
		    </div>
		  </div>
		</div>
		<br>
		<br>
		<br>
		<!-- Including footer -->
		<s:include value="Footer.jsp"></s:include>
	</body>
</html>