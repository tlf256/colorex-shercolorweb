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
		<script type="text/javascript" charset="utf-8" src="script/customershercolorweb-1.5.2.js"></script>
		<script type="text/javascript" src="script/spectro-1.5.2.js"></script>
		<script type="text/javascript" src="script/WSWrapper.js"></script>
		<script type="text/javascript" src="script/spectroCalibrate-1.0.0.js"></script>
		<script>
		

			
		$(document).ready(function() {	
			console.log("in docready");
			//this loads on startup! 
			ws_coloreye.receiver = RecdSpectroMessage;
			InitializeModelAndSerial("${sessionScope[reqGuid].spectro.model}", "${sessionScope[reqGuid].spectro.serialNbr}");
			var cntr = 1;

			InitializeCalibrationScreen();

			console.log("docready, between check and calibrate, isReady is " + ws_coloreye.isReady);
			
			//send the calibrate white message.
			GetCalSteps();
			
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
						<s:hidden name="compareColors" id="compareColors" value="%{compareColors}"/>
						<s:hidden name="measureStandard" id="measureStandard" value="%{measureStandard}"/>
						<s:hidden name="measureSample" id="measureSample" value="%{measureSample}"/>
						<s:hidden name="closestColors" id="closestColors" value="%{closestColors}"/>
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